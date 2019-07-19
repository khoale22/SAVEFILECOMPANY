/*
 *
 * DiscontinueExceptionRulesKey
 *
 * Copyright (c) 2016 HEB
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of HEB.
 *
 *
 */

package com.heb.pm.entity;

import org.junit.Assert;
import org.junit.Test;

/**
 * Tests DiscontinueExceptionParametersKeyTest.
 *
 * @author s573181
 * @since 2.0.2
 */
public class DiscontinueExceptionParametersKeyTest {

	/*
	 * equals
	 */

	/**
	 * Tests equals when passed the same object.
	 */
	@Test
	public void testEqualsSameObject() {
		DiscontinueExceptionParametersKey key = this.getDefaultKey();
		boolean equal = key.equals(key);
		Assert.assertTrue("same object not equal", equal);
	}

	/**
	 * Tests equals when passed a different object with the same values.
	 */
	@Test
	public void testEqualsSimilarObject() {
		DiscontinueExceptionParametersKey key1 = this.getDefaultKey();
		DiscontinueExceptionParametersKey key2 = this.getDefaultKey();
		boolean equal = key1.equals(key2);
		Assert.assertTrue(equal);
	}

	/**
	 * Tests equals when passed a different object with the same values.
	 */
	@Test
	public void testEqualsDifferentObject() {
		DiscontinueExceptionParametersKey key1 = this.getDefaultKey();
		DiscontinueExceptionParametersKey key2 = this.getAlternateKey();
		boolean equal = key1.equals(key2);
		Assert.assertFalse(equal);
	}

	/**
	 * Tests equals when passed a null.
	 */
	@Test
	public void testEqualsNull() {
		DiscontinueExceptionParametersKey key = this.getDefaultKey();
		boolean equal = key.equals(null);
		Assert.assertFalse(equal);
	}

	/**
	 * Tests equals when passed an object with a different ExceptionSequenceNumber but same SystemGeneratedId and sequenceNumber.
	 */
	@Test
	public void testEqualsOtherExceptionSequenceNumber() {
		DiscontinueExceptionParametersKey key1 = this.getDefaultKey();
		DiscontinueExceptionParametersKey key2 = this.getAlternateExceptionSequenceNumber();
		boolean equals = key1.equals(key2);
		Assert.assertFalse(equals);
	}
	/**
	 * Tests equals when passed an object with a different SequenceNumber but same SystemGeneratedId and ExceptionSequenceNumber.
	 */
	@Test
	public void testEqualsOtherSequenceNumber() {
		DiscontinueExceptionParametersKey key1 = this.getDefaultKey();
		DiscontinueExceptionParametersKey key2 = this.getAlternateSequenceNumber();
		boolean equals = key1.equals(key2);
		Assert.assertFalse(equals);
	}
	/**
	 * Tests equals when passed an object with a different SystemGeneratedId but same SequenceNumber and ExceptionSequenceNumber.
	 */
	@Test
	public void testEqualsOtherSystemGeneratedId() {
		DiscontinueExceptionParametersKey key1 = this.getDefaultKey();
		DiscontinueExceptionParametersKey key2 = this.getAlternateSystemGeneratedId();
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
		DiscontinueExceptionParametersKey key = this.getDefaultKey();
		Assert.assertEquals(key.hashCode(), key.hashCode());
	}


	/**
	 * Tests hashCode returns the same value for different objects with the same values.
	 */
	@Test
	public void testHashCodeSimilarObject() {
		DiscontinueExceptionParametersKey key1 = this.getDefaultKey();
		DiscontinueExceptionParametersKey key2 = this.getDefaultKey();
		Assert.assertEquals(key1.hashCode(), key2.hashCode());
	}

	/**
	 * Tests hashCode returns different values for completely different DiscontinueExceptionRulesKey.
	 */
	@Test
	public void testHashCodeDifferentObject() {
		DiscontinueExceptionParametersKey key1 = this.getDefaultKey();
		DiscontinueExceptionParametersKey key2 = this.getAlternateKey();
		Assert.assertNotEquals(key1.hashCode(), key2.hashCode());
	}

	/**
	 * Tests hashCode returns different values for objects with the same type but different SystemGeneratedIds.
	 */
	@Test
	public void testHashCodeDifferentSystemGeneratedId() {
		DiscontinueExceptionParametersKey key1 = this.getDefaultKey();
		DiscontinueExceptionParametersKey key2 = this.getAlternateSystemGeneratedId();
		Assert.assertNotEquals(key1.hashCode(), key2.hashCode());
	}

