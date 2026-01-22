package com.luffa.mototorque.dto;

import com.luffa.mototorque.service.TorqueLookupResult;

import java.util.List;

public record TorqueLookupResponse(
        List<TorqueLookupResult> specs,
        boolean found,
        String fallbackSearchUrl
) { }
