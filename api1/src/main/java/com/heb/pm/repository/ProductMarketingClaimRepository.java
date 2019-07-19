/*
 * ProductMarketingClaimRepository
 *
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
package com.heb.pm.repository;

import com.heb.pm.entity.ProductMarketingClaim;
import com.heb.pm.entity.ProductMarketingClaimKey;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository to retrieve information about a Product Marketing Claim.
 *
 * @author vn87351
 * @since 2.12.0
 */
public interface ProductMarketingClaimRepository extends JpaRepository<ProductMarketingClaim, ProductMarketingClaimKey> {
	/**
	 * find first ProductMarketingClaim by product id
	 * @param productId
	 * @return
	 */
	ProductMarketingClaim findFirstByKeyProdId(long productId);

	/**
	 * Find a Product Marketing Claim by product id and marketing claim code.
	 * @param productId - The product id.
	 * @param marketingClaimCode - The marketing claim code.
	 * @return ProductMarketingClaim
	 */
	ProductMarketingClaim findFirstByKeyProdIdAndKeyMarketingClaimCode(long productId, String marketingClaimCode);
}