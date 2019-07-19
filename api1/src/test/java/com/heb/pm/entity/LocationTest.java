package com.heb.pm.entity;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author s753601
 * @version 2.8.0
 */
public class LocationTest {

	/**
	 * Tests the getKey method.
	 */
	@Test
	public void getKeyTest(){
		LocationKey key = new LocationKey();
		key.setLocationNumber(1);
		key.setLocationType("D");
		Assert.assertEquals(key, getDefaultLocation().getKey());
	}

	/**
	 * Tests the setKey method
	 */
	@Test
	public void setKeyTest(){
		LocationKey key = new LocationKey();
		key.setLocationNumber(2);
		key.setLocationType("T");
		Location lo = getDefaultLocation();
		lo.setKey(key);
		Assert.assertEquals(key, lo.getKey());
	}

	/**
	 * Tests the getApVendorNumber method
	 */
	@Test
	public void getApVendorNumberTest(){
		Location lo = getAPLocation();
		Assert.assertEquals(new Integer(2), lo.getApVendorNumber());
	}

	/**
	 * Tests the setApVendorNumber method
	 */
	@Test
	public void setApVendorNumberTest(){
		Location lo = getAPLocation();
		lo.setApVendorNumber(new Integer(3));
		Assert.assertEquals(new Integer(3), lo.getApVendorNumber());
	}

	/**
	 * Tests the getApTypeCode method
	 */
	@Test
	public void getApTypeCodeTest(){
		Location lo = getAPLocation();
		Assert.assertEquals("A", lo.getApTypeCode());
	}

	/**
	 * Tests the setApTypeCode method
	 */
	@Test
	public void setApTypeCodeTest(){
		Location lo = getAPLocation();
		lo.setApTypeCode("B");
		Assert.assertEquals("B", lo.getApTypeCode());
	}

	/**
	 * Tests the getLocationName method
	 */
	@Test
	public void getLocationNameTest(){
		Assert.assertEquals("Default", getDefaultLocation().getLocationName());
	}

	/**
	 * Tests the setLocationNameTest
	 */
	@Test
	public void setLocationNameTest(){
		Location lo = getDefaultLocation();
		lo.setLocationName("Test");
		Assert.assertEquals("Test", lo.getLocationName());
	}

	/**
	 * Tests the getApLocationTest
	 */
	@Test
	public void getApLocationTest(){
		Assert.assertEquals(null, getAPLocation().getLocationName());
	}

	@Test
	public void setApLocationTest(){
		Location lo = getAPLocation();
		ApLocation alo = new ApLocation();
		ApLocationKey key= new ApLocationKey();
		key.setApTypeCode("Test");
		key.setApVendorNumber(123);
		alo.setKey(key);
		lo.setApLocation(alo);
		Assert.assertEquals(alo, lo.getApLocation());
	}

	@Test
	public void getDisplayNameTest(){
		Assert.assertEquals("Default[1]", getDefaultLocation().getDisplayName());
	}

	/**
	 * Tests the toString method
	 */
	@Test
	public void toStringTest(){
		Location lo = getDefaultLocation();
		Assert.assertEquals("Location{key=LocationKey{locationType='D', locationNumber=1}name=Default, apVendorNumber=null}", lo.toString());
	}

	/**
	 * Tests the equals method of the same object
	 */
	@Test
	public void equalsTest(){
		Location lo = getDefaultLocation();
		Assert.assertTrue(lo.equals(getDefaultLocation()));
	}

	/**
	 * Tests the equal method of two different objects
	 */
	@Test
	public void notEqualsTest(){
		Location lo = getDefaultLocation();
		Assert.assertFalse(lo.equals(getAPLocation()));
	}

	/**
	 * Tests the hash code of the same object
	 */
	@Test
	public void hashSameCodeTest(){
		Location lo = getDefaultLocation();
		Assert.assertEquals(lo.hashCode(), lo.hashCode());
	}

	/**
	 * Tests the hash code of two different objects
	 */
	@Test
	public void hashDifferentCodeTest(){
		Location lo = getDefaultLocation();
		Location lb = getAPLocation();
		Assert.assertNotEquals(lo.hashCode(), lb.hashCode());
	}

	private Location getDefaultLocation(){
		LocationKey key = new LocationKey();
		key.setLocationType("D");
		key.setLocationNumber(1);
		Location lo = new Location();
		lo.setKey(key);
		lo.setLocationName("Default");
		return lo;
	}

	private Location getAPLocation(){
		LocationKey key = new LocationKey();
		key.setLocationType("A");
		key.setLocationNumber(2);
		Location lo = new Location();
		lo.setKey(key);
		ApLocation ap = new ApLocation();
		lo.setApLocation(ap);
		lo.setApTypeCode("A");
		lo.setApVendorNumber(2);
		return lo;
	}
}