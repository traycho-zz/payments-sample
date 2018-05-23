package com.payments.client;

import java.math.BigDecimal;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

import com.payments.model.PaymentStatus;
import com.payments.model.TransactionStatus;
import com.payments.rest.requests.CardActionRequest;
import com.payments.rest.requests.CreateCardRequest;
import com.payments.rest.requests.CreatePaymentRequest;
import com.payments.rest.requests.PaymentRequest;
import com.payments.rest.responses.BalanceResponse;
import com.payments.rest.responses.CardResponse;
import com.payments.rest.responses.PaymentResponse;
import com.payments.rest.responses.TransactionResponse;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestDefaultPaymentWithReverse extends BaseTest {
	
	private static final Logger LOG = LoggerFactory.getLogger(TestDefaultPaymentWithReverse.class);

	@Test
	public void test1CardCreateAndTopup(){
		LOG.info("Test create card and topup");
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
	public void test2CreatePaymentAndAuthorize(){
		LOG.info("Test create payment and authorize");
		RestTemplate client = new RestTemplate();
		HttpHeaders headers = createHeaders(merchantAuth.getAccessToken());
		CreatePaymentRequest createPayment = new CreatePaymentRequest();
		createPayment.setAmount(new BigDecimal("5"));
		createPayment.setCurrency("GBP");
		createPayment.setDescription("Coffe");
		
		payment = client.exchange(PAYMENTS_URL, HttpMethod.POST,new HttpEntity<Object>(createPayment,headers),PaymentResponse.class).getBody();
		Assert.assertNotNull(payment);
		Assert.assertNotNull(payment.getUid());
		Assert.assertEquals(PaymentStatus.PENDING, payment.getStatus());
		
		
		PaymentRequest authorize = new PaymentRequest();
		authorize.setCard(card.getUid());
		authorize.setAmount(new BigDecimal("5"));
		
		
		TransactionResponse transaction = client.exchange(PAYMENTS_URL + "/" + payment.getUid() + "/authorize", HttpMethod.POST,new HttpEntity<Object>(authorize,headers),TransactionResponse.class).getBody();
		Assert.assertNotNull(transaction);
		Assert.assertNotNull(transaction.getUid());
	}
	
	@Test
	public void test3CaptureAndReverse(){
		LOG.info("Test reverse,capture and refund");
		RestTemplate client = new RestTemplate();
		HttpHeaders headers = createHeaders(merchantAuth.getAccessToken());
		
		PaymentRequest reverse = new PaymentRequest();
		reverse.setAmount(new BigDecimal("1"));
		reverse.setCard(card.getUid());
		
		TransactionResponse firstReverse = client.exchange(PAYMENTS_URL + "/" + payment.getUid() + "/reverse", HttpMethod.POST,new HttpEntity<Object>(reverse,headers),TransactionResponse.class).getBody();
		Assert.assertNotNull(firstReverse);
		Assert.assertNotNull(firstReverse.getUid());
		Assert.assertEquals(TransactionStatus.COMPLETE, firstReverse.getStatus());;
		
		
		PaymentRequest capture = new PaymentRequest();
		capture.setAmount(new BigDecimal("4"));
		capture.setCard(card.getUid());
		
		TransactionResponse firstCapture = client.exchange(PAYMENTS_URL + "/" + payment.getUid() + "/capture", HttpMethod.POST,new HttpEntity<Object>(capture,headers),TransactionResponse.class).getBody();
		Assert.assertNotNull(firstCapture);
		Assert.assertNotNull(firstCapture.getUid());
		Assert.assertEquals(TransactionStatus.COMPLETE, firstCapture.getStatus());;

		
		PaymentRequest refund = new PaymentRequest();
		refund.setCard(card.getUid());
		refund.setAmount(new BigDecimal("3"));
		
		
		TransactionResponse firstRefund = client.exchange(PAYMENTS_URL + "/" + payment.getUid() + "/refund", HttpMethod.POST,new HttpEntity<Object>(refund,headers),TransactionResponse.class).getBody();
		Assert.assertNotNull(firstRefund);
		Assert.assertNotNull(firstRefund.getUid());
		Assert.assertEquals(TransactionStatus.COMPLETE, firstRefund.getStatus());;
		

		BalanceResponse balance = client.exchange(CARDS_URL + "/" + card.getUid() +"/balance", HttpMethod.GET,new HttpEntity<Object>(headers),BalanceResponse.class).getBody();
		Assert.assertTrue(new BigDecimal("9").compareTo(balance.getTotal()) == 0);
		Assert.assertTrue(new BigDecimal("9").compareTo(balance.getAvailable()) == 0);
		Assert.assertTrue(BigDecimal.ZERO.compareTo(balance.getBlocked()) == 0);
	}
	
	
}
