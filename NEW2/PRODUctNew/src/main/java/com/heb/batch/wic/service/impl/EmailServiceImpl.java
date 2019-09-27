/*
 * $Id: EmailServiceImpl.java,v 1.9.2.5 2015/10/30 09:58:59 vn55228 Exp $
 *
 * Copyright (c) 2013 H-E-B
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of H-E-B.
 */
package com.heb.batch.wic.service.impl;

import com.heb.batch.wic.exception.WicException;
import com.heb.batch.wic.service.EmailService;
import com.heb.batch.wic.utils.EmailVO;
import com.heb.batch.wic.webservice.EmailServiceClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * EmailServiceImpl Class.
 * @author vn55306
 */
@Service
public class EmailServiceImpl implements EmailService {
	@Autowired
	private EmailServiceClient emailServiceClient;


	@Override
	public String sendProductionSupportCorruptFile(String subject,String content,String fromAddress,String toAddress) {
		EmailVO emailContent = new EmailVO();
		emailContent.setFromAddress(fromAddress);
		emailContent.setToAddress(toAddress);
		emailContent.setSubjectName(subject);
		emailContent.setBodyContent(content);
		return this.emailServiceClient.sendEmail(emailContent);
	}

	/**
	 * sendEmailReport.
	 *
	 * @param subject        String
	 * @param content        String
	 * @param pathAttachFile String
	 * @return the string
	 * @throws IOException IOException
	 * @throws Exception   Exception
	 * @author vn55306
	 */
	@Override
	public String sendEmailReport(String subject, String content, String pathAttachFile,String fromAddress,String toAddress) throws WicException {
		List<String> attachfiles = new ArrayList<>();
		EmailVO emailContent = new EmailVO();
		emailContent.setFromAddress(fromAddress);
		emailContent.setToAddress(toAddress);
		emailContent.setSubjectName(subject);
		emailContent.setBodyContent(content);
		attachfiles.add(pathAttachFile);
		emailContent.setAttachment(attachfiles);
		return this.emailServiceClient.sendEmailAttachment(emailContent);
	}


}
