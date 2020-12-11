package com.mtsan.techstore.configs;

import com.mtsan.techstore.services.TwitterService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

@Configuration
public class TwitterConfig {

	@Bean
	public TwitterService instantiateTwitterService(@Value("${twitter4j.oauth.consumerKey}") String twitterConsumerKey, @Value("${twitter4j.oauth.consumerSecret}") String twitterConsumerSecret, @Value("${twitter4j.oauth.accessToken}") String twitterAccessToken, @Value("${twitter4j.oauth.accessTokenSecret}") String twitterAccessTokenSecret) {
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb
			.setOAuthConsumerKey(twitterConsumerKey)
			.setOAuthConsumerSecret(twitterConsumerSecret)
			.setOAuthAccessToken(twitterAccessToken)
			.setOAuthAccessTokenSecret(twitterAccessTokenSecret);
		TwitterFactory tf = new TwitterFactory(cb.build());
		Twitter twitter = tf.getInstance();
		return new TwitterService(twitter);
	}
}
