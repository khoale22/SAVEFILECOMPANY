/*
 *
 *  DiscontinueExceptionParametersAuditService
 *
 *   Copyright (c) 2016 HEB
 *   All rights reserved.
 *
 *   This software is the confidential and proprietary information
 *    of HEB.
 *
 *
 */

package com.heb.pm.productDiscontinue;

import com.heb.pm.entity.*;
import com.heb.pm.repository.DiscontinueExceptionParametersAuditRepository;
import com.heb.pm.repository.DiscontinueParametersAuditRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Holds all business logic related to audit exceptions to the default product discontinue parameters.
 *
 * @author s573181
 * @since 2.0.3
 */
@Service
public class DiscontinueParametersAuditService {

	private static final Logger logger = LoggerFactory.getLogger(DiscontinueParametersAuditService.class);

	@Autowired
	private DiscontinueParametersToDiscontinueRules converter;

	@Autowired
	private DiscontinueParametersAuditRepository discontinueParametersAuditRepository;

	@Autowired
	private DiscontinueExceptionParametersAuditRepository discontinueExceptionParametersAuditRepository;

	/**
	 * Returns a list of DiscontinueParametersAuditRecords from their Exception Parameters Audit.
	 *
	 * @param exceptionType the type of exception, such as vendor, upd..
	 * @param exceptionTypeId  the Id string that corresponds to the exceptionType.
	 * @return  a list of DiscontinueParametersAuditRecords.
	 */
	List<DiscontinueParametersAuditRecord> findByExceptionTypeAndExceptionTypeId(String exceptionType, String exceptionTypeId){
		return this.converter.convertExceptionParametersAuditToDiscontinueParametersAuditRecord(
				this.discontinueExceptionParametersAuditRepository.findByExceptionTypeAndExceptionTypeId(exceptionType, exceptionTypeId, DiscontinueExceptionParametersAudit.getDefaultSort()));
	};

	/**
	 * Returns all audit parameters as a List of DiscontinueParametersAuditRecords.
	 *
	 * @return a List of DiscontinueParametersAuditRecord.
	 */
	public List<DiscontinueParametersAuditRecord> findAllAuditParameters() {
		return this.converter.convertDiscontinueParametersAuditToDiscontinueParametersAuditRecord(this.discontinueParametersAuditRepository.findAll(DiscontinueParametersAudit.getDefaultSort()));
	}

	/**
	 * Returns all Exception Parameters that were deleted as a list of DiscontinueParametersAuditRecords.
	 *
	 * @return a list of DiscontinueParametersAuditRecords.
	 */
	public List<DiscontinueRules> findAllDeletedExceptionParametersAudit() {
		return this.converter.convertDeletedExceptionParametersAuditToDiscontinueRules(this.discontinueExceptionParametersAuditRepository.findAll(DiscontinueExceptionParametersAudit.getDefaultSort()));
	}
}
