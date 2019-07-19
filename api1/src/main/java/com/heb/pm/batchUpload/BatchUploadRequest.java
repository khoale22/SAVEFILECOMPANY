/*
 *  ExcelUploadBase
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.batchUpload;

import com.heb.pm.batchUpload.parser.ProductAttribute;

import java.io.InputStream;
import java.io.Serializable;
import java.util.List;


/**
 * <p>
 * This is BatchUploadRequest Object class. IT contain information of file upload
 * </p>
 * @author vn55306
 */
public class BatchUploadRequest implements Serializable {
    /**
	 * 
	 */
    private static final long serialVersionUID = 3884054421364570290L;

    private InputStream dataAsStream;

	private byte[] data;
    private BatchUploadType batchUploadType;
    // for upload Description
    private String uploadDescription;
    private String uploadFileName;
    private String userId;
    private String userRole;

    public List<ProductAttribute> getProductAttributes() {
        return productAttributes;
    }

    public void setProductAttributes(List<ProductAttribute> productAttributes) {
        this.productAttributes = productAttributes;
    }

    private List<ProductAttribute> productAttributes;
    /**
     * Returns the data of file Upload
     * @return the data of file Upload.
     */
	public byte[] getData() {
		return data;
	}
    /**
     * Sets the data of file Upload.
     *
     * @param data the data of file Upload.
     */
	public void setData(byte[] data) {
		this.data = data;
	}


    /**
     * Returns the Batch Upload Type that user uploaded
     * @return The batch Upload Type.
     */
    public BatchUploadType getBatchUploadType() {
	return batchUploadType;
    }

    /**
     * Sets the Batch Upload Type to Creator Processor.
     *
     * @param batchUploadType The Batch Upload Type to Creator Processor.
     */
    public void setBatchUploadType(BatchUploadType batchUploadType) {
	this.batchUploadType = batchUploadType;
    }

    /**
     * Returns the User Id who the upload file
     * @return the User Id who the upload file.
     */
    public String getUserId() {
	return userId;
    }

    /**
     * Sets the User Id who the upload file.
     *
     * @param userId the User Id who the upload file.
     */
    public void setUserId(String userId) {
	this.userId = userId;
    }

    /**
     * Returns the User Role who the upload file
     * @return the User Role who the upload file.
     */
    public String getUserRole() {
	return userRole;
    }

    /**
     * Sets the user Role who the upload file.
     *
     * @param userRole the User Id who the upload file.
     */
    public void setUserRole(String userRole) {
	this.userRole = userRole;
    }

    /**
     * Returns the upload Description
     * @return the uploadDescription.
     */
    public String getUploadDescription() {
	return uploadDescription;
    }

    /**
     * @param uploadDescription
     *            the uploadDescription to set
     */
    public void setUploadDescription(String uploadDescription) {
	this.uploadDescription = uploadDescription;
    }

    /**
     * Returns the upload File Name
     * @return the uploadFileName.
     */
    public String getUploadFileName() {
	return uploadFileName;
    }

    /**
     * Sets the upload File Name.
     *
     * @param uploadFileName the upload File Name.
     */
    public void setUploadFileName(String uploadFileName) {
	this.uploadFileName = uploadFileName;
    }

	/**
	 * Returns the data in the batch upload request as a stream.
	 *
	 * @return The data in the batch upload request as a stream.
	 */
	public InputStream getDataAsStream() {
		return dataAsStream;
	}

	/**
	 * Sets the data in the batch upload request as a stream.
	 *
	 * @param dataAsStream The data in the batch upload request as a stream.
	 */
	public void setDataAsStream(InputStream dataAsStream) {
		this.dataAsStream = dataAsStream;
	}
}
