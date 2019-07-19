/*
 *  CreateCsv
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.productDiscontinue;

import com.heb.pm.entity.ItemNotDeleted;
import com.heb.pm.entity.ProductDiscontinue;
import com.heb.util.jpa.PageableResult;

import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Creates a CSV string from a list of product discontinues.
 *
 * @author s573181
 * @since 2.0.7
 */
public class CreateDiscontinueCsv {

	// Format for text attributes for CSV export.
	private static final String TEXT_EXPORT_FORMAT = "\"%s\",";
	private static final String NEWLINE_TEXT_EXPORT_FORMAT = "\"%s\"\n";
	private static final String CSV_HEADING = "Product ID, UPC, UPC Type, Size, Item Code, Channel, " +
			"Item Description, Discontinue Eligibility, Delete Eligibility";

	/**
	 * Creates a CSV string from a list of product discontinues.
	 *
	 * @param productDiscontinues a list of product discontinues.
	 * @return a CSV string with the product discontinue information.
	 */
	public static String createCsv(PageableResult<ProductDiscontinue> productDiscontinues){
		StringBuilder csv = new StringBuilder();
		for(ProductDiscontinue productDiscontinue  : productDiscontinues.getData()){
			csv.append(String.format(TEXT_EXPORT_FORMAT, productDiscontinue.getKey().getProductId()));
			csv.append(String.format(TEXT_EXPORT_FORMAT, productDiscontinue.getKey().getUpc()));
			if (productDiscontinue.getItemMaster() == null) {
				csv.append(String.format(TEXT_EXPORT_FORMAT, "Error: Item Master missing."));
			} else {
				if (productDiscontinue.getItemMaster().getOrderingUpc() == productDiscontinue.getKey().getUpc()) {
					csv.append(String.format(TEXT_EXPORT_FORMAT, "Primary"));
				} else {
					csv.append(String.format(TEXT_EXPORT_FORMAT, "Associate"));
				}
			}
			if (productDiscontinue.getSellingUnit() == null) {
				csv.append(String.format(TEXT_EXPORT_FORMAT, "Error: Selling Unit missing."));
			} else {
				csv.append(String.format(TEXT_EXPORT_FORMAT, productDiscontinue.getSellingUnit().getTagSize()));
			}
			csv.append(String.format(TEXT_EXPORT_FORMAT, productDiscontinue.getKey().getItemCode()));
			csv.append(String.format(TEXT_EXPORT_FORMAT, productDiscontinue.getDisplayItemType()));
			if (productDiscontinue.getItemMaster() == null) {
				csv.append(String.format(TEXT_EXPORT_FORMAT, "Error: Item Master missing."));
			} else {
				csv.append(String.format(TEXT_EXPORT_FORMAT, productDiscontinue.getItemMaster().getDescription()));
			}
			csv.append(String.format(TEXT_EXPORT_FORMAT, getDiscontinueCsvString(productDiscontinue)));
			csv.append(String.format(NEWLINE_TEXT_EXPORT_FORMAT, getItemNotDeletedReasonsCsv(productDiscontinue)));
		}
		return csv.toString();
	}

