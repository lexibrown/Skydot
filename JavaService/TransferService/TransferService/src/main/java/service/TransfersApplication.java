package service;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import utils.JsonUtil;
import utils.TransfersUtil;
import utils.Variables;

@Path("/transfer")
@Produces(MediaType.APPLICATION_JSON)
public class TransfersApplication {

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String sayPlainTextHello() {
		return "Hello";
	}
	
	@POST
	public Response submitTransaction(HashMap<String, Object> params) {
		try {
			if (!params.containsKey(Variables.USER_ID)) {
				return errorResponse();
			} else if (!params.containsKey(Variables.TO)) {
				return errorResponse();
			} else if (!params.containsKey(Variables.FROM)) {
				return errorResponse();
			} else if (!params.containsKey(Variables.AMOUNT)) {
				return errorResponse();
			} else if (!params.containsKey(Variables.CURRENCY)) {
				return errorResponse();
			}

			String userid = params.get(Variables.USER_ID).toString();
			int to_account = Integer.parseInt(params.get(Variables.TO).toString());
			int from_account = Integer.parseInt(params.get(Variables.FROM).toString());
			double amount = Double.parseDouble(params.get(Variables.AMOUNT).toString());
			String currency = params.get(Variables.CURRENCY).toString();

			Map<String, Object> response = TransfersUtil.submitTransaction(userid, from_account, to_account, amount, currency);
			if (response.containsKey("error")) {
				return Response.status((int) response.get("status")).entity(JsonUtil.stringify(response)).build();
			}
			return Response.ok(JsonUtil.stringify(response), MediaType.APPLICATION_JSON).build();
		} catch (Exception e) {
			return Response.status(Response.Status.BAD_GATEWAY).build();
		}
	}
	
	public Response errorResponse() throws Exception {
		return Response.status(Response.Status.BAD_REQUEST).entity(JsonUtil.errorJson("TRANS-1000", "Invalid request")).build();
	}

}