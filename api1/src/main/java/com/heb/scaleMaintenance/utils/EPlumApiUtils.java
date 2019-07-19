package com.heb.scaleMaintenance.utils;

import com.heb.scaleMaintenance.entity.ScaleMaintenanceAuthorizeRetail;
import com.heb.scaleMaintenance.entity.ScaleMaintenanceUpc;
import com.heb.scaleMaintenance.model.*;
import com.heb.util.jpa.SwitchToBooleanConverter;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Utilities for EPlum API.
 *
 * @author m314029
 * @since 2.17.8
 */
@Component
public class EPlumApiUtils {

	private static final Logger logger = LoggerFactory.getLogger(EPlumApiUtils.class);

	// error messages
	private static final String EPLUM_DEPARTMENT_NOT_FOUND_ERROR = "EPlum department not found for department: %s," +
			"subDepartment: %s.";
	private static final String EPLUM_DESCRIPTION_NOT_FOUND_ERROR = "EPlum description not found for store: %s," +
			"line number: %s, for scale product: %s.";
	private static final String EPLUM_DECIMAL_FORMAT_NOT_FOUND_ERROR = "No decimal format found for %d characters.";
	private static final String EPLUM_DECIMAL_PRECISION_NOT_FOUND_ERROR = "Double precision not specified for " +
			"precision: %d.";
	private final static String UNIT_OF_MEASURE_NOT_FOUND_ERROR = "Unit of Measure could not be defined for upc: %d ," +
			"store: %d";
   //Start - Changes are done for Scale Load by Department by Joseph Williams, Swaroopa Rani
	// bilingual stores for ePlum
	private static final List<Integer> bilingualStores = Arrays.asList(1,2,9,11,13,17,22,26,30,38,42,43,45,47,51,52,54,
			62,64,66,70,75,84,87,88,94,95,107,117,118,138,141,150,151,158,161,172,183,186,187,189,190,192,200,205,207,
			208,211,214,216,217,219,226,231,237,238,240,255,257,263,267,279,283,286,288,291,304,307,315,316,319,320,
			324,328,331,332,334,335,360,364,370,374,379,382,387,399,411,417,418,419,421,423,427,429,438,439,440,448,
			449,460,462,465,466,467,485,489,492,540,553,556,559,575,577,595,616,626,640,643,644,645,646,648,649,662,667,731,753);
   //End - Changes are done for Scale Load by Department by Joseph Williams, Swaroopa Rani

	// list of lab stores
	public static final List<Integer> LAB_STORES = Arrays.asList(838,891);
	// ePlum message related constants
	private static final String EPLUM_MESSAGE_DELIMITER = ",";
	private static final String EPLUM_MESSAGE_CHUNK_DELIMITER = ";";
	private static final String GIVEN_NUMBER_ZERO_PADDED_FORMAT_PREFIX = "%0";
	private static final String TWO_NUMBER_ZERO_PADDED_FORMAT = "%02d";
	private static final String THREE_NUMBER_ZERO_PADDED_FORMAT = "%03d";
	private static final String FOUR_NUMBER_ZERO_PADDED_FORMAT = "%04d";
	private static final String FIVE_NUMBER_ZERO_PADDED_FORMAT = "%05d";
	private static final String SIX_NUMBER_ZERO_PADDED_FORMAT = "%06d";
	private static final String ELEVEN_NUMBER_ZERO_PADDED_FORMAT = "%011d";
	private static final String THIRTEEN_NUMBER_ZERO_PADDED_FORMAT = "%013d";
	private static final String ONE_ENUM_FORMAT = "%s";
	private static final String TWO_ENUMS_FORMAT = "%s%s";
	private static final String EPLUM_BATCH_NAME_PREFIX = "BATCH";
	private static final String EPLUM_BATCH_NAME_DATE_FORMAT = "MMddyy";
	private static final String EPLUM_BATCH_EFFECTIVE_DATE_FORMAT = "yyyyMMdd";
	private static final int SIX_STRING_LENGTH = 6;
	private static final String ZERO_CHARACTER = "0";
	private static final String DECIMAL_REGEX_CHARACTER = "d";
	private static final int DESCRIPTION_LINE_1 = 1;
	private static final int DESCRIPTION_LINE_2 = 2;
	private static final int DESCRIPTION_LINE_3 = 3;
	private static final int DESCRIPTION_LINE_4 = 4;
	private static final String DESCRIPTION_SIZE = "5";
	private static final int DOUBLE_PRECISION_ONE_MULTIPLIER = 10;
	private static final int DOUBLE_PRECISION_TWO_MULTIPLIER = 100;
	private static final int DOUBLE_PRECISION_THREE_MULTIPLIER = 1000;
	private static final int DOUBLE_PRECISION_ONE = 1;
	private static final int DOUBLE_PRECISION_TWO = 2;
	private static final int DOUBLE_PRECISION_THREE = 3;
	private static final DecimalFormat sixCharacterDecimalFormat = new DecimalFormat("000000");
	private static final DecimalFormat fiveCharacterDecimalFormat = new DecimalFormat("00000");
	private static final DecimalFormat fourCharacterDecimalFormat = new DecimalFormat("0000");
	private static final String NUTRIENT_SERVING_SIZE_DESCRIPTION_FORMAT = "%s%s(%s%s)";
	private static final String STRING_BLOCK_START = "<";
	private static final String STRING_BLOCK_END = ">";
	private static final String TWELVE_THIRTY_AM_STRING = "003000";
	private static final DateTimeFormatter SIX_CHARACTER_TIME_FORMAT = DateTimeFormatter.ofPattern("HHmmss");

	// product departments used for ePlum
	private static final String PRODUCT_GENERAL_MERCHANDISE_DEPT = "12";
	private static final String PRODUCT_KOSHER_DEPT = "13";
	private static final String PRODUCT_CHEF_DEPT = "15";
	private static final String PRODUCT_HEALTHY_LIVING_DEPT = "16";
	private static final String PRODUCT_MARKET_DEPT = "02";
	private static final String PRODUCT_BAKERY_DEPT = "03";
	private static final String PRODUCT_DELI_DEPT = "06";
	private static final String PRODUCT_GROCERY_DEPT = "07";
	private static final String PRODUCT_PRODUCE_DEPT = "09";
	//Start - Changes are done for Scale Load by Department by Joseph Williams, Swaroopa Rani
	private static final String PRODUCT_BEAUTY_DEPT = "04";
	private static final String PRODUCT_GREATS = "14";
	private static final String PRODUCT_CHICKEN = "19";


	
	private static final String PRODUCT_BEAUTY_SUB_DEPT = "C";
	private static final String PRODUCT_BASIC_GM = "A";
	private static final String PRODUCT_TEXAS_BACKYARD_HARD_GOODS = "H";
	private static final String PRODUCT_TEXAS_BACKYARD_LIVE_GOODS = "L";
	
	//End - Changes are done for Scale Load by Department by Joseph Williams, Swaroopa Rani

    // product sub departments used for ePlum
	private static final String PRODUCT_CANDY_SUB_DEPT = "C";
	private static final String PRODUCT_CHINESE_SUB_DEPT = "C";
	private static final String PRODUCT_BULK_FOODS_SUB_DEPT = "H";

	// fraction strings
	private static final String ONE_TWELFTH_STRING = "1/1";
	private static final String ONE_TENTH_STRING = "1/1";
	private static final String ONE_EIGHTH_STRING = "1/8";
	private static final String ONE_SIXTH_STRING = "1/6";
	private static final String ONE_FIFTH_STRING = "1/5";
	private static final String ONE_FOURTH_STRING = "1/4";
	private static final String ONE_THIRD_STRING = "1/3";
	private static final String ONE_HALF_STRING = "1/2";
	private static final String TWO_THIRDS_STRING = "2/3";
	private static final String THREE_FOURTHS_STRING = "3/4";

	// object to hold text and a count
	private class TextWithCount{

		private String text;
		private int count;

		TextWithCount(String text, int count) {
			this.text = text;
			this.count = count;
		}

		/**
		 * Returns Text.
		 *
		 * @return The Text.
		 **/
		public String getText() {
			return text;
		}

		/**
		 * Returns Count.
		 *
		 * @return The Count.
		 **/
		public int getCount() {
			return count;
		}
	}

	// ePlum departments
	private enum EPlumDepartment{
		CANDY("121"),
		KOSHER("131"),
		CHEFS_CASE("151"),
		HEALTHY_LIVING("161"),
		MARKET_SEAFOOD("213"),
		BAKERY("301"),
		DELI("601"),
		CHINESE("603"),
		BULK_FOODS("701"),
		PRODUCE_FLORAL("901"),
		//Start - Changes are done for Scale Load by Department by Joseph Williams, Swaroopa Rani
		BEAUTY("401"),//Added For Load By Dept
		GREATS("141"),//Added For Load By Dept
		CHICKEN("191"),//Added For Load By Dept
		//TEXAS_GOODS("901"),//Added For Load By Dept
       //GROCERY("701"),//Added For Load By Dept
		MERCHANDISE("1201");//Added For Load By Dept
		//End - Changes are done for Scale Load by Department by Joseph Williams, Swaroopa Rani
		private String code;

		EPlumDepartment(String code){
			this.code = code;
		}

		/**
		 * Returns a String representation of the object.
		 *
		 * @return A String representation of the object.
		 */
		@Override
		public String toString() {
			return this.code;
		}
	}

	// ePlum message general information
	private enum EPlumGeneral{
		COMMAND("CCO"),
		STORE("SNO"),
		DEPARTMENT("DNO");

		private String code;

		EPlumGeneral(String code){
			this.code = code;
		}

		/**
		 * Returns a String representation of the object.
		 *
		 * @return A String representation of the object.
		 */
		@Override
		public String toString() {
			return this.code;
		}
	}

