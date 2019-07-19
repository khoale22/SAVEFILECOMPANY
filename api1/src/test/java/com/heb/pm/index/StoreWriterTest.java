/*
 * StoreWriterTest
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.index;

import com.heb.pm.entity.Store;
import com.heb.pm.repository.StoreIndexRepository;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import testSupport.CallChecker;

import java.util.ArrayList;
import java.util.List;

/**
 * Tests StoreWriter.
 *
 * @author d116773
 * @since 2.0.2
 */
public class StoreWriterTest {

	@Test
	public void writeNull() {

		StoreWriter writer = new StoreWriter();
		StoreCheckingCallChecker callChecker = new StoreCheckingCallChecker(null);
		writer.setIndexRepository(this.getIndexRepository(callChecker));
		try {
			writer.write(null);
			Assert.assertFalse(callChecker.isMethodCalled());
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		}
	}

	@Test
	public void writeEmptyList() {

		StoreWriter writer = new StoreWriter();
		StoreCheckingCallChecker callChecker = new StoreCheckingCallChecker(null);
		writer.setIndexRepository(this.getIndexRepository(callChecker));
		try {
			writer.write(new ArrayList<>());
			Assert.assertFalse(callChecker.isMethodCalled());
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		}
	}

	@Test
	public void writeGoodList() {

		StoreWriter writer = new StoreWriter();
		StoreCheckingCallChecker callChecker = new StoreCheckingCallChecker(this.getStore());
		writer.setIndexRepository(this.getIndexRepository(callChecker));
		try {
			writer.write(this.getStores());
			Assert.assertTrue(callChecker.isMethodCalled());
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		}
	}
	/*
	 * Support functions
	 */

	/**
	 * Mocks up the call to save on StoreIndexRepository.
	 */
	private class StoreCheckingCallChecker extends CallChecker {

		private Store toCompareTo = null;

		/**
		 * Constructs a new StoreCheckingCallChecker.
		 *
		 * @param s The store to make sure is passed to the call to save.
		 */
		public StoreCheckingCallChecker(Store s) {
			super();
			this.toCompareTo = s;
		}

		/**
		 * Mocks the call to save. It will make sure the right stuff was passed in.
		 *
		 * @param invocation The call of this method from Mockito.
		 * @return Null
		 * @throws Throwable
		 */
		@Override
		public Void answer(InvocationOnMock invocation) throws Throwable {
			Assert.assertTrue(invocation.getArguments()[0] instanceof List);
			List<Store> storeDocuments = (List)invocation.getArguments()[0];
			Assert.assertEquals(1, storeDocuments.size());
			Assert.assertEquals(this.toCompareTo, storeDocuments.get(0));
			return super.answer(invocation);
		}
	}

	/**
	 * Returns a Store to test with.
	 *
	 * @return A Store to test with.
	 */
	private Store getStore() {

		Store s = new Store();
		s.setStoreNumber(555L);
		s.setName("My HEB");
		return s;
	}

	/**
	 * Returns a list of StoreDocuments to test with.
	 *
	 * @return A list of StoreDocuments to test with.
	 */
	private List<Store> getStores() {

		List<Store> stores = new ArrayList<>(1);
		stores.add(this.getStore());
		return stores;
	}

	/**
	 * Mocks up the StoreIndexRepository to use for testing.
	 *
	 * @param callChecker The call checker to use.
	 * @return A StoreIndexRepository to use for testing.
	 */
	private StoreIndexRepository getIndexRepository(StoreCheckingCallChecker callChecker) {

		StoreIndexRepository repository = Mockito.mock(StoreIndexRepository.class);
		Mockito.doAnswer(callChecker).when(repository).save(Mockito.anyCollection());
		return repository;
	}
}
