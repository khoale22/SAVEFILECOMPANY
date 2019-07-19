package com.heb.pm.repository;

import com.heb.pm.entity.CustomerProductGroupMembershipAudit;
import com.heb.pm.entity.CustomerProductGroupMembershipAuditKey;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * JPA repository for Customer Product Group Membership Audit.
 *
 * @author vn70529
 * @since 2.15.0
 */
public interface CustomerProductGroupMembershipAuditRepository extends JpaRepository<CustomerProductGroupMembershipAudit, CustomerProductGroupMembershipAuditKey> {
}