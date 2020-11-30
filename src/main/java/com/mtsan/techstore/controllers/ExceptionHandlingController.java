package com.mtsan.techstore.controllers;

import com.mtsan.techstore.ErrorPage;
import com.mtsan.techstore.exceptions.TechstoreDataException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@RestControllerAdvice
public class ExceptionHandlingController {

	//custom TechStore exception that is thrown whenever the app determines it needs to throw one
	@ExceptionHandler(TechstoreDataException.class)
	public ResponseEntity handleTechstoreDataException(TechstoreDataException e) {
		return ErrorPage.generateErrorPage(e.getStatusCode(), e.getMessage());
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
		return ErrorPage.generateErrorPage(HttpServletResponse.SC_BAD_REQUEST, "Bad Request");
	}

	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity handleDataIntegrityViolationException(HttpMessageNotReadableException e) {
		return ErrorPage.generateErrorPage(HttpServletResponse.SC_BAD_REQUEST, "Bad Request");
	}

}