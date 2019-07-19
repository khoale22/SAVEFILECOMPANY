/*
 *  CasePackCandidateController
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.productDetails.casePackCandidate;

import com.heb.jaf.security.AuthorizedResource;
import com.heb.jaf.security.EditPermission;
import com.heb.jaf.security.ViewPermission;
import com.heb.pm.ApiConstants;
import com.heb.pm.ResourceConstants;
import com.heb.pm.entity.*;
import com.heb.util.controller.ModifiedEntity;
import com.heb.util.controller.UserInfo;
import com.heb.util.jpa.LazyObjectResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Represents case pack in candidate mode.
 *
 * @author vn73545
 * @since 2.7.0
 */
@RestController()
@RequestMapping(ApiConstants.BASE_APPLICATION_URL + CasePackCandidateController.CASE_PACK_CANDIDATE)
@AuthorizedResource(ResourceConstants.CASE_PACK_INFO)
public class CasePackCandidateController {

    private static final Logger logger = LoggerFactory.getLogger(CasePackCandidateController.class);


    private static final String UPDATE_SUCCESS_MESSAGE = "CasePackCandidateController.updateSuccessful";
    private static final String ACTIVATE_SUCCESS_MESSAGE = "CasePackCandidateController.activateSuccessful";
    private static final String REJECT_SUCCESS_MESSAGE = "CasePackCandidateController.rejectSuccessful";

    private static final String DEFAULT_UPDATE_SUCCESS_MESSAGE = "Updated successfully.";
    private static final String DEFAULT_ACTIVATE_SUCCESS_MESSAGE = "Activated successfully.";
    private static final String DEFAULT_REJECT_SUCCESS_MESSAGE = "Rejected successfully.";

    private static final String LOG_UPDATE_CANDIDATE_CASE_PACK_INFO = "User %s from IP %s has requested update Candidate Case Pack Information with candidate item id: '%s'";
    private static final String LOG_UPDATE_CANDIDATE_CASE_PACK_IMPORT = "User %s from IP %s has requested update Candidate Case Pack Import with import item: '%s'";
    private static final String LOG_UPDATE_CANDIDATE_CASE_PACK_IMPORT_FACTORIES = "User %s from IP %s has requested update Candidate Case Pack Import Factories with import item: '%s'";
    private static final String LOG_UPDATE_CANDIDATE_CASE_PACK_VENDOR = "User %s from IP %s has requested update Candidate Case Pack Vendor with ps item id: '%s'";
    private static final String LOG_UPDATE_CANDIDATE_CASE_PACK_WAREHOUSE = "User %s from IP %s has requested update Candidate Case Pack Warehouse with ps item id: '%s'";
    private static final String LOG_UPDATE_CANDIDATE_CASE_PACK_WAREHOUSE_COMMENTS = "User %s from IP %s has requested update Candidate Case Pack Warehouse Comments with ps item id: '%s'";
    private static final String LOG_GET_CANDIDATE_INFORMATION = "User %s from IP %s has requested get Candidate information with ps item id: %s";
    private static final String LOG_GET_ALL_CANDIDATE_PRODUCT_TYPES = "User %s from IP %s has requested get all Candidate product types";
    private static final String LOG_ACTIVATE_CANDIDATE = "User %s from IP %s has requested activate Candidate with ps work id: %s";
    private static final String LOG_REJECT_CANDIDATE = "User %s from IP %s has requested reject Candidate with psWorkId: %s";

