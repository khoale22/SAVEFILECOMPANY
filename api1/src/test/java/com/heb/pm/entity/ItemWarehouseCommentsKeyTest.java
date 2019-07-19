package com.heb.pm.entity;

import org.junit.Assert;
import org.junit.Test;

/**
 * Unit Tests for the ItemWarehouseCommentsKey entity
 * @author  s753601
 * @version 2.8.0
 */
public class ItemWarehouseCommentsKeyTest {

	private long DEFAULT_ITEM_ID=1L;
	private int DEFAULT_COMMENT_NUMBER=1;
	private int OTHER_COMMENT_NUMBER=2;
	private int DEFAULT_WAREHOUSE_NUMBER= 1;
	private String DEFAULT_ITEM_TYPE = "ITMCD";
	private String DEFAULT_COMMENT_TYPE = "REMAK";
	private String DEFAULT_WAREHOUSE_TYPE = "W";

	/**
	 * Tests the getItemId method
	 */
	@Test
	public void getItemIdTest() {
		Assert.assertEquals(DEFAULT_ITEM_ID, getItemWarehouseCommentsKey(DEFAULT_COMMENT_NUMBER).getItemId());
	}

	/**
	 * Tests the setItemId method
	 */
	@Test
	public void setItemIdTest() {
		long otherID = 2L;
		ItemWarehouseCommentsKey key = getItemWarehouseCommentsKey(DEFAULT_COMMENT_NUMBER);
		key.setItemId(otherID);
		Assert.assertEquals(otherID, getItemWarehouseCommentsKey(DEFAULT_COMMENT_NUMBER).getItemId());
	}

	/**
	 * Tests the getItemType method
	 */
	@Test
	public void getItemTypeTest() {
		Assert.assertEquals(DEFAULT_ITEM_TYPE, getItemWarehouseCommentsKey(DEFAULT_COMMENT_NUMBER).getItemType());
	}

	/**
	 * Tests the setItemType method
	 */
	@Test
	public void setItemTypeTest() {
		String otherType = "TEST";
		ItemWarehouseCommentsKey key = getItemWarehouseCommentsKey(DEFAULT_COMMENT_NUMBER);
		key.setItemType(otherType);
		Assert.assertEquals(otherType, key.getItemType());
	}

	/**
	 * Tests the getWarehouseNumber method
	 */
	@Test
	public void getWarehouseNumberTest() {
		Assert.assertEquals(DEFAULT_WAREHOUSE_NUMBER, getItemWarehouseCommentsKey(DEFAULT_COMMENT_NUMBER).getWarehouseNumber());
	}

	/**
	 * Tests the setWarehouseNumber method
	 */
	@Test
	public void setWarehouseNumberTest() {
		ItemWarehouseCommentsKey key = getItemWarehouseCommentsKey(DEFAULT_COMMENT_NUMBER);
		int otherNumber = 2;
		key.setWarehouseNumber(otherNumber);
		Assert.assertEquals(otherNumber, getItemWarehouseCommentsKey(DEFAULT_COMMENT_NUMBER).getWarehouseNumber());

	}

	/**
	 * Tests the getWarehouseType method
	 */
	@Test
	public void getWarehouseTypeTest() {
		Assert.assertEquals(DEFAULT_WAREHOUSE_TYPE, getItemWarehouseCommentsKey(DEFAULT_COMMENT_NUMBER).getWarehouseType());

	}

	/**
	 * Tests the setWarehouseType method
	 */
	@Test
	public void setWarehouseTypeTest() {
		String otherType = "TEST";
		ItemWarehouseCommentsKey key = getItemWarehouseCommentsKey(DEFAULT_COMMENT_NUMBER);
		key.setWarehouseType(otherType);
		Assert.assertEquals(otherType, key.getWarehouseType());
	}

	/**
	 * Tests the getItemCommentType method
	 */
	@Test
	public void getItemCommentTypeTest() {
		Assert.assertEquals(DEFAULT_COMMENT_TYPE, getItemWarehouseCommentsKey(DEFAULT_COMMENT_NUMBER).getItemCommentType());

	}

