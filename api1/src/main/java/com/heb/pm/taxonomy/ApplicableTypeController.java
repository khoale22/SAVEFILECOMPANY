package com.heb.pm.taxonomy;

import com.heb.jaf.security.AuthorizedResource;
import com.heb.jaf.security.ViewPermission;
import com.heb.pm.ApiConstants;
import com.heb.pm.ResourceConstants;
import com.heb.pm.entity.ApplicableType;
import com.heb.util.controller.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Rest endpoint for ApplicableType.
 *
 * @author s573181
 * @since 2.22.0
 */
@RestController()
@RequestMapping(ApplicableTypeController.ROOT_URL)
@AuthorizedResource(ResourceConstants.CODE_TABLE_METADATA)
public class ApplicableTypeController {

	private static final Logger logger = LoggerFactory.getLogger(ApplicableTypeController.class);

	// URLS
	static final String ROOT_URL = ApiConstants.BASE_APPLICATION_URL + "/attribute/applicableType";


	// logs
	private static final String FIND_ALL_MESSAGE = "User %s from IP %s requested to find all ApplicableTypes.";

	@Autowired
	private UserInfo userInfo;

	@Autowired
	private ApplicableTypeService applicableTypeService;

	/**
	 * Returns all ApplicableTypes.
	 *
	 * @param request The HTTP request that initiated this call.
	 * @return all ApplicableTypes.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET)
	public List<ApplicableType> findAll(HttpServletRequest request) {
		ApplicableTypeController.logger.info(ApplicableTypeController.FIND_ALL_MESSAGE, this.userInfo.getUserId(),
				request.getRemoteAddr());
		return applicableTypeService.findAll();
	}

}
