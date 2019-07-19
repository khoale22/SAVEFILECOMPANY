package com.heb.pm.entity;

import org.junit.Assert;
import org.junit.Test;

public class ProductScanImageBannerKeyTest {

	private static final long DEFAULT_LONG = 1L;
	private static final long OTHER_LONG = 2L;
	private static final String DEFAULT_STRING = "TEST";
	private static final String OTHER_STRING = "OTHER";

	/**
	 * Tests the getId method
	 */
	@Test
	public void getIdTest()  {
		Assert.assertEquals(DEFAULT_LONG, getDefaultRecord(DEFAULT_LONG, DEFAULT_STRING).getId());
	}

	/**
	 * Tests the setId method
	 */
	@Test
	public void setIdTest()  {
		ProductScanImageBannerKey key = getDefaultRecord(DEFAULT_LONG, DEFAULT_STRING);
		key.setId(OTHER_LONG);
		Assert.assertEquals(OTHER_LONG, key.getId());
	}

	/**
	 * Tests the getSequenceNumber method
	 */
	@Test
	public void getSequenceNumberTest()  {
		Assert.assertEquals(DEFAULT_STRING, getDefaultRecord(DEFAULT_LONG, DEFAULT_STRING).getSalesChannelCode());
	}

	/**
	 * Tests the setSequenceNumber method
	 */
	@Test
	public void setSequenceNumberTest()  {
		ProductScanImageBannerKey key = getDefaultRecord(DEFAULT_LONG, DEFAULT_STRING);
		key.setSalesChannelCode(OTHER_STRING);
		Assert.assertEquals(OTHER_STRING, key.getSalesChannelCode());
	}

	/**
	 * Tests the getSalesChannelCode method
	 */
	@Test
	public void getSalesChannelCodeTest()  {
		Assert.assertEquals(DEFAULT_LONG, getDefaultRecord(DEFAULT_LONG, DEFAULT_STRING).getSequenceNumber());
	}

	/**
	 * Tests the setSalesChannelCode method
	 */
	@Test
	public void setSalesChannelCodeTest() {
		ProductScanImageBannerKey key = getDefaultRecord(DEFAULT_LONG, DEFAULT_STRING);
		key.setSequenceNumber(OTHER_LONG);
		Assert.assertEquals(OTHER_LONG, key.getSequenceNumber());
	}

	/**
	 * Tests the toString method
	 */
	@Test
	public void toStringTest() {
		Assert.assertEquals("ProductScanImageBannerKey{id='1', sequenceNumber='1', salesChannelCode='TEST'}",
				getDefaultRecord(DEFAULT_LONG, DEFAULT_STRING).toString());
	}

	/**
	 * Tests the equals method of the same object
	 */
	@Test
	public void equalsTest(){
		ProductScanImageBannerKey obj = getDefaultRecord(DEFAULT_LONG, DEFAULT_STRING);
		Assert.assertTrue(obj.equals(getDefaultRecord(DEFAULT_LONG, DEFAULT_STRING)));
	}

	/**
	 * Tests the equal method of two different objects
	 */
	@Test
	public void notEqualsTest(){
		ProductScanImageBannerKey obj = getDefaultRecord(DEFAULT_LONG, DEFAULT_STRING);
		Assert.assertFalse(obj.equals(getDefaultRecord(OTHER_LONG, OTHER_STRING)));
	}

	/**
	 * Tests the hash code of the same object
	 */
	@Test
	public void hashSameCodeTest(){
		ProductScanImageBannerKey obj = getDefaultRecord(DEFAULT_LONG, DEFAULT_STRING);
		Assert.assertEquals(obj.hashCode(), obj.hashCode());
	}

	/**
	 * Tests the hash code of two different objects
	 */
	@Test
	public void hashDifferentCodeTest(){
		ProductScanImageBannerKey obj = getDefaultRecord(DEFAULT_LONG, DEFAULT_STRING);
		ProductScanImageBannerKey objb = getDefaultRecord(OTHER_LONG, OTHER_STRING);
		Assert.assertNotEquals(obj.hashCode(), objb.hashCode());
	}

	/**
	 * Generates default record for testing
	 * @param longs default long value
	 * @param string default string value
	 * @return record for testing
	 */
	public ProductScanImageBannerKey getDefaultRecord(long longs, String string){
		ProductScanImageBannerKey key = new ProductScanImageBannerKey();
		key.setId(longs);
		key.setSalesChannelCode(string);
		key.setSequenceNumber(longs);
		return key;
	}
}