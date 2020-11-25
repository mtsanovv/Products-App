package com.mtsan.techstore.controllers;


import com.mtsan.techstore.entities.Products;
import com.mtsan.techstore.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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
			String[] headers = {"ID", "Name", "Quantity", "Critical quantity", "Price per item (BGN)"};
			ArrayList<Map<String, Object>> rows = new ArrayList<>();
			for (Products product : allProducts)
			{
				Map<String, Object> productDataMap = Map.of(headers[0], product.getId(), headers[1], product.getName(), headers[2], product.getQuantity(), headers[3], product.getCriticalQuantity(), headers[4], product.getPricePerItem());
				rows.add(productDataMap);
			}
			model.addAttribute("headers", headers);
			model.addAttribute("rows", rows);
		}
		return "products";
	}

	//adding a product - getting the interface
	@RequestMapping(value = "/products/add", method = RequestMethod.GET)
	public String getProductAdditionInterface(Model model, HttpServletResponse response)
	{
		String[] headers = {"Name", "Quantity", "Critical quantity", "Price per item (BGN)"};
		Map<String, String[]> row = new HashMap<>();
		row.put(headers[0], new String[]{"^.{1,}$", "name"});
		row.put(headers[1], new String[]{"^\\d{1,}$", "quantity"});
		row.put(headers[2], new String[]{"^\\d{1,}$", "criticalQuantity"});
		row.put(headers[3], new String[]{"^[0-9]{1,10}\\.[0-9]{1,5}$", "pricePerItem"});
		model.addAttribute("headers", headers);
		model.addAttribute("row", row);
		model.addAttribute("product", new Products());
		return "addProduct";
	}

	//adding the actual product
	@RequestMapping(value = "/products/add", method = RequestMethod.POST)
	public String addProduct(@ModelAttribute Products product, Model model, HttpServletResponse response)
	{
		try
		{
			productRepository.save(product);
			response.setStatus(HttpServletResponse.SC_CREATED);
			return products(model, response);
		}
		catch(Exception e)
		{
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			model.addAttribute("status", response.getStatus());
			model.addAttribute("error", "Cannot add product: an error has occurred when trying to save the data.");
			model.addAttribute("returnTo", "/products/add");
			return "error";
		}
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
					return products(model, response);
				}
				else
				{
					throw new NoResultException();
				}
			}
			catch(NumberFormatException | NoResultException e)
			{
				response.setStatus(HttpServletResponse.SC_NOT_FOUND);
				model.addAttribute("status", response.getStatus());
				model.addAttribute("error", "Cannot delete product: invalid ID supplied.");
				model.addAttribute("returnTo", "/products");
				return "error";
			}
			catch(Exception e)
			{
				response.setStatus(HttpServletResponse.SC_NOT_FOUND);
				model.addAttribute("status", response.getStatus());
				model.addAttribute("error", "Cannot delete product: an error has occurred during the deletion process.");
				model.addAttribute("returnTo", "/products");
				return "error";
			}
		}
		else
		{
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			model.addAttribute("status", response.getStatus());
			model.addAttribute("error", "Cannot delete product: no products available.");
			model.addAttribute("returnTo", "/products");
			return "error";
		}
	}
}