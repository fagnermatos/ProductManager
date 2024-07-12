package io.fagner.product.manager.unit;

import io.fagner.product.manager.dao.ProductDao;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.springframework.transaction.annotation.Transactional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ProductDaoTest {

    @Mock
    private EntityManager entityManager;

    @Mock
    private Query query;

    @InjectMocks
    private ProductDao productDao;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @Transactional
    public void shouldDeleteAllProducts() {
        when(entityManager.createQuery("DELETE FROM Product")).thenReturn(query);

        productDao.deleteAll();

        verify(query).executeUpdate();
    }
}
