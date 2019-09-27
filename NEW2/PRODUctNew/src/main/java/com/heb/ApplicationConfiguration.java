/*
 * ApplicationConfiguration
 *
 * Copyright (c) 2018 H-E-B
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of H-E-B.
 */
package com.heb;

import com.heb.batch.wic.WicJobLauncher;
import com.heb.batch.wic.utils.WicConstants;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Application configuration for things that cannot be configured in XML.
 *
 * @author vn55306
 * @since 1.0.0
 */
public class ApplicationConfiguration   {
	public static void main(String[] args) {
		String[] configLocation = {"application-config.xml"};
		ApplicationContext appContext = new ClassPathXmlApplicationContext(configLocation);
		WicJobLauncher wicJobLauncher = (WicJobLauncher) appContext.getBean("wicJobLauncher");
		wicJobLauncher.runJob(WicConstants.JOB_NAME, new JobParametersBuilder());
	}
}
