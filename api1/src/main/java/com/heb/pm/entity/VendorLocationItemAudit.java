package com.heb.pm.entity;

import com.heb.util.audit.Audit;
import com.heb.util.audit.AuditableField;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Represents an audit for a vendor location item.
 *
 * @author m314029
 * @since 2.7.0
 */
@Entity
@Table(name = "vend_loc_itm_aud")
public class VendorLocationItemAudit implements Serializable, Audit {

	private static final long serialVersionUID = 1L;
	private static final String DISPLAY_NAME_DESCRIPTION_WITH_ID = "%s[%s]";

	@EmbeddedId
	private VendorLocationItemAuditKey key;

	@Column(name = "act_cd")
	private String action;

	@AuditableField(displayName = "Distribution Container ID")
	@Column(name = "dc_id")
	private Integer distributionContainerId;

	@AuditableField(displayName = "Inventory Sequence Number")
	@Column(name = "inv_seq_nbr")
	private Integer inventorySequenceNumber;

	@AuditableField(displayName = "Order Lead Time")
	@Column(name = "ord_lead_tm")
	private LocalDateTime orderLeadTime;

	@AuditableField(displayName = "Order Processing Time")
	@Column(name = "ord_procng_ctof_tm")
	private LocalDateTime orderProcessingTime;

	@AuditableField(displayName = "Supply Chain Analyst", codeTableDisplayNameMethod = "getSupplyChainAnalystDisplayName")
	@Column(name = "sca_cd")
	private String supplyChainAnalystId;

	@AuditableField(displayName = "Master Length")
	@Column(name = "vend_ln")
	private Double length;

	@AuditableField(displayName = "Master Width")
	@Column(name = "vend_wd")
	private Double width;

	@AuditableField(displayName = "Master Height")
	@Column(name = "vend_ht")
	private Double height;

	@AuditableField(displayName = "Master Weight")
	@Column(name = "vend_wt")
	private Double weight;

	@AuditableField(displayName = "Master Cube")
	@Column(name = "vend_cu")
	private Double cube;

	@AuditableField(displayName = "Vendor Tie")
	@Column(name = "vend_pal_tie")
	private Integer tie;

	@AuditableField(displayName = "Vendor Tier")
	@Column(name = "vend_pal_tier")
	private Integer tier;

	@AuditableField(displayName = "Vendor Pallet Quantity")
	@Column(name = "vend_pal_qty")
	private Integer palletQuantity;

	@AuditableField(displayName = "Vendor Pallet Size")
	@Column(name = "vend_pal_sz")
	private char palletSize;

	@AuditableField(displayName = "List Cost")
	@Column(name = "vend_list_cst")
	private Double listCost;

	@AuditableField(displayName = "Master Pack")
	@Column(name = "vend_pk_qty")
	private Integer packQuantity;

	@AuditableField(displayName = "Order Quantity Restriction", codeTableDisplayNameMethod = "getOrderQuantityRestrictionDisplayName")
	@Column(name = "ord_qty_rstr_cd")
	private String orderQuantityRestrictionCode;

	@AuditableField(displayName = "Country of Origin", codeTableDisplayNameMethod = "getCountryOfOriginDisplayName")
	@Column(name = "cntry_of_orig_id")
	private Integer countryOfOriginId;

	@AuditableField(displayName = "Cost Owner", codeTableDisplayNameMethod = "getCostOwnerDisplayName")
	@Column(name = "cst_own_id")
	private Integer costOwnerId;

	@AuditableField(displayName = "Cost Link Id")
	@Column(name = "cst_lnk_id")
	private Integer costLinkId;

	@AuditableField(displayName = "Net Cube")
	@Column(name = "vend_nest_cu")
	private Double nestCube;

	@AuditableField(displayName = "Net Max Qty")
	@Column(name = "vend_nest_max")
	private Integer nestMax;

	@AuditableField(displayName = "Vendor Product Code")
	@Column(name = "vend_itm_id")
	private String vendorItemId;

