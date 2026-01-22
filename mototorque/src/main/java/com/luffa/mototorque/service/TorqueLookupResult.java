package com.luffa.mototorque.service;

import com.luffa.mototorque.model.TorqueSpec;

public record TorqueLookupResult(TorqueSpec spec, MatchType matchedBy) {
}
