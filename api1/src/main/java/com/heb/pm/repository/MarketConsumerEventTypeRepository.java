/*
 * MarketConsumerEventTypeRepository
 *
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
package com.heb.pm.repository;

import com.heb.pm.entity.MarketConsumerEventType;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * JPA Repository for MarketConsumerEventType.
 *
 * @author vn70529
 * @since 2.12.0
 */
public interface MarketConsumerEventTypeRepository extends JpaRepository<MarketConsumerEventType, String>{

}
