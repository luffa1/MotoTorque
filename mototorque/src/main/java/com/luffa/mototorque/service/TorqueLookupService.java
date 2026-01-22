package com.luffa.mototorque.service;

import com.luffa.mototorque.dto.TorqueLookupResponse;
import com.luffa.mototorque.model.Motorcycle;
import com.luffa.mototorque.repository.MotorcycleRepository;
import com.luffa.mototorque.repository.PartAliasRepository;
import com.luffa.mototorque.repository.TorqueSpecRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;


import java.util.ArrayList;
import java.util.List;


@Service
public class TorqueLookupService {

    private final MotorcycleRepository motorcycleRepository;
    private final TorqueSpecRepository torqueSpecRepository;
    private final PartAliasRepository partAliasRepository;

    public TorqueLookupService(MotorcycleRepository motorcycleRepository,
                               TorqueSpecRepository torqueSpecRepository,
                               PartAliasRepository partAliasRepository) {
        this.motorcycleRepository = motorcycleRepository;
        this.torqueSpecRepository = torqueSpecRepository;
        this.partAliasRepository = partAliasRepository;
    }

    public TorqueLookupResponse findTorque(String brand,
                                           String model,
                                           int productionYear,
                                           String query) {

        Motorcycle motorcycle = motorcycleRepository
                .findByBrandIgnoreCaseAndModelAndProductionYear(brand, model, productionYear)
                .stream()
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Motocykl nie jest w bazie"));

        String normalizedQuery = query == null ? "" : query.trim();
        if (normalizedQuery.isEmpty()) {
            return new TorqueLookupResponse(List.of(), false, null);
        }

        List<TorqueLookupResult> matches = new ArrayList<>();

        partAliasRepository.findFirstByAliasIgnoreCase(normalizedQuery)
                .flatMap(alias -> torqueSpecRepository
                        .findByMotorcycleAndPartNameIgnoreCase(motorcycle, alias.getPartName()))
                .map(spec -> new TorqueLookupResult(spec, MatchType.ALIAS))
                .ifPresent(matches::add);

        if (matches.isEmpty()) {
            torqueSpecRepository
                    .findByMotorcycleAndThreadSizeContainingIgnoreCase(motorcycle, normalizedQuery)
                    .stream()
                    .map(spec -> new TorqueLookupResult(spec, MatchType.THREAD_SIZE))
                    .forEach(matches::add);
        }

        if (!matches.isEmpty()) {
            return new TorqueLookupResponse(matches, true, null);
        }

        String fallbackUrl = buildGoogleLink(brand, model, productionYear, normalizedQuery);
        return new TorqueLookupResponse(List.of(), false, fallbackUrl);
    }

    public List<String> suggestPartNames(String brand,
                                         String model,
                                         int productionYear,
                                         String query) {
        if (query == null || query.trim().length() < 2) {
            return List.of();
        }

        Motorcycle motorcycle = motorcycleRepository
                .findByBrandIgnoreCaseAndModelAndProductionYear(brand, model, productionYear)
                .stream()
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Motocykl nie jest w bazie"));

        return partAliasRepository.findAliasesForMotorcycleStartingWith(
                motorcycle, query.trim(), PageRequest.of(0, 8)
        );
    }

    private String buildGoogleLink(String brand,
                                   String model,
                                   int year,
                                   String query) {
        String searchPhrase = "%s %s %d torque %s".formatted(
                brand != null ? brand : "",
                model != null ? model : "",
                year,
                query != null ? query : ""
        ).trim();

        return "https://www.google.com/search?q=" +
                URLEncoder.encode(searchPhrase, StandardCharsets.UTF_8);
    }
}
