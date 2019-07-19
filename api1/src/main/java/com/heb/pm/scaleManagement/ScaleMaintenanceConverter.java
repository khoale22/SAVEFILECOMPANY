package com.heb.pm.scaleManagement;

import com.heb.pm.entity.*;
import com.heb.pm.repository.NutrientStatementPanelHeaderRepository;
import com.heb.scaleMaintenance.model.ScaleMaintenanceNutrient;
import com.heb.scaleMaintenance.model.ScaleMaintenanceNutrientStatement;
import com.heb.scaleMaintenance.model.ScaleMaintenanceProduct;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Utilities for converting scale management entities to scale maintenance java objects.
 *
 * @author m314029
 * @since 2.17.8
 */
@Component
public class ScaleMaintenanceConverter {

	private static final Logger logger = LoggerFactory.getLogger(ScaleMaintenanceConverter.class);

	// log messages
	private static final String PRODUCT_NOT_FOUND_MESSAGE = "Product master not found for upc: %d. Setting to null so" +
			" calling method can handle.";
	private static final String NUTRIENT_NOT_FOUND_MESSAGE = "Nutrient code: %d not found.";

	private static final List<Long> NLEA_LABEL_FORMATS = new ArrayList<>(Collections.singleton(5L));

	@Autowired
	private NutrientStatementPanelHeaderRepository nutrientStatementPanelHeaderRepository;

	/**
	 * Converts a list of upcs into a list of scale maintenance products.
	 *
	 * @param scaleUpcs Scale upcs to convert.
	 * @return List of scale maintenance products.
	 */
	public List<ScaleMaintenanceProduct> convertScaleUpcsToScaleMaintenanceProducts(List<ScaleUpc> scaleUpcs) {

		List<ScaleMaintenanceProduct> toReturn = new ArrayList<>();
		ScaleMaintenanceProduct currentScaleMaintenanceProduct;
		for(ScaleUpc scaleUpc : scaleUpcs){

			// convert the scale upc into a scale maintenance product
			currentScaleMaintenanceProduct = this.convertScaleUpcToScaleMaintenanceProduct(scaleUpc);
			if(currentScaleMaintenanceProduct != null){
				toReturn.add(currentScaleMaintenanceProduct);
			}
		}
		return toReturn;
	}

	/**
	 * Converts a scale upcs into a scale maintenance product.
	 *
	 * @param scaleUpc Scale upc to convert.
	 * @return Scale maintenance product.
	 */
	private ScaleMaintenanceProduct convertScaleUpcToScaleMaintenanceProduct(ScaleUpc scaleUpc) {
		ScaleMaintenanceProduct toReturn = this.setPLUInformation(scaleUpc);
		if(toReturn != null) {
			this.setIngredientInformation(toReturn, scaleUpc);
			this.setNutritionInformation(toReturn, scaleUpc);
		}
		return toReturn;
	}

