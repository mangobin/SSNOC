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
import edu.cmu.sv.ws.ssnoc.data.po.RequestPO;
import edu.cmu.sv.ws.ssnoc.data.po.UserPO;

public class RequestDAOImpl extends BaseDAOImpl implements IRequestDAO {

	@Override
	public long save(RequestPO po) {
		Log.enter(po);
		long insertedId=-1;
		if(po == null) {
			return insertedId;
		} else {

			try {
				Connection conn = getConnection();
				PreparedStatement stmt;
				if(findRequestById(po.getRequestId()) == null){
					stmt = conn.prepareStatement(SQL.INSERT_REQUEST, Statement.RETURN_GENERATED_KEYS);
					stmt.setLong(1,po.getRequesterId());
					stmt.setObject(2, po.getType());
					stmt.setTimestamp(3, new Timestamp(po.getCreated_at().getTime()));
					stmt.setString(4, po.getLocation());
					stmt.setString(5, po.getDescription());
					stmt.setString(6, po.getStatus());
					stmt.setString(7, po.getResolutionDetails());
				} else {
					stmt = conn.prepareStatement(SQL.UPDATE_REQUEST);
					stmt.setLong(1,po.getRequesterId());
					stmt.setObject(2, po.getType());
					stmt.setTimestamp(3, new Timestamp(po.getUpdated_at().getTime()));
					stmt.setString(4, po.getLocation());
					stmt.setString(5, po.getDescription());
					stmt.setString(6, po.getStatus());
					stmt.setString(7, po.getResolutionDetails());
					stmt.setLong(8, po.getRequestId());
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
	public RequestPO findRequestById(long requestid) {
		Log.enter(requestid);
		
		RequestPO po = null;
		try {
			Connection conn = getConnection();
			PreparedStatement stmt = conn.prepareStatement(SQL.FIND_REQUEST_BY_ID);
			stmt.setLong(1, requestid);
			List<RequestPO> requests = processResults(stmt);
			
			if(requests.size()==0){
				Log.debug("No request of id: " + requestid);
			} else {
				po = requests.get(0);
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
	public List<RequestPO> findAllRequestsByUserName(String userName) {
		Log.enter("Find all requests by username: "+ userName);
		
		UserPO userPO = DAOFactory.getInstance().getUserDAO().findByName(userName);
		long userId = userPO.getUserId();
		List<RequestPO> requestPOList = null;
		try {
			Connection conn = getConnection();
			PreparedStatement stmt = conn.prepareStatement(SQL.FIND_ALL_REQUEST_BY_USER);
			stmt.setLong(1, userId);
			requestPOList = processResults(stmt);
			conn.close();
		} catch(SQLException e){
			handleException(e);
		} finally {
			Log.exit(requestPOList);
		}
		
		return requestPOList;
	}

	@Override
	public List<RequestPO> getAllRequests(int limit, int offset) {
		Log.enter("Find all requests (limit: " + limit 
				+ ", offset: " + offset + ")");
		
		List<RequestPO> requestPOList = null;
		try {
			Connection conn = getConnection();
			PreparedStatement stmt = conn.prepareStatement(SQL.FIND_ALL_REQUEST);
			stmt.setInt(1, limit);
			stmt.setInt(2, offset);
			requestPOList = processResults(stmt);
			conn.close();
		} catch(SQLException e){
			handleException(e);
		} finally {
			Log.exit(requestPOList);
		}
		
		return requestPOList;
	}
	
	private List<RequestPO> processResults(PreparedStatement stmt) {
		Log.enter(stmt);
		if(stmt == null){
			Log.warn("Inside processResults method with NULL statement object");
			return null;
		}
		
		Log.debug("Executing stmt = " + stmt);
		List<RequestPO> request = new ArrayList<RequestPO>();
		try {
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				RequestPO po = new RequestPO();
				po.setRequestId(rs.getLong(1));
				po.setRequesterId(rs.getLong(2));
				po.setType((String[]) rs.getObject(3));
				po.setCreated_at(new Date(rs.getTimestamp(4).getTime()));
				po.setLocation(rs.getString(5));
				po.setDescription(rs.getString(6));
				po.setStatus(rs.getString(7));
				po.setResolutionDetails(rs.getString(8));
				if(rs.getTimestamp(9) != null) {
					po.setUpdated_at(new Date(rs.getTimestamp(9).getTime()));
				}
				request.add(po);
			}
			if(rs != null){
				rs.close();
			}
		} catch (SQLException e) {
			handleException(e);
		} finally {
			Log.exit(request);
		}
		
		return request;
	}

}
