/*
 *  ProductManagementServiceClientTest
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.ws;

import com.heb.pm.productDiscontinue.RemoveFromStores;
import com.heb.pm.upcMaintenance.UpcSwap;
import com.heb.util.controller.UserInfo;
import com.heb.xmlns.ei.Fault;
import com.heb.xmlns.ei.ProductManagementServicePort;
import com.heb.xmlns.ei.ProductManagementServiceServiceagent;
import com.heb.xmlns.ei.ProviderFaultSchema;
import com.heb.xmlns.ei.authentication.Authentication;
import com.heb.xmlns.ei.productmanagement.update_product_reply.UpdateProductReply;
import com.heb.xmlns.ei.productmanagement.update_product_request.UpdateProductRequest;
import com.heb.xmlns.ei.productupdateresponse.Detail;
import com.heb.xmlns.ei.productupdateresponse.ProductUpdateResponse;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;

/**
 * @author s573181
 * @since 2.0.7
 */
public class ProductManagementServiceClientTest {

	@InjectMocks
	private ProductManagementServiceClient productManagementServiceClient;

	@Mock
	private ProductManagementServiceServiceagent productManagementServiceServiceagent;

	@Mock
	private ProductManagementServicePort port;

	@Mock
	private UserInfo userInfo;

	@Before
	public void setup(){
		MockitoAnnotations.initMocks(this);
	}

	/**
	 * Tests submitRemoveFromStoresSwitch.
	 * @throws ProviderFaultSchema
	 * @throws CheckedSoapException
	 */
	@Test
	public void submitRemoveFromStoresSwitch() throws ProviderFaultSchema, CheckedSoapException {

		UpdateProductReply reply = new UpdateProductReply();
		reply.getProductUpdateResponse().add(0, new ProductUpdateResponse());
		reply.getProductUpdateResponse().get(0).getDetail().add(new Detail());
		reply.getProductUpdateResponse().get(0).getDetail().get(0).setSucessFlag("Y");

		RemoveFromStores removeFromStores = new RemoveFromStores();
		removeFromStores.setUpc(123L);
		removeFromStores.setRemovedInStores(false);
		removeFromStores.setSuccessful(true);

		Mockito.when(this.productManagementServiceServiceagent.getProductManagementService()).thenReturn(port);
		Mockito.when(this.port.updateProductDetails(Mockito.any(UpdateProductRequest.class))).thenReturn(reply);
		Mockito.when(this.userInfo.getUserId()).thenReturn("User");
		String results = this.productManagementServiceClient.submitRemoveFromStoresSwitch(removeFromStores);

		Assert.assertNull(results);
	}

	/**
	 * Tests both to dsd upc swap when source upc is an associate.
	 *
	 * @throws Fault
	 * @throws CheckedSoapException
	 */
	@Test
	public void submitBothToDsdAssociate() throws Fault, CheckedSoapException {

		UpdateProductReply reply = new UpdateProductReply();
		reply.getProductUpdateResponse().add(0, new ProductUpdateResponse());
		reply.getProductUpdateResponse().get(0).getDetail().add(new Detail());
		reply.getProductUpdateResponse().get(0).getDetail().get(0).setSucessFlag("Y");

		UpcSwap swap = new UpcSwap();
		swap.setSource(swap.new SwappableEndPoint());
		swap.getSource().setItemCode(2L);
		swap.getSource().setProductId(2L);
		swap.setSourceUpc(2L);
		swap.setSelectSourcePrimaryUpc(4L);
		swap.setDestination(swap.new SwappableEndPoint());

		Mockito.when(this.productManagementServiceServiceagent.getProductManagementService()).thenReturn(port);
		try {
			Mockito.when(this.port.swapScanCodes(Mockito.any(com.heb.xmlns.ei.swap_scancodes_request.SwapScanCodesRequest.class))).thenReturn(reply);
		} catch (ProviderFaultSchema providerFaultSchema) {
			providerFaultSchema.printStackTrace();
		}
		Mockito.when(this.userInfo.getUserId()).thenReturn("User");

		this.productManagementServiceClient.submitBothToDsd(swap);
	}

