/*
 *  FulfillmentChannelKey
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * The primary key class for the FLFL_CHNL database table.
 *
 * @author vn70516
 * @since 2.14.0
 */
@Embeddable
public class FulfillmentChannelKey implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "sals_chnl_cd")
    private String salesChannelCode;

    @Column(name = "flfl_chnl_cd")
    private String fulfillmentChannelCode;

    /**
     * @return Gets the value of salesChannelCode and returns salesChannelCode
     */
    public void setSalesChannelCode(String salesChannelCode) {
        this.salesChannelCode = salesChannelCode;
    }

    /**
     * Sets the salesChannelCode
     */
    public String getSalesChannelCode() {
        return salesChannelCode;
    }

    /**
     * @return Gets the value of fulfillmentChannelCode and returns fulfillmentChannelCode
     */
    public void setFulfillmentChannelCode(String fulfillmentChannelCode) {
        this.fulfillmentChannelCode = fulfillmentChannelCode;
    }

    /**
     * Sets the fulfillmentChannelCode
     */
    public String getFulfillmentChannelCode() {
        return fulfillmentChannelCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FulfillmentChannelKey)) return false;

        FulfillmentChannelKey that = (FulfillmentChannelKey) o;

        if (getSalesChannelCode() != null ? !getSalesChannelCode().equals(that.getSalesChannelCode()) : that.getSalesChannelCode() != null)
            return false;
        return getFulfillmentChannelCode() != null ? getFulfillmentChannelCode().equals(that.getFulfillmentChannelCode()) : that.getFulfillmentChannelCode() == null;
    }

    @Override
    public int hashCode() {
        int result = getSalesChannelCode() != null ? getSalesChannelCode().hashCode() : 0;
        result = 31 * result + (getFulfillmentChannelCode() != null ? getFulfillmentChannelCode().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "FulfillmentChannelKey{" +
                "salesChannelCode='" + salesChannelCode + '\'' +
                ", fulfillmentChannelCode='" + fulfillmentChannelCode + '\'' +
                '}';
    }
}