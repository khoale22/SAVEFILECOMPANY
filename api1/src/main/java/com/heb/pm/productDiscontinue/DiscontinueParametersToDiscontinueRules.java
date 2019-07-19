package com.heb.pm.productDiscontinue;

import com.heb.pm.entity.*;
import com.heb.pm.product.UpcService;
import com.heb.pm.productHierarchy.ClassCommodityService;
import com.heb.pm.productHierarchy.ItemClassService;
import com.heb.pm.productHierarchy.SubCommodityService;
import com.heb.pm.productHierarchy.SubDepartmentService;
import com.heb.pm.vendor.VendorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * Converts between DiscontinueParameters and DiscontinueRules.
 *
 * @author d116773
 * @since 2.0.2
 */
@Service
class DiscontinueParametersToDiscontinueRules {

	private static final int DEFAULT_SEQUENCE_NUMBER = 1;
	private static final int ORDER_DATE_SEQUENCE_NUMBER = 2;
	private static final int DAYS_PER_MONTH = 30;
	/**
	 * Purchase orders are a special case scenario where the sequence number can be 1 or 2. The value we are looking for
	 * is 2.
	 */
	private static final int PURCHASE_ORDERS_SEQUENCE_NUMBER = 2;
	/**
	 * Constant that represents the DELETE action.
	 */
	private static final String DELETE_ACTION_CODE = "DEL";
	/**
	 * Constant that represents the ADD action.
	 */
	private static final String ADD_ACTION_CODE = "ADD";
	/**
	 * Constant that represents the MODIFY action.
	 */
	private static final String MODIFY_ACTION_CODE = "MOD";
	/**
	 * Constant that represent Scan Based Trade.
	 */
	private static final String STRING_SCAN_BASED_TRADE = "SCAN BASED TRADE";


	private static final Logger logger = LoggerFactory.getLogger(DiscontinueParametersToDiscontinueRules.class);

	// Delegates searching for vendors to this service
	@Autowired
	private VendorService vendorService;

	// Delegates searching for commodities to this service
	@Autowired
	private ClassCommodityService classCommodityService;

	// Delegates searching for subDepartments to this service
	@Autowired
	private SubDepartmentService subDepartmentService;

	// Delegates searching for subCommodity to this service
	@Autowired
	private SubCommodityService subCommodityService;

	// Delegates searching for upc to this service
	@Autowired
	private UpcService upcService;

	// Delegates searching for item-class to this service
	@Autowired
	private ItemClassService itemClassService;

	/**
	 * Takes a set of discontinue exception parameters and converts them into a DiscontinueRules.
	 *
	 * @param exceptionParameters The set of product discontinue exception rules.
	 * @return A DiscontinueRules.
	 */
	public DiscontinueRules toDiscontinueRulesFromExceptions(List<DiscontinueExceptionParameters> exceptionParameters) {

		DiscontinueRules dro = new DiscontinueRules();
		logger.info("Returns the list of values toDiscontinueRulesFromExceptions ::"+exceptionParameters.size());
		// Some of the information needed is consistent across all the objects coming in, so it
		// just needs to be set once.
		DiscontinueExceptionParameters headerRecord = exceptionParameters.get(0);

		dro.setExceptionType(headerRecord.getExceptionType());
		dro.setExceptionTypeId(headerRecord.getExceptionTypeId());
		logger.info("headerRecord.getExceptionType().trim()) toDiscontinueRulesFromExceptions ::"+headerRecord.getExceptionType().trim());
		logger.info("dro.getExceptionTypeId() toDiscontinueRulesFromExceptions ::"+dro.getExceptionTypeId());
		dro.setExceptionName(this.getExceptionNameByID(
				ProductDiscontinueExceptionType.fromString(headerRecord.getExceptionType().trim()),
				dro.getExceptionTypeId()));
		dro.setNeverDiscontinueSwitch(headerRecord.isNeverDelete());
		dro.setExceptionNumber(headerRecord.getKey().getExceptionNumber());
		dro.setPriorityNumber(headerRecord.getPriority());

		// The remaining fields are stripped from different DiscontinueExceptionParameters records.
		for (DiscontinueExceptionParameters parm : exceptionParameters) {

			this.toStoreSales(dro, parm.getKey(), parm.getAttributes());
			this.toAddedDate(dro, parm.getKey(), parm.getAttributes());
			this.toWarehouseInventory(dro, parm.getKey(), parm.getAttributes());
			this.toStoreInventory(dro, parm.getKey(), parm.getAttributes());
			this.toLastReceived(dro, parm.getKey(), parm.getAttributes());
			this.toOrderedDate(dro, parm.getKey(), parm.getAttributes());
		}
		return dro;
	}

