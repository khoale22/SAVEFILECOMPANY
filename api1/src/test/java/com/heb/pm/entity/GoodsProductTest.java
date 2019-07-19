package com.heb.pm.entity;

import org.junit.Assert;
import org.junit.Test;
import java.sql.Date;
import java.time.LocalDate;

public class GoodsProductTest {

	private static final String DEFAULT_STRING="TEST";
	private static final String DEFAULT_PSE_STRING="Default";
	private static final String OTHER_PSE_STRING="PSE Non-Solid";
	private static final String DEFAULT_MEDICAID_STRING="Average Wholesale Price";
	private static final Integer DEFAULT_INTEGER=1;
	private static final double DEFAULT_DOUBLE=1.0;
	private static final double DEFAULT_DELTA = 0.0;
	private static final Long DEFAULT_LONG=1L;
	private static final boolean DEFAULT_BOOLEAN = true;
	private static final String OTHER_STRING="OTHER";
	private static final Integer OTHER_INTEGER=2;
	private static final double OTHER_DOUBLE=2.0;
	private static final Long OTHER_LONG=2L;
	private static final boolean OTHER_BOOLEAN = false;

	/**
	 * Tests the getLastUpdatedOn method
	 */
	@Test
	public void getLastUpdatedOnTest() {
		GoodsProduct record = getDefaultRecord(DEFAULT_STRING, DEFAULT_LONG, DEFAULT_INTEGER, DEFAULT_DOUBLE, DEFAULT_BOOLEAN);
		Assert.assertTrue(record.getLastUpdatedOn() instanceof Date);
	}

	/**
	 * Tests the setLastUpdatedOn method
	 */
	@Test
	public void setLastUpdatedOnTest() {
		Date newDate = Date.valueOf(LocalDate.now());
		GoodsProduct record = getDefaultRecord(DEFAULT_STRING, DEFAULT_LONG, DEFAULT_INTEGER, DEFAULT_DOUBLE, DEFAULT_BOOLEAN);
		record.setLastUpdatedOn(newDate);
		Assert.assertEquals(newDate, newDate);
	}

	/**
	 * Tests the getLastUpdateBy method
	 */
	@Test
	public void getLastUpdateByTest() {
		GoodsProduct record = getDefaultRecord(DEFAULT_STRING, DEFAULT_LONG, DEFAULT_INTEGER, DEFAULT_DOUBLE, DEFAULT_BOOLEAN);
		Assert.assertEquals(DEFAULT_STRING, record.getLastUpdateBy());
	}

	/**
	 * Tests the setLastUpdateBy method
	 */
	@Test
	public void setLastUpdateByTest() {
		GoodsProduct record = getDefaultRecord(DEFAULT_STRING, DEFAULT_LONG, DEFAULT_INTEGER, DEFAULT_DOUBLE, DEFAULT_BOOLEAN);
		record.setLastUpdateBy(OTHER_STRING);
		Assert.assertEquals(OTHER_STRING, record.getLastUpdateBy());
	}

	/**
	 * Tests the getRetailLength method
	 */
	@Test
	public void getRetailLengthTest() {
		GoodsProduct record = getDefaultRecord(DEFAULT_STRING, DEFAULT_LONG, DEFAULT_INTEGER, DEFAULT_DOUBLE, DEFAULT_BOOLEAN);
		Assert.assertEquals(DEFAULT_DOUBLE, record.getRetailLength(), DEFAULT_DELTA);
	}

	/**
	 * Tests the setRetailLength method
	 */
	@Test
	public void setRetailLengthTest() {
		GoodsProduct record = getDefaultRecord(DEFAULT_STRING, DEFAULT_LONG, DEFAULT_INTEGER, DEFAULT_DOUBLE, DEFAULT_BOOLEAN);
		record.setRetailLength(OTHER_DOUBLE);
		Assert.assertEquals(OTHER_DOUBLE, record.getRetailLength(), DEFAULT_DELTA);

	}

