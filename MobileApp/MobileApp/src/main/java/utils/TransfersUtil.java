package utils;

import java.util.HashMap;
import java.util.Map;

public class TransfersUtil {

	public static final String SERVICE = "TRAN";

	public static Map<String, Object> submitTransaction(String userId, int fromId, int toId, double amount,
			String currency) throws Exception {
		Map<String, Object> params = new HashMap<>();
		params.put(Variables.USER_ID, userId);
		params.put(Variables.FROM_ACCOUNT, fromId);
		params.put(Variables.TO_ACCOUNT, toId);
		params.put(Variables.AMOUNT, amount);
		params.put(Variables.CURRENCY, currency);

		return Connection.sendRequest(Endpoint.TRANSFER, params);
	}

}