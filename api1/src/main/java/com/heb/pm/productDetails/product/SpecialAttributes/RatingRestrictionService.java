package com.heb.pm.productDetails.product.SpecialAttributes;

import com.heb.pm.audit.AuditService;
import com.heb.pm.codeTable.ProductStateWarningService;
import com.heb.pm.entity.*;
import com.heb.pm.product.ProductInfoResolver;
import com.heb.pm.repository.ProductInfoRepository;
import com.heb.pm.repository.SellingRestrictionCodeRepository;
import com.heb.pm.repository.SellingRestrictionRepository;
import com.heb.pm.ws.ProductAttributeManagementServiceClient;
import com.heb.pm.ws.ProductManagementServiceClient;
import com.heb.util.audit.AuditRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.method.P;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Service for the Rating and Restrictions controller
 * @author s753601
 * @version 2.12.0
 */
@Service
public class RatingRestrictionService {

	private String INSERT_ACTION_CODE ="";
	private String DELETE_ACTION_CODE = "D";

	@Autowired
	SellingRestrictionCodeRepository restrictionsRepository;

	@Autowired
	SellingRestrictionRepository restrictionGroupRepository;

	@Autowired
	ProductInfoRepository productInfoRepository;

	@Autowired
	private ProductStateWarningService productStateWarningService;

	@Autowired
	ProductAttributeManagementServiceClient productAttributeManagementServiceClient;

	@Autowired
	ProductManagementServiceClient productManagementServiceClient;

	@Autowired
	private RestrictionService restrictionService;

	@Autowired
	private ProductInfoResolver productInfoResolver = new ProductInfoResolver();

	@Autowired
	private AuditService auditService;

	/**
	 * Gets the list of all selling restriction codes
	 * @return the list of all selling restriction
	 */
	List<SellingRestrictionCode> getAllRestrictions(){
		return this.restrictionsRepository.findAll();
	}

	/**
	 * Gets the list of all selling restrictions except shipping restriction
	 * @return a list of selling restriction
	 */
	List<SellingRestriction> getAllRestrictionGroups(){
		return this.restrictionGroupRepository.findAll();
	}

	/**
	 * This Method will take all Product Restriction Updates and send all of the additions and deletions to the
	 * ProductAttributeManagementServiceClient
	 * @param updates the additions and deletions for a products restrictions
	 * @param userId the user making the update
	 * @return
	 */
	public ProductMaster getUpdatedProductMaster(ProductRestrictionUpdates updates, String userId){
		for (ProductRestrictions restriction: updates.getRestrictionsRemoved()){
			this.productAttributeManagementServiceClient.updateProductRestriction(restriction, DELETE_ACTION_CODE, userId);
		}
		for (ProductRestrictions restriction:updates.getRestrictionsAdded()) {
			this.productAttributeManagementServiceClient.updateProductRestriction(restriction, INSERT_ACTION_CODE, userId);
		}
		ProductMaster updatedProductMaster = this.productInfoRepository.findOne(updates.getProdId());
		this.productInfoResolver.fetch(updatedProductMaster);
		return updatedProductMaster;
	}

	/**
	 * This Method will take all the update product restrictions for special attributes for addition or deletion to the
	 * ProductAttributeManagementServiceClient
	 *
	 * @param productRestrictions the product restrictions
	 * @param prodId              the prod id
	 * @param userId              the user id
	 * @return the product master
	 */
	public ProductMaster updateShippingRestrictions(List<ProductRestrictions> productRestrictions, Long prodId, String userId) {

		List<ProductRestrictions> originalRestrictionsList = this.restrictionService.getProductShippingRestrictionsByProductId(prodId);
		boolean foundRestriction = false;
		List<ProductRestrictions> updateList = new ArrayList<>();

		for(ProductRestrictions currentRestriction : originalRestrictionsList)  {
			foundRestriction = false;
			for(ProductRestrictions updatedRestriction : productRestrictions) {
				if(currentRestriction.getKey().getRestrictionCode().trim().equals(updatedRestriction.getKey().getRestrictionCode().trim())) {
					foundRestriction = true;
					break;
				}
			}
			if(!foundRestriction) {
				currentRestriction.setActionCode("D");
				updateList.add(currentRestriction);
			}
		}

		for(ProductRestrictions updatedRestriction : productRestrictions) {
			foundRestriction = false;
			for(ProductRestrictions currentRestriction : originalRestrictionsList) {
				if(currentRestriction.getKey().getRestrictionCode().trim().equals(updatedRestriction.getKey().getRestrictionCode().trim())) {
					foundRestriction = true;
					break;
				}
			}
			if(!foundRestriction) {
				updatedRestriction.setActionCode("");
				updateList.add(updatedRestriction);
			}
		}

		this.productAttributeManagementServiceClient.updateShippingRestrictions(updateList, userId);

		ProductMaster updatedProductMaster = this.productInfoRepository.findOne(prodId);
		this.productInfoResolver.fetch(updatedProductMaster);
		return updatedProductMaster;

	}

