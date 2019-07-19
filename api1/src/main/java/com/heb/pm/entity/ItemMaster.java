/*
 * com.heb.pm.entity.ItemMaster
 *
 * Copyright (c) 2016 HEB
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of HEB.
 */

package com.heb.pm.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Top level data about ship packs.
 *
 * @author d116773
 * @since 2.0.0
 */
@Entity
@Table(name="item_master")
//dB2Oracle changes vn00907 starts
@TypeDefs({
		@TypeDef(name = "fixedLengthChar", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharType.class),
		@TypeDef(name = "fixedLengthCharPK", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharTypePK.class)
})
//dB2Oracle changes vn00907
public class ItemMaster implements Serializable {

	private static final long serialVersionUID = 2L;

	private static final String UNKNOWN_DISCONTINUED_BY_UID = "Unknown";
	public static final String ITEM_TYPE_CODE = "ITMCD";
	private static final String DISPLAY_READY_PALLET_CODE="7";
	private static final String DISPLAY_READY_PALLET_READABLE="Display Ready Pallet";
	private static final String RETAIL_READY_PACKAGING_CODE="9";
	private static final String RETAIL_READY_PACKAGING_READABLE="Retail Ready Packaging";
	private static final String DEFAULT_DRU_TYPE = " ";
	public static final String VARIANT_CODE_UNDEFINE = "UNDEF";
	public static final String VARIANT_CODE_KNOWN = "KNOWN";
	public static final String VARIANT_CODE_EMPTY = "";
	public enum DisplayReadyUnitType{

		DisplayReadyPallet (DISPLAY_READY_PALLET_CODE, DISPLAY_READY_PALLET_READABLE),
		RetailReadyPpackaging (RETAIL_READY_PACKAGING_CODE, RETAIL_READY_PACKAGING_READABLE),
		DefaultDRU (DEFAULT_DRU_TYPE, DEFAULT_DRU_TYPE);

		private String code;
		private String displayName;


		DisplayReadyUnitType(String code, String displayName){
			this.code = code;
			this.displayName=displayName;
		}

		public String getCode(){
			return this.code;
		}
		public String getDisplayName(){
			return this.displayName;
		}

		public static DisplayReadyUnitType convertStringToDRUType(String value){
			if(value.equals(DISPLAY_READY_PALLET_CODE) || value.equals(DISPLAY_READY_PALLET_READABLE)){
				return DisplayReadyPallet;
			} else if(value.equals(RETAIL_READY_PACKAGING_CODE) || value.equals(RETAIL_READY_PACKAGING_READABLE)) {
				return RetailReadyPpackaging;
			} else{
				return DefaultDRU;
			}
		}
	}
	@EmbeddedId
	private ItemMasterKey key;

	@Column(name = "item_des")
	//db2o changes  vn00907
	@Type(type="fixedLengthChar")
	private String description;

	@Column(name = "dscon_dt")
	private LocalDate discontinueDate;

	@Column(name = "heb_itm_pk")
	private long pack;

	@Column(name = "ordering_upc")
	private long orderingUpc;

	@Column(name = "dscon_usr_id")
	//db2o changes  vn00907
	@Type(type="fixedLengthChar")
	private String discontinuedByUID;

	@Column(name = "added_dt")
	private LocalDate addedDate;

	@Column(name = "item_size_txt")
	//db2o changes  vn00907
	@Type(type="fixedLengthChar")
	private String itemSize;

	@Column(name = "case_upc")
	private Long caseUpc;

	@Column(name = "mrt_sw")
	private boolean mrt;

	@Column(name = "sw_cd")
	//db2o changes  vn00907
	@Type(type="fixedLengthChar")
	private String dsv;

	@Column(name = "dsply_rdy_pal_sw")
	private boolean displayReadyUnit;

	@Column(name = "srs_aff_typ_cd")
	private String typeOfDRU;

	@Column(name = "std_subst_logic_sw")
	private boolean alwaysSubWhenOut;

	@Column(name = "prod_fcng_nbr")
	private long rowsFacing;

	@Column(name = "prod_row_deep_nbr")
	private long rowsDeep;

	@Column(name = "prod_row_hi_nbr")
	private long rowsHigh;

	@Column(name = "nbr_of_orint_nbr")
	private long orientation;

	@Column(name = "itm_typ_cd")
	private String itemTypeCode;

	@Column(name = "one_touch_typ_cd")
	private String oneTouchTypeCode;

	@Column(name = "dscon_rsn_cd")
	private String discontinueReasonCode;

	@Column(name = "sub_dept_id_1")
	private String subDepartmentIdOne;

	@Column(name = "sub_dept_id_2")
	private String subDepartmentIdTwo;

	@Column(name = "sub_dept_id_3")
	private String subDepartmentIdThree;

	@Column(name = "sub_dept_id_4")
	private String subDepartmentIdFour;

	@Column(name = "dept_id_1")
	private Integer departmentIdOne;

	@Column(name = "dept_id_2")
	private Integer departmentIdTwo;

	@Column(name = "dept_id_3")
	private Integer departmentIdThree;

	@Column(name = "dept_id_4")
	private Integer departmentIdFour;

	@Column(name = "pss_dept_1")
	private Integer pssDepartmentCodeOne;

	@Column(name = "pss_dept_2")
	private Integer pssDepartmentCodeTwo;

	@Column(name = "pss_dept_3")
	private Integer pssDepartmentCodeThree;

	@Column(name = "pss_dept_4")
	private Integer pssDepartmentCodeFour;

	@Column(name = "dept_mdse_typ_1")
	private Character merchandiseTypeCodeOne;

	@Column(name = "dept_mdse_typ_2")
	private Character merchandiseTypeCodeTwo;

	@Column(name = "dept_mdse_typ_3")
	private Character merchandiseTypeCodeThree;

	@Column(name = "dept_mdse_typ_4")
	private Character merchandiseTypeCodeFour;


	@Column(name="ABC_ITM_CAT_NO")
	private Integer abcItemCategoryNo;

	@Column(name="ABC_AUTH_CD")
	private String abcAuthorizationCode;

	@Column(name="USDA_NBR")
	private Integer usdaNumber;

	@Column(name="CRIT_ITM_IND")
	private String criticalItemIndicator;

	@Column(name="MAX_SHIP_QTY")
	private Integer maxShipQty;

	@Column(name="VAR_WT_SW")
	private Boolean variableWeight;

	@Column(name="CATTLE_SW")
	private Boolean cattle;

	@Column(name="CTCH_WT_SW")
	private Boolean catchWeight;

	@Column(name="MEX_AUTH_CD")
	private String mexicoAuthorizationCode;

