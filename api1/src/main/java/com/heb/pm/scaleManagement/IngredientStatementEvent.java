/*
 * IngredientStatementEvent
 *
 *  Copyright (c) 2018 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
package com.heb.pm.scaleManagement;

import org.springframework.context.ApplicationEvent;

import java.util.List;

/**
 * The Ingredient Statement Event extend ApplicationEvent to store the event data.
 *
 * @author vn70529
 * @since 2.19.0
 */
public class IngredientStatementEvent extends ApplicationEvent {

    /**
     * The list of ingredient statement number change.
     */
    private List<Long> ingredientStatementNumberChangeList;
    /**
     * The upc key.
     */
    private Long upc;
    /**
     * The userId change data ingredient(Statement).
     */
    private String createdBy;

    /**
     * Create a new ApplicationEvent.
     *
     * @param source the object on which the event initially occurred (never {@code null})
     * @param upc the upc key.
     * @param ingredientStatementNumberChangeList the list of ingredient statement number change.
     * @param createdBy the user id.
     */
    public IngredientStatementEvent(Object source,Long upc, List<Long> ingredientStatementNumberChangeList, String createdBy) {
        super(source);
        this.ingredientStatementNumberChangeList = ingredientStatementNumberChangeList;
        this.createdBy = createdBy;
        this.upc = upc;
    }


    public List<Long> getIngredientStatementNumberChangeList() {
        return ingredientStatementNumberChangeList;
    }

    public void setIngredientStatementNumberChangeList(List<Long> ingredientStatementNumberChangeList) {
        this.ingredientStatementNumberChangeList = ingredientStatementNumberChangeList;
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

    /**
     * Gets the upc key.
     *
     * @return upc the upc key.
     */
    public Long getUpc() {
        return upc;
    }

    /**
     * Sets the upc key.
     *
     * @param upc the upc key.
     */
    public void setUpc(Long upc) {
        this.upc = upc;
    }
}
