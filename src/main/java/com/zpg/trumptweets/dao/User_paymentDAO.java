package com.zpg.trumptweets.dao;

import java.util.List;

import com.zpg.trumptweets.domain.User;
import com.zpg.trumptweets.domain.User_payment;

public interface User_paymentDAO {
	
	public List<User_payment> findByUser(User u);
}