	/**
	 * Sets nutrition information onto a scale maintenance product given a scale upc.
	 *
	 * @param toReturn Scale maintenance product to update nutrition values on.
	 * @param scaleUpc Scale upc with nutrition information.
	 */
	private void setNutritionInformation(ScaleMaintenanceProduct toReturn, ScaleUpc scaleUpc) {
	         // Code is commented as advised by Phill on 07-May-19
			/*if(this.isNLEA2016LabelFormat(scaleUpc.getLabelFormatOne())) {
				NutrientStatementPanelHeader nutrientStatementPanelHeader =
						this.nutrientStatementPanelHeaderRepository.findFirstBySourceSystemReferenceIdAndSourceSystemIdAndStatementMaintenanceSwitch(String.valueOf(scaleUpc.getNutrientStatement()), SourceSystem.SourceSystemNumber.SOURCE_SYSTEM_SCALE_MANAGEMENT.getValue(), NutrientStatementPanelHeader.ACTIVE_SW_Y);
				this.setNutrition(toReturn, nutrientStatementPanelHeader);

				toReturn.setNutrientStatement(
						new ScaleMaintenanceNutrientStatement()
								.setMeasureQuantity(Double.valueOf(nutrientStatementPanelHeader.getMeasureQuantity()))
								.setMetricQuantity(Long.valueOf(nutrientStatementPanelHeader.getMetricQuantity()))
								.setServingsPerContainer(nutrientStatementPanelHeader.getServingsPerContainer())
								.setUomCommonCode(nutrientStatementPanelHeader.getNutrientImperialUom() != null ?
										nutrientStatementPanelHeader.getNutrientImperialUom().getUomDescription() : StringUtils.EMPTY)
								.setUomMetricCode(nutrientStatementPanelHeader.getNutrientMetricUom() != null ?
										nutrientStatementPanelHeader.getNutrientMetricUom().getUomDescription() : StringUtils.EMPTY));
			//Start - Changes are done for Scale Load by Department by Joseph Williams, Swaroopa Rani
			} else  */
			  if (scaleUpc.getNutrientStatement() != 0 && scaleUpc.getNutrientStatement() != 9999999 && scaleUpc.getNutrientStatementHeader() != null) { 
			//End - Changes are done for Scale Load by Department by Joseph Williams, Swaroopa Rani
				if(scaleUpc.getNutrientStatementHeader().getNutrientStatementDetailList() != null) {
					this.setNutritionValuesByCode(toReturn, scaleUpc.getNutrientStatementHeader().getNutrientStatementDetailList());
				}
				NutrientStatementHeader nutrientStatementHeader = scaleUpc.getNutrientStatementHeader();
				toReturn.setNutrientStatement(
						new ScaleMaintenanceNutrientStatement()
								.setMeasureQuantity(nutrientStatementHeader.getMeasureQuantity())
								.setMetricQuantity(nutrientStatementHeader.getMetricQuantity())
								.setServingsPerContainer(nutrientStatementHeader.getServingsPerContainer())
								.setUomCommonCode(nutrientStatementHeader.getNutrientCommonUom() != null ?
										nutrientStatementHeader.getNutrientCommonUom().getNutrientUomDescription() : StringUtils.EMPTY)
								.setUomMetricCode(nutrientStatementHeader.getNutrientMetricUom() != null ?
										nutrientStatementHeader.getNutrientMetricUom().getNutrientUomDescription() : StringUtils.EMPTY));
			}
			toReturn.setNutrientStatementCode(scaleUpc.getNutrientStatement());
	}

