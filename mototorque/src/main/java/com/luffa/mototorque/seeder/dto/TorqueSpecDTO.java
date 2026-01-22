package com.luffa.mototorque.seeder.dto;

import java.util.List;

public record TorqueSpecDTO(
        String part,
        Integer torqueNm,
        String note,
        String source,
        String fastener,
        String tool,
        String treatment,
        String location,
        Integer manualPage,
        List<String> aliases
) { }
