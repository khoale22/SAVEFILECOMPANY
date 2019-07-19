package com.heb.pm.entity;

import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDateTime;

import static org.junit.Assert.*;

/**
 * Unit tests for the Item Master Audit entity
 * @author  s753601
 * @version 2.8.0
 */
public class ItemMasterAuditTest {

	/**
	 * Constants for building default values
	 */
	private static final String DEFAULT_KEY_TYPE="ITMCD";
	private static final long DEFAULT_KEY_CODE=1L;
	private static final String DEFAULT_CHANGED_BY = "TestUser1";
	private static final String DEFAULT_ACTION_CODE= "UPDAT";
	private static final String DEFAULT_RECORD_DRU_TYPE = "7";
	private static final boolean DEFAULT_IS_DRU = true;
	private static final boolean DEFAULT_ALWAYS_SUB=true;
	private static final long DEFAULT_ROWS_FACING = 1L;
	private static final long DEFAULT_ROWS_HIGH = 1L;
	private static final long DEFAULT_ROWS_DEEP = 1L;
	private static final long DEFAULT_ORIENTATION = 1L;
	private static final String DISPLAY_READY_PALLET_READABLE="Display Ready Pallet";

	/**
	 * Test for the getKey method
	 */
	@Test
	public void getKeyTest() {
		ItemMasterAuditKey test = getDefaultKey();
		Assert.assertEquals(test, getDefaultRecord().getKey());
	}

	/**
	 * Test for the setKey method
	 */
	@Test
	public void setKeyTest() {
		ItemMasterAuditKey key = getDefaultKey();
		key.setItemCode(2L);
		ItemMasterAudit audit = getDefaultRecord();
		audit.setKey(key);
		Assert.assertEquals(key, audit.getKey());
	}

	/**
	 * Test for the getAction method
	 */
	@Test
	public void getActionTest() {
		Assert.assertEquals(DEFAULT_ACTION_CODE, getDefaultRecord().getAction());
	}

	/**
	 * Test for the setAction method
	 */
	@Test
	public void setActionTest() {
		String test = "NOT_UPDAT";
		ItemMasterAudit audit = getDefaultRecord();
		audit.setAction(test);
		Assert.assertEquals(test, audit.getAction());
	}

	/**
	 * Test for the getChangedBy method
	 */
	@Test
	public void getChangedByTest() {
		Assert.assertEquals(DEFAULT_CHANGED_BY, getDefaultRecord().getChangedBy());
	}

	/**
	 * Test for the setChangedBy method
	 */
	@Test
	public void setChangedByTest() {
		String test = "TestUser2";
		ItemMasterAudit audit = getDefaultRecord();
		audit.setChangedBy(test);
		Assert.assertEquals(test, audit.getChangedBy());

	}

	/**
	 * Test for the getChangedOn method
	 */
	@Test
	public void getChangedOnTest() {
		Assert.assertTrue(getDefaultRecord().getChangedOn() instanceof LocalDateTime);
	}

	/**
	 * Test for the setChangedOn method
	 */
	@Test
	public void setChangedOnTest() {
		LocalDateTime test = LocalDateTime.now();
		ItemMasterAudit audit = getDefaultRecord();
		audit.setChangedOn(test);
		Assert.assertEquals(test, audit.getChangedOn());

	}

	/**
	 * Test for the getDisplayReadyUnit method
	 */
	@Test
	public void getDisplayReadyUnitTest() {
		Assert.assertEquals(DEFAULT_IS_DRU, getDefaultRecord().getDisplayReadyUnit());
	}

	/**
	 * Test for the setDisplayReadyUnit method
	 */
	@Test
	public void setDisplayReadyUnitTest() {
		ItemMasterAudit audit = getDefaultRecord();
		audit.setDisplayReadyUnit(false);
		Assert.assertEquals(false, audit.getDisplayReadyUnit());

	}

	/**
	 * Test for the getTypeOfDRU method
	 */
	@Test
	public void getTypeOfDRUTest() {
		Assert.assertEquals(DEFAULT_RECORD_DRU_TYPE, getDefaultRecord().getTypeOfDRU());
	}

