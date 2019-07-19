/*
 *
 * NutrientPanelColumnHeaderRepository.java
 *
 * Copyright (c) 2018 HEB
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of HEB.
 *
 */
package com.heb.pm.repository;

import com.heb.pm.entity.NutrientPanelColumnHeader;
import com.heb.pm.entity.NutrientPanelColumnHeaderKey;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for NutrientPanelColumnHeader.
 *
 * @author vn70529
 * @since 2.21.0
 */
public interface NutrientPanelColumnHeaderRepository extends JpaRepository<NutrientPanelColumnHeader, NutrientPanelColumnHeaderKey> {

}
