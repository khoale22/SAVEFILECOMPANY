/*
 *  NutritionFact
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.entity;

import java.time.LocalDate;
import java.util.List;

/**
 * Entity of nutrition fact.
 *
 * @author vn73545
 * @since 2.15.0
 */
public class NutritionFact implements java.io.Serializable{

	private static final long serialVersionUID = 1L;
	private Long sourceSystemId;
    private String sourceSystemDescription;
	private ScaleUpc scaleUpc;
	private boolean isSourceSystemDefault;
	private List<ProductNutrient> productNutrients;
	private List<ProductPkVariation> productPkVariations;
	private LocalDate createdDate;
	private List<CandidateNutrient> candidateNutrients;
	private List<CandidateProductPkVariation> candidateProductPkVariations;
	private String ingredients;
    private String allergens;
    
	/**
	 * Get the sourceSystemId.
	 * 
	 * @return the sourceSystemId
	 */
	public Long getSourceSystemId() {
		return sourceSystemId;
	}

	/**
	 * Set the sourceSystemId.
	 * 
	 * @param sourceSystemId the sourceSystemId to set
	 */
	public void setSourceSystemId(Long sourceSystemId) {
		this.sourceSystemId = sourceSystemId;
	}

	/**
	 * Get the sourceSystemDescription.
	 * 
	 * @return the sourceSystemDescription
	 */
	public String getSourceSystemDescription() {
		return sourceSystemDescription;
	}

	/**
	 * Set the sourceSystemDescription.
	 * 
	 * @param sourceSystemDescription the sourceSystemDescription to set
	 */
	public void setSourceSystemDescription(String sourceSystemDescription) {
		this.sourceSystemDescription = sourceSystemDescription;
	}

	/**
	 * Get the scaleUpc.
	 * 
	 * @return the scaleUpc
	 */
	public ScaleUpc getScaleUpc() {
		return scaleUpc;
	}

	/**
	 * Set the scaleUpc.
	 * 
	 * @param scaleUpc the scaleUpc to set
	 */
	public void setScaleUpc(ScaleUpc scaleUpc) {
		this.scaleUpc = scaleUpc;
	}

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
		this.isSourceSystemDefault = sourceSystemDefault;
	}
	
	/**
	 * Get the productNutrients.
	 * 
	 * @return the productNutrients
	 */
	public List<ProductNutrient> getProductNutrients() {
		return productNutrients;
	}
	
	/**
	 * Set the productNutrients.
	 * 
	 * @param productNutrients the productNutrients to set
	 */
	public void setProductNutrients(List<ProductNutrient> productNutrients) {
		this.productNutrients = productNutrients;
	}
	
	/**
	 * Get the productPkVariations.
	 * 
	 * @return the productPkVariations
	 */
	public List<ProductPkVariation> getProductPkVariations() {
		return productPkVariations;
	}
	
	/**
	 * Set the productPkVariations.
	 * 
	 * @param productPkVariations the productPkVariations to set
	 */
	public void setProductPkVariations(List<ProductPkVariation> productPkVariations) {
		this.productPkVariations = productPkVariations;
	}
	
	/**
	 * Get the createdDate.
	 * 
	 * @return the createdDate
	 */
	public LocalDate getCreatedDate() {
		return createdDate;
	}
	
	/**
	 * Set the createdDate.
	 * 
	 * @param createdDate the createdDate to set
	 */
	public void setCreatedDate(LocalDate createdDate) {
		this.createdDate = createdDate;
	}
	
	/**
	 * Get the candidateNutrients.
	 * 
	 * @return the candidateNutrients
	 */
	public List<CandidateNutrient> getCandidateNutrients() {
		return candidateNutrients;
	}
	
	/**
	 * Set the candidateNutrients.
	 * 
	 * @param candidateNutrients the candidateNutrients to set
	 */
	public void setCandidateNutrients(List<CandidateNutrient> candidateNutrients) {
		this.candidateNutrients = candidateNutrients;
	}
	
	/**
	 * Get the candidateProductPkVariations.
	 * 
	 * @return the candidateProductPkVariations
	 */
	public List<CandidateProductPkVariation> getCandidateProductPkVariations() {
		return candidateProductPkVariations;
	}
	
	/**
	 * Set the candidateProductPkVariations.
	 * 
	 * @param candidateProductPkVariations the candidateProductPkVariations to set
	 */
	public void setCandidateProductPkVariations(List<CandidateProductPkVariation> candidateProductPkVariations) {
		this.candidateProductPkVariations = candidateProductPkVariations;
	}
	
	/**
	 * Get the ingredients.
	 * 
	 * @return the ingredients
	 */
	public String getIngredients() {
		return ingredients;
	}
	
	/**
	 * Set the ingredients.
	 * 
	 * @param ingredients the ingredients to set
	 */
	public void setIngredients(String ingredients) {
		this.ingredients = ingredients;
	}
	
	/**
	 * Get the allergens.
	 * 
	 * @return the allergens
	 */
	public String getAllergens() {
		return allergens;
	}
	
	/**
	 * Set the allergens.
	 * 
	 * @param allergens the allergens to set
	 */
	public void setAllergens(String allergens) {
		this.allergens = allergens;
	}
}
