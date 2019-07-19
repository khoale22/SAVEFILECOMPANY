/*
 * com.heb.pm.entity.ProductDiscontinue
 *
 * Copyright (c) 2017 HEB
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of HEB.
 */

package com.heb.pm.entity;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.Column;
import javax.persistence.Entity;

import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Holds the parameters that indicate information relating to the relationship group.
 *
 * @author s753601
 * @since 2.5.0
 */
@Entity
@Table(name="RLSHP_GRP")
@TypeDefs({
	@TypeDef(name = "fixedLengthCharPK", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharTypePK.class),
})
public class RelationshipGroup implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name="RLSHP_GRP_ID")
    private int relationshipGroupId;

    @Column(name = "RLSHP_GRP_DES")
    @Type(type="fixedLengthCharPK")
    private String relationshipGroupDescription;

    /**
     * Get the relationship group id
     * @return the relationship group id
     */
    public int getRelationshipGroupId() {
        return relationshipGroupId;
    }

    /**
     * updates the relationship group id
     * @param relationshipGroupId the relationship group id
     */
    public void setRelationshipGroupId(int relationshipGroupId) {
        this.relationshipGroupId = relationshipGroupId;
    }

    /**
     * Returns the relationship group description
     * @return the relationship group description
     */
    public String getRelationshipGroupDescription() {
        return relationshipGroupDescription;
    }

    /**
     * Replaces the relationship group description
     * @param relationshipGroupDescription the new relationship group description
     */
    public void setRelationshipGroupDescription(String relationshipGroupDescription) {
        this.relationshipGroupDescription = relationshipGroupDescription;
    }

    /**
     * The new toString method
     * @return
     */
    @Override
    public String toString(){
        return "Relationship Group{" +
                "ID: " + getRelationshipGroupId() +
                ", Description: '" + getRelationshipGroupDescription() + "'}";
    }
}
