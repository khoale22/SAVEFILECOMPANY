/*
 *
 *  DiscontinueParametersAuditRepositoryTest
 *
 *   Copyright (c) 2016 HEB
 *   All rights reserved.
 *
 *   This software is the confidential and proprietary information
 *    of HEB.
 *
 *
 */

package com.heb.pm.entity;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Tests repository for DiscontinueParametersAudit.
 *
 * @author s573181
 * @since 2.0.3
 */
public interface DiscontinueParametersAuditRepositoryTest extends JpaRepository<DiscontinueParametersAudit, DiscontinueParametersAuditKey> {
}
