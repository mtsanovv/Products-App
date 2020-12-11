package com.mtsan.techstore.controllers;

import com.mtsan.techstore.entities.User;
import com.mtsan.techstore.exceptions.TechstoreDataException;
import com.mtsan.techstore.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/user")
@RestController
public class UserController {

	private final UserRepository userRepository;

	@Autowired
	public UserController(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	//fetching data about the currently authenticated user or login
	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST })
	public ResponseEntity user(Authentication authentication) throws TechstoreDataException {
		User user = userRepository.getUserByUsername(authentication.getName()).get(0);
		user.setPassword(null);
		user.setClients(null);
		user.setSales(null);
		return ResponseEntity.status(HttpStatus.OK).body(user);
	}
}
