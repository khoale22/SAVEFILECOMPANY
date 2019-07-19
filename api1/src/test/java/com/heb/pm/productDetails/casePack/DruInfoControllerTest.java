package com.heb.pm.productDetails.casePack;

import com.heb.pm.entity.ItemMaster;
import com.heb.pm.entity.ItemMasterKey;
import com.heb.util.audit.AuditRecord;
import com.heb.util.controller.ModifiedEntity;
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

import static org.junit.Assert.*;

/**
 * Created by s753601
 * @version 2.7.0
 */
public class DruInfoControllerTest {
	@InjectMocks
	DruInfoController druInfoController;

	@Mock
	DruInfoService druInfoService;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		this.druInfoController.setUserInfo(CommonMocks.getUserInfo());
		this.druInfoController.setParameterValidator(new NonEmptyParameterValidator());
		this.druInfoController.setMessageSource(Mockito.mock(MessageSource.class));
	}

	@Test
	public void saveDruInfo() throws Exception {
		ItemMaster test = getGenericItemMaster();
		test.getKey().setItemCode(2L);
		Mockito.when(this.druInfoService.updateDruQuantity(getGenericItemMaster())).thenReturn(test);
		ModifiedEntity<ItemMaster> result = this.druInfoController.saveDruInfo(getGenericItemMaster(), CommonMocks.getServletRequest());
		Assert.assertEquals(test, result.getData());
	}

	/**
	 * Tests the getDruAuditInfo method
	 */
	@Test
	public void getDruAuditInfoTest(){
		List<AuditRecord> test = getGenericRecordList();
		Mockito.when(this.druInfoService.getDruAuditInformation(Mockito.any(ItemMasterKey.class), Mockito.anyString()))
				.thenReturn(getGenericRecordList());
		List<AuditRecord> result = this.druInfoService.getDruAuditInformation(getGenericItemMaster().getKey(), "Test");
		Assert.assertEquals(test, result);
	}

	/**
	 * Create a generic Item master for testing
	 * @return
	 */
	private ItemMaster getGenericItemMaster(){
		ItemMaster im = new ItemMaster();
		ItemMasterKey key = new ItemMasterKey();
		key.setItemType("test1");
		key.setItemCode(1L);
		im.setKey(key);
		return im;
	}

	/**
	 * Create a generic list of Audit Records
	 * @return
	 */
	private List<AuditRecord> getGenericRecordList(){
		return new ArrayList<AuditRecord>();
	}

}