	// ePlum message general information
	private enum EPlumHeader{
		BATCH_NAME("BNA"),
		BATCH_NUMBER("BNO"),
		BATCH_CREATED_DATE("BDA"),
		BATCH_CREATED_TIME("BTI"),
		BATCH_EFFECTIVE_DATE("EFD"),
		BATCH_EFFECTIVE_TIME("EFT"),
		BATCH_TYPE("BTY"),
		BATCH_QUEUE_CODE("FLG"),
		ITEM_COUNT("CNT");

		private String code;

		EPlumHeader(String code){
			this.code = code;
		}

		/**
		 * Returns a String representation of the object.
		 *
		 * @return A String representation of the object.
		 */
		@Override
		public String toString() {
			return this.code;
		}
	}

	// ePlum message PLU information
	private enum EPlumPluInformation{
		PLU("PNO"),
		ITEM_ADD_COMMAND("SPIA"),
		ITEM_CHANGE_COMMAND("SPIC"),
		ITEM_PRICE_CHANGE_COMMAND("SPPC"),
		ITEM_DELETE_ONE_COMMAND("SPID"),
		ITEM_DELETE_ALL_COMMAND("SPFE"),
		DESCRIPTION_LINE_1("DN1"),
		DESCRIPTION_LINE_2("DN2"),
		DESCRIPTION_LINE_3("DN3"),
		DESCRIPTION_LINE_4("DN4"),
		DESCRIPTION_SIZE_1("DS1"),
		DESCRIPTION_SIZE_2("DS2"),
		DESCRIPTION_SIZE_3("DS3"),
		DESCRIPTION_SIZE_4("DS4"),
		UNIT_PRICE("UPR"),
		UNIT_OF_MEASURE("UME"),
		BY_COUNT_QUANTITY("BCO"),
		WRAPPED_TARE_WEIGHT("WTA"),
		UNWRAPPED_TARE_WEIGHT("UTA"),
		SHELF_LIFE("SLI"),
		USE_BY("EBY"),
		COMMODITY_CLASS("CCL"),
		UPC("UPC"),
		FIXED_WEIGHT("FWT"),
		LOGO_NUMBERS("LNU"), // can be up to five (6 digit) logo numbers comma separated
		LABEL_FORMAT_1("LF1"),
		LABEL_FORMAT_2("LF2"),
		FORCED_TARE("FTA"),
		SHELF_LIFE_TYPE("SLT"),
		GRAPHIC_NUMBERS("GNO"), // can be up to five (6 digit) graphic numbers comma separated
		ALLERGEN_NUMBER("ALG"),
		USER_DEFINED_TEXT_1("U1N"),
		USER_DEFINED_TEXT_2("U2N"),
		FS_DISCOUNT_PRICE("FR1"), // for frequent shopper
		FS_DISCOUNT_METHOD("FDT"), // for frequent shopper
		FS_BY_COUNT_QUANTITY("FSM"), // for frequent shopper
		FS_BY_COUNT_ITEM_EXCEPTION_PRICE("FSX"), // for frequent shopper
		PERCENTAGE_TARE_WEIGHT("PTS"),
		FORCE_ENTER_SHELF_LIFE("FSL"),
		FORCE_ENTER_USE_BY("FUB"),
		COOL_TEXT_NUMBER("CNO"),
		COOL_COMMODITY_CLASS("CCN"),
		SHORT_LIST_NUMBER("CSN"),
		SHORT_LIST_NUMBER_SEQUENCE("SQN"),
		COOL_MOST_RECENT_USED("CUN"),
		COOL_PLU_TRACKING_NUMBER("CRN"),
		FORCE_SCALE_TO_USE_COOL("CFX"),
		COOL_PLU_PRETEXT_NUMBER("CXN"),
		ACTION_NUMBER("ANO"),
		SCALE_WRAPPER_GRADE("GNU");

		private String code;

		/**
		 * Returns a String representation of the object.
		 *
		 * @return A String representation of the object.
		 */
		@Override
		public String toString() {
			return this.code;
		}

		EPlumPluInformation(String code){
			this.code = code;
		}
	}

	// ePlum message ingredient information
	private enum EPlumIngredientStatement{
		NUMBER("INO"),
		ITEM_ADD_COMMAND("SIIA"),
		ITEM_CHANGE_COMMAND("SIIC"),
		ITEM_DELETE_ONE_COMMAND("SIID"),
		TEXT("ITE"),
		LINKED_STATEMENTS("LNK"), // linked ingredient statements comma separated
		MODIFIED("MOD"); // N--Normal(default), A--Automatic

		private String code;

		EPlumIngredientStatement(String code){
			this.code = code;
		}

		/**
		 * Returns a String representation of the object.
		 *
		 * @return A String representation of the object.
		 */
		@Override
		public String toString() {
			return this.code;
		}
	}

	// ePlum message nutrient information
	private enum EPlumNutrientStatement{
		NUMBER("NTN"),
		ITEM_ADD_COMMAND("SNIA"),
		ITEM_CHANGE_COMMAND("SNIC"),
		ITEM_DELETE_ONE_COMMAND("SNID"),
		LABEL_FORMAT_1("LF1"),
		SERVINGS_PER_CONTAINER("SPC"),
		SERVICE_SIZE_DESCRIPTION("SSZ"),
		ADDED_SUGARS("ASU"),
		BETA_CAROTENE("BCR"),
		BIOTIN("BIO"),
		CALCIUM("CAC"),
		CALORIES("CAL"),
		CALORIES_ENERGY("CEN"),
		CALORIES_FROM_FAT("CFF"),
		CHLORIDE("CHL"),
		CHOLESTEROL("CHO"),
		COPPER("COP"),
		CHROMIUM("CRO"),
		CALORIES_SATURATED_FAT("CSF"),
		ENERGY("EGY"),
		TOTAL_FAT("FAT"),
		DIETARY_FIBER("FIB"),
		FOLIC_ACID("FOA"),
		FOLATE("FOL"),
		IODINE("IOD"),
		IRON("IRO"),
		INSOLUBLE_FIBER("ISF"),
		MONO_UNSATURATED_FAT("MUS"),
		NIACIN("NIA"),
		OMEGA_3_FATTY_ACIDS("O3F"),
		OMEGA_6_FATTY_ACIDS("O6F"),
		OTHER_CARBOHYDRATES("OCB"),
		PROTEIN("PRO"),
		POLY_UNSATURATED_FAT("PUS"),
		RIBOFLAVIN("RIB"),
		SATURATED_FAT("SAF"),
		SUGAR_ALCOHOL("SAH"),
		SOLUBLE_FIBER("SFB"),
		SODIUM("SOD"),
		STARCH("STC"),
		SATURATED_TRANS_FAT("STF"),
		SUGAR("SUG"),
		TOTAL_CARBOHYDRATES("TCA"),
		TRANS_FAT("TFT"),
		THIAMINE("THI"),
		VITAMIN_A("VIA"),
		VITAMIN_C("VIC"),
		MAGNESIUM("MAG"),
		MANGANESE("MAN"),
		MOLYBDENUM("MOL"),
		PANTOTH_ACID("PAN"),
		PHOSPHOROUS("PHO"),
		POTASSIUM("POT"),
		SELENIUM("SEL"),
		VITAMIN_B12("V12"),
		VITAMIN_B6("VB6"),
		VITAMIN_D("VID"),
		VITAMIN_K("VIK"),
		ZINC("ZIN");

		private String code;

		EPlumNutrientStatement(String code){
			this.code = code;
		}

		/**
		 * Returns a String representation of the object.
		 *
		 * @return A String representation of the object.
		 */
		@Override
		public String toString() {
			return this.code;
		}
	}

	// ePlum retail units of measure
	private enum EPlumRetailUnitOfMeasure{
		PRICE_OVERRIDE("OB"),
		WEIGHED("LB"),
		FIXED_WEIGHT("FW"),
		BY_COUNT("BC");

		private String code;

		EPlumRetailUnitOfMeasure(String code){
			this.code = code;
		}

		/**
		 * Returns a String representation of the object.
		 *
		 * @return A String representation of the object.
		 */
		@Override
		public String toString() {
			return this.code;
		}
	}

	private SwitchToBooleanConverter switchToBooleanConverter = null;
    //Start - Changes are done for Scale Load by Department by Joseph Williams, Swaroopa Rani
	/**
	 * This method generates the message to send to ePlum given a scale maintenance tracking, and an ePlum batch number.
	 *
	 * @param ePlumBatchNumber ePlum batch number for this body of work.
	 * @param store Store this message is being created for.
	 * @param currentUpcs List of current upc data.
	 * @param currentRetails List of current authorization and retail data.
	 * @return The message to send to ePlum.
	 */
	public EPlumMessage generateEPlumMessage(Long ePlumBatchNumber,
											 Integer store, List<ScaleMaintenanceUpc> currentUpcs,
											 List<ScaleMaintenanceAuthorizeRetail> currentRetails,int targetStoreNbr,Date effectiveDate) throws IllegalArgumentException {
											
		List<ScaleMaintenance> currentScaleMaintenance = this.generateCurrentScaleMaintenance(currentRetails, currentUpcs);
		TextWithCount pluInformation = this.createPLUInformation(currentScaleMaintenance, store, currentUpcs,targetStoreNbr);
		TextWithCount ingredientInformation = this.createIngredientInformation(currentScaleMaintenance, store,targetStoreNbr);
		TextWithCount nutrientInformation = this.createNutrientInformation(currentScaleMaintenance, store,targetStoreNbr);
		EPlumMessage toReturn = new EPlumMessage()
				.setPluData(pluInformation.getText())
				.setIngredientData(ingredientInformation.getText())
				.setNutrientData(nutrientInformation.getText());
		int totalCount = pluInformation.getCount() + ingredientInformation.getCount() + nutrientInformation.getCount();
		toReturn.setHeaderData(this.createHeaderInformation(ePlumBatchNumber, store, totalCount,targetStoreNbr,effectiveDate));
		return toReturn;
	}
	
	//End - Changes are done for Scale Load by Department by Joseph Williams, Swaroopa Rani

