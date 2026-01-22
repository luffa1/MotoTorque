package com.luffa.mototorque.seeder.dto;

public record MaintenanceSpecDTO(
        String section,
        String name,
        String value,
        String unit,
        String note,
        String extra
) { }
