/*
 * SubCommodityAuditImpl
 *
 *  Copyright (c) 2018 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
package com.heb.pm.productHierarchy;

import com.heb.pm.entity.*;
import com.heb.util.audit.*;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Handles the logic to do an audit comparison for Sub Commodity.
 *
 * @author vn70529
 * @since 2.18.4
 */
public class SubCommodityAuditImpl extends AuditComparisonImpl {

    private static final Logger logger = LoggerFactory.getLogger(AuditComparisonImpl.class);

    private static final String EQUAL = "equal.";
    private static final String NOT_EQUAL = "not equal.";

    // These are the constants for auditable values.
    private static final String NOT_APPLICABLE_VALUE = "N/A";
    private static final String UNKNOWN_VALUE = "Unknown";
    private static final String NULL_VALUE = "null";
    private static final String SEQUENCE_NO_VALUE = "Sequence No";
    private static final String SHIPPING_RESTRICTION_SWITCH_VALUE = "Shipping Restrictions Switch";

    /** STRING_EMPTY.*/
    public static final String EMPTY = "";
    // log messages
    private static final String LOG_COMPARING_VALUES = "Comparing {%s} and {%s}.";
    private static final String LOG_IS_EQUALS = "Values were found to be: %s";
    private static final String LOG_AUDIT_CREATED = "Created audit record: %s.";

    // Errors
    private static final String INCOMPATIBLE_COMPARISON_ERROR = "ERROR -- Current value: %s and previous value: %s " +
            "are of different class types and should not be compared.";


    /**
     * Takes in a list of audits ordered in changed by ascending order (most recent audit is last in list), and returns
     * the created audits from comparing current audit and previous audit in descending order (most recent audit is
     * first in list).
     *
     * @param audits The list of audits ordered in changed by ascending order.
     * @return List of all audit records created from comparing current and previous values of each audit in
     * descending order.
     */
    @Override
    public List<AuditRecord> processClassFromList(List<? extends Audit> audits, String...filters){


        // map of user id -> user display names
        Map<String, String> mapUserNames = this.mapUserIdToUserDisplayName(audits);

        List<AuditRecord> toReturn = new ArrayList<>();

        // get the audits by comparing current and previous audits, then add to beginning of return list
        for(int index = 0; index < audits.size(); index++){
            toReturn.addAll(0, this.processClass(
                    audits.get(index),
                    index == 0 ? null : audits.get(index - 1),
                    mapUserNames.get(audits.get(index).getChangedBy()),
                    filters));
        }

        // make sure audits are in the order they were changed on return
        toReturn.sort(Comparator.comparing(AuditRecord::getChangedOn));

        return toReturn;
    }

    /**
     * Compares two audit records, looking at all fields annotated with 'AuditableField', and creates a new audit for
     * each of these fields that changed. After looking at all the auditable fields, return the list of new audits.
     *
     * @param currentValue The audit currently being looked at.
     * @param previousValue The audit containing the previous values.
     * @param fullUserName The full user name of the person who made the change.
     * @return List of all audit records created from comparing current and previous values.
     */
    public List<AuditRecord> processClass(Audit currentValue, Audit previousValue, String fullUserName, String...filters) {

        if(previousValue == null || currentValue.getClass().isInstance(previousValue)){

            List<AuditRecord> toReturnAudits = new ArrayList<>();
            AuditRecord newAudit;

            if (currentValue instanceof SubCommodityAudit && previousValue == null) {
                return  toReturnAudits;
            }
            // Loop through all the fields in the destination object.
            for (Field field : currentValue.getClass().getDeclaredFields()) {

                // If it is annotated with AuditableField, process the field.
                if (field.isAnnotationPresent(AuditableField.class)) {
                    try {
                        newAudit = this.processField(field, currentValue, previousValue, fullUserName, filters);
                        if(newAudit != null){
                            toReturnAudits.add(newAudit);
                        }
                    }

                    // Convert all the exceptions to a runtime exception.
                    catch (InstantiationException | IllegalAccessException | NoSuchMethodException |
                            InvocationTargetException | NoSuchFieldException e) {
                        throw new AuditComparisonException(e.getCause());
                    }
                }
            }
            return toReturnAudits;

        } else {
            throw new IllegalArgumentException(String.format(
                    INCOMPATIBLE_COMPARISON_ERROR, currentValue, previousValue));
        }
    }