	/**
	 * This method creates a map of upc to scale maintenance, so the {PLU/ ingredient/ nutrition} information can be
	 * extracted individually.
	 *
	 * @param currentRetails List of current authorization and retails for a given store.
	 * @param currentUpcs List of current scale maintenance upcs that need to be sent to ePlum.
	 * @return Map of upc to scale maintenance containing all {PLU, ingredient/ nutrition} info for a given store.
	 */
	private List<ScaleMaintenance> generateCurrentScaleMaintenance(List<ScaleMaintenanceAuthorizeRetail> currentRetails, List<ScaleMaintenanceUpc> currentUpcs) {
		List<ScaleMaintenance> toReturn = new ArrayList<>();
		ScaleMaintenance currentScaleMaintenance;
		ScaleMaintenanceUpc currentUpc;
		for(ScaleMaintenanceAuthorizeRetail currentAuthorizeRetail : currentRetails){
			currentScaleMaintenance = new ScaleMaintenance()
					.setAuthorizeRetail(currentAuthorizeRetail);
			currentUpc = this.findCurrentScaleMaintenanceUpcMatchingUpc(
					currentAuthorizeRetail.getKey().getUpc(), currentUpcs);
			if(currentUpc != null){
				currentScaleMaintenance.setScaleMaintenanceUpc(currentUpc);
			}
			toReturn.add(currentScaleMaintenance);
		}
		return toReturn;
	}

	/**
	 * This method finds the scale maintenance upc linked to the given upc, or null if not found.
	 *
	 * @param upc Upc to look for.
	 * @param currentUpcs List of all scale maintenance upcs.
	 * @return Scale maintenance upc with the given upc, or null if not found.
	 */
	private ScaleMaintenanceUpc findCurrentScaleMaintenanceUpcMatchingUpc(Long upc, List<ScaleMaintenanceUpc> currentUpcs) {
		for(ScaleMaintenanceUpc currentUpc : currentUpcs){
			if(upc.equals(currentUpc.getKey().getUpc())){
				return currentUpc;
			}
		}
		return null;
	}
	
	//Start - Changes are done for Scale Load by Department by Joseph Williams, Swaroopa Rani

	/**
	 * This method creates the nutrient information of the body of an ePlum message.
	 *
	 * @param currentScaleMaintenance List of authorization/ retail and PLU data to use to retrieve information.
	 * @param store Store this nutrient information is for.
	 * @return Nutrient information of the body of an ePlum message, and count of nutrient items.
	 */
	private TextWithCount createNutrientInformation(List<ScaleMaintenance> currentScaleMaintenance, Integer store,int targetStoreNbr) throws IllegalArgumentException {
		StringBuilder toReturn = new StringBuilder(StringUtils.EMPTY);
		int itemCount = 0;
		for(ScaleMaintenance currentMaintenance : currentScaleMaintenance){

			// if this nutrient statement does not exist, continue to next
			if(currentMaintenance.getScaleMaintenanceUpc().getScaleProductAsJson().getNutrientStatement() == null){
				continue;
			}
			if(targetStoreNbr != 0) {
				store = Integer.parseInt(String.valueOf(targetStoreNbr));
			}
			if(currentScaleMaintenance.indexOf(currentMaintenance) != 0){
				toReturn.append(EPLUM_MESSAGE_CHUNK_DELIMITER);
			}
			// add this nutrient statement to ePlum message body
			toReturn
					.append(this.getEPlumFormatForCommand(EPlumNutrientStatement.ITEM_ADD_COMMAND))
					.append(EPLUM_MESSAGE_DELIMITER)
					.append(this.getEPlumFormatForKey(EPlumGeneral.STORE))
					.append(this.getStringFormattedFromLong(store, 5))
					.append(EPLUM_MESSAGE_DELIMITER)
					.append(this.getEPlumFormatForKey(EPlumGeneral.DEPARTMENT))
					.append(this.getEPlumFormatForDepartment(this.getEPlumDepartmentOfScaleMaintenance(
							currentMaintenance.getScaleMaintenanceUpc())))
					.append(EPLUM_MESSAGE_DELIMITER)
					.append(this.getEPlumFormatForKey(EPlumPluInformation.PLU))
					.append(this.getStringFormattedFromLong(
							currentMaintenance.getScaleMaintenanceUpc().getScaleProductAsJson().getPlu(), 6))
					.append(EPLUM_MESSAGE_DELIMITER)
					.append(this.getEPlumFormatForKey(EPlumNutrientStatement.NUMBER))
					.append(this.getStringFormattedFromLong(
							currentMaintenance.getScaleMaintenanceUpc().getScaleProductAsJson()
									.getNutrientStatementCode(), 6))
					.append(EPLUM_MESSAGE_DELIMITER)
					.append(this.getEPlumFormatForKey(EPlumNutrientStatement.SERVICE_SIZE_DESCRIPTION))
					.append(this.getEPlumServingSizeFromScaleMaintenanceNutrient(
							currentMaintenance.getScaleMaintenanceUpc().getScaleProductAsJson().getNutrientStatement()))
					.append(EPLUM_MESSAGE_DELIMITER)
					.append(this.getEPlumFormatForKey(EPlumNutrientStatement.SERVINGS_PER_CONTAINER))
					.append(this.getStringFormattedFromLong(
							currentMaintenance.getScaleMaintenanceUpc().getScaleProductAsJson()
									.getNutrientStatement().getServingsPerContainer(), 3))
					.append(EPLUM_MESSAGE_DELIMITER)
					// .append(this.getEPlumFormatForKey(EPlumNutrientStatement.CALCIUM))
					// .append(this.getStringFormattedForNutrient(
					// currentMaintenance.getScaleMaintenanceUpc().getScaleProductAsJson().getCalcium(),
					// false, true))
					.append(this.getStringFormattedForNutrient(
							this.getEPlumFormatForKey(EPlumNutrientStatement.CALCIUM),
							currentMaintenance.getScaleMaintenanceUpc().getScaleProductAsJson().getCalcium(), true,
							true))
					// .append(EPLUM_MESSAGE_DELIMITER)
					// .append(this.getEPlumFormatForKey(EPlumNutrientStatement.CALORIES))
					.append(this.getStringFormattedForNutrient(
							this.getEPlumFormatForKey(EPlumNutrientStatement.CALORIES),
							currentMaintenance.getScaleMaintenanceUpc().getScaleProductAsJson().getCalories(), true,
							true))
					// .append(EPLUM_MESSAGE_DELIMITER)
					// .append(this.getEPlumFormatForKey(EPlumNutrientStatement.CALORIES_FROM_FAT))
					.append(this.getStringFormattedForNutrient(
							this.getEPlumFormatForKey(EPlumNutrientStatement.CALORIES_FROM_FAT),
							currentMaintenance.getScaleMaintenanceUpc().getScaleProductAsJson().getCaloriesFromFat(),
							true, true))
					// .append(EPLUM_MESSAGE_DELIMITER)
					// .append(this.getEPlumFormatForKey(EPlumNutrientStatement.CHOLESTEROL))
					.append(this.getStringFormattedForNutrient(
							this.getEPlumFormatForKey(EPlumNutrientStatement.CHOLESTEROL),
							currentMaintenance.getScaleMaintenanceUpc().getScaleProductAsJson().getCholesterol(), true,
							true))
					// .append(EPLUM_MESSAGE_DELIMITER)
					// .append(this.getEPlumFormatForKey(EPlumNutrientStatement.TOTAL_FAT))
					.append(this.getStringFormattedForNutrient(
							this.getEPlumFormatForKey(EPlumNutrientStatement.TOTAL_FAT),
							currentMaintenance.getScaleMaintenanceUpc().getScaleProductAsJson().getTotalFat(), true,
							true))
					// .append(EPLUM_MESSAGE_DELIMITER)
					// .append(this.getEPlumFormatForKey(EPlumNutrientStatement.DIETARY_FIBER))
					.append(this.getStringFormattedForNutrient(
							this.getEPlumFormatForKey(EPlumNutrientStatement.DIETARY_FIBER),
							currentMaintenance.getScaleMaintenanceUpc().getScaleProductAsJson().getDietaryFiber(), true,
							true))
					// .append(EPLUM_MESSAGE_DELIMITER)
					// .append(this.getEPlumFormatForKey(EPlumNutrientStatement.IRON))
					.append(this.getStringFormattedForNutrient(this.getEPlumFormatForKey(EPlumNutrientStatement.IRON),
							currentMaintenance.getScaleMaintenanceUpc().getScaleProductAsJson().getIron(), true, true))
					// .append(EPLUM_MESSAGE_DELIMITER)
					// .append(this.getEPlumFormatForKey(EPlumNutrientStatement.PROTEIN))
					.append(this.getStringFormattedForNutrient(
							this.getEPlumFormatForKey(EPlumNutrientStatement.PROTEIN),
							currentMaintenance.getScaleMaintenanceUpc().getScaleProductAsJson().getProtein(), true,
							true))
					// .append(EPLUM_MESSAGE_DELIMITER)
					// .append(this.getEPlumFormatForKey(EPlumNutrientStatement.SATURATED_FAT))
					.append(this.getStringFormattedForNutrient(
							this.getEPlumFormatForKey(EPlumNutrientStatement.SATURATED_FAT),
							currentMaintenance.getScaleMaintenanceUpc().getScaleProductAsJson().getSaturatedFat(), true,
							true))
					// .append(EPLUM_MESSAGE_DELIMITER)
					// .append(this.getEPlumFormatForKey(EPlumNutrientStatement.SUGAR_ALCOHOL))
					.append(this.getStringFormattedForNutrient(
							this.getEPlumFormatForKey(EPlumNutrientStatement.SUGAR_ALCOHOL),
							currentMaintenance.getScaleMaintenanceUpc().getScaleProductAsJson().getSugarAlcohol(), true,
							true))
					// .append(EPLUM_MESSAGE_DELIMITER)
					// .append(this.getEPlumFormatForKey(EPlumNutrientStatement.SODIUM))
					.append(this.getStringFormattedForNutrient(this.getEPlumFormatForKey(EPlumNutrientStatement.SODIUM),
							currentMaintenance.getScaleMaintenanceUpc().getScaleProductAsJson().getSodium(), true,
							true))
					// .append(EPLUM_MESSAGE_DELIMITER)
					// .append(this.getEPlumFormatForKey(EPlumNutrientStatement.SUGAR))
					.append(this.getStringFormattedForNutrient(this.getEPlumFormatForKey(EPlumNutrientStatement.SUGAR),
							currentMaintenance.getScaleMaintenanceUpc().getScaleProductAsJson().getSugar(), true, true))
					// .append(EPLUM_MESSAGE_DELIMITER)
					// .append(this.getEPlumFormatForKey(EPlumNutrientStatement.TOTAL_CARBOHYDRATES))
					.append(this.getStringFormattedForNutrient(
							this.getEPlumFormatForKey(EPlumNutrientStatement.TOTAL_CARBOHYDRATES),
							currentMaintenance.getScaleMaintenanceUpc().getScaleProductAsJson().getTotalCarbohydrates(),
							true, true))
					// .append(EPLUM_MESSAGE_DELIMITER)
					// .append(this.getEPlumFormatForKey(EPlumNutrientStatement.TRANS_FAT))
					.append(this.getStringFormattedForNutrient(
							this.getEPlumFormatForKey(EPlumNutrientStatement.TRANS_FAT),
							currentMaintenance.getScaleMaintenanceUpc().getScaleProductAsJson().getTransFat(), true,
							true))
					// .append(EPLUM_MESSAGE_DELIMITER)
					// .append(this.getEPlumFormatForKey(EPlumNutrientStatement.VITAMIN_A))
					.append(this.getStringFormattedForNutrient(
							this.getEPlumFormatForKey(EPlumNutrientStatement.VITAMIN_A),
							currentMaintenance.getScaleMaintenanceUpc().getScaleProductAsJson().getVitaminA(), true,
							true))
					// .append(EPLUM_MESSAGE_DELIMITER)
					// .append(this.getEPlumFormatForKey(EPlumNutrientStatement.VITAMIN_C))
					.append(this.getStringFormattedForNutrient(
							this.getEPlumFormatForKey(EPlumNutrientStatement.VITAMIN_C),
							currentMaintenance.getScaleMaintenanceUpc().getScaleProductAsJson().getVitaminC(), true,
							true))
					// .append(EPLUM_MESSAGE_DELIMITER)
					// .append(this.getEPlumFormatForKey(EPlumNutrientStatement.POTASSIUM))
					.append(this.getStringFormattedForNutrient(
							this.getEPlumFormatForKey(EPlumNutrientStatement.POTASSIUM),
							currentMaintenance.getScaleMaintenanceUpc().getScaleProductAsJson().getPotassium(), true,
							true));
			if (toReturn.substring(toReturn.length() - 1).equals(EPLUM_MESSAGE_DELIMITER)) {
				toReturn = new StringBuilder(toReturn.toString().substring(0, toReturn.toString().length() - 1));
			}
			itemCount++;
		}
		return new TextWithCount(toReturn.toString(),itemCount);
	}
	
