/*
 *  ExcelUploadEcommerceAssortment
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.batchUpload;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * This is BatchUpload interface. This is contain all template of file upload
 *
 * @author vn55306
 * @since 2.8.0
 */
public abstract class BatchUpload implements Serializable{
    /**
     * generated serialVersionUID.
     */
    private static final long serialVersionUID = 5178941195244207553L;

    private List<String> errors;

    /**
     * Return errors reference. Before return check if it is NULL, if so initializes it to empty array list.
     *
     * @return list of errors.
     */
    public List<String> getErrors() {
        errors = (errors == null) ? new ArrayList<>() : errors;
        return errors;
    }

    /**
     * Sets errors.
     * @param errors list of errors.
     */
    public void setErrors(List<String> errors) {
        this.errors = errors;
    }

    public boolean hasErrors() {
        return !getErrors().isEmpty();
    }

}
