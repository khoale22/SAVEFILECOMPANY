package com.heb.pm.productDetails.casePack;

import com.heb.pm.audit.AuditService;
import com.heb.pm.entity.ItemMaster;
import com.heb.pm.entity.ItemMasterKey;
import com.heb.pm.repository.ItemMasterRepository;
import com.heb.pm.ws.ProductManagementServiceClient;
import com.heb.util.audit.AuditRecord;
import com.heb.util.ws.SoapException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class for updating DRU Attributes and retrieving DRU Audits
 * @author s753601
 * @since 2.7.0
 */
@Service
public class DruInfoService {

	@Autowired
	private ProductManagementServiceClient productManagementServiceClient;

	@Autowired
	private ItemMasterRepository itemMasterRepository;

	@Autowired
	private AuditService auditService;


	/**
	 * Update DRU quantity item master.
	 *
	 * @param itemMaster the item master
	 * @return the item master
	 */
	ItemMaster updateDruQuantity(ItemMaster itemMaster){
		try {
			this.productManagementServiceClient.updateDruQuantity(itemMaster);
		} catch (Exception e){
			throw  new SoapException(e.getMessage());
		}
		return itemMasterRepository.findOne(itemMaster.getKey());
	}

	/**
	 * This returns a list of shipper audits based on the upc.
	 * @param key way to uniquely ID the set of DRU audits requested
	 * @return a list of all the changes made to an item master's DRU attributes.
	 */
	List<AuditRecord> getDruAuditInformation(ItemMasterKey key, String filter) {
		return this.auditService.getDruAuditInformation(key, filter);
	}
}
