package utils;

import java.util.List;

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

	public static boolean addUser(String username, String password) {
		try {
			Profile newProfile = new Profile(username, password);
			return HibernateDatabaseProfileManager.getDefault().add(newProfile);
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
		return HibernateDatabaseProfileManager.getDefault().delete(username);
	}

	public static boolean updateProfile(Profile profile) {
		return HibernateDatabaseProfileManager.getDefault().update(profile);
	}

	public static List<Account> getAccountSummary(int userid) {
		return HibernateDatabaseAccountManager.getDefault().getAccountsWithName(userid);
	}

	public static Account getAccount(int userid, int accountid) {
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

}
