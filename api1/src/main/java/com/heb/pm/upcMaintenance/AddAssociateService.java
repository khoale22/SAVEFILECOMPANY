/*
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.upcMaintenance;

import com.heb.pm.entity.AssociatedUpc;
import com.heb.pm.entity.ItemMaster;
import com.heb.pm.entity.ItemMasterKey;
import com.heb.pm.inventory.TimException;
import com.heb.pm.repository.AssociatedUpcRepository;
import com.heb.pm.repository.ItemMasterRepository;
import com.heb.pm.repository.TimRepository;
import com.heb.pm.ws.CheckedSoapException;
import com.heb.pm.ws.PlanogramServiceClient;
import com.heb.pm.ws.ProductManagementServiceClient;
import com.heb.util.list.ListFormatter;
import com.heb.util.upc.UpcUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service that supports adding associate UPCs through the UPC Swap Service.
 *
 * @author d116773
 * @since 2.6.1
 */
@Service
public class AddAssociateService {

	private static final Logger logger = LoggerFactory.getLogger(AddAssociateService.class);

	// Error messages
	private static final String MISSING_SOURCE_ERROR = "Add associate is missing a target";
	private static final String MISSING_TO_ADD_ERROR = "Add associate is missing a UPC to add";
	private static final String UNDEFINED_TARGET_ERROR = "Item code, primary UPC, and product ID must be set";
	private static final String ITEM_CODE_PRIMARY_UPC_MISMATCH_ERROR = "Item code does not match primary UPC";
	private static final String UPC_AND_ITEM_CODE_MISSING_ERROR = "Either UPC or item code must be defined";
	private static final String UPC_NOT_FOUND_ERROR = "UPC %d not found";
	private static final String ITEM_CODE_NOT_FOUND_ERROR = "Item code %d not found";
	private static final String NO_PRODUCTS_FOUND_ERROR = "No products are tied to item code %d";
	private static final String ITEM_CODE_NOT_FOUND_FOR_UPC_ERROR = "Item code not found for UPC %d";
	private static final String DSD_NOT_ALLOWED_ERROR = "Cannot add associate to DSD UPC";
	private static final String MRT_NOT_ALLOWED_ERROR = "Cannot add associate to an MRT";
	private static final String TO_ADD_UPC_NOT_FINED_ERROR = "UPC to add not defined";
	private static final String UPC_EXISTS_ERROR = "UPC %d already exists";
	private static final String NO_PRE_DIGIT_TWO_ERROR =
			"Cannot add price embedded UPCs through this function, please use CPS";
	private static final String NO_ALT_PACK_ERROR = "Cannot add alt-pack UPCs through this function, please use CPS";
	private static final String CHECK_DIGIT_INVALID = "Check digit invalid";

	// Log Messages
	private static final String IN_ERROR_LOG_MESSAGE = "Add Associate %s is in error";
	private static final String VALIDATE_UPC_LOG_MESSAGE = "validating UPC %d with check digit %d";

	@Autowired
	private AssociatedUpcRepository associatedUpcRepository;

	@Autowired
	private ItemMasterRepository itemMasterRepository;

	@Autowired
	private TimRepository timRepository;

	@Autowired
	private ProductManagementServiceClient productManagementServiceClient;

	@Autowired
	private PlanogramServiceClient planogramServiceClient;

	/**
	 * Retrieves the detailed information about products for a list of requests to add associates.
	 *
	 * @param addAssociatesList The list of requests to get details for.
	 * @return The same list of associate requests with populated details.
	 */
	public List<UpcSwap> getAddAssociateDetails(List<UpcSwap> addAssociatesList) {
		return this.populateAddAssociateDetails(addAssociatesList);
	}

	/**
	 * Processes a list of request to add associate UPC.
	 *
	 * @param addAssociatesList The list of requests to process.
	 */
	public void addAssociates(List<UpcSwap> addAssociatesList) {

		for (UpcSwap addAssociate : addAssociatesList) {
			AddAssociateService.logger.debug(addAssociate.toString());

			// Make sure the object has a source and destination defined.
			this.validateSourceAndTargetObjects(addAssociate);
			if (addAssociate.isErrorFound()) {
				continue;
			}
			// Check that the target and UPC to add are valid before performing the swap.
			else if (!this.validateTargetForSwap(addAssociate.getSource())) {
				addAssociate.setErrorFound(true);
			}
			if(!this.validateUpcToAdd(addAssociate.getDestination())){
				addAssociate.setErrorFound(true);
			}
		}

		// Delegate the actual save to ProductManagementServiceClient.
		try {
			this.productManagementServiceClient.submitAddAssociate(addAssociatesList);
		} catch (CheckedSoapException e) {
			AddAssociateService.logger.error(e.getMessage());
			throw new RuntimeException(e.getMessage(), e.getCause());
		}
	}

