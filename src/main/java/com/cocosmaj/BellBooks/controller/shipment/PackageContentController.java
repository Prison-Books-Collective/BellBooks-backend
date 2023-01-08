package com.cocosmaj.BellBooks.controller.shipment;

import com.cocosmaj.BellBooks.exception.PackageContentNotFoundException;
import com.cocosmaj.BellBooks.model.shipment.Book;
import com.cocosmaj.BellBooks.model.shipment.PackageContent;
import com.cocosmaj.BellBooks.model.shipment.Zine;
import com.cocosmaj.BellBooks.service.shipment.PackageContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
public class PackageContentController {


    private PackageContentService packageContentService;

    public PackageContentController( PackageContentService packageContentService){
        this.packageContentService = packageContentService;
    }

    //get content by ID
    @GetMapping("/getContent")
    public ResponseEntity getContent(@RequestParam Long id){
        try {
            return ResponseEntity.ok(packageContentService.getContent(id));
        } catch (PackageContentNotFoundException exception){
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }


    @GetMapping("/getBookByISBN")
    public ResponseEntity getBookByISBN(@RequestParam String isbn) {
        if (isbn.length() == 10) {
            return getBookByIsbn10(isbn);
        } else if (isbn.length() == 13) {
            return getBookByIsbn13(isbn);
        } else {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    public void queryGoogle(String isbn) {
        packageContentService.queryGoogle(isbn);
    }

    public ResponseEntity getBookByIsbn10(String isbn10){
        Optional<Book> optional = packageContentService.getBookByIsbn10(isbn10);
        if (optional.isPresent()){
            return ResponseEntity.ok(optional.get());
        } else {
            queryGoogle(isbn10);
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity getBookByIsbn13(String isbn13){
        Optional<Book> optional = packageContentService.getBookByIsbn13(isbn13);
        if (optional.isPresent()){

            return ResponseEntity.ok(optional.get());
        } else {
            queryGoogle(isbn13);
            return new ResponseEntity(HttpStatus.BAD_REQUEST);

        }
    }

    @GetMapping("/getContentByTitle")
    public ResponseEntity getContentByTitle(@RequestParam String title){
        return ResponseEntity.ok(packageContentService.getContentByTitle(title));
    }

    @GetMapping("/getZineByCode")
    public ResponseEntity getZineByCode(@RequestParam String code){
        Optional<Zine> optional = packageContentService.getZineByCode(code);
        if (optional.isPresent()){
            return ResponseEntity.ok(optional.get());
        } else{
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/getAllZines")
    public ResponseEntity getAllZines(){
        return ResponseEntity.ok(packageContentService.getAllZines());
    }
    @GetMapping("/getNoIsbnBooks")
    public ResponseEntity getBooksWithNoIsbn(){
        return ResponseEntity.ok(packageContentService.getBooksWithNoIsbn());
    }
    @GetMapping("/getAllContent")
    public ResponseEntity getAllContent(){
        return ResponseEntity.ok(packageContentService.getAllContent());
    }

    //get content by type?

    @PutMapping("/updateContent")
    public ResponseEntity updateContent(@RequestBody PackageContent packageContent){
        try {
            return ResponseEntity.ok(packageContentService.updateContent(packageContent));
        } catch (PackageContentNotFoundException exception){
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/deleteContent")
    public ResponseEntity deleteContent(@RequestParam Long id){
        try {
            packageContentService.deleteContent(id);
            return new ResponseEntity(HttpStatus.OK);
        } catch (PackageContentNotFoundException exception){
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/addContent")
    public ResponseEntity addContent(@RequestBody PackageContent packageContent){

        return ResponseEntity.ok(this.packageContentService.addContent(packageContent));
    }

}
