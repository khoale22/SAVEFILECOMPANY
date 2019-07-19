package com.heb.pm.entity;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import testSupport.LoggingSupportTestRunner;

import static org.junit.Assert.assertEquals;

/**
 * Tests ProductCubiscan key.
 *
 * @author m594201
 * @since 2.7.0
 */
@RunWith(LoggingSupportTestRunner.class)
@ContextConfiguration(locations = "classpath*:testConfig.xml")
public class ProductCubiscanKeyTest {

	private static final Long TEST_ID = 127127L;
	private static final String TEST_DIM = "RHISP";
	private static final Long TEST_SEQ =  1L;

	@Test
	public void getProductCubiscanKeyTest(){
		ProductCubiscanKey key;
		key = this.getProductCubiscanKey();
		Assert.assertNotNull(key);
	}

	@Test
	public void getProdId(){
		assertEquals(ProductCubiscanKeyTest.TEST_ID, this.getProductCubiscanKey().getProdId(), 0);
	}

	@Test
	public void getDimTypCd(){
		assertEquals(ProductCubiscanKeyTest.TEST_DIM, this.getProductCubiscanKey().getDimTypCd());
	}

	@Test
	public void getSeqNumber(){
		assertEquals(ProductCubiscanKeyTest.TEST_SEQ, this.getProductCubiscanKey().getSeqNumber());
	}

	private ProductCubiscanKey getProductCubiscanKey(){
		ProductCubiscanKey productCubiscanKey = new ProductCubiscanKey();
		productCubiscanKey.setDimTypCd(TEST_DIM);
		productCubiscanKey.setProdId(TEST_ID);
		productCubiscanKey.setSeqNumber(TEST_SEQ);

		return productCubiscanKey;
	}
}
