/*
 * productSearchService.js
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
	angular.module('productMaintenanceUiApp').service('ProductSearchService', productSearchService);

	function productSearchService() {

		var self = this;

		self.searchCallback = null;
		self.complexSearchCallback = null;
		self.searchType = null;
		self.selectedTab = null;
		self.idTab = null;
		self.selectionType = null;
		self.searchSelection = null;
		self.selectedProductId = null;
		self.productGroupId = null;
		self.returnToListFlag = false;
		self.isBasicSearch = false;
		self.itemStatus = null;
		self.byosObject = null;
		self.byosObjects = [];
        self.byosObjectsBackup = [];
		self.showItemStatusFilterProperty = false;
		self.isSearchPanelVisible = true;

		self.showFullPanelProperty = true;
		self.productSearchCriteria = {};
        self.productSearchCriteriaBackup = {};
		self.pathCustomHierarchy = null;
		self.productUpdatesTaskCriteria = {};

		/**
		 * toggled sub tab
		 * @type {String}
		 */
		self.toggledTab = null;

        /**
         * Check clear multi search.
         * @type {boolean}
         */
        self.isClearMultiSearch = false;
		/**
		 * Selected product Id
		 * @type {Long}
		 */
		self.productGroupTypeCode = null;

		/**
		 * Selected alertId
		 * @type {Long}
		 */
		self.alertId = null;

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
		/**
		 * Disable return to list button at ecommerce view
		 * @type {boolean}
		 */
		self.disableReturnToList = true;
        /**
         * Clicked Return To List button Flag
         * @type {boolean}
         */
        self.returnToListButtonClicked = false;
		/**
		 * From page navigated to
		 * @type {String}
		 */
		self.fromPage = null;
        /**
		 * List Product received from detail task page.
		 */
		self.listOfProducts = [];
        /**
         * Backup List Product received from detail task page.
         */
        self.listOfProductsBackup = [];
        /**
         * Backup List Product received from detail task page temporarily.
         */
        self.listOfProductsTempBackup = [];
		/**
		 * Backup search type
		 * @type {String}
		 */
		self.searchTypeBackup = null;
		/**
		 * Backup selection type
		 * @type {String}
		 */
		self.selectionTypeBackup = null;
		/**
		 * Backup search selection
		 * @type {String}
		 */
		self.searchSelectionBackup = null;
		/**
		 * Backup message of activate candidate product
		 * @type {String}
		 */
		self.activateCandidateProductMessage = null;
		/**
		 * Navigate from home searching result
		 * @type {Boolean}
		 */
		self.navigateFromHomeSearchingResult = false;

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
			BYOS: "BYOS",
			CUSTOM_HIERARCHY: "CUSTOM_HIERARCHY",

			//Constants for the hierarchy search type
            DEPARTMENT_CLASS_COMMODITY_SUB_COMMODITY : "1",
            DEPARTMENT_CLASS_COMMODITY : "2",
            DEPARTMENT_COMMODITY_SUB_COMMODITY : "3",
            DEPARTMENT_CLASS : "4",
            COMMODITY_SUB_COMMODITY : "5",

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
				self.byosObjects = [];
				self.showFullPanelProperty = showFullPanel;
				self.showItemStatusFilterProperty = showStatusFilter;
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
			 * Returns whether or not to show the options to filter on item status.
			 *
			 * @returns {boolean|*}
			 */
			showItemStatusFilter:function() {
				return self.showItemStatusFilterProperty;
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
			 * Backups the type of search the user has chosen  (basic, hierarchy, bdm, etc.).
			 *
			 * @returns {null|*} The type of search the user has chosen.
			 */
			getSearchTypeBackup:function() {
				return self.searchTypeBackup;
			},
			/**
			 * Backups the type of search the user has chosen.
			 *
			 * @param searchType The type of search the user has chosen.
			 */
			setSearchTypeBackup:function(searchTypeBackup) {
				self.searchTypeBackup = searchTypeBackup;
			},

			/**
			 * Returns the values the user has chosen to search on. This will be the actual list of UPCs, etc., or an
			 * object representing a BDM or a level of  a hierarchy.
			 *
			 * @returns {null|*}
			 */
			getProductGroupTypeCode:function() {
				return self.productGroupTypeCode;
			},

			/**
			 * Sets the values the user has chosen to search on.
			 *
			 * @param selectedProductId
			 */
			setProductGroupTypeCode:function(productGroupTypeCode) {
				self.productGroupTypeCode = productGroupTypeCode;
			},

			/**
			 * Returns the values the user has chosen
			 * object representing a BDM or a level of  a hierarchy.
			 *
			 * @returns {null|*}
			 */
			getAlertId:function() {
				return self.alertId;
			},

			/**
			 * Sets the values the user has chosen
			 *
			 * @param alertId
			 */
			setAlertId:function(alertId) {
				self.alertId = alertId;
			},
			/**
			 * Returns return to list flag
			 *
			 * @returns {true|false}
			 */
			getReturnToListFlag:function() {
				return self.returnToListFlag;
			},

			/**
			 * Sets return to list flag.
			 *
			 * @param returnToListFlag
			 */
			setReturnToListFlag:function(returnToListFlag) {
				self.returnToListFlag = returnToListFlag;
			},

            /**
             * Returns isBasicSearch.
             *
             * @returns {true|false}
             */
            getIsBasicSearch:function() {
                return self.isBasicSearch;
            },

            /**
             * Sets isBasicSearch.
             *
             * @param isBasicSearch
             */
            setIsBasicSearch:function(isBasicSearch) {
                self.isBasicSearch = isBasicSearch;
            },

			/**
			 * Returns selected tab after
			 *
			 * @returns {null|*} The type of search the user has chosen.
			 */
			getSelectedTab:function() {
				return self.selectedTab;
			},

			/**
			 * Sets the type of search the user has chosen.
			 *
			 * @param searchType The type of search the user has chosen.
			 */
			setSelectedTab:function(selectedTab) {
				self.selectedTab = selectedTab;
			},

			/**
			 * Returns toggled tab after
			 *
			 * @returns {null|*} toggled tab.
			 */
			getToggledTab:function() {
				return self.toggledTab;
			},

			/**
			 * Sets toggled tab.
			 *
			 * @param toggled the toggled tab.
			 */
			setToggledTab:function(toggledTab) {
				self.toggledTab = toggledTab;
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
			 * Backup type of data in the search list.  For basic, this will be UPC, product ID, or item code. For
			 * BDM searches, this will be BDM. For hierarchy searches, this will be the level of the hierarchy.
			 *
			 * @returns {null|*} The type of data in the search list.
			 */
			getSelectionTypeBackup:function() {
				return self.selectionTypeBackup;
			},

			/**
			 * Sets the type of data in the search list.
			 *
			 * @param selectionType The type of data in the search list.
			 */
			setPathCustomHierarchy:function(pathCustomHierarchy) {
				self.pathCustomHierarchy = pathCustomHierarchy;
			},
			/**
			 * Backup type of data in the search list.  For basic, this will be UPC, product ID, or item code. For
			 * BDM searches, this will be BDM. For hierarchy searches, this will be the level of the hierarchy.
			 *
			 * @returns {null|*} The type of data in the search list.
			 */
			getPathCustomHierarchy:function() {
				return self.pathCustomHierarchy;
			},

			/**
			 * Backup the type of data in the search list.
			 *
			 * @param selectionType The type of data in the search list.
			 */
			setSelectionTypeBackup:function(selectionTypeBackup) {
				self.selectionTypeBackup = selectionTypeBackup;
			},

			/**
             * Returns listOfProducts
             *
             * @returns {null|*}
             */
            getListOfProducts:function() {
                return self.listOfProducts;
            },

            /**
             * Sets the values the listOfProducts.
             *
             * @param listOfProducts
             */
            setListOfProducts:function(listOfProducts) {
                self.listOfProducts = listOfProducts;
            },

            /**
             * Returns listOfProductsBackup
             *
             * @returns {null|*}
             */
            getListOfProductsBackup:function() {
                return self.listOfProductsBackup;
            },

            /**
             * Backup the values the listOfProducts.
             *
             * @param listOfProductsBackup
             */
            setListOfProductsBackup:function(listOfProductsBackup) {
                self.listOfProductsBackup = listOfProductsBackup;
            },
            /**
             * Returns listOfProductsTempBackup
             *
             * @returns {null|*}
             */
            getListOfProductsTempBackup:function() {
                return self.listOfProductsTempBackup;
            },

            /**
             * Backup the values the listOfProducts temporarily.
             *
             * @param listOfProductsTempBackup
             */
            setListOfProductsTempBackup:function(listOfProductsTempBackup) {
                self.listOfProductsTempBackup = listOfProductsTempBackup;
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
			/**
			 * Returns the values the user has chosen to search on. This will be the actual list of UPCs, etc., or an
			 * object representing a BDM or a level of  a hierarchy.
			 *
			 * @returns {null|*}
			 */
			getSearchSelectionBackup:function() {
				return self.searchSelectionBackup;
			},

			/**
			 * Sets the values the user has chosen to search on.
			 *
			 * @param searchSelection
			 */
			setSearchSelectionBackup:function(searchSelectionBackup) {
				self.searchSelectionBackup = searchSelectionBackup;
			},
			/**
			 * Returns the values the user has chosen to search on. This will be the actual list of UPCs, etc., or an
			 * object representing a BDM or a level of  a hierarchy.
			 *
			 * @returns {null|*}
			 */
			getSelectedProductId:function() {
				return self.selectedProductId;
			},

			/**
			 * Sets the values the user has chosen to search on.
			 *
			 * @param selectedProductId
			 */
			setSelectedProductId:function(selectedProductId) {
				self.selectedProductId = selectedProductId;
			},
			/**
			 * Returns the values the user has chosen to search on. This will be the actual list of UPCs, etc., or an
			 * object representing a BDM or a level of  a hierarchy.
			 *
			 * @returns {null|*}
			 */
			getProductGroupId:function() {
				return self.productGroupId;
			},

			/**
			 * Sets the values the user has chosen to search on.
			 *
			 * @param productGroupId
			 */
			setProductGroupId:function(productGroupId) {
				self.productGroupId = productGroupId;
			},
			/**
			 * Returns from page
			 *
			 * @returns {String}
			 */
			getFromPage:function() {
				return self.fromPage;
			},

			/**
			 * Sets from page.
			 *
			 * @param fromPage
			 */
			setFromPage:function(fromPage) {
				self.fromPage = fromPage;
			},

            /**
			 * Return Id tab.
             * @returns {null|*}
             */
            getIdTab:function() {
                return self.idTab;
            },

            /**
             * Sets Id tab.
             *
             * @param fromPage
             */
            setIdTab:function(idTab) {
                self.idTab = idTab;
            },


            /**
			 * Returns activate candidate product message.
			 *
			 * @returns {String}
			 */
			getActivateCandidateProductMessage:function() {
				return self.activateCandidateProductMessage;
			},

			/**
			 * Sets activate candidate product message.
			 *
			 * @param activateCandidateProductMessage
			 */
			setActivateCandidateProductMessage:function(activateCandidateProductMessage) {
				self.activateCandidateProductMessage = activateCandidateProductMessage;
			},

			/**
			 * Returns navigateFromHomeSearchingResult.
			 *
			 * @returns {Boolean}
			 */
			getNavigateFromHomeSearchingResult:function() {
				return self.navigateFromHomeSearchingResult;
			},

			/**
			 * Sets navigateFromHomeSearchingResult.
			 *
			 * @param navigateFromHomeSearchingResult
			 */
			setNavigateFromHomeSearchingResult:function(navigateFromHomeSearchingResult) {
				self.navigateFromHomeSearchingResult = navigateFromHomeSearchingResult;
			},

			/**
			 * Returns disable return to list flag
			 *
			 * @returns {true|false}
			 */
			getDisableReturnToList:function() {
				return self.disableReturnToList;
			},

			/**
			 * Sets disable return to list flag
			 *
			 * @param returnToListFlag
			 */
			setDisableReturnToList:function(disableReturnToList) {
				self.disableReturnToList = disableReturnToList;
			},

            /**
             * Returns the flag that "Return To List' button is clicked
             *
             * @returns {true|false}
             */
            getReturnToListButtonClicked:function() {
                return self.returnToListButtonClicked;
            },

            /**
             * Sets clicked return to list Button flag
             *
             * @param returnToListButtonClicked
             */
            setReturnToListButtonClicked:function(returnToListButtonClicked) {
                self.returnToListButtonClicked = returnToListButtonClicked;
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
			 * Sets whether or not to show the search panel.
			 *
			 * @param searchPanelVisible whether or not to show the search panel
			 */
			setSearchPanelVisibility:function(searchPanelVisible) {
				self.isSearchPanelVisible = searchPanelVisible;
			},

			/**
			 * Returns whether or not to show the search panel.
			 *
			 * @returns {boolean|*}
			 */
			isSearchPanelVisible:function() {
				return self.isSearchPanelVisible;
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
			 * Adds a lowest node level of the customer hierarchy search.
			 *
			 * @param lowestCustomerHierarchyNode
			 */
			addlowestCustomerHierarchyNode:function(lowestCustomerHierarchyNode) {
				self.productSearchCriteria.lowestCustomerHierarchyNode = lowestCustomerHierarchyNode ? lowestCustomerHierarchyNode.key : null;
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
			 * Returns the complex search criteria backup.
             * @returns {*}
             */
            getProductSearchCriteriaCallBack:function() {
                var searchCriteria = angular.copy(self.productSearchCriteriaBackup);

                // Add the byos objects to the search criteria.
                if (self.byosObjectsBackup.length > 0) {
                    searchCriteria.customSearchEntries = self.byosObjectsBackup;
                }

                return searchCriteria;
            },

            /**
			 * Backup product search criteria.
             */
            setBackupProductSearchCriteria:function(){
				self.productSearchCriteriaBackup = angular.copy(self.productSearchCriteria);
				self.byosObjectsBackup = angular.copy(self.byosObjects);
            },

			/**
			 * Empties out the complex search criteria.
			 */
			clearProductSearchCriteria:function() {
				self.productSearchCriteria = {};
				self.byosObjects = [];
			},

            /**
			 * Get byosObjects.
             * @returns {Array}
             */
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

				if(self.searchType == this.CUSTOM_HIERARCHY_SEARCH) {
					return {lowestCustomerHierarchyNode: this.getLowestNode().key};
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
			},
			/**
			 * Gets whether or not they are on the search page of the customer hierarchy.
			 * @returns {*}
			 */
			getCustomHierarchySearching: function() {
				return self.customHierarchySearching;
			},
			/**
			 * Sets whether or not they are on the search page of the customer hierarchy.
			 * @param isSearching
			 */
			setCustomerHierarchySearching: function(isSearching) {
				self.customHierarchySearching = isSearching;
				// self.lowestNode = null;
			},
			/**
			 * Sets the lowest node from the customer hierarchy.
			 * @param lowestNode
			 */
			setCustomerHierarchyLowestNode: function(lowestNode) {
				self.lowestNode =  lowestNode;
			},
			/**
			 * Retrieves the lowest node.
			 * @returns {*}
			 */
			getLowestNode: function() {
				if(typeof self.lowestNode !== undefined) {
					return self.lowestNode;
				} else {
					return null;
				}
			},

			/**
			 * set the name of the current lowest customer hierarchy
			 * @param lowestCustomerHierarchyNode
			 */
			setLowestCustomerHierarchyNodeName:function(lowestCustomerHierarchyNode){
				self.lowestCustomerHierarchyNodeName = lowestCustomerHierarchyNode.childDescription.shortDescription;
			},

			/**
			 * returns the lowest customer hierarchy node's name
			 * @returns {*}
			 */
			getLowestCustomerHierarchyNodeName:function(){
				return self.lowestCustomerHierarchyNodeName;
			},

			/**
			 * Removes a build your own search object on the complex search criteria, if that object type is
			 * already in the search
			 *
			 * @param typeToRemove The object to remove.
			 */
			removeByosObjectByType:function(typeToRemove) {
				var index = -1;
				for (var i = 0; i < self.byosObjects.length; i++) {
					if (self.byosObjects[i].type === typeToRemove) {
						index = i;
						break;
					}
				}
				if (index >= 0) {
					self.byosObjects.splice(index, 1);
				}
			}
		}
	}
})();
