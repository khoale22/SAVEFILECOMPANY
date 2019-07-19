/*
 *  UpcSwapConverter
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.upcMaintenance;

import com.heb.pm.entity.*;
import com.heb.pm.inventory.TimException;
import com.heb.pm.repository.AssociatedUpcRepository;
import com.heb.pm.repository.ItemMasterRepository;
import com.heb.pm.repository.ShipperRepository;
import com.heb.pm.repository.TimRepository;
import com.heb.pm.ws.CheckedSoapException;
import com.heb.pm.ws.PlanogramServiceClient;
import com.heb.util.upc.UpcUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * This class pulls out the boiler plate logic in DSD to Both, Both to DSD, and Warehouse to Warehouse swap services.
 *
 * @author s573181
 * @since 2.0.5
 */
@Component
class UpcSwapUtils {

	private static final Logger logger = LoggerFactory.getLogger(UpcUtils.class);

	@Autowired
	private AssociatedUpcRepository associatedUpcRepository;

	@Autowired
	private ItemMasterRepository itemMasterRepository;

	@Autowired
	private TimRepository timRepository;

	@Autowired
	private PlanogramServiceClient planogramServiceClient;

	@Autowired
	private ShipperRepository shipperRepository;

	// Initial count for first source upc found or first destination item code make primary found
	private static final int INITIAL_COUNT = 1;

	// Used to get first of prod items (only should be one, since MRTs aren't allowed in swaps.
	private static final int ONLY_RECORD = 0;

	// Used for values of a count that should not be reached.
	private static final int BAD_COUNT = 2;

	// Constant names for the type of service requests.
	public static final String WHS_TO_WHS = "warehouseToWarehouse";
	public static final String DSD_TO_BOTH = "dsdToBoth";
	public static final String BOTH_TO_DSD = "bothToDsd";

	// error messages
	private static final String ERROR_WRONG_ARGUMENT_SIZE= "There should be as many UPCs as item codes.";
	private static final String ERROR_NULL_UPC = "UPC cannot be null.";
	private static final String ERROR_NULL_ITEM = "Item code cannot be null.";
	private static final String ERROR_ITEM_CODE_NOT_FOUND_FOR_REQUESTED_TYPE = "Warehouse type item code not found" +
			" for UPC: %d.";
	private static final String ERROR_ITEM_CODE_NOT_FOUND_FOR_REQUESTED_TYPE_ITEM_CODE = "Warehouse type item code" +
			" not found for Item Code: %d.";
	private static final String ERROR_ITEM_CODE_NOT_FOUND= "Could not find Item Code: %d.";
	private static final String ERROR_UPC_NOT_FOUND= "Could not find UPC: %d.";
	private static final String ERROR_ALREADY_BOTH_DSD= "UPC: %d is already a both.";
	private static final String ERROR_NOT_BOTH_DSD= "Could not find a both for UPC: %d.";
	private static final String ERROR_NOT_DSD= "UPC: %d is not a DSD.";
	private static final String ERROR_SOURCE_IS_PRIMARY_BOTH= "Warehouse primary UPC: %d cannot be removed at " +
			"this time.";
	private static final String ERROR_ONE_PRIMARY_ONLY= "Item code: %d cannot have more than one primary UPC.";
	private static final String ERROR_ONE_SOURCE_UPC_ONLY= "UPC: %d cannot be the source UPC more than once.";
	private static final String ERROR_UPC_ALREADY_ITEM_PRIMARY_UPC= "UPC: %d is already primary UPC of item code: %d.";
	private static final String ERROR_CHANGE_PRIMARY_NOT_ALLOWED = "Please enter the associate UPC to become the" +
			" primary.";
	private static final String ERROR_MORE_THAN_ONE_ITEM = "Please only create one swap per item code: %d.";

	// date formatter
	private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

	/**Constant code for the type of alternate pack.*/
	public static final char ALTERNATE_PACK_CODE = 'A';

