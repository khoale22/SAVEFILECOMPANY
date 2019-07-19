package com.heb.pm.entity;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Represents an effective vertex tax category to group effective dated maintenance records into one record.
 *
 * @author m314029
 * @since 2.14.0
 */
public class EffectiveVertexTaxCategory implements Serializable {

	private static final long serialVersionUID = -7198657975086665944L;

	private Boolean retailTaxable;

	private String vertexTaxCategoryCode;

	private LocalDate effectiveDate;

	private VertexTaxCategory vertexTaxCategory;

	private Long productId;

	private Long retailTaxableSequenceNumber;

	private Long vertexTaxCategorySequenceNumber;
	private String actionCode;
	/**
	 * Gets retail taxable sequence number.
	 *
	 * @return the retail taxable sequence number
	 */
	public Long getRetailTaxableSequenceNumber() {
		return retailTaxableSequenceNumber;
	}

	/**
	 * Sets retail taxable sequence number.
	 *
	 * @param retailTaxableSequenceNumber the retail taxable sequence number
	 */
	public void setRetailTaxableSequenceNumber(Long retailTaxableSequenceNumber) {
		this.retailTaxableSequenceNumber = retailTaxableSequenceNumber;
	}

	/**
	 * Gets vertex tax category sequence number.
	 *
	 * @return the vertex tax category sequence number
	 */
	public Long getVertexTaxCategorySequenceNumber() {
		return vertexTaxCategorySequenceNumber;
	}

	/**
	 * Sets vertex tax category sequence number.
	 *
	 * @param vertexTaxCategorySequenceNumber the vertex tax category sequence number
	 */
	public void setVertexTaxCategorySequenceNumber(Long vertexTaxCategorySequenceNumber) {
		this.vertexTaxCategorySequenceNumber = vertexTaxCategorySequenceNumber;
	}

	/**
	 * Gets product id.
	 *
	 * @return the product id
	 */
	public Long getProductId() {
		return productId;
	}

	/**
	 * Sets product id.
	 *
	 * @param productId the product id
	 */
	public void setProductId(Long productId) {
		this.productId = productId;
	}

	/**
	 * Gets retail taxable.
	 *
	 * @return the retail taxable
	 */
	public Boolean getRetailTaxable() {
		return retailTaxable;
	}

	/**
	 * Sets retail taxable.
	 *
	 * @param retailTaxable the retail taxable
	 */
	public void setRetailTaxable(Boolean retailTaxable) {
		this.retailTaxable = retailTaxable;
	}

	/**
	 * Gets vertex tax category code.
	 *
	 * @return the vertex tax category code
	 */
	public String getVertexTaxCategoryCode() {
		return vertexTaxCategoryCode;
	}

	/**
	 * Sets vertex tax category code.
	 *
	 * @param vertexTaxCategoryCode the vertex tax category code
	 */
	public void setVertexTaxCategoryCode(String vertexTaxCategoryCode) {
		this.vertexTaxCategoryCode = vertexTaxCategoryCode;
	}

	/**
	 * Gets effective date.
	 *
	 * @return the effective date
	 */
	public LocalDate getEffectiveDate() {
		return effectiveDate;
	}

	/**
	 * Sets effective date.
	 *
	 * @param effectiveDate the effective date
	 */
	public void setEffectiveDate(LocalDate effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

	/**
	 * Gets vertex tax category.
	 *
	 * @return the vertex tax category
	 */
	public VertexTaxCategory getVertexTaxCategory() {
		return vertexTaxCategory;
	}

	/**
	 * Sets vertex tax category.
	 *
	 * @param vertexTaxCategory the vertex tax category
	 */
	public void setVertexTaxCategory(VertexTaxCategory vertexTaxCategory) {
		this.vertexTaxCategory = vertexTaxCategory;
	}

	/**
	 * Return the action code.
	 * @return the action code
	 */
	public String getActionCode() {
		return actionCode;
	}

	/**
	 * Set the action code.
	 * @param actionCode the action code.
	 */
	public void setActionCode(String actionCode) {
		this.actionCode = actionCode;
	}
	/**
	 * Returns a String representation of the object.
	 *
	 * @return A String representation of the object.
	 */
	@Override
	public String toString() {
		return "EffectiveVertexTaxCategory{" +
				"retailTaxable=" + retailTaxable +
				", vertexTaxCategoryCode='" + vertexTaxCategoryCode + '\'' +
				", effectiveDate=" + effectiveDate +
				", productId=" + productId +
				", retailTaxableSequenceNumber=" + retailTaxableSequenceNumber +
				", vertexTaxCategorySequenceNumber=" + vertexTaxCategorySequenceNumber +
				'}';
	}
}
