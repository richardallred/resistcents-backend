package com.zpg.trumptweets.routes;
import javax.persistence.EntityManagerFactory;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jpa.JpaComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.stereotype.Component;

import com.zpg.trumptweets.service.UserService;
 
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
        		+ "&persistenceUnit=postgresql&consumer.delay=50000")
        		.log("New Tweet found!")
          		.bean(UserService.class,"createTweetLogs")
          		.log("Done!");
        	
    }
}