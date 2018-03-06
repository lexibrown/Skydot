package utils;

import java.util.HashMap;
import java.util.Map;

public class TokenUtil {

	public static String verifyToken(String token) throws Exception {
		Map<String, Object> params = new HashMap<>();
		params.put(Variables.TOKEN, token);

		Map<String, Object> response = Connection.sendRequest(Endpoint.AUTH_VERIFY, params);

		if (!response.containsKey(Variables.ERROR)) {
			return null;
		}
		return JsonUtil.stringify(response);
	}

	public static String getUserId(String token) throws Exception {
		Map<String, Object> params = new HashMap<>();
		params.put(Variables.TOKEN, token);

		Map<String, Object> response = Connection.sendRequest(Endpoint.AUTH_USER, params);

		if (response.containsKey(Variables.USER_ID)) {
			return response.get(Variables.USER_ID).toString();
		}
		return null;
	}

}
