package com.heb.pm.productDetails.casePack;
import com.heb.jaf.security.AuthorizedResource;
import com.heb.jaf.security.EditPermission;
import com.heb.jaf.security.ViewPermission;
import com.heb.pm.ApiConstants;
import com.heb.pm.ResourceConstants;
import com.heb.pm.entity.*;
import com.heb.util.audit.AuditRecord;
import com.heb.util.controller.ModifiedEntity;
import com.heb.util.controller.NonEmptyParameterValidator;
import com.heb.util.controller.UserInfo;
import com.heb.util.jpa.LazyObjectResolver;
import com.heb.util.list.ListFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
/**
 * Resstful service for updating the Warehouse Location Item attributes in item master.
 * @author  s753601
 * @since 2.8.0
 */
@RestController()
@RequestMapping(ApiConstants.BASE_APPLICATION_URL + WarehouseLocationItemController.WAREHOUSE_ITEM_LOCATION_URL)
@AuthorizedResource(ResourceConstants.WAREHOUSE_INFO)
public class WarehouseLocationItemController {
	private static final Logger logger = LoggerFactory.getLogger(WarehouseLocationItemController.class);
	protected static final String WAREHOUSE_ITEM_LOCATION_URL = "/warehouse";
	protected static final String WAREHOUSE_ITEM_ID = "/getWarehouseInformation";
	protected static final String QUANTITY_ORDER_TYPES = "/getOrderQuantityTypes";
	protected static final String FLOW_TYPES = "/getFlowTypes";
	protected static final String COMMENT_AND_REMARKS="/getCommentAndRemarks";
	protected static final String SAVE_WAREHOUSE_ITEM_LOCATION = "/saveWarehouseItemLocation";
	protected static final String SAVE_COMMENT_AND_REMARKS="/saveCommentAndRemarks";
	protected static final String GET_WAREHOUSE_AUDIT_INFO = "/getWarehouseAuditInfo";

	private static final String NO_WAREHOUSE_LOCATION_ITEM__KEY = "VendorInfoController.missingItemId";
	private static final String NO_WAREHOUSE_LOCATION_ITEM_ID = "Vendor cannot be found.";

	private static final String WAREHOUSE_LOCATION_ITEM_SEARCH_BY_ITEM_ID =
			"User %s from IP %s searched for warehouse location item by Item Id %d";
	private static final String FETCH_ORDER_QUANTITY_TYPE=
			"User %s from IP %s fetched all of the Order Quantity types";
	private static final String FETCH_FLOW_TYPE=
			"User %s from IP %s fetched all of the Flow types";
	private static final String FETCH_REMARKS=
			"User %s from IP %s fetched all of the for item %d at location %d";
	private static final String WAREHOUSE_LOCATION_ITEM_SAVE_LOG =
			"User %s from IP %s requested to update the list of warehouse location item [%s].";
	private static final String UPDATE_SUCCESSFUL = "Updated successfully";

	private static final String WAREHOUSE_LOCATION_ITEM_UPDATE_COMMENT_REMARK_LOG =
			"User %s from IP %s requested to update a comment and remark details of warehouse location item [%d].";
	private static final String WAREHOUSE_LOCATION_ITEM_UPDATE_COMMENT_REMARK =
			"Warehouse Item Update Comment: Updated successfully.";
	private static final String LOG_WAREHOUSE_AUDIT_BY_PRODUCT_ID = "User %s from IP %s has requested case pack warehouse audit information for item code: %s and item type: %s";

	@Autowired
	private UserInfo userInfo;

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private NonEmptyParameterValidator parameterValidator;

	@Autowired
	private WarehouseLocationItemService service;

	private WarehouseLocationItemResolver resolver = new WarehouseLocationItemResolver();

	private class WarehouseLocationItemResolver implements LazyObjectResolver<WarehouseLocationItem> {

		/**
		 * Currently this fetch is getting:
		 * 1. the key
		 * 2. the order quantity type
		 * 3. the flow type
		 * 4. extended warehouse location attributes
		 * @param warehouseLocationItem
		 */
		@Override
		public void fetch(WarehouseLocationItem warehouseLocationItem) {
			warehouseLocationItem.getKey().getItemCode();
			if(warehouseLocationItem.getLocation() != null){
				warehouseLocationItem.getLocation().getKey().getLocationNumber();
			}
			if(warehouseLocationItem.getOrderQuantityType() != null){
				warehouseLocationItem.getOrderQuantityType().getAbbreviation();
			}
			if(warehouseLocationItem.getFlowType() != null){
				warehouseLocationItem.getFlowType().getAbbreviation();
			}
			if(warehouseLocationItem.getWarehouseLocationItemExtendedAttributes() !=null ){
				if(warehouseLocationItem.getLocation() != null) {
					warehouseLocationItem.getLocation().getKey();
				}
			}
		}
	}