	@Column(name = "lst_updt_uid")
	private String changedBy;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumns({
			@JoinColumn(name="cntry_of_orig_id", referencedColumnName = "cntry_id", insertable = false, updatable = false, nullable = false)
	})
	private Country country;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumns({
			@JoinColumn(name="cst_own_id", referencedColumnName = "cst_own_id", insertable = false, updatable = false, nullable = false)
	})
	private CostOwner costOwner;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumns({
			@JoinColumn(name="sca_cd", referencedColumnName = "sca_cd", insertable = false, updatable = false, nullable = false)
	})
	private Sca supplyChainAnalyst;

	/**
	 * The enum Order quantity restriction.
	 */
	public enum OrderQuantityRestriction {
		N("N", "NO-RESTRICTIONS"),
		P("P", "VENDOR-PALLET"),
		T("T", "VEN-TIE"),
		W("W", "WAREHOUSE-PALLET");

		private String id;
		private String description;

		OrderQuantityRestriction(String id, String description) {
			this.id = id;
			this.description = description;
		}

		/**
		 * Gets display name.
		 *
		 * @return the display name
		 */
		public String getDisplayName() {
			return String.format(VendorLocationItemAudit.DISPLAY_NAME_DESCRIPTION_WITH_ID, this.id, this.description);
		}

		/**
		 * Gets id.
		 *
		 * @return the id
		 */
		public String getId() {
			return this.id;
		}

		/**
		 * Gets description.
		 *
		 * @return the description
		 */
		public String getDescription() {
			return this.description;
		}

		// default order quantity restriction
		private static final OrderQuantityRestriction DEFAULT = OrderQuantityRestriction.N;

		/**
		 * Finds order quantity restriction by id. If order quantity restriction matching the id given is found,
		 * return that order quantity restriction. Else if the given id is null or the order quantity restriction is
		 * not found, return the default OrderQuantityRestriction.N
		 *
		 * @param id The id to search for.
		 * @return The order quantity restriction matching the id searched for, or the default if not found.
		 */
		public static OrderQuantityRestriction getById(String id){

			if(id != null) {
				id = id.trim();
				for (OrderQuantityRestriction orderQuantityRestriction : OrderQuantityRestriction.values()) {
					if (orderQuantityRestriction.getId().equalsIgnoreCase(id)) {
						return orderQuantityRestriction;
					}
				}
			}
			return OrderQuantityRestriction.DEFAULT;
		}
	}

	/**
	 * Gets key.
	 *
	 * @return the key
	 */
	public VendorLocationItemAuditKey getKey() {
		return key;
	}

	/**
	 * Sets key.
	 *
	 * @param key the key
	 */
	public void setKey(VendorLocationItemAuditKey key) {
		this.key = key;
	}

	/**
	 * Gets distribution container id.
	 *
	 * @return the distribution container id
	 */
	public Integer getDistributionContainerId() {
		return distributionContainerId;
	}

	/**
	 * Sets distribution container id.
	 *
	 * @param distributionContainerId the distribution container id
	 */
	public void setDistributionContainerId(Integer distributionContainerId) {
		this.distributionContainerId = distributionContainerId;
	}

	/**
	 * Gets inventory sequence number.
	 *
	 * @return the inventory sequence number
	 */
	public Integer getInventorySequenceNumber() {
		return inventorySequenceNumber;
	}

	/**
	 * Sets inventory sequence number.
	 *
	 * @param inventorySequenceNumber the inventory sequence number
	 */
	public void setInventorySequenceNumber(Integer inventorySequenceNumber) {
		this.inventorySequenceNumber = inventorySequenceNumber;
	}

	/**
	 * Gets order lead time.
	 *
	 * @return the order lead time
	 */
	public LocalDateTime getOrderLeadTime() {
		return orderLeadTime;
	}

	/**
	 * Sets order lead time.
	 *
	 * @param orderLeadTime the order lead time
	 */
	public void setOrderLeadTime(LocalDateTime orderLeadTime) {
		this.orderLeadTime = orderLeadTime;
	}

