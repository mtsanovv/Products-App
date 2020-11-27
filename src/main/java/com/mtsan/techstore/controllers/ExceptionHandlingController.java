package com.mtsan.techstore.controllers;

import com.mtsan.techstore.ErrorPage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@RestControllerAdvice
public class ExceptionHandlingController {

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<Map<String, Object>> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
		return ErrorPage.generateErrorPage(HttpStatus.BAD_REQUEST, HttpServletResponse.SC_BAD_REQUEST, "Bad Request");
	}

}