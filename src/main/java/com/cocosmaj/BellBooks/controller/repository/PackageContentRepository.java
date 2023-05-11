package com.cocosmaj.BellBooks.controller.repository;

import com.cocosmaj.BellBooks.model.shipment.Book;
import com.cocosmaj.BellBooks.model.shipment.PackageContent;
import com.cocosmaj.BellBooks.model.shipment.Zine;
import com.sun.xml.bind.v2.runtime.reflect.Lister;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PackageContentRepository<T extends PackageContent> extends CrudRepository<T, Long> {





}