	/**
	 * Tests the getRetailWidth method
	 */
	@Test
	public void getRetailWidthTest() {
		GoodsProduct record = getDefaultRecord(DEFAULT_STRING, DEFAULT_LONG, DEFAULT_INTEGER, DEFAULT_DOUBLE, DEFAULT_BOOLEAN);
		Assert.assertEquals(DEFAULT_DOUBLE, record.getRetailWidth(), DEFAULT_DELTA);
	}

	/**
	 * Tests the setRetailWidth method
	 */
	@Test
	public void setRetailWidthTest() {
		GoodsProduct record = getDefaultRecord(DEFAULT_STRING, DEFAULT_LONG, DEFAULT_INTEGER, DEFAULT_DOUBLE, DEFAULT_BOOLEAN);
		record.setRetailWidth(OTHER_DOUBLE);
		Assert.assertEquals(OTHER_DOUBLE, record.getRetailWidth(), DEFAULT_DELTA);
	}

	/**
	 * Tests the getRetailWidth method
	 */
	@Test
	public void getRetailHeightTest() {
		GoodsProduct record = getDefaultRecord(DEFAULT_STRING, DEFAULT_LONG, DEFAULT_INTEGER, DEFAULT_DOUBLE, DEFAULT_BOOLEAN);
		Assert.assertEquals(DEFAULT_DOUBLE, record.getRetailWidth(), DEFAULT_DELTA);

	}

	/**
	 * Tests the setRetailHeight method
	 */
	@Test
	public void setRetailHeightTest() {
		GoodsProduct record = getDefaultRecord(DEFAULT_STRING, DEFAULT_LONG, DEFAULT_INTEGER, DEFAULT_DOUBLE, DEFAULT_BOOLEAN);
		record.setRetailHeight(OTHER_DOUBLE);
		Assert.assertEquals(OTHER_DOUBLE, record.getRetailHeight(), DEFAULT_DELTA);

	}

	/**
	 * Tests the getRetailWeight method
	 */
	@Test
	public void getRetailWeightTest() {
		GoodsProduct record = getDefaultRecord(DEFAULT_STRING, DEFAULT_LONG, DEFAULT_INTEGER, DEFAULT_DOUBLE, DEFAULT_BOOLEAN);
		Assert.assertEquals(DEFAULT_DOUBLE, record.getRetailWeight(), DEFAULT_DELTA);
	}

	/**
	 * Tests the setRetailWeight method
	 */
	@Test
	public void setRetailWeightTest() {
		GoodsProduct record = getDefaultRecord(DEFAULT_STRING, DEFAULT_LONG, DEFAULT_INTEGER, DEFAULT_DOUBLE, DEFAULT_BOOLEAN);
		record.setRetailWeight(OTHER_DOUBLE);
		Assert.assertEquals(OTHER_DOUBLE, record.getRetailWeight(), DEFAULT_DELTA);
	}

	/**
	 * Tests the getMaxNestCount method
	 */
	@Test
	public void getMaxNestCountTest() {
		GoodsProduct record = getDefaultRecord(DEFAULT_STRING, DEFAULT_LONG, DEFAULT_INTEGER, DEFAULT_DOUBLE, DEFAULT_BOOLEAN);
		Assert.assertEquals(DEFAULT_INTEGER, record.getMaxNestCount());
	}

	/**
	 * Tests the getMaxNestCount method
	 */
	@Test
	public void setMaxNestCountTest() {
		GoodsProduct record = getDefaultRecord(DEFAULT_STRING, DEFAULT_LONG, DEFAULT_INTEGER, DEFAULT_DOUBLE, DEFAULT_BOOLEAN);
		record.setMaxNestCount(OTHER_INTEGER);
		Assert.assertEquals(OTHER_INTEGER, record.getMaxNestCount());
	}

	/**
	 * Tests the isStackableSwitch method
	 */
	@Test
	public void isStackableSwitchTest() {
		GoodsProduct record = getDefaultRecord(DEFAULT_STRING, DEFAULT_LONG, DEFAULT_INTEGER, DEFAULT_DOUBLE, DEFAULT_BOOLEAN);
		Assert.assertEquals(DEFAULT_BOOLEAN, record.getStackableSwitch());

	}

