package com.zpg.trumptweets.service;

import javax.inject.Named;

import org.apache.camel.Body;
import org.apache.camel.Exchange;
import org.apache.camel.Header;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.zpg.trumptweets.dao.User_tweet_logDAO;
import com.zpg.trumptweets.domain.Tweetlog;
import com.zpg.trumptweets.domain.User;
import com.zpg.trumptweets.domain.User_tweet_log;

@Named
public class UserTweetLogService {
	
	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private User_tweet_logDAO userTweetLogDAO;

	public void createTweetLogs(@Header(value="user") User user, @Body Tweetlog tweetLog, Exchange exchange){
		log.info("Creating user tweet log for User: "+ user.getLogin() + ", Tweetlog: " + tweetLog);
		User_tweet_log userTweetLog = new User_tweet_log();
		userTweetLog.setUser(user);
		userTweetLog.setTweet(tweetLog);
		userTweetLog.setCharge(user.getTweetLimit());
		userTweetLogDAO.addUser_tweet_log(userTweetLog);
	}
}
