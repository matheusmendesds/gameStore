package com.project.gameStore.repositories;

import com.project.gameStore.entities.Game;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface GameRepository extends JpaRepository<Game, UUID> {
    boolean existsByName(String name);

    boolean existsByNameAndIdNot(String name, UUID id);
    List<Game> findAllByActiveTrue();
    List<Game> findAllByActiveTrueAndNameContainingIgnoreCase(String name);
    List<Game> findAllByActiveTrueAndCategory_NameIgnoreCase(String categoryName);
    List<Game> findDistinctByActiveTrueAndPlatforms_NameIgnoreCase(String platformName);


}
