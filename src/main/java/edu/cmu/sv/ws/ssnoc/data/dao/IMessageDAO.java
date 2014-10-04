package edu.cmu.sv.ws.ssnoc.data.dao;

import java.util.List;

import edu.cmu.sv.ws.ssnoc.data.po.MessagePO;

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
}