	/**
	 * Returns a list of all UPC swap information from a list of UPCs and matching item codes.
	 *
	 * @param upcList      the upc list
	 * @param itemCodeList the item code list
	 * @param type         the type
	 * @return A list of all UPC swaps.
	 */
	@SuppressWarnings("unchecked")
	public List<UpcSwap> findAll(List<Long> upcList, List<Long> itemCodeList, String type) {
		AssociatedUpc associatedUpc;
		ItemMaster itemMaster = null;
		ItemMasterKey itemMasterKey;
		List<UpcSwap> returnList = new ArrayList<>();
		Map<Long, Integer> sameItemMap = new HashMap<>();
		for(int index = 0; index < upcList.size(); index++) {
			if(!type.equals(BOTH_TO_DSD)) {
				if(upcList.size() != itemCodeList.size()){
					throw new IllegalArgumentException(ERROR_WRONG_ARGUMENT_SIZE);
				}
				if (itemCodeList.get(index) == null) {
					throw new IllegalArgumentException(ERROR_NULL_ITEM);
				}
				itemMasterKey = new ItemMasterKey();
				itemMasterKey.setItemCode(itemCodeList.get(index));
				itemMasterKey.setItemType(ItemMasterKey.WAREHOUSE);
				itemMaster = this.itemMasterRepository.findOne(itemMasterKey);
			}
			if(upcList.get(index) == null){
				throw new IllegalArgumentException(ERROR_NULL_UPC);
			}
			associatedUpc = this.findAssociatedUpcByUpc(upcList.get(index));
			switch(type){
				case WHS_TO_WHS:
					returnList.add(this.whsToWhsToUpcSwap(associatedUpc, itemMaster, upcList.get(index),
							itemCodeList.get(index)));
					this.updateSameItemMap(returnList.get(returnList.size() -1), sameItemMap);
					break;
				case DSD_TO_BOTH:
					returnList.add(this.dsdToBothToUpcSwap(associatedUpc, itemMaster, upcList.get(index),
							itemCodeList.get(index)));
					break;
				case BOTH_TO_DSD:
					returnList.add(this.bothToDsdUpcSwap(associatedUpc, upcList.get(index)));
					break;
			}
		}
		if(sameItemMap.containsValue(BAD_COUNT)){
			List<Long> badList = new ArrayList<>();
			for (Object o : sameItemMap.entrySet()) {
				Map.Entry<Long, Integer> pair = (Map.Entry<Long, Integer>) o;
				if (pair.getValue() == BAD_COUNT) {
					badList.add(pair.getKey());
				}
			}
			for(UpcSwap swap : returnList){
				if(badList.contains(swap.getSource().getItemCode())){
					swap.getSource().setErrorMessage(
							String.format(ERROR_MORE_THAN_ONE_ITEM, swap.getSource().getItemCode()));
					swap.setErrorFound(true);
				} else if(badList.contains(swap.getDestination().getItemCode())){
					swap.getDestination().setErrorMessage(
							String.format(ERROR_MORE_THAN_ONE_ITEM, swap.getDestination().getItemCode()));
					swap.setErrorFound(true);
				}
			}
		}

		for(UpcSwap swap : returnList){
			if(!swap.isErrorFound() && swap.isSourcePrimaryUpc()){
				swap.setPrimarySelectOption(UpcSwap.PrimarySelectOption.JUST_PRIMARY);
			}
		}
		return returnList;
	}

	/**
	 * Updates the same item code map to either set the value for the key item code as:
	 * 'BAD_COUNT'(2) or
	 * 'INITIAL_COUNT'(1).
	 *
	 * @param upcSwap current upc swap
	 * @param sameItemMap same item code map
	 */
	private void updateSameItemMap(UpcSwap upcSwap, Map<Long, Integer> sameItemMap) {
		// if sameItemMap contains both source and destination item code
		if(sameItemMap.containsKey(upcSwap.getSource().getItemCode()) &&
				sameItemMap.containsKey(upcSwap.getDestination().getItemCode())){
			sameItemMap.put(upcSwap.getSource().getItemCode(), BAD_COUNT);
			sameItemMap.put(upcSwap.getDestination().getItemCode(), BAD_COUNT);
		}
		// else if sameItemMap contains source item code
		else if(sameItemMap.containsKey(upcSwap.getSource().getItemCode())) {
			sameItemMap.put(upcSwap.getSource().getItemCode(), BAD_COUNT);
			sameItemMap.put(upcSwap.getDestination().getItemCode(), INITIAL_COUNT);
		}
		// else if sameItemMap contains destination item code
		else if(sameItemMap.containsKey(upcSwap.getDestination().getItemCode())) {
			sameItemMap.put(upcSwap.getSource().getItemCode(), INITIAL_COUNT);
			sameItemMap.put(upcSwap.getDestination().getItemCode(), BAD_COUNT);
		}
		// else both item codes do not exist in map yet
		else {
			// both item codes do not exist in map yet
			sameItemMap.put(upcSwap.getSource().getItemCode(), INITIAL_COUNT);
			sameItemMap.put(upcSwap.getDestination().getItemCode(), INITIAL_COUNT);
		}
	}


	/**
	 * This is a method that finds an associated upc by UPC. If the search fails, returns null.
	 *
	 * @param upc UPC to search for.
	 * @return Associated upc that matches upc.
	 */
	public AssociatedUpc findAssociatedUpcByUpc(Long upc) {
		return this.associatedUpcRepository.findOne(upc);
	}


