package edu.cmu.sv.ws.ssnoc.data.analyzers;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ConnectionProbabilityChecker {

	public Set<Set<String>> processSets(List<Set<String>> sets) {
		Set<Set<String>> results = new HashSet<Set<String>>();
		for(int i=0; i < sets.size(); i++){
			Set<String> set1 = sets.get(i);
			for(int j=i+1; j < sets.size(); j++){
				Set<String> intersection = new HashSet<String>(set1);
				Set<String> set2 = sets.get(j);
				intersection.retainAll(set2);
				results.add(intersection);
			}
		}
		return results;
	}

}
