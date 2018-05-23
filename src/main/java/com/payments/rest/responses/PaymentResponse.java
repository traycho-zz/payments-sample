package com.payments.rest.responses;

import java.math.BigDecimal;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.payments.jackson.serializers.AmountSerializer;
import com.payments.model.PaymentStatus;

public class PaymentResponse {

	private String uid;

	private String description;
	
	@JsonSerialize(using = AmountSerializer.class)
	private BigDecimal amount;

	private PaymentStatus status;

	private String currency;

	private String user;

	@JsonSerialize(using = AmountSerializer.class)
	private BigDecimal refundAmount;

	@JsonSerialize(using = AmountSerializer.class)
	private BigDecimal reversedAmount;

	@JsonSerialize(using = AmountSerializer.class)
	private BigDecimal authorizedAmount;

	@JsonSerialize(using = AmountSerializer.class)
	private BigDecimal paidAmount;

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public PaymentStatus getStatus() {
		return status;
	}

	public void setStatus(PaymentStatus status) {
		this.status = status;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public BigDecimal getRefundAmount() {
		return refundAmount;
	}

	public void setRefundAmount(BigDecimal refundAmount) {
		this.refundAmount = refundAmount;
	}

	public BigDecimal getReversedAmount() {
		return reversedAmount;
	}

	public void setReversedAmount(BigDecimal reversedAmount) {
		this.reversedAmount = reversedAmount;
	}

	public BigDecimal getAuthorizedAmount() {
		return authorizedAmount;
	}

	public void setAuthorizedAmount(BigDecimal authorizedAmount) {
		this.authorizedAmount = authorizedAmount;
	}

	public BigDecimal getPaidAmount() {
		return paidAmount;
	}

	public void setPaidAmount(BigDecimal paidAmount) {
		this.paidAmount = paidAmount;
	}

}
