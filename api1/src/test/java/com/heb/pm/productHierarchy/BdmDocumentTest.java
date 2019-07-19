/*
 * BdmDocumentTest
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.productHierarchy;

import com.heb.pm.entity.Bdm;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests BdmDocument.
 *
 * @author m314029
 * @since 2.0.6
 */
public class BdmDocumentTest {

	private static final String BDM_CODE = "A1   ";
	private static final String FIRST_NAME = "KAREN               ";
	private static final String LAST_NAME = "CASSADY             ";
	private static final String FULL_NAME = "KAREN CASSADY                 ";

	/*
	 * toKey
	 */

	/**
	 * Tests toKey.
	 */
	@Test
	public void toKey() {
		Bdm bdm = this.getTestBdm();
		BdmDocument bdmDocument = new BdmDocument();

		String s = bdmDocument.toKey(bdm);
		Assert.assertEquals(BDM_CODE, s);
	}

	/**
	 * Tests toKey when passed a null.
	 */
	@Test
	public void toKeyNull() {
		BdmDocument bdmDocument = new BdmDocument();
		Assert.assertEquals("", bdmDocument.toKey(null));
	}

	/**
	 * Test toKey when passed a Bdm with a null key.
	 */
	@Test
	public void toKeyNullKey() {
		BdmDocument bdmDocument = new BdmDocument();
		Assert.assertEquals("0", bdmDocument.toKey(new Bdm()));
	}


	/*
	 * Constructor.
	 */

	@Test
	public void constuctor() {
		Bdm bdm = this.getTestBdm();
		BdmDocument bdmDocument = new BdmDocument(bdm);
		Assert.assertEquals(BDM_CODE,
				bdmDocument.getData().getBdmCode());
		Assert.assertEquals(FIRST_NAME,
				bdmDocument.getData().getFirstName());
		Assert.assertEquals(LAST_NAME,
				bdmDocument.getData().getLastName());
		Assert.assertEquals(FULL_NAME, bdmDocument.getData().getFullName());
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
