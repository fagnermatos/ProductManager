package io.fagner.product.manager.unit;

import io.fagner.product.manager.dao.ProductDao;
import io.fagner.product.manager.model.Product;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class DaoImplTest {

    @Mock
    private EntityManager entityManager;

    @Mock
    private TypedQuery<Product> query;

    @InjectMocks
    private ProductDao productDao;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void shouldFindAllProducts() {
        var products = List.of(new Product(), new Product());
        when(query.getResultList()).thenReturn(products);
        when(entityManager.createQuery("from Product", Product.class)).thenReturn(query);
        List<Product> result = productDao.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    public void shouldFindProductById() {
        Product product = new Product();
        when(entityManager.find(Product.class, 1L)).thenReturn(product);

        Optional<Product> result = productDao.findById(1L);

        assertNotNull(result);
        assertEquals(product, result.orElse(null));
    }

    @Test
    public void shouldSaveProducts() {
        Product product = new Product();
        doNothing().when(entityManager).persist(product);

        Product result = productDao.save(product);

        assertNotNull(result);
        assertEquals(product, result);
        verify(entityManager).persist(product);
    }

    @Test
    public void shouldUpdateProducts() {
        Product product = new Product();
        when(entityManager.merge(product)).thenReturn(product);

        Product result = productDao.update(product);

        assertNotNull(result);
        assertEquals(product, result);
        verify(entityManager).merge(product);
    }

    @Test
    public void shouldDeleteProducts() {
        Product product = new Product();
        doNothing().when(entityManager).remove(product);

        productDao.delete(product);

        verify(entityManager).remove(product);
    }
}