	/**
	 * Searches for a particular warehouse location item by Item Id.
	 *
	 * @param itemCode The AP number of the vendor you wish to find.
	 * @param request The HTTP request that initiated this call.
	 * @return The vendor matching the AP number you passed in. Null if not found.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = WarehouseLocationItemController.WAREHOUSE_ITEM_ID)
	public List<WarehouseLocationItem> findByItemId(@RequestParam Long itemCode, @RequestParam String itemType, HttpServletRequest request){
		this.parameterValidator.validate(itemCode, WarehouseLocationItemController.NO_WAREHOUSE_LOCATION_ITEM_ID,
				WarehouseLocationItemController.NO_WAREHOUSE_LOCATION_ITEM__KEY, request.getLocale());
		this.logFindByItemId(request.getRemoteAddr(), itemCode);
		List<WarehouseLocationItem> results = this.service.findAllWarehouseLocationItemsByItemCode(itemCode, itemType);
		results.forEach(this.resolver::fetch);
		return results;
	}

	/**
	 * Searches for a particular warehouse location item by Item Id.
	 *
	 * @param request The HTTP request that initiated this call.
	 * @return The vendor matching the AP number you passed in. Null if not found.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = WarehouseLocationItemController.QUANTITY_ORDER_TYPES)
	public List<OrderQuantityType> findAllOrderQuantityType(HttpServletRequest request){
		this.logFindAllOrderQuantityTypes(request.getRemoteAddr());
		List<OrderQuantityType> results = this.service.findAllOrderQuantityTypes();
		return results;
	}

	/**
	 * Searches for a particular warehouse location item by Item Id.
	 *
	 * @param request The HTTP request that initiated this call.
	 * @return The vendor matching the AP number you passed in. Null if not found.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = WarehouseLocationItemController.FLOW_TYPES)
	public List<FlowType> findAllFlowType(HttpServletRequest request){
		this.logFindAllFlowTypes(request.getRemoteAddr());
		List<FlowType> results = this.service.findAllFlowTypes();
		return results;
	}

	/**
	 * Searches for all remarks for an item in an warehouse
	 * @param key the unique identifier that tells the controller what item and in which warehouse to find the remarks
	 * for.
	 * @param request The HTTP request that initiated this call.
	 * @return
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.POST, value = WarehouseLocationItemController.COMMENT_AND_REMARKS)
	public List<ItemWarehouseComments> findAllItemWarehouseCommentsByItemAndWarehouse(@RequestBody ItemWarehouseCommentsKey key, HttpServletRequest request){
		this.logFindAllRemarks(request.getRemoteAddr(), key);
		List<ItemWarehouseComments> results = this.service.findAllItemWarehouseCommentsByItemAndWarehouse(key);
		return results;
	}

	/**
	 * Updates list of warehouse location item.
	 *
	 * @param warehouseLocationItems The list of warehouse location item to be updated.
	 * @param request the HTTP request that initiated this call.
	 * @return The list of warehouse location item and a message for the front end.
	 */
	@EditPermission
	@RequestMapping(method = RequestMethod.POST,  value = WarehouseLocationItemController.SAVE_WAREHOUSE_ITEM_LOCATION)
	public ModifiedEntity<List<WarehouseLocationItem>> saveWarehouseItemLocation(@RequestBody List<WarehouseLocationItem> warehouseLocationItems,
															HttpServletRequest request){
		this.logSaveWarehouseItemLocation(request.getRemoteAddr(), warehouseLocationItems);
		List<WarehouseLocationItem> warehouseLocationItemsUpdate = this.service.updateWarehouseLocationItems
				(warehouseLocationItems);
		warehouseLocationItemsUpdate.forEach(this.resolver::fetch);
		return new ModifiedEntity<>(warehouseLocationItemsUpdate, WarehouseLocationItemController.UPDATE_SUCCESSFUL);
	}

	/**
	 * Update for all remark and comments and item in an warehouse.
	 * @param warehouseLocationItem warehouse Location Item.
	 * @param request
	 * @return
	 */
	@EditPermission
	@RequestMapping(method = RequestMethod.POST, value = WarehouseLocationItemController.SAVE_COMMENT_AND_REMARKS)
	public ModifiedEntity<String> saveCommentAndRemark(
			@RequestBody WarehouseLocationItem warehouseLocationItem, HttpServletRequest request){

		this.logSaveCommentAndRemark(request.getRemoteAddr(),warehouseLocationItem.getKey().getWarehouseNumber());

		this.service.saveRemarkAndCommentForWarehouse(warehouseLocationItem);



		return new ModifiedEntity<>(WarehouseLocationItemController.UPDATE_SUCCESSFUL,
				WarehouseLocationItemController.WAREHOUSE_LOCATION_ITEM_UPDATE_COMMENT_REMARK);
	}

