/*
 *  NutrientPanelHeaderRepository
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.repository;

import com.heb.pm.entity.NutrientPanelHeader;
import com.heb.pm.entity.NutrientPanelHeaderKey;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * JPA repository for Nutrient Panel Header.
 *
 * @author vn73545
 * @since 2.15.0
 */
public interface NutrientPanelHeaderRepository extends JpaRepository<NutrientPanelHeader, NutrientPanelHeaderKey> {

    /**
     * Get NutrientPanelHeader by upc and source system id.
     *
     * @param upc the upc to find.
     * @param sourceSystemId the source system id.
     * @return the NutrientPanelHeader object.
     */
    List<NutrientPanelHeader> findByKeyUpcAndKeySourceSystemId(long upc, long sourceSystemId);
}