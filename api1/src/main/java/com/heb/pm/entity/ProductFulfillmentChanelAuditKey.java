package com.heb.pm.entity;

import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.*;

/**
 * The primary key class for the PROD_FLFL_CHNL database table.
 *
 */
@Embeddable
@TypeDefs({
        @TypeDef(name = "fixedLengthCharPK", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharTypePK.class)
})
public class ProductFulfillmentChanelAuditKey implements Serializable {
    //default serial version id, required for serializable classes.
    private static final long serialVersionUID = 1L;

    @Column(name="PROD_ID", insertable=false, updatable=false)
    private long productId;

    @Column(name="SALS_CHNL_CD", insertable=false, updatable=false)
    private String salesChanelCode;

    @Column(name="FLFL_CHNL_CD", insertable=false, updatable=false)
    private String fullfillmentChanelCode;
    @Column(name = "AUD_REC_CRE8_TS", columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP", nullable = false, length = 0)
    private LocalDateTime changedOn;

    public ProductFulfillmentChanelAuditKey() {
    }
    /**
     * Getter for changedOn attribute
     * @return
     */
    public LocalDateTime getChangedOn() {
        return changedOn;
    }

    /**
     * Setter for changedOn attribute
     * @param changedOn - new attribute to set
     */
    public void setChangedOn(LocalDateTime changedOn) {
        this.changedOn = changedOn;
    }
    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public String getSalesChanelCode() {
        return salesChanelCode;
    }

    public void setSalesChanelCode(String salesChanelCode) {
        this.salesChanelCode = salesChanelCode;
    }

    public String getFullfillmentChanelCode() {
        return fullfillmentChanelCode;
    }

    public void setFullfillmentChanelCode(String fullfillmentChanelCode) {
        this.fullfillmentChanelCode = fullfillmentChanelCode;
    }


    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof ProductFulfillmentChanelAuditKey)) {
            return false;
        }
        ProductFulfillmentChanelAuditKey castOther = (ProductFulfillmentChanelAuditKey)other;
        return
                (this.productId == castOther.productId)
                        && this.salesChanelCode.equals(castOther.salesChanelCode)
                        && this.fullfillmentChanelCode.equals(castOther.fullfillmentChanelCode);
    }

    public int hashCode() {
        final int prime = 31;
        int hash = 17;
        hash = hash * prime + ((int) (this.productId ^ (this.productId >>> 32)));
        hash = hash * prime + this.salesChanelCode.hashCode();
        hash = hash * prime + this.fullfillmentChanelCode.hashCode();

        return hash;
    }
}