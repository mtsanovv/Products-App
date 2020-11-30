package com.mtsan.techstore.controllers;

import com.mtsan.techstore.Rank;
import com.mtsan.techstore.entities.User;
import com.mtsan.techstore.exceptions.TechstoreDataException;
import com.mtsan.techstore.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@RestController
public class MerchantsController {

	@Autowired
	private UserRepository userRepository;

	PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	//fetching a list of all merchants
	@RequestMapping(value = "/merchants", method = RequestMethod.GET)
	public ResponseEntity merchants() throws TechstoreDataException {
		List<User> merchants = userRepository.getUsersByRank(Rank.Merchant);
		if (merchants.size() > 0) {
			ArrayList<ArrayList<Object>> merchantsData = new ArrayList<>();
			for (User merchant : merchants) {
				ArrayList<Object> productData = new ArrayList<>();
				productData.add(merchant.getId());
				productData.add(merchant.getUsername());
				productData.add(merchant.getDisplayName());
				merchantsData.add(productData);
			}

			return ResponseEntity.status(HttpStatus.OK).body(merchantsData);
		} else {
			throw new TechstoreDataException(HttpServletResponse.SC_NOT_FOUND, "No merchants found");
		}
	}

	//adding a merchant
	@RequestMapping(value = "/merchants", consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
	public ResponseEntity addMerchant(@RequestBody User postedMerchant) throws TechstoreDataException {
		try {
			postedMerchant.setPassword(passwordEncoder.encode(postedMerchant.getPassword()));
			postedMerchant.setRank(Rank.Merchant);
			User savedMerchant = userRepository.save(postedMerchant);

			return ResponseEntity.status(HttpStatus.CREATED).body(savedMerchant);
		}
		catch (DataIntegrityViolationException e) {
			throw new TechstoreDataException(HttpServletResponse.SC_NOT_FOUND, "This username is already in use, please use another one.");
		}
	}

	//fetching a merchant
	@RequestMapping(value = "/merchants/{merchantId}", method = RequestMethod.GET)
	public ResponseEntity getMerchant(@PathVariable Long merchantId) throws TechstoreDataException {
		if (userRepository.getUsersByRank(Rank.Merchant).size() > 0) {
			boolean isIdReal = userRepository.existsById(merchantId);
			if (isIdReal) {
				User merchant = userRepository.findById(merchantId).get();
				merchant.setPassword("");
				return ResponseEntity.status(HttpStatus.OK).body(merchant);
			} else {
				throw new TechstoreDataException(HttpServletResponse.SC_NOT_FOUND, "Merchant not found");
			}
		} else {
			throw new TechstoreDataException(HttpServletResponse.SC_NOT_FOUND, "No merchants found");
		}
	}
}
