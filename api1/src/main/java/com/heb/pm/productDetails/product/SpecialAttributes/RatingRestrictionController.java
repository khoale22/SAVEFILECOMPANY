package com.heb.pm.productDetails.product.SpecialAttributes;

import com.heb.jaf.security.AuthorizedResource;
import com.heb.jaf.security.EditPermission;
import com.heb.jaf.security.ViewPermission;
import com.heb.pm.ApiConstants;
import com.heb.pm.ResourceConstants;
import com.heb.pm.codeTable.ProductStateWarningService;
import com.heb.pm.entity.*;
import com.heb.pm.productHierarchy.SellingRestrictionCodeService;
import com.heb.util.audit.AuditRecord;
import com.heb.util.controller.ModifiedEntity;
import com.heb.util.controller.UserInfo;
import com.heb.util.jpa.LazyObjectResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Restful client for Ratings and restrictions
 * @author s753601
 * @version 2.12.0
 */
@RestController()
@RequestMapping(ApiConstants.BASE_APPLICATION_URL + RatingRestrictionController.SPECIAL_ATTRIBUTES)
@AuthorizedResource(ResourceConstants.SPECIAL_ATTRIBUTES_RESTRICTIONS)
public class RatingRestrictionController {

	private static final Logger logger = LoggerFactory.getLogger(RatingRestrictionController.class);

	private static final String RESTRICTION="User %s from IP %s has requested the Restrictions code table";
	private static final String RESTRICTION_CODE="User %s from IP %s has requested the Rating code table";
	private static final String RESTRICTION_UPDATE_REQUEST="User %s from IP %s has requested to update the ratings and restrictions for product: %s";
	private static final String RESTRICTION_UPDATE_COMPLETE="User %s from IP %s has completed an update for the ratings and restrictions for product: %s";
	private static final String LOG_SHIPPING_HANDLING_AUDIT_BY_PRODUCT_ID = "User %s from IP %s has requested special attributes shipping and handling audit information for prod ID: %s";
	private static final String LOG_RATING_RESTRICTIONS_AUDIT_BY_PRODUCT_ID = "User %s from IP %s has requested special attributes rating/restrictions audit information for prod ID: %s";
	private static final String LOG_GET_SALES_RESTRICTIONS_BY_SUB_COMMODITY = "User %s from IP %s has requested to get sales restrictions by sub commodity";
	private static final String LOG_GET_STATE_WARNING_BY_SUB_COMMODITY = "User %s from IP %s has requested to get state warning by sub commodity";

	protected static final String SPECIAL_ATTRIBUTES = "/specialAttributes";
	private static final String RATING_RESTRICTION="/ratingRestrictions";
	private static final String SHIPPING_ALL_RESTRICTIONS="/getAllShippingRestrictions";
	private static final String PRODUCT_SHIPPING_RESTRICTIONS="/getProductShippingRestrictions";
	private static final String GET_SALES_RESTRICTIONS_BY_SUB_COMMODITY="/getSalesRestrictionsBySubCommodity";
	private static final String GET_STATE_WARNING_BY_SUB_COMMODITY="/getStateWarningBySubcommodity";
	private static final String UPDATE_SHIPPING_RESTRICTIONS="/updateShippingRestrictions";
	private static final String SHIPPING_METHOD_RESTRICTIONS="/getShippingMethodRestrictions";
	private static final String UPDATE_PRODUCT_STATE_WARNINGS="/updateProductStateWarnings";
	private static final String UPDATE_SHIPPING_METHODS="/updateShippingMethods";
	private static final String RATING_RESTRICTION_GROUP="/ratingRestrictionGroup";
	private static final String UPDATE_RATING_RESTRICTION="/updateRatingRestrictions";
	private static final String UPDATE_SHIPPING_HANDLING_CHANGES="/updateShippingHandlingChanges";
	private static final String STATE_WARNINGS="/getStateWarnings";
	private static final String ALL_STATE_WARNINGS="/getAllStateWarnings";
	private static final String SHIPPING_ALL_METHOD_EXCEPTIONS ="/getAllShippingMethodExceptions";
	private static final String GET_SHIPPING_HANDLING_AUDITS = "/getShippingHandlingAudits";
	private static final String GET_RATING_RESTRICTION_AUDITS = "/getRatingRestrictionsAudits";

