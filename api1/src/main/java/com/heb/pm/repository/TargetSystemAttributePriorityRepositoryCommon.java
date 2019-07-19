package com.heb.pm.repository;

/**
 * The interface Target System Attribute Priority common strings repository.
 * @author  s753601
 * @since 2.5.0
 */
public interface TargetSystemAttributePriorityRepositoryCommon {

    String SOURCE_PRIORITY_TABLE_QUERY = "select ap " +
                                         "from  TargetSystemAttributePriority ap " +
                                         "  inner join ap.logicalPhysicalRelationship lp  "+
                                         "  left outer join lp.relationshipGroup rg " +
                                         "  inner join lp.attribute ar  " +
                                         "  inner join ap.sourceSystem ss " +
                                         "order by ar.attributeName, ap.attributePriorityNumber ";
}
