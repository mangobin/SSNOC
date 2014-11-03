package edu.cmu.sv.ws.ssnoc.data.analyzers;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class ConnectionSetRefinerTest {

	ConnectionSetRefiner sut;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		sut = new ConnectionSetRefiner();
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void testThatWeCanAddASet(){
		Set<String> results = new HashSet<String>();
		results.add("A");
		sut.addResult(results);
		assertEquals(sut.getResults().size(), 1);
		assertTrue(sut.getResults().contains(results));
	}
	
	@Test
	public void testThatAddingTheSetTwiceIsOk(){
		Set<String> results = new HashSet<String>();
		results.add("A");
		sut.addResult(results);
		sut.addResult(results);
		assertEquals(sut.getResults().size(), 1);
		assertTrue(sut.getResults().contains(results));
	}
	
	@Test
	public void testThatAddingDifferentSetsIsOk(){
		Set<String> result1 = new HashSet<String>();
		Set<String> result2 = new HashSet<String>();
		result1.add("A");
		result2.add("B");
		sut.addResult(result1);
		sut.addResult(result2);
		assertEquals(sut.getResults().size(), 2);
		assertTrue(sut.getResults().contains(result1));
		assertTrue(sut.getResults().contains(result2));
	}
	
	@Test
	public void testThatAddingSimilarSetsIsOk(){
		Set<String> result1 = new HashSet<String>();
		Set<String> result2 = new HashSet<String>();
		result1.add("A");
		result2.add("A");
		sut.addResult(result1);
		sut.addResult(result2);
		assertEquals(sut.getResults().size(), 1);
		assertTrue(sut.getResults().contains(result1));
		assertTrue(sut.getResults().contains(result2));
	}
	
	@Test
	public void testThatAddingASupersetWillReplaceASubset(){
		Set<String> result1 = new HashSet<String>();
		Set<String> result2 = new HashSet<String>();
		result1.add("A");
		result2.add("A");
		result2.add("B");
		sut.addResult(result1);
		sut.addResult(result2);
		assertEquals(sut.getResults().size(), 1);
		assertTrue(sut.getResults().contains(result2));
	}
	
	@Test
	public void testOurBigTest(){
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
		
		sut.addResult(result1);
		sut.addResult(result2);
		sut.addResult(result3);
		sut.addResult(result4);
		sut.addResult(result5);
		sut.addResult(result6);
		
		System.out.println("results: " + sut.getResults());
		
		assertEquals(3, sut.getResults().size());
		assertTrue(sut.getResults().contains(result3));
		assertTrue(sut.getResults().contains(result4));
		assertTrue(sut.getResults().contains(result5));
	}

}
