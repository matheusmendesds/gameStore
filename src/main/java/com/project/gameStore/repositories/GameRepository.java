package com.project.gameStore.repositories;

import com.project.gameStore.entities.Game;
import com.project.gameStore.enums.ProductType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface GameRepository extends JpaRepository<Game, UUID> {
    boolean existsByName(String name);

    boolean existsByNameAndIdNot(String name, UUID id);
    List<Game> findAllByActiveTrue();
    List<Game> findAllByActiveTrueAndNameContainingIgnoreCase(String name);
    List<Game> findAllByActiveTrueAndCategory_NameIgnoreCase(String categoryName);
    List<Game> findDistinctByActiveTrueAndPlatforms_NameIgnoreCase(String platformName);
    List<Game> findAllByActiveTrueAndProductType(ProductType productType);
    List<Game> findAllByActiveTrueAndPriceBetween(BigDecimal priceMin, BigDecimal priceMax);


}