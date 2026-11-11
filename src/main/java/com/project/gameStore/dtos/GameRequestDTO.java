package com.project.gameStore.dtos;

import com.project.gameStore.enums.ProductType;

import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;

public record GameRequestDTO(
        String name,
        String description,
        BigDecimal price,
        ProductType productType,
        Integer digitalStock,
        Integer physicalStock,
        UUID categoryId,
        Set<UUID> platformIds

) {

}