	/**
	 * Tests the setStackableSwitch method
	 */
	@Test
	public void setStackableSwitchTest() {
		GoodsProduct record = getDefaultRecord(DEFAULT_STRING, DEFAULT_LONG, DEFAULT_INTEGER, DEFAULT_DOUBLE, DEFAULT_BOOLEAN);
		record.setStackableSwitch(OTHER_BOOLEAN);
		Assert.assertEquals(OTHER_BOOLEAN, record.getStackableSwitch());
	}

	/**
	 * Tests the getRetailIncrLength method
	 */
	@Test
	public void getRetailIncrLengthTest() {
		GoodsProduct record = getDefaultRecord(DEFAULT_STRING, DEFAULT_LONG, DEFAULT_INTEGER, DEFAULT_DOUBLE, DEFAULT_BOOLEAN);
		Assert.assertEquals(DEFAULT_DOUBLE, record.getRetailIncrLength(), DEFAULT_DELTA);

	}

	/**
	 * Tests the setRetailIncrLength method
	 */
	@Test
	public void setRetailIncrLengthTest() {
		GoodsProduct record = getDefaultRecord(DEFAULT_STRING, DEFAULT_LONG, DEFAULT_INTEGER, DEFAULT_DOUBLE, DEFAULT_BOOLEAN);
		record.setRetailIncrLength(OTHER_DOUBLE);
		Assert.assertEquals(OTHER_DOUBLE, record.getRetailIncrLength(), DEFAULT_DELTA);
	}

	/**
	 * Tests the getRetailIncrHeight method
	 */
	@Test
	public void getRetailIncrHeightTest() {
		GoodsProduct record = getDefaultRecord(DEFAULT_STRING, DEFAULT_LONG, DEFAULT_INTEGER, DEFAULT_DOUBLE, DEFAULT_BOOLEAN);
		Assert.assertEquals(DEFAULT_DOUBLE, record.getRetailIncrHeight(), DEFAULT_DELTA);
	}

	/**
	 * Tests the setRetailIncrHeight method
	 */
	@Test
	public void setRetailIncrHeightTest() {
		GoodsProduct record = getDefaultRecord(DEFAULT_STRING, DEFAULT_LONG, DEFAULT_INTEGER, DEFAULT_DOUBLE, DEFAULT_BOOLEAN);
		record.setRetailIncrHeight(OTHER_DOUBLE);
		Assert.assertEquals(OTHER_DOUBLE, record.getRetailIncrHeight(), DEFAULT_DELTA);
	}

	/**
	 * Tests the getRetailIncrWidth method
	 */
	@Test
	public void getRetailIncrWidthTest() {
		GoodsProduct record = getDefaultRecord(DEFAULT_STRING, DEFAULT_LONG, DEFAULT_INTEGER, DEFAULT_DOUBLE, DEFAULT_BOOLEAN);
		Assert.assertEquals(DEFAULT_DOUBLE, record.getRetailIncrWidth(), DEFAULT_DELTA);
	}

	/**
	 * Tests the getRetailIncrWidth method
	 */
	@Test
	public void setRetailIncrWidthTest() {
		GoodsProduct record = getDefaultRecord(DEFAULT_STRING, DEFAULT_LONG, DEFAULT_INTEGER, DEFAULT_DOUBLE, DEFAULT_BOOLEAN);
		record.setRetailIncrWidth(OTHER_DOUBLE);
		Assert.assertEquals(OTHER_DOUBLE, record.getRetailIncrWidth(), DEFAULT_DELTA);
	}

	/**
	 * Tests the getProdId method
	 */
	@Test
	public void getProdIdTest() {
		GoodsProduct record = getDefaultRecord(DEFAULT_STRING, DEFAULT_LONG, DEFAULT_INTEGER, DEFAULT_DOUBLE, DEFAULT_BOOLEAN);
		Assert.assertEquals(DEFAULT_LONG, record.getProdId());
	}

