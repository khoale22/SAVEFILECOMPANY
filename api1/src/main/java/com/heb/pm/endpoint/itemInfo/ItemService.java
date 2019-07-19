/*
 * ItemInfoService
 *
 *  Copyright (c) 2018 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
package com.heb.pm.endpoint.itemInfo;

import com.heb.pm.entity.Product;
import com.heb.pm.entity.*;
import com.heb.pm.product.UpcService;
import com.heb.pm.productDetails.product.ProductInformationService;
import com.heb.pm.productDetails.sellingUnit.ItemMasterService;
import com.heb.pm.repository.RetailLinkRepository;
import com.heb.pm.repository.SellingUnitRepository;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Holds business logic related to Item info data retrieval.
 *
 * @author s573181
 * @since 2.23.0
 */
@Service
public class ItemService {

	@Autowired
	private ItemMasterService itemMasterService;

	@Autowired
	private SellingUnitRepository sellingUnitRepository;

	@Autowired
	private ProductInformationService productInformationService;

	@Autowired
	private RetailLinkRepository retailLinkRepository;

	@Autowired
	private UpcService upcService;

	/**
	 * Returns warehouse item info by item code.
	 *
	 * @param itemCode the item code.
	 * @return warehouse item info by item code.
	 */
	public Item getItemInfoByItemCode(Long itemCode) {

		ContainedUpc containedUpc = new ContainedUpc();
		Item item = new Item()
				.setItemCode(itemCode)
				.setItemType(ItemMasterKey.WAREHOUSE)
				.setContainedUpc(containedUpc);

		ItemMaster itemMaster = this.itemMasterService.findByItemCodeAndItemKeyTypeCode(itemCode, item.getItemType());
		if(itemMaster == null) {
			return null;
		}
		item.setCommodity(itemMaster.getCommodityCode())
				.setSubCommodity(itemMaster.getSubCommodityCode());

		final PrimaryUpc primaryUpc;
		List<Shipper> shippers = itemMaster.getPrimaryUpc().getShipper();

		// If ordering upc is in shipper, it can be a pre digit 4, so get real primary upc.
		if(CollectionUtils.isNotEmpty(shippers)) {
			//if not an mrt (only one shipper), get the primary upc
			if(shippers.size() == 1) {
				containedUpc.setPack(itemMaster.getPack());
				primaryUpc = shippers.get(0).getPrimaryUpc();
			} else {
				//TODO: handle MRT.
				return null;
			}
		} else {
			containedUpc.setPack(itemMaster.getPack());
			primaryUpc = itemMaster.getPrimaryUpc();
		}
		Upc upc = this.upcService.setUpcStatus(primaryUpc.getUpc());
		upc.setProduct(new Product());
		containedUpc.setUpc(upc);
		//remove the primary upc from the associates upc list.
		upc.setAssociatedUpcs(primaryUpc.getAssociateUpcs().stream().filter(associate -> associate.getUpc() !=
				primaryUpc.getUpc()).map(AssociatedUpc::getUpc).collect(Collectors.toList()));
		SellingUnit sellingUnit = this.sellingUnitRepository.findOne(upc.getScanCodeId());
		upc.getProduct().setProductDescription(sellingUnit.getProductMaster().getDescription());
		RetailLink retailLink = this.retailLinkRepository.findOne(upc.getScanCodeId());

		if(retailLink != null) {
			upc.setRetailLink(retailLink.getRetailLinkCd());
		}
		PriceDetail priceDetail = this.productInformationService.getPriceInformation(primaryUpc.getUpc());
		upc.setScanCodeId(itemMaster.getOrderingUpc())
				.setSize(sellingUnit.getTagSizeDescription())
				.setXFor(priceDetail.getxFor())
				.setRetailPrice(priceDetail.getRetailPrice())
				.setWeightSw(priceDetail.getWeight());

		return item;
	}
}
