package io.fagner.product.manager.unit;

import io.fagner.product.manager.model.Product;
import io.fagner.product.manager.model.dto.ProductRecord;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class ProductUnitTest {

    @Test
    public void testGettersAndSetters() {
        var product = Product.of(1L, "Name", "Description", new BigDecimal("25.00"), 10);

        assertThat(product.getId()).isEqualTo(1L);
        assertThat(product.getName()).isEqualTo("Name");
        assertThat(product.getDescription()).isEqualTo("Description");
        assertThat(product.getPrice()).isEqualTo(new BigDecimal("25.00"));
        assertThat(product.getQuantityInStock()).isEqualTo(10);
    }



    @Test
    public void testEquals() {
        var product1 = new Product(1L, "Name 1", "Description 1", new BigDecimal("25.00"), 10);
        var product2 = new Product(1L, "Name 1", "Description 1", new BigDecimal("25.00"), 10);
        var product3 = new Product(2L, "Name 2", "Description 2", new BigDecimal("26.00"), 5);

        var product4 = new ProductRecord(2L, "Name 3", "Description 2", new BigDecimal("26.00"), 5);
        var product5 = new Product(10L, "Name 3", "Description 3", new BigDecimal("26.00"), 5);
        var product6 = new Product(2L, "Name 6", "Description 2", new BigDecimal("26.00"), 5);

        assertThat(product1.equals(null)).isFalse();
        assertThat(product1.equals(product2)).isTrue();
        assertThat(product3.equals(product5)).isFalse();
        assertThat(product3.equals(product6)).isFalse();
        assertThat(product1).isEqualTo(product1);
        assertThat(product1).isEqualTo(product2);
        assertThat(product1).isNotEqualTo(product4);
    }


    @Test
    public void testHashCode() {
        var product1 = new Product(1L, "Name 1", "Description 1", new BigDecimal("25.00"), 10);
        var product2 = new Product(1L, "Name 1", "Description 1", new BigDecimal("25.00"), 10);
        var product3 = new Product(2L, "Name 2", "Description 2", new BigDecimal("26.00"), 5);
        var product4 = new ProductRecord(2L, "Name 3", "Description 2", new BigDecimal("26.00"), 5);

        assertThat(product1.hashCode()).isEqualTo(product2.hashCode());
        assertThat(product1.hashCode()).isNotEqualTo(product3.hashCode());
        assertThat(product1.hashCode()).isNotEqualTo(product4.hashCode());
        assertThat(product1).hasSameHashCodeAs(product2);

    }

    @Test
    public void testToString() {
        var product = new Product(1L, "Name", "Description", new BigDecimal("25.00"), 10);
        var result = product.toString();

        assertThat(result).isEqualTo("Product(id=1, name=Name, description=Description, price=25.00, quantityInStock=10)");
    }


}
