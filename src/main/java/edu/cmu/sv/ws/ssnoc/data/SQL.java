package edu.cmu.sv.ws.ssnoc.data;



/**
 * This class contains all the SQL related code that is used by the project.
 * Note that queries are grouped by their purpose and table associations for
 * easy maintenance.
 * 
 */
public class SQL {
	/*
	 * List all tables names, and list all queries related to this table here.
	 */
	public static final String SSN_USERS = "SSN_USERS";
	public static final String SSN_STATUSES = "SSN_STATUSES";
	public static final String SSN_MESSAGES = "SSN_MESSAGES";
	public static final String SSN_MEMORY = "SSN_MEMORY";
	public static final String SSN_FAKE_MESSAGES = "SSN_FAKE_MESSAGES";
	public static final String SSN_REQUEST = "SSN_REQUEST";
	public static final String SSN_RESPONDER = "SSN_RESPONDER";
	
	public static final String DROP_TABLE_IN_DB = "DROP TABLE IF EXISTS ";
	public static final String TRUNCATE_TABLE_IN_DB = "TRUNCATE TABLE ";

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
			+ " modifiedAt DATETIME, " + " lastStatusId BIGINT, "
			+ " privilegeLevel VARCHAR(20), " + " accountStatus VARCHAR(20), "
			+ " latitude VARCHAR(30), " + " longitude VARCHAR(30), "
			+ " location_updatedAt DATETIME ) ";

	/**
	 * Query to load all users in the system.
	 */
	public static final String FIND_ALL_USERS = "select user_id, user_name, password,"
			+ " salt, createdAt, modifiedAt, lastStatusId, privilegeLevel, accountStatus, "
			+ " latitude, longitude, location_updatedAt "
			+ " from " + SSN_USERS + " order by user_name";

	/**
	 * Query to find a user details depending on his name. Note that this query
	 * does a case insensitive search with the user name.
//	 */
//	public static final String FIND_USER_BY_NAME = "select user_id, user_name, password,"
//			+ " salt, createdAt, modifiedAt, lastStatusId, privilegeLevel, accountStatus  "
//			+ " latitude, longitude, location_updatedAt "
//			+ " from " + SSN_USERS + " where UPPER(user_name) = UPPER(?)";

	public static final String FIND_USER_BY_NAME = "select * "
			+ " from " + SSN_USERS + " where UPPER(user_name) = UPPER(?)";
//
//	public static final String FIND_USER_BY_ID = "select user_id, user_name, password,"
//			+ " salt, createdAt, modifiedAt, lastStatusId, privilegeLevel, accountStatus  "
//			+ " latitude, longitude, location_updatedAt  "
//			+ " from " + SSN_USERS + " where user_id = ?";

	public static final String FIND_USER_BY_ID = "select * "
			+ " from " + SSN_USERS + " where user_id = ?";
	/**
	 * Query to insert a row into the users table.
	 */
	public static final String INSERT_USER = "insert into "
			+ SSN_USERS
			+ " (user_name, password, salt, createdAt, modifiedAt, lastStatusId, "
			+ " privilegeLevel, accountStatus, latitude, longitude, location_updatedAt ) "
			+ " values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

	/**
	 * Query to update a row into the users table.
	 */
	public static final String UPDATE_USER = "update " + SSN_USERS + " set "
			+ " user_name=?," + " password=?," + " salt=?," + " createdAt=?,"
			+ " modifiedAt=?," + " lastStatusId=?," + " privilegeLevel=?,"
			+ " accountStatus=?, " 
			+ " latitude=?," + " longitude=?," + " location_updatedAt=? "
			+ " where user_id=?";

	// ****************************************************************
	// All queries related to STATUSES
	// ****************************************************************
	/**
	 * Query to create the STATUSES table.
	 */
	public static final String CREATE_STATUSES = "create table IF NOT EXISTS "
			+ SSN_STATUSES + " ( statusId IDENTITY PRIMARY KEY,"
			+ " user_id BIGINT," + " updatedAt DATETIME,"
			+ " statusCode VARCHAR(512)," + " locationLat REAL, "
			+ " locationLng REAL)";

	/**
	 * Query to load all latest STATUSES in the system.
	 */
	public static final String FIND_LATEST_STATUSES = "select statusId, user_id, updatedAt,"
			+ " statusCode, locationLat, locationLng "
			+ " from "
			+ SSN_STATUSES
			+ " order by updatedAt desc"
			+ " limit ?"
			+ " offset ?";

	/**
	 * Query to find a status depending on statusId.
	 */
	public static final String FIND_STATUS_BY_ID = "select statusId, user_id, updatedAt,"
			+ " statusCode, locationLat, locationLng"
			+ " from "
			+ SSN_STATUSES
			+ " where statusId = ?";

	public static final String FIND_LATEST_STATUSES_BY_USER_ID = "select statusId, user_id, updatedAt,"
			+ " statusCode, locationLat, locationLng "
			+ " from "
			+ SSN_STATUSES
			+ " where user_id = ?"
			+ " order by updatedAt desc"
			+ " limit ?" + " offset ?";

