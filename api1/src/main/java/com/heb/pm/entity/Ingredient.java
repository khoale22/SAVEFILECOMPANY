package com.heb.pm.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.springframework.data.domain.Sort;
 // dB2Oracle changes vn00907 starts
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;
 // dB2Oracle changes vn00907
import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents an ingredient in the scale system.
 *
 * @author m594201
 * @since 2.0.9
 */
@Entity
@Table(name="sl_ingredient")
 // dB2Oracle changes vn00907 starts
@TypeDefs({
@TypeDef(name = "fixedLengthChar", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharType.class),
@TypeDef(name = "fixedLengthCharPK", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharTypePK.class)
})
 // dB2Oracle changes vn00907
public class Ingredient implements Serializable {

	public static final String DELETE_SW = "D";

	private static final long serialVersionUID = 1L;
	
	private static final String SOI_DISPLAY_TEXT = "SOI";
	private static final String EXTENDED_DESCRIPTION_FORMAT = "(%s)";
	private static final String DISPLAY_NAME_FORMAT = "%s [%s] %s %s";
	private static final String INGREDIENT_CODE_SORT_FIELD = "ingredientCodeAsNumber";
	private static final String INGREDIENT_DESCRIPTION_SORT_FIELD = "ingredientDescription";

	private static final String SUB_INGREDIENT_LIST_BEGIN = "(";
	private static final String SUB_INGREDIENT_LIST_END = ")";
	private static final String INGREDIENT_DELIMITER = ", ";

	@Id
	@Column(name = "ingredient_code")
	 // dB2Oracle changes vn00907
	@Type(type="fixedLengthCharPK")  
	private String ingredientCode;

	@Column(name = "soi_flag")
	private boolean soiFlag;

	@Column(name = "maint_function")
	//db2o changes  vn00907
	@Type(type="fixedLengthChar")    
	private String maintFunction;

	@Column(name = "pd_ingrd_ext_des")
	//db2o changes  vn00907
	@Type(type="fixedLengthChar")    
	private String ingredientCatDescription;

	@Column(name = "pd_ingrd_cat_cd")
	private long categoryCode;

	@Column(name = "ingredient_desc")
	private String ingredientDescription;

	@Formula("to_number(ingredient_code)")
	private int ingredientCodeAsNumber;

	@Transient
	private int order;

	@NotFound(action = NotFoundAction.IGNORE)
	@OneToMany(mappedBy = "ingredientMaster", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonManagedReference
	@OrderBy(value = "soi_sequence")
	private List<IngredientSub> ingredientSubs = new ArrayList<>();

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "pd_ingrd_cat_cd", referencedColumnName = "pd_ingrd_cat_cd", insertable = false,  updatable = false)
	private IngredientCategory ingredientCategory;

	@JsonIgnoreProperties("ingredient")
	@OneToMany(mappedBy = "ingredient",fetch = FetchType.LAZY)
	private List<IngredientStatementDetail> statementDetails;

	/**
	 * Gets statement details.
	 *
	 * @return the statement details
	 */
	public List<IngredientStatementDetail> getStatementDetails() {
		return statementDetails;
	}

	/**
	 * Sets statement details.
	 *
	 * @param statementDetails the statement details
	 */
	public void setStatementDetails(List<IngredientStatementDetail> statementDetails) {
		this.statementDetails = statementDetails;
	}

	/**
	 * Gets ingredient subs.
	 *
	 * @return the ingredient subs
	 */
	public List<IngredientSub> getIngredientSubs() {
		return ingredientSubs;
	}

	/**
	 * Sets ingredient subs.
	 *
	 * @param ingredientSubs the ingredient subs
	 */
	public void setIngredientSubs(List<IngredientSub> ingredientSubs) {
		this.ingredientSubs = ingredientSubs;
	}

	/**
	 * Gets ingredient category.
	 *
	 * @return the ingredient category
	 */
	public IngredientCategory getIngredientCategory() {
		return ingredientCategory;
	}

