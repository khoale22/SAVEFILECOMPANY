/*
 * ClassCommodityRepositoryTest
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.entity;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Test JPA repository for ClassCommodity mapping.
 *
 * @author d116773
 * @since 2.0.2
 */
public interface ClassCommodityRepositoryTest extends JpaRepository<ClassCommodity, ClassCommodityKey>{
}