    /**
     * Handles creating an audit from an individual field.
     *
     * @param field The field to compare.
     * @param currentAudit The current audit.
     * @param previousAudit The previous audit.
     * @param userDisplayName The user friendly format of the user name and user id.
     *
     * @throws NoSuchMethodException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws InstantiationException
     *
     * @return New Audit if there is a change, null otherwise.
     */
    private AuditRecord processField(Field field, Audit currentAudit, Audit previousAudit, String userDisplayName, String...filters)
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException,
            InstantiationException, NoSuchFieldException {

        boolean isEquals;
        boolean isAccessible = field.isAccessible();
        AuditRecord returnAudit = null;

        //Check to see if the field is needed for this audit.
        boolean contains=false;
        List<String> currentFilters = Arrays.asList(field.getAnnotation(AuditableField.class).filter());
        if(filters.length>0){
            for (String filter: filters) {
                contains=currentFilters.contains(filter);
            }
            if(!contains){
                return null;
            }
        }

        try {

            field.setAccessible(true);

            // compare previous audit records
            logger.debug(String.format(
                    LOG_COMPARING_VALUES,
                    field.get(currentAudit), (previousAudit != null ? field.get(previousAudit) : null)));

            // if previous audit is null, audits are not equal
            if(previousAudit == null || currentAudit.getAction().trim().equals(AuditRecord.ActionCodes.PURGE.name())) {
                isEquals = false;
                if(currentAudit instanceof SubCommodityAudit && previousAudit != null){
                    isEquals = StringUtils.trim(field.get(previousAudit).toString()).equals(EMPTY);
                }
            }else if (currentAudit instanceof SubCommodityAudit && previousAudit.getAction().trim().equals(AuditRecord.ActionCodes.PURGE.name())){
                isEquals = StringUtils.trim(field.get(currentAudit).toString()).equals(EMPTY);
            }else if((currentAudit instanceof SubCommodityStateWarningAudit || currentAudit instanceof ShippingRestrictionHierarchyLevelAudit
                    || currentAudit instanceof SellingRestrictionHierarchyLevelAudit || currentAudit instanceof ProductPreferredUnitOfMeasureAudit)
                    && (currentAudit.getAction().trim().equals(AuditRecord.ActionCodes.ADD.name()) ||
                    currentAudit.getAction().trim().equals(AuditRecord.ActionCodes.DEL.name()))){
                isEquals = false;
            }else if(field.get(currentAudit) == null || field.get(previousAudit)  == null){
                isEquals = field.get(currentAudit) == null && field.get(previousAudit) == null;
            }

            // else compare previous and current audit record values by type
            // if the type is primitive, use primitive comparison (==) on the values
            else if (field.getType().equals(Integer.TYPE)) {
                isEquals = field.getInt(currentAudit) == field.getInt(previousAudit);
            } else if (field.getType().equals(Long.TYPE)) {
                isEquals = field.getLong(currentAudit) == field.getLong(previousAudit);
            } else if (field.getType().equals(Double.TYPE)) {
                isEquals = field.getDouble(currentAudit) == field.getDouble(previousAudit);
            } else if (field.getType().equals(Float.TYPE)) {
                isEquals = field.getFloat(currentAudit) == field.getFloat(previousAudit);
            } else if (field.getType().equals(Character.TYPE)) {
                isEquals = field.getChar(currentAudit) == field.getChar(previousAudit);
            } else if (field.getType().equals(Boolean.TYPE)) {
                isEquals = field.getBoolean(currentAudit) == field.getBoolean(previousAudit);
            } else if (field.getType().equals(Short.TYPE)) {
                isEquals = field.getShort(currentAudit) == field.getShort(previousAudit);
            } else if (field.getType().equals(Byte.TYPE)) {
                isEquals = field.getByte(currentAudit) == field.getByte(previousAudit);
            }

            // else compare using object equals (.equals) on the values
            else {
                isEquals = field.get(currentAudit).equals(field.get(previousAudit));
            }

            // log whether values were equal or not
            logger.debug(String.format(LOG_IS_EQUALS,
                    (isEquals ? EQUAL : NOT_EQUAL)));

            // if current field != previous field, create the audit record
            if(!isEquals){
                // need to extract name dynamically if audit is Dynamic Attribute
                String attrName;
                String methodName = field.getAnnotation(AuditableField.class).displayNameMethod();
                // this means that displayNameMethod is NOT in the AuditableField
                if (methodName.equals(AuditableField.NOT_APPLICABLE)) {
                    attrName = field.getAnnotation(AuditableField.class).displayName();
                } else {
                    Method method = currentAudit.getClass().getDeclaredMethod(methodName);
                    attrName = method.invoke(currentAudit).toString();
                    // format DB's result of attribute name from all caps to 1st letter of each word in upper case
                    attrName = WordUtils.capitalizeFully(attrName);
                }

                // call constructor with changed on, changed by, action, and attribute display name
                returnAudit = new AuditRecord(
                        currentAudit.getChangedOn(),
                        userDisplayName,
                        currentAudit.getAction(),
                        attrName
                );

                // if purge action
                if(currentAudit.getAction().trim().equals(AuditRecord.ActionCodes.PURGE.name()) || currentAudit.getAction().trim().equals(AuditRecord.ActionCodes.DEL.name())){

                    // changed to is not applicable
                    returnAudit.setChangedTo(NOT_APPLICABLE_VALUE);
                    if (currentAudit instanceof SubCommodityAudit) {
                        returnAudit.setChangedFrom(this.getAuditPropertyValue(field, previousAudit));
                    } else {
                        returnAudit.setChangedFrom(this.getAuditPropertyValue(field, currentAudit));
                    }

                }

                // else add or update action
                else {
                    returnAudit.setChangedTo(getAuditPropertyValue(field, currentAudit));
                    // if add action: changed from is not applicable
                    if(currentAudit.getAction().trim().equals(AuditRecord.ActionCodes.ADD.name())){
                        returnAudit.setChangedFrom(NOT_APPLICABLE_VALUE);
                    } else if (currentAudit instanceof SubCommodityAudit && (previousAudit.getAction().trim().equals(AuditRecord.ActionCodes.PURGE.name())|| previousAudit.getAction().trim().equals(AuditRecord.ActionCodes.ADD.name()))){
                        returnAudit.setChangedFrom("");
                    } else if (currentAudit instanceof ProductPreferredUnitOfMeasureAudit){
                        if(((ProductPreferredUnitOfMeasureAudit) currentAudit).getRetailUnitOfMeasureCode().equals(((ProductPreferredUnitOfMeasureAudit) previousAudit).getRetailUnitOfMeasureCode())){
                            returnAudit.setChangedFrom(this.getAuditPropertyValue(field, previousAudit));
                        }else{
                            returnAudit.setChangedFrom(NOT_APPLICABLE_VALUE);
                        }
                    } else if (currentAudit instanceof ShippingRestrictionHierarchyLevelAudit){
                        if(previousAudit != null && ((ShippingRestrictionHierarchyLevelAudit) currentAudit).getKey().getRestrictionCode().equals(((ShippingRestrictionHierarchyLevelAudit) previousAudit).getKey().getRestrictionCode())){
                            returnAudit.setChangedFrom(this.getAuditPropertyValue(field, previousAudit));
                        }else{
                            returnAudit.setChangedFrom(NOT_APPLICABLE_VALUE);
                        }
                    }

                    // else update action: changed from is previous field value
                    else {
                        returnAudit.setChangedFrom(this.getAuditPropertyValue(field, previousAudit));
                    }
                }
                if(currentAudit instanceof ShippingRestrictionHierarchyLevelAudit){
                    returnAudit.setAttributeValue(((ShippingRestrictionHierarchyLevelAudit) currentAudit).getSellingRestrictionCode().getRestrictionDescription());
                    returnAudit.setExtendableAttValue(SHIPPING_RESTRICTION_SWITCH_VALUE);
                }else if(currentAudit instanceof ProductPreferredUnitOfMeasureAudit){
                    returnAudit.setAttributeValue(((ProductPreferredUnitOfMeasureAudit) currentAudit).getRetailUnitOfMeasure().getDisplayName());
                    returnAudit.setExtendableAttValue(SEQUENCE_NO_VALUE);
                }
                logger.debug(String.format(LOG_AUDIT_CREATED, returnAudit));
            }
        } catch (IllegalAccessException e) {
            throw new IllegalAccessException(e.getLocalizedMessage());
        } finally {
            field.setAccessible(isAccessible);
        }

        return returnAudit;
    }

