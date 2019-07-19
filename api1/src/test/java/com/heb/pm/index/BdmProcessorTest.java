/*
 * BdmProcessorTest
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.index;

import com.heb.pm.entity.Bdm;
import com.heb.pm.productHierarchy.BdmDocument;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests BdmProcessor.
 *
 * @author m314029
 * @since 2.0.6
 */
public class BdmProcessorTest {

	private static final String BDM_CODE = "A1   ";
	private static final String FIRST_NAME = "KAREN               ";
	private static final String LAST_NAME = "CASSADY             ";
	private static final String FULL_NAME = "KAREN CASSADY                 ";

	/*
	 * process()
	 */

	/**
	 * Tests the call to process.
	 */
	@Test
	public void process() {

		BdmProcessor processor = new BdmProcessor();
		try {
			BdmDocument document = processor.process(this.getTestBdm());

			Assert.assertEquals(BDM_CODE,
					document.getData().getBdmCode());
			Assert.assertEquals(FIRST_NAME,
					document.getData().getFirstName());
			Assert.assertEquals(LAST_NAME,
					document.getData().getLastName());
			Assert.assertEquals(FULL_NAME, document.getData().getFullName());
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		}
	}

	/*
	 * Support functions
	 */

	/**
	 * Returns a Bdm to test with.
	 *
	 * @return A Bdm to test with.
	 */
	private Bdm getTestBdm() {

		Bdm bdm = new Bdm();
		bdm.setBdmCode(BDM_CODE);
		bdm.setFirstName(FIRST_NAME);
		bdm.setLastName(LAST_NAME);
		bdm.setFullName(FULL_NAME);
		return bdm;
	}
}
