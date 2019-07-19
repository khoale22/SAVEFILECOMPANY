/*
 *  ECommerceViewAttributePriority
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.entity;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.io.IOException;
import java.util.List;

/**
 * Entity of eCommerce View that contains attribute priority. Contain information main content text view, and the
 * list of value for attribute base on priority.
 *
 * @author vn70516
 * @since 2.14.0
 */
public class ECommerceViewAttributePriority implements java.io.Serializable{

	private Long productId;
	private Long primaryScanCode;
	private Long attributeId;
	private String salesChannel;
	private MasterDataExtensionAttribute masterDataExtensionAttribute;
	private ProductAttributeOverwrite productAttributeOverwrite;
	private List<ECommerceViewAttributePriorityDetails> eCommerceViewAttributePriorityDetails;
    private ContentInterface mainContent;

	/**
	 * This class is used to holds the content with specification type.
	 */
	public static class ContentSpecification implements ContentInterface<List<AttributeValue>>{
		List<AttributeValue> content;
		public ContentSpecification(){}
		public ContentSpecification(List<AttributeValue> content){
			this.content = content;
		}
		/**
		 * Returns the list of specifications.
		 *
		 * @return the list of specifications.
		 */
		@Override
		public List<AttributeValue> getContent() {
			return content;
		}

		/**
		 * Sets the list of specificaitons.
		 *
		 * @param content the list of specificaitons.
		 */
		public void setContent(List<AttributeValue> content) {
			this.content = content;
		}
	}
	/**
	 * This class is used to holds the content with Dimension type.
	 */
	public static class ContentDimension implements ContentInterface<List<AttributeValue>>{
		List<AttributeValue> content;
		public ContentDimension(){}
		public ContentDimension(List<AttributeValue> content){
			this.content = content;
		}
		/**
		 * Returns the list of Dimensions.
		 *
		 * @return the list of Dimensions.
		 */
		@Override
		public List<AttributeValue> getContent() {
			return content;
		}
		/**
		 * Sets the list of Dimensions.
		 *
		 * @param content the list of Dimensions.
		 */
		public void setContent(List<AttributeValue> content) {
			this.content = content;
		}
	}
	/**
	 * This class is used to holds the content with string type.
	 */
	public static class ContentString implements ContentInterface<String>{
		String content;
		public ContentString(){
		}
		public ContentString(String content){
			this.content = content;
		}
		@Override
		public String getContent() {
			return content;
		}

		public void setContent(String content) {
			this.content = content;
		}
	}
	/**
	 * @return Gets the value of productId and returns productId
	 */
	public void setProductId(Long productId) {
		this.productId = productId;
	}

	/**
	 * Sets the productId
	 */
	public Long getProductId() {
		return productId;
	}

	/**
	 * @return Gets the value of primaryScanCode and returns primaryScanCode
	 */
	public void setPrimaryScanCode(Long primaryScanCode) {
		this.primaryScanCode = primaryScanCode;
	}

	/**
	 * Sets the primaryScanCode
	 */
	public Long getPrimaryScanCode() {
		return primaryScanCode;
	}

	/**
	 * @return Gets the value of attributeId and returns attributeId
	 */
	public void setAttributeId(Long attributeId) {
		this.attributeId = attributeId;
	}

	/**
	 * Sets the attributeId
	 */
	public Long getAttributeId() {
		return attributeId;
	}

	/**
	 * @return Gets the value of salesChannel and returns salesChannel
	 */
	public void setSalesChannel(String salesChannel) {
		this.salesChannel = salesChannel;
	}

	/**
	 * Sets the salesChannel
	 */
	public String getSalesChannel() {
		return salesChannel;
	}

	/**
	 * @return Gets the value of masterDataExtensionAttribute and returns masterDataExtensionAttribute
	 */
	public void setMasterDataExtensionAttribute(MasterDataExtensionAttribute masterDataExtensionAttribute) {
		this.masterDataExtensionAttribute = masterDataExtensionAttribute;
	}

