package service;

import java.util.HashMap;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import utils.JsonUtil;
import utils.UserUtil;
import utils.Variables;

@Path("/user")
@Produces(MediaType.APPLICATION_JSON)
public class UserApplication extends BaseApplication {

	private final static String CREATE = "/create";
	private final static String DELETE = "/delete";

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String healthCheck() {
		return "User Service";
	}

	@POST
	@Path(CREATE)
	public String createUser(HashMap<String, Object> params) {
		try {
			String userId = "";
			String password = "";
			if (params.containsKey(Variables.USER_ID)) {
				userId = params.get(Variables.USER_ID).toString();
			}
			if (params.containsKey(Variables.PASSWORD)) {
				password = params.get(Variables.PASSWORD).toString();
			}
			return UserUtil.createUser(userId, password);
		} catch (Exception e) {
			e.printStackTrace();
			return JsonUtil.fail(e);
		}
	}

	@POST
	@Path(DELETE)
	public String deleteUser(HashMap<String, Object> params) {
		try {
			String userId = "";
			String password = "";
			if (params.containsKey(Variables.USER_ID)) {
				userId = params.get(Variables.USER_ID).toString();
			}
			if (params.containsKey(Variables.PASSWORD)) {
				password = params.get(Variables.PASSWORD).toString();
			}
			return UserUtil.deleteUser(userId, password);
		} catch (Exception e) {
			e.printStackTrace();
			return JsonUtil.fail(e);
		}
	}

}