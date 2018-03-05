package service;

import java.util.HashMap;

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

	private static final String FROM = "from";
	private static final String TO = "to";
	private static final String AMOUNT = "amount";
	private static final String CURRENCY = "currency";

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

			if (params.containsKey(FROM)) {
				fromId = (int) params.get(FROM);
			}
			if (params.containsKey(TO)) {
				toId = (int) params.get(TO);
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