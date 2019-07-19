/*
 * ClassWriterTest
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
import com.heb.pm.entity.ItemClass;
import com.heb.pm.productHierarchy.ItemClassDocument;
import com.heb.pm.repository.ItemClassIndexRepository;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import testSupport.CallChecker;

import java.util.ArrayList;
import java.util.List;

/**
 * Tests ClassWriter.
 *
 * @author d116773
 * @since 2.0.2
 */
public class ItemClassWriterTest {

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

		ClassCheckingCallChecker callChecker = new ClassCheckingCallChecker(this.getTestClassDocument());
		ItemClassWriter writer = new ItemClassWriter();
		writer.setIndexRepository(this.getIndexRepository(callChecker));
		try {
			writer.write(this.getClassDocumentList());
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
	private class ClassCheckingCallChecker extends CallChecker {

		private ItemClassDocument toCompareTo = null;

		/**
		 * Constructs a new ClassCheckingCallChecker.
		 *
		 * @param c The ItemClassDocument to make sure is passed to save.
		 */
		public ClassCheckingCallChecker(ItemClassDocument c) {
			super();
			this.toCompareTo = c;
		}

		/**
		 * Used to represent a call to save on ItemClassIndexRepository. It checks to make sure a List of
		 * ClassCommodities is passed in, that list has one element, and that element equals the ClassCommodity passed
		 * in to this object's constructor.
		 *
		 * @param invocation The call of this method from Mockito.
		 * @return Null.
		 * @throws Throwable
		 */
		@Override
		public Void answer(InvocationOnMock invocation) throws Throwable {

			Assert.assertTrue(invocation.getArguments()[0] instanceof List);
			List<ItemClassDocument> classCommodities = (List)invocation.getArguments()[0];
			Assert.assertEquals(1, classCommodities.size());
			ItemClassDocument cd = classCommodities.get(0);

			Assert.assertEquals(DocumentWrapperUtil.toData(this.toCompareTo), DocumentWrapperUtil.toData(cd));
			return super.answer(invocation);
		}
	}

	/**
	 * Creates a ItemClassDocument to test with.
	 *
	 * @return A ItemClassDocument to test with.
	 */
	private ItemClassDocument getTestClassDocument() {

		ItemClass itemClass = new ItemClass();
		itemClass.setItemClassDescription(DESCRIPTION);
		itemClass.setItemClassCode(CLASS_ID);
		return new ItemClassDocument(itemClass);
	}

	/**
	 * Returns a list of ClassDocuments to test with.
	 *
	 * @return A list of ClassDocuments to test with.
	 */
	private List<ItemClassDocument> getClassDocumentList() {

		List<ItemClassDocument> classCommodities = new ArrayList<>(1);
		classCommodities.add(this.getTestClassDocument());
		return classCommodities;
	}

	/**
	 * Returns a ItemClassIndexRepository that can be used to test calls to save.
	 *
	 * @param callChecker The Answer that will simulate a call to save on ItemClassIndexRepository.
	 * @return A ItemClassIndexRepository to test with.
	 */
	private ItemClassIndexRepository getIndexRepository(ClassCheckingCallChecker callChecker) {

		ItemClassIndexRepository repository = Mockito.mock(ItemClassIndexRepository.class);
		Mockito.doAnswer(callChecker).when(repository).save(Mockito.anyCollection());
		return repository;
	}
}