	/**
	 * Takes a list of product discontinue default rules and converts them into a DiscontinueRules.
	 *
	 * @param discontinueParameters The list of product discontinue default rules.
	 * @return A DiscontinueRules.
	 */
	public DiscontinueRules toDiscontinueRules(List<DiscontinueParameters> discontinueParameters) {

		DiscontinueRules dro = new DiscontinueRules();

		for (DiscontinueParameters parm : discontinueParameters) {
			this.toStoreSales(dro, parm.getKey(), parm.getAttributes());
			this.toAddedDate(dro, parm.getKey(), parm.getAttributes());
			this.toWarehouseInventory(dro, parm.getKey(), parm.getAttributes());
			this.toStoreInventory(dro, parm.getKey(), parm.getAttributes());
			this.toLastReceived(dro, parm.getKey(), parm.getAttributes());
			this.toOrderedDate(dro, parm.getKey(), parm.getAttributes());
		}

		return dro;
	}


	/**
	 * Takes a DiscontinueRules object and converts it to a list of DiscontinueParameters.
	 *
	 * @param discontinueRules The DiscontinueRules to convert.
	 * @return The records extracted from the DiscontinueRules.
	 */
	public List<DiscontinueParameters> toDiscontinueParameters(DiscontinueRules discontinueRules) {
		List<DiscontinueParameters> discontinueParametersList = new ArrayList<>();
		DiscontinueParameters adminParams;
		DiscontinueParametersKey adminParamsKey;
		for(DiscontinueParameterType discontinueParameterType
				: DiscontinueParameterType.allTypes) {

			adminParams = new DiscontinueParameters();

			adminParamsKey = new DiscontinueParametersKey();
			adminParamsKey.setId(discontinueParameterType.getId());
			// For purchase orders, the sequence number is different than all the others.
			if (discontinueParameterType == DiscontinueParameterType.PURCHASE_ORDERS) {
				adminParamsKey.setSequenceNumber(DiscontinueParametersToDiscontinueRules.ORDER_DATE_SEQUENCE_NUMBER);
			} else {
				adminParamsKey.setSequenceNumber(DiscontinueParametersToDiscontinueRules.DEFAULT_SEQUENCE_NUMBER);
			}
			adminParams.setKey(adminParamsKey);

			this.toCommonAttributes(adminParams.getAttributes(), discontinueParameterType, discontinueRules);

			discontinueParametersList.add(adminParams);
		}
		return discontinueParametersList;
	}

	/**
	 * Takes a set of DiscontinueRules and converts them to a list of DiscontinueExceptionParameters.
	 *
	 * @param discontinueRules The DiscontinueRules to convert.
	 * @return The records extracted from the DiscontinueRules.
	 */
	public List<DiscontinueExceptionParameters> toDiscontinueExceptionParameters(DiscontinueRules discontinueRules) {

		List<DiscontinueExceptionParameters> discontinueParametersList = new ArrayList<>();


		for(DiscontinueParameterType discontinueParameterType:
				DiscontinueParameterType.allTypes) {
			DiscontinueExceptionParameters exceptionParameters = new DiscontinueExceptionParameters();

			DiscontinueExceptionParametersKey exceptionParametersKey = new DiscontinueExceptionParametersKey();
			exceptionParametersKey.setId(discontinueParameterType.getId());
			// For purchase orders, the sequence number is different than all the others.
			if (discontinueParameterType == DiscontinueParameterType.PURCHASE_ORDERS) {
				exceptionParametersKey.setSequenceNumber(
						DiscontinueParametersToDiscontinueRules.ORDER_DATE_SEQUENCE_NUMBER);
			} else {
				exceptionParametersKey.setSequenceNumber(
						DiscontinueParametersToDiscontinueRules.DEFAULT_SEQUENCE_NUMBER);
			}
			exceptionParameters.setKey(exceptionParametersKey);

			exceptionParametersKey.setExceptionNumber(discontinueRules.getExceptionNumber());
			exceptionParameters.setPriority(discontinueRules.getPriorityNumber());
			exceptionParameters.setExceptionType(discontinueRules.getExceptionType());
			exceptionParameters.setExceptionTypeId(discontinueRules.getExceptionTypeId());
			exceptionParameters.setNeverDelete(discontinueRules.isNeverDiscontinueSwitch());

			this.toCommonAttributes(exceptionParameters.getAttributes(),
					discontinueParameterType, discontinueRules);

			discontinueParametersList.add(exceptionParameters);
		}
		return discontinueParametersList;
	}

