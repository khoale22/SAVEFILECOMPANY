/*
 * CustProductGroup
 *
 * Copyright (c) 2017 HEB
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of HEB.
 */

package com.heb.pm.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * This is the entity for customer product group. This includes the product group id and the product group name.
 * Represents the cust_prod_grp table..
 *
 * @author l730832
 * @since 2.14.0
 */
@Entity
@Table(name = "cust_prod_grp")
@TypeDefs({
		@TypeDef(name = "fixedLengthChar", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharType.class)
})
public class CustomerProductGroup implements Serializable {

	private static final long serialVersionUID = 1L;
	/**
	 * Holds the type of CustomerProductGroup.
	 */
	public static final String TYPE = "PGRP";
	@Id
	@Column(name = "cust_prod_grp_id")
	private long custProductGroupId;

	@Column(name = "prod_grp_typ_cd")
	@Type(type="fixedLengthChar")
	private String productGroupTypeCode;

	@Column(name = "cust_prod_grp_nm")
	private String custProductGroupName;

	@Column(name = "cust_prod_grp_long")
	private String custProductGroupDescriptionLong;

	@Column(name = "cust_prod_grp_des")
	private String custProductGroupDescription;

	@Column(name = "cre8_ts")
	private LocalDateTime createDate;

	@Column(name = "cre8_uid")
	private String createdUserId;

	@Column(name = "lst_updt_ts")
	private LocalDateTime lastUpdateDate;

	@Column(name = "lst_updt_uid")
	private String lastUpdateUserId;

