package com.project.gameStore.dtos;

import com.project.gameStore.enums.ProductType;

import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;

public record GameResponseDTO(
        UUID id,
        String name,
        String description,
        BigDecimal price,
        ProductType productType,
        Integer digitalStock,
        Integer physicalStock,
        Boolean active,
        String category,
        Set<String> platforms
) {
}
