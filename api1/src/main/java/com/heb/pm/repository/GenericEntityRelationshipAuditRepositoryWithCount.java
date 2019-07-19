package com.heb.pm.repository;

import com.heb.pm.entity.GenericEntityRelationshipAudit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * JPA repository for Generic Entity Relationship Audit entity with count.
 *
 * @author vn70529
 * @since 2.15.0
 */
public interface GenericEntityRelationshipAuditRepositoryWithCount  extends GenericEntityRelationshipAuditRepository {

    /**
     * Get the list of GenericEntityRelationshipAudit by displayNumber and hierarchyContext.
     *
     * @param displayNumber   the custProductGroupId of product group.
     * @param hierarchyContext the hierarchy context of GenericEntityRelationship.
     * @return the list of GenericEntityRelationshipAudit.
     */
    Page<GenericEntityRelationshipAudit> findAllByGenericChildEntityDisplayNumberAndKeyHierarchyContext(Long displayNumber, String hierarchyContext, Pageable pageRequest);
}
