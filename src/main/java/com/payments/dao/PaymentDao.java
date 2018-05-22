package com.payments.dao;

import java.util.List;

import com.payments.model.Payment;

public interface PaymentDao extends Dao<Payment> {
	
	List<Payment> findByUser(Integer userId);
}
