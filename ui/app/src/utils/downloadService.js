/*
 * downloadService.js
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

'use strict';

/**
 * Creates the service with utilities related to downloading files.
 */
(function () {
	angular.module('productMaintenanceUiApp').service('DownloadService', downloadService);

	downloadService.$inject = ['$interval', '$cookies', '$http'];

	function downloadService($interval, $cookies, $http) {

		var self = this;

		/**
		 * The ID of the cookie the download process will send back.
		 *
		 * @type {string}
		 */
		self.cookieId = null;

		self.filename = null;

		/**
		 * The timer that will allow the service to look for the response.
		 *
		 * @type {Object}
		 */
		self.timer = null;

		/**
		 * The function to call once the download is done.
		 *
		 * @type {function}
		 */
		self.downloadFinishedCallback = null;

		/**
		 * Generates an ID to pass to the backend to track the export download.
		 *
		 * @returns {string}
		 */
		self.generateId = function () {
			return Math.floor((1 + Math.random()) * 0x100000).toString(16);
		};

		/**
		 * The function called by the timer to look for the results of the export.
		 */
		self.wait = function () {
			if (self.cookieId == null) {
				return;
			}
			var ck = $cookies.get(self.cookieId);
			if (typeof ck != 'undefined') {
				$interval.cancel(self.timer);
				$cookies.remove(self.cookieId);
				self.cookieId = null;
				self.downloadFinishedCallback();
			}
		};

		/**
		 * handle when the app got the data to download and export to file
		 *
		 * @param response the data using for exporting
		 */
		self.createFileToDownload = function (response) {
			var blob = new Blob([response.data], {type: "text/csv;charset=utf-8;"});
			window.navigator.msSaveOrOpenBlob(blob, self.filename);
		}

		return {

			/**
			 * The function for clients to call to initiate a download.
			 *
			 * @param url The URL to get the download from.
			 * @param fileName The name of the file to save the download to.
			 * @param waitTime The maximum number of seconds to wait for the download
			 * @param downloadFinishedCallback The function to call once the download completes.
			 */
			export: function (url, fileName, waitTime, downloadFinishedCallback) {
				self.downloadFinishedCallback = downloadFinishedCallback;

				self.cookieId = self.generateId();
				self.filename = fileName;

				// Add the ID to the URL. If there is not a parameter included already, downloadId becomes the primary parameter.
				if (url.indexOf('?') > -1) {
					url = url + "&downloadId=" + self.cookieId;
				}else{
					url = url +"?downloadId=" + self.cookieId;
				}

				// Create a link to the download and click on it.
				var anchor = angular.element('<a/>');
				//var blob = new Blob([url], {type:  "text/csv;charset=utf-8;"});
				if (window.navigator.msSaveBlob) { // IE
					$http({
						method: 'GET',
						url: url
					}).then(self.createFileToDownload);
				} else if (navigator.userAgent.search("Firefox") !== -1) { // Firefox
					anchor.css({display: 'none'});
					angular.element(document.body).append(anchor);

					anchor.attr({
						href: url,
						target: '_blank',
						download: fileName,
						type: 'text/csv'
					})[0].click();

					anchor.remove();
				} else { // Chrome
					anchor.attr({
						href: url,
						target: '_blank',
						download: fileName,
						type: 'text/csv'
					})[0].click();
				}


				// Start the timer to go off every second, wait a maximum of waitTime seconds.
				self.timer = $interval(self.wait, 1000, waitTime);
			}
		}
	}
})();
