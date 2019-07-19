package com.heb.pm.productDetails.product.OnlineAttributes;

import com.heb.pm.audit.AuditRecordWithProdId;
import com.heb.pm.audit.AuditService;
import com.heb.pm.entity.ConsumerPurchaseChoice;
import com.heb.pm.entity.DistributionFilter;
import com.heb.pm.entity.HEBGuaranteeType;
import com.heb.pm.entity.ProductMaster;
import com.heb.pm.entity.ProductTrashCan;
import com.heb.pm.repository.ConsumerPurchaseChoiceRepository;
import com.heb.pm.repository.HEBGuaranteeTypeRepository;
import com.heb.pm.repository.OnlineAttributesRepository;
import com.heb.pm.repository.ProductMasterRepository;
import com.heb.pm.repository.ProductTrashCanRepository;
import com.heb.pm.ws.CodeTableManagementServiceClient;
import com.heb.pm.ws.ProductAttributeManagementServiceClient;
import com.heb.pm.ws.ProductManagementServiceClient;
import com.heb.util.audit.AuditRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Holds business logic related to online attributes
 *
 * @author m594201
 * @since 2.14.0
 */
@Service
public class OnlineAttributesService {

	@Autowired
	private AuditService auditService;

	@Autowired
	OnlineAttributesRepository repository;

	@Autowired
	HEBGuaranteeTypeRepository hebGuaranteeTypeRepository;

	@Autowired
	ProductTrashCanRepository productTrashCanRepository;

	@Autowired
	ConsumerPurchaseChoiceRepository consumerPurchaseChoiceRepository;

	@Autowired
	ProductMasterRepository productMasterRepository;

	@Autowired
	ProductAttributeManagementServiceClient productAttributeManagementServiceClient;

	@Autowired
	ProductManagementServiceClient productManagementServiceClient;

	@Autowired
	CodeTableManagementServiceClient codeTableManagementServiceClient;

	/**
	 * This method gets a distribution filter that holds the third party sellable information
	 * @param prodId
	 * @return
	 */
	public DistributionFilter getThirdPartySellableStatus(Long prodId) {
		DistributionFilter distributionFilter = this.repository.findByKeyKeyId(prodId);

		return distributionFilter;
	}

	/**
	 * This method gets all the possible HEBGuaranteeTypes for a drop down
	 * @return
	 */
	public List<HEBGuaranteeType> getGuaranteeImageOptions() {
		List<HEBGuaranteeType> hebGuaranteeTypes = this.hebGuaranteeTypeRepository.findAll();

		return hebGuaranteeTypes;
	}

	/**
	 * This method gets a product's product trash can to find the online only flag information
	 * @param prodId
	 * @return
	 */
	public ProductTrashCan getProductTrashCan(long prodId){
		return this.productTrashCanRepository.getOne(prodId);
	}

	/**
	 * This method fetches all of the possible values for the sold by code table
	 * @return
	 */
	public List<ConsumerPurchaseChoice> getConsumerChoices(){
		return this.consumerPurchaseChoiceRepository.findAll();
	}

	public void updateThirdPartySellable(DistributionFilter distributionFilter, String userId){
		this.codeTableManagementServiceClient.updateDistributionFilter(distributionFilter, userId);
	}

	/**
	 * This processes the requests for product trash can changes
	 * @param productTrashCan the changes requested
	 * @param userId the user requesting them
	 */
	public void updateProductTrashCan(ProductTrashCan productTrashCan, String userId){
		this.productAttributeManagementServiceClient.updateOnlineOnly(productTrashCan, userId);
	}

	/**
	 * This method processes requests for changes in a products onine attributes
	 * @param productMaster the changes to the online attributes
	 * @param userId the user making the request
	 */
	public void updateOnlineAttributes(ProductMaster productMaster, String userId){
		this.productManagementServiceClient.updateOnlineAttributes(productMaster, userId);
		if(productMaster.getProductMarketingClaims() != null){
			this.productAttributeManagementServiceClient.updateProductMarketingClaim(productMaster.getProductMarketingClaims(), userId);
		}
	}

	/**
	 * This returns a list of related products audits based on the prodId.
	 * @param prodId way to uniquely ID the set of related products audits requested
	 * @return a list of all the changes made to an product's related products audits.
	 */
	List<AuditRecordWithProdId> getRelatedProductsAuditInformation(Long prodId) {
		return this.auditService.getRelatedProductsAuditInformation(prodId);
	}

	/**
	 * This returns a list of online attributes audits based on the prodId.
	 * @param prodId way to uniquely ID the set of online attributes audits requested
	 * @return a list of all the changes made to an product's online attributes audits.
	 */
	List<AuditRecord> getOnlineAttributesAuditInformation(Long prodId) {
		return this.auditService.getOnlineAttributesAuditInformation(prodId);
	}

	/**
	 * This returns a list of tags and specs audits based on the prodId.
	 * @param prodId way to uniquely ID the set of tags and specs audits requested
	 * @return a list of all the changes made to an product's tags and specs audits.
	 */
	List<AuditRecord> getTagsAndSpecsAuditInformation(Long prodId) {
		return this.auditService.getTagsAndSpecsAuditInformation(prodId);
	}

	/**
	 * This method finds a current product master
	 * @param prodId
	 * @return
	 */
	public ProductMaster getUpdatedProductMaster(long prodId){
		return this.productMasterRepository.findOne(prodId);
	}
}
