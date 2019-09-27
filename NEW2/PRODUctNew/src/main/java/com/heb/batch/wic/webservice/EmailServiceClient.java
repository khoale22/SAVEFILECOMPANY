/*
 * EmailServiceClient
 *
 * Copyright (c) 2018 H-E-B
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of H-E-B.
 */
package com.heb.batch.wic.webservice;

import com.heb.batch.wic.dao.JobParamDAO;
import com.heb.batch.wic.utils.EmailVO;
import com.heb.batch.wic.utils.WicUtil;
import com.heb.batch.wic.webservice.util.BaseWebServiceClient;
import com.heb.xmlns.ei.EmailServicePortType;
import com.heb.xmlns.ei.EmailServiceServiceagent;
import com.heb.xmlns.ei.SendEmailAttachmentRequest;
import com.heb.xmlns.ei.SendEmailRequest;
import org.apache.axis.encoding.Base64;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.xml.rpc.ServiceException;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * EmailServiceClient.
 * @author vn55306
 */
@Service
public class EmailServiceClient extends BaseWebServiceClient<EmailServiceServiceagent, EmailServicePortType> {
	private static final Logger LOGGER = LogManager.getLogger(EmailServiceClient.class);
	public static final String FILE_CONTENT_TYPE = "application/octet-stream";
	/**
	 * DATE_MMM_dd_yyyy.
	 */
	public static final String DATE_FORMAT_FOR_ATT_EMAIL = "MMM_dd_yyyy";
	@Autowired
	private JobParamDAO jobParamDAO;

	//@Value("${emailService.endpoint}")
	private String uri;
	@PostConstruct
	private void initializeEmailServiceClient() {
		uri = this.jobParamDAO.getConfigurationInfor(JobParamDAO.J50X100_EMAIL_WS_ENDPOINT);
	}

	/**
	 * Subclasses should override this method to return an instance of the agent that will subsequently create
	 * the ports upon which service calls can be made.
	 *
	 * @return An instance of the ServiceAgent.
	 */
	@Override
	protected EmailServiceServiceagent getServiceAgent(){
		try {
			URL url = new URL(this.getWebServiceUri());
			return new EmailServiceServiceagent(url);
		} catch (MalformedURLException e) {
			return new EmailServiceServiceagent();
		}
	}

	/**
	 * Subclasses should override this method to return the port that will be used to make service calls.
	 *
	 * @param agent The agent to use to create the port.
	 * @return An instance of the port to use to make service calls.
	 */
	@Override
	protected EmailServicePortType getServicePort(EmailServiceServiceagent agent) {
		return agent.getEmailService();
	}

	/**
	 * Subclasses should override this to return the URI to connect to the web service. If it returns null, the
	 * one generated with the service JAR file will be used.
	 *
	 * @return The URI to connect to the web service.
	 */
	@Override
	protected String getWebServiceUri() {
		return this.uri;
	}
	/**
	 * sendEmailProductScanCodeWicWriter
	 *
	 * @param email EmailVO
	 * @return String
	 * @author vn55306
	 */
	public String sendEmail(EmailVO email) {
		String mess = "Could not send the email";
		SendEmailRequest erq = new SendEmailRequest();
		erq.setAuthentication(this.getAuthentication());
		if (StringUtils.isNotEmpty(email.getToAddress())) {
			erq.setMailRequest(this.createRequestMailRequest(email).getMailRequest());
			try {
				mess = this.getPort().sendEmail(erq).getResponse();
			} catch (Exception e) {
				LOGGER.error(e.getMessage(), e);
			}
		}
		return mess;
	}

	/**
	 * sendEmailAttachment
	 *
	 * @param email EmailVO
	 * @return String
	 * @throws IOException
	 * @throws Exception
	 * @author vn55306.
	 */
	public String sendEmailAttachment(EmailVO email) {
		String mess = "Could not send the email";
		SendEmailAttachmentRequest erq = new SendEmailAttachmentRequest();
		erq.setAuthentication(this.getAuthentication());
		if (email.getToAddress() != null || !StringUtils.EMPTY.equals(email.getToAddress().trim())) {
			erq.setMailRequest(createRequestEmailAttachment(email).getMailRequest());
			try {
				mess = this.getPort().sendEmailAttachment(erq).getMsg();
			} catch (Exception e) {
				LOGGER.error( e.getMessage(), e);
			}
		}

		return mess;
	}
	/**
	 * Convert EmailVO to SendEmail_RequestMailReques.
	 * @param email
	 *            : EmailVO
	 * @return SendEmail_RequestMailRequest
	 * @author vn55306
	 */
	private SendEmailRequest createRequestMailRequest(EmailVO email) {
		SendEmailRequest e = new SendEmailRequest();
		e.setMailRequest(new SendEmailRequest.MailRequest());
		e.getMailRequest().setTOADDRESS(email.getToAddress());
		e.getMailRequest().setFROMADDRESS(email.getFromAddress());
		e.getMailRequest().setSUBJECTNAME(email.getSubjectName());
		e.getMailRequest().setBODYCONTENT(email.getBodyContent());
		e.getMailRequest().setCCADDRESS(email.getCcAddress());
		return e;
	}
	/**
	 * Convert EmailVO to SendEmail_RequestMailReques.
	 * @param email
	 *            : EmailVO
	 * @return SendEmail_RequestMailRequest
	 * @author vn55306
	 * @throws Exception
	 */
	private SendEmailAttachmentRequest createRequestEmailAttachment(EmailVO email) {
		List<String> lstPath = email.getAttachment();
		SendEmailAttachmentRequest e = new SendEmailAttachmentRequest();
		e.setMailRequest(new SendEmailAttachmentRequest.MailRequest());
		//List<SendEmailAttachmentRequest.MailRequest.ATTACHMENT> attach = new ArrayList<>();
		String[] toAddresses = email.getToAddress().split(",");
		if(toAddresses!=null && toAddresses.length>0){
			for(String address:toAddresses){
				e.getMailRequest().getTOADDRESS().add(address);
			}
		}
		e.getMailRequest().setFROMADDRESS(email.getFromAddress());
		e.getMailRequest().setSUBJECTNAME(email.getSubjectName());
		e.getMailRequest().setBODYMSG(email.getBodyContent());
		//e.getMailRequest().setCCADDRESS(email.getCcAddress().split(","));
		int count = 0;
		String currentDay = WicUtil.getCurrentDay(DATE_FORMAT_FOR_ATT_EMAIL);
		if (lstPath !=null && lstPath.size()>0) {
			for (String filePath : lstPath) {
				File f = new File(filePath);
				SendEmailAttachmentRequest.MailRequest.ATTACHMENT file = new SendEmailAttachmentRequest.MailRequest.ATTACHMENT();
				file.setFILENAME(currentDay+"_"+f.getName().substring(0,f.getName().lastIndexOf('.')));
				file.setFILETYPE(f.getName().substring(f.getName().lastIndexOf('.') + 1));
				file.setCONTENTTYPE(FILE_CONTENT_TYPE);
				Path path = Paths.get(filePath);
				byte[] data;
				try {
					data = Files.readAllBytes(path);
					String bytesEncoded = Base64.encode(data);
					file.setFILECONTENT(bytesEncoded);
				} catch (IOException ex) {
					LOGGER.error(ex.getMessage(), ex);
				}
				e.getMailRequest().getATTACHMENT().add(file);
			}
		}

		return e;
	}
}