package com.heb.pm.entity;

/**
 * Return object for Service Case Sign in Shelf Attributes
 */
public class ServiceCaseSign {

    /**
     * Product Id related to this service case sign
     */
    private Long productId;
    /**
     * Status of Service Case Sign Tag
     */
    private String status;
    /**
     * Proposed Description of the Service Case Sign Tag
     */
    private String proposedDescription;
    /**
     * Approved Description of the Service Case Sign Tag
     */
    private String approvedDescription;

    public Long getProductId() { return productId; }

    public void setProductId(Long productId) { this.productId = productId; }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getProposedDescription() {
        return proposedDescription;
    }

    public void setProposedDescription(String proposedDescription) {
        this.proposedDescription = proposedDescription;
    }

    public String getApprovedDescription() {
        return approvedDescription;
    }

    public void setApprovedDescription(String approvedDescription) {
        this.approvedDescription = approvedDescription;
    }
}
