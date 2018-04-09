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

import utils.BillPaymentUtil;
import utils.JsonUtil;
import utils.TokenUtil;
import utils.Variables;

@Path("/bill")
@Produces(MediaType.APPLICATION_JSON)
public class BillPaymentApplication extends BaseApplication {

	private static final Logger log = LogManager.getLogger(BillPaymentApplication.class);
	
	private final static String PAYEE = "/payee";
	private final static String SEARCH = "/payee/search";

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String healthCheck() {
		return "Bill Payment Service";
	}

	@POST
	public Response submitBillPayment(HashMap<String, Object> params) {
		try {
			if (!params.containsKey(Variables.TOKEN)) {
				return noTokenResponse();
			}

			String token = params.get(Variables.TOKEN).toString();
			Map<String, Object> response = TokenUtil.verifyToken(token);
			if (response.containsKey(Variables.ERROR)) {
				return Response.status((int) response.get(Variables.STATUS)).entity(JsonUtil.stringify(response)).build();
			}

			if (!params.containsKey(Variables.FROM_ACCOUNT)) {
				return invalidResponse();
			} else if (!params.containsKey(Variables.PAYEE)) {
				return invalidResponse();
			} else if (!params.containsKey(Variables.AMOUNT)) {
				return invalidResponse();
			} else if (!params.containsKey(Variables.CURRENCY)) {
				return invalidResponse();
			}
			
			int fromId = (int) params.get(Variables.FROM_ACCOUNT);
			int payee = (int) params.get(Variables.PAYEE);
			double amount = new Double(params.get(Variables.AMOUNT).toString());
			String currency = params.get(Variables.CURRENCY).toString();

			response = BillPaymentUtil.submitBillPayment(TokenUtil.getUserId(token), fromId, payee, amount, currency);
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
	@Path(PAYEE)
	public Response getPayees(HashMap<String, Object> params) {
		try {
			if (!params.containsKey(Variables.TOKEN)) {
				return noTokenResponse();
			}

			String token = params.get(Variables.TOKEN).toString();
			Map<String, Object> response = TokenUtil.verifyToken(token);
			if (response.containsKey(Variables.ERROR)) {
				return Response.status((int) response.get(Variables.STATUS)).entity(JsonUtil.stringify(response)).build();
			}
			
			response = BillPaymentUtil.getPayees(TokenUtil.getUserId(token));
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
	@Path(SEARCH)
	public Response serachPayees(HashMap<String, Object> params) {
		try {
			if (!params.containsKey(Variables.TOKEN)) {
				return noTokenResponse();
			}

			String token = params.get(Variables.TOKEN).toString();
			Map<String, Object> response = TokenUtil.verifyToken(token);
			if (response.containsKey(Variables.ERROR)) {
				return Response.status((int) response.get(Variables.STATUS)).entity(JsonUtil.stringify(response)).build();
			}
			
			String search = "";
			if (params.containsKey(Variables.SEARCH)) {
				search = params.get(Variables.SEARCH).toString();
			}
			
			response = BillPaymentUtil.searchPayees(TokenUtil.getUserId(token), search);
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
