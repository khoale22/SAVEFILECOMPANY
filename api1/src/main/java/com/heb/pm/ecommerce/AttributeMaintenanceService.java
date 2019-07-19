/*
 * AttributeMaintenanceService.java
 *
 * Copyright (c) 2018 HEB
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of HEB.
 */package com.heb.pm.ecommerce;

import com.heb.pm.entity.*;
import com.heb.pm.repository.*;
import com.heb.pm.ws.CheckedSoapException;
import com.heb.pm.ws.CodeTableManagementServiceClient;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * Holds business logic related to Attribute Maintenance.
 *
 * @author a786878
 * @since 2.15.0
 */
@Service
public class AttributeMaintenanceService {

    public static final String ATTRIBUTE_ADD = "A";
    public static final String ATTRIBUTE_UPDATE = "U";

    private long PRODUCT_ENTITY_ID=16L;

    @Autowired
    private EntityAttributeCodeRepository entityAttributeCodeRepository;

    @Autowired
    private EntityAttributeRepository entityAttributeRepository;

    @Autowired
    private AttributeRepository attributeRepository;

    @Autowired
    private AttributeDomainRepository attributeDomainRepository;

    @Autowired
    private CodeTableManagementServiceClient serviceClient;

    @Autowired
    private GenericEntityDescriptionRespository genericEntityDescriptionRespository;

    @Autowired
    private GenericEntityRepository genericEntityRepository;

    /**
     * Return a list of paged attributes by searching by attribute name, source system id, or attribute id
     *
     * @param pageable
     * @param attributeName
     * @param sourceSystemId
     * @param attributeId
     * @return
     */
    public Page<Attribute> getAttributeMaintenanceTable(Pageable pageable, String attributeName, Long sourceSystemId, Long attributeId){
        Page<Attribute> dynamicAttributes;

        if (attributeId != null && StringUtils.isNotEmpty(attributeName) && sourceSystemId != null) {
            dynamicAttributes = this.attributeRepository.findDynamicAttributes(pageable, sourceSystemId, attributeId, attributeName);
        } else if (attributeId != null && StringUtils.isNotEmpty(attributeName)) {
            dynamicAttributes = this.attributeRepository.findDynamicAttributes(pageable, attributeId, attributeName);
        } else if (attributeId != null && sourceSystemId != null) {
            dynamicAttributes = this.attributeRepository.findDynamicAttributes(pageable, attributeId, sourceSystemId);
        } else if (StringUtils.isNotEmpty(attributeName) && sourceSystemId != null) {
            dynamicAttributes = this.attributeRepository.findDynamicAttributes(pageable, "%" + attributeName + "%", sourceSystemId);
        } else if (StringUtils.isNotEmpty(attributeName)) {
            dynamicAttributes = this.attributeRepository.findDynamicAttributes(pageable, "%" + attributeName + "%");
        } else if (attributeId != null) {
            dynamicAttributes = this.attributeRepository.findDynamicAttributesByAttributeId(pageable, attributeId);
        } else if (sourceSystemId != null) {
            dynamicAttributes = this.attributeRepository.findDynamicAttributes(pageable, sourceSystemId);
        } else {
            dynamicAttributes = this.attributeRepository.findDynamicAttributes(pageable);
        }

        dynamicAttributes.forEach(attr -> {
            getEntityDescriptions(attr);
            attr.isSpecification();
            attr.isTag();
        });

        return dynamicAttributes;
    }

    /**
     * Set the entity abbreviation for an attribute
     * @param attr
     */
    private void getEntityDescriptions(Attribute attr) {
        StringBuffer entityDescription = new StringBuffer();
        List<EntityAttribute> entityAttributes = entityAttributeRepository.getEntityAttributesForAttribute(attr.getAttributeId());
        for (EntityAttribute entityAttribute : entityAttributes) {
            GenericEntity entity = genericEntityRepository.findOne(entityAttribute.getKey().getEntityId());
            if (entity == null) {
                continue;
            }
            List<GenericEntityDescription> descriptions = entity.getDescriptions();
            flattenDescriptions(entityDescription, descriptions);
        }

        trimEntityDescription(attr, entityDescription);
    }

