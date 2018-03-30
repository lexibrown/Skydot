package utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import data.Account;
import data.Transaction;

public class AccountUtil {

	public static final String SERVICE = "ACCT";

	public static String getAccountSummary(String userId) throws Exception {
		Map<String, Object> params = new HashMap<>();
		params.put(Variables.USER_ID, userId);

		Map<String, Object> response = Connection.sendRequest(Endpoint.ACCOUNT_SUMMARY, params);

		if (response.containsKey(Variables.ERROR)) {
			return JsonUtil.stringify(response);
		} else if (response.containsKey(Variables.ACCOUNTS)) {
			if (response.get(Variables.ACCOUNTS) == null) {
				return JsonUtil.makeJson(Variables.ACCOUNTS, new ArrayList<Account>());
			}
		}
		return JsonUtil.stringify(response);
	}

	public static String getAccountDetails(String userId, int accountId) throws Exception {
		Map<String, Object> params = new HashMap<>();
		params.put(Variables.USER_ID, userId);
		params.put(Variables.ACCOUNT_ID, accountId);

		Map<String, Object> response = Connection.sendRequest(Endpoint.ACCOUNT_DETAILS, params);

		if (response.containsKey(Variables.ERROR)) {
			return JsonUtil.stringify(response);
		} else if (response.containsKey(Variables.TRANSACTIONS)) {
			if (response.get(Variables.TRANSACTIONS) == null) {
				response.put(Variables.TRANSACTIONS, new ArrayList<Transaction>());
				return JsonUtil.stringify(response);
			}
		}
		return JsonUtil.stringify(response);
	}

}
