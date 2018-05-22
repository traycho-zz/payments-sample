package com.payments.client;

import java.math.BigDecimal;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import com.payments.model.Payment;
import com.payments.model.PaymentStatus;
import com.payments.model.Transaction;
import com.payments.rest.requests.CardActionRequest;
import com.payments.rest.requests.CreateCardRequest;
import com.payments.rest.requests.CreatePaymentRequest;
import com.payments.rest.requests.CreateUserRequest;
import com.payments.rest.requests.CredentialsRequest;
import com.payments.rest.requests.PaymentRequest;
import com.payments.rest.responses.AuthResponse;
import com.payments.rest.responses.BalanceResponse;
import com.payments.rest.responses.CardResponse;
import com.payments.rest.responses.UserResponse;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.DEFINED_PORT)
public class ScenarioTest {
	
	private static final String SERVER_URL = "http://localhost:8080";
	private static final String AUTHORIZE = SERVER_URL + "/authorize";
	private static final String USERS_URL = SERVER_URL + "/users";
	
	private static final String CARDS_URL = SERVER_URL + "/cards";
	
	private static final String PAYMENTS_URL = SERVER_URL + "/payments";
	
	private static final String USER_USERNAME = "user@payments.com";
	
	private static final String MERCHANT_USERNAME = "merchant@payments.com";
	
	private static final String PASSWORD = "password";
	
	private static UserResponse user;
	
	private static UserResponse merchant;
	
	private static CardResponse card;
	
	private static Payment payment;
	
	private static AuthResponse userAuth;
	
	private static AuthResponse merchantAuth;
	
	@Before
	public void beforeTest(){
		if(user == null){
			user = createUser(USER_USERNAME,PASSWORD);
		}
		
		if(merchant == null){
			merchant = createUser(MERCHANT_USERNAME,PASSWORD);
		}
		
		if(userAuth == null){
			userAuth = authorize(USER_USERNAME,PASSWORD);
		}
		
		if(merchantAuth == null){
			merchantAuth = authorize(MERCHANT_USERNAME,PASSWORD);
		}
	}
	
	private static AuthResponse authorize(String username, String password){
		RestTemplate client = new RestTemplate();
		
		CredentialsRequest credentialRequest = new CredentialsRequest();
		credentialRequest.setUsername(username);
		credentialRequest.setPassword(password);
		AuthResponse auth  = client.postForEntity(AUTHORIZE, credentialRequest, AuthResponse.class).getBody();
		return auth;
		
	}
	
	private static HttpHeaders createHeaders(String token){
		   return new HttpHeaders() {{
		         set( "Authorization","Bearer " + token);
		      }};
		}
	
	private static UserResponse createUser(String username, String password){
		RestTemplate client = new RestTemplate();
		
		CreateUserRequest createUser = new CreateUserRequest();
		createUser.setUsername(username);
		createUser.setPassword(password);
		UserResponse user  = client.postForEntity(USERS_URL, createUser, UserResponse.class).getBody();
		return user;
	}

	
	@Test
	public void testCardCreateAndTopup(){
		RestTemplate client = new RestTemplate();
		HttpHeaders headers = createHeaders(userAuth.getAccessToken());
		CreateCardRequest request = new CreateCardRequest();
		request.setNumber("1234567890");
		request.setExpiryDate("11/2020");

		
		card = client.exchange(CARDS_URL, HttpMethod.POST,new HttpEntity<Object>(request,headers),CardResponse.class).getBody();
		Assert.assertNotNull(card);
		Assert.assertNotNull(card.getUid());
		Assert.assertEquals("7890", card.getLastFour());
		Assert.assertEquals("11/2020", card.getExpiryDate());
		
		CardActionRequest topup = new CardActionRequest();
		topup.setAmount(BigDecimal.TEN);
		topup.setCurrency("GBP");
		BalanceResponse balance = client.exchange(CARDS_URL + "/" + card.getUid() +"/topup", HttpMethod.POST,new HttpEntity<Object>(topup,headers),BalanceResponse.class).getBody();
		
		Assert.assertNotNull(balance);
		Assert.assertTrue(BigDecimal.TEN.compareTo(balance.getTotal()) == 0);
		Assert.assertTrue(BigDecimal.TEN.compareTo(balance.getAvailable()) == 0);
		Assert.assertTrue(BigDecimal.ZERO.compareTo(balance.getBlocked()) == 0);
	}
	
	
	@Test
	public void testCreatePaymentAndAuthorize(){
		RestTemplate client = new RestTemplate();
		HttpHeaders headers = createHeaders(merchantAuth.getAccessToken());
		CreatePaymentRequest request = new CreatePaymentRequest();
		request.setAmount(new BigDecimal("5"));
		request.setCurrency("GBP");
		request.setDescription("Coffe");
		
		payment = client.exchange(PAYMENTS_URL, HttpMethod.POST,new HttpEntity<Object>(request,headers),Payment.class).getBody();
		Assert.assertNotNull(payment);
		Assert.assertNotNull(payment.getUid());
		Assert.assertEquals(PaymentStatus.PENDING, payment.getStatus());
		
		
		PaymentRequest authorize = new PaymentRequest();
		authorize.setCard(card.getUid());
		authorize.setAmount(new BigDecimal("5"));
		
		
		Transaction transaction = client.exchange(PAYMENTS_URL + "/" + payment.getUid() + "/authorize", HttpMethod.POST,new HttpEntity<Object>(request,headers),Transaction.class).getBody();
		Assert.assertNotNull(transaction);
		Assert.assertNotNull(transaction.getUuid());
	}	
}
