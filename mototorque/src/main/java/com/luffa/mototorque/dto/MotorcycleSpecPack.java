package com.luffa.mototorque.dto;

import com.luffa.mototorque.model.MotorcycleSpec;
import lombok.Data;

import java.util.List;

@Data
public class MotorcycleSpecPack {
    private String version;
    private List<MotorcycleSpec> motorcycles;
}
