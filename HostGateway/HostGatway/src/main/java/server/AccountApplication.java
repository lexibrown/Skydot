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
import data.Transaction;
import utils.JsonUtil;
import utils.BankDatabaseUtil;

@Path("/host")
@Produces(MediaType.APPLICATION_JSON)
public class AccountApplication {

	public static final String SERVICE = "HOST";

	public static final String ACCOUNT_SUMMARY = "{userid}";
	public static final String ACCOUNT_DETAILS = "{userid}/{accountid}";

	public static final String CREATE = "create";
	public static final String DELETE = "delete";
	public static final String VERIFY = "verify";

	public static final String WITHDRAW = "withdraw/{userid}/{accountid}/{amount}/{currency}";
	public static final String DEPOSIT = "deposit/{userid}/{accountid}/{amount}/{currency}";

	public static final String ACCOUNTID = "accountid";
	public static final String USERID = "userid";
	public static final String PASSWORD = "password";
	public static final String AMOUNT = "amount";
	public static final String CURRENCY = "currency";

	public static final String SUCCESS = "success";
	public static final String ACCOUNTS = "accounts";
	public static final String TRANSACTIONS = "transactions";
	public static final String MESSAGE = "message";

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String defaultEndpoint() {
		return "Host gateway service.";
	}

	/**
	 * Validate that the userid and password are correct and authorize the user
	 * { userid: "username1", password: "pass12345", languageCode: "en" }
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
			return JsonUtil.makeJson(ACCOUNTS, JsonUtil.stringify(accounts));
		} catch (Exception e) {
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

			List<Transaction> transations = BankDatabaseUtil.getTransactions(account.getId());
			accountMap.put(TRANSACTIONS, JsonUtil.stringify(transations));

			return JsonUtil.stringify(accountMap);
		} catch (Exception e) {
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
