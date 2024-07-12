package io.fagner.product.manager.model.dto;

import io.fagner.product.manager.model.Product;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record ProductRecord(
        @Min(value = 1, message = "{productRecord.id.min}")
        Long id,

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

    public static ProductRecord createProductRecord(Product product) {
        return new ProductRecord(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getQuantityInStock()
        );
    }
}