	private static final String UPDATE_SUCCESS_MESSAGE =
			"Updated Successfully";

	private static final String DEFAULT_UPDATE_SUCCESS_MESSAGE = "Ratings and Restrictions for product: %d updated successfully.";

	@Autowired
	private RatingRestrictionService service;

	@Autowired
	private UserInfo userInfo;

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private SellingRestrictionCodeService sellingRestrictionCodeService;

	@Autowired
	private ProductStateWarningService productStateWarningService;

	@Autowired
	private RestrictionService restrictionService;

	ProductShippingRestrictionResolver productShippingRestrictionResolver = new ProductShippingRestrictionResolver();

	static class ProductRestrictionUpdate {

		private Long prodId;
		private List<ProductRestrictions> productRestrictions;

		public Long getProdId() {
			return prodId;
		}

		public void setProdId(Long prodId) {
			this.prodId = prodId;
		}

		public List<ProductRestrictions> getProductRestrictions() {
			return productRestrictions;
		}

		public void setProductRestrictions(List<ProductRestrictions> productRestrictions) {
			this.productRestrictions = productRestrictions;
		}

		public ProductRestrictionUpdate(){}
	}

	static class ShippingMethodUpdate {

		private Long prodId;

		public List<CustomShippingMethod> customShippingMethods;

		public Long getProdId() {
			return prodId;
		}

		public void setProdId(Long prodId) {
			this.prodId = prodId;
		}

		public List<CustomShippingMethod> getCustomShippingMethods() {
			return customShippingMethods;
		}

		public void setCustomShippingMethods(List<CustomShippingMethod> customShippingMethods) {
			this.customShippingMethods = customShippingMethods;
		}

		public ShippingMethodUpdate(){}
	}

	static class ProductStateWarningUpdate {

		private Long prodId;

		public List<ProductStateWarning> productStateWarnings;

		public Long getProdId() {
			return prodId;
		}

		public void setProdId(Long prodId) {
			this.prodId = prodId;
		}

		public List<ProductStateWarning> getProductStateWarnings() {
			return productStateWarnings;
		}

		public void setProductStateWarnings(List<ProductStateWarning> productStateWarnings) {
			this.productStateWarnings = productStateWarnings;
		}

		public ProductStateWarningUpdate(){}
	}

	private class ProductShippingRestrictionResolver implements LazyObjectResolver<ProductRestrictions> {

		@Override
		public void fetch(ProductRestrictions d) {
			if (d.getRestriction() != null) {
				d.getRestriction().getRestrictionDescription();
			}
		}
	}

