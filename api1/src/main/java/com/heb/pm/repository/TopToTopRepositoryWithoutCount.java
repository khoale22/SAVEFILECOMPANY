/*
 * TopToTopRepositoryWithoutCount.java
 *
 *  Copyright (c) 2019 H-E-B
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
package com.heb.pm.repository;

import com.heb.pm.entity.TopToTop;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Repository to retrieve information about TopToTop.
 *
 * @author vn73545
 * @since 2.41.0
 */
public interface TopToTopRepositoryWithoutCount extends JpaRepository<TopToTop, Integer>, TopToTopRepositoryCommon {

    /**
     * Find all TopToTop data filter by TopToTop id.
     *
     * @param id          The TopToTop id to search.
     * @param pageRequest The page request for pagination.
     * @return the list of TopToTops.
     */
    @Query(value = FIND_BY_TOP_TO_TOP_ID_SQL)
    List<TopToTop> findById(@Param("id") String id, Pageable pageRequest);

    /**
     * Find all TopToTop data filter by TopToTop name.
     *
     * @param topToTopName The topToTop name to search.
     * @param pageRequest  The page request for pagination.
     * @return the list of TopToTops.
     */
    List<TopToTop> findByTopToTopNameIgnoreCaseContaining(String topToTopName, Pageable pageRequest);

    /**
     * Find all TopToTop data filter by TopToTop id and TopToTop name.
     *
     * @param id          The TopToTop id to search.
     * @param name        The topToTop name to search.
     * @param pageRequest The page request for pagination.
     * @return the list of TopToTops.
     */
    @Query(value = FIND_BY_TOP_TO_TOP_ID_AND_TOP_TO_TOP_NAME_SQL)
    List<TopToTop> findByIdAndName(@Param("id") String id, @Param("name") String name, Pageable pageRequest);
    
    /**
     * Find all TopToTop data filter by TopToTop name.
     *
     * @param name The topToTop name to search.
     * @return the list of TopToTops.
     */
    @Query(value = FIND_BY_TOP_TO_TOP_NAME_SQL)
    List<TopToTop> findByName(@Param("name") String name);
}