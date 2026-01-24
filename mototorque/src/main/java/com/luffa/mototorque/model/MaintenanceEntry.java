package com.luffa.mototorque.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class MaintenanceEntry {
    private String section;
    private String name;
    private String value;
    private String unit;
    private String note;
    private String extra;
}
