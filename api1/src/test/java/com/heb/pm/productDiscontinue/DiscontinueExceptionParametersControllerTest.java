/*
 *
 *  * DiscontinueExceptionControllerTest
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

import com.heb.util.controller.ModifiedEntity;
import com.heb.util.controller.NonEmptyParameterValidator;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;
import org.springframework.context.MessageSource;
import testSupport.CommonMocks;

/**
 * Tests DiscontinueExceptionParametersController.
 *
 * @author s573181
 * @since 2.0.2
 */

public class DiscontinueExceptionParametersControllerTest {


	/*
	 * updateRule
	 */

	/**
	 * Tests updateRule with valid DiscontinueRule set.
	 */
	@Test
	public void updateRuleWithValidRule(){

		DiscontinueExceptionParametersController controller = new DiscontinueExceptionParametersController();

		DiscontinueExceptionParametersService service = Mockito.mock(DiscontinueExceptionParametersService.class);
		Mockito.doAnswer(
				this.getAddOrUpdateAnswer(this.getDefaultRulesObjectInMonths()))
				.when(service).update(this.getDefaultRulesObjectInMonths());

		controller.setService(service);
		controller.setUserInfo(CommonMocks.getUserInfo());
		controller.setMessageSource(Mockito.mock(MessageSource.class));

		ModifiedEntity result
				= controller.updateRule(this.getDefaultRulesObjectInMonths(), CommonMocks.getServletRequest());

		Assert.assertNotNull("results are null", result);
		Assert.assertNotNull("data are null", result.getData());
	}

	/*
	 * addRule
	 */

	/**
	 * Tests addRule with valid DiscontinueRule set.
	 */
	@Test
	public void addRuleWithValidRule(){

		DiscontinueExceptionParametersController controller = new DiscontinueExceptionParametersController();

		DiscontinueExceptionParametersService service = Mockito.mock(DiscontinueExceptionParametersService.class);
		Mockito.doAnswer(
				this.getAddOrUpdateAnswer(this.getDefaultRulesObjectInMonths()))
				.when(service).add(this.getDefaultRulesObjectInMonths());

		controller.setService(service);
		controller.setUserInfo(CommonMocks.getUserInfo());
		controller.setMessageSource(Mockito.mock(MessageSource.class));

		ModifiedEntity result
				= controller.addRule(this.getDefaultRulesObjectInMonths(), CommonMocks.getServletRequest());

		Assert.assertNotNull("results are null", result);
		Assert.assertNotNull("data are null", result.getData());
	}

	/*
	 * deleteRule
	 */

	/**
	 * Tests deleteRule with valid exception id.
	 */
	@Test
	public void deleteRuleWithValidExceptionId(){

		DiscontinueExceptionParametersController controller = new DiscontinueExceptionParametersController();

		DiscontinueExceptionParametersService service = Mockito.mock(DiscontinueExceptionParametersService.class);
		Mockito.doNothing()
				.when(service).delete(this.getDefaultRulesObjectInMonths().getExceptionNumber());

		controller.setService(service);
		controller.setUserInfo(CommonMocks.getUserInfo());
		controller.setMessageSource(Mockito.mock(MessageSource.class));
		controller.setParameterValidator(Mockito.mock(NonEmptyParameterValidator.class));

		ModifiedEntity result
				= controller.deleteRule(
				this.getDefaultRulesObjectInMonths().getExceptionNumber(), CommonMocks.getServletRequest());

		Assert.assertNotNull("results are null", result);
		Assert.assertNotNull("data are null", result.getData());
	}

	/**
	 * Tests deleteRule with null exception id.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void deleteRuleWithNullExceptionId(){

		DiscontinueExceptionParametersController controller = new DiscontinueExceptionParametersController();

		NonEmptyParameterValidator validator = Mockito.mock(NonEmptyParameterValidator.class);
		Mockito.doThrow(IllegalArgumentException.class)
				.when(validator).validate((Number)null, null, null, null);
		controller.setParameterValidator(new NonEmptyParameterValidator());

		controller.deleteRule(
				null, CommonMocks.getServletRequest());
	}

	/*
	 * Support functions.
	 */

	/**
	 * Mocks up the call to modification (add or update) on the service.
	 */
	private Answer<DiscontinueRules> getAddOrUpdateAnswer (DiscontinueRules rule){
		return invocation -> {
			Assert.assertEquals(rule, invocation.getArguments()[0]);
			return rule;
		};
	}

	/**
	 *  This Function creates a default DiscontinueRules with the numeric params in number of months values.
	 * @return DiscontinueRules.
	 */
	private DiscontinueRules getDefaultRulesObjectInMonths() {

		DiscontinueRules exceptionObject = new DiscontinueRules();
		exceptionObject.setStoreSales("12");
		exceptionObject.setSalesSwitch(true);
		exceptionObject.setNewItemPeriod("12");
		exceptionObject.setNewProductSetupSwitch(true);
		exceptionObject.setWarehouseUnits("0");
		exceptionObject.setWarehouseUnitSwitch(true);
		exceptionObject.setStoreUnits("0");
		exceptionObject.setStoreUnitSwitch(true);
		exceptionObject.setStoreReceipts("18");
		exceptionObject.setReceiptsSwitch(true);
		exceptionObject.setPurchaseOrders("18");
		exceptionObject.setPurchaseOrderSwitch(true);
		exceptionObject.setExceptionNumber(1);
		exceptionObject.setNeverDiscontinueSwitch(false);
		exceptionObject.setExceptionType("Vendor");
		exceptionObject.setExceptionTypeId("50016");
		return exceptionObject;
	}
}
