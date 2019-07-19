/*
 * StoreService
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Maintains all business logic related to stores.
 *
 * @author d116773
 * @since 2.0.1
 */
@Service
public class StoreService {


	@Autowired
	private StoreIndexRepository storeIndexRepository;

	/**
	 * Returns a list of open stores.
	 *
	 * @return A list of open stores.
	 */
	public List<Store> getStores() {
		List<Store> stores = new ArrayList<>();
		this.storeIndexRepository.findAll().forEach(stores::add);
		return stores;
	}

	/**
	 * Sets the repository to use to pull store information. This is primarily here for testing.
	 *
	 * @param storeIndexRepository The repository to use to pull store information.
	 */
	public void setStoreIndexRepository(StoreIndexRepository storeIndexRepository) {
		this.storeIndexRepository = storeIndexRepository;
	}
}
