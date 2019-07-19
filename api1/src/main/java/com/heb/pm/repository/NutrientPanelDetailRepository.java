/*
 *
 * NutrientPanelDetailRepository.java
 *
 * Copyright (c) 2018 HEB
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of HEB.
 *
 */
package com.heb.pm.repository;

import com.heb.pm.entity.NutrientPanelDetail;
import com.heb.pm.entity.NutrientPanelDetailKey;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for NutrientPanelDetail.
 *
 * @author vn70529
 * @since 2.21.0
 */
public interface NutrientPanelDetailRepository extends JpaRepository<NutrientPanelDetail, NutrientPanelDetailKey> {

}
