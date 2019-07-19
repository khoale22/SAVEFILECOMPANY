package com.heb.pm.entity;

import org.junit.Assert;
import org.junit.Test;

/**
 * Tests the ProductRestrictionsKey entity
 * @author s753601
 * @version 2.12.0
 */
public class ProductRestrictionsKeyTest {

	private static final long DEFAULT_LONG = 1L;
	private static final String DEFAULT_STRING = "TEST";
	private static final long OTHER_LONG = 1L;
	private static final String OTHER_STRING = "TEST2";

	/**
	 * Tests the getProdId method
	 */
	@Test
	public void getProdId() {
		Assert.assertEquals(DEFAULT_LONG, getDefaultRecord(DEFAULT_LONG, DEFAULT_STRING).getProdId());
	}

	/**
	 * Tests the setProdId method
	 */
	@Test
	public void setProdIdTest() {
		ProductRestrictionsKey key = getDefaultRecord(DEFAULT_LONG, DEFAULT_STRING);
		key.setProdId(OTHER_LONG);
		Assert.assertEquals(OTHER_LONG, key.getProdId());
	}

	/**
	 * Tests the getRestrictionCode method
	 */
	@Test
	public void getRestrictionCodeTest() {
		Assert.assertEquals(DEFAULT_STRING, getDefaultRecord(DEFAULT_LONG, DEFAULT_STRING).getRestrictionCode());
	}

	/**
	 * Tests the setRestrictionCode method
	 */
	@Test
	public void setRestrictionCodeTest() {
		ProductRestrictionsKey key = getDefaultRecord(DEFAULT_LONG, DEFAULT_STRING);
		key.setRestrictionCode(OTHER_STRING);
		Assert.assertEquals(OTHER_STRING, key.getRestrictionCode());
	}

	/*
	 * equals
	 */

	/**
	 * Tests that an object equals itself.
	 */
	@Test
	public void equalsSameObject() {
		ProductRestrictionsKey item = this.getDefaultRecord(DEFAULT_LONG, DEFAULT_STRING);
		boolean equals = item.equals(item);
		Assert.assertTrue(equals);
	}

	/**
	 * Tests that an object equals an object with the same values.
	 */
	@Test
	public void equalsSimilarObject() {
		ProductRestrictionsKey i1 = this.getDefaultRecord(DEFAULT_LONG, DEFAULT_STRING);
		ProductRestrictionsKey i2 = this.getDefaultRecord(DEFAULT_LONG, DEFAULT_STRING);
		boolean equals = i1.equals(i2);
		Assert.assertTrue(equals);
	}

	/**
	 * Tests that it does not equal when passed null.
	 */
	@Test
	public void equalsNull() {
		ProductRestrictionsKey item = this.getDefaultRecord(DEFAULT_LONG, DEFAULT_STRING);
		boolean equals = item.equals(null);
		Assert.assertFalse(equals);
	}

	/**
	 * Test that it does not equal an object of a different type.
	 */
	@Test
	public void equalsDifferentType() {
		ProductRestrictionsKey item = this.getDefaultRecord(DEFAULT_LONG, DEFAULT_STRING);
		boolean equals = item.equals(Integer.valueOf(0));
		Assert.assertFalse(equals);
	}

	/**
	 * Test that it does not equal an object with a null key.
	 */
	@Test
	public void equalsNullKeyOther() {
		ProductRestrictionsKey item = this.getDefaultRecord(DEFAULT_LONG, DEFAULT_STRING);
		ProductRestrictionsKey i2 = new ProductRestrictionsKey();
		boolean equals = item.equals(i2);
		Assert.assertFalse(equals);
	}

	/**
	 * Test that it does not equal an object when it's key is null.
	 */
	@Test
	public void equalsNullKeySelf() {
		ProductRestrictionsKey item = new ProductRestrictionsKey();
		ProductRestrictionsKey i1 = this.getDefaultRecord(DEFAULT_LONG, DEFAULT_STRING);
		boolean equals = item.equals(i1);
		Assert.assertFalse(equals);
	}

	/**
	 * Test that it does not equal an object when both keys are null
	 */
	@Test
	public void equalsNullKeyBoth() {
		ProductRestrictionsKey item = new ProductRestrictionsKey();
		ProductRestrictionsKey i1 = new ProductRestrictionsKey();
		boolean equals = item.equals(i1);
		Assert.assertTrue(equals);
	}

	/*
	 * hashCode
	 */

	/**
	 * Check hashCode is consistent on the same object.
	 */
	@Test
	public void hashCodeSelf() {
		ProductRestrictionsKey item = this.getDefaultRecord(DEFAULT_LONG, DEFAULT_STRING);
		Assert.assertEquals(item.hashCode(), item.hashCode());
	}

	/**
	 * Check hashCode is the same on similar objects.
	 */
	@Test
	public void hashCodeSimilarObjects() {
		ProductRestrictionsKey i1 = this.getDefaultRecord(DEFAULT_LONG, DEFAULT_STRING);
		ProductRestrictionsKey i2 = this.getDefaultRecord(DEFAULT_LONG, DEFAULT_STRING);
		Assert.assertEquals(i1.hashCode(), i2.hashCode());
	}

	@Test
	public void hashCodeDifferentObjects() {
		ProductRestrictionsKey i1 = this.getDefaultRecord(DEFAULT_LONG, DEFAULT_STRING);
	}

	/**
	 * Check hashCode is zero with a null key.
	 */
	@Test
	public void hashCodeNullKey() {
		ProductRestrictionsKey item = new ProductRestrictionsKey();
		Assert.assertEquals(0, item.hashCode());
	}

	/**
	 * Tests toString.
	 */
	@Test
	public void testToString() {
		ProductRestrictionsKey item = this.getDefaultRecord(DEFAULT_LONG, DEFAULT_STRING);
		Assert.assertEquals("ProductRestrictionsKey{" +
							"prodId='1', " +
							"resctrictionCode=TEST" +
						"}",
				item.toString());
	}

	/**
	 * Returns a default record for testing
	 * @param longs numeric component to identify the record
	 * @param string the string component to identify the record
	 * @return
	 */
	private ProductRestrictionsKey getDefaultRecord(long longs, String string){
		ProductRestrictionsKey key = new ProductRestrictionsKey();
		key.setProdId(longs);
		key.setRestrictionCode(string);
		return key;
	}
}