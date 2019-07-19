/*
 *  AuthorizeItem
 *  Copyright (c) 2018 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.authorization;

import com.heb.pm.authorization.util.AuthorizationConstants;
import org.apache.commons.lang.StringUtils;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * This class is used to holds the information about item for authorize.
 *
 * @author vn70529
 * @since 2.23.0
 */
public class AuthorizeItem implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -662593000075520235L;
	/**
	 * vendor Id
	 */
	private Integer vendorId;
	/**
	 * vendor name
	 */
	private String vendorName;
	/**
	 * upc number
	 */
	private String upc;
	/**
	 * product description
	 */
	private String productDescription;
	/**
	 * case list cost
	 */
	private Double caseListCost;
	/**
	 * master pack
	 */
	private String masterPack;
	/**
	 * unit cost
	 */
	private Double unitCost;
	/**
	 * retail price
	 */
	private Integer retail;
	/**
	 * retail XFOR
	 */
	private Double retailFor;
	/**
	 * List of stores
	 */
	private String store;
	/**
	 * item type
	 */
	private String itemType;
	/**
	 * item id
	 */
	private String itemId;
	/**
	 * department number
	 */
	private String departmentNo;
	/**
	 * sub department number
	 */
	private String subDepartmentNo;
	/**
	 * pss dept number
	 */
	private Integer pssDepartmentOne;
	/**
	 * primary upc
	 */
	private Long productPrimaryScanCodeId;
	/**
	 * item size
	 */
	private String itemSize;
	/**
	 *
	 * Product ID
	 */
	private Long productId;
	
	/**
	 * VAR_WT_SW
	 */
	private Boolean flexWeightSwitch;
	/**
	 * vend_pk_qty of vend_loc_itm
	 */
	private Integer packQuantity;
	/**
	 * ITM_SZ_QTY of item master
	 */
	private Double  itemSizeQuantity;
	/**
	 * ITM_SZ_UOM_CD of item master
	 */
	private String  itemSizeUom;
	/**
	 * SCN_TYP_CD of prod_scn_codes
	 */
	private String scnTypCd;
	/**
	 * vend_list_cst of vend_loc_itm
	 */
	private Double listCost;
	/**
	 * dscon_dt of item master
	 */
	private LocalDate discontinueDate;
	/**
	 * status basic on ItemType and dscon_dt
	 */
	private Boolean status;
	/**
	 * retl_unt_sell_sz_1
	 */
	private Double quantity;
	/**
	 * tag_sz_des
	 */
	private String tagSize;
	/**
	 * retl_sell_sz_des
	 */
	private String retailUnitOfMeasureDes;
	/**
	 * Returns the flexWeightSwitch of good product.
	 * @return the flexWeightSwitch of good product.
	 */
	public Boolean getFlexWeightSwitch() {
		return flexWeightSwitch;
	}
	/**
	 * Sets the flexWeightSwitch of good product.
	 * @param flexWeightSwitch the flexWeightSwitch of good product.
	 */
	public void setFlexWeightSwitch(Boolean flexWeightSwitch) {
		this.flexWeightSwitch = flexWeightSwitch;
	}

	/**
	 * Returns the item size.
	 * @return the item size.
	 */
	public String getItemSize() {
		return itemSize;
	}

	/**
	 * Sets the item size.
	 * @param itemSize the item size.
	 */
	public void setItemSize(String itemSize) {
		this.itemSize = itemSize;
	}

	/**
	 * Returns the product Primary Scan Code Id.
	 * @return the product Primary Scan Code Id
	 */
	public Long getProductPrimaryScanCodeId() {
		return productPrimaryScanCodeId;
	}

	/**
	 * Sets the product Primary Scan Code Id.
	 * @param productPrimaryScanCodeId the product Primary Scan Code Id
	 */
	public void setProductPrimaryScanCodeId(Long productPrimaryScanCodeId) {
		this.productPrimaryScanCodeId = productPrimaryScanCodeId;
	}

	/**
	 * Returns the pss Department One.
	 * @return the pss Department One.
	 */
	public Integer getPssDepartmentOne() {
		return pssDepartmentOne;
	}

	/**
	 * Sets the pss Department One.
	 * @param pssDepartmentOne the pss Department One.
	 */
	public void setPssDepartmentOne(Integer pssDepartmentOne) {
		this.pssDepartmentOne = pssDepartmentOne;
	}

	/**
	 * Returns the department No.
	 * @return the department No.
	 */
	public String getDepartmentNo() {
		return departmentNo;
	}

	/**
	 * Sets the department No.
	 * @param departmentNo the department No.
	 */
	public void setDepartmentNo(String departmentNo) {
		this.departmentNo = departmentNo;
	}

	/**
	 * Returns the subDepartmentNo.
	 * @return the subDepartmentNo.
	 */
	public String getSubDepartmentNo() {
		return subDepartmentNo;
	}

	/**
	 * Sets the subDepartmentNo.
	 * @param subDepartmentNo the subDepartmentNo.
	 */
	public void setSubDepartmentNo(String subDepartmentNo) {
		this.subDepartmentNo = subDepartmentNo;
	}

	/**
	 * Returns the item code.
	 * @return the item code.
	 */
	public String getItemId() {
		return itemId;
	}

	/**
	 * Sets the item code.
	 * @param itemId the item code.
	 */
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	/**
	 * Return the item type.
	 * @return the item type.
	 */
	public String getItemType() {
		return itemType;
	}

	/**
	 * Sets the item type.
	 * @param itemType the item type.
	 */
	public void setItemType(String itemType) {
		this.itemType = itemType;
	}

	/**
	 * Returns the store.
	 * @return the store.
	 */
	public String getStore() {
		return store;
	}

	/**
	 * Sets the store.
	 * @param stores the store list.
	 */
	public void setStore(String store) {
		this.store = store;
	}

	/**
	 * Return vendor id.
	 * @return the vendor id.
	 */
	public Integer getVendorId() {
		return vendorId;
	}

	/**
	 * Sets  the vendor id.
	 * @param vendorId the vendor id.
	 */
	public void setVendorId(Integer vendorId) {
		this.vendorId = vendorId;
	}

	/**
	 * Returns the vendor name.
	 * @return the vendor name.
	 */
	public String getVendorName() {
		return vendorName;
	}

	/**
	 * Sets the vendor name.
	 * @param vendorName the vendor name.
	 */
	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}

	/**
	 * Returns the upc.
	 * @return the upc.
	 */
	public String getUpc() {
		return upc;
	}

	/**
	 * Sets the upc.
	 * @param upc the upc.
	 */
	public void setUpc(String upc) {
		this.upc = upc;
	}

	/**
	 * Returns the description of product.
	 * @return the description of product
	 */
	public String getProductDescription() {
		return productDescription;
	}

	/**
	 * Sets the description of product.
	 * @param productDescription the description of product.
	 */
	public void setProductDescription(String productDescription) {
		this.productDescription = productDescription;
	}

	/**
	 * Returns case list cost.
	 * @return the case list cost.
	 */
	public Double getCaseListCost() {
		return caseListCost;
	}

	/**
	 * Sets the case list cost.
	 * @param caseListCost the case list cost.
	 */
	public void setCaseListCost(Double caseListCost) {
		this.caseListCost = caseListCost;
	}

	/**
	 * Returns the master pack.
	 * @return return master pack.
	 */
	public String getMasterPack() {
		return masterPack;
	}

	/**
	 * Sets the master pack.
	 * @param masterPack the master pack.
	 */
	public void setMasterPack(String masterPack) {
		this.masterPack = masterPack;
	}

	/**
	 * Returns the unit cost.
	 * @return the unit cost.
	 */
	public Double getUnitCost() {
		return unitCost;
	}

	/**
	 * Sets the unit cost.
	 * @param unitCost the unit cost.
	 */
	public void setUnitCost(Double unitCost) {
		this.unitCost = unitCost;
	}

	/**
	 * Returns the retail.
	 * @return the retail.
	 */
	public Integer getRetail() {
		return retail;
	}

	/**
	 * Sets the retail.
	 * @param retail the retail.
	 */
	public void setRetail(Integer retail) {
		this.retail = retail;
	}

	/**
	 * Returns the retail for.
	 * @return the retail for.
	 */
	public Double getRetailFor() {
		return retailFor;
	}

	/**
	 * Sets the retail for.
	 * @param retailFor
	 */
	public void setRetailFor(Double retailFor) {
		this.retailFor = retailFor;
	}

	/**
	 * Sets the product id.
	 * @param prodId the product id.
	 */
	public void setProductId(Long prodId )
	{
		this.productId = prodId;
	}

	/**
	 * Returns the product id.
	 * @return the product id.
	 */
	public Long getProductId(){
		return this.productId;
	}

	/**
	 * Returns the pack quantity.
	 * @return the pack quantity.
	 */
	public Integer getPackQuantity() {
		return packQuantity;
	}

	/**
	 * Sets the pack quantity.
	 * @param packQuantity the pack quantity.
	 */
	public void setPackQuantity(Integer packQuantity) {
		this.packQuantity = packQuantity;
	}

	/**
	 * Returns the item size quantity.
	 * @return the item size quantity.
	 */
	public Double getItemSizeQuantity() {
		return itemSizeQuantity;
	}

	/**
	 * Sets the item size quantity.
	 * @param itemSizeQuantity the item size quantity.
	 */
	public void setItemSizeQuantity(Double itemSizeQuantity) {
		this.itemSizeQuantity = itemSizeQuantity;
	}

	/**
	 * Returns the item size uom.
	 * @return the item size uom.
	 */
	public String getItemSizeUom() {
		return itemSizeUom;
	}

	/**
	 * Sets the item size uom.
	 * @param itemSizeUom the item size uom.
	 */
	public void setItemSizeUom(String itemSizeUom) {
		this.itemSizeUom = itemSizeUom;
	}

	/**
	 * Returns the scan type code.
	 * @return the scan type code.
	 */
	public String getScnTypCd() {
		return scnTypCd;
	}

	/**
	 *  Sets the scan type code.
	 * @param scnTypCd the scan type code.
	 */
	public void setScnTypCd(String scnTypCd) {
		this.scnTypCd = scnTypCd;
	}

	/**
	 * Return the list cost.
	 * @return the list cost.
	 */
	public double getListCost() {
		return listCost;
	}

	/**
	 * Sets the list cost.
	 * @param listCost the list cost.
	 */
	public void setListCost(double listCost) {
		this.listCost = listCost;
	}

	/**
	 * Returns the list cost.
	 * @return the list cost.
	 */
	public LocalDate getDiscontinueDate() {
		return discontinueDate;
	}

	/**
	 * Sets the list cost.
	 * @param discontinueDate the list cost.
	 */
	public void setDiscontinueDate(LocalDate discontinueDate) {
		this.discontinueDate = discontinueDate;
	}

	/**
	 * Returns the item status.
	 * @return the item status.
	 */
	public Boolean isStatus() {
		return status;
	}

	/**
	 * Sets the item status.
	 * @param status the item status.
	 */
	public void setStatus(Boolean status) {
		this.status = status;
	}

	/**
	 * Gets quantity the selling size of the product
	 *
	 * @return the quantity is the selling size of the product
	 */
	public Double getQuantity() {
		return quantity;
	}
	/**
	 * Sets quantity the selling size of the product
	 *
	 * @param quantity the quantity selling size of the product
	 */
	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}
	/**
	 * Generate unique id for this object.
	 * @return the unique id.
	 */
	/**
	 * Returns the size printed on the shelf tags.
	 *
	 * @return The size printed on the shelf tags.
	 */
	public String getTagSize() {
		return tagSize;
	}

	/**
	 * Sets the size printed on the shelf tags.
	 *
	 * @param tagSize The size printed on the shelf tags.
	 */
	public void setTagSize(String tagSize) {
		this.tagSize = tagSize;
	}
	/**
	 * Returns Description that is given to the retail unit of measure.
	 *
	 * @return The Description that is given to the retail unit of measure.
	 **/
	public String getRetailUnitOfMeasureDes() {
		return retailUnitOfMeasureDes;
	}
	/**
	 * Sets the Description that is given to the retail unit of measure.
	 *
	 * @param retailUnitOfMeasureDes The Description that is given to the retail unit of measure.
	 **/
	public void setRetailUnitOfMeasureDes(String retailUnitOfMeasureDes) {
		this.retailUnitOfMeasureDes = retailUnitOfMeasureDes;
	}

	public String getKey(){
		StringBuilder sb = new StringBuilder();
		sb.append(itemType!= null?itemType:StringUtils.EMPTY);
		sb.append(AuthorizationConstants.DASH);
		sb.append(itemId);
		sb.append(AuthorizationConstants.DASH);
		sb.append(vendorName!= null?vendorName:StringUtils.EMPTY);
		sb.append(AuthorizationConstants.DASH);
		sb.append(vendorId);
		sb.append(AuthorizationConstants.DASH);
		sb.append(scnTypCd!= null?scnTypCd:StringUtils.EMPTY);
		sb.append(AuthorizationConstants.DASH);
		sb.append(String.valueOf(flexWeightSwitch));
		sb.append(AuthorizationConstants.DASH);
		sb.append(productId !=null? productId :0);
		sb.append(AuthorizationConstants.DASH);
		sb.append(productDescription !=null? productDescription :StringUtils.EMPTY);
		sb.append(AuthorizationConstants.DASH);
		sb.append(packQuantity !=null?packQuantity:0);
		sb.append(AuthorizationConstants.DASH);
		sb.append(itemSizeUom !=null?itemSizeUom:StringUtils.EMPTY);
		sb.append(AuthorizationConstants.DASH);
		sb.append(itemSizeQuantity!=null?itemSizeQuantity:0);
		sb.append(AuthorizationConstants.DASH);
		sb.append(departmentNo !=null? subDepartmentNo :StringUtils.EMPTY);
		sb.append(AuthorizationConstants.DASH);
		sb.append(subDepartmentNo !=null? subDepartmentNo :StringUtils.EMPTY);
		sb.append(AuthorizationConstants.DASH);
		sb.append(pssDepartmentOne !=null? pssDepartmentOne :0);
		sb.append(AuthorizationConstants.DASH);
		sb.append(productPrimaryScanCodeId !=null? productPrimaryScanCodeId :0);
		sb.append(AuthorizationConstants.DASH);
		sb.append(listCost);
		sb.append(AuthorizationConstants.DASH);
		sb.append(discontinueDate!=null?discontinueDate.toString(): StringUtils.EMPTY);
		return sb.toString();
	}
}
