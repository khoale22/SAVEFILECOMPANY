package com.heb.pm.customHierarchy;

import com.heb.pm.entity.GenericEntityDescription;
import com.heb.pm.entity.GenericEntityDescriptionKey;
import com.heb.pm.repository.GenericEntityDescriptionRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Business logic related to generic entity description.
 *
 * @author m314029
 * @since 2.18.4
 */
@Service
public class GenericEntityDescriptionService {

	@Autowired
	private GenericEntityDescriptionRespository repository;


	/**
	 * Finds a generic entity description by entity id and hierarchy context.
	 *
	 * @param entityId Entity id to look for.
	 * @param hierarchyContext Hierarchy context to look for.
	 * @return Generic entity description matching the search criteria.
	 */
	public GenericEntityDescription findByEntityIdAndHierarchyContext(Long entityId, String hierarchyContext) {
		GenericEntityDescriptionKey key = new GenericEntityDescriptionKey();
		key.setEntityId(entityId);
		key.setHierarchyContext(hierarchyContext);
		return this.repository.findOne(key);
	}
}
