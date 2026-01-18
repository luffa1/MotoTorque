package com.luffa.mototorque.controller;

import com.luffa.mototorque.model.TorqueSpec;
import com.luffa.mototorque.service.TorqueLookupService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/torque")
public class TorqueController {

    private final TorqueLookupService service;

    public TorqueController(TorqueLookupService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<?> findTorque(
            @RequestParam String brand,
            @RequestParam String model,
            @RequestParam int productionYear,
            @RequestParam String query
    ) {
        var result = service.findTorque(brand, model, productionYear, query);

        if (result.isPresent()) {
            return ResponseEntity.ok(new TorqueResponse(result.get()));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Brak danych dla podanej części");
    }

    public record TorqueResponse(String partName, int torqueNm, String notes, String source){
        public TorqueResponse(TorqueSpec spec) {
            this(spec.getPartName(), spec.getTorqueNm(), spec.getNotes(), spec.getSource());
        }
    }
}
