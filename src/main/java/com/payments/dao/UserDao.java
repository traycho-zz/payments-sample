package com.payments.dao;

import com.payments.model.User;

public interface UserDao extends Dao<User> {
	
	User findByEmail(String email);
	
	User findByCredentials(String email,String password);

}
