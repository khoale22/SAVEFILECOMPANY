package com.heb.pm.entity;

import org.apache.commons.lang.StringUtils;

/**
 * Represents results from vertex service call.  Holds Vertex category data.
 * Assigned by Vertex (for global sales tax data) based on the buyers location.
 *
 * @author m594201
 * @since 2.12.0
 */
public class VertexTaxCategory{

	private static final String DISPLAY_NAME_FORMAT = "%s [%s]";

	private String dvrCode;

	private String categoryMapId;

	private String categoryCode;

	private String categoryName;

	private String categoryDescription;

	/**
	 * Gets dvr code.  The Vertex tax category code which is the driver code for the tax record.
	 *
	 * @return the The Vertex tax category code which is the driver code for the tax record.
	 */
	public String getDvrCode() {
		return dvrCode;
	}

	/**
	 * Sets dvr code. The Vertex tax category code which is the driver code for the tax record.
	 *
	 * @param dvrCode The Vertex tax category code which is the driver code for the tax record.
	 */
	public void setDvrCode(String dvrCode) {
		this.dvrCode = dvrCode;
	}

	/**
	 * Gets category map id. Id tied to the tax category.
	 *
	 * @return the category map id tied to the tax category.
	 */
	public String getCategoryMapId() {
		return categoryMapId;
	}

	/**
	 * Sets category map id. Id tied to the tax category.
	 *
	 * @param categoryMapId the category map id tied to the tax category.
	 */
	public void setCategoryMapId(String categoryMapId) {
		this.categoryMapId = categoryMapId;
	}

	/**
	 * Gets category code.  The numeric identifier of the tax category.
	 *
	 * @return the category code which is a numeric identifier of the tax category.
	 */
	public String getCategoryCode() {
		return categoryCode;
	}

	/**
	 * Sets category code.
	 *
	 * @param categoryCode the category code
	 */
	public void setCategoryCode(String categoryCode) {
		this.categoryCode = categoryCode;
	}

	/**
	 * Gets category name.  The name of the tax category for this product.
	 *
	 * @return the category name of the tax category for this product.
	 */
	public String getCategoryName() {
		return categoryName;
	}

	/**
	 * Sets category name.  The tax category display name.
	 *
	 * @param categoryName the category name that is displayed in the UI.
	 */
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	/**
	 * Gets category description. The tax category display description.
	 *
	 * @return the category description name that is displayed in the UI.
	 */
	public String getCategoryDescription() {
		return categoryDescription;
	}

	/**
	 * Sets category description.  The tax category display description
	 *
	 * @param categoryDescription tax category display description
	 */
	public void setCategoryDescription(String categoryDescription) {
		this.categoryDescription = categoryDescription;
	}

	/**
	 * Returns a display name for a VertexTaxCategory to display on the GUI.
	 *
	 * @return A display name.
	 */
	public String getDisplayName() {
		if(this.dvrCode == null && this.categoryName == null){
			return null;
		}
		else if(this.dvrCode == null){
			return this.categoryName.trim();
		}
		else if(this.categoryName == null){
			return String.format(VertexTaxCategory.DISPLAY_NAME_FORMAT, StringUtils.EMPTY, this.dvrCode.trim());
		}
		return String.format(VertexTaxCategory.DISPLAY_NAME_FORMAT, this.categoryName.trim(), this.dvrCode.trim());
	}
}
