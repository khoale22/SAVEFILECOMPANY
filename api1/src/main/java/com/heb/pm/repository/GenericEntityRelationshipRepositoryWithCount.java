package com.heb.pm.repository;

import com.heb.pm.entity.GenericEntityRelationship;
import com.heb.pm.entity.GenericEntityRelationshipKey;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author m314029
 * @since
 */
public interface GenericEntityRelationshipRepositoryWithCount extends JpaRepository<GenericEntityRelationship, GenericEntityRelationshipKey> {

	/**
	 * Returns a page of children relationships that have the same parent entity id, are in the same hierarchy,
	 * and the child entity type is of the given type. Ordered by child entity display number.
	 *
	 * @param parentEntityId The parent entity id to look for.
	 * @param hierarchyContext The hierarchy context to look for.
	 * @param entityType The type of entity to look for.
	 * @param pageRequest Page information for the current search.
	 * @return Page of relationships matching the given search.
	 */
	Page<GenericEntityRelationship> findByKeyParentEntityIdAndKeyHierarchyContextAndGenericChildEntityTypeOrderByGenericChildEntityDisplayNumber(Long parentEntityId, String hierarchyContext, String entityType, Pageable pageRequest);

}
