package com.payments.rest.requests;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

public class CardActionRequest {
	
	@NotNull
	private BigDecimal amount;
	
	@NotNull
	private String currency;

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}
	
	
	
	
}