	@JsonIgnoreProperties("custProductGroups")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "prod_grp_typ_cd", referencedColumnName = "prod_grp_typ_cd", insertable = false, nullable = false, updatable = false)
	private ProductGroupType productGroupType;

	@Transient
	private GenericEntity genericEntity;

	@JsonIgnoreProperties("customerProductGroup")
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "customerProductGroup")
	@Where(clause = "img_subj_typ_cd='PGRP'")
	private List<ImageMetaData> imageData;
	@Transient
	private String action;
	@JsonIgnoreProperties("customerProductGroup")
	public ImageMetaData getPrimaryImage(){
		if(imageData!=null && imageData.size()>0){
			for(ImageMetaData img:imageData){
				if(img.isActiveOnline() && img.isActive() && img.getImagePriorityCode().equalsIgnoreCase("P")){
					return img;
				}
			}
		}
		return null;
	}

	/**
	 * is active online
	 * @return
	 */
	public boolean getActiveOnlineImage(){
		for(ImageMetaData img:imageData){
			if(img.isActiveOnline()){
				return true;
			}
		}
		return false;
	}

	@Transient
	private GenericEntity lowestEntity;

	/**
	 * Gets lowest entity.
	 * @return lowest entity.
	 */
	public GenericEntity getLowestEntity() {
		return lowestEntity;
	}

	/**
	 * Set lowest entity.
	 * @param lowestEntity the lowest entity.
	 */
	public void setLowestEntity(GenericEntity lowestEntity) {
		this.lowestEntity = lowestEntity;
	}

	/**
	 * Get generic entity.
	 * @return generic entity.
	 */
	public GenericEntity getGenericEntity() {
		return genericEntity;
	}

	/**
	 * Set generic entity.
	 * @param genericEntity generic entity.
	 */
	public void setGenericEntity(GenericEntity genericEntity) {
		this.genericEntity = genericEntity;
	}

	/**
	 * Get image meta data.
	 * @return image meta data.
	 */
	public List<ImageMetaData> getImageData() {
		return imageData;
	}

	/**
	 * Set image metadata.
	 * @param imageData image metadata.
	 */
	public void setImageData(List<ImageMetaData> imageData) {
		this.imageData = imageData;
	}

	/**
	 * Get customer product group description long.
	 * @return customer product group description long.
	 */
	public String getCustProductGroupDescriptionLong() {
		return custProductGroupDescriptionLong;
	}

	/**
	 * Set customer product group description long.
	 * @param custProductGroupDescriptionLong customer product group description long.
	 */
	public void setCustProductGroupDescriptionLong(String custProductGroupDescriptionLong) {
		this.custProductGroupDescriptionLong = custProductGroupDescriptionLong;
	}

	/**
	 * Gets the id of CustomerProductGroup.
	 *
	 * @return the id of CustomerProductGroup.
	 */
	public long getCustProductGroupId() {
		return custProductGroupId;
	}

	/**
	 * Sets the id of CustomerProductGroup.
	 *
	 * @param custProductGroupId the id of CustomerProductGroup.
	 */
	public void setCustProductGroupId(long custProductGroupId) {
		this.custProductGroupId = custProductGroupId;
	}

	/**
	 * Gets the product group type code.
	 *
	 * @return the product group type code.
	 */
	public String getProductGroupTypeCode() {
		return productGroupTypeCode;
	}

	/**
	 * Sets the product group type code.
	 *
	 * @param productGroupTypeCode the product group type code.
	 */
	public void setProductGroupTypeCode(String productGroupTypeCode) {
		this.productGroupTypeCode = productGroupTypeCode;
	}

	/**
	 * Gets the cust product group name.
	 *
	 * @return the cust product group name.
	 */
	public String getCustProductGroupName() {
		return custProductGroupName;
	}

	/**
	 * Sets  the cust product group name.
	 *
	 * @param custProductGroupName the cust product group name.
	 */
	public void setCustProductGroupName(String custProductGroupName) {
		this.custProductGroupName = custProductGroupName;
	}

	/**
	 * Gets the cust product group description.
	 *
	 * @return the cust product group description.
	 */
	public String getCustProductGroupDescription() {
		return custProductGroupDescription;
	}

	/**
	 * Sets the cust product group description.
	 *
	 * @param custProductGroupDescription the cust product group description.
	 */
	public void setCustProductGroupDescription(String custProductGroupDescription) {
		this.custProductGroupDescription = custProductGroupDescription;
	}

	/**
	 * Get the create date.
	 *
	 * @return create date
	 */
	public LocalDateTime getCreateDate() {
		return createDate;
	}

	/**
	 * Set the create date.
	 *
	 * @param createDate the create date.
	 */
	public void setCreateDate(LocalDateTime createDate) {
		this.createDate = createDate;
	}

	/**
	 * Get the user id that created.
	 *
	 * @return created user id.
	 */
	public String getCreatedUserId() {
		return createdUserId;
	}

	/**
	 *Set the user id that created.
	 *
	 * @param createdUserId the create user id.
	 */
	public void setCreatedUserId(String createdUserId) {
		this.createdUserId = createdUserId;
	}

	/**
	 * Get the last update date.
	 *
	 * @return the last update date.
	 */
	public LocalDateTime getLastUpdateDate() {
		return lastUpdateDate;
	}

	/**
	 * Set the last update date.
	 *
	 * @param lastUpdateDate the last update date.
	 */
	public void setLastUpdateDate(LocalDateTime lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}

	/**
	 * Get the user id that did last update.
	 *
	 * @return
	 */
	public String getLastUpdateUserId() {
		return lastUpdateUserId;
	}

	/**
	 * Set the user id that did last update.
	 *
	 * @param lastUpdateUserId
	 */
	public void setLastUpdateUserId(String lastUpdateUserId) {
		this.lastUpdateUserId = lastUpdateUserId;
	}

	/**
	 * Get the product group type.
	 * @return the product group type.
	 */
	public ProductGroupType getProductGroupType() {
		return productGroupType;
	}

	/**
	 * Sets the product group type.
	 *
	 * @param productGroupType the product group type.
	 */
	public void setProductGroupType(ProductGroupType productGroupType) {
		this.productGroupType = productGroupType;
	}
	/**
	 * Get the action.
	 *
	 * @return return it will be the add/update/delete.
	 */
	public String getAction() {
		return action;
	}

	/**
	 * Sets the action.
	 *
	 * @param action it will be the add/update/delete.
	 */
	public void setAction(String action) {
		this.action = action;
	}

	/**
	 * Returns a string representation of the object.
	 *
	 * @return A string representation of the object.
	 */
	@Override
	public String toString() {
		return "CustomerProductGroup{" +
				"custProductGroupId=" + custProductGroupId +'\'' +
				", productGroupTypeCode='" + productGroupTypeCode + '\'' +
				", custProductGroupName=" + custProductGroupName +'\'' +
				", custProductGroupDescription=" + custProductGroupDescription +'\'' +
				", productGroupType=" + productGroupType +'\'' +
				", genericEntity="+(getGenericEntity()==null?"null":getGenericEntity().toString())+'\'' +
				", lowest="+getLowestEntity()+
				'}';
	}
}
