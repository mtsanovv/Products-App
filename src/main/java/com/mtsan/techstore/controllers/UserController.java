package com.mtsan.techstore.controllers;

import com.mtsan.techstore.entities.User;
import com.mtsan.techstore.exceptions.TechstoreDataException;
import com.mtsan.techstore.models.TweetModel;
import com.mtsan.techstore.repositories.UserRepository;
import com.mtsan.techstore.services.TwitterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import twitter4j.Status;
import twitter4j.TwitterException;

import javax.servlet.http.HttpServletResponse;

@RequestMapping("/user")
@RestController
public class UserController {

	private final UserRepository userRepository;

	private final TwitterService twitterService;

	@Autowired
	public UserController(UserRepository userRepository, TwitterService twitterService) {
		this.userRepository = userRepository;
		this.twitterService = twitterService;
	}

	//fetching data about the currently authenticated user or login
	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST })
	public ResponseEntity user(Authentication authentication) {
		User user = userRepository.getUserByUsername(authentication.getName()).get(0);
		user.setPassword(null);
		user.setClients(null);
		user.setSales(null);
		return ResponseEntity.status(HttpStatus.OK).body(user);
	}

	//fetching data about the currently authenticated user or login
	@RequestMapping(value = "/tweet", consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
	public ResponseEntity tweet(@RequestBody TweetModel postedTweet) throws TechstoreDataException {
		try {
			if(postedTweet.getText().length() > 0 && postedTweet.getText().length() <= 280) {
				Status status = twitterService.postTweet(postedTweet.getText());
				return ResponseEntity.status(HttpStatus.CREATED).body(status);
			}
			throw new TechstoreDataException(HttpServletResponse.SC_BAD_REQUEST, "The tweet should be between 1 and 280 characters long.");
		} catch(TwitterException e) {
			throw new TechstoreDataException(HttpServletResponse.SC_NOT_FOUND, "Cannot post tweet (status): " + e.getErrorMessage() + " (code " + e.getErrorCode() + ")");
		}
	}
}
