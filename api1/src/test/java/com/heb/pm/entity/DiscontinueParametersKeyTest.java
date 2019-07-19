/*
 *
 *  DiscontinueExceptionParametersKeyTest
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
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
 * Created by s573181 on 6/24/2016.
 */
public class DiscontinueParametersKeyTest {

	/*
	 * equals
	 */

	/**
	 * Tests equals when passed the same object.
	 */
	@Test
	public void testEqualsSameObject() {
		DiscontinueParametersKey key = this.getDefaultKey();
		boolean equal = key.equals(key);
		Assert.assertTrue("same object not equal", equal);
	}

	/**
	 * Tests equals when passed a different object with the same values.
	 */
	@Test
	public void testEqualsSimilarObject() {
		DiscontinueParametersKey key1 = this.getDefaultKey();
		DiscontinueParametersKey key2 = this.getDefaultKey();
		boolean equal = key1.equals(key2);
		Assert.assertTrue(equal);
	}

	/**
	 * Tests equals when passed a different object with the same values.
	 */
	@Test
	public void testEqualsDifferentObject() {
		DiscontinueParametersKey key1 = this.getDefaultKey();
		DiscontinueParametersKey key2 = this.getAlternateKey();
		boolean equal = key1.equals(key2);
		Assert.assertFalse(equal);
	}


	/**
	 * Tests equals when passed a null.
	 */
	@Test
	public void testEqualsNull() {
		DiscontinueParametersKey key = this.getDefaultKey();
		boolean equal = key.equals(null);
		Assert.assertFalse(equal);
	}

	/**
	 * Tests equals when passed an object with a different SequenceNumber but same SystemGeneratedId and ExceptionSequenceNumber.
	 */
	@Test
	public void testEqualsOtherSequenceNumber() {
		DiscontinueParametersKey key1 = this.getDefaultKey();
		DiscontinueParametersKey key2 = this.getAlternateSequenceNumber();
		boolean equals = key1.equals(key2);
		Assert.assertFalse(equals);
	}
	/**
	 * Tests equals when passed an object with a different SystemGeneratedId but same SequenceNumber and ExceptionSequenceNumber.
	 */
	@Test
	public void testEqualsOtherSystemGeneratedId() {
		DiscontinueParametersKey key1 = this.getDefaultKey();
		DiscontinueParametersKey key2 = this.getAlternateSystemGeneratedId();
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
		DiscontinueParametersKey key = this.getDefaultKey();
		Assert.assertEquals(key.hashCode(), key.hashCode());
	}

	/**
	 * Tests hashCode returns the same value for different objects with the same values.
	 */
	@Test
	public void testHashCodeSimilarObject() {
		DiscontinueParametersKey key1 = this.getDefaultKey();
		DiscontinueParametersKey key2 = this.getDefaultKey();
		Assert.assertEquals(key1.hashCode(), key2.hashCode());
	}

	/**
	 * Tests hashCode returns different values for completely different DiscontinueExceptionParametersKey.
	 */
	@Test
	public void testHashCodeDifferentObject() {
		DiscontinueParametersKey key1 = this.getDefaultKey();
		DiscontinueParametersKey key2 = this.getAlternateKey();
		Assert.assertNotEquals(key1.hashCode(), key2.hashCode());
	}

	/**
	 * Tests hashCode returns different values for objects with the same type but different SystemGeneratedIds.
	 */
	@Test
	public void testHashCodeDifferentSystemGeneratedId() {
		DiscontinueParametersKey key1 = this.getDefaultKey();
		DiscontinueParametersKey key2 = this.getAlternateSystemGeneratedId();
		Assert.assertNotEquals(key1.hashCode(), key2.hashCode());
	}

	/**
	 * Tests hashCode returns different values for objects with the same type but different SequenceNumber.
	 */
	@Test
	public void testHashCodeDifferentSequenceNumber() {
		DiscontinueParametersKey key1 = this.getDefaultKey();
		DiscontinueParametersKey key2 = this.getAlternateSequenceNumber();
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
		DiscontinueParametersKey key = this.getDefaultKey();
		Assert.assertEquals(key.getId(), 4);
	}

	/**
	 *  Tests getSequenceNumber
	 */
	@Test
	public void testGetSequenceNumber(){
		DiscontinueParametersKey key = this.getDefaultKey();
		Assert.assertEquals(key.getSequenceNumber(), 1);
	}

		/*
	 * toString
	 */

	/**
	 * Tests toString.
	 */
	@Test
	public void testToString() {
		DiscontinueParametersKey key = this.getDefaultKey();
		Assert.assertEquals("DiscontinueExceptionParametersKey{id=4, sequenceNumber=1}", key.toString());
	}

	/*
	 * Support functions.
	 */

	/**
	 * Returns the default test value.
	 *
	 * @return The default test value.
	 */
	private DiscontinueParametersKey getDefaultKey(){
		DiscontinueParametersKey key = new DiscontinueParametersKey();
		key.setId(4);
		key.setSequenceNumber(1);
		return key;
	}

	/** Returns an Alternate DiscontinueExceptionParametersKey.
	 *
	 * @return an Alternate DiscontinueExceptionParametersKey..
	 */
	private DiscontinueParametersKey getAlternateKey(){
		DiscontinueParametersKey key = new DiscontinueParametersKey();
		key.setId(3);
		key.setSequenceNumber(2);
		return key;
	}

	/** Returns a default DiscontinueExceptionParametersKey with a different SystemGeneratedId.
	 *
	 * @return a default DiscontinueExceptionParametersKey with a different SystemGeneratedId.
	 */
	private DiscontinueParametersKey getAlternateSystemGeneratedId(){
		DiscontinueParametersKey key = new DiscontinueParametersKey();
		key.setId(5);
		key.setSequenceNumber(1);
		return key;
	}

	/** Returns a default DiscontinueExceptionParametersKey with a different sequenceNumber.
	 *
	 * @return a default DiscontinueExceptionParametersKey with a different sequenceNumber.
	 */
	private DiscontinueParametersKey getAlternateSequenceNumber(){
		DiscontinueParametersKey key = new DiscontinueParametersKey();
		key.setId(4);
		key.setSequenceNumber(2);
		return key;
	}
}
