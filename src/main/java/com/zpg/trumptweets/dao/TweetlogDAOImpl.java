package com.zpg.trumptweets.dao;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.zpg.trumptweets.domain.Tweetlog;

@Repository("TweetlogDAO")
public class TweetlogDAOImpl implements TweetlogDAO{
	
	@Autowired
	EntityManager entityManager;

	@Override
	@Transactional
	public void updateTweetLog(Tweetlog tweet) {
		entityManager.merge(tweet);
	}

}
