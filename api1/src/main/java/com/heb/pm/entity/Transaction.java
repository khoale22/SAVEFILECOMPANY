/*
 *  Transaction
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.entity;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * This represents the transaction entity.
 *
 * @author vn70529
 * @since 2.23.0
 */
@Entity
@Table(name = "TRANSACTION")
public class Transaction implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @NotNull
    @Column(name = "TRX_ID")
    @GenericGenerator(name = "db-uuid", strategy = "guid")
    @GeneratedValue(generator = "db-uuid")
    private String transactionId;

    @Column(name="SRC_SYSTEM_ID")
    private int sourceSystem;

    @Column(name = "CRE8_TS", updatable = false)
    @ColumnDefault(value = "NOW()")
    private LocalDateTime createTime;

    @Column(name = "CRE8_UID")
    private String createUserId;

    @Column(name = "SRC_INFO_TXT")
    private String sourceInfoTxt;

    /**
     * Returns TransactionId.
     *
     * @return The TransactionId.
     **/
    public String getTransactionId() {
        return transactionId;
    }

    /**
     * Sets the transactionId.
     *
     * @param transactionId The transactionId.
     **/
    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    /**
     * Returns sourceSystem.
     *
     * @return The sourceSystem.
     **/
    public int getSourceSystem() {
        return sourceSystem;
    }

    /**
     * Sets the sourceSystemId.
     *
     * @param sourceSystem The sourceSystemId.
     **/
    public void setSourceSystem(int sourceSystem) {
        this.sourceSystem = sourceSystem;
    }

    /**
     * Returns the time this record was created.
     *
     * @return The time this record was created.
     */
    public LocalDateTime getCreateTime() {
        return createTime;
    }

    /**
     * Sets the time this record was created.
     *
     * @param createTime The time this record was created.
     */
    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    /** Returns the ID of the user who created this record.
     *
     * @return The ID of the user who created this record.
     */
    public String getCreateUserId() {
        return createUserId;
    }

    /**
     * Sets the ID of the user who created this record.
     *
     * @param createUserId The ID of the user who created this record.
     */
    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }

    /**
     * Returns sourceInfoTxt.
     *
     * @return The sourceInfoTxt.
     **/
    public String getSourceInfoTxt() {
        return sourceInfoTxt;
    }

    /**
     * Sets the sourceInfoTxt.
     *
     * @param sourceInfoTxt The sourceInfoTxt.
     **/
    public void setSourceInfoTxt(String sourceInfoTxt) {
        this.sourceInfoTxt = sourceInfoTxt;
    }

    /**
     * Compares this object to another for equality.
     *
     * @param o The object to compare to.
     * @return True if the objects are equal and false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Transaction)) return false;
        Transaction that = (Transaction) o;
        return transactionId != null ? transactionId.equals(that.transactionId) : that.transactionId == null;
    }

    /**
     * Return a String representation of this object.
     *
     * @return A String representation of this object.
     */
    @Override
    public String toString() {
        return "Transaction{" +
                "transactionId=" + transactionId +
                ", sourceSystem='" + sourceSystem + '\'' +
                ", createTime='" + createTime + '\'' +
                ", createUserId=" + createUserId +
                ", sourceInfoTxt='" + sourceInfoTxt + '\'' +
                '}';
    }

    /**
     * Returns a hash code for this object.
     *
     * @return A hash code for this object.
     */
    @Override
    public int hashCode() {
        return transactionId != null ? transactionId.hashCode() : 0;
    }

}
