package com.heb.pm.taxonomy;

import com.heb.jaf.security.AuthorizedResource;
import com.heb.jaf.security.ViewPermission;
import com.heb.pm.ApiConstants;
import com.heb.pm.ResourceConstants;
import com.heb.pm.entity.Audience;
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
 * Rest endpoint for Audience.
 *
 * @author s573181
 * @since 2.22.0
 */
@RestController()
@RequestMapping(AudienceController.ROOT_URL)
@AuthorizedResource(ResourceConstants.CODE_TABLE_METADATA)
public class AudienceController {

	private static final Logger logger = LoggerFactory.getLogger(AudienceController.class);

	// URLSs
	static final String ROOT_URL = ApiConstants.BASE_APPLICATION_URL + "/attribute/audience";


	// logs
	private static final String FIND_ALL_MESSAGE = "User %s from IP %s requested to find all AudienceController.";

	@Autowired
	private UserInfo userInfo;

	@Autowired
	private AudienceService audienceService;

	/**
	 * Returns all AudienceController.
	 *
	 * @param request The HTTP request that initiated this call.
	 * @return all AudienceController.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET)
	public List<Audience> findAll(HttpServletRequest request) {
		AudienceController.logger.info(AudienceController.FIND_ALL_MESSAGE, this.userInfo.getUserId(),
				request.getRemoteAddr());
		return audienceService.findAll();
	}
}