	/**
	 * Validates the target item to add the associate to.
	 *
	 * @param target The SwappableEndPoint with the fully populated details about the target item for the swap.
	 * @return True if the target item is valid and false otherwise.
	 */
	private boolean validateTargetForSwap(UpcSwap.SwappableEndPoint target) {

		AddAssociateService.logger.debug(target.toString());

		// Check that item code and primary UPC are set.
		if (target.getItemCode() == null || target.getPrimaryUpc() == null || target.getProductId() == null) {
			target.setErrorMessage(AddAssociateService.UNDEFINED_TARGET_ERROR);
			return false;
		}

		AddAssociateService.logger.debug(target.toString());
		// Pull the item tied to the primary UPC. It must be warehouse, not an MRT, and match what the user passed.
		ItemMaster itemMaster = this.getItemCodeForTarget(target, target.getPrimaryUpc());

		AddAssociateService.logger.debug(target.toString());
		// If an item code wasn't found, or it's not valid for a swap, return false.
		if (itemMaster == null || !this.validateItemMaster(target, itemMaster)) {
			return false;
		}

		AddAssociateService.logger.debug(target.toString());
		// Make sure the primary UPC they said they were trying to add to is the primary UPC tied to the item.
		if (!itemMaster.getKey().getItemCode().equals(target.getItemCode())) {
			target.setErrorMessage(AddAssociateService.ITEM_CODE_PRIMARY_UPC_MISMATCH_ERROR);
			return false;
		}

		return true;
	}

	/**
	 * Checks that an add associate request has both a source and destination defined. This only checks at the
	 * object level.
	 *
	 * @param addAssociate The add associate request to check.
	 * @return True if the source and destination defined and false otherwise.
	 */
	private void validateSourceAndTargetObjects(UpcSwap addAssociate) {

		AddAssociateService.logger.debug(addAssociate.toString());

		// If the UPC Swap doesn't have a source or destination, then it's not configured correctly, so return
		// errors. Since the source or destination is null, the procedure needs to add them so that the messages
		// have some place to live.

		if (addAssociate.getSource() == null) {
			UpcSwap.SwappableEndPoint source = addAssociate.new SwappableEndPoint();
			addAssociate.setSource(source);
			source.setErrorMessage(AddAssociateService.MISSING_SOURCE_ERROR);
			addAssociate.setErrorFound(true);
		}

		AddAssociateService.logger.debug(addAssociate.toString());
		if (addAssociate.getDestination() == null) {
			UpcSwap.SwappableEndPoint destination = addAssociate.new SwappableEndPoint();
			addAssociate.setDestination(destination);
			destination.setErrorMessage(AddAssociateService.MISSING_TO_ADD_ERROR);
			addAssociate.setErrorFound(true);
		}
	}

	/**
	 * Takes a list of UpcSwap objects that have partially filled out data and fully populates each of them. It
	 * must contain at least a UPC or item code for each source and a UPC and possibly a check digit for the target.
	 * This will set any errors it encounters on the source or destination objects.
	 *
	 * @param addAssociatesList The list of add associate requests to update.
	 * @return The same list fully populated.
	 */
	private List<UpcSwap> populateAddAssociateDetails(List<UpcSwap> addAssociatesList) {

		for (UpcSwap addAssociate : addAssociatesList) {

			AddAssociateService.logger.debug(addAssociate.toString());
			this.validateSourceAndTargetObjects(addAssociate);
			if (addAssociate.isErrorFound()) {
				continue;
			}

			AddAssociateService.logger.debug(addAssociate.toString());
			// Fetch the data for the source.
			this.populateTargetDetails(addAssociate.getSource());

			AddAssociateService.logger.debug(addAssociate.toString());
			// The sourceUpc of the main swap object needs to be set to record the audit information when
			// the service is called.
			addAssociate.setSourceUpc(addAssociate.getSource().getUpc());

			AddAssociateService.logger.debug(addAssociate.toString());
			// Fetch the data for the destination.
			this.validateUpcToAdd(addAssociate.getDestination());

			AddAssociateService.logger.debug(addAssociate.toString());
			// The destinationUpc of the main swap object needs to be set to record the audit information when
			// the service is called.
			addAssociate.setDestinationPrimaryUpc(addAssociate.getDestination().getUpc());
		}

		return addAssociatesList;
	}

