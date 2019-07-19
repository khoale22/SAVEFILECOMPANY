/*
 * ProductLineRepositoryWithCount
 *
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
package com.heb.pm.repository;

import com.heb.pm.entity.ProductLine;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository of the ProductLine entity with counts.
 *
 * @author m314029
 * @since 2.26.0
 */
public interface ProductLineRepositoryWithCount extends JpaRepository<ProductLine, String> {

    /**
     * Find all product sub brands data filter by prod sub brand id.
     *
     * @param id the id to search.
     * @param pageRequest the page request for pagination.
     * @return the page of product sub brands.
     */
    Page<ProductLine> findByIdIgnoreCaseContaining(String id, Pageable pageRequest);

    /**
     * Find all product sub brands data filter by prod sub brand name.
     *
     * @param description the description to search.
     * @param pageRequest the page request for pagination.
     * @return the page of product sub brands.
     */
    Page<ProductLine> findByDescriptionIgnoreCaseContaining(String description, Pageable pageRequest);

    /**
     * Find all product sub brands data filter by prod sub brand name.
     *
     * @param id the id to search.
     * @param description the description to search.
     * @param pageRequest      the page request for pagination.
     * @return the page of product sub brands.
     */
    Page<ProductLine> findByIdIgnoreCaseContainingAndDescriptionIgnoreCaseContaining(String id, String description,
                                                                                     Pageable pageRequest);
}
