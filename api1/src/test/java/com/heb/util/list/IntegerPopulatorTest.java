package com.heb.util.list;

import com.heb.util.list.IntegerPopulator;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by d116773 on 5/8/2016.
 */
public class IntegerPopulatorTest {

	private IntegerPopulator integerPopulator = new IntegerPopulator();

	@Test
	public void testPopulateWorksEpmtyList() {
		List<Integer> list = new ArrayList<>();

		this.integerPopulator.populate(list, 10);

		Assert.assertEquals("list wrong size", 10, list.size());
		for (int i = 0; i <  list.size(); i++) {
			Assert.assertEquals("incorrect Integer in list", Integer.valueOf(0), list.get(i));
		}
	}

	@Test
	public void testPopulateWorksNullList() {
		// Not much to do here except test to make sure it doesn't throw an error.
		try {
			this.integerPopulator.populate(null, 10);
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		}
	}

	@Test
	public void testPopulateWorksNonEmptyList() {
		List<Integer> list = new ArrayList<>();

		list.add(Integer.valueOf(5));
		list.add(Integer.valueOf(10));

		this.integerPopulator.populate(list, 10);
		Assert.assertEquals("list wrong size", 10, list.size());
		Assert.assertEquals("first element changed", Integer.valueOf(5), list.get(0));
		Assert.assertEquals("first element changed", Integer.valueOf(10), list.get(1));
		for (int i = 2; i <  list.size(); i++) {
			Assert.assertEquals("incorrect Integer in list", Integer.valueOf(0), list.get(i));
		}
	}

	@Test
	public void testPopulateWorksListTooLarge() {
		List<Integer> list = new ArrayList<>();

		list.add(Integer.valueOf(5));
		list.add(Integer.valueOf(10));
		list.add(Integer.valueOf(15));
		list.add(Integer.valueOf(20));

		this.integerPopulator.populate(list, 2);
		Assert.assertEquals("list wrong size", 4, list.size());
		Assert.assertEquals("first element wrong", Integer.valueOf(5), list.get(0));
		Assert.assertEquals("second element wrong", Integer.valueOf(10), list.get(1));
		Assert.assertEquals("third element wrong", Integer.valueOf(15), list.get(2));
		Assert.assertEquals("fourth element wrong", Integer.valueOf(20), list.get(3));
	}
}
