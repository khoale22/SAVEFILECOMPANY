/*
 * CommodityWriterTest
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
import com.heb.pm.productHierarchy.CommodityDocument;
import com.heb.pm.repository.CommodityIndexRepository;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import testSupport.CallChecker;

import java.util.ArrayList;
import java.util.List;

/**
 * Tests CommodityWrite.
 *
 * @author d116773
 * @since 2.0.2
 */
public class CommodityWriterTest {

	private static final int CLASS_ID = 2334;
	private static final int COMMODITY_ID = 23423;
	private static final String DESCRIPTION = "test class";

	/*
	 * write
	 */

	/**
	 * Tests the call to write.
	 */
	@Test
	public void write() {

		CommodityCheckingCallChecker callChecker = new CommodityCheckingCallChecker(this.getTestClassDocument());
		CommodityWriter writer = new CommodityWriter();
		writer.setIndexRepository(this.getIndexRepository(callChecker));
		try {
			writer.write(this.getCommodityDocumentList());
			Assert.assertTrue(callChecker.isMethodCalled());
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		}
	}


	/*
	 * Support functions.
	 */
	/**
	 * This class is used to make sure write does what it is supposed to.
	 */
	private class CommodityCheckingCallChecker extends CallChecker {

		private CommodityDocument toCompareTo = null;

		/**
		 * Constructs a new CommodityCheckingCallChecker.
		 *
		 * @param c The CommodityDocument to make sure is passed to save.
		 */
		public CommodityCheckingCallChecker(CommodityDocument c) {
			super();
			this.toCompareTo = c;
		}

		/**
		 * Used to represent a call to save on CommodityIndexRepository. It checks to make sure a List of
		 * CommodityDocuments is passed in, that list has one element, and that element equals the
		 * CommodityDocument passed in to this object's constructor.
		 *
		 * @param invocation The call of this method from Mockito.
		 * @return Null.
		 * @throws Throwable
		 */
		@Override
		public Void answer(InvocationOnMock invocation) throws Throwable {

			Assert.assertTrue(invocation.getArguments()[0] instanceof List);
			List<CommodityDocument> classCommodities = (List)invocation.getArguments()[0];
			Assert.assertEquals(1, classCommodities.size());
			CommodityDocument cd = classCommodities.get(0);

			Assert.assertEquals(DocumentWrapperUtil.toData(this.toCompareTo), DocumentWrapperUtil.toData(cd));
			return super.answer(invocation);
		}
	}

	/**
	 * Creates a ClassDocument to test with.
	 *
	 * @return A ClassDocument to test with.
	 */
	private CommodityDocument getTestClassDocument() {

		ClassCommodityKey key = new ClassCommodityKey();
		key.setClassCode(CommodityWriterTest.CLASS_ID);
		key.setCommodityCode(CommodityWriterTest.COMMODITY_ID);

		ClassCommodity cc = new ClassCommodity();
		cc.setKey(key);
		cc.setName(CommodityWriterTest.DESCRIPTION);
		return new CommodityDocument(cc);
	}

	/**
	 * Returns a list of CommodityDocuments to test with.
	 *
	 * @return A list of CommodityDocuments to test with.
	 */
	private List<CommodityDocument> getCommodityDocumentList() {

		List<CommodityDocument> classCommodities = new ArrayList<>(1);
		classCommodities.add(this.getTestClassDocument());
		return classCommodities;
	}

	/**
	 * Returns a CommodityIndexRepository that can be used to test calls to save.
	 *
	 * @param callChecker The Answer that will simulate a call to save on ClassIndexRepository.
	 * @return A CommodityIndexRepository to test with.
	 */
	private CommodityIndexRepository getIndexRepository(CommodityCheckingCallChecker callChecker) {

		CommodityIndexRepository repository = Mockito.mock(CommodityIndexRepository.class);
		Mockito.doAnswer(callChecker).when(repository).save(Mockito.anyCollection());
		return repository;
	}
}
