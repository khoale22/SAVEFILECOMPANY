/*
 * TopToTopRepository
 *
 *  Copyright (c) 2018 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
package com.heb.pm.repository;

import com.heb.pm.entity.TopToTop;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Repository to retrieve information about TopToTop.
 *
 * @author vn87351
 * @since 2.17.0
 */
public interface TopToTopRepository extends JpaRepository<TopToTop, Integer>, TopToTopRepositoryCommon {

    /**
     * Find all TopToTop data filter by TopToTop id or TopToTop name.
     *
     * @param id          The TopToTop id to search.
     * @param name        The topToTop name to search.
     * @param pageRequest The page request for pagination.
     * @return the page of TopToTops.
     */
    @Query(value = FIND_TOP_TO_TOP_BY_ID_AND_DESCRIPTION_SQL)
    Page<TopToTop> findByIdOrName(@Param("id") String id,
                                  @Param("name") String name,
                                  Pageable pageRequest);

    /**
     * Find all TopToTop data filter by TopToTop id.
     *
     * @param id          The TopToTop id to search.
     * @param pageRequest The page request for pagination.
     * @return the page of TopToTops.
     */
    @Query(value = FIND_BY_TOP_TO_TOP_ID_SQL)
    Page<TopToTop> findById(@Param("id") String id, Pageable pageRequest);

    /**
     * Find all TopToTop data filter by TopToTop name.
     *
     * @param topToTopName The topToTop name to search.
     * @param pageRequest  The page request for pagination.
     * @return the page of TopToTops.
     */
    Page<TopToTop> findByTopToTopNameIgnoreCaseContaining(String topToTopName, Pageable pageRequest);

    /**
     * Find all TopToTop data filter by TopToTop id and TopToTop name.
     *
     * @param id          The TopToTop id to search.
     * @param name        The topToTop name to search.
     * @param pageRequest The page request for pagination.
     * @return the page of TopToTops.
     */
    @Query(value = FIND_BY_TOP_TO_TOP_ID_AND_TOP_TO_TOP_NAME_SQL)
    Page<TopToTop> findByIdAndName(@Param("id") String id, @Param("name") String name, Pageable pageRequest);
    
    /**
     * Finds TopToTop with the highest ID.
     *
     * @return TopToTop with the highest ID.
     */
    TopToTop findFirstByOrderByTopToTopIdDesc();
}
