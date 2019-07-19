/*
 * MoveWarehouseUpcSwapService
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.upcMaintenance;

import com.heb.pm.ws.CheckedSoapException;
import com.heb.pm.ws.ProductManagementServiceClient;
import org.apache.commons.lang.SerializationUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Holds all business logic related to warehouse move upc swaps .
 *
 * @author m314029
 * @since 2.1.0
 */
@Service
public class WarehouseToWarehouseService {

	@Autowired
	private UpcSwapUtils upcSwapUtils;

	private static final String STRING_SUCCESS = "Success";
	private static final String NOT_ATTEMPTED_MESSAGE = "Not attempted.";

	@Autowired
	private ProductManagementServiceClient productManagementServiceClient;

	/**
	 * Returns a list of all UPC swap information from a list of UPCs and matching item codes.
	 *
	 * @param upcList      the upc list
	 * @param itemCodeList the item code list
	 * @return A list of all UPC swaps.
	 */
	public List<UpcSwap> findAll(List<Long> upcList, List<Long> itemCodeList) {
		return upcSwapUtils.findAll(upcList,itemCodeList, UpcSwapUtils.WHS_TO_WHS);
	}

	/**
	 * Submits a list of warehouse move UPC swaps.
	 *
	 * @param upcSwapList List of UPC swaps to save.
	 * @return The updated list of UPC swaps.
	 */
	public List<UpcSwap> update(List<UpcSwap> upcSwapList) {
		List<UpcSwap> returnList = new ArrayList<>();
		// PM-1313 PM Defect: UPC Maintenance > Warehouse to Warehouse Move > Alt Pack Associate UPCs
		// check the list of upc swap that has been sent from front end. Filter all element has destination is alternate pack.
		// if element swap has destination is alternate pack we need handle different with another case (should send MP2P request first and MP2A second)
		List<UpcSwap> alternatePackList = new ArrayList<>();
		List<UpcSwap> upcSwapListNoneAltPack = new ArrayList<>();
		for(UpcSwap upcSwap : upcSwapList) {
			if(upcSwap.isDestinationAltPack() && upcSwap.isSourcePrimaryUpc() &&
					!upcSwap.getPrimarySelectOption().equals(UpcSwap.PrimarySelectOption.JUST_PRIMARY)){
				alternatePackList.add(upcSwap);
			}else{
				upcSwapListNoneAltPack.add(upcSwap);
			}
		}
		//Handle update with the list of none alternate pack, that work is old way with index of request(A2A request first and A2P reuquest second and P2A is last).
		if(!upcSwapListNoneAltPack.isEmpty()) {
			upcSwapListNoneAltPack = upcSwapUtils.rearrangeSwapListByPrecedence(this.createAdditionalSwaps(upcSwapListNoneAltPack));
			String statusMessage;
			for (UpcSwap upcSwap : upcSwapListNoneAltPack) {
				statusMessage = StringUtils.EMPTY;
				if (upcSwap.getSource().getErrorMessage() == null && upcSwap.getDestination().getErrorMessage() == null) {
					try {
						this.productManagementServiceClient.submitUpcSwap(upcSwap);
						upcSwap.setStatusMessage(STRING_SUCCESS);
					} catch (CheckedSoapException e) {
						upcSwap.setErrorFound(true);
						upcSwap.getDestination().setErrorMessage(e.getMessage());
						upcSwap.setStatusMessage(e.getMessage());
					}
				} else {
					if (upcSwap.getSource().getErrorMessage() != null) {
						statusMessage = statusMessage.concat(upcSwap.getSource().getErrorMessage());
						upcSwap.setErrorFound(true);
					}
					if (upcSwap.getDestination().getErrorMessage() != null) {
						statusMessage = statusMessage.concat(upcSwap.getDestination().getErrorMessage());
						upcSwap.setErrorFound(true);
					}
					upcSwap.setStatusMessage(statusMessage);
				}
			}
			returnList.addAll(upcSwapUtils.getUpdatedAssociatedUpcList(upcSwapListNoneAltPack));
		}
		//Handle update with the list of alternate pack(should send MP2P request first and MP2A second)
		if(!alternatePackList.isEmpty()){
			returnList.addAll(this.updateForDestinationAlternatePack(alternatePackList));
		}
		return returnList;
	}

