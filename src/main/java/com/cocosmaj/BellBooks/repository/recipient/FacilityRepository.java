package com.cocosmaj.BellBooks.repository.recipient;

import com.cocosmaj.BellBooks.model.enums.State;
import com.cocosmaj.BellBooks.model.recipient.Facility;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@SuppressWarnings("unused")
@Repository
public interface FacilityRepository extends CrudRepository<Facility, Long> {

    List<Facility> findAllByOrderByName();

    List<Facility> findAllByNameAndState(String name, State state);

}
