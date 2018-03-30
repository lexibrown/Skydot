package service;

import java.util.HashMap;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import utils.AccountUtil;
import utils.JsonUtil;
import utils.TokenUtil;

@Path("/account")
@Produces(MediaType.APPLICATION_JSON)
public class AccountApplication extends BaseApplication {

	private final static String ACCOUNT_ID = "{account_id}";

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String healthCheck() {
		return "Account Service";
	}
	
	@POST
	public String account(HashMap<String, Object> params) {
		try {
			if (!params.containsKey(TOKEN)) {
				return JsonUtil.errorJson(SERVICE + "-1000", "No token provided.");
			}

			String token = params.get(TOKEN).toString();
			String verify = TokenUtil.verifyToken(token);
			if (verify != null) {
				return verify;
			}
			return AccountUtil.getAccountSummary(TokenUtil.getUserId(token));
		} catch (Exception e) {
			e.printStackTrace();
			return JsonUtil.fail(e);
		}
	}

	@POST
	@Path(ACCOUNT_ID)
	public String accountDetails(HashMap<String, Object> params, @PathParam(ACCOUNT_ID) int accountId) {
		try {
			if (!params.containsKey(TOKEN)) {
				return JsonUtil.errorJson(SERVICE + "-1000", "No token provided.");
			}

			String token = params.get(TOKEN).toString();
			String verify = TokenUtil.verifyToken(token);
			if (verify != null) {
				return verify;
			}
			return AccountUtil.getAccountDetails(TokenUtil.getUserId(token), accountId);
		} catch (Exception e) {
			e.printStackTrace();
			return JsonUtil.fail(e);
		}
	}

}
