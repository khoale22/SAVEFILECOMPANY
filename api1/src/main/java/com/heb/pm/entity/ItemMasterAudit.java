package com.heb.pm.entity;

import com.heb.util.audit.Audit;
import com.heb.util.audit.AuditableField;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Records off all changes made to the Item Master Table
 * @author s753601
 * @version 2.8.0
 */
@Entity
@Table(name = "ITEM_MASTER_AUD")
public class ItemMasterAudit  implements Serializable, Audit{

	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private ItemMasterAuditKey key;

	@Column(name = "ACT_CD")
	private String action;

	@Column(name = "SUB_DEPT_ID_1")
	private String subDepartmentId1;

	@Column(name = "SUB_DEPT_ID_2")
	private String subDepartmentId2;

	@Column(name = "SUB_DEPT_ID_3")
	private String subDepartmentId3;

	@Column(name = "SUB_DEPT_ID_4")
	private String subDepartmentId4;

	@Column(name = "LST_UPDT_USR_ID")
	private String changedBy;

	@AuditableField(displayName = "Department 1", filter = {FilterConstants.CASE_PACK_DEPARTMENT_AUDIT})
	@Column(name = "DEPT_ID_1")
	private String departmentId1;

	@AuditableField(displayName = "Department 2", filter = {FilterConstants.CASE_PACK_DEPARTMENT_AUDIT})
	@Column(name = "DEPT_ID_2")
	private String departmentId2;

	@AuditableField(displayName = "Department 3", filter = {FilterConstants.CASE_PACK_DEPARTMENT_AUDIT})
	@Column(name = "DEPT_ID_3")
	private String departmentId3;

	@AuditableField(displayName = "Department 4", filter = {FilterConstants.CASE_PACK_DEPARTMENT_AUDIT})
	@Column(name = "DEPT_ID_4")
	private String departmentId4;

	@AuditableField(displayName = "Merchandise Type 2", filter = {FilterConstants.CASE_PACK_DEPARTMENT_AUDIT})
	@Column(name = "DEPT_MDSE_TYP_2")
	private String departmentMerchandise2;

	@AuditableField(displayName = "PSS Dept 2", filter = {FilterConstants.CASE_PACK_DEPARTMENT_AUDIT})
	@Column(name = "PSS_DEPT_2")
	private String pssDepartment2;

	@AuditableField(displayName = "Merchandise Type 1", filter = {FilterConstants.CASE_PACK_DEPARTMENT_AUDIT})
	@Column(name = "DEPT_MDSE_TYP_1")
	private String departmentMerchandise1;

	@AuditableField(displayName = "PSS Dept 1", filter = {FilterConstants.CASE_PACK_DEPARTMENT_AUDIT})
	@Column(name = "PSS_DEPT_1")
	private String pssDepartment1;

	@AuditableField(displayName = "Merchandise Type 3", filter = {FilterConstants.CASE_PACK_DEPARTMENT_AUDIT})
	@Column(name = "DEPT_MDSE_TYP_3")
	private String departmentMerchandise3;

	@AuditableField(displayName = "PSS Dept 3", filter = {FilterConstants.CASE_PACK_DEPARTMENT_AUDIT})
	@Column(name = "PSS_DEPT_3")
	private String pssDepartment3;

	@AuditableField(displayName = "Merchandise Type 4", filter = {FilterConstants.CASE_PACK_DEPARTMENT_AUDIT})
	@Column(name = "DEPT_MDSE_TYP_4")
	private String departmentMerchandise4;

	@AuditableField(displayName = "PSS Dept 4", filter = {FilterConstants.CASE_PACK_DEPARTMENT_AUDIT})
	@Column(name = "PSS_DEPT_4")
	private String pssDepartment4;

	@AuditableField(displayName = "Display Ready Unit", filter = {FilterConstants.DRU_AUDIT})
	@Column(name = "dsply_rdy_pal_sw")
	private boolean displayReadyUnit;

	@AuditableField(displayName = "Type of Display Ready Unit",
			codeTableDisplayNameMethod = "getTypeOfDRUDisplayName",
			filter = {FilterConstants.DRU_AUDIT})
	@Column(name = "srs_aff_typ_cd")
	private String typeOfDRU;

	@AuditableField(displayName = "Always Sub When Out", filter = {FilterConstants.DRU_AUDIT})
	@Column(name = "std_subst_logic_sw")
	private boolean alwaysSubWhenOut;

	@AuditableField(displayName = "Rows Facing in Retail Units", filter = {FilterConstants.DRU_AUDIT})
	@Column(name = "prod_fcng_nbr")
	private long rowsFacing;

