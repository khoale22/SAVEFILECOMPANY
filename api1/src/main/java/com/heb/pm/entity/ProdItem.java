package com.heb.pm.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * Ties item to products.
 *
 * @author s573181
 * @since 2.0.1
 */
@Entity
@Table(name="PROD_ITEM")
public class ProdItem implements Serializable {

	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private ProdItemKey key;

	@Column(name="retl_pack_qty")
	private int productCount;

	@JsonIgnoreProperties("prodItems")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumns({
			@JoinColumn(name="prod_id", referencedColumnName = "prod_id", insertable = false, updatable = false, nullable = false)
	})
	private ProductMaster productMaster;

	@NotFound(action = NotFoundAction.IGNORE)
	@JsonIgnoreProperties("prodItems")

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumns({
			@JoinColumn(name="itm_id", referencedColumnName = "itm_id", insertable = false, updatable = false, nullable = false),
			@JoinColumn(name="itm_key_typ_cd", referencedColumnName = "itm_key_typ_cd", insertable = false, updatable = false, nullable = false)
	})
	private ItemMaster itemMaster;


	// These are required to support dynamic search capability and is not used outside that. Therefore, there are no
	// getters or setters on this.
	@Column(name="prod_id", insertable = false, updatable = false)
	private Long productId;

	@Column(name="itm_id", insertable = false, updatable = false)
	private Long itemCode;

	@OneToMany(fetch = FetchType.LAZY)
	@NotFound(action = NotFoundAction.IGNORE)
	@JoinColumn(name = "itm_id", referencedColumnName = "itm_id", insertable = false, updatable = false)
	private List<SearchCriteria> searchCriteria;

	@Transient
	private String actionCode;

	/**
	 * Returns the key for the ProdItem object.
	 *
	 * @return The key for the ProdItem object.
	 */
	public ProdItemKey getKey() {
		return this.key;
	}

	/**
	 * Sets the key for the ProdItem object.
	 *
	 * @param key They key for the ProdItem object.
	 */
	public void setKey(ProdItemKey key) {
		this.key = key;
	}


	/**
	 * Returns information about this record from the item_master table.
	 *
	 * @return Information about this record from the item_master table.
	 */
	public ItemMaster getItemMaster() {
		return itemMaster;
	}

	/**
	 * Sets the information about this record from the item_master table.
	 *
	 * @param itemMaster Information about this record from the item_master table.
	 */
	public void setItemMaster(ItemMaster itemMaster) {
		this.itemMaster = itemMaster;
	}


	/**
	 * Returns information about this record from the product_master table.
	 *
	 * @return Information about this record from the item_master table.
	 */
	public ProductMaster getProductMaster() {
		return productMaster;
	}

	/**
	 * Sets the information about this record from the product_master table.
	 *
	 * @param productMaster Information about this record from the item_master table.
	 */
	public void setProductMaster(ProductMaster productMaster) {
		this.productMaster = productMaster;
	}

	/**
	 * @return Gets the value of actionCode and returns actionCode
	 */
	public void setActionCode(String actionCode) {
		this.actionCode = actionCode;
	}

	/**
	 * Sets the actionCode
	 */
	public String getActionCode() {
		return actionCode;
	}

	/**
	 * @return Gets the value of productCount and returns productCount
	 */
	public void setProductCount(int productCount) {
		this.productCount = productCount;
	}

	/**
	 * Sets the productCount
	 */
	public int getProductCount() {
		return productCount;
	}

	/**
	 * Compares another object to this one. If that object is an ItemMaster, it uses they keys
	 * to determine if they are equal and ignores non-key values for the comparison.
	 *
	 * @param o The object to compare to.
	 * @return True if they are equal and false otherwise.
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof ProdItem)) return false;

		ProdItem item = (ProdItem) o;

		return !(key != null ? !key.equals(item.key) : item.key != null);

	}

	/**
	 * Returns a hash code for this object. If two objects are equal, they have the same hash. If they are not equal,
	 * they have different hash codes.
	 *
	 * @return The hash code for this obejct.
	 */
	@Override
	public int hashCode() {
		return key != null ? key.hashCode() : 0;
	}

	/**
	 * Returns a printable representation of the object.
	 *
	 * @return A printable representation of the object.
	 */
	@Override
	public String toString() {
		return "ProdItem{" +
				"key=" + key +
				", productCount=" + productCount +
				'}';
	}
}
