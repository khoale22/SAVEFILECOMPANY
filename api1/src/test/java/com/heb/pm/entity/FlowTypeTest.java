package com.heb.pm.entity;

import org.junit.Assert;
import org.junit.Test;


/**
 * Unit tests for the Flow type entity
 * @Author s753601
 * @version 2.8.0
 */
public class FlowTypeTest {

	/**
	 * Tests the getId method of flow type
	 */
	@Test
	public void getIdTest() {
		Assert.assertEquals(" ", defaultFlowType().getId());
	}

	/**
	 * Tests the setId method
	 */
	@Test
	public void setIdTest(){
		FlowType ft = defaultFlowType();
		ft.setId("Test");
		Assert.assertEquals("Test", ft.getId());
	}

	/**
	 * Tests the getAbbreviation method
	 */
	@Test
	public void getAbbreviationTest(){
		Assert.assertEquals("DEF", defaultFlowType().getAbbreviation());
	}

	/**
	 * Tests the setAbbreviation method
	 */
	@Test
	public void setAbbreviationTest(){
		FlowType ft = defaultFlowType();
		ft.setAbbreviation("ABC");
		Assert.assertEquals("ABC", ft.getAbbreviation());
	}

	/**
	 * Tests the getDescription method
	 */
	@Test
	public void getDescriptionTest(){
		Assert.assertEquals("DEFAULT", defaultFlowType().getDescription());
	}

	/**
	 * Tests the setDescription method
	 */
	@Test
	public void setDescriptionTest(){
		FlowType ft = defaultFlowType();
		ft.setDescription("TEST");
		Assert.assertEquals("TEST", ft.getDescription());
	}

	/**
	 * Tests the getDisplayName method
	 */
	@Test
	public void getDisplayNameTest(){
		Assert.assertEquals("DEFAULT[ ]", defaultFlowType().getDisplayName());
	}

	/**
	 * Tests the toString method
	 */
	@Test
	public void toStringTest(){
		FlowType ft = defaultFlowType();
		Assert.assertEquals("FlowType{id=' ', abbreviation='DEF', description='DEFAULT'}", ft.toString());
	}

	/**
	 * Tests the equals method of the same object
	 */
	@Test
	public void equalsTest(){
		FlowType ft = defaultFlowType();
		Assert.assertTrue(ft.equals(defaultFlowType()));
	}

	/**
	 * Tests the equal method of two different objects
	 */
	@Test
	public void notEqualsTest(){
		FlowType ft = defaultFlowType();
		Assert.assertFalse(ft.equals(flowFlowType()));
	}

	/**
	 * Tests the hash code of the same object
	 */
	@Test
	public void hashSameCodeTest(){
		FlowType ft = defaultFlowType();
		Assert.assertEquals(ft.hashCode(), ft.hashCode());
	}

	/**
	 * Tests the hash code of two different objects
	 */
	@Test
	public void hashDifferentCodeTest(){
		FlowType ft = defaultFlowType();
		FlowType fo = flowFlowType();
		Assert.assertNotEquals(ft.hashCode(), fo.hashCode());
	}

	/**
	 * Creates the default flow type
	 * @return
	 */
	private FlowType defaultFlowType(){
		FlowType ft = new FlowType();
		ft.setAbbreviation("DEF");
		ft.setDescription("DEFAULT");
		ft.setId(" ");
		return ft;
	}

	/**
	 * Creates an alternative flow type.
	 * @return
	 */
	private FlowType flowFlowType(){
		FlowType ft = new FlowType();
		ft.setAbbreviation("FLOW");
		ft.setDescription("LTL FLOW TYPE");
		ft.setId("FLO");
		return ft;
	}
}