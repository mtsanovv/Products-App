package com.mtsan.techstore.controllers;

import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;

@ControllerAdvice
public class ExceptionHandlingController
{

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ModelAndView handleHttpMessageNotReadableException (HttpMessageNotReadableException e)
	{
		ModelAndView model = new ModelAndView();
		model.addObject("status", HttpServletResponse.SC_BAD_REQUEST);
		model.addObject("error", "Bad Request");
		model.setViewName("error");

		return model;
	}

}