/*
 *  ShipperAuditRepository
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.repository;

import com.heb.pm.entity.ShipperAudit;
import com.heb.pm.entity.ShipperAuditKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository for Shipper Audit
 *
 * @author l730832
 * @since 2.6.0
 */
public interface ShipperAuditRepository extends JpaRepository<ShipperAudit, ShipperAuditKey> {

	List<ShipperAudit> findByKeyUpcOrderByKeyChangedOn(Long upc);
}
