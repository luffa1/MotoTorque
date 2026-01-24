package com.luffa.mototorque.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TorqueEntry {
    private String partName;
    private Integer toruqeNm;
    private String notes;
    private String source;
    private String festern;
    private String tool;
    private String treatment;
    private String location;
    private Integer manualPage;
    private List<String> aliases;
}
