package com.cocosmaj.BellBooks.service.recipient;

import com.cocosmaj.BellBooks.model.recipient.Recipient;
import com.cocosmaj.BellBooks.repository.RecipientRepository;
import com.cocosmaj.BellBooks.exception.RecipientNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;
import org.w3c.dom.html.HTMLButtonElement;
import org.w3c.dom.html.HTMLElement;
import org.w3c.dom.html.HTMLLinkElement;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class RecipientService {

    private RecipientRepository recipientRepository;

    public RecipientService( RecipientRepository recipientRepository){
        this.recipientRepository = recipientRepository;
    }

    public Recipient addRecipient(Recipient recipient) {
        return this.recipientRepository.save(recipient);
    }

    public Recipient updateRecipient(Recipient newRecipientInfo) throws RecipientNotFoundException {
        Recipient databaseRecipient = getRecipient(newRecipientInfo.getId());
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

    public Recipient getRecipient(Long recipientId) throws RecipientNotFoundException {
        Optional<Recipient> byId = recipientRepository.findById(recipientId);
        if (byId.isEmpty()){
            throw new RecipientNotFoundException();
        } else {
            return byId.get();
        }
    }

    public void deleteRecipient(Long id) throws RecipientNotFoundException{
        getRecipient(id);
        recipientRepository.deleteById(id);
    }

    public Recipient getRecipientByAssignedId(String assignedId) throws RecipientNotFoundException {
        Optional<Recipient> byId = recipientRepository.findByAssignedId(assignedId);
        if (byId.isEmpty()){
            throw new RecipientNotFoundException();
        } else {
            return byId.get();
        }
    }

    public String getRecipientLocation(String id) throws IOException, IndexOutOfBoundsException {
        WebClient webClient = new WebClient(BrowserVersion.FIREFOX);
        HtmlPage page = webClient.getPage("https://webapps.doc.state.nc.us/opi/offendersearch.do?method=view");

        HtmlTable table = (HtmlTable) page.getByXPath("//table[contains(@class,'displaytable')]").get(0);

        HtmlTextInput recipientIdField = (HtmlTextInput) table.getByXPath("//input[contains(@name,'searchOffenderId')]").get(0);
        recipientIdField.type(id);
        HtmlPage recipientList = (HtmlPage) recipientIdField.type('\n');
        webClient.waitForBackgroundJavaScript(5000);

        table = (HtmlTable) recipientList.getByXPath("//table[contains(@class,'displaytable')]").get(0);

        List<HtmlAnchor> listOfRecipientIds = table.getByXPath("//a[contains(@class,'tablelink')]");
        for (HtmlAnchor recipientLink : listOfRecipientIds){
            if (recipientLink.getTextContent().equals(id)){
                HtmlPage recipientProfile = recipientLink.click();
                webClient.waitForBackgroundJavaScript(5000);

                HtmlBold locationLabel = (HtmlBold) recipientProfile.getByXPath("//b[contains(text(), 'Current Location:')]").get(0);
                return locationLabel.getParentNode().getNextSibling().getTextContent();
            }
        }
        return "";

    }

    public List<Recipient> getAllRecipients() {
        return (List) recipientRepository.findAll();
    }

    public List<Recipient> getRecipients(String firstName, String lastName) {
        return recipientRepository.findAllByFirstNameContainingAndLastNameContaining(firstName, lastName);
    }
}
