/*
 * SourceSystemService.java
 *
 * Copyright (c) 2018 HEB
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of HEB.
 */
package com.heb.pm.sourceSystem;

import com.heb.pm.entity.SourceSystem;
import com.heb.pm.repository.SourceSystemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * REST endpoint for Source System.
 *
 * @author s769046
 * @since 2.21.0
 */

@Service
public class SourceSystemService {

    @Autowired
    private SourceSystemRepository sourceSystemRepository;

    /**
     * Method to get list of source systems.
     *
     * @return list of source systems.
     */

    public List<SourceSystem> findAllSourceSystems() {
        return this.sourceSystemRepository.findAll();
    }
}