	/**
	 * Submits a list of warehouse move UPC swaps for special case with destination is alternate pack.
	 *
	 * @param upcSwapList List of UPC swaps to save.
	 * @return The updated list of UPC swaps.
	 */
	public List<UpcSwap> updateForDestinationAlternatePack(List<UpcSwap> upcSwapList){
		List<UpcSwap> returnList = new ArrayList<>();
		for (UpcSwap upcSwap : upcSwapList) {
			List<UpcSwap> upcSwapListRet = upcSwapUtils.rearrangeSwapListByPrecedenceForAlternatePack(this.createAdditionalSwapsForAlternatePack(upcSwap));
			String statusMessage;
			boolean primaryHasError = false;
			int i = 0;
			for (UpcSwap upcSwapElm : upcSwapListRet) {
				if(!primaryHasError) {
					statusMessage = StringUtils.EMPTY;
					if (upcSwapElm.getSource().getErrorMessage() == null && upcSwapElm.getDestination().getErrorMessage() == null) {
						try {
							this.productManagementServiceClient.submitUpcSwap(upcSwapElm);
							upcSwapElm.setStatusMessage(STRING_SUCCESS);
						} catch (CheckedSoapException e) {
							if (i == 0 && upcSwapElm.isMakeDestinationPrimaryUpc()) {
								primaryHasError = true;
							}
							upcSwapElm.setErrorFound(true);
							upcSwapElm.getDestination().setErrorMessage(e.getMessage());
							upcSwapElm.setStatusMessage(e.getMessage());
						}
					} else {
						if (i == 0 && upcSwapElm.isMakeDestinationPrimaryUpc()) {
							primaryHasError = true;
						}
						if (upcSwapElm.getSource().getErrorMessage() != null) {
							statusMessage = statusMessage.concat(upcSwapElm.getSource().getErrorMessage());
							upcSwapElm.setErrorFound(true);
						}
						if (upcSwapElm.getDestination().getErrorMessage() != null) {
							statusMessage = statusMessage.concat(upcSwapElm.getDestination().getErrorMessage());
							upcSwapElm.setErrorFound(true);
						}
						upcSwapElm.setStatusMessage(statusMessage);
					}
					i++;
				}else{
					upcSwapElm.setErrorFound(true);
					upcSwapElm.setStatusMessage(NOT_ATTEMPTED_MESSAGE);
				}
			}
			returnList.addAll(upcSwapUtils.getUpdatedAssociatedUpcList(upcSwapListRet));
		}

		return returnList;
	}

	/**
	 * This method creates additional upc swaps from associates that the user declared as additional upc's to send,
	 * when the user entered a primary upc as the source upc.
	 *
	 * @param upcSwapList original upc swap list
	 * @return updated upc swap list
	 */
	private List<UpcSwap> createAdditionalSwaps(List<UpcSwap> upcSwapList) {
		List<UpcSwap> updatedList = new ArrayList<>();
		for(UpcSwap swap : upcSwapList){
			if(!swap.isSourcePrimaryUpc()){
				if(swap.getDestinationPrimaryUpcSelected() != null &&
						swap.getDestinationPrimaryUpcSelected().equals(swap.getSourceUpc())){
					swap.setMakeDestinationPrimaryUpc(true);
				}
				updatedList.add(swap);
			} else {
				switch (swap.getPrimarySelectOption()){
					case JUST_PRIMARY: {
						if(swap.getDestinationPrimaryUpcSelected() != null &&
								swap.getDestinationPrimaryUpcSelected().equals(swap.getSourceUpc())){
							swap.setMakeDestinationPrimaryUpc(true);
						} else {
							swap.setMakeDestinationPrimaryUpc(false);
						}
						updatedList.add(swap);
						break;
					}
					case PRIMARY_AND_ASSOCIATES: {
						this.createAssociateSwaps(swap, updatedList);
						if(swap.getDestinationPrimaryUpcSelected() != null &&
								swap.getDestinationPrimaryUpcSelected().equals(swap.getSourceUpc())){
							swap.setMakeDestinationPrimaryUpc(true);
						} else {
							swap.setMakeDestinationPrimaryUpc(false);
							if(swap.getDestinationPrimaryUpcSelected() != null) {
								swap.setDestinationPrimaryUpc(swap.getDestinationPrimaryUpcSelected());
							}
						}
						updatedList.add(swap);
						break;
					}
					case JUST_ASSOCIATES: {
						this.createAssociateSwaps(swap, updatedList);
						break;
					}
				}
			}
		}
		return updatedList;
	}

