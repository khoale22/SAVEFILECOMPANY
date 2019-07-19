/*
 * CommodityDocumentTest
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.productHierarchy;

import com.heb.pm.entity.ClassCommodity;
import com.heb.pm.entity.ClassCommodityKey;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests CommodityDocument.
 *
 * @author d116773
 * @since 2.0.2
 */
public class CommodityDocumentTest {

	private static final int CLASS_CODE = 23434;
	private static final int COMMODITY_CODE = 5165151;

	/**
	 * Test toKey.
	 */
	@Test
	public void toKey() {

		ClassCommodity classCommodity = this.getTestClassCommodity();
		CommodityDocument commodityDocument = new CommodityDocument();

		String s = commodityDocument.toKey(classCommodity);
		Assert.assertEquals(Integer.toString(CommodityDocumentTest.COMMODITY_CODE), s);
	}

	/**
	 * Tests toKey when passed a null.
	 */
	@Test
	public void toKeyNull() {

		CommodityDocument commodityDocument = new CommodityDocument();
		Assert.assertEquals("", commodityDocument.toKey(null));
	}

	/**
	 * Tests toKey when the ClassCommodity passed in has a null key.
	 */
	@Test
	public void toKeyNullKey() {

		CommodityDocument commodityDocument = new CommodityDocument();
		Assert.assertEquals("", commodityDocument.toKey(new ClassCommodity()));
	}

	/*
	 * Supporting functions.
	 */

	/**
	 * Returns a ClassCommodityKey to test with.
	 *
	 * @return A ClassCommodityKey to test with.
	 */
	private ClassCommodityKey getTestKey() {

		ClassCommodityKey key = new ClassCommodityKey();
		key.setClassCode(CommodityDocumentTest.CLASS_CODE);
		key.setCommodityCode(CommodityDocumentTest.COMMODITY_CODE);
		return key;
	}

	/**
	 * Returns a ClassCommodity to test with.
	 *
	 * @return A ClassCommodity to test with.
	 */
	private ClassCommodity getTestClassCommodity() {

		ClassCommodity classCommodity = new ClassCommodity();
		classCommodity.setKey(this.getTestKey());
		classCommodity.setName("description");
		return classCommodity;

	}
}
