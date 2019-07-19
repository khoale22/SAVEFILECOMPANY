/*
 * AlertStaging
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.entity;

import com.heb.pm.entity.CandidateWorkRequest;
import com.heb.pm.entity.ProductMaster;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.hibernate.annotations.*;

/**
 * Represents the Alert_Staging_Tbl.
 *
 * @author vn70633
 * @since 2.7.0
 */
@Entity
@Table(name="alert_staging_tbl")
@TypeDefs({
@TypeDef(name = "fixedLengthCharPK", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharTypePK.class)
})
public class AlertStaging implements Serializable{
    private static final long serialVersionUID = 1L;

    public static String ALERT_CATEGORY_ROLE_PM = "PMIMA";
    public static String ALERT_RECIPIENT_TYPE_USER = "USRID";
    public static Character ALERT_HIDDEN_SWITCH_YES = 'Y';

    @Id
    @Column(name = "alrt_id")
    private Integer alertID;

    @Column(name = "alrt_key")
    private String alertKey;

    @Formula("TRIM(LEADING '0' FROM alrt_key)")
    private String trimmedAlertKey;

    @Column(name = "alrt_data_txt")
    private String alertDataTxt;

    @Column(name = "alrt_stat_cd")
    private String alertStatusCD;

    @Column(name = "alrt_typ_cd")
    private String alertTypeCD;

    @Column(name = "alrt_hide_sw")
    private Character alertHideSw;

  	@Type(type="fixedLengthCharPK")
    @Column(name = "asgned_usr_id")
    private String assignedUserID;

    @Type(type="fixedLengthCharPK")
    @Column(name = "delgted_by_usr_id")
    private  String delegatedByUserID;

    @Column(name = "resp_by_dt")
    private Date responseByDate;

    @Column(name = "alrt_crit_pct")
    private double alertCriticalPercent;

    @Column(name = "alrt_stat_uid")
    private String alertStatusUserId;

