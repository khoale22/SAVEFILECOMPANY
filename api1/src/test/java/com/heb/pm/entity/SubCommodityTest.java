package com.heb.pm.entity;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import testSupport.LoggingSupportTestRunner;

/**
 * Test SubCommodity.
 *
 * @author d116773
 * @since 2.0.2
 */
@RunWith(LoggingSupportTestRunner.class)
@ContextConfiguration(locations = "classpath*:testConfig.xml")
public class SubCommodityTest {

	private static final int CLASS_CODE = 39;
	private static final int COMMODITY_CODE = 8222;
	private static final int SUB_COMMODITY_CODE = 9551;
	private static final String SUB_COMMODITY_NAME = "LEAN MULT SRV STUFD SNDWCH    ";

	@Autowired
	private SubCommodityRepositoryTest repository;

	/*
	 * equals
	 */

	/**
	 * Tests equals when passed itself.
	 */
	@Test
	public void equalsSameObject() {
		SubCommodity subCommodity = this.getTestSubCommodity();
		boolean eq = subCommodity.equals(subCommodity);
		Assert.assertTrue(eq);
	}

	/**
	 * Tests equals on an object with all mathcing values.
	 */
	@Test
	public void equalsEqualObjects() {
		SubCommodity subCommodity1 = this.getTestSubCommodity();
		SubCommodity subCommodity2 = this.getTestSubCommodity();
		boolean eq = subCommodity1.equals(subCommodity2);
		Assert.assertTrue(eq);
	}

	/**
	 * Tests equals when only the keys match.
	 */
	@Test
	public void equalsKeysOnly() {
		SubCommodity subCommodity1 = this.getTestSubCommodity();
		SubCommodity subCommodity2 = new SubCommodity();
		subCommodity2.setKey(this.getTestKey());
		boolean eq = subCommodity1.equals(subCommodity2);
		Assert.assertTrue(eq);
	}

	/**
	 * Tests equals when the keys are different.
	 */
	@Test
	public void equalsDifferentKey() {
		SubCommodity subCommodity1 = this.getTestSubCommodity();
		SubCommodity subCommodity2 = this.getTestSubCommodity();
		subCommodity2.getKey().setClassCode(subCommodity2.getKey().getClassCode() + 1);
		boolean eq = subCommodity1.equals(subCommodity2);
		Assert.assertFalse(eq);
	}

	/**
	 * Tests equals when passed a null.
	 */
	@Test
	public void equalsNull() {
		SubCommodity subCommodity = this.getTestSubCommodity();
		boolean eq = subCommodity.equals(null);
		Assert.assertFalse(eq);
	}

	/**
	 * Tests equals when passed an object of a different type.
	 */
	@Test
	public void equalsDifferentType() {
		SubCommodity subCommodity = this.getTestSubCommodity();
		boolean eq = subCommodity.equals(this.getTestKey());
		Assert.assertFalse(eq);
	}

	/*
	 * hashCode
	 */

	/**
	 * Makes sure hashCode equals the key's hashCode.
	 */
	@Test
	public void hashCodeEqualsKey() {
		Assert.assertEquals(this.getTestKey().hashCode(), this.getTestSubCommodity().hashCode());
	}

	/*
	 * getters
	 */

	/**
	 * Tests getKey.
	 */
	@Test
	public void getKey() {
		SubCommodity subCommodity = this.getTestSubCommodity();
		Assert.assertEquals(this.getTestKey(), subCommodity.getKey());
	}

	/**
	 * Tests getName.
	 */
	@Test
	public void getName() {
		SubCommodity subCommodity = this.getTestSubCommodity();
		Assert.assertEquals(SubCommodityTest.SUB_COMMODITY_NAME, subCommodity.getName());
	}

	/**
	 * Tests getDisplayName.
	 */
	@Test
	public void getDisplayName() {
		SubCommodity subCommodity = this.getTestSubCommodity();
		String displayName = subCommodity.getName().trim() + "[" + subCommodity.getKey().getSubCommodityCode() + "]";
		Assert.assertEquals(displayName, subCommodity.getDisplayName());
	}

	/**
	 * Tests getNormalizedId.
	 */
	@Test
	public void getNormalizedId() {
		SubCommodity subCommodity = this.getTestSubCommodity();
		Assert.assertEquals(SubCommodityTest.SUB_COMMODITY_CODE, subCommodity.getNormalizedId());
	}

	/*
	 * mapping
	 */
	@Test
	public void jpaMapping() {
		SubCommodityKey key = this.getTestKey();
		SubCommodity subCommodity = this.repository.findOne(key);
		Assert.assertEquals(SubCommodityTest.CLASS_CODE, (int) subCommodity.getKey().getClassCode());
		Assert.assertEquals(SubCommodityTest.COMMODITY_CODE, (int) subCommodity.getKey().getCommodityCode());
		Assert.assertEquals(SubCommodityTest.SUB_COMMODITY_CODE, (int) subCommodity.getKey().getSubCommodityCode());
		Assert.assertEquals(SubCommodityTest.SUB_COMMODITY_NAME, subCommodity.getName());
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
		key.setClassCode(SubCommodityTest.CLASS_CODE);
		key.setCommodityCode(SubCommodityTest.COMMODITY_CODE);
		key.setSubCommodityCode(SubCommodityTest.SUB_COMMODITY_CODE);
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
		subCommodity.setName(SubCommodityTest.SUB_COMMODITY_NAME);
		return subCommodity;
	}
}
