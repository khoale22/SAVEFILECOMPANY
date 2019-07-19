package com.heb.pm.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Holds the return object for the Dimensions Page.
 *
 * @author m594201
 * @since 2.13.0
 */
public class DimensionsParameters implements Serializable {

	private static final long serialVersionUID = 1L;


	private ProductMaster productMaster;

	private SellingUnit sellingUnit;

	private List<ProductScanCodeExtent> productScanCodeExtentList;

	/**
	 * Gets selling unit.
	 *
	 * @return the selling unit
	 */
	public SellingUnit getSellingUnit() {
		return sellingUnit;
	}

	/**
	 * Sets selling unit.
	 *
	 * @param sellingUnit the selling unit
	 */
	public void setSellingUnit(SellingUnit sellingUnit) {
		this.sellingUnit = sellingUnit;
	}

	/**
	 * Gets product master.
	 *
	 * @return the product master
	 */
	public ProductMaster getProductMaster() {
		return productMaster;
	}

	/**
	 * Sets product master.
	 *
	 * @param productMaster the product master
	 */
	public void setProductMaster(ProductMaster productMaster) {
		this.productMaster = productMaster;
	}

	/**
	 * Gets product scan code extent list.
	 *
	 * @return the product scan code extent list
	 */
	public List<ProductScanCodeExtent> getProductScanCodeExtentList() {
		return productScanCodeExtentList;
	}

	/**
	 * Sets product scan code extent list.
	 *
	 * @param productScanCodeExtentList the product scan code extent list
	 */
	public void setProductScanCodeExtentList(List<ProductScanCodeExtent> productScanCodeExtentList) {
		this.productScanCodeExtentList = productScanCodeExtentList;
	}
}
