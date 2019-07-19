package com.heb.pm.entity;

import org.junit.Assert;
import org.junit.Test;

/**
 * Tests the ProductScanImageURI entity
 * @author s753601
 * @version 2.9.0
 */
public class ProductScanImageURIKeyTest {

	private static final long DEFAULT_ID = 1L;
	private static final long DEFAULT_SEQUENCE=1L;
	private static final long OTHER_ID = 2L;
	private static final long OTHER_SEQUENCE=2L;
	private static final long LONG_ZERO = 0L;

	/**
	 * Tests the getId method
	 */
	@Test
	public void getIdTest() {
		Assert.assertEquals(DEFAULT_ID, getDefaultKey().getId());
	}

	/**
	 * Tests the setId method
	 */
	@Test
	public void setIdTest() {
		ProductScanImageURIKey key = getDefaultKey();
		key.setId(OTHER_ID);
		Assert.assertEquals(OTHER_ID, key.getId());
	}

	/**
	 * Tests the getSequenceNumber method
	 */
	@Test
	public void getSequenceNumberTest() {
		Assert.assertEquals(DEFAULT_SEQUENCE, getDefaultKey().getId());
	}

	/**
	 * Tests the setSequenceNumber method
	 */
	@Test
	public void setSequenceNumberTest() {
		ProductScanImageURIKey key = getDefaultKey();
		key.setSequenceNumber(OTHER_SEQUENCE);
		Assert.assertEquals(OTHER_SEQUENCE, key.getSequenceNumber());
	}

	/**
	 * Test equals comparing the same object.
	 */
	@Test
	public void equalsSameObject() {
		ProductScanImageURIKey key = this.getDefaultKey();
		boolean equals = key.equals(key);
		Assert.assertTrue(equals);
	}

	/**
	 * Test equals comparing an object with the same values.
	 */
	@Test
	public void equalsSimilarObjects() {
		ProductScanImageURIKey key1 = this.getDefaultKey();
		ProductScanImageURIKey key2 = this.getDefaultKey();
		boolean equals = key1.equals(key2);
		System.out.println();
		Assert.assertTrue(equals);
	}

	/**
	 * Test equals comparing against null.
	 */
	@Test
	public void equalsNull() {
		ProductScanImageURIKey key = this.getDefaultKey();
		boolean equals = key.equals(null);
		Assert.assertFalse(equals);
	}

	/**
	 * Test equals when the item type of the left object is null and the other is not.
	 */
	@Test
	public void equalsNullItemType() {
		ProductScanImageURIKey key1 = this.getDefaultKey();
		ProductScanImageURIKey key2 = this.getDefaultKey();
		key1.setId(LONG_ZERO);
		boolean equals = key1.equals(key2);
		Assert.assertFalse(equals);
	}

	/**
	 * Test equals when the item type of both objects is null.
	 */
	@Test
	public void equalsNullIdBoth() {
		ProductScanImageURIKey key1 = this.getDefaultKey();
		key1.setId(LONG_ZERO);
		ProductScanImageURIKey key2 = this.getDefaultKey();
		key2.setId(LONG_ZERO);
		boolean equals = key1.equals(key2);
		Assert.assertTrue(equals);
	}

	/**
	 * Test equals when the warehouse type of the left object is null and the other is not.
	 */
	@Test
	public void equalsNullSequenceNumber() {
		ProductScanImageURIKey key1 = this.getDefaultKey();
		ProductScanImageURIKey key2 = this.getDefaultKey();
		key1.setSequenceNumber(LONG_ZERO);
		boolean equals = key1.equals(key2);
		Assert.assertFalse(equals);
	}

	/**
	 * Test equals when the warehouse type of both objects is null.
	 */
	@Test
	public void equalsNullSequenceNumberBoth() {
		ProductScanImageURIKey key1 = this.getDefaultKey();
		key1.setSequenceNumber(LONG_ZERO);
		ProductScanImageURIKey key2 = this.getDefaultKey();
		key2.setSequenceNumber(LONG_ZERO);
		boolean equals = key1.equals(key2);
		Assert.assertTrue(equals);
	}

	/**
	 * Tests to make sure hashCode returns a consistent value.
	 */
	@Test
	public void hashCodeSameObject() {
		ProductScanImageURIKey key = this.getDefaultKey();
		Assert.assertEquals(key.hashCode(), key.hashCode());
	}

	/**
	 * Tests to make sure two objects with the same values return the same hash code.
	 */
	@Test
	public void hashCodeSimilarObjects() {
		ProductScanImageURIKey key1 = this.getDefaultKey();
		ProductScanImageURIKey key2 = this.getDefaultKey();
		Assert.assertEquals(key1.hashCode(), key2.hashCode());
	}

	/**
	 * Test hashCode on objects with different warehouses return different values.
	 */
	@Test
	public void hashCodeDifferentId() {
		ProductScanImageURIKey key1 = this.getDefaultKey();
		ProductScanImageURIKey key2 = this.getDefaultKey();
		key1.setId(2);
		Assert.assertNotEquals(key1.hashCode(), key2.hashCode());
	}

	/**
	 * Test hashCode on objects with different warehouse types return different values.
	 */
	@Test
	public void hashCodeDifferentSequenceNumber() {
		ProductScanImageURIKey key1 = this.getDefaultKey();
		ProductScanImageURIKey key2 = this.getDefaultKey();
		key1.setSequenceNumber(2);
		Assert.assertNotEquals(key1.hashCode(), key2.hashCode());
	}

	/**
	 * Test hashCode when the item type of the left object is null and the other is not return different values.
	 */
	@Test
	public void hashCodeNullID() {
		ProductScanImageURIKey key1 = this.getDefaultKey();
		ProductScanImageURIKey key2 = this.getDefaultKey();
		key1.setId(LONG_ZERO);
		Assert.assertNotEquals(key1.hashCode(), key2.hashCode());
	}

	/**
	 * Test hashCode when the warehouse type of the left object is null and the other is not return different values.
	 */
	@Test
	public void hashCodeNullSequenceNumber() {
		ProductScanImageURIKey key1 = this.getDefaultKey();
		ProductScanImageURIKey key2 = this.getDefaultKey();
		key1.setSequenceNumber(LONG_ZERO);
		Assert.assertNotEquals(key1.hashCode(), key2.hashCode());
	}

	/**
	 * Tests toString.
	 */
	@Test
	public void testToString() {
		ProductScanImageURIKey key = this.getDefaultKey();
		Assert.assertEquals("ProductScanImageURIKey{id=1, sequenceNumber=1}", key.toString());
	}

	private ProductScanImageURIKey getDefaultKey(){
		ProductScanImageURIKey key = new ProductScanImageURIKey();
		key.setId(DEFAULT_ID);
		key.setSequenceNumber(DEFAULT_SEQUENCE);
		return key;
	}
}