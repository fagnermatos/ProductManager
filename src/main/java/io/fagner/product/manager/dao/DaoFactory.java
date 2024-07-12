package io.fagner.product.manager.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class DaoFactory {

    @Autowired
    private ApplicationContext applicationContext;

    public ProductDao productDao() {
        return applicationContext.getBean(ProductDao.class);
    }

}