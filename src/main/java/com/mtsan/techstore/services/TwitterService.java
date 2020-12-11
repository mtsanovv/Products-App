package com.mtsan.techstore.services;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.scheduling.annotation.Async;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;

public class TwitterService {

	private final Twitter twitterInstance;

	public TwitterService(Twitter twitterInstance) {
		this.twitterInstance = twitterInstance;
	}

	public Status postTweet(String tweet) throws TwitterException {
		return twitterInstance.updateStatus(tweet);
	}
}
