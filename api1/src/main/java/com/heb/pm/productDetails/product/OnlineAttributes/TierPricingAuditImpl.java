package com.heb.pm.productDetails.product.OnlineAttributes;

import com.heb.pm.entity.*;
import com.heb.util.audit.AuditComparisonImpl;
import com.heb.util.audit.AuditRecord;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

/**
 * Handles the logic to do an tier pricing audit comparison.
 *
 * @author vn70633
 * @since 2.19.0
 */
@Service
@Qualifier("tierPricingAuditImpl")
public class TierPricingAuditImpl extends AuditComparisonImpl {
    /**
     * QUANTITY DISCOUNT
     */
    public static final String QUANTITY_DISCOUNT = "Min Order Quantity for Discount";
    /**
     * QUANTITY DISCOUNT TYPE
     */
    public static final String QUANTITY_DISCOUNT_TYPE = "Min Order Quantity Discount Type";
    /**
     * QUANTITY DISCOUNT VALUE
     */
    public static final String QUANTITY_DISCOUNT_VALUE = "Min Order Quantity Discount Value";
    /**
     * EFFECTIVE DATE
     */
    public static final String EFFECTIVE_DATE = "Effective Date";
    /**
     * ACTION_DELETE.
     */
    public static final String ACTION_DELETE = "Delete";
    /**
     * ACTION_ADD.
     */
    public static final String ACTION_ADD = "Add";
    /**
     * ACTION_UPDATE.
     */
    public static final String ACTION_UPDATE = "Update";
    /**
     * AUDIT_ACTION_PURGE.
     */
    public static final String AUDIT_ACTION_PURGE = "PURGE";
    /**
     * AUDIT_ACTION_DEL.
     */
    public static final String AUDIT_ACTION_DEL = "DEL";
    /**
     * AUDIT_ACTION_ADD.
     */
    public static final String AUDIT_ACTION_ADD = "ADD";
    /**
     * AUDIT_ACTION_UPDATE.
     */
    public static final String AUDIT_ACTION_UPDATE = "UPDT";

    /** STRING_EMPTY.*/
    public static final String EMPTY = "";

