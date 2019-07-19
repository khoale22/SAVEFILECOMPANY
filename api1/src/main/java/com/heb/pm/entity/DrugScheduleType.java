package com.heb.pm.entity;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.*;
import java.io.Serializable;

/**
 * * Pharmacy Values as set by DEA for controlling substances because of its abuse or potiential risk.  Schedule 1 are highest.
 * L - Legend Item
 * 5 - Scheduled 5 Drug
 * 4 - Scheduled 4 Drug
 * 3 - Scheduled 3 Drug
 * 2 - Scheduled 2 Drug
 * 1 - Scheduled 1 Drug
 * @author s753601
 * @version 2.12.0
 */
@Entity
@Table(name = "DRG_ITM_SCH")
@TypeDefs({
		@TypeDef(name = "fixedLengthChar", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharType.class),
		@TypeDef(name = "fixedLengthCharPK", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharTypePK.class)
})
public class DrugScheduleType implements Serializable{

	private static final long serialVersionUID = 1L;

	private static final int PRIME_NUMBER = 31;

	@Id
	@Column(name = "DRUG_SCH_TYP_CD")
	private String id;

	@Column(name = "DRUG_SCH_ABB")
	private String abbreviation;

	// this forces the getter for description to be used when description is referenced
	@Access(AccessType.PROPERTY)
	@Column(name = "DRUG_SCH_DES")
	private String description;

	/**
	 * The identifier for the type code
	 */
	public String getId() {
		return id;
	}

	/**
	 * Updates the id
	 * @param id the new id
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * The abbreviation for the code table
	 * @return abbreviation
	 */
	public String getAbbreviation() {
		return abbreviation;
	}

	/**
	 * Updates the abbreviation
	 * @param abbreviation the new abbreviation
	 */
	public void setAbbreviation(String abbreviation) {
		this.abbreviation = abbreviation;
	}

	/**
	 * Gets the long description for the row on the code table
	 * @return description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Updates the description
	 * @param description the new description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Tests if whether two objects are equal
	 * @param o the other object
	 * @return
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		DrugScheduleType drugScheduleType = (DrugScheduleType) o;

		if (id != null ? !id.equals(drugScheduleType.id) : drugScheduleType.id != null) return false;
		return true;
	}

	/**
	 * Returns a hash for this object. If two objects are equal, they will have the same hash. If they are not,
	 * they will (probably) have different hashes.
	 *
	 * @return The hash code for this object.
	 */
	@Override
	public int hashCode() {
		int result = id != null ? id.hashCode() : 0;
		result = PRIME_NUMBER * result + (description != null ? description.hashCode() : 0);
		result = PRIME_NUMBER * result + (abbreviation != null ? abbreviation.hashCode() : 0);
		return result;
	}

	/**
	 * Returns a String representation of this object.
	 *
	 * @return A String representation of this object.
	 */
	@Override
	public String toString() {
		return "DrugScheduleType{" +
				"id='" + id + '\'' +
				", description='" + description + '\'' +
				", abbreviation='" + abbreviation + '\'' +
				'}';
	}
}
