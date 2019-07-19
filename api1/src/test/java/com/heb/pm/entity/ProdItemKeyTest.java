package com.heb.pm.entity;

import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDate;

/**
 * Created by s573181 on 6/7/2016.
 */
public class ProdItemKeyTest {

    /*
	 * equals
	 */

    /**
     * Tests equals when passed the same object.
     */
    @Test
    public void testEqualsSameObject() {
        ProdItemKey key = this.getDefaultKey();
        boolean equal = key.equals(key);
        Assert.assertTrue("same object not equal", equal);
    }

    /**
     * Tests equals when passed a different object with the same values.
     */
    @Test
    public void testEqualsSimilarObject() {
        ProdItemKey key1 = this.getDefaultKey();
        ProdItemKey key2 = this.getDefaultKey();
        boolean equal = key1.equals(key2);
        Assert.assertTrue(equal);
    }


    /**
     * Tests equals when passed a null.
     */
    @Test
    public void testEqualsNull() {
        ProdItemKey key = this.getDefaultKey();
        boolean equal = key.equals(null);
        Assert.assertFalse(equal);
    }

    /**
     * Tests equals when passed an object with a different item code but the same type.
     */
    @Test
    public void testEqualsOtherItemCode() {
        ProdItemKey key1 = this.getDefaultKey();
        ProdItemKey key2 = this.getDsdKey();
        boolean equals = key1.equals(key2);
        Assert.assertFalse(equals);
    }


    /**
     * Tests equals when passed an object with the same item code but a different type.
     */
    @Test
    public void testEqualsDifferentType() {
        ProdItemKey key1 = this.getDefaultKey();
        ProdItemKey key2 = this.getDsdKey();
        boolean equals = key1.equals(key2);
        Assert.assertFalse(equals);
    }


	/*
	 * hashCode
	 */

    /**
     * Tests hashCode returns the same value for the same object.
     */
    @Test
    public void testHashCodeSameObject() {
        ProdItemKey key = this.getDefaultKey();
        Assert.assertEquals(key.hashCode(), key.hashCode());
    }

    /**
     * Tests hashCode returns the same value for different objects with the same values.
     */
    @Test
    public void testHashCodeSimilarObject() {
        ProdItemKey key1 = this.getDefaultKey();
        ProdItemKey key2 = this.getDefaultKey();
        Assert.assertEquals(key1.hashCode(), key2.hashCode());
    }


    /**
     * Tests hashCode returns different values for objects with the same type but different item codes.
     */
    @Test
    public void testHashCodeDifferentItemCode() {
        ProdItemKey key1 = this.getDefaultKey();
        ProdItemKey key2 = this.getAlternateItemCode();
        Assert.assertNotEquals(key1.hashCode(), key2.hashCode());
    }

    /**
     * Tests hashCode returns different values for objects with the same type but different item codes.
     */
    @Test
    public void testHashCodeDifferentItemType() {
        ProdItemKey key1 = this.getDefaultKey();
        ProdItemKey key2 = this.getAlternateItemType();
        Assert.assertNotEquals(key1.hashCode(), key2.hashCode());
    }

    /**
     * Tests hashCode returns different values for objects with the same type but different item codes.
     */
    @Test
    public void testHashCodeDifferentProdId() {
        ProdItemKey key1 = this.getDefaultKey();
        ProdItemKey key2 = this.getAlternateProductId();
        Assert.assertNotEquals(key1.hashCode(), key2.hashCode());
    }

    /*
	 * isDsd
	 */
    /**
     * Test isDsd on a DSD product.
     */
    @Test
    public void isDsdDsd() {
        ProdItemKey key = this.getAlternateItemType();
        Assert.assertTrue(key.isDsd());
    }

    /**
     * Tests isDsd on a warehouse product.
     */
    @Test
    public void isDsdWarehouse() {
        ProdItemKey key = this.getDefaultKey();
        Assert.assertFalse(key.isDsd());
    }

	/*
	 * isWarehouse
	 */
    /**
     * Tests isWarehouse on a DSD product.
     */
    @Test
    public void isWarehouseDsd() {
        ProdItemKey key = this.getAlternateItemType();
        Assert.assertFalse(key.isWarehouse());
    }

    /**
     * Tests isWarehouse on a warehouse product.
     */
    @Test
    public void isWarehouseWarehouse() {
        ProdItemKey key = this.getDefaultKey();
        Assert.assertTrue(key.isWarehouse());
    }

    /*
	 * getters
	 */

    /**
     * Tests getItemCode.
     */
    @Test
    public void testGetItemCode() {
        ProdItemKey key = this.getDefaultKey();
        Assert.assertEquals(key.getItemCode(), 86L);
    }

    /**
     * Tests getItemType.
     */
    @Test
    public void testGetItemType() {
        ProdItemKey key = this.getDefaultKey();
        Assert.assertEquals(key.getItemType(), ProdItemKey.WAREHOUSE);
    }

    /**
     * Tests getProdId.
     */
    @Test
    public void testGetProdId() {
        ProdItemKey key = this.getDefaultKey();
        Assert.assertEquals(key.getProductId(), 57L);
    }

    /**
     * Tests toString.
     */
    @Test
    public void testToString() {
        ProdItemKey key = this.getDefaultKey();
        Assert.assertEquals("ProdItemKey{itemType='ITMCD', itemCode=86, productId=57}", key.toString());
    }


    /*
	 * Support functions.
	 */

    /**
     * Returns the default test value.
     *
     * @return The default test value.
     */
    private ProdItemKey getDefaultKey() {
        ProdItemKey key = new ProdItemKey();
        key.setItemCode(86L);
        key.setProductId(57L);
        key.setItemType(ItemMasterKey.WAREHOUSE);
        return key;
    }

    /***
     * Returns an ItemCodeKey with the same type as the default but a different item code.
     *
     * @return An ItemCodeKey with the same type as the default but a different item code.
     */
    private ProdItemKey getDsdKey() {
        ProdItemKey key = new ProdItemKey();
        key.setItemCode(12282L);
        key.setProductId(264L);
        key.setItemType(ItemMasterKey.DSD);

        return key;
    }

    private ProdItemKey getAlternateItemCode(){
        ProdItemKey key = new ProdItemKey();
        key.setItemCode(12282L);
        key.setItemType(ItemMasterKey.WAREHOUSE);
        key.setProductId(57L);
        return key;
    }

    private ProdItemKey getAlternateItemType(){
        ProdItemKey key = new ProdItemKey();
        key.setItemCode(86L);
        key.setProductId(57L);
        key.setItemType(ItemMasterKey.DSD);
        return key;
    }
    private ProdItemKey getAlternateProductId(){
        ProdItemKey key = new ProdItemKey();
        key.setItemCode(86L);
        key.setProductId(200L);
        key.setItemType(ItemMasterKey.WAREHOUSE);
        return key;
    }


}
