package com.heb.pm.ecommerce;

import com.heb.pm.entity.TargetSystemAttributePriority;
import com.heb.util.jpa.LazyObjectResolver;

/**
 * Holds business logic related to ECommerce.
 *
 * @author s753601
 * @since 2.5.0
 */
public class eCommerceResolver implements LazyObjectResolver<TargetSystemAttributePriority>
{
    /**
     * Resolves a TargetSystemAttributePriority object. It will load the following properties:
     * 1. LogicalPhysicalRelationship
     * 2. LogicalPhysicalRelationship->Attribute
     * 3. SourceSystem
     * If the Relationship group type code = "GRP "
     * 4. LogicalPhysicalRelationship->RelationshipGroup
     *
     * @param targetSystemAttributePriority The TargetSystemAttributePriority to resolve.
     */
    @Override
    public void fetch(TargetSystemAttributePriority targetSystemAttributePriority){
        targetSystemAttributePriority.getLogicalPhysicalRelationship().getKey();
        targetSystemAttributePriority.getLogicalPhysicalRelationship().getAttribute().getAttributeName();
        targetSystemAttributePriority.getSourceSystem().getDescription();
        if(targetSystemAttributePriority.getLogicalPhysicalRelationship().getKey().getRelationshipGroupTypeCode().equals("Grp ")){
            targetSystemAttributePriority.getLogicalPhysicalRelationship().getRelationshipGroup().getRelationshipGroupDescription();
        }
    }
}
