/*
 *
 *  DiscontinueParametersAuditKeyTest
 *
 *   Copyright (c) 2016 HEB
 *   All rights reserved.
 *
 *   This software is the confidential and proprietary information
 *    of HEB.
 *
 *
 */

package com.heb.pm.entity;

import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Tests DiscontinueParametersAuditKey.
 *
 * @author s573181
 * @since 2.0.3
 */
public class DiscontinueParametersAuditKeyTest {

	private static final int ID = 5;
	private static final int SEQUENCE_NUMBER = 1;
	private static final LocalDateTime TIMESTAMP = LocalDateTime.parse("2012-05-15 09:47:19.888000", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS"));

	/*
	 * equals
	 */

	/**
	 * Tests equals when passed the same object.
	 */
	@Test
	public void testEqualsSameObject() {
		DiscontinueParametersAuditKey key = this.getDefaultKey();
		boolean equal = key.equals(key);
		Assert.assertTrue("same object not equal", equal);
	}

	/**
	 * Tests equals when passed a different object with the same values.
	 */
	@Test
	public void testEqualsSimilarObject() {
		DiscontinueParametersAuditKey key1 = this.getDefaultKey();
		DiscontinueParametersAuditKey key2 = this.getDefaultKey();
		boolean equal = key1.equals(key2);
		Assert.assertTrue(equal);
	}

	/**
	 * Tests equals when passed a null.
	 */
	@Test
	public void testEqualsNull() {
		DiscontinueParametersAuditKey key = this.getDefaultKey();
		boolean equal = key.equals(null);
		Assert.assertFalse(equal);
	}

	/**
	 * Tests equals when passed an object with a different sequenceNumber but same id, exceptionSequenceNumber and timestamp.
	 */
	@Test
	public void testEqualsOtherSequenceNumber() {
		DiscontinueParametersAuditKey key1 = this.getDefaultKey();
		DiscontinueParametersAuditKey key2 = this.getAlternateSequenceNumber();
		boolean equals = key1.equals(key2);
		Assert.assertFalse(equals);
	}

	/**
	 * Tests equals when passed an object with a different id but same sequenceNumber  exceptionSequenceNumber, and timestamp.
	 */
	@Test
	public void testEqualsOtherId() {
		DiscontinueParametersAuditKey key1 = this.getDefaultKey();
		DiscontinueParametersAuditKey key2 = this.getAlternateId();
		boolean equals = key1.equals(key2);
		Assert.assertFalse(equals);
	}
	/**
	 * Tests equals when passed an object with a different timestamp but same sequenceNumber, id. and exceptionSequenceNumber.
	 */
	@Test
	public void testEqualsOtherTimestamp() {
		DiscontinueParametersAuditKey key1 = this.getDefaultKey();
		DiscontinueParametersAuditKey key2 = this.getAlternateTimestamp();
		boolean equals = key1.equals(key2);
		Assert.assertFalse(equals);
	}
		/*
	 * hashCode
	 */

	/**
	 * Tests hashCode returns the same value for the same object.
	 */
	@Test
	public void testHashCodeSameObject() {
		DiscontinueParametersAuditKey key = this.getDefaultKey();
		Assert.assertEquals(key.hashCode(), key.hashCode());
	}


	/**
	 * Tests hashCode returns the same value for different objects with the same values.
	 */
	@Test
	public void testHashCodeSimilarObject() {
		DiscontinueParametersAuditKey key1 = this.getDefaultKey();
		DiscontinueParametersAuditKey key2 = this.getDefaultKey();
		Assert.assertEquals(key1.hashCode(), key2.hashCode());
	}

	/**
	 * Tests hashCode returns different values for objects with the same type but different SystemGeneratedIds.
	 */
	@Test
	public void testHashCodeDifferentSystemGeneratedId() {
		DiscontinueParametersAuditKey key1 = this.getDefaultKey();
		DiscontinueParametersAuditKey key2 = this.getAlternateId();
		Assert.assertNotEquals(key1.hashCode(), key2.hashCode());
	}

	/**
	 * Tests hashCode returns different values for objects with the same type but different SequenceNumber.
	 */
	@Test
	public void testHashCodeDifferentSequenceNumber() {
		DiscontinueParametersAuditKey key1 = this.getDefaultKey();
		DiscontinueParametersAuditKey key2 = this.getAlternateSequenceNumber();
		Assert.assertNotEquals(key1.hashCode(), key2.hashCode());
	}

	/**
	 * Tests hashCode returns different values for objects with the same type but different timestamp.
	 */
	@Test
	public void testHashCodeDifferentTimestamp() {
		DiscontinueParametersAuditKey key1 = this.getDefaultKey();
		DiscontinueParametersAuditKey key2 = this.getAlternateTimestamp();
		Assert.assertNotEquals(key1.hashCode(), key2.hashCode());
	}

	/*
	 * getters
	 */

	/**
	 *  Tests getId.
	 */
	@Test
	public void getId(){
		DiscontinueParametersAuditKey key = this.getDefaultKey();
		Assert.assertEquals(key.getId(), ID);
	}

	/**
	 *  Tests getSequenceNumber.
	 */
	@Test
	public void getSequenceNumber(){
		DiscontinueParametersAuditKey key = this.getDefaultKey();
		Assert.assertEquals(key.getSequenceNumber(), SEQUENCE_NUMBER);
	}

	/**
	 *  Tests getTimestamp.
	 */
	@Test
	public void getTimestamp(){
		DiscontinueParametersAuditKey key = this.getDefaultKey();
		Assert.assertEquals(key.getTimestamp(), TIMESTAMP);
	}

	/*
	 * toString
	 */

	/**
	 * Tests toString.
	 */
	@Test
	public void testToString() {
		DiscontinueParametersAuditKey key = this.getDefaultKey();
		Assert.assertEquals("DiscontinueParametersAuditKey{timestamp='2012-05-15T09:47:19.888', id=5, sequenceNumber=1}", key.toString());
	}

	/*
	 * Support functions.
	 */

	/**
	 * Returns the default test value.
	 *
	 * @return The default test value.
	 */
	private DiscontinueParametersAuditKey getDefaultKey(){
		DiscontinueParametersAuditKey key = new DiscontinueParametersAuditKey();
		key.setId(ID);
		key.setSequenceNumber(SEQUENCE_NUMBER);
		key.setTimestamp(TIMESTAMP);
		return key;
	}

	/** Returns a default DiscontinueParametersAuditKey with a different id.
	 *
	 * @return a default DiscontinueParametersAuditKey with a different SystemGeneratedId.
	 */
	private DiscontinueParametersAuditKey getAlternateId(){
		DiscontinueParametersAuditKey key = new DiscontinueParametersAuditKey();
		key.setId(8);
		key.setSequenceNumber(SEQUENCE_NUMBER);
		key.setTimestamp(TIMESTAMP);
		return key;
	}


	/** Returns a default DiscontinueParametersAuditKey with a different sequenceNumber.
	 *
	 * @return a default DiscontinueParametersAuditKey with a different sequenceNumber.
	 */
	private DiscontinueParametersAuditKey getAlternateSequenceNumber(){
		DiscontinueParametersAuditKey key = new DiscontinueParametersAuditKey();
		key.setId(ID);
		key.setSequenceNumber(2);
		key.setTimestamp(TIMESTAMP);
		return key;
	}

	/** Returns a default DiscontinueParametersAuditKey with a different timestamp.
	 *
	 * @return a default DiscontinueParametersAuditKey with a different timestamp.
	 */
	private DiscontinueParametersAuditKey getAlternateTimestamp(){
		DiscontinueParametersAuditKey key = new DiscontinueParametersAuditKey();
		key.setId(ID);
		key.setSequenceNumber(SEQUENCE_NUMBER);
		key.setTimestamp(LocalDateTime.parse("2016-08-31 09:47:19.588050",  DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS")));
		return key;
	}
}
