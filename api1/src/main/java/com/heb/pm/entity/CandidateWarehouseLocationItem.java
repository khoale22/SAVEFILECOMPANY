/*
 * CandidateWarehouseLocationItem
 *
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
package com.heb.pm.entity;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Represents a candidate ware house location item.
 *
 * @author vn70516
 * @since 2.12.0
 */
@Entity
@Table(name = "ps_whse_loc_itm")
@TypeDefs({
        @TypeDef(name = "fixedLengthChar", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharType.class),
        @TypeDef(name = "fixedLengthCharPK", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharTypePK.class)
})
public class CandidateWarehouseLocationItem implements Serializable {

    private static final long serialVersionUID = 1L;
    public static final String DEFAULT_EMPTY_STRING = " ";
    public static final String CURENT_SLOT_EDC = "101W101";
    public static final String CURENT_SLOT_DEFAULT = "00000";
    public static final String ORD_QTY_TYP_CD_DEFAULT="C";
    @EmbeddedId
    private CandidateWarehouseLocationItemKey key;

    @Column(name = "prch_stat_cd")
    @Type(type = "fixedLengthChar")
    private String purchasingStatus;

    @Column(name = "prch_stat_ts")
    private LocalDateTime purchaseStatusUpdateTime;

    @Column(name = "prch_stat_usr_id")
    @Type(type = "fixedLengthChar")
    private String purchaseStatusUserId;

    @Column(name = "ofsit_qty")
    private Integer offsiteInventory;

    @Column(name = "tot_on_hand_qty")
    private Integer totalOnHandInventory;

    @Column(name = "on_hld_qty")
    private Integer onHoldInventory;

    @Column(name = "billable_qty")
    private Integer billableInventory;

    @Column(name = "dscon_dt")
    private LocalDate discontinueDate;

    @Column(name = "dscon_usr_id")
    @Type(type = "fixedLengthChar")
    private String discontinuedByUID;

    @Column(name = "splr_stat_ts")
    private LocalDateTime supplierStatusUpdateTime;

    @Column(name = "ord_qty_typ_cd")
    private String orderQuantityTypeCode;

    @Column(name = "cs_unt_factr_1")
    private Double unitFactor1;

    @Column(name = "cs_unt_factr_2")
    private Double unitFactor2;

    @Column(name = "var_wt_sw")
    private Boolean variableWeight;

    @Column(name = "ctch_wt_sw")
    private Boolean catchWeight;

    @Column(name = "avg_wt")
    private Double averageWeight;

    @Column(name = "pal_ti")
    private Long whseTie;

    @Column(name = "pal_tier")
    private Long whseTier;

    @Column(name = "curr_pkslt")
    private String currentSlotNumber;

    @Column(name = "flw_typ_cd")
    private String flowTypeCode;

    @Column(name = "added_ts")
    private LocalDateTime createdOn;

    @Column(name = "added_usr_id")
    private String createdBy;

    @Column(name = "lst_updt_ts")
    private LocalDateTime lastUpdatedOn;

    @Column(name = "lst_updt_usr_id")
    private String lastUpdatedId;

    @Column(name = "bil_cst")
    private Double billCost;

    @Column(name = "curr_avg_cst")
    private Double averageCost;

    @Column(name = "po_cmt")
    @Type(type = "fixedLengthCharPK")
    private String comment;
    /**
     * The dscon trx sw.
     */
    @Column(name = "DSCON_TRX_SW")
    private boolean dsconTrxSwitch;

    /**
     * The mfg key.
     */
    @Column(name = "MFG_ID", nullable = false, length = 20)
    @Type(type = "fixedLengthCharPK")
    private String mfgId;

    /**
     * The itm remark sw.
     */
    @Column(name = "ITM_REMARK_SW")
    private boolean itemRemarkSwitch;
    /**
     * The daly hist sw.
     */
    @Column(name = "DALY_HIST_SW")
    private boolean dalyHistSwitch;
    /**
     * The trnsfr avail sw.
     */
    @Column(name = "TRNSFR_AVAIL_SW")
    private boolean trnsfrAvailSwitch;
    /**
     * The po rcmd sw.
     */
    @Column(name = "PO_RCMD_SW")
    private boolean poRcmdSwitch;
    /**
     * The mand flw thrg sw.
     */
    @Column(name = "MAND_FLW_THRG_SW")
    private boolean mandFlwThrgSwitch;

