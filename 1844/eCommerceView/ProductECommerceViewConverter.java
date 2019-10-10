/*
 *  ProductECommerceViewConverter
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.productDetails.product.eCommerceView;


import com.heb.pm.entity.*;
import org.apache.commons.lang.StringUtils;

import java.util.Comparator;
import java.util.List;

/**
 *  Converts Data objects to either object to sent to font end.
 *
 * @author vn70516
 * @since 2.0.14
 */
class ProductECommerceViewConverter {

    /**
     * Convert to ECommerceViewAttributePriorities from MasterDataExtensionAttribute
     * @param masterDataExtensionAttribute - The master data extension attribute.
     * @return ECommerceViewAttributePriorities
     */
    public static ECommerceViewAttributePriorities
    convertToECommerceViewAttributePrioritiesFromMasterDataExtensionAttribute
            (MasterDataExtensionAttribute masterDataExtensionAttribute){
    	ECommerceViewAttributePriorities eCommerceViewAttributePriorities = new ECommerceViewAttributePriorities();
    	eCommerceViewAttributePriorities.setContent(masterDataExtensionAttribute.getAttributeValueText());
    	eCommerceViewAttributePriorities.setSourceSystemId(masterDataExtensionAttribute.getKey().getDataSourceSystem());
		eCommerceViewAttributePriorities.setPhysicalAttributeId(masterDataExtensionAttribute.getKey().getAttributeId());
		if(!StringUtils.isEmpty(masterDataExtensionAttribute.getAttributeValueText())){
			eCommerceViewAttributePriorities.setContent(masterDataExtensionAttribute.getAttributeValueText().trim());
		}
		eCommerceViewAttributePriorities.setCreatedDate(masterDataExtensionAttribute.getLstUpdtTs());
        return eCommerceViewAttributePriorities;
    }
	/**
	 * Convert the list of ProductScanCodeExtents to ECommerceViewAttributePriorities for Gladson(7).
	 *
	 * @param productScanCodeExtents the list of ECommerceViewAttributePriorities.
	 * @return the ECommerceViewAttributePriorities object.
	 */
	public static ECommerceViewAttributePriorities convertToECommerceViewAttributePrioritiesFromGladson(List<ProductScanCodeExtent> productScanCodeExtents){
		ECommerceViewAttributePriorities eCommerceViewAttributePriorities = new ECommerceViewAttributePriorities();
		StringBuilder productDescriptionBuilder = new StringBuilder();
		if(productScanCodeExtents != null && !productScanCodeExtents.isEmpty()){
			for (ProductScanCodeExtent productScanCodeExtent: productScanCodeExtents) {
				if(!StringUtils.isEmpty(productScanCodeExtent.getProdDescriptionText())){
					productDescriptionBuilder.append(productScanCodeExtent.getProdDescriptionText());
					productDescriptionBuilder.append(" ");
				}
			}
			eCommerceViewAttributePriorities.setCreatedDate(productScanCodeExtents.stream()
					.filter(productScanCodeExtent -> productScanCodeExtent.getLstUpdtTs() != null)
					.map(ProductScanCodeExtent::getLstUpdtTs)
					.max(Comparator.naturalOrder()).orElse(null));
		}
		eCommerceViewAttributePriorities.setContent(productDescriptionBuilder.toString().trim());
		return eCommerceViewAttributePriorities;
	}
	/**
	 * Convert the list of ProductScanCodeExtents to ECommerceViewAttributePriorities for ProductManagement(4).
	 *
	 * @param productScanCodeExtents the list of ECommerceViewAttributePriorities.
	 * @return the ECommerceViewAttributePriorities object.
	 */
	public static ECommerceViewAttributePriorities convertToECommerceViewAttributePrioritiesFromProductManagement(List<ProductScanCodeExtent> productScanCodeExtents){
		ECommerceViewAttributePriorities eCommerceViewAttributePriorities = new ECommerceViewAttributePriorities();
		if(productScanCodeExtents != null && !productScanCodeExtents.isEmpty()){
			if(!StringUtils.isEmpty(productScanCodeExtents.get(0).getProdDescriptionText())) {
				eCommerceViewAttributePriorities.setContent(productScanCodeExtents.get(0).getProdDescriptionText().trim());
				eCommerceViewAttributePriorities.setCreatedDate(productScanCodeExtents.get(0).getLstUpdtTs());
			}
		}
		return eCommerceViewAttributePriorities;
	}
}