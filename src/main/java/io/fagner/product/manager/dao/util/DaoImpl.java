package io.fagner.product.manager.dao.util;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;


@Repository
public abstract class DaoImpl<T, ID extends Serializable> implements Dao<T, ID> {

    private Class<T> persistentClass;

    @PersistenceContext
    protected EntityManager entityManager;

    public DaoImpl(Class<T> persistentClass) {
        this.persistentClass = persistentClass;
    }

    @Override
    public List<T> findAll() {
        return entityManager.createQuery("from " + persistentClass.getSimpleName(), persistentClass).getResultList();
    }

    @Override
    public Optional<T> findById(ID id) {
        return Optional.ofNullable(entityManager.find(persistentClass, id));
    }

    @Override
    @Transactional
    public T save(T entity) {
        entityManager.persist(entity);
        return entity;
    }

    @Override
    @Transactional
    public T update(T entity) {
        return entityManager.merge(entity);
    }

    @Override
    @Transactional
    public void delete(T entity) {
        entityManager.remove(entity);
    }
}