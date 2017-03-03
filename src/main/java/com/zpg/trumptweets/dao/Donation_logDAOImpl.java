package com.zpg.trumptweets.dao;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.zpg.trumptweets.domain.Donation_log;

@Repository("Donation_logDAO")
public class Donation_logDAOImpl implements Donation_logDAO{

	@Autowired
	EntityManager entityManager;
	
	@Transactional
	@Override
	public void saveDonation_log(Donation_log donation) {
		
		entityManager.persist(donation);
		entityManager.flush();
		
	}

	
	
}
