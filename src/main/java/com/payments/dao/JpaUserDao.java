package com.payments.dao;

import javax.persistence.TypedQuery;

import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.payments.model.User;

@Transactional
@Repository
public class JpaUserDao extends GenericDao<User>implements UserDao {

	public JpaUserDao() {
		super(User.class);
	}

	@Override
	public User findByEmail(String email) {
		if (email == null) {
			return null;
		}

		TypedQuery<User> query = createNamedQuery(User.QUERY_FIND_BY_EMAIL);
		query.setParameter("email", email.toUpperCase());
		return getSingleResult(query);
	}

	@Override
	public User findByCredentials(String email, String password) {

		final User user = findByEmail(email);
		
		if (user != null && BCrypt.checkpw(password, user.getPassword())) {
			return user;
		}

		return null;
	}
}
