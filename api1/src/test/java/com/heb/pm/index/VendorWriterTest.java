/*
 * VendorWriterTest
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.index;

import com.heb.pm.entity.Vendor;

import com.heb.pm.repository.VendorIndexRepository;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;

import testSupport.CallChecker;

import java.util.ArrayList;
import java.util.List;

/**
 * Tests VendorWriter.
 *
 * @author d116773
 * @since 2.0.2
 */
public class VendorWriterTest {

	private static final int VENDOR_NUMBER = 823832;
	private static final String VENDOR_NAME = "Test vendor name";

	/*
	 * write
	 */
	/**
	 * Tests write.
	 */
	@Test
	public void write() {

		VendorWriter vendorWriter = new VendorWriter();
		VendorCheckingCallChecker vendorCheckingCallChecker = new VendorCheckingCallChecker(this.getVendor());
		vendorWriter.setVendorIndexRepository(this.getVendorIndexRepository(vendorCheckingCallChecker));

		// All the real tests are done in the mocked Answer to save.
		try {
			vendorWriter.write(this.getVendors());
			// Make sure write calls save on the repository.
			Assert.assertTrue(vendorCheckingCallChecker.isMethodCalled());
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		}
	}

	/**
	 * This class is used to make sure the write does the things it is supposed to do.
	 */
	private class VendorCheckingCallChecker extends CallChecker {

		private Vendor toCompareTo = null;

		/**
		 * Constructs a new VendorCheckingCallChecker.
		 *
		 * @param v The Vendor to see if it is passed to save.
		 */
		public VendorCheckingCallChecker(Vendor v) {
			super();
			this.toCompareTo = v;
		}

		/**
		 * Used to represent a call to save on VendorIndexRepository. It checks to make sure a List of Vendors
		 * is passed in, that list has one element, and that element equals the Vendor passed in to this object's
		 * constructor.
		 *
		 * @param invocation The call of this method from Mockito.
		 * @return Null.
		 * @throws Throwable
		 */
		@Override
		public Void answer(InvocationOnMock invocation) throws Throwable {

			Assert.assertTrue(invocation.getArguments()[0] instanceof List);
			List<Vendor> vendors = (List)invocation.getArguments()[0];
			Assert.assertEquals(1, vendors.size());
			Assert.assertEquals(this.toCompareTo, vendors.get(0));
			return super.answer(invocation);
		}
	}

	/**
	 * Returns a vendor to use during tests.
	 *
	 * @return A vendor to use during test.
	 */
	private Vendor getVendor() {
		Vendor v = new Vendor();
		v.setVendorNumber(VendorWriterTest.VENDOR_NUMBER);
		v.setVendorName(VendorWriterTest.VENDOR_NAME);

		return v;
	}

	/**
	 * Returns a List of vendors to test with. It will have one vendor in the list.
	 *
	 * @return A List of vendors to test with.
	 */
	private List<Vendor> getVendors() {

		List<Vendor> vendors = new ArrayList<>();
		vendors.add(this.getVendor());
		return vendors;
	}

	/**
	 * Returns a VendorIndexRepository to test with.
	 *
	 * @param callChecker The Answer to call when save is called on the VendorIndexRepository created.
	 * @return A VendorIndexRepository to test with.
	 */
	private VendorIndexRepository getVendorIndexRepository(VendorCheckingCallChecker callChecker) {

		VendorIndexRepository vendorIndexRepository = Mockito.mock(VendorIndexRepository.class);
		Mockito.doAnswer(callChecker).when(vendorIndexRepository).save(Mockito.anyCollection());
		return vendorIndexRepository;
	}
}
