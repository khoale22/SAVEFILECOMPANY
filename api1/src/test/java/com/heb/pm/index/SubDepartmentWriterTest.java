/*
 * SubDepartmentWriterTest
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.index;

import com.heb.pm.entity.SubDepartment;
import com.heb.pm.entity.SubDepartmentKey;
import com.heb.pm.productHierarchy.SubDepartmentDocument;
import com.heb.pm.repository.SubDepartmentIndexRepository;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import testSupport.CallChecker;

import java.util.ArrayList;
import java.util.List;

/**
 * Tests SubDepartmentWriter.
 *
 * @author d116773
 * @since 2.0.2
 */
public class SubDepartmentWriterTest {

	private static final String DEPARTMENT = "07   ";
	private static final String SUB_DEPARTMENT = "A    ";
	private static final String NAME = "GROCERY SOMETHING";

	/*
	 * write
	 */

	/**
	 * Tests write when passed null.
	 */
	@Test
	public void writeNull() {

		SubDepartmentWriter subDepartmentWriter = new SubDepartmentWriter();
		SubDepartmentCheckingCallChecker callChecker = new SubDepartmentCheckingCallChecker(this.getSubDepartment());
		subDepartmentWriter.setIndexRepository(this.getIndexRepository(callChecker));
		try {
			subDepartmentWriter.write(null);
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		}
	}

	/**
	 * Test write when passed an empty list.
	 */
	@Test
	public void writeEmptyList() {

		SubDepartmentWriter subDepartmentWriter = new SubDepartmentWriter();
		SubDepartmentCheckingCallChecker callChecker = new SubDepartmentCheckingCallChecker(this.getSubDepartment());
		subDepartmentWriter.setIndexRepository(this.getIndexRepository(callChecker));
		try {
			subDepartmentWriter.write(new ArrayList<>());
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		}
	}

	/**
	 * Tests write when passed in a good list.
	 */
	@Test
	public void writeGoodList() {
		SubDepartmentWriter subDepartmentWriter = new SubDepartmentWriter();
		SubDepartmentCheckingCallChecker callChecker = new SubDepartmentCheckingCallChecker(this.getSubDepartment());
		subDepartmentWriter.setIndexRepository(this.getIndexRepository(callChecker));
		try {
			subDepartmentWriter.write(this.getSubDepartments());
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
	private class SubDepartmentCheckingCallChecker extends CallChecker {

		private SubDepartmentDocument toCompareTo = null;

		/**
		 * Constructs a new SubDepartmentCheckingCallChecker.
		 *
		 * @param s The Sub-department to make sure is passed to save.
		 */
		public SubDepartmentCheckingCallChecker(SubDepartment s) {
			super();
			this.toCompareTo = new SubDepartmentDocument(s);
		}

		/**
		 * Used to represent a call to save on SubDepartmentIndexRepository. It checks to make sure a List of
		 * SubDepartments is passed in, that list has one element, and that element equals the SubDepartment passed
		 * in to this object's constructor.
		 *
		 * @param invocation The call of this method from Mockito.
		 * @return Null.
		 * @throws Throwable
		 */
		@Override
		public Void answer(InvocationOnMock invocation) throws Throwable {

			Assert.assertTrue(invocation.getArguments()[0] instanceof List);
			List<SubDepartmentDocument> subDepartmentDocuments = (List)invocation.getArguments()[0];
			Assert.assertEquals(1, subDepartmentDocuments.size());
			Assert.assertEquals(this.toCompareTo.getData(), subDepartmentDocuments.get(0).getData());
			Assert.assertEquals(this.toCompareTo.getKey(), subDepartmentDocuments.get(0).getKey());
			return super.answer(invocation);
		}
	}

	/**
	 * Returns a SubDepartment to test with.
	 *
	 * @return A SubDepartment to test with.
	 */
	private SubDepartment getSubDepartment() {

		SubDepartmentKey key = new SubDepartmentKey();
		key.setDepartment(SubDepartmentWriterTest.DEPARTMENT);
		key.setSubDepartment(SubDepartmentWriterTest.SUB_DEPARTMENT);

		SubDepartment subDepartment = new SubDepartment();
		subDepartment.setKey(key);
		subDepartment.setName(SubDepartmentWriterTest.NAME);

		return subDepartment;
	}

	private List<SubDepartmentDocument> getSubDepartments() {

		List<SubDepartmentDocument> subDepartments = new ArrayList<>();
		subDepartments.add(new SubDepartmentDocument(this.getSubDepartment()));
		return subDepartments;
	}

	/**
	 * Returns a SubDepartmentIndexRepository to test with.
	 *
	 * @param callChecker The Answer that will simulate a call to save on the SubDepartmentIndexRepository.
	 * @return A SubDepartmentIndexRepository to test with.
	 */
	private SubDepartmentIndexRepository getIndexRepository(SubDepartmentCheckingCallChecker callChecker) {

		SubDepartmentIndexRepository repository = Mockito.mock(SubDepartmentIndexRepository.class);
		Mockito.doAnswer(callChecker).when(repository).save(Mockito.anyCollection());
		return repository;
	}
}
