package edu.cmu.sv.ws.ssnoc.data.nosql.dao;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.WriteConcern;

import edu.cmu.sv.ws.ssnoc.data.nosql.util.DBUtils;

public class BaseDAOImpl<T> {
	
	protected DBCollection getCollection(String collectionName){
		return getCollection(collectionName, getWriteConcern());
	}
	
	protected DBCollection getCollection(String collectionName, WriteConcern writeConcern){
		DBCollection collection = DBUtils.getDatabase().getCollection(collectionName);
		collection.setWriteConcern(writeConcern);
		return collection;
	}

	protected WriteConcern getWriteConcern(){
		return WriteConcern.ACKNOWLEDGED;
	}
	
	protected BasicDBObject convertToDB(T po){
		return null;
	}
	
	protected T convertToPO(BasicDBObject db){
		return null;
	}
	
}
