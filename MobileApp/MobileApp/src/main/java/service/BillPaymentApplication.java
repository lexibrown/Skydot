package service;

import java.util.HashMap;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import utils.BillPaymentUtil;
import utils.JsonUtil;
import utils.TokenUtil;
import utils.Variables;

@Path("/bill")
@Produces(MediaType.APPLICATION_JSON)
public class BillPaymentApplication extends BaseApplication {

	private final static String PAYEE = "/payee";
	private final static String SEARCH = "/payee/search";

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String healthCheck() {
		return "Bill Payment Service";
	}

	@POST
	public String submitBillPayment(HashMap<String, Object> params) {
		try {
			if (!params.containsKey(Variables.TOKEN)) {
				return JsonUtil.errorJson(SERVICE + "-1000", "No token provided.");
			}

			String token = params.get(Variables.TOKEN).toString();
			String verify = TokenUtil.verifyToken(token);
			if (verify != null) {
				return verify;
			}

			int fromId = 0;
			int payee = 0;
			double amount = 0.0;
			String currency = "";

			if (params.containsKey(Variables.FROM_ACCOUNT)) {
				fromId = (int) params.get(Variables.FROM_ACCOUNT);
			}
			if (params.containsKey(Variables.PAYEE)) {
				payee = (int) params.get(Variables.PAYEE);
			}
			if (params.containsKey(Variables.AMOUNT)) {
				amount = (double) params.get(Variables.AMOUNT);
			}
			if (params.containsKey(Variables.CURRENCY)) {
				currency = params.get(Variables.CURRENCY).toString();
			}

			return BillPaymentUtil.submitBillPayment(TokenUtil.getUserId(token), fromId, payee, amount, currency);
		} catch (Exception e) {
			e.printStackTrace();
			return JsonUtil.fail(e);
		}
	}

	@POST
	@Path(PAYEE)
	public String getPayees(HashMap<String, Object> params) {
		try {
			if (!params.containsKey(Variables.TOKEN)) {
				return JsonUtil.errorJson(SERVICE + "-1000", "No token provided.");
			}

			String token = params.get(Variables.TOKEN).toString();
			String verify = TokenUtil.verifyToken(token);
			if (verify != null) {
				return verify;
			}
			return BillPaymentUtil.getPayees(TokenUtil.getUserId(token));
		} catch (Exception e) {
			e.printStackTrace();
			return JsonUtil.fail(e);
		}
	}

	@POST
	@Path(SEARCH)
	public String serachPayees(HashMap<String, Object> params) {
		try {
			if (!params.containsKey(Variables.TOKEN)) {
				return JsonUtil.errorJson(SERVICE + "-1000", "No token provided.");
			}

			String token = params.get(Variables.TOKEN).toString();
			String verify = TokenUtil.verifyToken(token);
			if (verify != null) {
				return verify;
			}
			
			String search = "";
			if (params.containsKey(Variables.SEARCH)) {
				search = params.get(Variables.SEARCH).toString();
			}
			return BillPaymentUtil.searchPayees(TokenUtil.getUserId(token), search);
		} catch (Exception e) {
			e.printStackTrace();
			return JsonUtil.fail(e);
		}
	}
	
}
