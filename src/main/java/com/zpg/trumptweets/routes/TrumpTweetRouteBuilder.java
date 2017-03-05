package com.zpg.trumptweets.routes;
import javax.persistence.EntityManagerFactory;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jpa.JpaComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.stereotype.Component;

import com.zpg.trumptweets.bean.UserListHeaderSplitBean;
import com.zpg.trumptweets.service.DonationLogService;
import com.zpg.trumptweets.service.UserBalanceService;
import com.zpg.trumptweets.service.UserService;
import com.zpg.trumptweets.service.UserTweetLogService;
 
@Component
public class TrumpTweetRouteBuilder extends RouteBuilder {
	
	@Autowired
	JpaTransactionManager transactionManager;
	
	@Autowired
	EntityManagerFactory emf;
 
    @Override
    public void configure() throws Exception {
    	
    	JpaComponent jpaComponent = new JpaComponent();
    	jpaComponent.setTransactionManager(transactionManager);
    	jpaComponent.setEntityManagerFactory(emf);
    	jpaComponent.setJoinTransaction(false);
    	jpaComponent.setSharedEntityManager(true);
    	getContext().addComponent("jpa",jpaComponent);
    	
        from("jpa:com.zpg.trumptweets.domain.Tweetlog?consumer.namedQuery=getUnprocessedCategorizedTweets&consumeDelete=false"
        		+ "&persistenceUnit=postgresql&consumer.delay=60000")
        		.log("New Tweet found!")
          		.bean(UserService.class,"findUsers")
          		.split().method(UserListHeaderSplitBean.class,"splitMessage")
          			.bean(UserTweetLogService.class,"createTweetLogs")
          			.bean(UserBalanceService.class,"createOrUpdateUserBalances")
          			.choice()
          				.when(header("donationLogCat").isNotNull())
          					.bean(DonationLogService.class,"createDonationLog")
          			.end()
          		.log("Done!");
        
        from("jpa:com.zpg.trumptweets.domain.Donation_log?consumer.namedQuery=getUnprocessedTransactions&consumeDelete=false"
        		+ "&persistenceUnit=postgresql&consumer.delay=60000&consumer.initialDelay=30000")
        		.log("Unprocessed Donations Found!")
        		.filter().method(DonationLogService.class, "filterMonthlyLimit")
        			.log("Sending ${body} to PandaPay(stub)")
        			.to("mock:pandaPay")
        			.bean(DonationLogService.class,"processDonation");
        
    }
}