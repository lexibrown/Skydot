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
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import data.Account;
import data.History;
import utils.BankDatabaseUtil;
import utils.JsonUtil;

@Path("/host")
@Produces(MediaType.APPLICATION_JSON)
public class AccountApplication {

	private static final Logger log = LogManager.getLogger(AccountApplication.class);

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
	public Response verify(HashMap<String, Object> params) {
		try {
			if (!params.containsKey(USERID)) {
				return noUserGivenResponse();
			}
			if (!params.containsKey(PASSWORD)) {
				return noPasswordGivenResponse();
			}
			String username = params.get(USERID).toString();
			String password = params.get(PASSWORD).toString();

			if (BankDatabaseUtil.userExists(username, password)) {
				return success("Verifed user.");
			}
			return invalidLoginResponse();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return crashResponse();
		}
	}

	@GET
	@Path(ACCOUNT_SUMMARY)
	public Response accountSummary(@PathParam(USERID) String userid) {
		try {
			List<Account> accounts = BankDatabaseUtil.getAccountSummary(userid);
			if (accounts == null || accounts.isEmpty()) {
				return noUserResponse();
			}
			return Response.ok(JsonUtil.makeJson(ACCOUNTS, accounts), MediaType.APPLICATION_JSON).build();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return crashResponse();
		}
	}

	@GET
	@Path(ACCOUNT_DETAILS)
	public Response accountDetails(@PathParam(USERID) String userid, @PathParam(ACCOUNTID) int accountid) {
		try {
			Account account = BankDatabaseUtil.getAccount(userid, accountid);
			if (account == null) {
				return noAccountResponse();
			}

			Map<String, Object> accountMap = account.toMap();

			List<History> transations = BankDatabaseUtil.getTransactions(account.getId());
			accountMap.put(TRANSACTIONS, transations);

			return Response.ok(JsonUtil.stringify(accountMap), MediaType.APPLICATION_JSON).build();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return crashResponse();
		}
	}

	@POST
	@Path(TRANSFER)
	public Response transfer(HashMap<String, Object> params) {
		try {
			if (!params.containsKey(USERID)) {
				return noUserGivenResponse();
			} else if (!params.containsKey(TO)) {
				return missingResponse();
			} else if (!params.containsKey(FROM)) {
				return missingResponse();
			} else if (!params.containsKey(AMOUNT)) {
				return missingResponse();
			} else if (!params.containsKey(CURRENCY)) {
				return missingResponse();
			}

			String userid = params.get(USERID).toString();
			int to_account = Integer.parseInt(params.get(TO).toString());
			int from_account = Integer.parseInt(params.get(FROM).toString());
			double amount = Double.parseDouble(params.get(AMOUNT).toString());
			String currency = params.get(CURRENCY).toString();

			if (BankDatabaseUtil.getAccount(userid, from_account) == null) {
				return noAccountResponse();
			} else if (BankDatabaseUtil.getAccount(userid, to_account) == null) {
				return noAccountResponse();
			} else if (BankDatabaseUtil.makeTransfer(userid, from_account, to_account, amount, currency)) {
				return success("Successfully made transfer.");
			}
			return failTransferResponse();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return crashResponse();
		}
	}

	@POST
	@Path(BILL_PAYMENT)
	public Response billPayment(HashMap<String, Object> params) {
		try {
			if (!params.containsKey(USERID)) {
				return noUserGivenResponse();
			} else if (!params.containsKey(PAYEE)) {
				return missingResponse();
			} else if (!params.containsKey(FROM)) {
				return missingResponse();
			} else if (!params.containsKey(AMOUNT)) {
				return missingResponse();
			} else if (!params.containsKey(CURRENCY)) {
				return missingResponse();
			}

			String userid = params.get(USERID).toString();
			int to_payee = Integer.parseInt(params.get(PAYEE).toString());
			int from_account = Integer.parseInt(params.get(FROM).toString());
			double amount = Double.parseDouble(params.get(AMOUNT).toString());
			String currency = params.get(CURRENCY).toString();

			if (BankDatabaseUtil.getAccount(userid, from_account) == null) {
				return noAccountResponse();
			} else if (BankDatabaseUtil.getPayee(to_payee) == null) {
				return noPayeeResponse();
			} else if (BankDatabaseUtil.makePayment(userid, from_account, to_payee, amount, currency)) {
				return success("Successfully made bill payment.");
			}
			return failBillResponse();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return crashResponse();
		}
	}

	@GET
	@Path(BILL_PAYEE)
	public Response billPayee() {
		try {
			return Response.ok(JsonUtil.makeJson(PAYEES, BankDatabaseUtil.getPayees()), MediaType.APPLICATION_JSON)
					.build();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return crashResponse();
		}
	}

