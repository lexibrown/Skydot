package server;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import data.Account;
import data.History;
import utils.JsonUtil;
import utils.BankDatabaseUtil;

@Path("/host")
@Produces(MediaType.APPLICATION_JSON)
public class AccountApplication {

	public static final String SERVICE = "HOST";

	public static final String ACCOUNT_SUMMARY = "{user_id}";
	public static final String ACCOUNT_DETAILS = "{user_id}/{account_id}";

	public static final String CREATE = "create";
	public static final String DELETE = "delete";
	public static final String VERIFY = "verify";

	public static final String TRANSFER = "transfer";
	public static final String BILL_PAYMENT = "bill";
	public static final String BILL_PAYEE = "bill/payee";

	public static final String WITHDRAW = "withdraw/{user_id}/{account_id}/{amount}/{currency}";
	public static final String DEPOSIT = "deposit/{user_id}/{account_id}/{amount}/{currency}";

	public static final String ACCOUNTID = "account_id";
	public static final String USERID = "user_id";
	public static final String PASSWORD = "password";
	public static final String AMOUNT = "amount";
	public static final String CURRENCY = "currency";
	public static final String TO = "to_account";
	public static final String FROM = "from_account";
	public static final String PAYEE = "payee";
	public static final String SEARCH = "search";

	public static final String SUCCESS = "success";
	public static final String ACCOUNTS = "accounts";
	public static final String TRANSACTIONS = "transactions";
	public static final String PAYEES = "payees";
	public static final String MESSAGE = "message";

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String defaultEndpoint() {
		return "Host gateway service.";
	}

	/**
	 * Validate that the userid and password are correct and authorize the user
	 * { user_id: "username1", password: "pass12345", languageCode: "en" }
	 * 
	 * @return Success if user is valid
	 */
	@POST
	@Path(VERIFY)
	public String verify(HashMap<String, Object> params) {
		try {
			if (!params.containsKey(USERID)) {
				return JsonUtil.errorJson(SERVICE + "-1000", "No username provided.");
			}
			if (!params.containsKey(PASSWORD)) {
				return JsonUtil.errorJson(SERVICE + "-1003", "No password provided.");
			}
			String username = params.get(USERID).toString();
			String password = params.get(PASSWORD).toString();

			if (BankDatabaseUtil.userExists(username, password)) {
				return JsonUtil.makeJson(SUCCESS, "Verifed user.");
			}
			return JsonUtil.errorJson(SERVICE + "-1001", "Invalid username and/or password.");
		} catch (Exception e) {
			return JsonUtil.fail(e);
		}
	}

	@GET
	@Path(ACCOUNT_SUMMARY)
	public String accountSummary(@PathParam(USERID) String userid) {
		try {
			List<Account> accounts = BankDatabaseUtil.getAccountSummary(userid);
			if (accounts == null || accounts.isEmpty()) {
				return JsonUtil.errorJson(SERVICE + "-1005", "No such user.");
			}
			return JsonUtil.makeJson(ACCOUNTS, accounts);
		} catch (Exception e) {
			e.printStackTrace();
			return JsonUtil.fail(e);
		}
	}

	@GET
	@Path(ACCOUNT_DETAILS)
	public String accountDetails(@PathParam(USERID) String userid, @PathParam(ACCOUNTID) int accountid) {
		try {
			Account account = BankDatabaseUtil.getAccount(userid, accountid);
			if (account == null) {
				return JsonUtil.errorJson(SERVICE + "-1006", "No such account.");
			}

			Map<String, Object> accountMap = account.toMap();

			List<History> transations = BankDatabaseUtil.getTransactions(account.getId());
			accountMap.put(TRANSACTIONS, transations);

			return JsonUtil.stringify(accountMap);
		} catch (Exception e) {
			e.printStackTrace();
			return JsonUtil.fail(e);
		}
	}

