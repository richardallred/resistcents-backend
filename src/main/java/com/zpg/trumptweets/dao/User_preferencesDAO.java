package com.zpg.trumptweets.dao;

import java.util.List;

import com.zpg.trumptweets.domain.Category;
import com.zpg.trumptweets.domain.User;

public interface User_preferencesDAO {

	List<User> findUsersForCategory(Category cat);
}
