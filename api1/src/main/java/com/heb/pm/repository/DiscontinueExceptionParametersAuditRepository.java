/*
 *
 *  DiscontinueExceptionParametersAuditRepository
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

import com.heb.pm.entity.DiscontinueExceptionParametersAudit;
import com.heb.pm.entity.DiscontinueExceptionParametersAuditKey;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 *  Repository for product discontinue Exception Parameters Audit.
 *
 * @author s573181
 * @since 2.0.3
 */
public interface DiscontinueExceptionParametersAuditRepository extends JpaRepository<DiscontinueExceptionParametersAudit, DiscontinueExceptionParametersAuditKey> {

	/**
	 * Returns a list of DiscontinueExceptionParameters that matches an exception type and exception type id, sorted based
	 * on the sort type sent.
	 *
	 * @param exceptionType The type of exceptions to search for.
	 * @param exceptionTypeId The id of the exception to search for.
	 * @return A list of DiscontinueExceptionParameters that match given type and id.
	 */
	List<DiscontinueExceptionParametersAudit> findByExceptionTypeAndExceptionTypeId(@Param("exceptionType") String exceptionType,
																					@Param("exceptionTypeId") String exceptionTypeId, Sort sort);
}