	/**
	 * Creates a string containing the discontinue information for a productDiscontinue.
	 * @param productDiscontinue A productDiscontinue.
	 * @return String containing the discontinue information for a productDiscontinue.
	 */
	private static String getDiscontinueCsvString(ProductDiscontinue productDiscontinue){
		StringBuilder csv = new StringBuilder("");
		if (productDiscontinue.getDiscontinued() == null) {
			return "Error: Item Master missing";
		}
		if (productDiscontinue.getSellingUnit() == null) {
			return "Error: Missing selling unit";
		}
		if(productDiscontinue.getDiscontinued()) {
			// If is primary upc
			if (productDiscontinue.getSellingUnit().isPrimaryUpc()) {
				if (productDiscontinue.getItemMaster().getNormalizedDiscontinueDate() != null &&
						productDiscontinue.getItemMaster().getNormalizedDiscontinuedByUID() != null) {
					return csv.append("Discontinued on ").append(productDiscontinue.getItemMaster().
							getNormalizedDiscontinueDate().format(DateTimeFormatter.ISO_LOCAL_DATE)).append(" by ").
							append(productDiscontinue.getItemMaster().getNormalizedDiscontinuedByUID()).append
							(", Estimated delete date: ").append(productDiscontinue.getProjectedDeleteDate()).toString();

				} else {
					return "Discontinue information missing.";
				}
				// If it's an associate and has a discontinue date
			} else if(productDiscontinue.getSellingUnit().getDiscontinueDate() != null){
				return csv.append("Discontinued on ").append(productDiscontinue.getSellingUnit().getDiscontinueDate()).
						append(" Estimated delete date: ").append(productDiscontinue.getProjectedDeleteDate()).toString();
				// If it's an associate and has no discontinue date (is discontinued at primary level).
			} else {
				return "Discontinued at primary level ";
			}
		} else {
			if(isInProgress(productDiscontinue)){
				return "In Progress,";
			} else {
				boolean hasValue = false;
				csv.append("Not Eligible: ");
				if(!productDiscontinue.getSalesSet()){
					csv.append(" Sales as of: ").append(productDiscontinue.getSellingUnit().getLastScanDate());
					hasValue =true;
				}
				if(!productDiscontinue.getStoreReceiptsSet()){
					if(hasValue){
						csv.append(",");
					}
					hasValue=true;
					csv.append(" Store receipts as of: ").append(productDiscontinue.getLastReceivedDate());
				}
				if(!productDiscontinue.getInventorySet()){
					if(hasValue){
						csv.append(",");
					}
					hasValue=true;
					csv.append(" Has Inventory");
				}
				if(!productDiscontinue.getWarehouseInventorySet()){
					if(hasValue){
						csv.append(",");
					}
					hasValue=true;
					csv.append(" Has WHS inventory");
				}
				if(!productDiscontinue.getNewItemSet()){
					if(hasValue){
						csv.append(",");
					}
					hasValue=true;
					csv.append(" Item set up on: ").append(productDiscontinue.getItemMaster().getAddedDate());
				}
				if(!productDiscontinue.getOpenPoSet()){
					if(hasValue){
						csv.append(",");
					}
					csv.append(" has PO ");
				}
				return csv.toString();
			}
		}
	}

	/**
	 * Returns true if the product is in discontinue progress, else false.
	 * @param productDiscontinue the current ProductDiscontinue object.
	 * @return Boolean value of whether the product is in discontinue progress.
	 */
	private static boolean isInProgress(ProductDiscontinue productDiscontinue){
		return productDiscontinue.getInventorySet() && productDiscontinue.getNewItemSet() &&
				productDiscontinue.getOpenPoSet() && productDiscontinue.getSalesSet() &&
				productDiscontinue.getStoreReceiptsSet();
	}

	/**
	 * Returns the reasons why an item was not deleted.
	 *
	 * @param productDiscontinue the current ProductDiscontinue object.
	 * @return the reasons why an item was not deleted.
	 */
	private static String getItemNotDeletedReasonsCsv(ProductDiscontinue productDiscontinue){
		if (productDiscontinue.getItemMaster() == null) {
			return "Error: Item Master missing";
		}

		List<ItemNotDeleted> reasonList = productDiscontinue.getItemMaster().getItemNotDeleted();
		if(reasonList == null || reasonList.size() == 0){
			return "";
		}
		StringBuilder reasons = new StringBuilder("Not Eligible: ");
		for (int x = 0; x<reasonList.size(); x++) {

			reasons = reasons.append(reasonList.get(x).getItemNotDeletedReason().getDescription().trim());
			if(x!=reasonList.size()-1){
				reasons.append(",");
			}
		}
		return reasons.toString();
	}

	/**
	 * Returns the heading to the CSV.
	 * @return The heading to the CSV.
	 */
	public static String getHeading(){
		return CSV_HEADING;
	}
}
