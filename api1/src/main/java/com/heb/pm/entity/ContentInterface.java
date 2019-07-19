/*
 *  ContentInterface
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.entity;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * This Interface is used to implement the type of the content for response object.
 *
 * @author vn70529
 * @since 2.14.0
 */
@JsonTypeInfo(use=JsonTypeInfo.Id.NAME, include= JsonTypeInfo.As.PROPERTY, property="@type")
@JsonSubTypes({
		@JsonSubTypes.Type(value=ECommerceViewAttributePriority.ContentSpecification.class, name="ContentSpecification"),
		@JsonSubTypes.Type(value=ECommerceViewAttributePriority.ContentDimension.class, name="ContentDimension"),
		@JsonSubTypes.Type(value=ECommerceViewAttributePriority.ContentString.class, name="ContentString")
})
public interface ContentInterface<T>{
	T getContent();
}
