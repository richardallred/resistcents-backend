package com.zpg.trumptweets.bean;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Named;

import org.apache.camel.Body;
import org.apache.camel.Header;
import org.apache.camel.Message;
import org.apache.camel.impl.DefaultMessage;

import com.zpg.trumptweets.domain.Tweetlog;
import com.zpg.trumptweets.domain.User;

@Named
public class UserListHeaderSplitBean {
	
	/**
	 * Splits a list of users from a header named "userList" and creates seperate exchanges for each user/tweetlog
	 * @param userList
	 * @param tweetLog
	 * @return List of Exchanges one for each user
	 */
	public List<Message> splitMessage(@Header(value = "userList") List<User> userList, @Body Tweetlog tweetLog){
		List<Message> answer = new ArrayList<Message>();
		
		for(User user: userList){
			Message message = new DefaultMessage();
			message.setHeader("user", user);
			message.setBody(tweetLog);
			answer.add(message);
		}
		
		return answer;
		
	}

}
