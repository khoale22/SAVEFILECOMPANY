/*
 * ExpirationDateCacheEventListenerFactory
 *
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.util.ehCache;

import net.sf.ehcache.event.CacheEventListener;
import net.sf.ehcache.event.CacheEventListenerFactory;

import java.util.Properties;

/**
 * This class defines a factory that creates an instance of the eh cache listener.
 *
 * This is based on code found here:
 * http://www.kdmooreconsulting.com/blogs/when-ehcache-elements-need-to-have-a-midnight-curfew/
 * by KD Moore Consulting | Keith D. Moore
 *
 * @author m314029
 * @since 2.4.0
 */
public class ExpirationDateCacheEventListenerFactory extends CacheEventListenerFactory {
	@Override
	public CacheEventListener createCacheEventListener(Properties properties) {
		return new ExpirationDateCacheEventListener();
	}
}
