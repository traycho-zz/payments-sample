package com.payments.exceptions;

import javax.validation.ValidationException;

public class ApiValidationException extends ValidationException {

	private static final long serialVersionUID = 6892439816964390630L;

	private String code;

	private String field;

	public ApiValidationException(final String code, final String message) {
		super(message);
		this.code = code;
	}

	public ApiValidationException(final String code, final String field, final String message) {
		super(message);
		this.code = code;
		this.field = field;
	}

	public String getCode() {
		return code;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}
}