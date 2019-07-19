/*
 * AlertUserResponseRepository.java
 *
 *  Copyright (c) 2018 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
package com.heb.pm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.heb.pm.entity.AlertUserResponse;
import com.heb.pm.entity.AlertUserResponseKey;
/**
 * JPA Repository for AlertUserResponse entity.
 *
 * @author vn40486
 * @since 2.16.0
 */
public interface AlertUserResponseRepository extends JpaRepository<AlertUserResponse, AlertUserResponseKey>{}