/*
 *
 *  DiscontinueParameterType
 *
 *   Copyright (c) 2016 HEB
 *   All rights reserved.
 *
 *   This software is the confidential and proprietary information
 *    of HEB.
 *
 *
 */

package com.heb.pm.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the the type of discontinue parameters.
 *
 * @author s573181
 * @since 2.0.3
 */
public class DiscontinueParameterType {

	public static final DiscontinueParameterType STORE_SALES;
	public static final DiscontinueParameterType NEW_ITEM_PERIOD;
	public static final DiscontinueParameterType WAREHOUSE_UNITS;
	public static final DiscontinueParameterType STORE_UNITS;
	public static final DiscontinueParameterType STORE_RECEIPTS;
	public static final DiscontinueParameterType PURCHASE_ORDERS;
	public static final List<DiscontinueParameterType> allTypes;

	static{
		STORE_SALES = new DiscontinueParameterType(1, "Store Sales", "LST_SCN_DT");
		NEW_ITEM_PERIOD = new DiscontinueParameterType(2, "New Product Setup", "ADDED_DT");
		WAREHOUSE_UNITS = new DiscontinueParameterType(3, "Inventory at Warehouse", "WHSE_INVEN.ON_HAND_QTY");
		STORE_UNITS = new DiscontinueParameterType(4, "Inventory at Stores", "STR_INVEN.ON_HAND_QTY");
		STORE_RECEIPTS = new DiscontinueParameterType(5,  "Receipts", "LST_RECD_TS");
		PURCHASE_ORDERS = new DiscontinueParameterType(6, "Purchase Orders", "ORDERED_DT");
		allTypes = new ArrayList<>(6);
		allTypes.add(STORE_SALES);
		allTypes.add(NEW_ITEM_PERIOD);
		allTypes.add(WAREHOUSE_UNITS);
		allTypes.add(STORE_UNITS);
		allTypes.add(STORE_RECEIPTS);
		allTypes.add(PURCHASE_ORDERS);
	}

	private int id;
	private String description;
	private String parameterName;

	/**
	 * Constructs a new DiscontinueParameterType.
	 *
	 * @param id The ID for this object. This combined with the sequence number defines what type of
	 * rule this exception is.
	 * @param description The description that the ID stands for. I.e. if the Id is 1, the description is Sales.
	 * @param parameterName The parameter name that the id stands for, such as if the id is 1 the field name is LST_SCN_DT.
	 */
	private DiscontinueParameterType(int id, String description, String parameterName) {
		this.id = id;
		this.description = description;
		this.parameterName = parameterName;
	}

	/**
	 * Returns the enumeration that the Id represents.
	 * @param id the ID of this type. It is the value in sys_gend_id.
	 * @return  the enumeration that the Id represents
	 */
	public static DiscontinueParameterType getTypeById(int id){
		switch(id){
			case 1:
				return STORE_SALES;
			case 2:
				return NEW_ITEM_PERIOD;
			case 3:
				return WAREHOUSE_UNITS;
			case 4:
				return STORE_UNITS;
			case 5:
				return STORE_RECEIPTS;
			case 6:
				return PURCHASE_ORDERS;
			default:
				throw new IllegalArgumentException(id + " is not defined as a DiscontinueParameterType.");
		}
	}

	/**
	 * Returns the ID of this type. It is the value in sys_gend_id.
	 *
	 * @return The ID of this type.
	 */
	public int getId() {
		return id;
	}

	/**
	 * Returns the description that the ID stands for. I.e. if the Id is 1, the description is Sales.
	 *
	 * @return  the description that the ID stands for.
	 */
	public String getDescription(){
		return description;
	}

	/**
	 * Returns the parameter name that the id stands for, such as if the id is 1 the field name is LST_SCN_DT.
	 *
	 * @return the parameter name that the id stands for.
	 */
	public String getParameterName() { return parameterName; }

	/**
	 * Returns a String representation of the object.
	 *
	 * @return A String representation of the object.
	 */
	@Override
	public String toString() {
		return this.description;
	}
}
