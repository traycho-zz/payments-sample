package com.payments.service;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.payments.dao.UserDao;
import com.payments.exceptions.ApiValidationException;
import com.payments.model.User;


@Service
@Transactional
public class JpaUserService implements UserService {
	
	private static final Logger LOG = LoggerFactory.getLogger(JpaUserService.class);
	    
	
	@Autowired
	private UserDao userDao;

	@Override
	public User get(Long id) {
		return userDao.find(id);
	}

	@Override
	public User findByEmail(String email) {
		return userDao.findByEmail(email);
	}

	@Override
    public User create(final User user) {

        final User existing = this.userDao.findByEmail(user.getEmail());
        if (existing != null) {
            throw new ApiValidationException("entityExists","User with such username already exists");
        }

        
        final String hashedPassword = BCrypt.hashpw(user.getPassword(),BCrypt.gensalt());
        user.setPassword(hashedPassword);
        user.setUid(UUID.randomUUID().toString());
        return userDao.save(user);
    }

	@Override
	public User findByCredentials(String email, String password) {
		return userDao.findByCredentials(email, password);
	}

	
	
}
