package com.hellokoding.springboot.jpa.TEstKHoa;

public class ProductLineBrandCostOwerTopToTop {

    private String productLineId;
    private String productLineDescription;
    private String productBrandId;
    private String productBrandDescription;
    private String costOwnerId;
    private String costOwnerName;
    private String topToTopId;
    private String topToTopName;

    public ProductLineBrandCostOwerTopToTop(String productLineId, String productLineDescription,
                                            String productBrandId, String productBrandDescription,
                                            String costOwnerId, String costOwnerName, String topToTopId,
                                            String topToTopName) {
        this.setProductLineId(productLineId);
        this.setProductLineDescription(productLineDescription);
        this.setProductBrandId(productBrandId);
        this.setProductBrandDescription(productBrandDescription);
        this.setCostOwnerId(costOwnerId);
        this.setCostOwnerName(costOwnerName);
        this.setTopToTopId(topToTopId);
        this.setTopToTopName(topToTopName);
    }

    public String getProductLineId() {
        return productLineId;
    }

    public void setProductLineId(String productLineId) {
        this.productLineId = productLineId;
    }

    public String getProductLineDescription() {
        return productLineDescription;
    }

    public void setProductLineDescription(String productLineDescription) {
        this.productLineDescription = productLineDescription;
    }

    public String getProductBrandId() {
        return productBrandId;
    }

    public void setProductBrandId(String productBrandId) {
        this.productBrandId = productBrandId;
    }

    public String getProductBrandDescription() {
        return productBrandDescription;
    }

    public void setProductBrandDescription(String productBrandDescription) {
        this.productBrandDescription = productBrandDescription;
    }

    public String getCostOwnerId() {
        return costOwnerId;
    }

    public void setCostOwnerId(String costOwnerId) {
        this.costOwnerId = costOwnerId;
    }

    public String getCostOwnerName() {
        return costOwnerName;
    }

    public void setCostOwnerName(String costOwnerName) {
        this.costOwnerName = costOwnerName;
    }

    public String getTopToTopId() {
        return topToTopId;
    }

    public void setTopToTopId(String topToTopId) {
        this.topToTopId = topToTopId;
    }

    public String getTopToTopName() {
        return topToTopName;
    }

    public void setTopToTopName(String topToTopName) {
        this.topToTopName = topToTopName;
    }
}
