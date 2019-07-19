/*
 * BdmFromStringFormatter
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.productDiscontinue;

import com.heb.pm.entity.Bdm;
import com.heb.pm.repository.BdmRepository;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.format.Formatter;

import java.text.ParseException;
import java.util.Locale;

/**
 * The Bdm from string formatter.
 *
 * @author m314029
 * @since 2.0.6
 */
public class BdmFromStringFormatter implements Formatter<Bdm>, ApplicationContextAware {

	@Autowired
	BdmRepository bdmRepository;

	@Autowired
	ApplicationContext ctx;

	/**
	 * Parses the String passed in into a Bdm by retrieving it by bdm code (the string passed in). If the string
	 * is null or the bdm isn't found then an IllegalArgumentException is thrown.
	 *
	 * @param stringToParse The String to parse.
	 * @param locale The Locale used.
	 * @return a ProdDiscoExceptionType parsed from stringToParse.
	 */
	@Override
	public Bdm parse(String stringToParse, Locale locale) throws ParseException {

		if (this.bdmRepository == null) {
			this.bdmRepository = this.ctx.getBean(BdmRepository.class);
		}

		Bdm foundBdm;

		if(stringToParse != null){
			foundBdm = bdmRepository.findOne(stringToParse);
		} else {
			throw new IllegalArgumentException("Cannot parse null bdm code");
		}

		if(foundBdm != null){
			return foundBdm;
		} else {
			throw new IllegalArgumentException("No bdm found with bdm code: " + stringToParse);
		}
	}

	/**
	 * Takes a Bdm and turns it into a string.
	 *
	 * @param bdm The Bdm to turn into a String.
	 * @param locale The Locale used.
	 * @return A String representation of the Bdm.
	 */
	@Override
	public String print(Bdm bdm, Locale locale) {
		return bdm.toString();
	}

	/**
	 * This custom formatter was not being scanned for beans, so to create the autowired bean of BdmRepository, this
	 * class required a workaround: to include the application context in the class, and then pull the bdmRepository
	 * bean from it.
	 *
	 * @param applicationContext ApplicationContext of application.
	 * @throws BeansException
	 */
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.ctx = applicationContext;
	}
}
