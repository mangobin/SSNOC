package edu.cmu.sv.ws.ssnoc.data.analyzers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserConnectionBuilder {
	
	Map<String, UserConnections> connections = new HashMap<String, UserConnections>();

	public void addUser(String username) {
		// TODO Auto-generated method stub
		UserConnections user = new UserConnections(username);
		connections.put(username, user);
	}

	public List<UserConnections> getUserConnections() {
		List<UserConnections> list = new ArrayList<UserConnections>();
		for(UserConnections conn : connections.values()){
			list.add(conn);
		}
		return list;
	}

	public void addConnection(String author, String target) {
		UserConnections conn = connections.get(author);
		if(conn != null){
			conn.addConnection(target);
		}
		conn = connections.get(target);
		if(conn != null){
			conn.addConnection(author);
		}
	}
}
