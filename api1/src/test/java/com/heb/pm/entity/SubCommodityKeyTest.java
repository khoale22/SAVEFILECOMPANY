package com.heb.pm.entity;


import org.junit.Assert;
import org.junit.Test;

/**
 * Tests SubCommodityKey.
 *
 * @author d116773
 * @since 2.0.2
 */
public class SubCommodityKeyTest {

	private static final int CLASS_CODE = 34435;
	private static final int COMMODITY_CODE = 23432;
	private static final int SUB_COMMODITY_CODE = 929283;

	/*
	 * equals
	 */

	/**
	 * Tests equals when it is the same object.
	 */
	@Test
	public void equalsSameObject() {
		SubCommodityKey key = this.getKey();
		boolean eq = key.equals(key);
		Assert.assertTrue(eq);
	}

	/**
	 * Tests equals when the objects are equal.
	 */
	@Test
	public void equalsEqualObjects() {
		SubCommodityKey key1 = this.getKey();
		SubCommodityKey key2 = this.getKey();
		boolean eq = key1.equals(key2);
		Assert.assertTrue(eq);
	}

	/**
	 * Tests equals when the objects have a different class.
	 */
	@Test
	public void equalsDifferentClass() {
		SubCommodityKey key1 = this.getKey();
		SubCommodityKey key2 = this.getKey();
		key2.setClassCode(key2.getClassCode() + 1);
		boolean eq = key1.equals(key2);
		Assert.assertFalse(eq);
	}

	/**
	 * Tests equals when the objects have a different commodity.
	 */
	@Test
	public void equalsDifferentCommodity() {
		SubCommodityKey key1 = this.getKey();
		SubCommodityKey key2 = this.getKey();
		key2.setCommodityCode(key2.getCommodityCode() + 1);
		boolean eq = key1.equals(key2);
		Assert.assertFalse(eq);
	}

	/**
	 * Tests equals when the objects have a different sub-commodity.
	 */
	@Test
	public void equalsDifferentSubCommodity() {
		SubCommodityKey key1 = this.getKey();
		SubCommodityKey key2 = this.getKey();
		key2.setSubCommodityCode(key2.getSubCommodityCode() + 1);
		boolean eq = key1.equals(key2);
		Assert.assertFalse(eq);
	}

	/**
	 * Tests equals when passed null.
	 */
	@Test
	public void equalsNull() {
		SubCommodityKey key = this.getKey();
		boolean eq = key.equals(null);
		Assert.assertFalse(eq);
	}

	/**
	 * Tests equals when passed an object of a different type.
	 */
	@Test
	public void equalsDifferentType() {
		SubCommodityKey key = this.getKey();
		boolean eq = key.equals(Integer.valueOf(SubCommodityKeyTest.CLASS_CODE));
		Assert.assertFalse(eq);
	}

	/*
	 * hashCode
	 */
	/**
	 * Tests hashCode when passed the same object.
	 */
	@Test
	public void hashCodeSameObject() {
		SubCommodityKey key = this.getKey();
		Assert.assertEquals(key.hashCode(), key.hashCode());
	}

	/**
	 * Tests hashCode with equal objects.
	 */
	@Test
	public void hashCodeEqualObjects() {
		SubCommodityKey key1 = this.getKey();
		SubCommodityKey key2 = this.getKey();
		Assert.assertEquals(key1.hashCode(), key2.hashCode());
	}

	/**
	 * Tests hashCode with unequal objects.
	 */
	@Test
	public void hashCodeUnequalObjects() {
		SubCommodityKey key1 = this.getKey();
		SubCommodityKey key2 = this.getKey();
		key2.setCommodityCode(key2.getCommodityCode() + 1);
		Assert.assertNotEquals(key1.hashCode(), key2.hashCode());
	}

	/*
	 * getters
	 */

	/**
	 * Tests getClassCode.
	 */
	@Test
	public void getClassCode() {
		SubCommodityKey key = this.getKey();
		Assert.assertEquals(SubCommodityKeyTest.CLASS_CODE, (int) key.getClassCode());
	}

	/**
	 * Tests getCommodityCode.
	 */
	@Test
	public void getCommodityCode() {
		SubCommodityKey key = this.getKey();
		Assert.assertEquals(SubCommodityKeyTest.COMMODITY_CODE, (int) key.getCommodityCode());
	}

	/**
	 * Tests getSubCommodityCode.
	 */
	@Test
	public void getSubCommodityCode() {
		SubCommodityKey key = this.getKey();
		Assert.assertEquals(SubCommodityKeyTest.SUB_COMMODITY_CODE, (int) key.getSubCommodityCode());
	}

	/*
	 * toString
	 */

	/**
	 * Tests toString.
	 */
	@Test
	public void testToString() {
		SubCommodityKey key = this.getKey();
		Assert.assertEquals("SubCommodityKey{classCode=34435, commodityCode=23432, subCommodityCode=929283}",
				key.toString());
	}
	/*
	 * Support functions
	 */

	/**
	 * Returns a SubCommodityKey to test with.
	 *
	 * @return A SubCommodityKey to test with.
	 */
	public SubCommodityKey getKey() {

		SubCommodityKey key = new SubCommodityKey();
		key.setClassCode(SubCommodityKeyTest.CLASS_CODE);
		key.setCommodityCode(SubCommodityKeyTest.COMMODITY_CODE);
		key.setSubCommodityCode(SubCommodityKeyTest.SUB_COMMODITY_CODE);
		return key;
	}
}
