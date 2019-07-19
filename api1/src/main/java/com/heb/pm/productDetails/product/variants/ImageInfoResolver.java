/*
 *
 * ImageInfoResolver.java
 *
 * Copyright (c) 2017 HEB
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of HEB.
 *
 */
package com.heb.pm.productDetails.product.variants;

import com.heb.pm.entity.ImageMetaData;
import com.heb.pm.entity.ProductScanImageBanner;
import com.heb.pm.entity.ProductScanImageURI;
import com.heb.pm.entity.SalesChannel;
import com.heb.util.jpa.LazyObjectResolver;
/**
 * fetch lazy data for ProductScanImageURI.
 *
 * @author vn87351
 * @since 2.16.0
 */
public class ImageInfoResolver implements LazyObjectResolver<ProductScanImageURI> {

	/**
	 * Currently this fetch is getting:
	 * 1. the key
	 * 2. the Image type
	 * 3. the Image Category
	 * 4. the Image Status
	 * 5. the Image Priority
	 * 6. the product scan image banner list
	 * 7. the source system
	 * 8. the image source
	 * 9. the image metadata
	 * 10. the image destinations
	 * @param productScanImageURI
	 */

	@Override
	public void fetch(ProductScanImageURI productScanImageURI) {
		if(productScanImageURI.getKey() != null){
			productScanImageURI.getKey().getSequenceNumber();
		}
		if(productScanImageURI.getImageCategory() != null){
			productScanImageURI.getImageCategory().getId();
		}
		if(productScanImageURI.getImageType() != null){
			productScanImageURI.getImageType().getId();
		}
		if(productScanImageURI.getImageStatus() !=null){
			productScanImageURI.getImageStatus().getAbbreviation();
		}
		if(productScanImageURI.getImagePriority() !=null){
			productScanImageURI.getImagePriority().getAbbreviation();
		}
		if(productScanImageURI.getProductScanImageBannerList() !=null){
			for (ProductScanImageBanner banner: productScanImageURI.getProductScanImageBannerList()) {
				banner.getKey().getId();
				if(banner.getSalesChannel() != null){
					banner.getSalesChannel().getAbbreviation();
				}
			}
		}
		if(productScanImageURI.getSourceSystem() !=null){
			productScanImageURI.getSourceSystem().getId();
		}
		if(productScanImageURI.getImageSource() != null){
			productScanImageURI.getImageSource().getAbbreviation();
		}
		if(productScanImageURI.getImageMetaDataList()!=null){
			for (ImageMetaData data: productScanImageURI.getImageMetaDataList()) {
				data.getKey().getId();
				data.getEntity().getAbbreviation();
			}
		}
		if(productScanImageURI.getDestinations().size() >0){
			for (SalesChannel destination: productScanImageURI.getDestinations()) {
				destination.getAbbreviation();
			}
		}
	}
}