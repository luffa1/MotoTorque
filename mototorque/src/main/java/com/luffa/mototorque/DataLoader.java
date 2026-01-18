package com.luffa.mototorque;

import com.luffa.mototorque.model.MaintenanceSpec;
import com.luffa.mototorque.model.Motorcycle;
import com.luffa.mototorque.model.PartAlias;
import com.luffa.mototorque.model.TorqueSpec;
import com.luffa.mototorque.repository.MaintenanceSpecRepository;
import com.luffa.mototorque.repository.MotorcycleRepository;
import com.luffa.mototorque.repository.PartAliasRepository;
import com.luffa.mototorque.repository.TorqueSpecRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    private final MotorcycleRepository motorcycleRepository;
    private final TorqueSpecRepository torqueSpecRepository;
    private final PartAliasRepository partAliasRepository;
    private final MaintenanceSpecRepository maintenanceSpecRepository;

    public DataLoader(MotorcycleRepository motorcycleRepository,
                      TorqueSpecRepository torqueSpecRepository,
                      PartAliasRepository partAliasRepository,
                      MaintenanceSpecRepository maintenanceSpecRepository) {
        this.motorcycleRepository = motorcycleRepository;
        this.torqueSpecRepository = torqueSpecRepository;
        this.partAliasRepository = partAliasRepository;
        this.maintenanceSpecRepository = maintenanceSpecRepository;
    }

    @Override
    public void run(String... args) {
        loadF800R2011();
        loadF900GS2025();
    }

    private void loadF800R2011() {
        Motorcycle f800r2011 = motorcycleRepository.save(new Motorcycle("BMW", "F800R", 2011));

        torqueSpecRepository.save(new TorqueSpec(
                f800r2011,
                "SIDE STAND MOUNTING BOLT",
                38,
                "Use Loctite 243",
                "BMW F800R Manual (section: Frame)",
                null, null, null,
                "M10 × 1.25",
                "Torx T45",
                "Loctite 243",
                "Chassis / Side stand",
                234
        ));
        partAliasRepository.save(new PartAlias("stopka", "SIDE STAND MOUNTING BOLT"));
        partAliasRepository.save(new PartAlias("boczna stopka", "SIDE STAND MOUNTING BOLT"));
        partAliasRepository.save(new PartAlias("nóżka", "SIDE STAND MOUNTING BOLT"));
        partAliasRepository.save(new PartAlias("side stand bolt", "SIDE STAND MOUNTING BOLT"));

        torqueSpecRepository.save(new TorqueSpec(
                f800r2011,
                "FRONT BRAKE CALIPER TO FORK",
                38,
                "Tighten both calipers evenly",
                "BMW F800R Manual (section: Front wheel)",
                null, null, null,
                "M10 × 65",
                "Torx T50",
                null,
                "Front wheel / Brake caliper",
                312
        ));
        partAliasRepository.save(new PartAlias("zacisk przedni", "FRONT BRAKE CALIPER TO FORK"));
        partAliasRepository.save(new PartAlias("zacisk hamulca przód", "FRONT BRAKE CALIPER TO FORK"));
        partAliasRepository.save(new PartAlias("front brake caliper", "FRONT BRAKE CALIPER TO FORK"));

        torqueSpecRepository.save(new TorqueSpec(
                f800r2011,
                "FRONT AXLE PINCH BOLT",
                19,
                "Tighten 6× alternately",
                "BMW F800R Manual (section: Front wheel)",
                null, null, null,
                "M8 × 35",
                "Torx T40",
                null,
                "Front wheel / Axle clamp",
                313
        ));
        partAliasRepository.save(new PartAlias("śruba dociskowa osi", "FRONT AXLE PINCH BOLT"));
        partAliasRepository.save(new PartAlias("zacisk osi", "FRONT AXLE PINCH BOLT"));

        torqueSpecRepository.save(new TorqueSpec(
                f800r2011,
                "FRONT AXLE",
                50,
                "",
                "BMW F800R Manual (section: Front wheel)",
                null, null, null,
                "M20 × 1.5",
                "Hex 17 mm",
                null,
                "Front wheel / Axle",
                313
        ));
        partAliasRepository.save(new PartAlias("oś przednia", "FRONT AXLE"));
        partAliasRepository.save(new PartAlias("śruba osi przedniej", "FRONT AXLE"));

        torqueSpecRepository.save(new TorqueSpec(
                f800r2011,
                "CHAIN ADJUSTER LOCKNUT",
                19,
                "",
                "BMW F800R Manual (section: Rear wheel)",
                null, null, null,
                "M8",
                "Hex 13 mm",
                null,
                "Rear wheel / Chain adjuster",
                318
        ));
        partAliasRepository.save(new PartAlias("kontrnakrętka naciągu", "CHAIN ADJUSTER LOCKNUT"));
        partAliasRepository.save(new PartAlias("naciąg łańcucha", "CHAIN ADJUSTER LOCKNUT"));

        torqueSpecRepository.save(new TorqueSpec(
                f800r2011,
                "REAR AXLE",
                100,
                "",
                "BMW F800R Manual (section: Rear wheel)",
                null, null, null,
                "M16 × 1.5",
                "Hex 27 mm",
                null,
                "Rear wheel / Axle",
                318
        ));
        partAliasRepository.save(new PartAlias("oś tylna", "REAR AXLE"));
        partAliasRepository.save(new PartAlias("śruba osi tylnej", "REAR AXLE"));

        torqueSpecRepository.save(new TorqueSpec(
                f800r2011,
                "MIRROR LOCKNUT",
                20,
                "Use multi-wax spray",
                "BMW F800R Manual (section: Mirrors)",
                null, null, null,
                "M14 × 1",
                "Wrench 17 mm",
                null,
                "Controls / Mirrors",
                330
        ));
        partAliasRepository.save(new PartAlias("kontrnakrętka lusterka", "MIRROR LOCKNUT"));
        partAliasRepository.save(new PartAlias("mocowanie lusterka", "MIRROR LOCKNUT"));

        torqueSpecRepository.save(new TorqueSpec(
                f800r2011,
                "HEADLIGHT BRACKET BOLT",
                5,
                "",
                "BMW F800R Manual (section: Lighting)",
                null, null, null,
                "M6 × 20",
                "Torx T30",
                null,
                "Lighting / Headlight",
                340
        ));
        partAliasRepository.save(new PartAlias("śruba reflektora", "HEADLIGHT BRACKET BOLT"));
        partAliasRepository.save(new PartAlias("mocowanie lampy", "HEADLIGHT BRACKET BOLT"));

        maintenanceSpecRepository.save(new MaintenanceSpec(
                f800r2011, "CHAIN", "Chain slack", "30–40", "mm",
                "Bike on side stand, no load, measure at tightest point", null
        ));
        maintenanceSpecRepository.save(new MaintenanceSpec(
                f800r2011, "CHAIN", "Chain wear limit", "144.30", "mm",
                "Measure across 9 pins", "Maximum value"
        ));
        maintenanceSpecRepository.save(new MaintenanceSpec(
                f800r2011, "TIRES", "Front tire pressure (solo)", "2.5", "bar",
                "Cold tyres", null
        ));
        maintenanceSpecRepository.save(new MaintenanceSpec(
                f800r2011, "TIRES", "Rear tire pressure (solo / passenger)", "2.9", "bar",
                "Cold tyres", null
        ));
        maintenanceSpecRepository.save(new MaintenanceSpec(
                f800r2011, "TIRES", "Minimum tread depth", "1.6", "mm", null, null
        ));
        maintenanceSpecRepository.save(new MaintenanceSpec(
                f800r2011, "LIGHTING", "Headlight – low / high beam", "H7 12V 55W", null, null, null
        ));
        maintenanceSpecRepository.save(new MaintenanceSpec(
                f800r2011, "LIGHTING", "Headlight – position", "W5W 12V 5W", null, null, null
        ));
        maintenanceSpecRepository.save(new MaintenanceSpec(
                f800r2011, "LIGHTING", "Turn signals front / rear", "R10W 12V 10W", null, null, null
        ));
        maintenanceSpecRepository.save(new MaintenanceSpec(
                f800r2011, "LIGHTING", "Tail/stop lamp", "P21/5W 12V", null, null, null
        ));
        maintenanceSpecRepository.save(new MaintenanceSpec(
                f800r2011, "LIGHTING", "License-plate light", "W5W 12V 5W", null, null, null
        ));
        maintenanceSpecRepository.save(new MaintenanceSpec(
                f800r2011, "ELECTRICAL", "Battery type", "12 V AGM (maintenance free)", null,
                "Connect + first, then –", null
        ));
        maintenanceSpecRepository.save(new MaintenanceSpec(
                f800r2011, "ELECTRICAL", "Fuses", "5 / 7.5 / 10 / 15 / 20", "A", null, null
        ));
        maintenanceSpecRepository.save(new MaintenanceSpec(
                f800r2011, "ENGINE_OIL", "Specification", "API SL, JASO MA2", null, null, null
        ));
        maintenanceSpecRepository.save(new MaintenanceSpec(
                f800r2011, "ENGINE_OIL", "Viscosity (recommended)", "SAE 15W-50", null, null, null
        ));
        maintenanceSpecRepository.save(new MaintenanceSpec(
                f800r2011, "ENGINE_OIL", "Viscosity (alternative)", "SAE 10W-40", null, null, null
        ));
        maintenanceSpecRepository.save(new MaintenanceSpec(
                f800r2011, "COOLANT", "Type", "Ethylene-glycol based, no nitrites/amines/phosphates", null,
                "Level between MIN–MAX", null
        ));
        maintenanceSpecRepository.save(new MaintenanceSpec(
                f800r2011, "BRAKE_FLUID", "Fluid", "DOT 4", null,
                "Level between MIN–MAX", null
        ));
        maintenanceSpecRepository.save(new MaintenanceSpec(
                f800r2011, "ADJUSTMENTS", "Throttle free play", "2–4", "mm", null, null
        ));
        maintenanceSpecRepository.save(new MaintenanceSpec(
                f800r2011, "ADJUSTMENTS", "Clutch lever free play", "5–7", "mm", null, null
        ));
        maintenanceSpecRepository.save(new MaintenanceSpec(
                f800r2011, "SUSPENSION", "Rear preload adjustment", "-", null,
                "Positions only (no torque)", null
        ));
    }

    private void loadF900GS2025() {
        Motorcycle f900gs2025 = motorcycleRepository.save(new Motorcycle("BMW", "F900GS ADV", 2025));

        torqueSpecRepository.save(new TorqueSpec(
                f900gs2025,
                "FRONT WHEEL SPEED SENSOR BOLT",
                8,
                "Replace screw (micro-encapsulated)",
                "BMW F900GS ADV Manual",
                null, null, null,
                "M6 × 16",
                "Torx T30",
                null,
                "Front wheel / Sensor",
                210
        ));
        partAliasRepository.save(new PartAlias("czujnik ABS przód", "FRONT WHEEL SPEED SENSOR BOLT"));
        partAliasRepository.save(new PartAlias("sensor koła przedniego", "FRONT WHEEL SPEED SENSOR BOLT"));

        torqueSpecRepository.save(new TorqueSpec(
                f900gs2025,
                "FRONT WHEEL COVER BOLT",
                3,
                "Replace screw (micro-encapsulated)",
                "BMW F900GS ADV Manual",
                null, null, null,
                "M6 × 16",
                "Torx T25",
                null,
                "Front wheel / Cover",
                211
        ));
        partAliasRepository.save(new PartAlias("osłona przedniego koła", "FRONT WHEEL COVER BOLT"));
        partAliasRepository.save(new PartAlias("błotnik przedni", "FRONT WHEEL COVER BOLT"));

        // Ujednolicone nazwy:
        torqueSpecRepository.save(new TorqueSpec(
                f900gs2025,
                "FRONT BRAKE CALIPER TO FORK",
                38,
                "",
                "BMW F900GS ADV Manual",
                null, null, null,
                "M10 × 45",
                "Torx T50",
                null,
                "Front wheel / Brake caliper",
                212
        ));
        partAliasRepository.save(new PartAlias("zacisk hamulca przód", "FRONT BRAKE CALIPER TO FORK"));
        partAliasRepository.save(new PartAlias("zacisk przedni", "FRONT BRAKE CALIPER TO FORK"));

        torqueSpecRepository.save(new TorqueSpec(
                f900gs2025,
                "FRONT AXLE PINCH BOLT",
                19,
                "Tighten six times alternately",
                "BMW F900GS ADV Manual",
                null, null, null,
                "M8 × 35",
                "Torx T40",
                null,
                "Front wheel / Axle clamp",
                212
        ));
        partAliasRepository.save(new PartAlias("zacisk szybkozłączki", "FRONT AXLE PINCH BOLT"));
        partAliasRepository.save(new PartAlias("szybkozłączka przód", "FRONT AXLE PINCH BOLT"));

        torqueSpecRepository.save(new TorqueSpec(
                f900gs2025,
                "FRONT AXLE",
                50,
                "",
                "BMW F900GS ADV Manual",
                null, null, null,
                "M20 × 1.5",
                "Hex 17 mm",
                null,
                "Front wheel / Axle",
                213
        ));
        partAliasRepository.save(new PartAlias("oś przednia", "FRONT AXLE"));
        partAliasRepository.save(new PartAlias("śruba osi przedniej", "FRONT AXLE"));

        torqueSpecRepository.save(new TorqueSpec(
                f900gs2025,
                "CHAIN ADJUSTER LOCKNUT",
                19,
                "",
                "BMW F900GS ADV Manual",
                null, null, null,
                "M8",
                "Hex 13 mm",
                null,
                "Rear wheel / Chain adjuster",
                318
        ));
        partAliasRepository.save(new PartAlias("kontrnakrętka naciągu", "CHAIN ADJUSTER LOCKNUT"));
        partAliasRepository.save(new PartAlias("naciąg łańcucha", "CHAIN ADJUSTER LOCKNUT"));

        torqueSpecRepository.save(new TorqueSpec(
                f900gs2025,
                "REAR AXLE",
                125,
                "",
                "BMW F900GS ADV Manual",
                null, null, null,
                "M24 × 1.5",
                "Hex 32 mm",
                null,
                "Rear wheel / Axle",
                221
        ));
        partAliasRepository.save(new PartAlias("oś szybkozłączka tył", "REAR AXLE"));
        partAliasRepository.save(new PartAlias("rear quick-release axle", "REAR AXLE"));

        torqueSpecRepository.save(new TorqueSpec(
                f900gs2025,
                "MIRROR LOCKNUT",
                22,
                "LH thread",
                "BMW F900GS ADV Manual",
                null, null, null,
                "M10 × 1.25 (LH)",
                "Wrench 17 mm",
                null,
                "Controls / Mirrors",
                250
        ));
        partAliasRepository.save(new PartAlias("kontrnakrętka lusterka", "MIRROR LOCKNUT"));

        torqueSpecRepository.save(new TorqueSpec(
                f900gs2025,
                "MIRROR ADAPTER BOLT",
                25,
                "Class 4.8 bolt",
                "BMW F900GS ADV Manual",
                null, null, null,
                "M10 × 14",
                "Wrench 17 mm",
                null,
                "Controls / Mirrors",
                250
        ));
        partAliasRepository.save(new PartAlias("adapter lusterka", "MIRROR ADAPTER BOLT"));
        partAliasRepository.save(new PartAlias("mocowanie lusterek", "MIRROR ADAPTER BOLT"));

        // Dane eksploatacyjne
        maintenanceSpecRepository.save(new MaintenanceSpec(
                f900gs2025, "TIRES", "Front tire pressure (solo)", "2.2", "bar",
                "Cold tyres", null
        ));
        maintenanceSpecRepository.save(new MaintenanceSpec(
                f900gs2025, "TIRES", "Front tire pressure (with load)", "2.5", "bar",
                "Cold tyres", null
        ));
        maintenanceSpecRepository.save(new MaintenanceSpec(
                f900gs2025, "TIRES", "Rear tire pressure (solo)", "2.5", "bar",
                "Cold tyres", null
        ));
        maintenanceSpecRepository.save(new MaintenanceSpec(
                f900gs2025, "TIRES", "Rear tire pressure (with load)", "2.9", "bar",
                "Cold tyres", null
        ));
        maintenanceSpecRepository.save(new MaintenanceSpec(
                f900gs2025, "CHAIN", "Chain slack", "30–40", "mm",
                "Bike on side stand, no load, measure at tightest point", null
        ));
        maintenanceSpecRepository.save(new MaintenanceSpec(
                f900gs2025, "CHAIN", "Chain wear limit", "144.30", "mm",
                "Measure across 9 pins", "Maximum value"
        ));
        maintenanceSpecRepository.save(new MaintenanceSpec(
                f900gs2025, "LIGHTING", "Headlight – low / high beam", "H7 12V 55W", null, null, null
        ));
        maintenanceSpecRepository.save(new MaintenanceSpec(
                f900gs2025, "ENGINE_OIL", "Specification", "API SL, JASO MA2", null, null, null
        ));
        maintenanceSpecRepository.save(new MaintenanceSpec(
                f900gs2025, "ENGINE_OIL", "Viscosity (recommended)", "SAE 15W-50", null, null, null
        ));
        maintenanceSpecRepository.save(new MaintenanceSpec(
                f900gs2025, "ENGINE_OIL", "Viscosity (alternative)", "SAE 10W-40", null, null, null
        ));
        maintenanceSpecRepository.save(new MaintenanceSpec(
                f900gs2025, "COOLANT", "Type", "Ethylene-glycol based, no nitrites/amines/phosphates", null,
                "Level between MIN–MAX", null
        ));
        maintenanceSpecRepository.save(new MaintenanceSpec(
                f900gs2025, "BRAKE_FLUID", "Fluid", "DOT 4", null,
                "Level between MIN–MAX", null
        ));
        maintenanceSpecRepository.save(new MaintenanceSpec(
                f900gs2025, "ADJUSTMENTS", "Throttle free play", "2–4", "mm", null, null
        ));
        maintenanceSpecRepository.save(new MaintenanceSpec(
                f900gs2025, "ADJUSTMENTS", "Clutch lever free play", "5–7", "mm", null, null
        ));
    }
}