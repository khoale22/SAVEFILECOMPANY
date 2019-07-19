/*
 *  CaseUPCGeneratorController
 *  Copyright (c) 2018 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.utilities.caseUPCGenerator;

import com.heb.jaf.security.AuthorizedResource;
import com.heb.jaf.security.ViewPermission;
import com.heb.pm.ApiConstants;
import com.heb.pm.ResourceConstants;
import com.heb.util.controller.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
/**
 * REST Controller to support the case upc generator.
 *
 * @author vn86116
 * @since 2.15.0
 */
@RestController()
@RequestMapping(ApiConstants.BASE_APPLICATION_URL + "/utilities/caseUPCGenerator")
@AuthorizedResource(ResourceConstants.CASE_UPC_GENERATOR)
public class CaseUPCGeneratorController {
	private static final Logger logger = LoggerFactory.getLogger(CaseUPCGeneratorController.class);
	private static  final String CHECK_CASE_UPC_GENERATE_LOG_MESSAGE = "User %s from IP %s has requested check case UPC generate with number %s";
	private static final String CHECK_CASE_UPC_GENERATE_URL = "/getUpcGenerateForCaseUpc";

	@Autowired
	private UserInfo userInfo;

	@Autowired CaseUPCGeneratorService caseUPCGeneratorService;

	/**
	 * Method to get upc generate for case upc.
	 *
	 * @param upcNumber the input upc number
	 * @param isMrtGen is mrt gen or not.
	 * @return map contain result.
	 */
	@ViewPermission
	@RequestMapping(value = CHECK_CASE_UPC_GENERATE_URL,method = RequestMethod.GET)
	private Map<String,Object> getUpcGenerateForCaseUpc (@RequestParam(value = "upcNumber") Long upcNumber,
													  @RequestParam(value = "isMrtGen") Boolean isMrtGen,HttpServletRequest request) {
		logger.info(String.format(CaseUPCGeneratorController.CHECK_CASE_UPC_GENERATE_LOG_MESSAGE, this.userInfo.getUserId(), request.getRemoteAddr(),upcNumber));
		return caseUPCGeneratorService.getUpcGenerateForCaseUpc(upcNumber,isMrtGen);
	}
}
