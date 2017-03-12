package com.zpg.trumptweets.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.zpg.trumptweets.domain.User;
import com.zpg.trumptweets.domain.User_payment;

@Repository("User_paymentDAO")
public class User_paymentDAOImpl implements User_paymentDAO{
	
	@Autowired
	private EntityManager entityManager;

	@SuppressWarnings("unchecked")
	@Override
	public List<User_payment> findByUser(User u) {
		Query findByUser = entityManager.createNamedQuery("findPaymentByUser");
		findByUser.setParameter("user", u);
		return findByUser.getResultList();
	}

}
