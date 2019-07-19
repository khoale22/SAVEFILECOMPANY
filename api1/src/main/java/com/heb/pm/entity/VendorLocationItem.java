/*
 * VendorLocationItem
 *
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import java.io.Serializable;
import java.util.List;

/**
 * Represents a vendor location item.
 *
 * @author m314029
 * @since 2.2.0
 */
@Entity
@Table(name = "vend_loc_itm")
//dB2Oracle changes vn00907 starts
@TypeDefs({
		@TypeDef(name = "fixedLengthChar", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharType.class),
		@TypeDef(name = "fixedLengthCharPK", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharTypePK.class)
})
//dB2Oracle changes vn00907
public class VendorLocationItem implements Serializable{

	private static final long serialVersionUID = 1L;
	public static final String ACTIVE_STATUS = "A";
	private static final String DISPLAY_NAME_FORMAT = "%s [%d]";

	@EmbeddedId
	private VendorLocationItemKey key;

	@JsonIgnoreProperties({"vendorLocationItems", "primaryUpc", "warehouseLocationItemExtendedAttributes",
			"warehouseLocationItems","itemNotDeleted", "vendorLocationItemMaster"})
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumns({
			@JoinColumn(name="itm_id", referencedColumnName = "itm_id", insertable = false, updatable = false, nullable = false),
			@JoinColumn(name="itm_key_typ_cd", referencedColumnName = "itm_key_typ_cd", insertable = false, updatable = false, nullable = false)
	})
	private ItemMaster itemMaster;

