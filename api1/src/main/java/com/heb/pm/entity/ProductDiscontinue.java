/*
 * com.heb.pm.entity.ProductDiscontinue
 *
 * Copyright (c) 2016 HEB
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of HEB.
 */

package com.heb.pm.entity;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;
import org.springframework.data.domain.Sort;

import javax.persistence.*;
import java.io.Serializable;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;

/**
 * Holds the parameters that indicate if a product can be discontinued or not.
 *
 * @author d116773
 * @since 2.0.0
 */
@Entity
@Table(name="prod_del")
//dB2Oracle changes vn00907 starts
@TypeDefs({
@TypeDef(name = "fixedLengthChar", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharType.class),
@TypeDef(name = "fixedLengthCharPK", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharTypePK.class)
})
//dB2Oracle changes vn00907
public class ProductDiscontinue implements Serializable {

	private static final long serialVersionUID = 1L;

	// The number of days a warehouse item can be in a discontinued state before it is projected to be deleted.
	private static final long TTL_DISCONTINUED_WAREHOUSE = 21;

	// The number of days a DSD item can be in a discontinued state before it is projected to be deleted.
	private static final long TTL_DISCONTINUED_DSD = 21;

	// The day of the week the warehouse delete job runs.
	private static final DayOfWeek WAREHOUSE_DELETE_JOB_DAY = DayOfWeek.SUNDAY;

	@EmbeddedId
	private ProductDiscontinueKey key;

	@Column(name = "sals_rcrd_sw") 
	private Boolean salesSet;

	@Column(name="inven_sw") 
	private Boolean inventorySet;

	@Column(name="str_rcpt_sw")
	private Boolean storeReceiptsSet;

	@Column(name="lst_recd_dt") 
	private LocalDate lastReceivedDate;

	@Column(name="new_prod_itm_sw")
	private Boolean newItemSet;

	@Column(name="open_po_sw")
	private Boolean openPoSet;

	@Column(name="dscon_mst_dta_sw")
	private Boolean autoDiscontinued;

	@Column(name="cre8_id")
	@Type(type="fixedLengthChar")  
	private String createId;

	@Column(name="cre8_ts")
	private LocalDateTime createTime;

	@Column(name="lst_updt_uid")
	@Type(type="fixedLengthChar")  
	private String lastUpdateId;

	@Column(name="lst_updt_ts")
	private LocalDateTime lastUpdateTime;

	@Column(name="whse_inven_sw")
	private Boolean warehouseInventorySet;

	@Column(name = "lst_bil_dt")
	private LocalDate lastBillDate;

	@Column(name = "vend_inven_sw")
	private Boolean vendorInventory;

	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumns({
			@JoinColumn(name="itm_id", referencedColumnName = "itm_id", insertable = false, updatable = false, nullable = false),
			@JoinColumn(name="itm_key_typ_cd", referencedColumnName = "itm_key_typ_cd", insertable = false, updatable = false, nullable = false)
	})
	private ItemMaster itemMaster;