	/**
	 * Return a UPC swap converted from an associated upc and item master WHS to WHS.
	 *
	 * @param associatedUpc Associate Upc found from repository.
	 * @param itemMaster Item Master found from repository.
	 * @return A UPC swap converted from particular associated upc and item master.
	 */
	private UpcSwap whsToWhsToUpcSwap(AssociatedUpc associatedUpc, ItemMaster itemMaster, Long sourceUpc,
									  Long destinationItemCode) {

		UpcSwap upcSwap = new UpcSwap();
		UpcSwap.SwappableEndPoint source =  upcSwap.new SwappableEndPoint();
		if(associatedUpc != null && associatedUpc.getSellingUnit() != null) {
			ItemMaster tempItemMaster = associatedUpc.getPrimaryUpc().getWarehouseItem();
			if (tempItemMaster != null) {
				if(itemMaster != null && itemMaster.getKey().getItemCode()
						== tempItemMaster.getKey().getItemCode() && associatedUpc.getSellingUnit().isPrimaryUpc()){
					source.setErrorMessage(ERROR_CHANGE_PRIMARY_NOT_ALLOWED);
					upcSwap.setErrorFound(true);
				} else {
					source.setProductId(associatedUpc.getSellingUnit().getProdId());
					source.setItemCode(tempItemMaster.getKey().getItemCode());
					source.setItemDescription(tempItemMaster.getDescription());
					source.setItemSize(associatedUpc.getSellingUnit().getTagSize());
					source.setBalanceOnHand(tempItemMaster.getTotalOnHandInventory());
					source.setBalanceOnOrder(tempItemMaster.getTotalOnOrderInventory());
					source.setAssociatedUpcList(this.getAssociatedUpcList(
							associatedUpc.getPrimaryUpc().getAssociateUpcs(),
							associatedUpc.getUpc(), associatedUpc.getSellingUnit().isPrimaryUpc()));
					source.setProductRetailLink(associatedUpc.getSellingUnit().getProductMaster().getRetailLink());
					try {
						source.setPurchaseOrders(this.timRepository.getPurchaseOrders(
								tempItemMaster.getKey().getItemCode()));
					} catch (TimException e){
						source.setPurchaseOrders(null);
					}
					try{
						source.setOnLiveOrPendingPog(this.planogramServiceClient.
								isOnPlanogramByUpc(tempItemMaster.getPrimaryUpc().getUpc()));
					} catch (CheckedSoapException e) {
						source.setOnLiveOrPendingPog(null);
					}
					upcSwap.setSourcePrimaryUpc(associatedUpc.getSellingUnit().isPrimaryUpc());
					if (!associatedUpc.getSellingUnit().isPrimaryUpc()) {
						upcSwap.setSelectSourcePrimaryUpc(associatedUpc.getPrimaryUpc().getUpc());
						if(itemMaster != null && itemMaster.getKey().getItemCode()
								== tempItemMaster.getKey().getItemCode()){
							upcSwap.setDestinationPrimaryUpcSelected(sourceUpc);
						}
					}
				}
			} else {
				source.setErrorMessage(String.format(ERROR_ITEM_CODE_NOT_FOUND_FOR_REQUESTED_TYPE,
						associatedUpc.getUpc()));
				upcSwap.setErrorFound(true);
			}
		} else {
			source.setErrorMessage(String.format(ERROR_UPC_NOT_FOUND, sourceUpc));
			upcSwap.setErrorFound(true);
		}
		source.setItemType(ItemMasterKey.WAREHOUSE);
		upcSwap.setSource(source);
		upcSwap.setSourceUpc(sourceUpc);
		this.setUpcSwapDestination(upcSwap, itemMaster, destinationItemCode);
		return upcSwap;
	}

	/**
	 *
	 * Return a UPC swap converted from an associated UPC and an item master DSD to Both.
	 *
	 * @param associatedUpc Associate Upc found from repository.
	 * @param itemMaster Item Master found from repository.
	 * @return A upc swap converted from particular associated upc and item master.
	 */
	private UpcSwap dsdToBothToUpcSwap(AssociatedUpc associatedUpc, ItemMaster itemMaster, Long sourceUpc,
									   Long destinationItemCode) {

		UpcSwap upcSwap = new UpcSwap();
		UpcSwap.SwappableEndPoint source =  upcSwap.new SwappableEndPoint();
		if(associatedUpc != null && associatedUpc.getSellingUnit() != null) {
			ItemMaster dsdItem = this.getDsdItem(associatedUpc);

			// If there's a WHS and DSD for the UPC then the UPC is already both
			if(dsdItem != null) {
				source.setProductId(associatedUpc.getSellingUnit().getProdId());
				source.setItemCode(dsdItem.getKey().getItemCode());
				source.setItemDescription(dsdItem.getDescription());
				source.setItemSize(associatedUpc.getSellingUnit().getTagSize());
				source.setProductRetailLink(associatedUpc.getSellingUnit().getProductMaster().getRetailLink());
				if (associatedUpc.getPrimaryUpc().getWarehouseItem()!= null) {
					source.setErrorMessage(String.format(ERROR_ALREADY_BOTH_DSD, sourceUpc));
					upcSwap.setErrorFound(true);
				} else {
					upcSwap.setSourcePrimaryUpc(associatedUpc.getSellingUnit().isPrimaryUpc());
					try {
						source.setOnLiveOrPendingPog(this.planogramServiceClient.
								isOnPlanogramByUpc(dsdItem.getPrimaryUpc().getUpc()));
					} catch (CheckedSoapException e) {
						source.setOnLiveOrPendingPog(null);
					}
				}
			} else {
				source.setErrorMessage(String.format(ERROR_NOT_DSD, associatedUpc.getUpc()));
				upcSwap.setErrorFound(true);
			}
		} else {
			source.setErrorMessage(String.format(ERROR_UPC_NOT_FOUND, sourceUpc));
			upcSwap.setErrorFound(true);
		}
		upcSwap.setSource(source);
		upcSwap.setSourceUpc(sourceUpc);
		this.setUpcSwapDestination(upcSwap, itemMaster, destinationItemCode);
		return upcSwap;
	}

