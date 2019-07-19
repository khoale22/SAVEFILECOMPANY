/*
 * ShippingRestrictionHierarchyLevelAuditRepository
 *
 *  Copyright (c) 2018 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
package com.heb.pm.repository;

import com.heb.pm.entity.ShippingRestrictionHierarchyLevelAudit;
import com.heb.pm.entity.ShippingRestrictionHierarchyLevelAuditKey;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository for shipping restrictions for hierarchy levels Audit.
 *
 * @author vn70529
 * @since 2.18.4
 */
public interface ShippingRestrictionHierarchyLevelAuditRepository extends JpaRepository<ShippingRestrictionHierarchyLevelAudit, ShippingRestrictionHierarchyLevelAuditKey> {
}
