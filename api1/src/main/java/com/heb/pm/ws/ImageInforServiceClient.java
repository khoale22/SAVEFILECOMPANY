/*
*  ImageInforServiceClient
*
*  Copyright (c) 2017 HEB
*  All rights reserved.
*
*  This software is the confidential and proprietary information
*  of HEB.
*/
package com.heb.pm.ws;

import com.example.xmlns._1371689437200.ContentManagementServicePortType;
import com.example.xmlns._1371689437200.ContentManagementServiceServiceagent;
import com.example.xmlns._1371689437200.ProviderFaultSchema;
import com.heb.pm.entity.ImageMetaData;
import com.heb.util.ws.SoapException;
import com.heb.xmlns.ei.contentmangement.update_contentmanagement_reply.UpdateContentManagementReply;
import com.heb.xmlns.ei.contentmangementupdate.ContentMangementUpdate;
import com.heb.xmlns.ei.imagemetadata.ImageMetadata;
import com.heb.util.ws.BaseWebServiceClient;
import com.heb.xmlns.ei.contentmanagement.update_contentmangement_request.UpdateContentMangementRequest;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Provides access to service endpoint for image information service.
 *
 * @author vn86116
 * @since 2.12.0
 */
@Service
public class ImageInforServiceClient extends BaseWebServiceClient<ContentManagementServiceServiceagent, ContentManagementServicePortType> {
    private static final Logger logger = LoggerFactory.getLogger(ImageInforServiceClient.class);

    @Value("${contentManagementService.uri}")
    private String uri;

    @Value("${app.sourceSystemId}")
    private int DefaultSourceSystemCode;

    //Constant
    private static final String STRING_0 = "0";
    private static final String SPACE = " ";
    private static final String WEBSERVICE_RESPONSE_MESSAGE_SUCCESS = "Success";
    private static final String REJECT_STATUS_CODE = "R";
    private static final String APPROVED_STATUS_CODE = "A";
    private static final String STRING_Y = "Y";
    /**
     * Return the service agent for this client.
     *
     * @return ContentManagementServiceServiceagent associated with this client.
     */
    @Override
    protected ContentManagementServiceServiceagent getServiceAgent() {
        try {
            URL url = new URL(this.getWebServiceUri());
            return new ContentManagementServiceServiceagent(url);
        } catch (MalformedURLException e) {
            ImageInforServiceClient.logger.error(e.getMessage());
        }
        return new ContentManagementServiceServiceagent();
    }

    /**
     * Return the port type for this client.
     *
     * @param agent The agent to use to create the port.
     * @return ContentManagementServicePortType associated with this client.
     */
    @Override
    protected ContentManagementServicePortType getServicePort(ContentManagementServiceServiceagent agent) {
        return agent.getContentManagementService();
    }

    /**
     * update image metadata. send to webservice list of object and action code.
     * then will get message from webservice to check status request
     *
     * @param listImageMetadata the list of image metadata use to update.
     * @param userId the user id do this event.
     */
    public void updateContentMangementDetails(List<ImageMetaData> listImageMetadata, String userId) {
        UpdateContentMangementRequest request = new UpdateContentMangementRequest();
        List<ImageMetadata> list = new ArrayList<>();
        for (ImageMetaData imageMetaData : listImageMetadata) {
            ImageMetadata imageMeta = new ImageMetadata();
            imageMeta.setSEQNBR(String.valueOf(imageMetaData.getKey().getSequenceNumber()));
            imageMeta.setKEYID(Long.toString(imageMetaData.getKey().getId()));
            imageMeta.setIMGSUBJTYPCD(imageMetaData.getKey().getImageSubjectTypeCode());
            imageMeta.setACTVONLINSW(this.convertBooleanToString(imageMetaData.isActiveOnline()));
            imageMeta.setIMGCATCD(imageMetaData.getImageCategoryCode());
            imageMeta.setIMGPRTYCD(imageMetaData.getImagePriorityCode());
            imageMeta.setLSTUPDTUID(userId);
            if(REJECT_STATUS_CODE.equals(imageMetaData.getImageStatusCode())){
                imageMeta.setIMGACPTABLESW(imageMetaData.getImageStatusCode());
                imageMeta.setIMGSTATCD(imageMetaData.getImageStatusCode());
            } else if (APPROVED_STATUS_CODE.equals(imageMetaData.getImageStatusCode())){
                imageMeta.setIMGACPTABLESW(STRING_Y);
            }
            if (imageMetaData.getImageAltText() != null) {
                if (StringUtils.isEmpty(imageMetaData.getImageAltText())) {
                    imageMeta.setIMGALTTXT(SPACE);
                } else {
                    imageMeta.setIMGALTTXT(imageMetaData.getImageAltText());
                }
            }
            list.add(imageMeta);
        }
        request.getImageMetadata().addAll(list);
        request.setTrackingNbr(STRING_0);
        request.setAuthentication(this.getAuthentication());
        try {
            UpdateContentManagementReply reply;
            reply = this.getPort().updateContentMangementDetails(request);
            StringBuilder errorMessage = new StringBuilder("");
            if (!reply.getContentMangementUpdate().isEmpty()) {
                for (ContentMangementUpdate updateContentManagementReply : reply.getContentMangementUpdate()) {
                    if (!this.isSuccess(updateContentManagementReply.getReturnMsg())) {
                        errorMessage.append(updateContentManagementReply.getReturnMsg());
                    }
                }
                if (!errorMessage.toString().equals(StringUtils.EMPTY)) {
                    throw new SoapException(errorMessage.toString());
                }
            }
        } catch (ProviderFaultSchema providerFaultSchema) {
            providerFaultSchema.printStackTrace();
        }
    }

    /**
     * Looking message return from webservice. If contain Success text => return handle successfully.
     *
     * @param returnMessage the return message needs to check.
     * @return true if it is success or not.
     */
    private boolean isSuccess(String returnMessage) {
        return (returnMessage != null && returnMessage.indexOf(WEBSERVICE_RESPONSE_MESSAGE_SUCCESS) >= 0);
    }

    /**
     * Return the url for this client.
     *
     * @return String url for this client.
     */
    @Override
    protected String getWebServiceUri() {
        return this.uri;
    }
}
