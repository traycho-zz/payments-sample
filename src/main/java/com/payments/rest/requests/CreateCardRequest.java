package com.payments.rest.requests;

import javax.validation.constraints.NotNull;

public class CreateCardRequest {
	
	@NotNull
	private String number;
	
	@NotNull
	private String expiryDate;

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(String expiryDate) {
		this.expiryDate = expiryDate;
	}
	
	
}
