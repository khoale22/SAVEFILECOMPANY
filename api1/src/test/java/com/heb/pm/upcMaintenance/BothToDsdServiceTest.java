/*
 *  BothToDsdServiceTest
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.upcMaintenance;

import com.heb.pm.entity.AssociatedUpc;
import com.heb.pm.entity.ItemMaster;
import com.heb.pm.entity.ItemMasterKey;
import com.heb.pm.entity.PrimaryUpc;
import com.heb.pm.entity.ProductMaster;
import com.heb.pm.entity.SellingUnit;
import com.heb.pm.repository.AssociatedUpcRepository;
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
 * Tests BothToDsdService.
 *
 * @author s573181
 * @since 2.1.0
 */
public class BothToDsdServiceTest {

	@InjectMocks
	private BothToDsdService service;

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
		List<Long> bothList = this.getDefaultBothList();

		UpcSwapUtils upcSwapUtils = new UpcSwapUtils();
		upcSwapUtils.setAssociatedUpcRepository(this.getAssociatedUpcRepository());
		upcSwapUtils.setTimRepository(this.getTimRepository());

		BothToDsdService service = new BothToDsdService();
		service.setUpcSwapUtils(upcSwapUtils);
		Assert.assertNotNull(service.findAll(bothList));
	}

	/**
	 * Tests update.
	 */
	@Test
	public void update() throws CheckedSoapException {
		this.upcSwapUtils.setAssociatedUpcRepository(this.getAssociatedUpcRepository());
		this.upcSwapUtils.setTimRepository(this.getTimRepository());

		this.productManagementServiceClient.setUserInfo(CommonMocks.getUserInfo());

		this.service.setUpcSwapUtils(this.upcSwapUtils);
		this.service.setProductManagementServiceClient(this.productManagementServiceClient);
		Mockito.doThrow(new CheckedSoapException()).when(this.productManagementServiceClient).submitBothToDsd(
				this.getDefaultUpcSwapList().get(0));
		Assert.assertNotNull(this.service.update(this.getDefaultUpcSwapList()));

	}

	/**
	 * Support functions.
	 */
	/**
	 * Returns a default UPC list of type both.
	 *
	 * @return a default UPC list of type both.
	 */
	private List<Long> getDefaultBothList(){
		List<Long> bothList = new ArrayList<>();
		bothList.add(20302500000L);
		return bothList;
	}

	/**
	 * Returns an AssociatedUpcRepository to test with.
	 *
	 * @return an AssociatedUpcRepository to test with
	 */
	private AssociatedUpcRepository getAssociatedUpcRepository(){
		AssociatedUpcRepository repository = Mockito.mock(AssociatedUpcRepository.class);
		AssociatedUpc associatedUpc = new AssociatedUpc();
		associatedUpc.setUpc(20302500000L);
		List<ItemMaster> itemMasters = new ArrayList<>();

		ItemMaster dsdItem = new ItemMaster();
		ItemMasterKey dsdKey = new ItemMasterKey();
		dsdKey.setItemType(ItemMasterKey.DSD);
		dsdKey.setItemCode(3025L);
		dsdItem.setKey(dsdKey);
		dsdItem.setDescription("CHILACA PEPPER                ");

		ItemMaster whsItem = new ItemMaster();
		ItemMasterKey whsKey = new ItemMasterKey();
		whsKey.setItemType(ItemMasterKey.WAREHOUSE);
		whsKey.setItemCode(72553L);
		whsItem.setKey(whsKey);
		whsItem.setDescription("CHILACA PEPPER                ");

		itemMasters.add(whsItem);
		itemMasters.add(dsdItem);
		PrimaryUpc primaryUpc = new PrimaryUpc();
		primaryUpc.setItemMasters(itemMasters);

		SellingUnit sellingUnit = new SellingUnit();
		sellingUnit.setProdId(894446);
		sellingUnit.setTagSize("LB  ");
		sellingUnit.setPrimaryUpc(false);

		ProductMaster productMaster = new ProductMaster();
		productMaster.setRetailLink(0);
		sellingUnit.setProductMaster(productMaster);
		associatedUpc.setSellingUnit(sellingUnit);
		associatedUpc.setPrimaryUpc(primaryUpc);
		Mockito.when(repository.findOne(this.getDefaultBothList().get(0))).thenReturn(associatedUpc);
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
	 * Returns a default UPC swap list.
	 *
	 * @return a default UPC swap list.
	 */
	private List<UpcSwap> getDefaultUpcSwapList(){
		List<UpcSwap> upcSwapList = new ArrayList<>();
		UpcSwap upcSwap = new UpcSwap();
		upcSwap.setSourceUpc(20302500000L);
		upcSwap.setSourcePrimaryUpc(false);
		upcSwap.setSelectSourcePrimaryUpc(3025L);
		UpcSwap.SwappableEndPoint source = new UpcSwap().new SwappableEndPoint();
		source.setProductId(894446L);
		source.setItemCode(72553L);
		UpcSwap.SwappableEndPoint destination = new UpcSwap().new SwappableEndPoint();
		upcSwap.setSource(source);
		upcSwap.setDestination(destination);

		upcSwapList.add(upcSwap);
		return upcSwapList;
	}
}