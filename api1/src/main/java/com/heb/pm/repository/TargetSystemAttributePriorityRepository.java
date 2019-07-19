package com.heb.pm.repository;

import com.heb.pm.entity.Attribute;
import com.heb.pm.entity.TargetSystemAttributePriority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.w3c.dom.Attr;

import java.util.List;

/**
 * The interface attribute repository.
 * @author  s753601
 * @since 2.5.0
 */
public interface TargetSystemAttributePriorityRepository extends JpaRepository<TargetSystemAttributePriority, Long>, TargetSystemAttributePriorityRepositoryCommon{

    /**
     * Returns all the Target Source Attribute Priorities grouped by attribute and ordered by their priority number
     * @return
     */
    @Query(value = SOURCE_PRIORITY_TABLE_QUERY)
    List<TargetSystemAttributePriority> findSourcePriorities();

    List<TargetSystemAttributePriority> findByKeyLogicalAttributeIdOrderByAttributePriorityNumber(int logicalAttributeId);
    /**
     * Get the TargetSystemAttributePriority by logicalAttributeId, dataSourceSystemId and physicalAttributeId.
     *
     * @param logicalAttributeId the logicalAttributeId.
     * @param dataSourceSystemId the dataSourceSystemId.
     * @param physicalAttributeId the physicalAttributeId
     * @return the TargetSystemAttributePriority.
     */
    TargetSystemAttributePriority findOneByKeyLogicalAttributeIdAndKeyDataSourceSystemIdAndKeyPhysicalAttributeIdOrderByAttributePriorityNumberAsc(
            int logicalAttributeId, long dataSourceSystemId, long physicalAttributeId);
}
