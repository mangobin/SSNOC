package edu.cmu.sv.ws.ssnoc.data.nosql.dao;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.WriteResult;

import edu.cmu.sv.ws.ssnoc.data.dao.IStatusDao;
import edu.cmu.sv.ws.ssnoc.data.po.StatusPO;

public class StatusDAOImpl extends BaseDAOImpl<StatusPO> implements IStatusDao {

	private static String COLLECTION_NAME = "statuses";
	
	private static String KEY_ID = "_id";
	private static String KEY_USER_ID = "userId";
	private static String KEY_STATUS_CODE = "statusCode";
	private static String KEY_UPDATED_AT = "updatedAt";
	
	@Override
	public String save(StatusPO statusPO) {
		DBCollection coll = getCollection(COLLECTION_NAME);
		BasicDBObject obj = convertToDB(statusPO);
		WriteResult result = coll.insert(obj);
		if(result.getUpsertedId() != null){
			return result.getUpsertedId().toString();
		} else {
			return null;
		}
	}

	@Override
	public StatusPO findStatusById(String statusId) {
		DBCollection coll = getCollection(COLLECTION_NAME);
		
		BasicDBObject obj = new BasicDBObject(KEY_ID, new ObjectId(statusId));
		DBObject one  = coll.findOne(obj);
				
		StatusPO po = null;
		if(one != null){
			po = convertToPO((BasicDBObject)one);
		}
		
		return po;
	}

	@Override
	public StatusPO findLatestStatusByUserId(String userId) {
		List<StatusPO> pos = findLatestStatusesByUserId(userId, 1, 0);
		return (pos.size() > 0) ? pos.get(0) : null;
	}

	@Override
	public List<StatusPO> findLatestStatusesByUserId(String userId, int limit,
			int offset) {
		DBCollection coll = getCollection(COLLECTION_NAME);

		List<StatusPO> pos = new ArrayList<StatusPO>();
		
		BasicDBObject filter = new BasicDBObject(KEY_USER_ID, userId);
		BasicDBObject sort = new BasicDBObject(KEY_UPDATED_AT, -1); //descending order

		DBCursor cursor = coll.find(filter).sort(sort).skip(offset).limit(limit);
		try {
		   while(cursor.hasNext()) {
			   DBObject obj = cursor.next();
			   StatusPO po = convertToPO((BasicDBObject)obj);
			   pos.add(po);
		   }
		} finally {
		   cursor.close();
		}
		
		return pos;
	}

	@Override
	public List<StatusPO> loadLatestStatuses(int limit, int offset) {
		DBCollection coll = getCollection(COLLECTION_NAME);

		List<StatusPO> pos = new ArrayList<StatusPO>();
		
		BasicDBObject sort = new BasicDBObject(KEY_UPDATED_AT, -1); //descending order

		DBCursor cursor = coll.find().sort(sort).skip(offset).limit(limit);
		try {
		   while(cursor.hasNext()) {
			   DBObject obj = cursor.next();
			   StatusPO po = convertToPO((BasicDBObject)obj);
			   pos.add(po);
		   }
		} finally {
		   cursor.close();
		}
		
		return pos;
	}
	
	protected BasicDBObject convertToDB(StatusPO po){
		BasicDBObject db = new BasicDBObject();
		if(po.getStatusId() != null){
			db.append(KEY_ID, new ObjectId(po.getStatusId()));
		}
		db.append(KEY_USER_ID, po.getUserId());
		db.append(KEY_UPDATED_AT, po.getUpdatedAt());
		db.append(KEY_STATUS_CODE, po.getStatusCode());
		return db;
	}
	
	protected StatusPO convertToPO(BasicDBObject db){
		StatusPO.Builder builder = new StatusPO.Builder();
		
		builder.setStatusId(db.getString(KEY_ID));
		builder.setUserId(db.getString(KEY_USER_ID));
		builder.setUpdatedAt(db.getDate(KEY_UPDATED_AT));
		builder.setStatusCode(db.getInt(KEY_STATUS_CODE));
		
		return builder.build();
	}

}
