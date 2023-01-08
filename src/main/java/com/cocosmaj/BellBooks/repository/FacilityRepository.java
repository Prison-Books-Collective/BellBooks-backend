package com.cocosmaj.BellBooks.repository;

import com.cocosmaj.BellBooks.model.recipient.Facility;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FacilityRepository extends CrudRepository<Facility, Long> {

    List<Facility> findAllByOrderByName();

}