	/**
	 * This method creates additional upc swaps from associates that the user declared as additional upc's to send,
	 * when the user entered a primary upc as the source upc.
	 *
	 * @param swap original upc swap
	 * @return updated upc swap list
	 */
	private List<UpcSwap> createAdditionalSwapsForAlternatePack(UpcSwap swap) {
		List<UpcSwap> updatedList = new ArrayList<>();
		Long newSourceUpc = null;
		UpcSwap newSwap = (UpcSwap) SerializationUtils.clone(swap);
		if(swap.getPrimarySelectOption().equals(UpcSwap.PrimarySelectOption.PRIMARY_AND_ASSOCIATES)) {
			//primary move to primary(for case sent primary UPC from source make to primary upc for destination)
			if (swap.getDestinationPrimaryUpcSelected() != null &&
					swap.getDestinationPrimaryUpcSelected().equals(swap.getSourceUpc())) {
				newSwap.setMakeDestinationPrimaryUpc(true);
				if (swap.getSelectSourcePrimaryUpc() == null) {
					newSwap.setSelectSourcePrimaryUpc(swap.getAdditionalUpcList().get(0));
					newSourceUpc = swap.getAdditionalUpcList().get(0);
					swap.setSourceUpc(newSourceUpc);
					swap.getAdditionalUpcList().remove(0);
				}else{
					swap.setSourceUpc(swap.getSelectSourcePrimaryUpc());
				}
			} else {
				newSwap.setMakeDestinationPrimaryUpc(false);
			}
			updatedList.add(newSwap);
		}
		//Associate move to Associate
		this.createAssociateSwapsForAlternatePack(swap, updatedList);
		//primary move to alternate
		if(newSourceUpc != null){
			newSwap = (UpcSwap) SerializationUtils.clone(swap);
			newSwap.setSourcePrimaryUpc(true);
			newSwap.setSelectSourcePrimaryUpc(null);
			if(swap.getDestinationPrimaryUpcSelected() != null) {
				newSwap.setDestinationPrimaryUpc(swap.getDestinationPrimaryUpcSelected());
			}
			updatedList.add(newSwap);
		}
		return updatedList;
	}

	/**
	 * This method clones the primary upc swap, then sets necessary attributes.
	 *
	 * @param primarySwap primary upc swap
	 * @param updatedList list of upc swaps
	 */
	private void createAssociateSwaps(UpcSwap primarySwap, List<UpcSwap> updatedList){
		UpcSwap newSwap;
		for(Long associateUpc : primarySwap.getAdditionalUpcList()) {
			newSwap = (UpcSwap) SerializationUtils.clone(primarySwap);
			newSwap.setSourceUpc(associateUpc);
			newSwap.setSourcePrimaryUpc(false);
			newSwap.setSelectSourcePrimaryUpc(primarySwap.getSourceUpc());
			if(primarySwap.getDestinationPrimaryUpcSelected() != null &&
					primarySwap.getDestinationPrimaryUpcSelected().equals(associateUpc)){
				newSwap.setMakeDestinationPrimaryUpc(true);
			} else {
				newSwap.setMakeDestinationPrimaryUpc(false);
			}
			updatedList.add(newSwap);
		}
	}