	//End - Changes are done for Scale Load by Department by Joseph Williams, Swaroopa Rani

	/**
	 * This helper function converts a nutrient into a {value},{percent} string pair for a given nutrient.
	 *
	 * @param nutrient Nutrient with the value and percent values for a particular nutrient (i.e. Calories).
	 * @param sendValue Whether this nutrient sends value data to ePlum.
	 * @param sendPercent Whether this nutrient sends percent data to ePlum.
	 * @return String {value},{percent} for the given nutrient.
	 */
	 public String getStringFormattedForNutrient(ScaleMaintenanceNutrient nutrient, boolean sendValue, boolean sendPercent) {
		if(nutrient != null) {
			return STRING_BLOCK_START
					// if this nutrient sends a value, add the value; else add a '0'
					.concat(this.getStringFormattedFromDouble(nutrient.getValue() != null &&
							sendValue ? nutrient.getValue() : 0.0, 5, DOUBLE_PRECISION_ONE))
					.concat(EPLUM_MESSAGE_DELIMITER)
					// if this nutrient sends a percent, add the percent; else add a '0'
					.concat(this.getStringFormattedFromLong(nutrient.getPercent() != null &&
							sendPercent ? nutrient.getPercent() : 0L, 4))
					.concat(STRING_BLOCK_END);
		} else {
			return STRING_BLOCK_START
					.concat(this.getStringFormattedFromDouble(0.0, 5, DOUBLE_PRECISION_ONE))
					.concat(EPLUM_MESSAGE_DELIMITER)
					.concat(this.getStringFormattedFromLong(0L, 4))
					.concat(STRING_BLOCK_END);
		}
	}
	
	//Start - Changes are done for Scale Load by Department by Joseph Williams, Swaroopa Rani

	public String getStringFormattedForNutrient(String nutrientKey, ScaleMaintenanceNutrient nutrient, boolean sendValue, boolean sendPercent) {
        if(nutrient != null && (nutrient.getValue() != null || nutrient.getPercent() != null)) {
                     return   nutrientKey 
					.concat(STRING_BLOCK_START)
					// if this nutrient sends a value, add the value; else add a '0'
					.concat(this.getStringFormattedFromDouble(
							nutrient.getValue() != null && sendValue ? nutrient.getValue() : 0.0, 5,
							DOUBLE_PRECISION_ONE))
					.concat(EPLUM_MESSAGE_DELIMITER)
					// if this nutrient sends a percent, add the percent; else add a '0'
					.concat(this.getStringFormattedFromLong(
							nutrient.getPercent() != null && sendPercent ? nutrient.getPercent() : 0L, 4))
					.concat(STRING_BLOCK_END).concat(EPLUM_MESSAGE_DELIMITER);
		}else{
        	return "";
        }
	} 
	
	//End - Changes are done for Scale Load by Department by Joseph Williams, Swaroopa Rani  

	/**
	 * This helper function gets the text for serving size description given a nutrient statement.
	 *
	 * @param nutrientStatement Nutrient statement with the required information.
	 * @return String representation of a serving size description.
	 */
	public String getEPlumServingSizeFromScaleMaintenanceNutrient(ScaleMaintenanceNutrientStatement nutrientStatement) {
		String metricUOM = this.getConvertedUomMetricCode(nutrientStatement.getUomMetricCode());
		String metricQuantity = this.getConvertedMetricQuantity(nutrientStatement.getMetricQuantity());
		String commonUOM = this.getConvertedUomCommonCode(nutrientStatement.getUomCommonCode());
		String commonQuantity = this.getConvertedMeasureQuantity(nutrientStatement.getMeasureQuantity());

		return String.format(
				NUTRIENT_SERVING_SIZE_DESCRIPTION_FORMAT,
				commonQuantity,
				commonUOM,
				metricQuantity,
				metricUOM
		);
	}

	/**
	 * Returns the formatted common quantity.
	 *
	 * @param measureQuantity The measureQuantity.
	 * @return The converted common quantity.
	 */
	private String getConvertedMeasureQuantity(double measureQuantity) {

		StringBuilder convertedMeasureQuantity = new StringBuilder(StringUtils.EMPTY);

		String fractionalMeasureQuantityPart = this.getConvertedMeasureQuantityDecimalPart(measureQuantity);
		String integralMeasureQuantityPart = this.getConvertedMeasureQuantityIntegerPart(measureQuantity);

		convertedMeasureQuantity.append(integralMeasureQuantityPart)
				.append(StringUtils.SPACE)
				.append(fractionalMeasureQuantityPart);

		return convertedMeasureQuantity.toString();
	}

	/**
	 * Returns the formatted integer part of the common quantity.
	 *
	 * @param measureQuantity The measureQuantity.
	 * @return The integer part of the converted common quantity.
	 */
	private String getConvertedMeasureQuantityIntegerPart(Double measureQuantity) {

		StringBuilder convertedMeasureQuantity = new StringBuilder(StringUtils.EMPTY);

		int measureQuantityInt = (measureQuantity).intValue();
		String convertedString = Integer.toString(measureQuantityInt);

		if(StringUtils.isNotBlank(convertedString) && measureQuantityInt > 0){
			if(convertedString.length() < 3){
				convertedMeasureQuantity.append(StringUtils.SPACE)
						.append(convertedString);
			}
			else {
				convertedMeasureQuantity.append(convertedString.substring(
						Math.max(0, convertedString.length() -3), convertedString.length()));
			}
		}

		return convertedMeasureQuantity.toString();
	}

	/**
	 * Returns the formatted decimal part of the common quantity.
	 *
	 * @param measureQuantity The measureQuantity.
	 * @return The decimal part of the converted common quantity.
	 */
	private String getConvertedMeasureQuantityDecimalPart(double measureQuantity) {
		double fractionalMeasureQuantityPart = measureQuantity % 1;

		StringBuilder decimalPartConvertedToString = new StringBuilder(StringUtils.EMPTY);

		if(fractionalMeasureQuantityPart > 0) {
			if (fractionalMeasureQuantityPart == 0.08) {
				decimalPartConvertedToString.append(ONE_TWELFTH_STRING);
			} else if (fractionalMeasureQuantityPart == 0.10) {
				decimalPartConvertedToString.append(ONE_TENTH_STRING);
			} else if (fractionalMeasureQuantityPart == 0.12) {
				decimalPartConvertedToString.append(ONE_EIGHTH_STRING);
			} else if (fractionalMeasureQuantityPart == 0.16) {
				decimalPartConvertedToString.append(ONE_SIXTH_STRING);
			} else if (fractionalMeasureQuantityPart == 0.20) {
				decimalPartConvertedToString.append(ONE_FIFTH_STRING);
			} else if (fractionalMeasureQuantityPart == 0.25) {
				decimalPartConvertedToString.append(ONE_FOURTH_STRING);
			} else if (fractionalMeasureQuantityPart == 0.33) {
				decimalPartConvertedToString.append(ONE_THIRD_STRING);
			} else if (fractionalMeasureQuantityPart == 0.50) {
				decimalPartConvertedToString.append(ONE_HALF_STRING);
			} else if (fractionalMeasureQuantityPart == 0.66) {
				decimalPartConvertedToString.append(TWO_THIRDS_STRING);
			} else if (fractionalMeasureQuantityPart == 0.75) {
				decimalPartConvertedToString.append(THREE_FOURTHS_STRING);
			}
		}

		return decimalPartConvertedToString.toString();
	}

