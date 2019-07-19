package com.heb.pm.salesChannel;

import com.heb.pm.ApiConstants;
import com.heb.pm.entity.FulfillmentChannel;
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
 * Provides access to functions related to sales and fulfilment channels.
 *
 * @author d116773
 * @since 2.14.0
 */
@RestController
@RequestMapping(ApiConstants.BASE_APPLICATION_URL + "/salesChannel")
public class SalesChannelController {

	private static final Logger logger = LoggerFactory.getLogger(SalesChannelController.class);

	private static final String FETCH_ALL_FULFILMENT_CHANNELS_LOG_MESSAGE =
			"User %s from IP %s has requested a list of all fulfilment channels";

	@Autowired
	private UserInfo userInfo;

	@Autowired
	private SalesChannelService salesChannelService;

	/**
	 * Returns a list of all fulfilment channels.
	 *
	 * @param request The HTTP request that initiated this call.
	 * @return A list of all fulfilment channels.
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/fulfilmentChannel")
	public List<FulfillmentChannel> fetchAllFulfilmentChannels(HttpServletRequest request) {

		this.logFetchAllFulfilmentChannels(request.getRemoteAddr());

		return this.salesChannelService.fetchAllFulfilmentChannels();
	}

	/**
	 * Logs a user's request for fulfilment channels.
	 *
	 * @param ip The IP address the user is logged in from.
	 */
	private void logFetchAllFulfilmentChannels(String ip) {
		SalesChannelController.logger.info(
				String.format(SalesChannelController.FETCH_ALL_FULFILMENT_CHANNELS_LOG_MESSAGE,
						this.userInfo.getUserId(), ip));
	}
}
