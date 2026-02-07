package com.luffa.mototorque.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TorqueEntry {
    private String part;
    private Integer torqueNm;
    private String note;
    private String source;
    private String festener;
    private String tool;
    private String treatment;
    private String location;
    private Integer manualPage;
    private List<String> aliases;
}
