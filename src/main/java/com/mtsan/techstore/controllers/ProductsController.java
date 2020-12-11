package com.mtsan.techstore.controllers;

import com.mtsan.techstore.entities.Product;
import com.mtsan.techstore.exceptions.TechstoreDataException;
import com.mtsan.techstore.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RequestMapping("/products")
@RestController
public class ProductsController {

	private final ProductRepository productRepository;

	@Autowired
	public ProductsController(ProductRepository productRepository) {
		this.productRepository = productRepository;
	}

	//fetching a list of all products
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity products() throws TechstoreDataException {
		if (productRepository.count() > 0) {
			return ResponseEntity.status(HttpStatus.OK).body(productRepository.findAll());
		} else {
			throw new TechstoreDataException(HttpServletResponse.SC_NOT_FOUND, "No products found");
		}
	}

	//adding a product
	@RequestMapping(consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
	public ResponseEntity addProduct(@RequestBody Product postedProduct) throws TechstoreDataException {
		Matcher productNameMatcher = Pattern.compile(Product.namePattern).matcher(postedProduct.getName());
		if(!productNameMatcher.matches()) {
			throw new TechstoreDataException(HttpServletResponse.SC_BAD_REQUEST, "Product name must be between 1 and 1024 characters.");
		}

		Product savedProduct = productRepository.save(postedProduct);

		return ResponseEntity.status(HttpStatus.CREATED).body(savedProduct);
	}

	//fetching a product
	@RequestMapping(value = "/{productId}", method = RequestMethod.GET)
	public ResponseEntity getProduct(@PathVariable Long productId) throws TechstoreDataException {
		if (productRepository.count() > 0) {
			boolean isIdReal = productRepository.existsById(productId);
			if (isIdReal) {
				return ResponseEntity.status(HttpStatus.OK).body(productRepository.findById(productId).get());
			} else {
				throw new TechstoreDataException(HttpServletResponse.SC_NOT_FOUND, "Product not found");
			}
		} else {
			throw new TechstoreDataException(HttpServletResponse.SC_NOT_FOUND, "No products found");
		}
	}

	//editing a product
	@RequestMapping(value = "/{productId}", consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.PUT)
	public ResponseEntity editProduct(@RequestBody Product newProduct, @PathVariable Long productId) throws TechstoreDataException {
		if (productRepository.count() > 0) {
			boolean isIdReal = productRepository.existsById(productId);
			if (isIdReal) {
				Matcher productNameMatcher = Pattern.compile(Product.namePattern).matcher(newProduct.getName());
				if(!productNameMatcher.matches()) {
					throw new TechstoreDataException(HttpServletResponse.SC_BAD_REQUEST, "Product name must be between 1 and 1024 characters.");
				}

				newProduct.setId(productId);
				Product savedProduct = productRepository.save(newProduct);
				return ResponseEntity.status(HttpStatus.OK).body(savedProduct);
			} else {
				throw new TechstoreDataException(HttpServletResponse.SC_NOT_FOUND, "Product not found");
			}
		} else {
			throw new TechstoreDataException(HttpServletResponse.SC_NOT_FOUND, "No products found");
		}
	}

	//deleting a product
	@RequestMapping(value = "/{productId}", method = RequestMethod.DELETE)
	public ResponseEntity deleteProduct(@PathVariable Long productId) throws TechstoreDataException {
		if (productRepository.count() > 0) {
			boolean isIdReal = productRepository.existsById(productId);
			if (isIdReal) {
				productRepository.deleteById(productId);
				return ResponseEntity.status(HttpStatus.OK).build();
			} else {
				throw new TechstoreDataException(HttpServletResponse.SC_NOT_FOUND, "Product not found");
			}
		} else {
			throw new TechstoreDataException(HttpServletResponse.SC_NOT_FOUND, "No products found");
		}
	}
}