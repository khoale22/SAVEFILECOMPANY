/*
 *  ECommerceViewAttributePriorities
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.entity;

import java.time.LocalDate;
import java.util.List;

/**
 * Entity of eCommerce View that contains attribute priorities.
 *
 * @author vn70516
 * @since 2.14.0
 */
public class ECommerceViewAttributePriorities implements java.io.Serializable{

    private String content;
    private String extContent;
    //Save data of HTML tab of Brand, Display Name and Romance Copy.
    private String htmlContent;
    private Long sourceSystemId;
    private String sourceSystemDescription;
	private Long physicalAttributeId;
	private Long logicAttributeId;
	private boolean isDifferentWithDefaultValue;
	private LocalDate lstUpdtTs;
	private String relationshipGroupTypeCode;
	private List<AttributeValue> dimensions;
	private List<AttributeValue> specifications;
	private List<ProductNutrient> productNutrients;
	private List<ProductPkVariation> productPkVariations;
	private ScaleUpc scaleUpc;
	private LocalDate postedDate;
	private LocalDate createdDate;
	private boolean isSubscriptionProductSw;
	private String startDate;
	private String endDate;
	private String actionCode;
	private String sourceSystemSelected;
	private boolean isSourceSystemDefault;
	private boolean isSelectedSourceSystem;
	private boolean isMandatory;

	/**
	 * Use for edit.
	 */
	ECommerceViewAttributePriorities productMaintenance;

	/**
	 * @return the content
	 */
	public String getContent() {
		return content;
	}

	/**
	 * @param content the content to set
	 */
	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * @return the extContent
	 */
	public String getExtContent() {
		return extContent;
	}

	/**
	 * @param extContent the extContent to set
	 */
	public void setExtContent(String extContent) {
		this.extContent = extContent;
	}

	/**
	 * @return the htmlContent
	 */
	public String getHtmlContent() {
		return htmlContent;
	}

	/**
	 * @param htmlContent the htmlContent to set
	 */
	public void setHtmlContent(String htmlContent) {
		this.htmlContent = htmlContent;
	}

	/**
	 * @return the sourceSystemId
	 */
	public Long getSourceSystemId() {
		return sourceSystemId;
	}

	/**
	 * @param sourceSystemId the sourceSystemId to set
	 */
	public void setSourceSystemId(Long sourceSystemId) {
		this.sourceSystemId = sourceSystemId;
	}
	/**
	 * Get the PhysicalAttributeId.
	 *
	 * @return the PhysicalAttributeId.
	 */
	public Long getPhysicalAttributeId() {
		return physicalAttributeId;
	}

	/**
	 * Sets the PhysicalAttributeId.
	 *
	 * @param physicalAttributeId the PhysicalAttributeId.
	 */
	public void setPhysicalAttributeId(Long physicalAttributeId) {
		this.physicalAttributeId = physicalAttributeId;
	}

	/**
	 * @return Gets the value of differentWithDefaultValue and returns differentWithDefaultValue
	 */
	public void setDifferentWithDefaultValue(boolean differentWithDefaultValue) { this.isDifferentWithDefaultValue = differentWithDefaultValue; }

	/**
	 * Sets the differentWithDefaultValue
	 */
	public boolean isDifferentWithDefaultValue() {
		return isDifferentWithDefaultValue;
	}

	/**
	 * Sets the lstUpdtTs
	 */
	public LocalDate getLstUpdtTs() {
		return lstUpdtTs;
	}

	/**
	 * @return Gets the value of lstUpdtTs and returns lstUpdtTs
	 */
	public void setLstUpdtTs(LocalDate lstUpdtTs) {
		this.lstUpdtTs = lstUpdtTs;
	}
	/**
	 * Returns the productMaintenance (4) for edit.
	 *
	 * @return the ECommerceViewAttributePriorities object.
	 */
	public ECommerceViewAttributePriorities getProductMaintenance() {
		return productMaintenance;
	}

	/**
	 * Holds the productMaintenance (4) for edit.
	 *
	 * @param productMaintenance the productMaintenance (4) for edit.
	 */
	public void setProductMaintenance(ECommerceViewAttributePriorities productMaintenance) { this.productMaintenance = productMaintenance; }
	/**
	 * @return the dimensions
	 */
	public List<AttributeValue> getDimensions() {
		return dimensions;
	}

	/**
	 * @param dimensions the dimensions to set
	 */
	public void setDimensions(List<AttributeValue> dimensions) {
		this.dimensions = dimensions;
	}

	/**
	 * @return the specifications
	 */
	public List<AttributeValue> getSpecifications() {
		return specifications;
	}

	/**
	 * @param specifications the specifications to set
	 */
	public void setSpecifications(List<AttributeValue> specifications) {
		this.specifications = specifications;
	}

	/**
	 * @return the productNutrients
	 */
	public List<ProductNutrient> getProductNutrients() {
		return productNutrients;
	}

	/**
	 * @param productNutrients the productNutrients to set
	 */
	public void setProductNutrients(List<ProductNutrient> productNutrients) {
		this.productNutrients = productNutrients;
	}

	/**
	 * @return the productPkVariations
	 */
	public List<ProductPkVariation> getProductPkVariations() {
		return productPkVariations;
	}