    /**
     * process class from list of TierPricingAudits
     * @param  mapTierPricingAudits
     * @return list of audit record
     */
    public List<AuditRecord> processClassFromListTierPricingAudits(Map<LocalDateTime, List<TierPricingAudit>> mapTierPricingAudits){
        TierPricingAudit previousTierPricingAudit = new TierPricingAudit();
        List<AuditRecord> result = new ArrayList<>();
        for (Map.Entry<LocalDateTime, List<TierPricingAudit>> entry : mapTierPricingAudits.entrySet()) {
            List<TierPricingAudit> tierPricingAuditTemps =  entry.getValue();
            for(TierPricingAudit tierPricingAudit : tierPricingAuditTemps){
                AuditRecord auditRecord1 = null;
                AuditRecord auditRecord2 = null;
                AuditRecord auditRecord3 = null;
                AuditRecord auditRecord4 = null;
                if(tierPricingAudit.getAction().trim().equals(AuditRecord.ActionCodes.ADD.toString())){
                    auditRecord1 = this.createAuditRecord(QUANTITY_DISCOUNT, tierPricingAudit.getCreateDate(), EMPTY, tierPricingAudit.getKey().getDiscountQuantity().toString(),
                            ACTION_ADD, tierPricingAudit.getChangedOn(), tierPricingAudit.getChangedBy());
                    auditRecord2 = this.createAuditRecord(QUANTITY_DISCOUNT_TYPE, tierPricingAudit.getCreateDate(), EMPTY, tierPricingAudit.getDiscountTypeCodeDisplayName(),
                            ACTION_ADD, tierPricingAudit.getChangedOn(), tierPricingAudit.getChangedBy());
                    auditRecord3 = this.createAuditRecord(QUANTITY_DISCOUNT_VALUE, tierPricingAudit.getCreateDate(), EMPTY, tierPricingAudit.getDiscountValue().toString(),
                            ACTION_ADD, tierPricingAudit.getChangedOn(), tierPricingAudit.getChangedBy());
                    auditRecord4 = this.createAuditRecord(EFFECTIVE_DATE, tierPricingAudit.getCreateDate(), EMPTY, tierPricingAudit.getKey().getEffectiveTimeStamp().toString(),
                            ACTION_ADD, tierPricingAudit.getChangedOn(), tierPricingAudit.getChangedBy());
                }else if(tierPricingAudit.getAction().trim().equals(AuditRecord.ActionCodes.DEL.toString())){
                    auditRecord1 = this.createAuditRecord(QUANTITY_DISCOUNT, tierPricingAudit.getCreateDate(), tierPricingAudit.getKey().getDiscountQuantity().toString() , EMPTY,
                            ACTION_DELETE, tierPricingAudit.getChangedOn(), tierPricingAudit.getChangedBy());
                    auditRecord2 = this.createAuditRecord(QUANTITY_DISCOUNT_TYPE, tierPricingAudit.getCreateDate(), tierPricingAudit.getDiscountTypeCodeDisplayName() , EMPTY,
                            ACTION_DELETE, tierPricingAudit.getChangedOn(), tierPricingAudit.getChangedBy());
                    auditRecord3 = this.createAuditRecord(QUANTITY_DISCOUNT_VALUE, tierPricingAudit.getCreateDate(),tierPricingAudit.getDiscountValue().toString() , EMPTY,
                            ACTION_DELETE, tierPricingAudit.getChangedOn(), tierPricingAudit.getChangedBy());
                    auditRecord4 = this.createAuditRecord(EFFECTIVE_DATE, tierPricingAudit.getCreateDate(),tierPricingAudit.getKey().getEffectiveTimeStamp().toString() , EMPTY,
                            ACTION_DELETE, tierPricingAudit.getChangedOn(), tierPricingAudit.getChangedBy());
                }else if(tierPricingAudit.getAction().trim().equals(AUDIT_ACTION_UPDATE)){
                    if(previousTierPricingAudit.getKey().getDiscountQuantity().longValue() != tierPricingAudit.getKey().getDiscountQuantity().longValue()){
                        auditRecord1 = this.createAuditRecord(QUANTITY_DISCOUNT, tierPricingAudit.getCreateDate(),previousTierPricingAudit.getKey().getDiscountQuantity().toString() ,
                                tierPricingAudit.getKey().getDiscountQuantity().toString(), ACTION_UPDATE, tierPricingAudit.getChangedOn(), tierPricingAudit.getChangedBy());
                    }
                    if(!previousTierPricingAudit.getDiscountTypeCodeDisplayName().equals(tierPricingAudit.getDiscountTypeCodeDisplayName())){
                        auditRecord2 = this.createAuditRecord(QUANTITY_DISCOUNT_TYPE, tierPricingAudit.getCreateDate(), previousTierPricingAudit.getDiscountTypeCodeDisplayName(),
                                tierPricingAudit.getDiscountTypeCodeDisplayName(), ACTION_UPDATE, tierPricingAudit.getChangedOn(), tierPricingAudit.getChangedBy());
                    }
                    if(previousTierPricingAudit.getDiscountValue().doubleValue() != tierPricingAudit.getDiscountValue().doubleValue()){
                        auditRecord3 = this.createAuditRecord(QUANTITY_DISCOUNT_VALUE, tierPricingAudit.getCreateDate(), previousTierPricingAudit.getDiscountValue().toString(),
                                tierPricingAudit.getDiscountValue().toString(), ACTION_UPDATE, tierPricingAudit.getChangedOn(), tierPricingAudit.getChangedBy());
                    }
                    if(previousTierPricingAudit.getKey().getEffectiveTimeStamp().compareTo(tierPricingAudit.getKey().getEffectiveTimeStamp()) != 0){
                        auditRecord4 = this.createAuditRecord(EFFECTIVE_DATE, tierPricingAudit.getCreateDate(), previousTierPricingAudit.getKey().getEffectiveTimeStamp().toString(),
                                tierPricingAudit.getKey().getEffectiveTimeStamp().toString(), ACTION_UPDATE, tierPricingAudit.getChangedOn(), tierPricingAudit.getChangedBy());
                    }
                }
                if(auditRecord1 != null){
                    result.add(auditRecord1);
                }
                if(auditRecord2 != null){
                    result.add(auditRecord2);
                }
                if(auditRecord3 != null){
                    result.add(auditRecord3);
                }
                if(auditRecord4 != null){
                    result.add(auditRecord4);
                }
                previousTierPricingAudit = tierPricingAudit;
            }
        }
        return result;
    }

    /**
     * Create new auditRecord
     * @return an AuditRecord
     */
    private AuditRecord createAuditRecord(String attributeName, LocalDateTime createdDate, String changedFrom, String changedTo, String action, LocalDateTime changedOn, String changedBy) {
        AuditRecord auditRecord = new AuditRecord();
        auditRecord.setAttributeName(attributeName);
        auditRecord.setCreatedDate(createdDate);
        auditRecord.setChangedFrom(changedFrom);
        auditRecord.setChangedTo(changedTo);
        auditRecord.setAction(action);
        auditRecord.setChangedOn(changedOn);
        auditRecord.setChangedBy(changedBy);
        return auditRecord;
    }
}


