package com.heb.scaleMaintenanceSetUp.endpoint;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.heb.jaf.security.AuthorizedResource;
import com.heb.pm.ResourceConstants;
import com.heb.scaleMaintenance.ScaleMaintenanceConstants;
import com.heb.scaleMaintenance.service.ScaleMaintenanceService;
import com.heb.scaleMaintenanceSetUp.entity.GblParm;
import com.heb.scaleMaintenanceSetUp.entity.StoreDetails;
import com.heb.scaleMaintenanceSetUp.model.StoreDeptDetails;
import com.heb.scaleMaintenanceSetUp.service.StoreDepartmentSetupService;
import com.heb.util.controller.NonEmptyParameterValidator;
import com.heb.util.controller.UserInfo;
import com.heb.util.jpa.PageableResult;

/**
 * Rest endpoint for scale maintenance loads.
 *
 * @author m314029
 * @since 2.17.8
 */
@RestController()
@RequestMapping(ScaleMaintenanceConstants.BASE_SCALE_MAINTENANCE_URL + StoreDepartmentSetupEndPoint.BASE_LOAD_URL)
@AuthorizedResource(ResourceConstants.SCALE_MAINTENANCE_CHECK_STATUS)
public class StoreDepartmentSetupEndPoint{
	private static final Logger logger = LoggerFactory.getLogger(StoreDepartmentSetupEndPoint.class);
	static final String BASE_LOAD_URL = "/storeDepartment";
	static final String FIND_STORES = "/findStore";
	static final String DELETE_STORES = "/deleteStore";
	static final String UPDATE_STORES = "/updateStore";
	private static final String FIND_ALL_STORES = "/findAllStores";
	private static final String ADD_STORES = "/addStores";
	private static final String GET_STORE_DESC = "/getStoreDesc";

	

	@Autowired
	private NonEmptyParameterValidator parameterValidator;

	@Autowired
	private UserInfo userInfo;

	@Autowired
	private ScaleMaintenanceService service;
	
	@Autowired
	private StoreDepartmentSetupService strDeptService;

	
	/**
	 * Gets the List of Stores available for STR DEPT Authorization in JBATJOB.GBL_PARM Table
	 *
	 */
	
	@RequestMapping(method = RequestMethod.GET, value = FIND_ALL_STORES)
	public PageableResult<StoreDetails> fetchAllStores(
			@RequestParam(value = "page") Integer page,
			@RequestParam(value = "pageSize") Integer pageSize,
			@RequestParam(value = "includeCount") Boolean includeCount,
			@RequestParam(value = "detailsNeeded") String detailsNeeded,
			HttpServletRequest request) {

		StoreDepartmentSetupEndPoint.logger.info("Request Received For Store Dept Auth with page"+page + "pageSize"+pageSize+"IncludeCnt"+includeCount);
		return this.strDeptService.fetchAllStores(page, pageSize, includeCount,detailsNeeded);
	}
	
	/**
	 * Adds the new Store in GBL PARM Table
	 *
	 */
	@RequestMapping(method = RequestMethod.POST, value = ADD_STORES)
	public StoreDetails addStores(
			@RequestBody StoreDetails storeDetails,
			HttpServletRequest request) {
		logger.info("Received call for Adding Stores with Store Number: "+storeDetails.getStoreNum());
		return this.strDeptService.addStores(storeDetails);
	}
	
	/**
	 * TO get the Store Desc Based on the Store Number
	 *
	 */
	@RequestMapping(method = RequestMethod.GET, value = GET_STORE_DESC)
	public StoreDetails getStoreDesc(
			@RequestParam(value = "storeNum") String storeNum,@RequestParam(value = "detailsNeeded") String detailsNeeded,
			HttpServletRequest request) {
		logger.info("Received call for Store Description with Store Number: "+storeNum);
		return this.strDeptService.getStoreDesc(storeNum,detailsNeeded);
	}
	
	/**
	 * TO delete the store based on the Store Number
	 *
	 */
	@RequestMapping(method = RequestMethod.GET, value = DELETE_STORES)
	public StoreDetails deleteStore(
			@RequestParam(value = "storeNum") String storeNum,
			@RequestParam(value = "detailsNeeded") String detailsNeeded,
			HttpServletRequest request) {
		logger.info("Received call for Delete store with Store Number: "+storeNum);
		return this.strDeptService.deleteStore(storeNum,detailsNeeded);
	}
	
	/**
	 * Adds the Update Store in GBL PARM 
	 *
	 *//*
	@RequestMapping(method = RequestMethod.GET, value = UPDATE_STORES)
	public String updateStore(
			@RequestBody StoreDetails storeDetails,
			HttpServletRequest request) {
		logger.info("Received call for Updating Stores with Store Number: "+storeDetails.getStoreNum());
		return this.strDeptService.updateStore(storeDetails);
	}*/
}
