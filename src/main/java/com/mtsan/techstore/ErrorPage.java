package com.mtsan.techstore;

import com.mtsan.techstore.models.ExceptionModel;
import org.springframework.http.ResponseEntity;

public class ErrorPage {
	public static ResponseEntity generateErrorPage(int httpCode, String errorMessage) {
		ExceptionModel em = new ExceptionModel(httpCode, errorMessage);
		return ResponseEntity.status(httpCode).body(em);
	}
}