	/**
	 * Sets ingredient category.
	 *
	 * @param ingredientCategory the ingredient category
	 */
	public void setIngredientCategory(IngredientCategory ingredientCategory) {
		this.ingredientCategory = ingredientCategory;
	}

	/**
	 * Gets ingredient code.
	 *
	 * @return the ingredient code
	 */
	public String getIngredientCode() {
		return ingredientCode.trim();
	}

	/**
	 * Sets ingredient code.
	 *
	 * @param ingredientCode the ingredient code
	 */
	public void setIngredientCode(String ingredientCode) {
		this.ingredientCode = ingredientCode;
	}

	/**
	 * Gets soi flag.
	 *
	 * @return the soi flag
	 */
	public boolean isSoiFlag() {
		return soiFlag;
	}

	/**
	 * Sets soi flag.
	 *
	 * @param soiFlag the soi flag
	 */
	public void setSoiFlag(boolean soiFlag) {
		this.soiFlag = soiFlag;
	}

	/**
	 * Gets maint function.
	 *
	 * @return the maint function
	 */
	public String getMaintFunction() {
		return maintFunction;
	}

	/**
	 * Sets maint function.
	 *
	 * @param maintFunction the maint function
	 */
	public void setMaintFunction(String maintFunction) {
		this.maintFunction = maintFunction;
	}

	/**
	 * Gets ingredient cat description.
	 *
	 * @return the ingredient cat description
	 */
	public String getIngredientCatDescription() {
		return ingredientCatDescription;
	}

	/**
	 * Sets ingredient cat description.
	 *
	 * @param ingredientCatDescription the ingredient cat description
	 */
	public void setIngredientCatDescription(String ingredientCatDescription) {
		this.ingredientCatDescription = ingredientCatDescription;
	}

	/**
	 * Gets category code.
	 *
	 * @return the category code
	 */
	public long getCategoryCode() {
		return categoryCode;
	}

	/**
	 * Sets category code.
	 *
	 * @param categoryCode the category code
	 */
	public void setCategoryCode(long categoryCode) {
		this.categoryCode = categoryCode;
	}

	/**
	 * Gets ingredient description.
	 *
	 * @return the ingredient description
	 */
	public String getIngredientDescription() {
		return ingredientDescription;
	}

	/**
	 * Sets ingredient description.
	 *
	 * @param ingredientDescription the ingredient description
	 */
	public void setIngredientDescription(String ingredientDescription) {
		this.ingredientDescription = ingredientDescription;
	}

	/**
	 * Gets order.
	 *
	 * @return the order
	 */
	public int getOrder() {
		return order;
	}

	/**
	 * Sets order.
	 *
	 * @param order the order
	 */
	public void setOrder(int order) {
		this.order = order;
	}

	/**
	 * Returns a full string list of the sub-ingredients of this ingredient (does not include this ingredient's
	 * description. It will match what would be displayed on a nutrition label.
	 *
	 * @return A full string list of the sub-ingredients of this ingredient.
	 */
	public String getSubIngredientsDisplayText() {

		StringBuilder subIngredientDisplayBuilder = new StringBuilder();

		if (this.getIngredientSubs() != null && !this.getIngredientSubs().isEmpty()) {

			// Start with an open paren.
			subIngredientDisplayBuilder.append(Ingredient.SUB_INGREDIENT_LIST_BEGIN);

			for (int i = 0; i < this.getIngredientSubs().size(); i++) {

				// If this isn't the first element, add a comma before adding the text.
				if (i != 0) {
					subIngredientDisplayBuilder.append(Ingredient.INGREDIENT_DELIMITER);
				}

				// Add the full description of the sub-ingredient.
				subIngredientDisplayBuilder.append(this.getIngredientSubs().get(i).getSubIngredient().getDisplayText());
			}

			// Add the closing paren.
			subIngredientDisplayBuilder.append(Ingredient.SUB_INGREDIENT_LIST_END);
		}

		return subIngredientDisplayBuilder.toString();
	}

