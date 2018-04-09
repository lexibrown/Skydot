package service;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import utils.AccountUtil;
import utils.JsonUtil;
import utils.TokenUtil;
import utils.Variables;

@Path("/account")
@Produces(MediaType.APPLICATION_JSON)
public class AccountApplication extends BaseApplication {

	private static final Logger log = LogManager.getLogger(AccountApplication.class);
	
	private final static String DETAILS = "/details";

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String healthCheck() {
		return "Account Service";
	}
	
	@POST
	public Response account(HashMap<String, Object> params) {
		try {
			if (!params.containsKey(Variables.TOKEN)) {
				return noTokenResponse();
			}

			String token = params.get(Variables.TOKEN).toString();
			Map<String, Object> response = TokenUtil.verifyToken(token);
			if (response.containsKey(Variables.ERROR)) {
				return Response.status((int) response.get(Variables.STATUS)).entity(JsonUtil.stringify(response)).build();
			}
			
			response = AccountUtil.getAccountSummary(TokenUtil.getUserId(token));
			if (response.containsKey(Variables.ERROR)) {
				return Response.status((int) response.get(Variables.STATUS)).entity(JsonUtil.stringify(response)).build();
			}
			return Response.ok(JsonUtil.stringify(response), MediaType.APPLICATION_JSON).build();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return crashResponse();
		}
	}

	@POST
	@Path(DETAILS)
	public Response accountDetails(HashMap<String, Object> params) {
		try {
			if (!params.containsKey(Variables.TOKEN)) {
				return noTokenResponse();
			}

			String token = params.get(Variables.TOKEN).toString();
			Map<String, Object> response = TokenUtil.verifyToken(token);
			if (response.containsKey(Variables.ERROR)) {
				return Response.status((int) response.get(Variables.STATUS)).entity(JsonUtil.stringify(response)).build();
			}
			
			if (!params.containsKey(Variables.ACCOUNT_ID)) {
				return invalidResponse();
			}
			int accountId = (int) params.get(Variables.ACCOUNT_ID);
			
			response = AccountUtil.getAccountDetails(TokenUtil.getUserId(token), accountId);
			if (response.containsKey(Variables.ERROR)) {
				return Response.status((int) response.get(Variables.STATUS)).entity(JsonUtil.stringify(response)).build();
			}
			return Response.ok(JsonUtil.stringify(response), MediaType.APPLICATION_JSON).build();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return crashResponse();
		}
	}

}
