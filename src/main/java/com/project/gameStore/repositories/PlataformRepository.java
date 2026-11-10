package com.project.gameStore.repositories;

import com.project.gameStore.entities.Platform;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PlataformRepository extends JpaRepository<Platform, UUID> {
    
}
