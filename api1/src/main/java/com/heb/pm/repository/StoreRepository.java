/*
 * StoreRepository
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.repository;

import com.heb.pm.entity.Store;
import com.heb.pm.store.StoreServiceClient;
import com.heb.util.ws.ObjectConverter;
import com.heb.util.ws.SoapException;
import com.heb.xmlns.ei.Fault1;
import com.heb.xmlns.ei.getstorelist_request_1.StoreListRequest1;
import com.heb.xmlns.ei.storelist_1.StoreList1;
import com.heb.xmlns.ei.storelist_1.StoreNumDesc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


/**
 * Repository to retrieve information about stores.
 *
 * @author d116773
 * @since 2.0.1
 */
@Service
public class StoreRepository {

	@Autowired
	private StoreServiceClient storeServiceClient;

	private ObjectConverter<StoreNumDesc, Store> objectConverter = new ObjectConverter<>(Store::new);

	/**
	 * Returns a list of open stores.
	 *
	 * @return A list of open stores.
	 */
	public List<Store> findAll() {
		StoreListRequest1 storeListRequest = new StoreListRequest1();
		storeListRequest.setAuthentication(this.storeServiceClient.getAuthentication());
		storeListRequest.setInActiveSwitch("Y");

		try {
			StoreList1 storeList = this.storeServiceClient.getPort().getStoreList(storeListRequest);

			List<Store> s = new ArrayList<>();
			if (storeList!= null && storeList.getStoreNumDesc() != null) {
				storeList.getStoreNumDesc().forEach((i) -> s.add(this.objectConverter.convert(i)));
			}

			return s;
		} catch (Fault1 fault1) {
			throw new SoapException(fault1.getMessage(), fault1.getCause());
		}
	}

	/**
	 * Sets the object that makes the actual calls to the store service. This method is primarily used for testing.
	 *
	 * @param storeServiceClient The bject that makes the actual calls to the store service.
	 */
	public void setStoreServiceClient(StoreServiceClient storeServiceClient) {
		this.storeServiceClient = storeServiceClient;
	}
}
