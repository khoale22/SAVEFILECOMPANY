package com.heb.pm.entity;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Represents product temperature control.
 *
 * @author m314029
 * @since 2.6.0
 */
@Entity
@Table(name = "prod_temp_cntl")
//dB2Oracle changes vn00907 starts
@TypeDefs({
	
@TypeDef(name = "fixedLengthChar", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharType.class),
@TypeDef(name = "fixedLengthCharPK", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharTypePK.class)
})
//dB2Oracle changes vn00907
public class ProductTemperatureControl implements Serializable {

	// default constructor
	public ProductTemperatureControl(){super();}

	// copy constructor
	public ProductTemperatureControl(ProductTemperatureControl productTemperatureControl){
		super();
		this.setId(productTemperatureControl.getId());
		this.setDescription(productTemperatureControl.getDescription());
		this.setAbbreviation(productTemperatureControl.getAbbreviation());
	}

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="prod_temp_cntl_cd")
	//db2o changes  vn00907
	@Type(type="fixedLengthCharPK")
	private String id;

	@Column(name="prod_temp_cntl_des")
	@Type(type="fixedLengthChar")
	private String description;

	@Column(name="prod_temp_cntl_abb")
	@Type(type="fixedLengthChar")
	private String abbreviation;

	/**
	 * Returns id.
	 *
	 * @return The id.
	 **/
	public String getId() {
		return id;
	}

	/**
	 * Sets the id.
	 *
	 * @param id The id.
	 **/
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Returns description.
	 *
	 * @return The description.
	 **/
	public String getDescription() {
		return description;
	}

	/**
	 * Sets the description.
	 *
	 * @param description The description.
	 **/
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Returns abbreviation.
	 *
	 * @return The abbreviation.
	 **/
	public String getAbbreviation() {
		return abbreviation;
	}

	/**
	 * Sets the abbreviation.
	 *
	 * @param abbreviation The abbreviation.
	 **/
	public void setAbbreviation(String abbreviation) {
		this.abbreviation = abbreviation;
	}

	/**
	 * Compares another object to this one. If that object is an ProductTemperatureControl, it uses the keys
	 * to determine if they are equal and ignores non-key values for the comparison.
	 *
	 * @param o The object to compare to.
	 * @return True if they are equal and false otherwise.
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		ProductTemperatureControl that = (ProductTemperatureControl) o;

		return id != null ? id.equals(that.id) : that.id == null;
	}

	/**
	 * Returns a hash code for this object. If two objects are equal, they have the same hash. If they are not equal,
	 * they have different hash codes.
	 *
	 * @return The hash code for this obejct.
	 */
	@Override
	public int hashCode() {
		return id != null ? id.hashCode() : 0;
	}

	/**
	 * Returns a printable representation of the object.
	 *
	 * @return A printable representation of the object.
	 */
	@Override
	public String toString() {
		return "ProductTemperatureControl{" +
				"id='" + id + '\'' +
				", description='" + description + '\'' +
				", abbreviation='" + abbreviation + '\'' +
				'}';
	}
}
