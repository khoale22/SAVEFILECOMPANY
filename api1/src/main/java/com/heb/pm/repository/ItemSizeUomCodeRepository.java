/*
 * ItemSizeUomCodeRepository
 *
 * Copyright (c) 2018 HEB
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of HEB.
 */
package com.heb.pm.repository;

import com.heb.pm.entity.ItemSizeUomCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository to retrieve ItemSizeUomCode without count.
 *
 * @author vn70529
 * @since 2.23.0
 */
public interface ItemSizeUomCodeRepository extends JpaRepository<ItemSizeUomCode, String> {
    List<ItemSizeUomCode> findAllByOrderByItemSizeUomCdAsc();
}
