package com.payments.rest.requests;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

public class PaymentRequest {
	
	@NotNull
	private String card;
	
	private BigDecimal amount;

	
	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getCard() {
		return card;
	}

	public void setCard(String card) {
		this.card = card;
	}
	
	
}
