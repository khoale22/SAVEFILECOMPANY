/*
 * ExpirationDateCacheEventListener
 *
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.util.ehCache;

import net.sf.ehcache.CacheException;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import net.sf.ehcache.event.CacheEventListener;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class defines a listener to expire the eh cache at a specified time.
 *
 * This is based on code found here:
 * http://www.kdmooreconsulting.com/blogs/when-ehcache-elements-need-to-have-a-midnight-curfew/
 * by KD Moore Consulting | Keith D. Moore
 *
 * @author m314029
 * @since 2.4.0
 */
public class ExpirationDateCacheEventListener implements CacheEventListener{

	public static final Logger logger = LoggerFactory.getLogger(ExpirationDateCacheEventListener.class);
	private static final String CACHE_EXPIRATION_TIME_MESSAGE = "The cache: %s is set to expire: %s.";
	private static final DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd kk:mm:ss.SSS");

	private static final int FOUR_AM_HOUR = 4;
	private static final int FOUR_AM_MINUTE = 0;
	private static final int ONE_DAY = 1;

	@Override
	public void notifyElementRemoved(Ehcache cache, Element element) throws CacheException {
	}

	@Override
	public void notifyElementPut(Ehcache cache, Element element) throws CacheException {
		this.handleExpiration(cache, element);
	}

	private void handleExpiration(Ehcache cache, Element element) throws CacheException {
		DateTime currentTime = new DateTime();
		DateTime expirationDateTime = new DateTime(element.getExpirationTime());
		DateTime fourAm = new DateTime(
				DateTime.now().getYear(),
				DateTime.now().getMonthOfYear(),
				DateTime.now().getDayOfMonth(),
				FOUR_AM_HOUR,
				FOUR_AM_MINUTE).plusDays(ONE_DAY);
		if(expirationDateTime.isAfter(fourAm)){
			element.setTimeToLive(fourAm.minus(currentTime.getMillis()).getSecondOfDay());
			logger.info(String.format(ExpirationDateCacheEventListener.CACHE_EXPIRATION_TIME_MESSAGE,
					cache.getName(), fourAm.toString(dateTimeFormatter)));
		} else {
			logger.info(String.format(ExpirationDateCacheEventListener.CACHE_EXPIRATION_TIME_MESSAGE,
					cache.getName(), expirationDateTime.toString(dateTimeFormatter)));
		}
	}

	@Override
	public void notifyElementUpdated(Ehcache cache, Element element) throws CacheException {
	}

	@Override
	public void notifyElementExpired(Ehcache cache, Element element) {
	}

	@Override
	public void notifyElementEvicted(Ehcache cache, Element element) {
	}

	@Override
	public void notifyRemoveAll(Ehcache cache) {
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	@Override
	public void dispose() {
	}
}
