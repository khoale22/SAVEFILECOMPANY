package com.heb.pm.entity;

import org.junit.Assert;
import org.junit.Test;


/**
 * @author  s753601
 * @version 2.8.0
 */
public class OrderQuantityTypeTest {

	/**
	 * Tests the getId method of order quantity type
	 */
	@Test
	public void getIdTest(){
		Assert.assertEquals("C", defaultOrderQuantityType().getId());
	}

	/**
	 * Tests the setId method
	 */
	@Test
	public void setIdTest(){
		OrderQuantityType oqt = defaultOrderQuantityType();
		oqt.setId("Test");
		Assert.assertEquals("Test", oqt.getId());
	}

	/**
	 * Tests the getAbbreviation method
	 */
	@Test
	public void getAbbreviationTest(){
		Assert.assertEquals("CASES", defaultOrderQuantityType().getAbbreviation());
	}

	/**
	 * Tests the setAbbreviation method
	 */
	@Test
	public void setAbbreviationTest(){
		OrderQuantityType oqt = defaultOrderQuantityType();
		oqt.setAbbreviation("ABC");
		Assert.assertEquals("ABC", oqt.getAbbreviation());
	}

	/**
	 * Tests the getDescription method
	 */
	@Test
	public void getDescriptionTest(){
		Assert.assertEquals("CASES", defaultOrderQuantityType().getDescription());
	}

	/**
	 * Tests the setDescription method
	 */
	@Test
	public void setDescriptionTest(){
		OrderQuantityType oqt = defaultOrderQuantityType();
		oqt.setDescription("TEST");
		Assert.assertEquals("TEST", oqt.getDescription());
	}

	/**
	 * Tests the getDisplayName method
	 */
	@Test
	public void getDisplayNameTest(){
		Assert.assertEquals("CASES[C]", defaultOrderQuantityType().getOrderQuantityTypeDisplay());
	}

	/**
	 * Tests the toString method
	 */
	@Test
	public void toStringTest(){
		OrderQuantityType oqt = defaultOrderQuantityType();
		Assert.assertEquals("FlowType{id='C', abbreviation='CASES', description='CASES'}", oqt.toString());
	}

	/**
	 * Tests the equals method of the same order quantity type
	 */
	@Test
	public void equalsTest(){
		OrderQuantityType oqt = defaultOrderQuantityType();
		Assert.assertTrue(oqt.equals(defaultOrderQuantityType()));
	}

	/**
	 * Tests the equal method of two different order quantity type
	 */
	@Test
	public void notEqualsTest(){
		OrderQuantityType oqt = defaultOrderQuantityType();
		Assert.assertFalse(oqt.equals(bagOrderQuantityType()));
	}

	/**
	 * Tests the hash code of the same order quantity type
	 */
	@Test
	public void hashSameCodeTest(){
		OrderQuantityType oqt = defaultOrderQuantityType();
		Assert.assertEquals(oqt.hashCode(), oqt.hashCode());
	}

	/**
	 * Tests the hash code of two different order quantity type
	 */
	@Test
	public void hashDifferentCodeTest(){
		OrderQuantityType oqt = defaultOrderQuantityType();
		OrderQuantityType bqt = bagOrderQuantityType();
		Assert.assertNotEquals(oqt.hashCode(), bqt.hashCode());
	}

	/**
	 * Cretes a default order quantity type
	 * @return
	 */
	private OrderQuantityType defaultOrderQuantityType(){
		OrderQuantityType oqt = new OrderQuantityType();
		oqt.setAbbreviation("CASES");
		oqt.setDescription("CASES");
		oqt.setId("C");
		return oqt;
	}

	/**
	 * creates an alternative order quantity type
	 * @return
	 */
	private OrderQuantityType bagOrderQuantityType(){
		OrderQuantityType oqt = new OrderQuantityType();
		oqt.setAbbreviation("BAG");
		oqt.setDescription("BAG");
		oqt.setId("B");
		return oqt;
	}

}