	/**
	 * Gets all shipping restrictions.
	 *
	 * @param request the request
	 * @return the all shipping restrictions
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = RatingRestrictionController.SHIPPING_ALL_RESTRICTIONS)
	public List<SellingRestrictionCode> getAllShippingRestrictions(HttpServletRequest request) {

		return sellingRestrictionCodeService.findAllShippingRestrictions();
	}

	/**
	 * Gets product shipping restrictions.
	 *
	 * @param prodId  the prod id
	 * @param request the request
	 * @return the product shipping restrictions
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = RatingRestrictionController.PRODUCT_SHIPPING_RESTRICTIONS)
	public List<ProductRestrictions> getProductShippingRestrictions(Long prodId, HttpServletRequest request) {

		List<ProductRestrictions> restrictions = this.restrictionService.getProductShippingRestrictionsByProductId(prodId);

		restrictions.forEach(productRestriction -> this.productShippingRestrictionResolver.fetch(productRestriction));

		return restrictions;
	}

	/**
	 * Get list of sales restrictions by sub commodity.
	 *
	 * @param subcomcd The sub commodity code.
	 * @param request the request.
	 * @return the list of sales restrictions.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = RatingRestrictionController.GET_SALES_RESTRICTIONS_BY_SUB_COMMODITY)
	public List<String> getSalesRestrictionsBySubCommodity(String subcomcd, HttpServletRequest request) {
		RatingRestrictionController.logger.info(
				String.format(RatingRestrictionController.LOG_GET_SALES_RESTRICTIONS_BY_SUB_COMMODITY, this.userInfo.getUserId(), request.getRemoteAddr())
		);
		return this.restrictionService.getSalesRestrictionsBySubCommodity(subcomcd);
	}

	/**
	 * Get list of state warning by sub commodity.
	 *
	 * @param subcomcd The sub commodity code.
	 * @param request the request.
	 * @return the list of state warning.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = RatingRestrictionController.GET_STATE_WARNING_BY_SUB_COMMODITY)
	public List<String> getStateWarningBySubcommodity(String subcomcd, HttpServletRequest request) {
		RatingRestrictionController.logger.info(
				String.format(RatingRestrictionController.LOG_GET_STATE_WARNING_BY_SUB_COMMODITY, this.userInfo.getUserId(), request.getRemoteAddr())
		);
		return this.restrictionService.getStateWarningBySubcommodity(subcomcd);
	}

	/**
	 * Gets shipping method exceptions.
	 *
	 * @param prodId  the prod id
	 * @param request the request
	 * @return the shipping method exceptions
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = RatingRestrictionController.SHIPPING_METHOD_RESTRICTIONS)
	public List<ProductShippingException> getShippingMethodExceptions(Long prodId, HttpServletRequest request) {

		return this.restrictionService.getShippingMethodExceptionsByProductId(prodId);
	}

	/**
	 * Gets all shipping method exceptions.
	 *
	 * @param request the request
	 * @return the all shipping method exceptions
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = RatingRestrictionController.SHIPPING_ALL_METHOD_EXCEPTIONS)
	public List<CustomShippingMethod> getAllShippingMethodExceptions(HttpServletRequest request) {
		return restrictionService.findAllShippingMethodExceptions();
	}

	/**
	 * Gets state warnings.
	 *
	 * @param prodId  the prod id
	 * @param request the request
	 * @return the state warnings
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = RatingRestrictionController.STATE_WARNINGS)
	public List<ProductWarning> getStateWarnings(Long prodId, HttpServletRequest request) {

		return this.productStateWarningService.getProductStateWarningsByProductId(prodId);
	}

	/**
	 * Get all state warnings list.
	 *
	 * @param request the request
	 * @return the list
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = RatingRestrictionController.ALL_STATE_WARNINGS)
	public List<ProductStateWarning> getAllStateWarnings(HttpServletRequest request){
		return this.productStateWarningService.findAllStateWarnings();
	}

	/**
	 * Returns a list of Restriction Code Types based on the Restriction Code Types code table.
	 * @param request the http request.
	 * @return list of description types.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = RATING_RESTRICTION)
	public List<SellingRestrictionCode> getRestrictions(HttpServletRequest request) {
		this.logRating(request.getRemoteAddr());
		List<SellingRestrictionCode> result=this.service.getAllRestrictions();
		return result;
	}

	/**
	 * Returns a list of Restriction Types based on the Restriction Types code table.
	 * @param request the http request.
	 * @return list of description types.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = RATING_RESTRICTION_GROUP)
	public List<SellingRestriction> getRestrictionGroup(HttpServletRequest request) {
		this.logRestrictions(request.getRemoteAddr());
		List<SellingRestriction> result=this.service.getAllRestrictionGroups();
		return result;
	}

	@EditPermission
	@RequestMapping (method=RequestMethod.POST, value = UPDATE_RATING_RESTRICTION)
	public ModifiedEntity<ProductMaster> updateRatingsAndRestrictions(@RequestBody ProductRestrictionUpdates update,
																	  HttpServletRequest request){
		RatingRestrictionController.logger.info(String.format(RESTRICTION_UPDATE_REQUEST, this.userInfo.getUserId(), request.getRemoteAddr(), update.getProdId()));
		ProductMaster updatedProductMaster = this.service.getUpdatedProductMaster(update, this.userInfo.getUserId());
		String updateMessage = this.messageSource.getMessage(
				RatingRestrictionController.UPDATE_SUCCESS_MESSAGE,
				new Object[]{update.getProdId()}, String.format(RatingRestrictionController.DEFAULT_UPDATE_SUCCESS_MESSAGE, update.getProdId()), request.getLocale());
		RatingRestrictionController.logger.info(String.format(RESTRICTION_UPDATE_COMPLETE, this.userInfo.getUserId(), request.getRemoteAddr(), update.getProdId()));
		return new ModifiedEntity<>(updatedProductMaster, updateMessage);
	}

	/**
	 * Update shipping and handling details for special attributes.
	 *
	 * @param productMaster the product master
	 * @param request       the request
	 * @return the modified entity
	 */
	@EditPermission
	@RequestMapping(method=RequestMethod.POST, value = UPDATE_SHIPPING_HANDLING_CHANGES)
	public ModifiedEntity<ProductMaster> updateShippingHandlingChanges(@RequestBody ProductMaster productMaster, HttpServletRequest request) {

		ProductMaster updatedProductMaster = this.service.updateShippingHandlingChanges(productMaster, this.userInfo.getUserId());

		String updateMessage = this.messageSource.getMessage(
				RatingRestrictionController.DEFAULT_UPDATE_SUCCESS_MESSAGE,
				new Object[]{productMaster.getProdId()}, String.format(RatingRestrictionController.UPDATE_SUCCESS_MESSAGE,
						productMaster.getProdId()), request.getLocale());

		return new ModifiedEntity<>(updatedProductMaster, updateMessage);
	}

