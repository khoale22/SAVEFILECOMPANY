package com.heb.pm.attribute;

import com.heb.pm.entity.EntityAttribute;
import com.heb.pm.repository.EntityAttributeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Contains Business Logic related to Entity Attributes
 *
 * @author o792389
 * @since 2.18.4
 */

@Service
public class EntityAttributeService {
	@Autowired
	private EntityAttributeRepository repository;

	/**
	 * This function takes a list of entityAttributes and
	 * returns an entityId based on the entity's product keys
	 * @param entityIds
	 * @return entityIds
	 */
	public List<EntityAttribute> getByEntityIds(List<Long> entityIds) {
		return this.repository.findByKeyEntityIdIn(entityIds);
	}

	/**
	 * This returns a repository call to find all entity attributes linked to a given attribute.
	 *
	 * @param attributeId Attribute id to look for.
	 * @return List of entity attributes with the given attribute id.
	 */
	public List<EntityAttribute> findByKeyAttributeId(Long attributeId) {
		return this.repository.findByKeyAttributeId(attributeId);
	}
}
