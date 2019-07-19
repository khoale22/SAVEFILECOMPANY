/*
 *
 *  DiscontinueExceptionParametersAuditKeyTest
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
 * Tests DiscontinueExceptionParametersAuditKey.
 *
 * @author s573181
 * @since 2.0.3
 */
public class DiscontinueExceptionParametersAuditKeyTest {

	private static final int ID = 5;
	private static final int SEQUENCE_NUMBER = 1;
	private static final int EXCEPTION_NUMBER = 1;
	private static final LocalDateTime TIMESTAMP = LocalDateTime.parse("2012-05-15 09:47:19.888000", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS"));


	/*
	 * equals
	 */

	/**
	 * Tests equals when passed the same object.
	 */
	@Test
	public void testEqualsSameObject() {
		DiscontinueExceptionParametersAuditKey key = this.getDefaultKey();
		boolean equal = key.equals(key);
		Assert.assertTrue("same object not equal", equal);
	}

	/**
	 * Tests equals when passed a different object with the same values.
	 */
	@Test
	public void testEqualsSimilarObject() {
		DiscontinueExceptionParametersAuditKey key1 = this.getDefaultKey();
		DiscontinueExceptionParametersAuditKey key2 = this.getDefaultKey();
		boolean equal = key1.equals(key2);
		Assert.assertTrue(equal);
	}

	/**
	 * Tests equals when passed a null.
	 */
	@Test
	public void testEqualsNull() {
		DiscontinueExceptionParametersAuditKey key = this.getDefaultKey();
		boolean equal = key.equals(null);
		Assert.assertFalse(equal);
	}

	/**
	 * Tests equals when passed an object with a different ExceptionSequenceNumber but same id, sequenceNumber  and timestamp..
	 */
	@Test
	public void testEqualsOtherExceptionSequenceNumber() {
		DiscontinueExceptionParametersAuditKey key1 = this.getDefaultKey();
		DiscontinueExceptionParametersAuditKey key2 = this.getAlternateExceptionSequenceNumber();
		boolean equals = key1.equals(key2);
		Assert.assertFalse(equals);
	}

	/**
	 * Tests equals when passed an object with a different sequenceNumber but same id, exceptionSequenceNumber and timestamp.
	 */
	@Test
	public void testEqualsOtherSequenceNumber() {
		DiscontinueExceptionParametersAuditKey key1 = this.getDefaultKey();
		DiscontinueExceptionParametersAuditKey key2 = this.getAlternateSequenceNumber();
		boolean equals = key1.equals(key2);
		Assert.assertFalse(equals);
	}

	/**
	 * Tests equals when passed an object with a different id but same sequenceNumber  exceptionSequenceNumber, and timestamp.
	 */
	@Test
	public void testEqualsOtherId() {
		DiscontinueExceptionParametersAuditKey key1 = this.getDefaultKey();
		DiscontinueExceptionParametersAuditKey key2 = this.getAlternateId();
		boolean equals = key1.equals(key2);
		Assert.assertFalse(equals);
	}
	/**
	 * Tests equals when passed an object with a different timestamp but same sequenceNumber, id. and exceptionSequenceNumber.
	 */
	@Test
	public void testEqualsOtherTimestamp() {
		DiscontinueExceptionParametersAuditKey key1 = this.getDefaultKey();
		DiscontinueExceptionParametersAuditKey key2 = this.getAlternateTimestamp();
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
		DiscontinueExceptionParametersAuditKey key = this.getDefaultKey();
		Assert.assertEquals(key.hashCode(), key.hashCode());
	}


	/**
	 * Tests hashCode returns the same value for different objects with the same values.
	 */
	@Test
	public void testHashCodeSimilarObject() {
		DiscontinueExceptionParametersAuditKey key1 = this.getDefaultKey();
		DiscontinueExceptionParametersAuditKey key2 = this.getDefaultKey();
		Assert.assertEquals(key1.hashCode(), key2.hashCode());
	}

	/**
	 * Tests hashCode returns different values for objects with the same type but different SystemGeneratedIds.
	 */
	@Test
	public void testHashCodeDifferentSystemGeneratedId() {
		DiscontinueExceptionParametersAuditKey key1 = this.getDefaultKey();
		DiscontinueExceptionParametersAuditKey key2 = this.getAlternateId();
		Assert.assertNotEquals(key1.hashCode(), key2.hashCode());
	}

	/**
	 * Tests hashCode returns different values for objects with the same type but different SequenceNumber.
	 */
	@Test
	public void testHashCodeDifferentSequenceNumber() {
		DiscontinueExceptionParametersAuditKey key1 = this.getDefaultKey();
		DiscontinueExceptionParametersAuditKey key2 = this.getAlternateSequenceNumber();
		Assert.assertNotEquals(key1.hashCode(), key2.hashCode());
	}

	/**
	 * Tests hashCode returns different values for objects with the same type but different ExceptionSequenceNumber.
	 */
	@Test
	public void testHashCodeDifferentExceptionSequenceNumber() {
		DiscontinueExceptionParametersAuditKey key1 = this.getDefaultKey();
		DiscontinueExceptionParametersAuditKey key2 = this.getAlternateExceptionSequenceNumber();
		Assert.assertNotEquals(key1.hashCode(), key2.hashCode());
	}

	/**
	 * Tests hashCode returns different values for objects with the same type but different timestamp.
	 */
	@Test
	public void testHashCodeDifferentTimestamp() {
		DiscontinueExceptionParametersAuditKey key1 = this.getDefaultKey();
		DiscontinueExceptionParametersAuditKey key2 = this.getAlternateTimestamp();
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
		DiscontinueExceptionParametersAuditKey key = this.getDefaultKey();
		Assert.assertEquals(key.getId(), ID);
	}

	/**
	 *  Tests getSequenceNumber.
	 */
	@Test
	public void getSequenceNumber(){
		DiscontinueExceptionParametersAuditKey key = this.getDefaultKey();
		Assert.assertEquals(key.getSequenceNumber(), SEQUENCE_NUMBER);
	}

	/**
	 *  Tests getExceptionNumber.
	 */
	@Test
	public void getExceptionNumber(){
		DiscontinueExceptionParametersAuditKey key = this.getDefaultKey();
		Assert.assertEquals(key.getExceptionNumber(), EXCEPTION_NUMBER);
	}

	/**
	 *  Tests getTimestamp.
	 */
	@Test
	public void getTimestamp(){
		DiscontinueExceptionParametersAuditKey key = this.getDefaultKey();
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
		DiscontinueExceptionParametersAuditKey key = this.getDefaultKey();
		Assert.assertEquals("DiscontinueExceptionParametersAuditKey{parametersAuditKey=DiscontinueParametersAuditKey{timestamp='2012-05-15T09:47:19.888', id=5, sequenceNumber=1}, exceptionNumber=1}", key.toString());
	}
   	/*
	 * Support functions.
	 */

	/**
	 * Returns the default test value.
	 *
	 * @return The default test value.
	 */
	private DiscontinueExceptionParametersAuditKey getDefaultKey(){
		DiscontinueExceptionParametersAuditKey key = new DiscontinueExceptionParametersAuditKey();
		key.setId(ID);
		key.setSequenceNumber(SEQUENCE_NUMBER);
		key.setTimestamp(TIMESTAMP);
		key.setExceptionNumber(EXCEPTION_NUMBER);
		return key;
	}

	/** Returns a default DiscontinueExceptionParametersAuditKey with a different id.
	 *
	 * @return a default DiscontinueExceptionParametersAuditKey with a different id.
	 */
	private DiscontinueExceptionParametersAuditKey getAlternateId(){
		DiscontinueExceptionParametersAuditKey key = new DiscontinueExceptionParametersAuditKey();
		key.setId(8);
		key.setSequenceNumber(SEQUENCE_NUMBER);
		key.setExceptionNumber(EXCEPTION_NUMBER);
		key.setTimestamp(TIMESTAMP);
		return key;
	}


	/** Returns a default DiscontinueExceptionParametersAuditKey with a different sequenceNumber.
	 *
	 * @return a default DiscontinueExceptionParametersAuditKey with a different sequenceNumber.
	 */
	private DiscontinueExceptionParametersAuditKey getAlternateSequenceNumber(){
		DiscontinueExceptionParametersAuditKey key = new DiscontinueExceptionParametersAuditKey();
		key.setId(ID);
		key.setSequenceNumber(2);
		key.setExceptionNumber(EXCEPTION_NUMBER);
		key.setTimestamp(TIMESTAMP);
		return key;
	}

	/** Returns a default DiscontinueExceptionParametersAuditKey with a different ExceptionSequenceNumber.
	 *
	 * @return a default DiscontinueExceptionParametersAuditKey with a different ExceptionSequenceNumber.
	 */
	private DiscontinueExceptionParametersAuditKey getAlternateExceptionSequenceNumber(){
		DiscontinueExceptionParametersAuditKey key = new DiscontinueExceptionParametersAuditKey();
		key.setId(ID);
		key.setSequenceNumber(SEQUENCE_NUMBER);
		key.setExceptionNumber(6);
		key.setTimestamp(TIMESTAMP);
		return key;
	}

	/** Returns a default DiscontinueExceptionParametersAuditKey with a different timestamp.
	 *
	 * @return a default DiscontinueExceptionParametersAuditKey with a different timestamp.
	 */
	private DiscontinueExceptionParametersAuditKey getAlternateTimestamp(){
		DiscontinueExceptionParametersAuditKey key = new DiscontinueExceptionParametersAuditKey();
		key.setId(ID);
		key.setSequenceNumber(SEQUENCE_NUMBER);
		key.setExceptionNumber(6);
		key.setTimestamp(LocalDateTime.parse("2016-08-31 09:47:19.588050", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS")));
		return key;
	}
}
