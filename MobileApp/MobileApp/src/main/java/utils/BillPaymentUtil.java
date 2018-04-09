package utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import data.Payee;

public class BillPaymentUtil {

	public static final String SERVICE = "BILL";

	public static Map<String, Object> getPayees(String userId) throws Exception {
		Map<String, Object> params = new HashMap<>();
		params.put(Variables.USER_ID, userId);

		Map<String, Object> response = Connection.sendRequest(Endpoint.BILL_PAYEE, params);

		if (response.containsKey(Variables.PAYEES) && response.get(Variables.PAYEES) == null) {
			response.put(Variables.PAYEES, new ArrayList<Payee>());
		}
		return response;
	}

	public static Map<String, Object> searchPayees(String userId, String search) throws Exception {
		Map<String, Object> params = new HashMap<>();
		params.put(Variables.USER_ID, userId);
		params.put(Variables.SEARCH, search);

		Map<String, Object> response = Connection.sendRequest(Endpoint.BILL_PAYEE_SEARCH, params);

		if (response.containsKey(Variables.PAYEES) && response.get(Variables.PAYEES) == null) {
			response.put(Variables.PAYEES, new ArrayList<Payee>());
		}
		return response;
	}

	public static Map<String, Object> submitBillPayment(String userId, int fromId, int payee, double amount,
			String currency) throws Exception {
		Map<String, Object> params = new HashMap<>();
		params.put(Variables.USER_ID, userId);
		params.put(Variables.FROM_ACCOUNT, fromId);
		params.put(Variables.PAYEE, payee);
		params.put(Variables.AMOUNT, amount);
		params.put(Variables.CURRENCY, currency);

		return Connection.sendRequest(Endpoint.BILL, params);
	}

}