	/**
	 * Tests the setProdId method
	 */
	@Test
	public void setProdIdTest() {
		GoodsProduct record = getDefaultRecord(DEFAULT_STRING, DEFAULT_LONG, DEFAULT_INTEGER, DEFAULT_DOUBLE, DEFAULT_BOOLEAN);
		record.setProdId(OTHER_LONG);
		Assert.assertEquals(OTHER_LONG, record.getProdId());
	}

	/**
	 * Tests the getTagEffectiveDate method
	 */
	@Test
	public void getTagEffectiveDateTest() {
		GoodsProduct record = getDefaultRecord(DEFAULT_STRING, DEFAULT_LONG, DEFAULT_INTEGER, DEFAULT_DOUBLE, DEFAULT_BOOLEAN);
		Assert.assertTrue(record.getTagEffectiveDate() instanceof LocalDate);
	}

	/**
	 * Tests the setTagEffectiveDate method
	 */
	@Test
	public void setTagEffectiveDateTest() {
		LocalDate newDate = LocalDate.now();
		GoodsProduct record = getDefaultRecord(DEFAULT_STRING, DEFAULT_LONG, DEFAULT_INTEGER, DEFAULT_DOUBLE, DEFAULT_BOOLEAN);
		record.setTagEffectiveDate(newDate);
		Assert.assertEquals(newDate, record.getTagEffectiveDate());
	}

	/**
	 * Tests the getShelfTagSizeCode method
	 */
	@Test
	public void getShelfTagSizeCodeTest() {
		GoodsProduct record = getDefaultRecord(DEFAULT_STRING, DEFAULT_LONG, DEFAULT_INTEGER, DEFAULT_DOUBLE, DEFAULT_BOOLEAN);
		Assert.assertEquals(DEFAULT_STRING, record.getShelfTagSizeCode());

	}

	/**
	 * Tests the setShelfTagSizeCode method
	 */
	@Test
	public void setShelfTagSizeCodeTest() {
		GoodsProduct record = getDefaultRecord(DEFAULT_STRING, DEFAULT_LONG, DEFAULT_INTEGER, DEFAULT_DOUBLE, DEFAULT_BOOLEAN);
		record.setShelfTagSizeCode(OTHER_STRING);
		Assert.assertEquals(OTHER_STRING, record.getShelfTagSizeCode());
	}

	/**
	 * Tests the getNumberOfTags method
	 */
	@Test
	public void getNumberOfTagsTest() {
		GoodsProduct record = getDefaultRecord(DEFAULT_STRING, DEFAULT_LONG, DEFAULT_INTEGER, DEFAULT_DOUBLE, DEFAULT_BOOLEAN);
		Assert.assertEquals(DEFAULT_INTEGER, record.getNumberOfTags());
	}

	/**
	 * Tests the setNumberOfTags method
	 */
	@Test
	public void setNumberOfTagsTest() {
		GoodsProduct record = getDefaultRecord(DEFAULT_STRING, DEFAULT_LONG, DEFAULT_INTEGER, DEFAULT_DOUBLE, DEFAULT_BOOLEAN);
		record.setNumberOfTags(OTHER_INTEGER);
		Assert.assertEquals(OTHER_INTEGER, record.getNumberOfTags());
	}

	/**
	 * Tests the getPSEDisplay method
	 */
	@Test
	public void getPseTypeCodeTest() {
		GoodsProduct record = getDefaultRecord(DEFAULT_STRING, DEFAULT_LONG, DEFAULT_INTEGER, DEFAULT_DOUBLE, DEFAULT_BOOLEAN);
		Assert.assertEquals(DEFAULT_PSE_STRING, record.getPSEDisplay());
	}

