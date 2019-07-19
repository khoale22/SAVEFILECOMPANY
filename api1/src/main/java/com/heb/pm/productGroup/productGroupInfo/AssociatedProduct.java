/*
 * AssociatedProduct
 *
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
package com.heb.pm.productGroup.productGroupInfo;
import com.heb.pm.entity.ChoiceType;
import com.heb.pm.entity.CustomerProductChoice;
import com.heb.pm.entity.ProductGroupChoiceOption;

import javax.persistence.Transient;
import java.util.List;
import java.util.Map;

/**
 * object to represent data associated product on product group info screen.
 *
 * @author vn87351
 * @since 2.14.0
 */
public class AssociatedProduct {
	private List<ChoiceType> dataHeader;
	private List<Map<String,Object>> rows;
	private Boolean checkAssociated;
	private byte[] imagePrimary;
	private Long productPrimaryScanCodeId;
	private Long productId;
	private List<ProductGroupChoiceOption> productGroupChoiceOptions;
	private List<CustomerProductChoice> customerProductChoices;
	private Map<String,String> errorMessage;
	@Transient
	private String action;

	public Map<String, String> getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(Map<String, String> errorMessage) {
		this.errorMessage = errorMessage;
	}

	/**
	 * get data header on dynamic associated table
	 * @return list choice type
	 */
	public List<ChoiceType> getDataHeader() {
		return dataHeader;
	}

	/**
	 * set data to header on dynamic associated table
	 * @param dataHeader list choice type
	 */
	public void setDataHeader(List<ChoiceType> dataHeader) {
		this.dataHeader = dataHeader;
	}

	/**
	 * Get the list of rows on table.
	 *
	 * @return the list of rows on table.
	 */
	public List<Map<String, Object>> getRows() {
		return rows;
	}

	/**
	 * Set the list of table.
	 *
	 * @param rows the list of rows on table.
	 */
	public void setRows(List<Map<String, Object>> rows) {
		this.rows = rows;
	}

	/**
	 * get check associated
	 * @return
	 */
	public Boolean getCheckAssociated() {
		return checkAssociated;
	}

	/**
	 * check associated
	 * @param checkAssociated
	 */
	public void setCheckAssociated(Boolean checkAssociated) {
		this.checkAssociated = checkAssociated;
	}
	/**
	 * get list of choice Product exit
	 *
	 * @return list of customerProductChoices
	 */
	public List<CustomerProductChoice> getCustomerProductChoices() {
		return customerProductChoices;
	}

	/**
	 * set list choice Product exit
	 * @param customerProductChoices list
	 */
	public void setCustomerProductChoices(List<CustomerProductChoice> customerProductChoices) {
		this.customerProductChoices = customerProductChoices;
	}

	/**
	 * Get the list of productGroupChoiceOptions.
	 *
	 * @return the list of productGroupChoiceOptions.
	 */
	public List<ProductGroupChoiceOption> getProductGroupChoiceOptions() {
		return productGroupChoiceOptions;
	}

	/**
	 * Set  the list of productGroupChoiceOptions.
	 *
	 * @param productGroupChoiceOptions the list of productGroupChoiceOptions.
	 */
	public void	setProductGroupChoiceOptions(List<ProductGroupChoiceOption> productGroupChoiceOptions) {
		this.productGroupChoiceOptions= productGroupChoiceOptions;
	}

	@Override
	public String toString() {
		return "AssociatedProduct{" +
				"dataHeader='" + dataHeader + '\'' +
				", checkAssociated='" + checkAssociated + '\'' +
				'}';
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
	 * Returns the ProductPrimaryScanCodeId
	 *
	 * @return ProductPrimaryScanCodeId
	 */
	public Long getProductPrimaryScanCodeId() {
		return productPrimaryScanCodeId;
	}

	/**
	 * Sets the ProductPrimaryScanCodeId
	 *
	 * @param productPrimaryScanCodeId The ProductPrimaryScanCodeId
	 */
	public void setProductPrimaryScanCodeId(Long productPrimaryScanCodeId) {
		this.productPrimaryScanCodeId = productPrimaryScanCodeId;
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
	 * Get the action of ProductGroupChoiceOption.
	 *
	 * @return return the action add/update/delete ProductGroupChoiceOption.
	 */
	public String getAction() {
		return action;
	}

	/**
	 * Sets the action for ProductGroupChoiceOption.
	 *
	 * @param action the action of ProductGroupChoiceOption.
	 */
	public void setAction(String action) {
		this.action = action;
	}
}
