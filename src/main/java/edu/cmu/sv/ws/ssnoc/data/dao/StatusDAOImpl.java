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
import edu.cmu.sv.ws.ssnoc.data.po.StatusPO;

public class StatusDAOImpl extends BaseDAOImpl implements IStatusDao {

	@Override
	public long save(StatusPO statusPO) {
		Log.enter(statusPO);
		if(statusPO == null){
			Log.warn("Inside save method with StatusPO == NULL");
			return -1;
		}
		
		long insertedId=-1;
	
		try {
			Connection conn = getConnection();
			PreparedStatement stmt;
			if(findStatusById(statusPO.getStatusId()) == null){
				stmt = conn.prepareStatement(SQL.INSERT_STATUS, Statement.RETURN_GENERATED_KEYS);
				stmt.setString(1, statusPO.getUserName());
				stmt.setTimestamp(2, new Timestamp(statusPO.getUpdatedAt().getTime()));
				stmt.setString(3, statusPO.getStatusCode());
				stmt.setFloat(4, statusPO.getLocLat());
				stmt.setFloat(5, statusPO.getLocLng());
			} else {
				stmt = conn.prepareStatement(SQL.UPDATE_STATUS);
				stmt.setString(1, statusPO.getUserName());
				stmt.setTimestamp(2, new Timestamp(statusPO.getUpdatedAt().getTime()));
				stmt.setString(3, statusPO.getStatusCode());
				stmt.setFloat(4, statusPO.getLocLat());
				stmt.setFloat(5, statusPO.getLocLng());
				stmt.setLong(6, statusPO.getStatusId());
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

	@Override
	public StatusPO findStatusById(long statusId) {
		Log.enter(statusId);
		
		StatusPO po = null;
		try {
			Connection conn = getConnection();
			PreparedStatement stmt = conn.prepareStatement(SQL.FIND_STATUS_BY_ID);
			stmt.setLong(1, statusId);
			List<StatusPO> statuses = processResults(stmt);
			
			if(statuses.size() == 0){
				Log.debug("No status of id: " + statusId);
			} else {
				po = statuses.get(0);
			}
			conn.close();
		} catch (SQLException e){
			handleException(e);
		} finally {
			Log.exit(po);
		}
		
		return po;
	}

	@Override
	public List<StatusPO> loadLatestStatuses(int limit, int offset) {
		Log.enter();
		List<StatusPO> statuses = null;
		try {
			Connection conn = getConnection();
			PreparedStatement stmt = conn.prepareStatement(SQL.FIND_LATEST_STATUSES);
			stmt.setInt(1, limit);
			stmt.setInt(2, offset);
			statuses = processResults(stmt);
			conn.close();
		} catch (SQLException e){
			handleException(e);
			Log.exit(statuses);
		}
		return statuses;
	}

	@Override
	public StatusPO findLatestStatusByUserId(String userId) {
		Log.enter(userId);
		StatusPO po = null;
		try {
			Connection conn = getConnection();
			PreparedStatement stmt = conn.prepareStatement(SQL.FIND_LATEST_STATUSES_BY_USER_ID);
			stmt.setString(1, userId);
			stmt.setInt(2, 1); // limit 1
			stmt.setInt(3, 0); // offset 0
			List<StatusPO> statuses = processResults(stmt);
			if(statuses.size() > 0){
				po = statuses.get(0);
			}
			conn.close();
		} catch (SQLException e){
			handleException(e);
			Log.exit(po);
		}
		return po;
	}

	@Override
	public List<StatusPO> findLatestStatusesByUserId(String userId, int limit,
			int offset) {
		Log.enter(userId);
		List<StatusPO> statuses = null;
		try {
			Connection conn = getConnection();
			PreparedStatement stmt = conn.prepareStatement(SQL.FIND_LATEST_STATUSES_BY_USER_ID);
			stmt.setString(1, userId);
			stmt.setInt(2, limit);
			stmt.setInt(3, offset);
			statuses = processResults(stmt);
			conn.close();
		} catch (SQLException e){
			handleException(e);
			Log.exit(statuses);
		}
		return statuses;
	}

	private List<StatusPO> processResults(PreparedStatement stmt) {
		Log.enter(stmt);

		if (stmt == null) {
			Log.warn("Inside processResults method with NULL statement object.");
			return null;
		}

		Log.debug("Executing stmt = " + stmt);
		List<StatusPO> statuses = new ArrayList<StatusPO>();
		try {
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				StatusPO po = new StatusPO();
				po.setStatusId(rs.getLong(1));
				po.setUserName(rs.getString(2));
				po.setUpdatedAt(new Date(rs.getTimestamp(3).getTime()));
				po.setStatusCode(rs.getString(4));
				po.setLocLat(rs.getFloat(5));
				po.setLocLng(rs.getFloat(6));
				statuses.add(po);
			}
			if(rs != null){
				rs.close();
			}
		} catch (SQLException e) {
			handleException(e);
		} finally {
			Log.exit(statuses);
		}

		return statuses;
	}
}
