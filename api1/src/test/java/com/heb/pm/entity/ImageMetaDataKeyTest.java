package com.heb.pm.entity;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Tests the imageMetaData object
 * @author s753601
 * @version 2.12.0
 */
public class ImageMetaDataKeyTest {

	private static final long DEFAULT_LONG = 1L;
	private static final long OTHER_LONG= 2L;
	private static final String DEFAULT_STRING="TEST";
	private static final String OTHER_STRING = "OTHER";

	/**
	 * Tests the getId method
	 */
	@Test
	public void getIdTest() {
		Assert.assertEquals(DEFAULT_LONG, getDefaultRecord(DEFAULT_LONG, DEFAULT_STRING).getId());
	}

	/**
	 * Tests the setId method
	 */
	@Test
	public void setIdTest() {
		ImageMetaDataKey key = getDefaultRecord(DEFAULT_LONG, DEFAULT_STRING);
		key.setId(OTHER_LONG);
		Assert.assertEquals(OTHER_LONG, key.getId());
	}

	/**
	 * Tests the getSequenceNumber method
	 */
	@Test
	public void getSequenceNumberTest(){
		Assert.assertEquals(DEFAULT_LONG, getDefaultRecord(DEFAULT_LONG, DEFAULT_STRING).getSequenceNumber());
	}

	/**
	 * Tests the setSequenceNumber method
	 */
	@Test
	public void setSequenceNumberTest(){
		ImageMetaDataKey key = getDefaultRecord(DEFAULT_LONG, DEFAULT_STRING);
		key.setSequenceNumber(OTHER_LONG);
		Assert.assertEquals(OTHER_LONG, key.getSequenceNumber());
	}

	/**
	 * Tests the getImageSubjectTypeCode method
	 */
	@Test
	public void getImageSubjectTypeCodeTest(){
		Assert.assertEquals(DEFAULT_STRING, getDefaultRecord(DEFAULT_LONG,DEFAULT_STRING).getImageSubjectTypeCode());
	}

	/**
	 * Tests the setImageSubjectTypeCode method
	 */
	@Test
	public void setImageSubjectTypeCodeTest() {
		ImageMetaDataKey key = getDefaultRecord(DEFAULT_LONG, DEFAULT_STRING);
		key.setImageSubjectTypeCode(OTHER_STRING);
		Assert.assertEquals(OTHER_STRING, key.getImageSubjectTypeCode());
	}

	/**
	 * Tests the toString method
	 */
	@Test
	public void toStringTest() {
		Assert.assertEquals("ImageMetaData{id='1', sequenceNumber='1', imageSubjectTypeCode='TEST'}",
				getDefaultRecord(DEFAULT_LONG, DEFAULT_STRING).toString());
	}

	/**
	 * Tests the equals method of the same object
	 */
	@Test
	public void equalsTest(){
		ImageMetaDataKey obj = getDefaultRecord(DEFAULT_LONG, DEFAULT_STRING);
		Assert.assertTrue(obj.equals(getDefaultRecord(DEFAULT_LONG, DEFAULT_STRING)));
	}

	/**
	 * Tests the equal method of two different objects
	 */
	@Test
	public void notEqualsTest(){
		ImageMetaDataKey obj = getDefaultRecord(DEFAULT_LONG, DEFAULT_STRING);
		Assert.assertFalse(obj.equals(getDefaultRecord(OTHER_LONG, OTHER_STRING)));
	}

	/**
	 * Tests the hash code of the same object
	 */
	@Test
	public void hashSameCodeTest(){
		ImageMetaDataKey obj = getDefaultRecord(DEFAULT_LONG, DEFAULT_STRING);
		Assert.assertEquals(obj.hashCode(), obj.hashCode());
	}

	/**
	 * Tests the hash code of two different objects
	 */
	@Test
	public void hashDifferentCodeTest(){
		ImageMetaDataKey obj = getDefaultRecord(DEFAULT_LONG, DEFAULT_STRING);
		ImageMetaDataKey objb = getDefaultRecord(OTHER_LONG, OTHER_STRING);
		Assert.assertNotEquals(obj.hashCode(), objb.hashCode());
	}

	/**
	 * Creates a default record for testing
	 * @param longs the default long values
	 * @param string the default string values
	 * @return
	 */
	private ImageMetaDataKey getDefaultRecord(long longs, String string){
		ImageMetaDataKey key = new ImageMetaDataKey();
		key.setId(longs);
		key.setSequenceNumber(longs);
		key.setImageSubjectTypeCode(string);
		return key;
	}
}