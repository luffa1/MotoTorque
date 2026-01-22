package com.luffa.mototorque.dto;


import java.util.List;

public record TorqueApiResponse(
        List<TorqueResponse> specs,
        boolean found,
        String fallbackSearchUrl
) { }
