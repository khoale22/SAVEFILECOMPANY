/*
 * AlertComment
 *
 *  Copyright (c) 2018 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.entity;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Represents the Alert_Comment table.
 *
 * @author vn40486
 * @since 2.16.0
 */
@Entity
@Table(name="alert_comment")
public class AlertComment implements Serializable{
    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private AlertCommentKey key;

    @Column(name = "cmt_txt")
    private String comment;

    @Column(name = "cre8_id")
    private String createUserID;

    @Column(name = "cre8_ts")
    private LocalDateTime createTime;

    public AlertCommentKey getKey() {
        return key;
    }

    public void setKey(AlertCommentKey key) {
        this.key = key;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getCreateUserID() {
        return createUserID;
    }

    public void setCreateUserID(String createUserID) {
        this.createUserID = createUserID;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AlertComment that = (AlertComment) o;

        return key.equals(that.key);
    }

    @Override
    public int hashCode() {
        return key.hashCode();
    }

    /**
     * Returns a string representation of the object.
     *
     * @return A string representation of the object.
     */
    @Override
    public String toString() {
        return "AlertComment{" +
                "key=" + key +
                ", comment='" + comment + '\'' +
                ", createUserID='" + createUserID + '\'' +
                ", createTime=" + createTime +
                '}';
    }
}