	/**
	 * Tests hashCode returns different values for objects with the same type but different SequenceNumber.
	 */
	@Test
	public void testHashCodeDifferentSequenceNumber() {
		DiscontinueExceptionParametersKey key1 = this.getDefaultKey();
		DiscontinueExceptionParametersKey key2 = this.getAlternateSequenceNumber();
		Assert.assertNotEquals(key1.hashCode(), key2.hashCode());
	}

	/**
	 * Tests hashCode returns different values for objects with the same type but different ExceptionSequenceNumber.
	 */
	@Test
	public void testHashCodeDifferentExceptionSequenceNumber() {
		DiscontinueExceptionParametersKey key1 = this.getDefaultKey();
		DiscontinueExceptionParametersKey key2 = this.getAlternateExceptionSequenceNumber();
		Assert.assertNotEquals(key1.hashCode(), key2.hashCode());
	}

	/*
	 * getters
	 */

	/**
	 *  Tests getId
	 */
	@Test
	public void getId(){
		DiscontinueExceptionParametersKey key = this.getDefaultKey();
		Assert.assertEquals(key.getId(), 4);
	}

	/**
	 *  Tests getSequenceNumber
	 */
	@Test
	public void getSequenceNumber(){
		DiscontinueExceptionParametersKey key = this.getDefaultKey();
		Assert.assertEquals(key.getSequenceNumber(), 1);
	}

	/**
	 *  Tests getExceptionNumber
	 */
	@Test
	public void getExceptionNumber(){
		DiscontinueExceptionParametersKey key = this.getDefaultKey();
		Assert.assertEquals(key.getExceptionNumber(), 9);
	}

	/*
	 * toString
	 */

	/**
	 * Tests toString.
	 */
	@Test
	public void testToString() {
		DiscontinueExceptionParametersKey key = this.getDefaultKey();
		Assert.assertEquals("DiscontinueExceptionRulesKey{id=4, sequenceNumber=1, exceptionNumber=9}", key.toString());
	}

	/*
	 * Support functions.
	 */

	/**
	 * Returns the default test value.
	 *
	 * @return The default test value.
	 */
	private DiscontinueExceptionParametersKey getDefaultKey(){
		DiscontinueExceptionParametersKey key = new DiscontinueExceptionParametersKey();
		key.setId(4);
		key.setSequenceNumber(1);
		key.setExceptionNumber(9);
		return key;
	}

	/** Returns an Alternate DiscontinueExceptionRulesKey.
	 *
	 * @return an Alternate DiscontinueExceptionRulesKey..
	 */
	private DiscontinueExceptionParametersKey getAlternateKey(){
		DiscontinueExceptionParametersKey key = new DiscontinueExceptionParametersKey();
		key.setId(3);
		key.setSequenceNumber(2);
		key.setExceptionNumber(8);
		return key;
	}

	/** Returns a default DiscontinueExceptionRulesKey with a different SystemGeneratedId.
	 *
	 * @return a default DiscontinueExceptionRulesKey with a different SystemGeneratedId.
	 */
	private DiscontinueExceptionParametersKey getAlternateSystemGeneratedId(){
		DiscontinueExceptionParametersKey key = new DiscontinueExceptionParametersKey();
		key.setId(5);
		key.setSequenceNumber(1);
		key.setExceptionNumber(9);
		return key;
	}

	/** Returns a default DiscontinueExceptionRulesKey with a different sequenceNumber.
	 *
	 * @return a default DiscontinueExceptionRulesKey with a different sequenceNumber.
	 */
	private DiscontinueExceptionParametersKey getAlternateSequenceNumber(){
		DiscontinueExceptionParametersKey key = new DiscontinueExceptionParametersKey();
		key.setId(4);
		key.setSequenceNumber(2);
		key.setExceptionNumber(9);
		return key;
	}

	/** Returns a default DiscontinueExceptionRulesKey with a different ExceptionSequenceNumber.
	 *
	 * @return a default DiscontinueExceptionRulesKey with a different ExceptionSequenceNumber.
	 */
	private DiscontinueExceptionParametersKey getAlternateExceptionSequenceNumber(){
		DiscontinueExceptionParametersKey key = new DiscontinueExceptionParametersKey();
		key.setId(4);
		key.setSequenceNumber(1);
		key.setExceptionNumber(7);
		return key;
	}


}
