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

import utils.JsonUtil;
import utils.TokenUtil;
import utils.TransfersUtil;
import utils.Variables;

@Path("/transfer")
@Produces(MediaType.APPLICATION_JSON)
public class TransfersApplication extends BaseApplication {

	private static final Logger log = LogManager.getLogger(TransfersApplication.class);
	
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String healthCheck() {
		return "Transfer Service";
	}

	@POST
	public Response submitTransaction(HashMap<String, Object> params) {
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
			} else if (!params.containsKey(Variables.TO_ACCOUNT)) {
				return invalidResponse();
			} else if (!params.containsKey(Variables.AMOUNT)) {
				return invalidResponse();
			} else if (!params.containsKey(Variables.CURRENCY)) {
				return invalidResponse();
			}

			int fromId = (int) params.get(Variables.FROM_ACCOUNT);
			int toId = (int) params.get(Variables.TO_ACCOUNT);
			double amount = new Double(params.get(Variables.AMOUNT).toString());
			String currency = params.get(Variables.CURRENCY).toString();
			
			response = TransfersUtil.submitTransaction(TokenUtil.getUserId(token), fromId, toId, amount, currency);
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