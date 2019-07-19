/*
 * MasterDataExtensionAuditRepository
 *
 *  Copyright (c) 2018 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.repository;

import com.heb.pm.entity.MasterDataExtensionAudit;
import com.heb.pm.entity.MasterDataExtensionAuditKey;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * JPA repository for Master Data Extension Audit.
 *
 * @author vn70529
 * @since 2.15.0
 */
public interface MasterDataExtensionAuditRepository extends JpaRepository<MasterDataExtensionAudit, MasterDataExtensionAuditKey> {
}