    protected static final String CASE_PACK_CANDIDATE = "/casePackCandidate";
    protected static final String GET_CANDIDATE_INFORMATION = "/getCandidateInformation";
    protected static final String GET_ALL_CANDIDATE_PRODUCT_TYPES = "/getAllCandidateProductTypes";
    protected static final String UPDATE_CASE_PACK_INFO_CANDIDATE = "/updateCasePackInfoCandidate";
    protected static final String UPDATE_CASE_PACK_IMPORT_CANDIDATE = "/updateCasePackImportCandidate";
    protected static final String UPDATE_CASE_PACK_IMPORT_FACTORIES_CANDIDATE = "/updateCasePackImportFactoriesCandidate";
    protected static final String UPDATE_CASE_PACK_VENDOR_CANDIDATE = "/updateCasePackVendorCandidate";
    protected static final String UPDATE_CASE_PACK_WAREHOUSE_CANDIDATE = "/updateCasePackWarehouseCandidate";
    protected static final String UPDATE_CASE_PACK_WAREHOUSE_COMMENTS_CANDIDATE = "/updateCasePackWarehouseCommentsCandidate";
    protected static final String ACTIVATE_CANDIDATE = "/activateCandidate";
    protected static final String REJECT_CANDIDATE = "/rejectCandidate";

	@Autowired
	private CasePackCandidateService service;

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private UserInfo userInfo;

	private LazyObjectResolver<List<CandidateItemMaster>> psItemMasterLazyObjectResolver = new CandidateItemMasterResolver();

	/**
	 * Resolves a CandidateItemMaster object. It will load the following properties:
	 */
	private class CandidateItemMasterResolver implements LazyObjectResolver<List<CandidateItemMaster>> {
		@Override
		public void fetch(List<CandidateItemMaster> candidateItemMasters) {
			candidateItemMasters.forEach((p) -> {
				if(p.getCandidateWorkRequest() != null){
					p.getCandidateWorkRequest().getCandidateProductMaster().size();
				}
				p.getCandidateWarehouseLocationItems().size();
				p.getCandidateWarehouseLocationItems().forEach((candidateWarehouseLocationItem) -> {
					if(candidateWarehouseLocationItem.getOrderQuantityType() != null){
						candidateWarehouseLocationItem.getOrderQuantityTypeDisplayName();
					}
					if(candidateWarehouseLocationItem.getFlowType() != null){
						candidateWarehouseLocationItem.getFlowType().getId();
					}
					if(candidateWarehouseLocationItem.getLocation() != null){
						candidateWarehouseLocationItem.getLocation().getApTypeCode();
					}
					candidateWarehouseLocationItem.getCandidateItemWarehouseComments().size();
				});
				p.getCandidateVendorLocationItems().size();
				p.getCandidateVendorLocationItems().forEach((candidateVendorLocationItem) -> {
					if(candidateVendorLocationItem.getVendorLocation() != null){
						candidateVendorLocationItem.getVendorLocation().getKey().getLocationNumber();
					}
					if(candidateVendorLocationItem.getSca() != null){
						candidateVendorLocationItem.getSca().getScaCode();
					}
					if(candidateVendorLocationItem.getCountry() != null){
						candidateVendorLocationItem.getCountry().getCountryId();
					}
					if(candidateVendorLocationItem.getCostOwner() != null && candidateVendorLocationItem.getCostOwner().getTopToTop() != null){
						candidateVendorLocationItem.getCostOwner().getTopToTop().getTopToTopId();
					}
					if(candidateVendorLocationItem.getLocation() != null && candidateVendorLocationItem.getLocation().getApLocation() != null){
						candidateVendorLocationItem.getLocation().getApLocation().getKey();
					}
					candidateVendorLocationItem.getCandidateVendorItemFactorys().size();
					candidateVendorLocationItem.getCandidateVendorItemFactorys().forEach((candidateVendorItemFactory) -> {
						if(candidateVendorItemFactory.getFactory() != null){
							candidateVendorItemFactory.getFactory().getFactoryId();
						}
					});
				});
			});
		}
	}

