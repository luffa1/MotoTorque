package com.luffa.mototorque.seeder.dto;

import java.util.List;

public record MotorcycleSeedDTO(
        String brand,
        String model,
        int productionYear,
        List<TorqueSpecDTO> torqueSpecs,
        List<MaintenanceSpecDTO> maintenanceSpecs
) { }
