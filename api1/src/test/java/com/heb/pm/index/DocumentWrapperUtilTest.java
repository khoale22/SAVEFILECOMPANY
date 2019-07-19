/*
 * DocumentWrapperUtilTest
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.index;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

/**
 * Tests DocumentWrapperUtil.
 *
 * @author d116773
 * @since 2.0.2
 */
public class DocumentWrapperUtilTest {

	private static final String TEST_DATA = "TEST DATA";

	/**
	 * Concrete class that extends DocumentWrapper to test the wrapper util functions.
	 */
	private class TestDocumentWrapper extends DocumentWrapper<String, String> {

		/**
		 * Creates a new TestDocumentWrappter.
		 */
		public TestDocumentWrapper(){}

		/**
		 * Creates a new TestDocumentWrapper.
		 *
		 * @param data The data for the class to store.
		 */
		public TestDocumentWrapper(String data) {
			super(data);
		}

		/**
		 * Returns the key for the wrapper.
		 *
		 * @param data The data this object will store.
		 * @return The key for the wrapper.
		 */
		@Override
		protected String toKey(String data) {
			return data.toLowerCase();
		}
	}

	/*
	 * toDataCollection.
	 */

	/**
	 * Tests toDataCollection when null is passed as a source.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void toDataCollectionNullSource() {

		ArrayList<String> targetList = new ArrayList<>();

		DocumentWrapperUtil.<String, String>toDataCollection(null, targetList);
	}

	/**
	 * Tests toDataCollection when null is passed as a destination.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void toDataCollectionNullDestination() {

		TestDocumentWrapper testDocumentWrapper = new TestDocumentWrapper(DocumentWrapperUtilTest.TEST_DATA);
		ArrayList<TestDocumentWrapper> sourceList = new ArrayList<>();
		sourceList.add(testDocumentWrapper);

		DocumentWrapperUtil.toDataCollection(sourceList, null);
	}

	/**
	 * Tests toDataCollection with a good source and target.
	 */
	@Test
	public void toDataCollection() {
		TestDocumentWrapper testDocumentWrapper = new TestDocumentWrapper(DocumentWrapperUtilTest.TEST_DATA);
		ArrayList<TestDocumentWrapper> sourceList = new ArrayList<>();
		sourceList.add(testDocumentWrapper);

		ArrayList<String> destination = new ArrayList<>();

		DocumentWrapperUtil.toDataCollection(sourceList, destination);
		Assert.assertEquals(sourceList.size(), destination.size());
		Assert.assertEquals(testDocumentWrapper.getData(), destination.get(0));
	}

	/*
	 * toData
	 */

	/**
	 * Tests toData when passed a wrapper with data.
	 */
	@Test
	public void toData() {
		TestDocumentWrapper testDocumentWrapper = new TestDocumentWrapper(DocumentWrapperUtilTest.TEST_DATA);
		String s = DocumentWrapperUtil.toData(testDocumentWrapper);
		Assert.assertEquals(DocumentWrapperUtilTest.TEST_DATA, s);
	}

	/**
	 * Tests toData when passed null.
	 */
	@Test
	public void toDataNull() {
		TestDocumentWrapper testDocumentWrapper = null;
		Assert.assertNull(DocumentWrapperUtil.toData(testDocumentWrapper));
	}

	/**
	 * Test toData when passed an object that does not have data.
	 */
	@Test
	public void toDataNullData() {
		TestDocumentWrapper testDocumentWrapper = new TestDocumentWrapper();
		Assert.assertNull(DocumentWrapperUtil.toData(testDocumentWrapper));
	}
}