	/**
	 * Tests the setItemCommentType method
	 */
	@Test
	public void setItemCommentTypeTest() {
		String otherType = "TEST";
		ItemWarehouseCommentsKey key = getItemWarehouseCommentsKey(DEFAULT_COMMENT_NUMBER);
		key.setItemCommentType(otherType);
		Assert.assertEquals(otherType, key.getItemCommentType());
	}

	/**
	 * Tests the getItemCommentNumber method
	 */
	@Test
	public void getItemCommentNumberTest() {
		Assert.assertEquals(DEFAULT_COMMENT_NUMBER, getItemWarehouseCommentsKey(DEFAULT_COMMENT_NUMBER).getItemCommentNumber());
	}

	/**
	 * Tests the setItemCommentNumber method
	 */
	@Test
	public void setItemCommentNumberTest() {
		int otherNumber = 2;
		ItemWarehouseCommentsKey key = getItemWarehouseCommentsKey(DEFAULT_COMMENT_NUMBER);
		key.setItemCommentNumber(otherNumber);
		Assert.assertEquals(otherNumber, key.getItemCommentNumber());
	}

	/**
	 * Tests the toString method
	 */
	@Test
	public void toStringTest(){
		ItemWarehouseCommentsKey iwca = getItemWarehouseCommentsKey(DEFAULT_COMMENT_NUMBER);
		Assert.assertEquals("WarehouseLocationItemKey{itemType='ITMCD', itemCode='1', warehouseType='W', " +
				"warehouseNumber='1', itemCommentType='REMAK', itemCommentNumber='1'}", iwca.toString());
	}

	/**
	 * Tests the equals method of the same object
	 */
	@Test
	public void equalsTest(){
		ItemWarehouseCommentsKey iwca = getItemWarehouseCommentsKey(DEFAULT_COMMENT_NUMBER);
		ItemWarehouseCommentsKey iwcb = getItemWarehouseCommentsKey(DEFAULT_COMMENT_NUMBER);
		Assert.assertTrue(iwca.equals(iwcb));
	}

	/**
	 * Tests the equal method of two different objects
	 */
	@Test
	public void notEqualsTest(){
		ItemWarehouseCommentsKey iwca = getItemWarehouseCommentsKey(DEFAULT_COMMENT_NUMBER);
		ItemWarehouseCommentsKey iwcb = getItemWarehouseCommentsKey(OTHER_COMMENT_NUMBER);
		Assert.assertFalse(iwca.equals(iwcb));
	}

	/**
	 * Tests the hash code of the same object
	 */
	@Test
	public void hashSameCodeTest(){
		ItemWarehouseCommentsKey iwca = getItemWarehouseCommentsKey(DEFAULT_COMMENT_NUMBER);
		ItemWarehouseCommentsKey iwcb = getItemWarehouseCommentsKey(DEFAULT_COMMENT_NUMBER);
		Assert.assertEquals(iwca.hashCode(), iwcb.hashCode());
	}

	/**
	 * Tests the hash code of two different objects
	 */
	@Test
	public void hashDifferentCodeTest(){
		ItemWarehouseCommentsKey iwca = getItemWarehouseCommentsKey(DEFAULT_COMMENT_NUMBER);
		ItemWarehouseCommentsKey iwcb = getItemWarehouseCommentsKey(OTHER_COMMENT_NUMBER);
		Assert.assertNotEquals(iwca.hashCode(), iwcb.hashCode());
	}

	/**
	 * Creates a ItemWarehouseCommentsKey
	 * @return
	 */
	private ItemWarehouseCommentsKey getItemWarehouseCommentsKey(int commentNumber){
		ItemWarehouseCommentsKey key = new ItemWarehouseCommentsKey();
		key.setItemId(DEFAULT_ITEM_ID);
		key.setItemCommentNumber(commentNumber);
		key.setWarehouseNumber(DEFAULT_WAREHOUSE_NUMBER);
		key.setItemType(DEFAULT_ITEM_TYPE);
		key.setItemCommentType(DEFAULT_COMMENT_TYPE);
		key.setWarehouseType(DEFAULT_WAREHOUSE_TYPE);
		return key;
	}


}