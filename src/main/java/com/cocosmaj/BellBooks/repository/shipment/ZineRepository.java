package com.cocosmaj.BellBooks.repository.shipment;

import com.cocosmaj.BellBooks.model.shipment.Zine;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@SuppressWarnings("unused")
@Repository
public interface ZineRepository extends PackageContentRepository<Zine> {
    Optional<Zine> findByCode(String code);
}
