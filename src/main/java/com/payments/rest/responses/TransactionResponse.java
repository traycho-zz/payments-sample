package com.payments.rest.responses;

import java.math.BigDecimal;

import com.payments.model.TransactionStatus;
import com.payments.model.TransactionType;

public class TransactionResponse {
	
	private String uid;
	
	private String payment;
	
	private TransactionStatus status;
	
	private TransactionType type;
	
	private Integer direction;
	
	private BigDecimal amount;

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}
	
	public String getPayment() {
		return payment;
	}

	public void setPayment(String payment) {
		this.payment = payment;
	}

	public TransactionStatus getStatus() {
		return status;
	}

	public void setStatus(TransactionStatus status) {
		this.status = status;
	}

	public TransactionType getType() {
		return type;
	}

	public void setType(TransactionType type) {
		this.type = type;
	}

	public Integer getDirection() {
		return direction;
	}

	public void setDirection(Integer direction) {
		this.direction = direction;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	
	
}
