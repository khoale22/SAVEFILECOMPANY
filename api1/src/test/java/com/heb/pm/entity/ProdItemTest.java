/*
 *  com.heb.pm.entity.ItemMasterRepositoryTest
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */

package com.heb.pm.entity;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import testSupport.LoggingSupportTestRunner;

import java.time.LocalDate;

/**
 * Tests Prod_Item
 *
 * Created by s573181 on 6/7/2016.
 */

@RunWith(LoggingSupportTestRunner.class)
@ContextConfiguration(locations = "classpath*:testConfig.xml")
public class ProdItemTest {

    @Autowired
    private ItemRepositoryTest itemRepositoryTest;

    /*
	 * equals
	 */

	/**
	 * Test equals on the same object.
	 */
	@Test
	public void equalsSameObject() {
        ProdItem im = this.getDefaultRecord();
		boolean equals = im.equals(im);
		Assert.assertTrue(equals);
	}

    /**
     * Test equals on similar objects.
     */
    @Test
    public void equalsSimilarObject(){
        ProdItem im1 = this.getDefaultRecord();
        ProdItem im2 = this.getDefaultRecord();
        boolean equals = im1.equals(im2);
        Assert.assertTrue(equals);
    }

    /**
     * Test equals on objects with different keys.
     */
    @Test
    public void equalsDifferentKeys() {
        ProdItem im1 = this.getDefaultRecord();
        ProdItem im2 = this.getDSDRecord();
        boolean equals = im1.equals(im2);
        Assert.assertFalse(equals);
    }

    /**
     * Test equals on null
     */
    @Test
    public void testEqualsNull() {
        ProdItem im = this.getDefaultRecord();
        boolean equals = im.equals(null);
        Assert.assertFalse(equals);
    }

    /**
     * Tests equals on a different class.
     */
    @Test
    public void testEqualsDifferentClass() {
        ProdItem im = this.getDefaultRecord();
        boolean equals = im.equals(Integer.valueOf(66));
        Assert.assertFalse(equals);
    }


    /**
     * Tests equals when the left side has a null key.
     */
    @Test
    public void testEqualsNullSourceKey() {
        ProdItem im1 = new ProdItem();
        ProdItem im2 = this.getDefaultRecord();
        boolean equals = im1.equals(im2);
        Assert.assertFalse(equals);
    }


    /**
     * Test hashCode on the same object.
     */
    @Test
    public void hashCodeSameObject() {
        ProdItem im = this.getDefaultRecord();
        Assert.assertEquals(im.hashCode(), im.hashCode());
    }

    /**
     * Test hashCode on similar objects.
     */
    @Test
    public void hashCodeSimilarObjects() {
        ProdItem im1 = this.getDefaultRecord();
        ProdItem im2 = this.getDefaultRecord();
        Assert.assertEquals(im1.hashCode(), im2.hashCode());
    }

    /**
     * Test hashCode on different objects.
     */
    @Test
    public void hashCodeDifferentObjects() {
        ProdItem im1 = this.getDefaultRecord();
        ProdItem im2 = this.getDSDRecord();
        Assert.assertNotEquals(im1.hashCode(), im2.hashCode());
    }

    /**
     * Test hashCode with a null key.
     */
    @Test
    public void hashCodeNullKey() {
        ItemMaster im = new ItemMaster();
        Assert.assertEquals(0, im.hashCode());
    }

    /**
     * Test hashCode when the left side has a key and the right side does not.
     */
    @Test
    public void hashCodeNullRight() {
        ProdItem im1 = this.getDefaultRecord();
        ProdItem im2 = new ProdItem();
        Assert.assertNotEquals(im1.hashCode(), im2.hashCode());
    }

    /**
     * TODO:
     * Tests toString.
     */
    @Test
    public void testToString() {
        ProdItem pm = this.getDSDRecord();
        System.out.println(pm.toString());
        Assert.assertEquals("ProdItem{key=ProdItemKey{itemType='DSD  ', itemCode=66, productId=272}, productCount=0}", pm.toString());
    }


    /*
	 * Support functions.
	 */

	/**
	 * Returns an ProdItem object that equals the one that is first in the test table.
	 *
	 * @return An ProdItem object that equals the one that is first in the test table.
	 */
	private ProdItem getDefaultRecord() {

        ProdItemKey prodItemKey = new ProdItemKey();
        prodItemKey.setProductId(272L);
        prodItemKey.setItemCode(66L);
        prodItemKey.setItemType(ProdItemKey.WAREHOUSE);

        ProductMaster productMaster= new ProductMaster();
        productMaster.setDescription("CAPPUCCINO SMALL              ");
        productMaster.setProdId(272L);

        ItemMasterKey itemMasterKey = new ItemMasterKey();
        itemMasterKey.setItemCode(66L);
        itemMasterKey.setItemType(ProdItemKey.WAREHOUSE);

        ItemMaster itemMaster = new ItemMaster();
        itemMaster.setDescription("HONEYWELL QTCL CMPT TWR AR PUR");
        itemMaster.setDiscontinueDate(LocalDate.of(1600, 01, 01));
        itemMaster.setKey(itemMasterKey);

        ProdItem prodItem = new ProdItem();
        prodItem.setProductMaster(productMaster);
        prodItem.setItemMaster(itemMaster);
        prodItem.setKey(prodItemKey);
		return prodItem;
	}

    /**
     * Returns an DSD ProdItem object .
	 *
	 * @return An ProdItem object a default ProdItem Object.
	 */
	private ProdItem getDSDRecord() {
        ProdItem prodItem = this.getDefaultRecord();

        prodItem.getKey().setItemType(ProdItemKey.DSD);
        prodItem.getItemMaster().getKey().setItemType(ProdItemKey.DSD);

		return prodItem;
	}

}
