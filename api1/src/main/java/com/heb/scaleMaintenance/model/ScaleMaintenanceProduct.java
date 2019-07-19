package com.heb.scaleMaintenance.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.function.Function;

/**
 * Represents product information for scale maintenance.
 *
 * @author m314029
 * @since 2.17.8
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ScaleMaintenanceProduct implements TopLevelModel<ScaleMaintenanceProduct>, Serializable{

	private static final long serialVersionUID = 7795492232274429657L;

	public static final String CURRENT_VERSION = "1.0.0";
	private Long upc;
	private int shelfLifeDays;
	private int freezeByDays;
	private int eatByDays;
	private long actionCode;
	private long graphicsCode;
	private long labelFormatOne;
	private long labelFormatTwo;
	private int grade;
	private double serviceCounterTare;
	private long ingredientStatement;
	private long nutrientStatementCode;
	private ScaleMaintenanceNutrientStatement nutrientStatement;
	private double prePackTare;
	private double netWeight;
	private boolean forceTare;
	private boolean priceOverride;
	private String englishDescriptionOne;
	private String englishDescriptionTwo;
	private String englishDescriptionThree;
	private String englishDescriptionFour;
	private String spanishDescriptionOne;
	private String spanishDescriptionTwo;
	private String spanishDescriptionThree;
	private String spanishDescriptionFour;
	private String ingredientText;
	private ScaleMaintenanceNutrient ascorbicAcid;
	private ScaleMaintenanceNutrient biotin;
	private ScaleMaintenanceNutrient calcium;
	private ScaleMaintenanceNutrient calories;
	private ScaleMaintenanceNutrient caloriesFromFat;
	private ScaleMaintenanceNutrient cholesterol;
	private ScaleMaintenanceNutrient copper;
	private ScaleMaintenanceNutrient dietaryFiber;
	private ScaleMaintenanceNutrient folacin;
	private ScaleMaintenanceNutrient folicAcid;
	private ScaleMaintenanceNutrient iodine;
	private ScaleMaintenanceNutrient iron;
	private ScaleMaintenanceNutrient magnesium;
	private ScaleMaintenanceNutrient niacin;
	private ScaleMaintenanceNutrient pantothenicAcid;
	private ScaleMaintenanceNutrient phosphorus;
	private ScaleMaintenanceNutrient potassium;
	private ScaleMaintenanceNutrient protein;
	private ScaleMaintenanceNutrient riboflavin;
	private ScaleMaintenanceNutrient saturatedFat;
	private ScaleMaintenanceNutrient sodium;
	private ScaleMaintenanceNutrient sugar;
	private ScaleMaintenanceNutrient sugarAlcohol;
	private ScaleMaintenanceNutrient thiamine;
	private ScaleMaintenanceNutrient totalCarbohydrates;
	private ScaleMaintenanceNutrient totalFat;
	private ScaleMaintenanceNutrient transFat;
	private ScaleMaintenanceNutrient vitaminA;
	private ScaleMaintenanceNutrient vitaminC;
	private ScaleMaintenanceNutrient vitaminD;
	private ScaleMaintenanceNutrient vitaminE;
	private ScaleMaintenanceNutrient vitaminB1;
	private ScaleMaintenanceNutrient vitaminB2;
	private ScaleMaintenanceNutrient vitaminB6;
	private ScaleMaintenanceNutrient vitaminB12;
	private ScaleMaintenanceNutrient zinc;
	private ScaleMaintenanceNutrient addedSugars;
	private String department;
	private String subDepartment;
	private Long plu;

	/**
	 * Returns Upc.
	 *
	 * @return The Upc.
	 **/
	public Long getUpc() {
		return upc;
	}

	/**
	 * Sets the Upc.
	 *
	 * @param upc The Upc.
	 **/
	public ScaleMaintenanceProduct setUpc(Long upc) {
		this.upc = upc;
		return this;
	}

	/**
	 * Returns ShelfLifeDays.
	 *
	 * @return The ShelfLifeDays.
	 **/
	public int getShelfLifeDays() {
		return shelfLifeDays;
	}

	/**
	 * Sets the ShelfLifeDays.
	 *
	 * @param shelfLifeDays The ShelfLifeDays.
	 **/
	public ScaleMaintenanceProduct setShelfLifeDays(int shelfLifeDays) {
		this.shelfLifeDays = shelfLifeDays;
		return this;
	}

	/**
	 * Returns FreezeByDays.
	 *
	 * @return The FreezeByDays.
	 **/
	public int getFreezeByDays() {
		return freezeByDays;
	}

	/**
	 * Sets the FreezeByDays.
	 *
	 * @param freezeByDays The FreezeByDays.
	 **/
	public ScaleMaintenanceProduct setFreezeByDays(int freezeByDays) {
		this.freezeByDays = freezeByDays;
		return this;
	}

	/**
	 * Returns EatByDays.
	 *
	 * @return The EatByDays.
	 **/
	public int getEatByDays() {
		return eatByDays;
	}

	/**
	 * Sets the EatByDays.
	 *
	 * @param eatByDays The EatByDays.
	 **/
	public ScaleMaintenanceProduct setEatByDays(int eatByDays) {
		this.eatByDays = eatByDays;
		return this;
	}

	/**
	 * Returns ActionCode.
	 *
	 * @return The ActionCode.
	 **/
	public long getActionCode() {
		return actionCode;
	}

	/**
	 * Sets the ActionCode.
	 *
	 * @param actionCode The ActionCode.
	 **/
	public ScaleMaintenanceProduct setActionCode(long actionCode) {
		this.actionCode = actionCode;
		return this;
	}

	/**
	 * Returns GraphicsCode.
	 *
	 * @return The GraphicsCode.
	 **/
	public long getGraphicsCode() {
		return graphicsCode;
	}

	/**
	 * Sets the GraphicsCode.
	 *
	 * @param graphicsCode The GraphicsCode.
	 **/
	public ScaleMaintenanceProduct setGraphicsCode(long graphicsCode) {
		this.graphicsCode = graphicsCode;
		return this;
	}

	/**
	 * Returns LabelFormatOne.
	 *
	 * @return The LabelFormatOne.
	 **/
	public long getLabelFormatOne() {
		return labelFormatOne;
	}

	/**
	 * Sets the LabelFormatOne.
	 *
	 * @param labelFormatOne The LabelFormatOne.
	 **/
	public ScaleMaintenanceProduct setLabelFormatOne(long labelFormatOne) {
		this.labelFormatOne = labelFormatOne;
		return this;
	}

	/**
	 * Returns LabelFormatTwo.
	 *
	 * @return The LabelFormatTwo.
	 **/
	public long getLabelFormatTwo() {
		return labelFormatTwo;
	}

	/**
	 * Sets the LabelFormatTwo.
	 *
	 * @param labelFormatTwo The LabelFormatTwo.
	 **/
	public ScaleMaintenanceProduct setLabelFormatTwo(long labelFormatTwo) {
		this.labelFormatTwo = labelFormatTwo;
		return this;
	}

	/**
	 * Returns Grade.
	 *
	 * @return The Grade.
	 **/
	public int getGrade() {
		return grade;
	}

	/**
	 * Sets the Grade.
	 *
	 * @param grade The Grade.
	 **/
	public ScaleMaintenanceProduct setGrade(int grade) {
		this.grade = grade;
		return this;
	}

	/**
	 * Returns ServiceCounterTare.
	 *
	 * @return The ServiceCounterTare.
	 **/
	public double getServiceCounterTare() {
		return serviceCounterTare;
	}

	/**
	 * Sets the ServiceCounterTare.
	 *
	 * @param serviceCounterTare The ServiceCounterTare.
	 **/
	public ScaleMaintenanceProduct setServiceCounterTare(double serviceCounterTare) {
		this.serviceCounterTare = serviceCounterTare;
		return this;
	}

	/**
	 * Returns IngredientStatement.
	 *
	 * @return The IngredientStatement.
	 **/
	public long getIngredientStatement() {
		return ingredientStatement;
	}

	/**
	 * Sets the IngredientStatement.
	 *
	 * @param ingredientStatement The IngredientStatement.
	 **/
	public ScaleMaintenanceProduct setIngredientStatement(long ingredientStatement) {
		this.ingredientStatement = ingredientStatement;
		return this;
	}

	/**
	 * Returns NutrientStatementCode.
	 *
	 * @return The NutrientStatementCode.
	 **/
	public long getNutrientStatementCode() {
		return nutrientStatementCode;
	}

	/**
	 * Sets the NutrientStatementCode.
	 *
	 * @param nutrientStatementCode The NutrientStatementCode.
	 **/
	public ScaleMaintenanceProduct setNutrientStatementCode(long nutrientStatementCode) {
		this.nutrientStatementCode = nutrientStatementCode;
		return this;
	}

	/**
	 * Returns NutrientStatement.
	 *
	 * @return The NutrientStatement.
	 **/
	public ScaleMaintenanceNutrientStatement getNutrientStatement() {
		return nutrientStatement;
	}

	/**
	 * Sets the NutrientStatement.
	 *
	 * @param nutrientStatement The NutrientStatement.
	 **/
	public ScaleMaintenanceProduct setNutrientStatement(ScaleMaintenanceNutrientStatement nutrientStatement) {
		this.nutrientStatement = nutrientStatement;
		return this;
	}

	/**
	 * Returns PrePackTare.
	 *
	 * @return The PrePackTare.
	 **/
	public double getPrePackTare() {
		return prePackTare;
	}

	/**
	 * Sets the PrePackTare.
	 *
	 * @param prePackTare The PrePackTare.
	 **/
	public ScaleMaintenanceProduct setPrePackTare(double prePackTare) {
		this.prePackTare = prePackTare;
		return this;
	}

	/**
	 * Returns NetWeight.
	 *
	 * @return The NetWeight.
	 **/
	public double getNetWeight() {
		return netWeight;
	}

	/**
	 * Sets the NetWeight.
	 *
	 * @param netWeight The NetWeight.
	 **/
	public ScaleMaintenanceProduct setNetWeight(double netWeight) {
		this.netWeight = netWeight;
		return this;
	}

	/**
	 * Returns ForceTare.
	 *
	 * @return The ForceTare.
	 **/
	public boolean isForceTare() {
		return forceTare;
	}

	/**
	 * Sets the ForceTare.
	 *
	 * @param forceTare The ForceTare.
	 **/
	public ScaleMaintenanceProduct setForceTare(boolean forceTare) {
		this.forceTare = forceTare;
		return this;
	}

	/**
	 * Returns PriceOverride.
	 *
	 * @return The PriceOverride.
	 **/
	public boolean isPriceOverride() {
		return priceOverride;
	}

	/**
	 * Sets the PriceOverride.
	 *
	 * @param priceOverride The PriceOverride.
	 **/
	public ScaleMaintenanceProduct setPriceOverride(boolean priceOverride) {
		this.priceOverride = priceOverride;
		return this;
	}

	/**
	 * Returns EnglishDescriptionOne.
	 *
	 * @return The EnglishDescriptionOne.
	 **/
	public String getEnglishDescriptionOne() {
		return englishDescriptionOne;
	}

	/**
	 * Sets the EnglishDescriptionOne.
	 *
	 * @param englishDescriptionOne The EnglishDescriptionOne.
	 **/
	public ScaleMaintenanceProduct setEnglishDescriptionOne(String englishDescriptionOne) {
		this.englishDescriptionOne = englishDescriptionOne;
		return this;
	}

	/**
	 * Returns EnglishDescriptionTwo.
	 *
	 * @return The EnglishDescriptionTwo.
	 **/
	public String getEnglishDescriptionTwo() {
		return englishDescriptionTwo;
	}

	/**
	 * Sets the EnglishDescriptionTwo.
	 *
	 * @param englishDescriptionTwo The EnglishDescriptionTwo.
	 **/
	public ScaleMaintenanceProduct setEnglishDescriptionTwo(String englishDescriptionTwo) {
		this.englishDescriptionTwo = englishDescriptionTwo;
		return this;
	}

	/**
	 * Returns EnglishDescriptionThree.
	 *
	 * @return The EnglishDescriptionThree.
	 **/
	public String getEnglishDescriptionThree() {
		return englishDescriptionThree;
	}

	/**
	 * Sets the EnglishDescriptionThree.
	 *
	 * @param englishDescriptionThree The EnglishDescriptionThree.
	 **/
	public ScaleMaintenanceProduct setEnglishDescriptionThree(String englishDescriptionThree) {
		this.englishDescriptionThree = englishDescriptionThree;
		return this;
	}

	/**
	 * Returns EnglishDescriptionFour.
	 *
	 * @return The EnglishDescriptionFour.
	 **/
	public String getEnglishDescriptionFour() {
		return englishDescriptionFour;
	}

	/**
	 * Sets the EnglishDescriptionFour.
	 *
	 * @param englishDescriptionFour The EnglishDescriptionFour.
	 **/
	public ScaleMaintenanceProduct setEnglishDescriptionFour(String englishDescriptionFour) {
		this.englishDescriptionFour = englishDescriptionFour;
		return this;
	}

	/**
	 * Returns SpanishDescriptionOne.
	 *
	 * @return The SpanishDescriptionOne.
	 **/
	public String getSpanishDescriptionOne() {
		return spanishDescriptionOne;
	}

	/**
	 * Sets the SpanishDescriptionOne.
	 *
	 * @param spanishDescriptionOne The SpanishDescriptionOne.
	 **/
	public ScaleMaintenanceProduct setSpanishDescriptionOne(String spanishDescriptionOne) {
		this.spanishDescriptionOne = spanishDescriptionOne;
		return this;
	}

	/**
	 * Returns SpanishDescriptionTwo.
	 *
	 * @return The SpanishDescriptionTwo.
	 **/
	public String getSpanishDescriptionTwo() {
		return spanishDescriptionTwo;
	}

	/**
	 * Sets the SpanishDescriptionTwo.
	 *
	 * @param spanishDescriptionTwo The SpanishDescriptionTwo.
	 **/
	public ScaleMaintenanceProduct setSpanishDescriptionTwo(String spanishDescriptionTwo) {
		this.spanishDescriptionTwo = spanishDescriptionTwo;
		return this;
	}

	/**
	 * Returns SpanishDescriptionThree.
	 *
	 * @return The SpanishDescriptionThree.
	 **/
	public String getSpanishDescriptionThree() {
		return spanishDescriptionThree;
	}

	/**
	 * Sets the SpanishDescriptionThree.
	 *
	 * @param spanishDescriptionThree The SpanishDescriptionThree.
	 **/
	public ScaleMaintenanceProduct setSpanishDescriptionThree(String spanishDescriptionThree) {
		this.spanishDescriptionThree = spanishDescriptionThree;
		return this;
	}

	/**
	 * Returns SpanishDescriptionFour.
	 *
	 * @return The SpanishDescriptionFour.
	 **/
	public String getSpanishDescriptionFour() {
		return spanishDescriptionFour;
	}

	/**
	 * Sets the SpanishDescriptionFour.
	 *
	 * @param spanishDescriptionFour The SpanishDescriptionFour.
	 **/
	public ScaleMaintenanceProduct setSpanishDescriptionFour(String spanishDescriptionFour) {
		this.spanishDescriptionFour = spanishDescriptionFour;
		return this;
	}

	/**
	 * Returns IngredientText.
	 *
	 * @return The IngredientText.
	 **/
	public String getIngredientText() {
		return ingredientText;
	}

	/**
	 * Sets the IngredientText.
	 *
	 * @param ingredientText The IngredientText.
	 **/
	public ScaleMaintenanceProduct setIngredientText(String ingredientText) {
		this.ingredientText = ingredientText;
		return this;
	}

	/**
	 * Returns AscorbicAcid.
	 *
	 * @return The AscorbicAcid.
	 **/
	public ScaleMaintenanceNutrient getAscorbicAcid() {
		return ascorbicAcid;
	}

	/**
	 * Sets the AscorbicAcid.
	 *
	 * @param ascorbicAcid The AscorbicAcid.
	 **/
	public ScaleMaintenanceProduct setAscorbicAcid(ScaleMaintenanceNutrient ascorbicAcid) {
		this.ascorbicAcid = ascorbicAcid;
		return this;
	}

	/**
	 * Returns Biotin.
	 *
	 * @return The Biotin.
	 **/
	public ScaleMaintenanceNutrient getBiotin() {
		return biotin;
	}

	/**
	 * Sets the Biotin.
	 *
	 * @param biotin The Biotin.
	 **/
	public ScaleMaintenanceProduct setBiotin(ScaleMaintenanceNutrient biotin) {
		this.biotin = biotin;
		return this;
	}

	/**
	 * Returns Calcium.
	 *
	 * @return The Calcium.
	 **/
	public ScaleMaintenanceNutrient getCalcium() {
		return calcium;
	}

	/**
	 * Sets the Calcium.
	 *
	 * @param calcium The Calcium.
	 **/
	public ScaleMaintenanceProduct setCalcium(ScaleMaintenanceNutrient calcium) {
		this.calcium = calcium;
		return this;
	}

	/**
	 * Returns Calories.
	 *
	 * @return The Calories.
	 **/
	public ScaleMaintenanceNutrient getCalories() {
		return calories;
	}

	/**
	 * Sets the Calories.
	 *
	 * @param calories The Calories.
	 **/
	public ScaleMaintenanceProduct setCalories(ScaleMaintenanceNutrient calories) {
		this.calories = calories;
		return this;
	}

	/**
	 * Returns CaloriesFromFat.
	 *
	 * @return The CaloriesFromFat.
	 **/
	public ScaleMaintenanceNutrient getCaloriesFromFat() {
		return caloriesFromFat;
	}

	/**
	 * Sets the CaloriesFromFat.
	 *
	 * @param caloriesFromFat The CaloriesFromFat.
	 **/
	public ScaleMaintenanceProduct setCaloriesFromFat(ScaleMaintenanceNutrient caloriesFromFat) {
		this.caloriesFromFat = caloriesFromFat;
		return this;
	}

	/**
	 * Returns Cholesterol.
	 *
	 * @return The Cholesterol.
	 **/
	public ScaleMaintenanceNutrient getCholesterol() {
		return cholesterol;
	}

	/**
	 * Sets the Cholesterol.
	 *
	 * @param cholesterol The Cholesterol.
	 **/
	public ScaleMaintenanceProduct setCholesterol(ScaleMaintenanceNutrient cholesterol) {
		this.cholesterol = cholesterol;
		return this;
	}

	/**
	 * Returns Copper.
	 *
	 * @return The Copper.
	 **/
	public ScaleMaintenanceNutrient getCopper() {
		return copper;
	}

	/**
	 * Sets the Copper.
	 *
	 * @param copper The Copper.
	 **/
	public ScaleMaintenanceProduct setCopper(ScaleMaintenanceNutrient copper) {
		this.copper = copper;
		return this;
	}

	/**
	 * Returns DietaryFiber.
	 *
	 * @return The DietaryFiber.
	 **/
	public ScaleMaintenanceNutrient getDietaryFiber() {
		return dietaryFiber;
	}

	/**
	 * Sets the DietaryFiber.
	 *
	 * @param dietaryFiber The DietaryFiber.
	 **/
	public ScaleMaintenanceProduct setDietaryFiber(ScaleMaintenanceNutrient dietaryFiber) {
		this.dietaryFiber = dietaryFiber;
		return this;
	}

	/**
	 * Returns Folacin.
	 *
	 * @return The Folacin.
	 **/
	public ScaleMaintenanceNutrient getFolacin() {
		return folacin;
	}

	/**
	 * Sets the Folacin.
	 *
	 * @param folacin The Folacin.
	 **/
	public ScaleMaintenanceProduct setFolacin(ScaleMaintenanceNutrient folacin) {
		this.folacin = folacin;
		return this;
	}

	/**
	 * Returns FolicAcid.
	 *
	 * @return The FolicAcid.
	 **/
	public ScaleMaintenanceNutrient getFolicAcid() {
		return folicAcid;
	}

	/**
	 * Sets the FolicAcid.
	 *
	 * @param folicAcid The FolicAcid.
	 **/
	public ScaleMaintenanceProduct setFolicAcid(ScaleMaintenanceNutrient folicAcid) {
		this.folicAcid = folicAcid;
		return this;
	}

	/**
	 * Returns Iodine.
	 *
	 * @return The Iodine.
	 **/
	public ScaleMaintenanceNutrient getIodine() {
		return iodine;
	}

	/**
	 * Sets the Iodine.
	 *
	 * @param iodine The Iodine.
	 **/
	public ScaleMaintenanceProduct setIodine(ScaleMaintenanceNutrient iodine) {
		this.iodine = iodine;
		return this;
	}

	/**
	 * Returns Iron.
	 *
	 * @return The Iron.
	 **/
	public ScaleMaintenanceNutrient getIron() {
		return iron;
	}

	/**
	 * Sets the Iron.
	 *
	 * @param iron The Iron.
	 **/
	public ScaleMaintenanceProduct setIron(ScaleMaintenanceNutrient iron) {
		this.iron = iron;
		return this;
	}

	/**
	 * Returns Magnesium.
	 *
	 * @return The Magnesium.
	 **/
	public ScaleMaintenanceNutrient getMagnesium() {
		return magnesium;
	}

	/**
	 * Sets the Magnesium.
	 *
	 * @param magnesium The Magnesium.
	 **/
	public ScaleMaintenanceProduct setMagnesium(ScaleMaintenanceNutrient magnesium) {
		this.magnesium = magnesium;
		return this;
	}

	/**
	 * Returns Niacin.
	 *
	 * @return The Niacin.
	 **/
	public ScaleMaintenanceNutrient getNiacin() {
		return niacin;
	}

	/**
	 * Sets the Niacin.
	 *
	 * @param niacin The Niacin.
	 **/
	public ScaleMaintenanceProduct setNiacin(ScaleMaintenanceNutrient niacin) {
		this.niacin = niacin;
		return this;
	}

	/**
	 * Returns PantothenicAcid.
	 *
	 * @return The PantothenicAcid.
	 **/
	public ScaleMaintenanceNutrient getPantothenicAcid() {
		return pantothenicAcid;
	}

	/**
	 * Sets the PantothenicAcid.
	 *
	 * @param pantothenicAcid The PantothenicAcid.
	 **/
	public ScaleMaintenanceProduct setPantothenicAcid(ScaleMaintenanceNutrient pantothenicAcid) {
		this.pantothenicAcid = pantothenicAcid;
		return this;
	}

	/**
	 * Returns Phosphorus.
	 *
	 * @return The Phosphorus.
	 **/
	public ScaleMaintenanceNutrient getPhosphorus() {
		return phosphorus;
	}

	/**
	 * Sets the Phosphorus.
	 *
	 * @param phosphorus The Phosphorus.
	 **/
	public ScaleMaintenanceProduct setPhosphorus(ScaleMaintenanceNutrient phosphorus) {
		this.phosphorus = phosphorus;
		return this;
	}

	/**
	 * Returns Potassium.
	 *
	 * @return The Potassium.
	 **/
	public ScaleMaintenanceNutrient getPotassium() {
		return potassium;
	}

	/**
	 * Sets the Potassium.
	 *
	 * @param potassium The Potassium.
	 **/
	public ScaleMaintenanceProduct setPotassium(ScaleMaintenanceNutrient potassium) {
		this.potassium = potassium;
		return this;
	}

	/**
	 * Returns Protein.
	 *
	 * @return The Protein.
	 **/
	public ScaleMaintenanceNutrient getProtein() {
		return protein;
	}

	/**
	 * Sets the Protein.
	 *
	 * @param protein The Protein.
	 **/
	public ScaleMaintenanceProduct setProtein(ScaleMaintenanceNutrient protein) {
		this.protein = protein;
		return this;
	}

	/**
	 * Returns Riboflavin.
	 *
	 * @return The Riboflavin.
	 **/
	public ScaleMaintenanceNutrient getRiboflavin() {
		return riboflavin;
	}

	/**
	 * Sets the Riboflavin.
	 *
	 * @param riboflavin The Riboflavin.
	 **/
	public ScaleMaintenanceProduct setRiboflavin(ScaleMaintenanceNutrient riboflavin) {
		this.riboflavin = riboflavin;
		return this;
	}

	/**
	 * Returns SaturatedFat.
	 *
	 * @return The SaturatedFat.
	 **/
	public ScaleMaintenanceNutrient getSaturatedFat() {
		return saturatedFat;
	}

	/**
	 * Sets the SaturatedFat.
	 *
	 * @param saturatedFat The SaturatedFat.
	 **/
	public ScaleMaintenanceProduct setSaturatedFat(ScaleMaintenanceNutrient saturatedFat) {
		this.saturatedFat = saturatedFat;
		return this;
	}

	/**
	 * Returns Sodium.
	 *
	 * @return The Sodium.
	 **/
	public ScaleMaintenanceNutrient getSodium() {
		return sodium;
	}

	/**
	 * Sets the Sodium.
	 *
	 * @param sodium The Sodium.
	 **/
	public ScaleMaintenanceProduct setSodium(ScaleMaintenanceNutrient sodium) {
		this.sodium = sodium;
		return this;
	}

	/**
	 * Returns Sugar.
	 *
	 * @return The Sugar.
	 **/
	public ScaleMaintenanceNutrient getSugar() {
		return sugar;
	}

	/**
	 * Sets the Sugar.
	 *
	 * @param sugar The Sugar.
	 **/
	public ScaleMaintenanceProduct setSugar(ScaleMaintenanceNutrient sugar) {
		this.sugar = sugar;
		return this;
	}

	/**
	 * Returns SugarAlcohol.
	 *
	 * @return The SugarAlcohol.
	 **/
	public ScaleMaintenanceNutrient getSugarAlcohol() {
		return sugarAlcohol;
	}

	/**
	 * Sets the SugarAlcohol.
	 *
	 * @param sugarAlcohol The SugarAlcohol.
	 **/
	public ScaleMaintenanceProduct setSugarAlcohol(ScaleMaintenanceNutrient sugarAlcohol) {
		this.sugarAlcohol = sugarAlcohol;
		return this;
	}

	/**
	 * Returns Thiamine.
	 *
	 * @return The Thiamine.
	 **/
	public ScaleMaintenanceNutrient getThiamine() {
		return thiamine;
	}

	/**
	 * Sets the Thiamine.
	 *
	 * @param thiamine The Thiamine.
	 **/
	public ScaleMaintenanceProduct setThiamine(ScaleMaintenanceNutrient thiamine) {
		this.thiamine = thiamine;
		return this;
	}

	/**
	 * Returns TotalCarbohydrates.
	 *
	 * @return The TotalCarbohydrates.
	 **/
	public ScaleMaintenanceNutrient getTotalCarbohydrates() {
		return totalCarbohydrates;
	}

	/**
	 * Sets the TotalCarbohydrates.
	 *
	 * @param totalCarbohydrates The TotalCarbohydrates.
	 **/
	public ScaleMaintenanceProduct setTotalCarbohydrates(ScaleMaintenanceNutrient totalCarbohydrates) {
		this.totalCarbohydrates = totalCarbohydrates;
		return this;
	}

	/**
	 * Returns TotalFat.
	 *
	 * @return The TotalFat.
	 **/
	public ScaleMaintenanceNutrient getTotalFat() {
		return totalFat;
	}

	/**
	 * Sets the TotalFat.
	 *
	 * @param totalFat The TotalFat.
	 **/
	public ScaleMaintenanceProduct setTotalFat(ScaleMaintenanceNutrient totalFat) {
		this.totalFat = totalFat;
		return this;
	}

	/**
	 * Returns TransFat.
	 *
	 * @return The TransFat.
	 **/
	public ScaleMaintenanceNutrient getTransFat() {
		return transFat;
	}

	/**
	 * Sets the TransFat.
	 *
	 * @param transFat The TransFat.
	 **/
	public ScaleMaintenanceProduct setTransFat(ScaleMaintenanceNutrient transFat) {
		this.transFat = transFat;
		return this;
	}

	/**
	 * Returns VitaminA.
	 *
	 * @return The VitaminA.
	 **/
	public ScaleMaintenanceNutrient getVitaminA() {
		return vitaminA;
	}

	/**
	 * Sets the VitaminA.
	 *
	 * @param vitaminA The VitaminA.
	 **/
	public ScaleMaintenanceProduct setVitaminA(ScaleMaintenanceNutrient vitaminA) {
		this.vitaminA = vitaminA;
		return this;
	}

	/**
	 * Returns VitaminC.
	 *
	 * @return The VitaminC.
	 **/
	public ScaleMaintenanceNutrient getVitaminC() {
		return vitaminC;
	}

	/**
	 * Sets the VitaminC.
	 *
	 * @param vitaminC The VitaminC.
	 **/
	public ScaleMaintenanceProduct setVitaminC(ScaleMaintenanceNutrient vitaminC) {
		this.vitaminC = vitaminC;
		return this;
	}

	/**
	 * Returns VitaminD.
	 *
	 * @return The VitaminD.
	 **/
	public ScaleMaintenanceNutrient getVitaminD() {
		return vitaminD;
	}

	/**
	 * Sets the VitaminD.
	 *
	 * @param vitaminD The VitaminD.
	 **/
	public ScaleMaintenanceProduct setVitaminD(ScaleMaintenanceNutrient vitaminD) {
		this.vitaminD = vitaminD;
		return this;
	}

	/**
	 * Returns VitaminE.
	 *
	 * @return The VitaminE.
	 **/
	public ScaleMaintenanceNutrient getVitaminE() {
		return vitaminE;
	}

	/**
	 * Sets the VitaminE.
	 *
	 * @param vitaminE The VitaminE.
	 **/
	public ScaleMaintenanceProduct setVitaminE(ScaleMaintenanceNutrient vitaminE) {
		this.vitaminE = vitaminE;
		return this;
	}

	/**
	 * Returns VitaminB1.
	 *
	 * @return The VitaminB1.
	 **/
	public ScaleMaintenanceNutrient getVitaminB1() {
		return vitaminB1;
	}

	/**
	 * Sets the VitaminB1.
	 *
	 * @param vitaminB1 The VitaminB1.
	 **/
	public ScaleMaintenanceProduct setVitaminB1(ScaleMaintenanceNutrient vitaminB1) {
		this.vitaminB1 = vitaminB1;
		return this;
	}

	/**
	 * Returns VitaminB2.
	 *
	 * @return The VitaminB2.
	 **/
	public ScaleMaintenanceNutrient getVitaminB2() {
		return vitaminB2;
	}

	/**
	 * Sets the VitaminB2.
	 *
	 * @param vitaminB2 The VitaminB2.
	 **/
	public ScaleMaintenanceProduct setVitaminB2(ScaleMaintenanceNutrient vitaminB2) {
		this.vitaminB2 = vitaminB2;
		return this;
	}

	/**
	 * Returns VitaminB6.
	 *
	 * @return The VitaminB6.
	 **/
	public ScaleMaintenanceNutrient getVitaminB6() {
		return vitaminB6;
	}

	/**
	 * Sets the VitaminB6.
	 *
	 * @param vitaminB6 The VitaminB6.
	 **/
	public ScaleMaintenanceProduct setVitaminB6(ScaleMaintenanceNutrient vitaminB6) {
		this.vitaminB6 = vitaminB6;
		return this;
	}

	/**
	 * Returns VitaminB12.
	 *
	 * @return The VitaminB12.
	 **/
	public ScaleMaintenanceNutrient getVitaminB12() {
		return vitaminB12;
	}

	/**
	 * Sets the VitaminB12.
	 *
	 * @param vitaminB12 The VitaminB12.
	 **/
	public ScaleMaintenanceProduct setVitaminB12(ScaleMaintenanceNutrient vitaminB12) {
		this.vitaminB12 = vitaminB12;
		return this;
	}

	/**
	 * Returns Zinc.
	 *
	 * @return The Zinc.
	 **/
	public ScaleMaintenanceNutrient getZinc() {
		return zinc;
	}

	/**
	 * Sets the Zinc.
	 *
	 * @param zinc The Zinc.
	 **/
	public ScaleMaintenanceProduct setZinc(ScaleMaintenanceNutrient zinc) {
		this.zinc = zinc;
		return this;
	}

	/**
	 * Returns Department.
	 *
	 * @return The Department.
	 **/
	public String getDepartment() {
		return department;
	}

	/**
	 * Sets the Department.
	 *
	 * @param department The Department.
	 **/
	public ScaleMaintenanceProduct setDepartment(String department) {
		this.department = department;
		return this;
	}

	/**
	 * Returns SubDepartment.
	 *
	 * @return The SubDepartment.
	 **/
	public String getSubDepartment() {
		return subDepartment;
	}

	/**
	 * Sets the SubDepartment.
	 *
	 * @param subDepartment The SubDepartment.
	 **/
	public ScaleMaintenanceProduct setSubDepartment(String subDepartment) {
		this.subDepartment = subDepartment;
		return this;
	}

	/**
	 * Returns Plu.
	 *
	 * @return The Plu.
	 **/
	public Long getPlu() {
		return plu;
	}

	/**
	 * Sets the Plu.
	 *
	 * @param plu The Plu.
	 **/
	public ScaleMaintenanceProduct setPlu(Long plu) {
		this.plu = plu;
		return this;
	}

	/**
	 * Returns AddedSugars.
	 *
	 * @return The AddedSugars.
	 **/
	public ScaleMaintenanceNutrient getAddedSugars() {
		return addedSugars;
	}

	/**
	 * Sets the AddedSugars.
	 *
	 * @param addedSugars The AddedSugars.
	 **/
	public ScaleMaintenanceProduct setAddedSugars(ScaleMaintenanceNutrient addedSugars) {
		this.addedSugars = addedSugars;
		return this;
	}

	/**
	 * Returns a String representation of the object.
	 *
	 * @return A String representation of the object.
	 */
	@Override
	public String toString() {
		return "ScaleMaintenanceProduct{" +
				"upc=" + upc +
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
				", ingredientText='" + ingredientText + '\'' +
				", ascorbicAcid=" + ascorbicAcid +
				", biotin=" + biotin +
				", calcium=" + calcium +
				", calories=" + calories +
				", caloriesFromFat=" + caloriesFromFat +
				", cholesterol=" + cholesterol +
				", copper=" + copper +
				", dietaryFiber=" + dietaryFiber +
				", folacin=" + folacin +
				", folicAcid=" + folicAcid +
				", iodine=" + iodine +
				", iron=" + iron +
				", magnesium=" + magnesium +
				", niacin=" + niacin +
				", pantothenicAcid=" + pantothenicAcid +
				", phosphorus=" + phosphorus +
				", potassium=" + potassium +
				", protein=" + protein +
				", riboflavin=" + riboflavin +
				", saturatedFat=" + saturatedFat +
				", sodium=" + sodium +
				", sugar=" + sugar +
				", sugarAlcohol=" + sugarAlcohol +
				", thiamine=" + thiamine +
				", totalCarbohydrates=" + totalCarbohydrates +
				", totalFat=" + totalFat +
				", transFat=" + transFat +
				", vitaminA=" + vitaminA +
				", vitaminC=" + vitaminC +
				", vitaminD=" + vitaminD +
				", vitaminE=" + vitaminE +
				", vitaminB1=" + vitaminB1 +
				", vitaminB2=" + vitaminB2 +
				", vitaminB6=" + vitaminB6 +
				", vitaminB12=" + vitaminB12 +
				", zinc=" + zinc +
				", department='" + department + '\'' +
				", plu=" + plu +
				'}';
	}

	@Override
	public <R> R map(Function<? super ScaleMaintenanceProduct, ? extends R> mapper) {
		return mapper.apply(this);
	}

	@Override
	public ScaleMaintenanceProduct validate(Function<? super ScaleMaintenanceProduct, ? extends ScaleMaintenanceProduct> validator) {
		return validator.apply(this);
	}
}
