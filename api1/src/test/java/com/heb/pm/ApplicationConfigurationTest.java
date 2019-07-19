/*
 * ApplicationConfigurationTest
 *
 * Copyright (c) 2016 HEB
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of HEB.
 */

package com.heb.pm;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.boot.context.embedded.ServletListenerRegistrationBean;
import org.springframework.security.web.session.HttpSessionEventPublisher;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author d116773
 * @since 2.0.1
 */
public class ApplicationConfigurationTest {

	private ApplicationConfiguration applicationConfiguration = new ApplicationConfiguration();

	/*
	 * geJacksonBuilder
	 */

	/**
	 * Test geJacksonBuilder.
	 */
	@Test
	public void getJacksonBuilder() {
		ObjectMapper om = this.applicationConfiguration.getJacksonBuilder();

		// This will check that a mapper that can hanlde LocalDates and LocalDateTimes is loaded.
		Assert.assertTrue(om.canSerialize(LocalDate.class));
		Assert.assertTrue(om.canSerialize(LocalDateTime.class));

		// This feature needs to be turned off to get the timestamps to serialize correctly.
		Assert.assertFalse(om.getSerializationConfig().isEnabled(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS));

		// These features need to be disabled to match the default Spring behaviour.
		Assert.assertFalse(om.getSerializationConfig().isEnabled(MapperFeature.DEFAULT_VIEW_INCLUSION));
		Assert.assertFalse(om.getDeserializationConfig().isEnabled(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES));
	}

	/**
	 * Tests getJacksonBuilder with false for JSON indent.
	 */
	@Test
	public void getJacksonBuilderFalseJsonIndent() {
		this.applicationConfiguration.setIndentJson(false);

		ObjectMapper om = this.applicationConfiguration.getJacksonBuilder();
		Assert.assertFalse(om.getSerializationConfig().isEnabled(SerializationFeature.INDENT_OUTPUT));
	}

	/**
	 * Tests getJacksonBuilder with true for JSON indent.
	 */
	@Test
	public void getJacksonBuilderTrueJsonIndent() {
		this.applicationConfiguration.setIndentJson(true);

		ObjectMapper om = this.applicationConfiguration.getJacksonBuilder();
		Assert.assertTrue(om.getSerializationConfig().isEnabled(SerializationFeature.INDENT_OUTPUT));
	}

	/*
	 * getSessionDestroyedListener
	 */

	/**
	 * Tests getSessionDestroyedListener.
	 */
	@Test
	public void getSessionDestroyedListener() {

		// Not much to do other than check that it gets created.
		ServletListenerRegistrationBean<HttpSessionEventPublisher> listenerRegistrationBean =
				this.applicationConfiguration.getSessionDestroyedListener();
		Assert.assertNotNull(listenerRegistrationBean);
	}
}
