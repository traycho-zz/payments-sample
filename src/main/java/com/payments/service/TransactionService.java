package com.payments.service;

import com.payments.model.Transaction;

public interface TransactionService {
	
	Transaction get(Long id);
	
	Transaction getByUid(Long id);

	Transaction create(Transaction user);

}
