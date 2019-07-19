/*
 *  CandidateNutrientPanelHeaderRepository
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.repository;

import com.heb.pm.entity.CandidateNutrientPanelHeader;
import com.heb.pm.entity.CandidateNutrientPanelHeaderKey;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * JPA repository for Candidate Nutrient Panel Header.
 *
 * @author vn73545
 * @since 2.15.0
 */
public interface CandidateNutrientPanelHeaderRepository extends JpaRepository<CandidateNutrientPanelHeader, CandidateNutrientPanelHeaderKey> {

    /**
     * Get CandidateNutrientPanelHeader by work request id.
     *
     * @param workRequestId the work request id.
     * @return the list of CandidateNutrientPanelHeader object.
     */
    List<CandidateNutrientPanelHeader> findByKeyWorkRequestId(long workRequestId);
}