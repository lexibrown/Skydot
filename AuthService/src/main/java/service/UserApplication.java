package service;

import java.util.HashMap;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import utils.JsonUtil;
import utils.UserUtil;

@Path("/user")
@Produces(MediaType.APPLICATION_JSON)
public class UserApplication extends BaseApplication {

	private final static String CREATE = "/create";
	private final static String DELETE = "/delete";

	private final static String USER_ID = "user_id";
	private final static String PASSWORD = "password";

	@POST
	@Path(CREATE)
	public String createUser(HashMap<String, Object> params) {
		try {
			String userId = "";
			String password = "";
			if (params.containsKey(USER_ID)) {
				userId = params.get(USER_ID).toString();
			}
			if (params.containsKey(PASSWORD)) {
				password = params.get(PASSWORD).toString();
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
			if (params.containsKey(USER_ID)) {
				userId = params.get(USER_ID).toString();
			}
			if (params.containsKey(PASSWORD)) {
				password = params.get(PASSWORD).toString();
			}

			return UserUtil.deleteUser(userId, password);
		} catch (Exception e) {
			e.printStackTrace();
			return JsonUtil.fail(e);
		}
	}

}