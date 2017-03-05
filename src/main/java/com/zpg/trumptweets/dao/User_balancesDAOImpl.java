package com.zpg.trumptweets.dao;

import java.util.Set;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.zpg.trumptweets.domain.User_balances;

@Repository("User_balancesDAO")
public class User_balancesDAOImpl implements User_balancesDAO{
	
	@Autowired
	private EntityManager entityManager;

	@Transactional
	@Override
	public void updateUserBalances(Set<User_balances> balances) {
		for(User_balances balance: balances){
			if(balance.getId() != null){
				entityManager.merge(balance);
			}else{
				entityManager.persist(balance);
			}
		}
	}

}
