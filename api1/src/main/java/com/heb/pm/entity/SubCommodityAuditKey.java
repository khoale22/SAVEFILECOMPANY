package com.heb.pm.entity;

import org.hibernate.annotations.TypeDef;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Represents sub commodity audit composite key.
 *
 * @author vn70529
 * @since 2.17.9
 */
@Embeddable
@TypeDef(name = "fixedLengthCharPK", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharTypePK.class)
public class SubCommodityAuditKey implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name="pd_omi_com_cls_cd")
    private Integer classCode;

    @Column(name="pd_omi_com_cd")
    private Integer commodityCode;

    @Column(name = "pd_omi_sub_com_cd")
    private Integer subCommodityCode;

    @Column(name = "AUD_REC_CRE8_TS")
    private LocalDateTime changedOn;

    // default constructor
    public SubCommodityAuditKey(){super();}

    /**
     * Returns the class ID.
     *
     * @return The class ID.
     */
    public Integer getClassCode() {
        return classCode;
    }

    /**
     * Sets the class ID.
     *
     * @param classCode The class ID.
     */
    public void setClassCode(Integer classCode) {
        this.classCode = classCode;
    }

    /**
     * Returns the commodity ID.
     *
     * @return The commodity ID.
     */
    public Integer getCommodityCode() {
        return commodityCode;
    }

    /**
     * Sets the commodity ID.
     *
     * @param commodityCode THe commodity ID.
     */
    public void setCommodityCode(Integer commodityCode) {
        this.commodityCode = commodityCode;
    }

    /**
     * Returns the sub-commodity ID.
     *
     * @return The sub-commodity ID.
     */
    public Integer getSubCommodityCode() {
        return subCommodityCode;
    }

    /**
     * Sets the sub-commodity ID.
     *
     * @param subCommodityCode The sub-commodity ID.
     */
    public void setSubCommodityCode(Integer subCommodityCode) {
        this.subCommodityCode = subCommodityCode;
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
     * Compares another object to this one. The key is the only thing used to determine equality.
     *
     * @param o The object to compare to.
     * @return True if they are equal and false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SubCommodityAuditKey that = (SubCommodityAuditKey) o;

        if (classCode != null ? !classCode.equals(that.classCode) : that.classCode != null) return false;
        if (commodityCode != null ? !commodityCode.equals(that.commodityCode) : that.commodityCode != null)
            return false;
        return subCommodityCode != null ? subCommodityCode.equals(that.subCommodityCode) : that.subCommodityCode == null;
    }

    /**
     * Returns a String representation of the object.
     *
     * @return A String representation of the object.
     */
    @Override
    public String toString() {
        return "SubCommodityKey{" +
                "classCode=" + classCode +
                ", commodityCode=" + commodityCode +
                ", subCommodityCode=" + subCommodityCode +
                ", changedOn='" + changedOn +
                '}';
    }

}