	@AuditableField(displayName = "Rows Deep in Retail Units", filter = {FilterConstants.DRU_AUDIT})
	@Column(name = "prod_row_deep_nbr")
	private long rowsDeep;

	@AuditableField(displayName = "Rows High in Retail Units", filter = {FilterConstants.DRU_AUDIT})
	@Column(name = "prod_row_hi_nbr")
	private long rowsHigh;

	@AuditableField(displayName = "Orientation on shelf", filter = {FilterConstants.DRU_AUDIT})
	@Column(name = "nbr_of_orint_nbr")
	private long orientation;

	@AuditableField(displayName = "Discontinue Reason", filter = {FilterConstants.CASE_PACK_INFO_AUDIT},
			codeTableDisplayNameMethod = "getDiscontinueReasonDisplayName")
	@Column(name = "dscon_rsn_cd")
	private String discontinueReasonId;

	@AuditableField(displayName = "Discontinue Date", filter = {FilterConstants.CASE_PACK_INFO_AUDIT})
	@Column(name = "dscon_dt")
	private LocalDateTime discontinueDate;

	@AuditableField(displayName = "Discontinued By", filter = {FilterConstants.CASE_PACK_INFO_AUDIT})
	@Column(name = "dscon_usr_id")
	private String discontinueUserId;

	@AuditableField(displayName = "Item Description", filter = {FilterConstants.CASE_PACK_INFO_AUDIT})
	@Column(name = "item_des")
	private String itemDescription;

	@AuditableField(displayName = "Case Upc", filter = {FilterConstants.CASE_PACK_INFO_AUDIT})
	@Column(name = "case_upc")
	private Long caseUpc;

	@AuditableField(displayName = "Case Pack Primary UPC", filter = {FilterConstants.CASE_PACK_INFO_AUDIT})
	@Column(name = "ordering_upc")
	private Long orderingUpc;

	@AuditableField(displayName = "Item Type", filter = {FilterConstants.CASE_PACK_INFO_AUDIT},
			codeTableDisplayNameMethod = "getItemTypeDisplayName")
	@Column(name = "itm_typ_cd")
	private String itemTypeId;

	@AuditableField(displayName = "One Touch Type", filter = {FilterConstants.CASE_PACK_INFO_AUDIT},
			codeTableDisplayNameMethod = "getOneTouchTypeDisplayName")
	@Column(name = "one_touch_typ_cd")
	private String oneTouchTypeId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumns({
			@JoinColumn(name = "one_touch_typ_cd", referencedColumnName = "one_touch_typ_cd", insertable = false, updatable = false, nullable = false)
	})
	private OneTouchType oneTouchType;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumns({
			@JoinColumn(name = "itm_typ_cd", referencedColumnName = "itm_typ_cd", insertable = false, updatable = false, nullable = false)
	})
	private ItemType itemType;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumns({
			@JoinColumn(name = "dscon_rsn_cd", referencedColumnName = "dscon_rsn_cd", insertable = false, updatable = false, nullable = false)
	})
	private DiscontinueReason discontinueReason;

	/**
	 * Gets the unique identifier for the ItemMaster Audit
	 * @return the unique identifier for the ItemMaster Audit
	 */
	public ItemMasterAuditKey getKey() {
		return key;
	}

	/**
	 * Updates the unique identifier for the ItemMaster Audit
	 * @param key the new unique identifier for the ItemMaster Audit
	 */
	public void setKey(ItemMasterAuditKey key) {
		this.key = key;
	}


	/**
	 * 	Returns the ActionCode. The action code pertains to the action of the audit. 'UPDAT', 'PURGE', or 'ADD'.
	 * 	@return ActionCode
	 */
	public String getAction() {
		return action;
	}

	/**
	 * Updates the action code
	 * @param action the new action
	 */
	public void setAction(String action) {
		this.action = action;
	}

	/**
	 * 	Returns the pssDepartment1.
	 * 	@return pssDepartment1
	 */
	public String getPssDepartment1() {
		return pssDepartment1;
	}

	/**
	 * Updates the departmentId1
	 * @param pssDepartment1 the new departmentId1
	 */
	public void setPssDepartment1(String pssDepartment1) {
		this.pssDepartment1 = pssDepartment1;
	}

	/**
	 * 	Returns the subDepartmentId4.
	 * 	@return subDepartmentId4
	 */
	public String getSubDepartmentId4() {
		return subDepartmentId4;
	}

