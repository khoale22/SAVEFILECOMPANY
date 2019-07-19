/*
 * DiscontinueParametersAuditServiceTest
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.productDiscontinue;

import com.heb.pm.entity.DiscontinueExceptionParametersAudit;
import com.heb.pm.entity.DiscontinueParametersAudit;
import com.heb.pm.repository.DiscontinueExceptionParametersAuditRepository;
import com.heb.pm.repository.DiscontinueParametersAuditRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by l730832 on 9/26/2016.
 */
public class DiscontinueParametersAuditServiceTest {

	@InjectMocks
	private DiscontinueParametersAuditService discontinueParametersAuditService;

	@Mock
	private DiscontinueParametersToDiscontinueRules converter;

	@Mock
	private DiscontinueParametersAuditRepository discontinueParametersAuditRepository;

	@Mock
	private DiscontinueExceptionParametersAuditRepository discontinueExceptionParametersAuditRepository;

	@Mock
	private DiscontinueExceptionParametersAudit discontinueExceptionParametersAudit;

	private List<DiscontinueRules> discontinueRules;
	private List<DiscontinueParametersAuditRecord> discontinueParametersAuditRecordList;
	private List<DiscontinueExceptionParametersAudit> auditEntityList;
	private List<DiscontinueParametersAudit> discontinueParametersAuditList;
	private List<DiscontinueExceptionParametersAudit> discontinueExceptionParametersAuditList;

	@Before
	public void setup(){
		MockitoAnnotations.initMocks(this);
		discontinueRules = new ArrayList<>();
		discontinueParametersAuditRecordList = new ArrayList<>();
		auditEntityList = new ArrayList<>();
		discontinueExceptionParametersAudit = new DiscontinueExceptionParametersAudit();
		discontinueParametersAuditList = new ArrayList<>();
		discontinueExceptionParametersAuditList = new ArrayList<>();
	}

	@Test
	public void testFindByExceptionTypeAndExceptionTypeId() {
		Mockito.when(this.converter.convertExceptionParametersAuditToDiscontinueParametersAuditRecord(Mockito.anyList()))
				.thenReturn(discontinueParametersAuditRecordList);

		Mockito.when(this.discontinueExceptionParametersAuditRepository
				.findByExceptionTypeAndExceptionTypeId(Mockito.anyString(),
						Mockito.anyString(),
						Mockito.any(Sort.class)))
				.thenReturn(auditEntityList);


		Assert.assertNotNull("Result is null", discontinueParametersAuditRecordList);
		Assert.assertEquals("Results aren't equal",
				discontinueParametersAuditRecordList,
				this.discontinueParametersAuditService
						.findByExceptionTypeAndExceptionTypeId("exceptionType", "exceptionTypeId"));
	}

	@Test
	public void testFindAllAuditParameters() {
		Mockito.when(this.converter.convertDiscontinueParametersAuditToDiscontinueParametersAuditRecord(Mockito.anyList()))
				.thenReturn(discontinueParametersAuditRecordList);

		Mockito.when(this.discontinueParametersAuditRepository.findAll(Mockito.any(Sort.class)))
				.thenReturn(discontinueParametersAuditList);

		Assert.assertNotNull("Result is null", this.discontinueParametersAuditService.findAllAuditParameters());

		Assert.assertEquals("Results aren't equal",
				discontinueParametersAuditRecordList,
				this.discontinueParametersAuditService.findAllAuditParameters());
	}

	@Test
	public void testFindAllDeletedExceptionParametersAudit() {
		Mockito.when(this.converter.convertDeletedExceptionParametersAuditToDiscontinueRules(Mockito.anyList()))
				.thenReturn(discontinueRules);

		Mockito.when(this.discontinueExceptionParametersAuditRepository.findAll(Mockito.any(Sort.class)))
				.thenReturn(discontinueExceptionParametersAuditList);

		Assert.assertNotNull("Result is null",
				this.discontinueParametersAuditService.findAllDeletedExceptionParametersAudit());

		Assert.assertEquals("Results aren't equal",
				discontinueRules,
				this.discontinueParametersAuditService.findAllDeletedExceptionParametersAudit());
	}
}
