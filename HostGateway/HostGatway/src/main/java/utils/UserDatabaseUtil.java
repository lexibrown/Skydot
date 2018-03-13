package utils;

import java.util.List;
import java.util.Random;

import data.Account;
import data.Profile;
import data.Transaction;
import database.HibernateDatabaseAccountManager;
import database.HibernateDatabaseProfileManager;
import database.HibernateDatabaseTransactionManager;

public class UserDatabaseUtil {

	public static boolean userExists(String username) {
		return HibernateDatabaseProfileManager.getDefault().profileExists(username);
	}

	public static boolean userExists(String username, String password) {
		try {
			Profile p = HibernateDatabaseProfileManager.getDefault().getProfileWithName(username);
			if (p == null) {
				return false;
			}
			return password.equals(p.getPassword());
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public static Profile getUser(String username) {
		return HibernateDatabaseProfileManager.getDefault().getProfileWithName(username);
	}

	public static Profile getUserWithoutPassword(String username) {
		Profile profile = HibernateDatabaseProfileManager.getDefault().getProfileWithName(username);
		if (profile == null) {
			return null;
		}
		profile.setPassword(null);
		return profile;
	}

	public static boolean removeUser(String username) {
		// TODO remove all transactions and accounts
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

	public static List<Transaction> getTransactions(int accountid) {
		return HibernateDatabaseTransactionManager.getDefault().getTransactions(accountid);
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
