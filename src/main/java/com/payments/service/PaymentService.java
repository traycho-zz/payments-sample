package com.payments.service;

import java.math.BigDecimal;
import java.util.List;

import com.payments.model.Payment;
import com.payments.model.Transaction;

public interface PaymentService {
	
	Payment get(Long id);

	List<Payment> findByUser(Integer userId);
	
	Payment create(Payment payment);
	
	Payment update(Payment payment);
	
	Payment findByUid(String uid);
	
	Transaction authorize(String paymentId, String cardId);
	
	Transaction capture(BigDecimal amount,String paymentId,String cardId);
	
	Transaction refund(BigDecimal amount,String paymentId,String cardId);
	
	Transaction reverse(BigDecimal amount,String paymentId,String cardId);
}
