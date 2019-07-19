package com.heb.pm.repository;

import com.heb.pm.entity.GenericEntityRelationship;
import com.heb.pm.entity.GenericEntityRelationshipKey;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Repository to retrieve information about generic entity relationships.
 *
 * @author m314029
 * @since 2.12.0
 */
public interface GenericEntityRelationshipRepository extends JpaRepository<GenericEntityRelationship, GenericEntityRelationshipKey>{

	/**
	 * This method retrieves all of the generic entity relationships matching a parent id and hierarchy context.
	 *
	 * @param parentId The parent id to search for.
	 * @param hierarchyContext The hierarchy context to search for.
	 * @return A list of generic entity relationships matching the requested ids.
	 */
	List<GenericEntityRelationship> findByKeyParentEntityIdAndHierarchyContext(Long parentId, String hierarchyContext);

	/**
	 * Returns a list of relationships where the childId is the child entity
	 * @param childId the child's entity id
	 * @param hierarchyContext the hierarchyContext in question
	 * @param parentSwitch the default parent switch
	 * @return
	 */
	GenericEntityRelationship findTop1ByKeyChildEntityIdAndHierarchyContextAndDefaultParent(Long childId, String hierarchyContext, boolean parentSwitch);


	/**
	 * Returns a list of relationships where the childId is the child entity
	 * @param childId the child's entity id
	 * @param hierarchyContext the hierarchyContext in question
	 * @param parentSwitch the default parent switch
	 * @return
	 */
	List<GenericEntityRelationship> findByKeyChildEntityIdAndHierarchyContextAndDefaultParent(Long childId, String hierarchyContext, boolean parentSwitch);


	/**
	 * Returns a list of children based on the product ID, its context, and if it is the default parent relationship
	 * @param displayNumber
	 * @param context
	 * @param defaultParent
	 * @return
	 */
	List<GenericEntityRelationship> findByGenericChildEntityDisplayNumberAndHierarchyContextAndDefaultParent(long displayNumber, String context, Boolean defaultParent);
	/**
	 * Returns a list of relationships where the childId is the child entity
	 * @param childId the child's entity id
	 * @param hierarchyContext the hierarchyContext in question
	 * @return
	 */
	List<GenericEntityRelationship> findByKeyChildEntityIdAndHierarchyContext(Long childId, String hierarchyContext);
	/**
	 * Returns a list of children based on the product ID, its context, and if it is the default parent relationship
	 * @param displayNumber
	 * @param context
	 * @return
	 */
	@Query(value = "SELECT distinct genericEntityRelationship from GenericEntityRelationship genericEntityRelationship where genericEntityRelationship.genericChildEntity.displayNumber in :displayNumber and TRIM(genericEntityRelationship.genericChildEntity.type)=:type and TRIM(genericEntityRelationship.key.hierarchyContext)=:hierarchyContext")
	List<GenericEntityRelationship> findByListDisplayNumberAndHierarchyContextAndGenericChildEntityType(@Param("displayNumber") List<Long> displayNumber,@Param("hierarchyContext") String context,@Param("type") String type);
	/**
	 * Returns a list of children based on the product ID, its context, and if it is the default parent relationship
	 * @param displayNumber
	 * @param context
	 * @return
	 */
	@Query(value = "SELECT distinct genericEntityRelationship.key.parentEntityId from GenericEntityRelationship genericEntityRelationship where genericEntityRelationship.genericChildEntity.displayNumber = :displayNumber and TRIM(genericEntityRelationship.genericChildEntity.type)=:type and TRIM(genericEntityRelationship.key.hierarchyContext)=:hierarchyContext")
	Long findByGenericChildEntityDisplayNumberAndHierarchyContextAndGenericChildEntityType(@Param("displayNumber")Long displayNumber,@Param("hierarchyContext")String context,@Param("type")String type);
	/**
	 * This method retrieves all of the generic entity relationships matching a parent id and hierarchy context.
	 *
	 * @param parentId The parent id to search for.
	 * @param hierarchyContext The hierarchy context to search for.
	 * @return A list of generic entity relationships matching the requested ids.
	 */
	@Query(value = "SELECT distinct genericEntityRelationship from GenericEntityRelationship genericEntityRelationship where genericEntityRelationship.key.parentEntityId = :parentId and TRIM(genericEntityRelationship.key.hierarchyContext)=:hierarchyContext")
	List<GenericEntityRelationship> findChildEntityIdByKeyParentEntityIdAndHierarchyContext(@Param("parentId") Long parentId, @Param("hierarchyContext")String hierarchyContext);
	/**
	 * Returns a list of children based on the product ID, its context, and if it is the default parent relationship
	 * @param displayNumber
	 * @param context
	 * @param type
	 * @return List<GenericEntityRelationship>
	 * @author vn55306
	 */
	@Query(value = "SELECT genericEntityRelationship from GenericEntityRelationship genericEntityRelationship where genericEntityRelationship.genericChildEntity.displayNumber in :displayNumber and TRIM(genericEntityRelationship.genericChildEntity.type)=:type and TRIM(genericEntityRelationship.key.hierarchyContext)=:hierarchyContext")
	List<GenericEntityRelationship> findByCurrentHierarchy(@Param("displayNumber") List<Long> displayNumber,@Param("hierarchyContext") String context,@Param("type") String type);
	/**
	 * This method retrieves all of the generic entity relationships matching a parent id and hierarchy context.
	 *
	 * @param parentId The parent id to search for.
	 * @param hierarchyContext The hierarchy context to search for.
	 * @return A list of generic entity relationships matching the requested ids.
	 */
	@Query(value = "SELECT distinct genericEntityRelationship.key.childEntityId from GenericEntityRelationship genericEntityRelationship where genericEntityRelationship.key.parentEntityId = :parentId and TRIM(genericEntityRelationship.key.hierarchyContext)=:hierarchyContext")
	List<Long> findBrickChildEntityIdByKeyParentEntityIdAndHierarchyContext(@Param("parentId") Long parentId, @Param("hierarchyContext")String hierarchyContext);