	/**
	 * Logs the update by item Id
	 * @param ip the users ip
	 */
	private void logSaveCommentAndRemark(String ip, int warehouseLocationItems) {
		WarehouseLocationItemController.logger.info(
				String.format(WarehouseLocationItemController.WAREHOUSE_LOCATION_ITEM_UPDATE_COMMENT_REMARK_LOG,
						this.userInfo.getUserId(),
						ip, warehouseLocationItems)
		);
	}
	/**
	 * Logs the findy by item Id
	 * @param ip the users ip
	 * @param itemId the item id that is being searched on.
	 */
	private void logFindByItemId(String ip, long itemId) {
		WarehouseLocationItemController.logger.info(
				String.format(WarehouseLocationItemController.WAREHOUSE_LOCATION_ITEM_SEARCH_BY_ITEM_ID, this.userInfo.getUserId(),
						ip, itemId)
		);
	}

	/**
	 * Logs the find all order quantity types request
	 * @param ip the users ip
	 */
	private void logFindAllOrderQuantityTypes(String ip) {
		WarehouseLocationItemController.logger.info(
				String.format(WarehouseLocationItemController.FETCH_ORDER_QUANTITY_TYPE, this.userInfo.getUserId(),
						ip)
		);
	}

	/**
	 * Logs the find all flow types request
	 * @param ip the users ip
	 */
	private void logFindAllFlowTypes(String ip) {
		WarehouseLocationItemController.logger.info(
				String.format(WarehouseLocationItemController.FETCH_FLOW_TYPE, this.userInfo.getUserId(),
						ip)
		);
	}

	/**
	 * Logs the find all flow types request
	 * @param ip the users ip
	 */
	private void logFindAllRemarks(String ip, ItemWarehouseCommentsKey key) {
		WarehouseLocationItemController.logger.info(
				String.format(WarehouseLocationItemController.FETCH_REMARKS, this.userInfo.getUserId(),
						ip, key.getItemId(), key.getWarehouseNumber())
		);
	}

	/**
	 * Logs the update the list of warehouse location item.
	 * @param ip the users ip
	 */
	private void logSaveWarehouseItemLocation(String ip, List<WarehouseLocationItem> warehouseLocationItems) {
		WarehouseLocationItemController.logger.info(
				String.format(WarehouseLocationItemController.WAREHOUSE_LOCATION_ITEM_SAVE_LOG, this.userInfo.getUserId(),
						ip, ListFormatter.formatAsString(warehouseLocationItems))
		);
	}

	/**
	 * Sets the warehouseLocationItemService for this object to use. This is primarily for testing.
	 *
	 * @param warehouseLocationItemService The warehouseLocationItemService for this object to use.
	 */
	public void setService(WarehouseLocationItemService warehouseLocationItemService) {
		this.service = warehouseLocationItemService;
	}

	/**
	 * Sets the UserInfo for this class to use. This is primarily for testing.
	 *
	 * @param userInfo The UserInfo for this class to use.
	 */
	public void setUserInfo(UserInfo userInfo) {
		this.userInfo = userInfo;
	}

	/**
	 * Sets the NonEmptyParameterValidator for this class to use. This is primarily for testing.
	 *
	 * @param parameterValidator The NonEmptyParameterValidator for this class to use.
	 */
	public void setParameterValidator(NonEmptyParameterValidator parameterValidator) {
		this.parameterValidator = parameterValidator;
	}

	/**
	 * Sets the message source for this class to use. This is primarily for testing.
	 *
	 * @param messageSource The NonEmptyParameterValidator for this class to use.
	 */
	public void setMessageSource(MessageSource messageSource){
		this.messageSource = messageSource;
	}

	/**
	 * Get warehouse audit information.
	 * @param itemCode The item code the audit is being searched on
	 * @param itemType The item type the audit is being searched on
	 * @param request The HTTP request that initiated this call.
	 * @return The list of warehouse audits attached to given product ID.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = WarehouseLocationItemController.GET_WAREHOUSE_AUDIT_INFO)
	public List<AuditRecord> getWarehouseAuditInfo(@RequestParam(value="itemCode") Long itemCode, @RequestParam(value="itemType") String itemType, HttpServletRequest request){
		this.logGetWarehouseAuditInfo(request.getRemoteAddr(), itemCode, itemType);
		List<AuditRecord> warehouseAuditRecords = this.service.getWarehouseAuditInformation(itemCode, itemType);
		return warehouseAuditRecords;
	}

	/**
	 * Logs get Warehouse audit information by itemcode and itemtype.
	 *
	 * @param ip The user's ip.
	 * @param itemCode the item code
	 * @param itemType the item type
	 */
	private void logGetWarehouseAuditInfo(String ip, Long itemCode, String itemType) {
		WarehouseLocationItemController.logger.info(
				String.format(WarehouseLocationItemController.LOG_WAREHOUSE_AUDIT_BY_PRODUCT_ID, this.userInfo.getUserId(), ip, itemCode, itemType)
		);
	}
}
