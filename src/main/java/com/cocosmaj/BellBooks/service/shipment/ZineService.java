package com.cocosmaj.BellBooks.service.shipment;

import com.cocosmaj.BellBooks.model.shipment.Zine;
import com.cocosmaj.BellBooks.repository.ZineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ZineService {

    @Autowired
    private ZineRepository zineRepository;

    public ZineService(@Autowired ZineRepository zineRepository){
        this.zineRepository = zineRepository;
    }


    public Zine addZine(Zine zine) {
        return zineRepository.save(zine);
    }

    public List<Zine> getAllZines() {
        return (List) zineRepository.findAll();
    }
}
