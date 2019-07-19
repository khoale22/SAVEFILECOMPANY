package com.heb.pm.entity;

import java.util.List;

/**
 * Represents the overall Product Info Parameters that are returned to the UI.  Instead of returning each individual object return all needed from services in one.
 *
 * @author m594201
 * @since 2.12.0
 */
public class ProductInfoParameters {

	private ProductAssortment productAssortment;

	private ProductMaster productMaster;

	private List<MapPrice> mapPrices;

	private ProductMaster depositRelatedProduct;

	/**
	 * Gets the deposit related product.
	 *
	 * @return the deposit related product.
	 */
	public ProductMaster getDepositRelatedProduct() {
		return depositRelatedProduct;
	}

	/**
	 * Sets the deposit related product.
	 *
	 * @param depositRelatedProduct the deposit related product.
	 */
	public void setDepositRelatedProduct(ProductMaster depositRelatedProduct) {
		this.depositRelatedProduct = depositRelatedProduct;
	}

	/**
	 * Gets map prices.
	 *
	 * @return the map prices
	 */
	public List<MapPrice> getMapPrices() {
		return mapPrices;
	}

	/**
	 * Sets map prices.
	 *
	 * @param mapPrices the map prices
	 */
	public void setMapPrices(List<MapPrice> mapPrices) {
		this.mapPrices = mapPrices;
	}

	/**
	 * Gets the product master.
	 *
	 * @return the product master.
	 */
	public ProductMaster getProductMaster() {
		return productMaster;
	}

	/**
	 * Sets the product master.
	 *
	 * @param productMaster the product master.
	 */
	public void setProductMaster(ProductMaster productMaster) {
		this.productMaster = productMaster;
	}

	/**
	 * Gets product assortment parameters.
	 *
	 * @return the product assortment parameters
	 */
	public ProductAssortment getProductAssortment() {
		return productAssortment;
	}

	/**
	 * Sets product assortment parameters.
	 *
	 * @param productAssortment the product assortment parameters
	 */
	public void setProductAssortment(ProductAssortment productAssortment) {
		this.productAssortment = productAssortment;
	}

	/**
	 * Returns a String representation of the object.
	 *
	 * @return A String representation of the object.
	 */
	@Override
	public String toString() {
		return "ProductInfoParameters{" +
				"productAssortment=" + productAssortment +
				", productMaster=" + productMaster +
				", mapPrices=" + mapPrices +
				", depositRelatedProduct=" + depositRelatedProduct +
				'}';
	}
}
