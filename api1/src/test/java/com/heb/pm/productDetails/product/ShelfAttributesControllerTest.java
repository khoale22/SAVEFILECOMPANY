/*
 *  ShelfAttributesControllerTest
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.productDetails.product;

import com.heb.pm.entity.DescriptionType;
import com.heb.pm.entity.ProductMaster;
import com.heb.util.controller.ModifiedEntity;
import com.heb.util.controller.UserInfo;
import javassist.runtime.Desc;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.context.MessageSource;
import sun.security.krb5.internal.crypto.Des;
import testSupport.CommonMocks;

import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

/**
 * This is the Test class for shelf attributes controller.
 *
 * @author l730832
 * @since 2.8.0
 */
public class ShelfAttributesControllerTest {

	private static final String testString = "TEST";
	private static final Integer testInt = 1;

	@InjectMocks
	private ShelfAttributesController shelfAttributesController;

	@Mock
	private ShelfAttributesService service;

	@Mock
	private UserInfo userInfo;

	@Mock
	private MessageSource messageSource;

	/**
	 * Initializes mockito.
	 */
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

	/**
	 * Tests the saving of shelf attribute changes.
	 */
	@Test
	public void testSaveShelfAttributeChanges() {

		Mockito.when(this.service.updateShelfAttributeChanges(Mockito.any(ProductMaster.class), CommonMocks.getUserInfo())).thenReturn(this.getDefaultProductMaster());
		ModifiedEntity<ProductMaster> result = this.shelfAttributesController.saveShelfAttributeChanges(this.getDefaultProductMaster(), CommonMocks.getServletRequest());
		Assert.assertEquals(result.getData(), this.getDefaultProductMaster());
	}

	/**
	 * Tests getListOfDescriptionTypes.
	 */
	@Test
	public void testGetListOfDescriptionTypes() {

		List<DescriptionType> descriptionTypes = this.getDefaultListDescriptionTypes();
		Mockito.when(this.service.getListOfDescriptionTypes()).thenReturn(descriptionTypes);
		List<DescriptionType> result = this.shelfAttributesController.getListOfDescriptionTypes(CommonMocks.getServletRequest());
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
		return pm;
	}

	/**
	 * Creates a default list of description types to test with.
	 *
	 * @return list of description types.
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