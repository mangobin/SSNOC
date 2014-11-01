package edu.cmu.sv.ws.ssnoc.data.dao;

import java.util.Date;
import java.util.List;

import edu.cmu.sv.ws.ssnoc.data.po.MessagePO;
import edu.cmu.sv.ws.ssnoc.data.po.UserPO;

public interface IMessageDAO {

	/**
	 * This method will save the information of the message into the database.
	 * 
	 * @param messagePO
	 *            - message information to be saved.
	 */
	long save(MessagePO messagePO);
	
	/**
	 * This method will load all the latest messages from the databases
	 * sorted by time.
	 * 
	 * @param limit - max number of messages to return
	 * @param offset - list offset from most recent messages to return
	 * 
	 * @return - List of messages.
	 */
	List<MessagePO> findLatestWallMessages(int limit, int offset);
	
	/**
	 * This method will search the database for a message of the
	 * given messageId
	 * 
	 * @param messageId
	 * @return - a messagePO or null
	 */
	MessagePO findMessageById(long messageId);
	
	List<MessagePO> findChatHistoryBetweenTwoUsers(long useroneID, long usertwoID);
	
	List<UserPO> findChatBuddies(long userID);
	
	List<MessagePO> findChatMessagesSinceDate(Date date);
	
	void truncateMessageTable();

	List<MessagePO> findAllAnnouncement(int limit, int offset);
	
}