	/**
	 * Sets nutrition values on a given scale maintenance product based on a list of nutrient statement details.
	 *
	 *  @param scaleMaintenanceProduct Scale maintenance product to set nutrition values on.
	 * @param details List of nutrient statement details.
	 */
	private void setNutritionValuesByCode(ScaleMaintenanceProduct scaleMaintenanceProduct, List<NutrientStatementDetail> details) {
		Nutrient.Codes nutrientCode;
		for(NutrientStatementDetail detail : details){
			nutrientCode = Nutrient.Codes.valueOf(detail.getKey().getNutrientLabelCode());
			switch (nutrientCode){
				case ASCORBIC_ACID : {
					scaleMaintenanceProduct.setAscorbicAcid(
							this.createScaleMaintenanceNutrientFromValueAndPercent(
									detail.getNutrientStatementQuantity(), detail.getNutrientDailyValue()));
					break;
				}
				case BIOTIN : {
					scaleMaintenanceProduct.setBiotin(
							this.createScaleMaintenanceNutrientFromValueAndPercent(
									detail.getNutrientStatementQuantity(), detail.getNutrientDailyValue()));
					break;
				}
				case CALCIUM : {
					scaleMaintenanceProduct.setCalcium(
							this.createScaleMaintenanceNutrientFromValueAndPercent(
									detail.getNutrientStatementQuantity(), detail.getNutrientDailyValue()));
					break;
				}
				case CALORIES : {
					scaleMaintenanceProduct.setCalories(
							this.createScaleMaintenanceNutrientFromValueAndPercent(
									detail.getNutrientStatementQuantity(), detail.getNutrientDailyValue()));
					break;
				}
				case CALORIES_FROM_FAT : {
					scaleMaintenanceProduct.setCaloriesFromFat(
							this.createScaleMaintenanceNutrientFromValueAndPercent(
									detail.getNutrientStatementQuantity(), detail.getNutrientDailyValue()));
					break;
				}
				case CHOLESTEROL : {
					scaleMaintenanceProduct.setCholesterol(
							this.createScaleMaintenanceNutrientFromValueAndPercent(
									detail.getNutrientStatementQuantity(), detail.getNutrientDailyValue()));
					break;
				}
				case COPPER : {
					scaleMaintenanceProduct.setCopper(
							this.createScaleMaintenanceNutrientFromValueAndPercent(
									detail.getNutrientStatementQuantity(), detail.getNutrientDailyValue()));
					break;
				}
				case DIETARY_FIBER : {
					scaleMaintenanceProduct.setDietaryFiber(
							this.createScaleMaintenanceNutrientFromValueAndPercent(
									detail.getNutrientStatementQuantity(), detail.getNutrientDailyValue()));
					break;
				}
				case FOLACIN : {
					scaleMaintenanceProduct.setFolacin(
							this.createScaleMaintenanceNutrientFromValueAndPercent(
									detail.getNutrientStatementQuantity(), detail.getNutrientDailyValue()));
					break;
				}
				case FOLIC_ACID : {
					scaleMaintenanceProduct.setFolicAcid(
							this.createScaleMaintenanceNutrientFromValueAndPercent(
									detail.getNutrientStatementQuantity(), detail.getNutrientDailyValue()));
					break;
				}
				case IODINE : {
					scaleMaintenanceProduct.setIodine(
							this.createScaleMaintenanceNutrientFromValueAndPercent(
									detail.getNutrientStatementQuantity(), detail.getNutrientDailyValue()));
					break;
				}
				case IRON : {
					scaleMaintenanceProduct.setIron(
							this.createScaleMaintenanceNutrientFromValueAndPercent(
									detail.getNutrientStatementQuantity(), detail.getNutrientDailyValue()));
					break;
				}
				case MAGNESIUM : {
					scaleMaintenanceProduct.setMagnesium(
							this.createScaleMaintenanceNutrientFromValueAndPercent(
									detail.getNutrientStatementQuantity(), detail.getNutrientDailyValue()));
					break;
				}
				case NIACIN : {
					scaleMaintenanceProduct.setNiacin(
							this.createScaleMaintenanceNutrientFromValueAndPercent(
									detail.getNutrientStatementQuantity(), detail.getNutrientDailyValue()));
					break;
				}
				case PANTOTHENIC_ACID : {
					scaleMaintenanceProduct.setPantothenicAcid(
							this.createScaleMaintenanceNutrientFromValueAndPercent(
									detail.getNutrientStatementQuantity(), detail.getNutrientDailyValue()));
					break;
				}
				case PHOSPHORUS : {
					scaleMaintenanceProduct.setPhosphorus(
							this.createScaleMaintenanceNutrientFromValueAndPercent(
									detail.getNutrientStatementQuantity(), detail.getNutrientDailyValue()));
					break;
				}
				case POTASSIUM : {
					scaleMaintenanceProduct.setPotassium(
							this.createScaleMaintenanceNutrientFromValueAndPercent(
									detail.getNutrientStatementQuantity(), detail.getNutrientDailyValue()));
					break;
				}
				case PROTEIN : {
					scaleMaintenanceProduct.setProtein(
							this.createScaleMaintenanceNutrientFromValueAndPercent(
									detail.getNutrientStatementQuantity(), detail.getNutrientDailyValue()));
					break;
				}
				case RIBOFLAVIN : {
					scaleMaintenanceProduct.setRiboflavin(
							this.createScaleMaintenanceNutrientFromValueAndPercent(
									detail.getNutrientStatementQuantity(), detail.getNutrientDailyValue()));
					break;
				}
				case SATURATED_FAT : {
					scaleMaintenanceProduct.setSaturatedFat(
							this.createScaleMaintenanceNutrientFromValueAndPercent(
									detail.getNutrientStatementQuantity(), detail.getNutrientDailyValue()));
					break;
				}
				case SODIUM : {
					scaleMaintenanceProduct.setSodium(
							this.createScaleMaintenanceNutrientFromValueAndPercent(
									detail.getNutrientStatementQuantity(), detail.getNutrientDailyValue()));
					break;
				}
				case SUGAR_ALCOHOL : {
					scaleMaintenanceProduct.setSugarAlcohol(
							this.createScaleMaintenanceNutrientFromValueAndPercent(
									detail.getNutrientStatementQuantity(), detail.getNutrientDailyValue()));
					break;
				}
				case SUGARS : {
					scaleMaintenanceProduct.setSugar(
							this.createScaleMaintenanceNutrientFromValueAndPercent(
									detail.getNutrientStatementQuantity(), detail.getNutrientDailyValue()));
					break;
				}
				case THIAMINE : {
					scaleMaintenanceProduct.setThiamine(
							this.createScaleMaintenanceNutrientFromValueAndPercent(
									detail.getNutrientStatementQuantity(), detail.getNutrientDailyValue()));
					break;
				}
				case TOTAL_CARBOHYDRATE : {
					scaleMaintenanceProduct.setTotalCarbohydrates(
							this.createScaleMaintenanceNutrientFromValueAndPercent(
									detail.getNutrientStatementQuantity(), detail.getNutrientDailyValue()));
					break;
				}
				case TOTAL_FAT : {
					scaleMaintenanceProduct.setTotalFat(
							this.createScaleMaintenanceNutrientFromValueAndPercent(
									detail.getNutrientStatementQuantity(), detail.getNutrientDailyValue()));
					break;
				}
				case TRANS_FAT : {
					scaleMaintenanceProduct.setTransFat(
							this.createScaleMaintenanceNutrientFromValueAndPercent(
									detail.getNutrientStatementQuantity(), detail.getNutrientDailyValue()));
					break;
				}
				case VITAMIN_A : {
					scaleMaintenanceProduct.setVitaminA(
							this.createScaleMaintenanceNutrientFromValueAndPercent(
									detail.getNutrientStatementQuantity(), detail.getNutrientDailyValue()));
					break;
				}
				case VITAMIN_B1 : {
					scaleMaintenanceProduct.setVitaminB1(
							this.createScaleMaintenanceNutrientFromValueAndPercent(
									detail.getNutrientStatementQuantity(), detail.getNutrientDailyValue()));
					break;
				}
				case VITAMIN_B2 : {
					scaleMaintenanceProduct.setVitaminB2(
							this.createScaleMaintenanceNutrientFromValueAndPercent(
									detail.getNutrientStatementQuantity(), detail.getNutrientDailyValue()));
					break;
				}
				case VITAMIN_B6 : {
					scaleMaintenanceProduct.setVitaminB6(
							this.createScaleMaintenanceNutrientFromValueAndPercent(
									detail.getNutrientStatementQuantity(), detail.getNutrientDailyValue()));
					break;
				}
				case VITAMIN_B12 : {
					scaleMaintenanceProduct.setVitaminB12(
							this.createScaleMaintenanceNutrientFromValueAndPercent(
									detail.getNutrientStatementQuantity(), detail.getNutrientDailyValue()));
					break;
				}
				case VITAMIN_C : {
					scaleMaintenanceProduct.setVitaminC(
							this.createScaleMaintenanceNutrientFromValueAndPercent(
									detail.getNutrientStatementQuantity(), detail.getNutrientDailyValue()));
					break;
				}
				case VITAMIN_D : {
					scaleMaintenanceProduct.setVitaminD(
							this.createScaleMaintenanceNutrientFromValueAndPercent(
									detail.getNutrientStatementQuantity(), detail.getNutrientDailyValue()));
					break;
				}
				case VITAMIN_E :{
					scaleMaintenanceProduct.setVitaminE(
							this.createScaleMaintenanceNutrientFromValueAndPercent(
									detail.getNutrientStatementQuantity(), detail.getNutrientDailyValue()));
					break;
				}
				case ZINC : {
					scaleMaintenanceProduct.setZinc(
							this.createScaleMaintenanceNutrientFromValueAndPercent(
									detail.getNutrientStatementQuantity(), detail.getNutrientDailyValue()));
					break;
				}
				default: {
					logger.error(String.format(NUTRIENT_NOT_FOUND_MESSAGE, nutrientCode.getCode()));
					break;
				}
			}
		}
	}

