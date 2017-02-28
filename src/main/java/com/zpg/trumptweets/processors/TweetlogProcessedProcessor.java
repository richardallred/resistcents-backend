package com.zpg.trumptweets.processors;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zpg.trumptweets.domain.Tweetlog;


public class TweetlogProcessedProcessor implements Processor {
	
	Logger log = LoggerFactory.getLogger(this.getClass());

	@Override
	public void process(Exchange exchange) throws Exception {
		Tweetlog tweet = exchange.getIn().getBody(Tweetlog.class);
		log.info("Marking tweet as processed!");
		tweet.setProcessed(true);
		
	}

}
