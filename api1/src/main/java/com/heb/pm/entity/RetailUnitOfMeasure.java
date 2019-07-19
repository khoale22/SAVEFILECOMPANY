package com.heb.pm.entity;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Represents a retail unit of measure.
 *
 * @author m314029
 * @since 2.7.0
 */
@Entity
@Table(name = "rtl_sell_units")
@TypeDef(name = "fixedLengthCharPK", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharTypePK.class)
public class RetailUnitOfMeasure implements Serializable {

	// default constructor
	public RetailUnitOfMeasure(){super();}

	// copy constructor
	public RetailUnitOfMeasure(RetailUnitOfMeasure retailUnitOfMeasure) {
		super();
		this.setId(retailUnitOfMeasure.getId());
		this.setAbbreviation(retailUnitOfMeasure.getAbbreviation());
		this.setDescription(retailUnitOfMeasure.getDescription());
		this.setStandardUnitOfMeasureCode(retailUnitOfMeasure.getStandardUnitOfMeasureCode());
	}

	private static final long serialVersionUID = 1L;

	private static final String DISPLAY_NAME_FORMAT = "%s[%s]";

	@Id
	@Column(name = "retl_sell_sz_cd")
	@Type(type="fixedLengthCharPK")
	private String id;

	@Column(name = "retl_sell_sz_abb")
	private String abbreviation;

	@Column(name = "retl_sell_sz_des")
	private String description;

	@Column(name = "std_uom_cd")
	private String standardUnitOfMeasureCode;

	@Column(name = "std_uom_conv_rto")
	private long retailUomConversion;

	/**
	 * Gets retail uom conversion.  Numeric unit available for retail, but not considered inventory.
	 *
	 * @return the retail uom conversion which is the Numeric unit available for retail, but not considered inventory.
	 */
	public long getRetailUomConversion() {
		return retailUomConversion;
	}

	/**
	 * Sets retail uom conversion.  Numeric unit available for retail, but not considered inventory.
	 *
	 * @param retailUomConversion the retail uom conversion which is the Numeric unit available for retail, but not considered inventory.
	 */
	public void setRetailUomConversion(long retailUomConversion) {
		this.retailUomConversion = retailUomConversion;
	}

	/**
	 * Returns RetailUnitOfMeasureCode that is given to a unit of measure that defines the measure that is adopted.
	 *
	 * @return The RetailUnitOfMeasureCode that is given to a unit of measure that defines the measure that is ado
	 **/
	public String getId() {
		return id;
	}

	/**
	 * Sets the RetailUnitOfMeasureCode that is given to a unit of measure that defines the measure that is ado
	 *
	 * @param id the id
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Returns Abbreviation that is given to the retail unit of measure.
	 *
	 * @return The Abbreviation that is given to the retail unit of measure.
	 **/
	public String getAbbreviation() {
		return abbreviation;
	}

	/**
	 * Sets the Abbreviation that is given to the retail unit of measure.
	 *
	 * @param abbreviation The Abbreviation that is given to the retail unit of measure.
	 **/
	public void setAbbreviation(String abbreviation) {
		this.abbreviation = abbreviation;
	}

	/**
	 * Returns Description that is given to the retail unit of measure.
	 *
	 * @return The Description that is given to the retail unit of measure.
	 **/
	public String getDescription() {
		return description;
	}

	/**
	 * Sets the Description that is given to the retail unit of measure.
	 *
	 * @param description The Description that is given to the retail unit of measure.
	 **/
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Returns StandardUnitOfMeasureCode given to a standard unit of measure which ties to the basic units.
	 *
	 * @return The StandardUnitOfMeasureCode given to a standard unit of measure which ties to the basic units.
	 **/
	public String getStandardUnitOfMeasureCode() {
		return standardUnitOfMeasureCode;
	}

	/**
	 * Sets the StandardUnitOfMeasureCode given to a standard unit of measure which ties to the basic units.
	 *
	 * @param standardUnitOfMeasureCode The StandardUnitOfMeasureCode given to a standard unit of measure which ties to the basic units.
	 **/
	public void setStandardUnitOfMeasureCode(String standardUnitOfMeasureCode) {
		this.standardUnitOfMeasureCode = standardUnitOfMeasureCode;
	}

	/**
	 * Compares another object to this one. The key is the only thing used to determine equality.
	 *
	 * @param o The object to compare to.
	 * @return True if they are equal and false otherwise.
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		RetailUnitOfMeasure retailUnitOfMeasure = (RetailUnitOfMeasure) o;

		return id != null ? id.equals(retailUnitOfMeasure.id) : retailUnitOfMeasure.id == null;
	}

	/**
	 * Returns a hash for this object. If two objects are equal, they will have the same hash. If they are not,
	 * they will (probably) have different hashes.
	 *
	 * @return The hash code for this object.
	 */
	@Override
	public int hashCode() {
		return id != null ? id.hashCode() : 0;
	}

	/**
	 * Returns a String representation of the object.
	 *
	 * @return A String representation of the object.
	 */
	@Override
	public String toString() {
		return "RetailUOM{" +
				"id='" + id + '\'' +
				", abbreviation='" + abbreviation + '\'' +
				", description='" + description + '\'' +
				", standardUnitOfMeasureCode='" + standardUnitOfMeasureCode + '\'' +
				'}';
	}

	/**
	 * Gets display name for UOM.
	 *
	 * @return the display name
	 */
	public String getDisplayName() {

		if(id != null && description != null) {
			return String.format(RetailUnitOfMeasure.DISPLAY_NAME_FORMAT, description.trim(), id.trim());

		} else if (id != null) {
			return id.trim();
		} else {
			return description.trim();
		}

	}
}
