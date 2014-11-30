package edu.cmu.sv.ws.ssnoc.data.dao;

import edu.cmu.sv.ws.ssnoc.common.logging.Log;

/**
 * Singleton Factory pattern class to fetch all DAO implementations.
 */
public class DAOFactory {
	private static DAOFactory instance;
	public static boolean fake = false;

	/**
	 * Singleton instance access method to get the instance of the class to
	 * request a specific DAO implementation.
	 * 
	 * @return - DAOFactory instance
	 */
	public static final DAOFactory getInstance() {
		if (instance == null) {
			Log.info("Creating a new DAOFactory singleton instance.");
			instance = new DAOFactory();
		}

		return instance;
	}

	/**
	 * Method to get a new object implementing IUserDAO
	 * 
	 * @return - Object implementing IUserDAO
	 */
	public IUserDAO getUserDAO() {
		return new UserDAOImpl();
	}
	
	/**
	 * Method to get a new object implementing IStatusDAO
	 * 
	 * @return - Object implementing IStatusDAO
	 */
	public IStatusDao getStatusDAO() {
		return new StatusDAOImpl();
	}
	
	/**
	 * Method to get a new object implementing IStatusDAO
	 * 
	 * @return - Object implementing IStatusDAO
	 */
	public IMessageDAO getMessageDAO() {
		if(fake){
			return new MessageDAOFakeImpl();
		}
			
		else {
			return new MessageDAOImpl();

		}
	}
	
	public IMemoryDAO getMemoryDAO() {
		return new MemoryDAOImpl();
	}
	
	public IRequestDAO getRequestDAO() {
		return new RequestDAOImpl();
	}
	
	public IResponderDAO getResponderDAO() {
		return new ResponderDAOImpl();
	}
}
