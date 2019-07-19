/*
 * CommodityProcessorTest
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.index;

import com.heb.pm.entity.ClassCommodity;
import com.heb.pm.entity.ClassCommodityKey;
import com.heb.pm.productHierarchy.CommodityDocument;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests CommodityProcessor.
 *
 * @author d116773
 * @since 2.0.2
 */
public class CommodityProcessorTest {


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
		CommodityProcessor commodityProcessor = new CommodityProcessor();

		try {

			CommodityDocument cd = commodityProcessor.process(this.getTestClassCommodity());
			Assert.assertEquals(CommodityProcessorTest.CLASS_ID, (int) cd.getData().getKey().getClassCode());
			Assert.assertEquals(CommodityProcessorTest.COMMODITY_ID, (int) cd.getData().getKey().getCommodityCode());
			Assert.assertEquals(CommodityProcessorTest.DESCRIPTION, cd.getData().getName());
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		}
	}

	/**
	 * Tests process when passed a null.
	 */
	@Test
	public void processNull() {
		CommodityProcessor commodityProcessor = new CommodityProcessor();

		try {

			CommodityDocument cd = commodityProcessor.process(null);
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
	private ClassCommodity getTestClassCommodity() {

		ClassCommodityKey key = new ClassCommodityKey();
		key.setClassCode(CommodityProcessorTest.CLASS_ID);
		key.setCommodityCode(CommodityProcessorTest.COMMODITY_ID);

		ClassCommodity cc = new ClassCommodity();
		cc.setKey(key);
		cc.setName(CommodityProcessorTest.DESCRIPTION);
		return cc;
	}
}
