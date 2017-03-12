package com.zpg.trumptweets.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.inject.Named;

import org.apache.camel.Body;
import org.apache.camel.Exchange;
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
	
	/**
	 * 
	 * @param donation
	 * @return
	 */
	public boolean filterMonthlyLimit(@Body Donation_log donation){
		
		List<Donation_log> existingDonationsForMonth = donationLogDAO.getUserProcessedDonationsForMonth(new Date(),donation.getUser());
		
		BigDecimal alreadyDonated = new BigDecimal(0);
		
		for(Donation_log donated: existingDonationsForMonth){
			alreadyDonated.add(donated.getAmount());
		}
		
		log.info("User: "+ donation.getUser() + " already donated: "+ alreadyDonated+" this month.");
		
		if(alreadyDonated.add(donation.getAmount()).compareTo(donation.getUser().getMonthlyLimit()) > 0){
			log.info("Filtering donation because it will exceed monthly limit! "+ donation);
			return false;
		}else{
			return true;
		}
		
	}
	
	public void processDonation(@Header(value = "donation") Donation_log donation){
		donationLogDAO.processDonation(donation);
	}
}
