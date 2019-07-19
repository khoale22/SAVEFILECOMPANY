/*
 *  PssDepartmentController
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.productDetails.casePack;

import com.heb.jaf.security.AuthorizedResource;
import com.heb.jaf.security.EditPermission;
import com.heb.jaf.security.ViewPermission;
import com.heb.pm.ApiConstants;
import com.heb.pm.ResourceConstants;
import com.heb.pm.entity.*;
import com.heb.pm.ws.CheckedSoapException;
import com.heb.util.audit.AuditRecord;
import com.heb.util.controller.ModifiedEntity;
import com.heb.util.controller.UserInfo;
import com.heb.util.jpa.LazyObjectResolver;
import com.heb.util.list.ListFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


/**
 * The controller PssDepartment.
 *
 * @author l730832
 * @since 2.8.0
 */
@RestController
@RequestMapping(ApiConstants.BASE_APPLICATION_URL + PssDepartmentController.PSS_DEPARTMENT_INFO)
@AuthorizedResource(ResourceConstants.CASE_PACK_PSS_DEPARTMENT)
public class PssDepartmentController {

	private static final Logger logger = LoggerFactory.getLogger(PssDepartmentController.class);

	protected static final String PSS_DEPARTMENT_INFO = "/pssDepartment";
	protected static final String UPDATE_PSS_DEPARTMENT = "/updateDepartment";
	protected static final String GET_DEPARTMENT_AUDIT = "/getDepartmentAudit";

	@Autowired
	private PssDepartmentService pssDepartmentService;

	@Autowired
	private UserInfo userInfo;

	//log messages
	private static final String MESSAGE_BY_METHOD = "User %s from IP %s requested %s";
	private static final String FIND_ALL_DEPARTMENT_MESSAGE = "all Department.";
	private static final String FIND_ALL_MERCHANDISE_TYPE_MESSAGE = "all Merchandise Type.";
	private static final String GET_DEPARTMENT_AUDIT_MESSAGE = "get department audit.";
	private static final String FIND_PSS_DEPARTMENT_CODE = "find pss department code by pss department number is %d";
	private static final String FIND_PSS_DEPARTMENT_CODE_BY_DEPARTMENT = "find pss department code by department number is %s and sub department is %s";
	private static final String FIND_ITEM_MASTER_BY_ITEM_TYPE_ITEM_ID = "find Item Master by item id is %s and item type code is %s";

	private static final String PSS_DEPARTMENT_SAVE_LOG = "to update the list of department [%s].";

	private static final String UPDATE_SUCCESSFUL = "Updated successfully";

	private LazyObjectResolver<ItemMaster> itemMasterLazyObjectResolver = new ItemMasterResolver();

	/**
	 * Resolves a ItemMaster object. It will load the following properties:
	 * 1 itemMasterKey
	 */
	private class ItemMasterResolver implements LazyObjectResolver<ItemMaster> {

		@Override
		public void fetch(ItemMaster itemMaster) {
			itemMaster.getKey().getItemCode();
			this.fetchMerchandiseTypes(itemMaster);
		}

		/**
		 * Resolves all of the merchandies types. JPA needs to be able to find a merchandise type code and the jpa resolver
		 * fails whenever the field is empty(not null). i.e. If the database doesn't have a pss department it is saved as ' '.
		 * This will trim all of the whitespace and then check to see if it is empty. This is lazily loaded because it should
		 * only be resolved for dsd items.
		 *
		 * @param im The item to fetch merchandise types for.
		 */
		private void fetchMerchandiseTypes(ItemMaster im) {
			if(!im.getMerchandiseTypeCodeOne().toString().trim().isEmpty()) {
				im.getMerchandiseTypeOne().getId();
			}

			if(!im.getMerchandiseTypeCodeTwo().toString().trim().isEmpty()) {
				im.getMerchandiseTypeTwo().getId();
			}

			if(!im.getMerchandiseTypeCodeThree().toString().trim().isEmpty()) {
				im.getMerchandiseTypeThree().getId();
			}

			if(!im.getMerchandiseTypeCodeFour().toString().trim().isEmpty()) {
				im.getMerchandiseTypeFour().getId();
			}
		}
	}

	/**
	 * Logs a users request for all pss department.
	 *
	 * @param ipAddress The IP address the user is logged in from.
	 * @messageFromMethod messageFromMethod The detail message sent from method.
	 */
	private void showLogFromMethodRequest(String ipAddress, String messageFromMethod) {
		PssDepartmentController.logger.info(String.format(PssDepartmentController.MESSAGE_BY_METHOD, this.userInfo
				.getUserId(), ipAddress, messageFromMethod));
	}