    /**
     * Trims the entity description after last append
     * @param attr
     * @param entityDescription
     */
    private void trimEntityDescription(Attribute attr, StringBuffer entityDescription) {
        int idx = entityDescription.lastIndexOf(", ");
        if (idx > 0 && idx == entityDescription.length() - 2) {
            attr.setEntityAbbreviation(entityDescription.substring(0,idx));
        } else {
            attr.setEntityAbbreviation(entityDescription.toString());
        }
    }

    /**
     * Returns a single dynamic attribute
     * @return attribute
     */
    public Attribute getAttributeDetails(Long id){
        Attribute attribute = this.attributeRepository.findOne(id);

        // need to make sure these are loaded from separate table
        attribute.isSpecification();
        attribute.isTag();

        AttributeDomain attributeDomain = this.attributeDomainRepository.findAttributeDomainByAttributeDomainCode(attribute.getAttributeDomainCode());
        attribute.setAttributeDomainDescription(attributeDomain.getAttributeDomainDescription());
        return attribute;
    }

    /**
     * This method will take an attribute id and generate a list of all
     * possible attribute codes tied to that for products
     *
     * @param attributeId the attribute identification number
     * @return list of attribute codes
     */
    public List<EntityAttributeCode> getAttributeValues(Long attributeId){
        List<EntityAttributeCode> entityAttributeCodes = this.entityAttributeCodeRepository.findByKeyAttributeIdOrderByAttributeCodeAttributeValueText(attributeId);

        for (EntityAttributeCode entityAttributeCode : entityAttributeCodes) {
            createEntityDescription(entityAttributeCode);
        }

        return entityAttributeCodes;
    }

    /**
     * Create entity description for attribute code
     * @param entityAttributeCode
     */
    private void createEntityDescription(EntityAttributeCode entityAttributeCode) {
        StringBuffer entityDescription = new StringBuffer();

        long entityId = entityAttributeCode.getKey().getEntityId();
        GenericEntity entity = genericEntityRepository.findOne(entityId);
        if (entity == null) {
            return;
        }
        List<GenericEntityDescription> descriptions = entity.getDescriptions();
        flattenDescriptions(entityDescription, descriptions);
        int idx = entityDescription.lastIndexOf(", ");
        if (idx > 0 && idx == entityDescription.length() - 2) {
            entityAttributeCode.setEntityDescription(entityDescription.substring(0,idx));
        } else {
            entityAttributeCode.setEntityDescription(entityDescription.toString());
        }
    }

    /**
     * Combines multiple descriptions into single comma separated description
     * @param entityDescription
     * @param descriptions
     */
    private void flattenDescriptions(StringBuffer entityDescription, List<GenericEntityDescription> descriptions) {
        for (GenericEntityDescription description : descriptions) {
            entityDescription.append(description.getShortDescription());
            entityDescription.append("[");
            entityDescription.append(description.getKey().getHierarchyContext());
            entityDescription.append("]");
            entityDescription.append(", ");
        }
    }

    /**
     * Get list of attribute domains
     * @return attribute domains
     */
    public List<AttributeDomain> getAttributeDomains(){
        List<AttributeDomain> attributeDomains = this.attributeDomainRepository.findAttributeDomains();
        return attributeDomains;
    }

    /**
     * Update a single attribute
     * @param attribute to update
     */
    public void updateAttributeDetails(Attribute attribute) throws CheckedSoapException {
        if (attribute.getOperation().equals(ATTRIBUTE_ADD)) {
            attribute.setAction(Attribute.INSERT);
            attribute.setDynamicAttributeSwitch(true);
            serviceClient.writeAttribute(Arrays.asList(attribute));
        } else if (attribute.getOperation().equals(ATTRIBUTE_UPDATE)) {
            attribute.setAction(Attribute.UPDATE);
            attribute.setDynamicAttributeSwitch(true);

            serviceClient.writeAttribute(Arrays.asList(attribute));
        }

        serviceClient.writeTagAndSpecEcommerUserGroupAttributes(attribute);

        List<EntityAttribute> attrs = entityAttributeRepository.getEntityAttributesForEntityAndAttribute(PRODUCT_ENTITY_ID, attribute.getAttributeId());
        int numEntityAttributes = attrs.size();

        int numEntityAttributeCodes = entityAttributeCodeRepository.findCountForEntityandAttribute(PRODUCT_ENTITY_ID ,attribute.getAttributeId());

        serviceClient.updateAttributeValues(numEntityAttributes, numEntityAttributeCodes, attribute);
    }
}