	/**
	 * Returns the formatted common UOM.
	 *
	 * @param uomCommonCode The uomCommonCode.
	 * @return The converted common uom.
	 */
	private String getConvertedUomCommonCode(String uomCommonCode) {

		StringBuilder convertedUomCommonCode = new StringBuilder(StringUtils.EMPTY);

		if (StringUtils.isNotBlank(uomCommonCode)){
			uomCommonCode = uomCommonCode.trim();
			if(uomCommonCode.length() > 9){
				convertedUomCommonCode.append(uomCommonCode.substring(0,8));
			}
			else if (uomCommonCode.length() < 9){
				convertedUomCommonCode.append(uomCommonCode)
						.append(StringUtils.SPACE);
			}
		}

		return convertedUomCommonCode.toString();
	}

	/**
	 * Returns the formatted metric quantity.
	 *
	 * @param metricQuantity The metricQuantity.
	 * @return The converted metric quantity.
	 */
	private String getConvertedMetricQuantity(long metricQuantity) {

		StringBuilder convertedMetricQuantity = new StringBuilder(StringUtils.EMPTY);
		String convertedString = Long.toString(metricQuantity);

		if(convertedString.length() <= 3){
			convertedMetricQuantity.append(convertedString);
			if(convertedMetricQuantity.length() < 3){
				convertedMetricQuantity.append(StringUtils.SPACE);
			}
		}
		else{
			convertedMetricQuantity.append(convertedString.substring(
					Math.max(0, convertedString.length() -3), convertedString.length()));
		}

		return convertedMetricQuantity.toString();
	}

	/**
	 * Returns the formatted metric UOM.
	 *
	 * @param uomMetricCode The uomMetricCode
	 * @return The converted metric UOM.
	 */
	private String getConvertedUomMetricCode(String uomMetricCode) {

		if (StringUtils.isNotBlank(uomMetricCode)){
			return String.valueOf(uomMetricCode.charAt(0));
		}
		else{
			return StringUtils.EMPTY;
		}
	}
	
	//Start - Changes are done for Scale Load by Department by Joseph Williams, Swaroopa Rani

	/**
	 * This method creates the ingredient information of the body of an ePlum message.
	 *
	 * @param currentScaleMaintenance List of authorization/ retail and PLU data to use to retrieve information.
	 * @param store Store
	 * @return Ingredient information of the body of an ePlum message, and count of ingredient items.
	 */
	private TextWithCount createIngredientInformation(List<ScaleMaintenance> currentScaleMaintenance, Integer store,int targetStoreNbr) throws IllegalArgumentException {
		StringBuilder toReturn = new StringBuilder(StringUtils.EMPTY);
		int itemCount = 0;
		for(ScaleMaintenance currentMaintenance : currentScaleMaintenance){

			// if there is no ingredient text, do not add the ingredient information for this upc
			if(StringUtils.isBlank(currentMaintenance.getScaleMaintenanceUpc().getScaleProductAsJson().getIngredientText())){
				continue;
			}
			if(targetStoreNbr != 0) {
				store = Integer.parseInt(String.valueOf(targetStoreNbr));
			}
			//End - Changes are done for Scale Load by Department by Joseph Williams, Swaroopa Rani

			if(currentScaleMaintenance.indexOf(currentMaintenance) != 0){
				toReturn.append(EPLUM_MESSAGE_CHUNK_DELIMITER);
			}
			// add ingredient information
			toReturn
					.append(this.getEPlumFormatForCommand(EPlumIngredientStatement.ITEM_ADD_COMMAND))
					.append(EPLUM_MESSAGE_DELIMITER)
					.append(this.getEPlumFormatForKey(EPlumGeneral.STORE))
					.append(this.getStringFormattedFromLong(store, 5))
					.append(EPLUM_MESSAGE_DELIMITER)
					.append(this.getEPlumFormatForKey(EPlumGeneral.DEPARTMENT))
					.append(this.getEPlumFormatForDepartment(this.getEPlumDepartmentOfScaleMaintenance(currentMaintenance.getScaleMaintenanceUpc())))
					.append(EPLUM_MESSAGE_DELIMITER)
					.append(this.getEPlumFormatForKey(EPlumPluInformation.PLU))
					.append(this.getStringFormattedFromLong(currentMaintenance.getScaleMaintenanceUpc().getScaleProductAsJson().getPlu(), 6))//MF Rewrite changes done by VN02849
					.append(EPLUM_MESSAGE_DELIMITER)
					.append(this.getEPlumFormatForKey(EPlumIngredientStatement.NUMBER))
					.append(this.getStringFormattedFromLong(
							currentMaintenance.getScaleMaintenanceUpc().getScaleProductAsJson().getIngredientStatement(), 6)) //Changes are done for Scale Load by Department by Joseph Williams, Swaroopa Rani

					.append(EPLUM_MESSAGE_DELIMITER)
					.append(this.getEPlumFormatForKey(EPlumIngredientStatement.TEXT))
					.append(STRING_BLOCK_START)
					.append(currentMaintenance.getScaleMaintenanceUpc().getScaleProductAsJson().getIngredientText())
					.append(STRING_BLOCK_END);
			itemCount++;
		}
		return new TextWithCount(toReturn.toString(),itemCount);
	}
	//Start - Changes are done for Scale Load by Department by Joseph Williams, Swaroopa Rani


