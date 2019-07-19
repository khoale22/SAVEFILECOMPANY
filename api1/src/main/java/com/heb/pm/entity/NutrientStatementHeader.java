/*
 *
 * NutrientStatementHeader.java
 *
 * Copyright (c) 2017 HEB
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of HEB.
 *
 */

package com.heb.pm.entity;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.Where;
import org.springframework.data.domain.Sort;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Represents a dynamic attribute of a NutrientStatementHeader.
 *
 * @author m594201
 * @since 2.1.0
 */
@Entity
@Table(name = "pd_ntrnt_stmt_hdr")
@Where(clause = "PD_NTRNT_STMT_NO != 0 and PD_NTRNT_STMT_NO != 9999 and PD_NTRNT_STMT_NO != 99999 and PD_NTRNT_STMT_NO != 9999999")
public class NutrientStatementHeader implements Serializable {

	private static final long serialVersionUID = 1L;

	private static final String TIED_TO_PLU = "Tied to PLU";
	
	private static final String DISPLAY_NAME_FORMAT_MEASURE = "%.2f %s";
	private static final String DISPLAY_NAME_FORMAT_METRIC = "%d %s";
	private static final String DISPLAY_NAME_FORMAT_SERVING = "%d";
	private static final String DISPLAY_NAME_FORMAT_SERVING_VARIES = "%d (%s)";

	private static final String DEFAULT_SORT = "nutrientStatementNumber";
	public static final char DELETE_MAINT_SW = 'D';
	public static final long SERVINGS_PER_CONTAINER_VARY = 999L;
	public static final String VARIES = "varies";
	public static final List<Long> NULL_LIST = new ArrayList<>(Arrays.asList(0L, 9999L, 99999L, 999999L, 9999999L));

	@Id
	@Column(name = "pd_ntrnt_stmt_no") 
	private long nutrientStatementNumber;

