package io.fagner.product.manager.unit;

import io.fagner.product.manager.dao.DaoFactory;
import io.fagner.product.manager.dao.ProductDao;
import io.fagner.product.manager.handler.EntityNotFoundException;
import io.fagner.product.manager.model.Product;
import io.fagner.product.manager.model.dto.ProductInputRecord;
import io.fagner.product.manager.model.dto.ProductRecord;
import io.fagner.product.manager.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceUnitTest {

    @Mock
    private DaoFactory daoFactory;

    @InjectMocks
    private ProductService service;

    @BeforeEach
    public void instantiateMocks() {
        MockitoAnnotations.openMocks(this);
        ProductDao productDaoMock = mock(ProductDao.class);
        when(daoFactory.productDao()).thenReturn(productDaoMock);
    }

    @Test
    public void shouldFindAllProducts() {
        List<Product> products = new ArrayList<>();
        products.add(new Product(1L, "Product 1", "Description 1", new BigDecimal("10.00"), 100));
        products.add(new Product(2L, "Product 2", "Description 2", new BigDecimal("20.00"), 200));

        when(daoFactory.productDao().findAll()).thenReturn(products);
        var productList = service.findAll();
        assertEquals(2, productList.size());
    }

    @Test
    public void shouldFindProductById() {
        Long productId = 1L;
        var product = new Product(1L, "Product 1", "Description 1", new BigDecimal("10.00"), 100);

        when(daoFactory.productDao().findById(productId)).thenReturn(Optional.of(product));
        var foundProduct = service.findById(productId);
        assertThat(foundProduct).isNotNull();
        assertEquals("Product 1", foundProduct.name());
    }

    @Test
    public void shouldThrowExceptionIfProductNotFound() {
        Long inexistingProduct = 1L;
        assertThrows(EntityNotFoundException.class, () -> service.findById(inexistingProduct));
    }

    @Test
    public void shouldSaveProduct() {
        var product = new Product(null, "New Product", "New Description", new BigDecimal("30.00"), 50);
        var productInput = new ProductInputRecord("New Product", "New Description", new BigDecimal("30.00"), 50);
        var savedProduct = new Product(1L, "New Product", "New Description", new BigDecimal("30.00"), 50);

        when(daoFactory.productDao().save(product)).thenReturn(savedProduct);
        var createdProduct = service.save(productInput);
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
        var updatedProduct = new Product(productId, "Updated Product", "Updated Description", new BigDecimal("26.00"), 76);

        when(daoFactory.productDao().findById(productId)).thenReturn(Optional.of(existingProduct));
        var savedProduct = service.findById(productId);

        assertEquals(1L, savedProduct.id());
        assertEquals("Existing Product", savedProduct.name());
        assertEquals("Existing Description", savedProduct.description());
        assertEquals(new BigDecimal("25.00"), savedProduct.price());
        assertEquals(75, savedProduct.quantityInStock());

        when(daoFactory.productDao().update(Product.createProduct(savedProduct))).thenReturn(updatedProduct);
        var updatedProductRecord = service.update(productInput);

        assertEquals(1L, updatedProductRecord.id());
        assertEquals("Updated Product", updatedProductRecord.name());
        assertEquals("Updated Description", updatedProductRecord.description());
        assertEquals(new BigDecimal("26.00"), updatedProductRecord.price());
        assertEquals(76, updatedProductRecord.quantityInStock());
    }

    @Test
    public void shouldDeleteProduct() {
        var product = new Product(1L, "Product 1", "Description 1", new BigDecimal("10.00"), 100);

        when(daoFactory.productDao().findById(product.getId())).thenReturn(Optional.of(product));

        service.deleteById(product.getId());
        var argumentCaptor = ArgumentCaptor.forClass(Product.class);
        verify(daoFactory.productDao()).delete(argumentCaptor.capture());
        assertEquals(product.getId(), argumentCaptor.getValue().getId());
    }


}