	/**
	 * Return the count of children entities tied to a parent in a given context that are not product entities.
	 *
	 * @param parentId The ID of the parent node to find children of.
	 * @param hierarchyContext The context of the hierarchy.
	 * @return The number of non-product child entities tied to the parent.
	 */
	@Query("SELECT COUNT(er) from GenericEntityRelationship er, GenericEntity e where er.key.childEntityId = e.id " +
			"and er.key.parentEntityId = :parentId and er.key.hierarchyContext = :hierarchyContext " +
			"and e.type not in ('PGRP ', 'PROD ')")
	Long countNonProductChildrenEntities(@Param("parentId") Long parentId, @Param("hierarchyContext")String hierarchyContext);

	@Query(value = "SELECT er FROM GenericEntityRelationship er WHERE er.genericChildEntity.type = 'PROD' " +
			" and er.genericChildEntity.displayNumber = :displayNumber AND er.defaultParent = 'Y' AND " +
			" er.key.hierarchyContext = :hierarchyContext")
	GenericEntityRelationship findByProduct(@Param("displayNumber") long productId,
											@Param("hierarchyContext") String  hierarchyContext);

	/**
	 * This method to find all child entity id by parent entity id, hierarchy context and default parent.
	 *
	 * @param parentIds the parent enntity id.
	 * @return list of children entity id.
	 */
	@Query(value = "SELECT genericEntityRelationship.key.childEntityId from GenericEntityRelationship genericEntityRelationship where genericEntityRelationship.parentEntityId in :parentIds")
	List<Long> findChildEntityIdsByKeyParentEntityIds(@Param("parentIds") List<Long> parentIds);

	/**
	 * This method to find all child entity id by parent entity id.
	 * @param parentId the parent enntity id.
	 * @return list of children entity id.
	 */
	@Query(value = "SELECT genericEntityRelationship.key.childEntityId from GenericEntityRelationship genericEntityRelationship where genericEntityRelationship.parentEntityId = :parentId")
	List<Long> findChildEntityIdsByKeyParentEntityId(@Param("parentId") Long parentId);

	/**
	 * Returns a list of children relationships that have the same parent entity id, are in the same hierarchy,
	 * and the child entity type is of the given type.
	 *
	 * @param parentEntityId The parent entity id to look for.
	 * @param hierarchyContext The hierarchy context to look for.
	 * @param entityType The type of entity to look for.
	 * @param pageRequest Page information for the current search.
	 * @return List of relationships matching the given search.
	 */
	List<GenericEntityRelationship> findByKeyParentEntityIdAndKeyHierarchyContextAndGenericChildEntityType(Long parentEntityId, String hierarchyContext, String entityType, Pageable pageRequest);