	/**
	 *
	 * Return a UPC swap converted from an associated UPC and item master both to DSD.
	 *
	 * @param associatedUpc Associate UPC found from repository.
	 * @param sourceUpc The UPC of the source.
	 * @return A UPC swap converted from particular associated upc and item master.
	 */
	private UpcSwap bothToDsdUpcSwap(AssociatedUpc associatedUpc, Long sourceUpc) {

		UpcSwap upcSwap = new UpcSwap();
		UpcSwap.SwappableEndPoint source =  upcSwap.new SwappableEndPoint();

		if(associatedUpc != null && associatedUpc.getSellingUnit() != null) {
			ItemMaster dsdItem = this.getDsdItem(associatedUpc);
			ItemMaster whsItem = associatedUpc.getPrimaryUpc().getWarehouseItem();

			// If there's not a WHS and DSD for the UPC then the UPC isn't a both
			if(whsItem == null || dsdItem == null){
				source.setErrorMessage(String.format(ERROR_NOT_BOTH_DSD, sourceUpc));
				upcSwap.setErrorFound(true);
			} else {
				source.setProductId(associatedUpc.getSellingUnit().getProdId());
				source.setItemCode(whsItem.getKey().getItemCode());
				source.setItemDescription(whsItem.getDescription());
				source.setItemSize(associatedUpc.getSellingUnit().getTagSize());
				source.setBalanceOnHand(whsItem.getTotalOnHandInventory());
				source.setBalanceOnOrder(whsItem.getTotalOnOrderInventory());
				source.setProductRetailLink(associatedUpc.getSellingUnit().getProductMaster().getRetailLink());
				try {
					source.setPurchaseOrders(this.timRepository.getPurchaseOrders(whsItem.getKey().getItemCode()));
				} catch (TimException e){
					source.setPurchaseOrders(null);
				}
				try{
					source.setOnLiveOrPendingPog(this.planogramServiceClient.
							isOnPlanogramByUpc(whsItem.getPrimaryUpc().getUpc()));
				} catch (CheckedSoapException e) {
					source.setOnLiveOrPendingPog(null);
				}
				upcSwap.setSourcePrimaryUpc(associatedUpc.getSellingUnit().isPrimaryUpc());
				if(!associatedUpc.getSellingUnit().isPrimaryUpc()){
					upcSwap.setSelectSourcePrimaryUpc(associatedUpc.getPrimaryUpc().getUpc());
				}
				source.setAssociatedUpcList(this.getAssociatedUpcList(associatedUpc.getPrimaryUpc().getAssociateUpcs(),
						associatedUpc.getUpc(), associatedUpc.getSellingUnit().isPrimaryUpc()));
				source.setProductRetailLink(associatedUpc.getSellingUnit().getProductMaster().getRetailLink());
			}
		} else {
			source.setErrorMessage(String.format(ERROR_UPC_NOT_FOUND, sourceUpc));
			upcSwap.setErrorFound(true);
		}
		source.setItemType(ItemMasterKey.WAREHOUSE);
		upcSwap.setSource(source);
		upcSwap.setSourceUpc(sourceUpc);
		return upcSwap;
	}

	/**
	 * This method retrieves the dsd item by one of two methods:
	 * 1. from an AssociatedUpc's PrimaryUpc's getDsdItem method (most cases)
	 * 2. from an AssociatedUpc's SellingUnit's ProductMaster's ProdItem's ItemMaster (rare case)
	 *
	 * @param associatedUpc AssociatedUpc in question
	 * @return The ItemMaster found, or null;
	 */
	private ItemMaster getDsdItem(AssociatedUpc associatedUpc) {

		//if the associated upc's primary upc is linked to a dsd item
		ItemMaster itemMaster = associatedUpc.getPrimaryUpc().getDsdItem();
		if(itemMaster != null){
			return itemMaster;
		}

		//else search through the upc's product's prodItems to find the dsd item matching the given upc
		if(associatedUpc.getSellingUnit() != null){
			List<ProdItem> prodItems = associatedUpc.getSellingUnit().getProductMaster().getProdItems();
			for(ProdItem prodItem : prodItems){
				if(prodItem.getKey().getItemType().equals(ItemMasterKey.DSD) &&
						associatedUpc.getUpc() == prodItem.getKey().getItemCode()){
					itemMaster = prodItem.getItemMaster();
					break;
				}
			}
		}
		return itemMaster;
	}

	/**
	 * Sets the UPC swap destination information.
	 *
	 * @param upcSwap The upc swap to set the destination.
	 * @param itemMaster The ItemMaster found in the repository.
	 * @param destinationItemCode The destinations item code.
	 */
	private void setUpcSwapDestination(UpcSwap upcSwap, ItemMaster itemMaster, long destinationItemCode){
		UpcSwap.SwappableEndPoint destination = upcSwap.new SwappableEndPoint();
		if(itemMaster != null) {
			// Get the product ID from the  first of prod items (there only should be one, since MRTs aren't
			// allowed in swaps).
			destination.setProductId(itemMaster.getProdItems().get(ONLY_RECORD).getProductMaster().getProdId());
			destination.setItemCode(itemMaster.getKey().getItemCode());
			destination.setItemDescription(itemMaster.getDescription());
			destination.setItemSize(itemMaster.getItemSize());
			destination.setBalanceOnHand(itemMaster.getTotalOnHandInventory());
			destination.setBalanceOnOrder(itemMaster.getTotalOnOrderInventory());
			destination.setItemType(itemMaster.getKey().getItemType());
			//Get the retail link from the  first of prod items (there only should be one, since MRTs aren't
			// allowed in swaps).
			destination.setProductRetailLink(itemMaster.getProdItems().get(ONLY_RECORD).
					getProductMaster().getRetailLink());
			try {
				destination.setPurchaseOrders(this.timRepository.getPurchaseOrders(itemMaster.getKey().getItemCode()));
			} catch (TimException e){
				destination.setPurchaseOrders(null);
			}
			try{
				destination.setOnLiveOrPendingPog(this.planogramServiceClient.
						isOnPlanogramByUpc(itemMaster.getPrimaryUpc().getUpc()));
			} catch (CheckedSoapException e) {
				destination.setOnLiveOrPendingPog(null);
			}
			upcSwap.setDestinationPrimaryUpc(itemMaster.getPrimaryUpc().getUpc());
			upcSwap.setMakeDestinationPrimaryUpc(false);
			List<Shipper> destinationAltPack = this.shipperRepository.findByKeyUpcAndShipperTypeCode(itemMaster.getPrimaryUpc().getUpc(), ALTERNATE_PACK_CODE);
			if(destinationAltPack != null && destinationAltPack.size() > 0){
				upcSwap.setDestinationAltPack(true);
			}
		} else {
			destination.setItemCode(destinationItemCode);
			destination.setErrorMessage(String.format(ERROR_ITEM_CODE_NOT_FOUND_FOR_REQUESTED_TYPE_ITEM_CODE,
					destinationItemCode));
			upcSwap.setErrorFound(true);
		}
		upcSwap.setDestination(destination);
	}