	/**
	 * Normalizes the attributes that are defined in DiscontinueRules to their values in either a default
	 * or exception rule.
	 *
	 * @param attributes The default or exception rule attributes to populate.
	 * @param discontinueParameterType The type of parameter to be populating.
	 * @param discontinueRules The rules to extract attributes from.
	 */
	private void toCommonAttributes(DiscontinueParameterCommonAttributes attributes,
									DiscontinueParameterType discontinueParameterType,
									DiscontinueRules discontinueRules) {
		if(DiscontinueParameterType.STORE_SALES == discontinueParameterType) {
			attributes.setParameterValue(stringValueToDays(discontinueRules.getStoreSales()));
			attributes.setActive(discontinueRules.isSalesSwitch());
		}else if(DiscontinueParameterType.NEW_ITEM_PERIOD  == discontinueParameterType) {
			attributes.setParameterValue(stringValueToDays(discontinueRules.getNewItemPeriod()));
			attributes.setActive(discontinueRules.isNewProductSetupSwitch());
		}else if(DiscontinueParameterType.WAREHOUSE_UNITS == discontinueParameterType) {
			attributes.setParameterValue(discontinueRules.getWarehouseUnits());
			attributes.setActive(discontinueRules.isWarehouseUnitSwitch());
		} else if(DiscontinueParameterType.STORE_UNITS  == discontinueParameterType) {
			attributes.setParameterValue(discontinueRules.getStoreUnits());
			attributes.setActive(discontinueRules.isStoreUnitSwitch());
		}else if(DiscontinueParameterType.STORE_RECEIPTS  == discontinueParameterType) {
			attributes.setParameterValue(stringValueToDays(discontinueRules.getStoreReceipts()));
			attributes.setActive(discontinueRules.isReceiptsSwitch());
		} else if (DiscontinueParameterType.PURCHASE_ORDERS == discontinueParameterType) {
			attributes.setParameterValue(stringValueToDays(discontinueRules.getPurchaseOrders()));
			attributes.setActive(discontinueRules.isPurchaseOrderSwitch());
		}
	}

	/**
	 * If this record represents a store sales parameter, it sets the appropriate values in the DiscontinueRules
	 * object passed in.
	 *
	 * @param dro The DiscontinueRules object to populate.
	 * @param key The key of this rule. This will contain the type of rule it is.
	 * @param attributes The part of the rule that contains the values to extract.
	 */
	private void toStoreSales(DiscontinueRules dro, DiscontinueParameterCommonKey key,
							  DiscontinueParameterCommonAttributes attributes) {

		if (key.getId() == DiscontinueParameterType.STORE_SALES.getId()) {
			dro.setSalesSwitch(attributes.isActive());
			dro.setStoreSales(this.stringValueToMonths(attributes.getParameterValue()));
		}
	}

	/**
	 * If this record represents an added date parameter, it sets the appropriate values in the DiscontinueRules
	 * object passed in.
	 *
	 * @param dro The DiscontinueRules object to populate.
	 * @param key The key of this rule. This will contain the type of rule it is.
	 * @param attributes The part of the rule that contains the values to extract.
	 */
	private void toAddedDate(DiscontinueRules dro, DiscontinueParameterCommonKey key,
							 DiscontinueParameterCommonAttributes attributes) {
		if ( key.getId() == DiscontinueParameterType.NEW_ITEM_PERIOD.getId()) {
			dro.setNewProductSetupSwitch(attributes.isActive());
			dro.setNewItemPeriod(this.stringValueToMonths(attributes.getParameterValue()));
		}
	}

	/**
	 * If this record represents a warehouse inventory parameter, it sets the appropriate values in the DiscontinueRules
	 * object passed in.
	 *
	 * @param dro The DiscontinueRules object to populate.
	 * @param key The key of this rule. This will contain the type of rule it is.
	 * @param attributes The part of the rule that contains the values to extract.
	 */
	private void toWarehouseInventory(DiscontinueRules dro, DiscontinueParameterCommonKey key,
									  DiscontinueParameterCommonAttributes attributes) {
		if (key.getId() == DiscontinueParameterType.WAREHOUSE_UNITS.getId()) {
			dro.setWarehouseUnitSwitch(attributes.isActive());
			dro.setWarehouseUnits(attributes.getParameterValue());
		}
	}

	/**
	 * If this record represents a store inventory parameter, it sets the appropriate values in the DiscontinueRules
	 * object passed in.
	 *
	 * @param dro The DiscontinueRules object to populate.
	 * @param key The key of this rule. This will contain the type of rule it is.
	 * @param attributes The part of the rule that contains the values to extract.
	 */
	private void toStoreInventory(DiscontinueRules dro, DiscontinueParameterCommonKey key,
								  DiscontinueParameterCommonAttributes attributes) {

		if (key.getId() == DiscontinueParameterType.STORE_UNITS.getId()) {
			dro.setStoreUnitSwitch(attributes.isActive());
			dro.setStoreUnits(attributes.getParameterValue());
		}
	}

	/**
	 * If this record represents a store sales parameter, it sets the appropriate values in the DiscontinueRules
	 * object passed in.
	 *
	 * @param dro The DiscontinueRules object to populate.
	 * @param key The key of this rule. This will contain the type of rule it is.
	 * @param attributes The part of the rule that contains the values to extract.
	 */
	private void toLastReceived(DiscontinueRules dro, DiscontinueParameterCommonKey key,
								DiscontinueParameterCommonAttributes attributes) {
		if ( key.getId() == DiscontinueParameterType.STORE_RECEIPTS.getId()) {
			dro.setReceiptsSwitch(attributes.isActive());
			dro.setStoreReceipts(this.stringValueToMonths(attributes.getParameterValue()));
		}
	}

