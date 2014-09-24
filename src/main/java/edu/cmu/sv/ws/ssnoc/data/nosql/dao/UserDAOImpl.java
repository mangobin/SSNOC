package edu.cmu.sv.ws.ssnoc.data.nosql.dao;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.WriteResult;

import edu.cmu.sv.ws.ssnoc.data.dao.IUserDAO;
import edu.cmu.sv.ws.ssnoc.data.po.UserPO;

public class UserDAOImpl extends BaseDAOImpl<UserPO> implements IUserDAO {
	
	private static String COLLECTION_NAME = "users";
	
	private static String KEY_ID = "_id";
	private static String KEY_USERNAME = "username";
	private static String KEY_PASSWORD = "password";
	private static String KEY_SALT = "salt";

	@Override
	public void save(UserPO userPO) {
		DBCollection coll = getCollection(COLLECTION_NAME);
		BasicDBObject obj = convertToDB(userPO);
		coll.insert(obj);
	}

	@Override
	public List<UserPO> loadUsers() {
		DBCollection coll = getCollection(COLLECTION_NAME);

		List<UserPO> pos = new ArrayList<UserPO>();
		
		DBCursor cursor = coll.find();
		try {
		   while(cursor.hasNext()) {
			   DBObject obj = cursor.next();
			   UserPO po = convertToPO((BasicDBObject)obj);
			   pos.add(po);
		   }
		} finally {
		   cursor.close();
		}
		
		return pos;
	}

	@Override
	public UserPO findByName(String userName) {	
		DBCollection coll = getCollection(COLLECTION_NAME);
		
		BasicDBObject obj = new BasicDBObject(KEY_USERNAME, userName);
		DBObject one  = coll.findOne(obj);
				
		UserPO po = null;
		if(one != null){
			po = convertToPO((BasicDBObject)one);
		}
		
		return po;
	}
	
	protected BasicDBObject convertToDB(UserPO po){
		BasicDBObject db = new BasicDBObject();
		if(po.getUserIdStr() != null){
			db.append(KEY_ID, new ObjectId(po.getUserIdStr()));
		}
		db.append(KEY_USERNAME, po.getUserName());
		db.append(KEY_PASSWORD, po.getPassword());
		db.append(KEY_SALT, po.getSalt());
		return db;
	}
	
	protected UserPO convertToPO(BasicDBObject db){
		UserPO po = new UserPO();
		po.setUserIdStr(db.getString(KEY_ID));
		po.setUserName(db.getString(KEY_USERNAME));
		po.setPassword(db.getString(KEY_PASSWORD));
		po.setSalt(db.getString(KEY_SALT));
		return po;
	}

}
