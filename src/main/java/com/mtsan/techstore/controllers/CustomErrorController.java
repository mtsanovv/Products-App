package com.mtsan.techstore.controllers;

import com.mtsan.techstore.ErrorPage;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@RestController
public class CustomErrorController implements ErrorController {

	@RequestMapping(value = "/error")
	public ResponseEntity<Map<String, Object>> showError(HttpServletRequest request) {
		Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

		if (status != null) {
			int statusCode = Integer.parseInt(status.toString());

			HttpStatus httpStatus = HttpStatus.resolve(statusCode);

			if(httpStatus != null) {
				return ErrorPage.generateErrorPage(httpStatus, statusCode, httpStatus.getReasonPhrase());
			}
		}
		return ErrorPage.generateErrorPage(HttpStatus.BAD_REQUEST, HttpServletResponse.SC_BAD_REQUEST, "Bad Request");
	}

	public String getErrorPath() {
		return null;
	}
}
