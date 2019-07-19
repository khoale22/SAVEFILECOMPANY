package com.heb.pm.repository;

import com.heb.pm.entity.HierarchyContext;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository to retrieve information about hierarchy contexts.
 *
 * @author m314029
 * @since 2.12.0
 */
public interface HierarchyContextRepository extends JpaRepository<HierarchyContext, String> {

	/**
	 * Returns a list of hierarchy contexts that do not match the given ids.
	 *
	 * @param idsNot Hierarchy context ids to not include in the search.
	 * @return List of hierarchy contexts that do not match the given id.
	 */
	List<HierarchyContext> findByIdNotIn(List<String> idsNot);

	/**
	 * Returns a list of hierarchy contexts match the given id (ignoring case). This is because users were allowed
	 * to create hierarchies with ids of any case ('CUST' and 'Cust' could exist).
	 *
	 * @param id Hierarchy context id search for.
	 * @return List of hierarchy contexts match the given id ignoring.
	 */
	List<HierarchyContext> findByIdIgnoreCase(String id);
}
