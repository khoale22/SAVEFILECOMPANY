package com.heb.pm.productDetails.product.OnlineAttributes;

import com.heb.jaf.security.AuthorizedResource;
import com.heb.jaf.security.EditPermission;
import com.heb.jaf.security.ViewPermission;
import com.heb.pm.ApiConstants;
import com.heb.pm.ResourceConstants;
import com.heb.pm.audit.AuditRecordWithProdId;
import com.heb.pm.entity.*;
import com.heb.util.audit.AuditRecord;
import com.heb.util.controller.ModifiedEntity;
import com.heb.util.controller.UserInfo;
import com.heb.util.jpa.LazyObjectResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Represents Online Attributes Controller
 *
 * @author m594201
 * @since 2.14.0
 */
@RestController()
@RequestMapping(ApiConstants.BASE_APPLICATION_URL + OnlineAttributesController.ONLINE_URL)
@AuthorizedResource(ResourceConstants.ONLINE_ATTRIBUTES)
public class OnlineAttributesController {

	private static final Logger logger = LoggerFactory.getLogger(OnlineAttributesController.class);

	private static final String LOG_ONLINE_ATTRIBUTE_BY_PRODUCT_ID = "User %s from IP %s has requested audit information for upc: %s";
	private static final String LOG_RELATED_PRODUCT_AUDIT_BY_PRODUCT_ID = "User %s from IP %s has requested related products audit information for prod ID: %s";
	private static final String LOG_ONLINE_ATTRIBUTES_AUDIT_BY_PRODUCT_ID = "User %s from IP %s has requested online attributes audit information for prod ID: %s";
	private static final String LOG_TAGS_AND_SPECS_AUDIT_BY_PRODUCT_ID = "User %s from IP %s has requested tags and specs audit information for prod ID: %s";

	private static final String ONLINE_ATTRIBUTE_UPDATE_REQUEST="User %s from address %s has requested to update the " +
			"online attributes of the product with id %s ";
	private static final String THIRD_PARTY_UPDATE_REQUEST="User %s from address %s has requested to update the " +
			"third party sellable flag of the product with id %s ";
	private static final String PRODUCT_TRASH_CAN_UPDATE_REQUEST="User %s from address %s has requested to update the " +
			"product trash can of the product with id %s ";

	protected static final String ONLINE_URL = "/onlineInfo";

	protected static final String GET_THIRD_PARTY_SELLABLE_STATUS = "/getThirdPartySellableStatus";

	protected static final String GET_GUARANTEE_IMAGE_OPTIONS="/getGuaranteeImages";
	protected static final String GET_PRODUCT_TRASH_CAN="/getProductTrashCan";
	protected static final String GET_SOLD_BY_OPTIONS="/getSoldByOptions";
	protected static final String UPDATE_THIRD_PARTY_OPTION = "/updateThirdPartySellable";
	protected static final String UPDATE_PRODUCT_TRASH_CAN = "/updateProductTrashCan";
	protected static final String UPDATE_ONLINE_ATTRIBUTE = "/updateOnlineAttributes";

	private static final String UPDATE_SUCCESS_MESSAGE ="OnlineAttributesOnlineAttributeController.updateSuccessful";
	private static final String DEFAULT_UPDATE_SUCCESS_MESSAGE = "Online Attribute for product: %s updated successfully.";
	private static final String GET_RELATED_PRODUCTS_AUDITS = "/getRelatedProductsAudits";
	private static final String GET_ONLINE_ATTRIBUTES_AUDITS = "/getOnlineAttributesAudits";
	private static final String GET_TAGS_AND_SPECS_AUDITS = "/getTagsAndSpecsAudits";

	@Autowired
	private UserInfo userInfo;

	@Autowired
	private OnlineAttributesService service;

	@Autowired
	private MessageSource messageSource;

	private ProductTrashCanResolver productTrashCanResolver = new ProductTrashCanResolver();

	private class ProductTrashCanResolver implements LazyObjectResolver<ProductTrashCan> {

		/**
		 * Currently this fetch is getting:
		 * 1. the key
		 * 2. the Image type
		 * @param 
		 */

		@Override
		public void fetch(ProductTrashCan productTrashCan) {
			if (productTrashCan.getOnlineSaleOnlySw()!= null){
				productTrashCan.getOnlineSaleOnlySw();
			}
		}
	}

