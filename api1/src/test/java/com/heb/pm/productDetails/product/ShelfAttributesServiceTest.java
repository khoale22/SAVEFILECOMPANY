/*
 *  ShelfAttributesServiceTest
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.productDetails.product;

import com.heb.pm.entity.DescriptionType;
import com.heb.pm.entity.ProductDescription;
import com.heb.pm.entity.ProductDescriptionKey;
import com.heb.pm.entity.ProductMaster;
import com.heb.pm.product.ProductInfoResolver;
import com.heb.pm.repository.DescriptionTypeRepository;
import com.heb.pm.repository.ProductInfoRepository;
import com.heb.pm.ws.ProductManagementServiceClient;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import sun.security.krb5.internal.crypto.Des;
import testSupport.CommonMocks;

import java.util.ArrayList;
import java.util.List;

/**
 * This is the Test class for shelf attributes service.
 *
 * @author l730832
 * @since 2.8.0
 */
public class ShelfAttributesServiceTest {

	private static final String testString = "Test";
	private static final Long testLong = 1L;
	private static final Integer testInt = 1;

	@InjectMocks
	private ShelfAttributesService service;

	@Mock
	private ProductManagementServiceClient productManagementServiceClient;

	@Mock
	private ProductInfoRepository productInfoRepository;

	@Mock
	private DescriptionTypeRepository descriptionTypeRepository;

	@Mock
	private ProductInfoResolver resolver;

	/**
	 * Initializes mockito.
	 */
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

	/**
	 * Tests updateShelfAttributeChanges.
	 */
	@Test
	public void testUpdateShelfAttributeChanges() {

		Mockito.when(this.productInfoRepository.findOne(Mockito.any(Long.class))).thenReturn(this.getDefaultProductMaster());
		ProductMaster result = this.service.updateShelfAttributeChanges(this.getDefaultProductMaster(), CommonMocks.getUserInfo());
		Assert.assertEquals(result, this.getDefaultProductMaster());
	}

	/**
	 * Tests the getListOfDescriptionTypes.
	 */
	@Test
	public void testGetListOfDescriptionTypes() {
		List<DescriptionType> descriptionTypes = this.getDefaultListDescriptionTypes();
		Mockito.when(this.descriptionTypeRepository.findAll()).thenReturn(descriptionTypes);
		List<DescriptionType> result = this.service.getListOfDescriptionTypes();
		Assert.assertEquals(result, descriptionTypes);
	}

	/**
	 * Creates a defualt product master used for testing.
	 * @return product Master
	 */
	private ProductMaster getDefaultProductMaster() {
		ProductMaster pm = new ProductMaster();
		pm.setProdItems(new ArrayList<>());
		pm.setSellingUnits(new ArrayList<>());
		List<ProductDescription> productDescriptions = new ArrayList<>();
		productDescriptions.add(this.getDefaultProductDescription());
		productDescriptions.add(this.getDefaultProductDescription());
		pm.setProductDescriptions(productDescriptions);
		return pm;
	}

	/**
	 * Retuns a default product Description.
	 *
	 * @return product Description
	 */
	private ProductDescription getDefaultProductDescription() {
		ProductDescription productDescription = new ProductDescription();

		productDescription.setDescription(testString);
		ProductDescriptionKey key = new ProductDescriptionKey();
		key.setDescriptionType(testString);
		key.setLanguageType(testString);
		key.setProductId(testLong);
		productDescription.setKey(key);
		return productDescription;
	}

	/**
	 * Returns a default list of description types.
	 *
	 * @return list of description types
	 */
	private List<DescriptionType> getDefaultListDescriptionTypes() {
		List<DescriptionType> descriptionTypes = new ArrayList<>();
		DescriptionType descriptionType = new DescriptionType();
		descriptionType.setAbbreviation(testString);
		descriptionType.setDescription(testString);
		descriptionType.setDescriptionLength(testInt);
		descriptionType.setId(testString);
		descriptionTypes.add(descriptionType);
		return descriptionTypes;
	}

}