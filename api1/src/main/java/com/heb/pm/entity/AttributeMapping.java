/*
 *  AttributeMappings
 *  Copyright (c) 2018 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.entity;

import javax.xml.bind.annotation.XmlElement;

/**
 * This represents a My Attribute.
 *
 * @author vn70529
 * @since 2.17.0
 */
public class AttributeMapping {

    private String attributeName;

    private String existingApplication;

    private String newScreen;

    public String getAttributeName() {
        return attributeName;
    }
    @XmlElement(name = "AttributeName")
    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    public String getExistingApplication() {
        return existingApplication;
    }
    @XmlElement(name = "ExistingApplication")
    public void setExistingApplication(String existingApplication) {
        this.existingApplication = existingApplication;
    }

    public String getNewScreen() {
        return newScreen;
    }
    @XmlElement(name = "NewScreen")
    public void setNewScreen(String newScreen) {
        this.newScreen = newScreen;
    }
}
