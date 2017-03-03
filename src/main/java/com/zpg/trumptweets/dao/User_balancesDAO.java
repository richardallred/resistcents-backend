package com.zpg.trumptweets.dao;

import java.util.Set;

import com.zpg.trumptweets.domain.User_balances;

public interface User_balancesDAO {
	
	public void updateUserBalances(Set<User_balances> balances);
}
