package com.heb.pm.entity;

import org.junit.Assert;
import org.junit.Test;

public class ProductScanImageBannerTest {

	private static final String DEFAULT_STRING = "TEST";
	private static final String OTHER_STRING = "OTHER";
	private static final long DEFAULT_LONG = 1L;
	private static final long OTHER_LONG =2L;

	/**
	 * Tests the getKey method
	 */
	@Test
	public void getKeyTest() {
		Assert.assertEquals(getDefaultKey(), getDefaultRecord().getKey());
	}

	/**
	 * Tests the setKey method
	 */
	@Test
	public void setKeyTest() {
		ProductScanImageBannerKey newKey= getDefaultKey();
		newKey.setId(OTHER_LONG);
		newKey.setSequenceNumber(OTHER_LONG);
		ProductScanImageBanner banner = getDefaultRecord();
		banner.setKey(newKey);
		Assert.assertEquals(newKey, banner.getKey());
	}

	/**
	 * Tests the getSalesChannel method
	 */
	@Test
	public void getSalesChannelTest() {
		Assert.assertEquals(getDefaultSalesChannel(), getDefaultRecord().getSalesChannel());
	}

	/**
	 * Tests the setSalesChannel method
	 */
	@Test
	public void setSalesChannelTest() {
		SalesChannel salesChannel = getDefaultSalesChannel();
		salesChannel.setId(OTHER_STRING);
		salesChannel.setDescription(OTHER_STRING);
		salesChannel.setAbbreviation(OTHER_STRING);
		ProductScanImageBanner banner = getDefaultRecord();
		banner.setSalesChannel(salesChannel);
		Assert.assertEquals(salesChannel, banner.getSalesChannel());
	}

	/**
	 * Tests that an object equals itself.
	 */
	@Test
	public void equalsSameObject() {
		ProductScanImageBanner test = this.getDefaultRecord();
		boolean equals = test.equals(test);
		Assert.assertTrue(equals);
	}

	/**
	 * Tests that an object equals an object with the same values.
	 */
	@Test
	public void equalsSimilarObject() {
		ProductScanImageBanner test1 = this.getDefaultRecord();
		ProductScanImageBanner test2 = this.getDefaultRecord();
		boolean equals = test1.equals(test2);
		Assert.assertTrue(equals);
	}

	/**
	 * Tests that it does not equal when passed null.
	 */
	@Test
	public void equalsNull() {
		ProductScanImageBanner test = this.getDefaultRecord();
		boolean equals = test.equals(null);
		Assert.assertFalse(equals);
	}

	/**
	 * Test that it does not equal an object of a different type.
	 */
	@Test
	public void equalsDifferentType() {
		ProductScanImageBanner test = this.getDefaultRecord();
		ProductScanImageBannerKey otherKey = getDefaultKey();
		otherKey.setSequenceNumber(OTHER_LONG);
		test.setKey(otherKey);
		ProductScanImageBanner other = getDefaultRecord();
		boolean equals = test.equals(other);
		Assert.assertFalse(equals);
	}

	/**
	 * Test that it does not equal an object with a null key.
	 */
	@Test
	public void equalsNullKeyOther() {
		ProductScanImageBanner test1 = this.getDefaultRecord();
		ProductScanImageBanner test2 = new ProductScanImageBanner();
		boolean equals = test1.equals(test2);
		Assert.assertFalse(equals);
	}

	/**
	 * Test that it does not equal an object when it's key is null.
	 */
	@Test
	public void equalsNullKeySelf() {
		ProductScanImageBanner test1 = new ProductScanImageBanner();
		ProductScanImageBanner test2 = this.getDefaultRecord();
		boolean equals = test1.equals(test2);
		Assert.assertFalse(equals);
	}

	/**
	 * Test that it does not equal an object when both keys are null
	 */
	@Test
	public void equalsNullKeyBoth() {
		ProductScanImageBanner test1 = new ProductScanImageBanner();
		ProductScanImageBanner test2 = new ProductScanImageBanner();
		boolean equals = test1.equals(test2);
		Assert.assertTrue(equals);
	}

	/**
	 * Check hashCode is consistent on the same object.
	 */
	@Test
	public void hashCodeSelf() {
		ProductScanImageBanner test = this.getDefaultRecord();
		Assert.assertEquals(test.hashCode(), test.hashCode());
	}

	/**
	 * Check hashCode is the same on similar objects.
	 */
	@Test
	public void hashCodeSimilarObjects() {
		ProductScanImageBanner test1 = this.getDefaultRecord();
		ProductScanImageBanner test2 = this.getDefaultRecord();
		Assert.assertEquals(test1.hashCode(), test2.hashCode());
	}

	/**
	 * Checks the hashCode of different objects
	 */
	@Test
	public void hashCodeDifferentObjects() {
		ProductScanImageBanner test1 = this.getDefaultRecord();
		ProductScanImageBanner test2 = this.getDefaultRecord();
		ProductScanImageBannerKey otherKey = getDefaultKey();
		otherKey.setSequenceNumber(OTHER_LONG);
		test2.setKey(otherKey);
		Assert.assertNotEquals(test1.hashCode(), test2.hashCode());
	}

	/**
	 * Check hashCode is zero with a null key.
	 */
	@Test
	public void hashCodeNullKey() {
		ProductScanImageBanner test = new ProductScanImageBanner();
		Assert.assertEquals(0, test.hashCode());
	}

	/**
	 * Tests toString.
	 */
	@Test
	public void testToString() {
		ProductScanImageBanner test = this.getDefaultRecord();
		Assert.assertEquals("ProductScanImageBanner{key=ProductScanImageBannerKey{id='1', sequenceNumber='1', salesChannelCode='TEST'}}",
				test.toString());
	}

	/**
	 * Builds a default record for testing
	 * @return
	 */
	private ProductScanImageBanner getDefaultRecord(){
		ProductScanImageBannerKey key = getDefaultKey();
		ProductScanImageBanner banner = new ProductScanImageBanner();
		banner.setSalesChannel(getDefaultSalesChannel());
		banner.setKey(key);
		return banner;
	}

	/**
	 * Builds a default key for testing
	 * @return
	 */
	private ProductScanImageBannerKey getDefaultKey(){
		ProductScanImageBannerKey key = new ProductScanImageBannerKey();
		key.setSalesChannelCode(DEFAULT_STRING);
		key.setId(DEFAULT_LONG);
		key.setSequenceNumber(DEFAULT_LONG);
		return key;
	}

	/**
	 * Builds a default sales channel for testing
	 * @return
	 */
	private SalesChannel getDefaultSalesChannel(){
		SalesChannel channel = new SalesChannel();
		channel.setAbbreviation(DEFAULT_STRING);
		channel.setDescription(DEFAULT_STRING);
		channel.setId(DEFAULT_STRING);
		return channel;
	}

}