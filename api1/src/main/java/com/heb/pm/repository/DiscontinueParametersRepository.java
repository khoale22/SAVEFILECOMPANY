/*
 *
 *  DiscontinueParametersRepository
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

import com.heb.pm.entity.DiscontinueParameters;
import com.heb.pm.entity.DiscontinueParametersKey;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for to access default discontinue parameters.
 *
 * @author s573181
 * @since 2.0.2
 */
public interface DiscontinueParametersRepository
		extends JpaRepository<DiscontinueParameters, DiscontinueParametersKey>{
}
