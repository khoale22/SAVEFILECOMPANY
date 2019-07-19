/*
 * ClassCommodityServiceTest
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.productHierarchy;

import com.heb.pm.entity.ClassCommodity;
import com.heb.pm.entity.ClassCommodityKey;
import com.heb.pm.repository.CommodityIndexRepository;
import com.heb.util.jpa.PageableResult;
import org.junit.Assert;
import org.junit.Test;

import org.mockito.Mockito;
import org.mockito.stubbing.Answer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * Tests ClassCommodityService.
 *
 * @author d116773
 * @since 2.0.2
 */
public class ClassCommodityServiceTest {

	private static final String SEARCH_STRING = "SRCH";
	private static final String REGEX_STRING = "*SRCH*";

	private static final int CLASS_ID = 2334;
	private static final int COMMODITY_ID = 23423;
	private static final String DESCRIPTION = "test class";

	/**
	 * Tests findCommoditiesByRegularExpression.
	 */
	@Test
	public void findCommoditiesByRegularExpression() {
		ClassCommodityService classCommodityService = this.getService(19, 23);
		PageableResult<ClassCommodity> list = classCommodityService.findCommoditiesByRegularExpression(
				ClassCommodityServiceTest.SEARCH_STRING, 19, 23);
		Assert.assertEquals(this.getTestClassCommodity(), list.getData().iterator().next());
	}

	/**
	 * Tests findCommodity.
	 */
	@Test
	public void findCommodity() {
		ClassCommodityService classCommodityService = this.getService(19, 23);
		ClassCommodity cc = classCommodityService.findCommodity(234);
		Assert.assertEquals(this.getTestClassCommodity(), cc);
	}

	/*
	 * Support functions.
	 */

	/**
	 * Creates a ClassCommodity to test with.
	 *
	 * @return A ClassCommodity to test with.
	 */
	private ClassCommodity getTestClassCommodity() {

		ClassCommodityKey key = new ClassCommodityKey();
		key.setClassCode(ClassCommodityServiceTest.CLASS_ID);
		key.setCommodityCode(ClassCommodityServiceTest.COMMODITY_ID);

		ClassCommodity cc = new ClassCommodity();
		cc.setKey(key);
		cc.setName(ClassCommodityServiceTest.DESCRIPTION);
		return cc;
	}

	/**
	 * Returns a list of CommodityDocument to test with.
	 *
	 * @return A list of CommodityDocument to test with.
	 */
	private List<CommodityDocument> getCommodityList() {

		List<CommodityDocument> classCommodities = new ArrayList<>(1);
		classCommodities.add(new CommodityDocument(this.getTestClassCommodity()));
		return classCommodities;
	}

	/**
	 * Returns an answer for when findByRegularExpression is called on the CommodityIndexRepository. It checks to make
	 * sure the right parameters are passed to the function.
	 *
	 * @param page The page number expected in the call.
	 * @param pageSize The page size expected in the call.
	 * @return An answer for findByRegularExpression.
	 */
	private Answer<Page<CommodityDocument>> getCommoditySearchAnswer(int page, int pageSize) {
		return invocation -> {
			Assert.assertEquals(ClassCommodityServiceTest.REGEX_STRING, invocation.getArguments()[0]);
			PageRequest request = (PageRequest)invocation.getArguments()[1];
			Assert.assertEquals(page, request.getPageNumber());
			Assert.assertEquals(pageSize, request.getPageSize());
			return new PageImpl<>(this.getCommodityList());
		};
	}

	/**
	 * Returns a ClassCommodityService to test with. It will have index repositories configured for testing.
	 *
	 * @param page The page number expected in the call.
	 * @param pageSize The page size expected in the call.
	 * @return A ClassCommodityService to test with.
	 */
	private ClassCommodityService getService(int page, int pageSize) {
		ClassCommodityService service = new ClassCommodityService();
		CommodityIndexRepository commodityIndexRepository = Mockito.mock(CommodityIndexRepository.class);
		Mockito.doAnswer(this.getCommoditySearchAnswer(page, pageSize)).when(commodityIndexRepository)
				.findByRegularExpression(Mockito.anyString(), Mockito.anyObject());
		Mockito.when(commodityIndexRepository.findOne(Mockito.anyString()))
				.thenReturn(new CommodityDocument(this.getTestClassCommodity()));
		service.setCommodityIndexRepository(commodityIndexRepository);

		return service;
	}
}
