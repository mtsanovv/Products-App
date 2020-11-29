package com.mtsan.techstore.controllers;

import com.mtsan.techstore.ErrorPage;
import com.mtsan.techstore.Rank;
import com.mtsan.techstore.entities.User;
import com.mtsan.techstore.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class MerchantsController {

	@Autowired
	private UserRepository userRepository;

	//fetching a list of all merchants
	@RequestMapping(value = "/merchants", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> products() {
		List<User> merchants = userRepository.getUserByRank(Rank.Merchant.toString());
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
}
