package com.heb.pm.entity;

import org.junit.Assert;
import org.junit.Test;

/**
 * Unit Tests for the ItemWarehouseComments entity
 * @author  s753601
 * @version 2.8.0
 */
public class ItemWarehouseCommentsTest {

	private long DEFAULT_ITEM_ID=1L;
	private int DEFAULT_COMMENT_NUMBER=1;
	private int OTHER_COMMENT_NUMBER=2;
	private int DEFAULT_WAREHOUSE_NUMBER= 1;
	private String DEFAULT_ITEM_TYPE = "ITMCD";
	private String DEFAULT_COMMENT_TYPE = "REMAK";
	private String DEFAULT_WAREHOUSE_TYPE = "W";
	private String DEFAULT_REMARK="DEFAULT";
	private String OTHER_REMARK="OTHER";


	/**
	 * Tests the getKey method
	 */
	@Test
	public void getKeyTest() {
		Assert.assertEquals(getItemWarehouseCommentsKey(DEFAULT_COMMENT_NUMBER), getItemWarehouseComments(DEFAULT_REMARK, DEFAULT_COMMENT_NUMBER).getKey());
	}

	/**
	 * Tests the setKey method
	 */
	@Test
	public void setKeyTest() {
		ItemWarehouseComments iwc= getItemWarehouseComments(DEFAULT_REMARK, DEFAULT_COMMENT_NUMBER);
		iwc.setKey(getItemWarehouseCommentsKey(OTHER_COMMENT_NUMBER));
		Assert.assertEquals(getItemWarehouseCommentsKey(OTHER_COMMENT_NUMBER), iwc.getKey());
	}

	/**
	 * Tests the getItemCommentContents method
	 */
	@Test
	public void getItemCommentContentsTest() {
		Assert.assertEquals(DEFAULT_REMARK, getItemWarehouseComments(DEFAULT_REMARK, DEFAULT_COMMENT_NUMBER).getItemCommentContents());
	}

	/**
	 * Tests the setItemCommentContent mthod
	 */
	@Test
	public void setItemCommentContentsTest() {
		ItemWarehouseComments iwc= getItemWarehouseComments(DEFAULT_REMARK, DEFAULT_COMMENT_NUMBER);
		iwc.setItemCommentContents(OTHER_REMARK);
		Assert.assertEquals(OTHER_REMARK, iwc.getItemCommentContents());
	}

	/**
	 * Tests the toString method
	 */
	@Test
	public void toStringTest(){
		ItemWarehouseComments iwca = getItemWarehouseComments(DEFAULT_REMARK, DEFAULT_COMMENT_NUMBER);
		Assert.assertEquals("WarehouseLocationItem{key=WarehouseLocationItemKey{itemType='ITMCD', itemCode='1', " +
				"warehouseType='W', warehouseNumber='1', itemCommentType='REMAK', itemCommentNumber='1'}, " +
				"itemCommentContents='DEFAULT'}", iwca.toString());
	}

	/**
	 * Tests the equals method of the same object
	 */
	@Test
	public void equalsTest(){
		ItemWarehouseComments iwca = getItemWarehouseComments(DEFAULT_REMARK, DEFAULT_COMMENT_NUMBER);
		ItemWarehouseComments iwcb = getItemWarehouseComments(DEFAULT_REMARK, DEFAULT_COMMENT_NUMBER);
		Assert.assertTrue(iwca.equals(iwcb));
	}

	/**
	 * Tests the equal method of two different objects
	 */
	@Test
	public void notEqualsTest(){
		ItemWarehouseComments iwca = getItemWarehouseComments(DEFAULT_REMARK, DEFAULT_COMMENT_NUMBER);
		ItemWarehouseComments iwcb = getItemWarehouseComments(OTHER_REMARK, OTHER_COMMENT_NUMBER);
		Assert.assertFalse(iwca.equals(iwcb));
	}

	/**
	 * Tests the hash code of the same object
	 */
	@Test
	public void hashSameCodeTest(){
		ItemWarehouseComments iwca = getItemWarehouseComments(DEFAULT_REMARK, DEFAULT_COMMENT_NUMBER);
		ItemWarehouseComments iwcb = getItemWarehouseComments(DEFAULT_REMARK, DEFAULT_COMMENT_NUMBER);
		Assert.assertEquals(iwca.hashCode(), iwcb.hashCode());
	}

	/**
	 * Tests the hash code of two different objects
	 */
	@Test
	public void hashDifferentCodeTest(){
		ItemWarehouseComments iwca = getItemWarehouseComments(DEFAULT_REMARK, DEFAULT_COMMENT_NUMBER);
		ItemWarehouseComments iwcb = getItemWarehouseComments(OTHER_REMARK, OTHER_COMMENT_NUMBER);
		Assert.assertNotEquals(iwca.hashCode(), iwcb.hashCode());
	}

	/**
	 * Creates a ItemWarehouseComments
	 * @return
	 */
	private ItemWarehouseComments getItemWarehouseComments(String comment, int commentNumber){
		ItemWarehouseComments iwc = new ItemWarehouseComments();
		ItemWarehouseCommentsKey key = getItemWarehouseCommentsKey(commentNumber);
		iwc.setKey(key);
		iwc.setItemCommentContents(comment);
		return iwc;
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