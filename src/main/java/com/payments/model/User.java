package com.payments.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "user")
@NamedQueries({
		@NamedQuery(name = User.QUERY_FIND_BY_EMAIL, query = "select u from User u where UPPER(u.email)= :email")
	})
public class User extends  DateTimeEntity {

	public static final String QUERY_FIND_BY_EMAIL = "USER_FIND_BY_EMAIL";

	@Id
	@SequenceGenerator(name = "SEQ_USER", sequenceName = "seq_user", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_USER")
	@Column(name = "user_id")
	private Integer id;

	@NotNull
	@Column(name = "email", unique = true)
	private String email;

	@NotNull
	@Column(name = "password")
	private String password;

	@Column(name = "uid")
	private String uid;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

}