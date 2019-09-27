/*
 * BaseVO
 *
 * Copyright (c) 2018 H-E-B
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of H-E-B.
 */
package com.heb.batch.wic.webservice.vo;

import java.io.Serializable;

/**
 * This is BaseVO class.
 *
 * @author vn55306
 * @since 1.0.0
 */
public class BaseVO implements Serializable {

    private String actionCode;
    private String systemEnvironment = "  "; // For Online mode  two space. For Batch mode BT
    public String getActionCode() {
        return actionCode;
    }
    public void setActionCode(String actionCode) {
        this.actionCode = actionCode;
    }

    public String getSystemEnvironment() {
        return systemEnvironment;
    }

    public void setSystemEnvironment(String systemEnvironment) {
        this.systemEnvironment = systemEnvironment;
    }


}
