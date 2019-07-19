/*
 * AssociatedUpcTest
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
 * Tests AssociatedUpc.
 *
 * @author d116773
 * @since 2.0.1
 */
@RunWith(LoggingSupportTestRunner.class)
@ContextConfiguration(locations = "classpath*:testConfig.xml")
public class AssociatedUpcTest {

	private static final long TEST_ASSOCIATE = 9015932509L;
	private static final long OTHER_ASSOCIATE = 233223L;
	private static final long TEST_PRIMARY = 9015932501L;

	@Autowired
	private AssociatedUpcRepositoryTest associatedUpcRepository;

	/*
	 * JPA mapping.
	 */
	@Test
	public void mappingUpc() {
		AssociatedUpc associatedUpc = this.associatedUpcRepository.findOne(9015932509L);
		Assert.assertEquals(9015932509L, associatedUpc.getUpc());
	}


	/*
	 * getters
	 */

	/**
	 * Tests getUpc.
	 */
	@Test
	public void getUpc() {
		AssociatedUpc associatedUpc= this.getTestAssociatedUpc();
		Assert.assertEquals(AssociatedUpcTest.TEST_ASSOCIATE, associatedUpc.getUpc());
	}

	/**
	 * Tests getPrimaryUpc.
	 */
	@Test
	public void getPrimaryUpc() {
		AssociatedUpc associatedUpc = this.getTestAssociatedUpc();
		Assert.assertEquals(this.getTestPrimaryUpc(), associatedUpc.getPrimaryUpc());
	}

	/*
	 * equals
	 */

	/**
	 * Tests equals on the same object.
	 */
	@Test
	public void equalsSameObject() {
		AssociatedUpc associatedUpc = this.getTestAssociatedUpc();
		boolean equals = associatedUpc.equals(associatedUpc);
		Assert.assertTrue(equals);
	}

	/**
	 * Tests equals on two objects with the same key.
	 */
	@Test
	public void equalsSimilarObjects() {
		AssociatedUpc associatedUpc1 = this.getTestAssociatedUpc();
		AssociatedUpc associatedUpc2 = this.getTestAssociatedUpc();
		boolean equals = associatedUpc1.equals(associatedUpc2);
		Assert.assertTrue(equals);
	}

	/**
	 * Test equals with same key and different other values.
	 */
	@Test
	public void equalsSameKey() {
		AssociatedUpc associatedUpc1 = this.getTestAssociatedUpc();
		AssociatedUpc associatedUpc2 = new AssociatedUpc();
		associatedUpc2.setUpc(AssociatedUpcTest.TEST_ASSOCIATE);
		boolean equals = associatedUpc1.equals(associatedUpc2);
		Assert.assertTrue(equals);
	}

	/**
	 * Tests equals on two different objects.
	 */
	@Test
	public void equalsDifferentObject() {
		AssociatedUpc associatedUpc1 = this.getTestAssociatedUpc();
		AssociatedUpc associatedUpc2 = this.getOtherAssociatedUpc();
		boolean equals = associatedUpc1.equals(associatedUpc2);
		Assert.assertFalse(equals);
	}

	/**
	 * Tests equals when passed in an object of a different class.
	 */
	@Test
	public void equalsDifferentClass() {
		AssociatedUpc associatedUpc = this.getTestAssociatedUpc();
		boolean equals = associatedUpc.equals(Long.valueOf(AssociatedUpcTest.TEST_ASSOCIATE));
		Assert.assertFalse(equals);
	}

	@Test
	public void equalsNull() {
		AssociatedUpc associatedUpc = this.getTestAssociatedUpc();
		boolean equals = associatedUpc.equals(null);
		Assert.assertFalse(equals);
	}

	/*
	 * hashCode
	 */

	/**
	 * Tests hashCode is consistent.
	 */
	@Test
	public void hashCodeSameObject() {
		AssociatedUpc associatedUpc = this.getTestAssociatedUpc();
		Assert.assertEquals(associatedUpc.hashCode(), associatedUpc.hashCode());
	}

	/**
	 * Tests hashCode is the same when the objects have the same values.
	 */
	@Test
	public void hashCodeSimilarObject() {
		AssociatedUpc associatedUpc1 = this.getTestAssociatedUpc();
		AssociatedUpc associatedUpc2 = this.getTestAssociatedUpc();
		Assert.assertEquals(associatedUpc1.hashCode(), associatedUpc2.hashCode());
	}

	/**
	 * Tests hashCode with two objects of the same key but different values.
	 */
	@Test
	public void hashCodeSameKey() {
		AssociatedUpc associatedUpc1 = this.getTestAssociatedUpc();
		AssociatedUpc associatedUpc2 = new AssociatedUpc();
		associatedUpc2.setUpc(AssociatedUpcTest.TEST_ASSOCIATE);
		Assert.assertEquals(associatedUpc1.hashCode(), associatedUpc2.hashCode());
	}

	/**
	 * Tests hashCode with different keys.
	 */
	@Test
	public void hashCodeDifferentObjects() {
		AssociatedUpc associatedUpc1 = this.getTestAssociatedUpc();
		AssociatedUpc associatedUpc2 = this.getOtherAssociatedUpc();
		Assert.assertNotEquals(associatedUpc1.hashCode(), associatedUpc2.hashCode());
	}

	/*
	 * toString
	 */
	@Test
	public void testToString() {
		AssociatedUpc associatedUpc = this.getTestAssociatedUpc();
		Assert.assertEquals("AssociatedUpc{upc='9015932509'}", associatedUpc.toString());
	}
	/*
	 * Support functions
	 */

	/**
	 * Returns the default PrimaryUpc to test with.
	 *
	 * @return The default PrimaryUpc to test wtih.
	 */
	private PrimaryUpc getTestPrimaryUpc() {

		PrimaryUpc primaryUpc = new PrimaryUpc();
		primaryUpc.setUpc(AssociatedUpcTest.TEST_PRIMARY);
		return primaryUpc;
	}

	/**
	 * Returns the default AssociatedUpc to test with.
	 *
	 * @return The default AssociatedUpc to test with.
	 */
	private AssociatedUpc getTestAssociatedUpc() {

		AssociatedUpc associatedUpc = new AssociatedUpc();
		associatedUpc.setUpc(AssociatedUpcTest.TEST_ASSOCIATE);
		associatedUpc.setPrimaryUpc(this.getTestPrimaryUpc());
		return associatedUpc;
	}

	private AssociatedUpc getOtherAssociatedUpc() {

		AssociatedUpc associatedUpc = new AssociatedUpc();
		associatedUpc.setUpc(AssociatedUpcTest.OTHER_ASSOCIATE);
		associatedUpc.setPrimaryUpc(this.getTestPrimaryUpc());
		return associatedUpc;
	}
}
