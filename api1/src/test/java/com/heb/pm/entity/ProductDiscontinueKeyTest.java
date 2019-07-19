/*
 *  com.heb.pm.entity.ProductDiscontinueKeyTest
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */

package com.heb.pm.entity;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.data.domain.Sort;

/**
 * Test class for ProductDiscontinueKey
 *
 * @author d116773
 * @since 2.0.0
 */
public class ProductDiscontinueKeyTest {

	/*
	 * hashCode
	 */

	@Test
	/**
	 * Tests hashCode returns the right value;
	 */
	public void hashCodeCorrect() {
		ProductDiscontinueKey key = this.getKey();
		Assert.assertEquals("hash code incorrect", -399950867, key.hashCode());
	}

	@Test
	/**
	 * Tests two equal objects return the same hash code.
	 */
	public void hashCodeSimilarObjects() {
		ProductDiscontinueKey key1 = this.getKey();
		ProductDiscontinueKey key2 = this.getKey();

		Assert.assertEquals("similar objects different hash", key1.hashCode(), key2.hashCode());
	}

	/**
	 * Tests two different objects return different hash codes.
	 */
	@Test
	public void hashCodeDissimilarObjects() {
		ProductDiscontinueKey key1 = this.getKey();
		ProductDiscontinueKey key2 = this.getKeyNewUpc();

		Assert.assertNotEquals("dissimilar objects same hash", key1.hashCode(), key2.hashCode());
	}

	/*
	 * equals
	 */

	/**
	 * Test an object is equal to itself.
	 */
	@Test
	public void equalSameObject() {
		ProductDiscontinueKey key = this.getKey();
		boolean equal = key.equals(key);
		Assert.assertTrue("same objects not equal", equal);
	}

	/**
	 * Tests two objects with the same values equal each other.
	 */
	@Test
	public void equalsSimilarObject() {
		ProductDiscontinueKey key1 = this.getKey();
		ProductDiscontinueKey key2 = this.getKey();
		boolean equal = key1.equals(key2);
		Assert.assertTrue("similar objects not equal", equal);
	}

	/**
	 * Tests two objects with different UPCs are not equal.
	 */
	@Test
	public void testEqualsDifferentUpc() {
		ProductDiscontinueKey key1 = this.getKey();
		ProductDiscontinueKey key2 = this.getKeyNewUpc();
		boolean equal = key1.equals(key2);
		Assert.assertFalse("unequal upc objects equal", equal);
	}

	/**
	 * Tests two objects with different prod IDs are not equal.
	 */
	@Test
	public void equalsDifferentProdId() {
		ProductDiscontinueKey key1 = this.getKey();
		ProductDiscontinueKey key2 = this.getKeyNewProdId();
		boolean equal = key1.equals(key2);
		Assert.assertFalse("unequal prod_id objects equal", equal);
	}

	/**
	 * Tests two objects with different item codes are not equal.
	 */
	@Test
	public void equalsDifferentItemCode() {
		ProductDiscontinueKey key1 = this.getKey();
		ProductDiscontinueKey key2 = this.getKeyNewItemCode();
		boolean equal = key1.equals(key2);
		Assert.assertFalse("unequal item code objects equal", equal);
	}

	/**
	 * Tests two objects with different item types are not eqaul.
	 */
	@Test
	public void equalsDifferentItemType() {
		ProductDiscontinueKey key1 = this.getKey();
		ProductDiscontinueKey key2 = this.getKeyNewItemType();
		boolean equal = key1.equals(key2);
		Assert.assertFalse("unequal item type objects equal", equal);
	}

	/**
	 * Tests ano bject is not equal to null.
	 */
	@Test
	public void equalsNull() {
		ProductDiscontinueKey key1 = this.getKey();
		boolean equal = key1.equals(null);
		Assert.assertFalse("null objects equal", equal);
	}

	/**
	 * Tests a key is not equal to a different object type.
	 */
	@Test
	public void equalsDifferentObject() {
		ProductDiscontinueKey key1 = this.getKey();
		boolean equal = key1.equals(Integer.valueOf(0));
		Assert.assertFalse("different objects equal", equal);
	}

	/*
	 * getSort functions
	 */

	/**
	 * Tests the default sort.
	 */
	@Test
	public void getDefaultSort() {
		Assert.assertEquals("default sort incorrect", this.getSortByUpc(Sort.Direction.ASC), ProductDiscontinueKey.getDefaultSort());
	}

	/**
	 * Tests getSortByUpc ascending.
	 */
	@Test
	public void getSortByUpcAscending() {
		Assert.assertEquals("sort by upc ascending wrong", this.getSortByUpc(Sort.Direction.ASC),
				ProductDiscontinueKey.getSortByUpc(Sort.Direction.ASC));
	}

