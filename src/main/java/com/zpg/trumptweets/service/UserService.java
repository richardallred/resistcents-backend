package com.zpg.trumptweets.service;

import java.util.HashSet;
import java.util.Set;

import javax.inject.Named;

import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.zpg.trumptweets.dao.User_preferencesDAO;
import com.zpg.trumptweets.domain.Category;
import com.zpg.trumptweets.domain.Tweetlog;
import com.zpg.trumptweets.domain.User;

@Named
public class UserService {
	
	Logger log = LoggerFactory.getLogger(UserService.class);
	
	@Autowired
	private User_preferencesDAO userPreferencesDAO;
	
	public void findUsers(Tweetlog body, Exchange exchange){
		Set<User> usersForCat = new HashSet<User>();
		for(Category cat: body.getCategories()){
			log.info("Finding users for category: " + cat);
			usersForCat.addAll(userPreferencesDAO.findUsersForCategory(cat));
		}
		
		log.info("Users found:" +usersForCat);
		exchange.getIn().setHeader("userList", usersForCat);
		
	}
	
}
