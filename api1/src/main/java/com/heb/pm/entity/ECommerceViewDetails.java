/*
 *  ECommerceView
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * This represents a eCommerce View. A eCommerce View contain all information will be show in product> eCommerce
 * View screen
 *
 * @author vn70516
 * @since 2.14.0
 */
public class ECommerceViewDetails implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private static final String DATE_FORMAT = "yyyy-MM-dd";
	private Long productId;
	private Long scanCodeId;
	private boolean showOnSite;
	private boolean productOnline;
	private boolean productPublished;
	@JsonFormat(
			pattern=DATE_FORMAT,
			shape=JsonFormat.Shape.STRING)
	private LocalDate showOnSiteStartDate;
	@JsonFormat(
			pattern=DATE_FORMAT,
			shape=JsonFormat.Shape.STRING)
	private LocalDate showOnSiteEndDate;
	private LocalDateTime publishedDate;
	private String publishedBy;
	private String snipes;
	private String pdpTemplateId;
	private List<ProductScanImageBanner> imageAlternates;
	private byte[] imagePrimary;
	private String imagePrimaryFormat;
	private Long storeCount;
	private String customerHierarchyPrimaryPath;
	private String nutriPanelTypCd;
	private String salsChnlCd;
	private List<FulfillmentChannel> fulfillmentChannels;
	private List<ProductFullfilmentChanel> productFullfilmentChanels;
	private boolean subscriptionChanged;
	private boolean subscription;
	@JsonFormat(
			pattern=DATE_FORMAT,
			shape=JsonFormat.Shape.STRING)
	private LocalDate subscriptionStartDate;
	@JsonFormat(
			pattern=DATE_FORMAT,
			shape=JsonFormat.Shape.STRING)
	private LocalDate subscriptionEndDate;

	private ECommerceViewAttributePriorities brand;
	private ECommerceViewAttributePriorities displayName;
	private ECommerceViewAttributePriorities size;
	private ECommerceViewAttributePriorities pdpTemplate;
	private ECommerceViewAttributePriorities ingredients;
	private ECommerceViewAttributePriorities ingredientsToDelete;
	private ECommerceViewAttributePriorities ingredientsAttribute;
	private ECommerceViewAttributePriorities warnings;
	private ECommerceViewAttributePriorities directions;
	private ECommerceViewAttributePriorities romanceCopy;
	private ECommerceViewAttributePriorities tags;
	private ECommerceViewAttributePriorities nutrient;
	private ECommerceViewAttributePriorities allergen;
	private ECommerceViewAttributePriorities specification;
	private ECommerceViewAttributePriorities dimension;
	private ECommerceViewAttributePriorities subscriptionDate;
	private ECommerceViewAttributePriorities favorItemDescription;
	private List<ECommerceViewAttributePriorities> nutrientList;

	private List<Long> attributeListChange;
	private Long alertId;

	/**
	 * Returns the alert id.
	 *
	 * @return the alert id.
	 */
	public Long getAlertId() {
		return alertId;
	}
	/**
	 * Sets the alert id.
	 *
	 * @param alertId the alert id.
	 */
	public void setAlertId(Long alertId) {
		this.alertId = alertId;
	}
	/**
	 * Returns the product id.
	 *
	 * @return the product id.

	 */
	public Long getProductId() {
		return productId;
	}

	/**
	 * Sets the product id.
	 *
	 * @param productId the product id.
	 */
	public void setProductId(Long productId) {
		this.productId = productId;
	}

	/**
	 * Returns the scan code id.
	 * @return the scan code id.
	 */
	public Long getScanCodeId() {
		return scanCodeId;
	}

	/**
	 * Sets the scan code id.
	 *
	 * @param scanCodeId the scan code id.
	 */
	public void setScanCodeId(Long scanCodeId) {
		this.scanCodeId = scanCodeId;
	}

	/**
	 * @return Gets the value of showOnSite and returns showOnSite
	 */
	public void setShowOnSite(boolean showOnSite) {
		this.showOnSite = showOnSite;
	}

	/**
	 * Sets the showOnSite
	 */
	public boolean isShowOnSite() {
		return showOnSite;
	}

	/**
	 * Sets the productPublished
	 */
	public boolean isProductPublished() {
		return productPublished;
	}

	/**
	 * @return Gets the value of productPublished and returns productPublished
	 */
	public void setProductPublished(boolean productPublished) {
		this.productPublished = productPublished;
	}

	/**
	 * Sets the productOnline
	 */
	public boolean isProductOnline() {
		return productOnline;
	}

	/**
	 * @return Gets the value of productOnline and returns productOnline
	 */
	public void setProductOnline(boolean productOnline) {
		this.productOnline = productOnline;
	}


	/**
	 * @return Gets the value of showOnSiteStartDate and returns showOnSiteStartDate
	 */
	public void setShowOnSiteStartDate(LocalDate showOnSiteStartDate) {
		this.showOnSiteStartDate = showOnSiteStartDate;
	}

	/**
	 * Sets the showOnSiteStartDate
	 */
	public LocalDate getShowOnSiteStartDate() {
		return showOnSiteStartDate;
	}

	/**
	 * @return Gets the value of showOnSiteEndDate and returns showOnSiteEndDate
	 */
	public void setShowOnSiteEndDate(LocalDate showOnSiteEndDate) {
		this.showOnSiteEndDate = showOnSiteEndDate;
	}

	/**
	 * Sets the showOnSiteEndDate
	 */
	public LocalDate getShowOnSiteEndDate() {
		return showOnSiteEndDate;
	}

	/**
	 * @return Gets the value of publishedDate and returns publishedDate
	 */
	public void setPublishedDate(LocalDateTime publishedDate) {
		this.publishedDate = publishedDate;
	}

	/**
	 * Sets the publishedDate
	 */
	public LocalDateTime getPublishedDate() {
		return publishedDate;
	}

	/**
	 * @return Gets the value of publishedBy and returns publishedBy
	 */
	public void setPublishedBy(String publishedBy) {
		this.publishedBy = publishedBy;
	}

	/**
	 * Sets the publishedBy
	 */
	public String getPublishedBy() {
		return publishedBy;
	}

	/**
	 * @return Gets the value of snipes and returns snipes
	 */
	public void setSnipes(String snipes) {
		this.snipes = snipes;
	}

	/**
	 * Sets the snipes
	 */
	public String getSnipes() {
		return snipes;
	}

	/**
	 * @return Gets the value of pdpTemplateId and returns pdpTemplateId
	 */
	public void setPdpTemplateId(String pdpTemplateId) {
		this.pdpTemplateId = pdpTemplateId;
	}

	/**
	 * Sets the pdpTemplateId
	 */
	public String getPdpTemplateId() {
		return pdpTemplateId;
	}

	/**
	 * @return Gets the value of imagePrimary and returns imagePrimary
	 */
	public void setImagePrimary(byte[] imagePrimary) {
		this.imagePrimary = imagePrimary;
	}

	/**
	 * Sets the imagePrimary
	 */
	public byte[] getImagePrimary() {
		return imagePrimary;
	}

	/**
	 * Sets the imagePrimaryFormat
	 */
	public void setImagePrimaryFormat(String imagePrimaryFormat) {
		this.imagePrimaryFormat = imagePrimaryFormat;
	}

	/**
	 * @return Gets the value of imagePrimaryFormat and returns imagePrimaryFormat
	 */
	public String getImagePrimaryFormat() {
		return imagePrimaryFormat;
	}

	/**
	 * @return Gets the value of imageAlternate and returns imageAlternate
	 */
	public void setImageAlternates(List<ProductScanImageBanner> imageAlternates) {
		this.imageAlternates = imageAlternates;
	}

	/**
	 * Sets the imageAlternate
	 */
	public List<ProductScanImageBanner> getImageAlternates() {
		return imageAlternates;
	}

	/**
	 * @return Gets the value of storeCount and returns storeCount
	 */
	public void setStoreCount(Long storeCount) {
		this.storeCount = storeCount;
	}

	/**
	 * Sets the storeCount
	 */
	public Long getStoreCount() {
		return storeCount;
	}

	/**
	 * @return Gets the value of fulfillmentChannels and returns fulfillmentChannels
	 */
	public void setFulfillmentChannels(List<FulfillmentChannel> fulfillmentChannels) {
		this.fulfillmentChannels = fulfillmentChannels;
	}

	/**
	 * Sets the fulfillmentChannels
	 */
	public List<FulfillmentChannel> getFulfillmentChannels() {
		return fulfillmentChannels;
	}

	/**
	 * @return Gets the value of brand and returns brand
	 */
	public void setBrand(ECommerceViewAttributePriorities brand) {
		this.brand = brand;
	}

	/**
	 * Sets the brand
	 */
	public ECommerceViewAttributePriorities getBrand() {
		return brand;
	}

	/**
	 * @return Gets the value of displayName and returns displayName
	 */
	public void setDisplayName(ECommerceViewAttributePriorities displayName) {
		this.displayName = displayName;
	}

	/**
	 * Sets the displayName
	 */
	public ECommerceViewAttributePriorities getDisplayName() {
		return displayName;
	}

	/**
	 * @return Gets the value of size and returns size
	 */
	public void setSize(ECommerceViewAttributePriorities size) {
		this.size = size;
	}

	/**
	 * Sets the size
	 */
	public ECommerceViewAttributePriorities getSize() {
		return size;
	}

	/**
	 * @return Gets the value of customerHierarchyPrimaryPath and returns customerHierarchyPrimaryPath
	 */
	public void setCustomerHierarchyPrimaryPath(String customerHierarchyPrimaryPath) {
		this.customerHierarchyPrimaryPath = customerHierarchyPrimaryPath;
	}

	/**
	 * Sets the customerHierarchyPrimaryPath
	 */
	public String getCustomerHierarchyPrimaryPath() {
		return customerHierarchyPrimaryPath;
	}

	/**
	 * @return Gets the value of productFullfilmentChanels and returns productFullfilmentChanels
	 */
	public void setProductFullfilmentChanels(List<ProductFullfilmentChanel> productFullfilmentChanels) {
		this.productFullfilmentChanels = productFullfilmentChanels;
	}

	/**
	 * @return Gets the value of subscriptionChanged and returns subscriptionChanged
	 */
	public void setSubscriptionChanged(boolean subscriptionChanged) {
		this.subscriptionChanged = subscriptionChanged;
	}

	/**
	 * Sets the subscriptionChanged
	 */
	public boolean isSubscriptionChanged() {
		return subscriptionChanged;
	}

	/**
	 * Sets the productFullfilmentChanels
	 */
	public List<ProductFullfilmentChanel> getProductFullfilmentChanels() {
		return productFullfilmentChanels;
	}

	/**
	 * @return Gets the value of subscription and returns subscription
	 */
	public void setSubscription(boolean subscription) {
		this.subscription = subscription;
	}

	/**
	 * Sets the subscription
	 */
	public boolean isSubscription() {
		return subscription;
	}

	/**
	 * @return Gets the value of subscriptionStartDate and returns subscriptionStartDate
	 */
	public void setSubscriptionStartDate(LocalDate subscriptionStartDate) {
		this.subscriptionStartDate = subscriptionStartDate;
	}

	/**
	 * Sets the subscriptionStartDate
	 */
	public LocalDate getSubscriptionStartDate() {
		return subscriptionStartDate;
	}

	/**
	 * @return Gets the value of subscriptionEndDate and returns subscriptionEndDate
	 */
	public void setSubscriptionEndDate(LocalDate subscriptionEndDate) {
		this.subscriptionEndDate = subscriptionEndDate;
	}

	/**
	 * Sets the subscriptionEndDate
	 */
	public LocalDate getSubscriptionEndDate() {
		return subscriptionEndDate;
	}

	/**
	 * Return published date to string by format "MM/dd/yyyy HH:mm:ss a"
	 *
	 * @return String
	 */
	public String getPublishedDateString() {
		String publishedDateString = "";
		if (this.publishedDate != null) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm:ss a");
			publishedDateString = this.publishedDate.format(formatter);
		}
		return publishedDateString;
	}

	public ECommerceViewAttributePriorities getPdpTemplate() {
		return pdpTemplate;
	}

	public void setPdpTemplate(ECommerceViewAttributePriorities pdpTemplate) {
		this.pdpTemplate = pdpTemplate;
	}

	public ECommerceViewAttributePriorities getIngredients() {
		return ingredients;
	}

	public void setIngredients(ECommerceViewAttributePriorities ingredients) {
		this.ingredients = ingredients;
	}

	public ECommerceViewAttributePriorities getIngredientsToDelete() { return ingredientsToDelete; }

	public void setIngredientsToDelete(ECommerceViewAttributePriorities ingredientsToDelete) { this.ingredientsToDelete = ingredientsToDelete; }

	public ECommerceViewAttributePriorities getIngredientsAttribute() { return ingredientsAttribute; }

	public void setIngredientsAttribute(ECommerceViewAttributePriorities ingredientsAttribute) { this.ingredientsAttribute = ingredientsAttribute; }

	public ECommerceViewAttributePriorities getWarnings() {
		return warnings;
	}

	public void setWarnings(ECommerceViewAttributePriorities warnings) {
		this.warnings = warnings;
	}

	public ECommerceViewAttributePriorities getDirections() {
		return directions;
	}

	public void setDirections(ECommerceViewAttributePriorities directions) {
		this.directions = directions;
	}

	public ECommerceViewAttributePriorities getRomanceCopy() {
		return romanceCopy;
	}

	public void setRomanceCopy(ECommerceViewAttributePriorities romanceCopy) {
		this.romanceCopy = romanceCopy;
	}

	public ECommerceViewAttributePriorities getTags() {
		return tags;
	}

	public void setTags(ECommerceViewAttributePriorities tags) {
		this.tags = tags;
	}

	public ECommerceViewAttributePriorities getAllergen() {
		return allergen;
	}

	public void setAllergen(ECommerceViewAttributePriorities allergen) {
		this.allergen = allergen;
	}

	public ECommerceViewAttributePriorities getSpecification() {
		return specification;
	}

	public void setSpecification(ECommerceViewAttributePriorities specification) {
		this.specification = specification;
	}

	public ECommerceViewAttributePriorities getDimension() {
		return dimension;
	}

	public void setDimension(ECommerceViewAttributePriorities dimension) {
		this.dimension = dimension;
	}

	public String getNutriPanelTypCd() {
		return nutriPanelTypCd;
	}

	public void setNutriPanelTypCd(String nutriPanelTypCd) {
		this.nutriPanelTypCd = nutriPanelTypCd;
	}

	public ECommerceViewAttributePriorities getSubscriptionDate() {
		return subscriptionDate;
	}

	public void setSubscriptionDate(ECommerceViewAttributePriorities subscriptionDate) {
		this.subscriptionDate = subscriptionDate;
	}

	/**
	 * Return sales chanel code.
	 *
	 * @return the salsChnlCd
	 */
	public String getSalsChnlCd() {
		return salsChnlCd;
	}

	/**
	 * Sets the sales chanel code.
	 *
	 * @param salsChnlCd the salsChnlCd to set
	 */
	public void setSalsChnlCd(String salsChnlCd) {
		this.salsChnlCd = salsChnlCd;
	}

	/**
	 * Returns the favor item description.
	 *
	 * @return the favor item description.
	 */
	public ECommerceViewAttributePriorities getFavorItemDescription() {
		return favorItemDescription;
	}

	/**
	 *  Sets the favor item description.
	 *
	 * @param favorItemDescription the favor item  description.
	 */
	public void setFavorItemDescription(ECommerceViewAttributePriorities favorItemDescription) {
		this.favorItemDescription = favorItemDescription;
	}
	/**
	 * @return the nutrient
	 */
	public ECommerceViewAttributePriorities getNutrient() {
		return nutrient;
	}

	/**
	 * @param nutrient the nutrient to set
	 */
	public void setNutrient(ECommerceViewAttributePriorities nutrient) {
		this.nutrient = nutrient;
	}

	/**
	 * @return the nutrientList
	 */
	public List<ECommerceViewAttributePriorities> getNutrientList() {
		return nutrientList;
	}

	/**
	 * @param nutrientList the nutrientList to set
	 */
	public void setNutrientList(List<ECommerceViewAttributePriorities> nutrientList) {
		this.nutrientList = nutrientList;
	}

	/**
	 * @return Gets the value of attributeListChange and returns attributeListChange
	 */
	public void setAttributeListChange(List<Long> attributeListChange) {
		this.attributeListChange = attributeListChange;
	}

	/**
	 * Sets the attributeListChange
	 */
	public List<Long> getAttributeListChange() {
		return attributeListChange;
	}
}