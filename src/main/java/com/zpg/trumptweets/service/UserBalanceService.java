package com.zpg.trumptweets.service;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import javax.inject.Named;

import org.apache.camel.Body;
import org.apache.camel.Exchange;
import org.apache.camel.Header;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.zpg.trumptweets.dao.Donation_logDAO;
import com.zpg.trumptweets.dao.User_balancesDAO;
import com.zpg.trumptweets.domain.Category;
import com.zpg.trumptweets.domain.Tweetlog;
import com.zpg.trumptweets.domain.User;
import com.zpg.trumptweets.domain.User_balances;

@Named
public class UserBalanceService {
	
	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private Donation_logDAO donationLogDAO;
	
	@Autowired
	private User_balancesDAO userBalancesDAO;

	public void createOrUpdateUserBalances(@Header(value="user") User user, @Body Tweetlog tweetLog, Exchange exchange){
		
		//If this user doesn't have any balances yet, create a new set for them
		if(user.getBalances() == null){
			Set<User_balances> newBalances = new HashSet<User_balances>();
			user.setBalances(newBalances);
			
		}
		
		//Update the user balance for each category assigned to the tweet
		for(Category cat: tweetLog.getCategories()){
			boolean matched = false;
			for(User_balances balance: user.getBalances()){
				if(balance.getCategory().equals(cat)){
					BigDecimal newTotal;
					if(balance.getBalance() != null){
						newTotal = balance.getBalance().add(user.getTweetLimit());
					}else{
						newTotal = user.getTweetLimit();
					}
					
					//If total is over the transfer threshold, reset balance and create donation log
					if(newTotal.compareTo(user.getTransferThreshold()) >= 0){
						log.info("User: "+ user.getLogin() + " has reached the transfer threshold for category: " + cat.getName() + ", creating new donation record.");
						exchange.getIn().setHeader("donationLogCat", cat);
						newTotal = newTotal.subtract(user.getTransferThreshold());
					}
					log.info("Updating user balance for User: "+ user.getLogin() + ", category: " + cat.getName() + ", new balance: "+ newTotal);
					balance.setBalance(newTotal);
					matched = true;
				}
			}
			
			//If we don't match, then we need to create a new user balance for this category
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
