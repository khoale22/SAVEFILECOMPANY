/*
 * ClassCommodityTest
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.entity;


import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import testSupport.LoggingSupportTestRunner;

/**
 * Tests ClassCommodity.
 *
 * @author d116773
 * @since 2.0.2
 */
@RunWith(LoggingSupportTestRunner.class)
@ContextConfiguration(locations = "classpath*:testConfig.xml")
public class ClassCommodityTest {

	private static final int CLASS_CODE = 14;
	private static final int COMMODITY_CODE = 8238;
	private static final String COMMODITY_DESCRIPTION = "NATURAL PORK MISC.            ";
	private static final String BDM_CODE = "A1   ";

	@Autowired
	private ClassCommodityRepositoryTest testRepository;

	/*
	 * equals
	 */

	/**
	 * Tests equals on the same object.
	 */
	@Test
	public void equalsSameObject() {
		ClassCommodity classCommodity = this.getTestClassCommodity();
		boolean eq = classCommodity.equals(classCommodity);
		Assert.assertTrue(eq);
	}

	/**
	 * Tests equals when the objects are equal.
	 */
	@Test
	public void equalsEqualObjects() {
		ClassCommodity classCommodity1 = this.getTestClassCommodity();
		ClassCommodity classCommodity2 = this.getTestClassCommodity();
		boolean eq = classCommodity1.equals(classCommodity2);
		Assert.assertTrue(eq);
	}

	/**
	 * Tests equals when they objects have the same key but different descriptions.
	 */
	@Test
	public void equalsSameKey() {
		ClassCommodity classCommodity1 = this.getTestClassCommodity();
		ClassCommodity classCommodity2 = new ClassCommodity();
		classCommodity2.setKey(this.getTestKey());
		boolean eq = classCommodity1.equals(classCommodity2);
		Assert.assertTrue(eq);
	}

	/**
	 * Tests equals when they have different keys.
	 */
	@Test
	public void equalsDifferentKey() {
		ClassCommodity classCommodity1 = this.getTestClassCommodity();
		ClassCommodity classCommodity2 = this.getTestClassCommodity();
		classCommodity2.getKey().setClassCode(classCommodity2.getKey().getClassCode() + 1);
		boolean eq = classCommodity1.equals(classCommodity2);
		Assert.assertFalse(eq);
	}

	/**
	 * Tests equals when they have the same key but different descriptions.
	 */
	@Test
	public void equalsDifferentDescription() {
		ClassCommodity classCommodity1 = this.getTestClassCommodity();
		ClassCommodity classCommodity2 = this.getTestClassCommodity();
		classCommodity2.setName("other description");
		boolean eq = classCommodity1.equals(classCommodity2);
		Assert.assertTrue(eq);
	}

	/**
	 * Tests equals when the left object has null for a key.
	 */
	@Test
	public void equalsNullLeftKey() {
		ClassCommodity classCommodity1 = new ClassCommodity();
		ClassCommodity classCommodity2 = this.getTestClassCommodity();
		boolean eq = classCommodity1.equals(classCommodity2);
		Assert.assertFalse(eq);
	}

	/**
	 * Tests equals when the right object has null for a key.
	 */
	@Test
	public void equalsNullRightKey() {
		ClassCommodity classCommodity1 = this.getTestClassCommodity();
		ClassCommodity classCommodity2 = new ClassCommodity();
		boolean eq = classCommodity1.equals(classCommodity2);
		Assert.assertFalse(eq);
	}

	/**
	 * Tests equals when both keys are null.
	 */
	@Test
	public void equalsBothKeysNull() {
		ClassCommodity classCommodity1 = new ClassCommodity();
		ClassCommodity classCommodity2 = new ClassCommodity();
		boolean eq = classCommodity1.equals(classCommodity2);
		Assert.assertTrue(eq);
	}

	/*
	 * hashCode
	 */

	/**
	 * Tests hashCode when the key is null.
	 */
	@Test
	public void hashCodeNullKey() {
		ClassCommodity classCommodity = new ClassCommodity();
		Assert.assertEquals(0, classCommodity.hashCode());
	}

	/**
	 * Tests hashCode when they key has a value.
	 */
	@Test
	public void hashCodeKey() {
		Assert.assertEquals(this.getTestKey().hashCode(), this.getTestClassCommodity().hashCode());
	}

	/*
	 * toString
	 */

	/**
	 * Tests toString.
	 */
	@Test
	public void testToString() {
		ClassCommodity  classCommodity = this.getTestClassCommodity();
		Assert.assertEquals("ClassCommodity{key=ClassCommodityKey{classCode=14, commodityCode=8238}, name='NATURAL PORK MISC.            ', bdmCode='A1   '}",
				classCommodity.toString());
	}

	/*
	 * isClass
	 */

	/**
	 * Tests isClass when the object is a Class.
	 */
	@Test
	public void isClassClass() {

		ClassCommodityKey key = new ClassCommodityKey();
		key.setClassCode(12);
		key.setCommodityCode(0);

		ClassCommodity cc = new ClassCommodity();
		cc.setKey(key);
		Assert.assertTrue(cc.isClass());
	}

