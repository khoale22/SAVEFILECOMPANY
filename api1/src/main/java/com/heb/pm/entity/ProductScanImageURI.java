package com.heb.pm.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Holds information about a product's image information
 * @author s753601
 * @version 2.13.0
 */
@Entity
@Table(name = "prod_scn_img_uri")
@TypeDefs({
		@TypeDef(name = "fixedLengthChar", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharType.class),
		@TypeDef(name = "fixedLengthCharPK", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharTypePK.class)
})
public class ProductScanImageURI implements Serializable{

	private static final long serialVersionUID = 1L;

	public static String IMAGE_STATUS_CD_APPROVED ="A";
	public static String IMAGE_PRIORITY_CD_ALTERNATE ="A";
	public static String IMAGE_PRIORITY_CD_PRIMARY ="P";
	public static final String IMAGE_REJECT = "R";

	@EmbeddedId
	private ProductScanImageURIKey key;

	@Column(name = "ACTV_ONLIN_SW")
	private Boolean activeOnline;

	@Column(name = "IMG_ALT_TXT")
	private String altTag;

	@Column(name = "IMG_SRC_NM")
	@Type(type = "fixedLengthCharPK")
	private String applicationSource;

	@Column(name = "IMG_CAT_CD")
	@Type(type = "fixedLengthCharPK")
	private String imageCategoryCode;

	@Column(name = "CRE8_TS")
	private LocalDateTime createdDate;

	@Column(name = "CRE8_ID")
	private String createdBy;

	@Column(name = "LST_UPDT_TS")
	private LocalDateTime lastModifiedOn;

	@Column(name = "LST_UPDT_UID")
	private String lastModifiedBy;

	@Column(name = "RESL_X_NBR")
	private Integer xAxisResolution;

	@Column(name = "RESL_Y_NBR")
	private Integer yAxisResolution;

	@Column(name = "IMG_ACPTABLE_SW")
	private String imageAccepted;

	@Column(name = "IMG_STAT_RSN_TXT")
	private String imageStatusReason;

	@Column(name= "IMG_STAT_CD")
	private String imageStatusCode;

	@Column(name = "URI_TXT")
	@Type(type = "fixedLengthCharPK")
	private String imageURI;

	@Column(name = "IMG_TYP_CD")
	private String imageTypeCode;

	@Column(name = "IMG_PRTY_CD")
	@Type(type = "fixedLengthCharPK")
	private String imagePriorityCode;

	@Column(name = "SRC_SYSTEM_ID")
	private Integer sourceSystemId;

	@Column(name = "ACTV_SW")
	private Boolean activeSwitch;

	@Column(name = "REVD_TS")
	private LocalDateTime revisedTimeStamp;

	@Column(name = "REVD_BY_UID")
	private String revisedByID;

	@Column(name = "img_frmat_cd")
	private String imageFormat;

	@Column(name = "img_lndscp_sw")
	private String imglndscpsw;

	public String getImglndscpsw() {
		return imglndscpsw;
	}

