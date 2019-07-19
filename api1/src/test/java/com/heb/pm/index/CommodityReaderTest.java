/*
 * CommodityReaderTest
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.index;

import com.heb.pm.entity.ClassCommodity;
import com.heb.pm.entity.ClassCommodityKey;
import com.heb.pm.productHierarchy.ClassCommodityService;
import com.heb.pm.repository.ClassCommodityRepository;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * Tests CommodityReader.
 *
 * @author d116773
 * @since 2.0.2
 */
public class CommodityReaderTest {

	private static final int CLASS_ID = 2334;
	private static final int COMMODITY_ID = 23423;
	private static final String DESCRIPTION = "test class";

	/*
	 * afterStep
	 */

	/**
	 * Tests afterStep.
	 */
	@Test
	public void afterStep() {
		CommodityReader commodityReader = new CommodityReader();
		Assert.assertNull(commodityReader.afterStep(null));
	}

	/*
	 * read
	 */

	/**
	 * Tests read when the data are good.
	 */
	@Test
	public void readGoodData() {
		CommodityReader commodityReader = new CommodityReader();
		commodityReader.setService(this.getService(new FindAllAnswer(this.getClassCommodityList(), 2)));
		commodityReader.beforeStep(null);

		try {
			// The first two should be a good ClassCommodity
			Assert.assertEquals(this.getTestClassCommodity(), commodityReader.read());
			Assert.assertEquals(this.getTestClassCommodity(), commodityReader.read());
			// This list is empty, so it should return null
			Assert.assertNull(commodityReader.read());
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		}
	}

	/**
	 * Tests read when the repository never returns data.
	 */
	@Test
	public void readNullList() {
		CommodityReader commodityReader = new CommodityReader();
		commodityReader.setService(this.getService(new FindAllAnswer(this.getClassCommodityList(), 0)));
		commodityReader.beforeStep(null);

		try {
			// This list is empty, so it should return null
			Assert.assertNull(commodityReader.read());
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		}
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
		key.setClassCode(CommodityReaderTest.CLASS_ID);
		key.setCommodityCode(CommodityReaderTest.COMMODITY_ID);

		ClassCommodity cc = new ClassCommodity();
		cc.setKey(key);
		cc.setName(CommodityReaderTest.DESCRIPTION);
		return cc;
	}

	/**
	 * Returns a list of ClassCommodities to test with.
	 *
	 * @return A list of ClassCommodities to test with.
	 */
	private List<ClassCommodity> getClassCommodityList() {

		List<ClassCommodity> classCommodities = new ArrayList<>(1);
		classCommodities.add(this.getTestClassCommodity());
		return classCommodities;
	}

	/**
	 * An answer to mock up the calls to findAllCommodities.
	 */
	private class FindAllAnswer implements  Answer<Page<ClassCommodity>> {

		private int currentPage = 0;
		private int timesToRun;
		private List<ClassCommodity> toReturn;

		public FindAllAnswer(List<ClassCommodity> toReturn, int timesToRun) {
			this.timesToRun = timesToRun;
			this.toReturn = toReturn;
		}

		/**
		 * Mocks the call to findAllCommodities. It'll return lists of data to help test the paging functionality
		 * in the read method. It'll return list only as many times as passed in as timesToRun in the constructor.
		 * After that, it'll return an empty list. It also checks to make sure
		 * the page number is being incremented by 1 each time it's called.
		 *
		 * @param invocation The call to this method.
		 * @return A List of ClassCommodities to process in read.
		 * @throws Throwable
		 */
		@Override
		public Page<ClassCommodity> answer(InvocationOnMock invocation) throws Throwable {
			PageRequest pageRequest = (PageRequest)invocation.getArguments()[0];
			// Make sure the page number is being incremented.
			Assert.assertEquals(this.currentPage++, pageRequest.getPageNumber());

			// only return data twice.
			if (this.currentPage >= this.timesToRun + 1) {
				return new PageImpl<>(new ArrayList<>());
			}
			return new PageImpl<>(this.toReturn);
		}
	}
	/**
	 * Returns a ClassCommodityRepository to test with.
	 *
	 * @param answer The mock of the call to findAllCommodities.
	 * @return A ClassCommodityRepository to test with.
	 */
	private ClassCommodityService getService(FindAllAnswer answer) {

		ClassCommodityService service = Mockito.mock(ClassCommodityService.class);

		Mockito.doAnswer(answer).when(service).findAllCommoditiesByPage(Mockito.any(PageRequest.class));

		return service;
	}
}
