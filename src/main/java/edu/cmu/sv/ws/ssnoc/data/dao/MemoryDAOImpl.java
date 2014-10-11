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
import edu.cmu.sv.ws.ssnoc.data.po.MemoryPO;

public class MemoryDAOImpl extends BaseDAOImpl implements IMemoryDAO {

	@Override
	public long save(MemoryPO memoryPO) {
		Log.enter(memoryPO);
		if(memoryPO == null){
			Log.warn("Inside save method with MemoryPO == NULL");
			return -1;
		}
		
		long insertedId=-1;
		
		try {
			Connection conn = getConnection();
			PreparedStatement stmt;
			if(findMemoryByID(memoryPO.getMemoryID()) == null){
				stmt = conn.prepareStatement(SQL.INSERT_MEMORY, Statement.RETURN_GENERATED_KEYS);
				stmt.setTimestamp(2, new Timestamp(memoryPO.getCreatedAt().getTime()));
				stmt.setInt(3, memoryPO.getUsedVolatile());
				stmt.setInt(4, memoryPO.getRemainingVolatile());
				stmt.setInt(5, memoryPO.getUsedPersistent());
				stmt.setInt(6, memoryPO.getRemainingPersistent());
			} else {
				stmt = conn.prepareStatement(SQL.UPDATE_MEMORY);
				stmt.setLong(1, memoryPO.getMemoryID());
				stmt.setTimestamp(2, new Timestamp(memoryPO.getCreatedAt().getTime()));
				stmt.setInt(3, memoryPO.getUsedVolatile());
				stmt.setInt(4, memoryPO.getRemainingVolatile());
				stmt.setInt(5, memoryPO.getUsedPersistent());
				stmt.setInt(6, memoryPO.getRemainingPersistent());
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
	public MemoryPO findMemoryByID(long memoryId) {
		Log.enter(memoryId);
		
		MemoryPO po = null;
		try {
			Connection conn = getConnection();
			PreparedStatement stmt = conn.prepareStatement(SQL.FIND_MEMORY_BY_ID);
			stmt.setLong(1, memoryId);
			List<MemoryPO> memory = processResults(stmt);
			
			if(memory.size()==0){
				Log.debug("No memory of id: " + memoryId);
			} else {
				po = memory.get(0);
			}
			conn.close();
		} catch(SQLException e){
			handleException(e);
		} finally {
			Log.exit(po);
		}
		
		return po;
	}
	
	private List<MemoryPO> processResults(PreparedStatement stmt) {
		Log.enter(stmt);
		if(stmt == null){
			Log.warn("Inside processResults method with NULL statement object");
			return null;
		}
		
		Log.debug("Executing stmt = " + stmt);
		List<MemoryPO> memory = new ArrayList<MemoryPO>();
		try {
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				MemoryPO po = new MemoryPO();
				po.setMemoryID(rs.getLong(1));
				po.setCreatedAt(new Date(rs.getTimestamp(2).getTime()));
				po.setUsedVolatile(rs.getInt(3));
				po.setRemainingVolatile(rs.getInt(4));
				po.setUsedPersistent(rs.getInt(5));
				po.setRemainingPersistent(rs.getInt(6));
				memory.add(po);
			}
			if(rs != null){
				rs.close();
			}
		} catch (SQLException e) {
			handleException(e);
		} finally {
			Log.exit(memory);
		}
		
		return memory;
	}
	
	public void deleteAllMemoryCrumbs() {
		Log.enter("enter delete all memory crumbs");
		
		try {
			Connection conn = getConnection();
			PreparedStatement stmt = conn.prepareStatement(SQL.DELETE_MEMORY);
			stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
	}

}
