package com.zpg.trumptweets.dao;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.zpg.trumptweets.domain.User_tweet_log;

@Repository("User_tweet_logDAO")
public class User_tweet_logDAOImpl implements User_tweet_logDAO {
	
	@Autowired
	private EntityManager entityManager;

	@Override
	@Transactional
	public void addUser_tweet_log(User_tweet_log tweetLog) {
		entityManager.persist(tweetLog);
		entityManager.flush();
	}

}
