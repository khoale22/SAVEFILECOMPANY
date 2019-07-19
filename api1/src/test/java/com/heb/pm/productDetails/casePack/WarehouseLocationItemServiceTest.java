package com.heb.pm.productDetails.casePack;

import com.heb.pm.entity.FlowType;
import com.heb.pm.entity.OrderQuantityType;
import com.heb.pm.entity.WarehouseLocationItem;
import com.heb.pm.entity.WarehouseLocationItemKey;
import com.heb.pm.repository.FlowTypeRepository;
import com.heb.pm.repository.OrderQuantityTypeRepository;
import com.heb.pm.repository.WarehouseLocationItemRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import java.util.ArrayList;
import java.util.List;

/**
 * Unit tests for the WarehouseLocationItemService
 * @author  s753601
 * @version 2.8.0
 */
public class WarehouseLocationItemServiceTest {

	private String DEFAULT_ITEM_TYPE = "ITMCD";
	private long DEFAULT_ITEM_CODE=1L;

	@InjectMocks
	WarehouseLocationItemService warehouseLocationItemService;

	@Mock
	WarehouseLocationItemRepository warehouseLocationItemRepository;

	@Mock
	OrderQuantityTypeRepository orderQuantityTypeRepository;

	@Mock
	FlowTypeRepository flowTypeRepository;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

	/**
	 * Tests the finadAllWarehouseLocationItemByItemCode method
	 */
	@Test
	public void findAllWarehouseLocationItemsByItemCodeTest(){
		List<WarehouseLocationItem> test = getDefaultWarehouseLocationItems();
		Mockito.when(this.warehouseLocationItemRepository.findByKeyItemCodeAndKeyItemType(Mockito.anyLong(), Mockito.anyString())).thenReturn(getDefaultWarehouseLocationItems());
		List<WarehouseLocationItem> result = this.warehouseLocationItemService.findAllWarehouseLocationItemsByItemCode(DEFAULT_ITEM_CODE,DEFAULT_ITEM_TYPE);
		Assert.assertEquals(test, result);
	}

	/**
	 * Tests the findAllQuantityTypes method
	 */
	@Test
	public void findAllQuantityTypesTest(){
		List<OrderQuantityType> test = getDefaultOrderQuantityTypeList();
		Mockito.when(this.orderQuantityTypeRepository.findAll()).thenReturn(getDefaultOrderQuantityTypeList());
		List<OrderQuantityType> result = this.warehouseLocationItemService.findAllOrderQuantityTypes();
		Assert.assertEquals(test, result);
	}

	/**
	 * Tests findAllFlowTypes method
	 */
	@Test
	public void findAllFlowTypesTest(){
		List<FlowType> test = getDefaultFlowTypeList();
		Mockito.when(this.flowTypeRepository.findAll()).thenReturn(getDefaultFlowTypeList());
		List<FlowType> result = this.warehouseLocationItemService.findAllFlowTypes();
		Assert.assertEquals(test, result);
	}

	/**
	 * Generates the generic WarehouseLocationItem list
	 * @return the generic WarehouseLocationItem list
	 */
	private List<WarehouseLocationItem> getDefaultWarehouseLocationItems(){
		ArrayList<WarehouseLocationItem> list = new ArrayList<>();
		list.add(getDefaultWarehouseLocationItem());
		WarehouseLocationItem wli = getDefaultWarehouseLocationItem();
		wli.setKey(getOtherWarehouseLocationItemKey());
		list.add(wli);
		return list;
	}

	/**
	 * Generates the generic WarehouseLocationItem
	 * @return the generic WarehouseLocationItem
	 */
	private WarehouseLocationItem getDefaultWarehouseLocationItem(){
		WarehouseLocationItem wli = new WarehouseLocationItem();
		wli.setKey(getDefaultWarehouseLocationItemKey());
		return wli;
	}

	/**
	 * Generates the generic WarehouseLocationItemKey
	 * @return the generic WarehouseLocationItemKey
	 */
	private WarehouseLocationItemKey getDefaultWarehouseLocationItemKey(){
		WarehouseLocationItemKey key = new WarehouseLocationItemKey();
		key.setItemCode(1L);
		key.setItemType("ITMCD");
		key.setWarehouseNumber(1);
		key.setWarehouseType("W");
		return key;
	}

	/**
	 * Generates the other WarehouseLocationItemKey
	 * @return the other WarehouseLocationItemKey
	 */
	private WarehouseLocationItemKey getOtherWarehouseLocationItemKey(){
		WarehouseLocationItemKey key = new WarehouseLocationItemKey();
		key.setItemCode(2L);
		key.setItemType("ITMCD");
		key.setWarehouseNumber(2);
		key.setWarehouseType("W");
		return key;
	}

	/**
	 * Generates the generic OrderQuantityType list
	 * @return generic OrderQuantityType list
	 */
	private List<OrderQuantityType> getDefaultOrderQuantityTypeList(){
		ArrayList<OrderQuantityType> list = new ArrayList<>();
		list.add(getDefaultOrderQuantityType());
		return list;
	}

	/**
	 * Generates the generic OrderQuantityType
	 * @return generic OrderQuantityType
	 */
	private OrderQuantityType getDefaultOrderQuantityType(){
		OrderQuantityType oqt = new OrderQuantityType();
		oqt.setDescription("CASES");
		oqt.setAbbreviation("CASES");
		oqt.setId("B");
		return oqt;
	}

	/**
	 * Generates the generic flow type list
	 * @return generic flow type list
	 */
	private List<FlowType> getDefaultFlowTypeList(){
		ArrayList<FlowType> list = new ArrayList<>();
		list.add(getDefaultFlowType());
		return list;
	}

	/**
	 * Generates the generic FlowType
	 * @return generic FlowType
	 */
	private FlowType getDefaultFlowType(){
		FlowType ft = new FlowType();
		ft.setId(" ");
		ft.setAbbreviation("DEF");
		ft.setDescription("DEFAULT");
		return ft;
	}
}