	/**
	 * Query to insert a row into the statuses table.
	 */
	public static final String INSERT_STATUS = "insert into " + SSN_STATUSES
			+ " (user_id, updatedAt, statusCode, locationLat, locationLng) "
			+ " values (?, ?, ?, ?, ?)";

	/**
	 * Query to update a row into the statuses table.
	 */
	public static final String UPDATE_STATUS = "update " + SSN_STATUSES
			+ " set " + " user_id=?," + " updatedAt=?," + " statusCode=?,"
			+ " locationLat=?," + " locationLng=?" + " where statusId=?";

	// ****************************************************************
	// All queries related to MESSAGES
	// ****************************************************************

	public static final String MESSAGE_TYPE_WALL = "WALL";
	public static final String MESSAGE_TYPE_CHAT = "CHAT";
	public static final String MESSAGE_TYPE_ANNOUNCEMENT = "PA";
	/**
	 * Query to create the MESSAGES table.
	 */
	public static final String CREATE_MESSAGES = "create table IF NOT EXISTS "
			+ SSN_MESSAGES + " ( message_id IDENTITY PRIMARY KEY,"
			+ " content VARCHAR," + " author BIGINT," + " target BIGINT,"
			+ " message_type VARCHAR(10), " + " posted_at DATETIME )";

	public static final String CREATE_FAKE_MESSAGES = "create table IF NOT EXISTS "
			+ SSN_FAKE_MESSAGES
			+ " ( message_id IDENTITY PRIMARY KEY,"
			+ " content VARCHAR,"
			+ " author BIGINT,"
			+ " target BIGINT,"
			+ " message_type VARCHAR(10), " + " posted_at DATETIME )";

	public static final String DELETE_FAKE_MESSAGES = "TRUNCATE TABLE "
			+ SSN_FAKE_MESSAGES;

	public static final String DELETE_MESSAGES = "TRUNCATE TABLE "
			+ SSN_MESSAGES;

	/**
	 * Query to insert a row into the messages table.
	 */
	public static final String INSERT_MESSAGE = "insert into " + SSN_MESSAGES
			+ " (content, author, target, message_type, posted_at) "
			+ " values (?, ?, ?, ?, ?)";

	public static final String INSERT_FAKE_MESSAGE = "insert into "
			+ SSN_FAKE_MESSAGES
			+ " (content, author, target, message_type, posted_at) "
			+ " values (?, ?, ?, ?, ?)";

	/**
	 * Query to update a row into the messages table.
	 */
	public static final String UPDATE_MESSAGE = "update " + SSN_MESSAGES
			+ " set " + " content=?," + " author=?," + " target=?,"
			+ " message_type=?," + " posted_at=?" + " where message_id=?";

	public static final String UPDATE_FAKE_MESSAGE = "update "
			+ SSN_FAKE_MESSAGES + " set " + " content=?," + " author=?,"
			+ " target=?," + " message_type=?," + " posted_at=?"
			+ " where message_id=?";

	/**
	 * Query to find a message by message_id
	 */
	public static final String FIND_MESSAGE_BY_ID = "select * from "
			+ SSN_MESSAGES + " where message_id=?";

	public static final String FIND_FAKE_MESSAGE_BY_ID = "select * from "
			+ SSN_FAKE_MESSAGES + " where message_id=?";

	/**
	 * Query to find the latest public wall messages
	 */
	public static final String FIND_LATEST_WALL_MESSAGES = "select * from "
			+ SSN_MESSAGES + " where UPPER(message_type) = " + " UPPER("
			+ MESSAGE_TYPE_WALL + ")" + " order by posted_at desc" + " limit ?"
			+ " offset ?";

	/**
	 * Query to find latest messages of type
	 */
	public static final String FIND_LATEST_MESSAGES_OF_TYPE = "select * from "
			+ SSN_MESSAGES + " where UPPER(message_type) = " + " UPPER(?)"
			+ " order by posted_at desc" + " limit ?" + " offset ?";

	public static final String FIND_FAKE_LATEST_MESSAGES_OF_TYPE = "select * from "
			+ SSN_FAKE_MESSAGES
			+ " where UPPER(message_type) = "
			+ " UPPER(?)"
			+ " order by posted_at desc" + " limit ?" + " offset ?";

	public static final String FIND_ALL_MESSAGES_BETWEEN_TWO_USERS = "SELECT * FROM "
			+ SSN_MESSAGES
			+ " WHERE ((author = ? AND target = ?)"
			+ " OR (author = ? AND target = ?))"
			+ " AND (UPPER(message_type) = "
			+ " UPPER(?))"
			+ " order by posted_at desc";

	public static final String FIND_CHAT_BUDDIES_AUTHOR = "SELECT target FROM "
			+ SSN_MESSAGES + " WHERE author = ?"
			+ " AND UPPER(message_type) = " + " UPPER(?)";

