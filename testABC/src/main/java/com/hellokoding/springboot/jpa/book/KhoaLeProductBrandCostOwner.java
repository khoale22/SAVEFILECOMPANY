package com.hellokoding.springboot.jpa.book;

public class KhoaLeProductBrandCostOwner {

    private String topToTopName22;

    private String productBrandDescription;

    public KhoaLeProductBrandCostOwner(String topToTopName22, String productBrandDescription) {
        this.topToTopName22 = topToTopName22;
        this.productBrandDescription = productBrandDescription;
    }

    public String getTopToTopName22() {
        return topToTopName22;
    }

    public void setTopToTopName22(String topToTopName22) {
        this.topToTopName22 = topToTopName22;
    }

    public String getProductBrandDescription() {
        return productBrandDescription;
    }

    public void setProductBrandDescription(String productBrandDescription) {
        this.productBrandDescription = productBrandDescription;
    }

    public String getDisplayName() {
        return String.format( "%s [%d]", this.productBrandDescription.trim(), 1234);
    }
}
