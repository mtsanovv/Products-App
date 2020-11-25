package com.mtsan.techstore;

import org.springframework.ui.Model;

import javax.servlet.http.HttpServletResponse;

public class ErrorPage
{
	public static String generateErrorPage(Model model, HttpServletResponse response, int httpResponse, String errorMessage, String returnTo)
	{
		response.setStatus(httpResponse);
		model.addAttribute("status", response.getStatus());
		model.addAttribute("error", errorMessage);
		model.addAttribute("returnTo", returnTo);
		return "error";
	}
}
