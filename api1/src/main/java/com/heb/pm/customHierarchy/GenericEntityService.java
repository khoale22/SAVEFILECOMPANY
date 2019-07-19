package com.heb.pm.customHierarchy;

import com.heb.pm.entity.GenericEntity;
import com.heb.pm.repository.GenericEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Holds all business logic related to generic entities.
 *
 * @author m314029
 * @since 2.18.4
 */
@Service
public class GenericEntityService {

	// error messages
	private static final String MORE_THAN_ONE_PRODUCT_TO_ENTITY_MESSAGE = "Product %d has more than one entity.";

	@Autowired
	private GenericEntityRepository repository;

	/**
	 * Finds a Generic Entity by product id. If no entity exists, return null. If there is more than one entity tied
	 * to the given product id, throw an error. Else return the single entity tied to the given product id.
	 *
	 * @param productId Product id to look for.
	 * @return Generic entity matching the product id given, or null if not found.
	 */
	public GenericEntity findByProductId(Long productId){
		List<GenericEntity> genericEntities =
				this.repository.findByDisplayNumberAndType(productId, GenericEntity.EntyType.PROD.getName());

		// if there are no entities tied to this product, return null.
		if(genericEntities.isEmpty()){
			return null;
		}

		// if there's more than one entity tied to this product, throw an error.
		if (genericEntities.size() > 1) {
			throw new IllegalArgumentException(String.format(MORE_THAN_ONE_PRODUCT_TO_ENTITY_MESSAGE, productId));
		}

		return genericEntities.get(0);
	}
}