	/**
	 * If this record represents a PO parameter, it sets the appropriate values in the DiscontinueRules
	 * object passed in.
	 *
	 * @param dro The DiscontinueRules object to populate.
	 * @param key The key of this rule. This will contain the type of rule it is.
	 * @param attributes The part of the rule that contains the values to extract.
	 */
	private void toOrderedDate(DiscontinueRules dro, DiscontinueParameterCommonKey key,
							   DiscontinueParameterCommonAttributes attributes) {
		if (key.getId() == DiscontinueParameterType.PURCHASE_ORDERS.getId() &&
				key.getSequenceNumber() == DiscontinueParametersToDiscontinueRules.ORDER_DATE_SEQUENCE_NUMBER) {
			dro.setPurchaseOrderSwitch(attributes.isActive());
			dro.setPurchaseOrders(this.stringValueToMonths(attributes.getParameterValue()));
		}
	}

	/**
	 * Used to convert a string integer value from months to number of days.
	 * @param value the string int value
	 * @return String int value in days
	 */
	private String stringValueToDays(String value){
		try{
			return Integer.toString(Integer.parseInt(value.trim()) *
					DiscontinueParametersToDiscontinueRules.DAYS_PER_MONTH);
		}catch (NumberFormatException e){
			throw new IllegalArgumentException("Parameter cannot be null, empty, or parse to NAN.");
		}
	}

	/**
	 * Used to convert a string integer value from days to number of months.
	 * @param value the string int value
	 * @return String int value in months
	 */
	private String stringValueToMonths(String value){
		try{
			return Integer.toString(Integer.parseInt(value.trim()) /
					DiscontinueParametersToDiscontinueRules.DAYS_PER_MONTH);
		}catch (NumberFormatException e){
			throw new IllegalArgumentException("Parameter cannot be null, empty, or parse to NAN.");
		}
	}

	/**
	 * Creates a name for a product discontinue exception rule based on the needs of the GUI.
	 *
	 * @param exceptionType The type of exception it is.
	 * @param exceptionID The ID of the exception.
	 * @return A name for the exception rule.
	 */
	private String getExceptionNameByID(ProductDiscontinueExceptionType exceptionType, String exceptionID) {

		String exceptionName = exceptionID.trim();

		if (exceptionType.equals(ProductDiscontinueExceptionType.VENDOR)) {
			Vendor vendor = this.vendorService.findByVendorNumber(Integer.parseInt(exceptionName));
			exceptionName = vendor != null ? vendor.getDisplayName() : exceptionName;
		} else if (exceptionType.equals(ProductDiscontinueExceptionType.SBT)) {
			exceptionName = STRING_SCAN_BASED_TRADE;
		} else if (exceptionType.equals(ProductDiscontinueExceptionType.DEPT)) {
			SubDepartment subDepartment = this.subDepartmentService.findSubDepartment(exceptionName);
			exceptionName = subDepartment != null ? subDepartment.getDisplayName() : exceptionName;
		} else if (exceptionType.equals(ProductDiscontinueExceptionType.CLASS)) {
			ItemClass itemClass = this.itemClassService.findOne(exceptionName);
			exceptionName = itemClass != null ? itemClass.getDisplayName() : exceptionName;
		} else if (exceptionType.equals(ProductDiscontinueExceptionType.COMMODITY)) {
			ClassCommodity commodity = this.classCommodityService.findCommodity(Integer.parseInt(exceptionName));
			exceptionName = commodity != null ? commodity.getDisplayName() : exceptionName;
		} else if (exceptionType.equals(ProductDiscontinueExceptionType.SUBCOMMODITY)) {
			SubCommodity subCommodity = this.subCommodityService.findSubCommodity(Integer.parseInt(exceptionName));
			exceptionName = subCommodity != null ? subCommodity.getDisplayName() : exceptionName;
		} else if (exceptionType.equals(ProductDiscontinueExceptionType.UPC)){
			SellingUnit sellingUnit = this.upcService.find(Long.parseLong(exceptionName));
			exceptionName = sellingUnit != null ? sellingUnit.getDisplayName() : exceptionName;
		}

		// if the respective service did not find the object, log a message detailing type and id not found
		if(exceptionName.equals(exceptionID.trim())){
			logger.warn(exceptionType + " with id: " + exceptionName + " not found.");
		}

		return exceptionName;
	}