	/**
	 * Creates and returns a new scale maintenance nutrient with the given value and percent.
	 *
	 * @param value Value to set on the nutrient.
	 * @param percent Percent to set on the nurtrient.
	 * @return New scale maintenance nutrient.
	 */
	private ScaleMaintenanceNutrient createScaleMaintenanceNutrientFromValueAndPercent(Double value, long percent) {
		return new ScaleMaintenanceNutrient(value, percent);
	}

	/**
	 * Sets ingredient information on a scale maintenance product given a scale upc.
	 *
	 * @param scaleMaintenanceProduct Scale maintenance product to set ingredient information on.
	 * @param scaleUpc Scale upc with the ingredient information.
	 */
	private void setIngredientInformation(ScaleMaintenanceProduct scaleMaintenanceProduct, ScaleUpc scaleUpc) {
	 //Start - Changes are done for Scale Load by Department by Joseph Williams, Swaroopa Rani
		if(scaleUpc.getIngredientStatement()  != 0 &&  scaleUpc.getIngredientStatementHeader() != null) {
	 //End - Changes are done for Scale Load by Department by Joseph Williams, Swaroopa Rani	
			scaleMaintenanceProduct.setIngredientText(scaleUpc.getIngredientStatementHeader().getIngredientsText());
		}
	}

