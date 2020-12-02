package com.mtsan.techstore.exceptions;

import org.springframework.http.HttpStatus;
import java.io.Serializable;

public class TechstoreDataException extends Exception implements Serializable {

	public static final long serialVersionUID = 1L;

	private HttpStatus status;
	private int statusCode;

	private String message;

	public TechstoreDataException(int statusCode, String message) {
		super(message);
		this.statusCode = statusCode;
		this.message = message;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	@Override
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
