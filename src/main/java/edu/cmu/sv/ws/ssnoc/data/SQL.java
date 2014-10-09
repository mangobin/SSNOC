package edu.cmu.sv.ws.ssnoc.data;

/**
 * This class contains all the SQL related code that is used by the project.
 * Note that queries are grouped by their purpose and table associations for
 * easy maintenance.
 * 
 */
public class SQL {
	/*
	 * List all tables names, and list all queries related to this table
	 * here.
	 */
	public static final String SSN_USERS = "SSN_USERS";
	public static final String SSN_STATUSES = "SSN_STATUSES";
	public static final String SSN_MESSAGES = "SSN_MESSAGES";

	/**
	 * Query to check if a given table exists in the H2 database.
	 */
	public static final String CHECK_TABLE_EXISTS_IN_DB = "SELECT count(1) as rowCount "
			+ " FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = SCHEMA() "
			+ " AND UPPER(TABLE_NAME) = UPPER(?)";

	// ****************************************************************
	// All queries related to USERS
	// ****************************************************************
	/**
	 * Query to create the USERS table.
	 */
	public static final String CREATE_USERS = "create table IF NOT EXISTS "
			+ SSN_USERS + " ( user_id IDENTITY PRIMARY KEY,"
			+ " user_name VARCHAR(100)," + " password VARCHAR(512),"
			+ " salt VARCHAR(512)," + " createdAt DATETIME, "
			+ " modifiedAt DATETIME, " + " lastStatusId BIGINT)";

	/**
	 * Query to load all users in the system.
	 */
	public static final String FIND_ALL_USERS = "select user_id, user_name, password,"
			+ " salt, createdAt, modifiedAt, lastStatusId " 
			+ " from " + SSN_USERS + " order by user_name";

	/**
	 * Query to find a user details depending on his name. Note that this query
	 * does a case insensitive search with the user name.
	 */
	public static final String FIND_USER_BY_NAME = "select user_id, user_name, password,"
			+ " salt, createdAt, modifiedAt, lastStatusId "
			+ " from "
			+ SSN_USERS
			+ " where UPPER(user_name) = UPPER(?)";

	/**
	 * Query to insert a row into the users table.
	 */
	public static final String INSERT_USER = "insert into " + SSN_USERS
			+ " (user_name, password, salt, createdAt, modifiedAt, lastStatusId) "
			+ " values (?, ?, ?, ?, ?, ?)";
	
	/**
	 * Query to update a row into the users table.
	 */
	public static final String UPDATE_USER = "update " + SSN_USERS + " set "
			+ " user_name=?,"
			+ " password=?,"
			+ " salt=?,"
			+ " createdAt=?," 
			+ " modifiedAt=?,"
			+ " lastStatusId=?"
			+ " where user_id=?";
	
	
	// ****************************************************************
	// All queries related to STATUSES
	// ****************************************************************
	/**
	 * Query to create the STATUSES table.
	 */
	public static final String CREATE_STATUSES = "create table IF NOT EXISTS "
			+ SSN_STATUSES + " ( statusId IDENTITY PRIMARY KEY,"
			+ " userName VARCHAR(100)," + " updatedAt DATETIME,"
			+ " statusCode VARCHAR(512),"
			+ " locationLat REAL, " + " locationLng REAL)";

	/**
	 * Query to load all latest STATUSES in the system.
	 */
	public static final String FIND_LATEST_STATUSES = "select statusId, userName, updatedAt,"
			+ " statusCode, locationLat, locationLng " 
			+ " from " + SSN_STATUSES + " order by updatedAt desc"
			+ " limit ?"
			+ " offset ?";

	/**
	 * Query to find a status depending on statusId.
	 */
	public static final String FIND_STATUS_BY_ID = "select statusId, userName, updatedAt,"
			+ " statusCode, locationLat, locationLng "
			+ " from "
			+ SSN_STATUSES
			+ " where statusId = ?";

