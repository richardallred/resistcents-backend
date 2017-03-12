package com.zpg.trumptweets.processors;

import java.util.List;
import java.util.Set;

import javax.inject.Named;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.restlet.data.MediaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.zpg.trumptweets.dao.User_paymentDAO;
import com.zpg.trumptweets.domain.Category;
import com.zpg.trumptweets.domain.Charity;
import com.zpg.trumptweets.domain.Donation_log;
import com.zpg.trumptweets.domain.User_payment;
import com.zpg.trumptweets.exception.InvalidDonationException;
import com.zpg.trumptweets.exception.InvalidUserException;
import com.zpg.trumptweets.exception.NoCharityForCategoryException;
import com.zpg.trumptweets.exception.NoValidPaymentOptionsException;

@Named("pandaPayRestProcessor")
public class PandaPayRestProcessor implements Processor {
	
	Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	User_paymentDAO paymentDAO;

	@Override
	public void process(Exchange exchange) throws Exception {
		Donation_log donation = exchange.getIn().getBody(Donation_log.class);
		
		exchange.getIn().setHeader("donation", donation);
		
		if(donation.getUser() != null){
		
			User_payment paymentToUse = findUserPayment(donation);
			
			String email = donation.getUser().getEmail();
			
			if(email == null || email.equalsIgnoreCase("")){
				throw new InvalidUserException("User: "+ donation.getUser().getLogin() + " has no email address!");
			}
			
			Category category = donation.getCategory();
			
			if(category == null){
				throw new InvalidDonationException("Donation record has no category! "+ donation);
			}
			
			Set<Charity> charities = category.getCharities();
			if(charities.isEmpty()){
				throw new NoCharityForCategoryException("No Charity for category: "+ category);
			}
			
			Charity charity = charities.iterator().next();
			
			String destinationEIN = charity.getEin();
		
			if(donation.getAmount() != null){
									
				StringBuilder formData = new StringBuilder();
				formData.append("amount="+donation.getAmount().movePointRight(2).toPlainString());
				formData.append("&currency=usd");
				formData.append("&source="+paymentToUse.getToken());
				formData.append("&receipt_email="+email);
				formData.append("&platform_fee=0");
				formData.append("&destination_ein="+destinationEIN);
				formData.append("&destination="+destinationEIN);
				
				log.info("Query : "+ formData);
			
				exchange.getIn().setHeader(Exchange.CONTENT_TYPE, MediaType.APPLICATION_WWW_FORM);
				exchange.getIn().setHeader(Exchange.HTTP_METHOD, "POST");
				exchange.getIn().setBody(formData.toString());
				
				
			}else{
				throw new InvalidDonationException("Donation record has no amount! "+ donation);
			}
			
		}else{
			throw new InvalidDonationException("Donation record has no user!");
		}
		
	}
	
	private User_payment findUserPayment(Donation_log donation) throws NoValidPaymentOptionsException{
		//Pull list of payment options
		List<User_payment> paymentOptions = paymentDAO.findByUser(donation.getUser());
		User_payment paymentToUse = null;
		
		
		for(User_payment payment: paymentOptions){
			if(payment.isValid() && payment.getToken() != null){
				paymentToUse = payment;
			}
		}
		//If no valid options were found, throw exception
		if(paymentToUse == null){
			throw new NoValidPaymentOptionsException("User: "+ donation.getUser().getLogin()+ " has no valid payment options!");
		}
		
		return paymentToUse;
	}

}