	/**
	 * Returns a full string description of this ingredient including its sub-ingredients. It will match
	 * what would be displayed on a nutrition label.
	 *
	 * @return A full string description of this ingredient including its sub-ingredients.
	 */
	public String getDisplayText() {
		StringBuilder returnString = new StringBuilder();

		// Add the ingredient's description.
		returnString.append(this.getIngredientDescription());

		// Delegate the sub-ingredients to this function. This is somewhat inefficient as this method will get called
		// twice during JSON serialization. I tried to do caching of the descriptions, but it was difficult to track
		// when the cached description needs to change (more work than I thought would save time during application
		// run time).
		returnString.append(this.getSubIngredientsDisplayText());
		return returnString.toString();
	}

	/**
	 * Get normalized id string.
	 *
	 * @return the string
	 */
	public String getNormalizedId(){return String.valueOf(this.ingredientCode);}

	/**
	 * Returns an extended string representation of this ingredient suitable for display on a GUI.
	 *
	 * @return An extended string representation of this ingredient suitable for display on a GUI.
	 */
	public String getDisplayName() {

		String soiString = this.ingredientSubs.isEmpty() ? StringUtils.EMPTY :
				Ingredient.SOI_DISPLAY_TEXT;

		String extendedString = StringUtils.isBlank(this.ingredientCatDescription) ? StringUtils.EMPTY :
				String.format(Ingredient.EXTENDED_DESCRIPTION_FORMAT, this.ingredientCatDescription.trim());

		return String.format(Ingredient.DISPLAY_NAME_FORMAT, this.ingredientDescription.trim(),
				this.ingredientCode.trim(), soiString, extendedString);
	}

	/**
	 * Returns a String representation of the object.
	 *
	 * @return A String representation of the object.
	 */
	@Override
	public String toString() {
		return "Ingredient{" +
				"ingredientCode=" + ingredientCode +
				", soiFlag='" + soiFlag + '\'' +
				", maintFunction='" + maintFunction + '\'' +
				", ingredientCatDescription='" + ingredientCatDescription + '\'' +
				", categoryCode=" + categoryCode +
				", ingredientDescription='" + ingredientDescription + '\'' +
				'}';
	}

	/**
	 * Returns the default sort order for the sl_ingredient table.
	 *
	 * @return The default sort order for the sl_ingredient table.
	 */
	public static Sort getDefaultSort() {
		return  new Sort(
				new Sort.Order(Sort.Direction.ASC, Ingredient.INGREDIENT_CODE_SORT_FIELD)
		);
	}

	/**
	 * Returns a sort by ingredient code.
	 *
	 * @param direction The direction the sort should be in.
	 * @return A a sort by ingredient code.
	 */
	public static Sort getIngredientCodeSort(Sort.Direction direction) {
		return  new Sort(
				new Sort.Order(direction, Ingredient.INGREDIENT_CODE_SORT_FIELD)
		);
	}

	/**
	 * Returns a sort by ingredient description.
	 *
	 * @param direction The direction the sort should be in.
	 * @return A sort by ingredient description.
	 */
	public static Sort getDescriptionSort(Sort.Direction direction) {
		return new Sort(
				new Sort.Order(direction, Ingredient.INGREDIENT_DESCRIPTION_SORT_FIELD).ignoreCase(),
				new Sort.Order(direction, Ingredient.INGREDIENT_CODE_SORT_FIELD)
		);
	}

	/**
	 * Compares this object to another for equality. Equality is based on ingredient statement number.
	 *
	 * @param o The object to compare to.
	 * @return True if the objects are equal and false otherwise.
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Ingredient)) return false;

		Ingredient that = (Ingredient) o;

		return !(ingredientCode != null ? !ingredientCode.trim().equals(that.ingredientCode.trim()) : that.ingredientCode != null);

	}

	/**
	 * Generates a hash code for this object.
	 *
	 * @return A hash code for this object.
	 */
	@Override
	public int hashCode() {
		return ingredientCode != null ? ingredientCode.trim().hashCode() : 0;
	}
}