	@POST
	@Path(TRANSFER)
	public String transfer(HashMap<String, Object> params) {
		try {
			if (!params.containsKey(USERID)) {
				return JsonUtil.errorJson(SERVICE + "-1000", "No username provided.");
			} else if (!params.containsKey(TO)) {
				return JsonUtil.errorJson(SERVICE + "-1009", "Important parameters missing.");
			} else if (!params.containsKey(FROM)) {
				return JsonUtil.errorJson(SERVICE + "-1009", "Important parameters missing.");
			} else if (!params.containsKey(AMOUNT)) {
				return JsonUtil.errorJson(SERVICE + "-1009", "Important parameters missing.");
			} else if (!params.containsKey(CURRENCY)) {
				return JsonUtil.errorJson(SERVICE + "-1009", "Important parameters missing.");
			}

			String userid = params.get(USERID).toString();
			int to_account = Integer.parseInt(params.get(TO).toString());
			int from_account = Integer.parseInt(params.get(FROM).toString());
			double amount = Double.parseDouble(params.get(AMOUNT).toString());
			String currency = params.get(CURRENCY).toString();

			if (BankDatabaseUtil.getAccount(userid, from_account) == null) {
				return JsonUtil.errorJson(SERVICE + "-1006", "No such account.");
			} else if (BankDatabaseUtil.getAccount(userid, to_account) == null) {
				return JsonUtil.errorJson(SERVICE + "-1006", "No such account.");
			} else if (BankDatabaseUtil.makeTransfer(userid, from_account, to_account, amount, currency)) {
				return JsonUtil.makeJson(SUCCESS, "Successfully made transfer.");
			}
			return JsonUtil.errorJson(SERVICE + "-1007", "Failed to make transfer.");
		} catch (Exception e) {
			return JsonUtil.fail(e);
		}
	}

	@POST
	@Path(BILL_PAYMENT)
	public String billPayment(HashMap<String, Object> params) {
		try {
			if (!params.containsKey(USERID)) {
				return JsonUtil.errorJson(SERVICE + "-1000", "No username provided.");
			} else if (!params.containsKey(PAYEE)) {
				return JsonUtil.errorJson(SERVICE + "-1009", "Important parameters missing.");
			} else if (!params.containsKey(FROM)) {
				return JsonUtil.errorJson(SERVICE + "-1009", "Important parameters missing.");
			} else if (!params.containsKey(AMOUNT)) {
				return JsonUtil.errorJson(SERVICE + "-1009", "Important parameters missing.");
			} else if (!params.containsKey(CURRENCY)) {
				return JsonUtil.errorJson(SERVICE + "-1009", "Important parameters missing.");
			}

			String userid = params.get(USERID).toString();
			int to_payee = Integer.parseInt(params.get(PAYEE).toString());
			int from_account = Integer.parseInt(params.get(FROM).toString());
			double amount = Double.parseDouble(params.get(AMOUNT).toString());
			String currency = params.get(CURRENCY).toString();

			if (BankDatabaseUtil.getAccount(userid, from_account) == null) {
				return JsonUtil.errorJson(SERVICE + "-1006", "No such account.");
			} else if (BankDatabaseUtil.getPayee(to_payee) == null) {
				return JsonUtil.errorJson(SERVICE + "-1006", "No such payee.");
			} else if (BankDatabaseUtil.makePayment(userid, from_account, to_payee, amount, currency)) {
				return JsonUtil.makeJson(SUCCESS, "Successfully made bill payment.");
			}
			return JsonUtil.errorJson(SERVICE + "-1007", "Failed to make bill payment.");
		} catch (Exception e) {
			return JsonUtil.fail(e);
		}
	}

	@GET
	@Path(BILL_PAYEE)
	public String billPayee() {
		try {
			return JsonUtil.makeJson(PAYEES, BankDatabaseUtil.getPayees());
		} catch (Exception e) {
			e.printStackTrace();
			return JsonUtil.fail(e);
		}
	}

