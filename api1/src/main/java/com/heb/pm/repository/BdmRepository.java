/*
 * BdmRepository
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.repository;

import com.heb.pm.entity.Bdm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * JPA repository for Bdm.
 *
 * @author m314029
 * @since 2.0.6
 */
public interface BdmRepository extends JpaRepository<Bdm, String> {

    /**
     * Find all bdms that has displayname contains search string.
     *
     * @param searchString the search string.
     * @return Lisy of bdm has displayname contains search string
     */
    @Query(value = "select bdm from Bdm bdm where UPPER(concat(trim(bdm.fullName),'[',trim(bdm.bdmCode),']')) "
            + "like concat('%',FUNCTION('REPLACE',:searchString,'%','/%') ,'%')" )
    List<Bdm> findBdmBySearchString(@Param("searchString") String searchString);
}
