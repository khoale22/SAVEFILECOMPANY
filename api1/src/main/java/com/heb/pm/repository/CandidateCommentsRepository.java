/*
 *  CandidateCommentsRepository
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.repository;

import com.heb.pm.entity.CandidateComments;
import com.heb.pm.entity.CandidateCommentsKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * This repository provides CRUD access to the Candidate Comments information.
 *
 * @author vn40486
 * @since 2.15.0
 */
public interface CandidateCommentsRepository extends JpaRepository<CandidateComments,CandidateCommentsKey> {

    List<CandidateComments> findByKeyWorkRequestIdOrderByTimeDesc(Long workRequestId);

    @Query("SELECT max(c.key.sequenceNumber) FROM CandidateComments c WHERE c.key.workRequestId = :workRequestId")
    Integer getMaxSequenceNumber(@Param("workRequestId") Long workRequestId);
}
