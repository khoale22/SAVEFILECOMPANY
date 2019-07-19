/*
 *  PssDepartmentService
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *  
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.productDetails.casePack;

import com.heb.pm.audit.AuditService;
import com.heb.pm.entity.*;
import com.heb.pm.repository.ItemMasterRepository;
import com.heb.pm.repository.MerchandiseTypeRepository;
import com.heb.pm.repository.PssDepartmentCodeRepository;
import com.heb.pm.repository.SubDepartmentRepository;
import com.heb.pm.ws.CheckedSoapException;
import com.heb.pm.ws.ProductHierarchyServiceClient;
import com.heb.pm.ws.ProductManagementServiceClient;
import com.heb.util.audit.AuditRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This holds all of the business logic for PssDepartment.
 *
 * @author l730832
 * @since 2.8.0
 */
@Service
public class PssDepartmentService {

	/**The const EMPTY_DESCRIPTION*/
	private static final String EMPTY_DESCRIPTION="";

	@Autowired
	private PssDepartmentCodeRepository pssDepartmentCodeRepository;

	@Autowired
	private SubDepartmentRepository subDepartmentRepository;

	@Autowired
	private MerchandiseTypeRepository merchandiseTypeRepository;

	@Autowired
	private ProductManagementServiceClient productManagementServiceClient;

	@Autowired
	private ProductHierarchyServiceClient productHierarchyServiceClient;

	@Autowired
	private ItemMasterRepository itemMasterRepository;

	@Autowired
	private AuditService auditService;

	/**
	 * Returns the item master information by item id and item type.
	 *
	 * @param itemId The item id.
	 * @param itemTypCd The item type cd.
	 * @return the list of item master.
	 */
	public ItemMaster findItemMaster(String itemId, String itemTypCd){
		ItemMasterKey itemMasterKey = new ItemMasterKey();
		itemMasterKey.setItemCode(Long.valueOf(itemId));
		itemMasterKey.setItemType(itemTypCd);
		return itemMasterRepository.findOne(itemMasterKey);
	}

	/**
	 * Returns a pss department according to the product. This is the default pss department.
	 *
	 * @param pssDepartmentId the department
	 * @return A pss department according to the product master.
	 */
	public PssDepartmentCode findPssDepartmentCodeByPssDepartmentId(Integer pssDepartmentId) {
		//Pss Department Code only find with PD_SCRN_CNTL_NO = 'I18MP462' AND PD_CNTL_FLD_CD='PD_PSS_DEPT_NO'.
		PssDepartmentCodeKey key = new PssDepartmentCodeKey(pssDepartmentId, PssDepartmentCode.PSS_IDENTIFIER_STRING,
				PssDepartmentCode.PSS_MAINFRAME_SCREEN);
		return this.pssDepartmentCodeRepository.findOne(key);
	}

	/**
	 * Returns list of department and information for the substitute sub department(pss department).
	 *
	 *  @return An list of Department records.
	 */
	public List<SubDepartment> findAllDepartment() {
		return this.subDepartmentRepository.findAll(SubDepartment.getDefaultSort());
	}

	/**
	 * Returns list of Merchandise Type with list of merchandise type hard for Department Screen.
	 * Filter MerchandiseType by with list of merchandise type hard for Department Screen.
	 *
	 *  @return An list of MerchandiseType records.
	 */
	public List<MerchandiseType> findAllMerchandiseType() {
		return merchandiseTypeRepository.findAll();
	}

	/**
	 * Returns the list of pss department by department id and sub department id.
	 *
	 * @param department The department
	 * @param subDepartment The sub department
	 * @return the list of pss department
	 */
	public List<PssDepartmentCode> findPssDepartmentsByDepartmentAndSubDepartment(String department, String subDepartment) throws CheckedSoapException{
		//Call Product Hierarchy Service to get list of pss department code basing department and subdepartment.
		List<Integer> pssDeptIds = this.productHierarchyServiceClient.findPssDepartmentsByDepartmentAndSubDepartment(department,subDepartment);
		//Collection list of pss department code return from product hierarchy service.
		List<PssDepartmentCode> pssDepartmentCodes = null;
		//Find more information of pss department (pss department description) from PD_NIS_CNTL_TAB table.
		if(pssDeptIds != null && !pssDeptIds.isEmpty()){
			pssDepartmentCodes = this.pssDepartmentCodeRepository.findByKeyIdIn(pssDeptIds);
		}
		//Set default pss department code with pssDeptIds have been received from webservice and empty description.
		//For case some pss department id return from ProductHierarchyService method getPSSubDeptByDeptSubDept but
		// not exist in PD_NIS_CNTL_TAB table.
		List<Integer> pssDeptIdsExits = new ArrayList<>();
		if(pssDepartmentCodes != null && !pssDepartmentCodes.isEmpty()) {
			pssDeptIdsExits = pssDepartmentCodes.stream().map(pd -> pd.getKey().getId()).collect(Collectors.toList());
		}
		pssDeptIds.removeAll(pssDeptIdsExits);
		if(pssDeptIds != null && !pssDeptIds.isEmpty()) {
			pssDepartmentCodes = pssDepartmentCodes == null? new ArrayList<>() :pssDepartmentCodes;
			PssDepartmentCode pssDepartmentCode = null;
			PssDepartmentCodeKey key = null;
			for (Integer pssDeptId : pssDeptIds) {
				pssDepartmentCode = new PssDepartmentCode();
				key = new PssDepartmentCodeKey(pssDeptId, PssDepartmentCode.PSS_IDENTIFIER_STRING, PssDepartmentCode.PSS_MAINFRAME_SCREEN);
				pssDepartmentCode.setKey(key);
				pssDepartmentCode.setDescription(EMPTY_DESCRIPTION);
				pssDepartmentCodes.add(pssDepartmentCode);
			}
		}
		return pssDepartmentCodes;
	}

	/**
	 * Updates list of department.
	 *
	 * @param pssDepartments The list of department to be updated.
	 * @return The list of department.
	 */
	public void updateDepartment(List<PssDepartment> pssDepartments){
		this.productManagementServiceClient.updatePssDepartment(pssDepartments);
	}

	/**
	 * This returns a list of online attributes audits based on the prodId.
	 * @param itemCode, itemType
	 * @return a list of all the changes made to an product's online attributes audits.
	 */
	List<AuditRecord> getDepartmentAudit(Long itemCode, String itemType) {
		return this.auditService.getCasePackDepartmentAudit(itemCode, itemType);
	}
}
