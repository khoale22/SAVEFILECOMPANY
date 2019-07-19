/*
 * VendorSubscriptionResolver
 *
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.gdsn;

import com.heb.util.jpa.LazyObjectResolver;

/**
 * Fully resolves VendorSubscription objects so they can be sent to the front end.
 *
 * @author d116773
 * @since 2.3.0
 */
public class VendorSubscriptionResolver implements LazyObjectResolver <VendorSubscription>{

	@Override
	public void fetch(VendorSubscription d) {
		Message m = d.getMessage();
		if (m != null) {
			m.getMessageErrors().size();
		}
	}
}
