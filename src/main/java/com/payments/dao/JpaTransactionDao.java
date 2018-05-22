package com.payments.dao;

import java.math.BigDecimal;
import java.util.UUID;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.payments.model.Transaction;
import com.payments.model.TransactionStatus;

@Transactional
@Repository
public class JpaTransactionDao extends GenericDao<Transaction>implements TransactionDao {

	public JpaTransactionDao() {
		super(Transaction.class);
	}
	
	
	@Override
	public Transaction credit(BigDecimal amount, String currency) {
		Transaction transaction = new Transaction();
		transaction.setAmount(amount);
		transaction.setCurrency(currency);
		transaction.setDirection(Transaction.CREDIT);
		transaction.setStatus(TransactionStatus.COMPLETE);
		transaction.setUuid(UUID.randomUUID().toString());
		
		return transaction;
	}
	
	@Override
	public Transaction debit(BigDecimal amount, String currency) {
		Transaction transaction = new Transaction();
		transaction.setAmount(amount);
		transaction.setCurrency(currency);
		transaction.setDirection(Transaction.DEBIT);
		transaction.setStatus(TransactionStatus.COMPLETE);
		transaction.setUuid(UUID.randomUUID().toString());
		
		return transaction;
	}
}

