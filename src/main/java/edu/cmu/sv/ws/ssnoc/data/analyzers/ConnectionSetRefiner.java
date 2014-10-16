package edu.cmu.sv.ws.ssnoc.data.analyzers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ConnectionSetRefiner {

	Set<Set<String>> results = new HashSet<Set<String>>();
	
	public void addResult(Set<String> result) {
		List<Set<String>> currentResults = new ArrayList<Set<String>>(results);
		boolean wasAdded = false;
		for(int i=0; i < currentResults.size(); i++){
			Set<String> set = currentResults.get(i);
			if(result.containsAll(set) || set.containsAll(result)){
				set.addAll(result);
				currentResults.set(i, set);
				wasAdded = true;
			}
		}
		if(!wasAdded){
			currentResults.add(result);
		}
		results = new HashSet<Set<String>>(currentResults);
	}

	public Set<Set<String>> getResults() {
		return results;
	}

}
