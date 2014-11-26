package edu.cmu.sv.ws.ssnoc.data.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import edu.cmu.sv.ws.ssnoc.common.logging.Log;
import edu.cmu.sv.ws.ssnoc.data.SQL;
import edu.cmu.sv.ws.ssnoc.data.po.UserPO;

/**
 * DAO implementation for saving User information in the H2 database.
 * 
 */
public class UserDAOImpl extends BaseDAOImpl implements IUserDAO {
	/**
	 * This method will load users from the DB with specified account status. If
	 * no status information (null) is provided, it will load all users.
	 * 
	 * @return - List of users
	 */
	public List<UserPO> loadUsers() {
		Log.enter();

		String query = SQL.FIND_ALL_USERS;

		List<UserPO> users = new ArrayList<UserPO>();
		try (Connection conn = getConnection();
				PreparedStatement stmt = conn.prepareStatement(query);) {
			users = processResults(stmt);
			conn.close();
		} catch (SQLException e) {
			handleException(e);
			Log.exit(users);
		}

		return users;
	}

	private List<UserPO> processResults(PreparedStatement stmt) {
		Log.enter(stmt);

		if (stmt == null) {
			Log.warn("Inside processResults method with NULL statement object.");
			return null;
		}

		Log.debug("Executing stmt = " + stmt);
		List<UserPO> users = new ArrayList<UserPO>();
		try (ResultSet rs = stmt.executeQuery()) {
			while (rs.next()) {
				UserPO po = new UserPO();
				po = new UserPO();
				po.setUserId(rs.getLong(1));
				po.setUserName(rs.getString(2));
				po.setPassword(rs.getString(3));
				po.setSalt(rs.getString(4));
				po.setCreatedAt(new Date(rs.getTimestamp(5).getTime()));
				po.setModifiedAt(new Date(rs.getTimestamp(6).getTime()));
				po.setLastStatusID(rs.getLong(7));
				po.setPrivilegeLevel(rs.getString(8));
				po.setAccountStatus(rs.getString(9));
				po.setLatitude(rs.getString(10));
				po.setLongitude(rs.getString(11));
				if(rs.getTimestamp(12) != null) {
					po.setLocation_updatedAt(new Date(rs.getTimestamp(12).getTime()));
				}
				users.add(po);
			}
		} catch (SQLException e) {
			handleException(e);
		} finally {
			Log.exit(users);
		}

		return users;
	}

	/**
	 * This method with search for a user by his userName in the database. The
	 * search performed is a case insensitive search to allow case mismatch
	 * situations.
	 * 
	 * @param userName
	 *            - User name to search for.
	 * 
	 * @return - UserPO with the user information if a match is found.
	 */
	@Override
	public UserPO findByName(String userName) {
		Log.enter(userName);

		if (userName == null) {
			Log.warn("Inside findByName method with NULL userName.");
			return null;
		}

		UserPO po = null;
		try (Connection conn = getConnection();
				PreparedStatement stmt = conn
						.prepareStatement(SQL.FIND_USER_BY_NAME)) {
			stmt.setString(1, userName.toUpperCase());

			List<UserPO> users = processResults(stmt);

			if (users.size() == 0) {
				Log.debug("No user account exists with userName = " + userName);
			} else {
				po = users.get(0);
			}
			conn.close();
		} catch (SQLException e) {
			handleException(e);
			Log.exit(po);
		}

		return po;
	}

	/**
	 * This method will save the information of the user into the database.
	 * 
	 * @param userPO
	 *            - User information to be saved.
	 */
	@Override
	public long save(UserPO userPO) {
		Log.enter(userPO);
		if (userPO == null) {
			Log.warn("Inside save method with userPO == NULL");
			return -1;
		}
		
		long insertedId=-1;

		try {
			Connection conn = getConnection();
			PreparedStatement stmt;
			// this isn't a good approach but it saves time
			// if user doesn't exist, insert into DB, else update
			if(findByUserID(userPO.getUserId()) == null){
				stmt = conn.prepareStatement(SQL.INSERT_USER);
				stmt.setString(1, userPO.getUserName());
				stmt.setString(2, userPO.getPassword());
				stmt.setString(3, userPO.getSalt());
				stmt.setTimestamp(4, new Timestamp(userPO.getCreatedAt().getTime()));
				stmt.setTimestamp(5, new Timestamp(new Date().getTime()));
				stmt.setLong(6, 0); // by default no status
				stmt.setString(7, userPO.getPrivilegeLevel());
				stmt.setString(8, userPO.getAccountStatus());
				stmt.setString(9, userPO.getLatitude());
				stmt.setString(10, userPO.getLongitude());
				if(userPO.getLocation_updatedAt() != null) {
					stmt.setTimestamp(11, new Timestamp(userPO.getLocation_updatedAt().getTime()));
				} else {
					stmt.setTimestamp(11, null);
				}
				
			} else {
				stmt = conn.prepareStatement(SQL.UPDATE_USER);
				stmt.setString(1, userPO.getUserName());
				stmt.setString(2, userPO.getPassword());
				stmt.setString(3, userPO.getSalt());
				stmt.setTimestamp(4, new Timestamp(userPO.getCreatedAt().getTime()));
				stmt.setTimestamp(5, new Timestamp(new Date().getTime()));
				stmt.setLong(6, userPO.getLastStatusID());
				stmt.setString(7, userPO.getPrivilegeLevel());
				stmt.setString(8, userPO.getAccountStatus());
				stmt.setString(9, userPO.getLatitude());
				stmt.setString(10, userPO.getLongitude());
				if(userPO.getLocation_updatedAt() != null) {
					stmt.setTimestamp(11, new Timestamp(userPO.getLocation_updatedAt().getTime()));
				} else {
					stmt.setTimestamp(11, null);
				}
				stmt.setLong(12, userPO.getUserId());
			}
			int rowCount = stmt.executeUpdate();
			Log.trace("Statement executed, and " + rowCount + " rows inserted.");
			ResultSet generatedKeys = stmt.getGeneratedKeys();
			if(generatedKeys.next()){
				insertedId = generatedKeys.getLong(1);
			}
			conn.close();
		} catch (SQLException e) {
			handleException(e);
		} finally {
			Log.exit();
		}
		
		return insertedId;
	}

	@Override
	public UserPO findByUserID(long userID) {
		Log.enter(userID);

		if (userID < 0) {
			Log.warn("Inside findByID method with NULL userID.");
			return null;
		}

		UserPO po = null;
		try (Connection conn = getConnection();
				PreparedStatement stmt = conn
						.prepareStatement(SQL.FIND_USER_BY_ID)) {
			stmt.setLong(1, userID);
			
			List<UserPO> users = processResults(stmt);

			if (users.size() == 0) {
				Log.debug("No user account exists with userID = " + userID);
			} else {
				po = users.get(0);
			}
			conn.close();
		} catch (SQLException e) {
			handleException(e);
			Log.exit(po);
		}

		return po;
	}

}
