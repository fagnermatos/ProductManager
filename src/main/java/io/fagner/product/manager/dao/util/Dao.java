package io.fagner.product.manager.dao.util;


import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public interface Dao<T, ID extends Serializable> {
    List<T> findAll();
    Optional<T> findById(ID id);
    T save(T entity);
    T update(T entity);
    void delete(T entity);
}

