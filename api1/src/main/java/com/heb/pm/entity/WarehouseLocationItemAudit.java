/*
 *  WarehouseLocationItemAudit
 *  Copyright (c) 2018 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.entity;

import com.heb.util.audit.Audit;
import com.heb.util.audit.AuditableField;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Holds case pack audit data for a particular warehouse.
 *  @author a786878
 *  @since 2.15.0
 *
 */
@Entity
@Table(name="whse_loc_itm_aud")
// dB2Oracle changes vn00907 starts
@TypeDefs({
@TypeDef(name = "fixedLengthChar", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharType.class),
@TypeDef(name = "fixedLengthCharPK", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharTypePK.class)
})
// dB2Oracle changes vn00907
public class WarehouseLocationItemAudit implements Audit, Serializable {

	private static final long serialVersionUID = 1L;

    @EmbeddedId
    private WarehouseLocationItemAuditKey key;

	@Column(name = "act_cd")
	private String action;

    @Column(name = "prch_stat_cd")
    //db2o changes  vn00907
    @Type(type="fixedLengthChar")
    private String purchasingStatus;

	@Column(name = "prch_stat_ts")
	private LocalDateTime purchaseStatusUpdateTime;

	@Column(name = "prch_stat_usr_id")
	 //db2o changes  vn00907
	 @Type(type="fixedLengthChar")
	private String purchaseStatusUserId;

	@Column(name="dstrb_res_qty")
	private int distributionReserveInventory;

	@Column(name="ofsit_qty")
	private int offsiteInventory;

	@Column(name="tot_on_hand_qty")
	private int totalOnHandInventory;

	@Column(name="on_hld_qty")
	private int onHoldInventory;

	@Column(name="billable_qty")
	private int billableInventory;

	@Column(name = "dscon_dt")
	private LocalDateTime discontinueDate;

	@Column(name = "dscon_usr_id")
	 //db2o changes  vn00907
	@Type(type="fixedLengthChar")
	private String discontinuedByUID;

	@Column(name = "shp_inr_pk")
	private long shipPack;

	@Column(name = "shp_cs_ln")
	private double shipLength;

	@Column(name = "shp_cs_wd")
	private double shipWidth;

	@Column(name = "shp_cs_ht")
	private double shipHeight;

	@Column(name = "shp_wt")
	private double shipWeight;

	@Column(name = "shp_cu")
	private double shipCube;

	@Column(name = "splr_itm_status_cd")
	private String supplierStatusCode;

	@Column(name = "splr_stat_ts")
	private LocalDateTime supplierStatusUpdateTime;

	@AuditableField(displayName = "Order Quantity Type",filter = FilterConstants.WAREHOUSE_AUDIT)
	@Column(name = "ord_qty_typ_cd")
	private String orderQuantityTypeCode;

	@AuditableField(displayName = "Expected Weekly Movement",filter = FilterConstants.WAREHOUSE_AUDIT)
	@Column(name = "expct_wkly_mvt")
	private Long expectedWeeklyMovement;

	@AuditableField(displayName = "Whse Max Ship Qty#",filter = FilterConstants.WAREHOUSE_AUDIT)
	@Column(name = "max_shp_whse_qty")
	private Long whseMaxShipQuantityNumber;

	@AuditableField(displayName = "Unit Factor 1",filter = FilterConstants.WAREHOUSE_AUDIT)
	@Column(name = "cs_unt_factr_1")
	private Double unitFactor1;

	@AuditableField(displayName = "Unit Factor 2",filter = FilterConstants.WAREHOUSE_AUDIT)
	@Column(name = "cs_unt_factr_2")
	private Double unitFactor2;

	@AuditableField(displayName = "Variable Weight",filter = FilterConstants.WAREHOUSE_AUDIT)
	@Column(name = "VAR_WT_SW")
	private boolean variableWeight;

	@AuditableField(displayName = "Catch Weight",filter = FilterConstants.WAREHOUSE_AUDIT)
	@Column(name = "CTCH_WT_SW")
	private boolean catchWeight;

	@AuditableField(displayName = "Average Weight",filter = FilterConstants.WAREHOUSE_AUDIT)
	@Column(name = "AVG_WT")
	private Double averageWeight;

