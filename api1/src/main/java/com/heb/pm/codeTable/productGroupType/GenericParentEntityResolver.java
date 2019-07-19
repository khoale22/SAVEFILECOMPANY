/*
 *  GenericParentEntityResolver.java
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.codeTable.productGroupType;

import com.heb.pm.entity.GenericEntityRelationship;
import com.heb.util.jpa.LazyObjectResolver;

/**
 * Resolves a GenericParentEntity object returned by the GenericEntityRelationship REST endpoint.
 *
 * @author vn70529
 * @since 2.15.0
 */
public class GenericParentEntityResolver implements LazyObjectResolver<GenericEntityRelationship> {

    /**
     * Performs the actual resolution of lazily loaded parameters.
     * @param genericEntityRelationship The object to resolve.
     */
    @Override
    public void fetch(GenericEntityRelationship genericEntityRelationship){
        genericEntityRelationship.getGenericParentEntity().getDisplayText();
    }
}
