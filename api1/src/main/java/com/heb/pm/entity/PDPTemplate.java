package com.heb.pm.entity;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;
import org.springframework.data.domain.Sort;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
/**
 * Represents a record in the usr_inrfc_tmplt table.
 *
 * @author m314029
 * @since 2.6.0
 */
@Entity
@Table(name="usr_inrfc_tmplt")
//dB2Oracle changes vn00907 starts
@TypeDefs({
@TypeDef(name = "fixedLengthChar", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharType.class),
@TypeDef(name = "fixedLengthCharPK", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharTypePK.class)
})
//dB2Oracle changes vn00907
public class PDPTemplate implements Serializable {

	// default constructor
	public PDPTemplate(){super();}

	// copy constructor
	public PDPTemplate(PDPTemplate pdpTemplate){
		super();
		this.setId(pdpTemplate.getId());
		this.setDescription(pdpTemplate.getDescription());
		this.setAbbreviation(pdpTemplate.getAbbreviation());
	}

	private static final long serialVersionUID = 1L;

	private static final String DEFAULT_TEMPLATE_SORT_FIELD = "description";

	@Id
	@Column(name="tmplt_id")
	//db2o changes  vn00907
	@Type(type="fixedLengthCharPK")
	private String id;

	@Column(name="tmplt_abb")
	//db2o changes  vn00907
	@Type(type="fixedLengthChar")
	private String abbreviation;

	@Column(name="tmplt_des")
	//db2o changes  vn00907
	@Type(type="fixedLengthChar")
	private String description;

	/**
	 * Returns Id.
	 *
	 * @return The Id.
	 **/
	public String getId() {
		return id;
	}

	/**
	 * Sets the Id.
	 *
	 * @param id The Id.
	 **/
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Returns Abbreviation.
	 *
	 * @return The Abbreviation.
	 **/
	public String getAbbreviation() {
		return abbreviation;
	}

	/**
	 * Sets the Abbreviation.
	 *
	 * @param abbreviation The Abbreviation.
	 **/
	public void setAbbreviation(String abbreviation) {
		this.abbreviation = abbreviation;
	}

	/**
	 * Returns Description.
	 *
	 * @return The Description.
	 **/
	public String getDescription() {
		return description;
	}

	/**
	 * Sets the Description.
	 *
	 * @param description The Description.
	 **/
	public void setDescription(String description) {
		this.description = description;
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

		PDPTemplate that = (PDPTemplate) o;

		return id != null ? id.equals(that.id) : that.id == null;
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
		return "PDPTemplateId{" +
				"id='" + id + '\'' +
				", abbreviation='" + abbreviation + '\'' +
				", description='" + description + '\'' +
				'}';
	}

	/**
	 * Returns the default sort order for the product brand tier table.
	 *
	 * @return The default sort order for the product brand tier table.
	 */
	public static Sort getDefaultSort() {
		return new Sort(
				new Sort.Order(Sort.Direction.ASC, PDPTemplate.DEFAULT_TEMPLATE_SORT_FIELD)
		);
	}
}