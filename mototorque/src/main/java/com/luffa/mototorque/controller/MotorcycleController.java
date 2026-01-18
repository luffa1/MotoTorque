package com.luffa.mototorque.controller;

import com.luffa.mototorque.model.Motorcycle;
import com.luffa.mototorque.repository.MotorcycleRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/motorcycles")
public class MotorcycleController {

    private final MotorcycleRepository repository;

    public MotorcycleController(MotorcycleRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<Motorcycle> getMotorcycles (
        @RequestParam(required = false) String brand,
        @RequestParam(required = false) String model,
        @RequestParam(required = false) Integer productionYear

    ) {
        if (brand != null && model != null) {
            return repository.findByBrandIgnoreCaseAndModelAndProductionYear(brand, model, productionYear);
        }
        if (brand != null) {
            return repository.findByBrandIgnoreCase(brand);
        }
        return repository.findAll();
    }

    @GetMapping("/brands")
    public List<String> autoCompleteBrands(@RequestParam String query){
        return repository.searchBrands(query);
    }

    @GetMapping("/models")
    public List<String> autoCompleteModels(
            @RequestParam String brand,
            @RequestParam String query
    ){
        return repository.searchModels(brand, query);
    }

    @GetMapping("/years")
    public List<Integer> getProductionYears(
            @RequestParam String brand,
            @RequestParam String model
    ){
        return repository.findProductionYearByBrandAndModel(brand, model);
    }
}
