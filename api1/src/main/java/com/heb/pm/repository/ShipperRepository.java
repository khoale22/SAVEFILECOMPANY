/*
 * ShipperRepository
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
package com.heb.pm.repository;


import com.heb.pm.entity.Shipper;
import com.heb.pm.entity.ShipperKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository to retrieve information about an shipper.
 *
 * @author vn70516
 * @since 2.26.0
 */
public interface ShipperRepository extends JpaRepository<Shipper, ShipperKey> {

    List<Shipper> findByKeyUpcAndShipperTypeCode(long upc, char shipperTypeCode);
}
