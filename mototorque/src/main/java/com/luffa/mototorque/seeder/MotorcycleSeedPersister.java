package com.luffa.mototorque.seeder;


import com.luffa.mototorque.model.MaintenanceSpec;
import com.luffa.mototorque.model.Motorcycle;
import com.luffa.mototorque.model.PartAlias;
import com.luffa.mototorque.model.TorqueSpec;
import com.luffa.mototorque.repository.MaintenanceSpecRepository;
import com.luffa.mototorque.repository.MotorcycleRepository;
import com.luffa.mototorque.repository.PartAliasRepository;
import com.luffa.mototorque.repository.TorqueSpecRepository;
import com.luffa.mototorque.seeder.dto.MotorcycleSeedDTO;
import org.springframework.stereotype.Component;

@Component
public class MotorcycleSeedPersister {

    private final MotorcycleRepository motorcycleRepository;
    private final TorqueSpecRepository torqueSpecRepository;
    private final PartAliasRepository partAliasRepository;
    private final MaintenanceSpecRepository maintenanceSpecRepository;

    public MotorcycleSeedPersister(MotorcycleRepository motorcycleRepository,
                                   TorqueSpecRepository torqueSpecRepository,
                                   PartAliasRepository partAliasRepository,
                                   MaintenanceSpecRepository maintenanceSpecRepository) {
        this.motorcycleRepository = motorcycleRepository;
        this.torqueSpecRepository = torqueSpecRepository;
        this.partAliasRepository = partAliasRepository;
        this.maintenanceSpecRepository = maintenanceSpecRepository;
    }

    public void persist(MotorcycleSeedDTO dto){
        Motorcycle motorcycle = motorcycleRepository.save(
                new Motorcycle(dto.brand(), dto.model(), dto.productionYear())
        );

        dto.torqueSpecs().forEach(spec -> {
            TorqueSpec entity = torqueSpecRepository.save(new TorqueSpec(
                    motorcycle,
                    spec.part(),
                    spec.torqueNm(),
                    spec.note(),
                    spec.source(),
                    null,null,null,
                    spec.fastener(),
                    spec.tool(),
                    spec.treatment(),
                    spec.location(),
                    spec.manualPage()
            ));

            if (spec.aliases() != null) {
                spec.aliases().forEach(alias ->
                        partAliasRepository.save(new PartAlias(alias, spec.part()))
                );
            }
        });

        dto.maintenanceSpecs().forEach(ms ->
                maintenanceSpecRepository.save(new MaintenanceSpec(
                        motorcycle,
                        ms.section(),
                        ms.name(),
                        ms.value(),
                        ms.unit(),
                        ms.note(),
                        ms.extra()
                ))
        );
    }
}
