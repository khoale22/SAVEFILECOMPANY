package com.heb.pm.monitor;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import testSupport.CommonMocks;

import javax.servlet.http.HttpServletRequest;

/**
 * Unit tests for com.heb.pm.monitor.StatusController.
 *
 * Created by d116773 on 4/29/2016.
 */
public class StatusControllerTest {

	/**
	 * Tests that getStatus returns the right version.
	 */
	@Test
	public void testGetStatus() {
		StatusController statusController = new StatusController();
		statusController.setApplicationVersion("2323");
		StatusController.StatusWrapper version = statusController.getStatus(CommonMocks.getServletRequest());
		Assert.assertEquals("2323", version.getVersion());
	}
}
