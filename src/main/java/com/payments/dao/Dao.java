package com.payments.dao;

import java.util.List;

import javax.persistence.EntityManager;

public interface Dao<T> {
    
    List<T> findAll();

    T save(T entity);

    void delete(T entity);

    T find(Object id);
    
    T findByUid(String uid);

    T update(T entity);
    
    EntityManager getEntityManager();
}
