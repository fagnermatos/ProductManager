package io.fagner.product.manager.unit;

import io.fagner.product.manager.ProductManagerApplication;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ProductManagerApplicationTest {

    @Test
    void contextLoads() {
    }

    @Test
    public void testMain() {
        ProductManagerApplication.main(new String[]{});
    }

}
