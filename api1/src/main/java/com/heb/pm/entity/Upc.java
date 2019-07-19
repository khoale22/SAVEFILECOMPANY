/*
 * UpcInfo.java
 *
 *  Copyright (c) 2018 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.entity;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.List;

/**
 * UPC Status object. Contains UPC and associated status.
 *
 * @author s769046
 * @since 2.21.0
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Upc implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final String CANDIDATE_ACTIVATED="108";
    public static final String CANDIDATE_DELETED="104";
    public static final String CANDIDATE_REJECTED="105";
    public static final String CANDIDATE_IN_PROGRESS="111";
    public static final String VENDOR_CANDIDATE="102";
    public static final String CANDIDATE_FAILED="109";

    private Long scanCodeId;
    private Long searchedUpc;
    private String status;
	private Long retailLink;
	private String size;
	private Integer xFor;
	private double retailPrice;
	private Boolean weightSw;
	private List<Long> associatedUpcs;
	private Product product;
	private Item item;

	/**
     * Sets the scanCodeId;
     * @param scanCodeId the scanCodeId;
     * @return the Upc object acted upon
     */
    public Upc setScanCodeId(Long scanCodeId) {
        this.scanCodeId = scanCodeId;
        return this;
    }

    /**
     * Returns the scanCodeId.
     *
     * @return the scanCodeId.
     */
    public Long getScanCodeId() {
        return this.scanCodeId;
    }

	/**
	 * Returns the searched upc.
	 *
	 * @return the searched upc.
	 */
	public Long getSearchedUpc() {
		return searchedUpc;
	}

	/**
	 * Sets the searched upc.
	 *
	 * @param searchedUpc the searched upc.
	 * @return the updated Upc.
	 */
	public Upc setSearchedUpc(Long searchedUpc) {
		this.searchedUpc = searchedUpc;
		return this;
	}

	/**
     * Sets the status.
     *
     * @param status the status.
     * @return the Upc object acted upon.
     */
    public Upc setStatus(String status) {
        this.status = status;
        return this;
    }

    /** Returns the status.
     *
     * @return the status.
     */
    public String getStatus() {
        return this.status;
    }

    /**
     * Sets the status based on if the product is discontinued.
     *
     * @param isDiscontinued if the product is discontinued.
     * @return the Upc object acted upon.
     */
    public Upc setProdStatus(boolean isDiscontinued) {
        if(!isDiscontinued) {
            this.status="ACTIVE_PRODUCT";
        } else {
            this.status="DISCONTINUED_PRODUCT";
        }
        return this;
    }

	/**
	 * Returns the product info.
	 *
	 * @return the product info.
	 */
	public Product getProduct() {
		return product;
	}

	/**
	 * Sets the product info.
	 *
	 * @param product the product info.
	 * @return the updated Upc object
	 */
	public Upc setProduct(Product product) {
		this.product = product;
		return this;
	}

	/**
	 * Returns the retail link.
	 *
	 * @return the retail link.
	 */
	public Long getRetailLink() {
		return retailLink;
	}

	/**
	 * Sets the retail link.
	 *
	 * @param retailLink the retail link.
	 * @return the updated scanCodeId info.
	 */
	public Upc setRetailLink(Long retailLink) {
		this.retailLink = retailLink;
		return this;
	}

	/**
	 * Returns the size.
	 *
	 * @return the size.
	 */
	public String getSize() {
		return size;
	}

	/**
	 * Sets the size.
	 *
	 * @param size the size.
	 * @return the updatedUpcInfo.
	 */
	public Upc setSize(String size) {
		this.size = size;
		return this;
	}

	/**
	 * Returns xFor.
	 *
	 * @return xFor.
	 */
	public Integer getXFor() {
		return xFor;
	}

	/**
	 * Sets xFor.
	 *
	 * @param xFor xFor.
	 * @return the updated Upc.
	 */
	public Upc setXFor(Integer xFor) {
		this.xFor = xFor;
		return this;
	}

	/**
	 * Returns the retail price.
	 *
	 * @return the retail price.
	 */
	public double getRetailPrice() {
		return retailPrice;
	}

	/**
	 * Returns the associated upcs.
	 *
	 * @return the associated upcs.
	 */
	public List<Long> getAssociatedUpcs() {
		return associatedUpcs;
	}

	/**
	 * Sets the associated upcs.
	 *
	 * @param associatedUpcs the associated upcs.
	 * @return the updated Upc.
	 */
	public Upc setAssociatedUpcs(List<Long> associatedUpcs) {
		this.associatedUpcs = associatedUpcs;
		return this;
	}

	/**
	 * Returns the Item.
	 *
	 * @return the Item.
	 */
	public Item getItem() {
		return item;
	}

	/**
	 * Sets the Item.
	 *
	 * @param item the ItemIfo.
	 * @return the updated Upc.
	 */
	public Upc setItem(Item item) {
		this.item = item;
		return this;
	}

	/**
	 * Sets the retail price.
	 *
	 * @param retailPrice the retail price.
	 * @return the updated Upc.
	 */
	public Upc setRetailPrice(double retailPrice) {
		this.retailPrice = retailPrice;
		return this;
	}

	/**
	 * Returns the weight switch.
	 *
	 * @return the weight switch.
	 */
	public Boolean getWeightSw() {
		return weightSw;
	}

	/**
	 * Sets the weight switch.
	 *
	 * @param weightSw the weight switch.
	 * @return The updated upc.
	 */
	public Upc setWeightSw(Boolean weightSw) {
		this.weightSw = weightSw;
		return this;
	}

	/**
     * Sets the status based on the candidate status code.
     *
     * @param code the candidate status code.
     * @return the Upc object acted upon.
     */
    public Upc setCandidateStatusByCode(String code) {
        switch (code) {
            case CANDIDATE_REJECTED: {
                this.status = "REJECTED_CANDIDATE";
                break;
            }
            case CANDIDATE_DELETED: {
                this.status = null;
                break;
            }
            case CANDIDATE_ACTIVATED: {
                this.status = "ACTIVE_CANDIDATE";
                break;
            }
            case VENDOR_CANDIDATE: {
                this.status = "VENDOR_CANDIDATE";
                break;
            }
            case CANDIDATE_IN_PROGRESS: {
                this.status = "IN_PROGRESS_CANDIDATE";
                break;
            }
            case CANDIDATE_FAILED: {
                this.status = "FAILED_CANDIDATE";
                break;
            }
            default: {
                this.status = "UNKNOWN_CANDIDATE";
                break;
            }
        }
        return this;
    }

}
