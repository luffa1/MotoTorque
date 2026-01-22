package com.luffa.mototorque.repository;

import com.luffa.mototorque.model.Motorcycle;
import com.luffa.mototorque.model.PartAlias;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PartAliasRepository extends JpaRepository<PartAlias, Long> {

    Optional<PartAlias> findFirstByAliasIgnoreCase(String alias);

    @Query("""
        select distinct pa.alias
        from PartAlias pa
        join TorqueSpec ts on pa.partName = ts.partName
        where ts.motorcycle = :motorcycle
          and lower(pa.alias) like lower(concat(:prefix, '%'))
        """)
    List<String> findAliasesForMotorcycleStartingWith(Motorcycle motorcycle,
                                                      String prefix,
                                                      Pageable pageable);
}