	/**
	 * Gets order processing time.
	 *
	 * @return the order processing time
	 */
	public LocalDateTime getOrderProcessingTime() {
		return orderProcessingTime;
	}

	/**
	 * Sets order processing time.
	 *
	 * @param orderProcessingTime the order processing time
	 */
	public void setOrderProcessingTime(LocalDateTime orderProcessingTime) {
		this.orderProcessingTime = orderProcessingTime;
	}

	/**
	 * Gets supply chain analyst id.
	 *
	 * @return the supply chain analyst id
	 */
	public String getSupplyChainAnalystId() {
		return supplyChainAnalystId;
	}

	/**
	 * Sets supply chain analyst id.
	 *
	 * @param supplyChainAnalystId the supply chain analyst id
	 */
	public void setSupplyChainAnalystId(String supplyChainAnalystId) {
		this.supplyChainAnalystId = supplyChainAnalystId;
	}

	/**
	 * Gets length.
	 *
	 * @return the length
	 */
	public Double getLength() {
		return length;
	}

	/**
	 * Sets length.
	 *
	 * @param length the length
	 */
	public void setLength(Double length) {
		this.length = length;
	}

	/**
	 * Gets width.
	 *
	 * @return the width
	 */
	public Double getWidth() {
		return width;
	}

	/**
	 * Sets width.
	 *
	 * @param width the width
	 */
	public void setWidth(Double width) {
		this.width = width;
	}

	/**
	 * Gets height.
	 *
	 * @return the height
	 */
	public Double getHeight() {
		return height;
	}

	/**
	 * Sets height.
	 *
	 * @param height the height
	 */
	public void setHeight(Double height) {
		this.height = height;
	}

	/**
	 * Gets weight.
	 *
	 * @return the weight
	 */
	public Double getWeight() {
		return weight;
	}

	/**
	 * Sets weight.
	 *
	 * @param weight the weight
	 */
	public void setWeight(Double weight) {
		this.weight = weight;
	}

	/**
	 * Gets cube.
	 *
	 * @return the cube
	 */
	public Double getCube() {
		return cube;
	}

	/**
	 * Sets cube.
	 *
	 * @param cube the cube
	 */
	public void setCube(Double cube) {
		this.cube = cube;
	}

	/**
	 * Gets tie.
	 *
	 * @return the tie
	 */
	public Integer getTie() {
		return tie;
	}

	/**
	 * Sets tie.
	 *
	 * @param tie the tie
	 */
	public void setTie(Integer tie) {
		this.tie = tie;
	}

	/**
	 * Gets tier.
	 *
	 * @return the tier
	 */
	public Integer getTier() {
		return tier;
	}

	/**
	 * Sets tier.
	 *
	 * @param tier the tier
	 */
	public void setTier(Integer tier) {
		this.tier = tier;
	}

	/**
	 * Gets pallet quantity.
	 *
	 * @return the pallet quantity
	 */
	public Integer getPalletQuantity() {
		return palletQuantity;
	}

	/**
	 * Sets pallet quantity.
	 *
	 * @param palletQuantity the pallet quantity
	 */
	public void setPalletQuantity(Integer palletQuantity) {
		this.palletQuantity = palletQuantity;
	}

	/**
	 * Gets pallet size.
	 *
	 * @return the pallet size
	 */
	public char getPalletSize() {
		return palletSize;
	}

	/**
	 * Sets pallet size.
	 *
	 * @param palletSize the pallet size
	 */
	public void setPalletSize(char palletSize) {
		this.palletSize = palletSize;
	}

	/**
	 * Gets list cost.
	 *
	 * @return the list cost
	 */
	public Double getListCost() {
		return listCost;
	}

	/**
	 * Sets list cost.
	 *
	 * @param listCost the list cost
	 */
	public void setListCost(Double listCost) {
		this.listCost = listCost;
	}

	/**
	 * Gets pack quantity.
	 *
	 * @return the pack quantity
	 */
	public Integer getPackQuantity() {
		return packQuantity;
	}