	/*
	* dsd to both upc swap
	*/

	/**
	 * Tests dsd to both upc swap when source upc is to be made destination primary.
	 *
	 * @throws Fault
	 * @throws CheckedSoapException
	 */
	@Test
	public void submitDsdToBothDestinationPrimary() throws Fault, CheckedSoapException {

		UpdateProductReply reply = new UpdateProductReply();
		reply.getProductUpdateResponse().add(0, new ProductUpdateResponse());
		reply.getProductUpdateResponse().get(0).getDetail().add(new Detail());
		reply.getProductUpdateResponse().get(0).getDetail().get(0).setSucessFlag("Y");

		UpcSwap swap = new UpcSwap();
		swap.setSource(swap.new SwappableEndPoint());
		swap.getSource().setItemCode(2L);
		swap.getSource().setProductId(2L);
		swap.setSourceUpc(2L);
		swap.setDestination(swap.new SwappableEndPoint());
		swap.getDestination().setItemCode(2L);
		swap.getDestination().setProductId(2L);
		swap.setMakeDestinationPrimaryUpc(true);

		Mockito.when(this.productManagementServiceServiceagent.getProductManagementService()).thenReturn(port);
		try {
			Mockito.when(this.port.swapScanCodes(Mockito.any(com.heb.xmlns.ei.swap_scancodes_request.SwapScanCodesRequest.class))).thenReturn(reply);
		} catch (ProviderFaultSchema providerFaultSchema) {
			providerFaultSchema.printStackTrace();
		}		Mockito.when(this.userInfo.getUserId()).thenReturn("User");

		this.productManagementServiceClient.submitDsdToBoth(swap);
	}

	/**
	 * Tests dsd to both upc swap when source upc is to be made destination associate.
	 *
	 * @throws Fault
	 * @throws CheckedSoapException
	 */
	@Test
	public void submitDsdToBothDestinationAssociate() throws Fault, CheckedSoapException {

		UpdateProductReply reply = new UpdateProductReply();
		reply.getProductUpdateResponse().add(0, new ProductUpdateResponse());
		reply.getProductUpdateResponse().get(0).getDetail().add(new Detail());
		reply.getProductUpdateResponse().get(0).getDetail().get(0).setSucessFlag("Y");

		UpcSwap swap = new UpcSwap();
		swap.setSource(swap.new SwappableEndPoint());
		swap.getSource().setItemCode(2L);
		swap.getSource().setProductId(2L);
		swap.setSourceUpc(2L);
		swap.setDestination(swap.new SwappableEndPoint());
		swap.getDestination().setItemCode(2L);
		swap.getDestination().setProductId(2L);
		swap.setDestinationPrimaryUpc(2L);

		Mockito.when(this.productManagementServiceServiceagent.getProductManagementService()).thenReturn(port);
		try {
			Mockito.when(this.port.swapScanCodes(Mockito.any(com.heb.xmlns.ei.swap_scancodes_request.SwapScanCodesRequest.class))).thenReturn(reply);
		} catch (ProviderFaultSchema providerFaultSchema) {
			providerFaultSchema.printStackTrace();
		}		Mockito.when(this.userInfo.getUserId()).thenReturn("User");

		this.productManagementServiceClient.submitDsdToBoth(swap);
	}

	/*
	* warehouse to warehouse upc swap
	*/

