package com.payments.dao;

import java.util.List;

import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.payments.model.Card;

@Transactional
@Repository
public class JpaCarDao extends GenericDao<Card> implements CardDao {
	
	public JpaCarDao() {
		super(Card.class);
	}

	@Override
	public List<Card> findByUser(Integer userId) {
		TypedQuery<Card> query = createNamedQuery(Card.QUERY_FIND_BY_USER);
		query.setParameter("userId", userId);
		return query.getResultList();
	}
	
	@Override
	public Card findByNumber(String number) {
		TypedQuery<Card> query = createNamedQuery(Card.QUERY_FIND_BY_NUMBER);
		query.setParameter("number", number);
		return getSingleResult(query);
	}

}
