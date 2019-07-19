package com.heb.scaleMaintenanceSetUp.entity;

import java.io.Serializable;
import java.util.List;

public class StoreDept implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<StoreDetails> storeDetails;

	public List<StoreDetails> getStoreDetails() {
		return storeDetails;
	}

	public void setStoreDetails(List<StoreDetails> storeDetails) {
		this.storeDetails = storeDetails;
	}
	
}
