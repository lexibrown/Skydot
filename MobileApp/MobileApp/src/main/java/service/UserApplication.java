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
import utils.UserUtil;
import utils.Variables;

@Path("/user")
@Produces(MediaType.APPLICATION_JSON)
public class UserApplication extends BaseApplication {

	private static final Logger log = LogManager.getLogger(UserApplication.class);
	
	private final static String CREATE = "/create";
	private final static String DELETE = "/delete";

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String healthCheck() {
		return "User Service";
	}

	@POST
	@Path(CREATE)
	public Response createUser(HashMap<String, Object> params) {
		try {
			if (!params.containsKey(Variables.USER_ID)) {
				return invalidResponse(); 
			} else if (!params.containsKey(Variables.PASSWORD)) {
				return invalidResponse(); 
			}

			String userId = params.get(Variables.USER_ID).toString();
			String password = params.get(Variables.PASSWORD).toString();
			
			Map<String, Object> response = UserUtil.createUser(userId, password);
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
	@Path(DELETE)
	public Response deleteUser(HashMap<String, Object> params) {
		try {
			if (!params.containsKey(Variables.USER_ID)) {
				return invalidResponse(); 
			} else if (!params.containsKey(Variables.PASSWORD)) {
				return invalidResponse(); 
			}

			String userId = params.get(Variables.USER_ID).toString();
			String password = params.get(Variables.PASSWORD).toString();
			
			Map<String, Object> response = UserUtil.deleteUser(userId, password);
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