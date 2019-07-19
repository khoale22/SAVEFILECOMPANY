/*
 * productSearchCriteriaService.js
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

'use strict';

/**
 * Creates the service used to interact with the product search box.
 */
(function() {
	angular.module('productMaintenanceUiApp').service('ProductSearchCriteriaService', productSearchCriteriaService);

	function productSearchCriteriaService() {

		var self = this;

		self.searchCallback = null;
		self.complexSearchCallback = null;
		self.searchType = null;
		self.selectionType = null;
		self.searchSelection = null;
		self.itemStatus = null;

		self.byosObject = null;
		self.byosObjects = [];

		self.showFullPanelProperty = false;
		self.productSearchCriteria = {};

		/**
		 * Wheter the user has selected to search by product description.
		 * @type {boolean}
		 */
		self.searchByProductDescription = true;

		/**
		 * Whether the user has selected to search by customer friendly description.
		 * @type {boolean}
		 */
		self.searchByCustomerFriendlyDescription = false;

		/**
		 * Wheter the user has selected to search by display name.
		 * @type {boolean}
		 */
		self.searchByDisplayName = false;

		/**
		 * Whether the user has selected to search by all extended attributes.
		 * @type {boolean}
		 */
		self.searchAllExtendedAttributes = false;

		return {
			// Constants for the search types.
			BASIC_SEARCH:"basicSearch",
			HIERARCHY_SEARCH:"hierarchySearch",
			BDM_SEARCH:"bdmSearch",
			BYOS_SEARCH:"byosSearch",
			MRT_SEARCH:"mrtSearch",
			VENDOR_SEARCH:"vendorSearch",
			CUSTOM_HIERARCHY_SEARCH:"customHierarchySearch",
			DESCRIPTION_SEARCH: "descriptionSearch",

			// Constants for the list types.
			UPC:"UPC",
			ITEM_CODE:"Item Code",
			PRODUCT_ID:"Product ID",
			PRODUCT_DESCRIPTION: "Product Description",
			BDM: "BDM",
			SUB_DEPARTMENT:"Sub Department",
			CLASS:"Class",
			COMMODITY:"Commodity",
			SUB_COMMODITY:"Sub Commodity",
			CASE_UPC:"Case UPC",


			/**
			 * Initializes the controller.
			 *
			 * @param showFullPanel Whether or not to show the full panel (excludes item status filter). This will add
			 * things like the MRT tab, the add button for complex search, product description search, etc.
			 * @param showStatusFilter Whether or not to show the item status filter options.
			 * @param basicSearchCallback The callback for when the user clicks on a basic search button. Basic
			 * searches only have one set of criteria.
			 * @param complexSearchCallback The callback for when the user clicks on the complex search button. Complex
			 * searches can contain multiple sets of criteria.
			 */
			init:function(showFullPanel, showStatusFilter, basicSearchCallback, complexSearchCallback) {
				self.productSearchCriteria = {};
				self.showFullPanelProperty = showFullPanel;
				self.searchCallback = basicSearchCallback;
				self.complexSearchCallback = complexSearchCallback;
			},

			/**
			 * Wheter or not all of the search options should be available.
			 *
			 * @returns {boolean|*}
			 */
			showFullPanel:function() {
				return self.showFullPanelProperty;
			},

			/**
			 * Returns item status the user has selected.
			 *
			 * @returns itemStatus
			 */
			getItemStatus:function(){
				return self.itemStatus;
			},

			/**
			 * Sets item status the user has selected.
			 *
			 * @param itemStatus
			 */
			setItemStatus:function(itemStatus){
				self.itemStatus = itemStatus;
			},

			/**
			 * Returns the callback for when the user clicks on the search button.
			 *
			 * @returns {null|*}
			 */
			getSearchCallback:function() {
				if (self.searchCallback == null) {
					console.log('returning null search callback');
				}
				return self.searchCallback;
			},

			/**
			 * Returns the callback to use when the user clicks on the complex search button.
			 * @returns {null|*}
			 */
			getComplexSearchCallback:function() {
				return self.complexSearchCallback;
			},

			/**
			 * Returns the type of search the user has chosen  (basic, hierarchy, bdm, etc.).
			 *
			 * @returns {null|*} The type of search the user has chosen.
			 */
			getSearchType:function() {
				return self.searchType;
			},

			/**
			 * Sets the type of search the user has chosen.
			 *
			 * @param searchType The type of search the user has chosen.
			 */
			setSearchType:function(searchType) {
				self.searchType = searchType;
			},

			/**
			 * Returns the type of data in the search list.  For basic, this will be UPC, product ID, or item code. For
			 * BDM searches, this will be BDM. For hierarchy searches, this will be the level of the hierarchy.
			 *
			 * @returns {null|*} The type of data in the search list.
			 */
			getSelectionType:function() {
				return self.selectionType;
			},

			/**
			 * Sets the type of data in the search list.
			 *
			 * @param selectionType The type of data in the search list.
			 */
			setSelectionType:function(selectionType) {
				self.selectionType = selectionType;
			},

			/**
			 * Returns the values the user has chosen to search on. This will be the actual list of UPCs, etc., or an
			 * object representing a BDM or a level of  a hierarchy.
			 *
			 * @returns {null|*}
			 */
			getSearchSelection:function() {
				return self.searchSelection;
			},

			/**
			 * Sets the values the user has chosen to search on.
			 *
			 * @param searchSelection
			 */
			setSearchSelection:function(searchSelection) {
				self.searchSelection = searchSelection;
			},

			setSearchByProductDescription:function(searchByProductDescription) {
				self.searchByProductDescription = searchByProductDescription;
			},

			setSearchByCustomerFriendlyDescription:function(searchByCustomerFriendlyDescription) {
				self.searchByCustomerFriendlyDescription = searchByCustomerFriendlyDescription;
			},

			setSearchByDisplayName:function(searchByDisplayName) {
				self.searchByDisplayName = searchByDisplayName;
			},

			setSearchAllExtendedAttributes:function(searchAllExtendedAttributes) {
				self.searchAllExtendedAttributes = searchAllExtendedAttributes;
			},

			/**
			 * Add a list of product IDs to the complex search criteria.
			 *
			 * @param productIds The product IDs to add.
			 */
			addProductIds:function(productIds) {
				self.productSearchCriteria.productIds = productIds;
			},

			/**
			 * Add a list of item codes to the complex search criteria.
			 *
			 * @param itemCodes The item codes to add.
			 */
			addItemCodes:function(itemCodes) {
				self.productSearchCriteria.itemCodes = itemCodes;
			},

			/**
			 * Add a list of item classes to the complex search criteria.
			 *
			 * @param classCodes The case UPCs to add.
			 */
			addClasses:function(classCodes) {
				self.productSearchCriteria.classCodes = classCodes;
			},

			/**
			 * Add a list of commodities to the complex search criteria.
			 *
			 * @param commodityCodes The case UPCs to add.
			 */
			addCommodities:function(commodityCodes) {
				self.productSearchCriteria.commodityCodes = commodityCodes;
			},

			/**
			 * Add a list of sub-commodities the complex search criteria.
			 *
			 * @param subCommodityCodes The case UPCs to add.
			 */
			addSubCommodities:function(subCommodityCodes) {
				self.productSearchCriteria.subCommodityCodes = subCommodityCodes;
			},

			/**
			 * Add a list of case UPCs to the complex search criteria.
			 *
			 * @param caseUpcs The case UPCs to add.
			 */
			addCaseUpcs:function(caseUpcs) {
				self.productSearchCriteria.caseUpcs = caseUpcs;
			},

			/**
			 * Adds a sub-department to the complex search criteria.
			 *
			 * @param subDepartment The sub-department to add.
			 */
			addSubDepartment:function(subDepartment) {
				self.productSearchCriteria.subDepartment = subDepartment;
			},

			/**
			 * Adds an item class to the complex search criteria.
			 *
			 * @param itemClass The item class to add.
			 */
			addClass:function(itemClass) {
				self.productSearchCriteria.itemClass = itemClass;
			},

			/**
			 * Adds a commodity to the complex search criteria.
			 *
			 * @param classCommodity The commodity to add.
			 */
			addCommodity:function(classCommodity) {
				self.productSearchCriteria.classCommodity = classCommodity;
			},

			/**
			 * Adds a sub-commodity to the complex search criteria.
			 *
			 * @param subCommodity The sub-commodity to add.
			 */
			addSubCommodity:function(subCommodity) {
				self.productSearchCriteria.subCommodity = subCommodity;
			},

			/**
			 * Adds a BDM to the complex search criteria.
			 *
			 * @param bdm The BDM to add.
			 */
			addBdm:function(bdm) {
				self.productSearchCriteria.bdm = bdm;
			},

			/**
			 * Adds a vendor to the complex search criteria.
			 *
			 * @param vendor The vendor to add.
			 */
			addVendor:function(vendor) {
				self.productSearchCriteria.vendor = vendor;
			},

			/**
			 * Add a list of UPCs to the complex search criteria.
			 *
			 * @param upcs The list of UPC to add.
			 */
			addUpcs:function(upcs) {
				self.productSearchCriteria.upcs = upcs;
			},

			/**
			 * Adds a description search to the complex search.
			 *
			 * @param description The description the user entered to search for.
			 * @param searchByProductDescription Whether or not to search for product descriptions (product_master).
			 * @param searchByCustomerFriendlyDescription Whether or no to search for customer friendly descriptions.
			 * @param searchByDisplayName Whether or not to search by display name.
			 * @param searchAllExtendedAttributes Whether or not to searc all extended attribtues.
			 */
			addDescription:function(description, searchByProductDescription, searchByCustomerFriendlyDescription,
									searchByDisplayName, searchAllExtendedAttributes) {

				self.productSearchCriteria.description = description;
				self.productSearchCriteria.searchByProductDescription = searchByProductDescription;
				self.productSearchCriteria.searchByCustomerFriendlyDescription = searchByCustomerFriendlyDescription;
				self.productSearchCriteria.searchByDisplayName = searchByDisplayName;
				self.productSearchCriteria.searchAllExtendedAttributes = searchAllExtendedAttributes;
			},

			/**
			 * Returns the complex search criteria.
			 *
			 * @returns {{}|*}
			 */
			getProductSearchCriteria:function() {
				var searchCriteria = angular.copy(self.productSearchCriteria);

				// Add the byos objects to the search criteria.
				if (self.byosObjects.length > 0) {
					searchCriteria.customSearchEntries = self.byosObjects;
				}

				return searchCriteria;
			},

			/**
			 * Empties out the complex search criteria.
			 */
			clearProductSearchCriteria:function() {
				self.productSearchCriteria = {};
				self.byosObjects = [];
			},

			/**
			 * Sets the build your own search object the user has constructed.
			 *
			 * @param objectToSet The BYOS object to store.
			 */
			setByosObject:function(objectToSet) {
				self.byosObject = objectToSet;
			},

			/**
			 * Adds a build your own search object to the complex search criteria.
			 *
			 * @param objectToAdd The object to add.
			 */
			addByosObject:function(objectToAdd) {
				self.byosObjects.push(objectToAdd);
			},

			/**
			 * Removes a build your own search object to the complex search criteria.
			 *
			 * @param objectToRemove The object to remove.
			 */
			removeByosObject:function(objectToRemove) {
				var index = -1;
				for (var i = 0; i < self.byosObjects.length; i++) {
					if (self.byosObjects[i] == objectToRemove) {
						index = i;
						break;
					}
				}
				if (index >= 0) {
					self.byosObjects.splice(index, 1);
				}
			},

			getByosObjects:function() {
				return self.byosObjects;
			},

			/**
			 * This will return an object with the same structure as the complex
			 * search but with only one attribute populated. This should be called
			 * when the user clicks on the search button on the top part of the screen.
			 *
			 * @returns {*}
			 */
			getBasicSearchObject:function() {
				if (self.searchType == this.BASIC_SEARCH) {
					if (self.selectionType == this.PRODUCT_ID)	 {
						return {productIds: self.searchSelection};
					}
					if (self.selectionType == this.UPC) {
						return {upcs: self.searchSelection};
					}
					if (self.selectionType == this.ITEM_CODE) {
						return {itemCodes: self.searchSelection};
					}
					if (self.selectionType == this.CASE_UPC) {
						return {caseUpcs: self.searchSelection};
					}
					if (self.selectionType == this.CLASS) {
						return {classCodes: self.searchSelection};
					}
					if (self.selectionType == this.COMMODITY) {
						return {commodityCodes: self.searchSelection};
					}
					if (self.selectionType == this.SUB_COMMODITY) {
						return {subCommodityCodes: self.searchSelection};
					}
				}

				if (self.searchType == this.HIERARCHY_SEARCH) {
					if (self.selectionType == this.SUB_DEPARTMENT) {
						return {subDepartment: self.searchSelection};
					}
					if (self.selectionType == this.CLASS) {
						return {itemClass: self.searchSelection};
					}
					if (self.selectionType == this.COMMODITY) {
						return {classCommodity: self.searchSelection};
					}
					if (self.selectionType == this.SUB_COMMODITY) {
						return {subCommodity: self.searchSelection};
					}
				}

				if (self.selectionType == this.BDM) {
					return {bdm: self.searchSelection};
				}

				if (self.searchType == this.VENDOR_SEARCH) {
					return {vendor: self.searchSelection};
				}

				if (self.searchType == this.BYOS_SEARCH) {
					return {customSearchEntries: [self.byosObject]};
				}

				if (self.searchType == this.DESCRIPTION_SEARCH) {
					return {
						description: self.searchSelection,
						searchByProductDescription: self.searchByProductDescription,
						searchByCustomerFriendlyDescription: self.searchByCustomerFriendlyDescription,
						searchByDisplayName: self.searchByDisplayName,
						searchAllExtendedAttributes: self.searchAllExtendedAttributes
					};
				}
				return null;
			}

		}
	}
})();
