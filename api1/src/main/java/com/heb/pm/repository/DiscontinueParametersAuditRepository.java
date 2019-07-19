/*
 *
 *  DiscontinueParametersAuditRepository
 *
 *   Copyright (c) 2016 HEB
 *   All rights reserved.
 *
 *   This software is the confidential and proprietary information
 *    of HEB.
 *
 *
 */

package com.heb.pm.repository;

import com.heb.pm.entity.DiscontinueParametersAudit;
import com.heb.pm.entity.DiscontinueParametersAuditKey;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for product discontinue Parameters Audit.
 *
 * @author s573181
 * @since 2.0.3
 */
public interface DiscontinueParametersAuditRepository extends JpaRepository<DiscontinueParametersAudit, DiscontinueParametersAuditKey> {
}
