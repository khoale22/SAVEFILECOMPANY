/*
 * NutritionValidator
 *
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
package com.heb.pm.batchUpload.nutritionIngredients;

import com.heb.pm.batchUpload.AbstractBatchUploadValidator;
import com.heb.pm.batchUpload.BatchUpload;
import com.heb.pm.batchUpload.UnexpectedInputException;
import com.heb.pm.batchUpload.util.Constants;
import com.heb.pm.entity.ServingSizeUOM;
import com.heb.pm.entity.SourceSystem;
import com.heb.pm.repository.ServingSizeUOMRepository;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

/**
 * the validator for nutrition upload
 *
 * @author vn87351
 * @since 2.12.0
 */
@Component
public class NutritionValidator extends AbstractBatchUploadValidator {
	private static final Logger LOGGER = LoggerFactory.getLogger(NutritionValidator.class);
	public static final String ERROR_UPC_MANDATORY_FIELD = "UPC is a mandatory field";
	public static final String ERROR_INVALID_UPC = "Invalid Upc %s.";
	protected static final String ERROR_VALIDATE_UOM_CODE = "Serving size UOM code not found for value: %s";
	private static final String ERROR_SERVING_SIZE_WRONG_TYPE = "Serving size is of wrong type, value: %s";
	private static final String ERROR_SERVING_SIZE_PER_CONTAINER_INVALID_NUMBER = "Invalid number for Servings Per Container: %s";
	private static final String ERROR_SERVING_SIZE_PER_CONTAINER_TEXT_TOO_LARGE = "Servings Per Container Text is too Large: %s";
	private static final String ERROR_TOTAL_CALORIES_WRONG_TYPE = "Total calories is of wrong type, value: %s";
	private static final String ERROR_TOTAL_CALORIES_FROM_FAT_WRONG_TYPE = "Calories from fat is of wrong type, value: %s";
	private static final String ERROR_TOTAL_FAT_WRONG_TYPE = "Total fat is of wrong type, value: %s";
	private static final String ERROR_TOTAL_FAT_PERCENT_WRONG_TYPE = "Total fat percent is of wrong type, value: %s";
	private static final String ERROR_SATURATED_FAT_WRONG_TYPE = "Saturated fat is of wrong type, value: %s";
	private static final String ERROR_SATURATED_FAT_PERCENT_WRONG_TYPE = "Saturated fat percent is of wrong type, value: %s";
	private static final String ERROR_TRANS_FAT_PERCENT_WRONG_TYPE = "Trans fat percent is of wrong type, value: %s";
	private static final String ERROR_CHOLESTEROL_WRONG_TYPE = "Cholesterol is of wrong type, value: %s";
	private static final String ERROR_CHOLESTEROL_PERCENT_WRONG_TYPE = "Cholesterol percent is of wrong type, value: %s";
	private static final String ERROR_SODIUM_WRONG_TYPE = "Sodium is of wrong type, value: %s";
	private static final String ERROR_SODIUM_PERCENT_WRONG_TYPE = "Sodium percent is of wrong type, value: %s";
	private static final String ERROR_POTASSIUM_WRONG_TYPE = "Potassium is of wrong type, value: %s";
	private static final String ERROR_TOTAL_CARBOHYDRATES_WRONG_TYPE = "Total carbohydrates is of wrong type, value: %s";
	private static final String ERROR_TOTAL_CARBOHYDRATES_PERCENT_WRONG_TYPE = "Total carbohydrates percent is of wrong type, value: %s";
	private static final String ERROR_DIETARY_FIBER_WRONG_TYPE = "Dietary fiber is of wrong type, value: %s";
	private static final String ERROR_DIETARY_FIBER_PERCENT_WRONG_TYPE = "Dietary fiber percent is of wrong type, value: %s";
	private static final String ERROR_SUGAR_WRONG_TYPE = "Sugars is of wrong type, value: %s";
	private static final String ERROR_PROTEIN_WRONG_TYPE = "Protein is of wrong type, value: %s";
	private static final String ERROR_VITAMIN_A_WRONG_TYPE = "Vitamin A is of wrong type, value: %s";
	private static final String ERROR_VITAMIN_C_WRONG_TYPE = "Vitamin C is of wrong type, value: %s";
	private static final String ERROR_CALCIUM_WRONG_TYPE = "Calcium is of wrong type, value: %s";
	private static final String ERROR_IRON_WRONG_TYPE = "Iron is of wrong type, value: %s";

