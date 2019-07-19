package com.heb.pm.entity;

import com.heb.util.audit.Audit;
import com.heb.util.audit.AuditableField;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Represents a sub-commodity audit.
 *
 * @author vn70529
 * @since 2.17.9
 */
@Entity
@Table(name = "pd_cls_com_sub_aud")
@TypeDef(name = "fixedLengthChar", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharType.class)
public class SubCommodityAudit  implements Serializable, Audit {
    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private SubCommodityAuditKey key;

    @Column(name = "act_cd")
    private String action;

    @Column(name = "lst_updt_uid")
    private String changedBy;

    @Column(name = "LST_UPDT_TS")
    private LocalDate lstUpdtTs;

    @AuditableField(displayName = "Taxable Tax Category", filter = FilterConstants.SUB_COMMODITY_DEFAULTS_AUDIT)
    @Column(name = "vertex_tax_cat_cd")
    @Type(type="fixedLengthChar")
    private String taxCategoryCode;

    @AuditableField(displayName = "Food Stamp", filter = FilterConstants.SUB_COMMODITY_DEFAULTS_AUDIT)
    @Column(name = "pd_fd_stamp_cd")
    private String foodStampEligible;

    @AuditableField(displayName = "Taxable", filter = FilterConstants.SUB_COMMODITY_DEFAULTS_AUDIT)
    @Column(name = "pd_crg_tax_cd")
    private String taxEligible;

    @AuditableField(displayName = "Non- Taxable Tax Category", filter = FilterConstants.SUB_COMMODITY_DEFAULTS_AUDIT)
    @Column(name = "vertex_non_tax_cd")
    @Type(type="fixedLengthChar")
    private String nonTaxCategoryCode;

    /**
     * 	Returns the ActionCode. The action code pertains to the action of the audit. 'UPDAT', 'PURGE', or 'ADD'.
     * 	@return ActionCode
     */
    @Override
    public String getAction() {
        return action;
    }

    /**
     * Updates the action code
     * @param action the new action
     */
    @Override
    public void setAction(String action) {
        this.action = action;
    }

    /**
     * Returns the key for this object.
     *
     * @return The key for this object.
     */
    public SubCommodityAuditKey getKey() {
        return key;
    }

    /**
     * Sets the key for this object.
     *
     * @param key They key for this object.
     */
    public void setKey(SubCommodityAuditKey key) {
        this.key = key;
    }

    /**
     * Returns changed by. The changed by shows who was doing the action that is being audited. This is the uid(login) that a
     * user has.
     *
     * @return the changed by
     */
    @Override
    public String getChangedBy() {
        return changedBy;
    }

    /**
     * Sets the changed by uid received from the database.
     *
     * @param changedBy the changed by
     */
    @Override
    public void setChangedBy(String changedBy) {
        this.changedBy = changedBy;
    }

    /**
     * Gets the changed on time. This is when the modification was done.
     *
     * @return The time the modification was done.
     */
    @Override
    public LocalDateTime getChangedOn() {
        return this.key.getChangedOn();
    }

    /**
     * Sets the changed on time.
     *
     * @param changedOn The time the modification was done.
     */
    @Override
    public void setChangedOn(LocalDateTime changedOn) {
        this.key.setChangedOn(changedOn);
    }

    /**
     * Returns FoodStampEligible.
     *
     * @return The FoodStampEligible.
     **/
    public String getFoodStampEligible() {
        return foodStampEligible;
    }

    /**
     * Sets the FoodStampEligible.
     *
     * @param foodStampEligible The FoodStampEligible.
     **/
    public void setFoodStampEligible(String foodStampEligible) {
        this.foodStampEligible = foodStampEligible;
    }

    /**
     * Returns NonTaxCategoryCode.
     *
     * @return The NonTaxCategoryCode.
     **/
    public String getNonTaxCategoryCode() {
        return nonTaxCategoryCode;
    }

    /**
     * Sets the NonTaxCategoryCode.
     *
     * @param nonTaxCategoryCode The NonTaxCategoryCode.
     **/
    public void setNonTaxCategoryCode(String nonTaxCategoryCode) {
        this.nonTaxCategoryCode = nonTaxCategoryCode;
    }

    /**
     * Returns TaxCategoryCode.
     *
     * @return The TaxCategoryCode.
     **/
    public String getTaxCategoryCode() {
        return taxCategoryCode;
    }

    /**
     * Sets the TaxCategoryCode.
     *
     * @param taxCategoryCode The TaxCategoryCode.
     **/
    public void setTaxCategoryCode(String taxCategoryCode) {
        this.taxCategoryCode = taxCategoryCode;
    }

    /**
     * Returns TaxEligible.
     *
     * @return The TaxEligible.
     **/
    public String getTaxEligible() {
        return taxEligible;
    }

    /**
     * Sets the TaxEligible.
     *
     * @param taxEligible The TaxEligible.
     **/
    public void setTaxEligible(String taxEligible) {
        this.taxEligible = taxEligible;
    }

    /**
     * Sets the lstUpdtTs
     */
    public LocalDate getLstUpdtTs() {
        return lstUpdtTs;
    }

    /**
     * @return Gets the value of lstUpdtTs and returns lstUpdtTs
     */
    public void setLstUpdtTs(LocalDate lstUpdtTs) {
        this.lstUpdtTs = lstUpdtTs;
    }

    /**
     * Returns a String representation of the object.
     *
     * @return A String representation of the object.
     */
    @Override
    public String toString() {
        return "SubCommodity{" +
                "key=" + key +
                ", action='" + action +
                ", changedBy='" + changedBy +
                ", foodStampEligible=" + foodStampEligible +
                ", taxEligible=" + taxEligible +
                ", nonTaxCategoryCode='" + nonTaxCategoryCode + '\'' +
                ", taxCategoryCode='" + taxCategoryCode + '\'' +
                '}';
    }
}
