package com.payments.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.payments.dao.TransactionDao;
import com.payments.model.Transaction;


@Service
@Transactional
public class JpaTransactionService implements TransactionService {
	
	private static final Logger LOG = LoggerFactory.getLogger(JpaTransactionService.class);
	    
	
	@Autowired
	private TransactionDao transactionDao;


	@Override
    public Transaction create(final Transaction transaction) {

        return transactionDao.save(transaction);
    }


	@Override
	public Transaction get(Long id) {
		return transactionDao.find(id);
	}


	@Override
	public Transaction getByUid(Long id) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
