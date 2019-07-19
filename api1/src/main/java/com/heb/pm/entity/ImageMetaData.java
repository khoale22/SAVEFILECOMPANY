package com.heb.pm.entity;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Object that holds the metadata for an image and ties an image to a link in the custom hierarchy
 * @author s753601
 * @updated by vn86116
 * @version 2.13.0
 */
@Entity
@Table(name = "IMG_METADTA")
@TypeDefs({
		@TypeDef(name = "fixedLengthChar", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharType.class),
		@TypeDef(name = "fixedLengthCharPK", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharTypePK.class)
})
public class ImageMetaData implements Serializable{

	public static final boolean IMAGE_ACTIVATED = true;
	public static final boolean IMAGE_ACTIVATED_ONLINE = true;
	public static final String IMAGE_PRIORITY_TYPE = "P    ";
	public static final String IMAGE_PRODUCT_GROUP = "PGRP";
	public static final String IMAGE_CHOICE_OPTION_SUBJECT_TYPE = "CHCOP";
	public static final String IMAGE_ACCEPTABLE = "Y";
	public static final String IMAGE_UNACCEPTABLE = "N";
	public static final String IMAGE_REJECT = "R";



	@EmbeddedId
	private ImageMetaDataKey key;

	@Column(name = "URI_TXT")
	@Type(type = "fixedLengthChar")
	private String uriText;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "KEY_ID", referencedColumnName = "ENTY_ID", insertable = false, updatable = false)
	private GenericEntity entity;

	@Column(name = "ACTV_SW")
	private Boolean active;

	@Column(name = "ACTV_ONLIN_SW")
	private Boolean activeOnline;

	@Column(name = "IMG_PRTY_CD")
	@Type(type = "fixedLengthChar")
	private String imagePriorityCode;

	@Column(name = "IMG_TYP_CD")
	@Type(type = "fixedLengthChar")
	private String imageTypeCode;

	@Column(name = "IMG_FRMAT_CD")
	private String imageFormatCode;

	@Column(name = "IMG_CAT_CD")
	@Type(type = "fixedLengthChar")
	private String imageCategoryCode;

	@Column(name = "RESL_X_NBR")
	private String imageSizeX;

	@Column(name = "RESL_Y_NBR")
	private String imageSizeY;

	@Column(name = "IMG_ALT_TXT")
	private String imageAltText;

	@Column(name = "IMG_STAT_CD")
	@Type(type = "fixedLengthChar")
	private String imageStatusCode;

	@Column(name = "IMG_SRC_NM")
	private String imageSourceName;

	@Column(name = "CRE8_TS")
	private LocalDateTime createDate;

	@Column(name = "LST_UPDT_TS")
	private LocalDateTime lastUpdateDate;

	@Column(name = "LST_UPDT_UID")
	private String lastUpdateUserId;

	@Column(name = "SRC_SYSTEM_ID")
	private Long sourceSystemId;

	@Column(name = "IMG_ACPTABLE_SW")
	private String imageAceptable;

	@Transient
	private ProductScanImageURI productScanImageURI;

	@JsonIgnoreProperties("imageData")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumns({
			@JoinColumn(name = "KEY_ID", referencedColumnName = "cust_prod_grp_id", insertable= false, updatable = false, nullable = false)
	})
	private CustomerProductGroup customerProductGroup;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumns({
			@JoinColumn(name = "IMG_CAT_CD", referencedColumnName = "IMG_CAT_CD", insertable= false, updatable = false, nullable = false)
	})
	private ImageCategory imageCategory;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumns({
			@JoinColumn(name = "img_prty_cd", referencedColumnName = "img_prty_cd", insertable = false, updatable = false, nullable = false)
	})
	private ImagePriority imagePriority;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumns({
			@JoinColumn(name = "SRC_SYSTEM_ID", referencedColumnName = "SRC_SYSTEM_ID", insertable = false, updatable = false, nullable = false)
	})
	private SourceSystem sourceSystem;

	@ManyToOne(fetch = FetchType.EAGER)
	@NotFound(action = NotFoundAction.IGNORE)
	@JoinColumns({
			@JoinColumn(name = "IMG_STAT_CD", referencedColumnName = "IMG_STAT_CD", insertable = false, updatable = false, nullable = false)
	})
	private ImageStatus imageStatus;

	public CustomerProductGroup getCustomerProductGroup() {
		return customerProductGroup;
	}

	public ImageMetaData setCustomerProductGroup(CustomerProductGroup customerProductGroup) {
		this.customerProductGroup = customerProductGroup;
		return this;
	}

	/**
	 * Returns the acceptable codes that is used to update status for image on custom hierarchy screen.
	 * @return the acceptable code.
	 */
	public String getImageAcceptableToSaving(){
		if(StringUtils.trimToEmpty(imageStatusCode).equals(ImageStatus.ImageStatusCode.APPROVED.getName())){
			return IMAGE_ACCEPTABLE;
		}else if(StringUtils.trimToEmpty(imageStatusCode).equals(ImageStatus.ImageStatusCode.FORREVIEW.getName())){
			return IMAGE_UNACCEPTABLE;
		}else{
			return IMAGE_REJECT;
		}
	}

	/**
	 * Returns the key object that is used to uniquely identify the ImageMetaData
	 *
	 * @return key
	 */
	public ImageMetaDataKey getKey() {
		return key;
	}

	/**
	 * Updates the key
	 *
	 * @param key the new key
	 */
	public void setKey(ImageMetaDataKey key) {
		this.key = key;
	}

	/**
	 * Returns the URI code used to map the image meta data back to the product scan image uri object
	 * @return uriText
	 */
	public String getUriText() {
		return uriText;
	}

	/**
	 * Updates the uriText
	 *
	 * @param uriText the new uriText
	 */
	public void setUriText(String uriText) {
		this.uriText = uriText;
	}

	/**
	 * Returns the generic entity that the image is tied to
	 * @return entity
	 */
	public GenericEntity getEntity() {
		return entity;
	}

	/**
	 * Updates the entity
	 *
	 * @param entity the new entity
	 */
	public void setEntity(GenericEntity entity) {
		this.entity = entity;
	}

	/**
	 * @return Gets the value of active and returns active
	 */
	public void setActive(Boolean active) {
		this.active = active;
	}

	/**
	 * Sets the active
	 */
	public Boolean isActive() {
		return active;
	}

	/**
	 * @return Gets the value of activeOnline and returns activeOnline
	 */
	public void setActiveOnline(Boolean activeOnline) {
		this.activeOnline = activeOnline;
	}

	/**
	 * Sets the activeOnline
	 */
	public Boolean isActiveOnline() {
		return activeOnline;
	}

	/**
	 * Sets the imagePrimaryCode
	 */
	public void setImagePriorityCode(String imagePriorityCode) {
		this.imagePriorityCode = imagePriorityCode;
	}


	/**
	 * @return Gets the value of imagePriorityCode and returns imagePriorityCode
	 */
	public String getImagePriorityCode() {
		return imagePriorityCode;
	}

	/**
	 * Sets the imageFormatCode
	 */
	public void setImageFormatCode(String imageFormatCode) {
		this.imageFormatCode = imageFormatCode;
	}


	/**
	 * @return Gets the value of imageFormatCode and returns imageFormatCode
	 */
	public String getImageFormatCode() {
		return imageFormatCode;
	}

	/**
	 * Get image category code.
	 * @return imageCategoryCode.
	 */
	public String getImageCategoryCode() {
		return imageCategoryCode;
	}

	/**
	 * set image category code.
	 * @param imageCategoryCode the image category code.
	 */
	public void setImageCategoryCode(String imageCategoryCode) {
		this.imageCategoryCode = imageCategoryCode;
	}

	/**
	 * get image width.
	 * @return imageSizeX.
	 */
	public String getImageSizeX() {
		return imageSizeX;
	}

	/**
	 * Set image width.
	 * @param imageSizeX
	 */
	public void setImageSizeX(String imageSizeX) {
		this.imageSizeX = imageSizeX;
	}

	/**
	 * Get image height.
	 * @return imageSizeY
	 */
	public String getImageSizeY() {
		return imageSizeY;
	}

	/**
	 * Set image height.
	 * @param imageSizeY the image height.
	 */
	public void setImageSizeY(String imageSizeY) {
		this.imageSizeY = imageSizeY;
	}

	/**
	 * Get image alt text.
	 * @return imageAltText
	 */
	public String getImageAltText() {
		return imageAltText;
	}

	/**
	 *  Set image alt text.
	 * @param imageAltText the image alt text.
	 */
	public void setImageAltText(String imageAltText) {
		this.imageAltText = imageAltText;
	}

	/**
	 *Get image status code.
	 * @return imageStatusCode
	 */
	public String getImageStatusCode() {
		return imageStatusCode;
	}

	/**
	 * Set image status code.
	 * @param imageStatusCode the image status code.
	 */
	public void setImageStatusCode(String imageStatusCode) {
		this.imageStatusCode = imageStatusCode;
	}

	/**
	 * Get image source name.
	 * @return imageSourceName
	 */
	public String getImageSourceName() {
		return imageSourceName;
	}

	/**
	 *  Set image source name.
	 * @param imageSuorceName the image source name.
	 */
	public void setImageSourceName(String imageSuorceName) {
		this.imageSourceName = imageSuorceName;
	}

	/**
	 * Get create date.
	 * @return createDate
	 */
	public LocalDateTime getCreateDate() {
		return createDate;
	}

	/**
	 * Set create date.
	 * @param createDate the create date.
	 */
	public void setCreateDate(LocalDateTime createDate) {
		this.createDate = createDate;
	}

	/**
	 * Get last update date.
	 * @return lastUpdateDate
	 */
	public LocalDateTime getLastUpdateDate() {
		return lastUpdateDate;
	}

	/**
	 * Set last update date.
	 * @param lastUpdateDate the last update date
	 */
	public void setLastUpdateDate(LocalDateTime lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}

	/**
	 * Get last update user id.
	 * @return lastUpdateUserId
	 */
	public String getLastUpdateUserId() {
		return lastUpdateUserId;
	}

	/**
	 * Set last update user id.
	 * @param lastUpdateUserId the last update user id.
	 */
	public void setLastUpdateUserId(String lastUpdateUserId) {
		this.lastUpdateUserId = lastUpdateUserId;
	}

	/**
	 * get source system id.
	 * @return sourceSystemId
	 */
	public Long getSourceSystemId() {
		return sourceSystemId;
	}

	/**
	 * Set source system id.
	 * @param sourceSystemId the source system id.
	 */
	public void setSourceSystemId(Long sourceSystemId) {
		this.sourceSystemId = sourceSystemId;
	}

	/**
	 * Get image acceptable.
	 * @return imageAceptable.
	 */
	public String getImageAcceptable() {
		return imageAceptable;
	}

	/**
	 * Set image acceptable.
	 * @param imageAceptable the image acceptable.
	 */
	public void setImageAcceptable(String imageAceptable) {
		this.imageAceptable = imageAceptable;
	}

	/**
	 * Get image category.
	 * @return imageCategory
	 */
	public ImageCategory getImageCategory() {
		return imageCategory;
	}

	/**
	 * Set image category.
	 * @param imageCategory the image category.
	 */
	public void setImageCategory(ImageCategory imageCategory) {
		this.imageCategory = imageCategory;
	}

	/**
	 * Get image primary.
	 * @return imagePrimary
	 */
	public ImagePriority getImagePriority() {
		return imagePriority;
	}

	/**
	 * Set image primary.
	 * @param imagePriority the image Primary
	 */
	public void setImagePriority(ImagePriority imagePriority) {
		this.imagePriority = imagePriority;
	}

	/**
	 * Get source system.
	 * @return sourceSystem
	 */
	public SourceSystem getSourceSystem() {
		return sourceSystem;
	}

	/**
	 * Set source system.
	 * @param sourceSystem the source system.
	 */
	public void setSourceSystem(SourceSystem sourceSystem) {
		this.sourceSystem = sourceSystem;
	}

	/**
	 * Get image status.
	 * @return imageStatus.
	 */
	public ImageStatus getImageStatus() {
		return imageStatus;
	}

	/**
	 * Set image status.
	 * @param imageStatus the image Status.
	 */
	public void setImageStatus(ImageStatus imageStatus) {
		this.imageStatus = imageStatus;
	}

	/**
	 * Get image category name.
	 * @return image category name.
	 */
	public String getImageCategoryName(){
		return this.imageCategory.getDescription();
	}

	/**
	 * returns the image info for the image meta data
	 * @return
	 */
	public ProductScanImageURI getProductScanImageURI() {
		return productScanImageURI;
	}

	/**
	 * Updates the image info
	 * @param productScanImageURI
	 */
	public void setProductScanImageURI(ProductScanImageURI productScanImageURI) {
		this.productScanImageURI = productScanImageURI;
	}

	/**
	 * returns the image aceptable flag for an image meta data
	 * @return
	 */
	public String getImageAceptable() {
		return imageAceptable;
	}

	/**
	 * updates the image aceptable flag
	 * @param imageAceptable
	 */
	public void setImageAceptable(String imageAceptable) {
		this.imageAceptable = imageAceptable;
	}

	/**
	 * Returns the UPC associated with an image
	 * @return
	 */
	public Long getReferenceUPC(){
		if(this.productScanImageURI == null){
			return null;
		} else {
			return this.productScanImageURI.getKey().getId();
		}
	}

	/**
	 * returns the iamge type for an image meta data
	 * @return
	 */
	public String getImageTypeCode() {
		return imageTypeCode;
	}

	/**
	 * updates the image type
	 * @param imageTypeCode
	 */
	public void setImageTypeCode(String imageTypeCode) {
		this.imageTypeCode = imageTypeCode;
	}


	/**
	 * Returns a string stating the dimensions of an image
	 * @return dimensions
	 */
	public String getDimensions(){
		if(this.imageSizeX != null && this.imageSizeY != null){
			return this.imageSizeX + "x\n" + this.imageSizeY;
		}
		return "";
	}

	/**
	 * Compares another object to this one. If that object is a ImportItem, it uses they keys
	 * to determine if they are equal and ignores non-key values for the comparison.
	 *
	 * @param o The object to compare to.
	 * @return True if they are equal and false otherwise.
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		ImageMetaData that = (ImageMetaData) o;

		return this.key != null ? this.key.equals(that.key) : that.key == null;
	}

	/**
	 * Returns a hash code for this object. If two objects are equal, they have the same hash. If they are not equal,
	 * they have different hash codes.
	 * @return The hash code for this obejct.
	 */
	@Override
	public int hashCode() {
		return this.key != null ? this.key.hashCode() : 0;
	}

	/**
	 * Returns a String representation of the object.
	 *
	 * @return A String representation of the object.
	 */
	@Override
	public String toString() {
		return "ImageMetaData{" +
				"key=" + key +
				", uriText=" + uriText +
				", entity=" + entity +
				'}';
	}
}