	/**
	 * Tests the setPseTypeCode method
	 */
	@Test
	public void setPseTypeCodeTest() {
		GoodsProduct record = getDefaultRecord(DEFAULT_STRING, DEFAULT_LONG, DEFAULT_INTEGER, DEFAULT_DOUBLE, DEFAULT_BOOLEAN);
		record.setPseTypeCode("L");
		Assert.assertEquals(OTHER_PSE_STRING, record.getPSEDisplay());
	}

	/**
	 * Tests the getMedicaidCode method
	 */
	@Test
	public void getMedicaidCodeTest() {
		GoodsProduct record = getDefaultRecord(DEFAULT_STRING, DEFAULT_LONG, DEFAULT_INTEGER, DEFAULT_DOUBLE, DEFAULT_BOOLEAN);
		Assert.assertEquals(DEFAULT_STRING, record.getMedicaidCode());
	}

	/**
	 * Tests the setMedicaidCode method
	 */
	@Test
	public void setMedicaidCodeTest() {
		GoodsProduct record = getDefaultRecord(DEFAULT_STRING, DEFAULT_LONG, DEFAULT_INTEGER, DEFAULT_DOUBLE, DEFAULT_BOOLEAN);
		record.setMedicaidCode(OTHER_STRING);
		Assert.assertEquals(OTHER_STRING, record.getMedicaidCode());
	}

	/**
	 * Tests the isRxProductFlag method
	 */
	@Test
	public void isRxProductFlagTest() {
		GoodsProduct record = getDefaultRecord(DEFAULT_STRING, DEFAULT_LONG, DEFAULT_INTEGER, DEFAULT_DOUBLE, DEFAULT_BOOLEAN);
		Assert.assertEquals(DEFAULT_BOOLEAN, record.getRxProductFlag());
	}

	/**
	 * Tests the setRxProductFlag method
	 */
	@Test
	public void setRxProductFlagTest() {
		GoodsProduct record = getDefaultRecord(DEFAULT_STRING, DEFAULT_LONG, DEFAULT_INTEGER, DEFAULT_DOUBLE, DEFAULT_BOOLEAN);
		record.setRxProductFlag(OTHER_BOOLEAN);
		Assert.assertEquals(OTHER_BOOLEAN, record.getRxProductFlag());
	}

	/**
	 * Tests the getGramsOfPSE method
	 */
	@Test
	public void getGramsOfPSETest() {
		GoodsProduct record = getDefaultRecord(DEFAULT_STRING, DEFAULT_LONG, DEFAULT_INTEGER, DEFAULT_DOUBLE, DEFAULT_BOOLEAN);
		Assert.assertEquals(DEFAULT_LONG, record.getGramsOfPSE());
	}

	/**
	 * Tests the setGramsOfPSE method
	 */
	@Test
	public void setGramsOfPSETest() {
		GoodsProduct record = getDefaultRecord(DEFAULT_STRING, DEFAULT_LONG, DEFAULT_INTEGER, DEFAULT_DOUBLE, DEFAULT_BOOLEAN);
		record.setGramsOfPSE(OTHER_LONG);
		Assert.assertEquals(OTHER_LONG, record.getGramsOfPSE());
	}

	/**
	 * Tests the getCreatedOn method
	 */
	@Test
	public void getCreatedOnTest() {
		GoodsProduct record = getDefaultRecord(DEFAULT_STRING, DEFAULT_LONG, DEFAULT_INTEGER, DEFAULT_DOUBLE, DEFAULT_BOOLEAN);
		Assert.assertTrue(record.getCreatedOn() instanceof Date);
	}

	/**
	 * Tests the setCreatedOn method
	 */
	@Test
	public void setCreatedOnTest() {
		Date newDate = Date.valueOf(LocalDate.now());
		GoodsProduct record = getDefaultRecord(DEFAULT_STRING, DEFAULT_LONG, DEFAULT_INTEGER, DEFAULT_DOUBLE, DEFAULT_BOOLEAN);
		record.setCreatedOn(newDate);
		Assert.assertEquals(newDate, record.getCreatedOn());
	}

