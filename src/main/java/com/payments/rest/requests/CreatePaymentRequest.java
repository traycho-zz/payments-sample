package com.payments.rest.requests;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

public class CreatePaymentRequest {
	
	@NotNull
	private BigDecimal amount;
	
	@NotNull
	private String currency;
	
	@NotNull
	private String description;
	
	@NotNull
	private String recepient;

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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getRecepient() {
		return recepient;
	}

	public void setRecepient(String recepient) {
		this.recepient = recepient;
	}
	
}
