/*
 * WarehouseMoveUpcSwapTest
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.upcMaintenance;

import com.heb.tim.services.vo.PurchaseOrderVO;
import org.junit.Assert;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class UpcSwapTest {

	/*
	 * sourceUpc
	 */

	@Test
	public void getSourceUpc(){
		UpcSwap upcSwap = new UpcSwap();
		upcSwap.setSourceUpc(7L);
		Assert.assertTrue(upcSwap.getSourceUpc() == 7L);
	}

	/*
	 * isSourcePrimaryUpc
	 */

	@Test
	public void isSourcePrimaryUpc(){
		UpcSwap upcSwap = new UpcSwap();
		upcSwap.setSourcePrimaryUpc(true);
		Assert.assertEquals(upcSwap.isSourcePrimaryUpc(), true);
	}

	/*
	 * selectSourcePrimaryUpc
	 */

	@Test
	public void getSelectSourcePrimaryUpc(){
		UpcSwap upcSwap = new UpcSwap();
		upcSwap.setSelectSourcePrimaryUpc(7L);
		Assert.assertTrue(upcSwap.getSelectSourcePrimaryUpc() == 7L);
	}

	/*
	 * destinationPrimaryUpc
	 */

	@Test
	public void getDestinationPrimaryUpc(){
		UpcSwap upcSwap = new UpcSwap();
		upcSwap.setDestinationPrimaryUpc(7L);
		Assert.assertTrue(upcSwap.getDestinationPrimaryUpc() == 7L);

	}

	/*
	 * makeDestinationPrimaryUpc
	 */

	@Test
	public void isMakeDestinationPrimaryUpc(){
		UpcSwap upcSwap = new UpcSwap();
		upcSwap.setMakeDestinationPrimaryUpc(true);
		Assert.assertEquals(upcSwap.isMakeDestinationPrimaryUpc(), true);
	}

	/*
	 * statusMessage
	 */

	@Test
	public void getStatusMessage(){
		UpcSwap upcSwap = new UpcSwap();
		upcSwap.setStatusMessage("Success");
		Assert.assertEquals(upcSwap.getStatusMessage(), "Success");
	}

	/*
	 * source
	 */

	@Test
	public void getSource(){
		UpcSwap.SwappableEndPoint source = getDefaultSwappableEndPointWithAssociates();
		Assert.assertTrue(source.getProductId() == 7L);
		Assert.assertTrue(source.getItemCode() == 7L);
		Assert.assertEquals(source.getItemDescription(), "Bananas");
		Assert.assertEquals(source.getItemSize(), "lb");
		Assert.assertEquals(source.isOnLiveOrPendingPog(), true);
		Assert.assertEquals(source.getAssociatedUpcList(), Arrays.asList(12L));
		Assert.assertEquals(source.getBalanceOnHand(), 200);
		Assert.assertEquals(source.getBalanceOnOrder(), 200);
		Assert.assertEquals(source.getAssociatedUpcList(), Arrays.asList(12L));

		UpcSwap upcSwap = new UpcSwap();
		upcSwap.setSource(source);

		Assert.assertEquals(upcSwap.getSource(), source);
	}

	/*
	 * destination
	 */

	@Test
	public void getDestination(){
		UpcSwap.SwappableEndPoint destination = getDefaultSwappableEndPointWithAssociates();
		Assert.assertTrue(destination.getProductId() == 7L);
		Assert.assertTrue(destination.getItemCode() == 7L);
		Assert.assertEquals(destination.getItemDescription(), "Bananas");
		Assert.assertEquals(destination.getItemSize(), "lb");
		Assert.assertEquals(destination.isOnLiveOrPendingPog(), true);
		Assert.assertEquals(destination.getAssociatedUpcList(), Arrays.asList(12L));
		Assert.assertEquals(destination.getBalanceOnHand(), 200);
		Assert.assertEquals(destination.getBalanceOnOrder(), 200);
		Assert.assertEquals(destination.getAssociatedUpcList(), Arrays.asList(12L));

		UpcSwap upcSwap = new UpcSwap();
		upcSwap.setDestination(destination);

		Assert.assertEquals(upcSwap.getDestination(), destination);
	}

	/*
	 * findAll
	 */

	@Test
	public void testToString(){
		UpcSwap upcSwap = getDefaultUpcSwap();
		Assert.assertEquals(upcSwap.toString(), "UpcSwap{sourceUpc=7, isSourcePrimaryUpc=true, " +
				"selectSourcePrimaryUpc=7, destinationPrimaryUpc=7, makeDestinationPrimaryUpc=true, " +
				"statusMessage='Success', source=SwappableEndPoint{productId=7, itemCode=7, itemType='null', " +
				"itemDescription='Bananas', itemSize='lb', balanceOnHand=200, balanceOnOrder=200, " +
				"onLiveOrPendingPog=true, productRetailLink=null}, " +
				"destination=SwappableEndPoint{productId=7, itemCode=7, itemType='null', itemDescription='Bananas'," +
				" itemSize='lb', balanceOnHand=200, balanceOnOrder=200, onLiveOrPendingPog=true, " +
				"productRetailLink=null}}");
	}

	/**
	 * Tests whether a purchase order is found within 7 days.
	 */
	@Test
	public void getLastPurchaseOrderInWeekTest() {
		UpcSwap upcSwap = new UpcSwap();
		UpcSwap.SwappableEndPoint endPoint = upcSwap.new SwappableEndPoint();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		ArrayList<PurchaseOrderVO> list = new ArrayList<>();
		PurchaseOrderVO purchaseOrderVOLast = new PurchaseOrderVO();


		Date date = new Date(new Date().getTime() + 3600);
		String formattedDate = simpleDateFormat.format(date);
		purchaseOrderVOLast.setArrivalDate(formattedDate);
		list.add(purchaseOrderVOLast);

		endPoint.setPurchaseOrders(list);

		PurchaseOrderVO returnPurchaseOrderVO = endPoint.getLastPurchaseOrderInWeek();
		Assert.assertNotNull("Purchase order is null", returnPurchaseOrderVO);
	}

	/**
	 * Tests whether a purchase Order VO does not have an arrival date.
	 */
	@Test
	public void getLastPurchaseOrderInWeekNullTest() {
		UpcSwap upcSwap = new UpcSwap();
		UpcSwap.SwappableEndPoint endPoint = upcSwap.new SwappableEndPoint();

		ArrayList<PurchaseOrderVO> list = new ArrayList<>();
		PurchaseOrderVO purchaseOrderVOFirst = new PurchaseOrderVO();
		list.add(purchaseOrderVOFirst);
		endPoint.setPurchaseOrders(list);

		PurchaseOrderVO returnPurchaseOrderVO = endPoint.getLastPurchaseOrderInWeek();
		Assert.assertNull("Purchase order isn't null", returnPurchaseOrderVO);
	}

	/**
	 * Tests whether a purchase order has a return date.
	 */
	@Test
	public void getPurchaseOrderDisplayTextTest() {
		UpcSwap upcSwap = new UpcSwap();
		UpcSwap.SwappableEndPoint endPoint = upcSwap.new SwappableEndPoint();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		ArrayList<PurchaseOrderVO> list = new ArrayList<>();
		PurchaseOrderVO purchaseOrderVOLast = new PurchaseOrderVO();


		Date date = new Date(new Date().getTime() + 3600);
		String formattedDate = simpleDateFormat.format(date);
		purchaseOrderVOLast.setArrivalDate(formattedDate);
		list.add(purchaseOrderVOLast);

		endPoint.setPurchaseOrders(list);
		String returnString = endPoint.getPurchaseOrderDisplayText();
		Assert.assertNotNull("String returned null", returnString);
	}

	/**
	 * Tests whether a purchase Order VO does not have an arrival date.
	 */
	@Test
	public void getPurchaseOrderDisplayTextNullTest() {
		UpcSwap upcSwap = new UpcSwap();
		UpcSwap.SwappableEndPoint endPoint = upcSwap.new SwappableEndPoint();

		ArrayList<PurchaseOrderVO> list = new ArrayList<>();
		PurchaseOrderVO purchaseOrderVOFirst = new PurchaseOrderVO();
		list.add(purchaseOrderVOFirst);
		endPoint.setPurchaseOrders(list);

		String returnString = endPoint.getPurchaseOrderDisplayText();
		Assert.assertEquals("No PO", returnString);
	}

	/*
	 * Support functions
	 */

	private UpcSwap getDefaultUpcSwap(){
		UpcSwap upcSwap = new UpcSwap();
		upcSwap.setSourceUpc(7L);
		upcSwap.setSelectSourcePrimaryUpc(7L);
		upcSwap.setDestinationPrimaryUpc(7L);
		upcSwap.setSourcePrimaryUpc(true);
		upcSwap.setMakeDestinationPrimaryUpc(true);
		upcSwap.setStatusMessage("Success");
		upcSwap.setSource(getDefaultSwappableEndPointWithAssociates());
		upcSwap.setDestination(getDefaultSwappableEndPointWithAssociates());
		return upcSwap;
	}

	private UpcSwap.SwappableEndPoint getDefaultSwappableEndPointWithAssociates(){
		UpcSwap upcSwap = new UpcSwap();
		UpcSwap.SwappableEndPoint endPoint = upcSwap.new SwappableEndPoint();
		endPoint.setProductId(7L);
		endPoint.setItemCode(7L);
		endPoint.setItemDescription("Bananas");
		endPoint.setItemSize("lb");
		endPoint.setOnLiveOrPendingPog(true);
		endPoint.setAssociatedUpcList(Arrays.asList(12L));
		endPoint.setBalanceOnHand(200);
		endPoint.setBalanceOnOrder(200);
		return endPoint;
	}
}