	/**
	 * Get candidate information by ps item id.
	 *
	 * @param psItmId - The ps item id.
	 * @param request The HTTP request that initiated this call.
	 * @return The list of CandidateItemMaster.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = CasePackCandidateController.GET_CANDIDATE_INFORMATION)
	public List<CandidateItemMaster> getCandidateInformation(@RequestParam("psItmId") Integer psItmId, HttpServletRequest request){
		CasePackCandidateController.logger.info(
                String.format(CasePackCandidateController.LOG_GET_CANDIDATE_INFORMATION,
                        this.userInfo.getUserId(), request.getRemoteAddr(), psItmId)
        );
		List<CandidateItemMaster> returnList = this.service.getCandidateInformation(psItmId);
		this.psItemMasterLazyObjectResolver.fetch(returnList);
		return returnList;
	}

	/**
	 * Get all candidate product types.
	 *
	 * @param request The HTTP request that initiated this call.
	 * @return list with all candidate product types.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = CasePackCandidateController.GET_ALL_CANDIDATE_PRODUCT_TYPES)
	public List<CandidateProductType> getAllCandidateProductTypes(HttpServletRequest request){
		CasePackCandidateController.logger.info(
                String.format(CasePackCandidateController.LOG_GET_ALL_CANDIDATE_PRODUCT_TYPES,
                        this.userInfo.getUserId(), request.getRemoteAddr())
        );
		return this.service.getAllCandidateProductTypes();
	}

	/**
	 * Updates case pack information in candidate mode.
	 *
	 * @param candidateItemMaster The item master that is sent to the web service to be updated.
	 * @return The list of CandidateItemMaster and a message for the front end.
	 */
	@EditPermission
	@RequestMapping(method = RequestMethod.POST, value = CasePackCandidateController.UPDATE_CASE_PACK_INFO_CANDIDATE)
	public ModifiedEntity<List<CandidateItemMaster>> updateCasePackInfoCandidate(@RequestBody CandidateItemMaster candidateItemMaster, HttpServletRequest request) {
		CasePackCandidateController.logger.info(
                String.format(CasePackCandidateController.LOG_UPDATE_CANDIDATE_CASE_PACK_INFO,
                        this.userInfo.getUserId(), request.getRemoteAddr(), candidateItemMaster.getCandidateItemId())
        );
		String updateMessage = this.messageSource.getMessage(
				CasePackCandidateController.UPDATE_SUCCESS_MESSAGE, null,
				CasePackCandidateController.DEFAULT_UPDATE_SUCCESS_MESSAGE, request.getLocale());
		this.service.updateCasePackInfoCandidate(candidateItemMaster, this.userInfo.getUserId());
		return new ModifiedEntity<>(null, updateMessage);
	}

	/**
	 * Updates case pack import in candidate mode.
	 *
	 * @param candidateVendorLocationItem The ps vend loc item to be updated.
	 * @param request the HTTP request that initiated this call.
	 * @return The list of CandidateItemMaster and a message for the front end.
	 */
	@EditPermission
	@RequestMapping(method = RequestMethod.POST, value = CasePackCandidateController.UPDATE_CASE_PACK_IMPORT_CANDIDATE)
	public ModifiedEntity<List<CandidateItemMaster>> updateCasePackImportCandidate(@RequestBody CandidateVendorLocationItem candidateVendorLocationItem, HttpServletRequest request){
		CasePackCandidateController.logger.info(
                String.format(CasePackCandidateController.LOG_UPDATE_CANDIDATE_CASE_PACK_IMPORT,
                        this.userInfo.getUserId(), request.getRemoteAddr(), candidateVendorLocationItem.getDisplayName())
        );
		String updateMessage = this.messageSource.getMessage(
				CasePackCandidateController.UPDATE_SUCCESS_MESSAGE, null,
				CasePackCandidateController.DEFAULT_UPDATE_SUCCESS_MESSAGE, request.getLocale());
		this.service.updateCasePackImportCandidate(candidateVendorLocationItem, this.userInfo.getUserId());
		return new ModifiedEntity<>(null, updateMessage);
	}

