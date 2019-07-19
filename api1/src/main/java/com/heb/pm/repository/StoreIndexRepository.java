/*
 * StoreIndexRepository
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.repository;

import com.heb.pm.entity.Store;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Repository to access information from the store index.
 *
 * @author d116773
 * @since 2.0.2
 */
public interface StoreIndexRepository extends ElasticsearchRepository<Store, Long> {
}