	/**
	 * Sets PLU information on a scale maintenance product given a scale upc.
	 *
	 * @param scaleUpc Scale upc with the PLU information.
	 */
	private ScaleMaintenanceProduct setPLUInformation(ScaleUpc scaleUpc) {
		ScaleMaintenanceProduct scaleMaintenanceProduct = null;
		try{
			ProductMaster product =
					scaleUpc.getAssociateUpc().getSellingUnit().getProductMaster();
			if(product != null) {
				scaleMaintenanceProduct = new ScaleMaintenanceProduct();
				scaleMaintenanceProduct.setDepartment(product.getDepartmentCode());
				scaleMaintenanceProduct.setSubDepartment(product.getSubDepartmentCode());
			} else {
				throw new EntityNotFoundException(String.format(PRODUCT_NOT_FOUND_MESSAGE, scaleUpc.getUpc()));
			}
		} catch (EntityNotFoundException e){
			logger.error(e.getLocalizedMessage());
			return scaleMaintenanceProduct;
		}
		scaleMaintenanceProduct
				.setActionCode(scaleUpc.getActionCode())
				.setEatByDays(scaleUpc.getEatByDays())
				.setEnglishDescriptionOne(scaleUpc.getEnglishDescriptionOne())
				.setEnglishDescriptionTwo(scaleUpc.getEnglishDescriptionTwo())
				.setEnglishDescriptionThree(scaleUpc.getEnglishDescriptionThree())
				.setEnglishDescriptionFour(scaleUpc.getEnglishDescriptionFour())
				.setSpanishDescriptionOne(scaleUpc.getSpanishDescriptionOne())
				.setSpanishDescriptionTwo(scaleUpc.getSpanishDescriptionTwo())
				.setSpanishDescriptionThree(scaleUpc.getSpanishDescriptionThree())
				.setSpanishDescriptionFour(scaleUpc.getSpanishDescriptionFour())
				.setForceTare(scaleUpc.isForceTare())
				.setFreezeByDays(scaleUpc.getFreezeByDays())
				.setGrade(scaleUpc.getGrade())
				.setGraphicsCode(scaleUpc.getGraphicsCode())
				.setIngredientStatement(scaleUpc.getIngredientStatement())
				.setLabelFormatOne(scaleUpc.getLabelFormatOne())
				.setLabelFormatTwo(scaleUpc.getLabelFormatTwo())
				.setNetWeight(scaleUpc.getNetWeight())
				.setNutrientStatementCode(scaleUpc.getNutrientStatement())
				.setPrePackTare(scaleUpc.getPrePackTare())
				.setPriceOverride(scaleUpc.isPriceOverride())
				.setServiceCounterTare(scaleUpc.getServiceCounterTare())
				.setShelfLifeDays(scaleUpc.getShelfLifeDays())
				.setUpc(scaleUpc.getUpc())
				.setPlu(scaleUpc.getPlu());
		return scaleMaintenanceProduct;
	}

