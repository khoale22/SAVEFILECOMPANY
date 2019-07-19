/*
 * ProductLineRepository
 *
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
package com.heb.pm.repository;

import com.heb.pm.entity.ProductLine;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Repository of the ProductLine entity.
 *
 * @author m314029
 * @since 2.26.0
 */
public interface ProductLineRepository extends JpaRepository<ProductLine, String> {

    /**
     * Find all product sub brands data filter by prod sub brand id.
     *
     * @param id the id to search.
     * @param pageRequest the page request for pagination.
     * @return the page of product sub brands.
     */
    List<ProductLine> findByIdIgnoreCaseContaining(String id, Pageable pageRequest);

    /**
     * Find all product sub brands data filter by prod sub brand name.
     *
     * @param description the description to search.
     * @param pageRequest the page request for pagination.
     * @return the page of product sub brands.
     */
    List<ProductLine> findByDescriptionIgnoreCaseContaining(String description, Pageable pageRequest);

    /**
     * Find all product sub brands data filter by prod sub brand name.
     *
     * @param description the description to search.
     * @return the page of product sub brands.
     */
    List<ProductLine> findByDescriptionIgnoreCase(String description);

    /**
     * Finds ProductLine with the highest ID.
     *
     * @return ProductLine with the highest ID.
     */
    ProductLine findTop1ByOrderByIdDesc();

    /**
     * Find all product sub brands data filter by prod sub brand name.
     *
     * @param id the id to search.
     * @param description the description to search.
     * @param pageRequest      the page request for pagination.
     * @return the page of product sub brands.
     */
    List<ProductLine> findByIdIgnoreCaseContainingAndDescriptionIgnoreCaseContaining(String id, String description,
                                                                                     Pageable pageRequest);
    /**
     * Find all product sub brands data filter by prod sub brand name.
     *
     * @param id the id to search.
     * @param description the description to search.
     * @param pageRequest      the page request for pagination.
     * @return the page of product sub brands.
     */
    List<ProductLine> findByIdIgnoreCaseContainingOrDescriptionIgnoreCaseContaining(String id, String description,
                                                                                    Pageable pageRequest);

    @Query(value = "select productLine from ProductLine productLine")
    List<ProductLine> findAllByPage(Pageable pageable);
}