	/**
	 * This method creates the PLU information of the body of an ePlum message.
	 *
	 * @param currentScaleMaintenance List of authorization/ retail and PLU data to use to retrieve information.
	 * @param store Store
	 * @param currentUpcs List of current upc data.
	 * @return PLU information of the body of an ePlum message, and count of PLU items.
	 */
	private TextWithCount createPLUInformation(List<ScaleMaintenance> currentScaleMaintenance, Integer store,
										List<ScaleMaintenanceUpc> currentUpcs,int targetStoreNbr) throws IllegalArgumentException {
		StringBuilder toReturn = new StringBuilder(StringUtils.EMPTY);
		int itemCount = 0;
		int inputStoreNo = store; // VN02849 Send the Input store for Bilingual check 
		for(ScaleMaintenance currentMaintenance : currentScaleMaintenance){
			if(currentScaleMaintenance.indexOf(currentMaintenance) != 0){
				toReturn.append(EPLUM_MESSAGE_CHUNK_DELIMITER);
			}			
			if(targetStoreNbr != 0) {
				store = Integer.parseInt(String.valueOf(targetStoreNbr));
			}
			//End - Changes are done for Scale Load by Department by Joseph Williams, Swaroopa Rani

			toReturn
					.append(this.getEPlumFormatForCommand(EPlumPluInformation.ITEM_ADD_COMMAND))
					.append(EPLUM_MESSAGE_DELIMITER)
					.append(this.getEPlumFormatForKey(EPlumGeneral.STORE))
					.append(this.getStringFormattedFromLong(store, 5))
					.append(EPLUM_MESSAGE_DELIMITER)
					.append(this.getEPlumFormatForKey(EPlumGeneral.DEPARTMENT))
					.append(this.getEPlumFormatForDepartment(this.getEPlumDepartmentOfScaleMaintenance(currentMaintenance.getScaleMaintenanceUpc())))
					.append(EPLUM_MESSAGE_DELIMITER)
					.append(this.getEPlumFormatForKey(EPlumPluInformation.PLU))
					.append(this.getStringFormattedFromLong(currentMaintenance.getScaleMaintenanceUpc().getScaleProductAsJson().getPlu(), 6))
					.append(EPLUM_MESSAGE_DELIMITER)
					.append(this.getEPlumFormatForKey(EPlumPluInformation.DESCRIPTION_LINE_1))
					//Start - Changes are done for Scale Load by Department by Joseph Williams, Swaroopa Rani
					.append(this.getProductDescriptionByLineNumber(
							inputStoreNo, DESCRIPTION_LINE_1, currentMaintenance.getScaleMaintenanceUpc().getScaleProductAsJson()))
					.append(EPLUM_MESSAGE_DELIMITER)
					.append(this.getEPlumFormatForKey(EPlumPluInformation.DESCRIPTION_LINE_2))
					.append(this.getProductDescriptionByLineNumber(
							inputStoreNo, DESCRIPTION_LINE_2, currentMaintenance.getScaleMaintenanceUpc().getScaleProductAsJson()))
					.append(EPLUM_MESSAGE_DELIMITER)
					.append(this.getEPlumFormatForKey(EPlumPluInformation.DESCRIPTION_LINE_3))
					.append(this.getProductDescriptionByLineNumber(
							inputStoreNo, DESCRIPTION_LINE_3, currentMaintenance.getScaleMaintenanceUpc().getScaleProductAsJson()))
					.append(EPLUM_MESSAGE_DELIMITER)
					.append(this.getEPlumFormatForKey(EPlumPluInformation.DESCRIPTION_LINE_4))
					.append(this.getProductDescriptionByLineNumber(
							inputStoreNo, DESCRIPTION_LINE_4, currentMaintenance.getScaleMaintenanceUpc().getScaleProductAsJson()))
					//End - Changes are done for Scale Load by Department by Joseph Williams, Swaroopa Rani
					.append(EPLUM_MESSAGE_DELIMITER)
					.append(this.getEPlumFormatForKey(EPlumPluInformation.DESCRIPTION_SIZE_1))
					.append(DESCRIPTION_SIZE)
					.append(EPLUM_MESSAGE_DELIMITER)
					.append(this.getEPlumFormatForKey(EPlumPluInformation.DESCRIPTION_SIZE_2))
					.append(DESCRIPTION_SIZE)
					.append(EPLUM_MESSAGE_DELIMITER)
					.append(this.getEPlumFormatForKey(EPlumPluInformation.DESCRIPTION_SIZE_3))
					.append(DESCRIPTION_SIZE)
					.append(EPLUM_MESSAGE_DELIMITER)
					.append(this.getEPlumFormatForKey(EPlumPluInformation.DESCRIPTION_SIZE_4))
					.append(DESCRIPTION_SIZE)
					.append(EPLUM_MESSAGE_DELIMITER)
					.append(this.getEPlumFormatForKey(EPlumPluInformation.UNIT_PRICE))
					.append(this.getStringFormattedFromDouble(
							currentMaintenance.getAuthorizeRetail().getRetail(), 6, DOUBLE_PRECISION_TWO))
					.append(EPLUM_MESSAGE_DELIMITER)
					.append(this.getEPlumFormatForKey(EPlumPluInformation.UNIT_OF_MEASURE))
					.append(this.getRetailUnitOfMeasure(currentMaintenance.getAuthorizeRetail(), currentUpcs,currentMaintenance)) //Changes are done for Scale Load by Department by Joseph Williams, Swaroopa Rani

					.append(EPLUM_MESSAGE_DELIMITER)
					.append(this.getEPlumFormatForKey(EPlumPluInformation.BY_COUNT_QUANTITY))
					.append(this.getStringFormattedFromLong(
							currentMaintenance.getAuthorizeRetail().getByCountQuantity(), 3))
					.append(EPLUM_MESSAGE_DELIMITER)
					.append(this.getEPlumFormatForKey(EPlumPluInformation.WRAPPED_TARE_WEIGHT))
					.append(this.getStringFormattedFromDouble(
							currentMaintenance.getScaleMaintenanceUpc().getScaleProductAsJson().getServiceCounterTare(), 4, DOUBLE_PRECISION_THREE))
					.append(EPLUM_MESSAGE_DELIMITER)
					.append(this.getEPlumFormatForKey(EPlumPluInformation.UNWRAPPED_TARE_WEIGHT))
					.append(this.getStringFormattedFromDouble(
							currentMaintenance.getScaleMaintenanceUpc().getScaleProductAsJson().getPrePackTare(), 4, DOUBLE_PRECISION_THREE))
					.append(EPLUM_MESSAGE_DELIMITER)
					.append(this.getEPlumFormatForKey(EPlumPluInformation.SHELF_LIFE))
					.append(this.getStringFormattedFromLong(
							currentMaintenance.getScaleMaintenanceUpc().getScaleProductAsJson().getShelfLifeDays(), 3))
					.append(EPLUM_MESSAGE_DELIMITER)
					.append(this.getEPlumFormatForKey(EPlumPluInformation.USE_BY))
					.append(this.getStringFormattedFromLong(
							currentMaintenance.getScaleMaintenanceUpc().getScaleProductAsJson().getEatByDays(), 3))
					.append(EPLUM_MESSAGE_DELIMITER)
					.append(this.getEPlumFormatForKey(EPlumPluInformation.COMMODITY_CLASS))
					.append(EPLUM_MESSAGE_DELIMITER)
					.append(this.getEPlumFormatForKey(EPlumIngredientStatement.NUMBER))
					.append(this.getStringFormattedFromLong(
							currentMaintenance.getScaleMaintenanceUpc().getScaleProductAsJson().getIngredientStatement(), 5))
					.append(EPLUM_MESSAGE_DELIMITER)
					.append(this.getEPlumFormatForKey(EPlumPluInformation.UPC))
					.append(this.getStringFormattedFromUpc(
							currentMaintenance.getScaleMaintenanceUpc().getScaleProductAsJson().getUpc()))
					.append(EPLUM_MESSAGE_DELIMITER)
					.append(this.getEPlumFormatForKey(EPlumPluInformation.FIXED_WEIGHT))
					.append(this.getStringFormattedFromDouble(
							currentMaintenance.getScaleMaintenanceUpc().getScaleProductAsJson().getPrePackTare(), 4, DOUBLE_PRECISION_TWO))
					.append(EPLUM_MESSAGE_DELIMITER)
					.append(this.getEPlumFormatForKey(EPlumNutrientStatement.NUMBER))
					.append(this.getStringFormattedFromLong(
							currentMaintenance.getScaleMaintenanceUpc().getScaleProductAsJson().getNutrientStatementCode(), 6))
					.append(EPLUM_MESSAGE_DELIMITER)
					.append(this.getEPlumFormatForKey(EPlumPluInformation.ACTION_NUMBER))
					.append(this.getStringFormattedFromLong(
							currentMaintenance.getScaleMaintenanceUpc().getScaleProductAsJson().getActionCode(), 4))
					.append(EPLUM_MESSAGE_DELIMITER)
					.append(this.getEPlumFormatForKey(EPlumPluInformation.LOGO_NUMBERS))
					.append(this.getStringFormattedFromLong(
							currentMaintenance.getScaleMaintenanceUpc().getScaleProductAsJson().getGraphicsCode(), 4))
					.append(EPLUM_MESSAGE_DELIMITER)
					.append(this.getEPlumFormatForKey(EPlumPluInformation.LABEL_FORMAT_1))
					.append(this.getStringFormattedFromLong(
							currentMaintenance.getScaleMaintenanceUpc().getScaleProductAsJson().getLabelFormatOne(), 4))
					.append(EPLUM_MESSAGE_DELIMITER)
					.append(this.getEPlumFormatForKey(EPlumPluInformation.LABEL_FORMAT_2))
					.append(this.getStringFormattedFromLong(
							currentMaintenance.getScaleMaintenanceUpc().getScaleProductAsJson().getLabelFormatTwo(), 4))
					.append(EPLUM_MESSAGE_DELIMITER)
					.append(this.getEPlumFormatForKey(EPlumPluInformation.FORCED_TARE))
					.append(this.getStringFormattedFromBoolean(
							currentMaintenance.getScaleMaintenanceUpc().getScaleProductAsJson().isForceTare()))
					.append(EPLUM_MESSAGE_DELIMITER)
					.append(this.getEPlumFormatForKey(EPlumPluInformation.SCALE_WRAPPER_GRADE))
					.append(this.getStringFormattedFromLong(
							currentMaintenance.getScaleMaintenanceUpc().getScaleProductAsJson().getGrade(), 2));
			itemCount++;
		}
		return new TextWithCount(toReturn.toString(), itemCount);
	}
	
	
	// helper functions

	/**
	 * This gets the string format for a given department by left padding zeroes onto the given department to get
	 * six characters.
	 *
	 * @param departmentToFormat The department to use for this part of the ePlum message.
	 * @return The department left padded with zeroes to six characters.
	 */
	private String getEPlumFormatForDepartment(EPlumDepartment departmentToFormat) {
		return StringUtils.leftPad(
				this.getEPlumFormatForKey(departmentToFormat),
				SIX_STRING_LENGTH, ZERO_CHARACTER);
	}

	/**
	 * This gets the string format for a given command by concatenating it onto the general ePlum command code.
	 *
	 * @param commandToFormat The command to use for this part of the ePlum message.
	 * @return The general ePlum command code + given command code.
	 */
	private String getEPlumFormatForCommand(Enum commandToFormat) {
		return String.format(
				TWO_ENUMS_FORMAT, EPlumGeneral.COMMAND, commandToFormat);
	}

	/**
	 * This converts a boolean into the string representation ('Y' for true, and 'N' for false/null);
	 *
	 * @param booleanToConvert Boolean to convert to string.
	 * @return 'Y' if given boolean is true, else 'N'.
	 */
	private String getStringFormattedFromBoolean(Boolean booleanToConvert) {
		if(switchToBooleanConverter == null){
			switchToBooleanConverter = new SwitchToBooleanConverter();
		}
		return switchToBooleanConverter.convertToDatabaseColumn(booleanToConvert);
	}

	/**
	 * This converts an enum into a string using for an ePlum message.
	 *
	 * @param enumToFormat The enum to format.
	 * @return The toString() version of the enum.
	 */
	private String getEPlumFormatForKey(Enum enumToFormat) {
		return String.format(
				ONE_ENUM_FORMAT,enumToFormat);
	}


	/**
	 * Gets a string value given a upc. The allowed UPC length to send to ePlum are 5, 6, 11, and 13 characters long.
	 * If the length of the upc is one of the allowed lengths, return the upc as a string. Else pad the upc with zeroes
	 * up to the next allowed length.
	 *
	 * @param upc Upc to be formatted.
	 * @return String with zero padded format of given upc.
	 */
	private String getStringFormattedFromUpc(Long upc) {
		int upcLength = String.valueOf(upc).length();
		if(upcLength == 5 ||
				upcLength == 6 ||
				upcLength == 11 ||
				upcLength == 13){
			return String.format(GIVEN_NUMBER_ZERO_PADDED_FORMAT_PREFIX.concat(String.valueOf(upcLength)).concat(DECIMAL_REGEX_CHARACTER),upc);
		} else if(upcLength < 5){
			return String.format(FIVE_NUMBER_ZERO_PADDED_FORMAT, upc);
		} else if(upcLength < 11){
			return String.format(ELEVEN_NUMBER_ZERO_PADDED_FORMAT, upc);
		} else {
			return String.format(THIRTEEN_NUMBER_ZERO_PADDED_FORMAT, upc);
		}
	}
	
	

