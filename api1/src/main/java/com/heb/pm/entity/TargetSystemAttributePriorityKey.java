package com.heb.pm.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import java.io.Serializable;

/**
 * Holds the primary keys for they target system attribute priority relationships.
 *
 * @author s753601
 * @since 2.5.0
 */
@Embeddable
// dB2Oracle changes vn00907 starts
@TypeDefs({
@TypeDef(name = "fixedLengthChar", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharType.class),
@TypeDef(name = "fixedLengthCharPK", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharTypePK.class)
})
// dB2Oracle changes vn00907
public class TargetSystemAttributePriorityKey implements Serializable{

    private static final long serialVersionUID = 1L;

    private static final int FOUR_BYTES = 32;
    private static final int PRIME_NUMBER = 31;

    @Column(name="DTA_SRC_SYS_ID")
    private long dataSourceSystemId;

    @Column(name="LOGIC_ATTR_ID")
    private int logicalAttributeId;

    @Column(name="PHY_ATTR_ID")
    private long physicalAttributeId;

    @Column(name = "RLSHP_GRP_TYP_CD")
    @Type(type="fixedLengthChar")    
    private String relationshipGroupTypeCode;

    @Column(name = "TRGT_SYS_ID")
    private int targetSystemId;

    /**
     * returns the data source system ID
     * @return the data source system ID
     */
    public long getDataSourceSystemId() {
        return dataSourceSystemId;
    }

    /**
     * Update the data source system ID
     * @param dataSourceSystemId the new data source system ID
     */
    public void setDataSourceSystemId(long dataSourceSystemId) {
        this.dataSourceSystemId = dataSourceSystemId;
    }

    /**
     * Returns the logical attribute ID
     * @return the logical attribute ID
     */
    public int getLogicalAttributeId() {
        return logicalAttributeId;
    }

    /**
     * Updates the logical attribute ID
     * @param logicalAttributeId the new logical attribute ID
     */
    public void setLogicalAttributeId(int logicalAttributeId) {
        this.logicalAttributeId = logicalAttributeId;
    }

    /**
     * Returns the physical attribute ID
     * @return the physical attribute ID
     */
    public long getPhysicalAttributeId() {
        return physicalAttributeId;
    }

    /**
     * Updates the physical attribute ID
     * @param physicalAttributeId the new physical attribute ID
     */
    public void setPhysicalAttributeId(long physicalAttributeId) {
        this.physicalAttributeId = physicalAttributeId;
    }

    /**
     * Get the relationship group type code
     * @return the relationship group type code
     */
    public String getRelationshipGroupTypeCode() {
        return relationshipGroupTypeCode;
    }

    /**
     * Updates the relationship group type code
     * @param relationshipGroupTypeCode the new relationship group type code
     */
    public void setRelationshipGroupTypeCode(String relationshipGroupTypeCode) {
        this.relationshipGroupTypeCode = relationshipGroupTypeCode;
    }

    /**
     * Returns the target system ID
     * @return the target system ID
     */
    public int getTargetSystemId() {
        return targetSystemId;
    }

    /**
     * Updates the target system ID
     * @param targetSystemId the new target system ID
     */
    public void setTargetSystemId(int targetSystemId) {
        this.targetSystemId = targetSystemId;
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

		TargetSystemAttributePriorityKey that = (TargetSystemAttributePriorityKey) o;

		if (dataSourceSystemId != that.dataSourceSystemId) return false;
		if (logicalAttributeId != that.logicalAttributeId) return false;
		if (physicalAttributeId != that.physicalAttributeId) return false;
		if (targetSystemId != that.targetSystemId) return false;
		return relationshipGroupTypeCode != null ? relationshipGroupTypeCode.equals(that.relationshipGroupTypeCode) :
				that.relationshipGroupTypeCode == null;
	}

	/**
	 * Returns a hash for this object. If two objects are equal, they will have the same hash. If they are not,
	 * they will (probably) have different hashes.
	 *
	 * @return The hash code for this object.
	 */
	@Override
	public int hashCode() {
		int result = (int) (dataSourceSystemId ^ (dataSourceSystemId >>> TargetSystemAttributePriorityKey.FOUR_BYTES));
		result = TargetSystemAttributePriorityKey.PRIME_NUMBER * result + logicalAttributeId;
		result = TargetSystemAttributePriorityKey.PRIME_NUMBER * result +
				(int) (physicalAttributeId ^ (physicalAttributeId >>> TargetSystemAttributePriorityKey.FOUR_BYTES));
		result = TargetSystemAttributePriorityKey.PRIME_NUMBER * result +
				(relationshipGroupTypeCode != null ? relationshipGroupTypeCode.hashCode() : 0);
		result = TargetSystemAttributePriorityKey.PRIME_NUMBER * result + targetSystemId;
		return result;
	}

	/**
	 * Returns a String representation of the object.
	 *
	 * @return A String representation of the object.
	 */
	@Override
	public String toString() {
		return "TargetSystemAttributePriorityKey{" +
				"dataSourceSystemId=" + dataSourceSystemId +
				", logicalAttributeId=" + logicalAttributeId +
				", physicalAttributeId=" + physicalAttributeId +
				", relationshipGroupTypeCode='" + relationshipGroupTypeCode + '\'' +
				", targetSystemId=" + targetSystemId +
				'}';
	}
}
