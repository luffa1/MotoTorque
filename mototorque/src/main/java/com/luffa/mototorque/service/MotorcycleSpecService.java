package com.luffa.mototorque.service;


import com.luffa.mototorque.model.MotorcycleSpec;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MotorcycleSpecService {

    private final MotorcycleSpecLoader loader;

    public List<MotorcycleSpec> getAll(){
        return loader.getAll().stream().toList();
    }

    public MotorcycleSpec getById(String id){
        MotorcycleSpec spec = loader.getById(id);

        if (spec == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Motorcycle " + id + " not found");
        }
        return spec;
    }
}