	/**
	 * Populates a SwappableEndPoint with data assuming that it is intended to be a target to add a UPC to. Either
	 * the UPC or item code field must be populated.
	 *
	 * @param target The SwappableEndPoint to populate data for.
	 */
	private void populateTargetDetails(UpcSwap.SwappableEndPoint target) {

		AddAssociateService.logger.debug(target.toString());
		// Make sure item code or UPC is set
		if (target.getUpc() == null && target.getItemCode() == null) {
			target.setErrorMessage(AddAssociateService.UPC_AND_ITEM_CODE_MISSING_ERROR);
			return;
		}

		// Clear out all the things that this method will set.
		target.setErrorMessage(null);
		target.setProductId(null);
		target.setItemType(null);
		target.setPrimaryUpc(null);
		target.setItemDescription(null);
		target.setItemSize(null);
		target.setBalanceOnHand(0);
		target.setBalanceOnOrder(0);
		target.setProductRetailLink(null);
		target.setPurchaseOrders(null);
		target.setOnLiveOrPendingPog(null);

		ItemMaster itemMaster;

		// If the user searched by UPC or both, find that data. Treat UPC as the default.
		if (target.getUpc()!= null) {

			AddAssociateService.logger.debug(target.toString());

			// First, get the primary UPC for the UPC they entered.
			AssociatedUpc associatedUpc = this.associatedUpcRepository.findOne(target.getUpc());
			if (associatedUpc == null) {
				target.setErrorMessage(String.format(AddAssociateService.UPC_NOT_FOUND_ERROR, target.getUpc()));
				return;
			}

			AddAssociateService.logger.debug(target.toString());
			// Get the item code associated with the primary UPC.
			itemMaster = this.getItemCodeForTarget(target, associatedUpc.getPrimaryUpc().getUpc());

			if (itemMaster == null) {
				// The error message is set already in getItemCodeForTarget.
				return;
			}
		} else {
			AddAssociateService.logger.debug(target.toString());

			ItemMasterKey key = new ItemMasterKey();
			key.setItemCode(target.getItemCode());
			// This is only supported for warehouse product.
			key.setItemType(ItemMasterKey.WAREHOUSE);
			itemMaster = this.itemMasterRepository.findOne(key);
			if (itemMaster == null) {
				target.setErrorMessage(
						String.format(AddAssociateService.ITEM_CODE_NOT_FOUND_ERROR, target.getItemCode()));
				return;
			}
			// Since the user didn't provide a UPC, just use the primary.
			target.setUpc(itemMaster.getOrderingUpc());
		}

		AddAssociateService.logger.debug(target.toString());
		// This is useful even if it is not valid, so go ahead and set these.
		target.setItemDescription(itemMaster.getDescription());
		target.setItemSize(itemMaster.getItemSize());
		target.setPrimaryUpc(itemMaster.getOrderingUpc());
		target.setItemCode(itemMaster.getKey().getItemCode());
		target.setItemType(itemMaster.getKey().getItemType());
		target.setBalanceOnHand(itemMaster.getTotalOnHandInventory());
		target.setBalanceOnOrder(itemMaster.getTotalOnOrderInventory());

		AddAssociateService.logger.debug(target.toString());
		try {
			target.setPurchaseOrders(this.timRepository.getPurchaseOrders(
					itemMaster.getKey().getItemCode()));
		} catch (TimException e) {
			// Treat not being able to contact TIM as a recoverable error.
			AddAssociateService.logger.error(e.getMessage());
		}

		AddAssociateService.logger.debug(target.toString());
		try {
			target.setOnLiveOrPendingPog(this.planogramServiceClient.
					isOnPlanogramByUpc(itemMaster.getPrimaryUpc().getUpc()));
		} catch (CheckedSoapException e) {
			// Treat not being able to contact IKB as a recoverable error.
			AddAssociateService.logger.error(e.getMessage());
		}

		AddAssociateService.logger.debug(target.toString());
		// Now that most of the data are there, validate it and, if valid, you can set the product ID.
		if (this.validateItemMaster(target, itemMaster)) {
			// See the check above, we're not processing MRTs, so the getProdItems() will only return
			// one object, so we can safely use .get(0).
			target.setProductId(itemMaster.getProdItems().get(0).getProductMaster().getProdId());
			target.setProductRetailLink(itemMaster.getProdItems().get(0).getProductMaster().getRetailLink());
		}
	}

