package com.heb.pm.entity;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import testSupport.LoggingSupportTestRunner;

import java.util.Date;

import static org.junit.Assert.assertEquals;

/**
 * Tests ProductCubiscan.
 *
 * @author m594201
 * @since 2.7.0
 */
@RunWith(LoggingSupportTestRunner.class)
@ContextConfiguration(locations = "classpath*:testConfig.xml")
public class ProductCubiscanTest {

	private static final double TEST_HEIGHT = 4.3000;
	private static final double TEST_WEIGHT = 1.0500;
	private static final double TEST_LENGHT = 2.9000;
	private static final double TEST_WIDTH  = 2.9000;
	private static final String TEST_USER  = "CUBISCAN";
	private static final Date TEST_DATE = new Date();

	private static final Long TEST_ID = 127127L;
	private static final String TEST_DIM = "RHISP";
	private static final Long TEST_SEQ =  1L;


	/**
	 * Test cubiscan.
	 */
	@Test
	public void getCubiscan(){
		ProductCubiscan testCubiscan;
		testCubiscan = getTestProductCubiscan();
		Assert.assertNotNull(testCubiscan);
	}

	/**
	 * Test height.
	 */
	@Test
	public void getHeight(){
		assertEquals(ProductCubiscanTest.TEST_HEIGHT, this.getTestProductCubiscan().getRetailHeight(), 0);
	}

	/**
	 * Get weight.
	 */
	@Test
	public void getWeight(){
		assertEquals(ProductCubiscanTest.TEST_WEIGHT, this.getTestProductCubiscan().getRetailWeight(), 0);
	}

	/**
	 * Get length.
	 */
	@Test
	public void getLength(){
		assertEquals(ProductCubiscanTest.TEST_LENGHT, this.getTestProductCubiscan().getRetailLength(), 0);
	}

	/**
	 * Get width.
	 */
	@Test
	public void getWidth(){
		assertEquals(ProductCubiscanTest.TEST_WIDTH, this.getTestProductCubiscan().getRetailWidth(), 0);
	}

	/**
	 * Get last update by.
	 */
	@Test
	public void getLastUpdateBy(){
		assertEquals(ProductCubiscanTest.TEST_USER, this.getTestProductCubiscan().getLastUpdatedBy());
	}

	/**
	 * Get last update time.
	 */
	@Test
	public void getLastUpdateTime(){
		assertEquals(ProductCubiscanTest.TEST_DATE, this.getTestProductCubiscan().getLastUpdatedTimestamp());
	}

	private ProductCubiscan getTestProductCubiscan() {
		ProductCubiscan productCubiscan = new ProductCubiscan();
		productCubiscan.setRetailHeight(TEST_HEIGHT);
		productCubiscan.setRetailLength(TEST_LENGHT);
		productCubiscan.setRetailWidth(TEST_WIDTH);
		productCubiscan.setRetailWeight(TEST_WEIGHT);
		productCubiscan.setLastUpdatedBy(TEST_USER);
		productCubiscan.setLastUpdatedTimestamp(TEST_DATE);
		productCubiscan.setKey(this.getProductCubiscanKey());

		return productCubiscan;
	}

	private ProductCubiscanKey getProductCubiscanKey(){
		ProductCubiscanKey productCubiscanKey = new ProductCubiscanKey();
		productCubiscanKey.setDimTypCd(TEST_DIM);
		productCubiscanKey.setProdId(TEST_ID);
		productCubiscanKey.setSeqNumber(TEST_SEQ);

		return productCubiscanKey;
	}
}
