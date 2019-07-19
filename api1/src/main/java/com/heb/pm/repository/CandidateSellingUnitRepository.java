/*
 * CandidateRepository
 *
 *  Copyright (c) 2018 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.repository;

import com.heb.pm.entity.CandidateSellingUnit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Repository for candidate product scan codes (UPC).
 *
 * @author m314029
 * @since 2.0.3
 */
public interface CandidateSellingUnitRepository extends JpaRepository<CandidateSellingUnit, Long>{

    /**
     *  Returns all candidate selling units by upc.
     *
     * @param upc the upc.
     * @return All candidate selling units by upc.
     */
    List<CandidateSellingUnit> findAllByUpcOrderByLastUpdatedOnDesc(Long upc);
}
