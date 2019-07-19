/*
 * ProductDiscontinueResolverTest
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.productDiscontinue;

import com.heb.pm.entity.ItemMaster;
import com.heb.pm.entity.ProductDiscontinue;
import com.heb.pm.entity.SellingUnit;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import testSupport.CallChecker;
import testSupport.GenericCallChecker;

/**
 * Tests ProductDiscontinueResolver.
 *
 * @author d116773
 * @since 2.0.1
 */
public class ProductDiscontinueResolverTest {

	ProductDiscontinueResolver productDiscontinueResolver = new ProductDiscontinueResolver();

	/**
	 * Tests fetch when passed a null.
	 */
	@Test
	public void fetchNullObject() {

		this.productDiscontinueResolver.fetch(null);
	}

	/**
	 * Tests fetch when getItemMaster returns null.
	 */
	@Test
	public void fetchNullGetItemMaster() {

		this.productDiscontinueResolver.fetch(this.getProductDiscontinueNullItemMaster());
	}

	/**
	 * Tests fetch when getSellingUnit returns null.
	 */
	@Test
	public void fetchNullSellingUnit() {
		this.productDiscontinueResolver.fetch(this.getProductDiscontinueNullSellingUnit());
	}

	/**
	 * Tests fetch calls all the functions it is supposed to.
	 */
	@Test
	public void fetchCheckFunctions() {
		CallChecker getKeyChecker = new CallChecker();
		ItemMaster im = Mockito.mock(ItemMaster.class);
		Mockito.doAnswer(getKeyChecker).when(im).getKey();

		GenericCallChecker<Integer> sellingUnitChecker = new GenericCallChecker<>(10);
		SellingUnit sellingUnit = Mockito.mock(SellingUnit.class);
		Mockito.doAnswer(sellingUnitChecker).when(sellingUnit).getUpc();

		ProductDiscontinue pd = Mockito.mock(ProductDiscontinue.class);
		Mockito.when(pd.getItemMaster()).thenReturn(im);
		Mockito.when(pd.getSellingUnit()).thenReturn(sellingUnit);

		this.productDiscontinueResolver.fetch(pd);
		Assert.assertTrue(getKeyChecker.isMethodCalled());
		Assert.assertTrue(sellingUnitChecker.isMethodCalled());
	}

	/*
	 * Support functions.
	 */

	/**
	 * Returns a ProductDiscontinue that will return null for getItemMaster.
	 *
	 * @return A ProductDiscontinue that will return null for getItemMaster.
	 */
	private ProductDiscontinue getProductDiscontinueNullItemMaster() {

		GenericCallChecker<Integer> sellingUnitChecker = new GenericCallChecker<>(10);
		SellingUnit sellingUnit = Mockito.mock(SellingUnit.class);
		Mockito.doAnswer(sellingUnitChecker).when(sellingUnit).getUpc();

		ProductDiscontinue pd = Mockito.mock(ProductDiscontinue.class);
		Mockito.when(pd.getSellingUnit()).thenReturn(sellingUnit);
		return pd;
	}

	/**
	 * Returns a ProductDiscontinue that will return null for getSellingUnit.
	 *
	 * @return A ProductDiscontinue that will return null for getSellingUnit.
	 */
	private ProductDiscontinue getProductDiscontinueNullSellingUnit() {
		return new ProductDiscontinue();
	}
}
