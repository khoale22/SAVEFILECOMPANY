/*
 *   caseUPCGeneratorController.js
 *
 *   Copyright (c) 2018 HEB
 *   All rights reserved.
 *
 *   This software is the confidential and proprietary information
 *   of HEB.
 */
'use strict';
/**
 * Controller to generate Upc number.
 *
 * @author 86116
 * @since 2.15.0
 */
(function () {
	angular.module('productMaintenanceUiApp').controller('CaseUPCGeneratorController', caseUPCGeneratorController);
	caseUPCGeneratorController.$inject = ['CaseUPCGeneratorApi' ];

	/**
	 * Constructs the controller.
	 * @param caseUPCGeneratorApi
	 */
	function caseUPCGeneratorController(caseUPCGeneratorApi) {
		var self = this;
		self.MESSAGE_PLEASE_ENTER_UPC_NUMBER = "Please enter Selling Unit UPC number.";
		self.MESSAGE_NUMBER_INPUT_OVER_THEN_DIGITS = "Case UPC can be generated only for Selling Unit with less than or equal to 10 digits.";
		self.success =  null;
		self.error = null;
		self.inputNumber = null;
		self.itemDetailTable = null;
		self.upcGeneratedIsNotMRT = null;
		self.upcGeneratedIsMRT = null;
		self.casePackList = [];
		self.listProductShipper = [];
		self.isWaiting = false;
		/**
		 * The default page size of main table.
		 *
		 * @type {number}
		 */
		const ZERO_STRING = "0";
		const UPC_CODE_MAX_LENGTH = 13;
		/**
		 * Function to call gennerate Upc Api.
		 *
		 * @param isMrtGen
		 */
		self.generateUpcNumber = function (isMrtGen) {
			self.casePackList = [];
			self.error = '';
			self.upcGeneratedIsNotMRT = '';
			self.upcGeneratedIsMRT = '';
			var params = [];
			params.isMrtGen = isMrtGen;
			if (!isMrtGen) {
				if (self.validation(self.inputNumber)) {
					params.upcNumber = self.inputNumber;
					self.isWaiting = true;
					caseUPCGeneratorApi.getUpcGenerateForCaseUpc(params).$promise.then(function (response) {
						self.isWaiting = false;
						if (!self.isNullOrEmpty(response.message)) {
							self.error = response.message;
						}
						else {
							self.upcGeneratedIsNotMRT = response.id + " - " + self.calculateCheckDigit(response.id);
							self.casePackList = self.getCasePackList(response);
						}
					});
				}
			}
			else if (isMrtGen){
				self.isWaiting = true;
				self.inputNumber = '';
				params.upcNumber = 0;
				caseUPCGeneratorApi.getUpcGenerateForCaseUpc(params).$promise.then(function (response) {
					self.isWaiting = false;
					if (!self.isNullOrEmpty(response.message)){
						self.error = response.message;
					}
					else {
						self.upcGeneratedIsMRT = response.id + " - " + self.calculateCheckDigit(response.id);
					}
				});
			}
		};

		/**
		 * Function to handle result from Api.
		 *
		 * @param response response result.
		 * @returns List of case pack.
		 */
		self.getCasePackList = function (response) {
			var casePackList = [];
			var listProductShipper = [];
			var listProductShipperCase2 = [];
			var lstCasePackByProdId =  angular.copy(response.lstCasePackByProdId);
			var lstCasePackByUpc = angular.copy(response.lstCasePackByUpc);
			if (!self.isNullOrEmpty(lstCasePackByProdId)) {
				for (var i = 0; i < lstCasePackByUpc.length; i++) {
					for (var j = 0; j < lstCasePackByProdId.length; j++) {
						if (lstCasePackByUpc[i].key.itemCode === lstCasePackByProdId[j].key.itemCode
							&& self.checkWhs(lstCasePackByUpc[i]) === self.checkWhs(lstCasePackByProdId[j])) {
							if (!self.isNullOrEmpty(casePackList)) {
								if (!self.isExistInArrayCasePack(lstCasePackByProdId[j],casePackList)) {
									casePackList.push(lstCasePackByProdId[j]);
								}
							}
							else casePackList.push(lstCasePackByProdId[j]);
							break;
						}
					}
				}
			}
			for (var i = 0; i < lstCasePackByUpc.length; i++) {
				if (!self.isNullOrEmpty(lstCasePackByUpc[i].primaryUpc.productShipper)){
					angular.forEach(lstCasePackByUpc[i].primaryUpc.productShipper,function (item) {
						listProductShipper.push(item);
					});
					break;
				}
			}
			angular.forEach (casePackList,function (item) {
				item.mrtAltPack = "-";
				if (item.mrt === true){
					item.mrtAltPack = "-"
				}
				else if (item.primaryUpc.productShipper !== null){
					for (var i = 0; i < item.primaryUpc.productShipper.length; i++) {
						if (item.orderingUpc === item.primaryUpc.productShipper[i].key.upc) {
							item.mrtAltPack = "Alt-Pack";
							break;
						}
					}
				}
			});
			angular.forEach (casePackList,function (casePack) {
				angular.forEach(listProductShipper,function (productShipper){
					if(casePack.orderingUpc === productShipper.key.shipperUpc){
						listProductShipperCase2.push(productShipper);
					}
				});
			});
			if(!self.isNullOrEmpty(listProductShipperCase2)){
				for (var i = 0; i < listProductShipperCase2.length; i++) {
					for (var j = 0; j < lstCasePackByProdId.length; j++){
						if(listProductShipperCase2[i].key.upc === lstCasePackByProdId[j].orderingUpc){
							lstCasePackByProdId[j].mrtAltPack = "-";
							if (lstCasePackByProdId[j].mrt === true){
								lstCasePackByProdId[j].mrtAltPack = "-"
							}
							else {
								for (var k = 0; k < listProductShipper.length; k++){
									if (lstCasePackByProdId[j].orderingUpc === listProductShipper[k].key.upc){
										lstCasePackByProdId[j].mrtAltPack = "Alt-Pack";
										break;
									}
								}
							}
							if (!self.isExistInArrayCasePack(lstCasePackByProdId[j],self.casePackList)){
								casePackList.push(lstCasePackByProdId[j]);
							}
						}
					}
				}
			}
			return casePackList;
		};

		/**
		 * Validation function to check input number before send to server.
		 *
		 * @returns {boolean} check result.
		 */
		self.validation =  function () {
			if (self.inputNumber === null || self.inputNumber === ''){
				self.error = self.MESSAGE_PLEASE_ENTER_UPC_NUMBER;
				return false;
			}
			else if (self.inputNumber.length>10){
				self.error = self.MESSAGE_NUMBER_INPUT_OVER_THEN_DIGITS;
				return false;
			}
			return true;
		}

		/**
		 * Check object null or empty
		 *
		 * @param object
		 * @returns {boolean} true if Object is null/ false or equals blank, otherwise return false.
		 */
		self.isNullOrEmpty = function (object) {
			return object === null || !object || object === "";
		};

		/**
		 * Function to handle and get check digit number.
		 *
		 * @param unitUpc generated Upc number.
		 * @returns {*} check digit number.
		 */
		self.calculateCheckDigit = function (unitUpc) {
			if(unitUpc !== null){
				var unitUpcBuffer = unitUpc.toString();
				unitUpcBuffer = self.paddingChar(unitUpcBuffer, ZERO_STRING, UPC_CODE_MAX_LENGTH);
				var checkDigit;
				var finalValue = 0;
				for (var i = 0; i < unitUpcBuffer.length; i++){
					if (i%2 === 1){
						finalValue = finalValue + parseInt(unitUpcBuffer.charAt(i));
					}
					else {
						var temp = parseInt(unitUpcBuffer.charAt(i));
						finalValue = finalValue + (temp*3);
					}
				}
				checkDigit = finalValue%10;
				if((checkDigit%10)!==0){
					checkDigit = Math.abs(10-checkDigit);
				}
				return checkDigit;
			}
			return 0;
		}

		/**
		 * Function to check a element is exist in a array or not.
		 *
		 * @param element the element.
		 * @param list the array list.
		 * @returns {boolean} check result.
		 */
		self.isExistInArrayCasePack = function (element,list) {
			for (var i=0 ; i<list.length ; i++){
				if (list[i].key.itemCode === element.key.itemCode&&list[i].key.itemType === element.key.itemType) {
					return true;
					break;
				}
			}
			return false;
		};

		/**
		 * Add character in front of string.
		 * @param val source number
		 * @param padCharacter to padding.
		 * @param length max length of string
		 * @returns {*}
		 */
		self.paddingChar = function(val, padCharacter, length) {
			while (val.length < length)
				val = padCharacter + val;
			return val;
		};

		self.checkWhs = function (casePack) {
			var resultCheck = false;
			if(casePack.channel === "ITMCD" || casePack.channel === "WHSE" || casePack.channel === "WHS"){
				resultCheck = true;
			}else{
				resultCheck = false;
			}
			return resultCheck;
		}
	}
})();
