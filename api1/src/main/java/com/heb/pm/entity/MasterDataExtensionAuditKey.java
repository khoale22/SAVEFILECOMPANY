package com.heb.pm.entity;

import org.hibernate.annotations.TypeDef;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Represents master data extension audit composite key.
 * @author vn70529
 * @since 2.15.0
 */
@Embeddable
@TypeDef(name = "fixedLengthCharPK", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharTypePK.class)
public class MasterDataExtensionAuditKey implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "attr_id")
    private Long attributeId;

    @Column(name = "key_id")
    private long keyId;

    @Column(name = "dta_src_sys")
    private Long dataSourceSystem;

    @Column(name = "AUD_REC_CRE8_TS")
    private LocalDateTime changedOn;

    /**
     * Returns the AttributeId. The id of the corresponding attribute
     *
     * @return AttributeId
     */
    public Long getAttributeId() {
        return attributeId;
    }

    /**
     * Sets the AttributeId
     *
     * @param attributeId The AttributeId
     */
    public void setAttributeId(Long attributeId) {
        this.attributeId = attributeId;
    }

    /**
     * Returns the keyId. This is either the prodId or the upc according to the ItemProdIdCode.
     *
     * @return keyId
     */
    public long getKeyId() {
        return keyId;
    }

    /**
     * Sets the keyId
     *
     * @param keyId The keyId
     */
    public void setKeyId(long keyId) {
        this.keyId = keyId;
    }

    /**
     * Returns the DataSourceSystem. The source system of the data.
     *
     * @return DataSourceSystem
     */
    public Long getDataSourceSystem() {
        return dataSourceSystem;
    }

    /**
     * Sets the DataSourceSystem
     *
     * @param dataSourceSystem The DataSourceSystem
     */
    public void setDataSourceSystem(Long dataSourceSystem) {
        this.dataSourceSystem = dataSourceSystem;
    }

    /**
     * Returns the changed on. The changed on is the timestamp in which the transaction occurred.
     *
     * @return Timestamp
     */
    public LocalDateTime getChangedOn() {
        return changedOn;
    }

    /**
     * Sets the Timestamp
     *
     * @param timestamp The Timestamp
     */
    public void setChangedOn(LocalDateTime timestamp) {
        this.changedOn = changedOn;
    }

    /**
     * Compares another object to this one. If that object is an MasterDataExtensionAuditKey, it uses they keys
     * to determine if they are equal and ignores non-key values for the comparison.
     *
     * @param o The object to compare to.
     * @return True if they are equal and false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MasterDataExtensionAuditKey that = (MasterDataExtensionAuditKey) o;
        return Objects.equals(attributeId, that.attributeId) &&
                Objects.equals(keyId, that.keyId) &&
                Objects.equals(dataSourceSystem, that.dataSourceSystem) &&
                Objects.equals(changedOn, that.changedOn) ;
    }

    /**
     * Returns a hash code for this object. If two objects are equal, they have the same hash. If they are not equal,
     * they have different hash codes.
     *
     * @return The hash code for this object.
     */
    @Override
    public int hashCode() {
        return Objects.hash(attributeId, keyId, dataSourceSystem, changedOn);
    }

    /**
     * Returns a String representation of the object.
     *
     * @return A String representation of the object.
     */
    @Override
    public String toString() {
        return "MasterDataExtensionAudit{" +
                "attributeId=" + attributeId +
                ", keyId='" + keyId +
                ", dataSourceSystem='" + dataSourceSystem +
                ", changedOn='" + changedOn +
                '}';
    }
}
