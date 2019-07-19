package com.heb.pm.entity;

import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.List;

/**
 * Represents a generic entity description.
 *
 * @author m314029
 * @since 2.12.0
 */
@Entity
@Table(name = "enty_des")
@TypeDefs({
		@TypeDef(name = "fixedLengthChar", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharType.class),
		@TypeDef(name = "fixedLengthCharPK", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharTypePK.class)
})
public class GenericEntityDescription implements Serializable {

	public static final int NOOP = 0;
	public static final int INSERT = 1;
	public static final int UPDATE = 2;
	public static final int DELETE = 3;

	private static final long serialVersionUID = 1L;
	public static String EMPTY_DESCRIPTION="";
	// default constructor
	public GenericEntityDescription(){super();}

	// copy constructor
	public GenericEntityDescription(GenericEntityDescription genericEntityDescription){
		super();
		this.setKey(new GenericEntityDescriptionKey(genericEntityDescription.getKey()));
		this.setShortDescription(genericEntityDescription.getShortDescription());
		this.setLongDescription(genericEntityDescription.getLongDescription());
	}

	@EmbeddedId
	private GenericEntityDescriptionKey key;

	@Column(name="enty_shrt_des")
	@Type(type="fixedLengthChar")
	private String shortDescription;

	@Column(name="enty_long_des")
	@Type(type="fixedLengthChar")
	private String longDescription;

	@Transient
	private int action;

	/**
	 * Gets key. This is the key for this generic entity description.
	 *
	 * @return the key
	 */
	public GenericEntityDescriptionKey getKey() {
		return key;
	}

	/**
	 * Sets key.
	 *
	 * @param key the key
	 */
	public void setKey(GenericEntityDescriptionKey key) {
		this.key = key;
	}

	/**
	 * Gets short description. This is the short description for this generic entity description.
	 *
	 * @return the short description
	 */
	public String getShortDescription() {
		return shortDescription;
	}

	/**
	 * Sets short description.
	 *
	 * @param shortDescription the short description
	 */
	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}

	/**
	 * Gets long description. This is the long description for this generic entity description.
	 *
	 * @return the long description
	 */
	public String getLongDescription() {
		return longDescription;
	}

	/**
	 * Sets long description.
	 *
	 * @param longDescription the long description
	 */
	public void setLongDescription(String longDescription) {
		this.longDescription = longDescription;
	}

	/**
	 * Returns the action to take with this entity.
	 *
	 * @return The action to take with this entity.
	 */
	public int getAction() {
		return action;
	}

	/**
	 * Sets the action to take with this entity.
	 *
	 * @param action The action to take with this entity.
	 */
	public void setAction(int action) {
		this.action = action;
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

		GenericEntityDescription that = (GenericEntityDescription) o;

		return key != null ? key.equals(that.key) : that.key == null;
	}

	/**
	 * Returns a hash for this object. If two objects are equal, they will have the same hash. If they are not,
	 * they will (probably) have different hashes.
	 *
	 * @return The hash code for this object.
	 */
	@Override
	public int hashCode() {
		return key != null ? key.hashCode() : 0;
	}

	/**
	 * Returns a String representation of the object.
	 *
	 * @return A String representation of the object.
	 */
	@Override
	public String toString() {
		return "GenericEntityDescription{" +
				"key=" + key +
				", shortDescription='" + shortDescription + '\'' +
				", longDescription='" + longDescription + '\'' +
				'}';
	}
}
