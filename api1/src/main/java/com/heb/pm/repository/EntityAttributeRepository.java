package com.heb.pm.repository;

import com.heb.pm.entity.EntityAttribute;
import com.heb.pm.entity.EntityAttributeKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * This repository will query the "ENTY_ATTR" table
 */
public interface EntityAttributeRepository extends JpaRepository<EntityAttribute, EntityAttributeKey>, EntityAttributeRepositoryCommon{

	@Query(value = TAGS_AND_SPECS_QUERY)
	List<EntityAttribute> GetTagsAndSpecsAttributes();

	EntityAttribute findByKeyEntityIdAndKeyAttributeId(Long entityId, Long attributeId);

	/**
	 * Returns a list of EntityAttributes that are tied to a provided attribute ID.
	 *
	 * @param attributeId The ID of the attribute to look for.
	 * @return The EntityAttributes that are tied to the passed in attribute ID.
	 */
	List<EntityAttribute> findByKeyAttributeId(Long attributeId);

	@Query("select ea from EntityAttribute ea where ea.key.entityId = :entityId and ea.key.attributeId = :attributeId")
	List<EntityAttribute> getEntityAttributesForEntityAndAttribute(@Param("entityId")Long entityId, @Param("attributeId")Long attributeId);

	@Query("select ea from EntityAttribute ea where ea.key.attributeId = :attributeId")
	List<EntityAttribute> getEntityAttributesForAttribute(@Param("attributeId")Long attributeId);

	/**
	 * Get all entity attributes tied to the given entity ids
	 * @param entityIds to look for
	 * @return entity attributes tied to given entity ids
	 */
	List<EntityAttribute> findByKeyEntityIdIn(List<Long> entityIds);
}
