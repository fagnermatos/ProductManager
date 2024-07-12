package io.fagner.product.manager.unit;

import io.fagner.product.manager.dao.DaoFactory;
import io.fagner.product.manager.dao.ProductDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.ApplicationContext;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

public class DaoFactoryTest {

    @Mock
    private ApplicationContext applicationContext;

    @Mock
    private ProductDao productDao;

    @InjectMocks
    private DaoFactory daoFactory;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void shouldIintializeProductDao() throws Exception {
        when(applicationContext.getBean(ProductDao.class)).thenReturn(productDao);

        ProductDao result = daoFactory.productDao();

        assertNotNull(result);
        assertNotNull(result, "O método productDao deve retornar uma instância de ProductDao");
    }
}