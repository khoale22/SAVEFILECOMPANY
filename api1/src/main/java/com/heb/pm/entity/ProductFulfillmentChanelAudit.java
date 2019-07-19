package com.heb.pm.entity;

import com.heb.util.audit.Audit;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import java.io.Serializable;
import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.sql.Timestamp;


/**
 * The persistent class for the PROD_FLFL_CHNL database table.
 *
 */
@Entity
@Table(name="PROD_FLFL_CHNL_AUD")
public class ProductFulfillmentChanelAudit implements Audit, Serializable {
    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private ProductFulfillmentChanelAuditKey key;

    @Column(name = "act_cd")
    private String action;
    @Column(name="EFF_DT")
    private LocalDate effectDate;

    @Column(name="EXPRN_DT")
    private LocalDate expirationDate;

    @Column(name="LST_UPDT_TS")
    private LocalDateTime lastUpdateTime;

    @Column(name="LST_UPDT_UID")
    private String changedBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name="sals_chnl_cd", referencedColumnName = "sals_chnl_cd", insertable = false, updatable = false),
            @JoinColumn(name="flfl_chnl_cd", referencedColumnName = "flfl_chnl_cd", insertable = false, updatable = false)
    })
    @NotFound(action = NotFoundAction.IGNORE)
    private FulfillmentChannel fulfillmentChannel;

    @Transient
    private String actionCode;

    public ProductFulfillmentChanelAudit() {
    }

    public ProductFulfillmentChanelAuditKey getKey() {
        return key;
    }

    public void setKey(ProductFulfillmentChanelAuditKey key) {
        this.key = key;
    }
    /**
     * 	Returns the ActionCode. The action code pertains to the action of the audit. 'UPDAT', 'PURGE', or 'ADD'.
     * 	@return ActionCode
     */
    public String getAction() {
        return action;
    }

    /**
     * Updates the action code
     * @param action the new action
     */
    public void setAction(String action) {
        this.action = action;
    }

    public LocalDate getEffectDate() {
        return effectDate;
    }

    public void setEffectDate(LocalDate effectDate) {
        this.effectDate = effectDate;
    }

    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDate expirationDate) {
        this.expirationDate = expirationDate;
    }

    public LocalDateTime getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(LocalDateTime lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    /**
     * @return Gets the value of fulfillmentChannel and returns fulfillmentChannel
     */
    public void setFulfillmentChannel(FulfillmentChannel fulfillmentChannel) {
        this.fulfillmentChannel = fulfillmentChannel;
    }

    /**
     * Sets the fulfillmentChannel
     */
    public FulfillmentChannel getFulfillmentChannel() {
        return fulfillmentChannel;
    }

    public String getActionCode() {
        return actionCode;
    }

    public void setActionCode(String actionCode) {
        this.actionCode = actionCode;
    }
    @Override
    public String getChangedBy() {
        return changedBy;
    }

    @Override
    public void setChangedBy(String changedBy) {
        this.changedBy = changedBy;
    }

    @Override
    public LocalDateTime getChangedOn() {
        return this.key.getChangedOn();
    }

    public void setChangedOn(LocalDateTime changedOn) {
        this.key.setChangedOn(changedOn);
    }
}