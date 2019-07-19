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

// dB2Oracle changes vn00907 starts
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;  
 // dB2Oracle changes vn00907 
/**
 * Represents a scale unit.
 *
 * @author m314029
 * @since 2.0.8
 */
@Entity
@Table(name="sl_scalescan")
 // dB2Oracle changes vn00907 starts
@TypeDefs({
@TypeDef(name = "fixedLengthChar", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharType.class), 
@TypeDef(name = "fixedLengthVarChar", typeClass = com.heb.pm.util.oracle.OracleEmptySpaceVarCharType.class) 
}) 


 // dB2Oracle changes vn00907
public class ScaleUpc implements Serializable {

	private static final long serialVersionUID = 1L;

	private static final int FOUR_BYTES = 32;

	private static final String SCALE_UPC_SORT_FIELD = "upc";

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

	@Column(name = "PRODUCT_DESC_LINE1")
	 // dB2Oracle changes vn00907 starts
	@Type(type="fixedLengthVarChar")
	// dB2Oracle changes vn00907 ends      
	private String englishDescriptionOne;

	@Column(name = "PRODUCT_DESC_LINE2")
	 // dB2Oracle changes vn00907 starts
		@Type(type="fixedLengthVarChar")
		// dB2Oracle changes vn00907 ends   
	private String englishDescriptionTwo;

	@Column(name = "PRODUCT_DESC_LINE3")
	 // dB2Oracle changes vn00907 starts
	@Type(type="fixedLengthChar")
	 // dB2Oracle changes vn00907 ends                   
	private String englishDescriptionThree;

	@Column(name = "PRODUCT_DESC_LINE4")
	 // dB2Oracle changes vn00907 starts
	@Type(type="fixedLengthChar")    
	 // dB2Oracle changes vn00907              
	private String englishDescriptionFour; 

	@Column(name = "SPANISH_DESC_LINE1")
	 // dB2Oracle changes vn00907 starts
		@Type(type="fixedLengthVarChar")
		// dB2Oracle changes vn00907 ends   
	private String spanishDescriptionOne;

	@Column(name = "SPANISH_DESC_LINE2")
	 // dB2Oracle changes vn00907 starts
		@Type(type="fixedLengthVarChar")
		// dB2Oracle changes vn00907 ends   
	private String spanishDescriptionTwo;

	@Column(name = "SPANISH_DESC_LINE3")
	 // dB2Oracle changes vn00907 starts
	@Type(type="fixedLengthChar")  
	 // dB2Oracle changes vn00907                 
	private String spanishDescriptionThree;

	@Column(name = "SPANISH_DESC_LINE4")
	 // dB2Oracle changes vn00907 starts
	@Type(type="fixedLengthChar")    
	 // dB2Oracle changes vn00907                  
	private String spanishDescriptionFour; 

