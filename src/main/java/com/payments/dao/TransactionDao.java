package com.payments.dao;

import java.math.BigDecimal;

import com.payments.model.Transaction;

public interface TransactionDao extends Dao<Transaction> {
	
	
	Transaction credit(BigDecimal amount, String currency);
	
	Transaction debit(BigDecimal amount, String currency);

}
