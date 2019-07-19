/*
 * MassUploadMessageTibcoSender
 *
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
package com.heb.pm.batchUpload.jms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.JmsException;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
/**
 * This is template for AssortmentCandidateWorkRequestCreator.
 *
 * @author vn55306
 * @since 2.8.0
 */
@Service
public class BatchUploadMessageTibcoSender {
    private static final Logger logger = LoggerFactory.getLogger(BatchUploadMessageTibcoSender.class);
    /**
     * DATE_YYYYMMDDHHMMSSSS.
     */
    public static final String DATE_YYYYMMDDHHMMSSSS = "yyyy-MM-dd'-'HH.mm.ss.SSSSSS";
    /**
     * STRING_SRC_SYSTEM.
     */
    public static final String STRING_SRC_SYSTEM = "04";

    /**
     * SPACE.
     */
    public static final String SPACE = " ";
	@Autowired
	@Qualifier("tibcoJmsTemplate")
	private JmsTemplate jmsTemplate;
    /**
     * Send message to JMS Queue.
     * @param message
     *            String
     * @author vn55306
     */

    public void sendMesageToJMSQueue(final String message){
        try {
            jmsTemplate.send(new MessageCreator(){
                @Override
                public Message createMessage(Session session) throws JMSException{
                    TextMessage textMessage = session.createTextMessage(message);
                    return textMessage;
                }
            });
        } catch (JmsException e) {
            logger.error(e.getMessage(), e);
//            throw new Exception(e.getMessage(), e.getCause());
        }
    }
    /**
     * Send TrackingId To TIBCO EMS Queue.
     * @param trkId
     *            long
     * @param userId
     *            String
     * @param countWorkIds
     *            int
     * @author vn55306
     */
    public void sendTrkIdToTibcoEMSQueue(final long trkId, final int countWorkIds, final String userId) {
        logger.info("sendTrkIdToTibcoEMSQueuesendTrkIdToTibcoEMSQueue trkId="+trkId+" -- "+countWorkIds+ " userId -- "+userId);
        String dataMessage = "";
        String createTs = "";
        Timestamp time = new Timestamp(System.currentTimeMillis());
        DateFormat formatter = new SimpleDateFormat(DATE_YYYYMMDDHHMMSSSS, Locale.getDefault());
        createTs = formatter.format(time);
        dataMessage = formatIntToString(Integer.valueOf(String.valueOf(trkId))) + formatIntToString(countWorkIds) + STRING_SRC_SYSTEM + userId + SPACE + createTs;
        this.sendMesageToJMSQueue(dataMessage);
    }
    /**
     * Format Int To String of Int 9 bytes.
     * @param num
     *            int
     * @return String
     * @author vn55306
     */
    private String formatIntToString(final int num) {
        return String.format("%09d", num);
    }

}
