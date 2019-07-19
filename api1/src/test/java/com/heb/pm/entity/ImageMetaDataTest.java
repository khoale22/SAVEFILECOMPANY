package com.heb.pm.entity;

import org.junit.Assert;
import org.junit.Test;

import javax.crypto.spec.DESedeKeySpec;

import static org.junit.Assert.*;

/**
 * Tests the ImageMetaData entity methods
 * @author s753601
 * @version 2.12.0
 */
public class ImageMetaDataTest {

	private static final String DEFAULT_STRING = "TEST";
	private static final String OTHER_STRING = "OTHER";
	private static final long DEFAULT_LONG = 1L;
	private static final long OTHER_LONG = 2L;

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
		ImageMetaDataKey key = getDefaultKey();
		key.setImageSubjectTypeCode(OTHER_STRING);
		ImageMetaData metaData = getDefaultRecord();
		metaData.setKey(key);
		Assert.assertEquals(key, metaData.getKey());
	}

	/**
	 * Tests the getUri method
	 */
	@Test
	public void getUriTextTest() {
		Assert.assertEquals(DEFAULT_STRING, getDefaultRecord().getUriText());
	}

	/**
	 * Tests the setUriText method
	 */
	@Test
	public void setUriTextTest() {
		ImageMetaData metaData = getDefaultRecord();
		metaData.setUriText(OTHER_STRING);
		Assert.assertEquals(OTHER_STRING, metaData.getUriText());
	}

	/**
	 * Tests the getEntity method
	 */
	@Test
	public void getEntityTest()  {
		Assert.assertEquals(getDefaultEntity(), getDefaultRecord().getEntity());
	}

	/**
	 * Tests the setEntity method
	 */
	@Test
	public void setEntityTest() {
		GenericEntity entity = getDefaultEntity();
		entity.setId(OTHER_LONG);
		ImageMetaData metaData = getDefaultRecord();
		metaData.setEntity(entity);
		Assert.assertEquals(entity, metaData.getEntity());
	}

	/**
	 * Tests that an object equals itself.
	 */
	@Test
	public void equalsSameObject() {
		ImageMetaData test = this.getDefaultRecord();
		boolean equals = test.equals(test);
		Assert.assertTrue(equals);
	}

	/**
	 * Tests that an object equals an object with the same values.
	 */
	@Test
	public void equalsSimilarObject() {
		ImageMetaData test1 = this.getDefaultRecord();
		ImageMetaData test2 = this.getDefaultRecord();
		boolean equals = test1.equals(test2);
		Assert.assertTrue(equals);
	}

	/**
	 * Tests that it does not equal when passed null.
	 */
	@Test
	public void equalsNull() {
		ImageMetaData test = this.getDefaultRecord();
		boolean equals = test.equals(null);
		Assert.assertFalse(equals);
	}

	/**
	 * Test that it does not equal an object of a different type.
	 */
	@Test
	public void equalsDifferentType() {
		ImageMetaData test = this.getDefaultRecord();
		ImageMetaDataKey otherKey = getDefaultKey();
		otherKey.setSequenceNumber(OTHER_LONG);
		test.setKey(otherKey);
		ImageMetaData other = getDefaultRecord();
		boolean equals = test.equals(other);
		Assert.assertFalse(equals);
	}

	/**
	 * Test that it does not equal an object with a null key.
	 */
	@Test
	public void equalsNullKeyOther() {
		ImageMetaData test1 = this.getDefaultRecord();
		ImageMetaData test2 = new ImageMetaData();
		boolean equals = test1.equals(test2);
		Assert.assertFalse(equals);
	}

	/**
	 * Test that it does not equal an object when it's key is null.
	 */
	@Test
	public void equalsNullKeySelf() {
		ImageMetaData test1 = new ImageMetaData();
		ImageMetaData test2 = this.getDefaultRecord();
		boolean equals = test1.equals(test2);
		Assert.assertFalse(equals);
	}

	/**
	 * Test that it does not equal an object when both keys are null
	 */
	@Test
	public void equalsNullKeyBoth() {
		ImageMetaData test1 = new ImageMetaData();
		ImageMetaData test2 = new ImageMetaData();
		boolean equals = test1.equals(test2);
		Assert.assertTrue(equals);
	}

	/**
	 * Check hashCode is consistent on the same object.
	 */
	@Test
	public void hashCodeSelf() {
		ImageMetaData test = this.getDefaultRecord();
		Assert.assertEquals(test.hashCode(), test.hashCode());
	}

	/**
	 * Check hashCode is the same on similar objects.
	 */
	@Test
	public void hashCodeSimilarObjects() {
		ImageMetaData test1 = this.getDefaultRecord();
		ImageMetaData test2 = this.getDefaultRecord();
		Assert.assertEquals(test1.hashCode(), test2.hashCode());
	}

	/**
	 * Checks the hashCode of different objects
	 */
	@Test
	public void hashCodeDifferentObjects() {
		ImageMetaData test1 = this.getDefaultRecord();
		ImageMetaData test2 = this.getDefaultRecord();
		ImageMetaDataKey otherKey = getDefaultKey();
		otherKey.setSequenceNumber(OTHER_LONG);
		test2.setKey(otherKey);
		Assert.assertNotEquals(test1.hashCode(), test2.hashCode());
	}

	/**
	 * Check hashCode is zero with a null key.
	 */
	@Test
	public void hashCodeNullKey() {
		ImageMetaData test = new ImageMetaData();
		Assert.assertEquals(0, test.hashCode());
	}

	/**
	 * Tests toString.
	 */
	@Test
	public void testToString() {
		ImageMetaData test = this.getDefaultRecord();
		Assert.assertEquals("ImageMetaData{" +
						"key=ImageMetaData{" +
							"id='1', " +
							"sequenceNumber='1', " +
							"imageSubjectTypeCode='TEST'" +
						"}, " +
						"uriText=TEST, " +
						"entity=GenericEntity{" +
							"id=1, " +
							"type='null', " +
							"abbreviation='null', " +
							"displayText='null', " +
							"displayNumber=null" +
							"}" +
						"}",
				test.toString());
	}

	/**
	 * Creates a default record for testing
	 * @return
	 */
	private ImageMetaData getDefaultRecord(){
		ImageMetaData metaData = new ImageMetaData();
		metaData.setKey(getDefaultKey());
		metaData.setEntity(getDefaultEntity());
		metaData.setUriText(DEFAULT_STRING);
		return metaData;
	}

	/**
	 * Generates the key for a default record
	 * @return
	 */
	private ImageMetaDataKey getDefaultKey(){
		ImageMetaDataKey key = new ImageMetaDataKey();
		key.setId(DEFAULT_LONG);
		key.setImageSubjectTypeCode(DEFAULT_STRING);
		key.setSequenceNumber(DEFAULT_LONG);
		return key;
	}

	/**
	 * Generates the entity for the default record
	 * @return
	 */
	private GenericEntity getDefaultEntity(){
		GenericEntity entity = new GenericEntity();
		entity.setId(DEFAULT_LONG);
		return entity;
	}
}