package com.luffa.mototorque.repository;

import com.luffa.mototorque.model.PartAlias;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PartAliasRepository extends JpaRepository<PartAlias, Long> {
    Optional<PartAlias> findFirstByAliasIgnoreCase(String alias);
}