	/**
	 * @param productPkVariations the productPkVariations to set
	 */
	public void setProductPkVariations(List<ProductPkVariation> productPkVariations) { this.productPkVariations = productPkVariations; }

	/**
	 * @return the scaleUpc
	 */
	public ScaleUpc getScaleUpc() {
		return scaleUpc;
	}

	/**
	 * @param scaleUpc the scaleUpc to set
	 */
	public void setScaleUpc(ScaleUpc scaleUpc) {
		this.scaleUpc = scaleUpc;
	}

	/**
	 * @return the sourceSystemDescription
	 */
	public String getSourceSystemDescription() {
		return sourceSystemDescription;
	}

	/**
	 * @param sourceSystemDescription the sourceSystemDescription to set
	 */
	public void setSourceSystemDescription(String sourceSystemDescription) { this.sourceSystemDescription = sourceSystemDescription; }

	/**
	 * @return the postedDate
	 */
	public LocalDate getPostedDate() {
		return postedDate;
	}

	/**
	 * @param postedDate the postedDate to set
	 */
	public void setPostedDate(LocalDate postedDate) {
		this.postedDate = postedDate;
	}

	/**
	 * @return the createdDate
	 */
	public LocalDate getCreatedDate() {
		return createdDate;
	}

	/**
	 * @param createdDate the createdDate to set
	 */
	public void setCreatedDate(LocalDate createdDate) {
		this.createdDate = createdDate;
	}

	/**
	 * Returns the logic attribute id.
	 *
	 * @return the logic attribute id
	 */
	public Long getLogicAttributeId() {
		return logicAttributeId;
	}

	/**
	 * Sets the logic attribute id.
	 *
	 * @param logicAttributeId the logic attribute id.
	 */
	public void setLogicAttributeId(Long logicAttributeId) {
		this.logicAttributeId = logicAttributeId;
	}
	/**
	 * @return Gets the value of relationshipGroupTypeCode and returns relationshipGroupTypeCode
	 */
	public void setRelationshipGroupTypeCode(String relationshipGroupTypeCode) { this.relationshipGroupTypeCode = relationshipGroupTypeCode; }

	/**
	 * Sets the relationshipGroupTypeCode
	 */
	public String getRelationshipGroupTypeCode() {
		return relationshipGroupTypeCode;
	}

	/**
	 * Checks if is SubscriptionProductSw.
	 * @return true, if is sub flag
	 */
	public boolean isSubscriptionProductSw() {
		return this.isSubscriptionProductSw;
	}

	/**
	 * Sets the SubscriptionProductSw.
	 * @param subFlag
	 *            the new sub flag
	 */
	public void setSubscriptionProductSw(boolean subFlag) {
		this.isSubscriptionProductSw = subFlag;
	}

	/**
	 * Gets the start date.
	 * @return the start date
	 */
	public String getStartDate() {
		return this.startDate;
	}

	/**
	 * Sets the start date.
	 * @param startDate
	 *            the new start date
	 */
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	/**
	 * Gets the end date.
	 * @return the end date
	 */
	public String getEndDate() {
		return this.endDate;
	}

	/**
	 * Sets the end date.
	 * @param endDate
	 *            the new end date
	 */
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	/**
	 * Returns the action code.
	 *
	 * @return the action code.
	 */
	public String getActionCode() {
		return actionCode;
	}

	/**
	 * Setsthe action code.
	 *
	 * @param actionCode the action code.
	 */
	public void setActionCode(String actionCode) {
		this.actionCode = actionCode;
	}

	/**
	 * Returns the sourceSystemSelected.
	 *
	 * @return the sourceSystemSelected.
	 */
	public String getSourceSystemSelected() {
		return sourceSystemSelected;
	}

	/**
	 * Sets the sourceSystemSelected.
	 *
	 * @param sourceSystemSelected the sourceSystemSelected to set.
	 */
	public void setSourceSystemSelected(String sourceSystemSelected) { this.sourceSystemSelected = sourceSystemSelected; }

	/**
	 * Returns the isSourceSystemDefault.
	 *
	 * @return the isSourceSystemDefault.
	 */
	public boolean isSourceSystemDefault() {
		return isSourceSystemDefault;
	}

	/**
	 * Sets the isSourceSystemDefault.
	 *
	 * @param sourceSystemDefault the isSourceSystemDefault to set.
	 */
	public void setSourceSystemDefault(boolean sourceSystemDefault) {
		isSourceSystemDefault = sourceSystemDefault;
	}

	/**
	 * @return the isSelectedSourceSystem
	 */
	public boolean isSelectedSourceSystem() {
		return isSelectedSourceSystem;
	}

	/**
	 * @param isSelectedSourceSystem the isSelectedSourceSystem to set
	 */
	public void setSelectedSourceSystem(boolean isSelectedSourceSystem) { this.isSelectedSourceSystem = isSelectedSourceSystem; }

	/**
	 * @return the isMandatory
	 */
	public boolean isMandatory() {
		return isMandatory;
	}

	/**
	 * @param mandatory the isMandatory to set
	 */
	public void setMandatory(boolean mandatory) { this.isMandatory = mandatory; }

}