	/**
	 * Update shipping restrictions for shipping and handling for special attributes.
	 *
	 * @param productRestrictionUpdates the product restriction updates
	 * @param request                   the request
	 * @return the modified entity
	 */
	@EditPermission
	@RequestMapping(method = RequestMethod.POST, value = UPDATE_SHIPPING_RESTRICTIONS)
	public ModifiedEntity<ProductMaster> updateShippingRestrictions(@RequestBody ProductRestrictionUpdate productRestrictionUpdates,
										   HttpServletRequest request) {

    ProductMaster updatedProductMaster = this.service.updateShippingRestrictions(productRestrictionUpdates.getProductRestrictions(),
				productRestrictionUpdates.prodId, this.userInfo.getUserId());

		String updateMessage = this.messageSource.getMessage(
				RatingRestrictionController.DEFAULT_UPDATE_SUCCESS_MESSAGE,
				new Object[]{productRestrictionUpdates.getProdId()}, String.format(RatingRestrictionController.UPDATE_SUCCESS_MESSAGE,
						productRestrictionUpdates.getProdId()), request.getLocale());

		return new ModifiedEntity<>(updatedProductMaster, updateMessage);

	}

	/**
	 * Update shipping method of shipping and handling for special attributes.
	 *
	 * @param shippingMethodUpdate the shipping method update
	 * @param request              the request
	 * @return the modified entity
	 */
	@EditPermission
	@RequestMapping(method = RequestMethod.POST, value = UPDATE_SHIPPING_METHODS)
	public ModifiedEntity<ProductMaster> updateShippingMethods(@RequestBody ShippingMethodUpdate shippingMethodUpdate, HttpServletRequest request) {

		ProductMaster updatedProductMaster = this.service.updateShippingMethods(shippingMethodUpdate.getCustomShippingMethods(),
				shippingMethodUpdate.getProdId(), this.userInfo.getUserId());

		String updateMessage = this.messageSource.getMessage(
				RatingRestrictionController.DEFAULT_UPDATE_SUCCESS_MESSAGE,
				new Object[]{shippingMethodUpdate.getProdId()}, String.format(RatingRestrictionController.UPDATE_SUCCESS_MESSAGE,
						shippingMethodUpdate.getProdId()), request.getLocale());

		return new ModifiedEntity<>(updatedProductMaster, updateMessage);
	}