	@Autowired
	ServingSizeUOMRepository servingSizeUOMRepository;
	private static final int DEFAULT_SHEET = 0;
	private static final int DEFAULT_ROW_BEGIN_READ_DATA = 0;
	private NutritionBatchUpload nutritionBatchUpload;

	@Override
	public void validateRow(BatchUpload batchUpload) {
		nutritionBatchUpload = (NutritionBatchUpload) batchUpload;
		runValidation();
	}

	/**
	 * run validation for nutrition
	 */
	private void runValidation(){
		validateUpc();
		validateServingSize();
		validateServingSizeUOM();
		validateServingsPerContainer();
		validateTotalCalories();
		validateCaloriesFromFat();
		validateTotalFat();
		validateTotalFatDV();
		validateSaturatedFat();
		validateSaturatedFatDV();
		validateTransFat();
		validateCholesterol();
		validateCholesterolDV();
		validateSodium();
		validateSodiumDV();
		validatePotassium();
		validateTotalCarbohydrates();
		validateTotalCarbohydratesDV();
		validateDietaryFiber();
		validateDietaryFiberDV();
		validateSugars();
		validateProtein();
		validateVitaminA();
		validateVitaminC();
		validatetCalcium();
		validateIron();
	}

	/**
	 * validate the upc
	 */
	private void validateUpc(){
		if (StringUtils.isBlank(nutritionBatchUpload.getUpcPlu())) {
			nutritionBatchUpload.getErrors().add(ERROR_UPC_MANDATORY_FIELD);
			throw new UnexpectedInputException(ERROR_UPC_MANDATORY_FIELD);
		}else if (!isUpc(nutritionBatchUpload.getUpcPlu())) {
			String errorMess=String.format(ERROR_INVALID_UPC, nutritionBatchUpload.getUpcPlu());
			nutritionBatchUpload.getErrors().add(errorMess);
			throw new UnexpectedInputException(errorMess);
		}
	}

	/**
	 * validate the serving size
	 */
	private void validateServingSize(){
		if(StringUtils.isNotBlank(nutritionBatchUpload.getServingSize()) && !isDouble(nutritionBatchUpload.getServingSize())){
			nutritionBatchUpload.getErrors().add(ERROR_SERVING_SIZE_WRONG_TYPE);
		}
	}

	/**
	 * validate the serving size uom by description on file upload
	 */
	private void validateServingSizeUOM(){
		if (StringUtils.isNotBlank(nutritionBatchUpload.getServingSizeUOM())) {
			List<ServingSizeUOM> uom = servingSizeUOMRepository.
					findBySourceSystemAndServingSizeUomDescription(SourceSystem.SOURCE_SYSTEM_GLADSON, nutritionBatchUpload.getServingSizeUOM().trim());
			if (uom == null || uom.isEmpty()) {
				nutritionBatchUpload.getErrors().add(String.format(ERROR_VALIDATE_UOM_CODE,
						nutritionBatchUpload.getServingSizeUOM().trim()));
			}
		}
	}

	/**
	 * validate serving size per container
	 */
	private void validateServingsPerContainer(){
		if(!StringUtils.isBlank(nutritionBatchUpload.getServingsPerContainer())){
			if(isDouble(nutritionBatchUpload.getServingsPerContainer())
					&& !checkMaxLengthsOfDecimals(nutritionBatchUpload.getServingsPerContainer(), Constants.NUM_5, Constants.NUM_4)){
				nutritionBatchUpload.getErrors().add(
						String.format(ERROR_SERVING_SIZE_PER_CONTAINER_INVALID_NUMBER, nutritionBatchUpload.getServingsPerContainer()));
			}
			if (nutritionBatchUpload.getServingsPerContainer().length() > Constants.NUM_50) {
				nutritionBatchUpload.getErrors().add(String.format(
						ERROR_SERVING_SIZE_PER_CONTAINER_TEXT_TOO_LARGE, nutritionBatchUpload.getServingsPerContainer()));
			}
		}
	}

