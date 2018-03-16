package database;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import data.Account;
import utils.HibernateUtil;
import utils.LoggerManager;

public class HibernateDatabaseAccountManager extends AbstractHibernateDatabaseManager {

	private static String TABLE_NAME = "ACCOUNT";
	private static String CLASS_NAME = "Account";

	private static String SELECT_ALL_ACCOUNTS = "from " + CLASS_NAME + " as account";

	private static String SELECT_ACCOUNT_WITH_NAME = "from " + CLASS_NAME
			+ " as account where account.userid = :userid";

	private static String SELECT_ACCOUNT = "from " + CLASS_NAME
			+ " as account where account.userid = :userid and account.id = :id";

	private static String SELECT_ACCOUNT_WITH_ID = "from " + CLASS_NAME + " as account where account.id = :id";

	private static String SELECT_NUMBER_ACCOUNTS = "select count (*) from " + CLASS_NAME;

	private static String METHOD_GET_ALL = "getAllAccounts";
	private static String METHOD_GET_ACCOUNT_WITH_NAME = "getAccountWithName";
	private static String METHOD_GET_ACCOUNT_WITH_ID = "getAccountWithId";
	private static String METHOD_NUMBER_ACCOUNTS = "getNumberOfAccounts";
	private static String METHOD_ACCOUNT_EXIST = "accountExist";

	private static String INCOMMING_NAME = "incomming msgs";
	public static String OUTGOING_NAME = "outgoing msgs";

	private static final String DROP_TABLE_SQL = "drop table " + TABLE_NAME + ";";

	// sqlserver
	private static String CREATE_TABLE_SQL = "create table " + TABLE_NAME
			+ "(PRIMARY_KEY char(36) primary key not null, ID int, USERID varchar(max), NAME varchar(max), "
			+ "TYPE varchar(max), CAD float, USD float);";

	// mysql
//	 private static String CREATE_TABLE_SQL = "create table " + TABLE_NAME
//	 + "(PRIMARY_KEY char(36) primary key not null, ID integer, USERID tinytext, NAME tinytext, "
//	 + "TYPE tinytext, CAD double, USD double);";

	private static HibernateDatabaseAccountManager manager;

	HibernateDatabaseAccountManager() {
		super();
	}

	/**
	 * Returns default instance.
	 * 
	 * @return
	 */
	public synchronized static HibernateDatabaseAccountManager getDefault() {
		if (manager == null) {
			manager = new HibernateDatabaseAccountManager();
		}
		return manager;
	}

