/*
 *  DsdToBothServiceTest
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
import com.heb.pm.entity.ProdItem;
import com.heb.pm.entity.ProductMaster;
import com.heb.pm.entity.SellingUnit;
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
 * Tests DsdToBothService.
 *
 * @author s573181
 * @since 2.1.0
 */
public class DsdToBothServiceTest {


	@InjectMocks
	private DsdToBothService service;

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
	 * Tests update.
	 *
	 * @throws CheckedSoapException checked exception.
	 */
	@Test
	public void update() throws CheckedSoapException {
		this.upcSwapUtils.setAssociatedUpcRepository(this.getAssociatedUpcRepository());
		this.upcSwapUtils.setItemMasterRepository(this.getItemMasterRepository());
		this.upcSwapUtils.setTimRepository(this.getTimRepository());

		this.productManagementServiceClient.setUserInfo(CommonMocks.getUserInfo());

		this.service.setUpcSwapUtils(this.upcSwapUtils);
		this.service.setProductManagementServiceClient(productManagementServiceClient);

		Mockito.doNothing().when(this.productManagementServiceClient).
				submitDsdToBoth(this.getDefaultUpcSwapList().get(0));
		Assert.assertNotNull(this.service.update(this.getDefaultUpcSwapList()));

	}

	/*
	 * Tests findAll.
	 */
	@Test
	public void findAll() {

		UpcSwapUtils upcSwapUtils = new UpcSwapUtils();
		upcSwapUtils.setAssociatedUpcRepository(this.getAssociatedUpcRepository());
		upcSwapUtils.setTimRepository(this.getTimRepository());
		upcSwapUtils.setItemMasterRepository(this.getItemMasterRepository());
		 DsdToBothService service = new DsdToBothService();
		service.setUpcSwapUtils(upcSwapUtils);
		Assert.assertNotNull(service.findAll(this.getDefaultUpcList(), this.getDefaultItemList()));
	}

	/**
	 * Returns a default list containing a DSD only UPC.
	 *
	 * @return a default list containing a DSD only UPC.
	 */
	private List<Long> getDefaultUpcList(){
		List<Long> defaultUpcList = new ArrayList<>();
		defaultUpcList.add(20302500000L);
		return defaultUpcList;
	}

	/**
	 * Returns a default list containing an item code only UPC.
	 *
	 * @return a default list containing an item code only UPC.
	 */
	private List<Long> getDefaultItemList(){
		List<Long> defaultItemList = new ArrayList<>();
		defaultItemList.add(72553L);
		return defaultItemList;	}

	/**
	 * Returns the default DSD AssociatedUpc to test with.
	 *
	 * @return The default DSD AssociatedUpc to test with.
	 */
	private AssociatedUpc getDsdAssociatedUpc() {
		AssociatedUpc associatedUpc = new AssociatedUpc();
		associatedUpc.setUpc(20302500000L);
		associatedUpc.setPrimaryUpc(this.getTestPrimaryUpc());
		return associatedUpc;
	}
	/**
	 * Returns the default PrimaryUpc to test with.
	 *
	 * @return The default PrimaryUpc to test wtih.
	 */
	private PrimaryUpc getTestPrimaryUpc() {
		PrimaryUpc primaryUpc = new PrimaryUpc();
		primaryUpc.setUpc(20302500000L);
		AssociatedUpc upc = new AssociatedUpc();
		upc.setUpc(2035L);
		List<AssociatedUpc> upcs = new ArrayList<>();
		primaryUpc.setAssociateUpcs(upcs);
		return primaryUpc;
	}

	/**
	 * Returns an ItemMasterKey for a non DSD item code.
	 *
	 * @return an ItemMasterKey for a non DSD item code.
	 */
	private ItemMasterKey getDefaultItemMasterKey(){
		ItemMasterKey key = new ItemMasterKey();
		key.setItemType("ITMCD");
		key.setItemCode(72553L);
		return key;
	}

	/**
	 * Returns an ItemMaster for a non DSD item code.
	 *
	 * @return an ItemMaster for a non DSD item code.
	 */
	private ItemMaster getDefaultItemMaster(){
		ItemMaster itemMaster = new ItemMaster();
		itemMaster.setKey(this.getDefaultItemMasterKey());
		itemMaster.setOrderingUpc(3025);
		itemMaster.setPrimaryUpc(this.getTestPrimaryUpc());
		List<ProdItem> prodItems = new ArrayList<>();
		ProdItem prodItem = new ProdItem();
		ProductMaster productMaster = new ProductMaster();
		productMaster.setProdId(72553L);
		prodItem.setProductMaster(productMaster);
		prodItems.add(prodItem);
		itemMaster.setProdItems(prodItems);
		PrimaryUpc primaryUpc = new PrimaryUpc();
		List<AssociatedUpc> associatedUpcs = new ArrayList<>();
		AssociatedUpc associatedUpc  = new AssociatedUpc();
		SellingUnit sellingUnit = new SellingUnit();
		sellingUnit.setPrimaryUpc(false);
		associatedUpc.setUpc(20302500000L);
		associatedUpc.setSellingUnit(sellingUnit);
		associatedUpcs.add(associatedUpc);
		itemMaster.setPrimaryUpc(primaryUpc);
		primaryUpc.setAssociateUpcs(associatedUpcs);
		return itemMaster;
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
		upcSwap.setSourcePrimaryUpc(true);
		upcSwap.setDestinationPrimaryUpc(3025L);
		upcSwap.setMakeDestinationPrimaryUpc(false);
		UpcSwap.SwappableEndPoint source = new UpcSwap().new SwappableEndPoint();
		source.setProductId(894446L);
		source.setItemCode(20302500000L);

		UpcSwap.SwappableEndPoint destination = new UpcSwap().new SwappableEndPoint();
		destination.setProductId(894446L);
		destination.setItemCode(72553L);
		destination.setItemType(ItemMasterKey.WAREHOUSE);
		upcSwap.setSource(source);
		upcSwap.setDestination(destination);

		upcSwapList.add(upcSwap);
		return upcSwapList;
	}

	/**
	 * Returns an AssociatedUpcRepository to test with.
	 *
	 * @return an AssociatedUpcRepository to test with
	 */
	private AssociatedUpcRepository getAssociatedUpcRepository(){
		AssociatedUpcRepository repository = Mockito.mock(AssociatedUpcRepository.class);
		AssociatedUpc associatedUpc = this.getDsdAssociatedUpc();
		Mockito.when(repository.findOne(this.getDefaultItemList().get(0))).thenReturn(associatedUpc);

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
	 * Returns a ItemMasterRepository to test with.
	 *
	 * @return a ItemMasterRepository to test with.
	 */
	private ItemMasterRepository getItemMasterRepository(){
		ItemMasterRepository repository = Mockito.mock(ItemMasterRepository.class);
		Mockito.when(repository.findOne(Mockito.any())).thenReturn(this.getDefaultItemMaster());
		return repository;
	}
}