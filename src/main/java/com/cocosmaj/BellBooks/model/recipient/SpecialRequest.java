package com.cocosmaj.BellBooks.model.recipient;

import javax.persistence.*;
import java.util.Date;

@Entity
public class SpecialRequest {

    @Id
    @GeneratedValue
    private Long id;

    private String requesterName;

    private String volunteerName;

    private String request;

    private Date specialRequestDate;

    private Date letterMailedDate;

    @Enumerated(value= EnumType.STRING)
    private SpecialRequestCategory category;

    @Enumerated(value=EnumType.STRING)
    private SpecialRequestStatus status;

    public void setId(Long id) {
        this.id = id;
    }

    @Id
    public Long getId() {
        return id;
    }


    public void setRequesterName(String requesterName){
        this.requesterName = requesterName;
    }

    public String getRequesterName(){
        return requesterName;
    }

    public void setVolunteerName(String volunteerName){
        this.volunteerName = volunteerName;
    }

    public String getVolunteerName(){
        return volunteerName;
    }

    public void setRequest(String request){
        this.request = request;
    }

    public String getRequest(){
        return request;
    }

    public void setSpecialRequestDate(Date specialRequestDate){
        this.specialRequestDate = specialRequestDate;
    }

    public Date getSpecialRequestDate(){
        return specialRequestDate;
    }

    public void setLetterMailedDate(Date letterMailedDate){
        this.letterMailedDate = letterMailedDate;
    }

    public Date getLetterMailedDate(){
        return letterMailedDate;
    }

    public SpecialRequestCategory getCategory() {
        return category;
    }

    public void setCategory(SpecialRequestCategory category) {
        this.category = category;
    }

    public SpecialRequestStatus getStatus() {
        return status;
    }

    public void setStatus(SpecialRequestStatus status) {
        this.status = status;
    }
}
