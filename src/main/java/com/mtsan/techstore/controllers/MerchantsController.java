package com.mtsan.techstore.controllers;

import com.mtsan.techstore.ErrorPage;
import com.mtsan.techstore.Rank;
import com.mtsan.techstore.entities.User;
import com.mtsan.techstore.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.persistence.NoResultException;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class MerchantsController {
	@Autowired
	private UserRepository userRepository;

	PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	//fetching a list of all merchants
	@RequestMapping(value = "/merchants", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> merchants() {
		List<User> merchants = userRepository.getUsersByRank(Rank.Merchant);
		if (merchants.size() > 0) {
			String[] headers = {"User ID", "Username", "Display Name"};
			ArrayList<ArrayList<Object>> rows = new ArrayList<>();
			for (User merchant : merchants) {
				ArrayList<Object> productData = new ArrayList<>();
				productData.add(merchant.getId());
				productData.add(merchant.getUsername());
				productData.add(merchant.getDisplayName());
				rows.add(productData);
			}

			Map<String, Object> result = new HashMap<>();
			result.put("code", HttpServletResponse.SC_OK);
			result.put("message", "Merchants listing available");
			result.put("headers", headers);
			result.put("rows", rows);
			return ResponseEntity.ok(result);
		} else {
			return ErrorPage.generateErrorPage(HttpStatus.NOT_FOUND, HttpServletResponse.SC_NOT_FOUND, "No merchants found");
		}
	}

	//adding a merchant
	@RequestMapping(value = "/merchants", consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> addMerchant(@RequestBody User postedMerchant) {
		try {
			postedMerchant.setPassword(passwordEncoder.encode(postedMerchant.getPassword()));
			postedMerchant.setRank(Rank.Merchant);
			userRepository.save(postedMerchant);

			List<User> merchants = userRepository.getUsersByRank(Rank.Merchant);
			String[] headers = {"User ID", "Username", "Display Name"};
			ArrayList<ArrayList<Object>> rows = new ArrayList<>();
			for (User merchant : merchants) {
				ArrayList<Object> productData = new ArrayList<>();
				productData.add(merchant.getId());
				productData.add(merchant.getUsername());
				productData.add(merchant.getDisplayName());
				rows.add(productData);
			}

			Map<String, Object> result = new HashMap<>();
			result.put("code", HttpServletResponse.SC_CREATED);
			result.put("message", "Merchant created successfully");
			result.put("headers", headers);
			result.put("rows", rows);
			return new ResponseEntity<>(result, HttpStatus.CREATED);
		}
		catch (DataIntegrityViolationException e) {
			return ErrorPage.generateErrorPage(HttpStatus.NOT_FOUND, HttpServletResponse.SC_NOT_FOUND, "Cannot add merchant: the username already exists.");
		}
		catch (Exception e) {
			return ErrorPage.generateErrorPage(HttpStatus.NOT_FOUND, HttpServletResponse.SC_NOT_FOUND, "Cannot add merchant: an error has occurred when trying to save the data.");
		}
	}

	//fetching a merchant
	@RequestMapping(value = "/merchants/{merchantId}", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> getMerchant(@PathVariable String merchantId) {
		if (userRepository.getUsersByRank(Rank.Merchant).size() > 0) {
			Long parsedId;
			try {
				parsedId = Long.parseLong(merchantId);
				boolean isIdReal = userRepository.existsById(parsedId);
				if (isIdReal) {
					User merchant = userRepository.findById(parsedId).get();
					merchant.setPassword("");
					Map<String, Object> result = new HashMap<>();
					result.put("code", HttpServletResponse.SC_OK);
					result.put("message", "Merchant found");
					result.put("merchant", merchant);
					return new ResponseEntity<>(result, HttpStatus.OK);
				} else {
					throw new NoResultException();
				}
			}
			catch (NumberFormatException | NoResultException e) {
				return ErrorPage.generateErrorPage(HttpStatus.NOT_FOUND, HttpServletResponse.SC_NOT_FOUND, "Cannot fetch merchant data: invalid ID supplied.");
			}
			catch (Exception e) {
				return ErrorPage.generateErrorPage(HttpStatus.NOT_FOUND, HttpServletResponse.SC_NOT_FOUND, "Cannot fetch merchant data: an error has occurred during the fetching process.");
			}
		} else {
			return ErrorPage.generateErrorPage(HttpStatus.NOT_FOUND, HttpServletResponse.SC_NOT_FOUND, "Cannot fetch merchant data: no merchants available.");
		}
	}
}
