package com.payments.service;

import java.math.BigDecimal;
import java.util.List;

import com.payments.model.Card;
import com.payments.model.Transaction;

public interface CardService {
	
	Card get(Long id);

	List<Card> findByUser(Integer userId);
	
	Card create(Card card);
	
	Card findByUid(String uid);
	
	Card topup(Transaction transaction,String uid);
	
	Card authorize(Card card,BigDecimal amount);
	
	Card capture(Card card,BigDecimal amount);
	
	Card refund(Card card,BigDecimal amount);
	
	Card reverse(Card card,BigDecimal amount);
}
