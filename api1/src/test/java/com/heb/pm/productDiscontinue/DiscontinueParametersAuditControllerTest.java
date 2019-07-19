/*
 *
 *  DiscontinueParametersAuditControllerTest
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

import com.heb.util.controller.NonEmptyParameterValidator;
import com.heb.util.controller.UserInfo;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import testSupport.CommonMocks;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by l730832 on 9/27/2016.
 */
public class DiscontinueParametersAuditControllerTest {

	@InjectMocks
	private DiscontinueParametersAuditController discontinueParametersAuditController;

	@Mock
	private UserInfo userInfo;

	@Mock
	private NonEmptyParameterValidator parameterValidator;

	@Mock
	private DiscontinueParametersAuditService discontinueParametersAuditService;

	private List<DiscontinueParametersAuditRecord> discontinueParametersAuditRecordList;
	private List<DiscontinueRules> discontinueRulesList;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		discontinueParametersAuditRecordList = new ArrayList<>();
		discontinueRulesList = new ArrayList<>();
	}

	@Test
	public void testGetExceptionParameterAudits() {
		Mockito.when(this.discontinueParametersAuditService.findByExceptionTypeAndExceptionTypeId(Mockito.anyString(), Mockito.anyString())).thenReturn(discontinueParametersAuditRecordList);

		Assert.assertNotNull("Results are null", discontinueParametersAuditRecordList);
		Assert.assertNotNull("Results are null", discontinueParametersAuditController.getExceptionParameterAudits("exceptionType", "exceptionTypeId", CommonMocks.getServletRequest()));
	}

	@Test
	public void testGetParameterAudits() {
		Mockito.when(this.discontinueParametersAuditService.findAllAuditParameters()).thenReturn(discontinueParametersAuditRecordList);

		Assert.assertNotNull("Results are null", discontinueParametersAuditRecordList);
		Assert.assertNotNull("Results are null", discontinueParametersAuditController.getParameterAudits(CommonMocks.getServletRequest()));
	}

	@Test
	public void testGetDeletedParameterAudits() {
		Mockito.when(this.discontinueParametersAuditService.findAllDeletedExceptionParametersAudit()).thenReturn(discontinueRulesList);

		Assert.assertNotNull("Results are null", discontinueRulesList);
		Assert.assertNotNull("Results are null", discontinueParametersAuditController.getDeletedParameterAudits(CommonMocks.getServletRequest()));
	}
}