    @Column(name = "cre8_uid")
    private String createUserId;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name="alrt_id" , referencedColumnName = "alrt_id", updatable = false, insertable = false)
    private List<AlertRecipient> alertRecipients;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinColumn(name="alrt_key" , referencedColumnName = "prod_id", updatable = false, insertable = false)
    private List<ProductOnline> productOnline;

    @Transient
    private ProductMaster productMaster;
    @Transient
    private List<CandidateWorkRequest> candidateWorkRequests;
	@Transient
	private boolean isAlertStagingsFromDB = true;
    @Transient
    private String createdByFullName;
    @Transient
    private String displayName;

    /**
     * Returns the trimmedAlertKey of the alert.
     * @return the trimmedAlertKey of the alert.
     */
    public String getTrimmedAlertKey() {
        return trimmedAlertKey;
    }

    /**
     * Set the trimmedAlertKey of the alert.
     * @param trimmedAlertKey the trimmedAlertKey of the alert.
     */
    public void setTrimmedAlertKey(String trimmedAlertKey) {
        this.trimmedAlertKey = trimmedAlertKey;
    }

    public enum AlertStatusCD {
        ACTIVE("ACTIV"),
        CLOSE("CLOSD");
        private String name;
        AlertStatusCD(String name) {
            this.name = name;
        }

        /**
         * Gets name.
         *
         * @return the name
         */
        public String getName() {
            return this.name;
        }
    }

    public enum AlertTypeCD {
        GENESIS_AP("GENAP"),
        PRODUCT_UPDATES("PRUPD"),
        ECOM_TASK("MYTSK"),
        PRODUCT_PUBLISH("PRPUB");
        private String name;
        AlertTypeCD(String name) {
            this.name = name;
        }
        /**
         * Gets name.
         *
         * @return the name
         */
        public String getName() {
            return this.name;
        }
    }

	/**
	 * Returns the is alert staging from database.
	 * @return
	 */
	public boolean isAlertStagingsFromDB() {
		return isAlertStagingsFromDB;
	}

	/**
	 * Sets the is alert staging from database.
	 * @param alertStagingsFromDB
	 */
	public void setAlertStagingsFromDB(boolean alertStagingsFromDB) {
		isAlertStagingsFromDB = alertStagingsFromDB;
	}

    /**
     * Returns the alert id of the alert.
     * @return The alert id of the alert.
     */
    public Integer getAlertID() {
        return alertID;
    }

    /**
     * Sets the alert id of the alert.
     * @param alertID The alert id of the alert.
     */
    public void setAlertID(Integer alertID) {
        this.alertID = alertID;
    }

    /**
     * Returns the alert key of the alert.
     * @return The alert key of the alert.
     */
    public String getAlertKey() {
        return alertKey;
    }

    /**
     * Sets the alert key of the alert.
     * @param alertKey The alert key of the alert.
     */
    public void setAlertKey(String alertKey) {
        this.alertKey = alertKey;
    }

    /**
     *  Returns the alert data txt of the alert.
     * @return The alert data txt of the alert.
     */
    public String getAlertDataTxt() {
        return alertDataTxt;
    }

    /**
     * Sets the alert data txt of the alert.
     * @param alertDataTxt The alert data txt of the alert.
     */
    public void setAlertDataTxt(String alertDataTxt) {
        this.alertDataTxt = alertDataTxt;
    }

    /**
     * Returns the alert status cd of the alert.
     * @return The alert status cd of the alert.
     */
    public String getAlertStatusCD() {
        return alertStatusCD;
    }

    /**
     * Sets the alert status cd of the alert.
     * @param alertStatusCD The alert status cd of the alert.
     */
    public void setAlertStatusCD(String alertStatusCD) {
        this.alertStatusCD = alertStatusCD;
    }

    /**
     * Returns the alert type cd of the alert.
     * @return The alert type cd of the alert.
     */
    public String getAlertTypeCD() {
        return alertTypeCD;
    }

    /**
     * Sets the alert type cd of the alert.
     * @param alertTypeCD The alert type cd of the alert.
     */
    public void setAlertTypeCD(String alertTypeCD) {
        this.alertTypeCD = alertTypeCD;
    }

    /**
     * Returns the alert hide switch of the alert.
     * @return The alert hide switch of the alert.
     */
    public Character getAlertHideSw() {
        return alertHideSw;
    }

    /**
     * Sets the alert hide switch of the alert.
     * @param alertHideSw The alert hide switch of the alert.
     */
    public void setAlertHideSw(Character alertHideSw) {
        this.alertHideSw = alertHideSw;
    }

    /**
     * Returns the assigned user id of the alert.
     * @return The assigned user id of the alert.
     */
    public String getAssignedUserID() {
        return assignedUserID;
    }

    /**
     * Sets the assigned user id of the alert.
     * @param assignedUserID The assigned user id of the alert.
     */
    public void setAssignedUserID(String assignedUserID) {
        this.assignedUserID = assignedUserID;
    }

    /**
     * Returns the delegated by user id of the alert.
     * @return The delegated by user id of the alert.
     */
    public String getDelegatedByUserID() {
        return delegatedByUserID;
    }

    /**
     * Sets the delegated by user id of the alert.
     * @param delegatedByUserID The delegated by user id of the alert.
     */
    public void setDelegatedByUserID(String delegatedByUserID) {
        this.delegatedByUserID = delegatedByUserID;
    }

    /**
     * Returns the response by date of the alert.
     * @return The response by date of the alert.
     */
    public Date getResponseByDate() {
        return responseByDate;
    }

    /**
     * Sets the response by date of the alert.
     * @param responseByDate The response by date of the alert.
     */
    public void setResponseByDate(Date responseByDate) {
        this.responseByDate = responseByDate;
    }

    /**
     * Returns the alert critical percent of the alert.
     * @return The alert critical percent of the alert.
     */
    public double getAlertCriticalPercent() {
        return alertCriticalPercent;
    }

    /**
     * Sets the alert critical percent of the alert.
     * @param alertCriticalPercent The alert critical percent of the alert.
     */
    public void setAlertCriticalPercent(double alertCriticalPercent) {
        this.alertCriticalPercent = alertCriticalPercent;
    }

    public ProductMaster getProductMaster() {
        return productMaster;
    }

    public void setProductMaster(ProductMaster productMaster) {
        this.productMaster = productMaster;
    }

    public List<CandidateWorkRequest> getCandidateWorkRequests() {
        return candidateWorkRequests;
    }

    public void setCandidateWorkRequests(List<CandidateWorkRequest> candidateWorkRequests) {
        this.candidateWorkRequests = candidateWorkRequests;
    }

    public String getAlertStatusUserId() {
        return alertStatusUserId;
    }

    public void setAlertStatusUserId(String alertStatusUserId) {
        this.alertStatusUserId = alertStatusUserId;
    }

    public String getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }

    /**
     * Returns display name of created user.
     * @return the display name.
     */
    public String getCreatedByFullName() {
        return createdByFullName;
    }

    /**
     * Sets the fullname
     * @param createdByFullName the fullname.
     */
    public void setCreatedByFullName(String createdByFullName) {
        this.createdByFullName = createdByFullName;
    }

    /**
     * Returns display name basing on product.
     * @return the display name.
     */
    public String getDisplayName() {
        return displayName;
    }
    /**
     * Sets the displayName
     * @param displayName the displayName.
     */
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    /**
     * Compares another object to this one. If that object is a Location, it uses they keys
     * to determine if they are equal and ignores non-key values for the comparison.
     *
     * @param o The object to compare to.
     * @return True if they are equal and false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AlertStaging that = (AlertStaging) o;

        return alertID != null ? alertID.equals(that.alertID) : that.alertID == null;
    }

    /**
     * Returns a hash code for the object. Equal objects return the same falue. Unequal objects (probably) return
     * different values.
     *
     * @return A hash code for the object.
     */
    @Override
    public int hashCode() {
        int result;
        result = alertID != null ? alertID.hashCode() : 0;
        return result;
    }

    /**
     * Returns a string representation of the object.
     *
     * @return A string representation of the object.
     */
    @Override
    public String toString() {
        return "AlertStaging{" +
                "alertID=" + alertID +
                ", alertKey='" + alertKey + '\'' +
                ", alertDataTxt='" + alertDataTxt + '\'' +
                ", alertStatusCD='" + alertStatusCD + '\'' +
                ", alertTypeCD='" + alertTypeCD + '\'' +
                ", alertHideSw=" + alertHideSw +
                ", assignedUserID='" + assignedUserID + '\'' +
                ", delegatedByUserID='" + delegatedByUserID + '\'' +
                ", responseByDate=" + responseByDate +
                ", alertCriticalPercent=" + alertCriticalPercent +
                ", alertStatusUserId='" + alertStatusUserId + '\'' +
                '}';
    }
}