	/**
	 * validate total calories
	 */
	private void validateTotalCalories(){
		if(!StringUtils.isBlank(nutritionBatchUpload.getTotalCalories())
				&& !isDouble(nutritionBatchUpload.getTotalCalories())){
			nutritionBatchUpload.getErrors().add(
					String.format(ERROR_TOTAL_CALORIES_WRONG_TYPE, nutritionBatchUpload.getTotalCalories()));
		}
	}

	/**
	 * validate calories from fat
	 */
	private void validateCaloriesFromFat(){
		if(!StringUtils.isBlank(nutritionBatchUpload.getCaloriesFromFat())
				&& !isDouble(nutritionBatchUpload.getCaloriesFromFat())){
			nutritionBatchUpload.getErrors().add(
					String.format(ERROR_TOTAL_CALORIES_FROM_FAT_WRONG_TYPE, nutritionBatchUpload.getCaloriesFromFat()));
		}
	}

	/**
	 * validate total fat
	 */
	private void validateTotalFat(){
		if(!StringUtils.isBlank(nutritionBatchUpload.getTotalFat())
				&& !isDouble(nutritionBatchUpload.getTotalFat())){
			nutritionBatchUpload.getErrors().add(
					String.format(ERROR_TOTAL_FAT_WRONG_TYPE, nutritionBatchUpload.getTotalFat()));
		}
	}

	/**
	 * validate total fat percen
	 */
	private void validateTotalFatDV(){
		if(!StringUtils.isBlank(nutritionBatchUpload.getTotalFatDV())
				&& !isDouble(nutritionBatchUpload.getTotalFatDV())){
			nutritionBatchUpload.getErrors().add(
					String.format(ERROR_TOTAL_FAT_PERCENT_WRONG_TYPE, nutritionBatchUpload.getTotalFatDV()));
		}
	}

	/**
	 * validate saturated fat
	 */
	private void validateSaturatedFat(){
		if(!StringUtils.isBlank(nutritionBatchUpload.getSaturatedFat())
				&& !isDouble(nutritionBatchUpload.getSaturatedFat())){
			nutritionBatchUpload.getErrors().add(
					String.format(ERROR_SATURATED_FAT_WRONG_TYPE, nutritionBatchUpload.getSaturatedFat()));
		}
	}

	/**
	 * validate saturated fat percent
	 */
	private void validateSaturatedFatDV(){
		if(!StringUtils.isBlank(nutritionBatchUpload.getSaturatedFatDV())
				&& !isDouble(nutritionBatchUpload.getSaturatedFatDV())){
			nutritionBatchUpload.getErrors().add(
					String.format(ERROR_SATURATED_FAT_PERCENT_WRONG_TYPE, nutritionBatchUpload.getSaturatedFatDV()));
		}
	}

	/**
	 * validate trans fat
	 */
	private void validateTransFat(){
		if(!StringUtils.isBlank(nutritionBatchUpload.getTransFat())
				&& !isDouble(nutritionBatchUpload.getTransFat())){
			nutritionBatchUpload.getErrors().add(
					String.format(ERROR_TRANS_FAT_PERCENT_WRONG_TYPE, nutritionBatchUpload.getTransFat()));
		}
	}

	/**
	 * validate cholesterol
	 */
	private void validateCholesterol(){
		if(!StringUtils.isBlank(nutritionBatchUpload.getCholesterol())
				&& !isDouble(nutritionBatchUpload.getCholesterol())){
			nutritionBatchUpload.getErrors().add(
					String.format(ERROR_CHOLESTEROL_WRONG_TYPE, nutritionBatchUpload.getCholesterol()));
		}
	}

	/**
	 * validate cholesterol percent
	 */
	private void validateCholesterolDV(){
		if(!StringUtils.isBlank(nutritionBatchUpload.getCholesterolDV())
				&& !isDouble(nutritionBatchUpload.getCholesterolDV())){
			nutritionBatchUpload.getErrors().add(
					String.format(ERROR_CHOLESTEROL_PERCENT_WRONG_TYPE, nutritionBatchUpload.getCholesterolDV()));
		}
	}

