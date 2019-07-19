/*
 * StoreServiceTest
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.store;

import com.heb.pm.entity.Store;

import com.heb.pm.repository.StoreIndexRepository;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;


/**
 * Tests StoreSerivce.
 *
 * @author d116773
 * @since 2.0.1
 */
public class StoreServiceTest {

	private static final long STORE_NUMBER = 444L;
	private static final String STORE_NAME = "store name";

	/**
	 * Tests getStores.
	 */
	@Test
	public void getStores() {

		StoreIndexRepository storeRepository = Mockito.mock(StoreIndexRepository.class);

		Mockito.when(storeRepository.findAll()).thenReturn(this.getStoreList());
		StoreService storeService = new StoreService();
		storeService.setStoreIndexRepository(storeRepository);

		List<Store> stores = storeService.getStores();
		Assert.assertNotNull(stores);
		Assert.assertEquals(1, stores.size());
		Assert.assertEquals(StoreServiceTest.STORE_NAME, stores.get(0).getName());
		Assert.assertEquals(StoreServiceTest.STORE_NUMBER, stores.get(0).getStoreNumber());
	}

	/*
	 * supporting functions
	 */

	/**
	 * Retuns a list of stores to test with.
	 *
	 * @return A list of stores to test with.
	 */
	private List<Store> getStoreList() {

		Store s = new Store();
		s.setStoreNumber(StoreServiceTest.STORE_NUMBER);
		s.setName(StoreServiceTest.STORE_NAME);

		List<Store> storeList = new ArrayList<>();
		storeList.add(s);

		return storeList;
	}
}
