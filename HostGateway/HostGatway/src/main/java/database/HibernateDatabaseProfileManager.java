package database;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import data.Profile;
import utils.HibernateUtil;
import utils.LoggerManager;

public class HibernateDatabaseProfileManager extends AbstractHibernateDatabaseManager {

	private static String TABLE_NAME = "PROFILE";
	private static String CLASS_NAME = "Profile";

	private static String SELECT_ALL_PROFILES = "from " + CLASS_NAME + " as profile";

	private static String SELECT_PROFILE_WITH_NAME = "from " + CLASS_NAME
			+ " as profile where profile.userid = :userid";

	private static String SELECT_NUMBER_PROFILES = "select count (*) from " + CLASS_NAME;

	private static String METHOD_GET_ALL = "getAllProfiles";
	private static String METHOD_GET_PROFILE_WITH_NAME = "getProfileWithName";
	private static String METHOD_NUMBER_PROFILES = "getNumberOfProfiles";
	private static String METHOD_PROFILE_EXIST = "profileExist";

	private static String INCOMMING_NAME = "incomming msgs";
	public static String OUTGOING_NAME = "outgoing msgs";

	private static final String DROP_TABLE_SQL = "drop table " + TABLE_NAME + ";";

	// sqlserver
	private static String CREATE_TABLE_SQL = "create table " + TABLE_NAME
			+ "(PRIMARY_KEY char(36) primary key not null, " + "USERID varchar(max), PASSWORD varchar(max));";

	// mysql
//	 private static String CREATE_TABLE_SQL = "create table " + TABLE_NAME +
//	 "(PRIMARY_KEY char(36) primary key not null, "
//	 + "USERID tinytext, PASSWORD tinytext);";

	private static HibernateDatabaseProfileManager manager;

	HibernateDatabaseProfileManager() {
		super();
	}

	/**
	 * Returns default instance.
	 * 
	 * @return
	 */
	public synchronized static HibernateDatabaseProfileManager getDefault() {
		if (manager == null) {
			manager = new HibernateDatabaseProfileManager();
		}
		return manager;
	}

	public synchronized Profile getProfileWithName(String userid) {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getCurrentSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery(SELECT_PROFILE_WITH_NAME);
			query.setParameter("userid", userid);
			@SuppressWarnings("unchecked")
			List<Profile> profiles = query.list();
			transaction.commit();

			if (profiles.isEmpty()) {
				return null;
			} else {
				Profile profile = profiles.get(0);
				return profile;
			}
		} catch (HibernateException exception) {
			LoggerManager.current().error(this, METHOD_GET_PROFILE_WITH_NAME, "error." + METHOD_GET_PROFILE_WITH_NAME,
					exception);
			return null;
		} finally {
			closeSession();
		}
	}

	public synchronized boolean profileExists(String id) {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getCurrentSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery(SELECT_PROFILE_WITH_NAME);
			query.setParameter("userid", id);
			@SuppressWarnings("unchecked")
			List<Profile> profiles = query.list();
			transaction.commit();

			if (profiles.isEmpty()) {
				return false;
			} else {
				return true;
			}
		} catch (HibernateException exception) {
			LoggerManager.current().error(this, METHOD_PROFILE_EXIST, "error." + METHOD_PROFILE_EXIST, exception);
			return false;
		} finally {
			closeSession();
		}
	}

	public synchronized boolean profileExists(String id, String password) {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getCurrentSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery(SELECT_PROFILE_WITH_NAME);
			query.setParameter("userid", id);
			@SuppressWarnings("unchecked")
			List<Profile> profiles = query.list();
			transaction.commit();

			if (profiles.isEmpty()) {
				return false;
			} else {
				return password.equals(profiles.get(0).getPassword());
			}
		} catch (HibernateException exception) {
			LoggerManager.current().error(this, METHOD_PROFILE_EXIST, "error." + METHOD_PROFILE_EXIST, exception);
			return false;
		} finally {
			closeSession();
		}
	}

	public synchronized Profile getProfileById(String id) {
		Session session = null;
		try {
			session = HibernateUtil.getCurrentSession();
			Profile profile = (Profile) session.load(Profile.class, id);
			return profile;
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
	public synchronized List<Profile> getAllProfiles() {

		Session session = null;
		List<Profile> errorResult = null;

		try {
			session = HibernateUtil.getCurrentSession();
			Query query = session.createQuery(SELECT_ALL_PROFILES);
			List<Profile> profiles = query.list();
			return profiles;
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

	public synchronized int getNumberOfProfiles() {
		Session session = null;
		Long aLong;

		try {
			session = HibernateUtil.getCurrentSession();
			Query query = session.createQuery(SELECT_NUMBER_PROFILES);
			aLong = (Long) query.uniqueResult();
			return aLong.intValue();
		} catch (ObjectNotFoundException exception) {
			LoggerManager.current().error(this, METHOD_NUMBER_PROFILES, Messages.OBJECT_NOT_FOUND_FAILED, exception);
			return 0;
		} catch (HibernateException exception) {
			LoggerManager.current().error(this, METHOD_NUMBER_PROFILES, Messages.HIBERNATE_FAILED, exception);
			return 0;
		} catch (RuntimeException exception) {
			LoggerManager.current().error(this, METHOD_NUMBER_PROFILES, Messages.GENERIC_FAILED, exception);
			return 0;
		} finally {
			closeSession();
		}
	}

	public synchronized boolean delete(Profile profile) {
		Session session = null;
		Transaction transaction = null;
		boolean errorResult = false;

		try {
			session = HibernateUtil.getCurrentSession();
			transaction = session.beginTransaction();
			session.delete(profile);
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

	public synchronized boolean delete(String userid) {
		Session session = null;
		Transaction transaction = null;
		boolean errorResult = false;

		try {
			Profile toDelete = getProfileWithName(userid);
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
		Profile profile = (Profile) object;

		try {
			// TODO
			// do update instead of delete/add
			if (profileExists(profile.getUserid())) {
				delete(profile.getUserid());
			}
			session = HibernateUtil.getCurrentSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery(SELECT_PROFILE_WITH_NAME);
			query.setParameter("userid", profile.getUserid());
			@SuppressWarnings("unchecked")
			List<Profile> tokens = query.list();

			if (!tokens.isEmpty()) {
				return false;
			}

			session.save(profile);
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