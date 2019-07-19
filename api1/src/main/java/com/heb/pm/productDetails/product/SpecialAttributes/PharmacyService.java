package com.heb.pm.productDetails.product.SpecialAttributes;

import com.heb.pm.audit.AuditService;
import com.heb.pm.entity.DrugScheduleType;
import com.heb.pm.entity.ProductMaster;
import com.heb.pm.product.ProductInfoResolver;
import com.heb.pm.repository.DrugScheduleTypeRepository;
import com.heb.pm.repository.ProductInfoRepository;
import com.heb.pm.ws.ProductManagementServiceClient;
import com.heb.util.audit.AuditRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class PharmacyService {

	@Autowired
	private AuditService auditService;

	@Autowired
	DrugScheduleTypeRepository drugScheduleTypeRepository;

	@Autowired
	public ProductManagementServiceClient productManagementServiceClient;

	@Autowired
	public ProductInfoRepository productInfoRepository;

	@Autowired
	private ProductInfoResolver productInfoResolver = new ProductInfoResolver();

	/**
	 * Returns all of the values in the DrugSchedule Code Table
	 * @return
	 */
	public List<DrugScheduleType> getDrugSchedulerTypes(){
		return this.drugScheduleTypeRepository.findAll();
	}

	/**
	 * Saves any changes that happened to the pharmacy tab on special attributes.
	 * @param productMaster the product master that is to be updated.
	 * @return returns an updated product master.
	 */
	public ProductMaster savePharmacyChanges(ProductMaster productMaster) {
		this.productManagementServiceClient.updatePharmacy(productMaster);
		ProductMaster updatedProductMaster = this.productInfoRepository.findOne(productMaster.getProdId());
		this.productInfoResolver.fetch(updatedProductMaster);
		return updatedProductMaster;
	}

	/**
	 * This returns a list of pharmacy audits based on the prodId.
	 * @param prodId way to uniquely ID the set of tags and specs audits requested
	 * @return a list of all the changes made to an product's tags and specs audits.
	 */
	List<AuditRecord> getPharmacyAuditInformation(Long prodId) {
		return this.auditService.getPharmacyAuditInformation(prodId);
	}
}
