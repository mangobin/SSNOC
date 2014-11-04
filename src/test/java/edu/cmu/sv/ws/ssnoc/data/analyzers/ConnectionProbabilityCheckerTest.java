package edu.cmu.sv.ws.ssnoc.data.analyzers;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class ConnectionProbabilityCheckerTest {

	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
		ConnectionProbabilityChecker sut = new ConnectionProbabilityChecker();
		
		List<Set<String>> test = new ArrayList<Set<String>>();
		Set<String> set1 = new HashSet<String>();
		set1.add("A");
		set1.add("B");
		set1.add("D");
		set1.add("E");
		Set<String> set2 = new HashSet<String>();
		set2.add("A");
		set2.add("B");
		set2.add("C");
		set2.add("E");
		Set<String> set3 = new HashSet<String>();
		set3.add("B");
		set3.add("C");
		Set<String> set4 = new HashSet<String>();
		set4.add("A");
		set4.add("D");
		Set<String> set5 = new HashSet<String>();
		set5.add("A");
		set5.add("B");
		set5.add("E");
		test.add(set1);
		test.add(set2);
		test.add(set3);
		test.add(set4);
		test.add(set5);
		
		Set<Set<String>> results = sut.processSets(test);
		
		Set<String> result1 = new HashSet<String>();
		result1.add("A");
		
		Set<String> result2 = new HashSet<String>();
		result2.add("B");
		
		Set<String> result3 = new HashSet<String>();
		result3.add("A");
		result3.add("B");
		result3.add("E");
		
		Set<String> result4 = new HashSet<String>();
		result4.add("A");
		result4.add("D");

		Set<String> result5 = new HashSet<String>();
		result5.add("B");
		result5.add("C");
		
		Set<String> result6 = new HashSet<String>();
		
		System.out.println("results: " + results);
		
		assertEquals(6, results.size());
		assertTrue(results.contains(result1));
		assertTrue(results.contains(result2));
		assertTrue(results.contains(result3));
		assertTrue(results.contains(result4));
		assertTrue(results.contains(result5));
		assertTrue(results.contains(result6));
	}

}
