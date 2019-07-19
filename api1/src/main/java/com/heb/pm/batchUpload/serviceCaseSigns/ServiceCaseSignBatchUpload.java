package com.heb.pm.batchUpload.serviceCaseSigns;

import com.heb.pm.batchUpload.BatchUpload;
import com.heb.pm.entity.CandidateWorkRequest;

import java.util.HashMap;
import java.util.Map;

public class ServiceCaseSignBatchUpload extends BatchUpload {

	 /*
	  * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    //colunm position
	public static final int COL_NM_UPC = 0;
	public static final int COL_NM_PRODUCT_ID = 3;
	public static final int COL_NM_PROPOSED_SERVICE_CASE_SIGN_DESCRIPTION = 9;
	public static final int COL_NM_APPROVED_SERVICE_CASE_SIGN_DESCRIPTION = 10;
	public static final int COL_NM_SERVICE_CASE_STATUS = 11;
	public static final int COL_NM_TAG_TYPE = 12;
	public static final int COL_NM_SERVICE_CASE_CALLOUT = 13;

	public static final String UPC = "UPC";
	public static final String PRODUCT_ID = "Product Id";
	public static final String PROPOSED_SERVICE_CASE_SIGN_DESCRIPTION = "Proposed Service Case Sign Description";
	public static final String APPROVED_SERVICE_CASE_SIGN_DESCRIPTION = "Approved Service Case Sign Description";
	public static final String SERVICE_CASE_STATUS = "Service Case Status";
	public static final String TAG_TYPE = "Tag Type";
	public static final String SERVICE_CASE_CALLOUT = "Service Case Callout";

    private String upc;
    private String productId;
    private String productDescription;
    private String proposedServiceCaseSign;
    private String approvedServiceCaseSign;
    private String serviceCaseSignStatus;
    private String serviceCaseSignStatusCode;
	private String tagType;
	private String serviceCaseCallout;

	public static final Map<Integer,String> uploadFileHeader;

	static {
		uploadFileHeader = new HashMap<Integer,String>();
		uploadFileHeader.put(COL_NM_UPC, UPC);
		uploadFileHeader.put(COL_NM_PRODUCT_ID, PRODUCT_ID);
		uploadFileHeader.put(COL_NM_PROPOSED_SERVICE_CASE_SIGN_DESCRIPTION, PROPOSED_SERVICE_CASE_SIGN_DESCRIPTION);
		uploadFileHeader.put(COL_NM_APPROVED_SERVICE_CASE_SIGN_DESCRIPTION, APPROVED_SERVICE_CASE_SIGN_DESCRIPTION);
		uploadFileHeader.put(COL_NM_SERVICE_CASE_STATUS, SERVICE_CASE_STATUS);
		uploadFileHeader.put(COL_NM_TAG_TYPE, TAG_TYPE);
		uploadFileHeader.put(COL_NM_SERVICE_CASE_CALLOUT, SERVICE_CASE_CALLOUT);
	}

	/**
	 * @return the upc
	 */
	public String getUpc() {
		return upc;
	}
	/**
	 * @param upc the upc to set
	 */
	public void setUpc(String upc) {
		this.upc = upc;
	}
	/**
	 * @return the productId
	 */
	public String getProductId() {
		return productId;
	}
	/**
	 * @param productId the productId to set
	 */
	public void setProductId(String productId) {
		this.productId = productId;
	}
	/**
	 * @return the productDescription
	 */
	public String getProductDescription() {
		return productDescription;
	}
	/**
	 * @param productDescription the productDescription to set
	 */
	public void setProductDescription(String productDescription) {
		this.productDescription = productDescription;
	}
	/**
	 * @return the proposedServiceCaseSign
	 */
	public String getProposedServiceCaseSign() {
		return proposedServiceCaseSign;
	}
	/**
	 * @param proposedServiceCaseSign the proposedServiceCaseSign to set
	 */
	public void setProposedServiceCaseSign(String proposedServiceCaseSign) {
		this.proposedServiceCaseSign = proposedServiceCaseSign;
	}
	/**
	 * @return the approvedServiceCaseSign
	 */
	public String getApprovedServiceCaseSign() {
		return approvedServiceCaseSign;
	}
	/**
	 * @param approvedServiceCaseSign the approvedServiceCaseSign to set
	 */
	public void setApprovedServiceCaseSign(String approvedServiceCaseSign) {
		this.approvedServiceCaseSign = approvedServiceCaseSign;
	}
	/**
	 * @return the serviceCaseSignStatus
	 */
	public String getServiceCaseSignStatus() {
		return serviceCaseSignStatus;
	}
	/**
	 * @param serviceCaseSignStatus the serviceCaseSignStatus to set
	 */
	public void setServiceCaseSignStatus(String serviceCaseSignStatus) {
		this.serviceCaseSignStatus = serviceCaseSignStatus;
	}
	/**
	 * @return the serviceCaseSignStatus
	 */
	public String getServiceCaseSignStatusCode() {
		if(this.serviceCaseSignStatus!=null) {
			if (this.serviceCaseSignStatus.equalsIgnoreCase("SUBMITTED"))
				return CandidateWorkRequest.StatusCode.IN_PROGRESS.getName();
			else if (this.serviceCaseSignStatus.equalsIgnoreCase("APPROVED"))
				return CandidateWorkRequest.StatusCode.SUCCESS.getName();
			else
				return CandidateWorkRequest.StatusCode.REJECTED.getName();
		} else {
			return CandidateWorkRequest.StatusCode.FAILURE.getName();
		}
	}
	/**
	 * @param serviceCaseSignStatusCode the serviceCaseSignStatusCode to set
	 */
	public void setServiceCaseSignStatusCode(String serviceCaseSignStatusCode) {
		this.serviceCaseSignStatusCode = serviceCaseSignStatusCode;
	}

	/**
	 * Returns the type of tag.
	 *
	 * @return the type of tag.
	 */
	public String getTagType() {
		return tagType;
	}

	/**
	 * Sets the type of tag.
	 *
	 * @param tagType The type of tag.
	 */
	public void setTagType(String tagType) {
		this.tagType = tagType;
	}

	/**
	 * Returns the service case callout of product.
	 *
	 * @return serviceCaseCallout
	 */
	public String getServiceCaseCallout() {
		return serviceCaseCallout;
	}

	/**
	 * Sets the service case callout of product
	 *
	 * @param serviceCaseCallout the service case callout.
	 */
	public void setServiceCaseCallout(String serviceCaseCallout) {
		this.serviceCaseCallout = serviceCaseCallout;
	}

}
