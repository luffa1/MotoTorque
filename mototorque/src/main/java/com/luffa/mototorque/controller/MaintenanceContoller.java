package com.luffa.mototorque.controller;

import com.luffa.mototorque.dto.MaintenanceResponse;
import com.luffa.mototorque.service.MaintenanceLookupService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class MaintenanceContoller {

    private final MaintenanceLookupService maintenanceLookupService;

    public MaintenanceContoller(MaintenanceLookupService maintenanceLookupService) {
        this.maintenanceLookupService = maintenanceLookupService;
    }

    @GetMapping("/maintenance")
    public List<MaintenanceResponse> getMaintenance(
            @RequestParam String brand,
            @RequestParam String model,
            @RequestParam int productionYear) {
        return maintenanceLookupService.findMaintenanceSpec(brand, model, productionYear)
                .stream()
                .map(MaintenanceResponse :: fromEntity)
                .toList();
    }
}
