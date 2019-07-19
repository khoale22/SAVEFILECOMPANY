package com.heb.pm.repository;

import com.heb.pm.entity.GenericEntityRelationshipAudit;
import com.heb.pm.entity.GenericEntityRelationshipAuditKey;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * JPA repository for Generic Entity Relationship Audit.
 *
 * @author vn70529
 * @since 2.15.0
 */
public interface GenericEntityRelationshipAuditRepository extends JpaRepository<GenericEntityRelationshipAudit, GenericEntityRelationshipAuditKey> {
}
