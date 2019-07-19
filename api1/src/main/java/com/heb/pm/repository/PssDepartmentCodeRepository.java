/*
 *  ControlTableRepository
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *  
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.repository;

import com.heb.pm.entity.PssDepartmentCode;
import com.heb.pm.entity.PssDepartmentCodeKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * This is the repository for a control table.
 *
 * @author l730832
 * @since 2.8.0
 */
public interface PssDepartmentCodeRepository extends JpaRepository<PssDepartmentCode, PssDepartmentCodeKey> {

   /**
     * Searches for a PssDepartmentCode by list of pss department id.
     *
     * @param ids The list of pss department id.
     * @return A list of pss department code matching the search.
     */
    List<PssDepartmentCode> findByKeyIdIn(List<Integer> ids);
}
