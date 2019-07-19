package com.heb.scaleMaintenance.model;

/**
 * @author m314029
 * @since 2.20.0
 */
public class EPlumMessage {

	private String headerData;
	private String pluData;
	private String ingredientData;
	private String nutrientData;

	/**
	 * Returns HeaderData.
	 *
	 * @return The HeaderData.
	 **/
	public String getHeaderData() {
		return headerData;
	}

	/**
	 * Sets the HeaderData.
	 *
	 * @param headerData The HeaderData.
	 **/
	public EPlumMessage setHeaderData(String headerData) {
		this.headerData = headerData;
		return this;
	}

	/**
	 * Returns PluData.
	 *
	 * @return The PluData.
	 **/
	public String getPluData() {
		return pluData;
	}

	/**
	 * Sets the PluData.
	 *
	 * @param pluData The PluData.
	 **/
	public EPlumMessage setPluData(String pluData) {
		this.pluData = pluData;
		return this;
	}

	/**
	 * Returns IngredientData.
	 *
	 * @return The IngredientData.
	 **/
	public String getIngredientData() {
		return ingredientData;
	}

	/**
	 * Sets the IngredientData.
	 *
	 * @param ingredientData The IngredientData.
	 **/
	public EPlumMessage setIngredientData(String ingredientData) {
		this.ingredientData = ingredientData;
		return this;
	}

	/**
	 * Returns NutrientData.
	 *
	 * @return The NutrientData.
	 **/
	public String getNutrientData() {
		return nutrientData;
	}

	/**
	 * Sets the NutrientData.
	 *
	 * @param nutrientData The NutrientData.
	 **/
	public EPlumMessage setNutrientData(String nutrientData) {
		this.nutrientData = nutrientData;
		return this;
	}

	/**
	 * Returns a String representation of the object.
	 *
	 * @return A String representation of the object.
	 */
	@Override
	public String toString() {
		return "EPlumMessage{" +
				"headerData='" + headerData + '\'' +
				", pluData='" + pluData + '\'' +
				", ingredientData='" + ingredientData + '\'' +
				", nutrientData='" + nutrientData + '\'' +
				'}';
	}
}
