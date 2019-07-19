/*
 * AlertRecipientRepository
 *
 *  Copyright (c) 2018 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
package com.heb.pm.repository;
import com.heb.pm.entity.AlertRecipient;
import com.heb.pm.entity.AlertRecipientKey;
import org.springframework.data.jpa.repository.JpaRepository;
/**
 * JPA Repository for AlertRecipientRepository.
 *
 * @author vn70529
 * @since 2.17.0
 */
public interface AlertRecipientRepository extends JpaRepository<AlertRecipient, AlertRecipientKey> {
}