	@NotFound(action = NotFoundAction.IGNORE)
	@OneToOne(fetch = FetchType.LAZY)
	// dB2Oracle changes vn00907 column name to caps
	@JoinColumn(name="UPC_KEY",referencedColumnName = "pd_assoc_upc_no", insertable = false, updatable = false,
	nullable = false)
	/*@JoinColumn(name="upc_key", referencedColumnName = "pd_assoc_upc_no", insertable = false, updatable = false,
			nullable = false)*/
	private AssociatedUpc associateUpc;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="SL_LBL_FRMAT_1_CD", referencedColumnName = "sl_lbl_frmat_cd",
			insertable = false, updatable = false, nullable = false)
	private ScaleLabelFormat firstLabelFormat;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="SL_LBL_FRMAT_2_CD", referencedColumnName = "sl_lbl_frmat_cd",
			insertable = false, updatable = false, nullable = false)
	private ScaleLabelFormat secondLabelFormat;

    @JsonIgnoreProperties("scaleUpcs")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "INGR_STATEMENT_NUM", referencedColumnName = "pd_ingrd_stmt_no", insertable = false, updatable = false)
	private IngredientStatementHeader ingredientStatementHeader;

    @NotFound(action = NotFoundAction.IGNORE)
    @JsonIgnoreProperties("scaleUpc")
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PD_NTRNT_STMT_NO", referencedColumnName = "PD_NTRNT_STMT_NO", insertable = false, updatable = false)
	private NutrientStatementHeader nutrientStatementHeader;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PD_SAFE_HAND_CD", referencedColumnName = "PD_SAFE_HAND_CD", insertable = false, updatable = false)
	private ScaleGraphicsCode scaleGraphicsCode;


	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PD_HILITE_PRNT_CD", referencedColumnName = "PD_HILITE_PRNT_CD", insertable = false, updatable = false)
	private ScaleActionCode scaleActionCode;

	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "PD_NTRNT_STMT_NO", referencedColumnName = "PD_NTRNT_STMT_NO", insertable = false, updatable = false)
	private List<NutrientStatementDetail> nutrientStatementDetails;

	/**
	 * Gets the nutrient statement header for this scale upc.
	 *
	 * @return The nutrient statement header.
	 */
	public NutrientStatementHeader getNutrientStatementHeader() {
		return nutrientStatementHeader;
	}

	/**
	 * Sets the nutrient statement header for this scale upc.
	 *
	 * @param nutrientStatementHeader The nutrient statement header.
	 */
	public void setNutrientStatementHeader(NutrientStatementHeader nutrientStatementHeader) {
		this.nutrientStatementHeader = nutrientStatementHeader;
	}

	/**
	 * @return the nutrientStatementDetails
	 */
	public List<NutrientStatementDetail> getNutrientStatementDetails() {
		return nutrientStatementDetails;
	}

	/**
	 * @param nutrientStatementDetails the nutrientStatementDetails to set
	 */
	public void setNutrientStatementDetails(List<NutrientStatementDetail> nutrientStatementDetails) {
		this.nutrientStatementDetails = nutrientStatementDetails;
	}

	/**
	 * Gets the ingredient statement header for this scale upc.
	 *
	 * @return The ingredient statement header.
	 */
	public IngredientStatementHeader getIngredientStatementHeader() {
		return ingredientStatementHeader;
	}

	/**
	 * Sets the ingredient statement header for this scale upc.
	 *
	 * @param ingredientStatementHeader The ingredient statement header.
	 */
	public void setIngredientStatementHeader(IngredientStatementHeader ingredientStatementHeader) {
		this.ingredientStatementHeader = ingredientStatementHeader;
	}

	/**
	 * Gets the upc for this scale upc.
	 *
	 * @return The upc for this scale upc.
	 */
	public long getUpc() {
		return upc;
	}

	/**
	 * Sets the upc for this scale upc.
	 *
	 * @param upc The upc for this scale upc.
	 */
	public void setUpc(long upc) {
		this.upc = upc;
	}

	/**
	 * Gets the maint_function for this scale upc.
	 * @return The maint_function for this scale upc.
	 */
	public char getMaintFunction() {
		return maintFunction;
	}

	/**
	 * Sets the maint_function for this scale upc.
	 * @param maintFunction The maint_function for this scale upc.
	 */
	public void setMaintFunction(char maintFunction) {
		this.maintFunction = maintFunction;
	}

	/**
	 * Gets the effectiveDate for this scale upc.
	 *
	 * @return The effectiveDate for this scale upc.
	 */
	public LocalDate getEffectiveDate() {
		return effectiveDate;
	}

	/**
	 * Sets the effectiveDate for this scale upc.
	 *
	 * @param effectiveDate The effectiveDate for this scale upc.
	 */
	public void setEffectiveDate(LocalDate effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

	/**
	 * Is strip flag boolean.
	 *
	 * @return the boolean
	 */
	public boolean isStripFlag() {
		return stripFlag;
	}

	/**
	 * Sets strip flag.
	 *
	 * @param stripFlag the strip flag
	 */
	public void setStripFlag(boolean stripFlag) {
		this.stripFlag = stripFlag;
	}

	/**
	 * Gets the shelfLifeDays for this scale upc.
	 *
	 * @return The shelfLifeDays for this scale upc.
	 */
	public int getShelfLifeDays() {
		return shelfLifeDays;
	}

	/**
	 * Sets the shelfLifeDays for this scale upc.
	 *
	 * @param shelfLifeDays The shelfLifeDays for this scale upc.
	 */
	public void setShelfLifeDays(int shelfLifeDays) {
		this.shelfLifeDays = shelfLifeDays;
	}

	/**
	 * Gets the freezeByDays for this scale upc.
	 *
	 * @return The freezeByDays for this scale upc.
	 */
	public int getFreezeByDays() {
		return freezeByDays;
	}

	/**
	 * Sets the freezeByDays for this scale upc.
	 *
	 * @param freezeByDays The freezeByDays for this scale upc.
	 */
	public void setFreezeByDays(int freezeByDays) {
		this.freezeByDays = freezeByDays;
	}

	/**
	 * Gets the eatByDays for this scale upc.
	 *
	 * @return The eatByDays for this scale upc.
	 */
	public int getEatByDays() {
		return eatByDays;
	}

	/**
	 * Sets the eatByDays for this scale upc.
	 *
	 * @param eatByDays The eatByDays for this scale upc.
	 */
	public void setEatByDays(int eatByDays) {
		this.eatByDays = eatByDays;
	}

	/**
	 * Gets the actionCode for this scale upc.
	 *
	 * @return The actionCode for this scale upc.
	 */
	public long getActionCode() {
		return actionCode;
	}

	/**
	 * Sets the actionCode for this scale upc.
	 *
	 * @param actionCode The actionCode for this scale upc.
	 */
	public void setActionCode(long actionCode) {
		this.actionCode = actionCode;
	}

	/**
	 * Gets the graphicsCode for this scale upc.
	 *
	 * @return The graphicsCode for this scale upc.
	 */
	public long getGraphicsCode() {
		return graphicsCode;
	}

	/**
	 * Sets the graphicsCode for this scale upc.
	 *
	 * @param graphicsCode The graphicsCode for this scale upc.
	 */
	public void setGraphicsCode(long graphicsCode) {
		this.graphicsCode = graphicsCode;
	}

	/**
	 * Gets the labelFormatOne for this scale upc.
	 *
	 * @return The labelFormatOne for this scale upc.
	 */
	public long getLabelFormatOne() {
		return labelFormatOne;
	}

	/**
	 * Sets the labelFormatOne for this scale upc.
	 *
	 * @param labelFormatOne The labelFormatOne for this scale upc.
	 */
	public void setLabelFormatOne(long labelFormatOne) {
		this.labelFormatOne = labelFormatOne;
	}

	/**
	 * Gets the labelFormatTwo for this scale upc.
	 *
	 * @return The labelFormatTwo for this scale upc.
	 */
	public long getLabelFormatTwo() {
		return labelFormatTwo;
	}

	/**
	 * Sets the labelFormatTwo this scale upc.
	 *
	 * @param labelFormatTwo The labelFormatTwo for this scale upc.
	 */
	public void setLabelFormatTwo(long labelFormatTwo) {
		this.labelFormatTwo = labelFormatTwo;
	}

	/**
	 * Gets the grade for this scale upc.
	 *
	 * @return The grade for this scale upc.
	 */
	public int getGrade() {
		return grade;
	}

	/**
	 * Sets the grade for this scale upc.
	 *
	 * @param grade The grade for this scale upc.
	 */
	public void setGrade(int grade) {
		this.grade = grade;
	}

	/**
	 * Gets the serviceCounterTare for this scale upc.
	 *
	 * @return The serviceCounterTare for this scale upc.
	 */
	public double getServiceCounterTare() {
		return serviceCounterTare;
	}

	/**
	 * Sets the serviceCounterTare for this scale upc.
	 *
	 * @param serviceCounterTare The serviceCounterTare for this scale upc.
	 */
	public void setServiceCounterTare(double serviceCounterTare) {
		this.serviceCounterTare = serviceCounterTare;
	}

	/**
	 * Gets the ingredientStatement for this scale upc.
	 *
	 * @return The ingredientStatement for this scale upc.
	 */
	public long getIngredientStatement() {
		return ingredientStatement;
	}

	/**
	 * Sets the ingredientStatement for this scale upc.
	 *
	 * @param ingredientStatement The ingredientStatement for this scale upc.
	 */
	public void setIngredientStatement(long ingredientStatement) {
		this.ingredientStatement = ingredientStatement;
	}

	/**
	 * Gets the nutrientStatement for this scale upc.
	 *
	 * @return The nutrientStatement for this scale upc.
	 */
	public long getNutrientStatement() {
		return nutrientStatement;
	}

	/**
	 * Sets the nutrientStatement for this scale upc.
	 *
	 * @param nutrientStatement The nutrientStatement for this scale upc.
	 */
	public void setNutrientStatement(long nutrientStatement) {
		this.nutrientStatement = nutrientStatement;
	}

	/**
	 * Gets the prePackTare for this scale upc.
	 *
	 * @return The prePackTare for this scale upc.
	 */
	public double getPrePackTare() {
		return prePackTare;
	}

	/**
	 * Sets the prePackTare for this scale upc.
	 *
	 * @param prePackTare The prePackTare for this scale upc.
	 */
	public void setPrePackTare(double prePackTare) {
		this.prePackTare = prePackTare;
	}

	/**
	 * Gets the netWeight for this scale upc.
	 *
	 * @return The netWeight for this scale upc.
	 */
	public double getNetWeight() {
		return netWeight;
	}

	/**
	 * Sets the netWeight for this scale upc.
	 *
	 * @param netWeight The netWeight for this scale upc.
	 */
	public void setNetWeight(double netWeight) {
		this.netWeight = netWeight;
	}

	/**
	 * Returns whether this scale upc should have the tare forced.
	 *
	 * @return The forceTare for this scale upc.
	 */
	public boolean isForceTare() {
		return forceTare;
	}

	/**
	 * Sets whether this scale upc should have the tare forced.
	 *
	 * @param forceTare The forceTare for this scale upc.
	 */
	public void setForceTare(boolean forceTare) {
		this.forceTare = forceTare;
	}

	/**
	 * Returns whether this scale upc should allow price override.
	 *
	 * @return The upc for this scale upc.
	 */
	public boolean isPriceOverride() {
		return priceOverride;
	}

	/**
	 * Sets whether this scale upc should allow price override.
	 *
	 * @param priceOverride The priceOverride for this scale upc.
	 */
	public void setPriceOverride(boolean priceOverride) {
		this.priceOverride = priceOverride;
	}

	/**
	 * Gets the englishDescriptionOne for this scale upc.
	 *
	 * @return The englishDescriptionOne for this scale upc.
	 */
	public String getEnglishDescriptionOne() {
		return englishDescriptionOne;
	}

	/**
	 * Sets the englishDescriptionOne for this scale upc.
	 *
	 * @param englishDescriptionOne The englishDescriptionOne for this scale upc.
	 */
	public void setEnglishDescriptionOne(String englishDescriptionOne) {
		this.englishDescriptionOne = englishDescriptionOne;
	}

	/**
	 * Gets the englishDescriptionTwo for this scale upc.
	 *
	 * @return The englishDescriptionTwo for this scale upc.
	 */
	public String getEnglishDescriptionTwo() {
		return englishDescriptionTwo;
	}

	/**
	 * Sets the englishDescriptionTwo for this scale upc.
	 *
	 * @param englishDescriptionTwo The englishDescriptionTwo for this scale upc.
	 */
	public void setEnglishDescriptionTwo(String englishDescriptionTwo) {
		this.englishDescriptionTwo = englishDescriptionTwo;
	}

	/**
	 * Gets the englishDescriptionThree for this scale upc.
	 *
	 * @return The englishDescriptionThree for this scale upc.
	 */
	public String getEnglishDescriptionThree() {
		return englishDescriptionThree;
	}

	/**
	 * Sets the englishDescriptionThree for this scale upc.
	 *
	 * @param englishDescriptionThree The englishDescriptionThree for this scale upc.
	 */
	public void setEnglishDescriptionThree(String englishDescriptionThree) {
		this.englishDescriptionThree = englishDescriptionThree;
	}

	/**
	 * Gets the englishDescriptionFour for this scale upc.
	 *
	 * @return The englishDescriptionFour for this scale upc.
	 */
	public String getEnglishDescriptionFour() {
		return englishDescriptionFour;
	}

	/**
	 * Sets the englishDescriptionFour for this scale upc.
	 *
	 * @param englishDescriptionFour The englishDescriptionFour for this scale upc.
	 */
	public void setEnglishDescriptionFour(String englishDescriptionFour) {
		this.englishDescriptionFour = englishDescriptionFour;
	}

	/**
	 * Gets the spanishDescriptionOne for this scale upc.
	 *
	 * @return The spanishDescriptionOne for this scale upc.
	 */
	public String getSpanishDescriptionOne() {
		return spanishDescriptionOne;
	}

	/**
	 * Sets the spanishDescriptionOne for this scale upc.
	 *
	 * @param spanishDescriptionOne for this scale upc.
	 */
	public void setSpanishDescriptionOne(String spanishDescriptionOne) {
		this.spanishDescriptionOne = spanishDescriptionOne;
	}

	/**
	 * Gets the spanishDescriptionTwo for this scale upc.
	 *
	 * @return The spanishDescriptionTwo for this scale upc.
	 */
	public String getSpanishDescriptionTwo() {
		return spanishDescriptionTwo;
	}

	/**
	 * Sets the spanishDescriptionTwo for this scale upc.
	 *
	 * @param spanishDescriptionTwo The spanishDescriptionTwo for this scale upc.
	 */
	public void setSpanishDescriptionTwo(String spanishDescriptionTwo) {
		this.spanishDescriptionTwo = spanishDescriptionTwo;
	}

	/**
	 * Gets the spanishDescriptionThree for this scale upc.
	 *
	 * @return The spanishDescriptionThree for this scale upc.
	 */
	public String getSpanishDescriptionThree() {
		return spanishDescriptionThree;
	}

	/**
	 * Sets the spanishDescriptionThree for this scale upc.
	 *
	 * @param spanishDescriptionThree The spanishDescriptionThree for this scale upc.
	 */
	public void setSpanishDescriptionThree(String spanishDescriptionThree) {
		this.spanishDescriptionThree = spanishDescriptionThree;
	}

	/**
	 * Gets the spanishDescriptionFour for this scale upc.
	 *
	 * @return The spanishDescriptionFour for this scale upc.
	 */
	public String getSpanishDescriptionFour() {
		return spanishDescriptionFour;
	}

	/**
	 * Sets the spanishDescriptionFour for this scale upc.
	 *
	 * @param spanishDescriptionFour The spanishDescriptionFour for this scale upc.
	 */
	public void setSpanishDescriptionFour(String spanishDescriptionFour) {
		this.spanishDescriptionFour = spanishDescriptionFour;
	}

	/**
	 * Gets the associateUpc for this scale upc.
	 *
	 * @return The associateUpc for this scale upc.
	 */
	public AssociatedUpc getAssociateUpc() {
		return associateUpc;
	}

	/**
	 * Sets the associateUpc for this scale upc.
	 *
	 * @param associateUpc The associateUpc for this scale upc.
	 */
	public void setAssociateUpc(AssociatedUpc associateUpc) {
		this.associateUpc = associateUpc;
	}

	/**
	 * Returns the first label format assigned tot the UPC.
	 *
	 * @return The first label format assigned tot the UPC.
	 */
	public ScaleLabelFormat getFirstLabelFormat() {
		return firstLabelFormat;
	}

	/**
	 * Sets the first label format assigned tot the UPC.
	 *
	 * @param firstLabelFormat The first label format assigned tot the UPC.
	 */
	public void setFirstLabelFormat(ScaleLabelFormat firstLabelFormat) {
		this.firstLabelFormat = firstLabelFormat;
	}

	/**
	 * Returns the second label format assigned tot the UPC.
	 *
	 * @return The second label format assigned tot the UPC.
	 */
	public ScaleLabelFormat getSecondLabelFormat() {
		return secondLabelFormat;
	}

	/**
	 * Sets the second label format assigned tot the UPC.
	 *
	 * @param secondLabelFormat The second label format assigned tot the UPC.
	 */
	public void setSecondLabelFormat(ScaleLabelFormat secondLabelFormat) {
		this.secondLabelFormat = secondLabelFormat;
	}

	/**
	 * Returns the object representing this UPC's graphics code.
	 *
	 * @return The object representing this UPC's graphics code.
	 */
	public ScaleGraphicsCode getScaleGraphicsCode() {
		return scaleGraphicsCode;
	}

	/**
	 * Sets the object representing this UPC's graphics code.
	 *
	 * @param scaleGraphicsCode The object representing this UPC's graphics code.
	 */
	public void setScaleGraphicsCode(ScaleGraphicsCode scaleGraphicsCode) {
		this.scaleGraphicsCode = scaleGraphicsCode;
	}

	/**
	 * Returns the object representing this UPC's action code.
	 *
	 * @return The object representing this UPC's action code.
	 */
	public ScaleActionCode getScaleActionCode() {
		return scaleActionCode;
	}

	/**
	 * Sets the object representing this UPC's action code.
	 *
	 * @param scaleActionCode The object representing this UPC's action code.
	 */
	public void setScaleActionCode(ScaleActionCode scaleActionCode) {
		this.scaleActionCode = scaleActionCode;
	}

	/**
	 * Returns the upc converted into a plu of this object.
	 *
	 * @return The plu of this object.
	 */
	public long getPlu() {

		long returnPlu =  (this.upc -20000000000L) / 100000;
		if(returnPlu < 0){
			return 0L;
		} else {
			return returnPlu;
		}
	}

	/**
	 * Compares another object to this one. If that object is an action code, it uses they keys to determine if
	 * they are equal and ignores non-key values for the comparison.
	 *
	 * @param o The object to compare to.
	 * @return True if they are equal and false otherwise.
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof ScaleUpc)) return false;

		ScaleUpc that = (ScaleUpc) o;

		return upc == that.upc;

	}

	/**
	 * Generates a string representation of this object.
	 *
	 * @return A string representation of this object.
	 */
	@Override
	public String toString() {
		return "ScaleUpc{" +
				"upc=" + upc +
				", maintFunction=" + maintFunction +
				", effectiveDate=" + effectiveDate +
				", stripFlag=" + stripFlag +
				", shelfLifeDays=" + shelfLifeDays +
				", freezeByDays=" + freezeByDays +
				", eatByDays=" + eatByDays +
				", actionCode=" + actionCode +
				", graphicsCode=" + graphicsCode +
				", labelFormatOne=" + labelFormatOne +
				", labelFormatTwo=" + labelFormatTwo +
				", grade=" + grade +
				", serviceCounterTare=" + serviceCounterTare +
				", ingredientStatement=" + ingredientStatement +
				", nutrientStatement=" + nutrientStatement +
				", prePackTare=" + prePackTare +
				", netWeight=" + netWeight +
				", forceTare=" + forceTare +
				", priceOverride=" + priceOverride +
				", englishDescriptionOne='" + englishDescriptionOne + '\'' +
				", englishDescriptionTwo='" + englishDescriptionTwo + '\'' +
				", englishDescriptionThree='" + englishDescriptionThree + '\'' +
				", englishDescriptionFour='" + englishDescriptionFour + '\'' +
				", spanishDescriptionOne='" + spanishDescriptionOne + '\'' +
				", spanishDescriptionTwo='" + spanishDescriptionTwo + '\'' +
				", spanishDescriptionThree='" + spanishDescriptionThree + '\'' +
				", spanishDescriptionFour='" + spanishDescriptionFour + '\'' +
				'}';
	}

	/**
	 * Returns a hash code for this object. If two objects are equal, they have the same hash. If they are not equal,
	 * they have different hash codes.
	 *
	 * @return The hash code for this object.
	 */
	@Override
	public int hashCode() {
		return (int) (upc ^ (upc >>> FOUR_BYTES));
	}

	/**
	 * Returns the default sort order for the Scale Upc table.
	 *
	 * @return The default sort order for the Scale Upc table.
	 */

	public static Sort getDefaultSort() {
		return  new Sort(
				new Sort.Order(Sort.Direction.ASC, ScaleUpc.SCALE_UPC_SORT_FIELD)
		);
	}
}