	/**
	 * Updates case pack import factories in candidate mode.
	 *
	 * @param candidateVendorLocationItem The ps vend loc item to be updated.
	 * @param request the HTTP request that initiated this call.
	 * @return The list of CandidateItemMaster and a message for the front end.
	 */
	@EditPermission
	@RequestMapping(method = RequestMethod.POST, value = CasePackCandidateController.UPDATE_CASE_PACK_IMPORT_FACTORIES_CANDIDATE)
	public ModifiedEntity<List<CandidateItemMaster>> updateCasePackImportFactoriesCandidate(@RequestBody CandidateVendorLocationItem candidateVendorLocationItem, HttpServletRequest request){
		CasePackCandidateController.logger.info(
                String.format(CasePackCandidateController.LOG_UPDATE_CANDIDATE_CASE_PACK_IMPORT_FACTORIES,
                        this.userInfo.getUserId(), request.getRemoteAddr(), candidateVendorLocationItem.getDisplayName())
        );
		String updateMessage = this.messageSource.getMessage(
				CasePackCandidateController.UPDATE_SUCCESS_MESSAGE, null,
				CasePackCandidateController.DEFAULT_UPDATE_SUCCESS_MESSAGE, request.getLocale());
		this.service.updateCasePackImportFactoriesCandidate(candidateVendorLocationItem, this.userInfo.getUserId());
		List<CandidateItemMaster> returnList = this.service.getCandidateInformation(candidateVendorLocationItem.getKey().getCandidateItemId());
		this.psItemMasterLazyObjectResolver.fetch(returnList);
		return new ModifiedEntity<>(returnList, updateMessage);
	}

	/**
	 * Updates case pack vendor in candidate mode.
	 *
	 * @param candidateItemMaster The ps item master to be updated.
	 * @param request the HTTP request that initiated this call.
	 * @return The list of CandidateItemMaster and a message for the front end.
	 */
	@EditPermission
	@RequestMapping(method = RequestMethod.POST, value = CasePackCandidateController.UPDATE_CASE_PACK_VENDOR_CANDIDATE)
	public ModifiedEntity<List<CandidateItemMaster>> updateCasePackVendorCandidate(@RequestBody CandidateItemMaster candidateItemMaster, HttpServletRequest request){
		CasePackCandidateController.logger.info(
                String.format(CasePackCandidateController.LOG_UPDATE_CANDIDATE_CASE_PACK_VENDOR,
                        this.userInfo.getUserId(), request.getRemoteAddr(), candidateItemMaster.getCandidateItemId())
        );
		String updateMessage = this.messageSource.getMessage(
				CasePackCandidateController.UPDATE_SUCCESS_MESSAGE, null,
				CasePackCandidateController.DEFAULT_UPDATE_SUCCESS_MESSAGE, request.getLocale());

		this.service.updateCasePackVendorCandidate(candidateItemMaster, this.userInfo.getUserId());
		return new ModifiedEntity<>(null, updateMessage);
	}

	/**
	 * Updates case pack warehouse in candidate mode.
	 *
	 * @param candidateItemMaster The ps item master to be updated.
	 * @param request the HTTP request that initiated this call.
	 * @return The list of CandidateItemMaster and a message for the front end.
	 */
	@EditPermission
	@RequestMapping(method = RequestMethod.POST, value = CasePackCandidateController.UPDATE_CASE_PACK_WAREHOUSE_CANDIDATE)
	public ModifiedEntity<List<CandidateItemMaster>> updateCasePackWarehouseCandidate(@RequestBody CandidateItemMaster candidateItemMaster, HttpServletRequest request){
		CasePackCandidateController.logger.info(
                String.format(CasePackCandidateController.LOG_UPDATE_CANDIDATE_CASE_PACK_WAREHOUSE,
                        this.userInfo.getUserId(), request.getRemoteAddr(), candidateItemMaster.getCandidateItemId())
        );
		String updateMessage = this.messageSource.getMessage(
				CasePackCandidateController.UPDATE_SUCCESS_MESSAGE, null,
				CasePackCandidateController.DEFAULT_UPDATE_SUCCESS_MESSAGE, request.getLocale());
		
		this.service.updateCasePackWarehouseCandidate(candidateItemMaster, this.userInfo.getUserId());
		return new ModifiedEntity<>(null, updateMessage);
	}

