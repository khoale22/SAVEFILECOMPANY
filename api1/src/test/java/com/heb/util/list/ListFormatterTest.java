package com.heb.util.list;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by d116773 on 5/8/2016.
 */
public class ListFormatterTest {

	@Test
	public void testFormatWorksGoodObject() {
		List<Integer> list = new ArrayList<>();
		list.add(Integer.valueOf(0));
		list.add(Integer.valueOf(1));

		String s = ListFormatter.formatAsString(list);
		Assert.assertEquals("good list not formatted correctly", "0,1", s);
	}

	@Test
	public void testFormatWorksNoComma() {
		List<Integer> list = new ArrayList<>();
		list.add(Integer.valueOf(0));
		list.add(Integer.valueOf(1));

		String s = ListFormatter.formatAsString(list);
		Assert.assertNotEquals("good list not formatted correctly", ',', s.charAt(s.length() -1 ));
	}

	@Test
	public void testEmpyListIsBlank() {
		List<Integer> list = new ArrayList<>();
		String s = ListFormatter.formatAsString(list);
		Assert.assertEquals("empty list not formatted correctly", "", s);
	}

	@Test
	public void testNullListIsBlank() {
		String s = ListFormatter.formatAsString(null);
		Assert.assertEquals("null list not formatted correctly", "", s);
	}

	@Test
	public void testSingleItemList() {
		List<Integer> list = new ArrayList<>();
		list.add(0);
		String s = ListFormatter.formatAsString(list);
		Assert.assertEquals("single item list not formatted correctly", "0", s);
	}

	@Test
	public void testFormatWorksGoodObjectNewDelimiter() {
		List<Integer> list = new ArrayList<>();
		list.add(Integer.valueOf(0));
		list.add(Integer.valueOf(1));

		String s = ListFormatter.formatAsString(list, '^');
		Assert.assertEquals("good list not formatted correctly", "0^1", s);
	}


	@Test
	public void testFormatWorksNoCarret() {
		List<Integer> list = new ArrayList<>();
		list.add(Integer.valueOf(0));
		list.add(Integer.valueOf(1));

		String s = ListFormatter.formatAsString(list, '^');
		Assert.assertNotEquals("good list not formatted correctly", '^', s.charAt(s.length() -1 ));
	}
}
