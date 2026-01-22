package com.luffa.mototorque.repository;

import com.luffa.mototorque.model.Motorcycle;
import com.luffa.mototorque.model.TorqueSpec;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TorqueSpecRepository extends JpaRepository<TorqueSpec, Long> {
    Optional<TorqueSpec> findByMotorcycleAndPartNameIgnoreCase(Motorcycle motorcycle, String partName);

    List<TorqueSpec> findByMotorcycleAndThreadSizeContainingIgnoreCase(Motorcycle motorcycle, String threadSizePart);
}

