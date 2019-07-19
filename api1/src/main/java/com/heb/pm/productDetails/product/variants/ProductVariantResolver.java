/*
 *
 * ProductVariantResolver.java
 *
 * Copyright (c) 2017 HEB
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of HEB.
 *
 */
package com.heb.pm.productDetails.product.variants;

import com.heb.pm.entity.*;
import com.heb.pm.repository.ProductItemVariantRepository;
import com.heb.util.jpa.LazyObjectResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * fetch lazy data for Product Master .
 *
 * @author vn87351
 * @since 2.16.0
 */
@Component
public class ProductVariantResolver implements LazyObjectResolver<ProductMaster> {
	private static final String RELATED_ITEM_KEY_TYPE_ITMCD = "ITMCD";
	private static final String ITEM_TYPE_DSD = "DSD";
	private static final String ITEM_RELATIONSHIP_TYPE_MORPH = "MORPH";


	/**
	 * Resolves a ProductMaster object. It will load the following properties:
	 * 1. prodItems
	 * 2. prodItems->itemMaster
	 * 3. lstProductItemVariant list product variant item
	 *
	 * @param productMaster The ProductMaster to resolve.
	 */
	@Override
	public void fetch(ProductMaster productMaster) {
		productMaster.getProdItems().size();

		// For DSD product, stop at the item master.
		productMaster.getProdItems().forEach((p) -> {
			this.getRemainingProductInformation(p.getItemMaster());
		});

		if(productMaster.getProductDescriptions() != null && productMaster.getProductDescriptions().size() > 0) {
			productMaster.getProductDescriptions().size();
			for (ProductDescription description : productMaster.getProductDescriptions()) {
				description.getKey();
			}
		}
		if(productMaster.getProdItems() != null && productMaster.getProdItems().size() > 0) {
			List<Long> lstItemCode = new ArrayList<Long>();
			for (ProdItem prodItem : productMaster.getProdItems()) {
				lstItemCode.add(prodItem.getItemMaster().getKey().getItemCode());
			}
			for (ProdItem prodItem : productMaster.getProdItems()) {
				if (prodItem.getItemMaster() != null && prodItem.getItemMaster().getRelatedItems() != null
						&& prodItem.getItemMaster().getRelatedItems().size() > 0) {
					RelatedItem relatedItem = prodItem.getItemMaster().getRelatedItems().get(0);
					Long itemCode = relatedItem.getKey().getItemCode();
					String itemType = relatedItem.getKey().getItemType();
					String itemRelationshipType = relatedItem.getItemRelationshipType();
					String relatedItemKeyType = relatedItem.getRelatedItemKeyType();
					if (lstItemCode.contains(itemCode) && ITEM_RELATIONSHIP_TYPE_MORPH.equalsIgnoreCase(itemRelationshipType)
							&& ITEM_TYPE_DSD.equalsIgnoreCase(itemType) && RELATED_ITEM_KEY_TYPE_ITMCD.equalsIgnoreCase(relatedItemKeyType)) {
						prodItem.getItemMaster().setDsdEdcStatus(true);
					}
				}
			}
		}
		productMaster.getLstProductItemVariant().size();

		//productMaster.getProdItemsVariant().size();
	}
	/**
	 * Traverses the UPC portion of the object model for dsd and warehouse products.
	 *
	 * @param im The item to traverse.
	 */
	private void getRemainingProductInformation(ItemMaster im) {

		// Check to make sure the item ties to a real primary UPC. It won't if this is a DSD product that the DSD
		// UPC is an associate to a warehouse item code.
		if (im.getPrimaryUpc() == null) {
			return;
		}
		im.getPrimaryUpc().getAssociateUpcs().size();
		im.getPrimaryUpc().getShipper().size();
		im.getPrimaryUpc().getShipper().forEach((s) -> {
			if (s.getRealUpc() != null ) {
				s.getRealUpc().getAssociateUpcs().size();
			}
		});

		im.getPrimaryUpc().getAssociateUpcs().forEach(AssociatedUpc::getSellingUnit);

	}
	/**
	 * Traverses the UPC portion of the object model for warehouse products.
	 *
	 * @param im The item to traverse.
	 */
	private void fetchWarehouseData(ItemMaster im) {

		im.getWarehouseLocationItems().size();
		im.getWarehouseLocationItemExtendedAttributes().size();
		im.getWarehouseLocationItems().forEach((w) -> {
			if (w.getLocation() != null ) {
				w.getLocation().getKey();
			}
		});

	}
}