	public synchronized List<Account> getAccountsWithName(String userid) {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getCurrentSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery(SELECT_ACCOUNT_WITH_NAME);
			query.setParameter("userid", userid);
			@SuppressWarnings("unchecked")
			List<Account> accounts = query.list();
			transaction.commit();

			return accounts;
		} catch (HibernateException exception) {
			LoggerManager.current().error(this, METHOD_GET_ACCOUNT_WITH_NAME, "error." + METHOD_GET_ACCOUNT_WITH_NAME,
					exception);
			return new ArrayList<Account>();
		} finally {
			closeSession();
		}
	}

	public synchronized Account getAccountWithId(int id) {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getCurrentSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery(SELECT_ACCOUNT_WITH_ID);
			query.setParameter("id", id);
			@SuppressWarnings("unchecked")
			List<Account> accounts = query.list();
			transaction.commit();

			if (accounts.isEmpty()) {
				return null;
			} else {
				Account account = accounts.get(0);
				return account;
			}
		} catch (HibernateException exception) {
			LoggerManager.current().error(this, METHOD_GET_ACCOUNT_WITH_ID, "error." + METHOD_GET_ACCOUNT_WITH_ID,
					exception);
			return null;
		} finally {
			closeSession();
		}
	}

	public synchronized boolean accountExists(int id) {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getCurrentSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery(SELECT_ACCOUNT_WITH_ID);
			query.setParameter("id", id);
			@SuppressWarnings("unchecked")
			List<Account> accounts = query.list();
			transaction.commit();

			if (accounts.isEmpty()) {
				return false;
			} else {
				return true;
			}
		} catch (HibernateException exception) {
			LoggerManager.current().error(this, METHOD_ACCOUNT_EXIST, "error." + METHOD_ACCOUNT_EXIST, exception);
			return false;
		} finally {
			closeSession();
		}
	}

	public synchronized Account getAccountById(String id) {
		Session session = null;
		try {
			session = HibernateUtil.getCurrentSession();
			Account account = (Account) session.load(Account.class, id);
			return account;
		} catch (HibernateException exception) {
			LoggerManager.current().error(this, Messages.METHOD_GET_CLIENT_BY_ID, "error.getClientById", exception);
			return null;
		} finally {
			closeSession();
		}
	}

	/**
	 * Returns all counters from the database. Upon error returns null.
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public synchronized List<Account> getAllAccounts() {
		Session session = null;
		List<Account> errorResult = null;

		try {
			session = HibernateUtil.getCurrentSession();
			Query query = session.createQuery(SELECT_ALL_ACCOUNTS);
			List<Account> accounts = query.list();
			return accounts;
		} catch (ObjectNotFoundException exception) {
			LoggerManager.current().error(this, METHOD_GET_ALL, Messages.OBJECT_NOT_FOUND_FAILED, exception);
			return errorResult;
		} catch (HibernateException exception) {
			LoggerManager.current().error(this, METHOD_GET_ALL, Messages.HIBERNATE_FAILED, exception);
			return errorResult;
		} catch (RuntimeException exception) {
			LoggerManager.current().error(this, METHOD_GET_ALL, Messages.GENERIC_FAILED, exception);
			return errorResult;
		} finally {
			closeSession();
		}
	}

	public synchronized int getNumberOfAccounts() {
		Session session = null;
		Long aLong;

		try {
			session = HibernateUtil.getCurrentSession();
			Query query = session.createQuery(SELECT_NUMBER_ACCOUNTS);
			aLong = (Long) query.uniqueResult();
			return aLong.intValue();
		} catch (ObjectNotFoundException exception) {
			LoggerManager.current().error(this, METHOD_NUMBER_ACCOUNTS, Messages.OBJECT_NOT_FOUND_FAILED, exception);
			return 0;
		} catch (HibernateException exception) {
			LoggerManager.current().error(this, METHOD_NUMBER_ACCOUNTS, Messages.HIBERNATE_FAILED, exception);
			return 0;
		} catch (RuntimeException exception) {
			LoggerManager.current().error(this, METHOD_NUMBER_ACCOUNTS, Messages.GENERIC_FAILED, exception);
			return 0;
		} finally {
			closeSession();
		}
	}

	public synchronized boolean delete(Account account) {
		Session session = null;
		Transaction transaction = null;
		boolean errorResult = false;

		try {
			session = HibernateUtil.getCurrentSession();
			transaction = session.beginTransaction();
			session.delete(account);
			transaction.commit();
			return true;
		} catch (HibernateException exception) {
			rollback(transaction);
			LoggerManager.current().error(this, Messages.METHOD_DELETE_CLIENT, Messages.HIBERNATE_FAILED, exception);
			return errorResult;
		} catch (RuntimeException exception) {
			rollback(transaction);
			LoggerManager.current().error(this, Messages.METHOD_DELETE_CLIENT, Messages.GENERIC_FAILED, exception);
			return errorResult;
		} finally {
			closeSession();
		}
	}

	public synchronized boolean delete(int id) {
		Session session = null;
		Transaction transaction = null;
		boolean errorResult = false;

		try {
			Account toDelete = getAccountWithId(id);
			if (toDelete == null) {
				return false;
			}
			session = HibernateUtil.getCurrentSession();
			transaction = session.beginTransaction();
			session.delete(toDelete);
			transaction.commit();
			return true;
		} catch (HibernateException exception) {
			rollback(transaction);
			LoggerManager.current().error(this, Messages.METHOD_DELETE_CLIENT, Messages.HIBERNATE_FAILED, exception);
			return errorResult;
		} catch (RuntimeException exception) {
			rollback(transaction);
			LoggerManager.current().error(this, Messages.METHOD_DELETE_CLIENT, Messages.GENERIC_FAILED, exception);
			return errorResult;
		} finally {
			closeSession();
		}
	}

	public synchronized boolean add(Object object) {
		Transaction transaction = null;
		Session session = null;
		Account account = (Account) object;

		try {
			session = HibernateUtil.getCurrentSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery(SELECT_ACCOUNT);
			query.setParameter("userid", account.getUserid());
			query.setParameter("id", account.getId());
			@SuppressWarnings("unchecked")
			List<Account> accounts = query.list();

			if (!accounts.isEmpty()) {
				return false;
			}

			session.save(account);
			transaction.commit();
			return true;

		} catch (HibernateException exception) {
			LoggerManager.current().error(this, Messages.METHOD_ADD_CLIENT, "error.addClientToDatabase", exception);

			rollback(transaction);
			return false;
		} finally {
			closeSession();
		}
	}

	public synchronized boolean update(Account account) {
		boolean result = super.update(account);
		return result;
	}

	public String getTableName() {
		return TABLE_NAME;
	}

	public boolean setupTable() {
		HibernateUtil.executeSQLQuery(DROP_TABLE_SQL);
		return HibernateUtil.executeSQLQuery(CREATE_TABLE_SQL);
	}

	public static String getIncommingName() {
		return INCOMMING_NAME;
	}

	public static String getOutgoingName() {
		return OUTGOING_NAME;
	}

	public String getClassName() {
		return CLASS_NAME;
	}

}