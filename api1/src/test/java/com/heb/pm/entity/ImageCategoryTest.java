package com.heb.pm.entity;

import org.junit.Assert;
import org.junit.Test;

/**
 * Unit Tests for the ImageCategory object
 * @author s753601
 * @version 2.9.0
 */
public class ImageCategoryTest {

	private static final String DEFAULT_ID="D";
	private static final String OTHER_ID="O";
	private static final String DEFAULT_ABBREVIATION="DEF";
	private static final String OTHER_ABBREVIATION="OTH";
	private static final String DEFAULT_DESCRIPTION="DEFAULT";
	private static final String OTHER_DESCRIPTION="OTHER";

	/**
	 * Tests the getId method
	 */
	@Test
	public void getIdTest() {
		Assert.assertEquals(DEFAULT_ID, getDefaultRecord(DEFAULT_ID, DEFAULT_ABBREVIATION, DEFAULT_DESCRIPTION).getId());
	}

	/**
	 * Tests the setId method
	 */
	@Test
	public void setIdTest() {
		ImageCategory ic = getDefaultRecord(DEFAULT_ID, DEFAULT_ABBREVIATION, DEFAULT_DESCRIPTION);
		ic.setId(OTHER_ID);
		Assert.assertEquals(OTHER_ID, ic.getId());
	}

	/**
	 * Tests the getAbbreviation method
	 */
	@Test
	public void getAbbreviationTest() {
		Assert.assertEquals(DEFAULT_ABBREVIATION, getDefaultRecord(DEFAULT_ID, DEFAULT_ABBREVIATION, DEFAULT_DESCRIPTION).getAbbreviation());
	}

	/**
	 * Tests the setAbbreviation method
	 */
	@Test
	public void setAbbreviationTest() {
		ImageCategory ic = getDefaultRecord(DEFAULT_ID, DEFAULT_ABBREVIATION, DEFAULT_DESCRIPTION);
		ic.setAbbreviation(OTHER_ABBREVIATION);
		Assert.assertEquals(OTHER_ABBREVIATION, ic.getAbbreviation());
	}

	/**
	 * Tests the getDescription method
	 */
	@Test
	public void getDescriptionTest() {
		Assert.assertEquals(DEFAULT_DESCRIPTION, getDefaultRecord(DEFAULT_ID, DEFAULT_ABBREVIATION, DEFAULT_DESCRIPTION).getDescription());
	}

	/**
	 * Tests the setDescription method
	 */
	@Test
	public void setDescriptionTest() {
		ImageCategory ic = getDefaultRecord(DEFAULT_ID, DEFAULT_ABBREVIATION, DEFAULT_DESCRIPTION);
		ic.setDescription(OTHER_DESCRIPTION);
		Assert.assertEquals(OTHER_DESCRIPTION, ic.getDescription());
	}

	/**
	 * Tests the toString method
	 */
	@Test
	public void toStringTest() {
		Assert.assertEquals("ImageCategory{id=D, abbreviation='DEF', description='DEFAULT'}",
				getDefaultRecord(DEFAULT_ID, DEFAULT_ABBREVIATION, DEFAULT_DESCRIPTION).toString());
	}

	/**
	 * Tests the equals method of the same object
	 */
	@Test
	public void equalsTest(){
		ImageCategory ic = getDefaultRecord(DEFAULT_ID, DEFAULT_ABBREVIATION, DEFAULT_DESCRIPTION);
		Assert.assertTrue(ic.equals(getDefaultRecord(DEFAULT_ID, DEFAULT_ABBREVIATION, DEFAULT_DESCRIPTION)));
	}

	/**
	 * Tests the equal method of two different objects
	 */
	@Test
	public void notEqualsTest(){
		ImageCategory ic = getDefaultRecord(DEFAULT_ID, DEFAULT_ABBREVIATION, DEFAULT_DESCRIPTION);
		Assert.assertFalse(ic.equals(getDefaultRecord(OTHER_ID, OTHER_ABBREVIATION, OTHER_DESCRIPTION)));
	}

	/**
	 * Tests the hash code of the same object
	 */
	@Test
	public void hashSameCodeTest(){
		ImageCategory ic = getDefaultRecord(DEFAULT_ID, DEFAULT_ABBREVIATION, DEFAULT_DESCRIPTION);
		Assert.assertEquals(ic.hashCode(), ic.hashCode());
	}

	/**
	 * Tests the hash code of two different objects
	 */
	@Test
	public void hashDifferentCodeTest(){
		ImageCategory ic = getDefaultRecord(DEFAULT_ID, DEFAULT_ABBREVIATION, DEFAULT_DESCRIPTION);
		ImageCategory oic = getDefaultRecord(OTHER_ID, OTHER_ABBREVIATION, OTHER_DESCRIPTION);
		Assert.assertNotEquals(ic.hashCode(), oic.hashCode());
	}

	private ImageCategory getDefaultRecord(String id, String abbreviation, String description){
		ImageCategory record = new ImageCategory();
		record.setId(id);
		record.setAbbreviation(abbreviation);
		record.setDescription(description);
		return record;
	}

}