	public static final String FIND_CHAT_BUDDIES_TARGET = "SELECT author FROM "
			+ SSN_MESSAGES + " WHERE target = ?"
			+ " AND UPPER(message_type) = " + " UPPER(?)";

	public static final String FIND_CHAT_MESSAGES_SINCE_DATE = "SELECT * FROM "
			+ SSN_MESSAGES + " WHERE posted_at >= ?"
			+ " AND UPPER(message_type) = " + " UPPER(?)";

	// ****************************************************************
	// All queries related to memory SQL
	// ****************************************************************

	public static final String CREATE_MEMORY = "CREATE TABLE IF NOT EXISTS "
			+ SSN_MEMORY + " (memoryID IDENTITY PRIMARY KEY, "
			+ " createdAt DATETIME, usedVolatile BIGINT,"
			+ " remainingVolatile BIGINT, usedPersistent BIGINT,"
			+ " remainingPersistent BIGINT )";

	public static final String INSERT_MEMORY = "INSERT INTO " + SSN_MEMORY
			+ " (createdAt, usedVolatile, remainingVolatile, usedPersistent, "
			+ " remainingPersistent)" + " values(?,?,?,?,?)";

	public static final String DELETE_MEMORY = "TRUNCATE TABLE " + SSN_MEMORY;

	public static final String UPDATE_MEMORY = "UPDATE "
			+ SSN_MEMORY
			+ " SET "
			+ " createdAt=?, usedVolatile=?, remainingVolatile=?, usedPersistent=?, "
			+ " remainingPersistent=?" + " where memoryID=?";

	public static final String FIND_MEMORY_BY_ID = "SELECT * FROM "
			+ SSN_MEMORY + " WHERE memoryID = ?";

	public static final String FIND_MEMORY_GREATER_THAN_DATE = "SELECT * FROM "
			+ SSN_MEMORY + " WHERE createdAt>=?";

	// ****************************************************************
	// All queries related to request SQL
	// ****************************************************************

	public static final String CREATE_REQUESTS = "create table IF NOT EXISTS "
			+ SSN_REQUEST + " ( requestId IDENTITY PRIMARY KEY,"
			+ " requesterId BIGINT," + " type ARRAY," + " created_at DATETIME,"
			+ " location VARCHAR , " + " description VARCHAR , "
			+ " status VARCHAR (30), " + " resolutionDetails VARCHAR , "
			+ " updated_at DATETIME )";

	public static final String INSERT_REQUEST = "INSERT INTO "
			+ SSN_REQUEST
			+ " (requesterId, type, created_at, location, "
			+ " description, status, resolutionDetails)" + " values(?,?,?,?,?,?,?)";

	public static final String UPDATE_REQUEST = "UPDATE "
			+ SSN_REQUEST
			+ " SET "
			+ " requesterId=?, type=?, updated_at=?, location=?, "
			+ " description=?, status=?, resolutionDetails=? "
			+ " where requestId=?";

	public static final String FIND_REQUEST_BY_ID = "SELECT * FROM "
			+ SSN_REQUEST + " WHERE requestId = ?";
	

	public static final String FIND_ALL_REQUEST = "SELECT * FROM "
			+ SSN_REQUEST 
			+ " order by created_at desc" + " limit ?"
			+ " offset ?";
	
	public static final String FIND_ALL_REQUEST_BY_USER = "SELECT * FROM "
			+ SSN_REQUEST
			+ " WHERE requesterId = ?";
	

	// ****************************************************************
	// All queries related to Responder SQL
	// ****************************************************************
	
	public static final String CREATE_RESPONDERS = "create table IF NOT EXISTS "
			+ SSN_RESPONDER + " ( responderId IDENTITY PRIMARY KEY,"
			+ " requestId BIGINT," + " userId BIGINT," 
			+ " status VARCHAR (30), " 
			+ " updated_at DATETIME )";
	
	public static final String INSERT_RESPONDER = "INSERT INTO "
			+ SSN_RESPONDER
			+ " (requestId, userId, status, "
			+ " updated_at )" + " values(?,?,?,?)";

	public static final String UPDATE_RESPONDER = "UPDATE "
			+ SSN_RESPONDER
			+ " SET "
			+ " requestId=?, userId=?, status=?, "
			+ " updated_at=? "
			+ " where responderId=?";
	
	public static final String FIND_RESPONDER_BY_ID = "SELECT * FROM "
			+ SSN_RESPONDER + " WHERE responderId = ?";
	
	public static final String FIND_ALL_RESPONDER_BY_USERID = "SELECT * FROM "
			+ SSN_RESPONDER
			+ " WHERE userId = ?"
			+ " order by updated_at desc";
	
	public static final String FIND_ALL_RESPONDER_BY_REQUESTID = "SELECT * FROM "
			+ SSN_RESPONDER
			+ " WHERE requestId = ?"
			+ " order by updated_at desc";
	
	public static final String DELETE_ALL_RESPONDER_BY_REQUESTID = "DELETE  FROM "
			+ SSN_RESPONDER
			+ " WHERE requestId = ?";
	
	
}
