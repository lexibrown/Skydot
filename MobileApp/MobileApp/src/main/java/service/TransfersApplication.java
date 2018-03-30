package service;

import java.util.HashMap;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import utils.JsonUtil;
import utils.TokenUtil;
import utils.TransfersUtil;

@Path("/transfer")
@Produces(MediaType.APPLICATION_JSON)
public class TransfersApplication extends BaseApplication {

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String healthCheck() {
		return "Transfer Service";
	}

	@POST
	public String submitTransaction(HashMap<String, Object> params) {
		try {
			if (!params.containsKey(TOKEN)) {
				return JsonUtil.errorJson(SERVICE + "-1000", "No token provided.");
			}

			String token = params.get(TOKEN).toString();
			String verify = TokenUtil.verifyToken(token);
			if (verify != null) {
				return verify;
			}

			int fromId = 0;
			int toId = 0;
			double amount = 0.0;
			String currency = "";

			if (params.containsKey(FROM_ACCOUNT)) {
				fromId = (int) params.get(FROM_ACCOUNT);
			}
			if (params.containsKey(TO_ACCOUNT)) {
				toId = (int) params.get(TO_ACCOUNT);
			}
			if (params.containsKey(AMOUNT)) {
				amount = (double) params.get(AMOUNT);
			}
			if (params.containsKey(CURRENCY)) {
				currency = params.get(CURRENCY).toString();
			}

			return TransfersUtil.submitTransaction(TokenUtil.getUserId(token), fromId, toId, amount, currency);
		} catch (Exception e) {
			e.printStackTrace();
			return JsonUtil.fail(e);
		}
	}

}