package com.mtsan.techstore.controllers;


import com.mtsan.techstore.entities.Products;
import com.mtsan.techstore.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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

	@RequestMapping(value = "/products", method = RequestMethod.GET)
	public String products(Model model, HttpServletResponse response)
	{
		if(productRepository.count() > 0)
		{
			Iterable<Products> allProducts = productRepository.findAll();
			ArrayList<String> headers = new ArrayList<>(Arrays.asList("ID", "Name", "Quantity", "Price per item"));
			ArrayList<Map<String, Object>> rows = new ArrayList<>();
			for (Products product : allProducts)
			{
				Map<String, Object> productDataMap = Map.of(headers.get(0), product.getId(), headers.get(1), product.getName(), headers.get(2), product.getQuantity(), headers.get(3), product.getPricePerItem());
				rows.add(productDataMap);
			}
			model.addAttribute("headers", headers);
			model.addAttribute("rows", rows);
		}
		else
		{
			response.setStatus(HttpServletResponse.SC_NO_CONTENT);
		}
		return "products";
	}

	@RequestMapping(value = "/products", method = RequestMethod.POST)
	public String addProduct(Model model, HttpServletResponse response)
	{
		if(productRepository.count() > 0)
		{
			Iterable<Products> allProducts = productRepository.findAll();
			ArrayList<String> headers = new ArrayList<>(Arrays.asList("ID", "Name", "Quantity", "Price per item"));
			ArrayList<Map<String, Object>> rows = new ArrayList<>();
			for (Products product : allProducts)
			{
				Map<String, Object> productDataMap = Map.of(headers.get(0), product.getId(), headers.get(1), product.getName(), headers.get(2), product.getQuantity(), headers.get(3), product.getPricePerItem());
				rows.add(productDataMap);
			}
			model.addAttribute("headers", headers);
			model.addAttribute("rows", rows);
		}
		return "products";
	}
}