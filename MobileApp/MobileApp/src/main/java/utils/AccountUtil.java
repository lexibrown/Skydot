package utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import data.Account;
import data.Transaction;

public class AccountUtil {

	public static final String SERVICE = "ACCT";

	public static Map<String, Object> getAccountSummary(String userId) throws Exception {
		Map<String, Object> params = new HashMap<>();
		params.put(Variables.USER_ID, userId);

		Map<String, Object> response = Connection.sendRequest(Endpoint.ACCOUNT_SUMMARY, params);

		if (response.containsKey(Variables.ACCOUNTS) && response.get(Variables.ACCOUNTS) == null) {
			response.put(Variables.ACCOUNTS, new ArrayList<Account>());
		}
		return response;
	}

	public static Map<String, Object> getAccountDetails(String userId, int accountId) throws Exception {
		Map<String, Object> params = new HashMap<>();
		params.put(Variables.USER_ID, userId);
		params.put(Variables.ACCOUNT_ID, accountId);

		Map<String, Object> response = Connection.sendRequest(Endpoint.ACCOUNT_DETAILS, params);

		if (response.containsKey(Variables.TRANSACTIONS) && response.get(Variables.TRANSACTIONS) == null) {
			response.put(Variables.TRANSACTIONS, new ArrayList<Transaction>());
		}
		return response;
	}

}
