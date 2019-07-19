package com.heb.pm.repository;

import com.heb.pm.entity.SearchCriteria;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for SearchCriteria entities.
 *
 * @author d116773
 * @since 2.13.0
 */
public interface SearchCriteriaRepository extends JpaRepository<SearchCriteria, String> {

	/**
	 * Returns the number of records in the search criteria table that contain a supplier ID.
	 *
	 * @param sessionId The session ID to look for.
	 * @return The number of records with that ID.
	 */
	long countBySessionId(String sessionId);

	/**
	 * Clears out the temp table for a given session ID.
	 *
	 * @param sessionId The session ID to clear data for.
	 */
	void deleteBySessionId(String sessionId);
}
