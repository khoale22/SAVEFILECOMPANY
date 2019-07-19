/*
 * StoreRepositoryTest
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.store;

import com.heb.pm.entity.Store;
import com.heb.pm.repository.StoreRepository;
import com.heb.util.ws.SoapException;
import com.heb.xmlns.ei.Fault1;
import com.heb.xmlns.ei.StoreServicePortType1;
import com.heb.xmlns.ei.authentication.Authentication;
import com.heb.xmlns.ei.getstorelist_request_1.StoreListRequest1;
import com.heb.xmlns.ei.providerfaultschema.ProviderSOAPFault;
import com.heb.xmlns.ei.storelist_1.StoreList1;
import com.heb.xmlns.ei.storelist_1.StoreNumDesc;

import org.junit.Assert;
import org.junit.Test;

import org.mockito.Mockito;
import org.mockito.stubbing.Answer;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * Tests StoreRepository.
 *
 * @author d116773
 * @since 2.0.1
 */
public class StoreRepositoryTest {

	private static final long STORE_NUMBER = 444L;
	private static final String STORE_NAME = "test store";

	/**
	 * Tests getStores when there is an error in the service.
	 */
	@Test
	public void getStoresFault() {
		try {

			StoreRepository storeRepository = new StoreRepository();
			storeRepository.setStoreServiceClient(this.getServiceClient(this.getErrorPort()));

			storeRepository.findAll();
			Assert.fail("did not throw exception");
		} catch(SoapException e) {
			// This is supposed to happen.
		}
	}

	/**
	 * Tests getStores when good results are returned.
	 */
	@Test
	public void getStores() {

		StoreRepository storeRepository = new StoreRepository();
		storeRepository.setStoreServiceClient(this.getServiceClient(this.getGoodPort()));

		List<Store> storeList = storeRepository.findAll();
		Assert.assertEquals(1, storeList.size());
		Assert.assertEquals(StoreRepositoryTest.STORE_NUMBER, storeList.get(0).getStoreNumber());
		Assert.assertEquals(StoreRepositoryTest.STORE_NAME, storeList.get(0).getName());
	}

	/**
	 * Makes sure that getStores passes the correct parameters to the service call.
	 */
	@Test
	public void getStoresCheckParameters() {
		StoreRepository storeRepository = new StoreRepository();
		storeRepository.setStoreServiceClient(this.getServiceClient(this.getTestPort()));
		storeRepository.findAll();
	}

	/**
	 * Tests getStores when the list returned by StoresList1 is null.
	 */
	@Test
	public void getStoresNullList() {
		StoreRepository storeRepository = new StoreRepository();
		storeRepository.setStoreServiceClient(this.getServiceClient(this.getNullListPort()));
		List<Store> stores = storeRepository.findAll();
		Assert.assertNotNull(stores);
		Assert.assertTrue(stores.isEmpty());
	}

	/**
	 * Tests getStores when a null is returned as StoreList1.
	 */
	@Test
	public void getStoresNullStoreList1() {
		StoreRepository storeRepository = new StoreRepository();
		storeRepository.setStoreServiceClient(this.getServiceClient(this.getNullStoreList1Port()));
		List<Store> stores = storeRepository.findAll();
		Assert.assertNotNull(stores);
		Assert.assertTrue(stores.isEmpty());
	}

	/*
	 * support functions
	 */

	/**
	 * Returns a StoreServiceClient to test with.
	 *
	 * @param port The port for the StoreServiceClient to use.
	 * @return A StoreServiceClient to test with.
	 */
	private StoreServiceClient getServiceClient(StoreServicePortType1 port) {

		StoreServiceClient storeServiceClient = Mockito.mock(StoreServiceClient.class);
		Mockito.when(storeServiceClient.getAuthentication()).thenReturn(new Authentication());
		Mockito.when(storeServiceClient.getPort()).thenReturn(port);
		return storeServiceClient;
	}

	/**
	 * Returns a port that will throw an exception.
	 *
	 * @return A port that will throw an exception.
	 */
	private StoreServicePortType1 getErrorPort() {

		StoreServicePortType1 port = null;

		try {
			port = Mockito.mock(StoreServicePortType1.class);
			Mockito.when(port.getStoreList(Mockito.anyObject())).thenThrow(new Fault1("test message", new ProviderSOAPFault()));
		} catch (Fault1 e) {
			Assert.fail(e.getMessage());
		}

		return port;
	}

