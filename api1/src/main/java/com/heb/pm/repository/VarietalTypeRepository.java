/*
 * VarietalTypeRepository
 *
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.repository;

import com.heb.pm.entity.VarietalType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Interface to the  Varietal Type info.
 *
 * @author vn87351
 * @since 2.12.0
 */
public interface VarietalTypeRepository extends JpaRepository<VarietalType, Integer> {

}
