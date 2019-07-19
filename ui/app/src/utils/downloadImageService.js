/*
 * downloadImageService.js
 *
 *  Copyright (c) 2018 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
'use strict';

/**
 * Creates the service with utilities related to downloading image.
 */
(function () {
	angular.module('productMaintenanceUiApp').service('DownloadImageService', downloadImageService);
    downloadImageService.$inject = ['$interval', '$filter'];

	function downloadImageService($interval, $filter) {
        var self = this;

        /**
		 * Convert image base 64 to byte array
		 * @param b64Data
		 * @param contentType
		 * @param sliceSize
		 * @returns {Blob}
		 */
		self.b64toBlob = function(b64Data, contentType, sliceSize){
			contentType = contentType || '';
			sliceSize = sliceSize || 512;

			var byteCharacters = atob(b64Data);
			var byteArrays = [];

			for (var offset = 0; offset < byteCharacters.length; offset += sliceSize) {
				var slice = byteCharacters.slice(offset, offset + sliceSize);

				var byteNumbers = new Array(slice.length);
				for (var i = 0; i < slice.length; i++) {
					byteNumbers[i] = slice.charCodeAt(i);
				}

				var byteArray = new Uint8Array(byteNumbers);

				byteArrays.push(byteArray);
			}

			var blob = new Blob(byteArrays, {type: contentType});
			return blob;
		}

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

        return {

			download: function (imageBytes, imageFormat) {
				if(imageBytes != null){
                    var type = imageFormat==''?'png':imageFormat.trim();
                    var contentType = 'image/'+type;
                    //replace data image base64 tag format
                    var b64Data = imageBytes.replace("data:image/jpg;base64,","");
                    //convert base64 to byte array
                    var blob = self.b64toBlob(b64Data, contentType);
                    //file name
                    var name = 'Image'+ $filter('date')(new Date(), 'MM/dd/yyyy HH:mm:ss')+'.'+type;
                    //generate image to download
                    if (imageBytes != null && navigator.msSaveBlob)
                        return navigator.msSaveBlob(new Blob([blob], { type: "octet/stream" }), name);
                    var a = $("<a style='display: none;'/>");
                    var url = window.URL.createObjectURL(new Blob([blob], {type: "octet/stream"}));
                    a.attr("href", url);
                    a.attr("download", name);
                    $("body").append(a);
                    a[0].click();
                    window.URL.revokeObjectURL(url);
                    a.remove();

                    // Start the timer to go off every second, wait a maximum of waitTime seconds.
                    self.timer = $interval(self.wait, 1000, 10000);
                }
			}
		}

    }
})();
