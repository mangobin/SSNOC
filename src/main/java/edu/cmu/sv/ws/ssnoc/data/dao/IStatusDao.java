package edu.cmu.sv.ws.ssnoc.data.dao;

import java.util.List;

import edu.cmu.sv.ws.ssnoc.data.po.StatusPO;

public interface IStatusDao {

	/**
	 * This method will save the information of the status into the database.
	 * 
	 * @param statusPO
	 *            - Status information to be saved.
	 */
	long save(StatusPO statusPO);
	
	/**
	 * This method with search for the statusId in the available statuses
	 * 
	 * @param statusId
	 *            - status id to search for.
	 * 
	 * @return - StatusPO with given statusId
	 */
	StatusPO findStatusById(long statusId);

	/**
	 * This method will load all the latest statuses from the databases
	 * sorted by time.
	 * 
	 * @param limit - max number of statuses to return
	 * @param offset - list offset from most recent status to return
	 * 
	 * @return - List of statuses.
	 */
	List<StatusPO> loadLatestStatuses(int limit, int offset);

	/**
	 * This method with search for the latest status by his userName in the database. The
	 * search performed is a case insensitive search to allow case mismatch
	 * situations.
	 * 
	 * @param userID
	 *            - User id to search for.
	 * 
	 * @return - most recent StatusPO submitted by the user.
	 */
	StatusPO findLatestStatusByUserId(long userId);
	
	/**
	 * This method with search for the latest statuses by his userName in the database. The
	 * search performed is a case insensitive search to allow case mismatch
	 * situations.
	 * 
	 * @param UserId - User id to search for.
	 * @param limit - max number of statuses to return
	 * @param offset - list offset from most recent status to return
	 * 
	 * @return - most recent StatusPOs submitted by the user.
	 */
	List<StatusPO> findLatestStatusesByUserId(long userId, int limit, int offset);
	
}