    /**
     * Returns the value for an audit object for a particular field. If the audit is null, the value is unknown.
     * If the field has a given code table display name method that is not the default (NOT_APPLICABLE), this method
     * will call that method. Else return the field value as is or 'null' if value is empty
     *
     * @param auditableField The auditable field in the audit object you want the value for.
     * @param audit The audit you want the getter from.
     * @return The value 'UNKNOWN_VALUE' if the audit is null, code table display name method invocation if the method
     * exists, or string value for a property on an audit.
     *
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    private String getAuditPropertyValue(Field auditableField, Audit audit) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {

        // if audit is null, property value is unknown
        if(audit == null){
            return UNKNOWN_VALUE;
        }

        String currentFieldStringValue;
        Object currentFieldValue;

        String codeTableDisplayNameMethod =
                auditableField.getAnnotation(AuditableField.class).codeTableDisplayNameMethod();

        // if field does not have a code table display name method, current property value is the value.toString()
        if(codeTableDisplayNameMethod.equals(AuditableField.NOT_APPLICABLE)){
            currentFieldValue = auditableField.get(audit);
            if(currentFieldValue != null){
                currentFieldStringValue = currentFieldValue.toString();
            } else {
                currentFieldStringValue = StringUtils.EMPTY;
            }
        }

        // else current property value is the code table display name method call
        else {
            Method method = audit.getClass().getMethod(codeTableDisplayNameMethod);
            currentFieldValue = method.invoke(audit);
            if(currentFieldValue != null){
                currentFieldStringValue = currentFieldValue.toString();
            } else {
                currentFieldStringValue = StringUtils.EMPTY;
            }
        }

        // if current property value is empty, return 'null'
        if(currentFieldStringValue.isEmpty()){
            return NULL_VALUE;
        }

        // else return the property value
        else {
            return currentFieldStringValue;
        }
    }

}
