package com.heb.pm.productDetails.product.SpecialAttributes;

import com.heb.pm.entity.SellingRestriction;
import com.heb.pm.entity.SellingRestrictionCode;
import com.heb.pm.repository.DrugScheduleTypeRepository;
import com.heb.pm.repository.SellingRestrictionCodeRepository;
import com.heb.pm.repository.SellingRestrictionRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Tests the RatingRestrictionService
 * @author s753601
 * @version 2.12.0
 */
public class RatingRestrictionServiceTest {

	@InjectMocks
	RatingRestrictionService service;

	@Mock
	private SellingRestrictionRepository sellingRestrictionRepository;

	@Mock
	private SellingRestrictionCodeRepository sellingRestrictionCodeRepository;

	@Before
	public void setup(){
		MockitoAnnotations.initMocks(this);
	}

	/**
	 * Tests the getAllRestrictions Method
	 */
	@Test
	public void getAllRestrictionsTest(){
		List<SellingRestrictionCode> test = getDefaultCodes();
		Mockito.when(this.sellingRestrictionCodeRepository.findAll()).thenReturn(getDefaultCodes());
		List<SellingRestrictionCode> result = this.service.getAllRestrictions();
		Assert.assertEquals(test, result);
	}

	/**
	 * Tests the getAllRestrictionGroups Method
	 */
	@Test
	public void getAllRestrictionGroupsTest(){
		List<SellingRestriction> test = getDefaultGroups();
		Mockito.when(this.sellingRestrictionRepository.findAll()).thenReturn(getDefaultGroups());
		List<SellingRestriction> result = this.service.getAllRestrictionGroups();
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