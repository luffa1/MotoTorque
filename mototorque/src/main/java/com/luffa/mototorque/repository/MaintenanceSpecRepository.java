package com.luffa.mototorque.repository;

import com.luffa.mototorque.model.MaintenanceSpec;
import com.luffa.mototorque.model.Motorcycle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MaintenanceSpecRepository extends JpaRepository<MaintenanceSpec, Long> {
    List<MaintenanceSpec> findByMotorcycleOrderByCategoryAscParameterAsc(Motorcycle motorcycle);

}
