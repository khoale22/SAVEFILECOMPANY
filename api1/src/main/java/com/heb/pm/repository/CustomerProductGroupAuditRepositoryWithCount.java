package com.heb.pm.repository;

import com.heb.pm.entity.CustomerProductGroupAudit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * JPA repository for Customer Product Group Audit entity with count.
 *
 * @author vn70529
 * @since 2.15.0
 */
public interface CustomerProductGroupAuditRepositoryWithCount extends CustomerProductGroupAuditRepository {

    /**
     * Get the list of CustomerProductGroupAudit by custProductGroupId.
     *
     * @param custProductGroupId   the custProductGroupId of product group.
     * @return the list of CustomerProductGroupAudit.
     */
    Page<CustomerProductGroupAudit> findByKeyCustProductGroupId(Long custProductGroupId, Pageable pageRequest);

}
