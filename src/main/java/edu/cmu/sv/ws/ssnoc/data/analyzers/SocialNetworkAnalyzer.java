package edu.cmu.sv.ws.ssnoc.data.analyzers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.cmu.sv.ws.ssnoc.dto.Message;
import edu.cmu.sv.ws.ssnoc.dto.User;

public class SocialNetworkAnalyzer {

	UserConnectionBuilder connectionBuilder = new UserConnectionBuilder();
	ConnectionSetAnalzer analyzer = new ConnectionSetAnalzer();
	ConnectionProbabilityChecker checker = new ConnectionProbabilityChecker();
	ConnectionSetRefiner refiner = new ConnectionSetRefiner();
	
	public void loadUsers(List<User> users) {
		Set<String> allUsers = new HashSet<String>();
		for(User user : users){
			connectionBuilder.addUser(user.getUserName());
			allUsers.add(user.getUserName());
			
			// pre-seed refiner with all users
			Set<String> set = new HashSet<String>();
			set.add(user.getUserName());
			refiner.addResult(set);
		}
		analyzer.loadUsers(allUsers);
	}
	
	public void loadMessages(List<Message> messages){
		for(Message msg : messages){
			connectionBuilder.addConnection(msg.getAuthor(), msg.getTarget());
		}
	}

	public List<Set<String>> getUnconnectedUsers() {
		List<UserConnections> connections = connectionBuilder.getUserConnections();
		List<Set<String>> possibleSets = new ArrayList<Set<String>>();
		for(UserConnections conn : connections){
			Set<String> possibleSet = analyzer.processConnection(conn);
			possibleSets.add(possibleSet);
		}
		
		System.out.println("possible sets: " + possibleSets);
			
		Set<Set<String>> filteredSets = checker.processSets(possibleSets);
		
		System.out.println("filtered sets: " + filteredSets);
		
		for(Set<String> result : filteredSets){
			refiner.addResult(result);
		}
		
		System.out.println("refined sets: " + refiner.getResults());
				
		return new ArrayList<Set<String>>(refiner.getResults());
	}

}
