package com.heb.pm.productDetails.casePack;

import com.heb.pm.entity.FlowType;
import com.heb.pm.entity.ItemWarehouseComments;
import com.heb.pm.entity.ItemWarehouseCommentsKey;
import com.heb.pm.entity.OrderQuantityType;
import com.heb.pm.entity.WarehouseLocationItem;
import com.heb.pm.entity.WarehouseLocationItemKey;
import com.heb.util.controller.NonEmptyParameterValidator;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.context.MessageSource;
import testSupport.CommonMocks;
import java.util.ArrayList;
import java.util.List;

/**
 * Unit tests for the WarehouseLocationItemController
 * Created by s753601
 * @version 2.8.0
 */
public class WarehouseLocationItemControllerTest {

	private String DEFAULT_ITEM_TYPE = "ITMCD";
	private long DEFAULT_ITEM_CODE=1L;

	@InjectMocks
	WarehouseLocationItemController warehouseLocationItemController;

	@Mock
	WarehouseLocationItemService warehouseLocationItemService;

	/**
	 * Runs before tests to set up mocks
	 */
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		this.warehouseLocationItemController.setUserInfo(CommonMocks.getUserInfo());
		this.warehouseLocationItemController.setParameterValidator(new NonEmptyParameterValidator());
		this.warehouseLocationItemController.setMessageSource(Mockito.mock(MessageSource.class));
	}

	/**
	 * Tests the findByItemId method
	 */
	@Test
	public void findByItemId(){
		List<WarehouseLocationItem> test = getDefaultWarehouseLocationItems();
		Mockito.when(this.warehouseLocationItemService.findAllWarehouseLocationItemsByItemCode(Mockito.anyLong(), Mockito.anyString())).thenReturn(getDefaultWarehouseLocationItems());
		List<WarehouseLocationItem> result = this.warehouseLocationItemController.findByItemId(DEFAULT_ITEM_CODE, DEFAULT_ITEM_TYPE, CommonMocks.getServletRequest());
		Assert.assertEquals(test, result);
	}

	/**
	 * Tests the findAllOrderQuantityTypes method
	 */
	@Test
	public void findAllOrderQuantityType(){
		List<OrderQuantityType> test = getDefaultOrderQuantityTypeList();
		Mockito.when(this.warehouseLocationItemService.findAllOrderQuantityTypes()).thenReturn(getDefaultOrderQuantityTypeList());
		List<OrderQuantityType> result = this.warehouseLocationItemController.findAllOrderQuantityType(CommonMocks.getServletRequest());
		Assert.assertEquals(test, result);
	}

	/**
	 * Tests the findAllFlowTypes method
	 */
	@Test
	public void findAllFlowType(){
		List<FlowType> test = getDefaultFlowTypeList();
		Mockito.when(this.warehouseLocationItemService.findAllFlowTypes()).thenReturn(getDefaultFlowTypeList());
		List<FlowType> result = this.warehouseLocationItemController.findAllFlowType(CommonMocks.getServletRequest());
		Assert.assertEquals(test, result);
	}

	/**
	 * Tests the findAllItemWarehouseComments method
	 */
	@Test
	public void findAllItemWarehouseCommentsTest(){
		List<ItemWarehouseComments> test = getDefaultItemWarehouseComments();
		Mockito.when(this.warehouseLocationItemService.findAllItemWarehouseCommentsByItemAndWarehouse(Mockito.any(ItemWarehouseCommentsKey.class))).
				thenReturn(getDefaultItemWarehouseComments());
		List<ItemWarehouseComments> result = this.warehouseLocationItemController.findAllItemWarehouseCommentsByItemAndWarehouse(
				new ItemWarehouseCommentsKey(), CommonMocks.getServletRequest());
		Assert.assertEquals(test, result);
	}

	/**
	 * Generates the generic WarehouseLocationItem list
	 * @return the generic WarehouseLocationItem list
	 */
	private List<WarehouseLocationItem> getDefaultWarehouseLocationItems(){
		ArrayList<WarehouseLocationItem> list = new ArrayList<>();
		WarehouseLocationItem wli = new WarehouseLocationItem();
		WarehouseLocationItemKey key = new WarehouseLocationItemKey();
		key.setItemCode(1L);
		wli.setKey(key);
		list.add(wli);
		return list;
	}

	/**
	 * Generates the generic OrderQuantityType list for testing
	 * @return generic OrderQuantityType list
	 */
	private List<OrderQuantityType> getDefaultOrderQuantityTypeList(){
		ArrayList<OrderQuantityType> list = new ArrayList<>();
		list.add(new OrderQuantityType());
		return list;
	}


	/**
	 * Generates the generic flow type list
	 * @return generic flow type list
	 */
	private List<FlowType> getDefaultFlowTypeList(){
		ArrayList<FlowType> list = new ArrayList<>();
		list.add(new FlowType());
		return list;
	}

	/**
	 * Generates the generic ItemWarehouseComments list
	 * @return generic ItemWarehouseComments list
	 */
	private List<ItemWarehouseComments> getDefaultItemWarehouseComments(){
		ArrayList<ItemWarehouseComments> list = new ArrayList<>();
		list.add(new ItemWarehouseComments());
		return list;
	}
}