	/**
	 * Retrieves list of other associated UPCs tied to an Associated Upc's item code. This is so the user can select a
	 * new primary for an item code only if the current upc is a primary. If the upc is not a primary, return an
	 * empty list.
	 *
	 * @param associatedUpcList A list of associated upcs attached to an item code.
	 * @param currentUpc The current upc in question.
	 * @param isPrimary Whether or not the current upc is a primary.
	 * @return A list of associated upcs not including current upc.
	 */
	private List<Long> getAssociatedUpcList(List<AssociatedUpc> associatedUpcList, long currentUpc, boolean isPrimary) {
		List<Long> returnList = new ArrayList<>();
		if(!isPrimary){
			return returnList;
		}
		returnList.addAll(associatedUpcList.stream().filter(upc -> upc.getUpc() != currentUpc)
				.map(AssociatedUpc::getUpc).collect(Collectors.toList()));
		return returnList;
	}

	/**
	 * Validates the list of upc swaps. If there is more than one 'make this upc a primary' for the same destination,
	 * add an error message. If there is the same upc more than once as the source upc, add an error message.
	 *
	 * @param upcSwapList List of upc swaps to validate.
	 * @return Modified upc swap list that can be sent to web service.
	 */
	public List<UpcSwap> validateUpcSwapList(List<UpcSwap> upcSwapList) {
		Map<Long, Integer> destinationMakePrimaryMap = new HashMap<>();
		Map<Long, Integer> sourceUpcMap = new HashMap<>();
		int count;
		List<UpcSwap> changeList = new ArrayList<>();

		// look for errors
		for(UpcSwap upcSwap: upcSwapList){
			if(upcSwap.isMakeDestinationPrimaryUpc()){
				if(destinationMakePrimaryMap.containsKey(upcSwap.getDestination().getItemCode())){
					count = destinationMakePrimaryMap.get(upcSwap.getDestination().getItemCode());
					destinationMakePrimaryMap.put(upcSwap.getDestination().getItemCode(), ++count);
				} else {
					destinationMakePrimaryMap.put(upcSwap.getDestination().getItemCode(), INITIAL_COUNT);
				}
			}
			if(sourceUpcMap.containsKey(upcSwap.getSourceUpc())){
				count = sourceUpcMap.get(upcSwap.getSourceUpc());
				sourceUpcMap.put(upcSwap.getSourceUpc(), ++count);
			} else{
				sourceUpcMap.put(upcSwap.getSourceUpc(), INITIAL_COUNT);
			}
		}

		// add error messages to all necessary records
		for(UpcSwap upcSwap: upcSwapList){
			if(upcSwap.isMakeDestinationPrimaryUpc() && destinationMakePrimaryMap.containsKey(
					upcSwap.getDestination().getItemCode()) &&
					destinationMakePrimaryMap.get(upcSwap.getDestination().getItemCode()) > INITIAL_COUNT ||
					sourceUpcMap.containsKey(upcSwap.getSourceUpc()) &&
							sourceUpcMap.get(upcSwap.getSourceUpc()) > INITIAL_COUNT) {
				if (destinationMakePrimaryMap.containsKey(upcSwap.getDestination().getItemCode()) &&
						destinationMakePrimaryMap.get(upcSwap.getDestination().getItemCode()) > INITIAL_COUNT) {
					upcSwap.getDestination().setErrorMessage((
							String.format(ERROR_ONE_PRIMARY_ONLY, upcSwap.getDestination().getItemCode())));
				}
				if (sourceUpcMap.containsKey(upcSwap.getSourceUpc()) &&
						sourceUpcMap.get(upcSwap.getSourceUpc()) > INITIAL_COUNT) {
					upcSwap.getSource().setErrorMessage(String.format(ERROR_ONE_SOURCE_UPC_ONLY,
							upcSwap.getSourceUpc()));
				}
				// Add make primary to the beginning of the list
				changeList.add(0, upcSwap);
			} else if(upcSwap.isMakeDestinationPrimaryUpc() && upcSwap.isSourcePrimaryUpc() &&
					upcSwap.getSource().getItemCode().longValue() ==
							upcSwap.getDestination().getItemCode().longValue()){
				upcSwap.getDestination().setErrorMessage(String.format(
						ERROR_UPC_ALREADY_ITEM_PRIMARY_UPC, upcSwap.getSourceUpc(), upcSwap.getSource().getItemCode()));
				changeList.add(0, upcSwap);
			} else if(upcSwap.isMakeDestinationPrimaryUpc()) {
				changeList.add(upcSwap);
			} else {
				// Add make primary to the beginning of the list
				changeList.add(0, upcSwap);
			}
		}
		return changeList;
	}

