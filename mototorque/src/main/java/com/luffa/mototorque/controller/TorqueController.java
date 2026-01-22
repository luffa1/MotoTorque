package com.luffa.mototorque.controller;

import com.luffa.mototorque.dto.TorqueApiResponse;
import com.luffa.mototorque.dto.TorqueLookupResponse;
import com.luffa.mototorque.dto.TorqueResponse;
import com.luffa.mototorque.service.TorqueLookupService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/torque")
public class TorqueController {

    private final TorqueLookupService service;

    public TorqueController(TorqueLookupService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<TorqueApiResponse> findTorque(@RequestParam String brand,
                                                        @RequestParam String model,
                                                        @RequestParam int productionYear,
                                                        @RequestParam(required = false) String query) {

        TorqueLookupResponse lookup = service.findTorque(brand, model, productionYear, query);

        if (!lookup.found()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new TorqueApiResponse(List.of(), false, lookup.fallbackSearchUrl()));
        }

        List<TorqueResponse> specs = lookup.specs().stream()
                .map(TorqueResponse::new)
                .toList();

        return ResponseEntity.ok(new TorqueApiResponse(specs, true, null));
    }

    @GetMapping("/suggestions")
    public List<String> suggestPartNames(@RequestParam String brand,
                                         @RequestParam String model,
                                         @RequestParam int productionYear,
                                         @RequestParam String query) {
        return service.suggestPartNames(brand, model, productionYear, query);
    }
}