	/**
	 * Update product state warnings for special attributes.
	 *
	 * @param productStateWarningUpdate the product state warning update
	 * @param request                   the request
	 * @return the modified entity
	 */
	@EditPermission
	@RequestMapping(method = RequestMethod.POST, value = UPDATE_PRODUCT_STATE_WARNINGS)
	public ModifiedEntity<ProductMaster> updateProductStateWarnings(@RequestBody ProductStateWarningUpdate productStateWarningUpdate, HttpServletRequest request) {

		ProductMaster updatedProductMaster = this.service.updateProductStateWarnings(productStateWarningUpdate.getProductStateWarnings(),
				productStateWarningUpdate.getProdId(), this.userInfo.getUserId());

		String updateMessage = this.messageSource.getMessage(
				RatingRestrictionController.DEFAULT_UPDATE_SUCCESS_MESSAGE,
				new Object[]{productStateWarningUpdate.getProdId()}, String.format(RatingRestrictionController.UPDATE_SUCCESS_MESSAGE,
						productStateWarningUpdate.getProdId()), request.getLocale());

		return new ModifiedEntity<>(updatedProductMaster, updateMessage);
	}

	/**
	 * Sets the UserInfo for this class to use. This is primarily for testing.
	 * @param userInfo The UserInfo for this class to use.
	 */
	public void setUserInfo(UserInfo userInfo) {
		this.userInfo = userInfo;
	}

	/**
	 * Log's a user's request to get all records for a prodId.
	 * @param ip The IP address th user is logged in from.
	 */
	private void logRestrictions(String ip){
		this.logger.info(String.format(RESTRICTION, this.userInfo.getUserId(), ip));
	}

	/**
	 * Log's a user's request to get all records for a prodId.
	 * @param ip The IP address th user is logged in from.
	 */
	private void logRating(String ip){
		this.logger.info(String.format(RESTRICTION_CODE, this.userInfo.getUserId(), ip));
	}

	/**
	 * Retrieves Shipping and Handling audit information.
	 * @param prodId The Product ID that the audit is being searched on.
	 * @param request The HTTP request that initiated this call.
	 * @return The list of Shipping and Handling audits attached to given product ID.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = RatingRestrictionController.GET_SHIPPING_HANDLING_AUDITS)
	public List<AuditRecord> getShippingHandlingAuditInfo(@RequestParam(value="prodId") Long prodId, HttpServletRequest request) {
		this.logGetShippingHandlingAuditInformation(request.getRemoteAddr(), prodId);
		List<AuditRecord> codeDateAuditRecords = this.service.getShippingHandlingAuditInformation(prodId);
		return codeDateAuditRecords;
	}

	/**
	 * Logs get Shipping and Handling audit information by prodId.
	 *
	 * @param ip The user's ip.
	 * @param prodId The prodId being searched on.
	 */
	private void logGetShippingHandlingAuditInformation(String ip, Long prodId) {
		RatingRestrictionController.logger.info(
				String.format(RatingRestrictionController.LOG_SHIPPING_HANDLING_AUDIT_BY_PRODUCT_ID, this.userInfo.getUserId(), ip, prodId)
		);
	}

	/**
	 * Retrieves Rating and Restrictions audit information.
	 * @param prodId The Product ID that the audit is being searched on.
	 * @param request The HTTP request that initiated this call.
	 * @return The list of Rating and Restrictions audits attached to given product ID.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = RatingRestrictionController.GET_RATING_RESTRICTION_AUDITS)
	public List<AuditRecord> getRatingRestrictionsAuditInfo(@RequestParam(value="prodId") Long prodId, HttpServletRequest request) {
		this.logRatingRestrictionsAuditInformation(request.getRemoteAddr(), prodId);
		List<AuditRecord> ratingRestrictionsAuditRecords = this.service.getRatingRestrictionsAuditInformation(prodId);
		return ratingRestrictionsAuditRecords;
	}

	/**
	 * Logs get Rating and Restrictions audit information by prodId.
	 *
	 * @param ip The user's ip.
	 * @param prodId The prodId being searched on.
	 */
	private void logRatingRestrictionsAuditInformation(String ip, Long prodId) {
		RatingRestrictionController.logger.info(
				String.format(RatingRestrictionController.LOG_RATING_RESTRICTIONS_AUDIT_BY_PRODUCT_ID, this.userInfo.getUserId(), ip, prodId)
		);
	}
}
