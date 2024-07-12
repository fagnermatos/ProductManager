package io.fagner.product.manager.unit;

import io.fagner.product.manager.model.Product;
import io.fagner.product.manager.controller.ProductController;
import io.fagner.product.manager.handler.BadRequestException;
import io.fagner.product.manager.model.dto.ProductInputRecord;
import io.fagner.product.manager.model.dto.ProductRecord;
import io.fagner.product.manager.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductControllerUnitTest {

    @Mock
    private ProductService service;

    @InjectMocks
    private ProductController controller;

    @BeforeEach
    public void instantiateMocks() {
        service.findAll().forEach(productRecord -> service.deleteById(productRecord.id()));
    }

    @Test
    public void shouldFindAllProducts() {
        List<ProductRecord> products = new ArrayList<>();
        products.add(new ProductRecord(1L, "Product 1", "Description 1", new BigDecimal("10.00"), 100));
        products.add(new ProductRecord(2L, "Product 2", "Description 2", new BigDecimal("20.00"), 200));

        when(service.findAll()).thenReturn(products);
        var productList = controller.findAll();
        assertEquals(2, productList.size());
    }

    @Test
    public void shouldFindProductById() {
        Long productId = 1L;
        var product = new ProductRecord(1L, "Product 1", "Description 1", new BigDecimal("10.00"), 100);

        when(service.findById(productId)).thenReturn(product);
        var foundProduct = controller.findById(productId);
        assertThat(foundProduct).isNotNull();
        assertEquals("Product 1", foundProduct.name());
    }

    @Test
    public void shouldSaveProduct() {
        var product = new ProductInputRecord( "New Product", "New Description", new BigDecimal("30.00"), 50);
        var productInput = new ProductInputRecord("New Product", "New Description", new BigDecimal("30.00"), 50);
        var savedProduct = new ProductRecord(1L, "New Product", "New Description", new BigDecimal("30.00"), 50);

        when(service.save(product)).thenReturn(savedProduct);
        var createdProduct = controller.save(productInput);
        assertEquals(1L, createdProduct.id());
        assertEquals("New Product", createdProduct.name());
        assertEquals("New Description", createdProduct.description());
        assertEquals(new BigDecimal("30.00"), createdProduct.price());
        assertEquals(50, createdProduct.quantityInStock());
    }

    @Test
    public void shouldUpdateProduct() {
        Long productId = 1L;
        var existingProduct = new Product(productId, "Existing Product", "Existing Description", new BigDecimal("25.00"), 75);
        var productInput = ProductRecord.createProductRecord(existingProduct);
        var updatedProduct = new ProductRecord(productId, "Updated Product", "Updated Description", new BigDecimal("26.00"), 76);

        when(service.findById(productId)).thenReturn(ProductRecord.createProductRecord(existingProduct));
        var savedProduct = controller.findById(productId);

        assertEquals(1L, savedProduct.id());
        assertEquals("Existing Product", savedProduct.name());
        assertEquals("Existing Description", savedProduct.description());
        assertEquals(new BigDecimal("25.00"), savedProduct.price());
        assertEquals(75, savedProduct.quantityInStock());

        when(service.update(savedProduct)).thenReturn(updatedProduct);
        controller.update(productId, productInput);
        verify(service).update(productInput);
    }

    @Test
    public void shouldThrowExceptionWhenArePassedInvalidArguments() {
        var product = new ProductRecord(1L, "Product", "Description", new BigDecimal("26.00"), 76);
        assertThrows(BadRequestException.class, () -> controller.update(2L, product));
    }

    @Test
    public void shouldDeleteProduct() {
        var productId = 1L;
        controller.delete(productId);
        verify(service).deleteById(productId);

    }
}
