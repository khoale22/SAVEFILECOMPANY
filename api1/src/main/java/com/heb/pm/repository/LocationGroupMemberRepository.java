/*
 * LocationGroupMemberRepository.java
 *
 *  Copyright (c) 2018 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
package com.heb.pm.repository;

import com.heb.pm.entity.LocationGroupMember;
import com.heb.pm.entity.LocationGroupMemberKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * JPA Repository for LocationGroupMember entity.
 *
 * @author vn70529
 * @since 2.23.0
 */
public interface LocationGroupMemberRepository extends JpaRepository<LocationGroupMember, LocationGroupMemberKey>{
    /**
     * Query to find LocationGroupMember object by custLocationNumber and splrLocationNumber and splrLocationTypeCode == 'D' and custLocationTypeCode = 'S'.
     */
    String FIND_LOCATION_GROUP_NUMBER_QUERY = "SELECT lgm FROM LocationGroupMember lgm" +
            " WHERE lgm.key.splrLocationTypeCode = 'D'" +
            " and lgm.key.custLocationTypeCode ='S'" +
            " and lgm.key.custLocationNumber =:custLocationNumber" +
            " and lgm.key.splrLocationNumber =:splrLocationNumber";
    @Query(FIND_LOCATION_GROUP_NUMBER_QUERY)
    List<LocationGroupMember> findByKeyCustLocationNumberAndKeySplrLocationNumber(@Param("custLocationNumber") Integer custLocationNumber, @Param("splrLocationNumber") Integer splrLocationNumber);
}