    /**
     * The mand flw thru sw.
     */
    @Column(name = "MAND_FLW_THRU_SW")
    private boolean mandFlwThruSwitch;
    /**
     * The new data sw.
     */
    @Column(name = "NEW_DATA_SW")
    private boolean newData;

    /**
     * The lst updt dt.
     */
    @Column(name = "LST_UPDT_DT", nullable = false, length = 0)
    private LocalDate lastUpdateDt;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumns({
            @JoinColumn(name = "prch_stat_cd", referencedColumnName = "prch_stat_cd", insertable = false, updatable = false, nullable = false)
    })
    private PurchasingStatusCode purchasingStatusCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "whse_loc_typ_cd", referencedColumnName = "loc_typ_cd", insertable = false, updatable = false),
            @JoinColumn(name = "whse_loc_nbr", referencedColumnName = "loc_nbr", insertable = false, updatable = false)
    })
    private Location location;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ord_qty_typ_cd", referencedColumnName = "ord_qty_typ_cd", insertable = false, updatable = false)
    private OrderQuantityType orderQuantityType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "flw_typ_cd", referencedColumnName = "flw_typ_cd", insertable = false, updatable = false)
    private FlowType flowType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ps_itm_id", referencedColumnName = "ps_itm_id", insertable = false, updatable = false)
    private CandidateItemMaster candidateItemMaster;

    @JsonIgnoreProperties("candidateWarehouseLocationItem")
    @OneToMany(mappedBy="candidateWarehouseLocationItem",fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private List<CandidateItemWarehouseComments> candidateItemWarehouseComments;

    //bi-directional many-to-one association to CandidateItemWarehouseVendor
    @OneToMany(mappedBy="candidateWarehouseLocationItem",fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private List<CandidateItemWarehouseVendor> candidateItemWarehouseVendors;


    @Transient
    private Long expctWklyMvt;
    /**
     * Returns the item's purchasing status for this warehouse.
     *
     * @return The item's purchasing status for this warehouse.
     */
    public String getPurchasingStatus() {
        return purchasingStatus;
    }

    /**
     * Sets the item's purchasing status for this warehouse.
     *
     * @param purchasingStatus The item's purchasing status for this warehouse.
     */
    public void setPurchasingStatus(String purchasingStatus) {
        this.purchasingStatus = purchasingStatus;
    }

    /**
     * Returns the time the purchasing status was last changed.
     *
     * @return The time the purchasing status was last changed.
     */
    public LocalDateTime getPurchaseStatusUpdateTime() {
        return purchaseStatusUpdateTime;
    }

    /**
     * Sets the time the purchasing status was last changed.
     *
     * @param purchaseStatusUpdateTime The time the purchasing status was last changed.
     */
    public void setPurchaseStatusUpdateTime(LocalDateTime purchaseStatusUpdateTime) {
        this.purchaseStatusUpdateTime = purchaseStatusUpdateTime;
    }

    /**
     * Returns the ID of the user who last changed the item's purchasing status at this warehouse.
     *
     * @return The ID of the user who last changed the item's purchasing status at this warehouse.
     */
    public String getPurchaseStatusUserId() {
        return purchaseStatusUserId;
    }

    /**
     * Sets the ID of the user who last changed the item's purchasing status at this warehouse.
     *
     * @param purchaseStatusUserId The ID of the user who last changed the item's purchasing status at this warehouse.
     */
    public void setPurchaseStatusUserId(String purchaseStatusUserId) {
        this.purchaseStatusUserId = purchaseStatusUserId;
    }

    /**
     * Returns the key for this record.
     *
     * @return The key for this record.
     */
    public CandidateWarehouseLocationItemKey getKey() {
        return key;
    }

    /**
     * Sets the key for this record.
     *
     * @param key The key for this record.
     */
    public void setKey(CandidateWarehouseLocationItemKey key) {
        this.key = key;
    }

    /**
     * Returns whether or not this item is discontinued at this warehouse.
     *
     * @return True if this item is discontinued at this warehouse and false otherwise.
     */
    public boolean isDiscontinued() {
        return PurchasingStatusCode.CodeValues.DISCONTINUED.getPurchasingStatus().equals(this.getPurchasingStatus());
    }

    /**
     * Returns off-site inventory.
     *
     * @return Off-site inventory.
     */
    public Integer getOffsiteInventory() {
        return offsiteInventory;
    }

    /**
     * Sets off-site inventory.
     *
     * @param offsiteInventory Off-site inventory.
     */
    public void setOffsiteInventory(Integer offsiteInventory) {
        this.offsiteInventory = offsiteInventory;
    }

    /**
     * Returns total inventory.
     *
     * @return Total inventory.
     */
    public Integer getTotalOnHandInventory() {
        return totalOnHandInventory;
    }

    /**
     * Sets total inventory.
     *
     * @param totalOnHandInventory Total inventory.
     */
    public void setTotalOnHandInventory(Integer totalOnHandInventory) {
        this.totalOnHandInventory = totalOnHandInventory;
    }

    /**
     * Returns on-hold inventory.
     *
     * @return On-hold inventory.
     */
    public Integer getOnHoldInventory() {
        return onHoldInventory;
    }

    /**
     * Sets on-hold inventory.
     *
     * @param onHoldInventory on-hold inventory.
     */
    public void setOnHoldInventory(Integer onHoldInventory) {
        this.onHoldInventory = onHoldInventory;
    }

    /**
     * Returns billable inventory.
     *
     * @return billable inventory.
     */
    public Integer getBillableInventory() {
        return billableInventory;
    }

    /**
     * Sets billable inventory.
     *
     * @param billableInventory Billable inventory.
     */
    public void setBillableInventory(Integer billableInventory) {
        this.billableInventory = billableInventory;
    }

    /**
     * Returns the discontinued date.
     *
     * @return the discontinued date.
     */
    public LocalDate getDiscontinueDate() {
        return discontinueDate;
    }

    /**
     * Sets the discontinued date.
     *
     * @param discontinueDate the discontinued date.
     */
    public void setDiscontinueDate(LocalDate discontinueDate) {
        this.discontinueDate = discontinueDate;
    }

    /**
     * Returns the user id of the person who discontinues the whs item.
     *
     * @return the user id of the person who discontinues the whs item.
     */
    public String getDiscontinuedByUID() {
        return discontinuedByUID;
    }

    /**
     * Sets the user id of the person who discontinues the whs item.
     *
     * @param discontinuedByUID the user id of the person who discontinues the whs item.
     */
    public void setDiscontinuedByUID(String discontinuedByUID) {
        this.discontinuedByUID = discontinuedByUID;
    }

    /**
     * Returns the SupplierStatusUpdateTime. The last time a supplier updated the warehouse item.
     *
     * @return SupplierStatusUpdateTime
     */
    public LocalDateTime getSupplierStatusUpdateTime() {
        return supplierStatusUpdateTime;
    }

    /**
     * Sets the SupplierStatusUpdateTime
     *
     * @param supplierStatusUpdateTime The SupplierStatusUpdateTime
     */
    public void setSupplierStatusUpdateTime(LocalDateTime supplierStatusUpdateTime) {
        this.supplierStatusUpdateTime = supplierStatusUpdateTime;
    }

    /**
     * Returns the PurchasingStatusCode. The information associated with the code table with the Warehouse purchasing
     * status id.
     *
     * @return PurchasingStatusCode
     */
    public PurchasingStatusCode getPurchasingStatusCode() {
        return purchasingStatusCode;
    }

    /**
     * Sets the PurchasingStatusCode
     *
     * @param purchasingStatusCode The PurchasingStatusCode
     */
    public void setPurchasingStatusCode(PurchasingStatusCode purchasingStatusCode) {
        this.purchasingStatusCode = purchasingStatusCode;
    }

    /**
     * The name and number of the warehouse location for the item
     *
     * @return
     */
    public Location getLocation() {
        return location;
    }

    /**
     * Updates the location of the warehouse location item
     *
     * @param location the new location
     */
    public void setLocation(Location location) {
        this.location = location;
    }

    /**
     * Code for the orderQuantityType used to reference the table
     *
     * @return orderQuantityTypeCode
     */
    public String getOrderQuantityTypeCode() {
        return orderQuantityTypeCode;
    }

    /**
     * Updates the orderQuantityTypeCode
     *
     * @param orderQuantityTypeCode the new orderQuantityTypeCode
     */
    public void setOrderQuantityTypeCode(String orderQuantityTypeCode) {
        this.orderQuantityTypeCode = orderQuantityTypeCode;
    }

    /**
     * Represents ordering units that do not directly relate to dollars, cube, applets, layers, pounts, or dozens.
     * Vendor instructs the value per case.  Values: .01-.99
     *
     * @return the unitFactor1
     */
    public Double getUnitFactor1() {
        return unitFactor1;
    }

    /**
     * Updates the unitFactor1
     *
     * @param unitFactor1 the new unitFactor1
     */
    public void setUnitFactor1(Double unitFactor1) {
        this.unitFactor1 = unitFactor1;
    }

    /**
     * Allows buying mixed layers within a vendor pallet.
     *
     * @return the unitFactor2
     */
    public Double getUnitFactor2() {
        return unitFactor2;
    }

    /**
     * Updates the unitFactor2
     *
     * @param unitFactor2 the new unitFactor2
     */
    public void setUnitFactor2(Double unitFactor2) {
        this.unitFactor2 = unitFactor2;
    }

    /**
     * Cases, Pounds, Pairs, Gross, Eachs - the way a shipment is flagged to be received as.  Default is cases.
     *
     * @return orderQuantityType
     */
    public OrderQuantityType getOrderQuantityType() {
        return orderQuantityType;
    }

    /**
     * Update the orderQuantityType
     *
     * @param orderQuantityType the new orderQuantityType
     */
    public void setOrderQuantityType(OrderQuantityType orderQuantityType) {
        this.orderQuantityType = orderQuantityType;
    }

    /**
     * Gets the String vision of the order quantity type
     *
     * @return orderQuantityTypeDisplayName
     */
    public String getOrderQuantityTypeDisplayName() {
    	String returnString = null;
		if (this.getOrderQuantityType() != null) {
			returnString = orderQuantityType.getOrderQuantityTypeDisplay();
		}
		return returnString;
    }

    /**
     * Price per lb. flagged to note as. (Y/N flag)
     *
     * @return variableWeight
     */
    public Boolean isVariableWeight() {
        return variableWeight;
    }

    /**
     * Updates the variableWeight
     *
     * @param variableWeight the new variableWeight
     */
    public void setVariableWeight(Boolean variableWeight) {
        this.variableWeight = variableWeight;
    }

    /**
     * Y/N flag - Item is received and reconciled by the pound. Used in Meat and Seafood, this flag would indicate that
     * it's in this format.
     *
     * @return
     */
    public Boolean isCatchWeight() {
        return catchWeight;
    }

    /**
     * Updates the catchWeight
     *
     * @param catchWeight the new catchWeight
     */
    public void setCatchWeight(Boolean catchWeight) {
        this.catchWeight = catchWeight;
    }

    /**
     * Numerical value to the average weight of the product.
     *
     * @return averageWeight
     */
    public Double getAverageWeight() {
        return averageWeight;
    }

    /**
     * Updates the averageWeight
     *
     * @param averageWeight the averageWeight
     */
    public void setAverageWeight(Double averageWeight) {
        this.averageWeight = averageWeight;
    }

    /**
     * How many cases the vendor stacks on a pallet on a layer at the HEB Warehouse.
     *
     * @return whseTie
     */
    public Long getWhseTie() {
        return whseTie;
    }

    /**
     * Updates the whseTie
     *
     * @param whseTie the new whseTie
     */
    public void setWhseTie(Long whseTie) {
        this.whseTie = whseTie;
    }

    /**
     * How many cases the vendor stacks on a pallet on height at the HEB Warehouse.
     *
     * @return whseTier
     */
    public Long getWhseTier() {
        return whseTier;
    }

    /**
     * Updates the whseTier
     *
     * @param whseTier the new whseTier
     */
    public void setWhseTier(Long whseTier) {
        this.whseTier = whseTier;
    }

    /**
     * The pick slot in the warehouse for which the product is assigned and stored.
     *
     * @return currentSlotNumber
     */
    public String getCurrentSlotNumber() {
        return currentSlotNumber;
    }

    /**
     * Updates the currentSlotNumber
     *
     * @param currentSlotNumber the new currentSlotNumber
     */
    public void setCurrentSlotNumber(String currentSlotNumber) {
        this.currentSlotNumber = currentSlotNumber;
    }

    /**
     * Gets the flow type code used to reference the flow typ code table
     *
     * @return flowTypeCode
     */
    public String getFlowTypeCode() {
        return flowTypeCode;
    }

    /**
     * Updates the flowTypeCode
     *
     * @param flowTypeCode the new flowTypeCode
     */
    public void setFlowTypeCode(String flowTypeCode) {
        this.flowTypeCode = flowTypeCode;
    }

    /**
     * Flow thru items that do not get stocked into the shelves until the warehouse flushes. More to do with supply
     * chain vs. the product itself (Supply Chain Attribute)
     *
     * @return flowType
     */
    public FlowType getFlowType() {
        return flowType;
    }

    /**
     * Updates flowType
     *
     * @param flowType the new flowType
     */
    public void setFlowType(FlowType flowType) {
        this.flowType = flowType;
    }

    /**
     * Date and Timestamp of when record was created within the system.
     *
     * @return createdOn
     */
    public LocalDateTime getCreatedOn() {
        return createdOn;
    }

    /**
     * Updates createdOn
     *
     * @param createdOn the new createdOn
     */
    public void setCreatedOn(LocalDateTime createdOn) {
        this.createdOn = createdOn;
    }

    /**
     * The ID of the Partner that created the item within the system.
     *
     * @return createdBy
     */
    public String getCreatedBy() {
        return createdBy;
    }

    /**
     * Updates createdBy
     *
     * @param createdBy the new createdBy
     */
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    /**
     * The date and timestap of when record was updated within the system.
     *
     * @return lastUpdatedOn
     */
    public LocalDateTime getLastUpdatedOn() {
        return lastUpdatedOn;
    }

    /**
     * Updates lastUpdatedOn
     *
     * @param lastUpdatedOn the new lastUpdatedOn
     */
    public void setLastUpdatedOn(LocalDateTime lastUpdatedOn) {
        this.lastUpdatedOn = lastUpdatedOn;
    }

    /**
     * The ID of the Partner that updated the item within the system.
     *
     * @return lastUpdatedId
     */
    public String getLastUpdatedId() {
        return lastUpdatedId;
    }

    /**
     * Updates lastUpdatedId
     *
     * @param lastUpdatedId the new lastUpdatedId
     */
    public void setLastUpdatedId(String lastUpdatedId) {
        this.lastUpdatedId = lastUpdatedId;
    }

    /**
     * Billing cost is the cost that the store is being billed when they order a case pack from the warehouse to store.
     *
     * @return billCost
     */
    public Double getBillCost() {
        return billCost;
    }

    /**
     * Updates billCost
     *
     * @param billCost the new billCost
     */
    public void setBillCost(Double billCost) {
        this.billCost = billCost;
    }

    /**
     * recalculated cost based off the inventory at the warehouse; 12 pack of coke w/cost of $12 dollars, mark down of
     * $10 and you buy another pack at $10 - so now you have to recalculate the cost…
     *
     * @return
     */
    public Double getAverageCost() {
        return averageCost;
    }

    /**
     * Updates averageCost
     *
     * @param averageCost averageCost
     */
    public void setAverageCost(Double averageCost) {
        this.averageCost = averageCost;
    }

    /**
     * Gets the text field housing additional comments by submitter or updater tied to this record.
     *
     * @return comment
     */
    public String getComment() {
        return comment;
    }

    /**
     * Updates the comment
     *
     * @param comment the new comment
     */
    public void setComment(String comment) {
        this.comment = comment;
    }

    public boolean isDsconTrxSwitch() {
        return dsconTrxSwitch;
    }

    public void setDsconTrxSwitch(boolean dsconTrxSwitch) {
        this.dsconTrxSwitch = dsconTrxSwitch;
    }
    public String getMfgId() {
        return mfgId;
    }

    public void setMfgId(String mfgId) {
        this.mfgId = mfgId;
    }
    public boolean isItemRemarkSwitch() {
        return itemRemarkSwitch;
    }

    public void setItemRemarkSwitch(boolean itemRemarkSwitch) {
        this.itemRemarkSwitch = itemRemarkSwitch;
    }
    public boolean isDalyHistSwitch() {
        return dalyHistSwitch;
    }

    public void setDalyHistSwitch(boolean dalyHistSwitch) {
        this.dalyHistSwitch = dalyHistSwitch;
    }

    public boolean isTrnsfrAvailSwitch() {
        return trnsfrAvailSwitch;
    }

    public void setTrnsfrAvailSwitch(boolean trnsfrAvailSwitch) {
        this.trnsfrAvailSwitch = trnsfrAvailSwitch;
    }

    public boolean isPoRcmdSwitch() {
        return poRcmdSwitch;
    }

    public void setPoRcmdSwitch(boolean poRcmdSwitch) {
        this.poRcmdSwitch = poRcmdSwitch;
    }

    public boolean isMandFlwThrgSwitch() {
        return mandFlwThrgSwitch;
    }

    public void setMandFlwThrgSwitch(boolean mandFlwThrgSwitch) {
        this.mandFlwThrgSwitch = mandFlwThrgSwitch;
    }

    public boolean isMandFlwThruSwitch() {
        return mandFlwThruSwitch;
    }

    public void setMandFlwThruSwitch(boolean mandFlwThruSwitch) {
        this.mandFlwThruSwitch = mandFlwThruSwitch;
    }

    public boolean isNewData() {
        return newData;
    }

    public void setNewData(boolean newData) {
        this.newData = newData;
    }

    public Long getExpctWklyMvt() {
        return expctWklyMvt;
    }

    public void setExpctWklyMvt(Long expctWklyMvt) {
        this.expctWklyMvt = expctWklyMvt;
    }
    public LocalDate getLastUpdateDt() {
        return lastUpdateDt;
    }

    public void setLastUpdateDt(LocalDate lastUpdateDt) {
        this.lastUpdateDt = lastUpdateDt;
    }
    /**
     * @return Gets the value of candidateItemMaster and returns candidateItemMaster
     */
    public void setCandidateItemMaster(CandidateItemMaster candidateItemMaster) {
        this.candidateItemMaster = candidateItemMaster;
    }

    /**
     * Sets the candidateItemMaster
     */
    public CandidateItemMaster getCandidateItemMaster() {
        return candidateItemMaster;
    }
    public List<CandidateItemWarehouseComments> getCandidateItemWarehouseComments() {
        return candidateItemWarehouseComments;
    }

    public void setCandidateItemWarehouseComments(List<CandidateItemWarehouseComments> candidateItemWarehouseComments) {
        this.candidateItemWarehouseComments = candidateItemWarehouseComments;
    }

    public List<CandidateItemWarehouseVendor> getCandidateItemWarehouseVendors() {
        return candidateItemWarehouseVendors;
    }

    public void setCandidateItemWarehouseVendors(List<CandidateItemWarehouseVendor> candidateItemWarehouseVendors) {
        this.candidateItemWarehouseVendors = candidateItemWarehouseVendors;
    }
    /**
     * Returns a String representation of the object.
     *
     * @return A String representation of the object.
     */
    @Override
    public String toString() {
        return "WarehouseLocationItem{" +
                "key=" + key +
                ", purchasingStatus='" + purchasingStatus + '\'' +
                ", purchaseStatusUpdateTime=" + purchaseStatusUpdateTime +
                ", offsiteInventory=" + offsiteInventory +
                ", totalOnHandInventory=" + totalOnHandInventory +
                ", onHoldInventory=" + onHoldInventory +
                ", billableInventory=" + billableInventory +
                ", discontinueDate=" + discontinueDate +
                ", discontinuedByUID='" + discontinuedByUID + '\'' +
                ", orderQuantityType='" + orderQuantityType + '\'' +
                ", unitFactor1='" + unitFactor1 + '\'' +
                ", unitFactor2='" + unitFactor2 + '\'' +
                ", variableWeight='" + variableWeight + '\'' +
                ", catchWeight='" + catchWeight + '\'' +
                ", averageWeight='" + averageWeight + '\'' +
                ", billCost='" + billCost + '\'' +
                ", averageCost='" + averageCost + '\'' +
                ", whseTie='" + whseTie + '\'' +
                ", whseTier='" + whseTier + '\'' +
                ", currentSlotNumber='" + currentSlotNumber + '\'' +
                ", flowType='" + flowType + '\'' +
                ", createdOn='" + createdOn + '\'' +
                ", createdBy='" + createdBy + '\'' +
                ", lastUpdatedOn='" + lastUpdatedOn + '\'' +
                ", lastUpdatedId='" + lastUpdatedId + '\'' +
                ", comment='" + comment + '\'' +
                '}';
    }

    /**
     * Compares another object to this one. If that object is a WarehouseLocationItem, it uses they keys
     * to determine if they are equal and ignores non-key values for the comparison.
     *
     * @param o The object to compare to.
     * @return True if they are equal and false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CandidateWarehouseLocationItem)) {
            return false;
        }

        CandidateWarehouseLocationItem that = (CandidateWarehouseLocationItem) o;
        if (this.key != null ? !this.key.equals(that.key) : that.key != null) return false;

        return true;
    }

    /**
     * Returns a hash code for this object. If two objects are equal, they have the same hash. If they are not equal,
     * they have different hash codes.
     *
     * @return The hash code for this obejct.
     */
    @Override
    public int hashCode() {
        return this.key == null ? 0 : this.key.hashCode();
    }

    @PrePersist
    public void setCandidateItemId() {
        if (this.getKey().getCandidateItemId() == null) {
            this.getKey().setCandidateItemId(this.candidateItemMaster.getCandidateItemId());
        }
    }
}
