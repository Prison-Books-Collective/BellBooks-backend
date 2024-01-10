package com.cocosmaj.BellBooks.repository.shipment;

import com.cocosmaj.BellBooks.model.shipment.PackageContent;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PackageContentRepository<T extends PackageContent> extends CrudRepository<T, Long> {
}
