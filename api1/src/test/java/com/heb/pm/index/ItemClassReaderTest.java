/*
 * ClassReaderTest
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.index;

import com.heb.pm.entity.ItemClass;
import com.heb.pm.productHierarchy.ItemClassService;
import com.heb.pm.repository.ItemClassRepository;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * Tests ClassReader.
 *
 * @author d116773
 * @since 2.0.2
 */
public class ItemClassReaderTest {

	private static final int CLASS_ID = 2334;
	private static final String DESCRIPTION = "test class";

	/*
	 * afterStep
	 */

	/**
	 * Tests afterStep.
	 */
	@Test
	public void afterStep() {
		ItemClassReader classReader = new ItemClassReader();
		Assert.assertNull(classReader.afterStep(null));
	}

	/*
	 * read
	 */

	/**
	 * Tests read when the data are good.
	 */
	@Test
	public void readGoodData() {

		ItemClassReader itemClassReader = new ItemClassReader();
		itemClassReader.setRepository(this.getRepository(this.getItemClassList()));
		itemClassReader.beforeStep(null);

		try {
			// The first one should be a good ClassCommodity
			Assert.assertEquals(this.getTestItemClass(), itemClassReader.read());
			// This list is empty, so it should return null
			Assert.assertNull(itemClassReader.read());
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		}
	}

	/**
	 * Tests read when the repository returns null.
	 */
	@Test
	public void readNullList() {

		ItemClassReader itemClassReader = new ItemClassReader();
		itemClassReader.setRepository(this.getRepository(null));

		try {
			// This list is empty, so it should return null
			Assert.assertNull(itemClassReader.read());
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		}
	}

	/**
	 * Tests read when the service returns an empty list.
	 */
	@Test
	public void readEmptylList() {

		ItemClassReader itemClassReader = new ItemClassReader();
		itemClassReader.setRepository(this.getRepository(new ArrayList<>()));

		try {
			// This list is empty, so it should return null
			Assert.assertNull(itemClassReader.read());
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		}
	}

	/*
	 * Support functions.
	 */
	/**
	 * Creates a ClassCommodity to test with.
	 *
	 * @return A ClassCommodity to test with.
	 */
	private ItemClass getTestItemClass() {

		ItemClass cc = new ItemClass();
		cc.setItemClassCode(CLASS_ID);
		cc.setItemClassDescription(DESCRIPTION);
		return cc;
	}

	/**
	 * Returns a list of ClassCommodities to test with.
	 *
	 * @return A list of ClassCommodities to test with.
	 */
	private List<ItemClass> getItemClassList() {

		List<ItemClass> classCommodities = new ArrayList<>(1);
		classCommodities.add(this.getTestItemClass());
		return classCommodities;
	}

	/**
	 * Returns a ItemClassService to test with.
	 *
	 * @return A ClassCommodityRepository to test with.
	 */
	private ItemClassRepository getRepository(List<ItemClass> pageToReturn) {

		ItemClassRepository repository = Mockito.mock(ItemClassRepository.class);
		Mockito.when(repository.findAll()).thenReturn(pageToReturn);
		return repository;
	}
}
