package com.payments.client;

import org.junit.Before;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;

import com.payments.rest.requests.CreateUserRequest;
import com.payments.rest.requests.CredentialsRequest;
import com.payments.rest.responses.AuthResponse;
import com.payments.rest.responses.CardResponse;
import com.payments.rest.responses.PaymentResponse;
import com.payments.rest.responses.UserResponse;

public class BaseTest {

	public static final String SERVER_URL = "http://localhost:8080";
	public static final String AUTHORIZE = SERVER_URL + "/authorize";
	public static final String USERS_URL = SERVER_URL + "/users";

	public static final String CARDS_URL = SERVER_URL + "/cards";

	public static final String PAYMENTS_URL = SERVER_URL + "/payments";

	public static final String USER_USERNAME = "user@payments.com";

	public static final String MERCHANT_USERNAME = "merchant@payments.com";

	public static final String PASSWORD = "password";

	protected static UserResponse user;

	protected static UserResponse merchant;

	protected static CardResponse card;

	protected static PaymentResponse payment;

	protected static AuthResponse userAuth;

	protected static AuthResponse merchantAuth;

	@Before
	public void beforeTest() {
		if (user == null) {
			user = createUser(BaseTest.USER_USERNAME, BaseTest.PASSWORD);
		}

		if (merchant == null) {
			merchant = createUser(BaseTest.MERCHANT_USERNAME, BaseTest.PASSWORD);
		}

		if (userAuth == null) {
			userAuth = authorize(BaseTest.USER_USERNAME, BaseTest.PASSWORD);
		}

		if (merchantAuth == null) {
			merchantAuth = authorize(BaseTest.MERCHANT_USERNAME, BaseTest.PASSWORD);
		}
	}

	private static AuthResponse authorize(String username, String password) {
		RestTemplate client = new RestTemplate();

		CredentialsRequest credentialRequest = new CredentialsRequest();
		credentialRequest.setUsername(username);
		credentialRequest.setPassword(password);
		AuthResponse auth = client.postForEntity(AUTHORIZE, credentialRequest, AuthResponse.class).getBody();
		return auth;

	}

	@SuppressWarnings("serial")
	protected  HttpHeaders createHeaders(String token) {
		return new HttpHeaders() {
			{
				set("Authorization", "Bearer " + token);
			}
		};
	}

	private static UserResponse createUser(String username, String password) {
		RestTemplate client = new RestTemplate();

		CreateUserRequest createUser = new CreateUserRequest();
		createUser.setUsername(username);
		createUser.setPassword(password);
		UserResponse user = client.postForEntity(USERS_URL, createUser, UserResponse.class).getBody();
		return user;
	}

}