	/**
	 * Reorders upc swap list by precedence order (listed at end of comments) so the data doesn't change before
	 * the other swaps are attempted. If you don't do this and commit a 'make destination primary' swap on an item code
	 * and then do another swap on the same item code, it will fail because it no longer matches the current state of
	 * the data. Also, if you commit a 'source primary' upc swap, and then attempt to do a 'source associate' upc swap,
	 * the associate swap will fail because it no longer matches the current state of the data.
	 * Order Precedence:
	 * 1. Associate UPC on source side (not make destination primary)
	 * 2. Primary UPC on source side (not make destination primary)
	 * 3. Make destination primary UPC
	 *
	 * @param upcSwapList List of upc swaps to reorder.
	 * @return Modified upc swap list that can be sent to web service.
	 */
	public List<UpcSwap> rearrangeSwapListByPrecedence(List<UpcSwap> upcSwapList) {
		List<UpcSwap> associateNonMakeDestinationPrimaryList = new ArrayList<>();
		List<UpcSwap> primaryNonMakeDestinationPrimaryList = new ArrayList<>();
		List<UpcSwap> makeDestinationPrimaryList = new ArrayList<>();
		for(UpcSwap upcSwap: upcSwapList){
			if(upcSwap.isMakeDestinationPrimaryUpc()) {
				makeDestinationPrimaryList.add(upcSwap);
			} else if(upcSwap.isSourcePrimaryUpc()){
				primaryNonMakeDestinationPrimaryList.add(upcSwap);
			} else {
				associateNonMakeDestinationPrimaryList.add(upcSwap);
			}
		}
		List<UpcSwap> changeList = new ArrayList<>();
		changeList.addAll(associateNonMakeDestinationPrimaryList);
		changeList.addAll(makeDestinationPrimaryList);
		changeList.addAll(primaryNonMakeDestinationPrimaryList);
		return changeList;
	}

	/**
	 * Reorders upc swap list by precedence order (listed at end of comments) so the data doesn't change before
	 * the other swaps are attempted. If you don't do this and commit a 'make destination primary' swap on an item code
	 * and then do another swap on the same item code, it will fail because it no longer matches the current state of
	 * the data. Also, if you commit a 'source primary' upc swap, and then attempt to do a 'source associate' upc swap,
	 * the associate swap will fail because it no longer matches the current state of the data.
	 * Order Precedence:
	 * 1. Associate UPC on source side (not make destination primary)
	 * 2. Primary UPC on source side (not make destination primary)
	 * 3. Make destination primary UPC
	 *
	 * @param upcSwapList List of upc swaps to reorder.
	 * @return Modified upc swap list that can be sent to web service.
	 */
	public List<UpcSwap> rearrangeSwapListByPrecedenceForAlternatePack(List<UpcSwap> upcSwapList) {
		List<UpcSwap> associateNonMakeDestinationPrimaryList = new ArrayList<>();
		List<UpcSwap> primaryNonMakeDestinationPrimaryList = new ArrayList<>();
		List<UpcSwap> makeDestinationPrimaryList = new ArrayList<>();
		for(UpcSwap upcSwap: upcSwapList){
			if(upcSwap.isMakeDestinationPrimaryUpc()) {
				makeDestinationPrimaryList.add(upcSwap);
			} else if(upcSwap.isSourcePrimaryUpc()){
				primaryNonMakeDestinationPrimaryList.add(upcSwap);
			} else {
				associateNonMakeDestinationPrimaryList.add(upcSwap);
			}
		}
		List<UpcSwap> changeList = new ArrayList<>();
		changeList.addAll(makeDestinationPrimaryList);
		changeList.addAll(associateNonMakeDestinationPrimaryList);
		changeList.addAll(primaryNonMakeDestinationPrimaryList);
		return changeList;
	}

	/**
	 * This method returns a list of associated UPCs (just the UPC) linked to an item with the primaries first.
	 *
	 * @param itemMasterKey Key to search for.
	 * @return List of associated upcs linked to searched for item.
	 */
	public List<Long> getPrimaryFirstUpcList(ItemMasterKey itemMasterKey) {
		List<Long> returnList = new ArrayList<>();
		for(AssociatedUpc associatedUpc : this.itemMasterRepository.findOne(
				itemMasterKey).getPrimaryUpc().getAssociateUpcs()){
			if(associatedUpc.getSellingUnit() != null){
				if(associatedUpc.getSellingUnit().isPrimaryUpc()){
					// Add primary UPC to the beginning of the list.
					returnList.add(0, associatedUpc.getUpc());
				} else {
					returnList.add(associatedUpc.getUpc());
				}
			}
		}
		return returnList;
	}

