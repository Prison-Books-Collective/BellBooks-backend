package com.cocosmaj.BellBooks.service.shipment;

import com.cocosmaj.BellBooks.exception.PackageContentNotFoundException;
import com.cocosmaj.BellBooks.model.shipment.*;
import com.cocosmaj.BellBooks.repository.CreatorRepository;
import com.cocosmaj.BellBooks.repository.PackageContentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class PackageContentService {

    @Autowired
    private PackageContentRepository packageContentRepository;

    @Autowired
    private CreatorRepository creatorRepository;

    public PackageContentService(@Autowired PackageContentRepository packageContentRepository, @Autowired CreatorRepository creatorRepository){
        this.packageContentRepository = packageContentRepository;
        this.creatorRepository = creatorRepository;
    }

    public PackageContent addContent(PackageContent packageContent) {
        if (packageContent.getClass()== Book.class){
            Book book = (Book) packageContent;
            Set<Creator> creators = book.getCreators();
            if (creators != null && creators.size() > 0){
                for (Creator creator : creators){
                    if (creator.getClass() == Author.class){
                        setAuthorId((Author) creator);
                    } else {
                        setGroupId((Group) creator);
                    }
                }
            }
        }
        return this.packageContentRepository.save(packageContent);
    }

    private void setGroupId(Group creator) {
        Group group = creator;
        Optional<Group> optionalGroup = creatorRepository.findByName(group.getName());
        if (!optionalGroup.isPresent()){
            Group savedGroup = creatorRepository.save(group);
            group.setId(savedGroup.getId());
        } else {
            group.setId(optionalGroup.get().getId());
        }
    }

    private void setAuthorId(Author creator) {
        Author author = creator;
        Optional<Author> optionalAuthor = creatorRepository.findByFirstNameAndLastName(author.getFirstName(), author.getLastName());
        if (!optionalAuthor.isPresent()){
            Author savedAuthor = creatorRepository.save(author);
            author.setId(savedAuthor.getId());
        } else {
            author.setId(optionalAuthor.get().getId());
        }
    }

    public PackageContent getContent(Long id) throws PackageContentNotFoundException {
        Optional<PackageContent> byId = packageContentRepository.findById(id);
        if (byId.isPresent()){
            return byId.get();
        } else {
            throw new PackageContentNotFoundException();
        }

    }

    public PackageContent updateContent(PackageContent packageContent) throws PackageContentNotFoundException {
        getContent(packageContent.getId());
        return packageContentRepository.save(packageContent);
    }

    public void deleteContent(Long id) throws PackageContentNotFoundException {
        getContent(id);
        packageContentRepository.deleteById(id);
    }

    public List<PackageContent> getAllContent() {
        return (List) packageContentRepository.findAll();
    }

    public Optional<Book> getBookByIsbn13(String isbn13) {
        return packageContentRepository.findByISBN13(isbn13);
    }

    public Optional<Book> getBookByIsbn10(String isbn10) {
        return packageContentRepository.findByISBN10(isbn10);
    }

    public List<PackageContent> getContentByTitle(String title) {
        return packageContentRepository.findAllByTitleContaining(title);
    }

    public List<Book> getBooksWithNoIsbn() {
        return packageContentRepository.findByISBN10IsNullAndISBN13IsNull();
    }

    public Optional<Zine> getZineByCode(String code) {
        return packageContentRepository.findByCode(code);
    }
}
