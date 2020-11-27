package com.mtsan.techstore;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

public class ErrorPage {
	public static ResponseEntity<Map<String, Object>> generateErrorPage(HttpStatus httpStatus, int httpCode, String errorMessage) {
		Map<String, Object> result = new HashMap<>();
		result.put("code", httpCode);
		result.put("message", errorMessage);
		return new ResponseEntity<>(result, httpStatus);
	}
}