	/**
	 * Updates the subDepartmentId4
	 * @param subDepartmentId4 the new subDepartmentId4
	 */
	public void setSubDepartmentId4(String subDepartmentId4) {
		this.subDepartmentId4 = subDepartmentId4;
	}

	/**
	 * 	Returns the departmentId1.
	 * 	@return departmentId1
	 */
	public String getSubDepartmentId1() {
		return subDepartmentId1;
	}

	/**
	 * Updates the departmentId1
	 * @param subDepartmentId1 the new departmentId1
	 */
	public void setSubDepartmentId1(String subDepartmentId1) {
		this.subDepartmentId1 = subDepartmentId1;
	}

	/**
	 * 	Returns the subDepartmentId2.
	 * 	@return subDepartmentId2
	 */
	public String getSubDepartmentId2() {
		return subDepartmentId2;
	}

	/**
	 * Updates the subDepartmentId2
	 * @param subDepartmentId2 the new subDepartmentId2
	 */
	public void setSubDepartmentId2(String subDepartmentId2) {
		this.subDepartmentId2 = subDepartmentId2;
	}

	/**
	 * 	Returns the subDepartmentId3.
	 * 	@return subDepartmentId3
	 */
	public String getSubDepartmentId3() {
		return subDepartmentId3;
	}

	/**
	 * Updates the subDepartmentId3
	 * @param subDepartmentId3 the new subDepartmentId3
	 */
	public void setSubDepartmentId3(String subDepartmentId3) {
		this.subDepartmentId3 = subDepartmentId3;
	}

	/**
	 * 	Returns the departmentMerchandise1.
	 * 	@return departmentMerchandise1
	 */
	public String getDepartmentMerchandise1() {
		return departmentMerchandise1;
	}

	/**
	 * Updates the departmentMerchandise1
	 * @param departmentMerchandise1 the new departmentMerchandise1
	 */
	public void setDepartmentMerchandise1(String departmentMerchandise1) {
		this.departmentMerchandise1 = departmentMerchandise1;
	}

	/**
	 * 	Returns the pssDepartment2.
	 * 	@return pssDepartment2
	 */
	public String getPssDepartment2() {
		return pssDepartment2;
	}

	/**
	 * Updates the pssDepartment2
	 * @param pssDepartment2 the new pssDepartment2
	 */
	public void setPssDepartment2(String pssDepartment2) {
		this.pssDepartment2 = pssDepartment2;
	}
	/**
	 * 	Returns the departmentMerchandise2.
	 * 	@return departmentMerchandise2
	 */
	public String getDepartmentMerchandise2() {
		return departmentMerchandise2;
	}

	/**
	 * Updates the departmentMerchandise2
	 * @param departmentMerchandise2 the new departmentMerchandise2
	 */
	public void setDepartmentMerchandise2(String departmentMerchandise2) {
		this.departmentMerchandise2 = departmentMerchandise2;
	}


	/**
	 * 	Returns the pssDepartment3.
	 * 	@return pssDepartment3
	 */
	public String getPssDepartment3() {
		return pssDepartment3;
	}

	/**
	 * Updates the pssDepartment3
	 * @param pssDepartment3 the new pssDepartment3
	 */
	public void setPssDepartment3(String pssDepartment3) {
		this.pssDepartment3 = pssDepartment3;
	}
	/**
	 * 	Returns the departmentMerchandise3.
	 * 	@return departmentMerchandise3
	 */
	public String getDepartmentMerchandise3() {
		return departmentMerchandise3;
	}

	/**
	 * Updates the departmentMerchandise3
	 * @param departmentMerchandise3 the new departmentMerchandise3
	 */
	public void setDepartmentMerchandise3(String departmentMerchandise3) {
		this.departmentMerchandise3 = departmentMerchandise3;
	}

	/**
	 * 	Returns the pssDepartment4.
	 * 	@return pssDepartment4
	 */
	public String getPssDepartment4() {
		return pssDepartment4;
	}

	/**
	 * Updates the pssDepartment4
	 * @param pssDepartment4 the new pssDepartment4
	 */
	public void setPssDepartment4(String pssDepartment4) {
		this.pssDepartment4 = pssDepartment4;
	}
	/**
	 * 	Returns the departmentMerchandise4.
	 * 	@return departmentMerchandise4
	 */
	public String getDepartmentMerchandise4() {
		return departmentMerchandise4;
	}

	/**
	 * Updates the departmentMerchandise4
	 * @param departmentMerchandise4 the new departmentMerchandise4
	 */
	public void setDepartmentMerchandise4(String departmentMerchandise4) {
		this.departmentMerchandise4 = departmentMerchandise4;
	}