	/**
	 * This Method will take all the update product shipping methods for special attributes for addition or deletion to the
	 * ProductManagementServiceClient
	 *
	 * @param customShippingMethods the custom shipping methods
	 * @param prodId                the prod id
	 * @param userId                the user id
	 * @return the product master
	 */
	public ProductMaster updateShippingMethods(List<CustomShippingMethod> customShippingMethods, Long prodId, String userId) {

		List<ProductShippingException> originalProductShippingException = this.restrictionService.getShippingMethodExceptionsByProductId(prodId);
		boolean foundMethodException = false;
		List<CustomShippingMethod> updateList = new ArrayList<>();

		for(ProductShippingException currentShippingMethodException : originalProductShippingException) {
			foundMethodException = false;
			for(CustomShippingMethod updateShippingMethod : customShippingMethods) {
				if(currentShippingMethodException.getCustomShippingMethod().getCustomShippingMethod() == updateShippingMethod.getCustomShippingMethod()) {
					foundMethodException = true;
					break;
				}
			}
			if(!foundMethodException) {
				currentShippingMethodException.getCustomShippingMethod().setActionCode("D");
				updateList.add(currentShippingMethodException.getCustomShippingMethod());
			}
		}

		for(CustomShippingMethod updatedShippingMethod : customShippingMethods) {
			foundMethodException = false;
			for(ProductShippingException currentMethodException : originalProductShippingException) {
				if(currentMethodException.getCustomShippingMethod().getCustomShippingMethod() == updatedShippingMethod.getCustomShippingMethod()) {
					foundMethodException = true;
					break;
				}
			}
			if(!foundMethodException) {
				updatedShippingMethod.setActionCode("");
				updateList.add(updatedShippingMethod);
			}
		}

		this.productManagementServiceClient.updateShippingMethodExceptions(updateList, prodId, userId);

		ProductMaster updatedProductMaster = this.productInfoRepository.findOne(prodId);
		this.productInfoResolver.fetch(updatedProductMaster);
		return updatedProductMaster;
	}

	/**
	 * This Method will take all the update product state warnings for special attributes for addition or deletion to the
	 * ProductAttributeManagementServiceClient
	 *
	 * @param productStateWarnings the product state warnings
	 * @param prodId               the prod id
	 * @param userId               the user id
	 * @return the product master
	 */
	public ProductMaster updateProductStateWarnings(List<ProductStateWarning> productStateWarnings, Long prodId, String userId) {

			List<ProductWarning> originalStateWarnings = this.productStateWarningService.getProductStateWarningsByProductId(prodId);
			boolean foundStateWarning = false;
			List<ProductStateWarning> updateStateList = new ArrayList<>();

		for(ProductWarning currentStateWarningsException : originalStateWarnings) {
			foundStateWarning = false;
			for(ProductStateWarning updateStateWarningMethod : productStateWarnings) {
				if(currentStateWarningsException.getKey().getStateCode().equals(updateStateWarningMethod.getKey().getStateCode())) {
					foundStateWarning = true;
					break;
				}
			}
			if(!foundStateWarning) {
				currentStateWarningsException.getProductStateWarning().setActionCode("D");
				updateStateList.add(currentStateWarningsException.getProductStateWarning());
			}
		}

		for(ProductStateWarning updatedStateWarning : productStateWarnings) {
			foundStateWarning = false;
			for(ProductWarning currentStateWarning : originalStateWarnings) {
				if(currentStateWarning.getKey().getStateCode().equals(updatedStateWarning.getKey().getStateCode())) {
					foundStateWarning = true;
					break;
				}
			}
			if(!foundStateWarning) {
				updatedStateWarning.setActionCode("");
				updateStateList.add(updatedStateWarning);
			}
		}

		this.productAttributeManagementServiceClient.updateProductStateWarnings(updateStateList, prodId, userId);

		ProductMaster updatedProductMaster = this.productInfoRepository.findOne(prodId);
		this.productInfoResolver.fetch(updatedProductMaster);
		return updatedProductMaster;
	}

	/**
	 * This Method will take all the update shipping and handling changes for special attributes for addition or deletion to the
	 * productManagementServiceClient
	 *
	 * @param productMaster the product master
	 * @param userId        the user id
	 * @return the product master
	 */
	public ProductMaster updateShippingHandlingChanges(ProductMaster productMaster, String userId) {
		this.productManagementServiceClient.updateShippingHandlingChanges(productMaster, userId);


		ProductMaster updatedProductMaster = this.productInfoRepository.findOne(productMaster.getProdId());
		this.productInfoResolver.fetch(updatedProductMaster);
		return updatedProductMaster;
	}

	/**
	 * This returns a list of shipping and handling audits based on the prodId.
	 * @param prodId way to uniquely ID the set of shipping and handling audits requested
	 * @return a list of all the changes made to an product's shipping and handling audits.
	 */
	List<AuditRecord> getShippingHandlingAuditInformation(Long prodId) {
		return this.auditService.getShippingHandlingAuditInformation(prodId);
	}

	/**
	 * This returns a list of rating and restrictions audits based on the prodId.
	 * @param prodId way to uniquely ID the set of rating and restrictions audits requested
	 * @return a list of all the changes made to an product's rating and restrictions audits.
	 */
	List<AuditRecord> getRatingRestrictionsAuditInformation(Long prodId) {
		return this.auditService.getRatingRestrictionsAuditInformation(prodId);
	}
}
