package com.luffa.mototorque.service;

import com.luffa.mototorque.model.Motorcycle;
import com.luffa.mototorque.model.PartAlias;
import com.luffa.mototorque.repository.MotorcycleRepository;
import com.luffa.mototorque.repository.PartAliasRepository;
import com.luffa.mototorque.repository.TorqueSpecRepository;
import com.luffa.mototorque.model.TorqueSpec;
import org.springframework.stereotype.Service;


import java.util.Optional;

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

    public Optional<TorqueSpec> findTorque(String brand, String model, int productionYear, String query) {

        Motorcycle motorcycle = motorcycleRepository
                .findByBrandIgnoreCaseAndModelAndProductionYear(brand, model, productionYear)
                .stream()
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Motocykl nie jest w bazie"));

        String normalizedQuery = query == null ? "" : query.trim();

        String partName = partAliasRepository.findFirstByAliasIgnoreCase(normalizedQuery)
                .map(PartAlias::getPartName)
                .orElse(normalizedQuery);
        return torqueSpecRepository.findByMotorcycleAndPartNameIgnoreCase(motorcycle, partName);
    }
}
