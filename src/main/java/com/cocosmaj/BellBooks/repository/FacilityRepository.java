package com.cocosmaj.BellBooks.repository;

import com.cocosmaj.BellBooks.model.Facility;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FacilityRepository extends CrudRepository<Facility, Long> {
}