	@NotFound(action = NotFoundAction.IGNORE)
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="scn_cd_id", insertable = false, updatable = false, nullable = false)
	private SellingUnit sellingUnit;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="prod_id", referencedColumnName = "prod_id", insertable = false, updatable = false)
	private ProductMaster productMaster;

	/**
	 * Returns the date on which this item is projected to be deleted. If the projected date is unknown (the item
	 * is not discontinued), then the function returns null.
	 *
	 * @return The date on which this item is projected to be deleted.
	 */
	public LocalDate getProjectedDeleteDate() {

		if (this.getDiscontinued() == null || !this.getDiscontinued()) {
			return null;
		}

		if (this.key.getItemType().equals(ItemMasterKey.WAREHOUSE)) {
			return this.calculateWarehouseDeleteDate();
		} else {
			return this.calculateDsdDeleteDate();
		}
	}

	/**
	 * Returns the key for the ProductDiscontinue object.
	 *
	 * @return The key for the ProductDiscontinue object.
	 */
	public ProductDiscontinueKey getKey() {
		return this.key;
	}

	/**
	 * Sets the key for the ProductDiscontinue object.
	 *
	 * @param key They key for the ProductDiscontinue object.
	 */
	public void setKey(ProductDiscontinueKey key) {
		this.key = key;
	}

	/**
	 * Returns whether or not this product can be discontinued based on sales.
	 *
	 * @return True if it can be discontinued based on sales and false if it cannot.
	 */
	public Boolean getSalesSet() {
		return salesSet;
	}

	/**
	 * Sets whether or not this product can be discontinued based on sales.
	 *
	 * @param salesSet True if it can be discontinued based on sales and false if it cannot.
	 */
	public void setSalesSet(Boolean salesSet) {
		this.salesSet = salesSet;
	}

	/**
	 * Returns whether or not this product can be discontinued based on store inventory.
	 *
	 * @return True if it can be discontinued based on store inventory and false if it cannot.
	 */
	public Boolean getInventorySet() {
		return inventorySet;
	}

	/**
	 * Sets whether or not this product can be discontinued based on store inventory.
	 *
	 * @param inventorySet True if it can be discontinued based on store inventory and false if it cannot.
	 */
	public void setInventorySet(Boolean inventorySet) {
		this.inventorySet = inventorySet;
	}

	/**
	 * Returns whether or not this product can be discontinued based on store receipts.
	 *
	 * @return True if it can be discontinued based on store receipts and false if it cannot.
	 */
	public Boolean getStoreReceiptsSet() {
		return storeReceiptsSet;
	}

	/**
	 * Sets whether or not this product can be discontinued based on store receipts.
	 *
	 * @param storeReceiptsSet True if it can be discontinued based on store receipts and false if it cannot.
	 */
	public void setStoreReceiptsSet(Boolean storeReceiptsSet) {
		this.storeReceiptsSet = storeReceiptsSet;
	}

	/**
	 * Returns the last time this item was received.
	 *
	 * @return The last time this item was received.
	 */
	public LocalDate getLastReceivedDate() {
		return lastReceivedDate;
	}

	/**
	 * Sets the last time this item was received.
	 *
	 * @param lastReceivedDate The last time this item was received.
	 */
	public void setLastReceivedDate(LocalDate lastReceivedDate) {
		this.lastReceivedDate = lastReceivedDate;
	}

	/**
	 * Returns whether or not this product can be discontinued based on how new the item is.
	 *
	 * @return True if it can be discontinued based on how new the item is and false if it cannot.
	 */
	public Boolean getNewItemSet() {
		return newItemSet;
	}

	/**
	 * Sets whether or not this product can be discontinued based on how new the item is.
	 *
	 * @param newItemSet True if it can be discontinued based on how new the item is and false if it cannot.
	 */
	public void setNewItemSet(Boolean newItemSet) {
		this.newItemSet = newItemSet;
	}

	/**
	 * Returns whether or not this product can be discontinued based on whether or not it has open POs.
	 *
	 * @return True if it can be discontinued based on whether or not it has open POs and false if it cannot.
	 */
	public Boolean getOpenPoSet() {
		return openPoSet;
	}

	/**
	 * Sets whether or not this product can be discontinued based on whether or not it has open POs.
	 *
	 * @param openPoSet True if it can be discontinued based on whether or not it has open POs and false if it cannot.
	 */
	public void setOpenPoSet(Boolean openPoSet) {
		this.openPoSet = openPoSet;
	}

	/**
	 * Returns the ID of the person or system who created this record.
	 *
	 * @return The ID of the person or system who created this record.
	 */
	public String getCreateId() {
		return createId;
	}

	/**
	 * Returns whether or not this item was discontinued by the automated process.
	 *
	 * @return True if this item was discontinued by the automated process and false otherwise.
	 */
	public Boolean getAutoDiscontinued() {
		return autoDiscontinued;
	}

	/**
	 * Sets whether or not this item was discontinued by the automated process.
	 *
	 * @param autoDiscontinued True if this item was discontinued by the automated process and false otherwise.
	 */
	public void setAutoDiscontinued(Boolean autoDiscontinued) {
		this.autoDiscontinued = autoDiscontinued;
	}

	/**
	 * Returns whether or not the item has been discontinued by the discontinue process.
	 *
	 * @return True if the item has been discontinued and False otherwise.
	 */
	public Boolean getDiscontinued() {
		if (this.itemMaster != null) {
			return this.itemMaster.isDiscontinued();
		}
		return null;
	}

	/**
	 * Sets the ID of the person or system who created this record.
	 *
	 * @param createId The ID of the person or system who created this record.
	 */
	public void setCreateId(String createId) {
		this.createId = createId;
	}

	/**
	 * Returns the time this record was created.
	 *
	 * @return The time this record was created.
	 */
	public LocalDateTime getCreateTime() {
		return createTime;
	}

	/**
	 * Sets the time this record was created.
	 *
	 * @param createTime The time this record was created.
	 */
	public void setCreateTime(LocalDateTime createTime) {
		this.createTime = createTime;
	}

	/**
	 * Returns the ID of the person or system who last updated this record.
	 *
	 * @return The ID of the person or system who last updated this record.
	 */
	public String getLastUpdateId() {
		return lastUpdateId;
	}

	/**
	 * Sets the ID of the person or system who last updated this record.
	 *
	 * @param lastUpdateId The ID of the person or system who last updated this record.
	 */
	public void setLastUpdateId(String lastUpdateId) {
		this.lastUpdateId = lastUpdateId;
	}

	/**
	 * Returns the time this record was last updated.
	 *
	 * @return The time this record was last updated.
	 */
	public LocalDateTime getLastUpdateTime() {
		return lastUpdateTime;
	}

	/**
	 * Sets the time this record was last updated.
	 *
	 * @param lastUpdateTime The time this record was last updated.
	 */
	public void setLastUpdateTime(LocalDateTime lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	/**
	 * Returns whether or not this product can be discontinued based on whether or not it has warehouse inventory.
	 *
	 * @return True if it can be discontinued based on whether or not it has warehouse inventory and false if it cannot.
	 */
	public Boolean getWarehouseInventorySet() {
		return warehouseInventorySet;
	}

	/**
	 * Sets whether or not this product can be discontinued based on whether or not it has warehouse inventory.
	 *
	 * @param warehouseInventorySet True if it can be discontinued based on whether or not it has warehouse inventory and false if it cannot.
	 */
	public void setWarehouseInventorySet(Boolean warehouseInventorySet) {
		this.warehouseInventorySet = warehouseInventorySet;
	}

	/**
	 * Returns the last time this item billed.
	 *
	 * @return The last time this item billed.
	 */
	public LocalDate getLastBillDate() {
		return lastBillDate;
	}

	/**
	 * Sets the last time this item billed.
	 *
	 * @param lastBillDate The last time this item billed.
	 */
	public void setLastBillDate(LocalDate lastBillDate) {
		this.lastBillDate = lastBillDate;
	}

	/**
	 * Returns information about this record from the item_master table.
	 *
	 * @return Information about this record from the item_master table.
	 */
	public ItemMaster getItemMaster() {
		return itemMaster;
	}

	/**
	 * Sets the information about this record from the item_master table.
	 *
	 * @param itemMaster Information about this record from the item_master table.
	 */
	public void setItemMaster(ItemMaster itemMaster) {
		this.itemMaster = itemMaster;
	}

	/**
	 * Returns details about this records selling unit.
	 *
	 * @return Details about this records selling unit.
	 */
	public SellingUnit getSellingUnit() {
		return sellingUnit;
	}

	/**
	 * Sets details about this records selling unit.
	 *
	 * @param sellingUnit Details about this records selling unit.
	 */
	public void setSellingUnit(SellingUnit sellingUnit) {
		this.sellingUnit = sellingUnit;
	}

	/**
	 * Returns details about this records product master.
	 *
	 * @return Details about this records master.
	 */
	public ProductMaster getProductMaster() {
		return productMaster;
	}

	/**
	 * Sets details about this records product master.
	 *
	 * @param productMaster Details about this records product master.
	 */
	public void setProductMaster(ProductMaster productMaster) {
		this.productMaster = productMaster;
	}

	/**
	 * Returns whether or not an external vendor (DSV) has inventory.
	 *
	 * @return True if the vendor has inventory and false otherwise.
	 */
	public Boolean getVendorInventory() {
		return vendorInventory;
	}

	/**
	 * Sets whether or not an external vendor (DSV) has inventory.
	 *
	 * @param vendorInventory True if the vendor has inventory and false otherwise.
	 */
	public void setVendorInventory(Boolean vendorInventory) {
		this.vendorInventory = vendorInventory;
	}

	/**
	 * Returns the item type to be displayed on the front end.
	 *
	 * @return the item type to be displayed on the front end.
	 */
	public String getDisplayItemType(){
		if(this.getKey().getItemType().trim().equals(ProductDiscontinueKey.WAREHOUSE)){
			return "Warehouse";
		} else {
			return this.getKey().getItemType().trim();
		}
	}

	/**
	 * Returns a printable representation of the object.
	 *
	 * @return A printable representation of the object.
	 */
	@Override
	public String toString() {
		return "ProductDiscontinue{" +
				"key=" + key +
				", salesSet=" + salesSet +
				", inventorySet=" + inventorySet +
				", storeReceiptsSet=" + storeReceiptsSet +
				", lastReceivedDate=" + lastReceivedDate +
				", newItemSet=" + newItemSet +
				", openPoSet=" + openPoSet +
				", createId='" + createId + '\'' +
				", createTime=" + createTime +
				", lastUpdateId='" + lastUpdateId + '\'' +
				", lastUpdateTime=" + lastUpdateTime +
				", warehouseInventorySet=" + warehouseInventorySet +
				", lastBillDate=" + lastBillDate +
				'}';
	}

	/**
	 * Compares another object to this one. This is a deep compare of the keys of the object. Values  of non-key
	 * fields are ignored.
	 *
	 * @param o The object to compare to.
	 * @return True if they are equal and false otherwise.
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof ProductDiscontinue)) return false;

		ProductDiscontinue that = (ProductDiscontinue) o;

		if (this.key != null ? !this.key.equals(that.key) : that.key != null) return false;

		return true;
	}

	/**
	 * Returns a hash for this object. If two objects are equal, they will have the same hash. If they
	 * are not, they will have different hashes. The hash is based on the key only.
	 *
	 * @return The hash code for this object.
	 */
	@Override
	public int hashCode() {
		return this.key != null ? key.hashCode() : 0;
	}


	/**
	 * Returns a sort order for prod_del not based on the keys. This is bascially a way to group things
	 * that have all the flags set to Y together. It has a secondary sort within that group based on
	 * item code and then upc.
	 *
	 * @param direction The direction you want the fields to sort in.
	 * @return A sort based on all the flag fields.
	 */
	public static Sort getSortByAllColumns(Sort.Direction direction) {
		return new Sort(
				new Sort.Order(direction, "salesSet"),
				new Sort.Order(direction, "inventorySet"),
				new Sort.Order(direction, "storeReceiptsSet"),
				new Sort.Order(direction, "newItemSet"),
				new Sort.Order(direction, "openPoSet"),
				new Sort.Order(direction, "warehouseInventorySet"),
				new Sort.Order(direction, "key.itemCode"),
				new Sort.Order(direction, "key.upc")
		);
	}

	/**
	 * Calculates a projected delete date for this item if it is warehouse item. This function assumes the check for
	 * warehouse is already done.
	 *
	 * @return A projected delete date for this item.
	 */
	private LocalDate calculateWarehouseDeleteDate() {

		LocalDateTime discontinueDate = this.itemMaster.getNormalizedDiscontinueDate();

		// Add 21 days and take the Friday after.
		return discontinueDate == null ? null : discontinueDate.plusDays(ProductDiscontinue.TTL_DISCONTINUED_WAREHOUSE)
				.with(TemporalAdjusters.next(ProductDiscontinue.WAREHOUSE_DELETE_JOB_DAY)).toLocalDate();
	}

	/**
	 * Calculates a projected delete date for this item if it a DSD item. This function assumes the check for
	 * DSD is already done.
	 *
	 * @return A projected delete date for this item.
	 */
	private LocalDate calculateDsdDeleteDate() {

		LocalDateTime discontinueDate = this.itemMaster.getNormalizedDiscontinueDate();

		return discontinueDate == null ? null :
				this.getItemMaster().getDiscontinueDate().plusDays(ProductDiscontinue.TTL_DISCONTINUED_DSD);
	}
}
