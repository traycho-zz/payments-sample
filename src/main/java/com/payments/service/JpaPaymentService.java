package com.payments.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.payments.dao.PaymentDao;
import com.payments.dao.TransactionDao;
import com.payments.dao.UserDao;
import com.payments.exceptions.ApiValidationException;
import com.payments.exceptions.ErrorCodes;
import com.payments.model.Card;
import com.payments.model.Payment;
import com.payments.model.PaymentStatus;
import com.payments.model.Transaction;
import com.payments.model.TransactionStatus;
import com.payments.model.TransactionType;
import com.payments.model.User;

@Service
@Transactional
public class JpaPaymentService implements PaymentService {

	private static final Logger LOG = LoggerFactory.getLogger(JpaPaymentService.class);

	@Autowired
	private TransactionDao transactionDao;

	@Autowired
	private CardService cardService;

	@Autowired
	private PaymentDao paymentDao;

	@Autowired
	private UserDao userDao;

	@Override
	public Payment get(Long id) {
		return this.paymentDao.find(id);
	}

	@Override
	public List<Payment> findByUser(Integer userId) {
		return this.paymentDao.findByUser(userId);
	}

	@Override
	public Payment update(Payment payment) {
		return this.paymentDao.update(payment);
	}

	@Override
	public Payment findByUid(String uid) {
		return this.paymentDao.findByUid(uid);
	}

	public Payment validatePayment(String paymentId) {
		Payment payment = this.findByUid(paymentId);
		if (payment == null) {
			throw new ApiValidationException("notExistingEntity", "Payment not found");
		}

		return payment;
	}
	
	public User validateUser(Integer userId){
		User user = this.userDao.find(userId);
		if (user == null) {
			throw new ApiValidationException("notExistingEntity", "User not found");
		}
		return user;
	}
	
	public Card validateCard(String cardId){
		Card card = this.cardService.findByUid(cardId);
		if (card == null) {
			throw new ApiValidationException("notExistingEntity", "Card not found");
		}
		return card;
	}

	@Override
	public Transaction authorize(String paymentUid, String carId) {

		LOG.info("Payment {} authorization with card {}", paymentUid, carId);

		
		Card card = validateCard(carId);
		Payment payment = validatePayment(paymentUid);

		if (!PaymentStatus.PENDING.equals(payment.getStatus())) {
			throw new ApiValidationException("authorizationFailed", "Payment could not be authorized");
		}

		BigDecimal amount = payment.getAmount();
		User merchant = validateUser(payment.getUserId());

		Transaction transaction = new Transaction();
		transaction.setAmount(payment.getAmount());
		transaction.setCard(card);
		transaction.setCurrency(payment.getCurrency());
		transaction.setStatus(TransactionStatus.COMPLETE);
		transaction.setType(TransactionType.AUTH);
		transaction.setUser(merchant);
		transaction.setUuid(UUID.randomUUID().toString());
		transaction.setPayment(payment);

		payment.setStatus(PaymentStatus.AUTORIZED);
		payment.setAuthorizedAmount(amount);
		
		this.update(payment);
		this.cardService.authorize(card, amount);
		return this.transactionDao.save(transaction);

	}
	

	private void increaseCaputedAmount(Payment payment, BigDecimal amount) {
		if (payment == null || amount == null) {
			return;
		}
		
		BigDecimal paid = payment.getPaidAmount().add(amount);
		
		payment.setPaidAmount(paid);
		if (payment.getAuthorizedAmount().equals(paid)) {
			payment.setStatus(PaymentStatus.PAID);
		}
		this.update(payment);
	}
	
	private void reverseAmount(Payment payment, BigDecimal amount) {
		if (payment == null || amount == null) {
			return;
		}
		
		BigDecimal newAuthorized = payment.getAuthorizedAmount().subtract(amount);
		payment.setAuthorizedAmount(newAuthorized);
		BigDecimal newReverseAmount = payment.getReversedAmount().add(amount);
		payment.setReversedAmount(newReverseAmount);
		
		if (payment.getAuthorizedAmount().equals(BigDecimal.ZERO)) {
			payment.setStatus(PaymentStatus.REVERSED);
		}
		this.update(payment);
	}

	
	private void refundAmount(Payment payment, BigDecimal amount) {
		if (payment == null || amount == null) {
			return;
		}
		BigDecimal paid = payment.getPaidAmount().subtract(amount);
		payment.setPaidAmount(paid);
		payment.setRefundAmount(amount);
		payment.setStatus(PaymentStatus.REFUNED);
		this.update(payment);
	}

