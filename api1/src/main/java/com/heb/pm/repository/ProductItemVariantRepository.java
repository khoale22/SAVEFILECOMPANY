/*
 * ProductItemVariantRepository.java
 *
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
package com.heb.pm.repository;

import com.heb.pm.entity.ProductItemVariant;
import com.heb.pm.entity.ProductItemVariantKey;
import com.heb.pm.entity.ProductMarketingClaimKey;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository to retrieve information about a Product Item Variant.
 *
 * @author vn87351
 * @since 2.16.0
 */
public interface ProductItemVariantRepository  extends JpaRepository<ProductItemVariant, ProductItemVariantKey> {
	/**
	 * find product variant item by product id
	 * @param productId
	 * @return
	 */
	ProductItemVariant findAllByKeyProductId (long productId);
}