	public void setImglndscpsw(String imglndscpsw) {
		this.imglndscpsw = imglndscpsw;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "IMG_CAT_CD", referencedColumnName = "IMG_CAT_CD", insertable = false, updatable = false)
	private ImageCategory imageCategory;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "IMG_TYP_CD", referencedColumnName = "IMG_TYP_CD", insertable = false, updatable = false)
	private ImageType imageType;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "IMG_STAT_CD", referencedColumnName = "IMG_STAT_CD", insertable = false, updatable = false)
	private ImageStatus imageStatus;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "IMG_PRTY_CD", referencedColumnName = "IMG_PRTY_CD", insertable = false, updatable = false)
	private ImagePriority imagePriority;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SRC_SYSTEM_ID", referencedColumnName = "SRC_SYSTEM_ID", insertable = false, updatable = false)
	private SourceSystem sourceSystem;

	@JsonIgnoreProperties("productScanImageURI")
	@OneToMany(mappedBy = "productScanImageURI", fetch = FetchType.LAZY)
	private List<ProductScanImageBanner> productScanImageBannerList;


	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "IMG_SRC_NM", referencedColumnName = "IMG_SRC_NM", insertable = false, updatable = false)
	private ImageSource imageSource;

	@JsonIgnoreProperties("productScanImageURI")
	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "URI_TXT", referencedColumnName = "URI_TXT", insertable = false, updatable = false)
	private List<ImageMetaData> imageMetaDataList;

	@JsonIgnoreProperties("productScanImageURI")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "scn_cd_id", referencedColumnName = "scn_cd_id", insertable = false, updatable = false)
	private SellingUnit sellingUnit;

	@Transient
	private byte[] image;

	@Transient
	private DestinationChanges destinationChanges;

	/**
	 * Retruns the key to uniquely identify the product scan image
	 * @return the key
	 */
	public ProductScanImageURIKey getKey() {
		return key;
	}

	/**
	 * Updates the key
	 * @param key the new key
	 */
	public void setKey(ProductScanImageURIKey key) {
		this.key = key;
	}

	/**
	 * Returns glag indicator if image is active on heb.com.
	 * @return activeOnline
	 */
	public Boolean isActiveOnline() {
		return activeOnline;
	}

	/**
	 * Updates activeOnline
	 * @param activeOnline the new activeOnline
	 */
	public void setActiveOnline(Boolean activeOnline) {
		this.activeOnline = activeOnline;
	}

	/**
	 * Returns verbiage customers see when hovering over image on heb.com.
	 * @return altTag
	 */
	public String getAltTag() {
		return altTag;
	}

	/**
	 * Updates the altTag
	 * @param altTag the new altTag
	 */
	public void setAltTag(String altTag) {
		this.altTag = altTag;
	}

	/**
	 * Returns same of the source from which image came from.
	 * @return applicationSource
	 */
	public String getApplicationSource() {
		return applicationSource;
	}

	/**
	 * Updates the applcationSource
	 * @param applicationSource the new appicationSource
	 */
	public void setApplicationSource(String applicationSource) {
		this.applicationSource = applicationSource;
	}

	/**
	 * Returns category image is assigned to when uploaded, could be:
	 * Beauty Shot (BSHOT)
	 * Product Image (PROD)
	 * Swatches (SWAT)
	 * @return
	 */
	public String getImageCategoryCode() {
		return imageCategoryCode;
	}

	/**
	 * Updates imageCategoryCode
	 * @param imageCategoryCode the new imageCategoryCode
	 */
	public void setImageCategoryCode(String imageCategoryCode) {
		this.imageCategoryCode = imageCategoryCode;
	}

	/**
	 * Returns the date the image was created in the system.
	 * @return createdDate
	 */
	public LocalDateTime getCreatedDate() {
		return createdDate;
	}

	/**
	 * Updates createdDate
	 * @param createdDate the new createdDate
	 */
	public void setCreatedDate(LocalDateTime createdDate) {
		this.createdDate = createdDate;
	}

	/**
	 * Returns the date the image was last modified on.
	 * @return lastModifiedOn
	 */
	public LocalDateTime getLastModifiedOn() {
		return lastModifiedOn;
	}

	/**
	 * Updates lastModifiedOn
	 * @param lastModifiedOn the new lastModifiedOn
	 */
	public void setLastModifiedOn(LocalDateTime lastModifiedOn) {
		this.lastModifiedOn = lastModifiedOn;
	}

	/**
	 * Returns the user who last modified the image.
	 * @return lastModifiedBy
	 */
	public String getLastModifiedBy() {
		return lastModifiedBy;
	}

	/**
	 * Updates lastModifiedBy
	 * @param lastModifiedBy the new lastModifiedBy
	 */
	public void setLastModifiedBy(String lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}

	/**
	 * Returns the indicator if the image is flagged as primary image vs being flagged alternate.
	 * @return primaryImage
	 */


	/**
	 * Updates primaryImage
	 * @param primaryImage the new primaryImage
	 */

	/**
	 * Returns the pixel size of the image in the x direction
	 * @return xAxisResolution
	 */
	public Integer getxAxisResolution() {
		return xAxisResolution;
	}

	/**
	 * Updates xAxisResolution
	 * @param xAxisResolution the new xAxisResolution
	 */
	public void setxAxisResolution(Integer xAxisResolution) {
		this.xAxisResolution = xAxisResolution;
	}

	/**
	 * Returns the pixel size of the image in the y direction
	 * @return yAxisResolution
	 */
	public Integer getyAxisResolution() {
		return yAxisResolution;
	}

	/**
	 * Updates yAxisResolution
	 * @param yAxisResolution the new yAxisResolution
	 */
	public void setyAxisResolution(Integer yAxisResolution) {
		this.yAxisResolution = yAxisResolution;
	}

	/**
	 * Returns the status of the image; where it's been approved, rejected, or still in review by eCommerce Team.
	 * @return imageStatus
	 */
	public String getImageAccepted() {
		return this.imageAccepted;
	}

	/**
	 * Updates imageStatus
	 * @param imageAccepted the new imageStatus
	 */
	public void setImageAccepted(String imageAccepted) {
		this.imageAccepted = imageAccepted;
	}

	/**
	 * Category image is assigned to when uploaded
	 * @return imageCategory
	 */
	public ImageCategory getImageCategory() {
		return imageCategory;
	}

	/**
	 * Updates imageCategory
	 * @param imageCategory the new imageCategory
	 */
	public void setImageCategory(ImageCategory imageCategory) {
		this.imageCategory = imageCategory;
	}

	/**
	 * Returns if image rejected or has another reason message
	 * @return imageStatusReason
	 */
	public String getImageStatusReason() {
		return imageStatusReason;
	}

	/**
	 * Updates imageStatusReason
	 * @param imageStatusReason the new imageStatusReason
	 */
	public void setImageStatusReason(String imageStatusReason) {
		this.imageStatusReason = imageStatusReason;
	}

	/**
	 * Returns the ImageType object that stores image type information about the object
	 * @return imageStatus
	 */
	public ImageType getImageType() {
		return imageType;
	}

	/**
	 * Updates the imageType
	 * @param imageType the new imageType
	 */
	public void setImageType(ImageType imageType) {
		this.imageType = imageType;
	}

	/**
	 * Returns the ImageStatus object that stores the status of the object
	 * @return imageStatus
	 */
	public ImageStatus getImageStatus() {
		return imageStatus;
	}

	/**
	 * Updates the imageStatus
	 * @param imageStatus the new imageStatus
	 */
	public void setImageStatus(ImageStatus imageStatus) {
		this.imageStatus = imageStatus;
	}

	/**
	 * Returns the image Priority object that stores the priority of the productScanImageURI
	 * @return
	 */
	public ImagePriority getImagePriority() {
		return imagePriority;
	}

	/**
	 * Updates the imagePriority
	 * @param imagePriority the new imagePriority
	 */
	public void setImagePriority(ImagePriority imagePriority) {
		this.imagePriority = imagePriority;
	}

	/**
	 * Returns the name of the user who created the record
	 * @return createdBy
	 */
	public String getCreatedBy() {
		return createdBy;
	}

	/**
	 * Updates the createdBy
	 * @param createdBy the new createdBy
	 */
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	/**
	 * Returns the unique identifier that ties image status to this object
	 * @return imageStatusCode
	 */
	public String getImageStatusCode() {
		return imageStatusCode;
	}

	/**
	 * Updates the imageStatusCode
	 * @param imagStatusCode the new imageStatusCode
	 */
	public void setImageStatusCode(String imagStatusCode) {
		this.imageStatusCode = imagStatusCode;
	}

	/**
	 * Returns the URI string used to get the image from the webservice
	 * @return imageURI
	 */
	public String getImageURI() {
		return imageURI;
	}

	/**
	 * Updates the imageURI
	 * @return the new imageURI
	 */
	public void setImageURI(String imageURI) {
		this.imageURI = imageURI;
	}

	/**
	 * Returns the image source object that hold the information about the source of an image
	 * @return imageSource
	 */
	public ImageSource getImageSource() {
		return imageSource;
	}

	/**
	 * Updates the ImageSource Object
	 * @param imageSource the new ImageSource
	 */
	public void setImageSource(ImageSource imageSource) {
		this.imageSource = imageSource;
	}

	/**
	 * Returns a list of SalesChannel that holds the destinations for the images
	 * @return destinations
	 */
	public List<SalesChannel> getDestinations(){
		ArrayList<SalesChannel> destinations = new ArrayList<>();
		for (ProductScanImageBanner banner: productScanImageBannerList) {
			destinations.add(banner.getSalesChannel());
		}
		return destinations;
	}

	/**
	 * Returns a string stating the dimensions of an image
	 * @return dimensions
	 */
	public String getDimensions(){
		if(this.xAxisResolution != null && this.yAxisResolution != null){
			return this.xAxisResolution + "x\n" + this.yAxisResolution;
		}
		return "";
	}

	/**
	 * Return the unique identifier that ties an image type to the productScanImageURI
	 * @return imageTypeCode
	 */
	public String getImageTypeCode() {
		return imageTypeCode;
	}

	/**
	 * Updates the imageTypeCode
	 * @param imageTypeCode the new imageTypeCode
	 */
	public void setImageTypeCode(String imageTypeCode) {
		this.imageTypeCode = imageTypeCode;
	}

	/**
	 * Returns the source system identifier
	 * @return sourceSystemId
	 */
	public Integer getSourceSystemId() {
		return sourceSystemId;
	}

	/**
	 * Updates the sourceSystemId
	 * @param sourceSystemId the new sourceSystemId
	 */
	public void setSourceSystemId(Integer sourceSystemId) {
		this.sourceSystemId = sourceSystemId;
	}

	/**
	 * Returns the application source for an image
	 * @return sourceSystem
	 */
	public SourceSystem getSourceSystem() {
		return sourceSystem;
	}

	/**
	 * Updates the sourceSystem
	 * @param sourceSystem the new sourceSystem
	 */
	public void setSourceSystem(SourceSystem sourceSystem) {
		this.sourceSystem = sourceSystem;
	}

	/**
	 * Returns the ImagePriorityCode that ties the ImagePriority to the object
	 * @return imagePriorityCode
	 */
	public String getImagePriorityCode() {
		return imagePriorityCode;
	}

	/**
	 * Updates the imagePriorityCode
	 * @param imagePriorityCode the new imagePriorityCode
	 */
	public void setImagePriorityCode(String imagePriorityCode) {
		this.imagePriorityCode = imagePriorityCode;
	}

	/**
	 * Returns the list of banners that hold the sales channel information
	 * @return productScanImageBannerList
	 */
	public List<ProductScanImageBanner> getProductScanImageBannerList() {
		return productScanImageBannerList;
	}

	/**
	 * Updates productScanImageBannerList
	 * @param productScanImageBannerList the new productScanImageBannerList
	 */
	public void setProductScanImageBannerList(List<ProductScanImageBanner> productScanImageBannerList) {
		this.productScanImageBannerList = productScanImageBannerList;
	}

	/**
	 * Returns a list of imageMetaData that holds where in the hierarchy the image is tied to.
	 * @return imageMetaDataList
	 */
	public List<ImageMetaData> getImageMetaDataList() {
		return imageMetaDataList;
	}

	/**
	 * Updates the imageMetaDataList
	 * @param imageMetaDataList the new imageMetaDataList
	 */
	public void setImageMetaDataList(List<ImageMetaData> imageMetaDataList) {
		this.imageMetaDataList = imageMetaDataList;
	}

	/**
	 * Gets the active status of an image
	 * @return
	 */
	public Boolean getActiveSwitch() {
		return activeSwitch;
	}

	/**
	 * Updates activeSwitch
	 * @param activeSwitch
	 */
	public void setActiveSwitch(Boolean activeSwitch) {
		this.activeSwitch = activeSwitch;
	}

	/**
	 * The revised timestamp
	 * @return
	 */
	public LocalDateTime getRevisedTimeStamp() {
		return revisedTimeStamp;
	}

	/**
	 * Updates the revised timestamp
	 * @param revisedTimeStamp
	 */
	public void setRevisedTimeStamp(LocalDateTime revisedTimeStamp) {
		this.revisedTimeStamp = revisedTimeStamp;
	}

	/**
	 * The id of who last revised the active stamp
	 * @return
	 */
	public String getRevisedByID() {
		return revisedByID;
	}

	/**
	 * update the revised id
	 * @param revisedByID
	 */
	public void setRevisedByID(String revisedByID) {
		this.revisedByID = revisedByID;
	}

	public String getImageFormat() {
		return imageFormat;
	}

	public void setImageFormat(String imageFormat) {
		this.imageFormat = imageFormat;
	}

	/**
	 * Returns image as binary.
	 * @return image binary data.
	 */
	public byte[] getImage() {
		return image;
	}

	/**
	 * Sets image binary content.
	 * @param image
	 */
	public void setImage(byte[] image) {
		this.image = image;
	}

	/**
	 * Gets the destination change of image.
	 *
	 * @return DestinationChanges
	 */
	public DestinationChanges getDestinationChanges() {
		return destinationChanges;
	}

	/**
	 * update the destination.
	 *
	 * @param destinationChanges the destination change of image.
	 */
	public void setDestinationChanges(DestinationChanges destinationChanges) {
		this.destinationChanges = destinationChanges;
	}

	/**
	 * Returns a String representation of the object.
	 *
	 * @return A String representation of the object.
	 */
	@Override
	public String toString() {
		return "ProductScanImageURI{" +
				"key=" + key +
				", activeOnline=" + activeOnline +
				", altTag='" + altTag + '\'' +
				", applicationSource='" + applicationSource + '\'' +
				", imageCategoryCode='" + imageCategoryCode + '\'' +
				", createdDate=" + createdDate +
				", createdBy='" + createdBy + '\'' +
				", lastModifiedOn=" + lastModifiedOn +
				", lastModifiedBy='" + lastModifiedBy + '\'' +
				", xAxisResolution=" + xAxisResolution +
				", yAxisResolution=" + yAxisResolution +
				", imageAccepted='" + imageAccepted + '\'' +
				", imageStatusReason='" + imageStatusReason + '\'' +
				", imageStatusCode='" + imageStatusCode + '\'' +
				", imageURI='" + imageURI + '\'' +
				", imageTypeCode='" + imageTypeCode + '\'' +
				", imagePriorityCode='" + imagePriorityCode + '\'' +
				", sourceSystemId=" + sourceSystemId +
				", activeSwitch=" + activeSwitch +
				", revisedTimeStamp=" + revisedTimeStamp +
				", revisedByID='" + revisedByID + '\'' +
				", imageCategory=" + imageCategory +
				", imageType=" + imageType +
				", imageStatus=" + imageStatus +
				", imagePriority=" + imagePriority +
				", sourceSystem=" + sourceSystem +
				", productScanImageBannerList=" + productScanImageBannerList +
				", imageSource=" + imageSource +
				", imageMetaDataList=" + imageMetaDataList +
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
		if (!(o instanceof ProductScanImageURI)) {
			return false;
		}

		ProductScanImageURI that = (ProductScanImageURI) o;
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
}