	/**
	 * Returns true if the provided label format is NLEA 2016 format.
	 *
	 * @param labelFormatCode the labelFormatCode.
	 * @return true if the provided label format is NLEA 2016 format.
	 */
	private boolean isNLEA2016LabelFormat(long labelFormatCode) {
		return NLEA_LABEL_FORMATS.contains(labelFormatCode);
	}

	/**
	 * Sets ScaleMaintenanceProduct nutrition values for the first NutrientStatementPanelHeader
	 * @param scaleMaintenanceProduct
	 * @param statementPanelHeader
	 */
	private void setNutrition(ScaleMaintenanceProduct scaleMaintenanceProduct, NutrientStatementPanelHeader statementPanelHeader){
		scaleMaintenanceProduct.setCalories(new ScaleMaintenanceNutrient(statementPanelHeader.getNutrientPanelColumnHeaders().get(0).getCaloriesQuantity(), 0L));
		for(NutrientPanelColumnHeader nutrientPanelColumnHeader : statementPanelHeader.getNutrientPanelColumnHeaders()) {
			if(nutrientPanelColumnHeader.getKey().getNutrientPanelColumnId() == NutrientPanelColumnHeader.SINGLE_COLUMN_HEADER_ID) {
				this.setNutritionValuesByNutrientPanelDetailCode(scaleMaintenanceProduct, nutrientPanelColumnHeader.getNutrientPanelDetails());
			}
		}
	}

