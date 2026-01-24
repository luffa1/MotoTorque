package com.luffa.mototorque.controller;

import com.luffa.mototorque.model.MotorcycleSpec;
import com.luffa.mototorque.service.MotorcycleSpecService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/motorcycles/specs")
@RequiredArgsConstructor
@CrossOrigin
public class MotorcycleSpecController {

    private final MotorcycleSpecService service;

    @GetMapping
    public List<MotorcycleSpec> getAll(){
        return service.getAll();
    }

    @GetMapping("/{id}")
    public MotorcycleSpec getById(@PathVariable String id){
        return service.getById(id);
    }
}
