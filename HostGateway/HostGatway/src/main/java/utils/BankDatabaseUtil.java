package utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.skydot.Client;
import com.skydot.paymentservice.PaymentServicePort;
import com.skydot.paymentservice.types.Search;

import data.Account;
import data.History;
import data.Payee;
import data.Profile;
import database.HibernateDatabaseAccountManager;
import database.HibernateDatabaseProfileManager;
import database.HibernateDatabaseTransactionManager;

public class BankDatabaseUtil {

	public static boolean userExists(String username) {
		return HibernateDatabaseProfileManager.getDefault().profileExists(username);
	}

	public static boolean userExists(String username, String password) {
		return HibernateDatabaseProfileManager.getDefault().profileExists(username, password);
	}

	public static Profile getUser(String username) {
		return HibernateDatabaseProfileManager.getDefault().getProfileWithName(username);
	}

	public static Profile getUserWithoutPassword(String username) {
		Profile profile = getUser(username);
		if (profile == null) {
			return null;
		}
		profile.setPassword(null);
		return profile;
	}

	public static boolean removeUser(String username) {
		List<Account> accounts = getAccountSummary(username);
		for (Account a : accounts) {
			List<History> transactions = getTransactions(a.getId());
			for (History t : transactions) {
				HibernateDatabaseTransactionManager.getDefault().delete(t);
			}
			HibernateDatabaseAccountManager.getDefault().delete(a);
		}

		return HibernateDatabaseProfileManager.getDefault().delete(username);
	}

	public static boolean updateProfile(Profile profile) {
		return HibernateDatabaseProfileManager.getDefault().update(profile);
	}

	public static List<Account> getAccountSummary(String userid) {
		return HibernateDatabaseAccountManager.getDefault().getAccountsWithName(userid);
	}

	public static Account getAccount(String userid, int accountid) {
		List<Account> accounts = getAccountSummary(userid);

		for (Account a : accounts) {
			if (a.getId() == accountid) {
				return a;
			}
		}
		return null;
	}

	public static List<History> getTransactions(int accountid) {
		return HibernateDatabaseTransactionManager.getDefault().getTransactions(accountid);
	}

	public static boolean makeTransfer(String userid, int from, int to, double amount,
			String currency) {
		Account from_account = getAccount(userid, from);
		Account to_account = getAccount(userid, to);

		if (from_account == null) {
			return false;
		} else if (to_account == null) {
			return false;
		} else if (amount < 0.0) {
			return false;
		}
		
		if (from_account.getType().equals("Investing")) {
			return false;
		}

		if (Variables.CAD.equalsIgnoreCase(currency)) {
			from_account.setBalanceCAD(from_account.getBalanceCAD() - amount);
			to_account.setBalanceCAD(to_account.getBalanceCAD() + amount);
		} else if (Variables.USD.equalsIgnoreCase(currency)) {
			from_account.setBalanceUSD(from_account.getBalanceUSD() - amount);
			to_account.setBalanceUSD(to_account.getBalanceUSD() + amount);
		} else {
			return false;
		}

		if (!HibernateDatabaseAccountManager.getDefault().update(from_account)) {
			return false;
		} else if (!HibernateDatabaseAccountManager.getDefault().update(to_account)) {
			return false;
		}
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");

		History from_history = new History();
		from_history.setAccountid(from);
		from_history.setAmount(-amount);
		from_history.setDate(format.format(new Date()));
		from_history.setName("Transfer to account " + to);
		from_history.setUserid(userid);

		int numTransactions = getTransactions(from).size();
		from_history.setId(numTransactions + 1);

		HibernateDatabaseTransactionManager.getDefault().add(from_history);


		History to_history = new History();
		to_history.setAccountid(to);
		to_history.setAmount(amount);
		to_history.setDate(format.format(new Date()));
		to_history.setName("Transfer from account " + from);
		to_history.setUserid(userid);

		numTransactions = getTransactions(to).size();
		to_history.setId(numTransactions + 1);

		HibernateDatabaseTransactionManager.getDefault().add(to_history);

		return true;
	}
	
	public static boolean makePayment(String userid, int from_account, int to_payee, double amount, String currency) {
		Account account = getAccount(userid, from_account);
		Payee payee = getPayee(to_payee);

		if (account == null) {
			return false;
		} else if (payee == null) {
			return false;
		} else if (amount < 0.0) {
			return false;
		}
		
		if (account.getType().equals("Investing")) {
			return false;
		}

		if (Variables.CAD.equalsIgnoreCase(currency)) {
			account.setBalanceCAD(account.getBalanceCAD() - amount);
		} else if (Variables.USD.equalsIgnoreCase(currency)) {
			account.setBalanceUSD(account.getBalanceUSD() - amount);
		} else {
			return false;
		}

		if (!HibernateDatabaseAccountManager.getDefault().update(account)) {
			return false;
		}
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");

		History from_history = new History();
		from_history.setAccountid(from_account);
		from_history.setAmount(-amount);
		from_history.setDate(format.format(new Date()));
		from_history.setName("Bill payment to " + payee.getName());
		from_history.setUserid(userid);

		int numTransactions = getTransactions(from_account).size();
		from_history.setId(numTransactions + 1);

		HibernateDatabaseTransactionManager.getDefault().add(from_history);

		return true;
	}

