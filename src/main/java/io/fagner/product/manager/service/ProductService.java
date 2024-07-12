package io.fagner.product.manager.service;

import io.fagner.product.manager.dao.DaoFactory;
import io.fagner.product.manager.handler.EntityNotFoundException;
import io.fagner.product.manager.model.Product;
import io.fagner.product.manager.model.dto.ProductInputRecord;
import io.fagner.product.manager.model.dto.ProductRecord;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static io.fagner.product.manager.model.Product.createProduct;
import static io.fagner.product.manager.model.dto.ProductRecord.createProductRecord;
import static java.lang.String.format;


@Service
@AllArgsConstructor
public class ProductService {

    private DaoFactory daoFactory;

    public List<ProductRecord> findAll() {
        return daoFactory.productDao()
                .findAll()
                .stream()
                .map(ProductRecord::createProductRecord)
                .toList();
    }

    public ProductRecord findById(Long id) {
        return daoFactory.productDao()
                .findById(id)
                .map(ProductRecord::createProductRecord)
                .orElseThrow(() -> new EntityNotFoundException(format("Não foi possível encontrar o produto de id %s.", id)));
    }

    @Transactional
    public ProductRecord save(ProductInputRecord productInput) {
        var savedProduct = daoFactory.productDao()
                .save(Product.createProduct(productInput));
        return createProductRecord(savedProduct);
    }

    @Transactional
    public ProductRecord update(ProductRecord productInput) {
        var updatedProduct = daoFactory.productDao()
                .update(Product.createProduct(productInput));
        return createProductRecord(updatedProduct);
    }

    @Transactional
    public void deleteById(Long id) {
        var deletedProduct = daoFactory.productDao().findById(id);
        deletedProduct.ifPresent(daoFactory.productDao()::delete);
    }

}