	@JsonIgnoreProperties("nutrientStatementHeader")
	@OneToMany(mappedBy = "nutrientStatementHeader", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<NutrientStatementDetail> nutrientStatementDetailList = new ArrayList<>();

	@Column(name = "pd_comn_srvng_qty", precision = 5, scale = 2)
	private double measureQuantity;

	@Column(name = "pd_met_srvng_qty")
	private long metricQuantity;

	@Column(name = "pd_lbl_met_uom_cd")
	private long uomMetricCode;

	@Column(name = "pd_lbl_comn_uom_cd")
	private long uomCommonCode;

	@Column(name = "pd_lbl_spc_qty")
	private long servingsPerContainer;

	@Column(name = "pd_stmt_maint_dt")
	private LocalDate effectiveDate;

	@Column(name= "pd_stmt_maint_sw")  
	private char statementMaintenanceSwitch;

	@OneToOne
	@JoinColumn(name = "pd_lbl_met_uom_cd", referencedColumnName = "pd_lbl_uom_cd", insertable = false, updatable = false)
	private NutrientUom nutrientMetricUom;

	@OneToOne
	@JoinColumn(name = "pd_lbl_comn_uom_cd", referencedColumnName = "pd_lbl_uom_cd", insertable = false, updatable = false)
	private NutrientUom nutrientCommonUom;

	@NotFound(action = NotFoundAction.IGNORE)
	@JsonIgnoreProperties({"nutrientStatementHeader","nutrientStatementDetails"})
	@OneToOne(fetch = FetchType.LAZY, mappedBy = "nutrientStatementHeader")
	/*@JoinColumn(name = "PD_NTRNT_STMT_NO", referencedColumnName = "pd_ntrnt_stmt_no", insertable = false, updatable = false)*/
	// db2o changes changed to caps
	//@JoinColumn(name = "pd_ntrnt_stmt_no", referencedColumnName = "PD_NTRNT_STMT_NO", insertable = false, updatable = false)
	private ScaleUpc scaleUpc;

	/**
	 * Gets the scale upc.
	 *
	 * @return The scale upc.
	 */
	public ScaleUpc getScaleUpc() {
		return scaleUpc;
	}

	/**
	 * Sets the scale upc.
	 *
	 * @param scaleUpc The scale upc.
	 */
	public void setScaleUpcs(ScaleUpc scaleUpc) {
		this.scaleUpc = scaleUpc;
	}

	/**
	 * Gets statement maintanance switch.
	 *
	 * @return the statement maintanance switch
	 */
	public char getStatementMaintenanceSwitch() {
		return statementMaintenanceSwitch;
	}

	/**
	 * Sets statement maintanance switch.
	 *
	 * @param statementMaintenanceSwitch the statement maintanance switch
	 */
	public void setStatementMaintenanceSwitch(char statementMaintenanceSwitch) {
		this.statementMaintenanceSwitch = statementMaintenanceSwitch;
	}

	/**
	 * Gets uom common code.
	 *
	 * @return the uom common code
	 */
	public long getUomCommonCode() {
		return uomCommonCode;
	}

	/**
	 * Sets uom common code.
	 *
	 * @param uomCommonCode the uom common code
	 */
	public void setUomCommonCode(long uomCommonCode) {
		this.uomCommonCode = uomCommonCode;
	}

	/**
	 * Gets nutrient common uom.
	 *
	 * @return the nutrient common uom
	 */
	public NutrientUom getNutrientCommonUom() {
		return nutrientCommonUom;
	}

	/**
	 * Sets nutrient common uom.
	 *
	 * @param nutrientCommonUom the nutrient common uom
	 */
	public void setNutrientCommonUom(NutrientUom nutrientCommonUom) {
		this.nutrientCommonUom = nutrientCommonUom;
	}

	/**
	 * Gets uom code.
	 *
	 * @return the uom code
	 */
	public long getUomMetricCode() {
		return uomMetricCode;
	}

	/**
	 * Sets uom code.
	 *
	 * @param uomMetricCode the uom code
	 */
	public void setUomMetricCode(long uomMetricCode) {
		this.uomMetricCode = uomMetricCode;
	}

	/**
	 * Gets nutrient uom.
	 *
	 * @return the nutrient uom
	 */
	public NutrientUom getNutrientMetricUom() {
		return nutrientMetricUom;
	}

	/**
	 * Sets nutrient uom.
	 *
	 * @param nutrientMetricUom the nutrient uom
	 */
	public void setNutrientMetricUom(NutrientUom nutrientMetricUom) {
		this.nutrientMetricUom = nutrientMetricUom;
	}

	/**
	 * Gets measure quantity.
	 *
	 * @return the measure quantity
	 */
	public double getMeasureQuantity() {
		return measureQuantity;
	}

	/**
	 * Sets measure quantity.
	 *
	 * @param measureQuantity the measure quantity
	 */
	public void setMeasureQuantity(double measureQuantity) {
		this.measureQuantity = measureQuantity;
	}

	/**
	 * Gets metric quantity.
	 *
	 * @return the metric quantity
	 */
	public long getMetricQuantity() {
		return metricQuantity;
	}

	/**
	 * Sets metric quantity.
	 *
	 * @param metricQuantity the metric quantity
	 */
	public void setMetricQuantity(long metricQuantity) {
		this.metricQuantity = metricQuantity;
	}

	/**
	 * Gets servings per container.
	 *
	 * @return the servings per container
	 */
	public long getServingsPerContainer() {
		return servingsPerContainer;
	}

	/**
	 * Sets servings per container.
	 *
	 * @param servingsPerContainer the servings per container
	 */
	public void setServingsPerContainer(long servingsPerContainer) {
		this.servingsPerContainer = servingsPerContainer;
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
	 * Gets nutrient statement number.
	 *
	 * @return the nutrient statement number
	 */
	public long getNutrientStatementNumber() {
		return nutrientStatementNumber;
	}

	/**
	 * Sets nutrient statement number.
	 *
	 * @param nutrientStatementNumber the nutrient statement number
	 */
	public void setNutrientStatementNumber(long nutrientStatementNumber) {
		this.nutrientStatementNumber = nutrientStatementNumber;
	}

	/**
	 * Gets nutrient statement detail list.
	 *
	 * @return the nutrient statement detail list
	 */
	public List<NutrientStatementDetail> getNutrientStatementDetailList() {
		return nutrientStatementDetailList;
	}

	/**
	 * Sets nutrient statement detail list.
	 *
	 * @param nutrientStatementDetailList the nutrient statement detail list
	 */
	public void setNutrientStatementDetailList(List<NutrientStatementDetail> nutrientStatementDetailList) {
		this.nutrientStatementDetailList = nutrientStatementDetailList;
	}

	/**
	 * Get display name string.
	 *
	 * @return the string
	 */
	public String getMetricDisplayName(){
		return String.format(NutrientStatementHeader.DISPLAY_NAME_FORMAT_METRIC, this.metricQuantity,
				this.nutrientMetricUom.getNutrientUomDescription().trim());
	}

	/**
	 * Get display name string.
	 *
	 * @return the string
	 */
	public String getCommonDisplayName(){
		return String.format(NutrientStatementHeader.DISPLAY_NAME_FORMAT_MEASURE, this.measureQuantity,
				this.nutrientCommonUom.getNutrientUomDescription().trim());
	}

	/**
	 * Returns a default sort for the table.
	 *
	 * @return A default sort for the table.
	 */
	public static Sort getDefaultSort() {
		return new Sort(new Sort.Order(Sort.Direction.ASC, NutrientStatementHeader.DEFAULT_SORT));
	}

	/**
	 * Get display name for Serving Per Container string.
	 *
	 * @return the string
	 */
	public String getServingPerContainerDisplayName(){
		if(this.servingsPerContainer == NutrientStatementHeader.SERVINGS_PER_CONTAINER_VARY)
			return String.format(NutrientStatementHeader.DISPLAY_NAME_FORMAT_SERVING_VARIES, this.servingsPerContainer,
					NutrientStatementHeader.VARIES);
		else
			return String.format(NutrientStatementHeader.DISPLAY_NAME_FORMAT_SERVING ,this.servingsPerContainer);
	}

	/**
	 * check servingsPerContainer  is varies
	 */
	public boolean isServingSizeVariable() {
		return this.servingsPerContainer == NutrientStatementHeader.SERVINGS_PER_CONTAINER_VARY ;
	}

	/**
	 * Get remarks string.
	 *
	 * @return the string
	 */
	public String getRemarks(){
		String returnString = StringUtils.EMPTY;
		if(null != this.scaleUpc && this.scaleUpc.getUpc() > 0){
			returnString = NutrientStatementHeader.TIED_TO_PLU;
		}
		return returnString;
	}

	/**
	 * Returns a string representation of this object.
	 *
	 * @return A string representation of this object.
	 */
	@Override
	public String toString() {
		return "NutrientStatementHeader{" +
				"nutrientStatementNumber=" + nutrientStatementNumber +
				", measureQuantity=" + measureQuantity +
				", metricQuantity=" + metricQuantity +
				", uomMetricCode=" + uomMetricCode +
				", uomCommonCode=" + uomCommonCode +
				", servingsPerContainer=" + servingsPerContainer +
				", effectiveDate=" + effectiveDate +
				", statementMaintenanceSwitch=" + statementMaintenanceSwitch +
				", nutrientMetricUom=" + nutrientMetricUom +
				", nutrientCommonUom=" + nutrientCommonUom +
				", scaleUpc=" + scaleUpc +
				'}';
	}
}