	@AuditableField(displayName = "Whse Tie",filter = FilterConstants.WAREHOUSE_AUDIT)
	@Column(name = "PAL_TI")
	private Long whseTie;

	@AuditableField(displayName = "Whse Tier",filter = FilterConstants.WAREHOUSE_AUDIT)
	@Column(name = "PAL_TIER")
	private Long whseTier;

	@AuditableField(displayName = "Flow Type",filter = FilterConstants.WAREHOUSE_AUDIT)
	@Column(name = "FLW_TYP_CD")
	private String flowTypeCode;

	@Column(name = "ADDED_TS")
	private LocalDateTime createdOn;

	@Column(name = "ADDED_USR_ID")
	private String createdBy;

	@Column(name = "LST_UPDT_TS")
	private LocalDateTime lastUpdatedOn;

	@Column(name = "LST_UPDT_USR_ID")
	private String changedBy;

	@AuditableField(displayName = "Billing Cost",filter = FilterConstants.WAREHOUSE_AUDIT)
	@Column(name = "BIL_CST")
	private Double billCost;

	@AuditableField(displayName = "Average Cost",filter = FilterConstants.WAREHOUSE_AUDIT)
	@Column(name = "CURR_AVG_CST")
	private Double averageCost;

	@Column(name = "PO_CMT")
	private String comment;

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
    public WarehouseLocationItemAuditKey getKey() {
        return key;
    }

