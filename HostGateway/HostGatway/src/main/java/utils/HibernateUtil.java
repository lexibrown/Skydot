package utils;

import org.hibernate.CacheMode;
import org.hibernate.FlushMode;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import database.HibernateDatabaseAccountManager;
import database.HibernateDatabaseProfileManager;
import database.HibernateDatabaseTransactionManager;

/**
 * Utility class related to the persistence. Contains API used for Hibernate
 * session management.
 *
 *
 */
public class HibernateUtil {

	private static SessionFactory sessionFactory;
	private static ServiceRegistry serviceRegistry;

	// local database name
	private static String DATABASE_NAME = "bank";

	// Keeps track of all Hibernate threads
	public static final ThreadLocal<Session> sessionThreadLocal = new ThreadLocal<Session>();

	// private static Session session;

	/**
	 * Builds Hibernate session factory based on the Hibernate configuration
	 * file.
	 */
	private static void buildSessionFactory() {
		if (sessionFactory == null) {
			try {
				Configuration configuration = new Configuration();
				// Should read from file

//				 configuration.setProperty("hibernate.connection.driver_class",
//				 "com.mysql.jdbc.Driver");
				configuration.setProperty("hibernate.connection.driver_class",
						"com.microsoft.sqlserver.jdbc.SQLServerDriver");

				// local database
//				 configuration.setProperty("hibernate.connection.url",
//				 "jdbc:mysql://localhost:3306/" + DATABASE_NAME +
//				 "?autoReconnect=true");

				// azure database
				configuration.setProperty("hibernate.connection.url",
						"jdbc:sqlserver://skydot-bank.database.windows.net:1433;database=skydot-bank-database;"
								+ "user=sysadmin@skydot-bank;password=P@ssw0rd1;encrypt=true;"
								+ "trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;");
				
					configuration.setProperty("show_sql", "true");

//				 configuration.setProperty("hibernate.dialect",
//				 "org.hibernate.dialect.MySQLDialect");
				configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.SQLServerDialect");

				// local login
//				 configuration.setProperty("hibernate.connection.password",
//				 "social");
//				 configuration.setProperty("hibernate.connection.username",
//				 "social");

				// azure login
				// configuration.setProperty("hibernate.connection.username",
				// "sysadmin@dndencounters");
				// configuration.setProperty("hibernate.connection.password",
				// "P@ssword1");

				// configuration.setProperty("hibernate.c3p0.min_size", "5");
				// configuration.setProperty("hibernate.c3p0.max_size", "20");
				// configuration.setProperty("hibernate.c3p0.idle_test_period",
				// "60");
				// configuration.setProperty("hibernate.c3p0.max_statements",
				// "100");

				configuration.setProperty("hibernate.transaction.factory_class",
						"org.hibernate.transaction.JDBCTransactionFactory");
				configuration.setProperty("hibernate.current_session_context_class", "thread");
				configuration.setProperty("hibernate.connection.autocommit", "true");
				// Next Configuration solves this problem:
				// http://stackoverflow.com/questions/25362831/solve-hibernate-lazy-init-issue-with-hibernate-enable-lazy-load-no-trans/25367976#25367976
				configuration.setProperty("hibernate.enable_lazy_load_no_trans", "true");
				configuration.setProperty("hibernate.connection.useSSL", "true");
				configuration.configure();
				serviceRegistry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties())
						.configure().build();
				sessionFactory = new MetadataSources(serviceRegistry).buildMetadata().buildSessionFactory();
			} catch (HibernateException ex) {
				throw new RuntimeException("Exception building SessionFactory: " + ex.getMessage(), ex);
			}
		}
	}

	public static void clearSessionFactory() {
		if (sessionFactory != null) {
			try {
				sessionFactory.close();
			} catch (HibernateException ex) {
				sessionFactory = null;
				throw new RuntimeException("Exception closing SessionFactory: " + ex.getMessage(), ex);
			}
		}
		sessionFactory = null;
	}

	/**
	 * Returns current Hibernate session based on the thread local. First looks
	 * into thread local for a session. If found returns the session. If not
	 * found creates new session and adds it to the thread local.
	 *
	 * @return
	 * @throws HibernateException
	 */
	public synchronized static Session getCurrentSession() throws HibernateException {
		Session session = (Session) sessionThreadLocal.get();

		// Open a new Session, if this Thread has none yet
		if (session == null) {
			buildSessionFactory();
			session = sessionFactory.openSession();
			session.setCacheMode(CacheMode.IGNORE);
			session.setFlushMode(FlushMode.COMMIT);

			sessionThreadLocal.set(session);
		}
		return session;
	}

	/**
	 * Closes current Hibernate session. Looks for a current session in thread
	 * local, and if found closes the session. Also sets thread local to null.
	 *
	 * @throws HibernateException
	 */
	public static void closeTheThreadLocalSession() throws HibernateException {
		Session session = (Session) sessionThreadLocal.get();
		sessionThreadLocal.set(null);
		if (session != null) {
			session.close();
		}
		session = null;
	}

	/**
	 * Used for initial table setup in the database.
	 *
	 * @param sql
	 * @return
	 */
	public static synchronized boolean executeSQLQuery(String sql) {
		Session session = null;
		Transaction transaction = null;

		try {
			session = getCurrentSession();
			transaction = session.beginTransaction();
			SQLQuery query = session.createSQLQuery(sql);
			query.executeUpdate();
			transaction.commit();
		} catch (Exception exception) {
			exception.printStackTrace();
			return false;
		} finally {
			closeTheThreadLocalSession();
		}
		return true;
	}

	public static void setupTables() {
		HibernateDatabaseAccountManager.getDefault().setupTable();
		HibernateDatabaseProfileManager.getDefault().setupTable();
		HibernateDatabaseTransactionManager.getDefault().setupTable();
	}
}