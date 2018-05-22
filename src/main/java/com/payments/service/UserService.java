package com.payments.service;

import com.payments.model.User;

public interface UserService {
	
	User get(Long id);
	
	User findByEmail(String email);
	
	User create(User user);
	
	User findByCredentials(String email,String password);
}
