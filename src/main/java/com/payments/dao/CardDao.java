package com.payments.dao;

import java.util.List;

import com.payments.model.Card;

public interface CardDao extends Dao<Card> {
	
	List<Card> findByUser(Integer userId);
	
	Card findByNumber(String number);

}