	/**
	 * Converts a list of DiscontinueParametersAudits to a list of DiscontinueParametersAuditRecords
	 *
	 * @param auditEntityList a list of DiscontinueParametersAudits.
	 * @return a list of DiscontinueParametersAuditRecords.
	 */
	public List<DiscontinueParametersAuditRecord> convertDiscontinueParametersAuditToDiscontinueParametersAuditRecord(List<DiscontinueParametersAudit> auditEntityList){
		List<DiscontinueParametersAuditRecord> auditList = new ArrayList<>();
		Map<DiscontinueParameterType, DiscontinueParametersAuditRecord> previousAuditMap = new HashMap<>();
		previousAuditMap.put(DiscontinueParameterType.STORE_SALES, null);
		previousAuditMap.put(DiscontinueParameterType.NEW_ITEM_PERIOD, null);
		previousAuditMap.put(DiscontinueParameterType.WAREHOUSE_UNITS, null);
		previousAuditMap.put(DiscontinueParameterType.STORE_UNITS, null);
		previousAuditMap.put(DiscontinueParameterType.STORE_RECEIPTS, null);
		previousAuditMap.put(DiscontinueParameterType.PURCHASE_ORDERS, null);
		DiscontinueParametersAudit auditEntity = null;
		DiscontinueParametersAuditRecord auditRecord = null;
		for(int x=0; x<auditEntityList.size(); x++) {
			auditEntity = auditEntityList.get(x);
			auditRecord = new DiscontinueParametersAuditRecord();
			auditRecord.setChangedBy(auditEntity.getUserId());
			auditRecord.setChangedOn(auditEntity.getTimestamp());
			auditRecord.setAction(auditEntity.getAction());

			// If the record is delete, then there's no new Active switch, or ParameterValue.
			if (!auditRecord.getAction().trim().equalsIgnoreCase(DELETE_ACTION_CODE)) {
				auditRecord.setNewActive(auditEntity.isActive());
				auditRecord.setNewAttributeValue(auditEntity.getParameterValue());
			}

			// if the Type matches the case, set the previous values for the new audit record to what is in the map
			// for that case (Type is the key) and replace the value results for that key with the new record so that
			// it can be used as the previous value for the next record. If the map is null for the key, it adds the value
			// to be used for the next record.
			// Add- previous value should be blank (as it was deleted before or non-existent)
			// Mod- previous value should be stored in map, and changes shown
			// del- new value will be blank (since not set above)
			if (DiscontinueParameterType.STORE_SALES == DiscontinueParameterType.getTypeById(auditEntity.getId())) {
				auditRecord.setAttributeName(DiscontinueParameterType.STORE_SALES.getDescription());
				if (previousAuditMap.get(DiscontinueParameterType.STORE_SALES) != null) {
					auditRecord.setPreviouslyActive(previousAuditMap.get(DiscontinueParameterType.STORE_SALES).isNewActive());
					auditRecord.setPreviouslyNeverDiscontinue(previousAuditMap.get(DiscontinueParameterType.STORE_SALES).isNewNeverDiscontinue());
					auditRecord.setPreviousAttributeValue(previousAuditMap.get(DiscontinueParameterType.STORE_SALES).getNewAttributeValue());
				}
				previousAuditMap.put(DiscontinueParameterType.STORE_SALES, auditRecord);
			} else if (DiscontinueParameterType.NEW_ITEM_PERIOD == DiscontinueParameterType.getTypeById(auditEntity.getId())) {
				auditRecord.setAttributeName(DiscontinueParameterType.NEW_ITEM_PERIOD.getDescription());
				if (previousAuditMap.get(DiscontinueParameterType.NEW_ITEM_PERIOD) != null) {
					auditRecord.setPreviouslyActive(previousAuditMap.get(DiscontinueParameterType.NEW_ITEM_PERIOD).isNewActive());
					auditRecord.setPreviouslyNeverDiscontinue(previousAuditMap.get(DiscontinueParameterType.NEW_ITEM_PERIOD).isNewNeverDiscontinue());
					auditRecord.setPreviousAttributeValue(previousAuditMap.get(DiscontinueParameterType.NEW_ITEM_PERIOD).getNewAttributeValue());
				}
				previousAuditMap.put(DiscontinueParameterType.NEW_ITEM_PERIOD, auditRecord);
			} else if (DiscontinueParameterType.WAREHOUSE_UNITS == DiscontinueParameterType.getTypeById(auditEntity.getId())) {
				auditRecord.setAttributeName(DiscontinueParameterType.WAREHOUSE_UNITS.getDescription());
				if (previousAuditMap.get(DiscontinueParameterType.WAREHOUSE_UNITS) != null) {
					auditRecord.setPreviouslyActive(previousAuditMap.get(DiscontinueParameterType.WAREHOUSE_UNITS).isNewActive());
					auditRecord.setPreviouslyNeverDiscontinue(previousAuditMap.get(DiscontinueParameterType.WAREHOUSE_UNITS).isNewNeverDiscontinue());
					auditRecord.setPreviousAttributeValue(previousAuditMap.get(DiscontinueParameterType.WAREHOUSE_UNITS).getNewAttributeValue());
				}
				previousAuditMap.put(DiscontinueParameterType.WAREHOUSE_UNITS, auditRecord);
			} else if (DiscontinueParameterType.STORE_UNITS == DiscontinueParameterType.getTypeById(auditEntity.getId())) {
				auditRecord.setAttributeName(DiscontinueParameterType.STORE_UNITS.getDescription());
				if (previousAuditMap.get(DiscontinueParameterType.STORE_UNITS) != null) {
					auditRecord.setPreviouslyActive(previousAuditMap.get(DiscontinueParameterType.STORE_UNITS).isNewActive());
					auditRecord.setPreviouslyNeverDiscontinue(previousAuditMap.get(DiscontinueParameterType.STORE_UNITS).isNewNeverDiscontinue());
					auditRecord.setPreviousAttributeValue(previousAuditMap.get(DiscontinueParameterType.STORE_UNITS).getNewAttributeValue());
				}
				previousAuditMap.put(DiscontinueParameterType.STORE_UNITS, auditRecord);
			} else if (DiscontinueParameterType.STORE_RECEIPTS == DiscontinueParameterType.getTypeById(auditEntity.getId())) {
				auditRecord.setAttributeName(DiscontinueParameterType.STORE_RECEIPTS.getDescription());
				if (previousAuditMap.get(DiscontinueParameterType.STORE_RECEIPTS) != null) {
					auditRecord.setPreviouslyActive(previousAuditMap.get(DiscontinueParameterType.STORE_RECEIPTS).isNewActive());
					auditRecord.setPreviouslyNeverDiscontinue(previousAuditMap.get(DiscontinueParameterType.STORE_RECEIPTS).isNewNeverDiscontinue());
					auditRecord.setPreviousAttributeValue(previousAuditMap.get(DiscontinueParameterType.STORE_RECEIPTS).getNewAttributeValue());
				}
				previousAuditMap.put(DiscontinueParameterType.STORE_RECEIPTS, auditRecord);
			}else if(DiscontinueParameterType.PURCHASE_ORDERS  == DiscontinueParameterType.getTypeById(auditEntity.getId())) {
				if(auditEntity.getSequenceNumber() == PURCHASE_ORDERS_SEQUENCE_NUMBER) {
					auditRecord.setAttributeName(DiscontinueParameterType.PURCHASE_ORDERS.getDescription());
					if(previousAuditMap.get(DiscontinueParameterType.PURCHASE_ORDERS) != null){
						auditRecord.setPreviouslyActive(previousAuditMap.get(DiscontinueParameterType.PURCHASE_ORDERS).isNewActive());
						auditRecord.setPreviouslyNeverDiscontinue(previousAuditMap.get(DiscontinueParameterType.PURCHASE_ORDERS).isNewNeverDiscontinue());
						auditRecord.setPreviousAttributeValue(previousAuditMap.get(DiscontinueParameterType.PURCHASE_ORDERS).getNewAttributeValue());
					}
				}
				previousAuditMap.put(DiscontinueParameterType.PURCHASE_ORDERS, auditRecord);
			}
			auditList.add(auditRecord);
		}
		return auditList;
	};

