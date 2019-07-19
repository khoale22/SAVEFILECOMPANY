/*
 *   adminComponent.js
 *
 *   Copyright (c) 2017 HEB
 *   All rights reserved.
 *
 *   This software is the confidential and proprietary information
 *   of HEB.
 *
 */
'use strict';

/**
 * Component of Admin (nav bar content) screen
 *
 * @author s753601
 * @since 2.5.0
 */
(function () {
	angular.module('productMaintenanceUiApp').component('sourcePriority', {
	// isolated scope binding
	bindings: {
		sourcePriority: '<'
	},
	// Inline template which is bound to message variable in the component controller
	templateUrl: 'src/admin/sourcePriority.html',
	// The controller that handles our component logic
	controller: SourcePriorityController
	});

	SourcePriorityController.$inject = ['SourcePriorityFactory', 'PMCommons', '$scope'];


	/**
	 * Product Info component's controller definition.
	 * @param $scope
	 * @constructor
	 */
	function SourcePriorityController(sourcePriorityFactory, PMCommons, $scope) {
		/** Product Info page's  all CRUD operation controller goes here */
		var self = this;

		/**
		 * Component ngOnInit lifecycle hook. This lifecycle is executed every time the component is initialized
		 * (or re-initialized).
		 */
		this.$onInit = function () {
			console.log('ProductInfoComp - Initialized');
			self.getData();//Get data from backend.
		};

		/**
		 * Component ngOnDestroy lifecycle hook. Called just before Angular destroys the directive/component.
		 */
		this.$onDestroy = function () {
			console.log('ProductInfoComp - Destroyed');
			/** Execute component destroy events if any. */
		};

		/**
		 * Used to retrieve product details from backend.
		 */
		self.getData = function() {
			PMCommons.displayLoading(self,true);
			var response = sourcePriorityFactory.getSourcePriorityTable()
			response.$promise.then(function(resp) {
				console.log(resp);
			}).catch(function(error) {
				console.log(error);
			}).finally(function(){
				PMCommons.displayLoading(self, false);
			});
		}
	}
})();
