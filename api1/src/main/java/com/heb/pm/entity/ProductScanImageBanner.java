package com.heb.pm.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Entity representing the product scan image banner
 * @author s753601
 * @version 2.13.0
 */
@Entity
@Table(name = "PROD_SCN_IMG_BNNR")
public class ProductScanImageBanner implements Serializable{
	/**
	 * PRIMARY_IMAGE.
	 */
	public static final String PRIMARY_IMAGE = "P";
	public static final String IMAGE_APPROVE_CODE = "A";

	@EmbeddedId
	private ProductScanImageBannerKey key;

	@Column(name = "ACTV_SW")
	private Boolean activeSwitch;

    @Column(name = "ACTV_ONLIN_SW")
	private Boolean activeOnlineSwitch;

	@Column(name = "IMG_PRTY_CD")
	private String imagePriorityCode;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SALS_CHNL_CD", referencedColumnName = "SALS_CHNL_CD", insertable = false, updatable = false)
	private SalesChannel salesChannel;

	@JsonIgnoreProperties("productScanImageBannerList")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumns({
			@JoinColumn(name = "scn_cd_id", referencedColumnName = "scn_cd_id", insertable = false, updatable = false),
			@JoinColumn(name = "seq_nbr", referencedColumnName = "seq_nbr", insertable = false, updatable = false)
	})
	private ProductScanImageURI productScanImageURI;

	/**
	 * Returns the complex key used to identify the banner
	 * @return key
	 */
	public ProductScanImageBannerKey getKey() {
		return key;
	}

	/**
	 * Updates the key
	 * @param key the new key
	 */
	public void setKey(ProductScanImageBannerKey key) {
		this.key = key;
	}

	/**
	 * Returns image prirotiy code likie P/A for Primary/Alternate.
	 * @return priority code.
	 */
    public String getImagePriorityCode() {
        return imagePriorityCode;
    }

	/**
	 * Sets image priority code.
	 * @param imagePriorityCode
	 */
	public void setImagePriorityCode(String imagePriorityCode) {
        this.imagePriorityCode = imagePriorityCode;
    }

    /**
	 * Returns the SalesChannel object tied to the productScanBanner this is used as the destination field in
	 * 		Product Details->Selling Unit->Image Info
	 * @return the SalesChannel
	 */
	public SalesChannel getSalesChannel() {
		return salesChannel;
	}

	/**
	 * Updates the SalesChannel Object
	 * @param salesChannel the new SalesChannel Object
	 */
	public void setSalesChannel(SalesChannel salesChannel) {
		this.salesChannel = salesChannel;
	}

	public Boolean getActiveSwitch() {
		return activeSwitch;
	}

	public void setActiveSwitch(Boolean activeSwitch) {
		this.activeSwitch = activeSwitch;
	}

    public Boolean getActiveOnlineSwitch() {
		return activeOnlineSwitch;
	}

	public void setActiveOnlineSwitch(Boolean activeOnlineSwitch) {
		this.activeOnlineSwitch = activeOnlineSwitch;
	}

	/**
	 * @return Gets the value of productScanImageURI and returns productScanImageURI
	 */
	public void setProductScanImageURI(ProductScanImageURI productScanImageURI) {
		this.productScanImageURI = productScanImageURI;
	}

	/**
	 * Sets the productScanImageURI
	 */
	public ProductScanImageURI getProductScanImageURI() {
		return productScanImageURI;
	}

	@Override
	public String toString() {
		return "ProductScanImageBanner{" +
				"key=" + key +
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
		if (!(o instanceof ProductScanImageBanner)) {
			return false;
		}

		ProductScanImageBanner that = (ProductScanImageBanner) o;
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
