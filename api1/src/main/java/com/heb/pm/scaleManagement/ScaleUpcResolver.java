/*
 * ScaleUpcResolver
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.scaleManagement;

import com.heb.pm.entity.ScaleUpc;
import com.heb.util.jpa.LazyObjectResolver;

/**
 * Resolves ScaleUpc objects to send them fully-populated back from the REST endpoint.
 *
 * @author m314029
 * @since 2.0.8
 */
public class ScaleUpcResolver implements LazyObjectResolver<ScaleUpc> {

	/**
	 * Resolves a ScaleUpc object. It will load the following properties:
	 * 1. scaleUpc -> associateUpc -> primaryUpc -> itemMasters
	 * 2. scaleUpc -> associateUpc -> sellingUnit -> productMaster -> subDepartment -> displayName
	 *
	 * @param scaleUpc The ScaleUpc to resolve.
	 */
	@Override
	public void fetch(ScaleUpc scaleUpc) {
		if (scaleUpc.getAssociateUpc() != null) {
			if (scaleUpc.getAssociateUpc().getPrimaryUpc() != null) {
				if (scaleUpc.getAssociateUpc().getPrimaryUpc().getItemMasters() != null) {
					scaleUpc.getAssociateUpc().getPrimaryUpc().getItemMasters().size();
				}
			}
			if (scaleUpc.getAssociateUpc().getSellingUnit() != null) {
				if (scaleUpc.getAssociateUpc().getSellingUnit().getProductMaster() != null) {
					if (scaleUpc.getAssociateUpc().getSellingUnit().getProductMaster().getSubDepartment() != null) {
						scaleUpc.getAssociateUpc().getSellingUnit().getProductMaster().getSubDepartment().getDisplayName();
					}
				}
			}
		}
	}
}