	/**
	 * Returns a relationship that has given child entity id, given hierarchy context, is the default
	 * parent relationship, and the child entity type is of the given type.
	 *
	 * @param childId The child entity id number to look for.
	 * @param hierarchyContext The hierarchy context to look for.
	 * @param defaultParent Whether the relationship is the default parent.
	 * @param entityType The type of entity to look for.
	 * @return List of relationships matching the given search.
	 */
	List<GenericEntityRelationship> findByKeyChildEntityIdAndHierarchyContextAndDefaultParentAndGenericChildEntityType(Long childId, String hierarchyContext, Boolean defaultParent, String entityType);

	/**
	 * Returns a list of children relationships that have the same parent entity id, are in the same hierarchy,
	 * and the child entity type is of the given type.
	 *
	 * @param parentEntityId The parent entity id to look for.
	 * @param hierarchyContext The hierarchy context to look for.
	 * @param entityType The type of entity to look for.
	 * @return List of relationships matching the given search.
	 */
	List<GenericEntityRelationship> findByKeyParentEntityIdAndKeyHierarchyContextAndGenericChildEntityType(Long parentEntityId, String hierarchyContext, String entityType);

	/**
	 * Returns a list of all children relationships that have the same hierarchy context, display number, and default parent.
	 * @param hierarchyContext
	 * @param displayNumber
	 * @param defaultParent
	 * @return
	 */
	List<GenericEntityRelationship> findByKeyHierarchyContextAndGenericChildEntityDisplayNumberAndDefaultParent(String hierarchyContext, Long displayNumber, Boolean defaultParent);

	/**
	 * Returns a children relationship that given a parent entity id, hierarchy context, and the child entity type,
	 * and child entity display number.
	 *
	 * @param parentEntityId The parent entity id to look for.
	 * @param hierarchyContext The hierarchy context to look for.
	 * @param entityType The type of entity to look for.
	 * @param displayNumber The display number to look for.
	 * @return List of relationships matching the given search.
	 */
	GenericEntityRelationship findByKeyParentEntityIdAndKeyHierarchyContextAndGenericChildEntityTypeAndGenericChildEntityDisplayNumber(Long parentEntityId, String hierarchyContext, String entityType, Long displayNumber);

	/**
	 * Returns a list of all children relationships that have the same hierarchy context, child entity display number, child entity type of entity, and default parent.
	 * @param hierarchyContext The hierarchy context to look for.
	 * @param displayNumber The display number to look for.
	 * @param defaultParent Whether the relationship is the default parent.
	 * @param entityType The type of entity to look for.
	 * @return
	 */
	List<GenericEntityRelationship> findByKeyHierarchyContextAndGenericChildEntityDisplayNumberAndDefaultParentAndGenericChildEntityType(String hierarchyContext, Long displayNumber, Boolean defaultParent, String entityType);

	/**
	 * Returns a list of children relationships that have the same parent entity id, are in the same hierarchy,
	 * and the child entity type is of the given type. Ordered by child entity display number.
	 *
	 * @param parentEntityId The parent entity id to look for.
	 * @param hierarchyContext The hierarchy context to look for.
	 * @param entityType The type of entity to look for.
	 * @param pageRequest Page information for the current search.
	 * @return List of relationships matching the given search.
	 */
	List<GenericEntityRelationship> findByKeyParentEntityIdAndKeyHierarchyContextAndGenericChildEntityTypeOrderByGenericChildEntityDisplayNumber(Long parentEntityId, String hierarchyContext, String entityType, Pageable pageRequest);

	/**
	 * Returns a list of children based on the product ID, its context, and if it is the default parent relationship
	 * @param displayNumber
	 * @param context
	 * @return
	 */
	@Query(value = "SELECT genericEntityRelationship from GenericEntityRelationship genericEntityRelationship where genericEntityRelationship.genericChildEntity.displayNumber = :displayNumber and TRIM(genericEntityRelationship.genericChildEntity.type)=:type and TRIM(genericEntityRelationship.key.hierarchyContext)=:hierarchyContext")
	List<GenericEntityRelationship> findByHierarchyContextAndGenericChildEntityDisplayNumberAndGenericChildEntityType(@Param("hierarchyContext")String context,@Param("displayNumber")Long displayNumber,@Param("type")String type);
}
