package com.heb.pm.entity;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import testSupport.LoggingSupportTestRunner;

/**
 * Tests ItemNotDeletedReason
 *
 * @author vn40486
 * @since 2.0.4
 */
@RunWith(LoggingSupportTestRunner.class)
@ContextConfiguration(locations = "classpath*:testConfig.xml")
public class ItemNotDeletedReasonTest {

	//item not deleted reason code - Alternate Pack.
	private static final String ITM_NOT_DELD_RSN_CD_1 = "ALTP";

	//item not deleted reason code - Associate now DSD.
	private static final String ITM_NOT_DELD_RSN_CD_2 = "ASDSD";

	private static final String ABBREVIATION = "ABBREV";
	private static final String DESCRIPTION = "DESC";
	private static final String USER_INSTRUCTIONS = "test instructions";

	@Autowired
	private ItemNotDeletedReasonRepositoryTest repo;

	/*
	 * JPA Mapping
	 */

	/**
	 * Tests the JPA Mapping of the class.
	 */
	@Test
	public void testJpaMapping() {
		ItemNotDeletedReason rsn = this.repo.findOne("ACTST");
		Assert.assertEquals("ACTST", rsn.getCode());
		Assert.assertEquals("ACTST ", rsn.getAbbreviation());
		Assert.assertEquals("Active Store                                      ", rsn.getDescription());
		Assert.assertEquals("Remove Active Store", rsn.getUserInstructions());
	}

	/*
	 * equals
	 */

	/**
	 * Test equals on the same object.
	 */
	@Test
	public void equalsSameObject() {
		ItemNotDeletedReason item = this.getDefaultRecord();
		boolean equals = item.equals(item);
		Assert.assertTrue(equals);
	}

	/**
	 * Test equals on similar objects.
	 */
	@Test
	public void equalsSimilarObject(){
		ItemNotDeletedReason item1 = this.getDefaultRecord();
		ItemNotDeletedReason item2 = this.getDefaultRecord();
		boolean equals = item1.equals(item2);
		Assert.assertTrue(equals);
	}

	/**
	 * Test equals on objects with different keys.
	 */
	@Test
	public void equalsDifferentKeys() {
		ItemNotDeletedReason item1 = this.getDefaultRecord();
		ItemNotDeletedReason item2 = this.getASDSDReasonCode();
		boolean equals = item1.equals(item2);
		Assert.assertFalse(equals);
	}

	/**
	 * Test equals on null
	 */
	@Test
	public void testEqualsNull() {
		ItemNotDeletedReason item = this.getDefaultRecord();
		boolean equals = item.equals(null);
		Assert.assertFalse(equals);
	}

	/**
	 * Tests equals on a different class.
	 */
	@Test
	public void testEqualsDifferentClass() {
		ItemNotDeletedReason item = this.getDefaultRecord();
		boolean equals = item.equals(Integer.valueOf(66));
		Assert.assertFalse(equals);
	}


	/**
	 * Tests equals when the left side has a null key.
	 */
	@Test
	public void testEqualsNullSourceKey() {
		ItemNotDeletedReason item1 = new ItemNotDeletedReason();
		ItemNotDeletedReason item2 = this.getDefaultRecord();
		boolean equals = item1.equals(item2);
		Assert.assertFalse(equals);
	}


	/**
	 * Test hashCode on the same object.
	 */
	@Test
	public void hashCodeSameObject() {
		ItemNotDeletedReason item = this.getDefaultRecord();
		Assert.assertEquals(item.hashCode(), item.hashCode());
	}

	/**
	 * Test hashCode on similar objects.
	 */
	@Test
	public void hashCodeSimilarObjects() {
		ItemNotDeletedReason item1 = this.getDefaultRecord();
		ItemNotDeletedReason item2 = this.getDefaultRecord();
		Assert.assertEquals(item1.hashCode(), item2.hashCode());
	}

	/**
	 * Test hashCode on different objects.
	 */
	@Test
	public void hashCodeDifferentObjects() {
		ItemNotDeletedReason item1 = this.getDefaultRecord();
		ItemNotDeletedReason item2 = this.getASDSDReasonCode();
		Assert.assertNotEquals(item1.hashCode(), item2.hashCode());
	}

	/**
	 * Test hashCode with a null key.
	 */
	@Test
	public void hashCodeNullKey() {
		ItemMaster item = new ItemMaster();
		Assert.assertEquals(0, item.hashCode());
	}

	/**
	 * Test hashCode when the left side has a key and the right side does not.
	 */
	@Test
	public void hashCodeNullRight() {
		ItemNotDeletedReason item1 = this.getDefaultRecord();
		ItemNotDeletedReason item2 = new ItemNotDeletedReason();
		Assert.assertNotEquals(item1.hashCode(), item2.hashCode());
	}

	/**
	 * Tests toString.
	 */
	@Test
	public void testToString() {
		ItemNotDeletedReason item = this.getASDSDReasonCode();
		Assert.assertEquals("ItemNotDeletedReason{code='ASDSD', abbreviation='null', description='null'}",
				item.toString());
	}

	/*
	 * getters
	 */

	/**
	 * Tests getCode.
	 */
	@Test
	public void getCode() {
		ItemNotDeletedReason rsn = this.getDefaultRecord();
		Assert.assertEquals(ItemNotDeletedReasonTest.ITM_NOT_DELD_RSN_CD_1, rsn.getCode());
	}

	/**
	 * Tests getAbbreviation.
	 */
	@Test
	public void getAbbreviation() {
		ItemNotDeletedReason rsn = this.getDefaultRecord();
		Assert.assertEquals(ItemNotDeletedReasonTest.ABBREVIATION, rsn.getAbbreviation());
	}

	/**
	 * Tests getDescription.
	 */
	@Test
	public void getDescription() {
		ItemNotDeletedReason rsn = this.getDefaultRecord();
		Assert.assertEquals(ItemNotDeletedReasonTest.DESCRIPTION, rsn.getDescription());
	}

	/**
	 * Tests getInstructions.
	 */
	@Test
	public void getInstructions() {
		ItemNotDeletedReason rsn = this.getDefaultRecord();
		Assert.assertEquals(ItemNotDeletedReasonTest.USER_INSTRUCTIONS, rsn.getUserInstructions());
	}

	/*
	 * Support functions.
	 */

	/**
	 * Returns an ItemNotDeletedReason object that equals the one that is first in the test table.
	 *
	 * @return An ItemNotDeletedReason object that equals the one that is first in the test table.
	 */
	private ItemNotDeletedReason getDefaultRecord() {
		ItemNotDeletedReason item = new ItemNotDeletedReason();
		item.setCode(ItemNotDeletedReasonTest.ITM_NOT_DELD_RSN_CD_1);
		item.setAbbreviation(ItemNotDeletedReasonTest.ABBREVIATION);
		item.setDescription(ItemNotDeletedReasonTest.DESCRIPTION);
		item.setUserInstructions(ItemNotDeletedReasonTest.USER_INSTRUCTIONS);
		return item;
	}

	/**
	 * Returns an ASDSD ItemNotDeletedReason object .
	 *
	 * @return An ItemNotDeletedReason object.
	 */
	private ItemNotDeletedReason getASDSDReasonCode() {
		ItemNotDeletedReason item = new ItemNotDeletedReason();
		item.setCode(ItemNotDeletedReasonTest.ITM_NOT_DELD_RSN_CD_2);
		return item;
	}
}
