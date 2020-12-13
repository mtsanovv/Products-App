package com.mtsan.techstore.controllers;

import com.mtsan.techstore.entities.Product;
import com.mtsan.techstore.exceptions.TechstoreDataException;
import com.mtsan.techstore.models.TweetModel;
import com.mtsan.techstore.repositories.ProductRepository;
import com.mtsan.techstore.services.TwitterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import twitter4j.Status;
import twitter4j.TwitterException;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RequestMapping("/products")
@RestController
public class ProductsController {

	private final ProductRepository productRepository;

	private final TwitterService twitterService;

	@Autowired
	public ProductsController(ProductRepository productRepository, TwitterService twitterService) {
		this.productRepository = productRepository;
		this.twitterService = twitterService;
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

	//posting a promotional offer about a product
	@RequestMapping(value = "/{productId}/tweet", method = RequestMethod.POST)
	public ResponseEntity tweet(@PathVariable Long productId) throws TechstoreDataException {
		if (productRepository.count() > 0) {
			boolean isIdReal = productRepository.existsById(productId);
			if (isIdReal) {
				try {
					Product product = productRepository.getOne(productId);
					final int tweetMaxLength = 280;
					final int offset = 3; //offset created due to misconceptions between Twitter counting characters and Java counting characters in a String
					//current offset created by emojis
					final String[] tweetElements = {"❗❗❗\uD83D\uDD25 SPECIAL OFFER! ", " now ONLY FOR $", " per item. \uD83C\uDF1F #TechStoreOffer"};

					String productName = product.getName();
					String productPrice = product.getPricePerItem().setScale(2, RoundingMode.HALF_EVEN).toString();

					final int tweetMinLength = String.join("", tweetElements).length() + productPrice.length();

					if(tweetMinLength + productName.length() + offset > tweetMaxLength) {
						productName = productName.substring(0, tweetMaxLength - tweetMinLength - offset);
					}

					StringBuilder tweet = new StringBuilder();
					tweet
							.append(tweetElements[0])
							.append(productName)
							.append(tweetElements[1])
							.append(productPrice)
							.append(tweetElements[2]);

					Status status = twitterService.postTweet(tweet.toString());
					return ResponseEntity.status(HttpStatus.OK).body(status);

				} catch(TwitterException e) {
					throw new TechstoreDataException(HttpServletResponse.SC_NOT_FOUND, "Cannot post tweet (status): " + e.getErrorMessage() + " (code " + e.getErrorCode() + ")");
				}
			} else {
				throw new TechstoreDataException(HttpServletResponse.SC_NOT_FOUND, "Product not found");
			}
		} else {
			throw new TechstoreDataException(HttpServletResponse.SC_NOT_FOUND, "No products found");
		}
	}
}