	/**
	 * Tests getSortByUpc descending.
	 */
	@Test
	public void getSortByUpcDescending() {
		Assert.assertEquals("sort by upc descending wrong", this.getSortByUpc(Sort.Direction.DESC),
				ProductDiscontinueKey.getSortByUpc(Sort.Direction.DESC));
	}

	/**
	 * Test getSortByItemCode ascending
	 */
	@Test
	public void getSortByItemCodeAscending() {
		Assert.assertEquals("sort by item code ascending wrong", this.getSortByItemCode(Sort.Direction.ASC),
				ProductDiscontinueKey.getSortByItemCode(Sort.Direction.ASC));
	}

	/**
	 * Tests getSortByItemCode descending.
	 */
	@Test
	public void getSortByItemCodeDescending() {
		Assert.assertEquals("sort by item code descending wrong", this.getSortByItemCode(Sort.Direction.DESC),
				ProductDiscontinueKey.getSortByItemCode(Sort.Direction.DESC));
	}

	/*
	 * Getters
	 */

	/**
	 * Tests getUpc.
	 */
	@Test
	public void getUpc() {
		ProductDiscontinueKey key = this.getKey();
		Assert.assertEquals("upc incorrect", 4775412479L, key.getUpc());
	}

	/**
	 * Tests getProductId.
	 */
	@Test
	public void getProductId() {
		ProductDiscontinueKey key = this.getKey();
		Assert.assertEquals("product ID incorrect", 1783158, key.getProductId());
	}

	/**
	 * Tests getItemCode.
	 */
	@Test
	public void getItemCode() {
		ProductDiscontinueKey key = this.getKey();
		Assert.assertEquals("upc incorrect", 622, key.getItemCode());
	}

	/**
	 * Tests getItemType.
	 */
	@Test
	public void getItemType() {
		ProductDiscontinueKey key = this.getKey();
		Assert.assertEquals("upc incorrect", "ITMCD", key.getItemType());
	}

	/*
	 * Support functions
	 */

	/**
	 * Returns a sort based on UPC.
	 *
	 * @param direction The direction to sort in.
	 * @return A sort based on UPC.
	 */
	private Sort getSortByUpc(Sort.Direction direction) {
		return new Sort(
				new Sort.Order(direction, "key.upc"),
				new Sort.Order(direction, "key.productId"),
				new Sort.Order(direction, "key.itemCode"),
				new Sort.Order(direction, "key.itemType")
		);
	}

	/**
	 * Returns a sort based on item code.
	 *
	 * @param direction The direction to sort in.
	 * @return A sort based on item code.
	 */
	public Sort getSortByItemCode(Sort.Direction direction) {
		return new Sort(
				new Sort.Order(direction, "key.itemCode"),
				new Sort.Order(direction, "key.upc"),
				new Sort.Order(direction, "key.productId"),
				new Sort.Order(direction, "key.itemType")
		);
	}

	/**
	 * Returns the default test key.
	 *
	 * @return The default test key.
	 */
	private ProductDiscontinueKey getKey() {
		ProductDiscontinueKey key = new ProductDiscontinueKey();
		key.setUpc(4775412479L);
		key.setProductId(1783158);
		key.setItemCode(622);
		key.setItemType("ITMCD");
		return key;
	}

	/**
	 * Returns a test key with a different UPC than the default.
	 *
	 * @return A test key with a different UPC than the default.
	 */
	private ProductDiscontinueKey getKeyNewUpc() {
		ProductDiscontinueKey key = this.getKey();
		key.setUpc(47754124790L);
		return key;
	}


	/**
	 * Returns a test key with a different prod ID than the default.
	 *
	 * @return At est key with a different prod ID than the default.
	 */
	private ProductDiscontinueKey getKeyNewProdId() {
		ProductDiscontinueKey key = this.getKey();
		key.setProductId(1783157);
		return key;
	}


	/**
	 * Returns a test key with a different item code than the default.
	 *
	 * @return At est key with a different item code than the default.
	 */
	private ProductDiscontinueKey getKeyNewItemCode() {
		ProductDiscontinueKey key = this.getKey();
		key.setItemCode(623);
		return key;
	}


	/**
	 * Returns a test key with a different item type than the default.
	 *
	 * @return At est key with a different item type than the default.
	 */
	private ProductDiscontinueKey getKeyNewItemType() {
		ProductDiscontinueKey key = this.getKey();
		key.setItemType("DSD  ");
		return key;
	}
}
