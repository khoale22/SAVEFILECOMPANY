package com.heb.pm.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.springframework.data.domain.Sort;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * Created by m594201 on 8/11/2017.
 */
@Entity
@Table(name = "prod_brnd")
@TypeDef(name = "fixedLengthCharPK", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharTypePK.class)
public class ProductBrand implements Serializable {

	private static final long serialVersionUID = 1L;

	private static final String DISPLAY_NAME_FORMAT = "%s [%d]";

	private static final String DEFAULT_PRODUCT_BRAND_SORT_FIELD = "productBrandId";

	public static final String BLANK = "      ";

	@Id
	@Column(name = "prod_brnd_id")
	private Long productBrandId;

	@Type(type="fixedLengthCharPK")
	@Column(name = "prod_brnd_des")
	private String productBrandDescription;

	@Formula("TRIM(BOTH ' ' FROM prod_brnd_des)")
	private String trimmedProductBrandDescription;

	@Column(name = "prod_brnd_tier_id")
	private Long productBrandTierId;

	@Column(name = "prod_brnd_abb")
	private String productBrandAbbreviation;

	@Column(name = "show_on_web_sw")
	private Boolean showOnWeb;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "prod_brnd_tier_id", referencedColumnName = "prod_brnd_tier_id" , insertable = false, updatable = false)
	private ProductBrandTier productBrandTier;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "prod_brnd_tier_id", referencedColumnName = "PROD_BRND_TIER_ID" , insertable = false, updatable = false)
	private ProductBrandTier productBrandTier2;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "prod_brnd_tier_id", referencedColumnName = "PROD_BRND_TIER_ID" , insertable = false, updatable = false)
	private ProductBrandTier productBrandTier3;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "prod_brnd_tier_id", referencedColumnName = "prod_brnd_tier_id" , insertable = false, updatable = false)
	private ProductBrandTier productBrandTier4;


	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "prod_brnd_tier_id", referencedColumnName = "prod_brnd_tier_id" , insertable = false, updatable = false)
	private ProductBrandTier productBrandTier5Test;


	@JsonIgnoreProperties("productBrand")
	@OneToMany(mappedBy = "productBrand",fetch = FetchType.LAZY)
	private List<ProductBrandCostOwner> productBrandCostOwners;

	public Long getProductBrandId() {
		return productBrandId;
	}

	public ProductBrand setProductBrandId(Long productBrandId) {
		this.productBrandId = productBrandId;
		return this;
	}

	public String getProductBrandDescription() {
		return productBrandDescription;
	}

	public void setProductBrandDescription(String productBrandDescription) {
		this.productBrandDescription = productBrandDescription;
	}

	public String getTrimmedProductBrandDescription() {
		return trimmedProductBrandDescription;
	}

	public void setTrimmedProductBrandDescription(String trimmedProductBrandDescription) {
		this.trimmedProductBrandDescription = trimmedProductBrandDescription;
	}

	public ProductBrandTier getProductBrandTier() {
		return productBrandTier;
	}

	public void setProductBrandTier(ProductBrandTier productBrandTier) {
		this.productBrandTier = productBrandTier;
	}

	public Long getProductBrandTierId() {
		return productBrandTierId;
	}

	public void setProductBrandTierId(Long productBrandTierId) {
		this.productBrandTierId = productBrandTierId;
	}

	public ProductBrandTier getProductBrandTier2() {
		return productBrandTier2;
	}

	public void setProductBrandTier2(ProductBrandTier productBrandTier2) {
		this.productBrandTier2 = productBrandTier2;
	}

	public ProductBrandTier getProductBrandTier3() {
		return productBrandTier3;
	}

	public void setProductBrandTier3(ProductBrandTier productBrandTier3) {
		this.productBrandTier3 = productBrandTier3;
	}

	public ProductBrandTier getProductBrandTier4() {
		return productBrandTier4;
	}

	public void setProductBrandTier4(ProductBrandTier productBrandTier4) {
		this.productBrandTier4 = productBrandTier4;
	}

	/**
	 * Returns the product brand abb.
	 *
	 * @return the product brand abb.
	 */
	public String getProductBrandAbbreviation() {
		return productBrandAbbreviation;
	}

	/**
	 * Set the product brand abb.
	 *
	 * @param productBrandAbbreviation the product brand abb.
	 */
	public void setProductBrandAbbreviation(String productBrandAbbreviation) {
		this.productBrandAbbreviation = productBrandAbbreviation;
	}
	/**
	 * Returns the show on web sw.
	 *
	 * @return the show on web sw.
	 */
	public Boolean getShowOnWeb() {
		return showOnWeb;
	}
	/**
	 * Set the show on web sw.
	 *
	 * @param showOnWeb the show on web sw.
	 */
	public void setShowOnWeb(Boolean showOnWeb) {
		this.showOnWeb = showOnWeb;
	}

	/**
	 * Gets the list of product Brand Cost Owners.
	 *
	 * @return the list of product Brand Cost Owners.
	 */
	public List<ProductBrandCostOwner> getProductBrandCostOwners() {
		return productBrandCostOwners;
	}

	/**
	 * Sets the list of product Brand Cost Owners.
	 *
	 * @param productBrandCostOwners the list of product Brand Cost Owners.
	 */
	public void setProductBrandCostOwners(List<ProductBrandCostOwner> productBrandCostOwners) {
		this.productBrandCostOwners = productBrandCostOwners;
	}

	/**
	 * Returns a display name for a ProductBrand to display on the GUI.
	 *
	 * @return A display name.
	 */
	public String getDisplayName() {
		return this.productBrandDescription == null ? this.productBrandId.toString() :
				String.format(ProductBrand.DISPLAY_NAME_FORMAT, this.productBrandDescription.trim(), this.productBrandId);
	}


	/**
	 * Returns the default sort order for the product brand table.
	 *
	 * @return The default sort order for the product brand table.
	 */
	public static Sort getDefaultSort() {
		return new Sort(
				new Sort.Order(Sort.Direction.ASC, ProductBrand.DEFAULT_PRODUCT_BRAND_SORT_FIELD)
		);
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

		ProductBrand productBrand = (ProductBrand) o;

		return productBrandId != null ? productBrandId.equals(productBrand.productBrandId) : productBrand.productBrandId == null;
	}

	/**
	 * Returns a hash for this object. If two objects are equal, they will have the same hash. If they are not,
	 * they will (probably) have different hashes.
	 *
	 * @return The hash code for this object.
	 */
	@Override
	public int hashCode() {
		return productBrandId != null ? productBrandId.hashCode() : 0;
	}

	/**
	 * Returns a String representation of the object.
	 *
	 * @return A String representation of the object.
	 */
	@Override
	public String toString() {
		return "ProductBrand{" +
				"productBrandId=" + productBrandId +
				", productBrandDescription='" + productBrandDescription + '\'' +
				'}';
	}

}
