/*
 * ProductInfoResolver
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.product;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import com.heb.pm.entity.*;
import com.heb.pm.taxCategory.TaxCategoryService;
import com.heb.pm.user.UserService;
import com.heb.pm.ws.CheckedSoapException;
import com.heb.util.jpa.LazyObjectResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Resolves ProductMaster objects to send them fully-populated back from the REST endpoint.
 *
 * @author d116773
 * @since 2.0.1
 */
@Service
public class ProductInfoResolver implements LazyObjectResolver<ProductMaster> {

	private static final String RELATED_ITEM_KEY_TYPE_ITMCD = "ITMCD";
	private static final String ITEM_TYPE_DSD = "DSD";
	private static final String ITEM_RELATIONSHIP_TYPE_MORPH = "MORPH";
	private static final Logger logger = LoggerFactory.getLogger(ProductInfoResolver.class);

	@Autowired
	private TaxCategoryService taxCategoryService;

	@Autowired
	private UserService userService;

	/**
	 * Resolves a ProductMaster object. It will load the following properties:
	 * 1. prodItems
	 * 2. prodItems->itemMaster
	 * For Warehouse items only:
	 * 3. prodItems->itemMaster->primaryUpc
	 * 4. prodItems->itemMaster->primaryUpc->associateUpcs
	 * 5. prodItems->itemMaster->primaryUpc->shipper
	 * 6. prodItems->itemMaster->primaryUpc->shipper->realUpc
	 * 7. prodItems->itemMaster->primaryUpc->shipper->realUpc->associateUpcs
	 * 8. prodItems->itemMaster->primaryUpc->associateUpcs->sellingUnit
	 * 9. prodItems->itemMaster->warehouseLocationItems
	 * 10. prodItems->itemMaster->warehouseLocationItemExtendedAttributes
	 * 11. goodsProduct
	 * 12. RxProduct
	 * 13. RxProduct->DrugScheduleType
	 * 14. ClassCommodity
	 * 15. ItemClass
	 * 16. productFulfillmentChannel
	 * 17. dynamic attributes
	 *
	 * @param productMaster The ProductMaster to resolve.
	 */
	@Override
	public void fetch(ProductMaster productMaster) {
		if (productMaster == null) {
			return;
		}

		productMaster.getProdItems().size();

		// For DSD product, stop at the item master.
		productMaster.getProdItems().forEach((p) -> {
			this.getRemainingProductInformation(p.getItemMaster());
			if (p.getItemMaster().getKey().isWarehouse()) {
				this.fetchWarehouseData(p.getItemMaster());
			} else {
				this.fetchMerchandiseTypes(p.getItemMaster());
			}
		});
		productMaster.getSellingUnits().size();
		for (SellingUnit s : productMaster.getSellingUnits()) {
			s.getNutritionalClaims().size();
		}
		if(productMaster.getProductDescriptions() != null && productMaster.getProductDescriptions().size() > 0) {
			productMaster.getProductDescriptions().size();
			for (ProductDescription description : productMaster.getProductDescriptions()) {
				description.getKey();
			}
		}
		productMaster.getProductMarketingClaims().size();
		this.initializeMarketingClaimsToFalse(productMaster);
		if(productMaster.getProductMarketingClaims() != null && productMaster.getProductMarketingClaims().size() > 0) {
			for (ProductMarketingClaim productMarketingClaim : productMaster.getProductMarketingClaims()) {
				productMarketingClaim.getKey().getProdId();
				productMarketingClaim.getMarketingClaim().getMarketingClaimCode();
				this.setMarketingClaimTrueValue(productMaster, productMarketingClaim);
			}
		}
		if(productMaster.getClassCommodity() != null){
			productMaster.getClassCommodity().getDisplayName();
		}
		if(productMaster.getClassCommodity().getItemClassMaster() != null){
			productMaster.getClassCommodity().getItemClassMaster().getDisplayName();
		}
		if(productMaster.getProductBrand() != null){
			productMaster.getProductBrand().getProductBrandDescription();
		}

		productMaster.getProductCubiscan().size();
		productMaster.getSubDepartment().getKey();
		if(productMaster.getGoodsProduct() != null && productMaster.getGoodsProduct().getRxProduct() != null){
			RxProduct rxProduct = productMaster.getGoodsProduct().getRxProduct();
			rxProduct.getId();
			if(rxProduct.getDrugScheduleType() != null){
				rxProduct.getDrugScheduleType().getId();
			}
		}
		productMaster.getClassCommodity().getKey();
		productMaster.getItemClass().getItemClassCode();
		if(productMaster.getRestrictions() != null){
			for (ProductRestrictions restriction: productMaster.getRestrictions()) {
				restriction.getKey().getRestrictionCode();
				restriction.getRestriction().getRestrictionDescription();
				if(restriction.getRestriction().getSellingRestriction() != null) {
					restriction.getRestriction().getSellingRestriction().getRestrictionDescription();
				}
			}
		}

		// If you need eBM, then use this class a a manged bean and not as a standard object.
		if (this.userService != null) {
			User user = this.userService.getUserById(productMaster.getClassCommodity().geteBMid());
			if (user != null) {
				productMaster.setEbmName(String.format("%s [%s]", user.getFullName(), user.getUid()));
			} else {
				productMaster.setEbmName(String.format("[%s]", productMaster.getClassCommodity().geteBMid()));
			}
		}


		productMaster.getDistributionFilters().size();

		if (productMaster.getGoodsProduct() != null && productMaster.getGoodsProduct().getVertexTaxCategoryCode()!= null && this.taxCategoryService != null) {
			// This is considered not-critical data, so just eat the error and move on.
			try {
				productMaster.getGoodsProduct().setVertexTaxCategory(
						this.taxCategoryService.fetchOneTaxCategory(productMaster.getGoodsProduct().getVertexTaxCategoryCode()));
			} catch (CheckedSoapException e) {
				ProductInfoResolver.logger.error(e.getMessage());
			}
		}
		if(productMaster.getTierPricings() != null && productMaster.getTierPricings().size() > 0) {
			for(TierPricing tierPricing : productMaster.getTierPricings()) {
				tierPricing.getKey();
			}
		}

		if(productMaster.getMasterDataExtensionAttributes() != null) {
			productMaster.getMasterDataExtensionAttributes().size();
			for(MasterDataExtensionAttribute masterDataExtensionAttribute : productMaster.getMasterDataExtensionAttributes()) {
				if(masterDataExtensionAttribute.getEntityAttribute() != null) {
					masterDataExtensionAttribute.getEntityAttribute().size();
				}
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

		if(productMaster.getTemperatureControl() != null) {
				productMaster.getTemperatureControl().getDescription();
		}

		if(productMaster.getProductFullfilmentChanels() != null){
			for(ProductFullfilmentChanel fullfilmentChanel: productMaster.getProductFullfilmentChanels()){
				fullfilmentChanel.getKey().getSalesChanelCode();
			}
		}

		if(productMaster.getMasterDataExtensionAttributes() != null){
			for(MasterDataExtensionAttribute masterDataExtensionAttribute: productMaster.getMasterDataExtensionAttributes()){
				masterDataExtensionAttribute.getKey().getAttributeId();
			}
		}

		if (this.userService != null) {
			User user = this.userService.getUserById(productMaster.getCreateUid());
			if (user != null) {
				productMaster.setDisplayCreatedName(user.getDisplayName());
			} else {
				productMaster.setDisplayCreatedName(productMaster.getCreateUid());
			}
		}

		if (this.userService != null) {
			for(ProdItem prodItem: productMaster.getProdItems()){
				User user = this.userService.getUserById(prodItem.getItemMaster().getAddedUsrId());
				if (user != null) {
					prodItem.getItemMaster().setDisplayCreatedName(user.getDisplayName());
				} else {
					prodItem.getItemMaster().setDisplayCreatedName(prodItem.getItemMaster().getAddedUsrId());
				}
			}

		}
		if(productMaster.getProductOnlines() != null) {
			for(ProductOnline productOnline : productMaster.getProductOnlines()) {
				productOnline.getKey().getProductId();
			}
		}
	}

	/**
	 * This method sets all marketing claim values on a product master to false. These values are Booleans, so before
	 * doing this all of these values are null. If this product master was not resolved, they would stay as null.
	 * Since this product master is being resolved, they are initialized to false. If the product master does have a
	 * particular marketing claim code, the value will be set to true when the product master resolves its product
	 * marketing claims.
	 *
	 * @param productMaster Product master being resolved.
	 */
	public void initializeMarketingClaimsToFalse(ProductMaster productMaster) {
		productMaster.setDistinctive(false);
		productMaster.setPrimoPick(false);
		productMaster.setOwnBrand(false);
		productMaster.setGoLocal(false);
		productMaster.setTotallyTexas(false);
		productMaster.setSelectIngredients(false);
	}

	/**
	 * This method sets the appropriate marketing claim value on product master to true, based on the given
	 * marketing claim's marketing claim code.
	 *
	 * @param productMaster Product master being resolved.
	 * @param productMarketingClaim Product marketing claim currently being looked at.
	 */
	public void setMarketingClaimTrueValue(ProductMaster productMaster, ProductMarketingClaim productMarketingClaim) {
		if(productMarketingClaim.getKey().getMarketingClaimCode().trim().equals(
				MarketingClaim.Codes.DISTINCTIVE.getCode())){
			productMaster.setDistinctive(true);
		} else if(productMarketingClaim.getKey().getMarketingClaimCode().trim().equals(MarketingClaim.Codes.PRIMO_PICK.getCode())
				&& productMarketingClaim.getMarketingClaimStatusCode().equals(ProductMarketingClaim.APPROVED.trim())){
			LocalDate today =  LocalDate.now();
			if (productMarketingClaim.getPromoPickEffectiveDate() != null && productMarketingClaim.getPromoPickExpirationDate() != null){
				if ((today.isAfter(productMarketingClaim.getPromoPickEffectiveDate()) || today.isEqual(productMarketingClaim.getPromoPickEffectiveDate())) &&
						(today.isBefore(productMarketingClaim.getPromoPickExpirationDate()) || today.isEqual(productMarketingClaim.getPromoPickExpirationDate())))
				{
					productMaster.setPrimoPick(true);
				}
			}
		} else if(productMarketingClaim.getKey().getMarketingClaimCode().trim().equals(
				MarketingClaim.Codes.OWN_BRAND.getCode())){
			productMaster.setOwnBrand(true);
		} else if(productMarketingClaim.getKey().getMarketingClaimCode().trim().equals(
				MarketingClaim.Codes.GO_LOCAL.getCode())){
			productMaster.setGoLocal(true);
		} else if(productMarketingClaim.getKey().getMarketingClaimCode().trim().equals(
				MarketingClaim.Codes.TOTALLY_TEXAS.getCode())){
			productMaster.setTotallyTexas(true);
		} else if(productMarketingClaim.getKey().getMarketingClaimCode().trim().equals(
				MarketingClaim.Codes.SELECT_INGREDIENTS.getCode())){
			productMaster.setSelectIngredients(true);
		}
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

	/**
	 * Resolves all of the merchandies types. JPA needs to be able to find a merchandise type code and the jpa resolver
	 * fails whenever the field is empty(not null). i.e. If the database doesn't have a pss department it is saved as ' '.
	 * This will trim all of the whitespace and then check to see if it is empty. This is lazily loaded because it should
	 * only be resolved for dsd items.
	 *
	 * @param im The item to fetch merchandise types for.
	 */
	private void fetchMerchandiseTypes(ItemMaster im) {
		if(!im.getMerchandiseTypeCodeOne().toString().trim().isEmpty()) {
			im.getMerchandiseTypeOne().getId();
		}

		if(!im.getMerchandiseTypeCodeTwo().toString().trim().isEmpty()) {
			im.getMerchandiseTypeTwo().getId();
		}

		if(!im.getMerchandiseTypeCodeThree().toString().trim().isEmpty()) {
			im.getMerchandiseTypeThree().getId();
		}

		if(!im.getMerchandiseTypeCodeFour().toString().trim().isEmpty()) {
			im.getMerchandiseTypeFour().getId();
		}
	}
}