	/**
	 * Gets a string value given a long (or integer), and character length of the string.
	 *
	 * @param longToFormat Long to be formatted.
	 * @param charactersInEPlumString Character count expected in return string.
	 * @return String with zero padded format of given long.
	 */
	private String getStringFormattedFromLong(long longToFormat, int charactersInEPlumString) {
		String longToFormatString=String.valueOf(longToFormat); // Changes are done for Scale Load by Department by Joseph Williams, Swaroopa Rani
		switch (charactersInEPlumString){
			case 2: {
				return String.format(TWO_NUMBER_ZERO_PADDED_FORMAT,longToFormat);
			}
			case 3: {
				return String.format(THREE_NUMBER_ZERO_PADDED_FORMAT,longToFormat);
			}
			case 4: {
				return String.format(FOUR_NUMBER_ZERO_PADDED_FORMAT,longToFormat);
			}
			case 5: {
				return String.format(FIVE_NUMBER_ZERO_PADDED_FORMAT,longToFormat);
			}
			case 6: {
			//Start - Changes are done for Scale Load by Department by Joseph Williams, Swaroopa Rani
			//	return String.format(SIX_NUMBER_ZERO_PADDED_FORMAT,longToFormat);
			if(longToFormatString.length() > 6){
					return longToFormatString.substring(0, 6);
			}else {
					return String.format(SIX_NUMBER_ZERO_PADDED_FORMAT,longToFormat);
			}
			//End - Changes are done for Scale Load by Department by Joseph Williams, Swaroopa Rani	
			}
			default: {
				throw new IllegalArgumentException(
						String.format(EPLUM_DECIMAL_FORMAT_NOT_FOUND_ERROR, charactersInEPlumString));
			}
		}
	}

	/**
	 * Gets a string value given a double, character length of the string, and precision of decimal to keep.
	 *
	 * @param doubleToFormat Double to be formatted.
	 * @param charactersInEPlumString Character count expected in return string.
	 * @param decimalPrecision Number of decimals to keep for precision in return string.
	 * @return String with zero padded format of given double.
	 */
	private String getStringFormattedFromDouble(double doubleToFormat, int charactersInEPlumString, Integer decimalPrecision) {

		int doubleMultiplier;
		if(decimalPrecision == DOUBLE_PRECISION_ONE){
			doubleMultiplier = DOUBLE_PRECISION_ONE_MULTIPLIER;
		} else if(decimalPrecision == DOUBLE_PRECISION_TWO){
			doubleMultiplier = DOUBLE_PRECISION_TWO_MULTIPLIER;
		} else if(decimalPrecision == DOUBLE_PRECISION_THREE){
			doubleMultiplier = DOUBLE_PRECISION_THREE_MULTIPLIER;
		} else {
			throw new IllegalArgumentException(
					String.format(EPLUM_DECIMAL_PRECISION_NOT_FOUND_ERROR, decimalPrecision));
		}

		switch (charactersInEPlumString){
			case 4: {
				//Changes are done for Scale Load by Department by Joseph Williams, Swaroopa Rani
				fourCharacterDecimalFormat.setRoundingMode(RoundingMode.FLOOR);	
				return fourCharacterDecimalFormat.format(doubleToFormat * doubleMultiplier);
			}
			case 5: {
				return fiveCharacterDecimalFormat.format(doubleToFormat * doubleMultiplier);
			}
			case 6: {
				return sixCharacterDecimalFormat.format(doubleToFormat * doubleMultiplier);
			}
			default: {
				throw new IllegalArgumentException(
						String.format(EPLUM_DECIMAL_FORMAT_NOT_FOUND_ERROR, charactersInEPlumString));
			}
		}
	}
	
	//End - Changes are done for Scale Load by Department by Joseph Williams, Swaroopa Rani

	/**
	 * Gets the product description for the given store, line number, and scale product. If the given store is
	 * bilingual, get the bilingual description; else get the english description.
	 *
	 * @param store Store in question.
	 * @param descriptionLineNumber Line number of the description.
	 * @param scaleProduct Scale product with description information.
	 * @return The description for the given line number.
	 */
	private String getProductDescriptionByLineNumber(int store, int descriptionLineNumber, ScaleMaintenanceProduct scaleProduct) {
		// if this store is bilingual, return the bilingual description
		if(this.isStoreBilingual(store)){
			return this.getBilingualDescriptionByLineNumber(descriptionLineNumber, scaleProduct);
		}
		// else return the english description
		else {
			return this.getEnglishDescriptionByLineNumber(descriptionLineNumber, scaleProduct);
		}
	}

	/**
	 * Gets the bilingual description for given line number. If the line number requested is not null or empty, return
	 * the bilingual description. Else return the english description.
	 *
	 * @param descriptionLineNumber Description line number.
	 * @param scaleProduct Scale product with description information.
	 * @return The specified english description for the given line number.
	 */
	private String getBilingualDescriptionByLineNumber(int descriptionLineNumber, ScaleMaintenanceProduct scaleProduct) {

		switch(descriptionLineNumber){
			case DESCRIPTION_LINE_1: {
				if(scaleProduct.getSpanishDescriptionOne() != null &&
						!StringUtils.isBlank(scaleProduct.getSpanishDescriptionOne())){
					if(scaleProduct.getSpanishDescriptionOne().contains(",")) {
						StringBuffer stringBuffer = new StringBuffer();
						stringBuffer.append("<");
						stringBuffer.append(scaleProduct.getSpanishDescriptionOne());
						stringBuffer.append(">");
						return stringBuffer.toString();
					} else {
					return scaleProduct.getSpanishDescriptionOne();
					}
				}
				break;
			}
			case DESCRIPTION_LINE_2: {
				if(scaleProduct.getSpanishDescriptionTwo() != null &&
						!StringUtils.isBlank(scaleProduct.getSpanishDescriptionTwo())){
					if(scaleProduct.getSpanishDescriptionTwo().contains(",")) {
						StringBuffer stringBuffer = new StringBuffer();
						stringBuffer.append("<");
						stringBuffer.append(scaleProduct.getSpanishDescriptionTwo());
						stringBuffer.append(">");
						return stringBuffer.toString();
					} else {
					return scaleProduct.getSpanishDescriptionTwo();
					}
				}
				break;
			}
			case DESCRIPTION_LINE_3: {
				if(scaleProduct.getSpanishDescriptionThree() != null &&
						!StringUtils.isBlank(scaleProduct.getSpanishDescriptionThree())){
					if(scaleProduct.getSpanishDescriptionThree().contains(",")) {
						StringBuffer stringBuffer = new StringBuffer();
						stringBuffer.append("<");
						stringBuffer.append(scaleProduct.getSpanishDescriptionThree());
						stringBuffer.append(">");
						return stringBuffer.toString();
					} else {
					return scaleProduct.getSpanishDescriptionThree();
					}
				}
				break;
			}
			case DESCRIPTION_LINE_4: {
				if(scaleProduct.getSpanishDescriptionFour() != null &&
						!StringUtils.isBlank(scaleProduct.getSpanishDescriptionFour())){
					if(scaleProduct.getSpanishDescriptionFour().contains(",")) {
						StringBuffer stringBuffer = new StringBuffer();
						stringBuffer.append("<");
						stringBuffer.append(scaleProduct.getSpanishDescriptionFour());
						stringBuffer.append(">");
						return stringBuffer.toString();
					} else {
					return scaleProduct.getSpanishDescriptionFour();
					}
				}
				break;
			}
		}
		// if the spanish description was empty, return the english description instead
		return this.getEnglishDescriptionByLineNumber(descriptionLineNumber, scaleProduct);
	}

	/**
	 * Gets the english description for given line number.
	 *
	 * @param descriptionLineNumber Description line number.
	 * @param scaleProduct Scale product with description information.
	 * @return The specified english description for the given line number.
	 */
	private String getEnglishDescriptionByLineNumber(int descriptionLineNumber, ScaleMaintenanceProduct scaleProduct) {

		// otherwise return the english description for the given line number.
		switch(descriptionLineNumber){
			case DESCRIPTION_LINE_1: {
				if(scaleProduct.getEnglishDescriptionOne().contains(",")) {
					StringBuffer stringBuffer = new StringBuffer();
					stringBuffer.append("<");
					stringBuffer.append(scaleProduct.getEnglishDescriptionOne());
					stringBuffer.append(">");
					scaleProduct.setEnglishDescriptionOne(stringBuffer.toString());
				}
				return scaleProduct.getEnglishDescriptionOne() != null ?
						scaleProduct.getEnglishDescriptionOne() : StringUtils.EMPTY;
			}
			case DESCRIPTION_LINE_2: {
				if(scaleProduct.getEnglishDescriptionTwo().contains(",")) {
					StringBuffer stringBuffer = new StringBuffer();
					stringBuffer.append("<");
					stringBuffer.append(scaleProduct.getEnglishDescriptionTwo());
					stringBuffer.append(">");
					scaleProduct.setEnglishDescriptionTwo(stringBuffer.toString());
				}
				return scaleProduct.getEnglishDescriptionTwo() != null ?
						scaleProduct.getEnglishDescriptionTwo() : StringUtils.EMPTY;
			}
			case DESCRIPTION_LINE_3: {
				if(scaleProduct.getEnglishDescriptionThree().contains(",")) {
					StringBuffer stringBuffer = new StringBuffer();
					stringBuffer.append("<");
					stringBuffer.append(scaleProduct.getEnglishDescriptionThree());
					stringBuffer.append(">");
					scaleProduct.setEnglishDescriptionThree(stringBuffer.toString());
				}
				return scaleProduct.getEnglishDescriptionThree() != null ?
						scaleProduct.getEnglishDescriptionThree() : StringUtils.EMPTY;
			}
			case DESCRIPTION_LINE_4: {
				if(scaleProduct.getEnglishDescriptionFour().contains(",")) {
					StringBuffer stringBuffer = new StringBuffer();
					stringBuffer.append("<");
					stringBuffer.append(scaleProduct.getEnglishDescriptionFour());
					stringBuffer.append(">");
					scaleProduct.setEnglishDescriptionFour(stringBuffer.toString());
				}
				return scaleProduct.getEnglishDescriptionFour() != null ?
						scaleProduct.getEnglishDescriptionFour() : StringUtils.EMPTY;
			}
			default: {
				throw new IllegalArgumentException(EPLUM_DESCRIPTION_NOT_FOUND_ERROR);
			}
		}
	}

