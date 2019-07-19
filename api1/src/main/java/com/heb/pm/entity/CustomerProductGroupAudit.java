package com.heb.pm.entity;

import com.heb.util.audit.Audit;
import com.heb.util.audit.AuditableField;
import org.springframework.data.domain.Sort;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Represents Product Scan Code Extent Audit Data.
 * @author vn70529
 * @since 2.15.0
 */
@Entity
@Table(name = "cust_prod_grp_aud")
public class CustomerProductGroupAudit implements Serializable, Audit {

    private static final String SORT_DEFAULT_FIELD = "key.changedOn";

    @EmbeddedId
    private CustomerProductGroupAuditKey key;

    @AuditableField(displayName = "Customer Product Group Name")
    @Column(name = "cust_prod_grp_nm")
    private String custProductGroupName;

    @AuditableField(displayName = "Product Group Long Description")
    @Column(name = "cust_prod_grp_long_d")
    private String custProductGroupDescriptionLong;

    @AuditableField(displayName = "Product Group Description")
    @Column(name = "cust_prod_grp_des")
    private String custProductGroupDescription;

    @Column(name = "act_cd")
    private String action;

    @Column(name = "lst_updt_uid")
    private String changedBy;

    /**
     * Returns the Key
     *
     * @return Key
     */
    public CustomerProductGroupAuditKey getKey() {
        return key;
    }

    /**
     * Sets the Key
     *
     * @param key The Key
     */
    public void setKey(CustomerProductGroupAuditKey key) {
        this.key = key;
    }

    /**
     * Gets the cust product group name.
     *
     * @return the cust product group name.
     */
    public String getCustProductGroupName() {
        return custProductGroupName;
    }

    /**
     * Sets  the cust product group name.
     *
     * @param custProductGroupName the cust product group name.
     */
    public void setCustProductGroupName(String custProductGroupName) {
        this.custProductGroupName = custProductGroupName;
    }

    /**
     * Gets the cust product group description.
     *
     * @return the cust product group description.
     */
    public String getCustProductGroupDescription() {
        return custProductGroupDescription;
    }

    /**
     * Sets the cust product group description.
     *
     * @param custProductGroupDescription the cust product group description.
     */
    public void setCustProductGroupDescription(String custProductGroupDescription) {
        this.custProductGroupDescription = custProductGroupDescription;
    }

    /**
     * Get customer product group description long.
     * @return customer product group description long.
     */
    public String getCustProductGroupDescriptionLong() {
        return custProductGroupDescriptionLong;
    }

    /**
     * Set customer product group description long.
     * @param custProductGroupDescriptionLong customer product group description long.
     */
    public void setCustProductGroupDescriptionLong(String custProductGroupDescriptionLong) {
        this.custProductGroupDescriptionLong = custProductGroupDescriptionLong;
    }

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
     * Returns the default sort order for the Vocabulary table.
     *
     * @return The default sort order for the Vocabulary table.
     */
    public static Sort getDefaultSort() {
        return  new Sort(
                new Sort.Order(Sort.Direction.ASC, CustomerProductGroupAudit.SORT_DEFAULT_FIELD)
        );
    }
}
