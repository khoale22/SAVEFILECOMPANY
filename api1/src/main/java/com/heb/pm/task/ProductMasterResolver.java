/*
 * ProductInfoResolver
 *
 *  Copyright (c) 2018 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.task;

import com.heb.pm.entity.*;
import com.heb.pm.taxCategory.TaxCategoryService;
import com.heb.pm.user.UserService;
import com.heb.pm.ws.CheckedSoapException;
import com.heb.util.jpa.LazyObjectResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Resolves ProductMaster objects to send them fully-populated back from the REST endpoint.
 *
 * @author vn87351
 * @since 2.17.0
 */
@Service
public class ProductMasterResolver implements LazyObjectResolver<ProductMaster> {

	@Autowired
	private UserService userService;

	/**
	 * Resolves a ProductMaster object.
	 *
	 * @param productMaster The ProductMaster to resolve.
	 */
	@Override
	public void fetch(ProductMaster productMaster) {
		if (productMaster == null) {
			return;
		}
		if(productMaster.getProductDescriptions() != null && productMaster.getProductDescriptions().size() > 0) {
			productMaster.getProductDescriptions().size();
			for (ProductDescription description : productMaster.getProductDescriptions()) {
				description.getKey();
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

		productMaster.getSubDepartment().getKey();
		productMaster.getClassCommodity().getKey();
		productMaster.getItemClass().getItemClassCode();

		// If you need eBM, then use this class a a manged bean and not as a standard object.
		if (this.userService != null) {
			User user = this.userService.getUserById(productMaster.getClassCommodity().geteBMid());
			if (user != null) {
				productMaster.setEbmName(String.format("%s [%s]", user.getFullName(), user.getUid()));
			} else {
				productMaster.setEbmName(String.format("[%s]", productMaster.getClassCommodity().geteBMid()));
			}
		}
		
		if(productMaster.getProductOnlines() != null) {
			for(ProductOnline productOnline : productMaster.getProductOnlines()) {
				productOnline.getKey().getProductId();
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
	}

}
