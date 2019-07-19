package com.heb.pm.entity;

import com.heb.util.audit.Audit;
import org.springframework.data.domain.Sort;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * This represents the CustomerProductGroupMembershipAudit.
 *
 * @author vn70529
 * @since 2.15.0
 */
@Entity
@Table(name = "cust_prod_grp_m_a")
public class CustomerProductGroupMembershipAudit implements Serializable, Audit {

    private static final String SORT_DEFAULT_FIELD = "key.changedOn";

    @EmbeddedId
    private CustomerProductGroupMembershipAuditKey key;

    @Column(name = "act_cd")
    private String action;

    @Column(name = "lst_updt_uid")
    private String changedBy;



    /**
     * Returns the Key
     *
     * @return Key
     */
    public CustomerProductGroupMembershipAuditKey getKey() {
        return key;
    }

    /**
     * Sets the Key
     *
     * @param key The Key
     */
    public void setKey(CustomerProductGroupMembershipAuditKey key) {
        this.key = key;
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
                new Sort.Order(Sort.Direction.ASC, CustomerProductGroupMembershipAudit.SORT_DEFAULT_FIELD)
        );
    }
}
