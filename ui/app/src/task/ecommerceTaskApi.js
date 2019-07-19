/*
 * ecommerceTaskApi.js
 *
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

'use strict';

/**
* API to support display eCommerce task details.
*
* @author vn40486
* @since 2.14.0
*/
(function () {

	angular.module('productMaintenanceUiApp').factory('EcommerceTaskApi', ecommerceTaskApi);
	ecommerceTaskApi.$inject = ['$http', 'urlBase', '$resource'];

	/**
	 * Constructs the API to call the backend functions related to ecommerce tasks.
	 *
	 * @param urlBase The Base URL for the back-end.
	 * @param $resource Angular $resource factory used to create the API.
	 * @returns {*}
	 */
	function ecommerceTaskApi($http, urlBase, $resource) {
		return $resource(null, null, {
			getActiveEcommerceTaskCount: {
				method: 'GET',
				url: urlBase + '/pm/task/ecommerceTask/ecommerceTaskCount',
				isArray: false
			},
			getAllTasks : {
				method: 'GET',
				url: urlBase + '/pm/task/ecommerceTask/tasks',
				isArray: false
			},
			createTask : {
				method: 'POST',
				url: urlBase + '/pm/task/ecommerceTask/create',
				isArray: false
			},
			getTaskInfo : {
				method: 'GET',
				url: urlBase + '/pm/task/ecommerceTask/taskInfo',
				isArray: false
			},
            deleteTask : {
                method: 'POST',
                url: urlBase + '/pm/task/ecommerceTask/delete',
                isArray: false
            },
			updateTask : {
				method: 'PUT',
				url: urlBase + '/pm/task/ecommerceTask/:taskId',
				params: {taskId:'@taskId'},
				isArray: false
			},
            updateTaskNotes : {
                method: 'POST',
                url: urlBase + '/pm/task/ecommerceTask/notes/update',
                isArray: false
            },
            deleteTaskNotes : {
                method: 'POST',
                url: urlBase + '/pm/task/ecommerceTask/notes/delete',
                isArray: false
            },
            updateProductNotes : {
                method: 'POST',
                url: urlBase + '/pm/task/ecommerceTask/productNotes/update',
                isArray: false
            },
            deleteProductNotes : {
                method: 'POST',
                url: urlBase + '/pm/task/ecommerceTask/productNotes/delete',
                isArray: false
            },
			getTaskDetail : {
				method: 'GET',
				url: urlBase + '/pm/task/ecommerceTask/taskDetail',
				isArray: false
			},
			getTaskProducts : {
				method: 'GET',
				url: urlBase + '/pm/task/ecommerceTask/taskProducts',
				isArray: false
			},
			getProductNotes : {
				method: 'GET',
				url: urlBase + '/pm/task/ecommerceTask/productNotes/:workRequestId',
				isArray: true
			},
			addProductNotes : {
				method: 'POST',
				url: urlBase + '/pm/task/ecommerceTask/productNotes/add',
				isArray: false
			},
			addProducts : {
				method: 'POST',
				url: urlBase + '/pm/task/ecommerceTask/products/add',
				isArray: false
			},
			removeProducts : {
				method: 'DELETE',
				url: urlBase + '/pm/task/ecommerceTask/products/delete',
				isArray: false
			},
			removeAllProducts : {
				method: 'DELETE',
				url: urlBase + '/pm/task/ecommerceTask/products/deleteAll',
				isArray: false
			},
			getProductsAssignee : {
				method: 'GET',
				url: urlBase + '/pm/task/ecommerceTask/products/assignee/:trackingId',
				isArray: true
			},
			getTaskNotes : {
				method: 'GET',
				url: urlBase + '/pm/task/ecommerceTask/notes/:taskId',
				isArray: true
			},
			addTaskNotes : {
				method: 'POST',
				url: urlBase + '/pm/task/ecommerceTask/notes/add',
				isArray: false
			},
			updateMassFillToProduct : {
				method: 'POST',
				url: urlBase + '/pm/task/ecommerceTask/updateMassFillToProduct',
				isArray: false
			},
			assignToBDM : {
				method: 'POST',
				url: urlBase + '/pm/task/ecommerceTask/assignToBdm',
				isArray: false
			},
			assignToEBM : {
				method: 'POST',
				url: urlBase + '/pm/task/ecommerceTask/assignToEbm',
				isArray: false
			},
			publishProduct: {
				method: 'POST',
				url: urlBase + '/pm/task/ecommerceTask/publishProduct',
				isArray: false
			}
		});

	}
})();
