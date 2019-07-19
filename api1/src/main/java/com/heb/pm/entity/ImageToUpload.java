package com.heb.pm.entity;

import java.util.List;

/**
 * This object hold all the data required to upload an image to the database
 * @author s753601
 * @version 2.13.0
 */
public class ImageToUpload {

	private static final int FOUR_BYTES = 32;


	private Long upc;
	private Long entityId;
	private String imageCategoryCode;
	private String imageSourceCode;
	private String imageName;
	private String userId;
	private List<SalesChannel> destinationList;
	private byte[] imageData;
	private String keyId;
	private String keyType;
	private boolean primary;
	private boolean showOnSite;
	private boolean alternate;

	private String imageURL;
	private String imageSource;

	private String existingImage;

	/**
	 * The upc the image will be attached to
	 * @return
	 */
	public Long getUpc() {
		return upc;
	}

	/**
	 * Returns the entity for the image meta data
	 * @return
	 */
	public Long getEntityId() {
		return entityId;
	}

	/**
	 * updates the entity id
	 * @param entityId
	 */
	public void setEntityId(Long entityId) {
		this.entityId = entityId;
	}

	/**
	 * Updates the upc
	 * @param upc the new upc
	 */
	public void setUpc(Long upc) {
		this.upc = upc;
	}

	/**
	 * The category for what kind of image that is being uploaded this value is tied to the Image Category code table
	 * @return imageCategoryCode
	 */
	public String getImageCategoryCode() {
		return imageCategoryCode;
	}

	/**
	 * Updates the iamgeCategoryCode
	 * @param imageCategoryCode the new imagecategoryCode
	 */
	public void setImageCategoryCode(String imageCategoryCode) {
		this.imageCategoryCode = imageCategoryCode;
	}

	/**
	 * The source of the image that be tied to the image source code table
	 * @return imageSourceCode
	 */
	public String getImageSourceCode() {
		return imageSourceCode;
	}

	/**
	 * Updates imageSourceCode
	 * @param imageSourceCode the new imageSourceCode
	 */
	public void setImageSourceCode(String imageSourceCode) {
		this.imageSourceCode = imageSourceCode;
	}

	/**
	 * All of the destinations where the image is used for each SalesChannel in the list a new product scan uri banner
	 * be created
	 * @return destinationList
	 */
	public List<SalesChannel> getDestinationList() {
		return destinationList;
	}

	/**
	 * Updates the destinationList
	 * @param destinationList the new destinationList
	 */
	public void setDestinationList(List<SalesChannel> destinationList) {
		this.destinationList = destinationList;
	}

	/**
	 * The name of the image that was upload includes file extension
	 * @return ImageName
	 */
	public String getImageName() {
		return imageName;
	}

	/**
	 * Updates the imageName
	 * @param imageName the new imageName
	 */
	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	/**
	 * The binary data of the image
	 * @return imageData
	 */
	public byte[] getImageData() {
		return this.imageData;
	}

	/**
	 * Updates the imageData
	 * @param imageData the new imageData
	 */
	public void setImageData(byte[] imageData) {
		this.imageData = imageData;
	}

	/**
	 * Gets the user who is requesting to upload an image
	 * @return
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * Updates the userId who is requesting to upload an image
	 * @param userId
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}
	/**
	 * The imageURL the image will be attached to
	 * @return
	 */
	public String getImageURL() {
		return imageURL;
	}
	/**
	 * Updates the imageURL
	 * @param imageURL the new imageURL
	 */
	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
	}
	/**
	 * The imageSource the image will be attached to
	 * @return
	 */
	public String getImageSource() {
		return imageSource;
	}
	/**
	 * Updates the imageSource
	 * @param imageSource the new imageSource
	 */
	public void setImageSource(String imageSource) {
		this.imageSource = imageSource;
	}

	/**
	 * Returns the action code for what to do to the current images existing primaries
	 * @return
	 */
	public String getExistingImage() {
		return existingImage;
	}

	/**
	 * Sets the action code for what to do to the current images existing primaries
	 * @return
	 */
	public void setExistingImage(String existingImage) {
		this.existingImage = existingImage;
	}

	/**
	 * Returns a String representation of the object.
	 *
	 * @return A String representation of the object.
	 */
	@Override
	public String toString() {
		return "ImageToUpload{" +
				"upc=" + upc +
				", imageCategoryCode='" + imageCategoryCode + '\'' +
				", imageSourceCode='" + imageSourceCode + '\'' +
				", imageName='" + imageName + '\'' +
				", userId='" + userId + '\'' +
				", destinationList=" + destinationList +
				", keyId='" + keyId + '\'' +
				", keyType='" + keyType + '\'' +
				", primary=" + primary +
				", imageURL='" + imageURL + '\'' +
				", imageSource='" + imageSource + '\'' +
				", existingImage='" + existingImage + '\'' +
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
		if (!(o instanceof ImageToUpload)) {
			return false;
		}

		ImageToUpload that = (ImageToUpload) o;
		if (this.upc != null ? !this.upc.equals(that.upc) : that.upc != null) return false;
		if (this.imageData != null ? !this.imageData.equals(that.imageData) : that.imageData != null) return false;
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
		int result = (int) (upc ^ (upc >>> FOUR_BYTES));
		result= result + imageData.hashCode();
		return result;
	}

	/**
	 * @return the keyId
	 */
	public String getKeyId() {
		return keyId;
	}

	/**
	 * @param keyId the keyId to set
	 */
	public void setKeyId(String keyId) {
		this.keyId = keyId;
	}

	/**
	 * @return the keyType
	 */
	public String getKeyType() {
		return keyType;
	}

	/**
	 * @param keyType the keyType to set
	 */
	public void setKeyType(String keyType) {
		this.keyType = keyType;
	}
	/**
	 * @return the primary
	 */
	public boolean isPrimary() {
		return primary;
	}
	/**
	 * @param primary the primary to set
	 */
	public void setPrimary(boolean primary) {
		this.primary = primary;
	}
	/**
	 * @return the showOnSite
	 */
	public boolean isShowOnSite() {
		return showOnSite;
	}
	/**
	 * @param showOnSite the show On Site to set
	 */
	public void setShowOnSite(boolean showOnSite) {
		this.showOnSite = showOnSite;
	}
	/**
	 * @return the alternate
	 */
	public boolean isAlternate() {
		return alternate;
	}
	/**
	 * @param alternate the alternate to set
	 */
	public void setAlternate(boolean alternate) {
		this.alternate = alternate;
	}

}
