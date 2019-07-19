/*
 *  AuthorizationService
 *  Copyright (c) 2018 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.authorization;

import com.heb.pm.CoreEntityManager;
import com.heb.pm.authorization.util.AuthorizationConstants;
import com.heb.pm.authorization.util.AuthorizationUtils;
import com.heb.pm.entity.*;
import com.heb.pm.repository.ApLocationRepository;
import com.heb.pm.repository.RetailLocationRepository;
import com.heb.pm.repository.VendorItemStoreRepository;
import com.heb.pm.repository.WarehouseLocationItemRepository;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.criteria.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This service will implement any function on Authorization screen.
 *
 * @author vn70529
 * @since 2.23.0
 */
@Service
public class AuthorizationService {
    /**
     * Response key.
     */
	private static final String IS_UPC_SUPPLIED_KEY = "isUPCSupplied";
	private static final String AUTHORIZE_ITEM__KEY = "authorizeItems";
    private static final String FLEX_WEIGHT_SWITCH_KEY = "flexWeightSwitch";
	private static final String UPC_FIELD_NAME = "pdUpcNo";
    private static final String DEFAULT_STORE_KEY = "isDefaultStore";
    private static final String STORES_KEY = "stores";
    private static final String STORE_LOCATION_TYPE_CODE = "S";
    private static final String RETAIL_LOCATION_STATUS_CODE = "A";
    private static final Integer FINANCIAL_DIVISION_ID = 23;

