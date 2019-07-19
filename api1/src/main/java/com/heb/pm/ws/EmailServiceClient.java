/*
 * EmailServiceClient
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
package com.heb.pm.ws;

import com.heb.util.ws.BaseWebServiceClient;
import com.heb.xmlns.ei.emailservice.EmailServicePortType;
import com.heb.xmlns.ei.emailservice.EmailServiceServiceagent;
import com.heb.xmlns.ei.sendemail_reply.SendEmailReply;
import com.heb.xmlns.ei.sendemail_request.SendEmailRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Service to send email messages
 *
 * @author a786878
 * @since 2.19.0
 */
@Service
public class EmailServiceClient extends BaseWebServiceClient<EmailServiceServiceagent, EmailServicePortType> {

    private static final Logger logger = LoggerFactory.getLogger(EmailServiceClient.class);

    @Value("${email.emailServiceUri}")
    private String uri;

    /**
     * Creates the service agent the class uses to send emails.
     *
     * @return The service agent the class uses to send emails.
     */
    @Override
    protected EmailServiceServiceagent getServiceAgent() {
        try {
            URL url = new URL(this.getWebServiceUri());
            return new EmailServiceServiceagent(url);
        } catch (MalformedURLException e) {
            return new EmailServiceServiceagent();
        }
    }

    /**
     * Creates the port the object uses to send emails.
     *
     * @param agent The agent to use to create the port.
     * @return The port the object uses to send emails.
     */
    @Override
    protected EmailServicePortType getServicePort(EmailServiceServiceagent agent) {
        return agent.getEmailService();
    }

    /**
     * Returns the URI to the email service.
     *
     * @return The URI to the email service.
     */
    @Override
    protected String getWebServiceUri() {
        return this.uri;
    }

    /**
     * Sending emails is delegeate to this function.
     *
     * @param fromAddress The address this email is from
     * @param toAddress The address to send emails to.
     * @param copyAddress The address to copy on the email. This can be null.
     * @param subject The email subject
     * @param message The message to send.
     */
    public void sendMail(String fromAddress, String toAddress, String copyAddress, String subject, String message) {

        SendEmailRequest request = new SendEmailRequest();

        request.setAuthentication(this.getAuthentication());
        request.setMailRequest(new SendEmailRequest.MailRequest());

        request.getMailRequest().setTOADDRESS(toAddress);
        if (copyAddress != null) {
            request.getMailRequest().setCCADDRESS(copyAddress);
        }

        request.getMailRequest().setFROMADDRESS(fromAddress);
        request.getMailRequest().setSUBJECTNAME(subject);
        request.getMailRequest().setBODYCONTENT(message);

        try {
            SendEmailReply reply = this.getPort().sendEmail(request);
            EmailServiceClient.logger.info(reply.getResponse());
        } catch (Exception e) {
            EmailServiceClient.logger.error(e.getMessage());
        }
    }
}