/*
 * CandidateStatusRepository
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
package com.heb.pm.repository;

import com.heb.pm.entity.CandidateStatus;
import com.heb.pm.entity.CandidateStatusKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author vn87351
 * @since 2.12.0
 */
public interface CandidateStatusRepository extends JpaRepository<CandidateStatus, CandidateStatusKey> {
	/**
	 * find first candidate work request by id and sort by last update time descending
	 * @param psWorkId
	 * @return Candidate Status
	 * @author vn87351
	 */
	CandidateStatus findFirstByKeyWorkRequestIdOrderByKeyLastUpdateDateDesc(Long psWorkId);
}
