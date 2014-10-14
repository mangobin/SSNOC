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
import edu.cmu.sv.ws.ssnoc.data.po.MessagePO;
import edu.cmu.sv.ws.ssnoc.data.po.UserPO;

public class MessageDAOFakeImpl extends BaseDAOImpl implements IMessageDAO{
	
	@Override
	public long save(MessagePO messagePO) {
		Log.enter(messagePO);
		if(messagePO == null){
			Log.warn("Inside save method with MessagePO == NULL");
			return -1;
		}
		
		long insertedId=-1;
		
		try {
			Connection conn = getConnection();
			PreparedStatement stmt;
			if(findMessageById(messagePO.getMessageId()) == null){
				stmt = conn.prepareStatement(SQL.INSERT_FAKE_MESSAGE, Statement.RETURN_GENERATED_KEYS);
				stmt.setString(1, messagePO.getContent());
				stmt.setString(2, messagePO.getAuthor());
				stmt.setString(3, messagePO.getTarget());
				stmt.setString(4, messagePO.getMessageType());
				stmt.setTimestamp(5, new Timestamp(messagePO.getPostedAt().getTime()));
			} else {
				stmt = conn.prepareStatement(SQL.UPDATE_FAKE_MESSAGE);
				stmt.setString(1, messagePO.getContent());
				stmt.setString(2, messagePO.getAuthor());
				stmt.setString(3, messagePO.getTarget());
				stmt.setString(4, messagePO.getMessageType());
				stmt.setTimestamp(5, new Timestamp(messagePO.getPostedAt().getTime()));
				stmt.setLong(6, messagePO.getMessageId());
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
	public List<MessagePO> findLatestWallMessages(int limit, int offset) {
		Log.enter("Find latest wall messages (limit: " + limit 
				+ ", offset: " + offset + ")");
		
		List<MessagePO> messages = null;
		try {
			Connection conn = getConnection();
			PreparedStatement stmt = conn.prepareStatement(SQL.FIND_FAKE_LATEST_MESSAGES_OF_TYPE);
			stmt.setString(1, SQL.MESSAGE_TYPE_WALL);
			stmt.setInt(2, limit);
			stmt.setInt(3, offset);
			messages = processResults(stmt);
			conn.close();
		} catch(SQLException e){
			handleException(e);
		} finally {
			Log.exit(messages);
		}
		
		return messages;
	}

	@Override
	public MessagePO findMessageById(long messageId) {
		Log.enter(messageId);
		
		MessagePO po = null;
		try {
			Connection conn = getConnection();
			PreparedStatement stmt = conn.prepareStatement(SQL.FIND_FAKE_MESSAGE_BY_ID);
			stmt.setLong(1, messageId);
			List<MessagePO> messages = processResults(stmt);
			
			if(messages.size()==0){
				Log.debug("No message of id: " + messageId);
			} else {
				po = messages.get(0);
			}
			conn.close();
		} catch(SQLException e){
			handleException(e);
		} finally {
			Log.exit(po);
		}
		
		return po;
	}
	
	private List<MessagePO> processResults(PreparedStatement stmt) {
		Log.enter(stmt);
		if(stmt == null){
			Log.warn("Inside processResults method with NULL statement object");
			return null;
		}
		
		Log.debug("Executing stmt = " + stmt);
		List<MessagePO> messages = new ArrayList<MessagePO>();
		try {
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				MessagePO po = new MessagePO();
				po.setMessageId(rs.getLong(1));
				po.setContent(rs.getString(2));
				po.setAuthor(rs.getString(3));
				po.setTarget(rs.getString(4));
				po.setMessageType(rs.getString(5));
				po.setPostedAt(new Date(rs.getTimestamp(6).getTime()));
				messages.add(po);
			}
			if(rs != null){
				rs.close();
			}
		} catch (SQLException e) {
			handleException(e);
		} finally {
			Log.exit(messages);
		}
		
		return messages;
	}

	@Override
	public List<MessagePO> findChatHistoryBetweenTwoUsers(String userNameOne, String userNameTwo) {
		Log.enter("Find Histroy messages between " + userNameOne 
				+ ", and : " + userNameTwo + ")");
		
		List<MessagePO> messages = null;
		
		return messages;
		
	}

	@Override
	public List<UserPO> findChatBuddies(String userName) {
		Log.enter("find chat buddies for user: "+userName);
		List<UserPO> users = new ArrayList<UserPO>();
		
		return users;
	} 
	
	@Override
	public void truncateMessageTable() {
		Log.enter("enter delete all fake messages records");
		try {
			Connection conn = getConnection();
			PreparedStatement stmt = conn.prepareStatement(SQL.DELETE_FAKE_MESSAGES);
			stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
