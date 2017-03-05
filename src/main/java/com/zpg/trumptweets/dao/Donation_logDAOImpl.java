package com.zpg.trumptweets.dao;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.zpg.trumptweets.domain.Donation_log;
import com.zpg.trumptweets.domain.User;

@Repository("Donation_logDAO")
public class Donation_logDAOImpl implements Donation_logDAO{

	@Autowired
	EntityManager entityManager;
	
	@Transactional
	@Override
	public void saveDonation_log(Donation_log donation) {
		
		entityManager.persist(donation);
		
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Donation_log> getUserProcessedDonationsForMonth(Date date,User user){
		Query findDonations = entityManager.createNamedQuery("getProcessedTransactionsForMonthByUser");
		
		//Query expects string of YYYY/MM so create that from Date w/ Calendar
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int month = cal.get(Calendar.MONTH);
		int year = cal.get(Calendar.YEAR);		
		String yearMonth = year + "/" + month;
		
		findDonations.setParameter("yearMonth", yearMonth);
		findDonations.setParameter("user", user);
		
		return findDonations.getResultList();
		
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Donation_log> getUnprocessedDonations(){
		Query findDonations = entityManager.createNamedQuery("getUnprocessedTransactions");
		
		return findDonations.getResultList();
	}

	@Transactional
	@Override
	public void processDonation(Donation_log donation) {
		
		donation.setProcessed(true);
		entityManager.merge(donation);
		
	}

	
	
}
