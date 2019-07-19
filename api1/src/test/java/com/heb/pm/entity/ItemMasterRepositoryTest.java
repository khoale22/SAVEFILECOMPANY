/*
 *  com.heb.pm.entity.ItemMasterRepositoryTest
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */

package com.heb.pm.entity;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository to support testing ItemMaster objects.
 *
 * @author d116773
 * @since 2.0.0
 */
public interface ItemMasterRepositoryTest extends JpaRepository<ItemMaster, ItemMasterKey> {
}
