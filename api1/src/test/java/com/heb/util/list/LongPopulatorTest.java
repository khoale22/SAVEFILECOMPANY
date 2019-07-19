package com.heb.util.list;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Tests LongPopulator
 * 
 * Created by d116773 on 5/8/2016.
 */
public class LongPopulatorTest {

	private LongPopulator LongPopulator = new LongPopulator();

	@Test
	public void testPopulateWorksEpmtyList() {
		List<Long> list = new ArrayList<>();

		this.LongPopulator.populate(list, 10);

		Assert.assertEquals("list wrong size", 10, list.size());
		for (int i = 0; i <  list.size(); i++) {
			Assert.assertEquals("incorrect Long in list", Long.valueOf(0), list.get(i));
		}
	}

	@Test
	public void testPopulateWorksNullList() {
		// Not much to do here except test to make sure it doesn't throw an error.
		try {
			this.LongPopulator.populate(null, 10);
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		}
	}

	@Test
	public void testPopulateWorksNonEmptyList() {
		List<Long> list = new ArrayList<>();

		list.add(Long.valueOf(5));
		list.add(Long.valueOf(10));

		this.LongPopulator.populate(list, 10);
		Assert.assertEquals("list wrong size", 10, list.size());
		Assert.assertEquals("first element changed", Long.valueOf(5), list.get(0));
		Assert.assertEquals("first element changed", Long.valueOf(10), list.get(1));
		for (int i = 2; i <  list.size(); i++) {
			Assert.assertEquals("incorrect Long in list", Long.valueOf(0), list.get(i));
		}
	}

	@Test
	public void testPopulateWorksListTooLarge() {
		List<Long> list = new ArrayList<>();

		list.add(Long.valueOf(5));
		list.add(Long.valueOf(10));
		list.add(Long.valueOf(15));
		list.add(Long.valueOf(20));

		this.LongPopulator.populate(list, 2);
		Assert.assertEquals("list wrong size", 4, list.size());
		Assert.assertEquals("first element wrong", Long.valueOf(5), list.get(0));
		Assert.assertEquals("second element wrong", Long.valueOf(10), list.get(1));
		Assert.assertEquals("third element wrong", Long.valueOf(15), list.get(2));
		Assert.assertEquals("fourth element wrong", Long.valueOf(20), list.get(3));
	}
}
