package com.heb.pm.index;

import com.heb.pm.entity.SubCommodity;

import com.heb.pm.entity.SubCommodityKey;
import com.heb.pm.productHierarchy.SubCommodityDocument;
import com.heb.pm.repository.SubCommodityIndexRepository;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import testSupport.CallChecker;

import java.util.ArrayList;
import java.util.List;

/**
 * Tests SubCommodityWriter.
 *
 * @author d116773
 * @since 2.0.2
 */
public class SubCommodityWriterTest {

	private static final int CLASS_CODE = 39;
	private static final int COMMODITY_CODE = 8222;
	private static final int SUB_COMMODITY_CODE = 9551;
	private static final String SUB_COMMODITY_NAME = "LEAN MULT SRV STUFD SNDWCH    ";

	/**
	 * Tests write when passed a null.
	 */
	@Test
	public void writeNull() {
		SubCommodityWriter writer = new SubCommodityWriter();
		writer.setIndexRepository(this.getIndexRepository(new SubCommodityCheckingCallChecker(null)));

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
		SubCommodityWriter writer = new SubCommodityWriter();
		writer.setIndexRepository(this.getIndexRepository(new SubCommodityCheckingCallChecker(null)));

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
		SubCommodityWriter writer = new SubCommodityWriter();
		SubCommodityCheckingCallChecker callChecker = new SubCommodityCheckingCallChecker(this.getTestSubCommodity());
		writer.setIndexRepository(this.getIndexRepository(callChecker));

		try {
			writer.write(this.getTestSubCommodities());
			Assert.assertTrue(callChecker.isMethodCalled());
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		}
	}
	/*
	 * Support functions
	 */

	/**
	 * This class mocks up the call to write on the SubCommodityIndexWriter.
	 */
	private class SubCommodityCheckingCallChecker extends CallChecker {

		private SubCommodityDocument toCompareTo = null;

		public SubCommodityCheckingCallChecker(SubCommodity sc) {
			super();
			this.toCompareTo = new SubCommodityDocument(sc);
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
			List<SubCommodityDocument> subCommodities = (List)invocation.getArguments()[0];
			Assert.assertEquals(1, subCommodities.size());
			Assert.assertEquals(this.toCompareTo.getData(), subCommodities.get(0).getData());
			Assert.assertEquals(this.toCompareTo.getKey(), subCommodities.get(0).getKey());
			return super.answer(invocation);
		}
	}

	/**
	 * Returns a key to test with.
	 *
	 * @return A key to test with.
	 */
	private SubCommodityKey getTestKey() {

		SubCommodityKey key = new SubCommodityKey();
		key.setClassCode(SubCommodityWriterTest.CLASS_CODE);
		key.setCommodityCode(SubCommodityWriterTest.COMMODITY_CODE);
		key.setSubCommodityCode(SubCommodityWriterTest.SUB_COMMODITY_CODE);
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
		subCommodity.setName(SubCommodityWriterTest.SUB_COMMODITY_NAME);
		return subCommodity;
	}

	/**
	 * Returns a list of SubCommodityDocuments to test with.
	 *
	 * @return A list of SubCommodityDocuments to test with.
	 */
	private List<SubCommodityDocument> getTestSubCommodities() {

		List<SubCommodityDocument> subCommodityDocuments = new ArrayList<>(1);
		subCommodityDocuments.add(new SubCommodityDocument(this.getTestSubCommodity()));
		return subCommodityDocuments;
	}

	/**
	 * Retusns a SubCommodityIndexRepository to test with.
	 *
	 * @param callChecker The Answer that will simulate the call to save.
	 * @return A SubCommodityIndexRepository to test with.
	 */
	private SubCommodityIndexRepository getIndexRepository(SubCommodityCheckingCallChecker callChecker) {

		SubCommodityIndexRepository indexRepository = Mockito.mock(SubCommodityIndexRepository.class);
		Mockito.doAnswer(callChecker).when(indexRepository).save(Mockito.anyCollection());
		return indexRepository;
	}
}