	/**
	 * Retrieves related Products audit information.
	 * @param prodId The Product ID that the audit is being searched on.
	 * @param request The HTTP request that initiated this call.
	 * @return The list of related products audits attached to given product ID.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = OnlineAttributesController.GET_RELATED_PRODUCTS_AUDITS)
	public List<AuditRecordWithProdId> getRelatedProductsAuditInfo(@RequestParam(value="prodId") Long prodId, HttpServletRequest request) {
		this.logGetRelatedProductsAuditInformation(request.getRemoteAddr(), prodId);
		List<AuditRecordWithProdId> relatedProductsAuditRecords = this.service.getRelatedProductsAuditInformation(prodId);
		return relatedProductsAuditRecords;
	}

	/**
	 * Retrieves Online Attributes audit information.
	 * @param prodId The Product ID that the audit is being searched on.
	 * @param request The HTTP request that initiated this call.
	 * @return The list of Online Attributes audits attached to given product ID.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = OnlineAttributesController.GET_ONLINE_ATTRIBUTES_AUDITS)
	public List<AuditRecord> getOnlineAttributesAuditInfo(@RequestParam(value="prodId") Long prodId, HttpServletRequest request) {
		this.logGetOnlineAttributesAuditInformation(request.getRemoteAddr(), prodId);
		List<AuditRecord> onlineAttributesAuditRecords = this.service.getOnlineAttributesAuditInformation(prodId);
		return onlineAttributesAuditRecords;
	}

	/**
	 * Retrieves Tags and Specs audit information.
	 * @param prodId The Product ID that the audit is being searched on.
	 * @param request The HTTP request that initiated this call.
	 * @return The list of Tags and Specs audits attached to given product ID.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = OnlineAttributesController.GET_TAGS_AND_SPECS_AUDITS)
	public List<AuditRecord> getTagsAndSpecsAuditInfo(@RequestParam(value="prodId") Long prodId, HttpServletRequest request) {
		this.logGetTagsAndSpecsAuditInformation(request.getRemoteAddr(), prodId);
		List<AuditRecord> tagsAndSpecsAuditRecords = this.service.getTagsAndSpecsAuditInformation(prodId);
		return tagsAndSpecsAuditRecords;
	}

	/**
	 * Gets third party sellable status.
	 *
	 * @param prodId  the prod id
	 * @param request the request
	 * @return the third party sellable status
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = OnlineAttributesController.GET_THIRD_PARTY_SELLABLE_STATUS)
	public DistributionFilter getThirdPartySellableStatus(@RequestParam(value="prodId") Long prodId, HttpServletRequest request) {

		this.logGetMrtAuditInformation(request.getRemoteAddr(), prodId);

		DistributionFilter distributionFilter = this.service.getThirdPartySellableStatus(prodId);

		return distributionFilter;
	}

	/**
	 * Logs get online attributes information by upc.
	 *
	 * @param ip The user's ip.
	 * @param prodId The upc being searched on.
	 */
	private void logGetMrtAuditInformation(String ip, Long prodId) {
		OnlineAttributesController.logger.info(
				String.format(OnlineAttributesController.LOG_ONLINE_ATTRIBUTE_BY_PRODUCT_ID, this.userInfo.getUserId(), ip, prodId)
		);
	}

	/**
	 * Logs get related products information by prodId.
	 *
	 * @param ip The user's ip.
	 * @param prodId The prodId being searched on.
	 */
	private void logGetRelatedProductsAuditInformation(String ip, Long prodId) {
		OnlineAttributesController.logger.info(
				String.format(OnlineAttributesController.LOG_RELATED_PRODUCT_AUDIT_BY_PRODUCT_ID, this.userInfo.getUserId(), ip, prodId)
		);
	}

	/**
	 * Logs get online attributes information by prodId.
	 *
	 * @param ip The user's ip.
	 * @param prodId The prodId being searched on.
	 */
	private void logGetOnlineAttributesAuditInformation(String ip, Long prodId) {
		OnlineAttributesController.logger.info(
				String.format(OnlineAttributesController.LOG_ONLINE_ATTRIBUTES_AUDIT_BY_PRODUCT_ID, this.userInfo.getUserId(), ip, prodId)
		);
	}

	/**
	 * Logs get tags and specs information by prodId.
	 *
	 * @param ip The user's ip.
	 * @param prodId The prodId being searched on.
	 */
	private void logGetTagsAndSpecsAuditInformation(String ip, Long prodId) {
		OnlineAttributesController.logger.info(
				String.format(OnlineAttributesController.LOG_TAGS_AND_SPECS_AUDIT_BY_PRODUCT_ID, this.userInfo.getUserId(), ip, prodId)
		);
	}