	/**
	 * Helper function to determine if the store in question is bilingual or not. Returns whether the given store is
	 * in the 'bilingual stores' list.
	 *
	 * @param store Store to check if bilingual.
	 * @return True if in bilingual stores list, false otherwise.
	 */
	private boolean isStoreBilingual(Integer store) {
		return bilingualStores.contains(store);
	}
	
	

	/**
	 * Returns the ePlum department for a given scale maintenance upc by finding a matching department/ subDepartment.
	 * If the match is found, return the ePlum department. If not, log the department and sub department that couldn't
	 * be matched, and return null.
	 *
	 * @param maintenanceUpc Scale maintenance upc that has department and subDepartment defined.
	 * @return EPlum department matching the department and sub department according to this method, or null if not found.
	 */
	private EPlumDepartment getEPlumDepartmentOfScaleMaintenance(ScaleMaintenanceUpc maintenanceUpc) throws IllegalArgumentException {
		String department = maintenanceUpc.getScaleProductAsJson().getDepartment().trim();
		String subDepartment = maintenanceUpc.getScaleProductAsJson().getSubDepartment().trim();
		switch(department){
			case PRODUCT_MARKET_DEPT:{
				return EPlumDepartment.MARKET_SEAFOOD;
			}
			case PRODUCT_BAKERY_DEPT:{
				return EPlumDepartment.BAKERY;
			}
			case PRODUCT_DELI_DEPT:{
				if(PRODUCT_CHINESE_SUB_DEPT.equals(subDepartment)){
					return EPlumDepartment.CHINESE;
				} else {
					return EPlumDepartment.DELI;
				}
			}
			case PRODUCT_GROCERY_DEPT:{
				if(PRODUCT_BULK_FOODS_SUB_DEPT.equals(subDepartment)){
					return EPlumDepartment.BULK_FOODS;
				} else {
					return EPlumDepartment.BULK_FOODS; //// Changes are done for Scale Load by Department by Joseph Williams, Swaroopa Rani
				}
			}
			case PRODUCT_PRODUCE_DEPT:{
				return EPlumDepartment.PRODUCE_FLORAL;
			}
	   	   //Start - Changes are done for Scale Load by Department by Joseph Williams, Swaroopa Rani
			case PRODUCT_GENERAL_MERCHANDISE_DEPT:{
				if((PRODUCT_CANDY_SUB_DEPT.equals(subDepartment))||(PRODUCT_BASIC_GM.equals(subDepartment))){
					return EPlumDepartment.CANDY;
				} else if ((PRODUCT_TEXAS_BACKYARD_HARD_GOODS.equals(subDepartment))||(PRODUCT_TEXAS_BACKYARD_LIVE_GOODS.equals(subDepartment))){
					return EPlumDepartment.PRODUCE_FLORAL;
				} else {
					return EPlumDepartment.MERCHANDISE;
				}
			}
			//End - Changes are done for Scale Load by Department by Joseph Williams, Swaroopa Rani
			case PRODUCT_KOSHER_DEPT:{
				return EPlumDepartment.KOSHER;
			}
			case PRODUCT_CHEF_DEPT:{
				return EPlumDepartment.CHEFS_CASE;
			}
			case PRODUCT_HEALTHY_LIVING_DEPT:{
				return EPlumDepartment.HEALTHY_LIVING;
			}
			
	    	//Start - Changes are done for Scale Load by Department by Joseph Williams, Swaroopa Rani
			case PRODUCT_BEAUTY_DEPT:{
				if(PRODUCT_BEAUTY_SUB_DEPT.equals(subDepartment)){
					return EPlumDepartment.HEALTHY_LIVING;
				} else {
					return EPlumDepartment.BEAUTY;
				}
			}
			case PRODUCT_GREATS:{
				return EPlumDepartment.GREATS;
			}
			case PRODUCT_CHICKEN:{
				return EPlumDepartment.CHICKEN;
			}
			//End - Changes are done for Scale Load by Department by Joseph Williams, Swaroopa Rani
		}
		logger.error(String.format(EPLUM_DEPARTMENT_NOT_FOUND_ERROR, department, subDepartment));
		throw new IllegalArgumentException(String.format(EPLUM_DEPARTMENT_NOT_FOUND_ERROR, department, subDepartment));
	}

    //Start - Changes are done for Scale Load by Department by Joseph Williams, Swaroopa Rani 
	/**
	 * This method creates the header information of an ePlum message.
	 *
	 * @param ePlumBatchNumber The batch number to use for this ePlum batch.
	 * @param storeNumber The store number to use for this ePlum batch.
	 * @param itemCount The count of items for this batch.
	 * @return Header information of and ePlum message.
	 */
	private String createHeaderInformation(Long ePlumBatchNumber, Integer storeNumber, int itemCount,int targetStoreNbr,Date effectiveDate) {
		DateFormat batchNameFormatter = new SimpleDateFormat(EPLUM_BATCH_NAME_DATE_FORMAT, Locale.getDefault());
		DateFormat batchEffectiveFormatter = new SimpleDateFormat(EPLUM_BATCH_EFFECTIVE_DATE_FORMAT, Locale.getDefault());
        
		Date effDt = new Date();
		if(targetStoreNbr !=0) {
			storeNumber = Integer.parseInt(String.valueOf(targetStoreNbr));
			effDt = effectiveDate;
		}
		//End - Changes are done for Scale Load by Department by Joseph Williams, Swaroopa Rani
		StringBuilder toReturn = new StringBuilder(StringUtils.EMPTY)
				.append(String.format(
						ONE_ENUM_FORMAT, EPlumHeader.BATCH_NAME))
				.append(EPLUM_BATCH_NAME_PREFIX)
				.append(String.format(
						FOUR_NUMBER_ZERO_PADDED_FORMAT, ePlumBatchNumber))
				.append(batchNameFormatter.format(new Date()))
				.append(EPLUM_MESSAGE_DELIMITER)
				.append(String.format(
						ONE_ENUM_FORMAT, EPlumHeader.BATCH_NUMBER))
				.append(String.format(
						FOUR_NUMBER_ZERO_PADDED_FORMAT, ePlumBatchNumber))
				.append(EPLUM_MESSAGE_DELIMITER)
				.append(String.format(
						ONE_ENUM_FORMAT, EPlumHeader.BATCH_EFFECTIVE_DATE))
				.append(batchEffectiveFormatter.format(effDt)) //Changes are done for Scale Load by Department by Joseph Williams, Swaroopa Rani
				.append(EPLUM_MESSAGE_DELIMITER)
				.append(String.format(
						ONE_ENUM_FORMAT, EPlumHeader.BATCH_EFFECTIVE_TIME))
				.append(TWELVE_THIRTY_AM_STRING)
				.append(EPLUM_MESSAGE_DELIMITER)
				.append(String.format(
						ONE_ENUM_FORMAT, EPlumHeader.BATCH_CREATED_TIME))
				.append(SIX_CHARACTER_TIME_FORMAT.format(LocalDateTime.now()))
				.append(EPLUM_MESSAGE_DELIMITER)
				.append(String.format(
						ONE_ENUM_FORMAT, EPlumHeader.ITEM_COUNT))
				.append(String.format(
						FIVE_NUMBER_ZERO_PADDED_FORMAT, itemCount))
				.append(EPLUM_MESSAGE_DELIMITER)
				.append(String.format(
						ONE_ENUM_FORMAT, EPlumGeneral.STORE))
				.append(String.format(
						FIVE_NUMBER_ZERO_PADDED_FORMAT, storeNumber));
		return toReturn.toString();
	}

	/**
	 * This method gets an ePlum unit of measure by taking into account price override, weight switch, and pre pack
	 * tare.
	 *
	 * @param authorizeRetail Current authorization retail with information.
	 * @param currentUpcDetails Map of upc to scale maintenance upc.
	 * @return String form of unit of measure to send to ePlum.
	 */
	 //Start - Changes are done for Scale Load by Department by Joseph Williams, Swaroopa Rani
	private String getRetailUnitOfMeasure(ScaleMaintenanceAuthorizeRetail authorizeRetail,
										  List<ScaleMaintenanceUpc> currentUpcDetails,ScaleMaintenance currentMaintenance){
	 //End - Changes are done for Scale Load by Department by Joseph Williams, Swaroopa Rani
		ScaleMaintenanceUpc currentUpcInformation = null;
		for(ScaleMaintenanceUpc currentUpc: currentUpcDetails){
			if(currentUpc.getKey().getUpc().equals(authorizeRetail.getKey().getUpc())){
				currentUpcInformation = currentUpc;
				break;
			}
		}

		if(currentUpcInformation != null) {
			if (currentUpcInformation.getScaleProductAsJson().isPriceOverride()) {
				return String.format(ONE_ENUM_FORMAT, EPlumRetailUnitOfMeasure.PRICE_OVERRIDE);
			}
			//Start - Changes are done for Scale Load by Department by Joseph Williams, Swaroopa Rani
			if (authorizeRetail.getWeighed()) {
			    if(currentMaintenance.getAuthorizeRetail().getByCountQuantity()<=1) { //This is the existing MF logic rewritten here.
					currentMaintenance.getAuthorizeRetail().setByCountQuantity(0);
				}
				//End - Changes are done for Scale Load by Department by Joseph Williams, Swaroopa Rani
				return String.format(ONE_ENUM_FORMAT, EPlumRetailUnitOfMeasure.WEIGHED);
			} else {
				if (currentUpcInformation.getScaleProductAsJson().getPrePackTare() != 0.0) {
					return String.format(ONE_ENUM_FORMAT, EPlumRetailUnitOfMeasure.FIXED_WEIGHT);
				} else {
					return String.format(ONE_ENUM_FORMAT, EPlumRetailUnitOfMeasure.BY_COUNT);
				}
			}
		}

		// if the data has not been found, throw an error stating the UOM could not be found
		else {
			throw new IllegalArgumentException(String.format(
					UNIT_OF_MEASURE_NOT_FOUND_ERROR,
					authorizeRetail.getKey().getUpc(),
					authorizeRetail.getKey().getStore()));
		}
	}
}
