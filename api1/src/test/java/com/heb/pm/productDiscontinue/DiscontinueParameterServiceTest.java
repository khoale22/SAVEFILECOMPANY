/*
 *
 *  * Test class for DiscontinueExceptionService
 *  *
 *  *  Copyright (c) 2016 HEB
 *  *  All rights reserved.
 *  *
 *  *  This software is the confidential and proprietary information
 *  *  of HEB.
 *  *
 *
 */

package com.heb.pm.productDiscontinue;

import com.heb.pm.entity.DiscontinueParameters;
import com.heb.pm.entity.DiscontinueParametersAudit;
import com.heb.pm.repository.DiscontinueParametersAuditRepository;
import com.heb.pm.repository.DiscontinueParametersRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by s573181 on 6/27/2016.
 */
public class DiscontinueParameterServiceTest {

	/*
	 * This class really just farms out it's work to other objects. The test class will test that it is farmed out
	 * correctly, not that the other classes work correctly. Those checks will be done in the tests for those classes.
	 */
	@InjectMocks
	private DiscontinueParametersService discontinueParametersService;

	@Mock
	private DiscontinueParametersRepository discontinueParametersRepository;

	@Mock
	private DiscontinueParametersToDiscontinueRules converter;

	@Mock
	private DiscontinueParametersAuditRepository discontinueParametersAuditRepository;

	private List<DiscontinueParameters> discontinueParametersList;
	private DiscontinueRules discontinueRules;
	private List<DiscontinueParametersAudit> parametersAuditList;

	@Before
	public void setup(){
		MockitoAnnotations.initMocks(this);
		discontinueRules = new DiscontinueRules();
		discontinueParametersList = new ArrayList<>();
		parametersAuditList = new ArrayList<>();
	}


	/**
	 * Tests FindAll
	 */
	@Test
	public void findAllTest() {
		Mockito.when(this.discontinueParametersRepository.findAll()).thenReturn(discontinueParametersList);
		Mockito.when(this.converter.toDiscontinueRules(Mockito.anyList())).thenReturn(discontinueRules);

		Assert.assertEquals("Results aren't equal", discontinueRules, this.discontinueParametersService.findAll());
	}

	/**
	 * Tests Update
	 */
	@Test
	public void updateTest() {
		Mockito.when(discontinueParametersRepository.findAll()).thenReturn(discontinueParametersList);
		Mockito.when(discontinueParametersRepository.save(Mockito.anyList())).thenReturn(discontinueParametersList);

		Assert.assertNotNull("Results are null", discontinueParametersList);

		Mockito.when(this.discontinueParametersAuditRepository.save(Mockito.anyList())).thenReturn(parametersAuditList);

		Assert.assertNotNull("Results are null", this.discontinueParametersService.update(discontinueRules));
	}
}
