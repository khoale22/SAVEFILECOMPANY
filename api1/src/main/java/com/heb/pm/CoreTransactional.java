/*
 * CoreTransactional
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm;


import org.springframework.transaction.annotation.Transactional;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Since the application has multiple transaction managers, this is a convenience annotation that allows you to
 * annotate a method as transactional using the core transaction manager (the one that is the main datasource
 * for the application).
 *
 * @author d116773
 * @since 2.0.2
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Transactional(transactionManager = "jpaTransactionManager")
public @interface CoreTransactional {
}