	/**
	 * 	Returns the departmentId1.
	 * 	@return departmentId1
	 */
	public String getDepartmentId1() {
		return departmentId1;
	}

	/**
	 * Updates the departmentId1
	 * @param departmentId1 the new departmentId1
	 */
	public void setDepartmentId1(String departmentId1) {
		this.departmentId1 = departmentId1;
	}

	/**
	 * 	Returns the departmentId2.
	 * 	@return departmentId2
	 */
	public String getDepartmentId2() {
		return departmentId2;
	}

	/**
	 * Updates the departmentId2
	 * @param departmentId2 the new departmentId2
	 */
	public void setDepartmentId2(String departmentId2) {
		this.departmentId2 = departmentId2;
	}

	/**
	 * 	Returns the departmentId3.
	 * 	@return departmentId3
	 */
	public String getDepartmentId3() {
		return departmentId3;
	}

	/**
	 * Updates the departmentId3
	 * @param departmentId3 the new departmentId3
	 */
	public void setDepartmentId3(String departmentId3) {
		this.departmentId3 = departmentId3;
	}

	/**
	 * 	Returns the departmentId4.
	 * 	@return departmentId4
	 */
	public String getDepartmentId4() {
		return departmentId4;
	}

	/**
	 * Updates the departmentId4
	 * @param departmentId4 the new departmentId2
	 */
	public void setDepartmentId4(String departmentId4) {
		this.departmentId4 = departmentId4;
	}

	/**
	 * Returns the user ID that made the change
	 * @return changedBy
	 */
	public String getChangedBy() {
		return changedBy;
	}

	/**
	 * Updates changedBy
	 * @param changedBy the new changedBy
	 */
	public void setChangedBy(String changedBy) {
		this.changedBy = changedBy;
	}

	/**
	 * Get the timestamp for when the record was created from the key
	 * @return the timestamp for when the record was created
	 */
	public LocalDateTime getChangedOn() {
		return this.key.getChangedOn();
	}

