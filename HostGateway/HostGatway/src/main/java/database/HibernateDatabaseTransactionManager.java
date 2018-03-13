package database;

import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import utils.HibernateUtil;
import utils.LoggerManager;

public class HibernateDatabaseTransactionManager extends AbstractHibernateDatabaseManager {

	private static String TABLE_NAME = "TRANSACTION";
	private static String CLASS_NAME = "Transaction";

	private static String SELECT_ALL_TRANSACTIONS = "from " + CLASS_NAME + " as transaction";

	private static String SELECT_TRANSACTION_WITH_ID = "from " + CLASS_NAME
			+ " as transaction where transaction.id = :id";

	private static String SELECT_TRANSACTION_WITH_ACCOUNT_ID = "from " + CLASS_NAME
			+ " as transaction where transaction.accountid = :accountid";

	private static String SELECT_NUMBER_TRANSACTIONS = "select count (*) from " + CLASS_NAME;

	private static String METHOD_GET_ALL = "getAllTransactions";
	private static String METHOD_GET_TRANSACTION_WITH_NAME = "getTransactionWithName";
	private static String METHOD_NUMBER_TRANSACTIONS = "getNumberOfTransactions";
	private static String METHOD_TRANSACTION_EXIST = "transactionExist";

	private static String INCOMMING_NAME = "incomming msgs";
	public static String OUTGOING_NAME = "outgoing msgs";

	private static final String DROP_TABLE_SQL = "drop table " + TABLE_NAME + ";";

	// sqlserver
//	private static String CREATE_TABLE_SQL = "create table " + TABLE_NAME
//			+ "(PRIMARY_KEY char(36) primary key not null, "
//			+ "ID int, ACCOUNTID int, USERID varchar(max), NAME varchar(max), DATE varchar(max), AMOUNT double);";

	// mysql
	 private static String CREATE_TABLE_SQL = "create table " + TABLE_NAME
	 + "(PRIMARY_KEY char(36) primary key not null, "
	 + "ID integer, ACCOUNTID integer, USERID tinytext, NAME tinytext, DATE tinytext, AMOUNT double);";

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
			List<data.Transaction> transactions = query.list();
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

	public synchronized data.Transaction getTransactionWithID(int id) {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getCurrentSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery(SELECT_TRANSACTION_WITH_ID);
			query.setParameter("id", id);
			@SuppressWarnings("unchecked")
			List<data.Transaction> transactions = query.list();
			transaction.commit();

			if (transactions.isEmpty()) {
				return null;
			} else {
				data.Transaction trans = transactions.get(0);
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

	public synchronized data.Transaction getTransactionById(String id) {
		Session session = null;
		try {
			session = HibernateUtil.getCurrentSession();
			data.Transaction transaction = (data.Transaction) session.load(data.Transaction.class, id);
			return transaction;
		} catch (HibernateException exception) {
			LoggerManager.current().error(this, Messages.METHOD_GET_CLIENT_BY_ID, "error.getClientById", exception);
			return null;
		} finally {
			closeSession();
		}
	}

	public synchronized List<data.Transaction> getTransactions(int accountid) {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getCurrentSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery(SELECT_TRANSACTION_WITH_ACCOUNT_ID);
			query.setParameter("accountid", accountid);
			@SuppressWarnings("unchecked")
			List<data.Transaction> transactions = query.list();
			transaction.commit();

			if (transactions.isEmpty()) {
				return null;
			}
			return transactions;
		} catch (HibernateException exception) {
			LoggerManager.current().error(this, METHOD_GET_TRANSACTION_WITH_NAME,
					"error." + METHOD_GET_TRANSACTION_WITH_NAME, exception);
			return null;
		} finally {
			closeSession();
		}
	}

	@SuppressWarnings("unchecked")
	public synchronized List<data.Transaction> getAllTransactions() {

		Session session = null;
		List<data.Transaction> errorResult = null;

		try {
			session = HibernateUtil.getCurrentSession();
			Query query = session.createQuery(SELECT_ALL_TRANSACTIONS);
			List<data.Transaction> transactions = query.list();
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

	public synchronized boolean delete(data.Transaction trans) {
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
			data.Transaction toDelete = getTransactionWithID(id);
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
		data.Transaction trans = (data.Transaction) object;

		try {
			session = HibernateUtil.getCurrentSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery(SELECT_TRANSACTION_WITH_ID);
			query.setParameter("id", trans.getId());
			@SuppressWarnings("unchecked")
			List<data.Transaction> transactions = query.list();

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

	public synchronized boolean update(data.Transaction transaction) {
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