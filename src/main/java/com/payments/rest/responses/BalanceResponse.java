package com.payments.rest.responses;

import java.math.BigDecimal;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.payments.jackson.serializers.AmountSerializer;

public class BalanceResponse {
	
	@JsonSerialize(using = AmountSerializer.class)
	private BigDecimal total;
	
	@JsonSerialize(using = AmountSerializer.class)
	private BigDecimal available;
	
	@JsonSerialize(using = AmountSerializer.class)
	private BigDecimal blocked;
	
	private String currency;

	public BigDecimal getAvailable() {
		return available;
	}

	public void setAvailable(BigDecimal available) {
		this.available = available;
	}

	public BigDecimal getBlocked() {
		return blocked;
	}

	public void setBlocked(BigDecimal blocked) {
		this.blocked = blocked;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}
	
}
