/*
 *  ECommerceViewAttributePriorityDetails
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.entity;

import java.time.LocalDate;

/**
 * Entity of eCommerce View that contains attribute priority detail. Contain information content text, or the list of
 * content text basing on each type attribute.
 *
 * @author vn70516
 * @since 2.14.0
 */
public class ECommerceViewAttributePriorityDetails {


	private TargetSystemAttributePriority targetSystemAttributePriority;
	private boolean isSourceEditable;
	private boolean isSourceDefault;
    private ContentInterface content;
    //Save data of HTML tab of Brand, Display Name and Romance Copy.
    private ContentInterface htmlContent;
    private boolean isSelected;
	private LocalDate postedDate;
	private LocalDate createdDate;
	private String sourceDescription;
	/**
	 * @return Gets the value of targetSystemAttributePriority and returns targetSystemAttributePriority
	 */
	public void setTargetSystemAttributePriority(TargetSystemAttributePriority targetSystemAttributePriority) {
		this.targetSystemAttributePriority = targetSystemAttributePriority;
	}

	/**
	 * Sets the targetSystemAttributePriority
	 */
	public TargetSystemAttributePriority getTargetSystemAttributePriority() {
		return targetSystemAttributePriority;
	}

	/**
	 * @return Gets the value of isSourceEditable and returns isSourceEditable
	 */
	public void setSourceEditable(boolean sourceEditable) {
		isSourceEditable = sourceEditable;
	}

	/**
	 * Sets the isSourceEditable
	 */
	public boolean isSourceEditable() {
		return isSourceEditable;
	}

	/**
	 * @return Gets the value of isSourceDefault and returns isSourceDefault
	 */
	public void setSourceDefault(boolean sourceDefault) {
		isSourceDefault = sourceDefault;
	}

	/**
	 * Sets the isSourceDefault
	 */
	public boolean isSourceDefault() {
		return isSourceDefault;
	}

	/**
	 * @return Gets the value of content and returns content
	 */
	public <T> void setContent(ContentInterface<T> content) {
		this.content = content;
	}

	/**
	 * Sets the content
	 */
	public <T> ContentInterface<T> getContent() {
		return content;
	}

	/**
	 * @return Gets the value of isSelected and returns isSelected
	 */
	public void setSelected(boolean selected) {
		isSelected = selected;
	}

	/**
	 * Sets the isSelected
	 */
	public boolean isSelected() {
		return isSelected;
	}

	/**
	 * @return Gets the value of postedDate and returns postedDate
	 */
	public void setPostedDate(LocalDate postedDate) {
		this.postedDate = postedDate;
	}

	/**
	 * Sets the postedDate
	 */
	public LocalDate getPostedDate() {
		return postedDate;
	}

	/**
	 * @return Gets the value of createdDate and returns createdDate
	 */
	public void setCreatedDate(LocalDate createdDate) {
		this.createdDate = createdDate;
	}

	/**
	 * Sets the createdDate
	 */
	public LocalDate getCreatedDate() {
		return createdDate;
	}

	/**
	 * @return Gets the value of sourceDescription and returns sourceDescription
	 */
	public void setSourceDescription(String sourceDescription) {
		this.sourceDescription = sourceDescription;
	}

	/**
	 * Sets the sourceDescription
	 */
	public String getSourceDescription() {
		return sourceDescription;
	}

	/**
	 * @return Gets the value of htmlContent and returns htmlContent
	 */
	public <T> void setHtmlContent(ContentInterface<T> htmlContent) {
		this.htmlContent = htmlContent;
	}

	/**
	 * Sets the htmlContent
	 */
	public <T> ContentInterface<T> getHtmlContent() {
		return htmlContent;
	}
}