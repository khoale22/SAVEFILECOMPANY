package com.heb.scaleMaintenance.model;

import java.io.Serializable;

/**
 * Represents a scale maintenance nutrient that contains a nutrition value and a percent.
 *
 * @author m314029
 * @since 2.17.8
 */
public class ScaleMaintenanceNutrient implements Serializable{

	private static final long serialVersionUID = 7793780440560854623L;
	private Double value;
	private Long percent;

	public ScaleMaintenanceNutrient(){
		super();
	}

	public ScaleMaintenanceNutrient(Double value, Long percent) {
		super();
		this.value = value;
		this.percent = percent;
	}

	/**
	 * Returns Value.
	 *
	 * @return The Value.
	 **/
	public Double getValue() {
		return value;
	}

	/**
	 * Sets the Value.
	 *
	 * @param value The Value.
	 **/
	public ScaleMaintenanceNutrient setValue(Double value) {
		this.value = value;
		return this;
	}

	/**
	 * Returns Percent.
	 *
	 * @return The Percent.
	 **/
	public Long getPercent() {
		return percent;
	}

	/**
	 * Sets the Percent.
	 *
	 * @param percent The Percent.
	 **/
	public ScaleMaintenanceNutrient setPercent(Long percent) {
		this.percent = percent;
		return this;
	}

	/**
	 * Returns a String representation of the object.
	 *
	 * @return A String representation of the object.
	 */
	@Override
	public String toString() {
		return "Nutrient{" +
				"value=" + value +
				", percent=" + percent +
				'}';
	}
}
