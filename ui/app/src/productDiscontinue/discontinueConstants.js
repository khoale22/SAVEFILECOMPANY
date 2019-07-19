'use strict';
/*
 * discontinueConstants.js
 *  *
 *  *
 *  *  Copyright (c) 2016 HEB
 *  *  All rights reserved.
 *  *
 *  *  This software is the confidential and proprietary information
 *  *  of HEB.
 *  *
 */

(function () {
    var app = angular.module('productMaintenanceUiApp');

    app.constant('discontinueDefinitions',{
        discontinue: 'Discontinue – When a product is discontinued by either the manufacturer or H-E-B Buyer/BDM for any reason where H-E-B will no longer be ordering, stocking, or selling the product.' +
            '\nDelete – When a product has been discontinued, and has met key rules to be qualified to be archived from all H-E-B applications and systems.',
        sales: 'Sales - Number of months of sales to be considered to discontinue a Product.' +
            ' For e.g. if set to 18 months - anything not sold for past 18 months would be a candidate for discontinue.',
        receipts: 'Receipts - Number of months since the last receipt of the product at store to determine a product discontinue.' +
            ' For e.g. if set to 18 months - any product not received in the last 18 months would be a viable candidate for discontinue. This include the freight.',
        inventory: 'Inventory - Max quantity of units on hand to be considered at Store/Warehouse for a product discontinue. ' +
            'For e.g. if set to 0 If the number of units on hand at store and warehouse needs to be 0 to allow discontinue of a product.',
        newProductSetup: 'New Product Set up - Age of the Product since entry into H-E-B systems. For e.g. if set to 12 months ' +
            '- any product setup in the last 12 months would be considered as a new product and would not be considered for discontinue.',
        purchaseOrder: 'Purchase Order - Active purchase orders if older than the months provided would be considered for discontinue.' +
            'Purchase orders in all the other status be considered as inactive and products would be marked for discontinue.',
		exceptions: 'Exceptions can be set on basis of SBT Vendor , Department , Class, Commodity, Sub Commodity, ' +
			'Merchandise type, Vendor, UPC in the increasing order of priority.'});
})();
