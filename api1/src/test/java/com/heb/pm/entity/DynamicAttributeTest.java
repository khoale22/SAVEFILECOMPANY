/*
 * DynamicAttributeTest
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.entity;

import com.heb.pm.repository.DynamicAttributeRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import testSupport.LoggingSupportTestRunner;

import java.util.List;

/**
 * Test DynamicAttribute.
 *
 * @author d116773
 * @since 2.0.7
 */
@RunWith(LoggingSupportTestRunner.class)
@ContextConfiguration(locations = "classpath*:testConfig.xml")
public class DynamicAttributeTest {

	private static final int ID = 1643;
	private static final long KEY = 61345;
	private static final String KEY_TYPE = "UPC  ";
	private static final int SEQUENCE_NUMBER = 0;
	private static final int SOURCE_SYSTEM = 4;

	private static final String TEXT_VALUE = "100% Arabica Coffee with Natural and Artificial Flavors.";

	private static final long UPC = KEY;

	@Autowired
	private DynamicAttributeRepositoryTest dynamicAttributeRepository;

	// JPA Mapping

	/**
	 * Tests the JPA mapping of the key.
	 */
	@Test
	public void mapping_getKey() {
		DynamicAttributeKey key = this.getTestKey();
		DynamicAttribute attribute = this.dynamicAttributeRepository.findOne(key);
		Assert.assertEquals(key, attribute.getKey());
	}

	/**
	 * Test the JPA mapping of the text value.
	 */
	@Test
	public void mapping_textValue() {
		DynamicAttributeKey key = this.getTestKey();
		DynamicAttribute attribute = this.dynamicAttributeRepository.findOne(key);
		Assert.assertEquals(TEXT_VALUE, attribute.getTextValue());
	}

	/**
	 * Tests the JPA mapping of the selling unit.
	 */
	@Test
	public void mapping_sellingUnit() {
		DynamicAttributeKey key = this.getTestKey();
		DynamicAttribute attribute = this.dynamicAttributeRepository.findOne(key);
		Assert.assertEquals(this.getTestSellingUnit(), attribute.getSellingUnit());
	}
	// Getters

	/**
	 * Tests getKey.
	 */
	@Test
	public void getKey() {

		Assert.assertEquals(this.getTestKey(), this.getTestAttribute().getKey());
	}

	/**
	 * Tests getTextValue.
	 */
	@Test
	public void getTextValue() {
		Assert.assertEquals(TEXT_VALUE, this.getTestAttribute().getTextValue());
	}

	/**
	 * Tests getSellingUnit.
	 */
	@Test
	public void getSellingUnit() {
		Assert.assertEquals(this.getTestSellingUnit(), this.getTestAttribute().getSellingUnit());
	}

	// Equals
	/**
	 * Tests equals on the same object.
	 */
	@Test
	public void equals_sameObject() {

		DynamicAttribute a1 = this.getTestAttribute();
		boolean eq = a1.equals(a1);
		Assert.assertTrue(eq);
	}

	/**
	 * Tests equals on equal objects.
	 */
	@Test
	public void equals_equalObjects() {
		DynamicAttribute a1 = this.getTestAttribute();
		DynamicAttribute a2 = this.getTestAttribute();
		boolean eq = a1.equals(a2);
		Assert.assertTrue(eq);
	}

	/**
	 * Tests equals when the objects just have the same key.
	 */
	@Test
	public void equals_sameKey() {
		DynamicAttribute a1 = this.getTestAttribute();
		DynamicAttribute a2 = new DynamicAttribute();
		a2.setKey(this.getTestKey());
		boolean eq = a1.equals(a2);
		Assert.assertTrue(eq);
	}

	/**
	 * Tests equals when passed a null.
	 */
	@Test
	public void equals_null() {
		DynamicAttribute a1 = this.getTestAttribute();
		boolean eq = a1.equals(null);
		Assert.assertFalse(eq);
	}

	/**
	 * Tests equals when passed an object of a different type.
	 */
	@Test
	public void equals_differentObjectType() {
		DynamicAttribute a1 = this.getTestAttribute();
		boolean eq = a1.equals(this.getTestKey());
		Assert.assertFalse(eq);
	}

	// hashCode
	/**
	 * Tests that hashCode returns the hash code of the key.
	 */
	@Test
	public void hashCode_equalsKeyHashCode() {
		Assert.assertEquals(this.getTestKey().hashCode(), this.getTestAttribute().hashCode());
	}

	/**
	 * Tests toString.
	 */
	@Test
	public void test_toString() {
		Assert.assertEquals("DynamicAttribute{key=DynamicAttributeKey{attributeId=1643, key=61345, keyType='UPC  ', sequenceNumber=0, sourceSystem=4}, textValue='100% Arabica Coffee with Natural and Artificial Flavors.'}",
				this.getTestAttribute().toString());
	}

	// Support functions.

	/**
	 * Returns a DynamicAttributeKey to test with.
	 *
	 * @return A DynamicAttributeKey to test with.
	 */
	private DynamicAttributeKey getTestKey() {

		DynamicAttributeKey key = new DynamicAttributeKey();

		key.setAttributeId(ID);
		key.setKey(KEY);
		key.setKeyType(KEY_TYPE);
		key.setSequenceNumber(SEQUENCE_NUMBER);
		key.setSourceSystem(SOURCE_SYSTEM);

		return key;
	}

	/**
	 * Returns a SellingUnit to test with.
	 *
	 * @return A SellingUnit to test with.
	 */
	private SellingUnit getTestSellingUnit() {

		SellingUnit sellingUnit = new SellingUnit();
		sellingUnit.setUpc(UPC);
		return sellingUnit;
	}

	/**
	 * Returns a DynamicAttribute to test with.
	 *
	 * @return A DynamicAttribute to test with.
	 */
	private DynamicAttribute getTestAttribute() {

		DynamicAttribute dynamicAttribute =  new DynamicAttribute();

		dynamicAttribute.setKey(this.getTestKey());
		dynamicAttribute.setTextValue(TEXT_VALUE);
		dynamicAttribute.setSellingUnit(this.getTestSellingUnit());

		return dynamicAttribute;
	}
}
