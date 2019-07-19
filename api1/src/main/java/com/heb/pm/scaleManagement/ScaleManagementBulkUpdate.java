/*
 * ScaleManagementBulkUpdate
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
package com.heb.pm.scaleManagement;

import com.heb.pm.entity.ScaleUpc;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Used to pull a Bulk Update from the front end that contains a list of scale upcs,
 * an attribute that needs to be updated, and the value that is going to be updated.
 *
 * @author l730832
 * @since 2.2.0
 */
public class ScaleManagementBulkUpdate implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<ScaleUpc> scaleUpcs;
	private BulkUpdateAttribute attribute;
	private String value;

	/**
	 * Gets scale upcs.
	 *
	 * @return the scale upcs
	 */
	public List<ScaleUpc> getScaleUpcs() {
		return scaleUpcs;
	}

	/**
	 * Sets scale upcs.
	 *
	 * @param scaleUpcs the scale upcs
	 */
	public void setScaleUpcs(List<ScaleUpc> scaleUpcs) {
		this.scaleUpcs = scaleUpcs;
	}

	/**
	 * Gets attribute.
	 *
	 * @return the attribute
	 */
	public BulkUpdateAttribute getAttribute() {
		return attribute;
	}

	/**
	 * Sets attribute.
	 *
	 * @param attribute the attribute
	 */
	public void setAttribute(BulkUpdateAttribute attribute) {
		this.attribute = attribute;
	}

	/**
	 * Gets value.
	 *
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * Sets value.
	 *
	 * @param value the value
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * The enum Bulk update attribute.
	 */
	public enum BulkUpdateAttribute {
		/**
		 * Shelf life days bulk update attribute.
		 */
		SHELF_LIFE_DAYS,
		/**
		 * Freeze by days bulk update attribute.
		 */
		FREEZE_BY_DAYS,
		/**
		 * Service counter tare bulk update attribute.
		 */
		SERVICE_COUNTER_TARE,
		/**
		 * Eat by days bulk update attribute.
		 */
		EAT_BY_DAYS,
		/**
		 * Prepack tare bulk update attribute.
		 */
		PREPACK_TARE,
		/**
		 * Action code bulk update attribute.
		 */
		ACTION_CODE,
		/**
		 * Graphics code bulk update attribute.
		 */
		GRAPHICS_CODE,
		/**
		 * Force tare bulk update attribute.
		 */
		FORCE_TARE,
		/**
		 * English line 1 bulk update attribute.
		 */
		ENGLISH_LINE_1,
		/**
		 * English line 2 bulk update attribute.
		 */
		ENGLISH_LINE_2,
		/**
		 * English line 3 bulk update attribute.
		 */
		ENGLISH_LINE_3,
		/**
		 * English line 4 bulk update attribute.
		 */
		ENGLISH_LINE_4,
		/**
		 * Ingredient Statement Number bulk update attribute.
		 */
		INGREDIENT_STATEMENT_NUMBER,
		/**
		 * Maintenance bulk update attribute.
		 */
		MAINTENANCE
	}

	/**
	 * Returns a string representation of the object.
	 *
	 * @return A string representation of the object.
	 */
	@Override
	public String toString() {
		List<Long> scaleUpcPluList = new ArrayList<>();
		for(ScaleUpc scaleUpc : scaleUpcs){
			scaleUpcPluList.add(scaleUpc.getPlu());

		}
		return "ScaleManagementBulkUpdate{" +
				"plu=" + scaleUpcPluList +
				", attribute=" + attribute +
				", value='" + value + '\'' +
				'}';
	}
}
