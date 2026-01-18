package com.luffa.mototorque.service;

import com.luffa.mototorque.model.MaintenanceSpec;
import com.luffa.mototorque.model.Motorcycle;
import com.luffa.mototorque.repository.MaintenanceSpecRepository;
import com.luffa.mototorque.repository.MotorcycleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MaintenanceLookupService {

    private final MotorcycleRepository motorcycleRepository;
    private final MaintenanceSpecRepository maintenanceSpecRepository;


    public MaintenanceLookupService(MotorcycleRepository motorcycleRepository,
                                    MaintenanceSpecRepository maintenanceSpecRepository){
        this.motorcycleRepository = motorcycleRepository;
        this.maintenanceSpecRepository = maintenanceSpecRepository;
    }

    public List<MaintenanceSpec> findMaintenanceSpec(String brand, String model, int prductionYear){
        Motorcycle motorcycle = motorcycleRepository
                .findByBrandIgnoreCaseAndModelAndProductionYear(brand, model, prductionYear)
                .stream()
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Motocykl nie jest w bazie"));
        return maintenanceSpecRepository.findByMotorcycleOrderByCategoryAscParameterAsc(motorcycle);
    }
}