	@POST
	@Path(BILL_PAYEE)
	public String billPayee(HashMap<String, Object> params) {
		try {
			if (!params.containsKey(SEARCH)) {
				return JsonUtil.errorJson(SERVICE + "-1009", "Important parameters missing.");
			}

			String search = params.get(SEARCH).toString();
			return JsonUtil.makeJson(PAYEES, BankDatabaseUtil.searchPayees(search));
		} catch (Exception e) {
			e.printStackTrace();
			return JsonUtil.fail(e);
		}
	}

	@GET
	@Path(WITHDRAW)
	public String withdraw(@PathParam(USERID) String userid, @PathParam(ACCOUNTID) int accountid,
			@PathParam(AMOUNT) double amount, @PathParam(CURRENCY) String currency) {
		try {
			if (BankDatabaseUtil.getAccount(userid, accountid) == null) {
				return JsonUtil.errorJson(SERVICE + "-1006", "No such account.");
			} else if (BankDatabaseUtil.withdraw(userid, accountid, amount, currency)) {
				return JsonUtil.makeJson(SUCCESS, "Successfully made withdrawal.");
			}
			return JsonUtil.errorJson(SERVICE + "-1007", "Failed to make withdrawal");
		} catch (Exception e) {
			return JsonUtil.fail(e);
		}
	}

	@GET
	@Path(DEPOSIT)
	public String deposit(@PathParam(USERID) String userid, @PathParam(ACCOUNTID) int accountid,
			@PathParam(AMOUNT) double amount, @PathParam(CURRENCY) String currency) {
		try {
			if (BankDatabaseUtil.getAccount(userid, accountid) == null) {
				return JsonUtil.errorJson(SERVICE + "-1006", "No such account.");
			} else if (BankDatabaseUtil.deposit(userid, accountid, amount, currency)) {
				return JsonUtil.makeJson(SUCCESS, "Successfully made deposit.");
			}
			return JsonUtil.errorJson(SERVICE + "-1008", "Failed to make deposit");
		} catch (Exception e) {
			return JsonUtil.fail(e);
		}
	}

	@POST
	@Path(CREATE)
	public String createUser(HashMap<String, Object> params) {
		try {
			if (!params.containsKey(USERID)) {
				return JsonUtil.errorJson(SERVICE + "-1000", "No username provided.");
			}
			if (!params.containsKey(PASSWORD)) {
				return JsonUtil.errorJson(SERVICE + "-1003", "No password provided.");
			}
			String userid = params.get(USERID).toString();
			String password = params.get(PASSWORD).toString();

			if (BankDatabaseUtil.userExists(userid)) {
				return JsonUtil.errorJson(SERVICE + "-1002", "Username is taken. Please try a new one.");
			} else if (!BankDatabaseUtil.addUser(userid, password)) {
				return JsonUtil.errorJson(SERVICE + "-1004", "Couldn't add user. Please try again.");
			}
			return JsonUtil.makeJson(SUCCESS, "Created user.");
		} catch (Exception e) {
			return JsonUtil.fail(e);
		}
	}

	@POST
	@Path(DELETE)
	public String removeUser(HashMap<String, Object> params) {
		try {
			if (!params.containsKey(USERID)) {
				return JsonUtil.errorJson(SERVICE + "-1000", "No username provided.");
			}
			if (!params.containsKey(PASSWORD)) {
				return JsonUtil.errorJson(SERVICE + "-1003", "No password provided.");
			}
			String userid = params.get(USERID).toString();
			String password = params.get(PASSWORD).toString();

			if (!BankDatabaseUtil.userExists(userid, password)) {
				return JsonUtil.errorJson(SERVICE + "-1001", "Invalid username and/or password.");
			}

			if (BankDatabaseUtil.removeUser(userid)) {
				return JsonUtil.makeJson(SUCCESS, "User successfully removed.");
			}
			return JsonUtil.errorJson(SERVICE + "-1001", "Invalid username and/or password.");
		} catch (Exception e) {
			return JsonUtil.fail(e);
		}
	}

}
