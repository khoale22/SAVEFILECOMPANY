package com.heb.pm.productHierarchy;

import com.heb.pm.entity.SubCommodity;
import com.heb.pm.entity.SubCommodityKey;

import org.junit.Assert;
import org.junit.Test;

/**
 * Tests SubCommodityDocument.
 *
 * @author d116773
 * @since 2.0.2
 */
public class SubCommodityDocumentTest {

	private static final int CLASS_CODE = 39;
	private static final int COMMODITY_CODE = 8222;
	private static final int SUB_COMMODITY_CODE = 9551;
	private static final String SUB_COMMODITY_NAME = "LEAN MULT SRV STUFD SNDWCH    ";

	/*
	 * toKey
	 */

	/**
	 * Tests toKey.
	 */
	@Test
	public void toKey() {
		SubCommodity subCommodity = this.getTestSubCommodity();
		SubCommodityDocument subCommodityDocument = new SubCommodityDocument();

		String s = subCommodityDocument.toKey(subCommodity);
		Assert.assertEquals(Integer.toString(SubCommodityDocumentTest.SUB_COMMODITY_CODE), s);
	}

	/**
	 * Tests toKey when passed a null.
	 */
	@Test
	public void toKeyNull() {
		SubCommodityDocument subCommodityDocument = new SubCommodityDocument();
		Assert.assertEquals("", subCommodityDocument.toKey(null));
	}

	/**
	 * Test toKey when passed a SubCommodity with a null key.
	 */
	@Test
	public void toKeyNullKey() {
		SubCommodityDocument subCommodityDocument = new SubCommodityDocument();
		Assert.assertEquals("0", subCommodityDocument.toKey(new SubCommodity()));
	}

	/*
	 * Constructor.
	 */
	@Test
	public void constuctor() {
		SubCommodity subCommodity = this.getTestSubCommodity();
		SubCommodityDocument subCommodityDocument = new SubCommodityDocument(subCommodity);
		Assert.assertEquals(SubCommodityDocumentTest.CLASS_CODE,
				(int) subCommodityDocument.getData().getKey().getClassCode());
		Assert.assertEquals(SubCommodityDocumentTest.COMMODITY_CODE,
				(int) subCommodityDocument.getData().getKey().getCommodityCode());
		Assert.assertEquals(SubCommodityDocumentTest.SUB_COMMODITY_CODE,
				(int) subCommodityDocument.getData().getKey().getSubCommodityCode());
		Assert.assertEquals(SubCommodityDocumentTest.SUB_COMMODITY_NAME, subCommodityDocument.getData().getName());
	}

	/*
	 * Support functions
	 */

	/**
	 * Returns a key to test with.
	 *
	 * @return A key to test with.
	 */
	private SubCommodityKey getTestKey() {

		SubCommodityKey key = new SubCommodityKey();
		key.setClassCode(SubCommodityDocumentTest.CLASS_CODE);
		key.setCommodityCode(SubCommodityDocumentTest.COMMODITY_CODE);
		key.setSubCommodityCode(SubCommodityDocumentTest.SUB_COMMODITY_CODE);
		return key;
	}

	/**
	 * Returns a SubCommodity to test with.
	 *
	 * @return A SubCommodity to test with.
	 */
	private SubCommodity getTestSubCommodity() {

		SubCommodity subCommodity = new SubCommodity();
		subCommodity.setKey(this.getTestKey());
		subCommodity.setName(SubCommodityDocumentTest.SUB_COMMODITY_NAME);
		return subCommodity;
	}
}
