/*
 * ItemClassProcessorTest
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.index;

import com.heb.pm.entity.ItemClass;
import com.heb.pm.productHierarchy.ItemClassDocument;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests ItemClassProcessor.
 *
 * @author d116773
 * @since 2.0.2
 */
public class ItemClassProcessorTest {


	private static final int CLASS_ID = 2334;
	private static final int COMMODITY_ID = 23423;
	private static final String DESCRIPTION = "test class";

	/*
	 * process
	 */
	/**
	 * Tests process when passed a good object.
	 */
	@Test
	public void processGoodData() {
		ItemClassProcessor ItemClassProcessor = new ItemClassProcessor();

		try {

			ItemClassDocument cd = ItemClassProcessor.process(this.getTestClassCommodity());
			Assert.assertEquals(ItemClassProcessorTest.CLASS_ID, (int) cd.getData().getItemClassCode());
			Assert.assertEquals(ItemClassProcessorTest.COMMODITY_ID, (int) cd.getData().getItemClassCode());
			Assert.assertEquals(ItemClassProcessorTest.DESCRIPTION, cd.getData().getItemClassCode());
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		}
	}

	/**
	 * Tests process when passed a null.
	 */
	@Test
	public void processNull() {
		ItemClassProcessor ItemClassProcessor = new ItemClassProcessor();

		try {

			ItemClassDocument cd = ItemClassProcessor.process(null);
			Assert.assertNull(cd);
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		}
	}

	/*
	 * Support functions.
	 */

	/**
	 * Creates a ClassCommodity to test with.
	 *
	 * @return A ClassCommodity to test with.
	 */
	private ItemClass getTestClassCommodity() {

		ItemClass itemClass = new ItemClass();
		itemClass.setItemClassCode(CLASS_ID);
		itemClass.setItemClassDescription(DESCRIPTION);
		return itemClass;
	}
}
