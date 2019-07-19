/*
 * StoreDepartmentAuditRepository
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.repository;

        import com.heb.pm.entity.StoreDepartmentAudit;
        import com.heb.pm.entity.SubDepartmentKey;
        import org.springframework.data.jpa.repository.JpaRepository;

/**
 * JPA Repository for StrDepartments.
 *
 * @author vn70633
 * @since 2.0.5
 */
public interface StoreDepartmentAuditRepository extends JpaRepository<StoreDepartmentAudit, SubDepartmentKey> {

}

