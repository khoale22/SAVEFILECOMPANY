/*
 * FulfillmentChannelRepository
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.repository;

import com.heb.pm.entity.FulfillmentChannel;
import com.heb.pm.entity.FulfillmentChannelKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository to retrieve information about an fulfillment channel.
 *
 * @author vn70516
 * @since 2.14.0
 */
public interface FulfillmentChannelRepository extends JpaRepository<FulfillmentChannel, FulfillmentChannelKey> {

    List<FulfillmentChannel> findByKeySalesChannelCodeOrderByDescription(String salesChannelCode);
}
