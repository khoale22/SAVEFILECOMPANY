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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.*;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * Holds the primary keys that indicate information relating to the attribute relationships.
 *
 * @author s753601
 * @since 2.5.0
 */
@Entity
@Table(name="ATTR")
@TypeDefs({
	@TypeDef(name = "fixedLengthCharPK", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharTypePK.class),
})
public class Attribute implements Serializable{

	public static final int INSERT = 0;
	public static final int UPDATE = 1;
	public static final int DELETE = 2;

    private static final long serialVersionUID = 1L;

    public static final long DISPLAY_NAME = 1664;
    public static final long BRAND = 1672;
    public static final long NET_CONTENT = 1628L;
    public static final long NET_CONTENT_UNIT_OF_MEASURE = 1629L;
    public static final long SIZE = 1636L;
    public static final long PRODUCT_UOM = 1680L;

	public static final String LIST_DOMAIN_CODE = "S";

	public static final String PHYSICAL_ATTRIBUTE = "PHY";
	public static final String OPTIONAL = "OPT";

	public enum LogicalAttribute {
    	LOGICAL_ATTRIBUTE_BRAND(1672L),
    	LOGICAL_ATTRIBUTE_WARNING(1677L),
    	LOGICAL_ATTRIBUTE_ALLERGENS(1644L),
    	LOGICAL_ATTRIBUTE_WARNINGS(1682L),
    	LOGICAL_ATTRIBUTE_PDP_TEMPLATE(515L),
    	LOGICAL_ATTRIBUTE_DIRECTIONS(1676L),
    	LOGICAL_ATTRIBUTE_NUTRITION_FACTS(1679L),
    	LOGICAL_ATTRIBUTE_DIMENSIONS(1784L),
    	LOGICAL_ATTRIBUTE_SPECIFICATION(1728L),
    	LOGICAL_ATTRIBUTE_ROMANCE_COPY(1666L),
    	LOGICAL_ATTRIBUTE_TAGS(1729L),
    	LOGICAL_ATTRIBUTE_INGREDIENT_STATEMENT(1674L),
    	LOGICAL_ATTRIBUTE_DISPLAY_NAME(1664L),
    	LOGICAL_ATTRIBUTE_SIZE(1667L),
    	LOGICAL_ATTRIBUTE_INGREDIENTS_PET_GUARANTEED(1803L),
    	LOGICAL_ATTRIBUTE_INGREDIENTS(1643L);

    	private Long value;

    	LogicalAttribute(Long value) {
    		this.value = value;
    	}

    	public Long getValue() {
    		return this.value;
    	}
    	
    	public static LogicalAttribute getAttribute(Long attributeId) {
            for (LogicalAttribute attribute : LogicalAttribute.values()) {
                if (attribute.getValue().equals(attributeId)) {
                    return attribute;
                }
            }
            return null;
        }
    }

    public Attribute() {
		super();
	}

    public Attribute(Attribute toCopy) {
		super();
		this.setApplicableTypeCode(toCopy.getApplicableTypeCode());
		this.setAttributeId(toCopy.getAttributeId());
		this.setAttributeName(toCopy.getAttributeName());
		this.setAttributeDescription(toCopy.getAttributeDescription());
		this.setSourceSystemId(toCopy.getSourceSystemId());
		this.setAttributeInstantDescription(toCopy.getAttributeInstantDescription());
		this.setAttributeValueList(toCopy.getAttributeValueList());
		this.setAttributeDomainCode(toCopy.getAttributeDomainCode());
		this.setDynamicAttributeSwitch(toCopy.getDynamicAttributeSwitch());
		this.setMultipleValueSwitch(toCopy.getMultipleValueSwitch());
		this.setPrecision(toCopy.getPrecision());
		this.setMaximumLength(toCopy.getMaximumLength());
		this.setCreateUserId(toCopy.getCreateUserId());
		this.setLastUpdateUserId(toCopy.getLastUpdateUserId());
		this.setRequired(toCopy.getRequired());
		this.setLogicalOrPhysical(toCopy.getLogicalOrPhysical());
		this.setOptionalOrRequired(toCopy.getOptionalOrRequired());
		this.setExternalId(toCopy.getExternalId());
	}