	@Override
	public Transaction capture(BigDecimal amount, String paymentUid, String cardId) {

		LOG.info("Caputred  amount from payment {}  with card {}", amount, paymentUid, cardId);
		
		Payment payment = validatePayment(paymentUid);
		if (!PaymentStatus.AUTORIZED.equals(payment.getStatus())) {
			throw new ApiValidationException(ErrorCodes.CAPTURE_FAILED, "Payment is not authorized for caputure");
		}

		Card card = validateCard(cardId);

		User user = validateUser(card.getUserId());
		User merchant = validateUser(card.getUserId());
		
		if(!payment.canCapture(amount)){
			throw new ApiValidationException(ErrorCodes.CAPTURE_FAILED, "Unable to capture this amount");
		}
	
		Transaction credit = transactionDao.credit(amount, payment.getCurrency());
		credit.setCard(card);
		credit.setCurrency(payment.getCurrency());
		credit.setType(TransactionType.CAPTURE);
		credit.setUser(user);
		credit.setPayment(payment);
		
		
		Transaction debit = transactionDao.debit(amount, payment.getCurrency());
		debit.setCard(card);
		debit.setCurrency(payment.getCurrency());
		debit.setType(TransactionType.CAPTURE);
		debit.setUser(merchant);
		debit.setPayment(payment);

		
		increaseCaputedAmount(payment, amount);
		this.cardService.capture(card, amount);
		this.transactionDao.save(credit);
		return this.transactionDao.save(debit);
	}

	@Override
	public Transaction refund(BigDecimal amount, String paymentId, String cardId) {
		LOG.info("Refund amount from payment{}  with card {}",amount,paymentId,cardId);
		
		
		Payment payment = validatePayment(paymentId);
		if (!PaymentStatus.PAID.equals(payment.getStatus())) {
			throw new ApiValidationException(ErrorCodes.REFUND_FAILED, "Refund is not allowed for this payment");
		}
		
		Card card = validateCard(cardId);
		User user = validateUser(card.getUserId());
		User merchant = validateUser(payment.getUserId());

		if(!payment.canRefund(amount)){
			throw new ApiValidationException(ErrorCodes.REFUND_FAILED, "Refund amount is more than paid");
		}

		Transaction credit = transactionDao.credit(amount, payment.getCurrency());
		credit.setCard(card);
		credit.setType(TransactionType.REFUND);
		credit.setUser(merchant);
		credit.setPayment(payment);
		
		Transaction debit = transactionDao.debit(amount, payment.getCurrency());
		debit.setCard(card);
		debit.setType(TransactionType.REFUND);
		debit.setUser(user);
		debit.setPayment(payment);
		
		refundAmount(payment,amount);
		this.cardService.refund(card, amount);
		this.transactionDao.save(debit);
		return this.transactionDao.save(credit);

		
	}
	
	@Override
	public Transaction reverse(BigDecimal amount, String paymentId, String cardId) {
		LOG.info("Reverse amount from payment {}  with card {}",amount,paymentId,cardId);
		
		Payment payment = validatePayment(paymentId);
		if (!PaymentStatus.AUTORIZED.equals(payment.getStatus())) {
			throw new ApiValidationException(ErrorCodes.REVERSE_FAILED, "Reverse is not allowed for this payment");
		}
		
		Card card = validateCard(cardId);
		User merchant = validateUser(payment.getUserId());
		
	
		if(!payment.canReverse(amount)){
			throw new ApiValidationException(ErrorCodes.REVERSE_FAILED, "Unable to revers this amount");
		}
		
		Transaction transaction = new Transaction();
		transaction.setAmount(amount);
		transaction.setCard(card);
		transaction.setCurrency(payment.getCurrency());
		transaction.setStatus(TransactionStatus.COMPLETE);
		transaction.setType(TransactionType.REVERSE);
		transaction.setUser(merchant);
		transaction.setUuid(UUID.randomUUID().toString());
		transaction.setPayment(payment);
		reverseAmount(payment,amount);
		this.cardService.reverse(card, amount);
		
		return transaction;
	}

	@Override
	public Payment create(Payment payment) {
		return this.paymentDao.save(payment);
	}

}
