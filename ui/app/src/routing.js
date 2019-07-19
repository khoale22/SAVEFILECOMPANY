'use strict';

/*
 * Decides the application states and which pages implement those states.
 */
(function () {
	angular.module('productMaintenanceUiApp').config(configFunction);

	function configFunction($stateProvider, $urlRouterProvider, $locationProvider, appConstants, KeepaliveProvider, IdleProvider, TitleProvider) {
		IdleProvider.idle(7200); //time is in seconds
		IdleProvider.timeout(300);
		IdleProvider.autoResume('notIdle'); //this line prevents user activity like mouse mvmt from dismissing timeout
		KeepaliveProvider.interval(10);
		TitleProvider.enabled(false);
		// Other path is not found in state provider, then navigate to not found page.
		$urlRouterProvider.otherwise(function ($injector) {
			var $state = $injector.get('$state');
			$state.go(appConstants.NOT_FOUND, { 'pageNotAccess': false});
		});
		$stateProvider
			.state(appConstants.LOGIN_STATE, {
				url: '/login',
				templateUrl: 'src/login/login.html',
				data: {
					pageTitle: 'Login'
				}
			})
			/*Handle case when user enter the url path is slash (/), then navigate to login page.*/
			.state('/', {
            	url: '/'
        	})
			/*Handle case when user enter the url path is empty, then navigate to login page.*/
			.state('//', {
            	url: ''
        	})
			.state(appConstants.HOME_STATE, {
				url: '/home',
				templateUrl: 'src/home/home.html',
				data: {
					pageTitle: 'Home'
				}
			})
			.state(appConstants.PRODUCT_DISCONTINUE_STATE, {
				url: '/productDiscontinue',
				templateUrl: 'src/productDiscontinue/productDiscontinue.html',
				data: {
					pageTitle: 'Product Discontinue',
					access: 'PD_DSCO_01'
				}
			})
			.state(appConstants.DISCONTINUE_ADMIN_RULES, {
				url: '/discontinueAdminRules',
				templateUrl: 'src/productDiscontinue/discontinueParameters.html',
				data: {
					pageTitle: 'Discontinue Admin Rules',
					access: 'PD_DSCO_02'
				}
			})
			.state(appConstants.DISCONTINUE_EXCEPTION_RULES, {
				url: '/discontinueExceptionRules',
				templateUrl: 'src/productDiscontinue/discontinueExceptionParameters.html',
				data: {
					pageTitle: 'Discontinue Exception Rules',
					access: 'PD_DSCO_03'
				}
			})
			.state(appConstants.PRODUCTION_SUPPORT_STATE, {
				url: '/productionSupport',
				templateUrl: 'src/productionSupport/productionSupport.html',
				data: {
					pageTitle: 'Production Support',
					access: 'IS_MENU_00'
				}
			})
			.state(appConstants.UPC_SWAP_STATE, {
				url: '/upcSwap',
				templateUrl: 'src/upcSwap/upcSwap.html',
				data: {
					pageTitle: 'UPC Swap'
				}
			})
			.state(appConstants.WAREHOUSE_TO_WAREHOUSE_STATE, {
				url: '/warehouseToWarehouse',
				templateUrl: 'src/upcMaintenance/warehouseToWarehouse.html',
				data: {
					pageTitle: 'Warehouse to Warehouse',
					access: 'PD_SWAP_01'
				}
			})
			.state(appConstants.WAREHOUSE_TO_WAREHOUSE_SWAP_STATE, {
				url: '/warehouseToWarehouseSwap',
				templateUrl: 'src/upcMaintenance/warehouseToWarehouseSwap.html',
				data: {
					pageTitle: 'Warehouse to Warehouse Swap',
					access: 'PD_SWAP_04'
				}
			})
			.state(appConstants.DSD_TO_BOTH_STATE, {
				url: '/dsdToBoth',
				templateUrl: 'src/upcMaintenance/dsdToBoth.html',
				data: {
					pageTitle: 'DSD to Both',
					access: 'PD_SWAP_02'
				}
			})
			.state(appConstants.BOTH_TO_DSD, {
				url: '/bothToDsd',
				templateUrl: 'src/upcMaintenance/bothToDsd.html',
				data: {
					pageTitle: 'Both to DSD',
					access: 'PD_SWAP_03'
				}
			})
			.state(appConstants.ADD_ASSOCIATE_STATE, {
				url: '/addAssociate',
				templateUrl: 'src/upcMaintenance/addAssociate.html',
				data: {
					pageTitle: 'Add Associate',
					access: 'PD_SWAP_05'
				}
			})
			.state(appConstants.SCALE_MANAGEMENT_UPC_MAINTENANCE, {
				url: '/scaleManagementUpcMaintenance',
				templateUrl: 'src/scaleManagement/scaleManagementUpcMaintenance.html',
				data: {
					pageTitle: 'UPC Maintenance',
					access: 'SM_UPCM_01'
				}
			})
			.state(appConstants.SCALE_MANAGEMENT_INGREDIENTS, {
				url: '/scaleManagementIngredients',
				templateUrl: 'src/scaleManagement/scaleManagementIngredients.html',
				data: {
					pageTitle: 'Ingredients',
					access: 'SM_INGR_01'
				}
			})
			.state(appConstants.SCALE_MANAGEMENT_NUTRIENTS, {
				url: '/scaleManagementNutrients',
				templateUrl: 'src/scaleManagement/scaleManagementNutrients.html',
				data: {
					pageTitle: 'Nutrients',
					access: 'SM_NTRN_01'
				}
			})
			.state(appConstants.SCALE_MANAGEMENT_NUTRIENT_STATEMENT, {
				url: '/scaleManagementNutrientStatement',
				templateUrl: 'src/scaleManagement/scaleManagementNutrientStatement.html',
				data: {
					pageTitle: 'Nutrient Statement',
					access: 'SM_NTRN_02'
				},
				params: {ntrStmtCode: null}
			})
			.state(appConstants.SCALE_MANAGEMENT_NLEA16_NUTRIENT_STATEMENT, {
				url: '/scaleManagementNLEA16NutrientStatement',
				templateUrl: 'src/scaleManagement/NLEA16NutrientStatement.html',
				data: {
					pageTitle: 'NLEA 2016 Nutrient Statement',
					access: 'SM_NLEA_01'
				},
				params: {nleaNtrStmtCode: null}
			})
			.state(appConstants.SCALE_MANAGEMENT_CODE_AUDIT, {
				url: '/scaleManagementCodeAudit',
				templateUrl: 'src/scaleManagement/scaleManagementCodeAudit.html',
				data: {
					pageTitle: 'Code Audit'
				}
			})
			.state(appConstants.SCALE_MANAGEMENT_LABEL_FORMATS, {
				url: '/scaleManagementLabelFormats',
				templateUrl: 'src/scaleManagement/labelFormats.html',
				data: {
					pageTitle: 'Label Formats',
					access: 'SM_CODE_02'
				}
			})
			.state(appConstants.SCALE_MANAGEMENT_REPORTS, {
				url: '/scaleManagementReports',
				templateUrl: 'src/scaleManagement/reports.html',
				data: {
					pageTitle: 'Reports',
					access: 'SM_MENU_01'
				}
			})
			.state(appConstants.REPORT_INGREDIENTS, {
				url: '/ingredientsReport',
				templateUrl: 'src/reports/ingredients.html',
				data: {
					pageTitle: 'Ingredients Report',
					access: 'RP_INGR_01'
				}
			})
			.state(appConstants.SCALE_MANAGEMENT_ACTION_CODES, {
				url: '/actionCodes',
				templateUrl: 'src/scaleManagement/actionCodes.html',
				data: {
					pageTitle: 'Action Codes',
					access: 'SM_CODE_01'
				}
			})
			.state(appConstants.SCALE_MANAGEMENT_GRAPHIC_CODES, {
				url: '/scaleManagement/graphicsCode',
				templateUrl: 'src/scaleManagement/graphicsCode.html',
				data: {
					pageTitle: 'Scale Management / Graphic Codes',
					access: 'SM_CODE_03'
				}
			})
			.state(appConstants.NUTRIENT_UOM_CODE, {
				url: '/scaleManagement/nutrientUomCode',
				templateUrl: 'src/scaleManagement/nutrientUomCode.html',
				data: {
					pageTitle: 'Scale Management / Nutrient Uom Codes',
					access: 'SM_CODE_04'
				}
			})
			.state(appConstants.SCALE_MANAGEMENT_INGREDIENT_CATEGORY_CODES, {
				url: '/ingredientCategoryCodes',
				templateUrl: 'src/scaleManagement/ingredientCategory.html',
				data: {
					pageTitle: 'Ingredient Category Codes',
					access: 'SM_CODE_03'
				}
			})
			.state(appConstants.SCALE_MANAGEMENT_INGREDIENT_STATEMENTS, {
				url: '/ingredientStatements',
				templateUrl: 'src/scaleManagement/ingredientStatements.html',
				data: {
					pageTitle: 'Ingredient Statements',
					access: 'SM_INGR_02'
				},
				params: {ingStmtCode: null}
			})
			.state(appConstants.PRODUCT_DETAILS, {
				url: '/productDetails',
				templateUrl: 'src/productDetails/productDetail.html',
				data: {
					pageTitle: 'Product Detail'
				}
			})
			.state(appConstants.GDSN_VENDOR_SUBSCRIPTIONS, {
				url: '/gdsnVendorSubscriptions',
				templateUrl: 'src/gdsn/vendorSubscription.html',
				data: {
					pageTitle: 'Vendor Subscriptions',
					access: 'GD_VEND_01'
				}
			})
			.state(appConstants.METADATA_ATTRIBUTES, {
				url: '/metadataAttributes',
				templateUrl: 'src/metadataAttributes/metadataAttributes.html',
				data: {
					pageTitle: 'Metadata Attributes',
					access: 'CT_MTDA_01'
				}
			})
			.state(appConstants.PRODUCT_HIERARCHY_ADMIN, {
				url: '/productHierarchy/admin',
				templateUrl: 'src/productHierarchy/productHierarchyAdmin.html',
				data: {
					pageTitle: 'Product Hierarchy'
				}
			})
			.state(appConstants.PRODUCT_HIERARCHY_DEFAULTS, {
				url: '/productHierarchy/defaults',
				templateUrl: 'src/productHierarchy/productHierarchyDefaults.html',
				data: {
					pageTitle: 'Product Hierarchy',
                    access: 'PH_VIEW_00'
				}
			})
			.state(appConstants.MASTER_ATTRIBUTE_TAXONOMY_HIERARCHY, {
				url: '/hierarchy/hierarchy',
				templateUrl: 'src/hierarchy/hierarchy.html',
				data: {
					pageTitle: 'Master Attribute Taxonomy Hierarchy',
					access: 'TX_HIERARCHY'
				}
			})
			.state(appConstants.CUSTOM_HIERARCHY_ADMIN, {
				url: '/customHierarchy',
				templateUrl: 'src/customHierarchy/customHierarchy.html',
				data: {
					pageTitle: 'Custom Hierarchy',
					access: 'CH_HIER_01'
				},
				params: {customerHierarchyId: null}
			})
			.state(appConstants.SOURCE_PRIORITY, {
				url: '/sourcePriority',
				templateUrl: 'src/eCommerce/sourcePriority.html',
				data: {
					pageTitle: 'Source Priority',
					access: 'EC_SRCP_01'
				}
			})
			.state(appConstants.ATTRIBUTE_MAINTENANCE, {
				url: '/attributeMaintenance',
				templateUrl: 'src/eCommerce/attributeMaintenance.html',
				data: {
					pageTitle: 'Attribute Maintenance',
					access: 'EC_AMAN_01'
				}
			})
			.state(appConstants.UTILITIES_DICTIONARY, {
				url: '/dictionary',
				templateUrl: 'src/utilities/dictionary.html',
				data: {
					pageTitle: 'Dictionary',
					access: 'UT_DICT_01'
				}
			})
			.state(appConstants.UTILITIES_CHECK_DIGIT, {
				url: '/checkDigitCalculator',
				templateUrl: 'src/utilities/checkDigitCalculator/checkDigitCalculator.html',
				data: {
					pageTitle: 'Check Digit Calculator',
					access: 'UT_CKDG_01'
				}
			})
			.state(appConstants.BATCH_UPLOAD_BY_CATEGORY_SPECIFIC_ATTRIBUTES, {
				url: '/batchUploadByCategorySpecificAttributes',
				templateUrl: 'src/batchUpload/categorySpecific/batchUploadByCategorySpecificAttributes.html',
				data: {
					pageTitle: 'Category Specific Attributes',
					access: 'BU_CSAT_01'
				}
			})
			.state(appConstants.ECOMMERCE_ATTRIBUTE, {
				url: '/batchUpload/ecommerceAttribute',
				templateUrl: 'src/batchUpload/eCommerceAttribute/eCommerceAttribute.html',
				data: {
					pageTitle: 'eCommerce Attributes',
					access: 'BU_ECOM_01'
				}
			})
			.state(appConstants.BATCH_UPLOAD_EXCEL_UPLOAD_BY_ASSORTMENT, {
				url: '/batchUpload/assortment',
				templateUrl: 'src/batchUpload/assortment/assortment.html',
				data: {
					pageTitle: 'assortment',
					access: 'BU_ASRT_01'
				}
			})
			.state(appConstants.BATCH_UPLOAD_EARLEY, {
				url: '/batchUpload/earley',
				templateUrl: 'src/batchUpload/earley/earleyUpload.html',
				data: {
					pageTitle: 'Earley',
					access: 'BU_EARL_00'
				}
			})
			.state(appConstants.BATCH_UPLOAD_RELATED_PRODUCTS, {
				url: '/batchUpload/relatedProducts',
				templateUrl: 'src/batchUpload/relatedProducts/relatedProducts.html',
				data: {
					pageTitle: 'Related Products',
					access: 'BU_RELP_01'
				}
			})
			.state(appConstants.BATCH_UPLOAD_MAT_ATTRIBUTE_VALUES, {
				url: '/batchUpload/matAttributeValues',
				templateUrl: 'src/batchUpload/matAttributeValues/matAttributeValues.html',
				data: {
					pageTitle: 'MAT Attribute Values',
					access: 'BU_MAT_01'
				}
			})
			.state(appConstants.CODE_TABLE, {
				url: '/code-table',
				templateUrl: 'src/codeTable/codeTableScreen.html',
				data: {
					pageTitle: 'Code Table ',
					access: 'CT_CODE_01'
				}
			})
			.state(appConstants.CHECK_STATUS, {
				url: '/utilities/checkstatus',
				templateUrl: 'src/utilities/checkstatus/checkstatus.html',
				data: {
					pageTitle: 'Status Checking',
					access: 'UT_DICT_01'
				},
				params : {trackingId: null}
			})
			.state(appConstants.BATCH_UPLOAD_BY_NUTRITION_AND_INGREDIENTS, {
				url: '/batchUpload/batchUploadByNutritionAndIngredients',
				templateUrl: 'src/batchUpload/nutritionIngredients/batchUploadByNutritionAndIngredients.html',
				data: {
					pageTitle: 'Nutrition and Ingredients',
					access: 'BU_NUDT_01'
				},
				params: {trackingId: null}
			})
			.state(appConstants.BATCH_UPLOAD_BY_SERVICE_CASE_SIGNS, {
				url: '/batchUpload/serviceCaseSigns',
				templateUrl: 'src/batchUpload/serviceCaseSigns/batchUploadByServiceCaseSigns.html',
				data: {
					pageTitle: 'Service Case Signs',
					access: 'BU_SCST_01'

				}
			})
			.state(appConstants.BATCH_UPLOAD_BY_PRIMO_PICK, {
				url: '/batchUpload/batchUploadByPrimoPick',
				templateUrl: 'src/batchUpload/primoPick/batchUploadByPrimoPick.html',
				data: {
					pageTitle: 'Primo Pick',
					access: 'BU_PRPK_01'
				}
			})
			.state(appConstants.PRODUCT_UPDATES_TASK, {
				url: '/task/productUpdatesTask',
				templateUrl: 'src/task/productUpdatesTask.html',
				data: {
					pageTitle: 'Product Updates Task',
					access: 'TK_PUDT_01'
				}
			})
			.state(appConstants.NUTRITION_UPDATES_TASK, {
				url: '/task/nutritionUpdatesTask',
				templateUrl: 'src/task/nutritionUpdatesTask.html',
				data: {
					pageTitle: 'Nutrition Updates Task',
					access: 'TK_NUDT_01'
				}
			})
			.state(appConstants.ECOMMERCE_TASK, {
				url: '/task/ecommerceTask/taskInfo/:taskId',
				templateUrl: 'src/task/ecommerceTaskDetail.html',
				data: {
					pageTitle: 'Ecommerce Task',
					access: 'TK_ECOM_01'
				}
			})
			.state(appConstants.IMAGE_MANAGEMENT, {
				url: '/imageUpload',
				templateUrl: 'src/imageUpload/imageUpload.html',
				data: {
					pageTitle: 'Image Upload',
					access: 'PI_MENU_00'
				}
			})
			.state(appConstants.PRODUCT_GROUP, {
				url: '/productGroup',
				templateUrl: 'src/productGroup/productGroup.html',
				data: {
					pageTitle: 'Product Group',
					access: 'PG_MENU_00'
				}
			})
			.state(appConstants.CASE_UPC_GENERATOR, {
				url: '/caseUPCGenerator',
				templateUrl: 'src/utilities/caseUPCGenerator/caseUPCGenerator.html',
				data: {
					pageTitle: 'Case UPC Generator',
					access: 'UT_CUPC_01'
				}
			})
			.state(appConstants.FIND_MY_ATTRIBUTE, {
				url: '/findMyAttribute',
				templateUrl: 'src/utilities/myAttribute/myAttribute.html',
				data: {
					pageTitle: 'Find My Attribute',
					access: 'UT_MENU_00'
				}
			})
			.state(appConstants.SCALE_MAINTENANCE_LOAD, {
				url: '/scaleMaintenance/load',
				templateUrl: 'src/scaleManagement/scaleMaintenanceLoad.html',
				data: {
					pageTitle: 'Scale Maintenance Load',
					access:'SM_MAINT_01'
				}
			})
			.state(appConstants.SCALE_MAINTENANCE_CHECK_STATUS, {
				url: '/scaleMaintenance/checkStatus',
				templateUrl: 'src/scaleManagement/scaleMaintenanceCheckStatus.html',
				data: {
					pageTitle: 'Scale Maintenance Check Status',
					access: 'SM_MAINT_02'
				},
				params: {transactionId: null}
			})
			.state(appConstants.SCALE_MAINTENANCE_STORE_DEPT, {
				url: '/scaleMaintenance/scaleMaintenanceStoreDept',
				templateUrl: 'src/scaleManagement/scaleMaintenanceStoreDept.html',
				data: {
					pageTitle: 'Scale Maintenance Store Dept',
					access: 'SM_MAINT_02'
				},
				params: {transactionId: null}
			})
			.state(appConstants.SCALE_MAINTENANCE_PANTRY_STORES, {
				url: '/scaleMaintenance/scaleMaintenancePantryStores',
				templateUrl: 'src/scaleManagement/scaleMaintenancePantryStores.html',
				data: {
					pageTitle: 'Scale Maintenance Pantry Stores',
					access: 'SM_MAINT_02'
				},
				params: {transactionId: null}
			})
			.state(appConstants.AUTHORIZATION, {
				url: '/authorization',
				templateUrl: 'src/authorization/authorizationMain.html',
				data: {
					pageTitle: 'Authorizations',
					access: 'AS_JOEV_00'
				}
			}).state(appConstants.BATCH_UPLOAD_EBM_BDA, {
				url: '/batchUpload/eBM_BDA',
				templateUrl: 'src/batchUpload/ebmBda/batchUploadByEbmBda.html',
				data: {
					pageTitle: 'eBM_BDA',
					access: 'BU_EBM_BDA'
				}
			}).state(appConstants.NOT_FOUND, {
				url: '/notFound',
				templateUrl: 'src/error/notFound.html',
				data: {
					pageTitle: 'Not Found'
				},
				params: {pageNotAccess: null}
			});
		$locationProvider.html5Mode(false);
	}
})();

/*
 .state(appConstants.JOBS_STATE, {
 url: '/',
 templateUrl: 'src/jobs/jobs.html'
 }).state(appConstants.EXECUTION_STATE, {
 url: '/execution',
 templateUrl: 'src/executions/executions.html',
 params: {
 jobName: null,
 windowId: null
 }
 }).state(appConstants.EXECUTION_DETAIL_STATE, {
 url: '/executionDetails',
 templateUrl: 'src/executionDetails/executionDetails.html',
 params: {
 jobId: null,
 parentWindowId: null
 }
 })
 */
