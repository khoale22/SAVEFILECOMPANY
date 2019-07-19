package com.heb.pm.entity;

import org.apache.commons.lang.StringUtils;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * Represents Product Relationship Data from prod_rlshp table.
 *
 * @author m594201
 * @since 2.8.0
 */
@Entity
@Table(name = "prod_rlshp")
public class ProductRelationship implements Serializable{

	private static final int PRIME_NUMBER = 31;
	private static final int FOUR_BYTES = 32;

	@EmbeddedId
	private ProductRelationshipKey key;

	@Column(name = "scn_cd_id")
	private Long elementUpc;

	@Column(name = "prod_qty")
	private Double productQuantity;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="related_prod_id", referencedColumnName = "PROD_ID", insertable = false, updatable = false, nullable = false)
	private ProductMaster relatedProduct;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="PROD_ID", referencedColumnName = "PROD_ID", insertable = false, updatable = false, nullable = false)
    private ProductMaster parentProduct;

	@Transient
	private String actionCode;

    @Transient
    private List<ProductRelationship> children;

	@Transient
	private List<Long> parentNodes;

	public static final String KIT_TYPE = "KIT";

	/**
	 * Represents known product relationship codes.
	 */
	public enum ProductRelationshipCode {
		ADD_ON_PRODUCT("ADDON", "ADD ON PRODUCT"),
		BREAK_PACK("BPACK", "BREAK PACK"),
		DEPOSIT_PRODUCT("DEPOS", "DEPOSIT PRODUCT"),
		FIXED_RELATED_PRODUCT("FPROD", "FIXED RELATED PRODUCT"),
		KIT_PRODUCT("KIT  ", "KIT PRODUCT"),
		UPSELL("USELL", "UPSELL"),
		Variant_Product("VARNT", "Variant Product");

		private String value;

		private String description;

		ProductRelationshipCode(String value, String description) {
			this.value = value;
			this.description = description;
		}

		public String getValue() {
			return this.value;
		}

		public String getDescription() {
			return this.description;
		}

		public static String getDescriptionByValue(String value) {
			for (ProductRelationshipCode p : ProductRelationshipCode.values()) {
				if (StringUtils.equals(value, p.value)) {
					return p.getDescription();
				}
			}
			return null;
		}
	}

	/**
	 * Gets related product master.
	 *
	 * @return the product master data that is joined by the related_prod_id.
	 */
	public ProductMaster getRelatedProduct() {
		return relatedProduct;
	}

	/**
	 * Sets related product master.
	 *
	 * @param relatedProduct the product master data that is joined by the related_prod_id.
	 */
	public void setRelatedProduct(ProductMaster relatedProduct) {
		this.relatedProduct = relatedProduct;
	}

	/**
	 * Gets scan code id.
	 *
	 * @return the scan code id identifier.
	 */
	public Long getElementUpc() {
		return elementUpc;
	}

	/**
	 * Sets scan code id.
	 *
	 * @param elementUpc the scan code id identifier.
	 */
	public void setElementUpc(Long elementUpc) {
		this.elementUpc = elementUpc;
	}

	/**
	 * Gets the elemental quantity.
	 *
	 * @return the elemental quantity.
	 */
	public Double getProductQuantity() {
		return productQuantity;
	}

	/**
	 * Sets the elemental quantity.
	 *
	 * @param productQuantity the elemental quantity.
	 */
	public void setProductQuantity(Double productQuantity) {
		this.productQuantity = productQuantity;
	}

	/**
	 * Returns the product relationship description by the product relationship code.
	 *
	 * @return the product relationship description.
	 */
	public String getProductRelationshipDescription() {
		return ProductRelationshipCode.getDescriptionByValue(this.getKey().getProductRelationshipCode());
	}

    /**
     * The returns the product master for the parent product
     * @return
     */
    public ProductMaster getParentProduct() {
        return parentProduct;
    }

    /**
     * Updates teh product master for the parent product
     * @param parentProduct
     */
    public void setParentProduct(ProductMaster parentProduct) {
        this.parentProduct = parentProduct;
    }

	/**
	 * Gets key.
	 *
	 * @return the key
	 */
	public ProductRelationshipKey getKey() {
		return key;
	}

	/**
	 * @param key the key
	 * Sets key product relationship key.
	 *
	 * @param key the key product relationship key.
	 */
	public void setKey(ProductRelationshipKey key) {
		this.key = key;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		ProductRelationship that = (ProductRelationship) o;

		return key != null ? key.equals(that.key) : that.key == null;
	}

	@Override
	public int hashCode() {
		return this.key != null ? this.key.hashCode() : 0;
	}

	public String getActionCode() {
		return actionCode;
	}

	public void setActionCode(String actionCode) {
		this.actionCode = actionCode;
	}

	/**
	 * @return Gets the value of children and returns children
	 */
	public void setChildren(List<ProductRelationship> children) {
		this.children = children;
	}

	/**
	 * Sets the children
	 */
	public List<ProductRelationship> getChildren() {
		return children;
	}

	public Long getProductId(){
		return key.getProductId();
	}

	public String getDescription(){
		if(parentProduct != null) {
			parentProduct.getProdId();
			return parentProduct.getDescription();
		}else{
			return StringUtils.EMPTY;
		}
	}

	public Long getUpc(){
		if(parentProduct != null){
			parentProduct.getProdId();
			return parentProduct.getProductPrimarySellingUnit().getUpc();
		}
		return Long.valueOf(0);
	}

	public String getSize(){
		if(parentProduct != null){
			parentProduct.getProdId();
			return parentProduct.getProductSizeText();
		}
		return StringUtils.EMPTY;
	}

	/**
	 * @return Gets the value of parentNodes and returns parentNodes
	 */
	public void setParentNodes(List<Long> parentNodes) {
		this.parentNodes = parentNodes;
	}

	/**
	 * Sets the parentNodes
	 */
	public List<Long> getParentNodes() {
		return parentNodes;
	}
}