	/**
	 * Sets the masterDataExtensionAttribute
	 */
	public MasterDataExtensionAttribute getMasterDataExtensionAttribute() {
		return masterDataExtensionAttribute;
	}

	/**
	 * @return Gets the value of productAttributeOverwrite and returns productAttributeOverwrite
	 */
	public void setProductAttributeOverwrite(ProductAttributeOverwrite productAttributeOverwrite) {
		this.productAttributeOverwrite = productAttributeOverwrite;
	}

	/**
	 * Sets the productAttributeOverwrite
	 */
	public ProductAttributeOverwrite getProductAttributeOverwrite() {
		return productAttributeOverwrite;
	}

	/**
	 * @return Gets the value of eCommerceViewAttributePriorityDetails and returns eCommerceViewAttributePriorityDetails
	 */
	public void seteCommerceViewAttributePriorityDetails(List<ECommerceViewAttributePriorityDetails> eCommerceViewAttributePriorityDetails) {
		this.eCommerceViewAttributePriorityDetails = eCommerceViewAttributePriorityDetails;
	}

	/**
	 * Sets the eCommerceViewAttributePriorityDetails
	 */
	public List<ECommerceViewAttributePriorityDetails> geteCommerceViewAttributePriorityDetails() {
		return eCommerceViewAttributePriorityDetails;
	}

	/**
	 * @return Gets the value of mainContent and returns mainContent
	 */
	public void setMainContent(ContentInterface mainContent) {
		this.mainContent = mainContent;
	}

	/**
	 * Sets the mainContent
	 */
	public ContentInterface getMainContent() {
		return mainContent;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof ECommerceViewAttributePriority)) return false;

		ECommerceViewAttributePriority that = (ECommerceViewAttributePriority) o;

		if (getProductId() != null ? !getProductId().equals(that.getProductId()) : that.getProductId() != null)
			return false;
		if (getPrimaryScanCode() != null ? !getPrimaryScanCode().equals(that.getPrimaryScanCode()) : that.getPrimaryScanCode() != null)
			return false;
		if (getAttributeId() != null ? !getAttributeId().equals(that.getAttributeId()) : that.getAttributeId() != null)
			return false;
		if (getSalesChannel() != null ? !getSalesChannel().equals(that.getSalesChannel()) : that.getSalesChannel() != null)
			return false;
		if (getProductAttributeOverwrite() != null ? !getProductAttributeOverwrite().equals(that.getProductAttributeOverwrite()) : that.getProductAttributeOverwrite() != null)
			return false;
		if (geteCommerceViewAttributePriorityDetails() != null ? !geteCommerceViewAttributePriorityDetails().equals(that.geteCommerceViewAttributePriorityDetails()) : that.geteCommerceViewAttributePriorityDetails() != null)
			return false;
		return getMainContent() != null ? getMainContent().equals(that.getMainContent()) : that.getMainContent() == null;
	}

	@Override
	public int hashCode() {
		int result = getProductId() != null ? getProductId().hashCode() : 0;
		result = 31 * result + (getPrimaryScanCode() != null ? getPrimaryScanCode().hashCode() : 0);
		result = 31 * result + (getAttributeId() != null ? getAttributeId().hashCode() : 0);
		result = 31 * result + (getSalesChannel() != null ? getSalesChannel().hashCode() : 0);
		result = 31 * result + (getProductAttributeOverwrite() != null ? getProductAttributeOverwrite().hashCode() : 0);
		result = 31 * result + (geteCommerceViewAttributePriorityDetails() != null ? geteCommerceViewAttributePriorityDetails().hashCode() : 0);
		result = 31 * result + (getMainContent() != null ? getMainContent().hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return "ECommerceViewAttributePriority{" +
				"productId=" + productId +
				", primaryScanCode=" + primaryScanCode +
				", attributeId=" + attributeId +
				", salesChannel='" + salesChannel + '\'' +
				", productAttributeOverwrite=" + productAttributeOverwrite +
				", eCommerceViewAttributePriorityDetails=" + eCommerceViewAttributePriorityDetails +
				", mainContent='" + mainContent + '\'' +
				'}';
	}
}