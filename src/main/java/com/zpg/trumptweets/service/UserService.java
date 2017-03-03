package com.zpg.trumptweets.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.zpg.trumptweets.dao.Donation_logDAO;
import com.zpg.trumptweets.dao.TweetlogDAO;
import com.zpg.trumptweets.dao.User_balancesDAO;
import com.zpg.trumptweets.dao.User_preferencesDAO;
import com.zpg.trumptweets.dao.User_tweet_logDAO;
import com.zpg.trumptweets.domain.Category;
import com.zpg.trumptweets.domain.Donation_log;
import com.zpg.trumptweets.domain.Tweetlog;
import com.zpg.trumptweets.domain.User;
import com.zpg.trumptweets.domain.User_balances;
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
	
	@Autowired
	private Donation_logDAO donationLogDAO;
	
	@Autowired
	private User_balancesDAO userBalancesDAO;
	
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
			
			if(user.getBalances() == null){
				Set<User_balances> newBalances = new HashSet<User_balances>();
				user.setBalances(newBalances);
				
			}
			
			for(Category cat: tweet.getCategories()){
				boolean matched = false;
				for(User_balances balance: user.getBalances()){
					if(balance.getCategory().equals(cat)){
						BigDecimal newTotal;
						if(balance.getBalance() != null){
							newTotal = balance.getBalance().add(user.getTweetLimit());
						}else{
							newTotal = user.getTweetLimit();
						}
						 
						if(newTotal.compareTo(user.getTransferThreshold()) >= 0){
							Donation_log donation = new Donation_log();
							donation.setAmount(user.getTransferThreshold());
							donation.setCategory(cat);
							donation.setUser(user);
							donation.setProcessed(false);
							donationLogDAO.saveDonation_log(donation);
							log.info("User: "+ user.getLogin() + " has reached the transfer threshold for category: " + cat.getName() + ", creating new donation record.");
							newTotal = newTotal.subtract(user.getTransferThreshold());
						}
						balance.setBalance(newTotal);
						matched = true;
					}
				}
				if(!matched){
					log.info("Creating new User_balance for user: " + user.getLogin() + " and category: "+ cat.getName());
					User_balances newBalance = new User_balances();
					newBalance.setUser(user);
					newBalance.setCategory(cat);
					newBalance.setBalance(user.getTweetLimit());
					user.getBalances().add(newBalance);
				}
			}
			
			userBalancesDAO.updateUserBalances(user.getBalances());
			
		}
		
	}
	
	
}
