package com.mtsan.techstore.controllers;


import com.mtsan.techstore.ErrorPage;
import com.mtsan.techstore.entities.Products;
import com.mtsan.techstore.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.persistence.NoResultException;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@RestController
public class ProductsController {

	@Autowired
	private ProductRepository productRepository;

	//fetching a list of all products
	@RequestMapping(value = "/products", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> products() {
		if (productRepository.count() > 0) {
			Iterable<Products> allProducts = productRepository.findAll();
			String[] headers = {"ID", "Name", "Quantity", "Critical quantity", "Price per item (BGN)"};
			ArrayList<Map<String, Object>> rows = new ArrayList<>();
			for (Products product : allProducts) {
				Map<String, Object> productDataMap = Map.of(headers[0], product.getId(), headers[1], product.getName(), headers[2], product.getQuantity(), headers[3], product.getCriticalQuantity(), headers[4], product.getPricePerItem());
				rows.add(productDataMap);
			}

			Map<String, Object> result = new HashMap<>();
			result.put("headers", headers);
			result.put("rows", rows);
			result.put("code", HttpServletResponse.SC_OK);
			result.put("message", "Products listing available");
			return ResponseEntity.ok(result);
		} else {
			return ErrorPage.generateErrorPage(HttpStatus.NOT_FOUND, HttpServletResponse.SC_NOT_FOUND, "No products found");
		}
	}

	//adding a product
	@RequestMapping(value = "/products", method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> addProduct(@ModelAttribute Products postedProduct) {
		try {

			productRepository.save(postedProduct);

			Iterable<Products> allProducts = productRepository.findAll();
			String[] headers = {"ID", "Name", "Quantity", "Critical quantity", "Price per item (BGN)"};
			ArrayList<Map<String, Object>> rows = new ArrayList<>();
			for (Products product : allProducts) {
				Map<String, Object> productDataMap = Map.of(headers[0], product.getId(), headers[1], product.getName(), headers[2], product.getQuantity(), headers[3], product.getCriticalQuantity(), headers[4], product.getPricePerItem());
				rows.add(productDataMap);
			}

			Map<String, Object> result = new HashMap<>();
			result.put("headers", headers);
			result.put("rows", rows);
			result.put("code", HttpServletResponse.SC_CREATED);
			result.put("message", "Product created successfully");
			return new ResponseEntity<>(result, HttpStatus.CREATED);
		}
		catch (Exception e) {
			return ErrorPage.generateErrorPage(HttpStatus.NOT_FOUND, HttpServletResponse.SC_NOT_FOUND, "Cannot add product: an error has occurred when trying to save the data.");
		}
	}

	//deleting a product
	@RequestMapping(value = "/products/{productId}", method = RequestMethod.DELETE)
	public ResponseEntity<Map<String, Object>> deleteProduct(@PathVariable String productId) {
		if (productRepository.count() > 0) {
			Long parsedId;
			try {
				parsedId = Long.parseLong(productId);
				boolean isIdReal = productRepository.existsById(parsedId);
				if (isIdReal) {
					productRepository.deleteById(parsedId);
					return products();
				} else {
					throw new NoResultException();
				}
			}
			catch (NumberFormatException | NoResultException e) {
				return ErrorPage.generateErrorPage(HttpStatus.NOT_FOUND, HttpServletResponse.SC_NOT_FOUND, "Cannot delete product: invalid ID supplied.");
			}
			catch (Exception e) {
				return ErrorPage.generateErrorPage(HttpStatus.NOT_FOUND, HttpServletResponse.SC_NOT_FOUND, "Cannot delete product: an error has occurred during the deletion process.");
			}
		} else {
			return ErrorPage.generateErrorPage(HttpStatus.NOT_FOUND, HttpServletResponse.SC_NOT_FOUND, "Cannot delete product: no products available.");
		}
	}

	//editing a product
	@RequestMapping(value = "/products/{productId}", consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.PUT)
	public ResponseEntity<Map<String, Object>> editProduct(@RequestBody Products newProduct, @PathVariable String productId, Model model, HttpServletResponse response) {
		if (productRepository.count() > 0) {
			Long parsedId;
			try {
				parsedId = Long.parseLong(productId);
				boolean isIdReal = productRepository.existsById(parsedId);
				if (isIdReal) {
					newProduct.setId(parsedId);
					productRepository.save(newProduct);
					return products();
				} else {
					throw new NoResultException();
				}
			}
			catch (NumberFormatException | NoResultException e) {
				return ErrorPage.generateErrorPage(HttpStatus.NOT_FOUND, HttpServletResponse.SC_NOT_FOUND, "Cannot edit product: invalid ID supplied.");
			}
			catch (Exception e) {
				return ErrorPage.generateErrorPage(HttpStatus.NOT_FOUND, HttpServletResponse.SC_NOT_FOUND, "Cannot edit product: an error has occurred during the update process.");
			}
		} else {
			return ErrorPage.generateErrorPage(HttpStatus.NOT_FOUND, HttpServletResponse.SC_NOT_FOUND, "Cannot edit product: no products available.");
		}
	}
}