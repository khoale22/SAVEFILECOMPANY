/*
 * SellingRestrictionHierarchyLevelAuditRepository
 *
 *  Copyright (c) 2018 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
package com.heb.pm.repository;

import com.heb.pm.entity.SellingRestrictionHierarchyLevelAudit;
import com.heb.pm.entity.SellingRestrictionHierarchyLevelAuditKey;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository for selling restrictions for hierarchy levels Audit.
 *
 * @author vn70529
 * @since 2.18.4
 */
public interface SellingRestrictionHierarchyLevelAuditRepository extends JpaRepository<SellingRestrictionHierarchyLevelAudit, SellingRestrictionHierarchyLevelAuditKey> {
}