	/**
	 * Tests the getCreatedBy method
	 */
	@Test
	public void getCreatedByTest() {
		GoodsProduct record = getDefaultRecord(DEFAULT_STRING, DEFAULT_LONG, DEFAULT_INTEGER, DEFAULT_DOUBLE, DEFAULT_BOOLEAN);
		Assert.assertEquals(DEFAULT_STRING, record.getCreatedBy());
	}

	/**
	 * Tests the setCreatedBy method
	 */
	@Test
	public void setCreatedByTest() {
		GoodsProduct record = getDefaultRecord(DEFAULT_STRING, DEFAULT_LONG, DEFAULT_INTEGER, DEFAULT_DOUBLE, DEFAULT_BOOLEAN);
		record.setCreatedBy(OTHER_STRING);
		Assert.assertEquals(OTHER_STRING, record.getCreatedBy());
	}

	/**
	 * Tests the getRxProduct method
	 */
	@Test
	public void getRxProductTest() {
		GoodsProduct record = getDefaultRecord(DEFAULT_STRING, DEFAULT_LONG, DEFAULT_INTEGER, DEFAULT_DOUBLE, DEFAULT_BOOLEAN);
		Assert.assertEquals(getDefaultRxProduct(DEFAULT_LONG), record.getRxProduct());
	}

	/**
	 * Tests the setRxProduct method
	 */
	@Test
	public void setRxProductTest() {
		GoodsProduct record = getDefaultRecord(DEFAULT_STRING, DEFAULT_LONG, DEFAULT_INTEGER, DEFAULT_DOUBLE, DEFAULT_BOOLEAN);
		record.setRxProduct(null);
		Assert.assertEquals(null, record.getRxProduct());
	}

	/**
	 * Tests the getPSEDisplay method
	 */
	@Test
	public void getPSEDisplayTest() {
		GoodsProduct record = getDefaultRecord(DEFAULT_STRING, DEFAULT_LONG, DEFAULT_INTEGER, DEFAULT_DOUBLE, DEFAULT_BOOLEAN);
		Assert.assertEquals(DEFAULT_PSE_STRING, record.getPSEDisplay());
	}

	/**
	 * Tests the getMedicaidDisplay method
	 */
	@Test
	public void getMedicaidDisplayTest() {
		GoodsProduct record = getDefaultRecord(DEFAULT_STRING, DEFAULT_LONG, DEFAULT_INTEGER, DEFAULT_DOUBLE, DEFAULT_BOOLEAN);
		Assert.assertEquals(DEFAULT_MEDICAID_STRING, record.getMedicaidDisplay());
	}



	/*
	 * equals
	 */

	/**
	 * Tests that an object equals itself.
	 */
	@Test
	public void equalsSameObject() {
		GoodsProduct item = this.getDefaultRecord(DEFAULT_STRING, DEFAULT_LONG, DEFAULT_INTEGER, DEFAULT_DOUBLE, DEFAULT_BOOLEAN);
		boolean equals = item.equals(item);
		Assert.assertTrue(equals);
	}

	/**
	 * Tests that an object equals an object with the different values.
	 */
	@Test
	public void equalsDifferentObject() {
		GoodsProduct i1 = this.getDefaultRecord(DEFAULT_STRING, DEFAULT_LONG, DEFAULT_INTEGER, DEFAULT_DOUBLE, DEFAULT_BOOLEAN);
		GoodsProduct i2 = this.getDefaultRecord(OTHER_STRING, OTHER_LONG, OTHER_INTEGER, OTHER_DOUBLE, OTHER_BOOLEAN);
		boolean equals = i1.equals(i2);
		Assert.assertFalse(equals);
	}

	/**
	 * Tests that an object equals an object with the same values.
	 */
	@Test
	public void equalsSimilarObject() {
		GoodsProduct i1 = this.getDefaultRecord(DEFAULT_STRING, DEFAULT_LONG, DEFAULT_INTEGER, DEFAULT_DOUBLE, DEFAULT_BOOLEAN);
		GoodsProduct i2 = this.getDefaultRecord(DEFAULT_STRING, DEFAULT_LONG, DEFAULT_INTEGER, DEFAULT_DOUBLE, DEFAULT_BOOLEAN);
		boolean equals = i1.equals(i2);
		Assert.assertTrue(equals);
	}

