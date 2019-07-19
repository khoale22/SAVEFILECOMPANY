/*
 * BdmWriterTest
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.index;

import com.heb.pm.entity.Bdm;
import com.heb.pm.productHierarchy.BdmDocument;
import com.heb.pm.repository.BdmIndexRepository;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import testSupport.CallChecker;

import java.util.ArrayList;
import java.util.List;

/**
 * Tests BdmWriter.
 *
 * @author m314029
 * @since 2.0.6
 */
public class BdmWriterTest {

	private static final String BDM_CODE = "A1   ";
	private static final String FIRST_NAME = "KAREN               ";
	private static final String LAST_NAME = "CASSADY             ";
	private static final String FULL_NAME = "KAREN CASSADY                 ";

	/*
	 * write()
	 */

	/**
	 * Tests write when passed a null.
	 */
	@Test
	public void writeNull() {
		BdmWriter writer = new BdmWriter();
		writer.setIndexRepository(this.getIndexRepository(new BdmCheckingCallChecker(null)));

		try {
			writer.write(null);
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		}
	}

	/**
	 * Tests write when passed an empty list.
	 */
	@Test
	public void writeEmptyList() {
		BdmWriter writer = new BdmWriter();
		writer.setIndexRepository(this.getIndexRepository(new BdmCheckingCallChecker(null)));

		try {
			writer.write(new ArrayList<>());
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		}
	}

	/**
	 * Tests call to write with a good list of data.
	 */
	@Test
	public void writeGoodList() {
		BdmWriter writer = new BdmWriter();
		BdmCheckingCallChecker callChecker = new BdmCheckingCallChecker(this.getTestBdm());
		writer.setIndexRepository(this.getIndexRepository(callChecker));

		try {
			writer.write(this.getTestBdms());
			Assert.assertTrue(callChecker.isMethodCalled());
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		}
	}

	/*
	 * Support functions
	 */

	/**
	 * Returns a Bdm to test with.
	 *
	 * @return A Bdm to test with.
	 */
	private Bdm getTestBdm() {

		Bdm bdm = new Bdm();
		bdm.setBdmCode(BDM_CODE);
		bdm.setFirstName(FIRST_NAME);
		bdm.setLastName(LAST_NAME);
		bdm.setFullName(FULL_NAME);
		return bdm;
	}

	/**
	 * This class mocks up the call to write on the BdmIndexWriter.
	 */
	private class BdmCheckingCallChecker extends CallChecker {

		private BdmDocument toCompareTo = null;

		public BdmCheckingCallChecker(Bdm bdm) {
			super();
			this.toCompareTo = new BdmDocument(bdm);
		}

		/**
		 * Mocks the call to write. It'll check to make sure the right stuff was passed in.
		 *
		 * @param invocation The call of this method from Mockito.
		 * @return Null.
		 * @throws Throwable
		 */
		@Override
		public Void answer(InvocationOnMock invocation) throws Throwable {
			Assert.assertTrue(invocation.getArguments()[0] instanceof List);
			List<BdmDocument> bdms = (List)invocation.getArguments()[0];
			Assert.assertEquals(1, bdms.size());
			Assert.assertEquals(this.toCompareTo.getData(), bdms.get(0).getData());
			Assert.assertEquals(this.toCompareTo.getKey(), bdms.get(0).getKey());
			return super.answer(invocation);
		}
	}

	/**
	 * Returns a list of BdmDocument to test with.
	 *
	 * @return A list of BdmDocument to test with.
	 */
	private List<BdmDocument> getTestBdms() {

		List<BdmDocument> bdmDocuments = new ArrayList<>(1);
		bdmDocuments.add(new BdmDocument(this.getTestBdm()));
		return bdmDocuments;
	}

	/**
	 * Retusns a BdmIndexRepository to test with.
	 *
	 * @param callChecker The Answer that will simulate the call to save.
	 * @return A BdmIndexRepository to test with.
	 */
	private BdmIndexRepository getIndexRepository(BdmCheckingCallChecker callChecker) {

		BdmIndexRepository indexRepository = Mockito.mock(BdmIndexRepository.class);
		Mockito.doAnswer(callChecker).when(indexRepository).save(Mockito.anyCollection());
		return indexRepository;
	}
}