	/**
	 * Updates the timestamp for when the record was created in the key
	 * @param changedOn the new timestamp for when the record was created
	 */
	public void setChangedOn(LocalDateTime changedOn) {
		this.key.setChangedOn(changedOn);
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
	 * Returns the facing which is a row of similar products /Stock Keeping Units (SKUs) facing forward. One “facing” is
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
	 * Returns the DiscontinueDate. The date the item was discontinued one.
	 *
	 * @return DiscontinueDate
	 */
	public LocalDateTime getDiscontinueDate() {
		return discontinueDate;
	}

	/**
	 * Sets the DiscontinueDate
	 *
	 * @param discontinueDate The DiscontinueDate
	 */
	public void setDiscontinueDate(LocalDateTime discontinueDate) {
		this.discontinueDate = discontinueDate;
	}

	/**
	 * Returns the DiscontinueUserId. The user who discontinued the item.
	 *
	 * @return DiscontinueUserId
	 */
	public String getDiscontinueUserId() {
		return discontinueUserId;
	}

	/**
	 * Sets the DiscontinueUserId
	 *
	 * @param discontinueUserId The DiscontinueUserId
	 */
	public void setDiscontinueUserId(String discontinueUserId) {
		this.discontinueUserId = discontinueUserId;
	}

	/**
	 * Returns the DiscontinueUserId. The user who discontinued the item.
	 *
	 * @return DiscontinueUserId
	 */
	public String getItemDescription() {
		return itemDescription;
	}

	/**
	 * Sets the DiscontinueUserId
	 *
	 * @param itemDescription The DiscontinueUserId
	 */
	public void setItemDescription(String itemDescription) {
		this.itemDescription = itemDescription;
	}
	/**
	 * Returns the CaseUpc. The case upc that is attached to the item.
	 *
	 * @return CaseUpc
	 */
	public Long getCaseUpc() {
		return caseUpc;
	}

	/**
	 * Sets the CaseUpc
	 *
	 * @param caseUpc The CaseUpc
	 */
	public void setCaseUpc(Long caseUpc) {
		this.caseUpc = caseUpc;
	}

	/**
	 * Sets the orderingUpc
	 */
	public Long getOrderingUpc() {
		return orderingUpc;
	}

	/**
	 * @return Gets the value of orderingUpc and returns orderingUpc
	 */
	public void setOrderingUpc(Long orderingUpc) {
		this.orderingUpc = orderingUpc;
	}

	/**
	 * Returns the ItemTypeId. The item type id attached to the item to be able to join on the code table to determine what
	 * the item type is.
	 *
	 * @return ItemTypeId
	 */
	public String getItemTypeId() {
		return itemTypeId;
	}

	/**
	 * Sets the ItemTypeId
	 *
	 * @param itemTypeId The ItemTypeId
	 */
	public void setItemTypeId(String itemTypeId) {
		this.itemTypeId = itemTypeId;
	}

	/**
	 * Returns the ItemType. The code table to determine what the item type is. Display, sellable, rental, shipper, wrap
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
	 * Returns the DiscontinueReasonId. The discontinue reason id that can be joined on the discontinue reason table to
	 * determine what the discontinue reason is.
	 *
	 * @return DiscontinueReasonId
	 */
	public String getDiscontinueReasonId() {
		return discontinueReasonId;
	}

	/**
	 * Sets the DiscontinueReasonId
	 *
	 * @param discontinueReasonId The DiscontinueReasonId
	 */
	public void setDiscontinueReasonId(String discontinueReasonId) {
		this.discontinueReasonId = discontinueReasonId;
	}

	/**
	 * Returns the DiscontinueReason. Returns what the discontinue reason is.
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
	 * Returns the OneTouchTypeId. Returns the one touch type id to be joined on the one touch type code table to
	 * determine what type of one touch it is.
	 *
	 * @return OneTouchTypeId
	 */
	public String getOneTouchTypeId() {
		return oneTouchTypeId;
	}

	/**
	 * Sets the OneTouchTypeId
	 *
	 * @param oneTouchTypeId The OneTouchTypeId
	 */
	public void setOneTouchTypeId(String oneTouchTypeId) {
		this.oneTouchTypeId = oneTouchTypeId;
	}

	/**
	 * Returns the OneTouchType. Returns the one touch type according to its one touch type id.
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
	 * Used to get the user readable name for the type of DRU
	 * @return the user readable name for the type of DRU
	 */
	public String getTypeOfDRUDisplayName() {
		ItemMaster.DisplayReadyUnitType dRUType = ItemMaster.DisplayReadyUnitType.convertStringToDRUType(this.getTypeOfDRU());
		return dRUType.getDisplayName();
	}

	/**
	 * Method to call to retrieve the display name for a oneTouchType id. If the oneTouchType id exists,
	 * return it's display name. Else return 'NOT FOUND[id]' display name.
	 *
	 * @return oneTouchType display name
	 */
	public String getOneTouchTypeDisplayName() {
		OneTouchType oneTouchType = this.getOneTouchType();
		if(oneTouchType != null) {
			return oneTouchType.getDisplayName();
		} else {
			return String.format(Audit.DISPLAY_NAME_FORMAT_FOR_CODE_TABLE_NOT_FOUND, this.getOneTouchTypeId().trim());
		}
	}

	/**
	 * Method to call to retrieve the display name for an item type id. If the item type id exists,
	 * return it's display name. Else return 'NOT FOUND[id]' display name.
	 *
	 * @return Item Type display name
	 */
	public String getItemTypeDisplayName() {
		ItemType itemType = this.getItemType();
		if(itemType != null) {
			return itemType.getDisplayName();
		} else {
			return String.format(Audit.DISPLAY_NAME_FORMAT_FOR_CODE_TABLE_NOT_FOUND, this.getItemTypeId().trim());
		}
	}

	/**
	 * Method to call to retrieve the display name for a discontinue reason id. If the discontinue reason id exists,
	 * return it's display name. Else return 'NOT FOUND[id]' display name.
	 *
	 * @return
	 */
	public String getDiscontinueReasonDisplayName() {
		DiscontinueReason discontinueReason = this.getDiscontinueReason();
		if(discontinueReason != null) {
			return discontinueReason.getDisplayName();
		} else {
			return String.format(Audit.DISPLAY_NAME_FORMAT_FOR_CODE_TABLE_NOT_FOUND, this.getDiscontinueReasonId().trim());
		}
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

		ItemMasterAudit that = (ItemMasterAudit) o;

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
		return "ItemMasterAudit{" +
				"key=" + key +
				", action='" + action + '\'' +
				", changedBy='" + changedBy + '\'' +
				", displayReadyUnit=" + displayReadyUnit +
				", typeOfDRU=" + typeOfDRU + '\'' +
				", alwaysSubWhenOut=" + alwaysSubWhenOut + '\'' +
				", rowsDeep=" + rowsDeep+ '\'' +
				", rowsFacing=" + rowsFacing+ '\'' +
				", rowsHigh=" + rowsHigh+ '\'' +
				", orientation=" + orientation+ '\'' +
				'}';
	}
}