	/**
	 * Tests warehouse to warehouse upc swap when reply.getVROWResponse is empty.
	 *
	 * @throws Fault
	 * @throws CheckedSoapException
	 */
	@Test(expected = CheckedSoapException.class)
	public void submitUpcSwapThrowsCheckedSoapException() throws Fault, CheckedSoapException {
		UpdateProductReply reply = new UpdateProductReply();
		reply.getProductUpdateResponse().add(0, new ProductUpdateResponse());
		reply.getProductUpdateResponse().get(0).getDetail().add(new Detail());
		reply.getProductUpdateResponse().get(0).getDetail().get(0).setSucessFlag("N");

		UpcSwap swap = new UpcSwap();
		swap.setSource(swap.new SwappableEndPoint());
		swap.getSource().setItemCode(2L);
		swap.getSource().setProductId(2L);
		swap.setSelectSourcePrimaryUpc(2L);
		swap.setSourceUpc(2L);
		swap.setDestination(swap.new SwappableEndPoint());
		swap.getDestination().setItemCode(2L);
		swap.getDestination().setProductId(2L);
		swap.setDestinationPrimaryUpc(2L);

		Mockito.when(this.productManagementServiceServiceagent.getProductManagementService()).thenReturn(port);
		try {
			Mockito.when(this.port.swapScanCodes(Mockito.any(com.heb.xmlns.ei.swap_scancodes_request.SwapScanCodesRequest.class))).thenReturn(reply);
		} catch (ProviderFaultSchema providerFaultSchema) {
			providerFaultSchema.printStackTrace();
		}
		Mockito.when(this.userInfo.getUserId()).thenReturn("User");

		this.productManagementServiceClient.submitUpcSwap(swap);
	}

	/**
	 * Tests warehouse to warehouse upc swap when reply.getVROWResponse is empty.
	 *
	 * @throws Fault
	 * @throws CheckedSoapException
	 */
	@Test
	public void submitUpcSwapReturnEmptyString() throws Fault, CheckedSoapException {

		UpdateProductReply reply = new UpdateProductReply();

		UpcSwap swap = new UpcSwap();
		swap.setSource(swap.new SwappableEndPoint());
		swap.getSource().setItemCode(2L);
		swap.getSource().setProductId(2L);
		swap.setSelectSourcePrimaryUpc(2L);
		swap.setSourceUpc(2L);
		swap.setDestination(swap.new SwappableEndPoint());
		swap.getDestination().setItemCode(2L);
		swap.getDestination().setProductId(2L);
		swap.setDestinationPrimaryUpc(2L);

		Mockito.when(this.productManagementServiceServiceagent.getProductManagementService()).thenReturn(port);
		try {
			Mockito.when(this.port.swapScanCodes(Mockito.any(com.heb.xmlns.ei.swap_scancodes_request.SwapScanCodesRequest.class))).thenReturn(reply);
		} catch (ProviderFaultSchema providerFaultSchema) {
			providerFaultSchema.printStackTrace();
		}		Mockito.when(this.userInfo.getUserId()).thenReturn("User");

		this.productManagementServiceClient.submitUpcSwap(swap);
	}

	/**
	 * Tests warehouse to warehouse upc swap with same item code for source and destination.
	 *
	 * @throws Fault
	 * @throws CheckedSoapException
	 */
	@Test
	public void submitUpcSwapSameItemCode() throws Fault, CheckedSoapException {

		UpdateProductReply reply = new UpdateProductReply();
		reply.getProductUpdateResponse().add(0, new ProductUpdateResponse());
		reply.getProductUpdateResponse().get(0).getDetail().add(new Detail());
		reply.getProductUpdateResponse().get(0).getDetail().get(0).setSucessFlag("Y");

		UpcSwap swap = new UpcSwap();
		swap.setSource(swap.new SwappableEndPoint());
		swap.getSource().setItemCode(2L);
		swap.getSource().setProductId(2L);
		swap.setSelectSourcePrimaryUpc(2L);
		swap.setSourceUpc(2L);
		swap.setDestination(swap.new SwappableEndPoint());
		swap.getDestination().setItemCode(2L);
		swap.getDestination().setProductId(2L);
		swap.setDestinationPrimaryUpc(2L);

		Mockito.when(this.productManagementServiceServiceagent.getProductManagementService()).thenReturn(port);
		try {
			Mockito.when(this.port.swapScanCodes(Mockito.any(com.heb.xmlns.ei.swap_scancodes_request.SwapScanCodesRequest.class))).thenReturn(reply);
		} catch (ProviderFaultSchema providerFaultSchema) {
			providerFaultSchema.printStackTrace();
		}		Mockito.when(this.userInfo.getUserId()).thenReturn("User");

		this.productManagementServiceClient.submitUpcSwap(swap);
	}