	@Autowired
    private VendorItemStoreRepository vendorItemStoreRepository;
	@Autowired
    private ApLocationRepository apLocationRepository;
    @Autowired
    private WarehouseLocationItemRepository warehouseLocationItemRepository;
    @Autowired
    private RetailLocationRepository retailLocationRepository;
    @Autowired
    @CoreEntityManager
    private EntityManager entityManager;
    @Value("${authorizeItem.linOfBusId}")
    private Integer linOfBusId;
    /**
     * Find list of authorize items, supplied of upc and weight switch by upc and store id.
     * @param upc the upc.
     * @param storeId the store id.
     * @return a object contain the list of item masters and isUPCSupplied and flexWeightSwitch
     */
	public Map<String, Object> findItems(long upc, Long storeId){
		List<VendorLocationItem> vendorLocationItems = findVendorLocationItems(upc);
		List<AuthorizeItem> results = new ArrayList<>();
		boolean isUPCSupplied = false;
		Boolean flexWeightSwitch = false;
		// Holds the key of authorize item to avoid duplicate in authorizeItems
		Map<String, Boolean> authorizeItemKey = new HashMap<>();
		String key;
		for(VendorLocationItem vendorLocationItem : vendorLocationItems){
		    List<ProdItem> prodItems = vendorLocationItem.getItemMaster().getProdItems();
            for (ProdItem prodItem:prodItems ) {
                SellingUnit sellingUnit = prodItem.getProductMaster().getProductPrimarySellingUnit();
                if(sellingUnit != null && sellingUnit.getGoodsProduct() != null && sellingUnit.getProdId() == prodItem.getProductMaster().getProdId()) {
                    AuthorizeItem authorizeItem = new AuthorizeItem();
                    // Get the data from vendor location item, location, item master.
                    authorizeItem.setVendorId(vendorLocationItem.getLocation().getApVendorNumber());
                    authorizeItem.setVendorName(vendorLocationItem.getLocation().getLocationName());
                    authorizeItem.setItemId(String.valueOf(vendorLocationItem.getItemMaster().getKey().getItemCode()));
                    authorizeItem.setItemType(vendorLocationItem.getItemMaster().getKey().getItemType());
                    authorizeItem.setProductDescription(vendorLocationItem.getItemMaster().getDescription());
                    authorizeItem.setPackQuantity(vendorLocationItem.getPackQuantity());
                    authorizeItem.setItemSizeUom(vendorLocationItem.getItemMaster().getItemSizeUom());
                    authorizeItem.setListCost(vendorLocationItem.getListCost());
                    authorizeItem.setDiscontinueDate(vendorLocationItem.getItemMaster().getDiscontinueDate());
                    authorizeItem.setItemSizeQuantity(vendorLocationItem.getItemMaster().getItemSizeQuantity());
                    // Get the data from selling unit, goods product, product master.
                    authorizeItem.setScnTypCd(sellingUnit.getScanTypeCode());
                    authorizeItem.setQuantity(sellingUnit.getQuantity());
                    authorizeItem.setRetailUnitOfMeasureDes(sellingUnit.getRetailUnitOfMeasure().getDescription());
                    authorizeItem.setTagSize(sellingUnit.getTagSize());
                    authorizeItem.setFlexWeightSwitch(sellingUnit.getGoodsProduct().getFlexWeightSwitch());
                    authorizeItem.setDepartmentNo(sellingUnit.getProductMaster().getDepartmentCode());
                    authorizeItem.setSubDepartmentNo(sellingUnit.getProductMaster().getSubDepartmentCode());
                    authorizeItem.setPssDepartmentOne(sellingUnit.getProductMaster().getPssDepartmentOne());
                    authorizeItem.setProductPrimaryScanCodeId(sellingUnit.getProductMaster().getProductPrimaryScanCodeId());
                    authorizeItem.setProductId(sellingUnit.getProductMaster().getProdId());
                    // Avoid duplicate item by the key of authorize item.
                    key = authorizeItem.getKey();
                    if (authorizeItemKey.containsKey(key)) {
                        continue;
                    }
                    // Set item status
                    setItemStatus(vendorLocationItem.getItemMaster(), authorizeItem);
                    // check UPC was supplied by store id, ap vendor number and item code.
                    isUPCSupplied = isUPCSupplied(vendorLocationItem, storeId);
                    if (isUPCSupplied) {
                        break;
                    }
                    results.add(authorizeItem);
                    authorizeItemKey.put(key, true);
                    if (!flexWeightSwitch) {
                        flexWeightSwitch = authorizeItem.getFlexWeightSwitch();
                    }
                }
            }
		}
		Map<String, Object> result = new HashMap();
		result.put(IS_UPC_SUPPLIED_KEY, isUPCSupplied);
		result.put(AUTHORIZE_ITEM__KEY, results);
        result.put(FLEX_WEIGHT_SWITCH_KEY, flexWeightSwitch);
		return result;
	}
    /**
     *  Returns the list of stores and is default store status.
     * @param userStore user store id.
     * @return a Map with isDefaultStore key and store key.
     */
    public Map<String, Object> getStores(String userStore){
        Map<String, Object> result = new HashMap<>();
        boolean isDefaultStore = true;
        List<RetailLocation> retailLocations = new ArrayList<>();
        List<String> stores = new ArrayList<>();
        if(StringUtils.isNotBlank(userStore)){
            // Get default store by user.
            RetailLocation retailLocation = retailLocationRepository.findRetailLocationByUserStoreId(STORE_LOCATION_TYPE_CODE, FINANCIAL_DIVISION_ID, RETAIL_LOCATION_STATUS_CODE, Integer.valueOf(userStore.trim()));
            if(retailLocation != null) {
                retailLocations.add(retailLocation);
            }
        }
        if(retailLocations == null || retailLocations.isEmpty()){
            // If there is no any default store then get the list of store by lin of bus id.
            isDefaultStore = false;
            retailLocations = retailLocationRepository.findRetailLocation(STORE_LOCATION_TYPE_CODE, FINANCIAL_DIVISION_ID, RETAIL_LOCATION_STATUS_CODE);
        }
        if(retailLocations != null){
            for (RetailLocation retailLocation : retailLocations) {
                stores.add(AuthorizationUtils.formatDecimal(retailLocation.getKey().getLocationNumber(), AuthorizationConstants.DECIMAL_00000_FORMAT));
            }
        }
        result.put(DEFAULT_STORE_KEY, isDefaultStore);
        result.put(STORES_KEY, stores);
        return result;
    }

    /**
     * Set item status. if item type is DSD and if discontinueDate > current or DiscontinueDate == null or 1600-01-01 -> status is true.
     * otherwise it is false.
     * @param itemMaster item master object.
     * @param authorizeItem authorizeItem object.
     */
    private void setItemStatus(ItemMaster itemMaster, AuthorizeItem authorizeItem){
        if((itemMaster.getKey().getItemType() != null) &&
                itemMaster.getKey().getItemType().trim().equalsIgnoreCase(AuthorizationConstants.ITEM_TYPE_DSD)) {
            if(itemMaster.getDiscontinueDate()!=null) {
                String discontinueDate = itemMaster.getDiscontinueDate().format(DateTimeFormatter.ofPattern(AuthorizationConstants.DATE_FORMAT_YYYYMMDD));
                if ((itemMaster.getDiscontinueDate().isAfter(LocalDate.now())) ||
                        (discontinueDate.equalsIgnoreCase(AuthorizationConstants.DEFAULT_DATE))) {
                    authorizeItem.setStatus(true);
                }
            }else{
                authorizeItem.setStatus(true);
            }
        } else {
            List<WarehouseLocationItem> warehouseLocationItems = warehouseLocationItemRepository.findByKeyItemCodeAndKeyItemType(itemMaster.getKey().getItemCode(), ItemMaster.ITEM_TYPE_CODE);
            for (WarehouseLocationItem warehouseLocationItem: warehouseLocationItems) {
                if(warehouseLocationItem.getSplrItemStatusCode() != null && WarehouseLocationItem.ACTIVE.equalsIgnoreCase(warehouseLocationItem.getSplrItemStatusCode())){
                    authorizeItem.setStatus(true);
                    break;
                }
            }
        }
    }

