/*
 * GoodsProductAuditRepository
 *
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.repository;

import com.heb.pm.entity.GoodsProductAudit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.HashSet;
import java.util.List;

/**
 * This repository is intended to get all change records on the goods product table based on the search criteria
 * @version 2.15.0
 */
public interface GoodsProductAuditRepository extends JpaRepository<GoodsProductAudit, HashSet<Long>> {
    /**
     * Retrieves all records of changes on an goods product attributes
     * @param prodId - product ID you are searching for
     * @return - GoodsProductAudit's changes
     */
    List<GoodsProductAudit> findByKeyProdIdOrderByKeyChangedOn(@Param("id") Long prodId);
}
