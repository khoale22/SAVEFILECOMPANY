package com.heb.pm.entity;

import org.hibernate.annotations.TypeDef;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Represents product scan code extent audit composite key.
 *
 * @since 2.15.0
 */
@Embeddable
@TypeDef(name = "fixedLengthCharPK", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharTypePK.class)
public class ProductScanCodeExtentAuditKey implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "scn_cd_id")
    private Long scanCodeId;

    @Column(name = "AUD_REC_CRE8_TS")
    private LocalDateTime changedOn;

    /**
     * Gets scan code id that is attached to the measurements given by the gladson measuring data.
     *
     * @return the scan code id that is attached to the measurements given by the gladson measuring data.
     */
    public Long getScanCodeId() {
        return scanCodeId;
    }

    /**
     * Sets scan code id that is attached to the measurements given by the gladson measuring data.
     *
     * @param scanCodeId the scan code id that is attached to the measurements given by the gladson measuring data.
     */
    public void setScanCodeId(Long scanCodeId) {
        this.scanCodeId = scanCodeId;
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
     * Compares another object to this one. If that object is an ProductScanCodeExtentAuditKey, it uses they keys
     * to determine if they are equal and ignores non-key values for the comparison.
     *
     * @param o The object to compare to.
     * @return True if they are equal and false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductScanCodeExtentAuditKey that = (ProductScanCodeExtentAuditKey) o;
        return Objects.equals(scanCodeId, that.scanCodeId) &&
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
        return Objects.hash(scanCodeId, changedOn);
    }

    /**
     * Returns a String representation of the object.
     *
     * @return A String representation of the object.
     */
    @Override
    public String toString() {
        return "ProductScanCodeExtentAuditKey{" +
                "scanCodeId=" + scanCodeId +
                ", changedOn='" + changedOn + '\'' +
                '}';
    }
}
