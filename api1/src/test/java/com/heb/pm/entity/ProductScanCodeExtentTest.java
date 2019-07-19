package com.heb.pm.entity;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import testSupport.LoggingSupportTestRunner;

import static org.junit.Assert.assertNotNull;

/**
 * Tests ProductScanCodeExtent.
 *
 * @author m594201
 * @since 2.7.0
 */
@RunWith(LoggingSupportTestRunner.class)
@ContextConfiguration(locations = "classpath*:testConfig.xml")
public class ProductScanCodeExtentTest {

	private static final Long TEST_SCAN_CD_ID = 4122074262L;
	private static final String TEST_DESC = "DEPTH";
	private static final String TEST_PROD_DES = "2.9";

	/**
	 * Test product scan code extent.
	 */
	@Test
	public void getProductScanCodeExtent(){
		ProductScanCodeExtent productScanCodeExtent;
		productScanCodeExtent = this.getTestProductScanCodeExtent();
		Assert.assertNotNull(productScanCodeExtent);
	}

	/**
	 * Test product scan code extent key.
	 */
	@Test
	public void getProductScanCodeExtentKey(){
		assertNotNull(this.getProductScanCodeExtentKeyTest());
	}

	/**
	 * Test product description text.
	 */
	@Test
	public void getProductDescriptionText(){
		assertNotNull(ProductScanCodeExtentTest.TEST_DESC, this.getTestProductScanCodeExtent().getProdDescriptionText());
	}

	private ProductScanCodeExtent getTestProductScanCodeExtent(){
		ProductScanCodeExtent productScanCodeExtent = new ProductScanCodeExtent();
		productScanCodeExtent.setKey(this.getProductScanCodeExtentKeyTest());
		productScanCodeExtent.setProdDescriptionText(TEST_PROD_DES);

		return productScanCodeExtent;
	}

	private ProductScanCodeExtentKey getProductScanCodeExtentKeyTest(){
		ProductScanCodeExtentKey key = new ProductScanCodeExtentKey();
		key.setProdExtDataCode(TEST_DESC);
		key.setScanCodeId(TEST_SCAN_CD_ID);

		return key;
	}
}