	@Column(name="MEX_BRDR_AUTH_CD")
	private String mexicanBorderAuthorizationCode;

	@Column(name="NEW_ITM_SW")
	private Boolean newItem;

	@Column(name="ADDED_USR_ID")
	private String addedUsrId;

	@Column(name="LST_UPDT_TS")
	private LocalDateTime lastUpdateDate;

	@Column(name="LST_UPDT_USR_ID")
	private String lastUpdateUserId;

	@Column(name="ITM_SZ_QTY")
	private Double itemSizeQuantity;

	@Column(name="ITM_SZ_UOM_CD")
	private String itemSizeUom;

	@Column(name="PD_OMI_COM_CD")
	private Integer commodityCode;

	@Column(name="PD_OMI_SUB_COM_CD")
	private Integer subCommodityCode;

	@Column(name="PD_OMI_COM_CLS_CD")
	private Short classCode;

	@Column(name="GUARN_TO_STR_DD")
	private Integer guaranteeToStoreDays;

	@Column(name="WHSE_REACTION_DD")
	private Integer warehouseReactionDays;

	@Column(name="ON_RCPT_LIF_DD")
	private Integer onReceiptLifeDays;

	@Column(name="VARIANT_CD")
	private String variantCode;

	@Column(name = "ADDED_USR_ID", insertable = false, updatable = false)
	public String displayCreatedName;

