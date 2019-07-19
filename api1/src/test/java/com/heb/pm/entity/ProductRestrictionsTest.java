package com.heb.pm.entity;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Tests the ProductRestriction entity
 * @author s753601
 * @version 2.12.0
 */
public class ProductRestrictionsTest {

	private static final long DEFAULT_LONG = 1L;
	private static final String DEFAULT_STRING = "TEST";
	private static final long OTHER_LONG = 1L;
	private static final String OTHER_STRING = "TEST2";

	/**
	 * Tests the getKey Method
	 */
	@Test
	public void getKeyTest() {
		Assert.assertEquals(getDefaultKey(DEFAULT_LONG, DEFAULT_STRING), getDefaultRecord().getKey());
	}

	/**
	 * Tests the setKey Method
	 */
	@Test
	public void setKeyTest() {
		ProductRestrictionsKey key = getDefaultKey(OTHER_LONG, OTHER_STRING);
		ProductRestrictions restrictions= getDefaultRecord();
		restrictions.setKey(key);
		Assert.assertEquals(key, restrictions.getKey());
	}

	/**
	 * Tests the getRestriction Method
	 */
	@Test
	public void getRestrictionTest() {
		Assert.assertEquals(new SellingRestrictionCode(), getDefaultRecord().getRestriction());
	}

	/**
	 * Tests the setRestrictionCode Method
	 */
	@Test
	public void setRestrictionTest() {
		SellingRestrictionCode code = new SellingRestrictionCode();
		code.setRestrictionCode("25");
		ProductRestrictions restrictions = getDefaultRecord();
		restrictions.setRestriction(code);
		Assert.assertEquals(code, restrictions.getRestriction());
	}

	/*
	 * equals
	 */

	/**
	 * Tests that an object equals itself.
	 */
	@Test
	public void equalsSameObject() {
		ProductRestrictions item = this.getDefaultRecord();
		boolean equals = item.equals(item);
		Assert.assertTrue(equals);
	}

	/**
	 * Tests that an object equals an object with the same values.
	 */
	@Test
	public void equalsSimilarObject() {
		ProductRestrictions i1 = this.getDefaultRecord();
		ProductRestrictions i2 = this.getDefaultRecord();
		boolean equals = i1.equals(i2);
		Assert.assertTrue(equals);
	}

	/**
	 * Tests that it does not equal when passed null.
	 */
	@Test
	public void equalsNull() {
		ProductRestrictions item = this.getDefaultRecord();
		boolean equals = item.equals(null);
		Assert.assertFalse(equals);
	}

	/**
	 * Test that it does not equal an object of a different type.
	 */
	@Test
	public void equalsDifferentType() {
		ProductRestrictions item = this.getDefaultRecord();
		boolean equals = item.equals(Integer.valueOf(0));
		Assert.assertFalse(equals);
	}

	/**
	 * Test that it does not equal an object with a null key.
	 */
	@Test
	public void equalsNullKeyOther() {
		ProductRestrictions item = this.getDefaultRecord();
		ProductRestrictions i2 = new ProductRestrictions();
		boolean equals = item.equals(i2);
		Assert.assertFalse(equals);
	}

	/**
	 * Test that it does not equal an object when it's key is null.
	 */
	@Test
	public void equalsNullKeySelf() {
		ProductRestrictions item = new ProductRestrictions();
		ProductRestrictions i1 = this.getDefaultRecord();
		boolean equals = item.equals(i1);
		Assert.assertFalse(equals);
	}

	/**
	 * Test that it does not equal an object when both keys are null
	 */
	@Test
	public void equalsNullKeyBoth() {
		ProductRestrictions item = new ProductRestrictions();
		ProductRestrictions i1 = new ProductRestrictions();
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
		ProductRestrictions item = this.getDefaultRecord();
		Assert.assertEquals(item.hashCode(), item.hashCode());
	}

	/**
	 * Check hashCode is the same on similar objects.
	 */
	@Test
	public void hashCodeSimilarObjects() {
		ProductRestrictions i1 = this.getDefaultRecord();
		ProductRestrictions i2 = this.getDefaultRecord();
		Assert.assertEquals(i1.hashCode(), i2.hashCode());
	}

	@Test
	public void hashCodeDifferentObjects() {
		ProductRestrictions i1 = this.getDefaultRecord();
	}

	/**
	 * Check hashCode is zero with a null key.
	 */
	@Test
	public void hashCodeNullKey() {
		ProductRestrictions item = new ProductRestrictions();
		Assert.assertEquals(0, item.hashCode());
	}

	/**
	 * Tests toString.
	 */
	@Test
	public void testToString() {
		ProductRestrictions item = this.getDefaultRecord();
		Assert.assertEquals("ProductRestrictions={" +
						"key=ProductRestrictionsKey{" +
							"prodId='1', " +
							"resctrictionCode=TEST" +
							"}, " +
						"restriction=SellingRestrictionCode{" +
							"restrictionCode='null', " +
							"restrictionAbbreviation='null', " +
							"restrictionDescription='null', " +
							"restrictionGroupCode='null', " +
							"dateTimeRestriction=null, " +
							"minimumRestrictedAge=null, " +
							"restrictedQuantity=null" +
							"}" +
						"}",
				item.toString());
	}

	/**
	 * Returns a default record for testing
	 * @return
	 */
	private ProductRestrictions getDefaultRecord(){
		ProductRestrictions productRestrictions= new ProductRestrictions();
		productRestrictions.setRestriction(new SellingRestrictionCode());
		productRestrictions.setKey(getDefaultKey(DEFAULT_LONG, DEFAULT_STRING));
		return productRestrictions;
	}

	/**
	 * Returns a default key for the record.
	 * @param longs the product id
	 * @param string the sellingRestriction code
	 * @return
	 */
	private ProductRestrictionsKey getDefaultKey(long longs, String string){
		ProductRestrictionsKey key = new ProductRestrictionsKey();
		key.setProdId(longs);
		key.setRestrictionCode(string);
		return key;
	}
}