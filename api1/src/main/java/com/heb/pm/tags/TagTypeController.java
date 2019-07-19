package com.heb.pm.tags;

import com.heb.jaf.security.AuthorizedResource;
import com.heb.jaf.security.ViewPermission;
import com.heb.pm.ApiConstants;
import com.heb.pm.ResourceConstants;
import com.heb.pm.entity.TagType;
import com.heb.pm.repository.TagTypeRepository;
import com.heb.util.controller.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;

/**
 * REST endpoint for requests related to tag type.
 *
 * @author d116773
 * @since 2.17.0
 */
@RestController
@RequestMapping(ApiConstants.BASE_APPLICATION_URL + "/tagType")
@AuthorizedResource(ResourceConstants.PRODUCT_BASIC_INFORMATION)
public class TagTypeController {

	private static final Logger logger = LoggerFactory.getLogger(TagTypeController.class);

	@Autowired
	private TagTypeRepository tagTypeRepository;

	@Autowired
	private UserInfo userInfo;

	/**
	 * Returns the list of all available tag types.
	 *
	 * @param request The HTTP request that initiated this call.
	 * @return The list of all available tag types.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET)
	public Collection<TagType> getAllTagTypes(HttpServletRequest request) {

		TagTypeController.logger.info(String.format("User %s from IP %s has requested a list of all tag types",
				this.userInfo.getUserId(), request.getRemoteAddr()));

		return this.tagTypeRepository.findAll();
	}
}