	/**
	 * Tests that an object equals an object with the same values.
	 */
	@Test
	public void equalsSameKeyObject() {
		GoodsProduct i1 = this.getDefaultRecord(DEFAULT_STRING, DEFAULT_LONG, DEFAULT_INTEGER, DEFAULT_DOUBLE, DEFAULT_BOOLEAN);
		GoodsProduct i2 = this.getDefaultRecord(OTHER_STRING, DEFAULT_LONG, OTHER_INTEGER, OTHER_DOUBLE, OTHER_BOOLEAN);
		boolean equals = i1.equals(i2);
		Assert.assertTrue(equals);
	}

	/**
	 * Tests that it does not equal when passed null.
	 */
	@Test
	public void equalsNull() {
		GoodsProduct item = this.getDefaultRecord(DEFAULT_STRING, DEFAULT_LONG, DEFAULT_INTEGER, DEFAULT_DOUBLE, DEFAULT_BOOLEAN);
		boolean equals = item.equals(null);
		Assert.assertFalse(equals);
	}

	/**
	 * Test that it does not equal an object of a different type.
	 */
	@Test
	public void equalsDifferentType() {
		GoodsProduct item = this.getDefaultRecord(DEFAULT_STRING, DEFAULT_LONG, DEFAULT_INTEGER, DEFAULT_DOUBLE, DEFAULT_BOOLEAN);
		boolean equals = item.equals(Integer.valueOf(0));
		Assert.assertFalse(equals);
	}

	/**
	 * Test that it does not equal an object with a null key.
	 */
	@Test
	public void equalsNullKeyOther() {
		GoodsProduct item = this.getDefaultRecord(DEFAULT_STRING, DEFAULT_LONG, DEFAULT_INTEGER, DEFAULT_DOUBLE, DEFAULT_BOOLEAN);
		GoodsProduct i2 = new GoodsProduct();
		boolean equals = item.equals(i2);
		Assert.assertFalse(equals);
	}

	/**
	 * Test that it does not equal an object when it's key is null.
	 */
	@Test
	public void equalsNullKeySelf() {
		GoodsProduct item = new GoodsProduct();
		GoodsProduct i1 = this.getDefaultRecord(DEFAULT_STRING, DEFAULT_LONG, DEFAULT_INTEGER, DEFAULT_DOUBLE, DEFAULT_BOOLEAN);
		boolean equals = item.equals(i1);
		Assert.assertFalse(equals);
	}

	/**
	 * Test that it does not equal an object when both keys are null
	 */
	@Test
	public void equalsNullKeyBoth() {
		GoodsProduct item = new GoodsProduct();
		GoodsProduct i1 = new GoodsProduct();
		boolean equals = item.equals(i1);
		Assert.assertTrue(equals);
	}

	/**
	 * Check hashCode is consistent on the same object.
	 */
	@Test
	public void hashCodeSelf() {
		GoodsProduct item = this.getDefaultRecord(DEFAULT_STRING, DEFAULT_LONG, DEFAULT_INTEGER, DEFAULT_DOUBLE, DEFAULT_BOOLEAN);
		Assert.assertEquals(item.hashCode(), item.hashCode());
	}

	/**
	 * Check hashCode is the same on similar objects.
	 */
	@Test
	public void hashCodeSimilarObjects() {
		GoodsProduct i1 = this.getDefaultRecord(DEFAULT_STRING, DEFAULT_LONG, DEFAULT_INTEGER, DEFAULT_DOUBLE, DEFAULT_BOOLEAN);
		GoodsProduct i2 = this.getDefaultRecord(DEFAULT_STRING, DEFAULT_LONG, DEFAULT_INTEGER, DEFAULT_DOUBLE, DEFAULT_BOOLEAN);
		Assert.assertEquals(i1.hashCode(), i2.hashCode());
	}

