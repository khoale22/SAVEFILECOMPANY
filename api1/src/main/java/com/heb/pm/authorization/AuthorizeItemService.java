/*
 *  AuthorizeItemService
 *  Copyright (c) 2018 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.authorization;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.heb.pm.authorization.jms.AuthorizeItemTibcoJmsTopicSender;
import com.heb.pm.authorization.jms.BIIntegrationTibcoJmsTopicSender;
import com.heb.pm.authorization.util.AuthorizationConstants;
import com.heb.pm.authorization.util.AuthorizationUtils;
import com.heb.pm.authorization.util.AuthorizeItemCreatorUtils;
import com.heb.pm.authorization.xml.*;
import com.heb.pm.entity.*;
import com.heb.pm.repository.ApLocationRepository;
import com.heb.pm.repository.LocationGroupMemberRepository;
import com.heb.pm.repository.SellingUnitRepository;
import com.heb.pm.repository.TransactionRepository;
import com.heb.pm.user.UserService;
import com.heb.pm.ws.EmailServiceClient;
import com.heb.util.controller.UserInfo;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import java.io.IOException;
import java.io.StringWriter;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;

/**
 * This service will implement any function related to Authorize item.
 *
 * @author vn70529
 * @since 2.23.0
 */
@Service
public class AuthorizeItemService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthorizeItemService.class);
    @Autowired
    private SellingUnitRepository sellingUnitRepository;
    @Autowired
    private ApLocationRepository apLocationRepository;
    @Autowired
    private BIIntegrationTibcoJmsTopicSender bIIntegrationTibcoJmsTopicSender;
    @Autowired
    private AuthorizeItemTibcoJmsTopicSender authorizeItemTibcoJmsTopicSender;
    @Autowired
    private LocationGroupMemberRepository locationGroupMemberRepository;
    @Autowired
    private UserInfo userInfo;
    @Autowired
    private UserService userService;
    @Autowired
    private TransactionRepository transactionRepository;
    @Value("${authorizeItem.prod.desc.prefix}")
    private String productDescriptionPrefix;
    @Value("${authorizeItem.scale.desc.size1}")
    private String scaleDescriptionSize;
    @Value("${authorizeItem.batch.log.name}")
    private String batchLogName;
    @Value("${authorizeItem.log.path}")
    private String logPath;
    @Value("${authorizeItem.batch.reset.number}")
    private String batchResetNumber;
    @Value("${authorizeItem.batch.initial.number}")
    private String batchInitialNumber;
    private static final String SUBMIT_AUTHORIZE_LOG_ERROR ="An error occurs while submitting authorize item ";
    private static final String AUTHORIZE_ITEM_TRANSACTION_VERSION = "1.0";
    private static final String AUTHORIZE_ITEM_TRANSACTION_FUNCTION = "JOEV-AUTHORIZATION";
    private static final String SAVE_TRANSACTION_ERROR ="An error occurs while save transaction to DB ";
    private static final String XML_LOG_HEADER = "Authorize Item With Store: %s - %s";
    /**
     * This method submits the authorize item
     * @param authorizeItem the authorizeItem
     * @throws Exception throw error.
     */
    public void submitAuthorizeItem(AuthorizeItem authorizeItem) throws Exception {
        final String currentDateTime = AuthorizationUtils.getCurrentDateByFormat(AuthorizationConstants.DATE_FORMAT_MMDDYYYYHHMMSS_Z);
        try {
            this.submitAuthorizeItem(authorizeItem,  currentDateTime);
            /* PLU pre digit 2 Fix
             * UPC/PLU TYPE: PLU Then send pre digit 2.
             */
            this.submitAuthorizeItemWithPreFixTwoDigitsForPlu(authorizeItem, currentDateTime);
        } catch (Exception exp) {
            LOGGER.error(SUBMIT_AUTHORIZE_LOG_ERROR, exp);
            throw exp;
        }
        // Write transaction.
        this.saveTransaction(authorizeItem);
    }
    /**
     * Submit authorize item for upc is Plu.
     * @param authorizeItem authorizeItem object.
     * @param currentDateTime the current date time.
     * @throws Exception throw error.
     */
    private void submitAuthorizeItemWithPreFixTwoDigitsForPlu(AuthorizeItem authorizeItem, String currentDateTime) throws Exception {
        if (StringUtils.isNotBlank(authorizeItem.getScnTypCd()) && authorizeItem.getScnTypCd().trim().equalsIgnoreCase(AuthorizationConstants.ITEM_TYPE_PLU)) {
            String upc = authorizeItem.getUpc().trim();
            if ((upc.length() == 4 || upc.length() == 5))
                //Changes made to address issues with pre digit 2 UPC
            {
                String preDigit2PLU = AuthorizationUtils.formatPluByUpc(upc);
                if (StringUtils.isNotBlank(preDigit2PLU)) {
                    authorizeItem.setUpc(preDigit2PLU);
                    this.submitAuthorizeItem(authorizeItem, currentDateTime);
                }
            }
        }
    }

    /**
     * Submit authorize item to tibco.
     * @param authorizeItem authorizeItem object.
     * @param currentDateTime current date time.
     * @throws Exception throw error.
     */
    private void submitAuthorizeItem(AuthorizeItem authorizeItem, String currentDateTime) throws Exception {
        MSGContainer container = ObjectFactory.createMSGContainer();
        G3VNDRREC g3VendorRec = ObjectFactory.createG3VNDRREC();
        G3DEPTREC g3DeptRec = ObjectFactory.createG3DEPTREC();
        G3PRODREC g3ProductRec = ObjectFactory.createG3PRODREC();
        G3CONSMUNTREC g3ConsmUntRec = ObjectFactory.createG3CONSMUNTREC();
        G3PKREC g3PckRec = ObjectFactory.createG3PKREC();
        G3SPLRPACKREC g3SplrPackRec = ObjectFactory.createG3SPLRPACKREC();
        SCALEREC scaleRec = ObjectFactory.createSCALEREC();
        SCALEHDRREC scaleHdrRec = ObjectFactory.createSCALEHDRREC();
        SCALEDTLREC scaleDtlRec = ObjectFactory.createSCALEDTLREC();
        G3HDRREC g3HeaderRec = ObjectFactory.createG3HDRREC();
        G3HOSTLAYOUT g3HstLayout = ObjectFactory.createG3HOSTLAYOUT();
        G3HOSTLAYOUT.G3COMP g3Comp = ObjectFactory.createG3HOSTLAYOUTG3COMP();
        //set the data for <G3_HDR_REC> tag
        AuthorizeItemCreatorUtils.setG3HeaderRec(authorizeItem.getStore(), g3HeaderRec);
        // set the data for <G3_VNDR_REC> tag
        // Check if Vendor to Store exists then pass vendor details...
        setVendorDetails(authorizeItem, authorizeItem.getStore(), g3VendorRec);
       // set the data for <G3_DEPT_REC> tag
        AuthorizeItemCreatorUtils.setDeptDetails(authorizeItem, g3DeptRec);
        // Set the data for <G3_PROD_REC> tag
        AuthorizeItemCreatorUtils.setProductDetails(authorizeItem, g3ProductRec, sellingUnitRepository.findOne(Long.valueOf(authorizeItem.getUpc())).getProdId());
        //set the data for <G3_CONSM_UNT_REC> tag SALS_TAX_SW - >retailTaxSwitch, FD_STMP_SW-> foodStampSwitch
        setConsumerUnitDetails(authorizeItem, g3ConsmUntRec);
        //set the data for <G3_PK_REC> tag
        AuthorizeItemCreatorUtils.setPackageDetails(authorizeItem, g3PckRec);
        //set the values for <G3_SPLR_PACK_REC> tag
        AuthorizeItemCreatorUtils.setSupplierPackDetails(authorizeItem, g3SplrPackRec);
       // set the data for <SCALE_REC> tag
        setPluDetails(authorizeItem, scaleRec, scaleHdrRec, scaleDtlRec, authorizeItem.getStore());
       // Set data for <G3_COMP> tag
        g3Comp.setG3VNDRREC(g3VendorRec);
        g3Comp.setG3DEPTREC(g3DeptRec);
        g3Comp.setG3CONSMUNTREC(g3ConsmUntRec);
        g3Comp.setG3PRODREC(g3ProductRec);
        g3Comp.setG3PKREC(g3PckRec);
        g3Comp.setG3SPLRPACKREC(g3SplrPackRec);
        g3Comp.setSCALEREC(scaleRec);
        //place the <G3_HDR_REC> &  <G3_COMP> tags inside <G3_HOST_LAYOUT>
        g3HstLayout.setG3HDRREC(g3HeaderRec);
        g3HstLayout.getG3COMP().add(g3Comp);
        //set the values in <Header> tag
        Header header = createHeader(currentDateTime);
        container.setHeader(header);
        // Set data for <Body> tags inside the root tag
        Body body = ObjectFactory.createBody();
        //place the <G3_HOST_LAYOUT> inside the <Body> tag
        body.setG3HOSTLAYOUT(g3HstLayout);
        container.setBody(body);
        // Convert to xml string and send to tibco
        JAXBContext context = JAXBContext.newInstance(MSGContainer.class);
        Marshaller m = context.createMarshaller();
        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE); // To format XML
        StringWriter sw = new StringWriter();
        m.marshal(container, sw);
        final String xmlString = sw.toString();
        //submit the details to the JMS queue
        authorizeItemTibcoJmsTopicSender.send(xmlString, AuthorizationConstants.TYPE_AUTHORIZE, authorizeItem.getStore());
        //Submit Data to BI Integration
        bIIntegrationTibcoJmsTopicSender.send(xmlString, AuthorizationConstants.TYPE_ITEM_SETUP);
        // Write xml to log.
        this.writeXmlToLog(authorizeItem.getStore(), currentDateTime, xmlString);
    }

    /**
     * Create Header tag
     * @param currentDateTime current date time
     * @return Header object.
     */
    private Header createHeader(String currentDateTime){
        Header header = ObjectFactory.createHeader();
        header.setProjectName(AuthorizationConstants.PROJECT_NAME_AUTHORIZE_ITEM);
        header.setSourceDomain(AuthorizationConstants.APPLICATION);
        header.setDestinationDomain(StringUtils.EMPTY);
        header.setTransactionType(AuthorizationConstants.AUTHORIZATION_TRANSACTION_TYPE);
        header.setTimeStamp(currentDateTime);
        return header;
    }
    /**
     * Set the values for G3_VNDR_REC tag.
     * @param authorizeItem the object holds the info of item to authorize.
     * @param storeId the store id.
     * @param g3VendorRec the object holds the info of G3_VNDR_REC tag.
     */
    private void setVendorDetails(AuthorizeItem authorizeItem, String storeId, G3VNDRREC g3VendorRec) {
        //Check if Vendor to Store is not exists then pass vendor details...
        List<LocationGroupMember> locationGroupMembers = locationGroupMemberRepository.findByKeyCustLocationNumberAndKeySplrLocationNumber(Integer.valueOf(storeId), authorizeItem.getVendorId());
        if (locationGroupMembers == null || locationGroupMembers.size() == 0) {
            ApLocation apLocation = apLocationRepository.findFirstByKeyApVendorNumber(authorizeItem.getVendorId());
            AuthorizeItemCreatorUtils.setVendorDetails(authorizeItem, apLocation, g3VendorRec);
        }
    }
    /**
     *  Set the data for G3_CONSM_UNT_REC tag
     * @param authorizeItem AuthorizeItem object.
     * @param g3ConsUntRec G3CONSMUNTREC object.
     */
    private void setConsumerUnitDetails(AuthorizeItem authorizeItem, G3CONSMUNTREC g3ConsUntRec) {
        //set the values for <G3_CONSM_UNT_REC> tag
        SellingUnit sellingUnit = sellingUnitRepository.findOneByUpc(Long.valueOf(authorizeItem.getUpc()));
        //set the values for <G3_CONSM_UNT_REC> tag SALS_TAX_SW - >retailTaxSwitch, FD_STMP_SW-> foodStampSwitch
        AuthorizeItemCreatorUtils.setConsumerUnitDetails(authorizeItem, g3ConsUntRec, sellingUnit.getGoodsProduct().getRetailTaxSwitch(),
                sellingUnit.getGoodsProduct().getFoodStampSwitch());
    }
    /**
     * Sets Plu info to SCALEREC object.
     * @param authorizeItem AuthorizeItem object.
     * @param scaleRec SCALEREC object.
     * @param scaleHdrHdr SCALEHDRREC object.
     * @param scaleDtlRec SCALEDTLREC object.
     * @param storeId the id of store.
     * @throws Exception throw error.
     */
    private void setPluDetails(AuthorizeItem authorizeItem, SCALEREC scaleRec, SCALEHDRREC scaleHdrHdr, SCALEDTLREC scaleDtlRec,
                               String storeId) throws Exception {
        if(StringUtils.isNotBlank(authorizeItem.getScnTypCd()) &&
                (AuthorizationConstants.ITEM_TYPE_PLU).equalsIgnoreCase(authorizeItem.getScnTypCd().trim())) {
            BigInteger batchNumber = AuthorizationUtils.getBatchNumber(batchInitialNumber, batchResetNumber, logPath, batchLogName);
            //set the values for <SCALE_REC> tag
            AuthorizeItemCreatorUtils.setPluDetails(authorizeItem, scaleRec, scaleHdrHdr, scaleDtlRec, storeId, productDescriptionPrefix, scaleDescriptionSize, batchNumber);
        }
    }
    /**
     * Write XML to log file.
     * @param storeId the store id.
     * @param currentDateTime the current date time.
     * @param xml the xml to log.
     */
    private void writeXmlToLog(String storeId, String currentDateTime, String xml){
        LOGGER.info(String.format(XML_LOG_HEADER, storeId, currentDateTime));
        LOGGER.info(xml);
    }
    /**
     * Save transaction authorize item to database.
     *
     * @param authorizeItem authorizeItem object.
     */
    private void saveTransaction(AuthorizeItem authorizeItem) {
        String userId = userInfo.getUserId();
        String fullName = userId;
        User user = this.userService.getUserById(userId);
        if (user != null && StringUtils.isNotBlank(user.getFullName())) {
            fullName = user.getFullName();
        }
        ObjectMapper mapper = new ObjectMapper();
        AuthorizationMessage authorizationMessage = new AuthorizationMessage();
        authorizationMessage.setFunction(AUTHORIZE_ITEM_TRANSACTION_FUNCTION);
        authorizationMessage.setVersion(AUTHORIZE_ITEM_TRANSACTION_VERSION);
        authorizationMessage.setItem(Long.valueOf(authorizeItem.getItemId()));
        authorizationMessage.setItemDescription(authorizeItem.getProductDescription());
        authorizationMessage.setStoreNumber(authorizeItem.getStore());
        authorizationMessage.setUnitCost(authorizeItem.getUnitCost());
        authorizationMessage.setUnitRetail(AuthorizationUtils.calculateUnitRetail(authorizeItem.getRetailFor(), Double.valueOf(authorizeItem.getRetail())));
        authorizationMessage.setUserId(userId);
        authorizationMessage.setUserName(fullName);
        try {
            String sourceInfoTxt = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(authorizationMessage);
            Transaction transaction = new Transaction();
            transaction.setSourceSystem(SourceSystem.SOURCE_SYSTEM_PRODUCT_MAINTENANCE);
            transaction.setCreateUserId(userId);
            transaction.setCreateTime(LocalDateTime.now());
            transaction.setSourceInfoTxt(sourceInfoTxt);
            transactionRepository.save(transaction);
        } catch (JsonProcessingException e) {
            LOGGER.error(SAVE_TRANSACTION_ERROR, e);
        }
    }
}
