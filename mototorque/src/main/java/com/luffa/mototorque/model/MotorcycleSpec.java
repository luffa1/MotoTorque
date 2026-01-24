package com.luffa.mototorque.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;


@Data
public class MotorcycleSpec {
    private String id;
    private String brand;
    private String model;
    private int productionYear;

    @JsonProperty("maintenanceSpecs")
    private List<MaintenanceEntry> maintenance;
    @JsonProperty("torqueSpecs")
    private List<TorqueEntry> torque;
}