	@Test
	public void hashCodeDifferentObjects() {
		GoodsProduct i1 = this.getDefaultRecord(DEFAULT_STRING, DEFAULT_LONG, DEFAULT_INTEGER, DEFAULT_DOUBLE, DEFAULT_BOOLEAN);
		GoodsProduct i2 = this.getDefaultRecord(OTHER_STRING, OTHER_LONG, OTHER_INTEGER, OTHER_DOUBLE, OTHER_BOOLEAN);
		Assert.assertNotEquals(i1.hashCode(), i2.hashCode());
	}

	/**
	 * Check hashCode is zero with a null key.
	 */
	@Test
	public void hashCodeNullKey() {
		GoodsProduct item = new GoodsProduct();
		Assert.assertEquals(0, item.hashCode());
	}

	/**
	 * Tests the toString method
	 */
	@Test
	public void toStringTest(){
		GoodsProduct item = getDefaultRecord(DEFAULT_STRING, DEFAULT_LONG, DEFAULT_INTEGER, DEFAULT_DOUBLE, DEFAULT_BOOLEAN);
		Assert.assertEquals(
				"GoodsProduct{" +
						"prodId=1, tagEffectiveDate=2017-08-16, shelfTagSizeCode='TEST', numberOfTags=1, maxNestCount='1', " +
						"stackableSwitch='true', retailLength='1.0', retailIncrLength='1.0', retailWidth='1.0', " +
						"retailIncrWidth='1.0', retailHeight='1.0', retailIncrHeight='1.0', retailWeight='1.0', " +
						"medicaidCode=TEST, pseTypeCode=TEST, gramsOfPSE='1', rxProductFlag='true', " +
						"rxProduct=RxProduct{" +
							"id=1, drugScheduleTypeCode=null, drugScheduleType=null, ndc=null" +
							"}, " +
						"codeDate='false', maxShelfLifeDays='null', inboundSpecificationDays='null', " +
						"warehouseReactionDays='null', guaranteeToStoreDays='null', sendCodeDate='false', " +
						"lastUpdateBy=TEST, lastUpdatedOn=2017-08-16, createdBy=TEST, createdOn=2017-08-16}",
				item.toString());
	}

	/**
	 * Creates a default record for desting
	 * @param string default string
	 * @param longs default long
	 * @param integer default integer
	 * @param doubles default double
	 * @param bool default boolean
	 * @return
	 */
	private GoodsProduct getDefaultRecord(String string, Long longs, Integer integer, Double doubles, Boolean bool){
		GoodsProduct record = new GoodsProduct();
		record.setProdId(longs);
		record.setShelfTagSizeCode(string);
		record.setNumberOfTags(integer);
		record.setMaxNestCount(integer);
		record.setStackableSwitch(bool);
		record.setRetailLength(doubles);
		record.setRetailIncrLength(doubles);
		record.setRetailHeight(doubles);
		record.setRetailIncrHeight(doubles);
		record.setRetailWidth(doubles);
		record.setRetailIncrWidth(doubles);
		record.setRetailWeight(doubles);
		record.setPseTypeCode(string);
		record.setGramsOfPSE(longs);
		record.setMedicaidCode(string);
		record.setRxProductFlag(bool);
		record.setTagEffectiveDate(LocalDate.now());
		record.setRxProduct(getDefaultRxProduct(longs));
		record.setCreatedBy(string);
		record.setCreatedOn(Date.valueOf(LocalDate.now()));
		record.setLastUpdateBy(string);
		record.setLastUpdatedOn(Date.valueOf(LocalDate.now()));
		return record;
	}

	/**
	 * generates default RxProduct for testing
	 * @param id id for entity
	 * @return
	 */
	private RxProduct getDefaultRxProduct(Long id){
		RxProduct rxProduct = new RxProduct();
		rxProduct.setId(id);
		return rxProduct;
	}
}