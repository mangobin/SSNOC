package edu.cmu.sv.ws.ssnoc.data.nosql.util;

import java.net.UnknownHostException;

import com.mongodb.DB;
import com.mongodb.MongoClient;

import edu.cmu.sv.ws.ssnoc.common.config.MongoDBConfig;
import edu.cmu.sv.ws.ssnoc.common.logging.Log;

public class DBUtils {

	private static MongoClient sClient = null;
	
	/*
	 * Initializes the MongoDB Connection
	 */
	public static void initializeDatabase() throws UnknownHostException {
		if(sClient == null){
			sClient = new MongoClient(MongoDBConfig.DATABASE_URL, MongoDBConfig.DATABASE_PORT);
		}
	}
	
	/*
	 * Returns the connection to MongoDB
	 */
	private static MongoClient getDatabaseClient() {
		if(sClient == null){
			try {
				initializeDatabase();
			} catch (UnknownHostException e) {
				Log.error("Failed to return MongoDB Client", e);
				e.printStackTrace();
			}
		}
		return sClient;
	}
	
	/*
	 * Get the database associated with this application
	 */
	public static DB getDatabase(){
		return getDatabaseClient().getDB(MongoDBConfig.DATABASE_NAME);
	}
	
}
