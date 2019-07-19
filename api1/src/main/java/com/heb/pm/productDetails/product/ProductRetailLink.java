package com.heb.pm.productDetails.product;

/**
 * Custom class for returning product retail link information.
 *
 * @author vn70529
 * @since 2.15.0
 */
public class ProductRetailLink {

    private Long productId;
    private Long itemId;
    private Long upc;
    private String productDescription;
    private String size;
    private Double quantity;
    private String unitOfMeasure;
    private String subCommodityDescription;

    /**
     * Gets product id.
     *
     * @return the product id
     */
    public Long getProductId() {
        return productId;
    }

    /**
     * Sets product id.
     *
     * @param productId the product id
     */
    public void setProductId(Long productId) {
        this.productId = productId;
    }

    /**
     * Gets item id.
     *
     * @return the item id
     */
    public Long getItemId() {
        return itemId;
    }

    /**
     * Sets item id.
     *
     * @param itemId the item id
     */
    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    /**
     * Gets upc.
     *
     * @return the upc
     */
    public Long getUpc() {
        return upc;
    }

    /**
     * Sets upc.
     *
     * @param upc the upc
     */
    public void setUpc(Long upc) {
        this.upc = upc;
    }

    /**
     * Gets product description.
     *
     * @return the product description
     */
    public String getProductDescription() {
        return productDescription;
    }

    /**
     * Sets product description.
     *
     * @param productDescription the product description
     */
    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    /**
     * Gets size.
     *
     * @return the size
     */
    public String getSize() {
        return size;
    }

    /**
     * Sets size.
     *
     * @param size the size
     */
    public void setSize(String size) {
        this.size = size;
    }

    /**
     * Gets quantity.
     *
     * @return the quantity
     */
    public Double getQuantity() {
        return quantity;
    }

    /**
     * Sets quantity.
     *
     * @param quantity the quantity
     */
    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    /**
     * Gets unit of measure.
     *
     * @return the unit of measure
     */
    public String getUnitOfMeasure() {
        return unitOfMeasure;
    }

    /**
     * Sets unit of measure.
     *
     * @param unitOfMeasure the unit of measure
     */
    public void setUnitOfMeasure(String unitOfMeasure) {
        this.unitOfMeasure = unitOfMeasure;
    }

    /**
     * Gets sub commodity description.
     *
     * @return the sub commodity description
     */
    public String getSubCommodityDescription() {
        return subCommodityDescription;
    }

    /**
     * Sets sub commodity description.
     *
     * @param subCommodityDescription the sub commodity description
     */
    public void setSubCommodityDescription(String subCommodityDescription) {
        this.subCommodityDescription = subCommodityDescription;
    }
}