	/**
	 * Tests isClass when the object is a Commodity.
	 */
	@Test
	public void isClassCommodity() {

		ClassCommodityKey key = new ClassCommodityKey();
		key.setClassCode(12);
		key.setCommodityCode(5);

		ClassCommodity cc = new ClassCommodity();
		cc.setKey(key);
		Assert.assertFalse(cc.isClass());
	}

	/*
	 * isCommodity
	 */

	/**
	 * Tests isCommodity when the object is a Class.
	 */
	@Test
	public void isCommodityClass() {

		ClassCommodityKey key = new ClassCommodityKey();
		key.setClassCode(12);
		key.setCommodityCode(0);

		ClassCommodity cc = new ClassCommodity();
		cc.setKey(key);
		Assert.assertFalse(cc.isCommodity());
	}

	/**
	 * Tests isCommodity when the object is a Commodity.
	 */
	@Test
	public void isCommodityCommodity() {

		ClassCommodityKey key = new ClassCommodityKey();
		key.setClassCode(12);
		key.setCommodityCode(5);

		ClassCommodity cc = new ClassCommodity();
		cc.setKey(key);
		Assert.assertTrue(cc.isCommodity());
	}

	/*
	 * mapping
	 */

	/**
	 * Tests the JPA mapping of the key.
	 */
	@Test
	public void mappingKey() {

		ClassCommodity classCommodity = this.testRepository.findOne(this.getTestKey());
		Assert.assertEquals(this.getTestKey(), classCommodity.getKey());
	}

	/**
	 * Tests the JPA mapping of the description.
	 */
	@Test
	public void mappingDescription() {

		ClassCommodity classCommodity = this.testRepository.findOne(this.getTestKey());
		Assert.assertEquals(ClassCommodityTest.COMMODITY_DESCRIPTION,  classCommodity.getName());
	}

	/*
	 * getters
	 */

	/**
	 * Tests getKey.
	 */
	@Test
	public void getKey() {
		ClassCommodity classCommodity = this.getTestClassCommodity();
		Assert.assertEquals(this.getTestKey(), classCommodity.getKey());
	}

	/**
	 * Tests getName.
	 */
	@Test
	public void getDescription() {
		ClassCommodity classCommodity = this.getTestClassCommodity();
		Assert.assertEquals(ClassCommodityTest.COMMODITY_DESCRIPTION, classCommodity.getName());
	}

	/**
	 * Tests getDisplayName when the object is a class.
	 */
	@Test
	public void getDisplayNameClass() {
		ClassCommodityKey key = new ClassCommodityKey();
		key.setClassCode(12);
		key.setCommodityCode(0);

		ClassCommodity cc = new ClassCommodity();
		cc.setKey(key);
		cc.setName(ClassCommodityTest.COMMODITY_DESCRIPTION);

		String displayName = ClassCommodityTest.COMMODITY_DESCRIPTION.trim() + "[12]";
		Assert.assertEquals(displayName, cc.getDisplayName());
	}

	/**
	 * Tests getDisplayName when the object is a commodity.
	 */
	@Test
	public void getDisplayNameCommodity() {
		ClassCommodityKey key = new ClassCommodityKey();
		key.setClassCode(12);
		key.setCommodityCode(15);

		ClassCommodity cc = new ClassCommodity();
		cc.setKey(key);
		cc.setName(ClassCommodityTest.COMMODITY_DESCRIPTION);

		String displayName = ClassCommodityTest.COMMODITY_DESCRIPTION.trim() + "[15]";
		Assert.assertEquals(displayName, cc.getDisplayName());
	}

	/**
	 * Tests getNormalizedId when the object is a commodity.
	 */
	@Test
	public void getNormalizedIdCommodity() {
		ClassCommodityKey key = new ClassCommodityKey();
		key.setClassCode(12);
		key.setCommodityCode(15);

		ClassCommodity cc = new ClassCommodity();
		cc.setKey(key);
		cc.setName(ClassCommodityTest.COMMODITY_DESCRIPTION);

		Assert.assertEquals(15, (int) cc.getNormalizedId());
	}

	/**
	 * Tests getNormalizedId when the object is a class.
	 */
	@Test
	public void getNormalizedIdClass() {
		ClassCommodityKey key = new ClassCommodityKey();
		key.setClassCode(12);
		key.setCommodityCode(0);

		ClassCommodity cc = new ClassCommodity();
		cc.setKey(key);
		cc.setName(ClassCommodityTest.COMMODITY_DESCRIPTION);

		Assert.assertEquals(12, (int) cc.getNormalizedId());
	}

	/*
	 * Support functions
	 */

	/**
	 * Returns a key to test with.
	 *
	 * @return A key to test with.
	 */
	private ClassCommodityKey getTestKey() {

		ClassCommodityKey key = new ClassCommodityKey();
		key.setClassCode(ClassCommodityTest.CLASS_CODE);
		key.setCommodityCode(ClassCommodityTest.COMMODITY_CODE);
		return key;
	}

	/**
	 * Returns a ClassCommodity to test with.
	 *
	 * @return A ClassCommodity to test with.
	 */
	private ClassCommodity getTestClassCommodity() {

		ClassCommodity classCommodity = new ClassCommodity();
		classCommodity.setKey(this.getTestKey());
		classCommodity.setName(ClassCommodityTest.COMMODITY_DESCRIPTION);
		classCommodity.setBdmCode(ClassCommodityTest.BDM_CODE);

		return classCommodity;
	}
}