	/**
	 * Tests warehouse to warehouse upc swap with source primary getting set to destination primary.
	 *
	 * @throws Fault
	 * @throws CheckedSoapException
	 */
	@Test
	public void submitUpcSwapPrimaryToPrimary() throws Fault, CheckedSoapException {

		UpdateProductReply reply = new UpdateProductReply();
		reply.getProductUpdateResponse().add(0, new ProductUpdateResponse());
		reply.getProductUpdateResponse().get(0).getDetail().add(new Detail());
		reply.getProductUpdateResponse().get(0).getDetail().get(0).setSucessFlag("Y");

		UpcSwap swap = new UpcSwap();
		swap.setSource(swap.new SwappableEndPoint());
		swap.getSource().setItemCode(4L);
		swap.getSource().setProductId(2L);
		swap.setSelectSourcePrimaryUpc(2L);
		swap.setSourcePrimaryUpc(true);
		swap.setSourceUpc(2L);
		swap.setDestination(swap.new SwappableEndPoint());
		swap.getDestination().setItemCode(2L);
		swap.getDestination().setProductId(2L);
		swap.setMakeDestinationPrimaryUpc(true);
		swap.setDestinationPrimaryUpc(2L);

		Mockito.when(this.productManagementServiceServiceagent.getProductManagementService()).thenReturn(port);
		try {
			Mockito.when(this.port.swapScanCodes(Mockito.any(com.heb.xmlns.ei.swap_scancodes_request.SwapScanCodesRequest.class))).thenReturn(reply);
		} catch (ProviderFaultSchema providerFaultSchema) {
			providerFaultSchema.printStackTrace();
		}		Mockito.when(this.userInfo.getUserId()).thenReturn("User");

		this.productManagementServiceClient.submitUpcSwap(swap);
	}

	/**
	 * Tests warehouse to warehouse upc swap with source primary getting set to destination associate.
	 *
	 * @throws Fault
	 * @throws CheckedSoapException
	 */
	@Test
	public void submitUpcSwapPrimaryToAssociate() throws Fault, CheckedSoapException {

		UpdateProductReply reply = new UpdateProductReply();
		reply.getProductUpdateResponse().add(0, new ProductUpdateResponse());
		reply.getProductUpdateResponse().get(0).getDetail().add(new Detail());
		reply.getProductUpdateResponse().get(0).getDetail().get(0).setSucessFlag("Y");

		UpcSwap swap = new UpcSwap();
		swap.setSource(swap.new SwappableEndPoint());
		swap.getSource().setItemCode(4L);
		swap.getSource().setProductId(2L);
		swap.setSelectSourcePrimaryUpc(2L);
		swap.setSourcePrimaryUpc(true);
		swap.setSourceUpc(2L);
		swap.setDestination(swap.new SwappableEndPoint());
		swap.getDestination().setItemCode(2L);
		swap.getDestination().setProductId(2L);
		swap.setDestinationPrimaryUpc(2L);

		Mockito.when(this.productManagementServiceServiceagent.getProductManagementService()).thenReturn(port);
		try {
			Mockito.when(this.port.swapScanCodes(Mockito.any(com.heb.xmlns.ei.swap_scancodes_request.SwapScanCodesRequest.class))).thenReturn(reply);
		} catch (ProviderFaultSchema providerFaultSchema) {
			providerFaultSchema.printStackTrace();
		}		Mockito.when(this.userInfo.getUserId()).thenReturn("User");

		this.productManagementServiceClient.submitUpcSwap(swap);
	}

