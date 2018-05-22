package com.payments.dao;

import java.util.List;

import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.payments.model.Payment;

@Transactional
@Repository
public class JpaPaymentDao extends GenericDao<Payment>implements PaymentDao {

	public JpaPaymentDao() {
		super(Payment.class);
	}
	
	@Override
	public List<Payment> findByUser(Integer userId) {
		TypedQuery<Payment> query = createNamedQuery(Payment.QUERY_FIND_BY_USER_ID);
		query.setParameter("userId", userId);
		return query.getResultList();
	}

	
}
