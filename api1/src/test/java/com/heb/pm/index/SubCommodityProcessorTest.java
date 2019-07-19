package com.heb.pm.index;

import com.heb.pm.entity.SubCommodity;
import com.heb.pm.entity.SubCommodityKey;
import com.heb.pm.productHierarchy.SubCommodityDocument;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests SubCommodityProcessor.
 *
 * @author d116773
 * @since 2.0.2
 */
public class SubCommodityProcessorTest {

	private static final int CLASS_CODE = 39;
	private static final int COMMODITY_CODE = 8222;
	private static final int SUB_COMMODITY_CODE = 9551;
	private static final String SUB_COMMODITY_NAME = "LEAN MULT SRV STUFD SNDWCH    ";

	/**
	 * Tests the call to process.
	 */
	@Test
	public void process() {

		SubCommodityProcessor processor = new SubCommodityProcessor();
		try {
			SubCommodityDocument document = processor.process(this.getTestSubCommodity());

			Assert.assertEquals(SubCommodityProcessorTest.CLASS_CODE,
					(int) document.getData().getKey().getClassCode());
			Assert.assertEquals(SubCommodityProcessorTest.COMMODITY_CODE,
					(int) document.getData().getKey().getCommodityCode());
			Assert.assertEquals(SubCommodityProcessorTest.SUB_COMMODITY_CODE,
					(int) document.getData().getKey().getSubCommodityCode());
			Assert.assertEquals(SubCommodityProcessorTest.SUB_COMMODITY_NAME, document.getData().getName());
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		}
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
		key.setClassCode(SubCommodityProcessorTest.CLASS_CODE);
		key.setCommodityCode(SubCommodityProcessorTest.COMMODITY_CODE);
		key.setSubCommodityCode(SubCommodityProcessorTest.SUB_COMMODITY_CODE);
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
		subCommodity.setName(SubCommodityProcessorTest.SUB_COMMODITY_NAME);
		return subCommodity;
	}
}
