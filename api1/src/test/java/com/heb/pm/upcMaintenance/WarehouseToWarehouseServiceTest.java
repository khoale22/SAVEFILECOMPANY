/*
 * WarehouseUpcSwapServiceTest
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.upcMaintenance;

import com.heb.pm.entity.*;
import com.heb.pm.repository.AssociatedUpcRepository;
import com.heb.pm.repository.ItemMasterRepository;
import com.heb.pm.repository.TimRepository;
import com.heb.pm.ws.CheckedSoapException;
import com.heb.pm.ws.ProductManagementServiceClient;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import testSupport.CommonMocks;

import java.util.ArrayList;
import java.util.List;

/**
 * Tests WarehouseToWarehouseService.
 *
 * @author s573181
 * @since 2.1.0
 */
public class WarehouseToWarehouseServiceTest {

	@InjectMocks
	private WarehouseToWarehouseService service;

	@Mock
	private ProductManagementServiceClient productManagementServiceClient;

	@Mock
	private UpcSwapUtils upcSwapUtils;

	private static final String ACTION_CODE = "actionCode";

	@Before
	public void setup(){
		MockitoAnnotations.initMocks(this);
	}

	/**
	 * Tests findAll.
	 */
	@Test
	public void findAll() {
		UpcSwapUtils upcSwapUtils = new UpcSwapUtils();
		upcSwapUtils.setAssociatedUpcRepository(this.getAssociatedUpcRepository());
		upcSwapUtils.setTimRepository(this.getTimRepository());
		upcSwapUtils.setItemMasterRepository(this.getItemMasterRepository());
		WarehouseToWarehouseService service = new WarehouseToWarehouseService();
		service.setUpcSwapUtils(upcSwapUtils);
		Assert.assertNotNull(service.findAll(this.getDefaultSourceUpcList(),this.getDefaultDestinationWarehouseList()));
	}

	/**
	 * Tests Update
	 *
	 * @throws CheckedSoapException
	 */
	@Test
	public void update() throws CheckedSoapException {
		this.upcSwapUtils.setAssociatedUpcRepository(this.getAssociatedUpcRepository());
		this.upcSwapUtils.setTimRepository(this.getTimRepository());
		this.upcSwapUtils.setItemMasterRepository(this.getItemMasterRepository());

		this.productManagementServiceClient.setUserInfo(CommonMocks.getUserInfo());

		this.service.setUpcSwapUtils(upcSwapUtils);
		this.service.setProductManagementServiceClient(productManagementServiceClient);
		Mockito.doNothing().when(productManagementServiceClient).submitUpcSwap(this.getDefaultUpcSwapList().get(0));
		Assert.assertNotNull(service.update(this.getDefaultUpcSwapList()));
	}


	/**
	 * Support functions.
	 */
	/**
	 * Returns a default UPC list of type both.
	 *
	 * @return a default UPC list of type both.
	 */
	private List<Long> getDefaultSourceUpcList(){
		List<Long> upcs = new ArrayList<>();
		upcs.add(4088L);
		return upcs;
	}

	/**
	 * Returns a default UPC list of type both.
	 *
	 * @return a default UPC list of type both.
	 */
	private List<Long> getDefaultDestinationWarehouseList(){
		List<Long> upcs = new ArrayList<>();
		upcs.add(428557L);
		return upcs;
	}

	/**
	 * Returns an AssociatedUpcRepository to test with.
	 *
	 * @return an AssociatedUpcRepository to test with
	 */
	private AssociatedUpcRepository getAssociatedUpcRepository() {
		AssociatedUpcRepository repository = Mockito.mock(AssociatedUpcRepository.class);
		AssociatedUpc associatedUpc = new AssociatedUpc();
		associatedUpc.setUpc(4088L);
		List<ItemMaster> itemMasters  = new ArrayList<>();
		ItemMaster itemMaster = new ItemMaster();
		itemMaster.setDescription("YELLOW BANANAS                ");
		ItemMasterKey key = new ItemMasterKey();
		key.setItemCode(428557L);
		key.setItemType(ItemMasterKey.WAREHOUSE);
		itemMaster.setKey(key);
		itemMasters.add(itemMaster);
		PrimaryUpc primaryUpc = new PrimaryUpc();
		primaryUpc.setItemMasters(itemMasters);
		primaryUpc.setUpc(0);
		SellingUnit sellingUnit = new SellingUnit();
		sellingUnit.setUpc(4088);
		sellingUnit.setProdId(325164);
		sellingUnit.setTagSize("EACH  ");
		sellingUnit.setPrimaryUpc(false);
		ProductMaster productMaster = new ProductMaster();
		productMaster.setRetailLink(0);
		sellingUnit.setProductMaster(productMaster);
		associatedUpc.setSellingUnit(sellingUnit);
		associatedUpc.setPrimaryUpc(primaryUpc);
		Mockito.when(repository.findOne(Mockito.anyLong())).thenReturn(associatedUpc);
		return repository;
	}

	/**
	 * Returns an ItemMasterRepository to tests with.
	 *
	 * @return an ItemMasterRepository to tests with.
	 */
	private ItemMasterRepository getItemMasterRepository(){
		ItemMasterRepository repository = Mockito.mock(ItemMasterRepository.class);

		Mockito.when(repository.findOne(Mockito.any(ItemMasterKey.class))).thenReturn(null);
		return repository;
	}


	/**
	 * Returns a TimRepository to test with.
	 *
	 * @return a TimRepository to test with.
	 */
	private TimRepository getTimRepository(){
		TimRepository repository = Mockito.mock(TimRepository.class);
		Mockito.when(repository.getPurchaseOrders(Mockito.anyLong())).thenReturn(null);
		return repository;
	}

	/**
	 * Returns a default Upc Swap list.
	 *
	 * @return a default Upc Swap list.
	 */
	private List<UpcSwap> getDefaultUpcSwapList(){
		UpcSwap upcSwap = new UpcSwap();
		upcSwap.setSourceUpc(4088L);
		upcSwap.setSourcePrimaryUpc(true);
		upcSwap.setMakeDestinationPrimaryUpc(false);
		upcSwap.setDestinationPrimaryUpc(4011L);
		upcSwap.setSelectSourcePrimaryUpc(5783604688L);

		UpcSwap.SwappableEndPoint source = new UpcSwap().new SwappableEndPoint();
		source.setItemCode(30405L);
		source.setProductId(325164L);

		UpcSwap.SwappableEndPoint destination = new UpcSwap().new SwappableEndPoint();
		destination.setItemCode(428557L);
		destination.setProductId(325164L);

		upcSwap.setSource(source);
		upcSwap.setDestination(destination);
		List<UpcSwap> upcSwaps = new ArrayList<>();
		upcSwaps.add(upcSwap);
		return upcSwaps;
	}
}