	/**
	 * Test for the setTypeOfDRU method
	 */
	@Test
	public void setTypeOfDRUTest() {
		String test = "9";
		ItemMasterAudit audit = getDefaultRecord();
		audit.setTypeOfDRU(test);
		Assert.assertEquals(test, audit.getTypeOfDRU());

	}

	/**
	 * Test for the getAlwaysSubWhenOut method
	 */
	@Test
	public void getAlwaysSubWhenOutTest() {
		Assert.assertEquals(DEFAULT_ALWAYS_SUB, getDefaultRecord().getAlwaysSubWhenOut());
	}

	/**
	 * Test for the setAlwaysSubWhenOut method
	 */
	@Test
	public void setAlwaysSubWhenOutTest() {
		ItemMasterAudit audit = getDefaultRecord();
		audit.setAlwaysSubWhenOut(false);
		Assert.assertEquals(false, audit.getAlwaysSubWhenOut());

	}

	/**
	 * Test for the getRowsFacing method
	 */
	@Test
	public void getRowsFacingTest() {
		Assert.assertEquals(DEFAULT_ROWS_FACING, getDefaultRecord().getRowsFacing());
	}

	/**
	 * Test for the setRowsFacing method
	 */
	@Test
	public void setRowsFacingTest() {
		long test = 2L;
		ItemMasterAudit audit = getDefaultRecord();
		audit.setRowsFacing(test);
		Assert.assertEquals(test, audit.getRowsFacing());

	}

	/**
	 * Test for the getRowsDeep method
	 */
	@Test
	public void getRowsDeepTest() {
		Assert.assertEquals(DEFAULT_ROWS_DEEP, getDefaultRecord().getRowsDeep());
	}

	/**
	 * Test for the setRowsDeep method
	 */
	@Test
	public void setRowsDeepTest() {
		long test = 2L;
		ItemMasterAudit audit = getDefaultRecord();
		audit.setRowsDeep(test);
		Assert.assertEquals(test, audit.getRowsDeep());

	}

	/**
	 * Test for the getRowsHigh method
	 */
	@Test
	public void getRowsHighTest() {
		Assert.assertEquals(DEFAULT_ROWS_HIGH, getDefaultRecord().getRowsHigh());
	}

	/**
	 * Test for the setRowsHigh method
	 */
	@Test
	public void setRowsHighTest() {
		long test = 2L;
		ItemMasterAudit audit = getDefaultRecord();
		audit.setRowsHigh(test);
		Assert.assertEquals(test, audit.getRowsHigh());

	}

	/**
	 * Test for the getOrientation method
	 */
	@Test
	public void getOrientationTest() {
		Assert.assertEquals(DEFAULT_ORIENTATION, getDefaultRecord().getOrientation());
	}

	/**
	 * Test for the setOrientation method
	 */
	@Test
	public void setOrientationTest() {
		long test = 2L;
		ItemMasterAudit audit = getDefaultRecord();
		audit.setOrientation(test);
		Assert.assertEquals(test, audit.getOrientation());

	}

	/**
	 * Test for the getTypeOfDRUDisplayName method
	 */
	@Test
	public void getTypeOfDRUDisplayNameTest() {
		Assert.assertEquals(DISPLAY_READY_PALLET_READABLE, getDefaultRecord().getTypeOfDRUDisplayName());
	}

	/**
	 * Tests that an object equals itself.
	 */
	@Test
	public void equalsSameObject() {
		ItemMasterAudit item = this.getDefaultRecord();
		boolean equals = item.equals(item);
		Assert.assertTrue(equals);
	}

	/**
	 * Tests that an object equals an object with the same values.
	 */
	@Test
	public void equalsSimilarObject() {
		ItemMasterAudit i1 = this.getDefaultRecord();
		ItemMasterAudit i2 = this.getDefaultRecord();
		boolean equals = i1.equals(i2);
		Assert.assertTrue(equals);
	}

	/**
	 * Tests that it does not equal when passed null.
	 */
	@Test
	public void equalsNull() {
		ItemMasterAudit item = this.getDefaultRecord();
		boolean equals = item.equals(null);
		Assert.assertFalse(equals);
	}

