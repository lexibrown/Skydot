package utils;

import java.util.HashMap;
import java.util.Map;

public class BillPaymentUtil {

	public static final String SERVICE = "BILL";

	public static String getPayees(String userId) throws Exception {
		Map<String, Object> params = new HashMap<>();
		params.put(Variables.USER_ID, userId);

		Map<String, Object> response = Connection.sendRequest(Endpoint.BILL_PAYEE, params);

		if (response.containsKey(Variables.ERROR)) {
			return JsonUtil.stringify(response);
		}
		// TODO change to fit response
		return JsonUtil.stringify(response);
	}

	public static String submitBillPayment(String userId, int fromId, int toId, double amount, String currency)
			throws Exception {
		Map<String, Object> params = new HashMap<>();
		params.put(Variables.USER_ID, userId);
		params.put(Variables.FROM, fromId);
		params.put(Variables.TO, toId);
		params.put(Variables.AMOUNT, amount);
		params.put(Variables.CURRENCY, currency);

		Map<String, Object> response = Connection.sendRequest(Endpoint.BILL, params);
		return JsonUtil.stringify(response);
	}

}