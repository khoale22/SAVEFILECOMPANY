package com.heb.pm.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.domain.Sort;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a dynamic attribute of a IngredientStatementHeader.
 *
 * @author m594201
 * @since 2.0.9
 */
@Entity
@Table(name="sl_ingrd_stmt_hdr")
public class IngredientStatementHeader implements Serializable {

	public static final char DELETE_CODE = 'D';
	public static final char UPDATE_CODE = 'C';
	public static final char ADD_CODE = 'A';

	// These are the values that are set when a UPC does not have a nutrient statement attached to it.
	public static final long[] EMPTY_SCALE_NUTRIENT_STATEMENT_NUMBERS = {0, 9999999};

	private static final long serialVersionUID = 1L;

	private static final String DEFAULT_SORT = "statementNumber";
	private static final String DISPLAY_NAME_FORMAT = "Ingredient Statement %d";

	// Modified this delimiter to NOT contain a space after comma, since this is how the mainframe sends ingredients
	// to ePlum
	private static final String INGREDIENT_DELIMITER = ",";

	@Id
	@Column(name = "pd_ingrd_stmt_no")
	private long statementNumber;

	@Column(name = "PD_INGRD_MAINT_DT")
	private LocalDate maintenanceDate;

	@Column(name = "PD_INGRD_MAINT_SW")   
	private boolean maintenanceSwitch;

	@Column(name = "PD_MAINT_TYP_CD")  
	private char maintenanceCode;
	  
	@JsonIgnoreProperties("ingredientStatementHeader")
	@OneToMany(mappedBy = "ingredientStatementHeader", fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL)
	@OrderBy(value = "pd_ingrd_pct DESC")
	private List<IngredientStatementDetail> ingredientStatementDetails = new ArrayList<>();

	@JsonIgnoreProperties("ingredientStatementHeader")
	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "INGR_STATEMENT_NUM", referencedColumnName = "pd_ingrd_stmt_no", insertable = false, updatable = false)
	private List<ScaleUpc> scaleUpcs;

	/**
	 * Gets scale upcs.
	 *
	 * @return the scale upcs
	 */
	public List<ScaleUpc> getScaleUpcs() {
		return scaleUpcs;
	}

	/**
	 * Sets scale upcs.
	 *
	 * @param scaleUpcs the scale upcs
	 */
	public void setScaleUpcs(List<ScaleUpc> scaleUpcs) {
		this.scaleUpcs = scaleUpcs;
	}

	/**
	 * Gets ingredient statement details.
	 *
	 * @return the ingredient statement details
	 */
	public List<IngredientStatementDetail> getIngredientStatementDetails() {
		return ingredientStatementDetails;
	}

	/**
	 * Sets ingredient statement details.
	 *
	 * @param ingredientStatementDetails the ingredient statement details
	 */
	public void setIngredientStatementDetails(List<IngredientStatementDetail> ingredientStatementDetails) {
		this.ingredientStatementDetails = ingredientStatementDetails;
	}

	/**
	 * Gets statement number.
	 *
	 * @return the statement number
	 */
	public long getStatementNumber() {
		return statementNumber;
	}

	/**
	 * Sets statement number.
	 *
	 * @param statementNumber the statement number
	 */
	public void setStatementNumber(long statementNumber) {
		this.statementNumber = statementNumber;
	}

	/**
	 * Returns a default sort for the table.
	 *
	 * @return A default sort for the table.
	 */
	public static Sort getDefaultSort() {
		return new Sort(new Sort.Order(Sort.Direction.ASC, IngredientStatementHeader.DEFAULT_SORT));
	}

	/**
	 * Returns the maintenance date.
	 *
	 * @return the maintenance date.
	 */
	public LocalDate getMaintenanceDate() {
		return maintenanceDate;
	}

	/**
	 * Sets the maintenance date.
	 *
	 * @param maintenanceDate The maintenance date.
	 */
	public void setMaintenanceDate(LocalDate maintenanceDate) {
		this.maintenanceDate = maintenanceDate;
	}

	/**
	 * Returns true if it needs maintenance.
	 *
	 * @return Boolean value to whether it needs maintenance or not.
	 */
	public boolean isMaintenanceSwitch() {
		return maintenanceSwitch;
	}

	/**
	 * Sets the maintenance switch.
	 *
	 * @param maintenanceSwitch Boolean value to whether it needs maintenance or not.
	 */
	public void setMaintenanceSwitch(boolean maintenanceSwitch) {
		this.maintenanceSwitch = maintenanceSwitch;
	}

	/**
	 * Returns the maintenance code.
	 *
	 * @return the maintenance code.
	 */
	public char getMaintenanceCode() {
		return maintenanceCode;
	}

	/**
	 * Sets the maintenance code.
	 *
	 * @param maintenanceCode the maintenance code.
	 */
	public void setMaintenanceCode(char maintenanceCode) {
		this.maintenanceCode = maintenanceCode;
	}

	/**
	 * Returns a full string representation of the ingredient statement as it would be displayed on a nutrition label.
	 *
	 * @return A full string representation of the ingredient statement as it would be displayed on a nutrition label.
	 */
	public String getIngredientsText() {

		StringBuilder ingredientStatementBuilder = new StringBuilder();

		if(this.getIngredientStatementDetails() != null) {
			for (int i = 0; i < this.getIngredientStatementDetails().size(); i++) {
				if (i != 0) {
					ingredientStatementBuilder.append(IngredientStatementHeader.INGREDIENT_DELIMITER);
				}
				ingredientStatementBuilder.append(
						this.getIngredientStatementDetails().get(i).getIngredient().getDisplayText());
			}
		}
		return ingredientStatementBuilder.toString();
	}

	/**
	 * Returns a string representation of this object that is presentable on the GUI.
	 *
	 * @return A string representation of this object that is presentable on the GUI.
	 */
	public String getDisplayName() {
		return String.format(IngredientStatementHeader.DISPLAY_NAME_FORMAT, this.statementNumber);
	}
}
