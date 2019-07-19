package com.heb.pm.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import java.io.Serializable;

/**
 * Holds the primary keys that indicate information relating to the logical physical relationships.
 *
 * @author s753601
 * @since 2.5.0
 */
@Embeddable
//dB2Oracle changes vn00907 starts
@TypeDefs({
@TypeDef(name = "fixedLengthChar", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharType.class),
@TypeDef(name = "fixedLengthCharPK", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharTypePK.class)
})
// dB2Oracle changes vn00907
public class LogicalPhysicalRelationshipKey implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final int PRIME_NUMBER = 31;

    @Column(name="LOGIC_ATTR_ID")
    private int logicalAttributeId;

    @Column(name="PHY_ATTR_ID")
    private int physicalAttributeId;

    @Column(name="RLSHP_GRP_TYP_CD")
    //db2o changes  vn00907
    @Type(type="fixedLengthCharPK")  
    private String relationshipGroupTypeCode;

    /**
     * Get the logical attribute ID
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
     * Gets the physical attribute ID
     * @return the physical attribute ID
     */
    public int getPhysicalAttributeId() {
        return physicalAttributeId;
    }

    /**
     * Sets the physical attribute ID
     * @param physicalAttributeId the new physical attribute ID
     */
    public void setPhysicalAttributeId(int physicalAttributeId) {
        this.physicalAttributeId = physicalAttributeId;
    }

    /**
     * Returns the relationship group type code (this will need to be converted to an int.
     * @return relationship group type code
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
     * Compares another object to this one. The key is the only thing used to determine equality.
     *
     * @param o The object to compare to.
     * @return True if they are equal and false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LogicalPhysicalRelationshipKey that = (LogicalPhysicalRelationshipKey) o;

        if (logicalAttributeId != that.logicalAttributeId) return false;
        if (physicalAttributeId != that.physicalAttributeId) return false;
        return relationshipGroupTypeCode != null ? relationshipGroupTypeCode.equals(that.relationshipGroupTypeCode) : that.relationshipGroupTypeCode == null;
    }

    /**
     * Returns a hash for this object. If two objects are equal, they will have the same hash. If they are not,
     * they will (probably) have different hashes.
     *
     * @return The hash code for this object.
     */
    @Override
    public int hashCode() {
        int result = logicalAttributeId;
        result = LogicalPhysicalRelationshipKey.PRIME_NUMBER * result + physicalAttributeId;
        result = LogicalPhysicalRelationshipKey.PRIME_NUMBER * result +
                (relationshipGroupTypeCode != null ? relationshipGroupTypeCode.hashCode() : 0);
        return result;
    }

	/**
	 * Returns a String representation of the object.
	 *
	 * @return A String representation of the object.
	 */
	@Override
	public String toString() {
		return "LogicalPhysicalRelationshipKey{" +
				"logicalAttributeId=" + logicalAttributeId +
				", physicalAttributeId=" + physicalAttributeId +
				", relationshipGroupTypeCode='" + relationshipGroupTypeCode + '\'' +
				'}';
	}
}
