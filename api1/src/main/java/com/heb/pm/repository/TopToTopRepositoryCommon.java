/*
 * TopToTopRepositoryCommon.java
 *
 *  Copyright (c) 2019 H-E-B
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
package com.heb.pm.repository;

/**
 * Common constants for the count and non-count TopToTop JPA repository implementations.
 *
 * @author vn73545
 * @since 2.41.0
 */
public interface TopToTopRepositoryCommon {
	/**
     * SQL statement that filter TOP TO TOP by name or id.
     */
    String FIND_TOP_TO_TOP_BY_ID_AND_DESCRIPTION_SQL =
            "from TopToTop topToTop " +
            "where (topToTop.topToTopId like concat('%', :id, '%') " +
            "or upper(topToTop.topToTopName) like concat('%', upper(:name), '%')) ";
    /**
     * SQL statement that filter TOP TO TOP by id.
     */
    String FIND_BY_TOP_TO_TOP_ID_SQL = "from TopToTop topToTop " +
            "where topToTop.topToTopId like concat('%', :id, '%')";
    /**
     * SQL statement that filter TOP TO TOP by name and id.
     */
    String FIND_BY_TOP_TO_TOP_ID_AND_TOP_TO_TOP_NAME_SQL = "from TopToTop topToTop " +
            "where topToTop.topToTopId like concat('%', :id, '%') " +
            "and upper(topToTop.topToTopName) like concat('%', upper(:name), '%')";
    /**
     * SQL statement that filter TOP TO TOP by name.
     */
    String FIND_BY_TOP_TO_TOP_NAME_SQL = "from TopToTop topToTop " +
    		"where upper(trim(topToTop.topToTopName)) = upper(trim(:name))";
}
