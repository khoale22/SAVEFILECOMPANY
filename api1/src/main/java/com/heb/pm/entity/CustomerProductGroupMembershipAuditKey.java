package com.heb.pm.entity;

import org.hibernate.annotations.TypeDef;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * This is the embedded key for customer product group membership audit.
 *
 * @author 70529
 * @since 2.15.0
 */
@Embeddable
@TypeDef(name = "fixedLengthCharPK", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharTypePK.class)
public class CustomerProductGroupMembershipAuditKey implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "CUST_PROD_GRP_ID")
    private Long custProductGroupId;

    @Column(name = "AUD_REC_CRE8_TS")
    private LocalDateTime changedOn;

    @Column(name = "prod_id")
    private Long prodId;

    /**
     * Returns the ProdId
     *
     * @return ProdId
     */
    public Long getProdId() {
        return prodId;
    }

    /**
     * Sets the ProdId
     *
     * @param prodId The ProdId
     */
    public void setProdId(Long prodId) {
        this.prodId = prodId;
    }

    /**
     * Gets the id of CustomerProductGroup.
     *
     * @return the id of CustomerProductGroup.
     */
    public long getCustProductGroupId() {
        return custProductGroupId;
    }

    /**
     * Sets the id of CustomerProductGroup.
     *
     * @param custProductGroupId the id of CustomerProductGroup.
     */
    public void setCustProductGroupId(long custProductGroupId) {
        this.custProductGroupId = custProductGroupId;
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
}
