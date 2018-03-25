package database;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import data.History;
import utils.HibernateUtil;
import utils.LoggerManager;

public class HibernateDatabaseTransactionManager extends AbstractHibernateDatabaseManager {

	private static String TABLE_NAME = "HISTORY";
	private static String CLASS_NAME = "History";

	private static String SELECT_ALL_TRANSACTIONS = "from " + CLASS_NAME + " as history";

	private static String SELECT_TRANSACTION_WITH_ID = "from " + CLASS_NAME
			+ " as history where history.id = :id";

	private static String SELECT_TRANSACTION = "from " + CLASS_NAME
			+ " as history where history.id = :id and history.accountid = :accountid";

	private static String SELECT_TRANSACTION_WITH_ACCOUNT_ID = "from " + CLASS_NAME
			+ " as history where history.accountid = :accountid";

	private static String SELECT_NUMBER_TRANSACTIONS = "select count (*) from " + CLASS_NAME;

	private static String METHOD_GET_ALL = "getAllTransactions";
	private static String METHOD_GET_TRANSACTION_WITH_NAME = "getTransactionWithName";
	private static String METHOD_NUMBER_TRANSACTIONS = "getNumberOfTransactions";
	private static String METHOD_TRANSACTION_EXIST = "transactionExist";

	private static String INCOMMING_NAME = "incomming msgs";
	public static String OUTGOING_NAME = "outgoing msgs";

	private static final String DROP_TABLE_SQL = "drop table " + TABLE_NAME + ";";

	// sqlserver
	private static String CREATE_TABLE_SQL = "create table " + TABLE_NAME
			+ "(PRIMARY_KEY char(36) primary key not null, "
			+ "ID int, ACCOUNTID int, USERID varchar(max), NAME varchar(max), DATE varchar(max), AMOUNT float);";

	// mysql
	// private static String CREATE_TABLE_SQL = "create table " + TABLE_NAME
	// + "(PRIMARY_KEY char(36) primary key not null, "
	// + "ID integer, ACCOUNTID integer, USERID tinytext, NAME tinytext, DATE
	// tinytext, AMOUNT double);";

	private static HibernateDatabaseTransactionManager manager;

	HibernateDatabaseTransactionManager() {
		super();
	}

	/**
	 * Returns default instance.
	 * 
	 * @return
	 */
	public synchronized static HibernateDatabaseTransactionManager getDefault() {
		if (manager == null) {
			manager = new HibernateDatabaseTransactionManager();
		}
		return manager;
	}

	public synchronized boolean transactionExists(int id) {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getCurrentSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery(SELECT_TRANSACTION_WITH_ID);
			query.setParameter("id", id);
			@SuppressWarnings("unchecked")
			List<History> transactions = query.list();
			transaction.commit();

			if (transactions.isEmpty()) {
				return false;
			} else {
				return true;
			}
		} catch (HibernateException exception) {
			LoggerManager.current().error(this, METHOD_TRANSACTION_EXIST, "error." + METHOD_TRANSACTION_EXIST,
					exception);
			return false;
		} finally {
			closeSession();
		}
	}

	public synchronized History getTransactionWithID(int id) {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getCurrentSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery(SELECT_TRANSACTION_WITH_ID);
			query.setParameter("id", id);
			@SuppressWarnings("unchecked")
			List<History> transactions = query.list();
			transaction.commit();

			if (transactions.isEmpty()) {
				return null;
			} else {
				History trans = transactions.get(0);
				return trans;
			}
		} catch (HibernateException exception) {
			LoggerManager.current().error(this, METHOD_GET_TRANSACTION_WITH_NAME,
					"error." + METHOD_GET_TRANSACTION_WITH_NAME, exception);
			return null;
		} finally {
			closeSession();
		}
	}

	public synchronized History getTransactionById(String id) {
		Session session = null;
		try {
			session = HibernateUtil.getCurrentSession();
			History transaction = (History) session.load(History.class, id);
			return transaction;
		} catch (HibernateException exception) {
			LoggerManager.current().error(this, Messages.METHOD_GET_CLIENT_BY_ID, "error.getClientById", exception);
			return null;
		} finally {
			closeSession();
		}
	}

	public synchronized List<History> getTransactions(int accountid) {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getCurrentSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery(SELECT_TRANSACTION_WITH_ACCOUNT_ID);
			query.setParameter("accountid", accountid);
			@SuppressWarnings("unchecked")
			List<History> transactions = query.list();
			transaction.commit();

			return transactions;
		} catch (HibernateException exception) {
			LoggerManager.current().error(this, METHOD_GET_TRANSACTION_WITH_NAME,
					"error." + METHOD_GET_TRANSACTION_WITH_NAME, exception);
			return new ArrayList<History>();
		} finally {
			closeSession();
		}
	}

	@SuppressWarnings("unchecked")
	public synchronized List<History> getAllTransactions() {

		Session session = null;
		List<History> errorResult = null;

		try {
			session = HibernateUtil.getCurrentSession();
			Query query = session.createQuery(SELECT_ALL_TRANSACTIONS);
			List<History> transactions = query.list();
			return transactions;
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

	public synchronized int getNumberOfTransactions() {
		Session session = null;
		Long aLong;

		try {
			session = HibernateUtil.getCurrentSession();
			Query query = session.createQuery(SELECT_NUMBER_TRANSACTIONS);
			aLong = (Long) query.uniqueResult();
			return aLong.intValue();
		} catch (ObjectNotFoundException exception) {
			LoggerManager.current().error(this, METHOD_NUMBER_TRANSACTIONS, Messages.OBJECT_NOT_FOUND_FAILED,
					exception);
			return 0;
		} catch (HibernateException exception) {
			LoggerManager.current().error(this, METHOD_NUMBER_TRANSACTIONS, Messages.HIBERNATE_FAILED, exception);
			return 0;
		} catch (RuntimeException exception) {
			LoggerManager.current().error(this, METHOD_NUMBER_TRANSACTIONS, Messages.GENERIC_FAILED, exception);
			return 0;
		} finally {
			closeSession();
		}
	}

	public synchronized boolean delete(History trans) {
		Session session = null;
		Transaction transaction = null;
		boolean errorResult = false;

		try {
			session = HibernateUtil.getCurrentSession();
			transaction = session.beginTransaction();
			session.delete(trans);
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
			History toDelete = getTransactionWithID(id);
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
		History trans = (History) object;

		try {
			session = HibernateUtil.getCurrentSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery(SELECT_TRANSACTION);
			query.setParameter("id", trans.getId());
			query.setParameter("accountid", trans.getAccountid());
			
			@SuppressWarnings("unchecked")
			List<History> transactions = query.list();

			if (!transactions.isEmpty()) {
				return false;
			}

			session.save(trans);
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

	public synchronized boolean update(History transaction) {
		boolean result = super.update(transaction);
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