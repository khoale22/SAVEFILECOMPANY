package com.heb.pm.repository;

import com.heb.pm.entity.CustomerProductGroupMembershipAudit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * JPA repository for Customer Product Group Membership Audit entity with count.
 *
 * @author vn70529
 * @since 2.15.0
 */
public interface CustomerProductGroupMembershipAuditRepositoryWithCount extends CustomerProductGroupMembershipAuditRepository {

    /**
     * Get the list of CustomerProductGroupMembershipAudit by custProductGroupId.
     *
     * @param custProductGroupId   the custProductGroupId of product group.
     * @return the list of CustomerProductGroupMembershipAudit.
     */
    Page<CustomerProductGroupMembershipAudit> findByKeyCustProductGroupId(Long custProductGroupId, Pageable pageRequest);
}