	public static final String FIND_LATEST_STATUSES_BY_USER_ID = "select statusId, userName, updatedAt,"
			+ " statusCode, locationLat, locationLng "
			+ " from "
			+ SSN_STATUSES
			+ " where UPPER(userName) = UPPER(?)"
			+ " order by updatedAt desc"
			+ " limit ?"
			+ " offset ?";
	
	/**
	 * Query to insert a row into the statuses table.
	 */
	public static final String INSERT_STATUS = "insert into " + SSN_STATUSES
			+ " (userName, updatedAt, statusCode, locationLat, locationLng) "
			+ " values (?, ?, ?, ?, ?)";
	
	/**
	 * Query to update a row into the statuses table.
	 */
	public static final String UPDATE_STATUS = "update " + SSN_STATUSES + " set "
			+ " userName=?,"
			+ " updatedAt=?,"
			+ " statusCode=?,"
			+ " locationLat=?," 
			+ " locationLng=?,"
			+ " where statusId=?";

	// ****************************************************************
	// All queries related to MESSAGES
	// ****************************************************************
	
	public static final String MESSAGE_TYPE_WALL = "WALL";
	public static final String MESSAGE_TYPE_CHAT = "CHAT";	
	/**
	 * Query to create the MESSAGES table.
	 */
	public static final String CREATE_MESSAGES = "create table IF NOT EXISTS "
			+ SSN_MESSAGES + " ( message_id IDENTITY PRIMARY KEY,"
			+ " content VARCHAR," + " author VARCHAR(100),"
			+ " target VARCHAR(100)," + " message_type VARCHAR(10), "
			+ " posted_at DATETIME )";
	
	/**
	 * Query to insert a row into the messages table.
	 */
	public static final String INSERT_MESSAGE = "insert into " + SSN_MESSAGES
			+ " (content, author, target, message_type, posted_at) "
			+ " values (?, ?, ?, ?, ?)";
	
	/**
	 * Query to update a row into the messages table.
	 */
	public static final String UPDATE_MESSAGE = "update " + SSN_MESSAGES + " set "
			+ " content=?,"
			+ " author=?,"
			+ " target=?,"
			+ " message_type=?," 
			+ " posted_at=?"
			+ " where message_id=?";
	
	/**
	 * Query to find a message by message_id
	 */
	 public static final String FIND_MESSAGE_BY_ID = "select * from " 
	 + SSN_MESSAGES
	 + " where message_id=?";
	 
	 /**
	  * Query to find the latest public wall messages
	  */
	 public static final String FIND_LATEST_WALL_MESSAGES = "select * from "
		+ SSN_MESSAGES
		+ " where UPPER(message_type) = " + " UPPER(" + MESSAGE_TYPE_WALL + ")" 
		+ " order by posted_at desc"
		+ " limit ?"
		+ " offset ?";
	 
	 /**
	  * Query to find latest messages of type
	  */
	 public static final String FIND_LATEST_MESSAGES_OF_TYPE = "select * from "
				+ SSN_MESSAGES
				+ " where UPPER(message_type) = " + " UPPER(?)" 
				+ " order by posted_at desc"
				+ " limit ?"
				+ " offset ?";
	 
	 public static final String FIND_ALL_MESSAGES_BETWEEN_TWO_USERS = "SELECT * FROM "
			 	+ SSN_MESSAGES
			 	+ " WHERE (author = ? AND target = ?)"
			 	+ " OR (author = ? AND target = ?)"
			 	+ " AND UPPER(message_type) = " + " UPPER(" + MESSAGE_TYPE_CHAT + ")"
			 	+ " order by posted_at desc";
	 
	 public static final String FIND_CHAT_BUDDIES_AUTHOR= "SELECT target FROM "
			 	+ SSN_MESSAGES
			 	+ " WHERE author = ?"
			 	+ " AND UPPER(message_type) = " + " UPPER(" + MESSAGE_TYPE_CHAT + ")";
	 
	 public static final String FIND_CHAT_BUDDIES_TARGET= "SELECT author FROM "
			 	+ SSN_MESSAGES
			 	+ " WHERE target = ?"
			 	+ " AND UPPER(message_type) = " + " UPPER(" + MESSAGE_TYPE_CHAT + ")";
	  
}
