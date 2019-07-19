/*
 *  DiscontinueParametersControllerTest
 *
 *   Copyright (c) 2016 HEB
 *   All rights reserved.
 *
 *   This software is the confidential and proprietary information
 *    of HEB.
 */

package com.heb.pm.productDiscontinue;


import com.heb.pm.entity.DiscontinueParameters;
import com.heb.pm.repository.DiscontinueParametersRepository;
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
 *
 * Test class to unit test the DiscontinueParametersController class
 */
public class DiscontinueParametersControllerTest {

	@InjectMocks
	private DiscontinueParametersController discontinueParametersController;

	@Mock
	private DiscontinueParametersService discontinueParametersService;

	@Mock
	private DiscontinueParametersRepository discontinueParametersRepository;

	@Mock
	private DiscontinueParametersToDiscontinueRules converter;

	@Mock
	private UserInfo userInfo;

	@Mock
	private NonEmptyParameterValidator parameterValidator;

	private DiscontinueRules discontinueRules;
	private List<DiscontinueParameters> discontinueParametersList;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		discontinueRules = new DiscontinueRules();
		discontinueParametersList = new ArrayList<>();
	}

	/**
	 * Test the find all function
	 */
	@Test
	public void testFindAll() {
		Mockito.when(this.discontinueParametersRepository.findAll()).thenReturn(discontinueParametersList);
		Mockito.when(this.converter.toDiscontinueRules(Mockito.anyList())).thenReturn(discontinueRules);
		Mockito.when(discontinueParametersService.findAll()).thenReturn(discontinueRules);

		Assert.assertEquals("Results aren't equal", discontinueRules, this.discontinueParametersService.findAll());
		Assert.assertNotNull("Result is null", discontinueParametersController.findAll(CommonMocks.getServletRequest()));
	}

	/**
	 * Tests the update all function
	 */
	@Test
	public void testUpdateAll() {
		Mockito.when(this.discontinueParametersService.update(discontinueRules)).thenReturn(discontinueRules);

		Assert.assertNotNull(this.discontinueParametersController.updateAll(discontinueRules, CommonMocks.getServletRequest()));

	}
}