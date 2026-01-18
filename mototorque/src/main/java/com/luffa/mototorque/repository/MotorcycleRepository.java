package com.luffa.mototorque.repository;

import com.luffa.mototorque.model.Motorcycle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MotorcycleRepository extends JpaRepository<Motorcycle, Long> {
    List<Motorcycle> findByBrandIgnoreCase(String brand);
    List<Motorcycle> findByBrandIgnoreCaseAndModelAndProductionYear(String brand, String model, int productionYear);


    @Query("""
           SELECT DISTINCT m.brand
           FROM Motorcycle m
           WHERE LOWER(m.brand) LIKE LOWER(CONCAT('%', :query, '%'))
           ORDER BY m.brand ASC
           """)
    List<String> searchBrands(@Param("query") String query);

    @Query("""
           SELECT DISTINCT m.model
           FROM Motorcycle m
           WHERE LOWER(m.brand) = LOWER(:brand)
             AND LOWER(m.model) LIKE LOWER(CONCAT('%', :query, '%'))
           ORDER BY m.model ASC
           """)
    List<String> searchModels(@Param("brand") String brand,
                              @Param("query") String query);

    @Query("SELECT DISTINCT m.productionYear FROM Motorcycle m " +
            "WHERE LOWER(m.brand) = LOWER (:brand)" +
            "AND LOWER(m.model) = LOWER (:model)" +
            "ORDER BY m.productionYear")
    List<Integer> findProductionYearByBrandAndModel(@Param("brand") String brand,
                                                    @Param("model") String model);

}