	/**
	 * Updates case pack warehouse comments in candidate mode.
	 *
	 * @param candidateWarehouseLocationItem The ps whse loc item to be updated.
	 * @param request the HTTP request that initiated this call.
	 * @return The list of CandidateItemMaster and a message for the front end.
	 */
	@EditPermission
	@RequestMapping(method = RequestMethod.POST, value = CasePackCandidateController.UPDATE_CASE_PACK_WAREHOUSE_COMMENTS_CANDIDATE)
	public ModifiedEntity<List<CandidateItemMaster>> updateCasePackWarehouseCommentsCandidate(@RequestBody CandidateWarehouseLocationItem candidateWarehouseLocationItem, HttpServletRequest request){
		CasePackCandidateController.logger.info(
                String.format(CasePackCandidateController.LOG_UPDATE_CANDIDATE_CASE_PACK_WAREHOUSE_COMMENTS,
                        this.userInfo.getUserId(), request.getRemoteAddr(), candidateWarehouseLocationItem.getKey().getCandidateItemId())
        );
		String updateMessage = this.messageSource.getMessage(
				CasePackCandidateController.UPDATE_SUCCESS_MESSAGE, null,
				CasePackCandidateController.DEFAULT_UPDATE_SUCCESS_MESSAGE, request.getLocale());
		this.service.updateCasePackWarehouseCommentsCandidate(candidateWarehouseLocationItem, this.userInfo.getUserId());
		List<CandidateItemMaster> returnList = this.service.getCandidateInformation(candidateWarehouseLocationItem.getKey().getCandidateItemId());
		this.psItemMasterLazyObjectResolver.fetch(returnList);
		return new ModifiedEntity<>(returnList, updateMessage);
	}

    /**
     * Activate Candidate by psWorkId.
     *
     * @param psWorkId the Candidate work request id.
     * @param request the http servlet request.
     * @return result of activation.
     */
    @EditPermission
    @RequestMapping(method = RequestMethod.GET, value = CasePackCandidateController.ACTIVATE_CANDIDATE)
    public ModifiedEntity<String> activateCandidate(@RequestParam("psWorkId") Long psWorkId, HttpServletRequest request) {
        CasePackCandidateController.logger.info(
                String.format(CasePackCandidateController.LOG_ACTIVATE_CANDIDATE,
                        this.userInfo.getUserId(), request.getRemoteAddr(), psWorkId)
        );
        this.service.activateCandidate(psWorkId);
        String updateMessage = this.messageSource.getMessage(
                CasePackCandidateController.ACTIVATE_SUCCESS_MESSAGE, null,
                CasePackCandidateController.DEFAULT_ACTIVATE_SUCCESS_MESSAGE, request.getLocale());
        return new ModifiedEntity<>(psWorkId.toString(), updateMessage);
    }

    /**
     * Reject Candidate by psWorkId.
     *
     * @param psWorkId the Candidate work request id.
     * @param request the http servlet request.
     * @return result of rejection.
     */
    @EditPermission
    @RequestMapping(method = RequestMethod.GET, value = CasePackCandidateController.REJECT_CANDIDATE)
    public ModifiedEntity<String> rejectCandidate(@RequestParam("psWorkId") Long psWorkId, HttpServletRequest request) {
        CasePackCandidateController.logger.info(
                String.format(CasePackCandidateController.LOG_REJECT_CANDIDATE,
                        this.userInfo.getUserId(), request.getRemoteAddr(), psWorkId)
        );
        this.service.rejectCandidate(psWorkId, this.userInfo.getUserId());
        String updateMessage = this.messageSource.getMessage(
                CasePackCandidateController.REJECT_SUCCESS_MESSAGE, null,
                CasePackCandidateController.DEFAULT_REJECT_SUCCESS_MESSAGE, request.getLocale());
        return new ModifiedEntity<>(psWorkId.toString(), updateMessage);
    }
}
