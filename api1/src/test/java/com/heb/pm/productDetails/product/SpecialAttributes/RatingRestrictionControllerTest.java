package com.heb.pm.productDetails.product.SpecialAttributes;

import com.heb.pm.entity.SellingRestriction;
import com.heb.pm.entity.SellingRestrictionCode;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import testSupport.CommonMocks;

import java.util.ArrayList;
import java.util.List;

/**
 * Test the RatingRestrictionController
 * @author s753601
 * @version 2.12.0
 */
public class RatingRestrictionControllerTest {

	@InjectMocks
	RatingRestrictionController controller;

	@Mock
	private RatingRestrictionService service;

	@Before
	public void setup(){
		MockitoAnnotations.initMocks(this);
		this.controller.setUserInfo(CommonMocks.getUserInfo());
	}

	/**
	 * Tests the getRestrictions method
	 */
	@Test
	public void getRestrictionsTest() {
		List<SellingRestrictionCode> test = getDefaultCodes();
		Mockito.when(this.service.getAllRestrictions()).thenReturn(getDefaultCodes());
		List<SellingRestrictionCode> result = this.controller.getRestrictions(CommonMocks.getServletRequest());
		Assert.assertEquals(test, result);
	}

	/**
	 * Tests the getRestrictionGroup method
	 */
	@Test
	public void getRestrictionGroupTest(){
		List<SellingRestriction> test = getDefaultGroups();
		Mockito.when(this.service.getAllRestrictionGroups()).thenReturn(getDefaultGroups());
		List<SellingRestriction> result = this.controller.getRestrictionGroup(CommonMocks.getServletRequest());
		Assert.assertEquals(test, result);
	}

	/**
	 * Generates a default selling restriction list for testing
	 * @return
	 */
	private List<SellingRestriction> getDefaultGroups(){
		ArrayList<SellingRestriction> list =new ArrayList<>();
		return list;
	}

	/**
	 * Generates a default selling restriction code list for testing
	 * @return
	 */
	private List<SellingRestrictionCode> getDefaultCodes(){
		ArrayList<SellingRestrictionCode> list =new ArrayList<>();
		return list;
	}
}