	/**
	 * Tests warehouse to warehouse upc swap with source associate getting set to destination primary.
	 *
	 * @throws Fault
	 * @throws CheckedSoapException
	 */
	@Test
	public void submitUpcSwapAssociateToPrimary() throws Fault, CheckedSoapException {

		UpdateProductReply reply = new UpdateProductReply();
		reply.getProductUpdateResponse().add(0, new ProductUpdateResponse());
		reply.getProductUpdateResponse().get(0).getDetail().add(new Detail());
		reply.getProductUpdateResponse().get(0).getDetail().get(0).setSucessFlag("Y");

		UpcSwap swap = new UpcSwap();
		swap.setSource(swap.new SwappableEndPoint());
		swap.getSource().setItemCode(4L);
		swap.getSource().setProductId(2L);
		swap.setSelectSourcePrimaryUpc(2L);
		swap.setSourceUpc(2L);
		swap.setDestination(swap.new SwappableEndPoint());
		swap.getDestination().setItemCode(2L);
		swap.getDestination().setProductId(2L);
		swap.setMakeDestinationPrimaryUpc(true);
		swap.setDestinationPrimaryUpc(2L);

		Mockito.when(this.productManagementServiceServiceagent.getProductManagementService()).thenReturn(port);
		try {
			Mockito.when(this.port.swapScanCodes(Mockito.any(com.heb.xmlns.ei.swap_scancodes_request.SwapScanCodesRequest.class))).thenReturn(reply);
		} catch (ProviderFaultSchema providerFaultSchema) {
			providerFaultSchema.printStackTrace();
		}		Mockito.when(this.userInfo.getUserId()).thenReturn("User");

		this.productManagementServiceClient.submitUpcSwap(swap);
	}

	/**
	 * Tests warehouse to warehouse upc swap with source associate getting set to destination associate.
	 *
	 * @throws Fault
	 * @throws CheckedSoapException
	 */
	@Test
	public void submitUpcSwapAssociateToAssociate() throws Fault, CheckedSoapException {

		UpdateProductReply reply = new UpdateProductReply();
		reply.getProductUpdateResponse().add(0, new ProductUpdateResponse());
		reply.getProductUpdateResponse().get(0).getDetail().add(new Detail());
		reply.getProductUpdateResponse().get(0).getDetail().get(0).setSucessFlag("Y");

		UpcSwap swap = new UpcSwap();
		swap.setSource(swap.new SwappableEndPoint());
		swap.getSource().setItemCode(4L);
		swap.getSource().setProductId(2L);
		swap.setSelectSourcePrimaryUpc(2L);
		swap.setSourceUpc(2L);
		swap.setDestination(swap.new SwappableEndPoint());
		swap.getDestination().setItemCode(2L);
		swap.getDestination().setProductId(2L);
		swap.setDestinationPrimaryUpc(2L);

		Mockito.when(this.productManagementServiceServiceagent.getProductManagementService()).thenReturn(port);
		try {
			Mockito.when(this.port.swapScanCodes(Mockito.any(com.heb.xmlns.ei.swap_scancodes_request.SwapScanCodesRequest.class))).thenReturn(reply);
		} catch (ProviderFaultSchema providerFaultSchema) {
			providerFaultSchema.printStackTrace();
		}		Mockito.when(this.userInfo.getUserId()).thenReturn("User");

		this.productManagementServiceClient.submitUpcSwap(swap);
	}

	/*
	* getters
	*/

	/**
	 * Tests getServiceAgent.
	 */
	@Test
	public void getServiceAgent() {
		this.productManagementServiceClient.setUri("http://drdapl0046702.heb.com:9579/ProductManagementService");
		Assert.assertNotNull(this.productManagementServiceClient.getServiceAgent());
	}

	/**
	 * Tests getPort.
	 */
	@Test
	public void getPort() {
		this.productManagementServiceClient.setUri("http://drdapl0046702.heb.com:9579/ProductManagementService");
		ProductManagementServicePort portType = this.productManagementServiceClient.getPort();
		Assert.assertNotNull(portType);
	}

	/**
	 * Tests getWebServiceUri.
	 */
	@Test
	public void getWebServiceUri() {
		this.productManagementServiceClient.setUri("http://drdapl0046702.heb.com:9579/ProductManagementService");
		Assert.assertEquals("http://drdapl0046702.heb.com:9579/ProductManagementService",
				this.productManagementServiceClient.getWebServiceUri());
	}

	/**
	 * Tests getAuthentication.
	 */
	@Test
	public void getAuthentication() {
		Authentication authentication = this.productManagementServiceClient.getAuthentication();
		Assert.assertNotNull(authentication.getUSERID());
		Assert.assertNotNull(authentication.getUSERID());
		Assert.assertNotNull(authentication.getPWD());
	}
}
