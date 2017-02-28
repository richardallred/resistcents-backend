package com.zpg.trumptweets.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.zpg.trumptweets.domain.Category;
import com.zpg.trumptweets.domain.User;

@Repository("User_preferencesDAO")
public class User_preferencesDAOImpl implements User_preferencesDAO {
	
	@Autowired
	private EntityManager entityManager;


	@Override
	public List<User> findUsersForCategory(Category cat) {
		Query findUsersQuery = entityManager.createNamedQuery("findUsersForCategory");
		findUsersQuery.setParameter("category", cat);
		return findUsersQuery.getResultList();
	}

}
