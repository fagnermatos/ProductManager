package io.fagner.product.manager.dao;

import io.fagner.product.manager.model.Product;
import io.fagner.product.manager.dao.util.DaoImpl;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


/***
 Comumente eu crio interfaces de repositório implementando
 o JpaRepository para utilizar os métodos presentes no DAO.
 Assim, utilizo os métodos fornecidos  pelo JpaRepository
 injetando os repositórios na camada de serviço.

 As classes DAO e Factory foram feitas tentando seguir os
 requisitos solicitados.
***/
@Repository
public class ProductDao extends DaoImpl<Product, Long> {

    public ProductDao() {
        super(Product.class);
    }

    @Transactional
    public void deleteAll() {
        entityManager.createQuery("DELETE FROM Product").executeUpdate();
    }
}
