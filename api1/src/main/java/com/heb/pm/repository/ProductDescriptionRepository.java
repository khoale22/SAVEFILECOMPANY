/*
 * ProductDescriptionRepository
 *
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
package com.heb.pm.repository;

import com.heb.pm.entity.ProductDescription;
import com.heb.pm.entity.ProductDescriptionKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * JPA Repository for ProductDescriptionRepository.
 *
 * @author vn70516
 * @since 2.14.0
 */
public interface ProductDescriptionRepository extends JpaRepository<ProductDescription, ProductDescriptionKey>{

	List<ProductDescription> findByKeyProductIdAndAndKeyDescriptionTypeInAndKeyLanguageType(Long productId,
																							List<String> descriptionType,
																							String languageType);
}
