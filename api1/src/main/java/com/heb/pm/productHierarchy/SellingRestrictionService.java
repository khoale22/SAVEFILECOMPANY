package com.heb.pm.productHierarchy;

import com.heb.pm.entity.SellingRestriction;
import com.heb.pm.repository.SellingRestrictionRepository;
import com.heb.pm.ws.ProductHierarchyManagementServiceClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Holds all business logic related to product hierarchy selling restrictions.
 *
 * @author m314029
 * @since 2.8.0
 */
@Service
public class SellingRestrictionService {

	@Autowired
	private SellingRestrictionRepository repository;

	/**
	 * Calls the selling restriction repository to find all records.
	 * This will not include group code '9' (shipping restrictions), as these are not included in the entity
	 * SellingRestriction.
	 *
	 * @return List of all selling restrictions.
	 */
	public List<SellingRestriction> findAll() {
		return repository.findAllByOrderByRestrictionDescription();
	}
}