	/**
	 * Converts a list a DiscontinueExceptionParametersAudits to a list of DiscontinueParametersAuditRecords.
	 *
	 * @param auditEntityList a list a DiscontinueExceptionParametersAudits.
	 * @return a list of DiscontinueParametersAuditRecords.
	 */
	public List<DiscontinueParametersAuditRecord> convertExceptionParametersAuditToDiscontinueParametersAuditRecord(List<DiscontinueExceptionParametersAudit> auditEntityList){
		List<DiscontinueParametersAuditRecord> auditList = new ArrayList<>();
		Map<DiscontinueParameterType, DiscontinueParametersAuditRecord> previousAuditMap = new HashMap<>();
		previousAuditMap.put(DiscontinueParameterType.STORE_SALES, null);
		previousAuditMap.put(DiscontinueParameterType.NEW_ITEM_PERIOD, null);
		previousAuditMap.put(DiscontinueParameterType.WAREHOUSE_UNITS, null);
		previousAuditMap.put(DiscontinueParameterType.STORE_UNITS, null);
		previousAuditMap.put(DiscontinueParameterType.STORE_RECEIPTS, null);
		previousAuditMap.put(DiscontinueParameterType.PURCHASE_ORDERS, null);
		DiscontinueExceptionParametersAudit auditEntity = null;
		DiscontinueParametersAuditRecord auditRecord = null;
		for(int x=0; x<auditEntityList.size(); x++){
			auditEntity = auditEntityList.get(x);
			auditRecord = new DiscontinueParametersAuditRecord();
			auditRecord.setChangedBy(auditEntity.getUserId());
			auditRecord.setChangedOn(auditEntity.getTimestamp());
			auditRecord.setAction(auditEntity.getAction());
			auditRecord.setExceptionType(auditEntity.getExceptionType());
			auditRecord.setExceptionDescription(this.getExceptionNameByID(ProductDiscontinueExceptionType.fromString(auditEntity.getExceptionType().trim()), auditEntity.getExceptionTypeId().trim()));
			// If the record is delete, then there's no new Active switch, never discontinue switch, or ParameterValue.
			if(!auditRecord.getAction().trim().equalsIgnoreCase(DELETE_ACTION_CODE)){
				auditRecord.setNewActive(auditEntity.isActive());
				auditRecord.setNewNeverDiscontinue(auditEntity.isNeverDelete());
				auditRecord.setNewAttributeValue(auditEntity.getParameterValue());
			}

			// if the Type matches the case, set the previous values for the new audit record to what is in the map
			// for that case (Type is the key) and replace the value results for that key with the new record so that
			// it can be used as the previous value for the next record. If the map is null for the key, it adds the value
			// to be used for the next record.
			// Add- previous value should be blank (as it was deleted before or non-existent)
			// Mod- previous value should be stored in map, and changes shown
			// del- new value will be blank (since not set above)
			if(DiscontinueParameterType.STORE_SALES  == DiscontinueParameterType.getTypeById(auditEntity.getId())) {
				auditRecord.setAttributeName(DiscontinueParameterType.STORE_SALES.getDescription());
				if(previousAuditMap.get(DiscontinueParameterType.STORE_SALES) != null){
					auditRecord.setPreviouslyActive(previousAuditMap.get(DiscontinueParameterType.STORE_SALES).isNewActive());
					auditRecord.setPreviouslyNeverDiscontinue(previousAuditMap.get(DiscontinueParameterType.STORE_SALES).isNewNeverDiscontinue());
					auditRecord.setPreviousAttributeValue(previousAuditMap.get(DiscontinueParameterType.STORE_SALES).getNewAttributeValue());
				}
				previousAuditMap.put(DiscontinueParameterType.STORE_SALES, auditRecord);
			} else if(DiscontinueParameterType.NEW_ITEM_PERIOD  == DiscontinueParameterType.getTypeById(auditEntity.getId())){
				auditRecord.setAttributeName(DiscontinueParameterType.NEW_ITEM_PERIOD.getDescription());
				if(previousAuditMap.get(DiscontinueParameterType.NEW_ITEM_PERIOD) != null){
					auditRecord.setPreviouslyActive(previousAuditMap.get(DiscontinueParameterType.NEW_ITEM_PERIOD).isNewActive());
					auditRecord.setPreviouslyNeverDiscontinue(previousAuditMap.get(DiscontinueParameterType.NEW_ITEM_PERIOD).isNewNeverDiscontinue());
					auditRecord.setPreviousAttributeValue(previousAuditMap.get(DiscontinueParameterType.NEW_ITEM_PERIOD).getNewAttributeValue());
				}
				previousAuditMap.put(DiscontinueParameterType.NEW_ITEM_PERIOD, auditRecord);
			} else if(DiscontinueParameterType.WAREHOUSE_UNITS  == DiscontinueParameterType.getTypeById(auditEntity.getId())){
				auditRecord.setAttributeName(DiscontinueParameterType.WAREHOUSE_UNITS.getDescription());
				if(previousAuditMap.get(DiscontinueParameterType.WAREHOUSE_UNITS) != null){
					auditRecord.setPreviouslyActive(previousAuditMap.get(DiscontinueParameterType.WAREHOUSE_UNITS).isNewActive());
					auditRecord.setPreviouslyNeverDiscontinue(previousAuditMap.get(DiscontinueParameterType.WAREHOUSE_UNITS).isNewNeverDiscontinue());
					auditRecord.setPreviousAttributeValue(previousAuditMap.get(DiscontinueParameterType.WAREHOUSE_UNITS).getNewAttributeValue());
				}
				previousAuditMap.put(DiscontinueParameterType.WAREHOUSE_UNITS, auditRecord);
			} else if(DiscontinueParameterType.STORE_UNITS  == DiscontinueParameterType.getTypeById(auditEntity.getId())){
				auditRecord.setAttributeName(DiscontinueParameterType.STORE_UNITS.getDescription());
				if(previousAuditMap.get(DiscontinueParameterType.STORE_UNITS) != null){
					auditRecord.setPreviouslyActive(previousAuditMap.get(DiscontinueParameterType.STORE_UNITS).isNewActive());
					auditRecord.setPreviouslyNeverDiscontinue(previousAuditMap.get(DiscontinueParameterType.STORE_UNITS).isNewNeverDiscontinue());
					auditRecord.setPreviousAttributeValue(previousAuditMap.get(DiscontinueParameterType.STORE_UNITS).getNewAttributeValue());
				}
				previousAuditMap.put(DiscontinueParameterType.STORE_UNITS, auditRecord);
			} else if(DiscontinueParameterType.STORE_RECEIPTS  == DiscontinueParameterType.getTypeById(auditEntity.getId())) {
				auditRecord.setAttributeName(DiscontinueParameterType.STORE_RECEIPTS.getDescription());
				if (previousAuditMap.get(DiscontinueParameterType.STORE_RECEIPTS) != null) {
					auditRecord.setPreviouslyActive(previousAuditMap.get(DiscontinueParameterType.STORE_RECEIPTS).isNewActive());
					auditRecord.setPreviouslyNeverDiscontinue(previousAuditMap.get(DiscontinueParameterType.STORE_RECEIPTS).isNewNeverDiscontinue());
					auditRecord.setPreviousAttributeValue(previousAuditMap.get(DiscontinueParameterType.STORE_RECEIPTS).getNewAttributeValue());
				}
				previousAuditMap.put(DiscontinueParameterType.STORE_RECEIPTS, auditRecord);
			} else if(DiscontinueParameterType.PURCHASE_ORDERS  == DiscontinueParameterType.getTypeById(auditEntity.getId())){
				{
					if(auditEntity.getSequenceNumber() == PURCHASE_ORDERS_SEQUENCE_NUMBER) {
						auditRecord.setAttributeName(DiscontinueParameterType.PURCHASE_ORDERS.getDescription());
						if(previousAuditMap.get(DiscontinueParameterType.PURCHASE_ORDERS) != null){
							auditRecord.setPreviouslyActive(previousAuditMap.get(DiscontinueParameterType.PURCHASE_ORDERS).isNewActive());
							auditRecord.setPreviouslyNeverDiscontinue(previousAuditMap.get(DiscontinueParameterType.PURCHASE_ORDERS).isNewNeverDiscontinue());
							auditRecord.setPreviousAttributeValue(previousAuditMap.get(DiscontinueParameterType.PURCHASE_ORDERS).getNewAttributeValue());
						}
					}
					previousAuditMap.put(DiscontinueParameterType.PURCHASE_ORDERS, auditRecord);
				}
			}
			auditList.add(auditRecord);
		}
		return auditList;
	}