	/**
	 * Returns the item master information by item id and item type. (Refresh data item master after update department
	 * successful.)
	 *
	 * @param itemId The item id.
	 * @param itemTypCd The item type cd.
	 * @param request the request
	 * @return the list of item master.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = "findItemMaster")
	public ItemMaster findItemMaster(@RequestParam(value = "itemId", required = true) String itemId,
												  @RequestParam(value = "itemTypCd", required = true) String itemTypCd,
												  HttpServletRequest request){
		this.showLogFromMethodRequest(request.getRemoteAddr(), String.format(FIND_ITEM_MASTER_BY_ITEM_TYPE_ITEM_ID, itemId, itemTypCd));
		ItemMaster itemMaster = this.pssDepartmentService.findItemMaster(itemId, itemTypCd);
		this.itemMasterLazyObjectResolver.fetch(itemMaster);
		return itemMaster;
	}

	/**
	 * Returns the list of pss department by department id and sub department.
	 *
	 * @param department The department
	 * @param subDepartment The sub department
	 * @param request the request
	 * @return the list of pss department
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = "findPssDepartmentsByDepartmentAndSubDepartment")
	public List<PssDepartmentCode> findPssDepartmentsByDepartmentAndSubDepartment(@RequestParam(value = "department",required = true) String department,
														   @RequestParam(value = "subDepartment", required = true) String subDepartment,
														   HttpServletRequest request) throws CheckedSoapException {
		this.showLogFromMethodRequest(request.getRemoteAddr(), String.format(FIND_PSS_DEPARTMENT_CODE_BY_DEPARTMENT, department, subDepartment));
		return this.pssDepartmentService.findPssDepartmentsByDepartmentAndSubDepartment(department, subDepartment);
	}

	/**
	 * Returns a pss department at the product level. This is the default pss department.
	 *
	 * @param pssDepartmentId The department
	 * @param request the request
	 * @return the pss department attached at the product level
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = "findPssDepartmentCodeByPssDepartmentId")
	public PssDepartmentCode findPssDepartmentCodeByPssDepartmentId(@RequestParam(value = "pssDepartmentId", required = true) Integer pssDepartmentId,
												   						HttpServletRequest request) {
		this.showLogFromMethodRequest(request.getRemoteAddr(), String.format(FIND_PSS_DEPARTMENT_CODE, pssDepartmentId));
		return this.pssDepartmentService.findPssDepartmentCodeByPssDepartmentId(pssDepartmentId);
	}

	/**
	 * Returns list of department and information for the substitute sub department(pss department).
	 *
	 * @param request The HTTP request that initiated this call.
	 * @return An list of Department records.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = "findAllDepartment")
	public List<SubDepartment> findAllDepartment(HttpServletRequest request) {
		this.showLogFromMethodRequest(request.getRemoteAddr(), FIND_ALL_DEPARTMENT_MESSAGE);
		return this.pssDepartmentService.findAllDepartment();
	}

	/**
	 * Returns list of Merchandise Type with list of merchandise type hard for Department Screen.
	 *
	 * @param request The HTTP request that initiated this call.
	 * @return An list of MerchandiseType records.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = "findAllMerchandiseType")
	public List<MerchandiseType> findAllMerchandiseType(HttpServletRequest request) {
		this.showLogFromMethodRequest(request.getRemoteAddr(), FIND_ALL_MERCHANDISE_TYPE_MESSAGE);
		return this.pssDepartmentService.findAllMerchandiseType();
	}

	/**
	 * Updates list of pss department.
	 *
	 * @param pssDepartments The list of pss department to be updated.
	 * @param request the HTTP request that initiated this call.
	 * @return The list of pss department and a message for the front end.
	 */
	@EditPermission
	@RequestMapping(method = RequestMethod.POST,  value = PssDepartmentController.UPDATE_PSS_DEPARTMENT)
	public ModifiedEntity<String> updateDepartment(@RequestBody List<PssDepartment> pssDepartments, HttpServletRequest request){
		this.showLogFromMethodRequest(request.getRemoteAddr(), String.format(PssDepartmentController
				.PSS_DEPARTMENT_SAVE_LOG, ListFormatter.formatAsString(pssDepartments)));
		this.pssDepartmentService.updateDepartment(pssDepartments);
		return new ModifiedEntity<>(PssDepartmentController.UPDATE_PSS_DEPARTMENT, PssDepartmentController.UPDATE_SUCCESSFUL);
	}

	/**
	 * Retrieves published Attributes audit information.
	 * @param itemCode, itemType
	 * @param request The HTTP request that initiated this call.
	 * @return The list of published Attributes audits attached to given product ID.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = PssDepartmentController.GET_DEPARTMENT_AUDIT)
	public List<AuditRecord> getDepartmentAuditInfo(@RequestParam(value="itemCode") Long itemCode,
													@RequestParam(value="itemType") String itemType, HttpServletRequest request) {
		this.showLogFromMethodRequest(request.getRemoteAddr(), GET_DEPARTMENT_AUDIT_MESSAGE);
		return this.pssDepartmentService.getDepartmentAudit(itemCode, itemType);
	}
}
