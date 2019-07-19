/*
 *  ProductPanelTypeRepository
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.repository;

import com.heb.pm.entity.ProductPanelType;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * JPA repository for ProductPanelType.
 *
 * @author vn70516
 * @since 2.14.0
 */
public interface ProductPanelTypeRepository extends JpaRepository<ProductPanelType, String> {
}
