/*
 *
 *  *
 *  *  Copyright (c) 2016 HEB
 *  *  All rights reserved.
 *  *
 *  *  This software is the confidential and proprietary information
 *  *  of HEB.
 *  *
 *
 */

package com.heb.pm.entity;

/*
 * created by m314029 on 06/23/2016.
*/

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository to support testing Shipper objects.
 *
 */
public interface ShipperRepositoryTest extends JpaRepository<Shipper, ShipperKey> {
}
