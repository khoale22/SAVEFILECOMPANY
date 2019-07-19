package com.heb.pm.entity;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * Represents the key of the EFF_DTD_MAINT table.
 *
 * @author d116773
 * @since 2.13.0
 */
@Embeddable
@TypeDef(name = "fixedLengthCharPK", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharTypePK.class)
public class EffectiveDatedMaintenanceKey implements Serializable{

	private static final long serialVersionUID = 6375052893291247938L;

	@Column(name="tbl_nm_id")
	@Type(type="fixedLengthCharPK")
	private String tableName;

	@Column(name="col_nm_id")
	@Type(type="fixedLengthCharPK")
	private String columnName;

	@Column(name="seq_id")
	private Long sequenceNumber;

	/**
	 * Returns the name of the table this record is there to update.
	 *
	 * @return The name of the table this record is there to update.
	 */
	public String getTableName() {
		return tableName;
	}

	/**
	 * Sets the name of the table this record is there to update.
	 *
	 * @param tableName The name of the table this record is there to update.
	 */
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	/**
	 * Returns the name of the column this record is there to update.
	 *
	 * @return The name of the column this record is there to update.
	 */
	public String getColumnName() {
		return columnName;
	}

	/**
	 * Sets the name of the column this record is there to update.
	 *
	 * @param columnName The name of the column this record is there to update.
	 */
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	/**
	 * Returns a number to make this record unique (if the record had been updated before).
	 *
	 * @return A number to make this record unique
	 */
	public Long getSequenceNumber() {
		return sequenceNumber;
	}

	/**
	 * Sets a number to make this record unique.
	 *
	 * @param sequenceNumber A number to make this record unique.
	 */
	public void setSequenceNumber(Long sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}

	/**
	 * Returns a string representation of this object.
	 *
	 * @return A string representation of this object.
	 */
	@Override
	public String toString() {
		return "EffectiveDatedMaintenanceKey{" +
				"tableName='" + tableName + '\'' +
				", columnName='" + columnName + '\'' +
				", sequenceNumber=" + sequenceNumber +
				'}';
	}

	/**
	 * Compares this object to another for equality.
	 *
	 * @param o The object to compare to.
	 * @return True if the objects are equal and false otherwise.
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof EffectiveDatedMaintenanceKey)) return false;

		EffectiveDatedMaintenanceKey that = (EffectiveDatedMaintenanceKey) o;

		if (tableName != null ? !tableName.equals(that.tableName) : that.tableName != null) return false;
		if (columnName != null ? !columnName.equals(that.columnName) : that.columnName != null) return false;
		return !(sequenceNumber != null ? !sequenceNumber.equals(that.sequenceNumber) : that.sequenceNumber != null);

	}

	/**
	 * Returns a hash code for this object.
	 *
	 * @return A hash code for this object.
	 */
	@Override
	public int hashCode() {
		int result = tableName != null ? tableName.hashCode() : 0;
		result = 31 * result + (columnName != null ? columnName.hashCode() : 0);
		result = 31 * result + (sequenceNumber != null ? sequenceNumber.hashCode() : 0);
		return result;
	}
}
