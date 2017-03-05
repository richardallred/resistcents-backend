package com.zpg.trumptweets.service;

import javax.inject.Named;

import org.apache.camel.Header;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.zpg.trumptweets.dao.Donation_logDAO;
import com.zpg.trumptweets.domain.Category;
import com.zpg.trumptweets.domain.Donation_log;
import com.zpg.trumptweets.domain.User;

@Named
public class DonationLogService {
	
	private Logger log = Logger.getLogger(this.getClass());
	
	@Autowired
	Donation_logDAO donationLogDAO;
	
	public void createDonationLog(@Header(value="donationLogCat") Category cat, @Header(value="user") User user){
		log.info("Creating Donation Log for User: "+ user.getLogin()+" Category: "+ cat.getName());
		Donation_log donation = new Donation_log();
		donation.setAmount(user.getTransferThreshold());
		donation.setCategory(cat);
		donation.setUser(user);
		donation.setProcessed(false);
		donationLogDAO.saveDonation_log(donation);
	}
}