	/**
	 * Sets pack quantity.
	 *
	 * @param packQuantity the pack quantity
	 */
	public void setPackQuantity(Integer packQuantity) {
		this.packQuantity = packQuantity;
	}

	/**
	 * Gets order quantity restriction code.
	 *
	 * @return the order quantity restriction code
	 */
	public String getOrderQuantityRestrictionCode() {
		return orderQuantityRestrictionCode;
	}

	/**
	 * Sets order quantity restriction code.
	 *
	 * @param orderQuantityRestrictionCode the order quantity restriction code
	 */
	public void setOrderQuantityRestrictionCode(String orderQuantityRestrictionCode) {
		this.orderQuantityRestrictionCode = orderQuantityRestrictionCode;
	}

	/**
	 * Gets country of origin id.
	 *
	 * @return the country of origin id
	 */
	public Integer getCountryOfOriginId() {
		return countryOfOriginId;
	}

	/**
	 * Sets country of origin id.
	 *
	 * @param countryOfOriginId the country of origin id
	 */
	public void setCountryOfOriginId(Integer countryOfOriginId) {
		this.countryOfOriginId = countryOfOriginId;
	}

	/**
	 * Gets cost owner id.
	 *
	 * @return the cost owner id
	 */
	public Integer getCostOwnerId() {
		return costOwnerId;
	}

	/**
	 * Sets cost owner id.
	 *
	 * @param costOwnerId the cost owner id
	 */
	public void setCostOwnerId(Integer costOwnerId) {
		this.costOwnerId = costOwnerId;
	}

	/**
	 * Gets cost link id.
	 *
	 * @return the cost link id
	 */
	public Integer getCostLinkId() {
		return costLinkId;
	}

	/**
	 * Sets cost link id.
	 *
	 * @param costLinkId the cost link id
	 */
	public void setCostLinkId(Integer costLinkId) {
		this.costLinkId = costLinkId;
	}

	/**
	 * Gets nest cube.
	 *
	 * @return the nest cube
	 */
	public Double getNestCube() {
		return nestCube;
	}

	/**
	 * Sets nest cube.
	 *
	 * @param nestCube the nest cube
	 */
	public void setNestCube(Double nestCube) {
		this.nestCube = nestCube;
	}

	/**
	 * Gets nest max.
	 *
	 * @return the nest max
	 */
	public Integer getNestMax() {
		return nestMax;
	}

	/**
	 * Sets nest max.
	 *
	 * @param nestMax the nest max
	 */
	public void setNestMax(Integer nestMax) {
		this.nestMax = nestMax;
	}

	/**
	 * Gets vendor item id.
	 *
	 * @return the vendor item id
	 */
	public String getVendorItemId() {
		return vendorItemId;
	}

	/**
	 * Sets vendor item id.
	 *
	 * @param vendorItemId the vendor item id
	 */
	public void setVendorItemId(String vendorItemId) {
		this.vendorItemId = vendorItemId;
	}

	/**
	 * Gets country.
	 *
	 * @return the country
	 */
	public Country getCountry() {
		return country;
	}

	/**
	 * Sets country.
	 *
	 * @param country the country
	 */
	public void setCountry(Country country) {
		this.country = country;
	}

	/**
	 * Gets cost owner.
	 *
	 * @return the cost owner
	 */
	public CostOwner getCostOwner() {
		return costOwner;
	}

	/**
	 * Sets cost owner.
	 *
	 * @param costOwner the cost owner
	 */
	public void setCostOwner(CostOwner costOwner) {
		this.costOwner = costOwner;
	}

	/**
	 * Gets supply chain analyst.
	 *
	 * @return the supply chain analyst
	 */
	public Sca getSupplyChainAnalyst() {
		return supplyChainAnalyst;
	}

	/**
	 * Sets supply chain analyst.
	 *
	 * @param supplyChainAnalyst the supply chain analyst
	 */
	public void setSupplyChainAnalyst(Sca supplyChainAnalyst) {
		this.supplyChainAnalyst = supplyChainAnalyst;
	}

