package com.heb.pm.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * Represents a key for meta data.
 *
 * @author m314029
 * @since 2.21.0
 */
@Embeddable
public class AttributeMetaDataKey implements Serializable {
	private static final long serialVersionUID = -727392252090488837L;

	@Column(name = "TBL_NM")
	private String table;

	@Column(name = "FLD_NM")
	private String field;

	public AttributeMetaDataKey() {
	}

	public AttributeMetaDataKey(String table, String field) {
		this.table = table;
		this.field = field;
	}

	/**
	 * Returns Table.
	 *
	 * @return The Table.
	 **/
	public String getTable() {
		return table;
	}

	/**
	 * Sets the Table.
	 *
	 * @param table The Table.
	 **/
	public AttributeMetaDataKey setTable(String table) {
		this.table = table;
		return this;
	}

	/**
	 * Returns Field.
	 *
	 * @return The Field.
	 **/
	public String getField() {
		return field;
	}

	/**
	 * Sets the Field.
	 *
	 * @param field The Field.
	 **/
	public AttributeMetaDataKey setField(String field) {
		this.field = field;
		return this;
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

		AttributeMetaDataKey that = (AttributeMetaDataKey) o;

		if (table != null ? !table.equals(that.table) : that.table != null) return false;
		return field != null ? field.equals(that.field) : that.field == null;
	}

	/**
	 * Returns a hash for this object. If two objects are equal, they will have the same hash. If they are not,
	 * they will (probably) have different hashes.
	 *
	 * @return The hash code for this object.
	 */
	@Override
	public int hashCode() {
		int result = table != null ? table.hashCode() : 0;
		result = 31 * result + (field != null ? field.hashCode() : 0);
		return result;
	}

	/**
	 * Returns a String representation of the object.
	 *
	 * @return A String representation of the object.
	 */
	@Override
	public String toString() {
		return "MetaDataKey{" +
				"table='" + table + '\'' +
				", field='" + field + '\'' +
				'}';
	}
}
