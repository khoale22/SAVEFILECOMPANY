/*
 * ProductOnlineAuditRepository
 *
 *  Copyright (c) 2018 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
package com.heb.pm.repository;

import com.heb.pm.entity.ProductOnlineAudit;
import com.heb.pm.entity.ProductOnlineAuditKey;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * JPA repository for Product Online Audit.
 *
 * @author vn86116
 * @since 2.18.4
 */
public interface ProductOnlineAuditRepository extends JpaRepository<ProductOnlineAudit, ProductOnlineAuditKey> {
	/**
	 * Get Product Online Audit by product group id and SaleChannelCode contain space and SaleChannelCode not contain space.
	 * @param productId the prodcut Id
	 * @param salesChannel the sale channel code
	 * @param productId1 the prodcut Id
	 * @param salesChannel1 the sale channel code
	 * @param pageRequest the page request
	 * @return ProductOnlineAudit
	 */
	Page<ProductOnlineAudit> findByKeyProductIdAndKeySalesChannelCodeOrKeyProductIdAndKeySalesChannelCode(Long productId, String salesChannel, Long productId1, String salesChannel1, Pageable pageRequest);
}