	/**
	 * This method clones the primary upc swap, then sets necessary attributes.
	 *
	 * @param primarySwap primary upc swap
	 * @param updatedList list of upc swaps
	 */
	private void createAssociateSwapsForAlternatePack(UpcSwap primarySwap, List<UpcSwap> updatedList){
		UpcSwap newSwap;
		for(Long associateUpc : primarySwap.getAdditionalUpcList()) {
			newSwap = (UpcSwap) SerializationUtils.clone(primarySwap);
			newSwap.setSourceUpc(associateUpc);
			newSwap.setSourcePrimaryUpc(false);
			newSwap.setSelectSourcePrimaryUpc(primarySwap.getSourceUpc());
			if(primarySwap.getDestinationPrimaryUpcSelected() != null &&
					primarySwap.getDestinationPrimaryUpcSelected().equals(associateUpc)){
				newSwap.setMakeDestinationPrimaryUpc(true);
			} else {
				newSwap.setMakeDestinationPrimaryUpc(false);
				if(primarySwap.getDestinationPrimaryUpcSelected() != null) {
					newSwap.setDestinationPrimaryUpc(primarySwap.getDestinationPrimaryUpcSelected());
				}
			}
			updatedList.add(newSwap);
		}
	}

	/**
	 * Find all warehouse swap list.
	 *
	 * @param upcSwapList the list of upc swaps
	 * @return the list
	 */
	public List<UpcSwap> findAllWarehouseSwap(List<UpcSwap> upcSwapList) {
		return upcSwapUtils.findAllWarehouseSwaps(upcSwapList);
	}

	/**
	 * Update whs to whs swap list.
	 *
	 * @param whsToWhsSwapList the whs to whs swap list
	 * @return the list
	 */
	public List<UpcSwap> submitWarehouseToWarehouseSwap(List<UpcSwap> whsToWhsSwapList) {

		String statusMessage;
		List<UpcSwap> returnList = new ArrayList<>();

		for (UpcSwap upcSwap : whsToWhsSwapList) {
			statusMessage = StringUtils.EMPTY;
			if (upcSwap.getSource().getErrorMessage() == null && upcSwap.getDestination().getErrorMessage() == null) {
				try {
					this.productManagementServiceClient.submitWhsToWhsSwap(upcSwap);
                    upcSwap.setStatusMessage(STRING_SUCCESS);
				} catch (CheckedSoapException e) {
					upcSwap.setErrorFound(true);
					upcSwap.setStatusMessage(e.getMessage());
					upcSwap.getDestination().setErrorMessage(e.getMessage());
				}
			} else {
				if (upcSwap.getSource().getErrorMessage() != null) {
					statusMessage = statusMessage.concat(upcSwap.getSource().getErrorMessage());
					upcSwap.setErrorFound(true);
				}
				if (upcSwap.getDestination().getErrorMessage() != null) {
					statusMessage = statusMessage.concat(upcSwap.getDestination().getErrorMessage());
					upcSwap.setErrorFound(true);
				}
				upcSwap.setStatusMessage(statusMessage);
			}
		}

		returnList.addAll(upcSwapUtils.getUpdatedAssociatedUpcList(whsToWhsSwapList));

		return returnList;
	}

	/*
	 * These functions are to be used for support in testing.
	 */

	/**
	 * Sets the UpcSwapUtils.
	 *
	 * @param utils the UpcSwapUtils.
	 */
	public void setUpcSwapUtils(UpcSwapUtils utils){
		this.upcSwapUtils = utils;
	}

	/**
	 * Sets the ProductManagementServiceClient.
	 *
	 * @param productManagementServiceClient the ProductManagementServiceClient.
	 */
	public void setProductManagementServiceClient(ProductManagementServiceClient productManagementServiceClient){
		this.productManagementServiceClient = productManagementServiceClient;
	}
}
