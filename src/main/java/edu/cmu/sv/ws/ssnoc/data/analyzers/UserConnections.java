package edu.cmu.sv.ws.ssnoc.data.analyzers;

import java.util.HashSet;
import java.util.Set;

public class UserConnections {

	String username;
	Set<String> connections = new HashSet<String>();
	
	public UserConnections(String user){
		this.username = user;
	}

	public void addConnection(String user) {
		this.connections.add(user);
	}

	public Set<String> getConnections() {
		return connections;
	}
	
}
