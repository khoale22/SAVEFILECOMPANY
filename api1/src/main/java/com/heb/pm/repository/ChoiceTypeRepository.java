/*
 * ChoiceTypeRepository
 *
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
package com.heb.pm.repository;

import com.heb.pm.entity.ChoiceType;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * JPA Repository for Choice Type.
 *
 * @author vn70516
 * @since 2.12.0
 */
public interface ChoiceTypeRepository extends JpaRepository<ChoiceType, String> {

}
