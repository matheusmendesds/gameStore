package com.project.gameStore.dtos;

import com.project.gameStore.enums.ProductType;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;

public record GameRequestDTO(

        @NotBlank(message = "O nome do jogo é obrigatório")
        @Size(max = 150, message ="O nome deve ter no máximo 150 caracteres")
        String name,

        String description,

        @NotNull(message = "O preço é obrigatório")
        @DecimalMin(value = "0.0", message = "O preço não pode ser negativo")
        BigDecimal price,

        @NotNull(message = "O tipo do produto é obrigatório")
        ProductType productType,

        @NotNull(message = "O estoque digital é obrigatório")
        @Min(value = 0, message = "O estoque digital não pode ser negativo")
        Integer digitalStock,

        @NotNull(message = "O estoque físico é obrigatório")
        @Min(value = 0, message = "O estoque físico não pode ser negativo")
        Integer physicalStock,

        @NotNull(message = "A categoria é obrigatória")
        UUID categoryId,

        @NotNull(message = "Informe uma plataforma")
        Set<UUID> platformIds

) {

}
