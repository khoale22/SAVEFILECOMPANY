package com.heb.scaleMaintenance.model;

import java.io.Serializable;

/**
 * Represents nutrient statement information for scale maintenance.
 *
 * @author m314029
 * @since 2.17.8
 */
public class ScaleMaintenanceNutrientStatement implements Serializable{

	private static final long serialVersionUID = -4048808807989180651L;

	private double measureQuantity;
	private long metricQuantity;
	private String uomMetricCode;
	private String uomCommonCode;
	private long servingsPerContainer;

	/**
	 * Returns MeasureQuantity.
	 *
	 * @return The MeasureQuantity.
	 **/
	public double getMeasureQuantity() {
		return measureQuantity;
	}

	/**
	 * Sets the MeasureQuantity.
	 *
	 * @param measureQuantity The MeasureQuantity.
	 **/
	public ScaleMaintenanceNutrientStatement setMeasureQuantity(double measureQuantity) {
		this.measureQuantity = measureQuantity;
		return this;
	}

	/**
	 * Returns MetricQuantity.
	 *
	 * @return The MetricQuantity.
	 **/
	public long getMetricQuantity() {
		return metricQuantity;
	}

	/**
	 * Sets the MetricQuantity.
	 *
	 * @param metricQuantity The MetricQuantity.
	 **/
	public ScaleMaintenanceNutrientStatement setMetricQuantity(long metricQuantity) {
		this.metricQuantity = metricQuantity;
		return this;
	}

	/**
	 * Returns UomMetricCode.
	 *
	 * @return The UomMetricCode.
	 **/
	public String getUomMetricCode() {
		return uomMetricCode;
	}

	/**
	 * Sets the UomMetricCode.
	 *
	 * @param uomMetricCode The UomMetricCode.
	 **/
	public ScaleMaintenanceNutrientStatement setUomMetricCode(String uomMetricCode) {
		this.uomMetricCode = uomMetricCode;
		return this;
	}

	/**
	 * Returns UomCommonCode.
	 *
	 * @return The UomCommonCode.
	 **/
	public String getUomCommonCode() {
		return uomCommonCode;
	}

	/**
	 * Sets the UomCommonCode.
	 *
	 * @param uomCommonCode The UomCommonCode.
	 **/
	public ScaleMaintenanceNutrientStatement setUomCommonCode(String uomCommonCode) {
		this.uomCommonCode = uomCommonCode;
		return this;
	}

	/**
	 * Returns ServingsPerContainer.
	 *
	 * @return The ServingsPerContainer.
	 **/
	public long getServingsPerContainer() {
		return servingsPerContainer;
	}

	/**
	 * Sets the ServingsPerContainer.
	 *
	 * @param servingsPerContainer The ServingsPerContainer.
	 **/
	public ScaleMaintenanceNutrientStatement setServingsPerContainer(long servingsPerContainer) {
		this.servingsPerContainer = servingsPerContainer;
		return this;
	}
}
