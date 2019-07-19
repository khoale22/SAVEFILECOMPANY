/*
 * ItemClassDocumentTest
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.productHierarchy;

import com.heb.pm.entity.ItemClass;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests ItemClassDocument.
 *
 * @author d116773
 * @since 2.0.2
 */
public class ItemClassDocumentTest {

	private static final int CLASS_CODE = 23434;
	private static final String DESCRIPTION = "test class";

	/**
	 * Test toKey.
	 */
	@Test
	public void toKey() {
		ItemClass itemClass = this.getTestItemClass();
		ItemClassDocument itemClassDocument = new ItemClassDocument();

		String s = itemClassDocument.toKey(itemClass);
		Assert.assertEquals(Integer.toString(ItemClassDocumentTest.CLASS_CODE), s);
	}

	/**
	 * Tests toKey when passed a null.
	 */
	@Test
	public void toKeyNull() {
		ItemClassDocument itemClassDocument = new ItemClassDocument();
		Assert.assertEquals("", itemClassDocument.toKey(null));
	}

	/**
	 * Tests toKey when the ItemClass passed in has a null key.
	 */
	@Test
	public void toKeyNullKey() {
		ItemClassDocument itemClassDocument = new ItemClassDocument();
		Assert.assertEquals("", itemClassDocument.toKey(new ItemClass()));
	}

	/*
	 * Supporting functions.
	 */

	/**
	 * Returns a ItemClass to test with.
	 *
	 * @return A ItemClass to test with.
	 */
	private ItemClass getTestItemClass() {
		ItemClass itemClass = new ItemClass();
		itemClass.setItemClassCode(ItemClassDocumentTest.CLASS_CODE);
		itemClass.setItemClassDescription(ItemClassDocumentTest.DESCRIPTION);
		return itemClass;
	}
}
