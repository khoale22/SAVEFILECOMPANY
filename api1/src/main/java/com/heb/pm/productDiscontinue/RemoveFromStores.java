/*
 *
 *  RemoveFromStores
 *
 *   Copyright (c) 2016 HEB
 *   All rights reserved.
 *
 *   This software is the confidential and proprietary information
 *    of HEB.
 *
 *
 */
package com.heb.pm.productDiscontinue;

import java.io.Serializable;

/**
 * Represents RemoveFromStores.
 *
 * @author m594201
 * @since 9/21/2016
 */
public class RemoveFromStores implements Serializable {

    private static final long serialVersionUID = 1L;

    private long upc;
    private boolean removedInStores;
    public boolean successful;
    private String errorMessage;

    /**
     * Gets is successful
     *
     * @return successful
     */
    public boolean isSuccessful() {
        return successful;
    }

    /**
     * Set is successful
     *
     * @return successful
     */
    public void setSuccessful(boolean successful) {
        this.successful = successful;
    }

    /**
     * Gets error message.
     *
     * @return the error message
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * Sets error message.
     *
     * @param errorMessage the error message
     */
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }


    /**
     * Gets upc.
     *
     * @return the upc
     */
    public long getUpc() {
        return upc;
    }

    /**
     * Sets upc.
     *
     * @param upc the upc
     */
    public void setUpc(Long upc) {
        this.upc = upc;
    }

    /**
     * Is removed in stores boolean.
     *
     * @return the boolean
     */
    public boolean isRemovedInStores() {
        return removedInStores;
    }

    /**
     * Sets removed in stores.
     *
     * @param removedInStores the removed in stores
     */
    public void setRemovedInStores(boolean removedInStores) {
        this.removedInStores = removedInStores;
    }
}
