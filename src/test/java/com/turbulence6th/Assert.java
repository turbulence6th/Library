package com.turbulence6th;

import java.util.Collection;

public class Assert {
	
	public static void assertNull(Object actual) {
		if(actual != null) {
			throw new AssertException("Excepted: NULL, Actual: " + actual);
		}
	}
	
	public static void assertNotNull(Object actual) {
		if(actual == null) {
			throw new AssertException("Excepted: NOT NULL, Actual: " + actual);
		}
	}

	public static void assertEquals(Object expected, Object actual) {
		if(!expected.equals(actual)) {
			throw new AssertException("Excepted: " + expected + ", Actual: " + actual);
		}
	}
	
	public static void assertNotEquals(Object notExpected, Object actual) {
		if(notExpected.equals(actual)) {
			throw new AssertException("Not Excepted: " + notExpected + ", Actual: " + actual);
		}
	}
	
	public static void assertIn(Object expected, Collection<?> collection) {
		if(!collection.contains(expected)) {
			throw new AssertException("Expected: " + expected + ", In: " + collection);
		}
	}
	
	public static void assertNotIn(Object notExpected, Collection<?> collection) {
		if(collection != null && collection.contains(notExpected)) {
			throw new AssertException("Not Expected: " + notExpected + ", In: " + collection);
		}
	}
	
}
