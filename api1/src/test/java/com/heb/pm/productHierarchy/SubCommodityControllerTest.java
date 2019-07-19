package com.heb.pm.productHierarchy;

import com.heb.pm.entity.SubCommodity;
import com.heb.pm.entity.SubCommodityKey;
import com.heb.util.controller.NonEmptyParameterValidator;
import com.heb.util.jpa.PageableResult;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;
import testSupport.CommonMocks;

import java.util.ArrayList;
import java.util.List;

/**
 * Tests SubCommodityController.
 *
 * @author d116773
 * @since 2.0.2
 */
public class SubCommodityControllerTest {

	private static final String SEARCH_STRING = "SEARCH STRING";

	private static final int CLASS_CODE = 39;
	private static final int COMMODITY_CODE = 8222;
	private static final int SUB_COMMODITY_CODE = 9551;
	private static final String SUB_COMMODITY_NAME = "LEAN MULT SRV STUFD SNDWCH    ";

	/*
	 * findByRegularExpression.
	 */

	/**
	 * Tests findByRegularExpression with all the values.
	 */
	@Test
	public void findByRegularExpressionAllValues() {

		SubCommodityService service = this.getService(5, 100);
		SubCommodityController controller = new SubCommodityController();
		controller.setSubCommodityService(service);
		controller.setUserInfo(CommonMocks.getUserInfo());
		controller.setParameterValidator(new NonEmptyParameterValidator());

		controller.findSubCommoditiesByRegularExpression(SubCommodityControllerTest.SEARCH_STRING, 5, 100,
				CommonMocks.getServletRequest());
		// The tests are done in the answer.
	}

	/**
	 * Tests findByRegularExpression with a null search string.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void findByRegularExpressionNullSearchString() {

		SubCommodityService service = this.getService(5, 100);
		SubCommodityController controller = new SubCommodityController();
		controller.setSubCommodityService(service);
		controller.setUserInfo(CommonMocks.getUserInfo());
		controller.setParameterValidator(new NonEmptyParameterValidator());

		controller.findSubCommoditiesByRegularExpression(null, 5, 100,
				CommonMocks.getServletRequest());
	}

	/**
	 * Tests findByRegularExpression with null page and page size.
	 */
	@Test
	public void findByRegularExpressionNullPageAndSize() {

		SubCommodityService service = this.getService(0, 15);
		SubCommodityController controller = new SubCommodityController();
		controller.setSubCommodityService(service);
		controller.setUserInfo(CommonMocks.getUserInfo());
		controller.setParameterValidator(new NonEmptyParameterValidator());

		controller.findSubCommoditiesByRegularExpression(SubCommodityControllerTest.SEARCH_STRING, null, null,
				CommonMocks.getServletRequest());
		// The tests are done in the answer.
	}

	/*
	 * Support functions
	 */

	/**
	 * Returns a key to test with.
	 *
	 * @return A key to test with.
	 */
	private SubCommodityKey getTestKey() {

		SubCommodityKey key = new SubCommodityKey();
		key.setClassCode(SubCommodityControllerTest.CLASS_CODE);
		key.setCommodityCode(SubCommodityControllerTest.COMMODITY_CODE);
		key.setSubCommodityCode(SubCommodityControllerTest.SUB_COMMODITY_CODE);
		return key;
	}

	/**
	 * Returns a SubCommodity to test with.
	 *
	 * @return A SubCommodity to test with.
	 */
	private SubCommodity getTestSubCommodity() {

		SubCommodity subCommodity = new SubCommodity();
		subCommodity.setKey(this.getTestKey());
		subCommodity.setName(SubCommodityControllerTest.SUB_COMMODITY_NAME);
		return subCommodity;
	}

	/**
	 * Returns a PageableResult for the service to return.
	 *
	 * @return A PageableResult for the service to return.
	 */
	private PageableResult<SubCommodity> getResult() {
		List<SubCommodity> subCommodities = new ArrayList<>(1);
		subCommodities.add(this.getTestSubCommodity());

		return new PageableResult<>(0, 1, 1L, subCommodities);
	}

	/**
	 * Mocks a findByRegularExpression call. It checks to make sure the right parameters are passed in.
	 *
	 * @param page The page value to expect.
	 * @param pageSize The pageSize to expect.
	 * @return A mock for findByRegularExpression.
	 */
	private Answer<PageableResult<SubCommodity>> getFindByRegularExpressionAnswer(int page, int pageSize) {

		return invocation -> {
			Assert.assertEquals(SubCommodityControllerTest.SEARCH_STRING, invocation.getArguments()[0]);
			Assert.assertEquals(page, invocation.getArguments()[1]);
			Assert.assertEquals(pageSize, invocation.getArguments()[2]);
			return this.getResult();
		};
	}

	/**
	 * Mocks up a SubCommodityService to test with.
	 *
	 * @param page The page parameter to expect in findByRegularExpression.
	 * @param pageSize The pageSize parameter to expect in findByRegularExpression.
	 * @return A SubCommodityService to test with.
	 */
	private SubCommodityService getService(int page, int pageSize) {

		SubCommodityService service = Mockito.mock(SubCommodityService.class);
		Mockito.doAnswer(this.getFindByRegularExpressionAnswer(page, pageSize)).when(service)
				.findByRegularExpression(Mockito.anyString(), Mockito.anyInt(), Mockito.anyInt());

		return service;
	}
}