	/**
	 * This method retrieves the current state of the source and destination list of associated UPCs after a UPC swap
	 * has been completed. A WHS to WHS move will have a source and destination as both sides are being affected
	 * and thus the changes need to be shown. A DSD to both will only have a destination as the source won't be affected
	 * (destination will have a new UPC). A both to DSD will only have a source (the WHS being removed) since there
	 * won't be DSD changes.
	 *
	 * @param upcSwapList The list of UPC swaps.
	 * @return The list of UPCs with updated list of associated UPCs.
	 */
	public List<UpcSwap> getUpdatedAssociatedUpcList(List<UpcSwap> upcSwapList) {
		ItemMasterKey itemMasterKey;
		for(UpcSwap upcSwap : upcSwapList){
			if(upcSwap.getSource() != null && StringUtils.isNotEmpty(upcSwap.getSource().getItemType())){
				itemMasterKey = new ItemMasterKey();
				itemMasterKey.setItemCode(upcSwap.getSource().getItemCode());
				itemMasterKey.setItemType(upcSwap.getSource().getItemType());
				upcSwap.getSource().setAssociatedUpcList(this.getPrimaryFirstUpcList(itemMasterKey));
			}
			if(upcSwap.getDestination() != null && StringUtils.isNotEmpty(upcSwap.getDestination().getItemType())) {
				itemMasterKey = new ItemMasterKey();
				itemMasterKey.setItemCode(upcSwap.getDestination().getItemCode());
				itemMasterKey.setItemType(upcSwap.getDestination().getItemType());
				upcSwap.getDestination().setAssociatedUpcList(this.getPrimaryFirstUpcList(itemMasterKey));
			}
		}
		return upcSwapList;
	}

	/**
	 * These functions are to be used for support in testing.
	 */

	/**
	 * Sets the associatedUpcRepository.
	 *
	 * @param associatedUpcRepository The associatedUpcRepository to be set.
	 */
	public void setAssociatedUpcRepository(AssociatedUpcRepository associatedUpcRepository){
		this.associatedUpcRepository = associatedUpcRepository;
	}

	/**
	 * Sets the itemMasterRepository.
	 *
	 * @param itemMasterRepository The itemMasterRepository to be set.
	 */
	public void setItemMasterRepository(ItemMasterRepository itemMasterRepository){
		this.itemMasterRepository = itemMasterRepository;
	}

	/**
	 * Sets the Tim Repository.
	 *
	 * @param timRepository The Tim Repository.
	 */
	public void setTimRepository(TimRepository timRepository){
		this.timRepository = timRepository;
	}

	/**
	 * Find warehouse swap.
	 *
	 * @param upcSwap           the source upc
	 */
	private void findWarehouseSwap(UpcSwap upcSwap) {

		// set upc swap source endpoint values
		this.setUpcSwapEndPointValues(upcSwap, true);

		// set upc swap destination endpoint values
		this.setUpcSwapEndPointValues(upcSwap, false);

		// set future primary UPC for both items to current primary UPC of opposite item
		upcSwap.setSelectSourcePrimaryUpc(upcSwap.getDestinationPrimaryUpc());
		upcSwap.setDestinationPrimaryUpcSelected(upcSwap.getSourceUpc());
	}

