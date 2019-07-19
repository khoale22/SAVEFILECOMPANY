package com.heb.pm.entity;

import org.junit.Assert;
import org.junit.Test;

public class ImageTypeTest {

	private static final String DEFAULT_STRING="TEST";
	private static final String OTHER_STRING="OTHER";

	/**
	 * Tests the getId method
	 */
	@Test
	public void getIdTest() {
		Assert.assertEquals(DEFAULT_STRING, getDefaultRecord(DEFAULT_STRING).getId());
	}

	/**
	 * Tests the setId method
	 */
	@Test
	public void setIdTest() {
		ImageType obj = getDefaultRecord(DEFAULT_STRING);
		obj.setId(OTHER_STRING);
		Assert.assertEquals(OTHER_STRING, obj.getId());
	}

	/**
	 * Tests the getDescription method
	 */
	@Test
	public void getImageFormatTest() {
		Assert.assertEquals(DEFAULT_STRING, getDefaultRecord(DEFAULT_STRING).getImageFormat());
	}

	/**
	 * Tests the setDescription method
	 */
	@Test
	public void setImageFormatTest() {
		ImageType obj = getDefaultRecord(DEFAULT_STRING);
		obj.setImageFormat(OTHER_STRING);
		Assert.assertEquals(OTHER_STRING, obj.getImageFormat());
	}

	/**
	 * Tests the toString method
	 */
	@Test
	public void toStringTest() {
		Assert.assertEquals("ImageType{id='TEST', imageFormat='TEST'}",
				getDefaultRecord(DEFAULT_STRING).toString());
	}

	/**
	 * Tests the equals method of the same object
	 */
	@Test
	public void equalsTest(){
		ImageType obj = getDefaultRecord(DEFAULT_STRING);
		Assert.assertTrue(obj.equals(getDefaultRecord(DEFAULT_STRING)));
	}

	/**
	 * Tests the equal method of two different objects
	 */
	@Test
	public void notEqualsTest(){
		ImageType obj = getDefaultRecord(DEFAULT_STRING);
		Assert.assertFalse(obj.equals(getDefaultRecord(OTHER_STRING)));
	}

	/**
	 * Tests the hash code of the same object
	 */
	@Test
	public void hashSameCodeTest(){
		ImageType obj = getDefaultRecord(DEFAULT_STRING);
		Assert.assertEquals(obj.hashCode(), obj.hashCode());
	}

	/**
	 * Tests the hash code of two different objects
	 */
	@Test
	public void hashDifferentCodeTest(){
		ImageType obj = getDefaultRecord(DEFAULT_STRING);
		ImageType objb = getDefaultRecord(OTHER_STRING);
		Assert.assertNotEquals(obj.hashCode(), objb.hashCode());
	}

	private ImageType getDefaultRecord(String string){
		ImageType obj = new ImageType();
		obj.setId(string);
		obj.setImageFormat(string);
		return obj;
	}

}