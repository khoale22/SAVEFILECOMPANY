package com.heb.pm.entity;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author s753601
 * @version 2.8.0
 */
public class LocationKeyTest {

	/**
	 * Tests the getLocationType method
	 */
	@Test
	public void getLocationTypeTest(){
		Assert.assertEquals("D", getDefaultLocationKey().getLocationType());
	}

	/**
	 * Tests the setLocationType method
	 */
	@Test
	public void setLocationTypeTest(){
		LocationKey lk = getDefaultLocationKey();
		lk.setLocationType("T");
		Assert.assertEquals("T", lk.getLocationType());
	}

	/**
	 *Tests the getLocationNumber method
	 */
	@Test
	public void getLocationNumberTest(){
		Assert.assertEquals(1, getDefaultLocationKey().getLocationNumber());
	}

	/**
	 *Tests the setLocationNumber method
	 */
	@Test
	public void setLocationNumberTest(){
		LocationKey lk = getDefaultLocationKey();
		lk.setLocationNumber(3);
		Assert.assertEquals(3, lk.getLocationNumber());
	}

	/**
	 * Tests the toString method
	 */
	@Test
	public void toStringTest(){
		LocationKey lk = getDefaultLocationKey();
		Assert.assertEquals("LocationKey{locationType='D', locationNumber=1}", lk.toString());
	}

	/**
	 * Tests the equals method of the same object
	 */
	@Test
	public void equalsTest(){
		LocationKey lk = getDefaultLocationKey();
		Assert.assertTrue(lk.equals(getDefaultLocationKey()));
	}

	/**
	 * Tests the equal method of two different objects
	 */
	@Test
	public void notEqualsTest(){
		LocationKey lk = getDefaultLocationKey();
		Assert.assertFalse(lk.equals(getOtherLocationKey()));
	}

	/**
	 * Tests the hash code of the same object
	 */
	@Test
	public void hashSameCodeTest(){
		LocationKey lk = getDefaultLocationKey();
		Assert.assertEquals(lk.hashCode(), lk.hashCode());
	}

	/**
	 * Tests the hash code of two different objects
	 */
	@Test
	public void hashDifferentCodeTest(){
		LocationKey lk = getDefaultLocationKey();
		LocationKey lo = getOtherLocationKey();
		Assert.assertNotEquals(lk.hashCode(), lo.hashCode());
	}

	private LocationKey getDefaultLocationKey(){
		LocationKey lk = new LocationKey();
		lk.setLocationNumber(1);
		lk.setLocationType("D");
		return lk;
	}

	private LocationKey getOtherLocationKey(){
		LocationKey lk = new LocationKey();
		lk.setLocationType("O");
		lk.setLocationNumber(2);
		return lk;
	}
}