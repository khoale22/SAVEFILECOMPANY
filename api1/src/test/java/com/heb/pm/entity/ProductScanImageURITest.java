package com.heb.pm.entity;

import org.junit.Assert;
import org.junit.Test;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ProductScanImageURITest {

	private static final String DEFAULT_STRING= "TEST";
	private static final Integer DEFAULT_INTEGER = 1;
	private static final String OTHER_STRING="OTHER";
	private static final Integer OTHER_INTEGER = 2;
	private static final long DEFAULT_LONG=1;
	private static final long OTHER_LONG=2;
	private static final boolean DEFAULT_BOOLEAN = true;
	private static final boolean OTHER_BOOLEAN = false;
	private static final String DEFAULT_DIMENSIONS="1x\n1";

	/**
	 * Tests the getKey method
	 */
	@Test
	public void getKeyTest() {
		Assert.assertEquals(getDefaultKey(), getDefaultRecord().getKey());
	}

	/**
	 * Tests the setKey method
	 */
	@Test
	public void setKeyTest() {
		ProductScanImageURI imageURI = getDefaultRecord();
		ProductScanImageURIKey key = getDefaultKey();
		key.setId(OTHER_LONG);
		imageURI.setKey(key);
		Assert.assertEquals(key, imageURI.getKey());
	}

	/**
	 * Tests the isActiveOnline method
	 */
	@Test
	public void isActiveOnlineTest() {
		Assert.assertEquals(DEFAULT_BOOLEAN, getDefaultRecord().isActiveOnline());
	}

	/**
	 * Tests the setActiveOnline method
	 */
	@Test
	public void setActiveOnlineTest() {
		ProductScanImageURI imageURI = getDefaultRecord();
		imageURI.setActiveOnline(OTHER_BOOLEAN);
		Assert.assertEquals(OTHER_BOOLEAN, imageURI.isActiveOnline());
	}

	/**
	 * Tests the getAltTag method
	 */
	@Test
	public void getAltTagTest() {
		Assert.assertEquals(DEFAULT_STRING, getDefaultRecord().getAltTag());
	}

	/**
	 * Tests the setAltTag method
	 */
	@Test
	public void setAltTagTest() {
		ProductScanImageURI imageURI = getDefaultRecord();
		imageURI.setAltTag(OTHER_STRING);
		Assert.assertEquals(OTHER_STRING, imageURI.getAltTag());
	}

	/**
	 * Tests the getApplicationSource method
	 */
	@Test
	public void getApplicationSourceTest() {
		Assert.assertEquals(DEFAULT_STRING, getDefaultRecord().getApplicationSource());
	}

	/**
	 * Tests the setApplicationSource method
	 */
	@Test
	public void setApplicationSourceTest() {
		ProductScanImageURI imageURI = getDefaultRecord();
		imageURI.setApplicationSource(OTHER_STRING);
		Assert.assertEquals(OTHER_STRING, imageURI.getApplicationSource());
	}

	/**
	 * Tests the getImageCategoryCode method
	 */
	@Test
	public void getImageCategoryCodeTest() {
		Assert.assertEquals(DEFAULT_STRING, getDefaultRecord().getImageCategoryCode());
	}

	/**
	 * Tests the setImageCategoryCode method
	 */
	@Test
	public void setImageCategoryCodeTest() {
		ProductScanImageURI imageURI = getDefaultRecord();
		imageURI.setImageCategoryCode(OTHER_STRING);
		Assert.assertEquals(OTHER_STRING, imageURI.getImageCategoryCode());
	}

	/**
	 * Tests the getCreatedDate method
	 */
	@Test
	public void getCreatedDateTest() {
		Assert.assertTrue(getDefaultRecord().getCreatedDate() instanceof LocalDateTime);
	}

	/**
	 * Tests the setCreatedDate method
	 */
	@Test
	public void setCreatedDateTest() {
		LocalDateTime newDate = LocalDateTime.now();
		ProductScanImageURI imageURI = getDefaultRecord();
		imageURI.setCreatedDate(newDate);
		Assert.assertEquals(newDate, imageURI.getCreatedDate());
	}

	/**
	 * Tests the getLastModifiedOn method
	 */
	@Test
	public void getLastModifiedOnTest(){
		Assert.assertTrue(getDefaultRecord().getLastModifiedOn() instanceof LocalDateTime);
	}

	/**
	 * Tests the setLastModifiedOn method
	 */
	@Test
	public void setLastModifiedOnTest() {
		LocalDateTime newDate = LocalDateTime.now();
		ProductScanImageURI imageURI = getDefaultRecord();
		imageURI.setLastModifiedOn(newDate);
		Assert.assertEquals(newDate, imageURI.getLastModifiedOn());
	}

	/**
	 * Tests the getLastModifiedBy method
	 */
	@Test
	public void getLastModifiedByTest() {
		Assert.assertEquals(DEFAULT_STRING, getDefaultRecord().getLastModifiedBy());
	}

	/**
	 * Tests the setLastModifiedBy method
	 */
	@Test
	public void setLastModifiedByTest(){
		ProductScanImageURI imageURI = getDefaultRecord();
		imageURI.setLastModifiedBy(OTHER_STRING);
		Assert.assertEquals(OTHER_STRING, imageURI.getLastModifiedBy());
	}

	/**
	 * Tests the getxAxisResolution method
	 */
	@Test
	public void getxAxisResolutionTest() {
		Assert.assertEquals(DEFAULT_INTEGER, getDefaultRecord().getxAxisResolution());
	}

	/**
	 * Tests the setxAxisResolution method
	 */
	@Test
	public void setxAxisResolutionTest() {
		ProductScanImageURI imageURI = getDefaultRecord();
		imageURI.setxAxisResolution(OTHER_INTEGER);
		Assert.assertEquals(OTHER_INTEGER, imageURI.getxAxisResolution());
	}

	/**
	 * Tests the getyAxisResolution method
	 */
	@Test
	public void getyAxisResolutionTest() {
		Assert.assertEquals(DEFAULT_INTEGER, getDefaultRecord().getyAxisResolution());
	}

	/**
	 * Tests the setyAxisResolution method
	 */
	@Test
	public void setyAxisResolutionTest() {
		ProductScanImageURI imageURI = getDefaultRecord();
		imageURI.setyAxisResolution(OTHER_INTEGER);
		Assert.assertEquals(OTHER_INTEGER, imageURI.getyAxisResolution());
	}

	/**
	 * Tests the getImageAccepted method
	 */
	@Test
	public void getImageAcceptedTest() {
		Assert.assertEquals(DEFAULT_STRING, getDefaultRecord().getImageAccepted());
	}

	/**
	 * Tests the setImageAccepted method
	 */
	@Test
	public void setImageAcceptedTest() {
		ProductScanImageURI imageURI = getDefaultRecord();
		imageURI.setImageAccepted(OTHER_STRING);
		Assert.assertEquals(OTHER_STRING, imageURI.getImageAccepted());
	}

	/**
	 * Tests the getImageCategory method
	 */
	@Test
	public void getImageCategoryTest() {
		Assert.assertEquals(getDefaultCategory(), getDefaultRecord().getImageCategory());
	}

	/**
	 * Tests the setImageCategory method
	 */
	@Test
	public void setImageCategoryTest() {
		ImageCategory category = new ImageCategory();
		category.setId(OTHER_STRING);
		ProductScanImageURI imageURI = getDefaultRecord();
		imageURI.setImageCategory(category);
		Assert.assertEquals(category, imageURI.getImageCategory());
	}

	/**
	 * Tests the getImageStatusReason method
	 */
	@Test
	public void getImageStatusReasonTest() {
		Assert.assertEquals(DEFAULT_STRING, getDefaultRecord().getImageStatusReason());
	}

	/**
	 * Tests the  setImageStatusReason method
	 */
	@Test
	public void setImageStatusReasonTest() {
		ProductScanImageURI imageURI = getDefaultRecord();
		imageURI.setImageStatusReason(OTHER_STRING);
		Assert.assertEquals(OTHER_STRING, imageURI.getImageStatusReason());
	}

	/**
	 * Tests the getImageType method
	 */
	@Test
	public void getImageTypeTest() {
		Assert.assertEquals(getDefaultType(), getDefaultRecord().getImageType());
	}

	/**
	 * Tests the setImageType method
	 */
	@Test
	public void setImageTypeTest() {
		ImageType type = getDefaultType();
		type.setId(OTHER_STRING);
		ProductScanImageURI imageURI = getDefaultRecord();
		imageURI.setImageType(type);
		Assert.assertEquals(type, imageURI.getImageType());
	}

	/**
	 * Tests the getImageStatus method
	 */
	@Test
	public void getImageStatusTest() {
		Assert.assertEquals(getDefaultStatus(), getDefaultRecord().getImageStatus());
	}

	/**
	 * Tests the setImageStatus method
	 */
	@Test
	public void setImageStatusTest() {
		ImageStatus obj = getDefaultStatus();
		obj.setId(OTHER_STRING);
		ProductScanImageURI imageURI = getDefaultRecord();
		imageURI.setImageStatus(obj);
		Assert.assertEquals(obj, imageURI.getImageStatus());
	}

	/**
	 * Tests the getImagePriority method
	 */
	@Test
	public void getImagePriorityTest() {
		Assert.assertEquals(getDefaultPriority(), getDefaultRecord().getImagePriority());
	}

	/**
	 * Tests the setImagePriority method
	 */
	@Test
	public void setImagePriorityTest() {
		ImagePriority obj = getDefaultPriority();
		obj.setId(OTHER_STRING);
		ProductScanImageURI imageURI = getDefaultRecord();
		imageURI.setImagePriority(obj);
		Assert.assertEquals(obj, imageURI.getImagePriority());
	}

	/**
	 * Tests the getCreatedBy method
	 */
	@Test
	public void getCreatedByTest() {
		Assert.assertEquals(DEFAULT_STRING, getDefaultRecord().getCreatedBy());
	}

	/**
	 * Tests the setCreatedBy method
	 */
	@Test
	public void setCreatedByTest() {
		ProductScanImageURI imageURI = getDefaultRecord();
		imageURI.setCreatedBy(OTHER_STRING);
		Assert.assertEquals(OTHER_STRING, imageURI.getCreatedBy());
	}

	/**
	 * Tests the getImageStatusCode method
	 */
	@Test
	public void getImageStatusCodeTest() {
		Assert.assertEquals(DEFAULT_STRING, getDefaultRecord().getImageStatusCode());
	}

	/**
	 * Tests the setImageStatusCode method
	 */
	@Test
	public void setImageStatusCodeTest() {
		ProductScanImageURI imageURI = getDefaultRecord();
		imageURI.setImageStatusCode(OTHER_STRING);
		Assert.assertEquals(OTHER_STRING, imageURI.getImageStatusCode());
	}

	/**
	 * Tests the getImageURI method
	 */
	@Test
	public void getImageURITest() {
		Assert.assertEquals(DEFAULT_STRING, getDefaultRecord().getImageURI());
	}

	/**
	 * Tests the setImageURI method
	 */
	@Test
	public void setImageURITest() {
		ProductScanImageURI imageURI = getDefaultRecord();
		imageURI.setImageURI(OTHER_STRING);
		Assert.assertEquals(OTHER_STRING, imageURI.getImageURI());
	}

	/**
	 * Tests the getImageSource method
	 */
	@Test
	public void getImageSourceTest() {
		Assert.assertEquals(getDefaultSource(), getDefaultRecord().getImageSource());
	}

	/**
	 * Tests the setImageSource method
	 */
	@Test
	public void setImageSourceTest() {
		ImageSource obj = getDefaultSource();
		obj.setId(OTHER_STRING);
		ProductScanImageURI imageURI = getDefaultRecord();
		imageURI.setImageSource(obj);
		Assert.assertEquals(obj, imageURI.getImageSource());
	}

	/**
	 * Tests the getDimensions method
	 */
	@Test
	public void getDimensionsTest() {
		System.out.println(getDefaultRecord().getDimensions());
		Assert.assertEquals(DEFAULT_DIMENSIONS, getDefaultRecord().getDimensions());
	}

	/**
	 * Tests the getImageTypeCode method
	 */
	@Test
	public void getImageTypeCodeTest() {
		Assert.assertEquals(DEFAULT_STRING, getDefaultRecord().getImageTypeCode());
	}

	/**
	 * Tests the setImageTypeCode method
	 */
	@Test
	public void setImageTypeCodeTest() {
		ProductScanImageURI imageURI = getDefaultRecord();
		imageURI.setImageTypeCode(OTHER_STRING);
		Assert.assertEquals(OTHER_STRING, imageURI.getImageTypeCode());
	}

	/**
	 * Tests the getSourceSystemId method
	 */
	@Test
	public void getSourceSystemIdTest() {
		Assert.assertEquals(DEFAULT_INTEGER, getDefaultRecord().getSourceSystemId());
	}

	/**
	 * Tests the setSourceSystemId method
	 */
	@Test
	public void setSourceSystemIdTest() {
		ProductScanImageURI imageURI = getDefaultRecord();
		imageURI.setSourceSystemId(OTHER_INTEGER);
		Assert.assertEquals(OTHER_INTEGER, imageURI.getSourceSystemId());
	}

	/**
	 * Tests the getSourceSystem method
	 */
	@Test
	public void getSourceSystemTest() {
		Assert.assertEquals(getDefaultSourceSystem(), getDefaultRecord().getSourceSystem());
	}

	/**
	 * Tests the setSourceSystem method
	 */
	@Test
	public void setSourceSystemTest() {
		SourceSystem obj = getDefaultSourceSystem();
		obj.setId(OTHER_INTEGER);
		ProductScanImageURI imageURI = getDefaultRecord();
		imageURI.setSourceSystem(obj);
		Assert.assertEquals(obj, imageURI.getSourceSystem());
	}

	/**
	 * Tests the getImagePriorityCode method
	 */
	@Test
	public void getImagePriorityCodeTest() {
		Assert.assertEquals(DEFAULT_STRING, getDefaultRecord().getImagePriorityCode());
	}

	/**
	 * Tests the setImagePriorityCode method
	 */
	@Test
	public void setImagePriorityCodeTest() {
		ProductScanImageURI imageURI = getDefaultRecord();
		imageURI.setImagePriorityCode(OTHER_STRING);
		Assert.assertEquals(OTHER_STRING, imageURI.getImagePriorityCode());
	}

	/**
	 * Tests the getProductScanImageBannerList method
	 */
	@Test
	public void getProductScanImageBannerListTest() {
		Assert.assertEquals(getDefaultBannerList(), getDefaultRecord().getProductScanImageBannerList());
	}

	/**
	 * Tests the setProductScanImageBannerList method
	 */
	@Test
	public void setProductScanImageBannerListTest() {
		List<ProductScanImageBanner> obj = getDefaultBannerList();
		obj.get(0).getKey().setId(OTHER_LONG);
		ProductScanImageURI imageURI = getDefaultRecord();
		imageURI.setProductScanImageBannerList(obj);
		Assert.assertEquals(obj, imageURI.getProductScanImageBannerList());
	}

	/**
	 * Tests the getImageMetaDataList method
	 */
	@Test
	public void getImageMetaDataListTest() {
		Assert.assertEquals(getDefaultMetaDataList(), getDefaultRecord().getImageMetaDataList());
	}

	/**
	 * Test the setImageMetaDataList method
	 */
	@Test
	public void setImageMetaDataListTest() {
		List<ImageMetaData> obj = getDefaultMetaDataList();
		obj.get(0).getKey().setId(OTHER_LONG);
		ProductScanImageURI imageURI = getDefaultRecord();
		imageURI.setImageMetaDataList(obj);
		Assert.assertEquals(obj, imageURI.getImageMetaDataList());
	}

	/**
	 * Tests that an object equals itself.
	 */
	@Test
	public void equalsSameObject() {
		ProductScanImageURI test = this.getDefaultRecord();
		boolean equals = test.equals(test);
		Assert.assertTrue(equals);
	}

	/**
	 * Tests that an object equals an object with the same values.
	 */
	@Test
	public void equalsSimilarObject() {
		ProductScanImageURI test1 = this.getDefaultRecord();
		ProductScanImageURI test2 = this.getDefaultRecord();
		boolean equals = test1.equals(test2);
		Assert.assertTrue(equals);
	}

	/**
	 * Tests that it does not equal when passed null.
	 */
	@Test
	public void equalsNull() {
		ProductScanImageURI test = this.getDefaultRecord();
		boolean equals = test.equals(null);
		Assert.assertFalse(equals);
	}

	/**
	 * Test that it does not equal an object of a different type.
	 */
	@Test
	public void equalsDifferentType() {
		ProductScanImageURI test = this.getDefaultRecord();
		ProductScanImageURIKey otherKey = getDefaultKey();
		otherKey.setSequenceNumber(OTHER_INTEGER);
		test.setKey(otherKey);
		boolean equals = test.equals(getDefaultRecord());
		Assert.assertFalse(equals);
	}

	/**
	 * Test that it does not equal an object with a null key.
	 */
	@Test
	public void equalsNullKeyOther() {
		ProductScanImageURI test1 = this.getDefaultRecord();
		ProductScanImageURI test2 = new ProductScanImageURI();
		boolean equals = test1.equals(test2);
		Assert.assertFalse(equals);
	}

	/**
	 * Test that it does not equal an object when it's key is null.
	 */
	@Test
	public void equalsNullKeySelf() {
		ProductScanImageURI test1 = new ProductScanImageURI();
		ProductScanImageURI test2 = this.getDefaultRecord();
		boolean equals = test1.equals(test2);
		Assert.assertFalse(equals);
	}

	/**
	 * Test that it does not equal an object when both keys are null
	 */
	@Test
	public void equalsNullKeyBoth() {
		ProductScanImageURI test1 = new ProductScanImageURI();
		ProductScanImageURI test2 = new ProductScanImageURI();
		boolean equals = test1.equals(test2);
		Assert.assertTrue(equals);
	}

	/**
	 * Check hashCode is consistent on the same object.
	 */
	@Test
	public void hashCodeSelf() {
		ProductScanImageURI test = this.getDefaultRecord();
		Assert.assertEquals(test.hashCode(), test.hashCode());
	}

	/**
	 * Check hashCode is the same on similar objects.
	 */
	@Test
	public void hashCodeSimilarObjects() {
		ProductScanImageURI test1 = this.getDefaultRecord();
		ProductScanImageURI test2 = this.getDefaultRecord();
		Assert.assertEquals(test1.hashCode(), test2.hashCode());
	}

	/**
	 * Checks the hashCode of different objects
	 */
	@Test
	public void hashCodeDifferentObjects() {
		ProductScanImageURI test1 = this.getDefaultRecord();
		ProductScanImageURI test2 = this.getDefaultRecord();
		ProductScanImageURIKey otherKey = getDefaultKey();
		otherKey.setSequenceNumber(OTHER_INTEGER);
		test2.setKey(otherKey);
		Assert.assertNotEquals(test1.hashCode(), test2.hashCode());
	}

	/**
	 * Check hashCode is zero with a null key.
	 */
	@Test
	public void hashCodeNullKey() {
		ProductScanImageURI test = new ProductScanImageURI();
		Assert.assertEquals(0, test.hashCode());
	}

	/**
	 * Tests toString.
	 */
	@Test
	public void testToString() {
		ProductScanImageURI test = this.getDefaultRecord();
		Assert.assertEquals("ProductScanImageURI{" +
						"key=ProductScanImageURIKey{" +
							"id=1, " +
							"sequenceNumber=1" +
						"}, " +
						"activeOnline='true', " +
						"altTag='TEST', " +
						"imageCategory='TEST', " +
						"createdDate=" + test.getCreatedDate() + ", " +
						"lastModifiedOn="+ test.getLastModifiedOn()+ ", " +
						"lastModifiedBy='TEST', " +
						"xAxisResolution='1', " +
						"yAxisResolution='1', " +
						"imageAccepted='TEST', " +
						"imageStatusCode='TEST', " +
						"imageStatusReason='TEST', " +
						"imageStatus='ImageStatus{" +
							"id='TEST', " +
							"abbreviation='null', " +
							"description='null'" +
						"}', " +
						"imageTypeCode='TEST', " +
						"imageType='ImageType{" +
							"id='TEST', " +
							"imageFormat='null'" +
						"}', " +
						"imagePriorityCode='TEST', " +
						"imagePriority='ImagePriority{" +
							"id='TEST', " +
							"abbreviation='null', " +
							"description='null'" +
						"}', " +
						"imageCategoryCode='TEST', " +
						"imageCategory='ImageCategory{" +
							"id=TEST, " +
							"abbreviation='null', " +
							"description='null'" +
						"}', " +
						"applicationSource='TEST', " +
						"imageSource='ImageSource{" +
							"id='TEST', " +
							"abbreviation='null', " +
							"description='null', " +
							"isActive=false" +
						"}', " +
						"sourceSystemId='1', " +
						"sourceSystem='SourceSystem{" +
							"id=1, " +
							"description='TEST" +
						"}', " +
						"productScanImageBannerList='[ProductScanImageBanner{" +
								"key=ProductScanImageBannerKey{" +
									"id='1', " +
									"sequenceNumber='1', " +
									"salesChannelCode='TEST'" +
									"}" +
								"}" +
							"]', " +
						"imageMetaDataList='[ImportItem{" +
								"key=ImagePriority{" +
									"id='1', " +
									"sequenceNumber='1', " +
									"imageSubjectTypeCode='TEST'" +
									"}, " +
								"uriText=null" +
								"}" +
							"]'" +
						"}",
				test.toString());
	}

	/**
	 * Generates a default record for testing
	 * @return
	 */
	private ProductScanImageURI getDefaultRecord(){
		ProductScanImageURI imageURI = new ProductScanImageURI();
		imageURI.setKey(getDefaultKey());
		imageURI.setAltTag(DEFAULT_STRING);
		imageURI.setApplicationSource(DEFAULT_STRING);
		imageURI.setImageSource(getDefaultSource());
		imageURI.setImageCategoryCode(DEFAULT_STRING);
		imageURI.setImageCategory(getDefaultCategory());
		imageURI.setImageStatusCode(DEFAULT_STRING);
		imageURI.setImageStatus(getDefaultStatus());
		imageURI.setImageStatusReason(DEFAULT_STRING);
		imageURI.setImagePriorityCode(DEFAULT_STRING);
		imageURI.setImagePriority(getDefaultPriority());
		imageURI.setImageTypeCode(DEFAULT_STRING);
		imageURI.setImageType(getDefaultType());
		imageURI.setSourceSystemId(DEFAULT_INTEGER);
		imageURI.setSourceSystem(getDefaultSourceSystem());
		imageURI.setImageAccepted(DEFAULT_STRING);
		imageURI.setProductScanImageBannerList(getDefaultBannerList());
		imageURI.setImageURI(DEFAULT_STRING);
		imageURI.setImageMetaDataList(getDefaultMetaDataList());
		imageURI.setxAxisResolution(DEFAULT_INTEGER);
		imageURI.setyAxisResolution(DEFAULT_INTEGER);
		imageURI.setActiveOnline(DEFAULT_BOOLEAN);
		imageURI.setLastModifiedOn(LocalDateTime.now());
		imageURI.setLastModifiedBy(DEFAULT_STRING);
		imageURI.setCreatedDate(LocalDateTime.now());
		imageURI.setCreatedBy(DEFAULT_STRING);
		return imageURI;
	}

	/**
	 * Return a default key for testing purposes
	 * @return
	 */
	private ProductScanImageURIKey getDefaultKey(){
		ProductScanImageURIKey key = new ProductScanImageURIKey();
		key.setSequenceNumber(DEFAULT_LONG);
		key.setId(DEFAULT_LONG);
		return key;
	}

	/**
	 * Generates a default source system for testing
	 * @return
	 */
	private SourceSystem getDefaultSourceSystem(){
		SourceSystem sourceSystem = new SourceSystem();
		sourceSystem.setId(DEFAULT_LONG);
		sourceSystem.setDescription(DEFAULT_STRING);
		return sourceSystem;
	}

	/**
	 * Generates a default category for testing
	 * @return
	 */
	private ImageCategory getDefaultCategory(){
		ImageCategory category = new ImageCategory();
		category.setId(DEFAULT_STRING);
		return category;
	}

	/**
	 * Generates default source for testing
	 * @return
	 */
	private ImageSource getDefaultSource(){
		ImageSource source = new ImageSource();
		source.setId(DEFAULT_STRING);
		return source;
	}

	/**
	 * Generates default status for testing
	 * @return
	 */
	private ImageStatus getDefaultStatus(){
		ImageStatus status = new ImageStatus();
		status.setId(DEFAULT_STRING);
		return status;
	}

	/**
	 * Generates default priority for testing
	 * @return
	 */
	private ImagePriority getDefaultPriority(){
		ImagePriority priority = new ImagePriority();
		priority.setId(DEFAULT_STRING);
		return priority;
	}

	/**
	 * Generates default type for testing
	 * @return
	 */
	private ImageType getDefaultType(){
		ImageType type = new ImageType();
		type.setId(DEFAULT_STRING);
		return type;
	}

	/**
	 * Generates default banner list for testing
	 * @return
	 */
	private List<ProductScanImageBanner> getDefaultBannerList(){
		ArrayList<ProductScanImageBanner> list = new ArrayList<>();
		ProductScanImageBannerKey key = new ProductScanImageBannerKey();
		key.setSequenceNumber(DEFAULT_LONG);
		key.setId(DEFAULT_LONG);
		key.setSalesChannelCode(DEFAULT_STRING);
		ProductScanImageBanner banner = new ProductScanImageBanner();
		banner.setKey(key);
		list.add(banner);
		return list;
	}

	/**
	 * Generates default metadata list for testing
	 * @return
	 */
	private List<ImageMetaData> getDefaultMetaDataList(){
		ArrayList<ImageMetaData> list = new ArrayList<>();
		ImageMetaDataKey key = new ImageMetaDataKey();
		key.setId(DEFAULT_LONG);
		key.setSequenceNumber(DEFAULT_LONG);
		key.setImageSubjectTypeCode(DEFAULT_STRING);
		ImageMetaData data = new ImageMetaData();
		data.setKey(key);
		list.add(data);
		return list;
	}
}