	@POST
	@Path(BILL_PAYEE)
	public Response billPayee(HashMap<String, Object> params) {
		try {
			if (!params.containsKey(SEARCH)) {
				return missingResponse();
			}

			String search = params.get(SEARCH).toString();
			return Response
					.ok(JsonUtil.makeJson(PAYEES, BankDatabaseUtil.searchPayees(search)), MediaType.APPLICATION_JSON)
					.build();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return crashResponse();
		}
	}

	@POST
	@Path(CREATE)
	public Response createUser(HashMap<String, Object> params) {
		try {
			if (!params.containsKey(USERID)) {
				return noUserGivenResponse();
			}
			if (!params.containsKey(PASSWORD)) {
				return noPasswordGivenResponse();
			}
			String userid = params.get(USERID).toString();
			String password = params.get(PASSWORD).toString();

			if (BankDatabaseUtil.userExists(userid)) {
				return takenUserResponse();
			} else if (!BankDatabaseUtil.addUser(userid, password)) {
				return failAddUserResponse();
			}
			return success("Created user.");
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return crashResponse();
		}
	}

	@POST
	@Path(DELETE)
	public Response removeUser(HashMap<String, Object> params) {
		try {
			if (!params.containsKey(USERID)) {
				return noUserGivenResponse();
			}
			if (!params.containsKey(PASSWORD)) {
				return noPasswordGivenResponse();
			}
			String userid = params.get(USERID).toString();
			String password = params.get(PASSWORD).toString();

			if (!BankDatabaseUtil.userExists(userid, password)) {
				return invalidLoginResponse();
			}

			if (BankDatabaseUtil.removeUser(userid)) {
				return success("User successfully removed.");
			}
			return invalidLoginResponse();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return crashResponse();
		}
	}

	public Response success(String message) throws Exception {
		return Response.ok(JsonUtil.makeJson(MESSAGE, message), MediaType.APPLICATION_JSON).build();
	}

	public Response invalidResponse() throws Exception {
		return Response.status(Response.Status.BAD_REQUEST)
				.entity(JsonUtil.errorJson(SERVICE + "-2000", "Invalid request.")).build();
	}

	public Response noUserGivenResponse() throws Exception {
		return Response.status(Response.Status.BAD_REQUEST)
				.entity(JsonUtil.errorJson(SERVICE + "-1000", "No username provided.")).build();
	}

	public Response invalidLoginResponse() throws Exception {
		return Response.status(Response.Status.BAD_REQUEST)
				.entity(JsonUtil.errorJson(SERVICE + "-1001", "Invalid username and/or password.")).build();
	}

	public Response takenUserResponse() throws Exception {
		return Response.status(Response.Status.BAD_REQUEST)
				.entity(JsonUtil.errorJson(SERVICE + "-1002", "User id is taken. Please try a new one.")).build();
	}

	public Response noPasswordGivenResponse() throws Exception {
		return Response.status(Response.Status.BAD_REQUEST)
				.entity(JsonUtil.errorJson(SERVICE + "-1003", "No password provided.")).build();
	}

	public Response failAddUserResponse() throws Exception {
		return Response.status(Response.Status.BAD_REQUEST)
				.entity(JsonUtil.errorJson(SERVICE + "-1004", "Couldn't add user. Please try again.")).build();
	}

	public Response noUserResponse() throws Exception {
		return Response.status(Response.Status.BAD_REQUEST)
				.entity(JsonUtil.errorJson(SERVICE + "-1005", "No such user.")).build();
	}

	public Response noAccountResponse() throws Exception {
		return Response.status(Response.Status.BAD_REQUEST)
				.entity(JsonUtil.errorJson(SERVICE + "-1006", "No such account.")).build();
	}

	public Response noPayeeResponse() throws Exception {
		return Response.status(Response.Status.BAD_REQUEST)
				.entity(JsonUtil.errorJson(SERVICE + "-1006", "No such payee.")).build();
	}

	public Response failTransferResponse() throws Exception {
		return Response.status(Response.Status.BAD_REQUEST)
				.entity(JsonUtil.errorJson(SERVICE + "-1007", "Failed to make transfer.")).build();
	}

	public Response failBillResponse() throws Exception {
		return Response.status(Response.Status.BAD_REQUEST)
				.entity(JsonUtil.errorJson(SERVICE + "-1008", "Failed to make bill payment.")).build();
	}

	public Response missingResponse() throws Exception {
		return Response.status(Response.Status.BAD_REQUEST)
				.entity(JsonUtil.errorJson(SERVICE + "-1009", "Important parameters missing.")).build();
	}

	public Response crashResponse() {
		try {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity(JsonUtil.errorJson(SERVICE + "-5000", "Something went wrong. Please try again.")).build();
		} catch (Exception e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		}
	}

}
