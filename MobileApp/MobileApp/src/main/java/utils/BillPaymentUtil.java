package utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import data.Payee;

public class BillPaymentUtil {

	public static final String SERVICE = "BILL";

	public static String getPayees(String userId) throws Exception {
		Map<String, Object> params = new HashMap<>();
		params.put(Variables.USER_ID, userId);

		Map<String, Object> response = Connection.sendRequest(Endpoint.BILL_PAYEE, params);

		if (response.containsKey(Variables.ERROR)) {
			return JsonUtil.stringify(response);
		} else if (response.containsKey(Variables.PAYEES)) {
			if (response.get(Variables.PAYEES) == null) {
				return JsonUtil.makeJson(Variables.PAYEES, new ArrayList<Payee>());
			}
		}
		return JsonUtil.stringify(response);
	}
	
	public static String searchPayees(String userId, String search) throws Exception {
		Map<String, Object> params = new HashMap<>();
		params.put(Variables.USER_ID, userId);
		params.put(Variables.SEARCH, search);

		Map<String, Object> response = Connection.sendRequest(Endpoint.BILL_PAYEE_SEARCH, params);

		if (response.containsKey(Variables.ERROR)) {
			return JsonUtil.stringify(response);
		} else if (response.containsKey(Variables.PAYEES)) {
			if (response.get(Variables.PAYEES) == null) {
				return JsonUtil.makeJson(Variables.PAYEES, new ArrayList<Payee>());
			}
		}
		return JsonUtil.stringify(response);
	}

	public static String submitBillPayment(String userId, int fromId, int payee, double amount, String currency)
			throws Exception {
		Map<String, Object> params = new HashMap<>();
		params.put(Variables.USER_ID, userId);
		params.put(Variables.FROM_ACCOUNT, fromId);
		params.put(Variables.PAYEE, payee);
		params.put(Variables.AMOUNT, amount);
		params.put(Variables.CURRENCY, currency);

		Map<String, Object> response = Connection.sendRequest(Endpoint.BILL, params);
		return JsonUtil.stringify(response);
	}

}