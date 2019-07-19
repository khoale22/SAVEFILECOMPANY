/*
 *  DiscontinueParametersAuditRecordTest
 *
 *   Copyright (c) 2016 HEB
 *   All rights reserved.
 *
 *   This software is the confidential and proprietary information
 *    of HEB.
 */

package com.heb.pm.productDiscontinue;

import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDateTime;

/**
 * @author s573181
 * @since 2.0.4
 */
public class DiscontinueParametersAuditRecordTest {

	/**
	 * Tests isPreviouslyActive.
	 */
	@Test
	public void isPreviouslyActive() {
		DiscontinueParametersAuditRecord audit = this.getDefaultAudit();
		Assert.assertEquals(false, audit.isPreviouslyActive());
	}

	/**
	 * Tests isNewActive.
	 */
	@Test
	public void isNewActive() {
		DiscontinueParametersAuditRecord audit = this.getDefaultAudit();
		Assert.assertEquals(true, audit.isNewActive());
	}

	/**
	 * Tests isPreviouslyNeverDiscontinue.
	 */
	@Test
	public void isPreviouslyNeverDiscontinue() {
		DiscontinueParametersAuditRecord audit = this.getDefaultAudit();
		Assert.assertEquals(false, audit.isPreviouslyNeverDiscontinue());
	}

	/**
	 * Tests isNewNeverDiscontinue.
	 */
	@Test
	public void isNewNeverDiscontinue() {
		DiscontinueParametersAuditRecord audit = this.getDefaultAudit();
		Assert.assertEquals(true, audit.isNewNeverDiscontinue());
	}

	/**
	 * Tests getExceptionType.
	 */
	@Test
	public void getExceptionType() {
		DiscontinueParametersAuditRecord audit = this.getDefaultAudit();
		Assert.assertEquals("Vendor", audit.getExceptionType());
	}

	@Test
	public void getExceptionDescription() {
		DiscontinueParametersAuditRecord audit = this.getDefaultAudit();
		Assert.assertEquals("DUMMY NATIONAL PORK[67890]", audit.getExceptionDescription());
	}

	/**
	 * Tests getAttributeName.
	 */
	@Test
	public void getAttributeName() {
		DiscontinueParametersAuditRecord audit = this.getDefaultAudit();
		Assert.assertEquals("Product Id", audit.getAttributeName());
	}

	/**
	 * Tests getAction.
	 */
	@Test
	public void getAction() {
		DiscontinueParametersAuditRecord audit = this.getDefaultAudit();
		Assert.assertEquals("ADD", audit.getAction());

	}

	/**
	 * Tests getChangedOn.
	 */
	@Test
	public void getChangedOn() {
		DiscontinueParametersAuditRecord audit = this.getDefaultAudit();
		Assert.assertEquals("v357407", audit.getChangedOn());

	}

	/**
	 * Tests getChangedBy.
	 */
	@Test
	public void getChangedBy() {
		DiscontinueParametersAuditRecord audit = this.getDefaultAudit();
		Assert.assertEquals("2016-07-28 14:25:20.98", audit.getChangedBy());

	}

	/**
	 * Tests getPreviousAttributeValue.
	 */
	@Test
	public void getPreviousAttributeValue() {
		DiscontinueParametersAuditRecord audit = this.getDefaultAudit();
		Assert.assertEquals("12345", audit.getPreviousAttributeValue());

	}

	/**
	 * Tests getNewAttributeValue.
	 */
	@Test
	public void getNewAttributeValue() {
		DiscontinueParametersAuditRecord audit = this.getDefaultAudit();
		Assert.assertEquals("67890", audit.getNewAttributeValue());
	}

	/**
	 * Returns a default Audit object to be used for testing.
	 *
	 * @return A default Audit object to be used for testing.
	 */
	private DiscontinueParametersAuditRecord getDefaultAudit(){
		DiscontinueParametersAuditRecord audit = new DiscontinueParametersAuditRecord();
		audit.setPreviouslyActive(false);
		audit.setNewActive(true);
		audit.setPreviouslyNeverDiscontinue(false);
		audit.setNewNeverDiscontinue(true);
		audit.setExceptionType("Vendor");
		audit.setExceptionDescription("DUMMY NATIONAL PORK[67890]");
		audit.setAction("ADD");
		audit.setAttributeName("Product Id");
		audit.setChangedOn(LocalDateTime.now());
		audit.setChangedBy("v357407");
		audit.setPreviousAttributeValue("12345");
		audit.setNewAttributeValue("67890");
		return audit;
	}
}
