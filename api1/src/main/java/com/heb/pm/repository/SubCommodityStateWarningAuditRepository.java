/*
 * SubCommodityStateWarningAuditRepository
 *
 *  Copyright (c) 2018 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
package com.heb.pm.repository;

import com.heb.pm.entity.SubCommodityStateWarningAudit;
import com.heb.pm.entity.SubCommodityStateWarningAuditKey;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository for sub-commodity state warning audit.
 *
 * @author vn70529
 * @since 2.18.4
 */
public interface SubCommodityStateWarningAuditRepository  extends JpaRepository<SubCommodityStateWarningAudit, SubCommodityStateWarningAuditKey> {
}
