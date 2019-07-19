/*
 * DocumentWrapperTest
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

/**
 * Tests DocumentWrapper.
 *
 * @author d116773
 * @since 2.0.2
 */
public class DocumentWrapperTest {

	private static final String TEST_DATA = "TEST DATA";

	/**
	 * Concrete class that extends DocumentWrapper to test its functionality.
	 */
	private class TestDocumentWrapper extends DocumentWrapper<String, String> {

		private boolean toKeyCalled;

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
			this.toKeyCalled = true;
			return data.toLowerCase();
		}

		/**
		 * Returns whether or not toKey was called.
		 *
		 * @return True if toKey was called and false otherwise.
		 */
		boolean isToKeyCalled() {
			return this.toKeyCalled;
		}
	}

	/**
	 * The only functionality in DocumentWrapper is in its constructor.
	 */
	@Test
	public void constructor() {

		TestDocumentWrapper  testDocumentWrapper = new TestDocumentWrapper(DocumentWrapperTest.TEST_DATA);
		Assert.assertEquals(DocumentWrapperTest.TEST_DATA, testDocumentWrapper.getData());
		Assert.assertEquals(DocumentWrapperTest.TEST_DATA.toLowerCase(), testDocumentWrapper.getKey());
		Assert.assertTrue(testDocumentWrapper.isToKeyCalled());
	}
}
