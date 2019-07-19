/*
 * VendorRepositoryTest
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.vendor;

import com.heb.pm.entity.Vendor;
import com.heb.pm.repository.VendorRepository;
import com.heb.util.ws.SoapException;
import com.heb.xmlns.ei.Fault;
import com.heb.xmlns.ei.VendorServicePortType;

import com.heb.xmlns.ei.activeapvendorlist.ActiveApVendorList;
import com.heb.xmlns.ei.authentication.Authentication;
import com.heb.xmlns.ei.getactiveapvendorlist_reply.GetActiveApVendorListReply;
import com.heb.xmlns.ei.providerfaultschema.ProviderSOAPFault;
import org.junit.Assert;
import org.junit.Test;

import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Tests VendorRepository.
 *
 * @author d116773
 * @since 2.0.2
 */
public class VendorRepositoryTest {

	public static final int TEST_VENDOR_NUMBER = 333434;
	public static final String TEST_VENDOR_NAME = "TEST VENDOR NAME";

	/**
	 * Tests getVendors when there is an error in the service.
	 */
	@Test(expected = SoapException.class)
	public void getVendorsFault() {
		VendorRepository vendorRepository = new VendorRepository();
		vendorRepository.setVendorServiceClient(this.getServiceClient(this.getErrorPort()));
		vendorRepository.findAll();
	}

	/**
	 * Tests getVendors when the reply contains null instead of a list
	 */
	@Test
	public void getVendorsNullList() {
		VendorRepository vendorRepository = new VendorRepository();
		vendorRepository.setVendorServiceClient(this.getServiceClient(this.getNullListPort()));
		Set<Vendor> vendors = vendorRepository.findAll();
		Assert.assertNotNull(vendors);
		Assert.assertTrue(vendors.isEmpty());
	}

	/**
	 * Tests getVendors when the service returns an empty list.
	 */
	@Test
	public void getVendorsEmptyList() {
		VendorRepository vendorRepository = new VendorRepository();
		vendorRepository.setVendorServiceClient(this.getServiceClient(this.getEmptyListPort()));
		Set<Vendor> vendors = vendorRepository.findAll();
		Assert.assertNotNull(vendors);
		Assert.assertTrue(vendors.isEmpty());
	}

	/**
	 * Tests getVendors when the service returns null for a reply.
	 */
	@Test
	public void getVendorsNullReply() {
		VendorRepository vendorRepository = new VendorRepository();
		vendorRepository.setVendorServiceClient(this.getServiceClient(this.getNullReplyPort()));
		Set<Vendor> vendors = vendorRepository.findAll();
		Assert.assertNotNull(vendors);
		Assert.assertTrue(vendors.isEmpty());
	}

	/**
	 * Tests getVendors when a good list of vendors is returned.
	 */
	@Test
	public void getVendors() {
		VendorRepository vendorRepository = new VendorRepository();
		vendorRepository.setVendorServiceClient(this.getServiceClient(this.getGoodPort()));
		Set<Vendor> vendors = vendorRepository.findAll();
		Assert.assertEquals(1, vendors.size());
		List<Vendor> listVendors = new ArrayList<Vendor>(vendors);
		Assert.assertEquals(VendorRepositoryTest.TEST_VENDOR_NUMBER, listVendors.get(0).getVendorNumber());
		Assert.assertEquals(VendorRepositoryTest.TEST_VENDOR_NAME, listVendors.get(0).getVendorName());
		Assert.assertEquals(Integer.toString(VendorRepositoryTest.TEST_VENDOR_NUMBER),
				listVendors.get(0).getVendorNumberAsString());
	}

	/**
	 * Returns a port which will return null for a reply.
	 *
	 * @return A port which will return null for a reply.
	 */
	private VendorServicePortType getNullReplyPort() {
		VendorServicePortType port = Mockito.mock(VendorServicePortType.class);
		try {
			Mockito.when(port.getActiveApVendorList(Mockito.anyObject())).thenReturn(null);
		} catch (Fault fault) {
			Assert.fail(fault.getMessage());
		}

		return port;
	}

	/**
	 * Returns a port which will return a list of vendors.
	 *
	 * @return A port which will return a list of vendors.
	 */
	private VendorServicePortType getGoodPort() {
		ActiveApVendorList v = new ActiveApVendorList();
		v.setAPNUMBER(VendorRepositoryTest.TEST_VENDOR_NUMBER);
		v.setAPNAME(VendorRepositoryTest.TEST_VENDOR_NAME);

		List<ActiveApVendorList> vendors = new ArrayList<>();
		vendors.add(v);

		GetActiveApVendorListReply reply = Mockito.mock(GetActiveApVendorListReply.class);
		Mockito.when(reply.getActiveApVendorList()).thenReturn(vendors);

		VendorServicePortType port = Mockito.mock(VendorServicePortType.class);
		try {
			Mockito.when(port.getActiveApVendorList(Mockito.anyObject())).thenReturn(reply);
		} catch (Fault fault) {
			Assert.fail(fault.getMessage());
		}

		return port;
	}
	/**
	 * Returns a port which will return an empty list.
	 *
	 * @return A port which will return an empty list.
	 */
	private VendorServicePortType getEmptyListPort() {
		GetActiveApVendorListReply reply = Mockito.mock(GetActiveApVendorListReply.class);
		Mockito.when(reply.getActiveApVendorList()).thenReturn(new ArrayList<>());

		VendorServicePortType port = Mockito.mock(VendorServicePortType.class);
		try {
			Mockito.when(port.getActiveApVendorList(Mockito.anyObject())).thenReturn(reply);
		} catch (Fault fault) {
			Assert.fail(fault.getMessage());
		}

		return port;
	}

	/**
	 * Returns a port that will return a reply which will return null for the list of vendors.
	 *
	 * @return A port that will return a reply which will return null for the list of vendors.
	 */
	private VendorServicePortType getNullListPort() {

		GetActiveApVendorListReply reply = Mockito.mock(GetActiveApVendorListReply.class);
		Mockito.when(reply.getActiveApVendorList()).thenReturn(null);

		VendorServicePortType port = Mockito.mock(VendorServicePortType.class);
		try {
			Mockito.when(port.getActiveApVendorList(Mockito.anyObject())).thenReturn(reply);
		} catch (Fault fault) {
			Assert.fail(fault.getMessage());
		}

		return port;
	}

	/**
	 * Returns a vendor port that will throw an error.
	 *
	 * @return A vendor port that will throw an error.
	 */
	private VendorServicePortType getErrorPort() {

		VendorServicePortType port = null;

		try {
			port = Mockito.mock(VendorServicePortType.class);
			Mockito.when(port.getActiveApVendorList(Mockito.anyObject())).thenThrow(new Fault("message", new ProviderSOAPFault()));
		} catch (Fault fault) {
			Assert.fail(fault.getMessage());
		}

		return port;
	}

	/**
	 * Returns a VendorServiceClient which will contain a particular port.
	 *
	 * @param port The port for the VendorServiceClient to contain.
	 * @return A VendorServiceClient which will return the provided port.
	 */
	private VendorServiceClient getServiceClient(VendorServicePortType port) {

		VendorServiceClient vendorServiceClient = Mockito.mock(VendorServiceClient.class);
		Mockito.when(vendorServiceClient.getAuthentication()).thenReturn(new Authentication());
		Mockito.when(vendorServiceClient.getPort()).thenReturn(port);

		return vendorServiceClient;
	}
}