    /**
     * Check UPC was supplied by store id, ap vendor number and item code from apLocation and vendor item store.
     * @param vendorLocationItem the vendorLocationItem object.
     * @param storeId the id of store.
     * @return true if AuthnSw and DsdSbbSw of vendor item store is true or false.
     */
    private boolean isUPCSupplied(VendorLocationItem vendorLocationItem, long storeId){
        boolean isUPCSupplied = false;
        // Get ap location by ApVendorNumber
        ApLocation apLocation = apLocationRepository.findOneByVendorNumber(vendorLocationItem.getLocation().getApVendorNumber());
        if (apLocation != null) {
            // Get VendorItemStore by ApVendorNumber and store id and item code.
            VendorItemStoreKey key = new VendorItemStoreKey();
            key.setItmId(vendorLocationItem.getItemMaster().getKey().getItemCode());
            key.setVendLocNbr(vendorLocationItem.getLocation().getApVendorNumber());
            key.setLocNbr(storeId);
            key.setItmKeyTypCd(VendorItemStore.ITEM_KEY_TYPE_DSD_CODE);
            key.setLocTypCd(VendorItemStore.LOCATION_TYPE_S_CODE);
            key.setVendLocTypCd(VendorItemStore.VENDOR_LOCATION_TYPE_D_CODE);
            VendorItemStore vendorItemStore = vendorItemStoreRepository.findOne(key);
            if (vendorItemStore != null && apLocation.getDsdSbbSw() && vendorItemStore.getAuthnSw()) {
                isUPCSupplied = true;
            }
        }
        return isUPCSupplied;
    }
	/**
	* Find the vendor location item by upc.
	* @param upc the upc.
	*/
	 private List<VendorLocationItem> findVendorLocationItems(Long upc) {
        // init criteria builder.
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        // Create vendor location item query builder.
        CriteriaQuery<VendorLocationItem> vendorLocationItemCQ = criteriaBuilder.createQuery(VendorLocationItem.class);
        // Create vendor location item root.
        Root<VendorLocationItem> vendorLocationItemRoot = vendorLocationItemCQ.from(VendorLocationItem.class);
        // Join vendor location item to location.
        Join<VendorLocationItem, Location> locationJoin = vendorLocationItemRoot.join(VendorLocationItem_.location);
        // Join Vendor location item ot item master.
        Join<VendorLocationItem, ItemMaster> itemMasterJoin = vendorLocationItemRoot.join(VendorLocationItem_.itemMaster);
        // Join item master to product item.
        Join<ItemMaster, ProdItem> prodItemJoin =itemMasterJoin.join(ItemMaster_.prodItems);
        // Join product item to product master.
        Join<ProdItem, ProductMaster> productMasterJoin = prodItemJoin.join(ProdItem_.productMaster);
        // Join Product master to selling unit.
        Join<ProductMaster, SellingUnit> sellingUnitJoin = productMasterJoin.join(ProductMaster_.sellingUnits);
        // Join Selling unit to goods product
        sellingUnitJoin.join(SellingUnit_.goodsProduct);
        // Create associated upc sub query.
        Subquery<AssociatedUpc> associatedUpcExists = vendorLocationItemCQ.subquery(AssociatedUpc.class);
        Root<AssociatedUpc> associatedUpcRoot = associatedUpcExists.from(AssociatedUpc.class);
        associatedUpcExists.select(associatedUpcRoot.get(UPC_FIELD_NAME));
        // Create condition for sub query.
        Predicate[] predicates = new Predicate[1];
        predicates[0] = criteriaBuilder.equal(associatedUpcRoot.get(AssociatedUpc_.upc), upc);
        associatedUpcExists.where(predicates);
        // Create condition for  Vendor location item.
        predicates = new Predicate[3];
        predicates[0] = criteriaBuilder.equal(itemMasterJoin.get(ItemMaster_.orderingUpc), associatedUpcExists);
        predicates[1] = criteriaBuilder.equal(productMasterJoin.get(ProductMaster_.productPrimaryScanCodeId), sellingUnitJoin.get(SellingUnit_.upc));
        predicates[2] = criteriaBuilder.equal(locationJoin.get(Location_.inactiveSW), VendorLocationItem.ACTIVE_STATUS);
        vendorLocationItemCQ.where(criteriaBuilder.and(predicates));
        return this.entityManager.createQuery(vendorLocationItemCQ).getResultList();
    }
}

