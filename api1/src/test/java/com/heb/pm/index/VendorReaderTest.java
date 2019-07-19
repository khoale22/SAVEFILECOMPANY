/*
 * VendorReaderTest
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.index;

import com.heb.pm.entity.Vendor;
import com.heb.pm.repository.VendorRepository;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.batch.core.StepExecution;

import java.util.HashSet;
import java.util.Set;


/**
 * Tests VendorReader.
 *
 * @author d116773
 * @since 2.0.2
 */
public class VendorReaderTest {

	private static final int VENDOR_NUMBER = 823832;
	private static final String VENDOR_NAME = "Test vendor name";

	/*
	 * read
	 */
	@Test
	public void read() {

		VendorReader vendorReader = new VendorReader();
		vendorReader.setVendorRepository(this.getVendorRepository());

		// beforeStep has to be called before read is to set up the data.
		vendorReader.beforeStep(this.getStepExecution());

		try {
			// The first call to read should return a Vendor.
			Vendor v = vendorReader.read();
			Assert.assertEquals(VendorReaderTest.VENDOR_NUMBER, v.getVendorNumber());
			Assert.assertEquals(VendorReaderTest.VENDOR_NAME, v.getVendorName());

			// The second call should return null.
			Assert.assertNull(vendorReader.read());
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		}
	}

	/*
	 * afterStep.
	 */
	/**
	 * Tests afterStep. It just returns null.
	 */
	@Test
	public void afterStep() {

		VendorReader vendorReader = new VendorReader();
		Assert.assertNull(vendorReader.afterStep(this.getStepExecution()));
	}

	/**
	 * Returns a StepExecution for the StepExecutionListener functions.
	 *
	 * @return A StepExecution
	 */
	private StepExecution getStepExecution() {
		return Mockito.mock(StepExecution.class);
	}

	/**
	 * Returns a List of vendors to test with. It will have one vendor in the list.
	 *
	 * @return A List of vendors to test with.
	 */
	private Set<Vendor> getVendors() {
		Vendor v = new Vendor();
		v.setVendorNumber(VendorReaderTest.VENDOR_NUMBER);
		v.setVendorName(VendorReaderTest.VENDOR_NAME);

		Set<Vendor> vendors = new HashSet<>();
		vendors.add(v);
		return vendors;
	}

	/**
	 * Returns a VendorRepository that will return the list from getVendors.
	 *
	 * @return A VendorRepository to test with.
	 */
	private VendorRepository getVendorRepository() {
		VendorRepository repository = Mockito.mock(VendorRepository.class);
		Mockito.when(repository.findAll()).thenReturn(this.getVendors());
		return repository;
	}

}
