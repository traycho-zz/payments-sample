package com.payments.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.payments.dao.CardDao;
import com.payments.exceptions.ApiValidationException;
import com.payments.exceptions.ErrorCodes;
import com.payments.model.Card;
import com.payments.model.Transaction;


@Service
@Transactional
public class JpaCardService implements CardService {
	
	@Autowired
	private CardDao cardDao;

	@Override
	public Card get(Long id) {
		return this.cardDao.find(id);
	}

	@Override
	public List<Card> findByUser(Integer userId) {
		return this.cardDao.findByUser(userId);
	}
	
	@Override
	public Card create(Card card) {
		Card existing = this.cardDao.findByNumber(card.getNumber());
		if(existing != null){
			throw new ApiValidationException("existingEntity", "Card with such number alredy exists");
		}
		
		return this.cardDao.save(card);
	}
	
	@Override
	public Card findByUid(String uid) {
		return this.cardDao.findByUid(uid);
	}
	
	@Override
	public Card topup(Transaction transaction, String uid) {
		Card card = this.findByUid(uid);
		BigDecimal balance = card.getBalance().add(transaction.getAmount());
		card.setBalance(balance);
		transaction.setCard(card);
		this.cardDao.save(card);
		this.cardDao.getEntityManager().persist(transaction);
		return card;
	}
	
	@Override
	public Card authorize(Card card, BigDecimal amount) {
		if(!card.hasFunds(amount)){
			throw new ApiValidationException(ErrorCodes.AUTH_FAILED,"Insufficient funds");
		}
		
		card.decreaseBalance(amount);
		card.increaseBlockedAmount(amount);
		return this.cardDao.save(card);
	}
	
	@Override
	public Card capture(Card card, BigDecimal amount) {
		if(!card.hasBlockedFunds(amount)){
			throw new ApiValidationException(ErrorCodes.CAPTURE_FAILED,"Insufficient funds");
		}
		
		card.decreaseBlockedAmount(amount);
		return this.cardDao.save(card);
	}
	
	@Override
	public Card refund(Card card, BigDecimal amount) {
		card.increaseBalance(amount);
		return this.cardDao.save(card);
	}
	
	@Override
	public Card reverse(Card card, BigDecimal amount) {
		if(!card.hasBlockedFunds(amount)){
			throw new ApiValidationException(ErrorCodes.REVERSE_FAILED,"Insufficient funds");
		}
		
		card.increaseBalance(amount);
		card.decreaseBlockedAmount(amount);
		return this.cardDao.save(card);
	}
}