	/**
	 * validate sodium
	 */
	private void validateSodium(){
		if(!StringUtils.isBlank(nutritionBatchUpload.getSodium())
				&& !isDouble(nutritionBatchUpload.getSodium())){
			nutritionBatchUpload.getErrors().add(
					String.format(ERROR_SODIUM_WRONG_TYPE, nutritionBatchUpload.getSodium()));
		}
	}

	/**
	 * validate sodium percent
 	 */
	private void validateSodiumDV(){
		if(!StringUtils.isBlank(nutritionBatchUpload.getSodiumDV())
				&& !isDouble(nutritionBatchUpload.getSodiumDV())){
			nutritionBatchUpload.getErrors().add(
					String.format(ERROR_SODIUM_PERCENT_WRONG_TYPE, nutritionBatchUpload.getSodiumDV()));
		}
	}

	/**
	 * validate potassium
	 */
	private void validatePotassium(){
		if(!StringUtils.isBlank(nutritionBatchUpload.getPotassium())
				&& !isDouble(nutritionBatchUpload.getPotassium())){
			nutritionBatchUpload.getErrors().add(
					String.format(ERROR_POTASSIUM_WRONG_TYPE, nutritionBatchUpload.getPotassium()));
		}
	}

	/**
	 * validate total carbohydrates
	 */
	private void validateTotalCarbohydrates(){
		if(!StringUtils.isBlank(nutritionBatchUpload.getTotalCarbohydrates())
				&& !isDouble(nutritionBatchUpload.getTotalCarbohydrates())){
			nutritionBatchUpload.getErrors().add(
					String.format(ERROR_TOTAL_CARBOHYDRATES_WRONG_TYPE, nutritionBatchUpload.getTotalCarbohydrates()));
		}
	}

	/**
	 * validate total carbohydrates percent
	 */
	private void validateTotalCarbohydratesDV(){
		if(!StringUtils.isBlank(nutritionBatchUpload.getTotalCarbohydratesDV())
				&& !isDouble(nutritionBatchUpload.getTotalCarbohydratesDV())){
			nutritionBatchUpload.getErrors().add(
					String.format(ERROR_TOTAL_CARBOHYDRATES_PERCENT_WRONG_TYPE, nutritionBatchUpload.getTotalCarbohydratesDV()));
		}
	}

	/**
	 * validate dietary fiber
	 */
	private void validateDietaryFiber(){
		if(!StringUtils.isBlank(nutritionBatchUpload.getDietaryFiber())
				&& !isDouble(nutritionBatchUpload.getDietaryFiber())){
			nutritionBatchUpload.getErrors().add(
					String.format(ERROR_DIETARY_FIBER_WRONG_TYPE, nutritionBatchUpload.getDietaryFiber()));
		}
	}

	/**
	 * validate dietary fiber percent
	 */
	private void validateDietaryFiberDV(){
		if(!StringUtils.isBlank(nutritionBatchUpload.getDietaryFiberDV())
				&& !isDouble(nutritionBatchUpload.getDietaryFiberDV())){
			nutritionBatchUpload.getErrors().add(
					String.format(ERROR_DIETARY_FIBER_PERCENT_WRONG_TYPE, nutritionBatchUpload.getDietaryFiberDV()));
		}
	}

	/**
	 * validate sugars
	 */
	private void validateSugars(){
		if(!StringUtils.isBlank(nutritionBatchUpload.getSugars())
				&& !isDouble(nutritionBatchUpload.getSugars())){
			nutritionBatchUpload.getErrors().add(
					String.format(ERROR_SUGAR_WRONG_TYPE, nutritionBatchUpload.getSugars()));
		}
	}

