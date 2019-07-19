/*
 *
 *  DiscontinueExceptionParametersRepository
 *
 *   Copyright (c) 2016 HEB
 *   All rights reserved.
 *
 *   This software is the confidential and proprietary information
 *    of HEB.
 *
 *
 */

package com.heb.pm.repository;

import com.heb.pm.CoreTransactional;
import com.heb.pm.entity.DiscontinueExceptionParameters;
import com.heb.pm.entity.DiscontinueExceptionParametersKey;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Repository for product discontinue exception rules.
 *
 * @author s573181
 * @since 2.0.2
 */
public interface DiscontinueExceptionParametersRepository
		extends JpaRepository<DiscontinueExceptionParameters, DiscontinueExceptionParametersKey> {

	String MAX_SEQUENCE_NUMBER_SQL =
			"select max(dep.key.exceptionNumber) from DiscontinueExceptionParameters dep";

	String DELETE_BY_EXCEPTION_NUBMER_SQL =
			"delete from DiscontinueExceptionParameters dep where dep.key.exceptionNumber = :exceptionNumber";

	String FIND_BY_EXCEPTION_NUBMER_SQL =
			"select dep from DiscontinueExceptionParameters dep where dep.key.exceptionNumber = :exceptionNumber";
	/**
	 * Returns a list of DiscontinueExceptionParameters filtered by type.
	 *
	 * @param exceptionType The type of exceptions to filter on.
	 * @param sort The sort order to use when pulling the result set.
	 * @return A list of DiscontinueExceptionParameters filtered by type.
	 */
	List<DiscontinueExceptionParameters> findByExceptionType(@Param("exceptionType") String exceptionType,
															  Sort sort);

	/**
	 * Returns a list of DiscontinueExceptionParameters that matches an exception type and exception type id.
	 *
	 * @param exceptionType The type of exceptions to search for.
	 * @param exceptionTypeId The id of the exception to search for.
	 * @return A list of DiscontinueExceptionParameters that match given type and id.
	 */
	List<DiscontinueExceptionParameters> findByExceptionTypeAndExceptionTypeId(@Param("exceptionType") String exceptionType,
																  @Param("exceptionTypeId") String exceptionTypeId);

	/**
	 * Returns the maximum exception sequence number in the table.
	 *
	 * @return The maximum exception sequence number in the table.
	 */
	@Query(DiscontinueExceptionParametersRepository.MAX_SEQUENCE_NUMBER_SQL)
	int getMaxExceptionSequenceNumber();

	/**
	 * Deletes all the exceptions with a given exception sequence number (excp_seq_nbr).
	 *
	 * @param exceptionNumber The exception sequence number to delete.
	 */
	@Modifying
	@CoreTransactional
	@Query(DiscontinueExceptionParametersRepository.DELETE_BY_EXCEPTION_NUBMER_SQL)
	void deleteByExceptionNumber(@Param("exceptionNumber") int exceptionNumber);

	/**
	 * Returns a list of DiscontinueExceptionParameters that matches an exception type and exception type id.
	 *
	 * @param exceptionNumber The exception sequence number (that corresponds to an exception) to search for.
	 * @return A list of DiscontinueExceptionParameters that match given exception sequence number.
	 */
	@Query(DiscontinueExceptionParametersRepository.FIND_BY_EXCEPTION_NUBMER_SQL)
	List<DiscontinueExceptionParameters> findByExceptionNumber(@Param("exceptionNumber") int exceptionNumber);
}
