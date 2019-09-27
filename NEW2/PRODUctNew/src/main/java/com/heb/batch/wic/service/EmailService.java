/*
 *  EmailService
 *  Copyright (c) 2018 H-E-B
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of H-E-B.
 */
package com.heb.batch.wic.service;

import com.heb.batch.wic.exception.WicException;

/**
 * EmailService.
 * @author vn55306
 */

public interface EmailService {

	/**
	 * sendProductionSupport.
	 * @param subject String
	 * @param content String
	 * @param fromAddress String
	 * @param toAddress String
	 * @return the string
	 * @author vn55306
	 */
	String sendProductionSupportCorruptFile(String subject,String content,String fromAddress,String toAddress);
	/**
	 * sendEmailReport.
	 * @param subject String
	 * @param content String
	 * @param pathAttachFile String
	 * @param fromAddress String
	 * @param toAddress String
	 * @throws WicException IOException
	 * @return the string
	 * @author vn55306
	 */
	String sendEmailReport(String subject,String content,String pathAttachFile,String fromAddress,String toAddress) throws WicException;
}
