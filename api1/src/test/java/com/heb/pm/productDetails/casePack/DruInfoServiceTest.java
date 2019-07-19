package com.heb.pm.productDetails.casePack;

import com.heb.pm.audit.AuditService;
import com.heb.pm.entity.ItemMaster;
import com.heb.pm.entity.ItemMasterKey;
import com.heb.pm.repository.ItemMasterRepository;
import com.heb.pm.ws.ProductManagementServiceClient;
import com.heb.util.audit.AuditRecord;
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
 * Created by s753601
 * @version 2.7.0
 */
public class DruInfoServiceTest {

	@InjectMocks
	DruInfoService druInfoService;

	@Mock
	ItemMasterRepository itemMasterRepository;

	@Mock
	ProductManagementServiceClient productManagementServiceClient;

	@Mock
	AuditService auditService;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

	/**
	 * Tests the updateDruQuantity method
	 * @throws Exception
	 */
	@Test
	public void updateDruQuantity() {
		ItemMaster test = getGenericItemMaster();
		test.getKey().setItemCode(1L);
		Mockito.when(this.itemMasterRepository.findOne(getGenericItemMaster().getKey())).thenReturn(test);
		ItemMaster result = this.druInfoService.updateDruQuantity(getGenericItemMaster());
		Assert.assertEquals(test, result);
	}

	/**
	 * Tests the getDruAuditInformation method
	 */
	@Test
	public void getDruAuditInformationTest(){
		List<AuditRecord> test = getGenericRecordList();
		Mockito.when(this.auditService.getDruAuditInformation(Mockito.any(ItemMasterKey.class), Mockito.anyString()))
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
		key.setItemCode(2L);
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