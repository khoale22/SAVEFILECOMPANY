package com.heb.pm.salesChannel;

import com.heb.pm.entity.FulfillmentChannel;
import com.heb.pm.repository.FulfillmentChannelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Holds business logic related to sales and fulfilment channels.
 *
 * @author d116773
 * @since 2.14.0
 */
@Service
public class SalesChannelService {

	@Autowired
	private FulfillmentChannelRepository fulfillmentChannelRepository;

	/**
	 * Returns a list of all available fulfillment channels.
	 *
	 * @return A list of all available fulfillment channels.
	 */
	public List<FulfillmentChannel> fetchAllFulfilmentChannels() {
		return this.fulfillmentChannelRepository.findAll(FulfillmentChannel.getDefaultSort());
	}
}
