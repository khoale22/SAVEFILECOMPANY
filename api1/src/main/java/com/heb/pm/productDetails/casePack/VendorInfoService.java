/*
 * VendorInfoService
 *
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
package com.heb.pm.productDetails.casePack;

import com.heb.pm.entity.*;
import com.heb.pm.repository.CandidateItemMasterRepository;
import com.heb.pm.repository.CandidateWorkRequestRepository;
import com.heb.pm.repository.VendorLocationItemRepository;
import com.heb.pm.vendor.VendorServiceClient;
import com.heb.pm.ws.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service for vendor information controller.
 *
 * @author l730832
 * @since 2.6.0
 */
@Service
public class VendorInfoService {

	private static final Logger logger = LoggerFactory.getLogger(VendorInfoService.class);
	private static final String RETAIL_LOOKUP_ERROR= "Unable to lookup retail for item %s";
	private static final String COST_LOOKUP_ERROR= "Unable to lookup cost item %s";

	private static final int INTNT_ADD_TO_WAREHOUSE = 11;
	private static final String PROD_SETUP_STATUS_WORKING = "107";
	private static final String NEW_DATA_SW_Y = "Y";

	@Value("${app.defaultRetailZone}")
	private int defaultRetailZone;

	@Autowired
	private VendorLocationItemRepository vendorLocationItemRepository;

	@Autowired
	private PriceServiceClient priceServiceClient;

	@Autowired
	private CostServiceClient costServiceClient;

	@Autowired
	private ProductManagementServiceClient productManagementServiceClient;

	@Autowired
	private CandidateWorkRequestRepository candidateWorkRequestRepository;

	@Autowired
	private CandidateItemMasterRepository candidateItemMasterRepository;

	@Autowired
	private VendorServiceClient vendorServiceClient;



	/**
	 * Returns a list of vendor location items.
	 *
	 * @param itemId the Item id being searched on.
	 * @return the list of vendor location items
	 */
	public List<VendorLocationItem> findByKeyItemCodeAndKeyItemType(long itemId, String itemType) {
		List<VendorLocationItem> vendorLocationItemList =
				this.vendorLocationItemRepository.findByKeyItemCodeAndKeyItemType(itemId, itemType);

		if (vendorLocationItemList.isEmpty()) {
			return vendorLocationItemList;
		}

		// Fetch the retail for the item. Since all the vendor location items have the same item, we can
		// just get the first.
		Retail retail = this.getRetail(vendorLocationItemList.get(0).getItemMaster());

		for (VendorLocationItem vendorLocationItem : vendorLocationItemList) {
			vendorLocationItem.getItemMaster().setRetail(retail);
			vendorLocationItem.setCost(this.getCostDetailsForVendorLocationItem(vendorLocationItem));
		}
		return vendorLocationItemList;
	}

	/**
	 * Updates the list vendor location item tie to item code.
	 * @param vendorLocationItems the vendor location item being updated.
	 */
	public void updateVendorInfo(List<VendorLocationItem> vendorLocationItems) {
		this.productManagementServiceClient.updateVendorLocationItem(vendorLocationItems);
	}

	/**
	 * Looks up the retail for the primary UPC associated to an item.
	 *
	 * @param itemMaster The Item to lookup retail for.
	 * @return The retail for the primary UPC associated to the item. Will return null if there is an error.
	 */
	private Retail getRetail(ItemMaster itemMaster) {

		try {
			Retail r = this.priceServiceClient.getRegularRetail(this.defaultRetailZone, itemMaster.getOrderingUpc());
			logger.info(r.toString());
			return r;
		} catch (CheckedSoapException e) {
			VendorInfoService.logger.error(String.format(VendorInfoService.RETAIL_LOOKUP_ERROR,
					itemMaster.getKey()));
		}

		return null;
	}

	/**
	 * Receives the cost details for the specified vendor location item.
	 *
	 * @param vendorLocationItem the vendor location item to getting the details for.
	 */
	private Cost getCostDetailsForVendorLocationItem(VendorLocationItem vendorLocationItem) {

		// TODO: This fetches cost by a UPC. Once the operation for cost service to take an item code is done, this
		// needs to be modified.

		try {
			return this.costServiceClient.getCostDetail(
					vendorLocationItem.getItemMaster().getProdItems().get(0).getProductMaster().getProductPrimaryScanCodeId(),
					vendorLocationItem.getKey().getVendorNumber(),
					vendorLocationItem.getKey().getItemCode());

		} catch (CheckedSoapException e) {
			VendorInfoService.logger.error(String.format(VendorInfoService.COST_LOOKUP_ERROR,
					vendorLocationItem.getKey()));
		}

		return null;
	}

	/**
	 * Check candidate associated with product. It's mean check product already exists an candidate. If exist, we
	 * will search this candidate and view, not show list beciep vendor to user add, and create an new candidate
	 * associated to product id.
	 *
	 * @param productId - The product id.
	 * @return ps work id of the candidate associated with product.
	 */
	public Long findAddToWarehouseCandidate(Long productId) {
		Long psWorkRequest = 0L;
		List<CandidateWorkRequest> candidateWorkRequests = this.candidateWorkRequestRepository.findByIntentAndStatusAndProductIdOrderByCreateDateDesc(INTNT_ADD_TO_WAREHOUSE,
				PROD_SETUP_STATUS_WORKING, productId);
		if (null != candidateWorkRequests && !candidateWorkRequests.isEmpty()) {
			psWorkRequest = candidateWorkRequests.get(0).getWorkRequestId();
		}
		return psWorkRequest;
	}

	/**
	 * Get candidate information associated with product.
	 *
	 * @param psWorkId - The ps work id.
	 * @return The list of PsItemMaster.
	 */
	public List<CandidateItemMaster> getCandidateInformation(Long psWorkId){
		List<CandidateItemMaster> returnList = null;
		CandidateItemMaster psItemMaster = this.candidateItemMasterRepository.findByCandidateWorkRequestIdAndNewData(psWorkId, true);
		if(null != psItemMaster){
			Integer psItmId = psItemMaster.getCandidateItemId();
			if(psItmId > 0){
				returnList = this.candidateItemMasterRepository.findByCandidateItemId(psItmId);
			}
		}else{
			CandidateItemMaster psItemMasterWHS = this.candidateItemMasterRepository.findFirstByCandidateWorkRequestIdAndCandidateVendorLocationItemsNewDataAndCandidateWarehouseLocationItemsNewData(psWorkId, true, true);
			if(null != psItemMasterWHS){
				Integer psItmIdWHS = psItemMasterWHS.getCandidateItemId();
				if(psItmIdWHS > 0){
					returnList = this.candidateItemMasterRepository.findByCandidateItemId(psItmIdWHS);
				}
			}
		}
		return returnList;
	}

	/**
	 * Find list of bicep vendor (A bicep vendor contain information vendor location number, bicep, ap location
	 * number, warehouse number, facility) to user add, and create an new candidate associated to product.
	 *
	 * @param apVendor The app location vendor
	 * @param classCode  - The class code
	 * @return The list of bicep vendor.
	 */
	public List<BicepVendor> findBicepVendorsByApVendorAndClass(Integer apVendor, Integer classCode) throws CheckedSoapException{
		return this.vendorServiceClient.getBicepsListByApVendorAndClass(apVendor, classCode);
	}
}
