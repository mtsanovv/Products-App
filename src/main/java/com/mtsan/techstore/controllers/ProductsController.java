package com.mtsan.techstore.controllers;


import com.mtsan.techstore.ErrorPage;
import com.mtsan.techstore.entities.Products;
import com.mtsan.techstore.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
		row.put(headers[0], new String[] { "^.{1,}$", "name" });
		row.put(headers[1], new String[] { "^\\d{1,}$", "quantity" });
		row.put(headers[2], new String[] { "^\\d{1,}$", "criticalQuantity" });
		row.put(headers[3], new String[] { "^[0-9]{1,10}\\.[0-9]{1,4}$", "pricePerItem" });
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
			return ErrorPage.generateErrorPage(model, response, HttpServletResponse.SC_NOT_FOUND, "Cannot add product: an error has occurred when trying to save the data.", "/products/add");
		}
	}

	//deleting products
	@RequestMapping(value = "/products/{productId}", method = RequestMethod.DELETE)
	public String deleteProduct(@PathVariable String productId, Model model, HttpServletResponse response)
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
				return ErrorPage.generateErrorPage(model, response, HttpServletResponse.SC_NOT_FOUND, "Cannot delete product: invalid ID supplied.", "/products");
			}
			catch(Exception e)
			{
				return ErrorPage.generateErrorPage(model, response, HttpServletResponse.SC_NOT_FOUND, "Cannot delete product: an error has occurred during the deletion process.", "/products");
			}
		}
		else
		{
			return ErrorPage.generateErrorPage(model, response, HttpServletResponse.SC_NOT_FOUND, "Cannot delete product: no products available.", "/products");
		}
	}

	//editing a product - getting the interface
	@RequestMapping(value = "/products/{productId}", method = RequestMethod.GET)
	public String getProductEditor(@PathVariable String productId, Model model, HttpServletResponse response)
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
					Products product = productRepository.findById(parsedId).get();
					String[] headers = {"Name", "Quantity", "Critical quantity", "Price per item (BGN)"};
					Map<String, Object[]> row = new HashMap<>();
					row.put(headers[0], new Object[] { "^.{1,}$", "name", product.getName() });
					row.put(headers[1], new Object[] { "^\\d{1,}$", "quantity", product.getQuantity() });
					row.put(headers[2], new Object[] { "^\\d{1,}$", "criticalQuantity", product.getCriticalQuantity() });
					row.put(headers[3], new Object[] { "^[0-9]{1,10}\\.[0-9]{1,4}$", "pricePerItem", product.getPricePerItem() });
					row.put("ID", new Object[] { parsedId });
					model.addAttribute("headers", headers);
					model.addAttribute("row", row);
					return "editProduct";
				}
				else
				{
					throw new NoResultException();
				}
			}
			catch(NumberFormatException | NoResultException e)
			{
				return ErrorPage.generateErrorPage(model, response, HttpServletResponse.SC_NOT_FOUND, "Cannot edit product: invalid ID supplied.", "/products");
			}
			catch(Exception e)
			{
				return ErrorPage.generateErrorPage(model, response, HttpServletResponse.SC_NOT_FOUND, "Cannot edit product: an error has occurred when fetching data.", "/products");
			}
		}
		else
		{
			return ErrorPage.generateErrorPage(model, response, HttpServletResponse.SC_NOT_FOUND, "Cannot edit product: no products available.", "/products");
		}
	}

	//edit a product
	@RequestMapping(value = "/products/{productId}", consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.PUT)
	public String editProduct(@RequestBody Products newProduct, @PathVariable String productId, Model model, HttpServletResponse response)
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
					newProduct.setId(parsedId);
					productRepository.save(newProduct);
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
				return ErrorPage.generateErrorPage(model, response, HttpServletResponse.SC_NOT_FOUND, "Cannot edit product: invalid ID supplied.", "/products");
			}
			catch(Exception e)
			{
				return ErrorPage.generateErrorPage(model, response, HttpServletResponse.SC_NOT_FOUND, "Cannot edit product: an error has occurred during the update process.", "/products");
			}
		}
		else
		{
			return ErrorPage.generateErrorPage(model, response, HttpServletResponse.SC_NOT_FOUND, "Cannot edit product: no products available.", "/products");
		}
	}
}