	/**
	 * Sets nutrition values on a given scale maintenance product based on a list of nutrient panel details.
	 *
	 *  @param scaleMaintenanceProduct Scale maintenance product to set nutrition values on.
	 * @param details List of nutrient panel details.
	 */
	private void setNutritionValuesByNutrientPanelDetailCode(ScaleMaintenanceProduct scaleMaintenanceProduct, List<NutrientPanelDetail> details) {
		Nutrient.Codes nutrientCode;
		double nutrientQuantity;
		long nutrientDailyValue;
		for(NutrientPanelDetail detail : details){
			nutrientCode = Nutrient.Codes.valueOf(Long.valueOf(detail.getNutrient().getSourceSystemReferenceId()));
			nutrientQuantity = detail.getNutrientQuantity() == null ? 0 : detail.getNutrientQuantity();
			nutrientDailyValue = detail.getNutrientDailyValue() == null ? 0 : detail.getNutrientDailyValue().longValue();
			switch (nutrientCode){
				case ASCORBIC_ACID : {
					scaleMaintenanceProduct.setAscorbicAcid(
							this.createScaleMaintenanceNutrientFromValueAndPercent(
									nutrientQuantity, nutrientDailyValue));
					break;
				}
				case BIOTIN : {
					scaleMaintenanceProduct.setBiotin(
							this.createScaleMaintenanceNutrientFromValueAndPercent(
									nutrientQuantity, nutrientDailyValue));
					break;
				}
				case CALCIUM : {
					scaleMaintenanceProduct.setCalcium(
							this.createScaleMaintenanceNutrientFromValueAndPercent(
									nutrientQuantity, nutrientDailyValue));
					break;
				}
				case CALORIES_FROM_FAT : {
					scaleMaintenanceProduct.setCaloriesFromFat(
							this.createScaleMaintenanceNutrientFromValueAndPercent(
									nutrientQuantity, nutrientDailyValue));
					break;
				}
				case CHOLESTEROL : {
					scaleMaintenanceProduct.setCholesterol(
							this.createScaleMaintenanceNutrientFromValueAndPercent(
									nutrientQuantity, nutrientDailyValue));
					break;
				}
				case COPPER : {
					scaleMaintenanceProduct.setCopper(
							this.createScaleMaintenanceNutrientFromValueAndPercent(
									nutrientQuantity, nutrientDailyValue));
					break;
				}
				case DIETARY_FIBER : {
					scaleMaintenanceProduct.setDietaryFiber(
							this.createScaleMaintenanceNutrientFromValueAndPercent(
									nutrientQuantity, nutrientDailyValue));
					break;
				}
				case FOLACIN : {
					scaleMaintenanceProduct.setFolacin(
							this.createScaleMaintenanceNutrientFromValueAndPercent(
									nutrientQuantity, nutrientDailyValue));
					break;
				}
				case FOLIC_ACID : {
					scaleMaintenanceProduct.setFolicAcid(
							this.createScaleMaintenanceNutrientFromValueAndPercent(
									nutrientQuantity, nutrientDailyValue));
					break;
				}
				case IODINE : {
					scaleMaintenanceProduct.setIodine(
							this.createScaleMaintenanceNutrientFromValueAndPercent(
									nutrientQuantity, nutrientDailyValue));
					break;
				}
				case IRON : {
					scaleMaintenanceProduct.setIron(
							this.createScaleMaintenanceNutrientFromValueAndPercent(
									nutrientQuantity, nutrientDailyValue));
					break;
				}
				case MAGNESIUM : {
					scaleMaintenanceProduct.setMagnesium(
							this.createScaleMaintenanceNutrientFromValueAndPercent(
									nutrientQuantity, nutrientDailyValue));
					break;
				}
				case NIACIN : {
					scaleMaintenanceProduct.setNiacin(
							this.createScaleMaintenanceNutrientFromValueAndPercent(
									nutrientQuantity, nutrientDailyValue));
					break;
				}
				case PANTOTHENIC_ACID : {
					scaleMaintenanceProduct.setPantothenicAcid(
							this.createScaleMaintenanceNutrientFromValueAndPercent(
									nutrientQuantity, nutrientDailyValue));
					break;
				}
				case PHOSPHORUS : {
					scaleMaintenanceProduct.setPhosphorus(
							this.createScaleMaintenanceNutrientFromValueAndPercent(
									nutrientQuantity, nutrientDailyValue));
					break;
				}
				case POTASSIUM : {
					scaleMaintenanceProduct.setPotassium(
							this.createScaleMaintenanceNutrientFromValueAndPercent(
									nutrientQuantity, nutrientDailyValue));
					break;
				}
				case PROTEIN : {
					scaleMaintenanceProduct.setProtein(
							this.createScaleMaintenanceNutrientFromValueAndPercent(
									nutrientQuantity, nutrientDailyValue));
					break;
				}
				case RIBOFLAVIN : {
					scaleMaintenanceProduct.setRiboflavin(
							this.createScaleMaintenanceNutrientFromValueAndPercent(
									nutrientQuantity, nutrientDailyValue));
					break;
				}
				case SATURATED_FAT : {
					scaleMaintenanceProduct.setSaturatedFat(
							this.createScaleMaintenanceNutrientFromValueAndPercent(
									nutrientQuantity, nutrientDailyValue));
					break;
				}
				case SODIUM : {
					scaleMaintenanceProduct.setSodium(
							this.createScaleMaintenanceNutrientFromValueAndPercent(
									nutrientQuantity, nutrientDailyValue));
					break;
				}
				case SUGAR_ALCOHOL : {
					scaleMaintenanceProduct.setSugarAlcohol(
							this.createScaleMaintenanceNutrientFromValueAndPercent(
									nutrientQuantity, nutrientDailyValue));
					break;
				}
				case SUGARS : {
					scaleMaintenanceProduct.setSugar(
							this.createScaleMaintenanceNutrientFromValueAndPercent(
									nutrientQuantity, nutrientDailyValue));
					break;
				}
				case THIAMINE : {
					scaleMaintenanceProduct.setThiamine(
							this.createScaleMaintenanceNutrientFromValueAndPercent(
									nutrientQuantity, nutrientDailyValue));
					break;
				}
				case TOTAL_CARBOHYDRATE : {
					scaleMaintenanceProduct.setTotalCarbohydrates(
							this.createScaleMaintenanceNutrientFromValueAndPercent(
									nutrientQuantity, nutrientDailyValue));
					break;
				}
				case TOTAL_FAT : {
					scaleMaintenanceProduct.setTotalFat(
							this.createScaleMaintenanceNutrientFromValueAndPercent(
									nutrientQuantity, nutrientDailyValue));
					break;
				}
				case TRANS_FAT : {
					scaleMaintenanceProduct.setTransFat(
							this.createScaleMaintenanceNutrientFromValueAndPercent(
									nutrientQuantity, nutrientDailyValue));
					break;
				}
				case VITAMIN_A : {
					scaleMaintenanceProduct.setVitaminA(
							this.createScaleMaintenanceNutrientFromValueAndPercent(
									nutrientQuantity, nutrientDailyValue));
					break;
				}
				case VITAMIN_B1 : {
					scaleMaintenanceProduct.setVitaminB1(
							this.createScaleMaintenanceNutrientFromValueAndPercent(
									nutrientQuantity, nutrientDailyValue));
					break;
				}
				case VITAMIN_B2 : {
					scaleMaintenanceProduct.setVitaminB2(
							this.createScaleMaintenanceNutrientFromValueAndPercent(
									nutrientQuantity, nutrientDailyValue));
					break;
				}
				case VITAMIN_B6 : {
					scaleMaintenanceProduct.setVitaminB6(
							this.createScaleMaintenanceNutrientFromValueAndPercent(
									nutrientQuantity, nutrientDailyValue));
					break;
				}
				case VITAMIN_B12 : {
					scaleMaintenanceProduct.setVitaminB12(
							this.createScaleMaintenanceNutrientFromValueAndPercent(
									nutrientQuantity, nutrientDailyValue));
					break;
				}
				case VITAMIN_C : {
					scaleMaintenanceProduct.setVitaminC(
							this.createScaleMaintenanceNutrientFromValueAndPercent(
									nutrientQuantity, nutrientDailyValue));
					break;
				}
				case VITAMIN_D : {
					scaleMaintenanceProduct.setVitaminD(
							this.createScaleMaintenanceNutrientFromValueAndPercent(
									nutrientQuantity, nutrientDailyValue));
					break;
				}
				case VITAMIN_E :{
					scaleMaintenanceProduct.setVitaminE(
							this.createScaleMaintenanceNutrientFromValueAndPercent(
									nutrientQuantity, nutrientDailyValue));
					break;
				}
				case ZINC : {
					scaleMaintenanceProduct.setZinc(
							this.createScaleMaintenanceNutrientFromValueAndPercent(
									nutrientQuantity, nutrientDailyValue));
					break;
				}
				case ADDED_SUGARS: {
					scaleMaintenanceProduct.setAddedSugars(this.createScaleMaintenanceNutrientFromValueAndPercent(
							nutrientQuantity, nutrientDailyValue));
					break;
				}
				default: {
					logger.error(String.format(NUTRIENT_NOT_FOUND_MESSAGE, nutrientCode.getCode()));
					break;
				}
			}
		}
	}
}
