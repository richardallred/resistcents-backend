package com.zpg.trumptweets.dao;

import java.util.Date;
import java.util.List;

import com.zpg.trumptweets.domain.Donation_log;
import com.zpg.trumptweets.domain.User;

public interface Donation_logDAO {
	
	public void saveDonation_log(Donation_log donation);
	
	public List<Donation_log> getUserProcessedDonationsForMonth(Date date, User user);
	
	public List<Donation_log> getUnprocessedDonations();
	
	public void processDonation(Donation_log donation);
	
}