	/**
	 * Test that it does not equal an object of a different type.
	 */
	@Test
	public void equalsDifferentType() {
		ItemMasterAudit item = this.getDefaultRecord();
		boolean equals = item.equals(Integer.valueOf(0));
		Assert.assertFalse(equals);
	}

	/**
	 * Test that it does not equal an object with a null key.
	 */
	@Test
	public void equalsNullKeyOther() {
		ItemMasterAudit item = this.getDefaultRecord();
		ItemMasterAudit i2 = new ItemMasterAudit();
		boolean equals = item.equals(i2);
		Assert.assertFalse(equals);
	}

	/**
	 * Test that it does not equal an object when it's key is null.
	 */
	@Test
	public void equalsNullKeySelf() {
		ItemMasterAudit item = new ItemMasterAudit();
		ItemMasterAudit i1 = this.getDefaultRecord();
		boolean equals = item.equals(i1);
		Assert.assertFalse(equals);
	}

	/**
	 * Test that it does not equal an object when both keys are null
	 */
	@Test
	public void equalsNullKeyBoth() {
		ItemMasterAudit item = new ItemMasterAudit();
		ItemMasterAudit i1 = new ItemMasterAudit();
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
		ItemMasterAudit item = this.getDefaultRecord();
		Assert.assertEquals(item.hashCode(), item.hashCode());
	}

	/**
	 * Check hashCode is the same on similar objects.
	 */
	@Test
	public void hashCodeSimilarObjects() {
		ItemMasterAudit i1 = this.getDefaultRecord();
		ItemMasterAudit i2 = this.getDefaultRecord();
		Assert.assertEquals(i1.hashCode(), i2.hashCode());
	}

	@Test
	public void hashCodeDifferentObjects() {
		ItemMasterAudit i1 = this.getDefaultRecord();
	}

	/**
	 * Check hashCode is zero with a null key.
	 */
	@Test
	public void hashCodeNullKey() {
		ItemMasterAudit item = new ItemMasterAudit();
		Assert.assertEquals(0, item.hashCode());
	}


	/*
	 * toString
	 */

	/**
	 * Tests toString.
	 */
	@Test
	public void testToString() {
		ItemMasterAudit item = this.getDefaultRecord();
		Assert.assertEquals("ItemMasterAudit{key=ItemMasterAuditKey{" +
						"itemType='ITMCD', " +
						"itemCode=1', " +
						"changedOn=" + item.getChangedOn()+"}, " +
						"action='UPDAT', " +
						"changedBy='TestUser1', " +
						"displayReadyUnit=true, " +
						"typeOfDRU=7', " +
						"alwaysSubWhenOut=true', " +
						"rowsDeep=1', " +
						"rowsFacing=1', " +
						"rowsHigh=1', " +
						"orientation=1'}",
				item.toString());
	}

	private ItemMasterAudit getDefaultRecord(){
		ItemMasterAuditKey key = getDefaultKey();
		ItemMasterAudit audit = new ItemMasterAudit();
		audit.setChangedBy(DEFAULT_CHANGED_BY);
		audit.setAction(DEFAULT_ACTION_CODE);
		audit.setDisplayReadyUnit(DEFAULT_IS_DRU);
		audit.setAlwaysSubWhenOut(DEFAULT_ALWAYS_SUB);
		audit.setTypeOfDRU(DEFAULT_RECORD_DRU_TYPE);
		audit.setRowsFacing(DEFAULT_ROWS_FACING);
		audit.setRowsHigh(DEFAULT_ROWS_HIGH);
		audit.setRowsDeep(DEFAULT_ROWS_DEEP);
		audit.setOrientation(DEFAULT_ORIENTATION);
		audit.setKey(key);
		return audit;
	}

	/**
	 * Returns a default key for testing
	 * @return a default key for testing
	 */
	private ItemMasterAuditKey getDefaultKey(){
		ItemMasterAuditKey key = new ItemMasterAuditKey();
		key.setItemType(DEFAULT_KEY_TYPE);
		key.setItemCode(DEFAULT_KEY_CODE);
		key.setChangedOn(LocalDateTime.now());
		return key;
	}

}