	/**
	 * Sets the key for this record.
	 *
	 * @param key The key for this record.
	 */
    public void setKey(WarehouseLocationItemAuditKey key) {
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
	 * Returns distribution reserve quantity.
	 *
	 * @return Distribution reserve quantity.
	 */
	public int getDistributionReserveInventory() {
		return distributionReserveInventory;
	}

	/**
	 * Sets distribution reserve quantity.
	 *
	 * @param distributionReserveInventory Distribution reserve quantity.
	 */
	public void setDistributionReserveInventory(int distributionReserveInventory) {
		this.distributionReserveInventory = distributionReserveInventory;
	}

	/**
	 * Returns off-site inventory.
	 *
	 * @return Off-site inventory.
	 */
	public int getOffsiteInventory() {
		return offsiteInventory;
	}

	/**
	 * Sets off-site inventory.
	 *
	 * @param offsiteInventory Off-site inventory.
	 */
	public void setOffsiteInventory(int offsiteInventory) {
		this.offsiteInventory = offsiteInventory;
	}

	/**
	 * Returns total inventory.
	 *
	 * @return Total inventory.
	 */
	public int getTotalOnHandInventory() {
		return totalOnHandInventory;
	}

	/**
	 * Sets total inventory.
	 *
	 * @param totalOnHandInventory Total inventory.
	 */
	public void setTotalOnHandInventory(int totalOnHandInventory) {
		this.totalOnHandInventory = totalOnHandInventory;
	}

	/**
	 * Returns on-hold inventory.
	 *
	 * @return On-hold inventory.
	 */
	public int getOnHoldInventory() {
		return onHoldInventory;
	}

	/**
	 * Sets on-hold inventory.
	 *
	 * @param onHoldInventory on-hold inventory.
	 */
	public void setOnHoldInventory(int onHoldInventory) {
		this.onHoldInventory = onHoldInventory;
	}

	/**
	 * Returns billable inventory.
	 *
	 * @return billable inventory.
	 */
	public int getBillableInventory() {
		return billableInventory;
	}

	/**
	 * Sets billable inventory.
	 *
	 * @param billableInventory Billable inventory.
	 */
	public void setBillableInventory(int billableInventory) {
		this.billableInventory = billableInventory;
	}

	/**
	 * Returns the discontinued date.
	 *
	 * @return the discontinued date.
	 */
	public LocalDateTime getDiscontinueDate() {
		return discontinueDate;
	}

	/**
	 * Sets the discontinued date.
	 *
	 * @param discontinueDate the discontinued date.
	 */
	public void setDiscontinueDate(LocalDateTime discontinueDate) {
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
	 * Returns the ShipPack
	 *
	 * @return ShipPack
	 **/
	public long getShipPack() {
		return shipPack;
	}

	/**
	 * Sets the ShipPack
	 *
	 * @param shipPack The ShipPack
	 **/

	public void setShipPack(long shipPack) {
		this.shipPack = shipPack;
	}

	/**
	 * Returns the ShipLength
	 *
	 * @return ShipLength
	 **/
	public double getShipLength() {
		return shipLength;
	}

	/**
	 * Sets the ShipLength
	 *
	 * @param shipLength The ShipLength
	 **/

	public void setShipLength(double shipLength) {
		this.shipLength = shipLength;
	}

	/**
	 * Returns the ShipWidth
	 *
	 * @return ShipWidth
	 **/
	public double getShipWidth() {
		return shipWidth;
	}

	/**
	 * Sets the ShipWidth
	 *
	 * @param shipWidth The ShipWidth
	 **/

	public void setShipWidth(double shipWidth) {
		this.shipWidth = shipWidth;
	}

	/**
	 * Returns the ShipHeight
	 *
	 * @return ShipHeight
	 **/
	public double getShipHeight() {
		return shipHeight;
	}

	/**
	 * Sets the ShipHeight
	 *
	 * @param shipHeight The ShipHeight
	 **/

	public void setShipHeight(double shipHeight) {
		this.shipHeight = shipHeight;
	}

	/**
	 * Returns the ShipWeight
	 *
	 * @return ShipWeight
	 **/
	public double getShipWeight() {
		return shipWeight;
	}

	/**
	 * Sets the ShipWeight
	 *
	 * @param shipWeight The ShipWeight
	 **/

	public void setShipWeight(double shipWeight) {
		this.shipWeight = shipWeight;
	}

	/**
	 * Returns the ShipCube
	 *
	 * @return ShipCube
	 **/
	public double getShipCube() {
		return shipCube;
	}

	/**
	 * Sets the ShipCube
	 *
	 * @param shipCube The ShipCube
	 **/

	public void setShipCube(double shipCube) {
		this.shipCube = shipCube;
	}

	/**
	 * Returns the SupplierStatusCode. This determines whether the warehouse item is Discontinued or Active. This is the
	 * warehouse status.
	 *
	 * @return SupplierStatusCode
	 */
	public String getSupplierStatusCode() {
		return supplierStatusCode;
	}

	/**
	 * Sets the SupplierStatusCode
	 *
	 * @param supplierStatusCode The SupplierStatusCode
	 */
	public void setSupplierStatusCode(String supplierStatusCode) {
		this.supplierStatusCode = supplierStatusCode;
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
	 * Code for the orderQuantityType used to reference the table
	 * @return orderQuantityTypeCode
	 */
	public String getOrderQuantityTypeCode() {
		return orderQuantityTypeCode;
	}

	/**
	 * Updates the orderQuantityTypeCode
	 * @param orderQuantityTypeCode the new orderQuantityTypeCode
	 */
	public void setOrderQuantityTypeCode(String orderQuantityTypeCode) {
		this.orderQuantityTypeCode = orderQuantityTypeCode;
	}

	/**
	 * New item set up - when they are not sure what the forecast is going to be; vendor provides - not used for
	 * anything else.
	 * @return expectedWeeklyMovement
	 */
	public Long getExpectedWeeklyMovement() {
		return expectedWeeklyMovement;
	}

	/**
	 * Updates the expectedWeeklyMovement
	 * @param expectedWeeklyMovement the new expectedWeeklyMovement
	 */
	public void setExpectedWeeklyMovement(Long expectedWeeklyMovement) {
		this.expectedWeeklyMovement = expectedWeeklyMovement;
	}

	/**
	 * Max # of cases a store can receive of this item
	 * @return whseMaxShipQuantityNumber
	 */
	public Long getWhseMaxShipQuantityNumber() {
		return whseMaxShipQuantityNumber;
	}

	/**
	 * Updates the whseMaxShipQuantityNumber
	 * @param whseMaxShipQuantityNumber the new whseMaxShipQuantityNumber
	 */
	public void setWhseMaxShipQuantityNumber(Long whseMaxShipQuantityNumber) {
		this.whseMaxShipQuantityNumber = whseMaxShipQuantityNumber;
	}

	/**
	 * Represents ordering units that do not directly relate to dollars, cube, applets, layers, pounts, or dozens.
	 * Vendor instructs the value per case.  Values: .01-.99
	 * @return the unitFactor1
	 */
	public Double getUnitFactor1() {
		return unitFactor1;
	}

	/**
	 * Updates the unitFactor1
	 * @param unitFactor1 the new unitFactor1
	 */
	public void setUnitFactor1(Double unitFactor1) {
		this.unitFactor1 = unitFactor1;
	}

	/**
	 *Allows buying mixed layers within a vendor pallet.
	 * @return the unitFactor2
	 */
	public Double getUnitFactor2() {
		return unitFactor2;
	}

	/**
	 * Updates the unitFactor2
	 * @param unitFactor2 the new unitFactor2
	 */
	public void setUnitFactor2(Double unitFactor2) {
		this.unitFactor2 = unitFactor2;
	}

	/**
	 * Price per lb. flagged to note as. (Y/N flag)
	 * @return variableWeight
	 */
	public boolean isVariableWeight() {
		return variableWeight;
	}

	/**
	 * Updates the variableWeight
	 * @param variableWeight the new variableWeight
	 */
	public void setVariableWeight(boolean variableWeight) {
		this.variableWeight = variableWeight;
	}

	/**
	 * Y/N flag - Item is received and reconciled by the pound. Used in Meat and Seafood, this flag would indicate that
	 * it's in this format.
	 * @return
	 */
	public boolean isCatchWeight() {
		return catchWeight;
	}

	/**
	 * Updates the catchWeight
	 * @param catchWeight the new catchWeight
	 */
	public void setCatchWeight(boolean catchWeight) {
		this.catchWeight = catchWeight;
	}

	/**
	 * Numerical value to the average weight of the product.
	 * @return averageWeight
	 */
	public Double getAverageWeight() {
		return averageWeight;
	}

	/**
	 * Updates the averageWeight
	 * @param averageWeight the averageWeight
	 */
	public void setAverageWeight(Double averageWeight) {
		this.averageWeight = averageWeight;
	}

	/**
	 * How many cases the vendor stacks on a pallet on a layer at the HEB Warehouse.
	 * @return whseTie
	 */
	public Long getWhseTie() {
		return whseTie;
	}

	/**
	 * Updates the whseTie
	 * @param whseTie the new whseTie
	 */
	public void setWhseTie(Long whseTie) {
		this.whseTie = whseTie;
	}

	/**
	 * How many cases the vendor stacks on a pallet on height at the HEB Warehouse.
	 * @return whseTier
	 */
	public Long getWhseTier() {
		return whseTier;
	}

	/**
	 * Updates the whseTier
	 * @param whseTier the new whseTier
	 */
	public void setWhseTier(Long whseTier) {
		this.whseTier = whseTier;
	}

	/**
	 * Gets the flow type code used to reference the flow typ code table
	 * @return flowTypeCode
	 */
	public String getFlowTypeCode() {
		return flowTypeCode;
	}

	/**
	 * Updates the flowTypeCode
	 * @param flowTypeCode the new flowTypeCode
	 */
	public void setFlowTypeCode(String flowTypeCode) {
		this.flowTypeCode = flowTypeCode;
	}

	/**
	 * Date and Timestamp of when record was created within the system.
	 * @return createdOn
	 */
	public LocalDateTime getCreatedOn() {
		return createdOn;
	}

	/**
	 * Updates createdOn
	 * @param createdOn the new createdOn
	 */
	public void setCreatedOn(LocalDateTime createdOn) {
		this.createdOn = createdOn;
	}

	/**
	 * The ID of the Partner that created the item within the system.
	 * @return createdBy
	 */
	public String getCreatedBy() {
		return createdBy;
	}

	/**
	 * Updates createdBy
	 * @param createdBy the new createdBy
	 */
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	/**
	 * The date and timestap of when record was updated within the system.
	 * @return lastUpdatedOn
	 */
	public LocalDateTime getLastUpdatedOn() {
		return lastUpdatedOn;
	}

	/**
	 * Updates lastUpdatedOn
	 * @param lastUpdatedOn the new lastUpdatedOn
	 */
	public void setLastUpdatedOn(LocalDateTime lastUpdatedOn) {
		this.lastUpdatedOn = lastUpdatedOn;
	}

	/**
	 * Billing cost is the cost that the store is being billed when they order a case pack from the warehouse to store.
	 * @return billCost
	 */
	public Double getBillCost() {
		return billCost;
	}

	/**
	 * Updates billCost
	 * @param billCost the new billCost
	 */
	public void setBillCost(Double billCost) {
		this.billCost = billCost;
	}

	/**
	 * recalculated cost based off the inventory at the warehouse; 12 pack of coke w/cost of $12 dollars, mark down of
	 * $10 and you buy another pack at $10 - so now you have to recalculate the cost…
	 * @return
	 */
	public Double getAverageCost() {
		return averageCost;
	}

	/**
	 * Updates averageCost
	 * @param averageCost averageCost
	 */
	public void setAverageCost(Double averageCost) {
		this.averageCost = averageCost;
	}

	/**
	 * Gets the text field housing additional comments by submitter or updater tied to this record.
	 * @return comment
	 */
	public String getComment() {
		return comment;
	}

	/**
	 * Updates the comment
	 * @param comment the new comment
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}

	/**
	 * Returns a String representation of the object.
	 *
	 * @return A String representation of the object.
	 */
	@Override
	public String toString() {
		return "WarehouseLocationItemAudit{" +
				"key=" + key +
				", action='" + action + '\'' +
				", changedBy='" + changedBy + '\'' +
				", purchasingStatus='" + purchasingStatus + '\'' +
				", purchaseStatusUpdateTime=" + purchaseStatusUpdateTime +
				", purchaseStatusUserId='" + purchaseStatusUserId + '\'' +
				", distributionReserveInventory=" + distributionReserveInventory +
				", offsiteInventory=" + offsiteInventory +
				", totalOnHandInventory=" + totalOnHandInventory +
				", onHoldInventory=" + onHoldInventory +
				", billableInventory=" + billableInventory +
				", discontinueDate=" + discontinueDate +
				", discontinuedByUID='" + discontinuedByUID + '\'' +
				", shipPack=" + shipPack +
				", shipLength=" + shipLength +
				", shipWidth=" + shipWidth +
				", shipHeight=" + shipHeight +
				", shipWeight=" + shipWeight +
				", shipCube=" + shipCube +
				", supplierStatusCode='" + supplierStatusCode + '\'' +
				", supplierStatusUpdateTime=" + supplierStatusUpdateTime +
				", orderQuantityTypeCode='" + orderQuantityTypeCode + '\'' +
				", expectedWeeklyMovement=" + expectedWeeklyMovement +
				", whseMaxShipQuantityNumber=" + whseMaxShipQuantityNumber +
				", unitFactor1=" + unitFactor1 +
				", unitFactor2=" + unitFactor2 +
				", variableWeight=" + variableWeight +
				", catchWeight=" + catchWeight +
				", averageWeight=" + averageWeight +
				", whseTie=" + whseTie +
				", whseTier=" + whseTier +
				", flowTypeCode='" + flowTypeCode + '\'' +
				", createdOn=" + createdOn +
				", createdBy='" + createdBy + '\'' +
				", lastUpdatedOn=" + lastUpdatedOn +
				", billCost=" + billCost +
				", averageCost=" + averageCost +
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
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		WarehouseLocationItemAudit that = (WarehouseLocationItemAudit) o;
		return distributionReserveInventory == that.distributionReserveInventory &&
				offsiteInventory == that.offsiteInventory &&
				totalOnHandInventory == that.totalOnHandInventory &&
				onHoldInventory == that.onHoldInventory &&
				billableInventory == that.billableInventory &&
				shipPack == that.shipPack &&
				Double.compare(that.shipLength, shipLength) == 0 &&
				Double.compare(that.shipWidth, shipWidth) == 0 &&
				Double.compare(that.shipHeight, shipHeight) == 0 &&
				Double.compare(that.shipWeight, shipWeight) == 0 &&
				Double.compare(that.shipCube, shipCube) == 0 &&
				variableWeight == that.variableWeight &&
				catchWeight == that.catchWeight &&
				Objects.equals(key, that.key) &&
				Objects.equals(action, that.action) &&
				Objects.equals(changedBy, that.changedBy) &&
				Objects.equals(purchasingStatus, that.purchasingStatus) &&
				Objects.equals(purchaseStatusUpdateTime, that.purchaseStatusUpdateTime) &&
				Objects.equals(purchaseStatusUserId, that.purchaseStatusUserId) &&
				Objects.equals(discontinueDate, that.discontinueDate) &&
				Objects.equals(discontinuedByUID, that.discontinuedByUID) &&
				Objects.equals(supplierStatusCode, that.supplierStatusCode) &&
				Objects.equals(supplierStatusUpdateTime, that.supplierStatusUpdateTime) &&
				Objects.equals(orderQuantityTypeCode, that.orderQuantityTypeCode) &&
				Objects.equals(expectedWeeklyMovement, that.expectedWeeklyMovement) &&
				Objects.equals(whseMaxShipQuantityNumber, that.whseMaxShipQuantityNumber) &&
				Objects.equals(unitFactor1, that.unitFactor1) &&
				Objects.equals(unitFactor2, that.unitFactor2) &&
				Objects.equals(averageWeight, that.averageWeight) &&
				Objects.equals(whseTie, that.whseTie) &&
				Objects.equals(whseTier, that.whseTier) &&
				Objects.equals(flowTypeCode, that.flowTypeCode) &&
				Objects.equals(createdOn, that.createdOn) &&
				Objects.equals(createdBy, that.createdBy) &&
				Objects.equals(lastUpdatedOn, that.lastUpdatedOn) &&
				Objects.equals(billCost, that.billCost) &&
				Objects.equals(averageCost, that.averageCost) &&
				Objects.equals(comment, that.comment);
	}

	/**
	 * Returns a hash code for this object. If two objects are equal, they have the same hash. If they are not equal,
	 * they have different hash codes.
	 *
	 * @return The hash code for this obejct.
	 */
	@Override
	public int hashCode() {

		return Objects.hash(key, action, changedBy, purchasingStatus, purchaseStatusUpdateTime, purchaseStatusUserId, distributionReserveInventory, offsiteInventory, totalOnHandInventory, onHoldInventory, billableInventory, discontinueDate, discontinuedByUID, shipPack, shipLength, shipWidth, shipHeight, shipWeight, shipCube, supplierStatusCode, supplierStatusUpdateTime, orderQuantityTypeCode, expectedWeeklyMovement, whseMaxShipQuantityNumber, unitFactor1, unitFactor2, variableWeight, catchWeight, averageWeight, whseTie, whseTier, /*currentSlotNumber,*/ flowTypeCode, createdOn, createdBy, lastUpdatedOn, billCost, averageCost, comment);
	}

	@Override
	public String getAction() {
		return action;
	}

	/**
	 * Sets the action for this audit.
	 *
	 * @param action the action
	 */
	@Override
	public void setAction(String action) {
		this.action = action;
	}

	/**
	 * Returns changed by. The changed by shows who was doing the action that is being audited. This is the uid(login)
	 * that a user has.
	 *
	 * @return changedBy
	 */
	@Override
	public String getChangedBy() {
		return changedBy;
	}

	/**
	 * Sets the changedBy
	 *
	 * @param changedBy The changedBy
	 */
	@Override
	public void setChangedBy(String changedBy) {
		this.changedBy = changedBy;
	}

	/**
	 * Gets the changed on time. This is when the modification was done.
	 *
	 * @return The time the modification was done.
	 */
	@Override
	public LocalDateTime getChangedOn() {
		return this.key.getChangedOn();
	}

	/**
	 * Sets the changed on time.
	 *
	 * @param changedOn The time the modification was done.
	 */
	public void setChangedOn(LocalDateTime changedOn) {
		this.key.setChangedOn(changedOn);
	}
}
