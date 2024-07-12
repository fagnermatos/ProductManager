package io.fagner.product.manager.model;

import io.fagner.product.manager.model.dto.ProductInputRecord;
import io.fagner.product.manager.model.dto.ProductRecord;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.Objects;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@ToString
@Table(name = "product")
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(name = "name", unique = true)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "quantityInStock")
    private Integer quantityInStock;

    public static Product of(Long id, String name, String description, BigDecimal price, Integer quantityInStock) {
        return new Product(id, name, description, price, quantityInStock);
    }

    public static Product createProduct(ProductInputRecord record) {
        return new Product(
                null,
                record.name(),
                record.description(),
                record.price(),
                record.quantityInStock());
    }

    public static Product createProduct(ProductRecord record) {
        return new Product(
                record.id(),
                record.name(),
                record.description(),
                record.price(),
                record.quantityInStock());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Product product)) return false;
        return Objects.equals(id, product.id) && Objects.equals(name, product.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
