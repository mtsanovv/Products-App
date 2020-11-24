package com.mtsan.techstore.controllers;


import com.mtsan.techstore.entities.Products;
import com.mtsan.techstore.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.persistence.NoResultException;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Controller
public class ProductsController
{

	@Autowired
	private ProductRepository productRepository;

	//fetching a list of all products
	@RequestMapping(value = "/products", method = RequestMethod.GET)
	public String products(Model model, HttpServletResponse response)
	{
		if(productRepository.count() > 0)
		{
			Iterable<Products> allProducts = productRepository.findAll();
			ArrayList<String> headers = new ArrayList<>(Arrays.asList("ID", "Name", "Quantity", "Critical quantity", "Price per item"));
			ArrayList<Map<String, Object>> rows = new ArrayList<>();
			for (Products product : allProducts)
			{
				Map<String, Object> productDataMap = Map.of(headers.get(0), product.getId(), headers.get(1), product.getName(), headers.get(2), product.getQuantity(), headers.get(3), product.getCriticalQuantity(), headers.get(4), product.getPricePerItem());
				rows.add(productDataMap);
			}
			model.addAttribute("headers", headers);
			model.addAttribute("rows", rows);
		}
		return "products";
	}


	//deleting products
	@RequestMapping(value = "/products/{productId}", method = RequestMethod.DELETE)
	public String deleteProducts(@PathVariable String productId, Model model, HttpServletResponse response)
	{
		if(productRepository.count() > 0)
		{
			Long parsedId;
			try
			{
				parsedId = Long.parseLong(productId);
				boolean isIdReal = productRepository.existsById(parsedId);
				if(isIdReal)
				{
					productRepository.deleteById(parsedId);
					response.setStatus(HttpServletResponse.SC_OK);
					model.addAttribute("info", "The product with ID " + productId + " has been deleted successfully.");
					model.addAttribute("returnTo", "/products");
					return "productSuccess";
				}
				else
				{
					throw new NoResultException();
				}
			}
			catch(NumberFormatException | NoResultException e)
			{
				response.setStatus(HttpServletResponse.SC_NOT_FOUND);
				model.addAttribute("error", "Cannot delete product: invalid ID supplied.");
				model.addAttribute("returnTo", "/products");
				return "error";
			}
			catch(Exception e)
			{
				response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				model.addAttribute("error", "Cannot delete product: an error has occurred.");
				model.addAttribute("returnTo", "/products");
				return "error";
			}
		}
		else
		{
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			model.addAttribute("error", "Cannot delete product: no products available.");
			model.addAttribute("returnTo", "/products");
			return "error";
		}
	}
}