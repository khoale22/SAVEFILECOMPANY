/*
 * AlertCommentRepository
 *
 *  Copyright (c) 2018 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
package com.heb.pm.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import com.heb.pm.entity.AlertComment;
import com.heb.pm.entity.AlertCommentKey;

/**
 * JPA Repository for AlertCommentRepository.
 *
 * @author vn40486
 * @since 2.16.0
 */
public interface AlertCommentRepository extends JpaRepository<AlertComment, AlertCommentKey>{

    /**
     * Fetches notes or comments tagged to an alert/task id.
     * @param alertID alert or task id.
     * @return list of alert comments/notes.
     */
    List<AlertComment> findByKeyAlertIDOrderByCreateTimeDesc(Integer alertID);
}