package edu.cmu.sv.ws.ssnoc.data.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import edu.cmu.sv.ws.ssnoc.common.logging.Log;
import edu.cmu.sv.ws.ssnoc.common.utils.ConverterUtils;
import edu.cmu.sv.ws.ssnoc.common.utils.SSNCipher;
import edu.cmu.sv.ws.ssnoc.data.SQL;
import edu.cmu.sv.ws.ssnoc.data.dao.DAOFactory;
import edu.cmu.sv.ws.ssnoc.data.po.StatusPO;
import edu.cmu.sv.ws.ssnoc.data.po.UserPO;
import edu.cmu.sv.ws.ssnoc.dto.Status;

/**
 * This is a utility class to provide common functions to access and handle
 * Database operations.
 * 
 */
public class DBUtils {
	private static boolean DB_TABLES_EXIST = false;
	private static Map<String, String> CREATE_TABLE_MAP;
	private static boolean TEST_MODE = false;

	static {		
		CREATE_TABLE_MAP = new HashMap<String, String>();
		CREATE_TABLE_MAP.put(SQL.SSN_USERS, SQL.CREATE_USERS);
		CREATE_TABLE_MAP.put(SQL.SSN_STATUSES, SQL.CREATE_STATUSES);
		CREATE_TABLE_MAP.put(SQL.SSN_MESSAGES, SQL.CREATE_MESSAGES);
		CREATE_TABLE_MAP.put(SQL.SSN_MEMORY, SQL.CREATE_MEMORY);
		//create fake message table
		CREATE_TABLE_MAP.put(SQL.SSN_FAKE_MESSAGES, SQL.CREATE_FAKE_MESSAGES);
	}
	
	/*
	 * This method will set the whether the database used is the real
	 * one or the test one
	 */
	public static void setTestMode(boolean testMode){
		TEST_MODE = testMode;
	}
	
	public static void dropDatabase() throws SQLException {
		Log.enter();

		Log.info("Dropping tables in database ...");
		for(String tableName : CREATE_TABLE_MAP.keySet()){
			try {
					Connection conn = getConnection();
					Statement stmt = conn.createStatement();
					stmt.execute(SQL.DROP_TABLE_IN_DB + tableName);	
					conn.close();
				} catch(Exception e){
					Log.error("Error dropping table: " + tableName);
					e.printStackTrace();
				}
		}
		Log.info("Tables dropped successfully");
		DB_TABLES_EXIST = false;

		Log.exit();
	}
	
	public static void truncateDatabase() throws SQLException {
		Log.enter();
		Log.info("Truncating tables in database ...");
		for(String tableName : CREATE_TABLE_MAP.keySet()){
			try {
					Connection conn = getConnection();
					Statement stmt = conn.createStatement();
					stmt.execute(SQL.TRUNCATE_TABLE_IN_DB + tableName);	
					conn.close();
				} catch(Exception e){
					Log.error("Error Truncating table: " + tableName);
					e.printStackTrace();
				}
		}
		Log.info("Tables Truncating successfully");
		Log.exit();
	}
  
	/**
	 * This method will initialize the database.
	 * 
	 * @throws SQLException
	 */
	public static void initializeDatabase() throws SQLException {
		createTablesInDB();
		insertAdministrator();
	}
	
	private static void insertAdministrator() {
		Log.enter("insert default Administrator");
		UserPO userPO = new UserPO();
		userPO.setUserName("SSNAdmin");
		userPO.setPassword("admin");
		userPO.setPrivilegeLevel("Administrator");
		userPO.setCreatedAt(new Date());
		userPO = SSNCipher.encryptPassword(userPO);
		DAOFactory.getInstance().getUserDAO().save(userPO);
		UserPO user = DAOFactory.getInstance().getUserDAO().findByName(userPO.getUserName());
		
		StatusPO status = new StatusPO();
		status.setUserId(userPO.getUserId());
		status.setStatusCode("GREEN");
		status.setUpdatedAt(new Date());

		long id = DAOFactory.getInstance().getStatusDAO().save(status);
		userPO.setLastStatusID(id);
		DAOFactory.getInstance().getUserDAO().save(user);		
		
	}

	/**
	 * This method will create necessary tables in the database.
	 * 
	 * @throws SQLException
	 */
	protected static void createTablesInDB() throws SQLException {
		Log.enter();
		if (DB_TABLES_EXIST) {
			return;
		}
		
		Log.info("Creating tables in database ...");
		for(String tableName : CREATE_TABLE_MAP.keySet()){
			try (Connection conn = getConnection();
				Statement stmt = conn.createStatement();) {
					if(!doesTableExistInDB(conn, tableName)){
						String query = CREATE_TABLE_MAP.get(tableName);
						Log.debug("Executing query: " + query);
						boolean status = stmt.execute(query);
						Log.debug("Query execution completed with status: " + status);
					}
					conn.close();
				} catch(Exception e){
					Log.error("Error initializing table: " + tableName);
				}
		}
		Log.info("Tables initialized successfully");
		DB_TABLES_EXIST = true;

		Log.exit();
	}

	/**
	 * This method will check if the table exists in the database.
	 * 
	 * @param conn
	 *            - Connection to the database
	 * @param tableName
	 *            - Table name to check.
	 * 
	 * @return - Flag whether the table exists or not.
	 * 
	 * @throws SQLException
	 */
	public static boolean doesTableExistInDB(Connection conn, String tableName)
			throws SQLException {
		Log.enter(tableName);

		if (conn == null || tableName == null || "".equals(tableName.trim())) {
			Log.error("Invalid input parameters. Returning doesTableExistInDB() method with FALSE.");
			return false;
		}

		boolean tableExists = false;

		final String SELECT_QUERY = SQL.CHECK_TABLE_EXISTS_IN_DB;

		ResultSet rs = null;
		try (PreparedStatement selectStmt = conn.prepareStatement(SELECT_QUERY)) {
			selectStmt.setString(1, tableName.toUpperCase());
			rs = selectStmt.executeQuery();
			int tableCount = 0;
			if (rs.next()) {
				tableCount = rs.getInt(1);
			}

			if (tableCount > 0) {
				tableExists = true;
			}
		} finally {
			if (rs != null) {
				rs.close();
			}
		}

		Log.exit(tableExists);

		return tableExists;
	}

	/**
	 * This method returns a database connection from the Hikari CP Connection
	 * Pool
	 * 
	 * @return - Connection to the H2 database
	 * 
	 * @throws SQLException
	 */
	public static final Connection getConnection() throws SQLException {
		IConnectionPool pool = null;
		if(!TEST_MODE){
			pool = ConnectionPoolFactory.getInstance()
					.getH2ConnectionPool();
		} else {
			pool = ConnectionPoolFactory.getInstance().getTestH2ConnectionPool();
		}
		return pool.getConnection();
	}
}
