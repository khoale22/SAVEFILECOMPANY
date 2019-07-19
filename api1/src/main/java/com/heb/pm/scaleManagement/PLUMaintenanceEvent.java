/*
 * PLUMaintenanceEvent
 *
 *  Copyright (c) 2018 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
package com.heb.pm.scaleManagement;

import com.heb.pm.entity.ScaleUpc;
import org.springframework.context.ApplicationEvent;

import java.util.List;

/**
 * The PLU maintenance event extend ApplicationEvent to store the event data.
 *
 * @author vn70529
 * @since 2.20.0
 */
public class PLUMaintenanceEvent extends ApplicationEvent {

    /**
     * The list of scale upc maintenance.
     */
    List<ScaleUpc> scaleUpcs;

    /**
     * The userId change data ingredient(Statement).
     */
    private String createdBy;

    /**
     * Create a new ApplicationEvent.
     *
     * @param source the object on which the event initially occurred (never {@code null})
     * @param scaleUpcs the upc key.
     * @param createdBy the user id.
     */
    public PLUMaintenanceEvent(Object source, List<ScaleUpc> scaleUpcs, String createdBy) {
        super(source);
        this.createdBy = createdBy;
        this.scaleUpcs = scaleUpcs;
    }

    /**
     * The list ScaleUpc maintenance.
     *
     * @return List<ScaleUpc>
     */
    public List<ScaleUpc> getScaleUpcs() {
        return scaleUpcs;
    }

    /**
     * Set the list ScaleUpc maintenance.
     *
     * @param scaleUpcs
     */
    public void setScaleUpcs(List<ScaleUpc> scaleUpcs) {
        this.scaleUpcs = scaleUpcs;
    }

    /**
     * Gets the user create alert.
     *
     * @return createdBy the user id.
     */
    public String getCreatedBy() {
        return createdBy;
    }

    /**
     * Set the user created alert.
     *
     * @param createdBy the user id.
     */
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }
}