	/**
	 * Returns a port that will return good data.
	 *
	 * @return A port that will return good data.
	 */
	private StoreServicePortType1 getGoodPort() {

		StoreServicePortType1 port = null;
		try {
			port = Mockito.mock(StoreServicePortType1.class);
			StoreList1 storeList1 = this.getGoodStoreList1();
			Mockito.when(port.getStoreList(Mockito.anyObject())).thenReturn(storeList1);
		} catch (Fault1 e) {
			Assert.fail(e.getMessage());
		}

		return port;
	}

	/**
	 * Returns a port that will return a StoreList1, but that StoreList1 will return null for getStoreNumDesc.
	 *
	 * @return A port that will return a StoreList1, but that StoreList1 will return null for getStoreNumDesc.
	 */
	private StoreServicePortType1 getNullListPort() {

		StoreServicePortType1 port = null;
		try {
			port = Mockito.mock(StoreServicePortType1.class);
			StoreList1 storeList1 = this.getNullStoreList1();
			Mockito.when(port.getStoreList(Mockito.anyObject())).thenReturn(storeList1);
		} catch (Fault1 e) {
			Assert.fail(e.getMessage());
		}

		return port;
	}

	/**
	 * Returns a port that will return null for its StoreList1.
	 *
	 * @return A port that will return null for its StoreList1.
	 */
	private StoreServicePortType1 getNullStoreList1Port() {

		StoreServicePortType1 port = null;
		try {
			port = Mockito.mock(StoreServicePortType1.class);
			Mockito.when(port.getStoreList(Mockito.anyObject())).thenReturn(null);
		} catch (Fault1 e) {
			Assert.fail(e.getMessage());
		}

		return port;
	}

	/**
	 * Returns a port that checks the parameters passed into the call to the service.
	 *
	 * @return A port that checks the parameters passed into the call to the service.
	 */
	private StoreServicePortType1 getTestPort() {
		Answer<StoreList1> getStoreListAnswer = invocation -> {
			StoreListRequest1 request = (StoreListRequest1)invocation.getArguments()[0];
			Assert.assertNotNull(request.getAuthentication());
			Assert.assertEquals("Y", request.getInActiveSwitch());
			return this.getGoodStoreList1();
		};

		StoreServicePortType1 port = null;
		try {
			port = Mockito.mock(StoreServicePortType1.class);
			Mockito.when(port.getStoreList(Mockito.anyObject())).thenAnswer(getStoreListAnswer);
		} catch (Fault1 e) {
			Assert.fail(e.getMessage());
		}

		return port;
	}

	/**
	 * Returns a good StoreList1 that has a good collection of stores.
	 *
	 * @return A good StoreList1 that has a good collection of stores.
	 */
	private StoreList1 getGoodStoreList1(){
		StoreList1 storeList1 = Mockito.mock(StoreList1.class);
		List<StoreNumDesc> storeNumDescs = this.getStoreNumDescList();
		Mockito.when(storeList1.getStoreNumDesc()).thenReturn(storeNumDescs);
		return storeList1;
	}

	/**
	 * Returns a StoreList1 that returns null when asked for a store list.
	 *
	 * @return  A StoreList1 that returns null when asked for a store list.
	 */
	private StoreList1 getNullStoreList1() {
		StoreList1 storeList1 = Mockito.mock(StoreList1.class);
		Mockito.when(storeList1.getStoreNumDesc()).thenReturn(null);
		return storeList1;
	}

	/**
	 * Returns a list of stores.
	 *
	 * @return A list of stores.
	 */
	private List<StoreNumDesc> getStoreNumDescList() {
		StoreNumDesc storeNumDesc = new StoreNumDesc();
		storeNumDesc.setLOCNBR(BigInteger.valueOf(StoreRepositoryTest.STORE_NUMBER));
		storeNumDesc.setLOCNM(StoreRepositoryTest.STORE_NAME);
		List<StoreNumDesc> storeNumDescList = new ArrayList<>();
		storeNumDescList.add(storeNumDesc);

		return storeNumDescList;
	}
}