	/**
	 * Fetches an ItemMaster for a primary UPC.
	 *
	 * @param target If there are any errors, they will be put in the error message of the target.
	 * @param primaryUpc The primary UPC to look for an ItemMaster for.
	 * @return The ItemMaster for a given primary UPC. If not found, will return a null.
	 */
	private ItemMaster getItemCodeForTarget(UpcSwap.SwappableEndPoint target, long primaryUpc) {

		// Get the item code associated with the primary UPC.
		List<ItemMaster> itemMasters = this.itemMasterRepository.findItemMasterByOrderingUpc(primaryUpc);
		AddAssociateService.logger.debug(ListFormatter.formatAsString(itemMasters));
		if (itemMasters == null || itemMasters.isEmpty()) {
			target.setErrorMessage(String.format(AddAssociateService.ITEM_CODE_NOT_FOUND_FOR_UPC_ERROR, primaryUpc));
			return null;
		}

		// We can't add associates to DSD only UPCs.
		if (itemMasters.size() == 1 && itemMasters.get(0).getKey().isDsd()) {
			target.setErrorMessage(AddAssociateService.DSD_NOT_ALLOWED_ERROR);
			return null;
		}

		// Since we can ge back items that are warehouse and DSD, find the one that is a warehouse product
		// and return it.
		for (ItemMaster im : itemMasters) {
			if (im.getKey().isWarehouse()) {
				return im;
			}
		}

		target.setErrorMessage(String.format(AddAssociateService.ITEM_CODE_NOT_FOUND_FOR_UPC_ERROR, primaryUpc));

		return null;
	}

	/**
	 * Checks to make sure an item code is valid to add an associate to.
	 *
	 * @param target The target the user is trying to add an associate to.
	 * @param itemMaster The ItemMaster to validate.
	 * @return True if the item is valid and false otherwise.
	 */
	private boolean validateItemMaster(UpcSwap.SwappableEndPoint target,  ItemMaster itemMaster) {

		AddAssociateService.logger.debug(itemMaster.toString());

		// Must be tied to a product.
		if (itemMaster.getProdItems() == null) {
			target.setErrorMessage(
					String.format(AddAssociateService.NO_PRODUCTS_FOUND_ERROR, itemMaster.getKey().getItemCode()));
			return false;
		}

		// Cannot be an MRT.
		if (itemMaster.getProdItems().size() > 1) {
			target.setErrorMessage(AddAssociateService.MRT_NOT_ALLOWED_ERROR);
			return false;
		}

		return true;
	}

	/**
	 * Validates the UPC that the user is trying to add.
	 *
	 * @param upcToAdd The SwappableEndPoint with the UPC to add defined in it.
	 * @return True if the UPC is valid and false otherwise.
	 */
	private boolean validateUpcToAdd(UpcSwap.SwappableEndPoint upcToAdd) {

		AddAssociateService.logger.debug(String.format(AddAssociateService.VALIDATE_UPC_LOG_MESSAGE,
				upcToAdd.getUpc(), upcToAdd.getCheckDigit()));

		upcToAdd.setErrorMessage(null);

		// There has to be a UPC to add.
		if (upcToAdd.getUpc() == null) {
			upcToAdd.setErrorMessage(AddAssociateService.TO_ADD_UPC_NOT_FINED_ERROR);
			return false;
		}

		// Make sure it's not an existing UPC.
		AssociatedUpc associatedUpc = this.associatedUpcRepository.findOne(upcToAdd.getUpc());
		if (associatedUpc != null) {
			upcToAdd.setErrorMessage(String.format(AddAssociateService.UPC_EXISTS_ERROR, upcToAdd.getUpc()));
			return false;
		}

		// If it's a PLU, just use it, don't need to check for special UPCs or the check digit.
		if (UpcUtils.isPlu(upcToAdd.getUpc())) {
			return true;
		}

		// If it's not:
		// It must not be a pre-digit 2
		// Must not be a pre-digit 4
		// Must have a valid check-digit
		// Must be a valid g14 UPC (this is done in all the other three functions).
		try {
			if (UpcUtils.isPreDigitTwo(upcToAdd.getUpc())) {
				upcToAdd.setErrorMessage(AddAssociateService.NO_PRE_DIGIT_TWO_ERROR);
				return false;
			}

			if (UpcUtils.isPreDigitFour(upcToAdd.getUpc())) {
				upcToAdd.setErrorMessage(AddAssociateService.NO_ALT_PACK_ERROR);
				return false;
			}

			if (upcToAdd.getCheckDigit() == null ||
					!UpcUtils.validateCheckDigit(upcToAdd.getUpc(), upcToAdd.getCheckDigit())){
				upcToAdd.setErrorMessage(AddAssociateService.CHECK_DIGIT_INVALID);
				return false;
			}
		} catch (IllegalArgumentException e) {
			upcToAdd.setErrorMessage(e.getMessage());
		}

		return true;
	}
}