	public static Payee getPayee(int to_payee) {
		List<Payee> payees = getPayees();
		if (payees == null || payees.isEmpty()) {
			return null;
		}
		for (Payee p : payees) {
			if (p.getId() == to_payee) {
				return p;				
			}
		}
		return null;
	}
	
	public static List<Payee> getPayees() {
		// TODO Auto-generated method stub
		
		List<Payee> payees = new ArrayList<>();
		payees.add(new Payee(1, "Bill1"));
		payees.add(new Payee(2, "Bill2"));
		payees.add(new Payee(3, "Bill3"));
		payees.add(new Payee(4, "Bill4"));
		payees.add(new Payee(5, "Bill5"));
		
		return payees;
	}
	
	public static List<Payee> searchPayees(String searchStr) {
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:/META-INF/spring/cxf-requester.xml");
		PaymentServicePort client = (PaymentServicePort) applicationContext.getBean("payeeRequesterBean");

		Search search = new Search();
		search.setSearch(searchStr);
		List<com.skydot.paymentservice.types.Payees.Payee> payeeResults = client.search(search).getPayee();
		List<Payee> payees = new ArrayList<>();
		for (com.skydot.paymentservice.types.Payees.Payee p : payeeResults) {
			payees.add(new Payee(p.getId().intValue(), p.getName()));
		}
		
		System.out.println("Result: " + client.search(search).getMessage());
		return payees;
	}
	
	public static boolean withdraw(String userid, int accountid, double amount, String currency) {
		Account account = getAccount(userid, accountid);
		if (account == null) {
			return false;
		} else if (amount < 0.0) {
			return false;
		}

		if (Variables.CAD.equalsIgnoreCase(currency)) {
			account.setBalanceCAD(account.getBalanceCAD() - amount);
		} else if (Variables.USD.equalsIgnoreCase(currency)) {
			account.setBalanceUSD(account.getBalanceUSD() - amount);
		} else {
			return false;
		}

		if (!HibernateDatabaseAccountManager.getDefault().update(account)) {
			return false;
		}

		History history = new History();
		history.setAccountid(accountid);
		history.setAmount(amount);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
		history.setDate(format.format(new Date()));
		history.setName("A Withdrawal");
		history.setUserid(userid);

		int numTransactions = getTransactions(accountid).size();
		history.setId(numTransactions + 1);

		HibernateDatabaseTransactionManager.getDefault().add(history);

		return true;
	}

	public static boolean deposit(String userid, int accountid, double amount, String currency) {
		Account account = getAccount(userid, accountid);
		if (account == null) {
			return false;
		} else if (amount < 0.0) {
			return false;
		}

		if (Variables.CAD.equalsIgnoreCase(currency)) {
			account.setBalanceCAD(account.getBalanceCAD() + amount);
		} else if (Variables.USD.equalsIgnoreCase(currency)) {
			account.setBalanceUSD(account.getBalanceUSD() + amount);
		} else {
			return false;
		}

		if (!HibernateDatabaseAccountManager.getDefault().update(account)) {
			return false;
		}

		History history = new History();
		history.setAccountid(accountid);
		history.setAmount(amount);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
		history.setDate(format.format(new Date()));
		history.setName("A Deposit");
		history.setUserid(userid);

		int numTransactions = getTransactions(accountid).size();
		history.setId(numTransactions + 1);

		HibernateDatabaseTransactionManager.getDefault().add(history);

		return true;
	}

	public static boolean addUser(String username, String password) {
		try {
			Profile newProfile = new Profile(username, password);
			if (!HibernateDatabaseProfileManager.getDefault().add(newProfile)) {
				return false;
			}

			Random rand = new Random();
			int numAccounts = rand.nextInt(10) + 1;
			int randID = rand.nextInt(9000) + 1000;
			for (int i = 0; i < numAccounts; i++) {
				Account a = new Account();
				a.setId(randID++);
				a.setUserid(username);

				int randType = rand.nextInt(3) + 1;
				if (randType == 1) {
					a.setType("Banking");
				} else if (randType == 2) {
					a.setType("Borrowing");
				} else {
					a.setType("Investing");
				}

				a.setName("A simple " + a.getType() + " account");

				a.setBalanceCAD(rand.nextInt(10000) + 100);

				int hasUSD = rand.nextInt(10);
				if (hasUSD < 3) {
					a.setBalanceUSD(rand.nextInt(10000) + 100);
				} else {
					a.setBalanceUSD(0.0);
				}
				HibernateDatabaseAccountManager.getDefault().add(a);
			}

			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

}
