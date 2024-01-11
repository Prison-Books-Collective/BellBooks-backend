package com.cocosmaj.BellBooks.service.recipient;

import com.cocosmaj.BellBooks.exception.RecipientNotFoundException;
import com.cocosmaj.BellBooks.model.recipient.Recipient;
import com.cocosmaj.BellBooks.repository.recipient.RecipientRepository;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTable;
import com.gargoylesoftware.htmlunit.html.HtmlTableRow;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class RecipientService {

    private final RecipientRepository recipientRepository;

    @SuppressWarnings("unused")
    public RecipientService(RecipientRepository recipientRepository) {
        this.recipientRepository = recipientRepository;
    }

    public Recipient addRecipient(Recipient recipient) {
        return this.recipientRepository.save(recipient);
    }

    public Recipient updateRecipient(Recipient newRecipientInfo) throws RecipientNotFoundException {
        Recipient databaseRecipient = recipientRepository
            .findById(newRecipientInfo.getId())
            .orElseThrow(RecipientNotFoundException::new);

        if (!databaseRecipient.getAssignedId().equals(newRecipientInfo.getAssignedId())) {
            databaseRecipient.setAssignedId(newRecipientInfo.getAssignedId());
        }
        if (!databaseRecipient.getFirstName().equals(newRecipientInfo.getFirstName())) {
            databaseRecipient.setFirstName(newRecipientInfo.getFirstName());
        }
        if (!databaseRecipient.getLastName().equals(newRecipientInfo.getLastName())) {
            databaseRecipient.setLastName(newRecipientInfo.getLastName());
        }
        return recipientRepository.save(databaseRecipient);
    }

    public void deleteRecipient(Long id) throws RecipientNotFoundException {
        getRecipientById(id);
        recipientRepository.deleteById(id);
    }

    public Recipient getRecipientById(Long id) throws RecipientNotFoundException {
        return recipientRepository.findById(id).orElseThrow(RecipientNotFoundException::new);
    }

    public Recipient getRecipientByAssignedId(String assignedId) throws RecipientNotFoundException {
        Optional<Recipient> byId = recipientRepository.findByAssignedId(assignedId);
        if (byId.isEmpty()) {
            throw new RecipientNotFoundException();
        } else {
            return byId.get();
        }
    }

    public String getRecipientLocation(String id) throws IOException, IndexOutOfBoundsException {
        if (id.length() == 7) {
            try {
                HtmlPage page;
                try (WebClient webClient = new WebClient(BrowserVersion.FIREFOX)) {
                    page = webClient.getPage("https://webapps.doc.state.nc.us/opi/viewoffender.do?method=view&offenderID=" + id);
                }
                HtmlTable recipientInfoTable = (HtmlTable) page.getByXPath("//table[contains(@class, 'displaydatatable')]").get(0);

                List<HtmlTableRow> tableRows = recipientInfoTable.getByXPath("//tr");
                for (HtmlTableRow row : tableRows) {
                    if (row.getChildElementCount() != 2) continue;
                    if (row.getFirstElementChild().getChildElementCount() != 1) continue;
                    if (row.getFirstElementChild().getFirstElementChild().getTextContent().contains("Current Location")) {
                        return row.getLastChild().getTextContent();
                    }

                }
            } catch (RuntimeException e) {
                System.err.println("Encountered error while attempting to parse NC State website: " + e.getMessage());
                return "ERROR";
            }
        } else {
            return "Id.length != 7";
        }
        return "";
    }

    public List<Recipient> getAllRecipients() {
        return (List<Recipient>) recipientRepository.findAll();
    }

    public List<Recipient> getRecipients(String firstName, String lastName) {
        return recipientRepository.findAllByFirstNameContainingAndLastNameContaining(firstName, lastName);
    }
}
