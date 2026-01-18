package com.luffa.mototorque.dto;

import com.luffa.mototorque.model.MaintenanceSpec;

public record MaintenanceResponse(
        String category,
        String parameter,
        String value,
        String unit,
        String conditions,
        String notes
) {
    public static MaintenanceResponse fromEntity(MaintenanceSpec spec) {
        return new MaintenanceResponse(
                spec.getCategory(),
                spec.getParameter(),
                spec.getValue(),
                spec.getUnit(),
                spec.getConditions(),
                spec.getNotes()
        );
    }
}