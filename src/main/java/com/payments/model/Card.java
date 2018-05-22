package com.payments.model;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name="card",uniqueConstraints=@UniqueConstraint(columnNames={"number"}))
@NamedQueries({
	@NamedQuery(name = Card.QUERY_FIND_BY_USER, query = "select c from Card c where c.userId=:userId"),
	@NamedQuery(name = Card.QUERY_FIND_BY_NUMBER, query = "select c from Card c where c.number=:number")
})
public class Card extends DateTimeEntity {
	
	public static final String QUERY_FIND_BY_USER = "CARDS_FIND_BY_USER";
	
	public static final String QUERY_FIND_BY_NUMBER = "CARDS_FIND_BY_NUMBER";
	
	@Id
	@SequenceGenerator(name = "SEQ_CARD", sequenceName = "seq_card", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_CARD")
	@Column(name = "card_id")
	private Integer id;
	
	@Column(name = "uid")
	private String uid;
	
	@Column(name = "number")
	private String number;
	
	@Column(name = "expiry_date")
	private Date expiryDate;
	
	@Column(name = "user_id")
	private Integer userId;
	
	@Column(name="available_balance")
	private BigDecimal balance;
	
	@Column(name="blocked_balance")
	private BigDecimal blockedBalance;

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

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public Date getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}
	
	
	public BigDecimal getBlockedBalance() {
		return blockedBalance;
	}

	public void setBlockedBalance(BigDecimal blockedBalance) {
		this.blockedBalance = blockedBalance;
	}
	
	public void increaseBalance(BigDecimal amount){
		BigDecimal newBalance = increaseAmount(this.balance,amount);
		setBalance(newBalance);
	}
	
	public void decreaseBalance(BigDecimal amount){
		BigDecimal newBalance =  decreaseAmount(this.balance,amount);
		setBalance(newBalance);
	}
	
	public void increaseBlockedAmount(BigDecimal amount){
		BigDecimal newBalance = increaseAmount(this.blockedBalance,amount);
		setBlockedBalance(newBalance);
	}
	
	public void decreaseBlockedAmount(BigDecimal amount){
		BigDecimal newBalance =  decreaseAmount(this.blockedBalance,amount);
		setBlockedBalance(newBalance);
	}
	
	private BigDecimal decreaseAmount(BigDecimal current, BigDecimal amount){
		if(amount != null && current != null){
			return current.subtract(amount);
		}
		
		return null;
	}
	
	private BigDecimal increaseAmount(BigDecimal current, BigDecimal amount){
		if(amount != null && current != null){
			return current.add(amount);
		}
		
		return null;
	}

	public boolean hasBlockedFunds(BigDecimal amount){
		if(blockedBalance == null){
			return false;
		}
		return this.blockedBalance != null && this.blockedBalance.compareTo(amount) >= 0;
	}

	public boolean hasFunds(BigDecimal amount){
		if(balance == null){
			return false;
		}
		return this.balance != null && this.balance.compareTo(amount) >= 0;
	}
	
	public BigDecimal total(){
		return this.balance.add(this.blockedBalance);
	}
	
	public String lastFour(){
		String lastFour = null;
		if(this.number != null && this.number.length() >= 4){
			int index = this.number.length() - 4;
			lastFour = this.number.substring(index, index+4);
		}
		
		return lastFour;
	}
}