	/**
	 * Gets All possible options for Guarantee Images
	 *
	 * @param request the request
	 * @return the third party sellable status
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = OnlineAttributesController.GET_GUARANTEE_IMAGE_OPTIONS)
	public List<HEBGuaranteeType> getGuaranteeImageOptions(HttpServletRequest request) {
		List<HEBGuaranteeType> hebGuaranteeTypes= this.service.getGuaranteeImageOptions();

		return hebGuaranteeTypes;
	}

	/**
	 * Gets Trash Can for the product to the the online only switch
	 *
	 * @param request the request
	 * @return the third party sellable status
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = OnlineAttributesController.GET_PRODUCT_TRASH_CAN)
	public ProductTrashCan getProductTrashCan(@RequestParam(value="prodId") Long prodId,  HttpServletRequest request) {
		ProductTrashCan productTrashCan = this.service.getProductTrashCan(prodId);
		try{
			this.productTrashCanResolver.fetch(productTrashCan);
		} catch (EntityNotFoundException e){
			productTrashCan = new ProductTrashCan();
			productTrashCan.setProductId(prodId);
			productTrashCan.setOnlineSaleOnlySw(false);
		}
		return productTrashCan;

	}

	/**
	 * Gets All possible options for Guarantee Images
	 *
	 * @param request the request
	 * @return the third party sellable status
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = OnlineAttributesController.GET_SOLD_BY_OPTIONS)
	public List<ConsumerPurchaseChoice> getSoldByChoices(HttpServletRequest request) {
		List<ConsumerPurchaseChoice> consumerPurchaseChoices= this.service.getConsumerChoices();
		return consumerPurchaseChoices;
	}

	/**
	 * This method will accept distribution filter changes and will atempt to omplete the request
	 * @param distributionFilter the changes to the distribution filter to be made
	 * @param request
	 * @return
	 */
	@EditPermission
	@RequestMapping(method = RequestMethod.POST, value = OnlineAttributesController.UPDATE_THIRD_PARTY_OPTION)
	public ModifiedEntity<DistributionFilter> updateThirdPartySellable(@RequestBody DistributionFilter distributionFilter, HttpServletRequest request){
		OnlineAttributesController.logger.info(String.format(THIRD_PARTY_UPDATE_REQUEST, this.userInfo.getUserId(), request.getRemoteAddr(), distributionFilter.getKey().getKeyId()));
		this.service.updateThirdPartySellable(distributionFilter, this.userInfo.getUserId());
		String updateMessage = this.messageSource.getMessage(
				OnlineAttributesController.UPDATE_SUCCESS_MESSAGE,
				new Object[]{distributionFilter.getKey().getKeyId()}, String.format(OnlineAttributesController.DEFAULT_UPDATE_SUCCESS_MESSAGE,
						distributionFilter.getKey().getKeyId()), request.getLocale());
		DistributionFilter update = this.service.getThirdPartySellableStatus(distributionFilter.getKey().getKeyId());
		return new ModifiedEntity<>(update, updateMessage);
	}

	/**
	 * The method will accept a ProductMaster with online attribute changes from the ui
	 * @param productMaster the product master changes being requested
	 * @param request
	 * @return
	 */
	@EditPermission
	@RequestMapping(method = RequestMethod.POST, value = OnlineAttributesController.UPDATE_ONLINE_ATTRIBUTE)
	public ModifiedEntity<ProductMaster> updateOnlineAttributes(@RequestBody ProductMaster productMaster, HttpServletRequest request){
		OnlineAttributesController.logger.info(String.format(ONLINE_ATTRIBUTE_UPDATE_REQUEST, this.userInfo.getUserId(), request.getRemoteAddr(), productMaster.getProdId()));
		this.service.updateOnlineAttributes(productMaster, this.userInfo.getUserId());
		String updateMessage = this.messageSource.getMessage(
				OnlineAttributesController.UPDATE_SUCCESS_MESSAGE,
				new Object[]{productMaster.getProdId()}, String.format(OnlineAttributesController.DEFAULT_UPDATE_SUCCESS_MESSAGE,
						productMaster.getProdId()), request.getLocale());
		this.service.getUpdatedProductMaster(productMaster.getProdId());
		return new ModifiedEntity<>(productMaster, updateMessage);
	}

	/**
	 * This method will accept a product trash can update request and send it to the service
	 * @param productTrashCan the product trash can to be updated
	 * @param request
	 * @return
	 */
	@EditPermission
	@RequestMapping(method = RequestMethod.POST, value = OnlineAttributesController.UPDATE_PRODUCT_TRASH_CAN)
	public ModifiedEntity<ProductTrashCan> updateProductTrashCan(@RequestBody ProductTrashCan productTrashCan, HttpServletRequest request){
		OnlineAttributesController.logger.info(String.format(PRODUCT_TRASH_CAN_UPDATE_REQUEST, this.userInfo.getUserId(), request.getRemoteAddr(), productTrashCan.getProductId()));
		this.service.updateProductTrashCan(productTrashCan, this.userInfo.getUserId());
		String updateMessage = this.messageSource.getMessage(
				OnlineAttributesController.UPDATE_SUCCESS_MESSAGE,
				new Object[]{productTrashCan.getProductId()}, String.format(OnlineAttributesController.DEFAULT_UPDATE_SUCCESS_MESSAGE,
						productTrashCan.getProductId()), request.getLocale());
		ProductTrashCan update = this.service.getProductTrashCan(productTrashCan.getProductId());
		return new ModifiedEntity<>(update, updateMessage);
	}
}
