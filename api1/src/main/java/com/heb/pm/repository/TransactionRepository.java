/*
 * TransactionRepository
 *
 *  Copyright (c) 2018 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
package com.heb.pm.repository;

import com.heb.pm.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * The JPA repository for the Transaction.
 *
 * @author vn70529
 * @since 2.23.0
 */
public interface TransactionRepository extends JpaRepository<Transaction, String> {

}
