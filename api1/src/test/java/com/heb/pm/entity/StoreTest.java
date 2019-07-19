/*
 * StoreTest
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.entity;


import com.heb.util.ws.ObjectConverter;
import com.heb.xmlns.ei.storelist_1.StoreNumDesc;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigInteger;

/**
 * Tests the Store object.
 *
 * @author d116773
 * @since 2.0.1
 */
public class StoreTest {

	private static final long BIG_STORE_NUMBER = 444L;
	private static final long SMALL_STORE_NUMBER = 116L;
	private static final String BIG_STORE_NAME = "big store";
	private static final String SMALL_STORE_NAME = "small store";

	/*
	 * equals
	 */

	/**
	 * Tests equals with the same object.
	 */
	@Test
	public void equalsSameObject() {
		Store s = this.getBigStore();
		boolean equals = s.equals(s);
		Assert.assertTrue(equals);
	}

	/**
	 * Tests equals with an object with similar values.
	 */
	@Test
	public void equalsSimilarObject() {
		Store s1 = this.getBigStore();
		Store s2 = this.getBigStore();
		boolean equals = s1.equals(s2);
		Assert.assertTrue(equals);
	}

	/**
	 * Test equals with an object with different values.
	 */
	@Test
	public void equalsDifferentObject() {
		Store s1 = this.getBigStore();
		Store s2 = this.getSmallStore();
		boolean equals = s1.equals(s2);
		Assert.assertFalse(equals);
	}

	/**
	 * Tests equals on similar objects where just the name is different.
	 */
	@Test
	public void equalsDifferentName() {
		Store s1 = this.getBigStore();
		Store s2 = this.getBigStore();
		s2.setName("wrong name");
		boolean equals = s1.equals(s2);
		Assert.assertTrue(equals);
	}

	/**
	 * Tests equals when passed a null.
	 */
	@Test
	public void equalsNull() {
		Store s = this.getBigStore();
		boolean equals = s.equals(null);
		Assert.assertFalse(equals);
	}

	/**
	 * Tests equals when passed an object of a different type.
	 */
	@Test
	public void equalsOtherObjectType() {
		Store s = new  Store();
		boolean equals = s.equals(Integer.valueOf(0));
		Assert.assertFalse(equals);
	}


	/*
	 * hashCode
	 */

	/**
	 * Test hashCode is consistent.
	 */
	@Test
	public void hashCodeSameObject() {
		Store s = this.getBigStore();
		Assert.assertEquals(s.hashCode(), s.hashCode());
	}

	/**
	 * Test hashCode on equal objects.
	 */
	@Test
	public void hashCodeSimilarObjects() {
		Store s1 = this.getBigStore();
		Store s2 = this.getBigStore();
		Assert.assertEquals(s1.hashCode(), s2.hashCode());
	}

	/**
	 * Tests hashCode on different objects.
	 */
	@Test
	public void hashCodeDifferentObjects() {
		Store s1 = this.getBigStore();
		Store s2 = this.getSmallStore();
		Assert.assertNotEquals(s1.hashCode(), s2.hashCode());
	}

	/**
	 * Tests hashCode when the name of one object has changed.
	 */
	@Test
	public void hashCodeDifferentName() {
		Store s1 = this.getBigStore();
		Store s2 = this.getBigStore();
		s2.setName("other name");
		Assert.assertEquals(s1.hashCode(), s2.hashCode());
	}

	/*
	 * getters
	 */

	/**
	 * Tests getStoreNumber
	 */
	@Test
	public void getStoreNumber() {
		Assert.assertEquals(StoreTest.BIG_STORE_NUMBER, this.getBigStore().getStoreNumber());
	}

	/**
	 * Tests getStoreName
	 */
	@Test
	public void getStoreName() {
		Assert.assertEquals(StoreTest.BIG_STORE_NAME, this.getBigStore().getName());
	}

	/*
	 * mapping
	 */

	/**
	 * Tests the mapping of store number from the web service result.
	 */
	@Test
	public void storeNumberMapping() {
		ObjectConverter<StoreNumDesc, Store> objectConverter = new ObjectConverter<>(Store::new);
		Store s = objectConverter.convert(this.getTestStoreNumDesc());
		Assert.assertEquals(StoreTest.BIG_STORE_NUMBER, s.getStoreNumber());
	}

	/**
	 * Tests the mapping of store name from the web service result.
	 */
	@Test
	public void storeNameMaping() {
		ObjectConverter<StoreNumDesc, Store> objectConverter = new ObjectConverter<>(Store::new);
		Store s = objectConverter.convert(this.getTestStoreNumDesc());
		Assert.assertEquals(StoreTest.BIG_STORE_NAME, s.getName());
	}

	/*
	 * Support functions
	 */

	/**
	 * Returns a Store with the corporate number set to 444.
	 *
	 * @return A Store with the corporate number set to 444.
	 */
	private Store getBigStore() {

		Store s = new Store();
		s.setStoreNumber(StoreTest.BIG_STORE_NUMBER);
		s.setName(StoreTest.BIG_STORE_NAME);
		return s;
	}

	/**
	 * Returns a Store with the corporate number set to 116.
	 *
	 * @return A Store with the corporate number set to 116.
	 */
	private Store getSmallStore() {

		Store s = new Store();
		s.setStoreNumber(StoreTest.SMALL_STORE_NUMBER);
		s.setName(StoreTest.SMALL_STORE_NAME);
		return s;
	}

	/**
	 * Returns a StoreNumDesc to simulate what comes back from the web service.
	 *
	 * @return A a StoreNumDesc to simulate what comes back from the web service.
	 */
	private StoreNumDesc getTestStoreNumDesc() {

		StoreNumDesc storeNumDesc = new StoreNumDesc();
		storeNumDesc.setLOCNBR(BigInteger.valueOf(StoreTest.BIG_STORE_NUMBER));
		storeNumDesc.setLOCNM(StoreTest.BIG_STORE_NAME);
		return storeNumDesc;
	}
}
