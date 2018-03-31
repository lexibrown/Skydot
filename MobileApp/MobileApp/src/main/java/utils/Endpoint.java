package utils;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Endpoint {

	public static final String HTTP = "http://";
	public static final String PORT = ":8080";

	public static final String AUTH_VERIFY = "/auth/verify";
	public static final String AUTH_USER = "/auth/user";

	public static final String ACCOUNT_SUMMARY = "/account/summary";
	public static final String ACCOUNT_DETAILS = "/account/details";

	public static final String USER_CREATE = "/user/create";
	public static final String USER_DELETE = "/user/delete";

	public static final String TRANSFER = "/transfer";

	public static final String BILL_PAYEE = "/bill/payee";
	public static final String BILL = "/bill";

	public static final Map<String, String> URLs;
	static {
		Map<String, String> aMap = new HashMap<>();
		aMap.put(AUTH_VERIFY, "auth-app");
		aMap.put(AUTH_USER, "auth-app");

		aMap.put(ACCOUNT_SUMMARY, "account-summary-service-python");
		aMap.put(ACCOUNT_DETAILS, "account-details-service-python");

		aMap.put(USER_CREATE, "create-service-cplus");
		aMap.put(USER_DELETE, "delete-service-cplus");
		// aMap.put(USER_CREATE, "create-service-python");
		// aMap.put(USER_DELETE, "delete-service-python");

		aMap.put(TRANSFER, "transfer-service-java");
		// aMap.put(TRANSFER, "transfer-service-python");
		// aMap.put(TRANSFER, "transfer-service-javascript");
		// aMap.put(TRANSFER, "transfer-service-cplus");

		aMap.put(BILL_PAYEE, "bill-payment-service-javascript");
		// aMap.put(BILL_PAYEE, "bill-payment-service-python");
		// aMap.put(BILL_PAYEE, "bill-payment-service-java");
		// aMap.put(BILL_PAYEE, "bill-payment-service-cplus");

		aMap.put(BILL, "bill-payee-service-javascript");
		// aMap.put(BILL, "bill-payee-service-python");
		// aMap.put(BILL, "bill-payee-service-java");

		URLs = Collections.unmodifiableMap(aMap);
	}

}
