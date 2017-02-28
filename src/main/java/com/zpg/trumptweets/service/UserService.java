package com.zpg.trumptweets.service;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.zpg.trumptweets.dao.TweetlogDAO;
import com.zpg.trumptweets.dao.User_preferencesDAO;
import com.zpg.trumptweets.dao.User_tweet_logDAO;
import com.zpg.trumptweets.domain.Category;
import com.zpg.trumptweets.domain.Tweetlog;
import com.zpg.trumptweets.domain.User;
import com.zpg.trumptweets.domain.User_tweet_log;

@Named
public class UserService {
	
	Logger log = LoggerFactory.getLogger(UserService.class);
	
	@Autowired
	private User_preferencesDAO userPreferencesDAO;
	
	@Autowired
	private User_tweet_logDAO userTweetLogDAO;
	
	@Autowired
	private TweetlogDAO tweetLogDAO;
	
	public void createTweetLogs(Tweetlog tweet){
		
		log.info("Creating tweet logs for tweet!");
		
		List<User> usersForCat = new ArrayList<User>();
		for(Category cat: tweet.getCategories()){
			log.info("Finding users for category: " + cat);
			usersForCat.addAll(userPreferencesDAO.findUsersForCategory(cat));
		}
		
		log.info("Users found:" +usersForCat);
		
		for(User user: usersForCat){
			User_tweet_log tweetLog = new User_tweet_log();
			tweetLog.setUser(user);
			tweetLog.setTweet(tweet);
			tweetLog.setCharge(user.getTweetLimit());
			userTweetLogDAO.addUser_tweet_log(tweetLog);
		}
		
		
	}
	
	
}
