package edu.cmu.sv.ws.ssnoc.data.util;

import java.sql.Connection;
import java.sql.SQLException;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import edu.cmu.sv.ws.ssnoc.common.logging.Log;
import edu.cmu.sv.ws.ssnoc.common.utils.PropertyUtils;

public class H2TestConnectionPoolImpl implements IConnectionPool {

	private static H2TestConnectionPoolImpl instance = null;
	private HikariDataSource ds = null;

	static {
		try {
			Log.info("Initializing the test connection pool ... ");
			instance = new H2TestConnectionPoolImpl();
			Log.info("Test Connection pool initialized successfully.");
		} catch (Exception e) {
			Log.error(
					"Exception when trying to initialize the test connection pool",
					e);
		}
	}

	/**
	 * Constructor to initialize H2 connection pool.
	 */
	private H2TestConnectionPoolImpl() {
		HikariConfig config = new HikariConfig();

		config.setDataSourceClassName("org.h2.jdbcx.JdbcDataSource");
		config.addDataSourceProperty("URL", PropertyUtils.DB_TEST_CONN_URL);
		config.addDataSourceProperty("user", PropertyUtils.DB_USERNAME);
		config.addDataSourceProperty("password", PropertyUtils.DB_PASSWORD);

		ds = new HikariDataSource(config);
		ds.setMaximumPoolSize(PropertyUtils.DB_CONNECTION_POOL_SIZE);
	}

	public static H2TestConnectionPoolImpl getInstance() {
		return instance;
	}

	/**
	 * This method will return the connection object to the database.
	 */
	public Connection getConnection() throws SQLException {
		if (ds == null) {
			Log.error("Test HikariDataSource is NULL. Nooooooooooo :)");
			return null;
		}

		return ds.getConnection();
	}

}
