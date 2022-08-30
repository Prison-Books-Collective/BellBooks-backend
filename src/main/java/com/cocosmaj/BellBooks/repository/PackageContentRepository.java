package com.cocosmaj.BellBooks.repository;

import com.cocosmaj.BellBooks.model.shipment.PackageContent;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PackageContentRepository extends CrudRepository<PackageContent, Long> {
}
