/*
 * ScaleManagementUnit
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.springframework.data.domain.Sort;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;  
/**
 * Represents a scale unit.
 *
 * @author vn73545
 * @since 2.18.4
 */
@Entity
@Table(name="sl_scalescan")
@TypeDefs({
@TypeDef(name = "fixedLengthChar", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharType.class), 
@TypeDef(name = "fixedLengthVarChar", typeClass = com.heb.pm.util.oracle.OracleEmptySpaceVarCharType.class) 
}) 
public class ExportScaleUpc implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "UPC_KEY")
	private long upc;

	@Column(name= "MAINT_FUNCTION")
	private char maintFunction;

	@Column(name = "EFFECTIVE_DATE")
	private LocalDate effectiveDate;

	@Column(name = "STRIP_FLAG")
	private boolean stripFlag;

	@Column(name = "SHELF_LIFE")
	private int shelfLifeDays;

	@Column(name = "FREEZE_BY_DAYS")
	private int freezeByDays;

	@Column(name = "EAT_BY_DAYS")
	private int eatByDays;

	@Column(name = "PD_HILITE_PRNT_CD")
	private long actionCode;

	@Column(name = "PD_SAFE_HAND_CD")
	private long graphicsCode;

	@Column(name = "SL_LBL_FRMAT_1_CD")
	private long labelFormatOne;

	@Column(name = "SL_LBL_FRMAT_2_CD")
	private long labelFormatTwo;

	@Column(name = "GRADE_NBR")
	private int grade;

	@Column(name = "TARE_SERV_COUNTER")
	private double serviceCounterTare;

	@Column(name = "INGR_STATEMENT_NUM")
	private long ingredientStatement;

	@Column(name = "PD_NTRNT_STMT_NO")
	private long nutrientStatement;

	@Column(name = "TARE_PREPACK")
	private double prePackTare;

	@Column(name = "NET_WT")
	private double netWeight;

	@Column(name = "FRC_TARE_SW")
	private boolean forceTare;

	@Column(name = "PRC_OVRD_SW")
	private boolean priceOverride;
	
	@OrderBy("pd_ingrd_pct desc")
	@NotFound(action = NotFoundAction.IGNORE)
	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "pd_ingrd_stmt_no", referencedColumnName = "INGR_STATEMENT_NUM", insertable = false, updatable = false)
	private List<IngredientStatementDetail> ingredientStatementDetails;

	/**
	 * Get the ingredientStatementDetails.
	 *
	 * @return the ingredientStatementDetails
	 */
	public List<IngredientStatementDetail> getIngredientStatementDetails() {
		return ingredientStatementDetails;
	}

	/**
	 * Set the ingredientStatementDetails.
	 *
	 * @param ingredientStatementDetails the ingredientStatementDetails to set
	 */
	public void setIngredientStatementDetails(List<IngredientStatementDetail> ingredientStatementDetails) {
		this.ingredientStatementDetails = ingredientStatementDetails;
	}

	/**
	 * Get the upc.
	 *
	 * @return the upc
	 */
	public long getUpc() {
		return upc;
	}

	/**
	 * Set the upc.
	 *
	 * @param upc the upc to set
	 */
	public void setUpc(long upc) {
		this.upc = upc;
	}

	/**
	 * Get the maintFunction.
	 *
	 * @return the maintFunction
	 */
	public char getMaintFunction() {
		return maintFunction;
	}

	/**
	 * Set the maintFunction.
	 *
	 * @param maintFunction the maintFunction to set
	 */
	public void setMaintFunction(char maintFunction) {
		this.maintFunction = maintFunction;
	}

	/**
	 * Get the effectiveDate.
	 *
	 * @return the effectiveDate
	 */
	public LocalDate getEffectiveDate() {
		return effectiveDate;
	}

	/**
	 * Set the effectiveDate.
	 *
	 * @param effectiveDate the effectiveDate to set
	 */
	public void setEffectiveDate(LocalDate effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

	/**
	 * Get the stripFlag.
	 *
	 * @return the stripFlag
	 */
	public boolean isStripFlag() {
		return stripFlag;
	}

	/**
	 * Set the stripFlag.
	 *
	 * @param stripFlag the stripFlag to set
	 */
	public void setStripFlag(boolean stripFlag) {
		this.stripFlag = stripFlag;
	}

	/**
	 * Get the shelfLifeDays.
	 *
	 * @return the shelfLifeDays
	 */
	public int getShelfLifeDays() {
		return shelfLifeDays;
	}

	/**
	 * Set the shelfLifeDays.
	 *
	 * @param shelfLifeDays the shelfLifeDays to set
	 */
	public void setShelfLifeDays(int shelfLifeDays) {
		this.shelfLifeDays = shelfLifeDays;
	}

	/**
	 * Get the freezeByDays.
	 *
	 * @return the freezeByDays
	 */
	public int getFreezeByDays() {
		return freezeByDays;
	}

	/**
	 * Set the freezeByDays.
	 *
	 * @param freezeByDays the freezeByDays to set
	 */
	public void setFreezeByDays(int freezeByDays) {
		this.freezeByDays = freezeByDays;
	}

	/**
	 * Get the eatByDays.
	 *
	 * @return the eatByDays
	 */
	public int getEatByDays() {
		return eatByDays;
	}

	/**
	 * Set the eatByDays.
	 *
	 * @param eatByDays the eatByDays to set
	 */
	public void setEatByDays(int eatByDays) {
		this.eatByDays = eatByDays;
	}

	/**
	 * Get the actionCode.
	 *
	 * @return the actionCode
	 */
	public long getActionCode() {
		return actionCode;
	}

	/**
	 * Set the actionCode.
	 *
	 * @param actionCode the actionCode to set
	 */
	public void setActionCode(long actionCode) {
		this.actionCode = actionCode;
	}

	/**
	 * Get the graphicsCode.
	 *
	 * @return the graphicsCode
	 */
	public long getGraphicsCode() {
		return graphicsCode;
	}

	/**
	 * Set the graphicsCode.
	 *
	 * @param graphicsCode the graphicsCode to set
	 */
	public void setGraphicsCode(long graphicsCode) {
		this.graphicsCode = graphicsCode;
	}

	/**
	 * Get the labelFormatOne.
	 *
	 * @return the labelFormatOne
	 */
	public long getLabelFormatOne() {
		return labelFormatOne;
	}

	/**
	 * Set the labelFormatOne.
	 *
	 * @param labelFormatOne the labelFormatOne to set
	 */
	public void setLabelFormatOne(long labelFormatOne) {
		this.labelFormatOne = labelFormatOne;
	}

	/**
	 * Get the labelFormatTwo.
	 *
	 * @return the labelFormatTwo
	 */
	public long getLabelFormatTwo() {
		return labelFormatTwo;
	}

	/**
	 * Set the labelFormatTwo.
	 *
	 * @param labelFormatTwo the labelFormatTwo to set
	 */
	public void setLabelFormatTwo(long labelFormatTwo) {
		this.labelFormatTwo = labelFormatTwo;
	}

	/**
	 * Get the grade.
	 *
	 * @return the grade
	 */
	public int getGrade() {
		return grade;
	}

	/**
	 * Set the grade.
	 *
	 * @param grade the grade to set
	 */
	public void setGrade(int grade) {
		this.grade = grade;
	}

	/**
	 * Get the serviceCounterTare.
	 *
	 * @return the serviceCounterTare
	 */
	public double getServiceCounterTare() {
		return serviceCounterTare;
	}

	/**
	 * Set the serviceCounterTare.
	 *
	 * @param serviceCounterTare the serviceCounterTare to set
	 */
	public void setServiceCounterTare(double serviceCounterTare) {
		this.serviceCounterTare = serviceCounterTare;
	}

	/**
	 * Get the ingredientStatement.
	 *
	 * @return the ingredientStatement
	 */
	public long getIngredientStatement() {
		return ingredientStatement;
	}

	/**
	 * Set the ingredientStatement.
	 *
	 * @param ingredientStatement the ingredientStatement to set
	 */
	public void setIngredientStatement(long ingredientStatement) {
		this.ingredientStatement = ingredientStatement;
	}

	/**
	 * Get the nutrientStatement.
	 *
	 * @return the nutrientStatement
	 */
	public long getNutrientStatement() {
		return nutrientStatement;
	}

	/**
	 * Set the nutrientStatement.
	 *
	 * @param nutrientStatement the nutrientStatement to set
	 */
	public void setNutrientStatement(long nutrientStatement) {
		this.nutrientStatement = nutrientStatement;
	}

	/**
	 * Get the prePackTare.
	 *
	 * @return the prePackTare
	 */
	public double getPrePackTare() {
		return prePackTare;
	}

	/**
	 * Set the prePackTare.
	 *
	 * @param prePackTare the prePackTare to set
	 */
	public void setPrePackTare(double prePackTare) {
		this.prePackTare = prePackTare;
	}

	/**
	 * Get the netWeight.
	 *
	 * @return the netWeight
	 */
	public double getNetWeight() {
		return netWeight;
	}

	/**
	 * Set the netWeight.
	 *
	 * @param netWeight the netWeight to set
	 */
	public void setNetWeight(double netWeight) {
		this.netWeight = netWeight;
	}

	/**
	 * Get the forceTare.
	 *
	 * @return the forceTare
	 */
	public boolean isForceTare() {
		return forceTare;
	}

	/**
	 * Set the forceTare.
	 *
	 * @param forceTare the forceTare to set
	 */
	public void setForceTare(boolean forceTare) {
		this.forceTare = forceTare;
	}

	/**
	 * Get the priceOverride.
	 *
	 * @return the priceOverride
	 */
	public boolean isPriceOverride() {
		return priceOverride;
	}

	/**
	 * Set the priceOverride.
	 *
	 * @param priceOverride the priceOverride to set
	 */
	public void setPriceOverride(boolean priceOverride) {
		this.priceOverride = priceOverride;
	}
}
