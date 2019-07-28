package com.hellokoding.springboot.jpa.book;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class ProductBrand implements Serializable {

	private static final long serialVersionUID = 1L;

	private static final String DISPLAY_NAME_FORMAT = "%s [%d]";

	private static final String DEFAULT_PRODUCT_BRAND_SORT_FIELD = "productBrandId";

	public static final String BLANK = "      ";

	@Id
	@Column(name = "prod_brnd_id")
	private Long productBrandId;

	@Column(name = "prod_brnd_des")
	private String productBrandDescription;

	@JsonIgnore
	/*@JsonIgnoreProperties("productBrand")*/
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
