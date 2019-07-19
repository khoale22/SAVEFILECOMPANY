/*
 * com.heb.pm.entity.ProductDiscontinue
 *
 * Copyright (c) 2017 HEB
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of HEB.
 */package com.heb.pm.ecommerce;

import com.heb.pm.entity.TargetSystemAttributePriority;
import com.heb.pm.repository.TargetSystemAttributePriorityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Holds business logic related to ECommerce.
 *
 * @author s753601
 * @since 2.5.0
 */
@Service
public class EcommerceService {

    @Autowired
    TargetSystemAttributePriorityRepository targetSystemAttributePriorityRepository;

    /**
     * Returns a list of Target System Attribute Priorities grouped by attribute and ordered by priority number
     * @return
     */
    public List<TargetSystemAttributePriority> getSourcePriorityTable(){
        List<TargetSystemAttributePriority> sourcePriorities = this.targetSystemAttributePriorityRepository.findSourcePriorities();
        return sourcePriorities;
    }
}