	@Column(name="ADDED_DT", insertable = false, updatable = false)
	private LocalDateTime createdDateTime;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "dept_mdse_typ_1", referencedColumnName = "mdse_typ_cd", insertable = false, updatable = false, nullable = false)
	private MerchandiseType merchandiseTypeOne;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "dept_mdse_typ_2", referencedColumnName = "mdse_typ_cd", insertable = false, updatable = false, nullable = false)
	private MerchandiseType merchandiseTypeTwo;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "dept_mdse_typ_3", referencedColumnName = "mdse_typ_cd", insertable = false, updatable = false, nullable = false)
	private MerchandiseType merchandiseTypeThree;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "dept_mdse_typ_4", referencedColumnName = "mdse_typ_cd", insertable = false, updatable = false, nullable = false)
	private MerchandiseType merchandiseTypeFour;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "dscon_rsn_cd", referencedColumnName = "dscon_rsn_cd", insertable = false, updatable = false, nullable = false)
	private DiscontinueReason discontinueReason;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "itm_typ_cd", referencedColumnName = "itm_typ_cd", insertable = false, updatable = false, nullable = false)
	private ItemType itemType;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "one_touch_typ_cd", referencedColumnName = "one_touch_typ_cd", insertable = false, updatable = false, nullable = false)
	private OneTouchType oneTouchType;

	@Transient
	private Retail retail;

	@Transient
	private Boolean reActive;

	@Transient
	private Boolean isDiscontinueAction;

	@JsonIgnoreProperties("itemMaster")
	@OneToMany(mappedBy = "itemMaster", fetch = FetchType.LAZY)
	private List<WarehouseLocationItem> warehouseLocationItems;

	@JsonIgnoreProperties("itemMaster")
	@OneToMany(mappedBy = "itemMaster", fetch = FetchType.LAZY)
	private List<ProdItem> prodItems;

	@NotFound(action = NotFoundAction.IGNORE)
	@JsonIgnoreProperties("itemMasters")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="ordering_upc", referencedColumnName = "pd_upc_no", insertable = false, updatable = false)
	private PrimaryUpc primaryUpc;

	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumns({
			@JoinColumn(name = "itm_key_typ_cd", referencedColumnName = "itm_key_typ_cd", insertable = false, updatable = false),
			@JoinColumn(name = "itm_id", referencedColumnName = "itm_id", insertable = false,  updatable = false)
	})
	private List<WarehouseLocationItemExtendedAttributes> warehouseLocationItemExtendedAttributes;

	/**
	 * Collection of item not deleted reasons.
	 */
	@JsonManagedReference
	@OneToMany(mappedBy = "itemMaster", fetch = FetchType.LAZY)
	private List<ItemNotDeleted> itemNotDeleted;

	@JsonIgnoreProperties("itemMaster")
	@OneToMany(mappedBy = "itemMaster", fetch = FetchType.LAZY)
	private List<VendorLocationItem> vendorLocationItems;

	@JsonIgnoreProperties("itemMasterByRelatedItemCode")
	@OneToMany(mappedBy = "itemMasterByRelatedItemCode", fetch = FetchType.LAZY)
	private List<RelatedItem> relatedItems;

	@Transient
	private boolean dsdEdcStatus;

	// These are required to support dynamic search capability and is not used outside that. Therefore, there are no
	// getters or setters on this.

	@OneToMany(fetch = FetchType.LAZY)
	@NotFound(action = NotFoundAction.IGNORE)
	@JoinColumn(name = "scn_cd_id", referencedColumnName = "case_upc", insertable = false, updatable = false)
	private List<SearchCriteria> searchCriteria;

	/**
	 * Returns discontinue action from ui. This flag will be define discontinue first action.
	 * If an item is not discontinued, then user discontinue the item. this flag will turn true.
	 * If an item is discontinued, then update date or reason discontinue. this flag will turn false.
	 * @return
	 */
	public Boolean getDiscontinueAction() {
		return isDiscontinueAction;
	}

	/**
	 * set discontinue action
	 * @param discontinueAction
	 */
	public void setDiscontinueAction(Boolean discontinueAction) {
		isDiscontinueAction = discontinueAction;
	}

	/**
	 * Returns a discontinue date for this item normalized across warehouse and DSD. If an item is not discontinued,
	 * then the function returns null.
	 *
	 * @return A discontinue date for this item normalized across warehouse and DSD.
	 */
	public LocalDateTime getNormalizedDiscontinueDate() {

		// If the item is not discontinued, return null;
		if (!this.isDiscontinued()) {
			return null;
		}

		if (this.key.isDsd()) {
			// If an item is DSD, the date is on this record.
			return this.discontinueDate.atStartOfDay();
		} else {
			// Find the most recent time for this item being discontinued across all warehouses.
			LocalDateTime mostRecentDateTime = null;

			for (WarehouseLocationItem wli : this.getWarehouseLocationItems()) {
				if (mostRecentDateTime == null || wli.getPurchaseStatusUpdateTime().isAfter(mostRecentDateTime)) {
					mostRecentDateTime = wli.getPurchaseStatusUpdateTime();
				}
			}
			return mostRecentDateTime;
		}
	}

	/**
	 * get  variant code
	 * @return
	 */
	public String getVariantCode() {
		return variantCode;
	}

	/**
	 * set variant code
	 * @param variantCode
	 */
	public void setVariantCode(String variantCode) {
		this.variantCode = variantCode;
	}

	/**
	 * Returns a discontinue by uid for this item normalized across warehouse and DSD. If an item is not discontinued,
	 * then the function returns null.
	 *
	 * @return A discontinue uid for this item normalized across warehouse and DSD.
	 */
	public String getNormalizedDiscontinuedByUID() {

		// If the item is not discontinued, return null;
		if (!this.isDiscontinued()) {
			return null;
		}

		if (this.key.isDsd()) {
			// If an item is DSD, the date is on this record.
			if(StringUtils.isBlank(this.discontinuedByUID)){
				return UNKNOWN_DISCONTINUED_BY_UID;
			}
			return this.discontinuedByUID;
		} else {
			// Find the most recent time for this item being discontinued across all warehouses.
			String whsDiscontinuedByUID = null;
			LocalDateTime mostRecentDateTime = null;

			for (WarehouseLocationItem wli : this.getWarehouseLocationItems()) {
				if (mostRecentDateTime == null || wli.getPurchaseStatusUpdateTime().isAfter(mostRecentDateTime)) {
					mostRecentDateTime = wli.getPurchaseStatusUpdateTime();
					whsDiscontinuedByUID = wli.getPurchaseStatusUserId();
				}
			}
			if(StringUtils.isBlank(whsDiscontinuedByUID)){
				whsDiscontinuedByUID = UNKNOWN_DISCONTINUED_BY_UID;
			}
			return whsDiscontinuedByUID;
		}
	}

	/**
	 * Returns the key for this object.
	 *
	 * @return The key for this object.
	 */
	public ItemMasterKey getKey() {
		return key;
	}

	/**
	 * Sets the key for this object.
	 *
	 * @param key They key for this object.
	 */
	public void setKey(ItemMasterKey key) {
		this.key = key;
	}

	/**
	 * Returns the item's description.
	 *
	 * @return The item's description.
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets the item's description.
	 *
	 * @param description The item's description.
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * For DSD product, returns the date this item was discontinued.
	 *
	 * @return The date this item was disontinued.
	 */
	public LocalDate getDiscontinueDate() {
		return discontinueDate;
	}

	/**
	 * For DSD product, sets the date this item was discontinued.
	 *
	 * @param discontinueDate The date this item was discontinued
	 */
	public void setDiscontinueDate(LocalDate discontinueDate) {
		this.discontinueDate = discontinueDate;
	}

	/**
	 * Returns the WarehouseLocationItem records associated with this item.
	 *
	 * @return The WarehouseLocationItem records associated with this item.
	 */
	public List<WarehouseLocationItem> getWarehouseLocationItems() {
		return warehouseLocationItems;
	}

	/**
	 * Sets the WarehouseLocationItem records associated with this item.
	 *
	 * @param warehouseLocationItems The WarehouseLocationItem records associated with this item.
	 */
	public void setWarehouseLocationItems(List<WarehouseLocationItem> warehouseLocationItems) {
		this.warehouseLocationItems = warehouseLocationItems;
	}
	/**
	 * Returns the WarehouseLocationItemExtendedAttributes object for this object.
	 *
	 * @return The WarehouseLocationItemExtendedAttributes object for this object.
	 */
	public List<WarehouseLocationItemExtendedAttributes> getWarehouseLocationItemExtendedAttributes() {
		return warehouseLocationItemExtendedAttributes;
	}

	/**
	 * Sets the WarehouseLocationItemExtendedAttributes list object for this object.
	 *
	 * @return The WarehouseLocationItemExtendedAttributes list object for this object.
	 */
	public void setWarehouseLocationItemExtendedAttributes(List<WarehouseLocationItemExtendedAttributes> warehouseLocationItemExtendedAttributes) {
		this.warehouseLocationItemExtendedAttributes = warehouseLocationItemExtendedAttributes;
	}
	/**
	 * Returns the item's default pack.
	 *
	 * @return The item's default pack.
	 */
	public long getPack() {
		return pack;
	}

	/**
	 * Sets the item's default pack.
	 *
	 * @param pack The item's default pack.
	 */
	public void setPack(long pack) {
		this.pack = pack;
	}

	/**
	 * Returns the ProdItem records associated with this item.
	 *
	 * @return The ProdItem records associated with this item.
	 */
	public List<ProdItem> getProdItems() {
		return prodItems;
	}

	/**
	 * Sets the prodItems records associated with this item.
	 *
	 * @param prodItems The prodItems records associated with this item.
	 */
	public void setProdItems(List<ProdItem> prodItems) {
		this.prodItems = prodItems;
	}

	/**
	 * Returns the ordering UPC for this item.
	 *
	 * @return The ordering UPC for this item.
	 */
	public long getOrderingUpc() {
		return orderingUpc;
	}

	/**
	 * Sets the ordering UPC for this item.
	 *
	 * @param orderingUpc The ordering UPC for this item.
	 */
	public void setOrderingUpc(long orderingUpc) {
		this.orderingUpc = orderingUpc;
	}

	/**
	 * Returns the primary UPC tied to this item.
	 *
	 * @return The primary UPC tied to this item.
	 */
	public PrimaryUpc getPrimaryUpc() {
		return primaryUpc;
	}

	/**
	 * Sets the primary UPC tied to this item.
	 *
	 * @param primaryUpc The primary UPC tied to this item.
	 */
	public void setPrimaryUpc(PrimaryUpc primaryUpc) {
		this.primaryUpc = primaryUpc;
	}

	/**
	 * Returns the list of all not deleted reasons associated to this item.
	 *
	 * @return The not deleted reasons associated to this item.
	 */
	public List<ItemNotDeleted> getItemNotDeleted() {
		return itemNotDeleted;
	}

	/**
	 * Sets the collection of all not deleted reasons associated to this item.
	 *
	 * @param itemNotDeleted not deleted reasons.
	 */
	public void setItemNotDeleted(List<ItemNotDeleted> itemNotDeleted) {
		this.itemNotDeleted = itemNotDeleted;
	}

	/**
	 * Returns the items added date.
	 *
	 * @return the items added date.
	 */
	public LocalDate getAddedDate() {
		return addedDate;
	}

	/**
	 * Sets the items added date.
	 *
	 * @param addedDate the items added date.
	 */
	public void setAddedDate(LocalDate addedDate) {
		this.addedDate = addedDate;
	}

	/**
	 * Returns the ItemType. The item type describes whether the item is sellable, shipper, or display according to its
	 * itm_typ_cd id.
	 *
	 * @return ItemType
	 */
	public ItemType getItemType() {
		return itemType;
	}

	/**
	 * Sets the ItemType
	 *
	 * @param itemType The ItemType
	 */
	public void setItemType(ItemType itemType) {
		this.itemType = itemType;
	}

	/**
	 * Returns whether or not this item is discontinued. For DSD product, that means the discontinue date
	 * has been set. For warehouse product, that means it is discontinued in all warehouses. If the key is not
	 * set, this will throw an IllegalStateException.
	 *
	 * @return True if the item is discontinued and false otherwise.
	 */
	public boolean isDiscontinued() {

		if (this.getKey() == null) {
			throw new IllegalStateException("key not set for item code");
		}

		if (this.getKey().isDsd()) {
			return this.getDiscontinueDate() != null;
		} else {
			for (WarehouseLocationItem itm : this.getWarehouseLocationItems()) {
				if (!(itm.isDiscontinued())) {
					return false;
				}
			}
			return true;
		}
	}

	/**
	 * If this is a warehouse product, returns the sum of all inventory on distribution reserve across all warehouses.
	 *
	 * @return The sum of all inventory on distribution reserve across all warehouses.
	 */
	public int getDistributionReserveInventory() {

		if (this.warehouseLocationItems == null || this.warehouseLocationItems.isEmpty()) {
			return 0;
		}

		int totalInventory = 0;
		for (WarehouseLocationItem w : this.warehouseLocationItems) {
			totalInventory += w.getDistributionReserveInventory();
		}
		return totalInventory >= 0 ? totalInventory : 0;
	}

	/**
	 * If this is a warehouse product, returns the sum of all off-site inventory across all warehouses.
	 *
	 * @return The sum of all off-site inventory across all warehouses.
	 */
	public int getOffsiteInventory() {

		if (this.warehouseLocationItems == null || this.warehouseLocationItems.isEmpty()) {
			return 0;
		}

		int totalInventory = 0;
		for (WarehouseLocationItem w : this.warehouseLocationItems) {
			totalInventory += w.getOffsiteInventory();
		}
		return totalInventory >= 0 ? totalInventory : 0;
	}

	/**
	 * If this is a warehouse product, returns the sum of all total inventory across all warehouses.
	 *
	 * @return The sum of all total inventory across all warehouses.
	 */
	public int getTotalOnHandInventory() {

		if (this.warehouseLocationItems == null || this.warehouseLocationItems.isEmpty()) {
			return 0;
		}

		int totalInventory = 0;
		for (WarehouseLocationItem w : this.warehouseLocationItems) {
			totalInventory += w.getTotalOnHandInventory();
		}
		return totalInventory >= 0 ? totalInventory : 0;
	}

	/**
	 * If this is a warehouse product, returns all on-hold inventory across all warehouses.
	 *
	 * @return All on-hold inventory across all warehouses.
	 */
	public int getOnHoldInventory() {

		if (this.warehouseLocationItems == null || this.warehouseLocationItems.isEmpty()) {
			return 0;
		}

		int totalInventory = 0;
		for (WarehouseLocationItem w : this.warehouseLocationItems) {
			totalInventory += w.getOnHoldInventory();
		}
		return totalInventory >= 0 ? totalInventory : 0;
	}

	/**
	 * If this is a warehouse product, returns all billable inventory across all warehouses.
	 *
	 * @return All billable inventory across all warehouses.
	 */
	public int getBillableInventory() {

		if (this.warehouseLocationItems == null || this.warehouseLocationItems.isEmpty()) {
			return 0;
		}

		int totalInventory = 0;
		for (WarehouseLocationItem w : this.warehouseLocationItems) {
			totalInventory += w.getBillableInventory();
		}
		return totalInventory >= 0 ? totalInventory : 0;
	}

	/**
	 * If this is a warehouse product, returns the sum of all inventory on order across all warehouses.
	 *
	 * @return The sum of all inventory on order across all warehouses.
	 */
	public int getTotalOnOrderInventory() {

		if (this.warehouseLocationItemExtendedAttributes == null || this.warehouseLocationItemExtendedAttributes.isEmpty()) {
			return 0;
		}

		int totalInventory = 0;
		for (WarehouseLocationItemExtendedAttributes w : this.warehouseLocationItemExtendedAttributes) {
			totalInventory += w.getTotalOnOrder();
		}
		return totalInventory >= 0 ? totalInventory : 0;
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
	 * Returns the item size.
	 *
	 * @return the item size.
	 */
	public String getItemSize() {
		return itemSize;
	}

	/**
	 * Sets the item size.
	 *
	 * @param itemSize the item size.
	 */
	public void setItemSize(String itemSize) {
		this.itemSize = itemSize;
	}

	/**
	 * Returns the case upc.
	 *
	 * @return the case upc
	 */
	public Long getCaseUpc() {
		return caseUpc;
	}

	/**
	 * Sets the case upc.
	 *
	 * @param caseUpc the case upc
	 */
	public void setCaseUpc(Long caseUpc) {
		this.caseUpc = caseUpc;
	}

	/**
	 * Returns whether item is MRT or not.
	 *
	 * @return the mrt value
	 */
	public boolean isMrt() {
		return mrt;
	}

	/**
	 * Sets whether item is MRT or not.
	 *
	 * @param mrt the mrt value
	 */
	public void setMrt(boolean mrt) {
		this.mrt = mrt;
	}

	/**
	 * Gets the vendor location items.
	 *
	 * @return the vendor location items
	 */
	public List<VendorLocationItem> getVendorLocationItems() {
		return vendorLocationItems;
	}

	/**
	 * Sets the vendor location items.
	 *
	 * @param vendorLocationItems the vendor location items
	 */
	public void setVendorLocationItems(List<VendorLocationItem> vendorLocationItems) {
		this.vendorLocationItems = vendorLocationItems;
	}

	/**
	 * Is dsv boolean.
	 *
	 * @return the boolean
	 */
	public String isDsv() {
		return dsv;
	}

	/**
	 * @return Gets the value of reActive and returns reActive
	 */
	public void setReActive(Boolean reActive) {
		this.reActive = reActive;
	}

	/**
	 * Sets the reActive
	 */
	public Boolean getReActive() {
		return reActive;
	}

	/**
	 * Sets is dsv.
	 *
	 * @param isDsv the is dsv
	 */
	public void setDsv(String isDsv) {
		this.dsv = isDsv;
	}

	/**
	 * Returns the retail for the primary UPC associated with this item.
	 *
	 * Note: This is not set by JPA and may or may not be readily available. If it is needed, it needs to be
	 * set by a service. You cannot count on it always being available.
	 *
	 * @return The retail for the primary UPC associated with this item.
	 */
	public Retail getRetail() {
		return this.retail;
	}

	/**
	 * Sets the retail for the primary UPC associated with this item.
	 *
	 * @param retail The retail for the primary UPC associated with this item.
	 */
	public void setRetail(Retail retail) {
		this.retail = retail;
	}

	/**
	 * Returns a flag stating whether or not the current item is a display read unit(DRU)
	 * DRU:    refers to special configuration of packaging & merchandising that allows goods to be moved directly to
	 *         shelf with minimal handling, enabling products to be displayed effectively within the packaging medium.
	 *         Possible values are – Display Ready Pallets, Retail Ready Packaging.
	 * @return the current isDisplayReadyUnit string
	 */
	public boolean getDisplayReadyUnit() {
		return displayReadyUnit;
	}

	/**
	 * Updates the isDisplayReadyUnit flag
	 * @param displayReadyUnit the new displayReadyUnit boolean.
	 */
	public void setDisplayReadyUnit(boolean displayReadyUnit) {
		this.displayReadyUnit = displayReadyUnit;
	}

	/**
	 * Returns a string describing the type of DRU {@link #getDisplayReadyUnit()}  based on a code values, the
	 * accepted values are:
	 * "7"-Display Ready Pallet (DRP)
	 * "9"-Retail Ready Packaging (RRP)
	 * ""-Regular stock items(Default)
	 *
	 * @return the current typeOFDRU code;
	 */
	public String getTypeOfDRU() {
		return typeOfDRU;
	}

	/**
	 * This wil convert the the single character string from typeOfDRU and convert it to a more more descriptive
	 * string
	 *
	 * Display Ready Pallet:	is a system of packaging that allows items to be merchandised directly from the pallet
	 * Retail Ready Packaging:	is a system of packaging and merchandising that allows goods to be moved directly to
	 * 							shelf with minimal handling, enabling products to be displayed effectively within the
	 *							secondary packaging medium .
	 *							The most typical styles of RRP are trays and bins.
	 *" " 						The Default value the item is not a DRU
	 * @return
	 */
	public DisplayReadyUnitType getDisplayReadyUnitType(){
		return DisplayReadyUnitType.convertStringToDRUType(getTypeOfDRU());
	}

	public String getDisplayReadyUnitDisplayName(){
		return getDisplayReadyUnitType().getDisplayName();
	}


	/**
	 * Updates the type of DRU code string
	 * @param typeOfDRU the new type of DRU code string.
	 */
	public void setTypeOfDRU(String typeOfDRU) {
		this.typeOfDRU = typeOfDRU;
	}

	/**
	 * This boolean states if whether or not the item will always be substituted when out flag the
	 * values represent:
	 * true-for regular open stock items (Default)
	 * false-default only if the type of DRU is DRP and RRP {@link #getTypeOfDRU()}
	 * @return
	 */
	public boolean getAlwaysSubWhenOut() {
		return alwaysSubWhenOut;
	}

	/**
	 * Updates the alwaysSubWhenOut flag boolean.
	 * @param alwaysSubWhenOut the new always sub when out flag.
	 */
	public void setAlwaysSubWhenOut(boolean alwaysSubWhenOut) {
		this.alwaysSubWhenOut = alwaysSubWhenOut;
	}

	/**
	 * Returns the facing which is a row of similar products /Stock Keeping Units (SKUs) facing forward. One facing is
	 * equivalent to one row of similar product whatever the size of the product face.
	 * @return the current number of rowFacing.
	 */
	public long getRowsFacing() {
		return rowsFacing;
	}

	/**
	 * updates the rowFacing
	 * @param rowsFacing the new rowFacing value.
	 */
	public void setRowsFacing(long rowsFacing) {
		this.rowsFacing = rowsFacing;
	}

	/**
	 * Returns the number of retail units  within a Display Ready Unit that the shelf depth will hold
	 * @return the current number of rows deep.
	 */
	public long getRowsDeep() {
		return rowsDeep;
	}

	/**
	 * Updates the number of rows deep for an item
	 * @param rowsDeep the new number of rows deep.
	 */
	public void setRowsDeep(long rowsDeep) {
		this.rowsDeep = rowsDeep;
	}

	/**
	 * Returns the number of retail units  within a Display Ready Unit  that can be 'stacked' on top of each other
	 * fitting into the space available.
	 * @return the number of rows high
	 */
	public long getRowsHigh() {
		return rowsHigh;
	}

	/**
	 * Updates the number of rows high for an item
	 * @param rowsHigh the new number of rows high for an item.
	 */
	public void setRowsHigh(long rowsHigh) {
		this.rowsHigh = rowsHigh;
	}

	/**
	 * returns the to the number of possible orientation of DRP /RRP on the Pallet rack /shelf without affecting the
	 * ease of shopping for the customer. There are two acceptable values:
	 * "1"-Default value
	 * "2"-More than one possible orientations.
	 * @return the orientation of the item.
	 */
	public long getOrientation() {
		return orientation;
	}

	/**
	 * updetes the number of possible orientations for an item.
	 * @param orientation the new number of orientations for an item.
	 */
	public void setOrientation(long orientation) {
		this.orientation = orientation;
	}

	/**
	 * Returns the OneTouchTypeCode. The one touch type id that is matched between the code table one_touch_typ code
	 * table to determine whether it is a one touch type(C, H, P...) or not a one touch type('N').
	 *
	 * @return OneTouchTypeCode
	 */
	public String getOneTouchTypeCode() {
		return oneTouchTypeCode;
	}

	/**

	 * Sets the OneTouchTypeCode.
	 *
	 * @param oneTouchTypeCode The OneTouchTypeCode
	 */
	public void setOneTouchTypeCode(String oneTouchTypeCode) {
		this.oneTouchTypeCode = oneTouchTypeCode;
	}

	/**
	 * Returns the ItemTypeCode. The item type code determines whether the item is Sellable or an
	 * ingredient or display or freights.
	 *
	 * @return ItemTypeCode
	 */
	public String getItemTypeCode() {
		return itemTypeCode;
	}
	/**
	 * Sets the ItemTypeCode
	 *
	 * @param itemTypeCode The ItemTypeCode
	 */
	public void setItemTypeCode(String itemTypeCode) {
		this.itemTypeCode = itemTypeCode;
	}

	/**
	 * Returns the OneTouchType. This pulls the one touch type entity according to the id from one_touch_typ_cd.
	 *
	 * @return OneTouchType
	 */
	public OneTouchType getOneTouchType() {
		return oneTouchType;
	}

	/**
	 * Sets the OneTouchType
	 *
	 * @param oneTouchType The OneTouchType
	 */
	public void setOneTouchType(OneTouchType oneTouchType) {
		this.oneTouchType = oneTouchType;
	}

	/**
	 * Returns the DiscontinueReasonCode. The discontinue reason code that will be matched with the "disco_rsn_cd" table
	 * id. This code tells whether it is a seasonal discontinue or a non-std-discontinue....
	 *
	 * @return DiscontinueReasonCode
	 */
	public String getDiscontinueReasonCode() {
		return discontinueReasonCode;
	}

	/**
	 * Sets the DiscontinueReasonCode
	 *
	 * @param discontinueReasonCode The DiscontinueReasonCode
	 */
	public void setDiscontinueReasonCode(String discontinueReasonCode) {
		this.discontinueReasonCode = discontinueReasonCode;
	}

	/**
	 * Returns the DiscontinueReason. The discontinue reason is the reason why they are discontinuing the item.
	 * Seasonal or Non-std-discontinue.
	 *
	 * @return DiscontinueReason
	 */
	public DiscontinueReason getDiscontinueReason() {
		return discontinueReason;
	}

	/**
	 * Sets the DiscontinueReason
	 *
	 * @param discontinueReason The DiscontinueReason
	 */
	public void setDiscontinueReason(DiscontinueReason discontinueReason) {
		this.discontinueReason = discontinueReason;
	}

	/**
	 * Returns the SubDepartmentIdOne. This is the first sub department id used to associate with a pss department.
	 * (Max of 4)
	 *
	 * @return SubDepartmentIdOne
	 */
	public String getSubDepartmentIdOne() {
		return subDepartmentIdOne;
	}

	/**
	 * Sets the SubDepartmentIdOne
	 *
	 * @param subDepartmentIdOne The SubDepartmentIdOne
	 */
	public void setSubDepartmentIdOne(String subDepartmentIdOne) {
		this.subDepartmentIdOne = subDepartmentIdOne;
	}

	/**
	 * Returns the SubDepartmnetIdTwo. This is the second sub department id used to associate with a pss department.
	 * (Max of 4)
	 *
	 * @return SubDepartmnetIdTwo
	 */
	public String getSubDepartmentIdTwo() {
		return subDepartmentIdTwo;
	}

	/**
	 * Sets the SubDepartmnetIdTwo
	 *
	 * @param subDepartmentIdTwo The SubDepartmnetIdTwo
	 */
	public void setSubDepartmentIdTwo(String subDepartmentIdTwo) {
		this.subDepartmentIdTwo = subDepartmentIdTwo;
	}

	/**
	 * Returns the SubDepartmentIdThree. This is the third sub department id used to associate with a pss department.
	 * (Max of 4)
	 *
	 * @return SubDepartmentIdThree
	 */
	public String getSubDepartmentIdThree() {
		return subDepartmentIdThree;
	}

	/**
	 * Sets the SubDepartmentIdThree
	 *
	 * @param subDepartmentIdThree The SubDepartmentIdThree
	 */
	public void setSubDepartmentIdThree(String subDepartmentIdThree) {
		this.subDepartmentIdThree = subDepartmentIdThree;
	}

	/**
	 * Returns the SubDepartmentIdFour. This is the fourth sub department id used to associate with a pss department.
	 * (Max of 4)
	 *
	 * @return SubDepartmentIdFour
	 */
	public String getSubDepartmentIdFour() {
		return subDepartmentIdFour;
	}

	/**
	 * Sets the SubDepartmentIdFour
	 *
	 * @param subDepartmentIdFour The SubDepartmentIdFour
	 */
	public void setSubDepartmentIdFour(String subDepartmentIdFour) {
		this.subDepartmentIdFour = subDepartmentIdFour;
	}

	/**
	 * Returns the DepartmentIdOne. This is the first department id used to associate with a pss department.
	 * (Max of 4)
	 *
	 * @return DepartmentIdOne
	 */
	public Integer getDepartmentIdOne() {
		return departmentIdOne;
	}

	/**
	 * Sets the DepartmentIdOne
	 *
	 * @param departmentIdOne The DepartmentIdOne
	 */
	public void setDepartmentIdOne(Integer departmentIdOne) {
		this.departmentIdOne = departmentIdOne;
	}

	/**
	 * Returns the DepartmentIdTwo. This is the second department id used to associate with a pss department.
	 * (Max of 4)
	 *
	 * @return DepartmentIdTwo
	 */
	public Integer getDepartmentIdTwo() {
		return departmentIdTwo;
	}

	/**
	 * Sets the DepartmentIdTwo
	 *
	 * @param departmentIdTwo The DepartmentIdTwo
	 */
	public void setDepartmentIdTwo(Integer departmentIdTwo) {
		this.departmentIdTwo = departmentIdTwo;
	}

	/**
	 * Returns the DepartmentIdThree. This is the third department id used to associate with a pss department.
	 * (Max of 4)
	 *
	 * @return DepartmentIdThree
	 */
	public Integer getDepartmentIdThree() {
		return departmentIdThree;
	}

	/**
	 * Sets the DepartmentIdThree
	 *
	 * @param departmentIdThree The DepartmentIdThree
	 */
	public void setDepartmentIdThree(Integer departmentIdThree) {
		this.departmentIdThree = departmentIdThree;
	}

	/**
	 * Returns the DepartmentIdFour. This is the fourth department id used to associate with a pss department.
	 * (Max of 4)
	 *
	 * @return DepartmentIdFour
	 */
	public Integer getDepartmentIdFour() {
		return departmentIdFour;
	}

	/**
	 * Sets the DepartmentIdFour
	 *
	 * @param departmentIdFour The DepartmentIdFour
	 */
	public void setDepartmentIdFour(Integer departmentIdFour) {
		this.departmentIdFour = departmentIdFour;
	}

	/**
	 * Returns the PssDepartmentCodeOne. The first code that is associated with a pss department.
	 *
	 * @return PssDepartmentCodeOne
	 */
	public Integer getPssDepartmentCodeOne() {
		return pssDepartmentCodeOne;
	}

	/**
	 * Sets the PssDepartmentCodeOne
	 *
	 * @param pssDepartmentCodeOne The PssDepartmentCodeOne
	 */
	public void setPssDepartmentCodeOne(Integer pssDepartmentCodeOne) {
		this.pssDepartmentCodeOne = pssDepartmentCodeOne;
	}

	/**
	 * Returns the PssDepartmentCodeTwo. The second code that is associated with a pss department.
	 *
	 * @return PssDepartmentCodeTwo
	 */
	public Integer getPssDepartmentCodeTwo() {
		return pssDepartmentCodeTwo;
	}

	/**
	 * Sets the PssDepartmentCodeTwo
	 *
	 * @param pssDepartmentCodeTwo The PssDepartmentCodeTwo
	 */
	public void setPssDepartmentCodeTwo(Integer pssDepartmentCodeTwo) {
		this.pssDepartmentCodeTwo = pssDepartmentCodeTwo;
	}

	/**
	 * Returns the PssDepartmentCodeThree. The third code that is associated with a pss department.
	 *
	 * @return PssDepartmentCodeThree
	 */
	public Integer getPssDepartmentCodeThree() {
		return pssDepartmentCodeThree;
	}

	/**
	 * Sets the PssDepartmentCodeThree
	 *
	 * @param pssDepartmentCodeThree The PssDepartmentCodeThree
	 */
	public void setPssDepartmentCodeThree(Integer pssDepartmentCodeThree) {
		this.pssDepartmentCodeThree = pssDepartmentCodeThree;
	}

	/**
	 * Returns the PssDepartmentCodeFour. The fourth code that is associated with a pss department.
	 *
	 * @return PssDepartmentCodeFour
	 */
	public Integer getPssDepartmentCodeFour() {
		return pssDepartmentCodeFour;
	}

	/**
	 * Sets the PssDepartmentCodeFour
	 *
	 * @param pssDepartmentCodeFour The PssDepartmentCodeFour
	 */
	public void setPssDepartmentCodeFour(Integer pssDepartmentCodeFour) {
		this.pssDepartmentCodeFour = pssDepartmentCodeFour;
	}

	/**
	 * Returns the MerchandiseTypeCodeOne. The merchandise type code for pss department one.
	 *
	 * @return MerchandiseTypeCodeOne
	 */
	public Character getMerchandiseTypeCodeOne() {
		return merchandiseTypeCodeOne;
	}

	/**
	 * Sets the MerchandiseTypeCodeOne
	 *
	 * @param merchandiseTypeCodeOne The MerchandiseTypeCodeOne
	 */
	public void setMerchandiseTypeCodeOne(Character merchandiseTypeCodeOne) {
		this.merchandiseTypeCodeOne = merchandiseTypeCodeOne;
	}

	/**
	 * Returns the MerchandiseTypeCodeTwo. The merchandise type code for pss department two.
	 *
	 * @return MerchandiseTypeCodeTwo
	 */
	public Character getMerchandiseTypeCodeTwo() {
		return merchandiseTypeCodeTwo;
	}

	/**
	 * Sets the MerchandiseTypeCodeTwo
	 *
	 * @param merchandiseTypeCodeTwo The MerchandiseTypeCodeTwo
	 */
	public void setMerchandiseTypeCodeTwo(Character merchandiseTypeCodeTwo) {
		this.merchandiseTypeCodeTwo = merchandiseTypeCodeTwo;
	}

	/**
	 * Returns the MerchandiseTypeCodeThree. The merchandise type code for pss department three.
	 *
	 * @return MerchandiseTypeCodeThree
	 */
	public Character getMerchandiseTypeCodeThree() {
		return merchandiseTypeCodeThree;
	}

	/**
	 * Sets the MerchandiseTypeCodeThree
	 *
	 * @param merchandiseTypeCodeThree The MerchandiseTypeCodeThree
	 */
	public void setMerchandiseTypeCodeThree(Character merchandiseTypeCodeThree) {
		this.merchandiseTypeCodeThree = merchandiseTypeCodeThree;
	}

	/**
	 * Returns the MerchandiseTypeCodeFour. The merchandise type code for pss department four.
	 *
	 * @return MerchandiseTypeCodeFour
	 */
	public Character getMerchandiseTypeCodeFour() {
		return merchandiseTypeCodeFour;
	}

	/**
	 * Sets the MerchandiseTypeCodeFour
	 *
	 * @param merchandiseTypeCodeFour The MerchandiseTypeCodeFour
	 */
	public void setMerchandiseTypeCodeFour(Character merchandiseTypeCodeFour) {
		this.merchandiseTypeCodeFour = merchandiseTypeCodeFour;
	}

	/**
	 * Returns the MerchandiseTypeOne. This is the first merchandise type that is associated to the first pss department.
	 *
	 * @return MerchandiseTypeOne
	 */
	public MerchandiseType getMerchandiseTypeOne() {
		return merchandiseTypeOne;
	}

	/**
	 * Sets the MerchandiseTypeOne
	 *
	 * @param merchandiseTypeOne The MerchandiseTypeOne
	 */
	public void setMerchandiseTypeOne(MerchandiseType merchandiseTypeOne) {
		this.merchandiseTypeOne = merchandiseTypeOne;
	}

	/**
	 * Returns the MerchandiseTypeTwo. This is the second merchandise type that is associated to the second pss department.
	 *
	 * @return MerchandiseTypeTwo
	 */
	public MerchandiseType getMerchandiseTypeTwo() {
		return merchandiseTypeTwo;
	}

	/**
	 * Sets the MerchandiseTypeTwo
	 *
	 * @param merchandiseTypeTwo The MerchandiseTypeTwo
	 */
	public void setMerchandiseTypeTwo(MerchandiseType merchandiseTypeTwo) {
		this.merchandiseTypeTwo = merchandiseTypeTwo;
	}

	/**
	 * Returns the MerchandiseTypeThree. This is the third merchandise type that is associated to the third pss department.
	 *
	 * @return MerchandiseTypeThree
	 */
	public MerchandiseType getMerchandiseTypeThree() {
		return merchandiseTypeThree;
	}

	/**
	 * Sets the MerchandiseTypeThree
	 *
	 * @param merchandiseTypeThree The MerchandiseTypeThree
	 */
	public void setMerchandiseTypeThree(MerchandiseType merchandiseTypeThree) {
		this.merchandiseTypeThree = merchandiseTypeThree;
	}

	/**
	 * Returns the MerchandiseTypeFour. This is the third merchandise type that is associated to the fourth pss department.
	 *
	 * @return MerchandiseTypeFour
	 */
	public MerchandiseType getMerchandiseTypeFour() {
		return merchandiseTypeFour;
	}

	/**
	 * Sets the MerchandiseTypeFour
	 *
	 * @param merchandiseTypeFour The MerchandiseTypeFour
	 */
	public void setMerchandiseTypeFour(MerchandiseType merchandiseTypeFour) {
		this.merchandiseTypeFour = merchandiseTypeFour;
	}
	public Integer getAbcItemCategoryNo() {
		return abcItemCategoryNo;
	}

	public void setAbcItemCategoryNo(Integer abcItemCategoryNo) {
		this.abcItemCategoryNo = abcItemCategoryNo;
	}

	public String getAbcAuthorizationCode() {
		return abcAuthorizationCode;
	}

	public void setAbcAuthCode(String abcAuthorizationCode) {
		this.abcAuthorizationCode = abcAuthorizationCode;
	}
	public Integer getUsdaNumber() {
		return usdaNumber;
	}

	public void setUsdaNumber(Integer usdaNumber) {
		this.usdaNumber = usdaNumber;
	}
	public String getCriticalItemIndicator() {
		return criticalItemIndicator;
	}

	public void setCriticalItemIndicator(String criticalItemIndicator) {
		this.criticalItemIndicator = criticalItemIndicator;
	}

	public Integer getMaxShipQty() {
		return maxShipQty;
	}

	public void setMaxShipQty(Integer maxShipQty) {
		this.maxShipQty = maxShipQty;
	}
	public Boolean getVariableWeight() {
		return variableWeight;
	}

	public void setVariableWeight(Boolean variableWeight) {
		this.variableWeight = variableWeight;
	}

	public Boolean isCattle() {
		return cattle;
	}

	public void setCattle(Boolean cattle) {
		this.cattle = cattle;
	}

	public Boolean istCatchWeight() {
		return catchWeight;
	}

	public void setCatchWeight(Boolean catchWeight) {
		this.catchWeight = catchWeight;
	}

	public String getMexicoAuthorizationCode() {
		return mexicoAuthorizationCode;
	}

	public void setMexicoAuthorizationCode(String mexicoAuthorizationCode) {
		this.mexicoAuthorizationCode = mexicoAuthorizationCode;
	}

	public String getMexicanBorderAuthorizationCode() {
		return mexicanBorderAuthorizationCode;
	}

	public void setMexicanBorderAuthorizationCode(String mexicanBorderAuthorizationCode) {
		this.mexicanBorderAuthorizationCode = mexicanBorderAuthorizationCode;
	}
	public Boolean isNewItem() {
		return newItem;
	}

	public void setNewItem(Boolean newItem) {
		this.newItem = newItem;
	}
	public String getAddedUsrId() {
		return addedUsrId;
	}

	public void setAddedUsrId(String addedUsrId) {
		this.addedUsrId = addedUsrId;
	}

	public LocalDateTime getLastUpdateDate() {
		return lastUpdateDate;
	}

	public void setLastUpdateDate(LocalDateTime lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}

	public String getLastUpdateUserId() {
		return lastUpdateUserId;
	}

	public void setLastUpdateUserId(String lastUpdateUserId) {
		this.lastUpdateUserId = lastUpdateUserId;
	}

	public Double getItemSizeQuantity() {
		return itemSizeQuantity;
	}

	public void setItemSizeQuantity(Double itemSizeQuantity) {
		this.itemSizeQuantity = itemSizeQuantity;
	}

	public String getItemSizeUom() {
		return itemSizeUom;
	}

	public void setItemSizeUom(String itemSizeUom) {
		this.itemSizeUom = itemSizeUom;
	}
	public Integer getCommodityCode() {
		return commodityCode;
	}

	public void setCommodityCode(Integer commodityCode) {
		this.commodityCode = commodityCode;
	}

	public Integer getSubCommodityCode() {
		return subCommodityCode;
	}

	public void setSubCommodityCode(Integer subCommodityCode) {
		this.subCommodityCode = subCommodityCode;
	}

	public Short getClassCode() {
		return classCode;
	}

	public void setClassCode(Short classCode) {
		this.classCode = classCode;
	}
	public Integer getGuaranteeToStoreDays() {
		return guaranteeToStoreDays;
	}

	public void setGuaranteeToStoreDays(Integer guaranteeToStoreDays) {
		this.guaranteeToStoreDays = guaranteeToStoreDays;
	}

	public Integer getWarehouseReactionDays() {
		return warehouseReactionDays;
	}

	public void setWarehouseReactionDays(Integer warehouseReactionDays) {
		this.warehouseReactionDays = warehouseReactionDays;
	}

	public Integer getOnReceiptLifeDays() {
		return onReceiptLifeDays;
	}

	public void setOnReceiptLifeDays(Integer onReceiptLifeDays) {
		this.onReceiptLifeDays = onReceiptLifeDays;
	}

	/**
	 * Gets the value of relatedItems.
	 *
	 * @return the relatedItems
	 */
	public List<RelatedItem> getRelatedItems() {
		return relatedItems;
	}

	/**
	 * Sets the value of relatedItems.
	 *
	 * @param relatedItems the relatedItems to set
	 */
	public void setRelatedItems(List<RelatedItem> relatedItems) {
		this.relatedItems = relatedItems;
	}

	/**
	 * Gets the value of dsdEdcStatus.
	 *
	 * @return the dsdEdcStatus
	 */
	public boolean getDsdEdcStatus() {
		return dsdEdcStatus;
	}

	/**
	 * Sets the value of dsdEdcStatus.
	 *
	 * @param dsdEdcStatus the dsdEdcStatus to set
	 */
	public void setDsdEdcStatus(boolean dsdEdcStatus) {
		this.dsdEdcStatus = dsdEdcStatus;
	}

	public String getDisplayCreatedName() {
		return displayCreatedName;
	}

	public void setDisplayCreatedName(String displayCreatedName) {
		this.displayCreatedName=displayCreatedName;
	}

	public LocalDateTime getCreatedDateTime() {
		return createdDateTime;
	}

	public void setCreatedDateTime(LocalDateTime createdDateTime) {
		this.createdDateTime=createdDateTime;
	}

	/**
	 * Compares another object to this one. If that object is an ItemMaster, it uses they keys
	 * to determine if they are equal and ignores non-key values for the comparison.
	 *
	 * @param o The object to compare to.
	 * @return True if they are equal and false otherwise.
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof ItemMaster)) return false;

		ItemMaster that = (ItemMaster)o;

		if (this.key != null ? !this.key.equals(that.key) : that.key != null) return false;

		return true;
	}

	/**
	 * Returns a hash code for this object. If two objects are equal, they have the same hash. If they are not equal,
	 * they have different hash codes.
	 *
	 * @return The hash code for this object.
	 */
	@Override
	public int hashCode() {
		return this.key == null ? 0 : this.key.hashCode();
	}

	/**
	 * Returns a printable representation of the object.
	 *
	 * @return A printable representation of the object.
	 */
	@Override
	public String toString() {
		return "ItemMaster{" +
				"key=" + key +
				", description='" + description + '\'' +
				", discontinueDate=" + discontinueDate +
				", pack=" + pack +
				", orderingUpc=" + orderingUpc +
				", discontinuedByUID='" + discontinuedByUID + '\'' +
				", addedDate=" + addedDate +
				", itemSize='" + itemSize + '\'' +
				", retail='" + retail + '\'' +
				'}';
	}
}
