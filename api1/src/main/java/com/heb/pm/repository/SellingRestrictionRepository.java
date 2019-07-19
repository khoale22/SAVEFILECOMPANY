package com.heb.pm.repository;

import com.heb.pm.entity.SellingRestriction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository for selling restrictions.
 *
 * @author m314029
 * @since 2.8.0
 */
public interface SellingRestrictionRepository extends JpaRepository<SellingRestriction, String> {

	/**
	 * This method finds all selling restrictions ordered by restriction description.
	 *
	 * @return List of selling Restrictions ordered by description.
	 */
	List<SellingRestriction> findAllByOrderByRestrictionDescription();
}
