/*
 *  TexasStateReader
 *  Copyright (c) 2018 H-E-B
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of H-E-B.
 */
package com.heb.batch.wic.reader;

import com.heb.batch.wic.dao.JobParamDAO;
import com.heb.batch.wic.exception.WicException;
import com.heb.batch.wic.index.TexasStateDocument;
import com.heb.batch.wic.index.repository.TexasStateIndexRepository;
import com.heb.batch.wic.service.EmailService;
import com.heb.batch.wic.service.TexasFieldValidator;
import com.heb.batch.wic.service.TexasStateData;
import com.heb.batch.wic.utils.WicConstants;
import com.heb.batch.wic.utils.WicUtil;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Locale;

/**
 * This is TexasStateReader class.
 *
 * @author vn55306
 * @since 1.0.0
 */
public class TexasStateReader extends FlatFileItemReader<TexasStateDocument> implements StepExecutionListener {
    private static final Logger LOGGER = LogManager.getLogger(TexasStateReader.class);
    @Value("${texasState.dataparse}")
    String fileDataParse;
    @Autowired
    private TexasStateIndexRepository texasStateIndexRepository;
    @Autowired
    private EmailService emailService;
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private TexasStateData texasStateData;
    @Autowired
    private JobParamDAO jobParamDAO;
    @Autowired
    private TexasFieldValidator texasFieldValidator;
    private Resource resource;
    @Override
    public void beforeStep(StepExecution stepExecution) {
        LOGGER.info("Start J50X100D-PARSE-TEXASTATE-D6-FILE-STEP-1/J50X100D-PARSE-TEXASTATE-D4-FILE-STEP-5");
        // validate File exist, format correct
        // send Email to production support team if invalid rule
        // clear indexing old data if valid file
        try (FileWriter pw = new FileWriter(fileDataParse,false)){
            pw.flush();
        } catch (IOException e){
            LOGGER.error(e.getMessage());
        }
        this.texasStateIndexRepository.refresh();
        this.texasStateIndexRepository.deleteAll();
        this.texasStateIndexRepository.count();
        this.texasStateData.getProductScanCodeWicDocuments().clear();
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        return null;
    }
   @SuppressWarnings("unchecked")
   @Override
   protected void doOpen() throws Exception {
       String fileName= this.jobParamDAO.getConfigurationInfor(JobParamDAO.J50X100_INPUT_NAS);
       LOGGER.debug("open file and init config from db fileName="+fileName);
       resource = new FileSystemResource(fileName);
      if(resource.exists() && resource.contentLength()>0 && WicConstants.TEXAS_STATE_NAME.equals(resource.getFile().getName())) {
           this.setResource(resource);
           super.doOpen();
       } else {
          LOGGER.debug("Input resource does not exist, sent Email to Production support team");
          String content;
          String subject;
          String currentDay = WicUtil.getCurrentDay(WicUtil.FORMAT_DATE_SEND_EMAIL);
          String fromAddress = this.jobParamDAO.getConfigurationInfor(JobParamDAO.J50X100_EMAIL_FROM);
          String toAddress = this.jobParamDAO.getConfigurationInfor(JobParamDAO.J50X100_EMAIL_ADDRESS_PRODUCTION_SUPPORT);
          subject = messageSource.getMessage("wic.email.production.subject", null, Locale.US);
           if(!resource.exists()) {
               content = messageSource.getMessage("wic.email.production.file.not.found", new Object[] { currentDay}, Locale.US);
           } else {
               content = messageSource.getMessage("wic.email.production.file.empty",  new Object[] { currentDay}, Locale.US);
               if(resource.isFile()) {
                   FileUtils.forceDeleteOnExit(resource.getFile());
               }
           }
           this.emailService.sendProductionSupportCorruptFile(subject,content,fromAddress,toAddress);
           throw new WicException(content);
       }
   }
   @Override
   protected TexasStateDocument doRead() throws Exception {
       TexasStateDocument texasStateDocument =  super.doRead();
       if(texasStateDocument!=null && !texasFieldValidator.validateFormatFile(texasStateDocument)){
           String content;
           String subject;
           String currentDay = WicUtil.getCurrentDay(WicUtil.FORMAT_DATE_SEND_EMAIL);
           subject = messageSource.getMessage("wic.email.production.subject", null, Locale.US);
           content = messageSource.getMessage("wic.email.production.file.corrupt", new Object[] { currentDay}, Locale.US);
           String fromAddress = this.jobParamDAO.getConfigurationInfor(JobParamDAO.J50X100_EMAIL_FROM);
           String toAddress = this.jobParamDAO.getConfigurationInfor(JobParamDAO.J50X100_EMAIL_ADDRESS_PRODUCTION_SUPPORT);
           this.emailService.sendProductionSupportCorruptFile(subject,content,fromAddress,toAddress);
           if(resource.exists() && resource.isFile()) {
               FileUtils.forceDeleteOnExit(resource.getFile());
           }
           throw new WicException(content);
       }
       return texasStateDocument;
   }
}
