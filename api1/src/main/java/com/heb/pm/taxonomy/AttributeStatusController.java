package com.heb.pm.taxonomy;

import com.heb.jaf.security.AuthorizedResource;
import com.heb.jaf.security.ViewPermission;
import com.heb.pm.ApiConstants;
import com.heb.pm.ResourceConstants;
import com.heb.pm.entity.AttributeStatus;
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
 * Rest endpoint for AttributeStatus.
 *
 * @author s573181
 * @since 2.22.0
 */
@RestController()
@RequestMapping(AttributeStatusController.ROOT_URL)
@AuthorizedResource(ResourceConstants.CODE_TABLE_METADATA)
public class AttributeStatusController {


	private static final Logger logger = LoggerFactory.getLogger(AttributeStatusController.class);

	// URLS
	static final String ROOT_URL = ApiConstants.BASE_APPLICATION_URL + "/attribute/attributeStatus";


	// logs
	private static final String FIND_ALL_MESSAGE = "User %s from IP %s requested to find all AttributeStatus.";

	@Autowired
	private UserInfo userInfo;

	@Autowired
	private AttributeStatusService attributeStatusService;

	/**
	 * Returns all AttributeStatus.
	 *
	 * @param request The HTTP request that initiated this call.
	 * @return all AttributeStatus.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET)
	public List<AttributeStatus> findAll(HttpServletRequest request) {
		AttributeStatusController.logger.info(AttributeStatusController.FIND_ALL_MESSAGE, this.userInfo.getUserId(),
				request.getRemoteAddr());
		return attributeStatusService.findAll();
	}
}