	/**
	 * validate protein
	 */
	private void validateProtein(){
		if(!StringUtils.isBlank(nutritionBatchUpload.getProtein())
				&& !isDouble(nutritionBatchUpload.getProtein())){
			nutritionBatchUpload.getErrors().add(
					String.format(ERROR_PROTEIN_WRONG_TYPE, nutritionBatchUpload.getProtein()));
		}
	}
	/**
	 * validate vitamin a
	 */
	private void validateVitaminA(){
		if(!StringUtils.isBlank(nutritionBatchUpload.getVitaminA())
				&& !isDouble(nutritionBatchUpload.getVitaminA())){
			nutritionBatchUpload.getErrors().add(
					String.format(ERROR_VITAMIN_A_WRONG_TYPE, nutritionBatchUpload.getVitaminA()));
		}
	}

	/**
	 * validate vitamin c
	 */
	private void validateVitaminC(){
		if(!StringUtils.isBlank(nutritionBatchUpload.getVitaminC())
				&& !isDouble(nutritionBatchUpload.getVitaminC())){
			nutritionBatchUpload.getErrors().add(
					String.format(ERROR_VITAMIN_C_WRONG_TYPE, nutritionBatchUpload.getVitaminC()));
		}
	}

	/**
	 * validate calcium
	 */
	private void validatetCalcium(){
		if(!StringUtils.isBlank(nutritionBatchUpload.getCalcium())
				&& !isDouble(nutritionBatchUpload.getCalcium())){
			nutritionBatchUpload.getErrors().add(
					String.format(ERROR_CALCIUM_WRONG_TYPE, nutritionBatchUpload.getCalcium()));
		}
	}

	/**
	 * validate iron
	 */
	private void validateIron(){
		if(!StringUtils.isBlank(nutritionBatchUpload.getIron())
				&& !isDouble(nutritionBatchUpload.getIron())){
			nutritionBatchUpload.getErrors().add(
					String.format(ERROR_IRON_WRONG_TYPE, nutritionBatchUpload.getIron()));
		}
	}

	/**
	 * Checks to see if the string number is within the permissible size in
	 * respect to the integerPartMax and fractionalPartMax
	 *
	 * @param value
	 *            - string value to be compared
	 * @param integerPartMax
	 *            - max allowed size for integer side
	 * @param fractionalPartMax
	 *            - max size allowed for decimal side
	 * @return true if the value is within the permissible size in respect to
	 *         the integerPartMax and fractionalPartMax
	 */

	public static boolean checkMaxLengthsOfDecimals(String value, int integerPartMax, int fractionalPartMax) {
		if (NumberUtils.isNumber(value)) {
			String[] strArray = value.split("\\.");
			if (strArray[0].length() > integerPartMax) {
				return false;
			} else if (strArray.length > 1 && (strArray[1].length() > fractionalPartMax)) {
				return false;
			} else {
				return true;
			}
		} else {
			return false;
		}
	}
	/**
	 * Validate File Upload.
	 * @param data
	 *            the byte[]
	 * @throws UnexpectedInputException
	 *        The UnexpectedInputException
	 * @author vn87351
	 */
	public void validateTemplate(byte[] data) throws UnexpectedInputException{
		try {
			InputStream inputStream = new ByteArrayInputStream(data);
			Workbook workBook = new XSSFWorkbook(inputStream);
			int numberOfSheets = workBook.getNumberOfSheets();
			if (numberOfSheets > 0) {
				Row row = workBook.getSheetAt(DEFAULT_SHEET).getRow(DEFAULT_ROW_BEGIN_READ_DATA);
				String header;
				for (int columnCounter = 0; columnCounter < row.getLastCellNum(); columnCounter++) {
					header =StringUtils.trimToEmpty(getValueOfCell(row.getCell(columnCounter)));
					if(NutritionBatchUpload.uploadFileHeader.containsKey(columnCounter) && !NutritionBatchUpload.uploadFileHeader.get(columnCounter).equalsIgnoreCase(header)){
						throw new UnexpectedInputException(AbstractBatchUploadValidator.ERROR_FILE_WRONG_FORMAT);
					}
				}
			}
		}catch (Exception e) {
			LOGGER.error("error",e);
			throw new UnexpectedInputException(AbstractBatchUploadValidator.ERROR_FILE_WRONG_FORMAT);
		}
	}

}
