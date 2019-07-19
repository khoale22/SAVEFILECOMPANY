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
 * Repository to support testing Item objects.
 *
 * Created by s573181 on 6/7/2016.
 */
public interface ItemRepositoryTest extends JpaRepository<ProdItem, ProdItemKey> {
}