    @Id
    @Column(name="ATTR_ID")
    private Long attributeId;

    @Column(name="ATTR_NM")
    private String attributeName;

    @JsonIgnoreProperties("attribute")
    @OneToMany(mappedBy = "attribute", fetch = FetchType.LAZY)
    private List<LogicalPhysicalRelationship> logicalPhysicalRelationships;

	@OneToMany(mappedBy="attribute", fetch = FetchType.LAZY)
	private List<EcommerUserGroupAttribute> ecommerUserGroupAttributes;

	@Column(name="SRC_SYSTEM_ID")
    private Long sourceSystemId;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="SRC_SYSTEM_ID", referencedColumnName = "src_system_id", insertable = false, updatable = false)
	private SourceSystem sourceSystem;

	@Column(name="ATTR_XTRNL_ID")
	private String externalId;

	@Column(name = "ATTR_DES")
    @Type(type="fixedLengthCharPK")
    private String attributeDescription;

    @Column(name = "ATTR_INSTN_DES")
    @Type(type="fixedLengthCharPK")
    private String attributeInstantDescription;

    @Column(name = "ATTR_VAL_LIST_SW")
	private boolean attributeValueList;

    @Transient
	private List<EntityAttributeCode> attributeValues;

    @Column(name = "ATTR_DOMAIN_CD")
    @Type(type="fixedLengthCharPK")
    private String attributeDomainCode;

    @Column(name="DYN_ATTR_SW")
    private Boolean dynamicAttributeSwitch;

    @Column(name = "MULT_VALS_SW")
    private Boolean multipleValueSwitch;

	@Column(name="ATTR_PRCSN_NBR")
	private Long precision;

	@Column(name = "ATTR_MAX_LN_NBR")
	private Long maximumLength;

	@Column(name="CRE8_UID")
	private String createUserId;

	@Column(name="LST_UPDT_UID")
	private String lastUpdateUserId;

	@Column(name="ITM_PROD_KEY_CD")
	@Type(type="fixedLengthCharPK")
	private String applicableTypeCode;

    @Transient
	private boolean tag;

	@Transient
	private boolean specification;

	@Transient
	private String tagOperation;

	@Transient
	private String specificationOperation;

	@Transient
	private String entityAbbreviation;

	@Transient
	private String attributeDomainDescription;

	@Transient
	private String operation;

	@Transient
	private int action;

	@Column(name="ATTR_REQ_SW")
	private String required;

	@Column(name = "LOGIC_PHY_TYP_CD")
	private String logicalOrPhysical;

	@Column(name="OPT_LVL_CD")
	private String optionalOrRequired;

	@Transient
	private boolean inherited;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ITM_PROD_KEY_CD", referencedColumnName = "ITM_PROD_KEY_CD", insertable = false, updatable = false)
	private ApplicableType applicableType;

	@Transient
	private int levelOfHierarchy;

	/**
	 * Returns LevelOfHierarchy.
	 *
	 * @return The LevelOfHierarchy.
	 **/
	public int getLevelOfHierarchy() {
		return levelOfHierarchy;
	}

	/**
	 * Sets the LevelOfHierarchy.
	 *
	 * @param levelOfHierarchy The LevelOfHierarchy.
	 **/
	public Attribute setLevelOfHierarchy(int levelOfHierarchy) {
		this.levelOfHierarchy = levelOfHierarchy;
		return this;
	}

	public boolean isInherited() {
		return inherited;
	}

	public void setInherited(boolean inherited) {
		this.inherited = inherited;
	}

	/**
	 * Getter for entity abbreviation
	 * @return
	 */
	public String getEntityAbbreviation() {
		return entityAbbreviation;
	}

	/**
	 * Setter for entity abbreviation
	 * @param entityAbbreviation
	 */
	public void setEntityAbbreviation(String entityAbbreviation) {
		this.entityAbbreviation = entityAbbreviation;
	}

	/**
	 * Getter for tag operation
	 * @return
	 */
	public String getTagOperation() {
		return tagOperation;
	}

	/**
	 * Getter for specification operation
	 * @return
	 */
	public String getSpecificationOperation() {
		return specificationOperation;
	}

	/**
	 * Getter for specification
	 * @return
	 */
	public boolean getSpecification() {
		return specification;
	}

	/**
	 * Getter for tag property
	 * @return
	 */
	public boolean getTag() {
		return tag;
	}

	/**
	 * Check attributeValueList property
	 * @return attributeValueList property
	 */
	public boolean isAttributeValueList() {
		return attributeValueList;
	}

	/**
	 * Getter for required property
	 * @return
	 */
	public String getRequired() {
		return required;
	}

	/**
	 * Setter for required property
	 * @param required
	 */
	public void setRequired(String required) {
		this.required = required;
	}

	/**
	 * Getter for action property
	 * @return
	 */
	public int getAction() {
		return action;
	}

	/**
	 * Setter for action property
	 * @param action
	 */
	public void setAction(int action) {
		this.action = action;
	}

	/**
	 * Getter for operation field
	 * @return operation
	 */
	public String getOperation() {
		return operation;
	}

	/**
	 * Setter for operation field
	 * @param operation to set
	 */
	public void setOperation(String operation) {
		this.operation = operation;
	}

	/**
	 * Get attribute values
	 * @return attribute values
	 */
	public List<EntityAttributeCode> getAttributeValues() {
		return attributeValues;
	}

	/**
	 * Set attribute values
	 * @param attributeValues to be set
	 */
	public void setAttributeValues(List<EntityAttributeCode> attributeValues) {
		this.attributeValues = attributeValues;
	}

	/**
	 * Setter for attribute domain description
	 * @return domain description attribute
	 */
	public void setAttributeDomainDescription(String attributeDomainDescription) {
		this.attributeDomainDescription = attributeDomainDescription;
	}

	/**
	 * Getter for attribute domain description
	 * @return domain description attribute
	 */
	public String getAttributeDomainDescription() {
		return attributeDomainDescription;
	}

	/**
	 * Getter for precision attribute
	 * @return precision
	 */
	public Long getPrecision() {
		return precision;
	}

	/**
	 * Setter for precision attribute
	 * @param precision
	 */
	public void setPrecision(Long precision) {
		this.precision = precision;
	}

	/**
	 * Getter for maximum length attribute
	 * @return
	 */
	public Long getMaximumLength() {
		return maximumLength;
	}

	/**
	 * Setter for maximum length attribute
	 * @param maximumLength
	 */
	public void setMaximumLength(Long maximumLength) {
		this.maximumLength = maximumLength;
	}

	/**
     * Get the attribute id
     * @return the attribute id
     */
    public Long getAttributeId() {
        return attributeId;
    }

    /**
     * Updates the attribute id
     * @param attributeId the new attribute id
     */
    public void setAttributeId(Long attributeId) {
        this.attributeId = attributeId;
    }

    /**
	 * @return the attributeDescription
	 */
	public String getAttributeDescription() {
		return attributeDescription;
	}

	/**
	 * @param attributeDescription the attributeDescription to set
	 */
	public void setAttributeDescription(String attributeDescription) {
		this.attributeDescription = attributeDescription;
	}

	/**
	 * @return the attributeDomainCode
	 */
	public String getAttributeDomainCode() {
		return attributeDomainCode;
	}

	/**
	 * @param attributeDomainCode the attributeDomainCode to set
	 */
	public void setAttributeDomainCode(String attributeDomainCode) {
		this.attributeDomainCode = attributeDomainCode;
	}

	/**
	 * @return the attributeValueList
	 */
	public boolean getAttributeValueList() {
		return attributeValueList;
	}

	/**
	 * @param attributeValueList the attributeValueList to set
	 */
	public void setAttributeValueList(boolean attributeValueList) {
		this.attributeValueList = attributeValueList;
	}

	/**
	 * @return the attributeInstantDescription
	 */
	public String getAttributeInstantDescription() {
		return attributeInstantDescription;
	}

	/**
	 * @param attributeInstantDescription the attributeInstantDescription to set
	 */
	public void setAttributeInstantDescription(String attributeInstantDescription) {
		this.attributeInstantDescription = attributeInstantDescription;
	}

	/**
     * Returns the attribute name.
	 *
     * @return the attribute name
     */
    public String getAttributeName() {
        return attributeName;
    }

    /**
     * Updates the attribute name.
	 *
     * @param attributeName the new attribute name
     */
    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    /**
     * gets the logical physical relationships for an attribute.
	 *
     * @return the logical physical relationships for an attribute
     */
    public List<LogicalPhysicalRelationship> getLogicalPhysicalRelationship() {
        return logicalPhysicalRelationships;
    }

    /**
     * Changes the logical physical relationships for an attribute.
	 *
     * @param logicalPhysicalRelationships the new logical physical relationships for an attribute
     */
    public void setLogicalPhysicalRelationshipLists(List<LogicalPhysicalRelationship> logicalPhysicalRelationships) {
        this.logicalPhysicalRelationships = logicalPhysicalRelationships;
    }

    /**
     * Returns the source system id for the attribute.
	 *
     * @return the source system id for the attribute
     */
    public Long getSourceSystemId() {
        return sourceSystemId;
    }

    /**
     * Replaces  the source system id for the attribute
     * @param sourceSystemId the new source system id for the attribute
     */
    public void setSourceSystemId(Long sourceSystemId) {
        this.sourceSystemId = sourceSystemId;
    }

	/**
	 * Returns the source system for the attribute.
	 *
	 * @return the source system for the attribute
	 */
	public SourceSystem getSourceSystem() {
		return sourceSystem;
	}

	/**
	 * Replaces  the source system for the attribute
	 * @param sourceSystem the new source system for the attribute
	 */
	public void setSourceSystem(SourceSystem sourceSystem) {
		this.sourceSystem = sourceSystem;
	}

	/**
	 * This returns whether or not the attribute is a dynamic attribute
	 * @return
	 */
	public Boolean getDynamicAttributeSwitch() {
		return dynamicAttributeSwitch;
	}

	/**
	 * Updates the dynamicAttributeSwitch
	 * @param dynamicAttributeSwitch
	 */
	public void setDynamicAttributeSwitch(Boolean dynamicAttributeSwitch) {
		this.dynamicAttributeSwitch = dynamicAttributeSwitch;
	}

	/**
	 * This states if the attribute can hold multiple values
	 * @return
	 */
	public Boolean getMultipleValueSwitch() {
		return multipleValueSwitch;
	}

	/**
	 * Updates the multipleValueSwitch
	 * @param multipleValueSwitch
	 */
	public void setMultipleValueSwitch(Boolean multipleValueSwitch) {
		this.multipleValueSwitch = multipleValueSwitch;
	}

	/**
	 * Returns the one-pass ID of the user who created this attribute.
	 *
	 * @return The one-pass ID of the user who created this attribute.
	 */
	public String getCreateUserId() {
		return createUserId;
	}

	/**
	 * Sets the one-pass ID of the user who created this attribute.
	 *
	 * @param createUserId The one-pass ID of the user who created this attribute.
	 */
	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}

	/**
	 * Retruns the EcommerUserGroupAttributes tied to this entity.
	 *
	 * @return The EcommerUserGroupAttributes tied to this entity.
	 */
	public List<EcommerUserGroupAttribute> getEcommerUserGroupAttributes() {
		if (ecommerUserGroupAttributes == null) {
			this.ecommerUserGroupAttributes = new LinkedList<>();
		}
		return ecommerUserGroupAttributes;
	}

	/**
	 * Sets the EcommerUserGroupAttributes tied to this entity.
	 *
	 * @param ecommerUserGroupAttributes The EcommerUserGroupAttributes tied to this entity.
	 */
	public void setEcommerUserGroupAttributes(List<EcommerUserGroupAttribute> ecommerUserGroupAttributes) {
		this.ecommerUserGroupAttributes = ecommerUserGroupAttributes;
	}

	/**
	 * Returns a String representation of the object.
	 *
	 * @return A String representation of the object.
	 */
	@Override
	public String toString() {
		return "Attribute{" +
				"attributeId=" + attributeId +
				", attributeName='" + attributeName + '\'' +
				", logicalPhysicalRelationships=" + logicalPhysicalRelationships +
				", sourceSystem=" + sourceSystem +
				", attributeDomainCode='" + attributeDomainCode + '\'' +
				", attributeValueList=" + attributeValueList +
				", dynamicAttributeSwitch='" + dynamicAttributeSwitch + '\'' +
				", multipleValueSwitch='" + multipleValueSwitch + '\'' +
				'}';
	}

	/**
	 * Is the Specification boolean set
	 * @return boolean if specification is set
	 */
	private boolean isSpecificationSW() {
		boolean result = false;

		for (EcommerUserGroupAttribute ecommerUserGroupAttribute : ecommerUserGroupAttributes) {
			if (ecommerUserGroupAttribute.getKey().getUsrInrfcGrpCd().trim().equalsIgnoreCase("spec")) {
				result = true;
				break;
			}
		}

		return result;
	}

	/**
	 * Is the tag boolean set
	 * @return boolean if tag is set
	 */
	private boolean isTagSW() {
		boolean result = false;

		for (EcommerUserGroupAttribute ecommerUserGroupAttribute : ecommerUserGroupAttributes) {
			if (ecommerUserGroupAttribute.getKey().getUsrInrfcGrpCd().trim().equalsIgnoreCase("tag")) {
				result = true;
				break;
			}
		}

		return result;
	}

	/**
	 * Check tag transient field set and return it
	 * @return tag attribute
	 */
	public boolean isTag() {
		tag =  isTagSW();
		return tag;
	}

	/**
	 * Check specification transient field set and return it
	 * @return tag attribute
	 */
	public boolean isSpecification() {
		specification = isSpecificationSW();
		return specification;
	}

	/**
	 * If this attribute comes from another system, the ID of the attribute in that system.
	 *
	 * @return The ID of this attribute in another system.
	 */
	public String getExternalId() {
		return externalId;
	}

	/**
	 * Sets the ID of this attribute in another system.
	 *
	 * @param externalId The ID of this attribute in another system.
	 */
	public void setExternalId(String externalId) {
		this.externalId = externalId;
	}

	public String getLogicalOrPhysical() {
		return logicalOrPhysical;
	}

	public void setLogicalOrPhysical(String logicalOrPhysical) {
		this.logicalOrPhysical = logicalOrPhysical;
	}

	public String getLastUpdateUserId() {
		return lastUpdateUserId;
	}

	public void setLastUpdateUserId(String lastUpdateUserId) {
		this.lastUpdateUserId = lastUpdateUserId;
	}

	public String getOptionalOrRequired() {
		return optionalOrRequired;
	}

	public void setOptionalOrRequired(String optionalOrRequired) {
		this.optionalOrRequired = optionalOrRequired;
	}

	/**
	 * Returns ApplicableTypeCode.
	 *
	 * @return The ApplicableTypeCode.
	 **/
	public String getApplicableTypeCode() {
		return applicableTypeCode;
	}

	/**
	 * Sets the ApplicableTypeCode.
	 *
	 * @param applicableTypeCode The ApplicableTypeCode.
	 **/
	public Attribute setApplicableTypeCode(String applicableTypeCode) {
		this.applicableTypeCode = applicableTypeCode;
		return this;
	}

	/**
	 * Returns ApplicableType.
	 *
	 * @return The ApplicableType.
	 **/
	public ApplicableType getApplicableType() {
		return applicableType;
	}

	/**
	 * Sets the ApplicableType.
	 *
	 * @param applicableType The ApplicableType.
	 **/
	public Attribute setApplicableType(ApplicableType applicableType) {
		this.applicableType = applicableType;
		return this;
	}

	/**
	 * Compares another object to this one. The key is the only thing used to determine equality.
	 *
	 * @param o The object to compare to.
	 * @return True if they are equal and false otherwise.
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Attribute attribute = (Attribute) o;

		return attributeId != null ? attributeId.equals(attribute.attributeId) : attribute.attributeId == null;
	}

	/**
	 * Returns a hash for this object. If two objects are equal, they will have the same hash. If they are not,
	 * they will (probably) have different hashes.
	 *
	 * @return The hash code for this object.
	 */
	@Override
	public int hashCode() {
		return attributeId != null ? attributeId.hashCode() : 0;
	}
}