	/**
	 * Method that is used to set the values of either the source or the destination end point on a upc swap.
	 * @param upcSwap
	 * @param isSource
	 */
	private void setUpcSwapEndPointValues(UpcSwap upcSwap, Boolean isSource){
		AssociatedUpc associatedUpc = null;
		ItemMasterKey itemMasterKey;
		ItemMaster itemMaster = null;
		Long sentItemCodeOrUpc = null;
		boolean didSendItemCode = false;
		UpcSwap.SwappableEndPoint endPoint = isSource ? upcSwap.getSource() : upcSwap.getDestination();

		if(isSource){
			if(upcSwap.getSource().getItemCode() != null) {
				sentItemCodeOrUpc = upcSwap.getSource().getItemCode();
				itemMasterKey = new ItemMasterKey();
				itemMasterKey.setItemCode(sentItemCodeOrUpc);
				itemMasterKey.setItemType(ItemMasterKey.WAREHOUSE);
				itemMaster = this.itemMasterRepository.findOne(itemMasterKey);
				didSendItemCode = true;
			} else if (upcSwap.getSourceUpc() != null) {
				sentItemCodeOrUpc = upcSwap.getSourceUpc();
				associatedUpc = this.findAssociatedUpcByUpc(sentItemCodeOrUpc);
			}
		} else {
			if(upcSwap.getDestination().getItemCode() != null) {
				sentItemCodeOrUpc = upcSwap.getDestination().getItemCode();
				itemMasterKey = new ItemMasterKey();
				itemMasterKey.setItemCode(sentItemCodeOrUpc);
				itemMasterKey.setItemType(ItemMasterKey.WAREHOUSE);
				itemMaster = this.itemMasterRepository.findOne(itemMasterKey);
				didSendItemCode = true;
			} else if (upcSwap.getDestinationPrimaryUpc() != null) {
				sentItemCodeOrUpc = upcSwap.getDestinationPrimaryUpc();
				associatedUpc = this.findAssociatedUpcByUpc(sentItemCodeOrUpc);
			}
		}

		if(associatedUpc !=null && associatedUpc.getSellingUnit() != null){
			ItemMaster tempItemMaster = associatedUpc.getPrimaryUpc().getWarehouseItem();
			if(tempItemMaster != null) {

				endPoint.setProductId(associatedUpc.getSellingUnit().getProdId());
				endPoint.setItemCode(tempItemMaster.getKey().getItemCode());
				endPoint.setItemDescription(tempItemMaster.getDescription());
				endPoint.setItemSize(associatedUpc.getSellingUnit().getTagSize());
				endPoint.setBalanceOnHand(tempItemMaster.getTotalOnHandInventory());
				endPoint.setBalanceOnOrder(tempItemMaster.getTotalOnOrderInventory());
				endPoint.setProductRetailLink(associatedUpc.getSellingUnit().getProductMaster().getRetailLink());
				endPoint.setItemType(tempItemMaster.getKey().getItemType());

				try {
					endPoint.setPurchaseOrders(this.timRepository.getPurchaseOrders(tempItemMaster.getKey().getItemCode()));
				} catch (TimException e) {
					endPoint.setPurchaseOrders(null);
				}
				try {
					endPoint.setOnLiveOrPendingPog(this.planogramServiceClient.
							isOnPlanogramByUpc(tempItemMaster.getPrimaryUpc().getUpc()));
				} catch (CheckedSoapException e) {
					endPoint.setOnLiveOrPendingPog(null);
				}
			} else {
				endPoint.setErrorMessage(
						String.format(ERROR_ITEM_CODE_NOT_FOUND_FOR_REQUESTED_TYPE, associatedUpc.getUpc()));
				upcSwap.setErrorFound(true);
			}
		} else if (itemMaster != null) {

			// Get the product ID from the  first of prod items (there only should be one, since MRTs aren't
			// allowed in swaps) .
			endPoint.setProductId(itemMaster.getProdItems().get(ONLY_RECORD).getProductMaster().getProdId());
			endPoint.setItemCode(itemMaster.getKey().getItemCode());
			endPoint.setItemDescription(itemMaster.getDescription());
			endPoint.setItemSize(itemMaster.getItemSize());
			endPoint.setBalanceOnHand(itemMaster.getTotalOnHandInventory());
			endPoint.setBalanceOnOrder(itemMaster.getTotalOnOrderInventory());
			endPoint.setItemType(itemMaster.getKey().getItemType());
			//Get the retail link from the  first of prod items (there only should be one, since MRTs aren't
			// allowed in swaps).
			endPoint.setProductRetailLink(itemMaster.getProdItems().get(
					ONLY_RECORD).getProductMaster().getRetailLink());
			endPoint.setItemType(itemMaster.getKey().getItemType());
			try {
				endPoint.setPurchaseOrders(this.timRepository.getPurchaseOrders(itemMaster.getKey().getItemCode()));
			} catch (TimException e){
				endPoint.setPurchaseOrders(null);
			}
			try{
				endPoint.setOnLiveOrPendingPog(this.planogramServiceClient.
						isOnPlanogramByUpc(itemMaster.getPrimaryUpc().getUpc()));
			} catch (CheckedSoapException e) {
				endPoint.setOnLiveOrPendingPog(null);
			}
			if(isSource){
				upcSwap.setSourceUpc(itemMaster.getPrimaryUpc().getUpc());
			} else {
				upcSwap.setDestinationPrimaryUpc(itemMaster.getPrimaryUpc().getUpc());
			}
		} else {
			if(didSendItemCode) {
				endPoint.setErrorMessage(String.format(ERROR_ITEM_CODE_NOT_FOUND, sentItemCodeOrUpc));
				upcSwap.setErrorFound(true);
			} else {
				endPoint.setErrorMessage(String.format(ERROR_UPC_NOT_FOUND, sentItemCodeOrUpc));
				upcSwap.setErrorFound(true);
			}
		}
	}

	/**
	 * This method takes in a list of UpcSwaps, with only the following filled in:
	 * 1. sourceUpc or source.itemCode, AND
	 * 2. destinationPrimaryUpc or destination.itemCode
	 * then fills in the rest of the data for the UpcSwap.
	 *
	 * @param upcSwaps List of UpcSwaps with minimal data.
	 * @return Fully populated list of UpcSwaps.
	 */
	public List<UpcSwap> findAllWarehouseSwaps(List<UpcSwap> upcSwaps) {
		List<UpcSwap> toReturns = this.removeEmptyUpcSwapsFromList(upcSwaps);
		toReturns.forEach(this::findWarehouseSwap);
		return toReturns;
	}

	/**
	 * This method removes any upc swaps that do not have at least 2 out of the 4 necessary parts to a upc swap:
	 * 1. sourceUpc or
	 * 2. source.itemCode
	 * AND
	 * 3. destinationPrimaryUpc or
	 * 4. destination.itemCode
	 * It is at least 2 our of 4 because, if the user has previously fetched the details for this upc swap, then
	 * fetched the details again, there may be up to all four necessary parts.
	 *
	 * @param upcSwaps List of upc swaps sent from front end to fetch details for.
	 * @return The Modified list of upc swaps only containing non-empty upc swaps.
	 */
	private List<UpcSwap> removeEmptyUpcSwapsFromList(List<UpcSwap> upcSwaps) {
		List<UpcSwap> noEmptyUpcSwaps = new ArrayList<>();

		for(UpcSwap upcSwap : upcSwaps){
			if((upcSwap.getSourceUpc() != null || upcSwap.getSource().getItemCode() != null) &&
					(upcSwap.getDestinationPrimaryUpc() != null || upcSwap.getDestination().getItemCode() != null)){
				noEmptyUpcSwaps.add(upcSwap);
			}
		}
		return noEmptyUpcSwaps;
	}
}
