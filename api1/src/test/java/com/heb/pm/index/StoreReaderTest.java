/*
 * StoreReaderTest
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.index;

import com.heb.pm.entity.Store;
import com.heb.pm.repository.StoreRepository;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.batch.core.StepExecution;

import java.util.ArrayList;
import java.util.List;


/**
 * Tests StoreReader.
 *
 * @author d116773
 * @since 2.0.2
 */
public class StoreReaderTest {

	private static final long STORE_NUMBER = 444L;
	private static final String STORE_NAME = "My HEB";

	/*
	 * read
	 */

	/**
	 * Tests read.
	 */
	@Test
	public void read() {

		StoreReader storeReader = new StoreReader();
		storeReader.setStoreRepository(this.getStoreRepository());

		// beforeStep has to be called before read is to set up the data.
		storeReader.beforeStep(this.getStepExecution());

		try {
			// The first call to read should return a Store.
			Store s = storeReader.read();
			Assert.assertEquals(StoreReaderTest.STORE_NUMBER, s.getStoreNumber());
			Assert.assertEquals(StoreReaderTest.STORE_NAME, s.getName());

			// The second call should return null.
			Assert.assertNull(storeReader.read());
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		}
	}

	/*
	 * afterStep.
	 */
	/**
	 * Tests afterStep. It just returns null.
	 */
	@Test
	public void afterStep() {

		StoreReader storeReader = new StoreReader();
		Assert.assertNull(storeReader.afterStep(this.getStepExecution()));
	}

	/*
	 * Support functions.
	 */

	/**
	 * Returns a StepExecution for the StepExecutionListener functions.
	 *
	 * @return A StepExecution
	 */
	private StepExecution getStepExecution() {
		return Mockito.mock(StepExecution.class);
	}

	/**
	 * Returns a List of stores to test with. It will have one store in the list.
	 *
	 * @return A List of stores to test with.
	 */
	private List<Store> getStores() {

		Store s = new Store();
		s.setStoreNumber(StoreReaderTest.STORE_NUMBER);
		s.setName(StoreReaderTest.STORE_NAME);

		List<Store> stores = new ArrayList<>();
		stores.add(s);
		return stores;
	}

	/**
	 * Returns a StoreRepository that will return the list from getStores.
	 *
	 * @return A StoreRepository to test with.
	 */
	private StoreRepository getStoreRepository() {
		StoreRepository repository = Mockito.mock(StoreRepository.class);
		Mockito.when(repository.findAll()).thenReturn(this.getStores());
		return repository;
	}

}
