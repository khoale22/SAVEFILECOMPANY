/*
 * ImagePrimaryRepository
 *
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
package com.heb.pm.repository;

import com.heb.pm.entity.ImagePrimary;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * JPA Repository for Choice Option Detail.
 *
 * @author vn86116
 * @since 2.12.0
 */
public interface ImagePrimaryRepository extends JpaRepository<ImagePrimary, String> {

}