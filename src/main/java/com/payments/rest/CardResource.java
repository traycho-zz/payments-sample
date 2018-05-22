package com.payments.rest;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.payments.exceptions.ApiValidationException;
import com.payments.exceptions.ErrorCodes;
import com.payments.model.Card;
import com.payments.model.Transaction;
import com.payments.model.TransactionStatus;
import com.payments.model.TransactionType;
import com.payments.model.User;
import com.payments.rest.requests.CardActionRequest;
import com.payments.rest.requests.CreateCardRequest;
import com.payments.rest.responses.BalanceResponse;
import com.payments.rest.responses.CardResponse;
import com.payments.rest.responses.Responses;
import com.payments.security.annotations.Secured;
import com.payments.service.CardService;

@Component
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path(CardResource.PATH)
public class CardResource extends BaseResource {
	
	private static final DateFormat DATE_FORMATTER = new SimpleDateFormat("MM/yyyy");
	
	public final static String PATH = "/cards";	
	
	@Autowired
	private CardService cardService;

	@GET
	@Secured
	@Path("/")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response get() {
		final User user = currentUser();
		List<Card> cards = cardService.findByUser(user.getId());
		
		List<CardResponse> responses = new ArrayList<>();
		for(Card card : cards){
			responses.add(toResponse(card));
		}
        return Response.ok(responses).build();
	}
	
	@POST
	@Secured
	@Path("/")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response create(CreateCardRequest request)  {
		final User user = currentUser();
		
		Date expiryDate = null;
		try {
			expiryDate = DATE_FORMATTER.parse(request.getExpiryDate());
		} catch (ParseException e) {
			throw new ApiValidationException(ErrorCodes.VALIDATION_FAILED, "Invalid expiry date, pattern use MM/yyyy");
		}
		Card card = new Card();
		card.setExpiryDate(expiryDate);
		card.setUid(UUID.randomUUID().toString());
		card.setNumber(request.getNumber());
		card.setUserId(user.getId());
		card.setBalance(BigDecimal.ZERO);
		card.setBlockedBalance(BigDecimal.ZERO);
		card = cardService.create(card);

		CardResponse response = toResponse(card);
        return Response.ok(response).build();
	}
	
	@GET
	@Secured
	@Path("/{uid}/balance")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response balance(@PathParam(value = "uid") final String uid) {
		Card card = cardService.findByUid(uid);
		if(card == null){
			return Responses.notFound();
		}
		
		BalanceResponse balance = new BalanceResponse();
		balance.setAvailable(card.getBalance());
		balance.setBlocked(card.getBlockedBalance());
		balance.setTotal(card.total());
		balance.setCurrency("GBP");
		return Responses.ok(balance);
	}

	@POST
	@Secured
	@Path("/{uid}/topup")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response topup(@PathParam(value = "uid") final String uid,CardActionRequest request) {
		final User user = currentUser();	
		
		Transaction transaction = new Transaction();
		transaction.setAmount(request.getAmount());
		transaction.setCurrency(request.getCurrency());
		transaction.setStatus(TransactionStatus.COMPLETE);
		transaction.setType(TransactionType.TOPUP);
		transaction.setUser(user);
		transaction.setDirection(Transaction.CREDIT);
		Card card = cardService.topup(transaction,uid);
		
		BalanceResponse balance = new BalanceResponse();
		balance.setAvailable(card.getBalance());
		balance.setBlocked(card.getBlockedBalance());
		balance.setTotal(card.total());
		balance.setCurrency("GBP");
        return Response.ok(balance).build();
	}	

	private CardResponse toResponse(Card card){
		CardResponse response = new CardResponse();
		response.setUid(card.getUid());
		response.setLastFour(card.lastFour());
		response.setExpiryDate(DATE_FORMATTER.format(card.getExpiryDate()));
		
		return response;
	}
}