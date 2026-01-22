package com.luffa.mototorque.dto;

import com.luffa.mototorque.service.TorqueLookupResult;

public record TorqueResponse(
        String partName,
        int torqueNm,
        String notes,
        String source,
        String threadSize,
        String matchedBy
    ) {

    public TorqueResponse(TorqueLookupResult result){
        this(
                result.spec().getPartName(),
                result.spec().getTorqueNm(),
                result.spec().getNotes(),
                result.spec().getSource(),
                result.spec().getThreadSize(),
                result.matchedBy().name()
        );
    }
}