	@JsonIgnoreProperties("vendorLocationItem")
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "vendorLocationItem")
	private List<ItemWarehouseVendor> itemWarehouseVendorList;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumns({
			@JoinColumn(name="vend_loc_typ_cd", referencedColumnName = "loc_typ_cd", insertable = false, updatable = false, nullable = false),
			@JoinColumn(name="vend_loc_nbr", referencedColumnName = "loc_nbr", insertable = false, updatable = false, nullable = false)
	})
	private Location location;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumns({
			@JoinColumn(name="sca_cd", referencedColumnName = "sca_cd", insertable = false, updatable = false, nullable = false)
	})
	private Sca sca;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumns({
			@JoinColumn(name="cst_own_id", referencedColumnName = "cst_own_id", insertable = false, updatable = false, nullable = false)
	})
	private CostOwner costOwner;

	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumns({
			@JoinColumn(name="itm_key_typ_cd", referencedColumnName = "itm_key_typ_cd", insertable = false, updatable = false, nullable = false),
			@JoinColumn(name="itm_id", referencedColumnName = "itm_id", insertable = false, updatable = false, nullable = false),
			@JoinColumn(name="vend_loc_nbr", referencedColumnName = "loc_nbr", insertable = false, updatable = false, nullable = false),
			@JoinColumn(name="vend_loc_typ_cd", referencedColumnName = "loc_typ_cd", insertable = false, updatable = false, nullable = false)
	})
	private VendorLocationItemMaster vendorLocationItemMaster;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumns({
			@JoinColumn(name="vend_loc_nbr", referencedColumnName = "loc_nbr", insertable = false, updatable = false, nullable = false),
			@JoinColumn(name="vend_loc_typ_cd", referencedColumnName = "loc_typ_cd", insertable = false, updatable = false, nullable = false)
	})
	private VendorLocation vendorLocation;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumns({
			@JoinColumn(name="vend_loc_nbr", referencedColumnName = "loc_nbr", insertable = false, updatable = false, nullable = false),
			@JoinColumn(name="vend_loc_typ_cd", referencedColumnName = "loc_typ_cd", insertable = false, updatable = false, nullable = false)
	})
	private DsdLocation dsdLocation;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumns({
			@JoinColumn(name="cntry_of_orig_id", referencedColumnName = "cntry_id", insertable = false, updatable = false, nullable = false)
	})
	private Country country;

	@Column(name = "vend_ln")
	private Double length;

	@Column(name = "vend_wd")
	private Double width;

	@Column(name = "vend_ht")
	private Double height;

	@Column(name = "vend_wt")
	private Double weight;

	@Column(name = "vend_cu")
	private Double cube;

	@Column(name = "vend_pal_tie")
	private Integer tie;

	@Column(name = "vend_pal_tier")
	private Integer tier;

	@Column(name = "vend_pal_qty")
	private Integer palletQuantity;

	@Column(name = "vend_pal_sz")
	private String palletSize;

	@Column(name = "vend_list_cst")
	private Double listCost;

	@Column(name = "vend_pk_qty")
	private Integer packQuantity;

	@Column(name = "ord_qty_rstr_cd")
	//db2o changes  vn00907
	@Type(type="fixedLengthChar")
	private String orderQuantityRestrictionCode;

	@Column(name = "sca_cd")
	//db2o changes  vn00907
	@Type(type="fixedLengthChar")
	private String scaCode;

	@Column(name = "cntry_of_orig_id")
	private Integer countryOfOriginId;

	@Column(name = "cst_own_id")
	private Integer costOwnerId;

	@Column(name = "cst_lnk_id")
	private Integer costLinkId;

	@Column(name = "vend_nest_cu")
	private Double nestCube;

	@Column(name = "vend_nest_max")
	private Integer nestMax;

	@Column(name = "vend_itm_id")
	//db2o changes  vn00907
	@Type(type="fixedLengthChar")
	private String vendItemId;

	@Column(name="LST_UPDT_UID")
	private String lstUpdtUid;

	//bi-directional many-to-one association to VendItmStr
	@JsonIgnoreProperties("vendorLocationItem")
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "vendorLocationItem")
	private List<VendorItemStore> vendorItemStores;

	@Transient
	private Cost cost;

	/**
	 * Gets the vendor location item key.
	 *
	 * @return the vendor location item key
	 */
	public VendorLocationItemKey getKey() {
		return key;
	}

	/**
	 * Sets the vendor location item key.
	 *
	 * @param key the vendor location item key
	 */
	public void setKey(VendorLocationItemKey key) {
		this.key = key;
	}

	/**
	 * Returns the ItemWarehouseVendor
	 *
	 * @return ItemWarehouseVendor
	 */
	public List<ItemWarehouseVendor> getItemWarehouseVendorList() {
		return itemWarehouseVendorList;
	}

	/**
	 * Sets the ItemWarehouseVendor
	 *
	 * @param itemWarehouseVendorList The ItemWarehouseVendor
	 */
	public void setItemWarehouseVendorList(List<ItemWarehouseVendor> itemWarehouseVendorList) {
		this.itemWarehouseVendorList = itemWarehouseVendorList;
	}

	/**
	 * Gets the location.
	 *
	 * @return the location
	 */
	public Location getLocation() {
		return location;
	}

	/**
	 * Sets the location.
	 *
	 * @param location the location
	 */
	public void setLocation(Location location) {
		this.location = location;
	}


	/**
	 * Returns the Length
	 *
	 * @return Length
	 */
	public Double getLength() {
		return length;
	}

	/**
	 * Sets the Length
	 *
	 * @param length The Length
	 */

	public void setLength(Double length) {
		this.length = length;
	}

	/**
	 * Returns the Width
	 *
	 * @return Width
	 */
	public Double getWidth() {
		return width;
	}

	/**
	 * Sets the Width
	 *
	 * @param width The Width
	 */

	public void setWidth(Double width) {
		this.width = width;
	}

	/**
	 * Returns the Height
	 *
	 * @return Height
	 */
	public Double getHeight() {
		return height;
	}

	/**
	 * Sets the Height
	 *
	 * @param height The Height
	 */

	public void setHeight(Double height) {
		this.height = height;
	}

	/**
	 * Returns the Weight
	 *
	 * @return Weight
	 */
	public Double getWeight() {
		return weight;
	}

	/**
	 * Sets the Weight
	 *
	 * @param weight The Weight
	 */

	public void setWeight(Double weight) {
		this.weight = weight;
	}

	/**
	 * Returns the Cube
	 *
	 * @return Cube
	 */
	public Double getCube() {
		return cube;
	}

	/**
	 * Sets the Cube
	 *
	 * @param cube The Cube
	 */

	public void setCube(Double cube) {
		this.cube = cube;
	}

	/**
	 * Returns the Tie
	 *
	 * @return Tie
	 */
	public Integer getTie() {
		return tie;
	}

	/**
	 * Sets the Tie
	 *
	 * @param tie The Tie
	 */

	public void setTie(Integer tie) {
		this.tie = tie;
	}

	/**
	 * Returns the Tier
	 *
	 * @return Tier
	 */
	public Integer getTier() {
		return tier;
	}

	/**
	 * Sets the Tier
	 *
	 * @param tier The Tier
	 */

	public void setTier(Integer tier) {
		this.tier = tier;
	}

	/**
	 * Returns the PalletQuantity
	 *
	 * @return PalletQuantity
	 */
	public Integer getPalletQuantity() {
		return palletQuantity;
	}

	/**
	 * Sets the PalletQuantity
	 *
	 * @param palletQuantity The PalletQuantity
	 */

	public void setPalletQuantity(Integer palletQuantity) {
		this.palletQuantity = palletQuantity;
	}

	/**
	 * Returns the PalletSize
	 *
	 * @return PalletSize
	 */
	public String getPalletSize() {
		return palletSize;
	}

	/**
	 * Sets the PalletSize
	 *
	 * @param palletSize The PalletSize
	 */

	public void setPalletSize(String palletSize) {
		this.palletSize = palletSize;
	}

	/**
	 * Returns the ListCost
	 *
	 * @return ListCost
	 */
	public double getListCost() {
		return listCost;
	}

	/**
	 * Sets the ListCost
	 *
	 * @param listCost The ListCost
	 */

	public void setListCost(double listCost) {
		this.listCost = listCost;
	}

	/**
	 * Returns the PackQuantity
	 *
	 * @return PackQuantity
	 */
	public Integer getPackQuantity() {
		return packQuantity;
	}

	/**
	 * Sets the PackQuantity
	 *
	 * @param packQuantity The PackQuantity
	 */

	public void setPackQuantity(Integer packQuantity) {
		this.packQuantity = packQuantity;
	}

	/**
	 * Returns the OrderQuantityRestrictionCode
	 *
	 * @return OrderQuantityRestrictionCode
	 */
	public String getOrderQuantityRestrictionCode() {
		return orderQuantityRestrictionCode;
	}

	/**
	 * Sets the OrderQuantityRestrictionCode
	 *
	 * @param orderQuantityRestrictionCode The OrderQuantityRestrictionCode
	 */

	public void setOrderQuantityRestrictionCode(String orderQuantityRestrictionCode) {
		this.orderQuantityRestrictionCode = orderQuantityRestrictionCode;
	}

	/**
	 * Returns the ScaCode
	 *
	 * @return ScaCode
	 */
	public String getScaCode() {
		return scaCode;
	}

	/**
	 * Sets the ScaCode
	 *
	 * @param scaCode The ScaCode
	 */

	public void setScaCode(String scaCode) {
		this.scaCode = scaCode;
	}

	/**
	 * Returns the CountryOfOriginId
	 *
	 * @return CountryOfOriginId
	 */
	public Integer getCountryOfOriginId() {
		return countryOfOriginId;
	}

	/**
	 * Sets the CountryOfOriginId
	 *
	 * @param countryOfOriginId The CountryOfOriginId
	 */

	public void setCountryOfOriginId(Integer countryOfOriginId) {
		this.countryOfOriginId = countryOfOriginId;
	}

	/**
	 * Returns the CostOwnerId
	 *
	 * @return CostOwnerId
	 */
	public Integer getCostOwnerId() {
		return costOwnerId;
	}

	/**
	 * Sets the CostOwnerId
	 *
	 * @param costOwnerId The CostOwnerId
	 */

	public void setCostOwnerId(Integer costOwnerId) {
		this.costOwnerId = costOwnerId;
	}

	/**
	 * Returns the CostLinkId
	 *
	 * @return CostLinkId
	 */
	public Integer getCostLinkId() {
		return costLinkId;
	}

	/**
	 * Sets the CostLinkId
	 *
	 * @param costLinkId The CostLinkId
	 */

	public void setCostLinkId(Integer costLinkId) {
		this.costLinkId = costLinkId;
	}

	/**
	 * Returns the NetCube
	 *
	 * @return NetCube
	 */
	public Double getNestCube() {
		return nestCube;
	}

	/**
	 * Sets the NetCube
	 *
	 * @param nestCube The NetCube
	 */

	public void setNestCube(double nestCube) {
		this.nestCube = nestCube;
	}

	/**
	 * Returns the NetMax
	 *
	 * @return NetMax
	 */
	public Integer getNestMax() {
		return nestMax;
	}

	/**
	 * Sets the NetMax
	 *
	 * @param nestMax The NetMax
	 */

	public void setNestMax(Integer nestMax) {
		this.nestMax = nestMax;
	}

	/**
	 * Returns the ItemMaster
	 *
	 * @return ItemMaster
	 */
	public ItemMaster getItemMaster() {
		return itemMaster;
	}

	/**
	 * Sets the ItemMaster
	 *
	 * @param itemMaster The ItemMaster
	 */

	public void setItemMaster(ItemMaster itemMaster) {
		this.itemMaster = itemMaster;
	}

	/**
	 * Returns the VendItemId
	 *
	 * @return VendItemId
	 */
	public String getVendItemId() {
		return vendItemId;
	}

	/**
	 * Sets the VendItemId
	 *
	 * @param vendItemId The VendItemId
	 */

	public void setVendItemId(String vendItemId) {
		this.vendItemId = vendItemId;
	}

	/**
	 * Get display name string.
	 *
	 * @return the string
	 */
	public String getDisplayBicepVendorName(){

		return String.format(VendorLocationItem.DISPLAY_NAME_FORMAT, this.getLocation().getLocationName().trim(),
				this.key.getVendorNumber());
	}

	/**
	 * Get display name string.
	 *
	 * @return the string
	 */
	public String getDisplayApVendorName() {
		return String.format(VendorLocationItem.DISPLAY_NAME_FORMAT, this.getLocation().getLocationName().trim(),
				this.getLocation().getApVendorNumber());
	}

	/**
	 * Get display name string.
	 *
	 * @return the string
	 */
	public String getDisplayApLocationApVendorName() {
		try {
		        return this.getLocation().getApLocation().getDisplayName();
		} catch(NullPointerException e) {
			return StringUtils.EMPTY;
		}
	}

	/**
	 * Returns the cost for this item from this vendor to this location.
	 *
	 * @return The cost for this item from this vendor to this location.
	 */
	public Cost getCost() {
		return this.cost;
	}

	/**
	 * Sets the cost for this item from this vendor to this location.
	 *
	 * @param cost The cost for this item from this vendor to this location.
	 */
	public void setCost(Cost cost) {
		this.cost = cost;
	}

	/**
	 * Returns the Sca
	 *
	 * @return Sca
	 **/
	public Sca getSca() {
		return sca;
	}

	/**
	 * Sets the Sca
	 *
	 * @param sca The Sca
	 **/

	public void setSca(Sca sca) {
		this.sca = sca;
	}

	/**
	 * Returns the CostOwner
	 *
	 * @return CostOwner
	 **/
	public CostOwner getCostOwner() {
		return costOwner;
	}

	/**
	 * Sets the CostOwner
	 *
	 * @param costOwner The CostOwner
	 **/

	public void setCostOwner(CostOwner costOwner) {
		this.costOwner = costOwner;
	}

	/**
	 * Returns the VendorLocationItemMaster
	 *
	 * @return VendorLocationItemMaster
	 */
	public VendorLocationItemMaster getVendorLocationItemMaster() {
		return vendorLocationItemMaster;
	}

	/**
	 * Sets the VendorLocationItemMaster
	 *
	 * @param vendorLocationItemMaster The VendorLocationItemMaster
	 */

	public void setVendorLocationItemMaster(VendorLocationItemMaster vendorLocationItemMaster) {
		this.vendorLocationItemMaster = vendorLocationItemMaster;
	}

	/**
	 * Returns the VendorLocation. The vendor location represents a location from which a vendor ships products.
	 *
	 * @return VendorLocation
	 **/
	public VendorLocation getVendorLocation() {
		return vendorLocation;
	}

	/**
	 * Sets the VendorLocation
	 *
	 * @param vendorLocation The VendorLocation
	 **/
	public void setVendorLocation(VendorLocation vendorLocation) {
		this.vendorLocation = vendorLocation;
	}

	/**
	 * Returns the DsdLocation. The dsd location represents from whom we purchase DSD products.
	 *
	 * @return DsdLocation
	 **/
	public DsdLocation getDsdLocation() {
		return dsdLocation;
	}

	/**
	 * Sets the DsdLocation
	 *
	 * @param dsdLocation The DsdLocation
	 **/
	public void setDsdLocation(DsdLocation dsdLocation) {
		this.dsdLocation = dsdLocation;
	}

	/**
	 * Returns the Country
	 *
	 * @return Country
	 */
	public Country getCountry() {
		return country;
	}

	/**
	 * Sets the Country
	 *
	 * @param country The Country
	 */

	public void setCountry(Country country) {
		this.country = country;
	}
	/**
	 * Returns the vendorItemStores
	 *
	 * @return vendorItemStores
	 */
	public List<VendorItemStore> getVendorItemStores() {
		return vendorItemStores;
	}
	/**
	 * Sets the vendorItemStores
	 *
	 * @param vendorItemStores The vendorItemStores
	 */
	public void setVendorItemStores(List<VendorItemStore> vendorItemStores) {
		this.vendorItemStores = vendorItemStores;
	}
	/**
	 * Returns the lstUpdtUid
	 *
	 * @return lstUpdtUid
	 */
	public String getLstUpdtUid() {
		return this.lstUpdtUid;
	}
	/**
	 * Sets the lstUpdtUid
	 *
	 * @param lstUpdtUid The lstUpdtUid
	 */
	public void setLstUpdtUid(String lstUpdtUid) {
		this.lstUpdtUid = lstUpdtUid;
	}
	/**
	 * Returns a String representation of the object.
	 *
	 * @return A String representation of the object.
	 */
	@Override
	public String toString() {
		return "VendorLocationItem{" +
				"key=" + key +
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
				", scaCode='" + scaCode + '\'' +
				", countryOfOriginId=" + countryOfOriginId +
				", costOwnerId=" + costOwnerId +
				", costLinkId=" + costLinkId +
				", nestCube=" + nestCube +
				", nestMax=" + nestMax +
				", vendItemId='" + vendItemId + '\'' +
				'}';
	}

	/**
	 * Compares another object to this one. If that object is a VendorLocationItem, it uses they keys
	 * to determine if they are equal and ignores non-key values for the comparison.
	 *
	 * @param o The object to compare to.
	 * @return True if they are equal and false otherwise.
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		VendorLocationItem that = (VendorLocationItem) o;

		return this.key != null ? this.key.equals(that.key) : that.key == null;
	}

	/**
	 * Returns a hash code for this object. If two objects are equal, they have the same hash. If they are not equal,
	 * they have different hash codes.
	 *
	 * @return The hash code for this obejct.
	 */
	@Override
	public int hashCode() {
		return this.key != null ? this.key.hashCode() : 0;
	}
}
