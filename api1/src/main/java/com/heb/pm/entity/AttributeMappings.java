/*
 *  AttributeMappings
 *  Copyright (c) 2018 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.entity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * This represents a list of My Attributes.
 *
 * @author vn70529
 * @since 2.17.0
 */
@XmlRootElement(name = "AttributeMappings")
@XmlAccessorType(XmlAccessType.FIELD)
public class AttributeMappings {
    @XmlElement(name = "AttributeMapping")
    private List<AttributeMapping> attributeMappings = null;

    public List<AttributeMapping> getAttributeMappings() {
        return attributeMappings;
    }

    public void setAttributeMappings(List<AttributeMapping> attributeMappings) {
        this.attributeMappings = attributeMappings;
    }
}
