package edu.cmu.sv.ws.ssnoc.data.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import edu.cmu.sv.ws.ssnoc.common.logging.Log;
import edu.cmu.sv.ws.ssnoc.data.SQL;
import edu.cmu.sv.ws.ssnoc.data.po.ResponderPO;
import edu.cmu.sv.ws.ssnoc.data.po.UserPO;

public class ResponderDAOImpl extends BaseDAOImpl implements IResponderDAO {

	@Override
	public long save(ResponderPO po) {
		Log.enter(po);
		long insertedId=-1;
		if(po == null) {
			return insertedId;
		} else {
			try {
				Connection conn = getConnection();
				PreparedStatement stmt;
				if(findResponderById(po.getResponderId()) == null){
					stmt = conn.prepareStatement(SQL.INSERT_RESPONDER, Statement.RETURN_GENERATED_KEYS);
					stmt.setLong(1,po.getRequestId());
					stmt.setLong(2,po.getUserId());
					stmt.setString(3, po.getStatus());
					stmt.setTimestamp(4, new Timestamp(po.getUpdated_at().getTime()));
				} else {
					stmt = conn.prepareStatement(SQL.UPDATE_RESPONDER);
					stmt.setLong(1,po.getRequestId());
					stmt.setLong(2,po.getUserId());
					stmt.setString(3, po.getStatus());
					stmt.setTimestamp(4, new Timestamp(po.getUpdated_at().getTime()));
					stmt.setLong(5, po.getResponderId());
				}
				int rowCount = stmt.executeUpdate();
				Log.debug("Statement executed, and " + rowCount + " rows inserted.");
				ResultSet generatedKeys = stmt.getGeneratedKeys();
				if(generatedKeys.next()){
					insertedId = generatedKeys.getLong(1);
				}
				conn.close();
			} catch(SQLException e){
				handleException(e);
			} finally {
				Log.exit();
			}
			
			return insertedId;
		}
	
	}

	@Override
	public ResponderPO findResponderById(long id) {

		Log.enter(id);
		
		ResponderPO po = null;
		try {
			Connection conn = getConnection();
			PreparedStatement stmt = conn.prepareStatement(SQL.FIND_RESPONDER_BY_ID);
			stmt.setLong(1, id);
			List<ResponderPO> responders = processResults(stmt);
			
			if(responders.size()==0){
				Log.debug("No responder of id: " + id);
			} else {
				po = responders.get(0);
			}
			conn.close();
		} catch(SQLException e){
			handleException(e);
		} finally {
			Log.exit(po);
		}
		
		return po;
	}

	@Override
	public List<ResponderPO> findRespondersByUserName(String userName) {

		Log.enter("Find all responders by username: "+ userName);
		List<ResponderPO> responderPOList = new ArrayList<ResponderPO>();
		
		UserPO userPO = DAOFactory.getInstance().getUserDAO().findByName(userName);
		if(userPO == null) {
			Log.info("non existing username: "+ userName);
			return responderPOList;
		}
		try {
			Connection conn = getConnection();
			PreparedStatement stmt = conn.prepareStatement(SQL.FIND_ALL_RESPONDER_BY_USERID);
			stmt.setLong(1, userPO.getUserId());
			responderPOList = processResults(stmt);
			conn.close();
		} catch(SQLException e){
			handleException(e);
		} finally {
			Log.exit(responderPOList);
		}
		
		return responderPOList;
	
	}
	
	private List<ResponderPO> processResults(PreparedStatement stmt) {
		Log.enter(stmt);
		if(stmt == null){
			Log.warn("Inside processResults method with NULL statement object");
			return null;
		}
		
		Log.debug("Executing stmt = " + stmt);
		List<ResponderPO> responder = new ArrayList<ResponderPO>();
		try {
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				ResponderPO po = new ResponderPO();
				po.setResponderId(rs.getLong(1));
				po.setRequestId(rs.getLong(2));
				po.setUserId(rs.getLong(3));
				po.setStatus(rs.getString(4));
				po.setUpdated_at((new Date(rs.getTimestamp(5).getTime())));
				
				responder.add(po);
			}
			if(rs != null){
				rs.close();
			}
		} catch (SQLException e) {
			handleException(e);
		} finally {
			Log.exit(responder);
		}
		
		return responder;
	}

	@Override
	public List<ResponderPO> findRespondersByRequestId(long requestId) {
		Log.enter("Find all responders by requestId: "+ requestId);
		List<ResponderPO> responderPOList = new ArrayList<ResponderPO>();
		
		try {
			Connection conn = getConnection();
			PreparedStatement stmt = conn.prepareStatement(SQL.FIND_ALL_RESPONDER_BY_REQUESTID);
			stmt.setLong(1, requestId);
			responderPOList = processResults(stmt);
			conn.close();
		} catch(SQLException e){
			handleException(e);
		} finally {
			Log.exit(responderPOList);
		}
		
		return responderPOList;
	
	}

}
