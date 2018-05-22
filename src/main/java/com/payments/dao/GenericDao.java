package com.payments.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

public class GenericDao<T> implements Dao<T> {

	@PersistenceContext
	protected EntityManager entityManager;
	
	protected Class<T> type;

	public GenericDao(final Class<T> type) {
		this.type = type;
	}

	@SuppressWarnings("unchecked")
	public List<T> findAll() {
		final Query query = getEntityManager().createQuery("select x from " + this.type.getName() + " x");
		return query.getResultList();
	}

	@Override
	public T findByUid(String uid) {
		final Query query = getEntityManager().createQuery("select x from " + this.type.getName() + " x where x.uid=:uid");
		query.setParameter("uid", uid);
		return getSingleResult(query);
	}

	public T save(final T entity) {
		if (entity == null) {
			return null;
		}

		getEntityManager().persist(entity);
		return entity;
	}

	public void delete(final T entity) {
		if (entity == null) {
			return;
		}

		getEntityManager().remove(entity);
	}

	public T find(final Object id) {
		return getEntityManager().find(this.type, id);
	}

	public T update(final T entity) {
		if (entity == null) {
			return null;
		}

		return getEntityManager().merge(entity);
	}
	
	@SuppressWarnings("unchecked")
	protected <X> X getSingleResult(final Query query) {
		final List<X> results = (List<X>)query.getResultList();
		X foundEntity = null;
		if (!results.isEmpty()) {
			foundEntity = results.get(0);
		}

		return foundEntity;
	}

	protected <X> X getSingleResult(final TypedQuery<X> query) {
		final List<X> results = query.getResultList();
		X foundEntity = null;
		if (!results.isEmpty()) {
			foundEntity = results.get(0);
		}

		return foundEntity;
	}
	
	public <X> TypedQuery<X> createNamedQuery(final String name,Class<X> type) {
		return getEntityManager().createNamedQuery(name, type);
	}

	public TypedQuery<T> createNamedQuery(final String name) {
		return getEntityManager().createNamedQuery(name, this.type);
	}

	public Query createQuery(final String name) {
		return getEntityManager().createNamedQuery(name);
	}
	
	public EntityManager getEntityManager() {
		return entityManager;
	}

}
