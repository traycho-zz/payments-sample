
package com.payments.model;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "payment")
@NamedQueries({
    @NamedQuery(name = Payment.QUERY_FIND_BY_USER_ID, query = "select p from Payment p where p.userId=:userId")})
public class Payment extends DateTimeEntity {
	
	 public static final String QUERY_FIND_BY_USER_ID = "PAYMENT_FIND_BY_USER_ID";
	
	@Id
	@SequenceGenerator(name = "SEQ_PAYMENT", sequenceName = "seq_payment", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_PAYMENT")
	@Column(name = "payment_id")
	private Integer id;

	@Column(name = "uid")
	private String uid;
	
	@Column(name = "description")
	private String description;
	
	@Column(name = "amount")
	private BigDecimal amount;
	
	@Enumerated(EnumType.STRING)
    @Column(name = "status")
    private PaymentStatus status;
	
	@Column(name = "currency")
	private String currency;
	
	@Column(name = "user_id")
	private Integer userId;

	@Column(name = "refundAmount")
	private BigDecimal refundAmount = BigDecimal.ZERO;
	
	@Column(name = "reversedAmount")
	private BigDecimal reversedAmount = BigDecimal.ZERO;
	
	@Column(name = "authorizedAmount")
	private BigDecimal authorizedAmount = BigDecimal.ZERO;
	
	@Column(name = "paidAmount")
	private BigDecimal paidAmount = BigDecimal.ZERO;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

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

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public PaymentStatus getStatus() {
		return status;
	}

	public void setStatus(PaymentStatus status) {
		this.status = status;
	}

	public BigDecimal getPaidAmount() {
		return paidAmount;
	}

	public void setPaidAmount(BigDecimal paidAmount) {
		this.paidAmount = paidAmount;
	}


	public boolean canRefund(BigDecimal amount) {
		return amount != null && this.paidAmount.compareTo(amount) >= 0;
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
	
	public boolean canCapture(BigDecimal amount){
		if(amount == null){
			return false;
		}
		BigDecimal canCapture = this.getAuthorizedAmount().subtract(this.getPaidAmount());
		return canCapture.compareTo(amount) >= 0;
		
	}
	
	public boolean canReverse(BigDecimal amount){
		if(amount == null){
			return false;
		}
		
		BigDecimal reversable = this.authorizedAmount.subtract(this.getPaidAmount());
		
		return reversable.compareTo(amount) >= 0;
	}
	
	
}
