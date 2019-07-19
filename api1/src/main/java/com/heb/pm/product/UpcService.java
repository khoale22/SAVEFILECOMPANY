/*
 * UpcService
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.product;

import com.heb.pm.entity.*;
import com.heb.pm.productDetails.product.ProductInformationService;
import com.heb.pm.productDetails.sellingUnit.ItemMasterService;
import com.heb.pm.repository.*;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Holds business logic related to upc.
 *
 * @author m314029
 * @since 2.0.3
 */
@Service
public class UpcService {

	@Autowired
	private SellingUnitRepository sellingUnitRepository;

	@Autowired
	private CandidateSellingUnitRepository candidateRepository;

	@Autowired
	private AssociatedUpcRepository associatedUpcRepository;

	@Autowired
	private RetailLinkRepository retailLinkRepository;

	@Autowired
	private ProductInformationService productInformationService;

	@Autowired
	private ItemMasterService ItemMasterService;

	/**
	 * Searches for a selling unit record based on a upc.
	 *
	 * @param upc The upc to search for records on.
	 * @return A selling unit record based on search criteria.
	 */
	public SellingUnit find(Long upc){

		return this.sellingUnitRepository.findOne(upc);
	}


	/**
	 * Sets the upc info object data.
	 *
	 * @param upc the upc to get info on.
	 * @return the updated Upc object.
	 */
	public Upc getUpcInfo(Long upc) {

		Upc upcInfo = this.setUpcStatus(upc);
		if(upcInfo == null) {
			return null;
		}
		upcInfo.setSearchedUpc(upc);
		PrimaryUpc primaryUpc = this.associatedUpcRepository.findOne(upc).getPrimaryUpc();
		upcInfo.setScanCodeId(primaryUpc.getUpc());
		RetailLink retailLink = this.retailLinkRepository.findOne(primaryUpc.getUpc());
		if(retailLink != null) {
			upcInfo.setRetailLink(retailLink.getRetailLinkCd());
		}
		SellingUnit sellingUnit = this.sellingUnitRepository.findOne(primaryUpc.getUpc());
		PriceDetail priceDetail = this.productInformationService.getPriceInformation(primaryUpc.getUpc());

		upcInfo.setSize(sellingUnit.getTagSizeDescription())
				.setXFor(priceDetail.getxFor())
				.setRetailPrice(priceDetail.getRetailPrice())
				.setWeightSw(priceDetail.getWeight());
		// remove the primary upc from the associates upc list.
		upcInfo.setAssociatedUpcs(primaryUpc.getAssociateUpcs().stream().filter(associate -> associate.getUpc() !=
				primaryUpc.getUpc()).map(AssociatedUpc::getUpc).collect(Collectors.toList()));

		ProductMaster productMaster = sellingUnit.getProductMaster();
		upcInfo.setProduct(new Product());
		upcInfo.getProduct()
				.setProductDescription(productMaster.getDescription());
		List<ItemMaster> itemMasters = this.ItemMasterService.findByKeyItemTypeAndOrderingUpc(ItemMasterKey.WAREHOUSE,
				primaryUpc.getUpc());
		if(CollectionUtils.isNotEmpty(itemMasters) && itemMasters.size() > 1) {
			throw new IllegalArgumentException("More than one ItemMaster for given upc.");
		} else if (CollectionUtils.isEmpty(itemMasters)) {
			//TODO:
			return upcInfo;
		}
		ItemMaster itemMaster = itemMasters.get(0);
		upcInfo.setItem(new Item()
				.setItemType(ItemMasterKey.WAREHOUSE)
				.setItemCode(itemMaster.getKey().getItemCode())
				.setCommodity(itemMaster.getCommodityCode())
				.setSubCommodity(itemMaster.getSubCommodityCode()));
		return upcInfo;
	}

	/**
	 * Searches for the status of a UPC.
	 *
	 * @param upc The upc to search for statuses on.
	 * @return A UPC-Status key value pair via a custom Object.
	 */
	public Upc setUpcStatus(Long upc) {
		Upc upcInfo = new Upc();
		SellingUnit sellingUnit=sellingUnitRepository.findOne(upc);
		upcInfo.setScanCodeId(upc);
		if(sellingUnit!=null)
		{
			boolean isDateNull=(sellingUnit.getDiscontinueDate() == null);
			upcInfo.setProdStatus(isDateNull);
			return upcInfo;
		}

		List<CandidateSellingUnit> candidateSellingUnits=candidateRepository.findAllByUpcOrderByLastUpdatedOnDesc(upc);

		if(CollectionUtils.isNotEmpty(candidateSellingUnits)) {
			int index = 0;
			boolean isDeleted = true;
			for(int x = 0; x < candidateSellingUnits.size(); x++) {
				if(!candidateSellingUnits.get(x).
						getCandidateProductMaster().
						getCandidateWorkRequest().getStatus().equalsIgnoreCase(Upc.CANDIDATE_DELETED)) {
					index = x;
					isDeleted = false;
					break;
				}
			}
			if(isDeleted) {
				return null;
			}
			upcInfo.setCandidateStatusByCode(candidateSellingUnits.get(index).
					getCandidateProductMaster().
					getCandidateWorkRequest().getStatus());
			return upcInfo;
		}
		return null;
	}

	public void setRepository(SellingUnitRepository repository) {
		this.sellingUnitRepository = repository;
	}
}
