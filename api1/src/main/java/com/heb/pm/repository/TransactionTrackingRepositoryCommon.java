/*
 * TransactionTrackingRepositoryCommon
 *
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
package com.heb.pm.repository;

import com.heb.pm.entity.TransactionTracker;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author vn87351
 * @since 2.12.0
 */
public interface TransactionTrackingRepositoryCommon {

	/**
	 * Query get list of user created tracking.
	 */
	String FIND_USER_CREATED_TRACKING = "SELECT distinct LOWER(trk.userId) " +
			"FROM TransactionTracker trk JOIN trk.candidateWorkRequest b " +
			"WHERE b.sourceSystem IN(:listSource) " +
			"AND trk.fileNm NOT IN (:listFileName) ";

}
