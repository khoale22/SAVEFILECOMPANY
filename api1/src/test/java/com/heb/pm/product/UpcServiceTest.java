/*
 * UpcServiceTest
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.product;

import com.heb.pm.entity.SellingUnit;
import com.heb.pm.repository.SellingUnitRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * Test class for UpcService.
 *
 * @author m314029
 * @since 2.0.3
 */
public class UpcServiceTest{

	private SellingUnit defaultSellingUnit = new SellingUnit();
	private final long DEFAULT_SELLING_UNIT_UPC = 6289L;


	@Before
	public void setup(){
		defaultSellingUnit = getDefaultSellingUnit();
	}

	/*
	 * find
	 */

	/**
	 * Tests find using same upc as default selling unit.
	 */
	@Test
	public void testFind() {
		UpcService service = new UpcService();
		service.setRepository(this.getRepositoryReturnsDefault());
		SellingUnit retrievedSellingUnit = service.find(DEFAULT_SELLING_UNIT_UPC);
		this.fullItemCompare(defaultSellingUnit, retrievedSellingUnit);
	}

	/*
	 * Support functions.
	 */

	/**
	 *  This Function creates a default SellingUnit that matches first record in mach db.
	 * @return SellingUnit.
	 */
	private SellingUnit getDefaultSellingUnit() {

		SellingUnit sellingUnit = new SellingUnit();
		sellingUnit.setUpc(DEFAULT_SELLING_UNIT_UPC);
		sellingUnit.setBonusSwitch(false);
		sellingUnit.setPrimaryUpc(true);
		sellingUnit.setProdId(982265);
		sellingUnit.setTagSize("LB    ");
		return sellingUnit;
	}

	/**
	 *  This Function creates a SellingUnitRepository that returns a SellingUnit that matches first record in mach db.
	 * @return SellingUnit.
	 */
	private SellingUnitRepository getRepositoryReturnsDefault(){
		SellingUnitRepository repository = Mockito.mock(SellingUnitRepository.class);
		SellingUnit sellingUnit = new SellingUnit();
		sellingUnit.setUpc(DEFAULT_SELLING_UNIT_UPC);
		sellingUnit.setBonusSwitch(false);
		sellingUnit.setPrimaryUpc(true);
		sellingUnit.setProdId(982265);
		sellingUnit.setTagSize("LB    ");
		Mockito.when(repository.findOne(Mockito.anyLong())).thenReturn(sellingUnit);
		return repository;
	}

	/**
	 * Since the equals on selling unit only compares keys, this goes deeper anc compares all values in the object.
	 *	NOTE: only compares values saved into table entity, not necessarily all columns in database.
	 *
	 * @param a The first one to compare.
	 * @param b The second one to compare.
	 */
	private void fullItemCompare(SellingUnit a, SellingUnit b) {
		Assert.assertEquals(a.getUpc(), b.getUpc());
		Assert.assertEquals(a.getBonusSwitch(), b.getBonusSwitch());
		Assert.assertEquals(a.isPrimaryUpc(), b.isPrimaryUpc());
		Assert.assertEquals(a.getProdId(), b.getProdId());
		Assert.assertEquals(a.getTagSize(), b.getTagSize());
	}
}
