package io.fagner.product.manager.model.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record ProductInputRecord(
        @NotBlank(message = "{productRecord.name.notBlank}")
        String name,

        @NotBlank(message = "{productRecord.description.notBlank}")
        String description,

        @Min(value = 0, message = "{productRecord.price.min}")
        @NotNull(message = "{productRecord.price.notNull}")
        BigDecimal price,

        @Min(value = 0, message = "{productRecord.quantityInStock.min}")
        @NotNull(message = "{productRecord.quantityInStock.notNull}")
        Integer quantityInStock
) {

}
