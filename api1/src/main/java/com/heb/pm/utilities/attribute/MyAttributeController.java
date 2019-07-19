/*
 *  MyAttributeController
 *  Copyright (c) 2018 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.utilities.attribute;

import com.heb.pm.ApiConstants;
import com.heb.pm.entity.AttributeMapping;
import com.heb.pm.entity.AttributeMappings;
import com.heb.util.controller.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.IOException;
import java.util.List;

/**
 * REST Controller to support attribute mappings.
 *
 * @author vn70529
 * @since 2.17.0
 */
@RestController()
@RequestMapping(ApiConstants.BASE_APPLICATION_URL + MyAttributeController.ATTRIBUTE_URL)
public class MyAttributeController {
	private static final Logger logger = LoggerFactory.getLogger(MyAttributeController.class);
	protected static final String ATTRIBUTE_URL = "/attribute";
	private static final String ATTRIBUTE_MAPPING_NAME = "AttributeMapping.xml";
	private static  final String FIND_ATTRIBUTE_LOG_MESSAGE = "User %s from IP %s has requested find all attributes";
	private static  final String FIND_ATTRIBUTE_LOG_ERROR = "User %s from IP %s has requested find all attributes occur error: %s";
	private static final String FIND_ATTRIBUTE_URL = "/findAllMyAttributes";
	@Autowired
	private UserInfo userInfo;

	/**
	 * Find all attribute mappings.
	 *
	 * @return map contain result.
	 */
	@RequestMapping(value = FIND_ATTRIBUTE_URL,method = RequestMethod.GET)
	public List<AttributeMapping> findAllMyAttributes(HttpServletRequest request) throws IOException {
		logger.info(String.format(MyAttributeController.FIND_ATTRIBUTE_LOG_MESSAGE, this.userInfo.getUserId(), request.getRemoteAddr()));
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(AttributeMappings.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			AttributeMappings attributeMappings = (AttributeMappings) jaxbUnmarshaller.unmarshal(new ClassPathResource(ATTRIBUTE_MAPPING_NAME).getInputStream());
			return attributeMappings.getAttributeMappings();
		} catch (JAXBException e) {
			logger.error(String.format(MyAttributeController.FIND_ATTRIBUTE_LOG_ERROR, this.userInfo.getUserId(), request.getRemoteAddr(), e.getMessage()));
		}
		return null;
	}
}
