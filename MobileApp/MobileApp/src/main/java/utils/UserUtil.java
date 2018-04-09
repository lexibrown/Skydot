package utils;

import java.util.HashMap;
import java.util.Map;

public class UserUtil {

	public static final String SERVICE = "USER";

	public static Map<String, Object> createUser(String userId, String password) throws Exception {
		Map<String, Object> params = new HashMap<>();
		params.put(Variables.USER_ID, userId);
		params.put(Variables.PASSWORD, password);

		return Connection.sendRequest(Endpoint.USER_CREATE, params);
	}

	public static Map<String, Object> deleteUser(String userId, String password) throws Exception {
		Map<String, Object> params = new HashMap<>();
		params.put(Variables.USER_ID, userId);
		params.put(Variables.PASSWORD, password);

		return Connection.sendRequest(Endpoint.USER_DELETE, params);
	}

}
