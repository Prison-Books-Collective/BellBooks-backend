package com.cocosmaj.BellBooks.service.shipment;

import com.cocosmaj.BellBooks.controller.repository.CreatorRepository;
import org.springframework.stereotype.Service;

@Service
public class CreatorService {

    CreatorRepository creatorRepository;

    public CreatorService(CreatorRepository creatorRepository){
        this.creatorRepository = creatorRepository;
    }

    public void deleteCreator(Long id){
        this.creatorRepository.deleteById(id);
    }

}
