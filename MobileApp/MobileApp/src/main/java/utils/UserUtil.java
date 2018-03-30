package utils;

import java.util.HashMap;
import java.util.Map;

public class UserUtil {

	public static final String SERVICE = "USER";

	public static String createUser(String userId, String password) throws Exception {
		Map<String, Object> params = new HashMap<>();
		params.put(Variables.USER_ID, userId);
		params.put(Variables.PASSWORD, password);

		Map<String, Object> response = Connection.sendRequest(Endpoint.USER_CREATE, params);
		return JsonUtil.stringify(response);
	}

	public static String deleteUser(String userId, String password) throws Exception {
		Map<String, Object> params = new HashMap<>();
		params.put(Variables.USER_ID, userId);
		params.put(Variables.PASSWORD, password);

		Map<String, Object> response = Connection.sendRequest(Endpoint.USER_DELETE, params);
		return JsonUtil.stringify(response);
	}

}