	/**
	 * Method to call to retrieve the display name for an order quantity restriction code.
	 *
	 * @return Display name for an order quantity restriction.
	 */
	public String getOrderQuantityRestrictionDisplayName() {
		return OrderQuantityRestriction.getById(this.getOrderQuantityRestrictionCode()).getDisplayName();
	}

	/**
	 * Method to call to retrieve the display name for a supply chain analyst id. If the supply chain analyst exists,
	 * return it's display name. Else return 'NOT FOUND[id]' display name.
	 *
	 * @return Display name for a supply chain analyst.
	 */
	public String getSupplyChainAnalystDisplayName(){
		Sca supplyChainAnalyst = this.getSupplyChainAnalyst();
		if(supplyChainAnalyst != null){
			return supplyChainAnalyst.getDisplayName();
		} else {
			return String.format(Audit.DISPLAY_NAME_FORMAT_FOR_CODE_TABLE_NOT_FOUND, this.getSupplyChainAnalystId().trim());
		}
	}

	/**
	 * Method to call to retrieve the display name for a country of origin id. If the country of origin exists,
	 * return it's display name. Else return 'NOT FOUND[id]' display name.
	 *
	 * @return Display name for a country of origin code.
	 */
	public String getCountryOfOriginDisplayName(){
		Country country = this.getCountry();
		if(country != null){
			return country.getDisplayName();
		} else {
			return String.format(Audit.DISPLAY_NAME_FORMAT_FOR_CODE_TABLE_NOT_FOUND, this.getCountryOfOriginId().toString());
		}
	}

	/**
	 * Method to call to retrieve the display name for a cost owner id. If the cost owner exists, return it's display
	 * name. Else return 'NOT FOUND[id]' display name.
	 *
	 * @return Display name for a cost owner code.
	 */
	public String getCostOwnerDisplayName(){
		CostOwner costOwner = this.getCostOwner();
		if(costOwner != null){
			return costOwner.getDisplayName();
		} else {
			return String.format(Audit.DISPLAY_NAME_FORMAT_FOR_CODE_TABLE_NOT_FOUND, this.getCostOwnerId().toString());
		}
	}

	/**
	 * Returns the action for this audit (i.e. 'ADD', 'UPDAT', 'PURGE').
	 *
	 * @return action
	 */
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

		VendorLocationItemAudit that = (VendorLocationItemAudit) o;

		return key != null ? key.equals(that.key) : that.key == null;
	}

	/**
	 * Returns a hash for this object. If two objects are equal, they will have the same hash. If they are not,
	 * they will (probably) have different hashes.
	 *
	 * @return The hash code for this object.
	 */
	@Override
	public int hashCode() {
		return key != null ? key.hashCode() : 0;
	}

	/**
	 * Returns a String representation of the object.
	 *
	 * @return A String representation of the object.
	 */
	@Override
	public String toString() {
		return "VendorLocationItemAudit{" +
				"key=" + key +
				", action='" + action + '\'' +
				", distributionContainerId=" + distributionContainerId +
				", inventorySequenceNumber=" + inventorySequenceNumber +
				", orderLeadTime=" + orderLeadTime +
				", orderProcessingTime=" + orderProcessingTime +
				", supplyChainAnalystId='" + supplyChainAnalystId + '\'' +
				", length=" + length +
				", width=" + width +
				", height=" + height +
				", weight=" + weight +
				", cube=" + cube +
				", tie=" + tie +
				", tier=" + tier +
				", palletQuantity=" + palletQuantity +
				", palletSize=" + palletSize +
				", listCost=" + listCost +
				", packQuantity=" + packQuantity +
				", orderQuantityRestrictionCode='" + orderQuantityRestrictionCode + '\'' +
				", countryOfOriginId=" + countryOfOriginId +
				", costOwnerId=" + costOwnerId +
				", costLinkId=" + costLinkId +
				", nestCube=" + nestCube +
				", nestMax=" + nestMax +
				", vendorItemId='" + vendorItemId + '\'' +
				", changedBy='" + changedBy + '\'' +
				'}';
	}
}
