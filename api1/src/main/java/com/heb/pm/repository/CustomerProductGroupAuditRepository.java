package com.heb.pm.repository;

import com.heb.pm.entity.CustomerProductGroupAudit;
import com.heb.pm.entity.CustomerProductGroupAuditKey;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * JPA repository for Customer Product Group Audit.
 *
 * @author vn70529
 * @since 2.15.0
 */
public interface CustomerProductGroupAuditRepository extends JpaRepository<CustomerProductGroupAudit, CustomerProductGroupAuditKey> {
}