	/**
	 * Converts a List of DiscontinueExceptionParametersAudits to a list of DiscontinueRules that contains
	 * Exception Parameters that are currently deleted.
	 *
	 * @param auditEntityList a List of DiscontinueExceptionParametersAudits.
	 * @return  a list of DiscontinueParametersAuditRecords that contain Exception Parameters that were deleted.
	 */
	public List<DiscontinueRules> convertDeletedExceptionParametersAuditToDiscontinueRules(List<DiscontinueExceptionParametersAudit> auditEntityList){
		List<DiscontinueRules> discontinueRulesList = new ArrayList<>();
		if(CollectionUtils.isEmpty(auditEntityList)){
			return discontinueRulesList;
		}
		Map<String,Set<String>> ignoreMap = new HashMap<>();
		Map<String,Set<String>> deletesMap = new HashMap<>();
		String actionCode = null;
		String exceptionType = null;
		String exceptionId = null;
		for(int x=auditEntityList.size()-1; x>=0; x--){
			actionCode = auditEntityList.get(x).getAction().trim();
			exceptionType = auditEntityList.get(x).getExceptionType().trim();
			exceptionId = auditEntityList.get(x).getExceptionTypeId().trim();

			// If it's a delete and the map doesn't contain a Id value for the Type (including a null return for the key)
			// then the record has been deleted (and not re-added), so add it to the delete map (creating a new set for
			// the type if it doesn't exist.
			// If the value isn't a delete (MOD or ADD), and it isn't already in the delete map to be deleted, then add
			// it to the ignore map.
			if(actionCode.equalsIgnoreCase(DELETE_ACTION_CODE) && (ignoreMap.get(exceptionType) == null ||
					!ignoreMap.get(exceptionType).contains(exceptionId))){
				if(deletesMap.get(exceptionType)== null){
					deletesMap.put(exceptionType, new HashSet<>());
				}
				deletesMap.get(exceptionType).add(exceptionId);
			} else if(!actionCode.equalsIgnoreCase(DELETE_ACTION_CODE) && (deletesMap.get(exceptionType) == null ||
					!deletesMap.get(exceptionType).contains(exceptionId))) {
				if(ignoreMap.get(exceptionType) == null) {
					ignoreMap.put(exceptionType, new HashSet<>());
				}
				ignoreMap.get(exceptionType).add(exceptionId);
			}
		}
		if(CollectionUtils.isEmpty(deletesMap)){
			return discontinueRulesList;
		}
		DiscontinueRules discontinueRules = null;
		for(String type : deletesMap.keySet()){
			for(String id :deletesMap.get(type)){
				discontinueRules = new DiscontinueRules();
				discontinueRules.setExceptionType(type);
				discontinueRules.setExceptionTypeId(id);
				discontinueRules.setExceptionName(this.getExceptionNameByID(ProductDiscontinueExceptionType.fromString(type), id));
				discontinueRulesList.add(discontinueRules);
			}
		}
		return discontinueRulesList;
	};
}
