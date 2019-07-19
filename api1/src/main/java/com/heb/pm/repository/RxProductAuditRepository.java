/*
 * RxProductAuditRepository
 *
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.repository;

import com.heb.pm.entity.RxProductAudit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.HashSet;
import java.util.List;

/**
 * This repository is intended to get all change records on the RX product audit table based on the search criteria
 * @version 2.15.0
 */
public interface RxProductAuditRepository extends JpaRepository<RxProductAudit, HashSet<Long>> {
    /**
     * Retrieves all records of changes on an RX product audit attributes
     * @param prodId - product ID you are searching for
     * @return - RxProductAudit's changes
     */
    List<RxProductAudit> findByKeyProdIdOrderByKeyChangedOn(@Param("id") Long prodId);
}
