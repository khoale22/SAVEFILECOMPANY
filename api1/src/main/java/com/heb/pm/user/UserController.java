/*
 * UserController
 *
 *  Copyright (c) 2018 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
package com.heb.pm.user;

import com.heb.jaf.security.ViewPermission;
import com.heb.pm.ApiConstants;
import com.heb.pm.entity.User;
import com.heb.util.controller.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * This is the controller for User Info.
 *
 * @author s573181
 * @since
 */
@RestController()
@RequestMapping(ApiConstants.BASE_APPLICATION_URL + UserController.USER_INFORMATION_URL)
public class UserController {

	private static final Logger logger = LoggerFactory.getLogger(UserController.class);

	// urls
	protected static final String USER_INFORMATION_URL = "/userInformation";
	protected static final String GET_USER_BY_ID = "/getUserById";

	// logging
	private static final String USER_INFO_LOG_MESSAGE =
			"User %s from IP %s has requested user info for the id %s";

	@Autowired
	private UserService userService;

	@Autowired
	private UserInfo userInfo;

	@RequestMapping(method = RequestMethod.GET, value = UserController.GET_USER_BY_ID)
	public User getUserById(@RequestParam(value = "userId") String userId, HttpServletRequest request) {
		this.logUserSearch(request.getRemoteAddr(), userId);
		return this.userService.getUserById(userId.trim());
	}

	/**
	 * Sets the UserInfo for this object to use. This is used for testing.
	 *
	 * @param userInfo The UserInfo for this object to use.
	 */
	public void setUserInfo(UserInfo userInfo) {
		this.userInfo = userInfo;
	}

	/**
	 * Logs a search for a user.
	 *
	 * @param ip The IP address of the user searching for user.
	 * @param userId The user id.
	 */
	private void logUserSearch(String ip, String userId) {
		UserController.logger.info(
				String.format(UserController.USER_INFO_LOG_MESSAGE,
						this.userInfo.getUserId(), ip, userId)
		);
	}
}
