package com.heb.pm.ws;


import com.example.xmlns._1371689437200.ContentManagementServicePortType;
import com.example.xmlns._1371689437200.ContentManagementServiceServiceagent;
import com.example.xmlns._1371689437200.Fault;
import com.example.xmlns._1371689437200.ProviderFaultSchema;
import com.heb.pm.entity.CopyToHierarchyRequest;
import com.heb.pm.entity.GenericEntity;
import com.heb.pm.entity.ImageMetaData;
import com.heb.pm.entity.ImageToUpload;
import com.heb.pm.entity.ProductScanImageURI;
import com.heb.pm.entity.SalesChannel;
import com.heb.util.ws.BaseWebServiceClient;
import com.heb.util.ws.SoapException;
import com.heb.xmlns.ei.contentmanagement.update_contentmangement_request.UpdateContentMangementRequest;
import com.heb.xmlns.ei.contentmangement.update_contentmanagement_reply.UpdateContentManagementReply;
import com.heb.xmlns.ei.contentmangementupdate.ContentMangementUpdate;
import com.heb.xmlns.ei.imagemetadata.ImageMetadata;
import com.heb.xmlns.ei.retrieveimage_reply.RetrieveImageReply;
import com.heb.xmlns.ei.retrieveimage_request.RetrieveImageRequest;
import com.heb.xmlns.ei.uploadimage_reply.UploadImageReply;
import com.heb.xmlns.ei.uploadimagesforonlinemode.UploadImagesForOnlineMode;
import com.heb.xmlns.ei.uploadimagesforonlinemode_reply.UploadImagesForOnlineModeReply;
import com.heb.xmlns.ei.uploadimagesforonlinemode_request.UploadImagesForOnlineModeRequest;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * This client is used to get the images based on an URI
 * @author s753601
 * @version 2.13.0
 */
@Service
public class ContentManagementServiceClient extends BaseWebServiceClient
		<ContentManagementServiceServiceagent, ContentManagementServicePortType>{

	private static final Logger logger = LoggerFactory.getLogger(ContentManagementServiceClient.class);

	private static final String UPC_IMAGE_UPLOAD="UPC";
	private static final String TRACKING_NUMBER_DEFAULT = "0";
	private static final String IMAGE_TEXT_DEFAULT = " ";

	private static final String DEFAULT_ACTIVE_ONLINE_SWITCH="N";
	private static final String IMAGE_PRIORITY_PRIMARY_CODE="P";
	private static final String IMAGE_PRIORITY_ALTERNATE_CODE = "A";
	private static final String CLIENT_TRUE_FLAG="Y";
	private static final String DEFAULT_TRUE_STRING="Y";
	private static final String DEFAULT_FALSE_STRING="N";
	private static final String DEFAULT_CUSTOMER_HIERARCHY="CUSTH";
	public static final String ERR_MSG_IMG_UPLD_FAILURE = "Image Upload Failed.";
	public static final String IMG_ALTERNATE = "A";
	private static final String WEBSERVICE_RESPONSE_MESSAGE_SUCCESS = "Success";
	@Value("${contentManagementService.uri}")
	private String uri;

	@Value("${app.sourceSystemId}")
	private int defaultSourceSystemCode;

	/**
	 * Retrieves the byte[] representation of the image
	 * @param id the URI of the image and used by the webservice to find a specific id
	 * @return byte[] of image
	 */
	public byte[] getImage(String id) {
		if(!StringUtils.isBlank(id)){
			RetrieveImageRequest request = new RetrieveImageRequest();
			request.setAuthentication(this.getAuthentication());
			request.getClipId().add(id.trim());
			try{
				RetrieveImageReply reply = getRetrieveImageReply(request);
				List<RetrieveImageReply.ImageData> imageData =reply.getImageData();
				if(imageData!=null && !imageData.isEmpty()) {
					return imageData.get(0).getImageData();
				}
			}catch (Exception e){
				ContentManagementServiceClient.logger.error(e.getMessage());
				return new byte[0];
			}
		}
		return new byte[0];
	}

	/**
	 * Creates a request to upload a new image.  The image is first scanned to insure it doesn't contain a virus then
	 * a request to upload the image is created
	 * @param imageToUpload the image to upload object that contains all information to complete the request
	 */
	public void uploadImageFromSellingUnitDetails(ImageToUpload imageToUpload) throws CheckedSoapException{
			UploadImagesForOnlineMode webserviceImageUpload = new UploadImagesForOnlineMode();
			webserviceImageUpload.setKeyId(imageToUpload.getUpc().toString());
			webserviceImageUpload.setKeyType(UPC_IMAGE_UPLOAD);
			webserviceImageUpload.setImgSubTyp(UPC_IMAGE_UPLOAD);
			webserviceImageUpload.setImageName(imageToUpload.getImageName());
			webserviceImageUpload.setUserId(imageToUpload.getUserId());
			webserviceImageUpload.setIMGSRCNM(imageToUpload.getImageSourceCode());
			webserviceImageUpload.setImageCategory(imageToUpload.getImageCategoryCode());
			webserviceImageUpload.setBinaryImage(imageToUpload.getImageData());
			if(imageToUpload.getDestinationList() !=null){
				for (SalesChannel salesChannel : imageToUpload.getDestinationList()) {
					webserviceImageUpload.getSALSCHNLCD().add(salesChannel.getId());
				}
			}
			webserviceImageUpload.setSourceSystem(Integer.toString(defaultSourceSystemCode));

		if(imageToUpload.isPrimary()){
			webserviceImageUpload.setIMGPRTYCD(IMAGE_PRIORITY_PRIMARY_CODE);
			webserviceImageUpload.setACTVONLINSW(DEFAULT_TRUE_STRING);
			webserviceImageUpload.setIMGSTATCD(DEFAULT_TRUE_STRING);
			webserviceImageUpload.setEXISTINGPRIMARYACTION(imageToUpload.getExistingImage());
		} else {
			if(imageToUpload.isShowOnSite()){
				webserviceImageUpload.setIMGPRTYCD(IMAGE_PRIORITY_ALTERNATE_CODE);
				webserviceImageUpload.setACTVONLINSW(DEFAULT_ACTIVE_ONLINE_SWITCH);
				webserviceImageUpload.setIMGSTATCD(DEFAULT_FALSE_STRING);
				webserviceImageUpload.setEXISTINGALTERNATEACTION(imageToUpload.getExistingImage());
			}else{
				webserviceImageUpload.setIMGPRTYCD(IMAGE_PRIORITY_ALTERNATE_CODE);
				webserviceImageUpload.setACTVONLINSW(DEFAULT_ACTIVE_ONLINE_SWITCH);
				webserviceImageUpload.setIMGSTATCD(DEFAULT_FALSE_STRING);
			}
		}

			UploadImagesForOnlineModeRequest request = new UploadImagesForOnlineModeRequest();
			request.getUploadImagesForOnlineMode().add(webserviceImageUpload);
			request.setAuthentication(this.getAuthentication());

			this.uploadImage(request);
	}

	/**
	 * Creates a request to upload a new image.  The image is first scanned to insure it doesn't contain a virus then
	 * a request to upload the image is created
	 * @param imageToUpload the image to upload object that contains all information to complete the request
	 */
	public void uploadImageFromCustomHierarchy(ImageToUpload imageToUpload) throws CheckedSoapException{
		UploadImagesForOnlineMode webserviceImageUpload = new UploadImagesForOnlineMode();
		webserviceImageUpload.setKeyId(imageToUpload.getEntityId().toString());
		webserviceImageUpload.setKeyType(DEFAULT_CUSTOMER_HIERARCHY);
		webserviceImageUpload.setImgSubTyp(DEFAULT_CUSTOMER_HIERARCHY);
		webserviceImageUpload.setImageName(imageToUpload.getImageName());
		webserviceImageUpload.setUserId(imageToUpload.getUserId());
		webserviceImageUpload.setIMGSRCNM(imageToUpload.getImageSourceCode());
		webserviceImageUpload.setImageCategory(imageToUpload.getImageCategoryCode());
		webserviceImageUpload.setBinaryImage(imageToUpload.getImageData());
		webserviceImageUpload.setSourceSystem(Integer.toString(defaultSourceSystemCode));
		webserviceImageUpload.setACTVONLINSW(DEFAULT_ACTIVE_ONLINE_SWITCH);
		UploadImagesForOnlineModeRequest request = new UploadImagesForOnlineModeRequest();
		request.getUploadImagesForOnlineMode().add(webserviceImageUpload);
		request.setAuthentication(this.getAuthentication());

		this.uploadImage(request);
	}

	/**
	 * Upload image using a URL.
	 * @param imageToUpload ImageToUpload
	 * @return String if error message received
	 */
	public String uploadImageFromURL(ImageToUpload imageToUpload) throws CheckedSoapException{
		String result;
		UploadImagesForOnlineModeRequest req = new UploadImagesForOnlineModeRequest();
		req.setAuthentication(this.getAuthentication());
		try {
			UploadImagesForOnlineMode uploadList = new UploadImagesForOnlineMode();
			uploadList.setKeyId(String.valueOf(imageToUpload.getUpc()));
			uploadList.setKeyType(imageToUpload.getImageSourceCode());
			uploadList.setImageName(imageToUpload.getImageName());
			uploadList.setImageCategory(imageToUpload.getImageCategoryCode());
			uploadList.setURL(imageToUpload.getImageURL());
			uploadList.setUserId(imageToUpload.getUserId());
			uploadList.setIMGSRCNM(imageToUpload.getImageSource());
			uploadList.setSourceSystem(Integer.toString(defaultSourceSystemCode));
			req.getUploadImagesForOnlineMode().add(uploadList);
			UploadImageReply reply = this.getPort().uploadImageFromURL(req);
			if (reply != null && !StringUtils.isBlank(reply.getMessage()) && reply.getMessage().length() > 0) {
				result = reply.getMessage();
			} else {
				throw new CheckedSoapException(ERR_MSG_IMG_UPLD_FAILURE);
			}
		}
		catch (Exception e) {
			//	throw new SoapException(String.format(ERROR_WEB_SERVICE_UPDATE_RESPONSE, e.getMessage()));
			logger.error(e.getMessage());
			throw new CheckedSoapException(ERR_MSG_IMG_UPLD_FAILURE);
		}
		return result;
	}
	/**
	 * Takes a uploadImageForOnlineMOdeRequest and gets the reply from the webservice
	 * @param request the image request to be uploaded.
	 */
	private void uploadImage(UploadImagesForOnlineModeRequest request) throws CheckedSoapException{
		try{
			UploadImagesForOnlineModeReply reply = this.getPort().uploadImagesForOnlineMode(request);
			StringBuilder errorMessage = new StringBuilder("");
			for (UploadImagesForOnlineModeReply.ImagesForOnlineModeUpload uploadRequest: reply.getImagesForOnlineModeUpload()) {
				if(uploadRequest.getError() != null && uploadRequest.getError().equals(CLIENT_TRUE_FLAG)){
					errorMessage.append(ERR_MSG_IMG_UPLD_FAILURE);
				}
			}
			if(!errorMessage.toString().equals(StringUtils.EMPTY)){
				throw new CheckedSoapException(errorMessage.toString());
			}
		} catch (Fault e) {
			throw new CheckedSoapException(e.getMessage());
		}
	}

	/**
	 * Reply from asking for image request
	 * @param request the request for image data
	 * @return
	 * @throws CheckedSoapException
	 */
	private RetrieveImageReply getRetrieveImageReply(RetrieveImageRequest request) throws CheckedSoapException{
		RetrieveImageReply reply;
		try{
			reply = this.getPort().retrieveImage(request);
		} catch (Fault fault) {
			ContentManagementServiceClient.logger.error(fault.getMessage());
			throw new CheckedSoapException(fault.getMessage());
		}
		return reply;
	}

	/**
	 *  This method sets up the request to create a link between the image info and the customer hierarchy
	 * @param copyToHierarchyRequest holds most data to complete the request
	 * @param id the id of the user making the request
	 * @throws Exception
	 */
	public void copyToHierarchy(CopyToHierarchyRequest copyToHierarchyRequest, String id) throws CheckedSoapException {
		String workId = this.getWorkId().toString();
		UpdateContentMangementRequest updateContentMangementRequest=new UpdateContentMangementRequest();
		for (ProductScanImageURI imageInfo: copyToHierarchyRequest.getImageInfo()) {
			for (GenericEntity entity: copyToHierarchyRequest.getGenericEntities()) {
				ImageMetadata webserviceImageMetadata= new ImageMetadata();
				webserviceImageMetadata.setACTVONLINSW(convertBooleanToString(imageInfo.isActiveOnline()));
				webserviceImageMetadata.setKEYID(entity.getId().toString());
				webserviceImageMetadata.setSEQNBR(Long.toString(imageInfo.getKey().getSequenceNumber()));
				webserviceImageMetadata.setIMGTYPCD(imageInfo.getImageTypeCode());
				webserviceImageMetadata.setACTVSW(convertBooleanToString(imageInfo.getActiveSwitch()));
				webserviceImageMetadata.setIMGSRCNM(imageInfo.getImageSource().getId());
				webserviceImageMetadata.setIMGCRETNTS(imageInfo.getCreatedDate().toString());
				webserviceImageMetadata.setIMGACPTABLESW(imageInfo.getImageAccepted());
				webserviceImageMetadata.setREVDBYUID(imageInfo.getRevisedByID());
				webserviceImageMetadata.setREVDTS(imageInfo.getRevisedTimeStamp().toString());
				webserviceImageMetadata.setSRCSYSTEMID(Integer.toString(imageInfo.getSourceSystemId()));
				webserviceImageMetadata.setLSTUPDTTS(LocalDateTime.now().toString());
				webserviceImageMetadata.setLSTUPDTUID(id);
				webserviceImageMetadata.setIMGCATCD(imageInfo.getImageCategoryCode());
				webserviceImageMetadata.setIMGPRTYCD(imageInfo.getImagePriorityCode());
				webserviceImageMetadata.setRESLXNBR(imageInfo.getxAxisResolution().toString());
				webserviceImageMetadata.setRESLYNBR(imageInfo.getyAxisResolution().toString());
				webserviceImageMetadata.setIMGALTTXT(imageInfo.getAltTag());
				webserviceImageMetadata.setIMGSTATRSNTXT(imageInfo.getImageStatusReason());
				webserviceImageMetadata.setIMGSUBJTYPCD(DEFAULT_CUSTOMER_HIERARCHY);
				webserviceImageMetadata.setURITXT(imageInfo.getImageURI());

				webserviceImageMetadata.setWorkId(workId);
				webserviceImageMetadata.setActionCd("");

				updateContentMangementRequest.getImageMetadata().add(webserviceImageMetadata);
			}
		}
		updateContentMangementRequest.setTrackingNbr(workId.trim());
		updateContentMangementRequest.setAuthentication(this.getAuthentication());
		this.copyToHierarchyReply(updateContentMangementRequest);
	}

	/**
	 *  This method sets up the request to create a link between the image info and the customer hierarchy
	 * @param id the id of the user making the request
	 * @throws Exception
	 */
	public void editHierarchyImage(List<ImageMetaData> updates, String id) throws CheckedSoapException {
		String workId = this.getWorkId().toString();
		UpdateContentMangementRequest updateContentMangementRequest=new UpdateContentMangementRequest();
		for (ImageMetaData metaData: updates) {
			ImageMetadata webserviceImageMetadata= new ImageMetadata();
			webserviceImageMetadata.setACTVONLINSW(convertBooleanToString(metaData.isActiveOnline()));
			webserviceImageMetadata.setKEYID(String.valueOf(metaData.getKey().getId()));
			webserviceImageMetadata.setSEQNBR(Long.toString(metaData.getKey().getSequenceNumber()));
			webserviceImageMetadata.setIMGTYPCD(metaData.getImageTypeCode());
			webserviceImageMetadata.setACTVSW(convertBooleanToString(metaData.isActive()));
			webserviceImageMetadata.setIMGSRCNM(metaData.getImageSourceName());
			if(metaData.getCreateDate() != null){
				webserviceImageMetadata.setIMGCRETNTS(metaData.getCreateDate().toString());
			}
			webserviceImageMetadata.setIMGACPTABLESW(metaData.getImageAcceptableToSaving());
			webserviceImageMetadata.setREVDBYUID(metaData.getLastUpdateUserId());
			if(metaData.getLastUpdateDate() != null){
				webserviceImageMetadata.setREVDTS(metaData.getLastUpdateDate().toString());
			}
			if(metaData.getSourceSystemId() != null){
				webserviceImageMetadata.setSRCSYSTEMID(Long.toString(metaData.getSourceSystemId()));
			}
			webserviceImageMetadata.setLSTUPDTTS(LocalDateTime.now().toString());
			webserviceImageMetadata.setLSTUPDTUID(id);
			webserviceImageMetadata.setIMGCATCD(metaData.getImageCategoryCode());
			webserviceImageMetadata.setIMGPRTYCD(metaData.getImagePriorityCode());
			webserviceImageMetadata.setRESLXNBR(metaData.getImageSizeX());
			webserviceImageMetadata.setRESLYNBR(metaData.getImageSizeY());
			webserviceImageMetadata.setIMGALTTXT(metaData.getImageAltText());
			webserviceImageMetadata.setIMGSUBJTYPCD(DEFAULT_CUSTOMER_HIERARCHY);
			webserviceImageMetadata.setURITXT(metaData.getUriText());

			webserviceImageMetadata.setWorkId(workId);
			webserviceImageMetadata.setActionCd("");

			updateContentMangementRequest.getImageMetadata().add(webserviceImageMetadata);
		}
		updateContentMangementRequest.setTrackingNbr(workId.trim());
		updateContentMangementRequest.setAuthentication(this.getAuthentication());
		this.copyToHierarchyReply(updateContentMangementRequest);
	}


	/**
	 * This Method sends the request to the webservice reports any problems with the reply
	 * @param request
	 * @throws Exception
	 */
	private void copyToHierarchyReply(UpdateContentMangementRequest request) throws CheckedSoapException{
		try{
			this.getPort().updateContentMangementDetails(request);
		} catch (Exception fault) {
			ContentManagementServiceClient.logger.error(fault.getMessage());
			throw new CheckedSoapException(fault.getMessage());
		}
	}

	/**
	 * Return the service agent for this client.
	 * @return ContentManagementServiceServiceagent associated with this client.
	 */
	@Override
	protected ContentManagementServiceServiceagent getServiceAgent() {
		try {
			URL url = new URL(this.getWebServiceUri());
			return new ContentManagementServiceServiceagent(url);
		} catch (MalformedURLException e) {
			ContentManagementServiceClient.logger.error(e.getMessage());
			ContentManagementServiceClient.logger.error(e.getLocalizedMessage());
		}
		this.setLogInboundMessages(false);
		this.setLogOutboundMessages(false);
		return new ContentManagementServiceServiceagent();
	}

	/**
	 * Return the port type for this client.
	 * @param agent The agent to use to create the port.
	 * @return ContentManagementServicePortType associated with this client.
	 */
	@Override
	protected ContentManagementServicePortType getServicePort(ContentManagementServiceServiceagent agent) {
		return agent.getContentManagementService();
	}

	/**
	 * Return the url for this client.
	 * @return String url for this client.
	 */
	@Override
	protected String getWebServiceUri() {
		return this.uri;
	}

	/**
	 * Used for testing
	 * @param uri
	 */
	public void setUri(String uri){
		this.uri = uri;
	}

	/**
	 * Upload image for choice option.
	 * @param imageToUpload ImageToUpload
	 * @return String if error message received
	 */
	public void uploadImage(ImageToUpload imageToUpload) throws CheckedSoapException {
		UploadImagesForOnlineModeRequest request = new UploadImagesForOnlineModeRequest();
		request.setAuthentication(this.getAuthentication());
		try {
			UploadImagesForOnlineMode webserviceImageUpload = new UploadImagesForOnlineMode();
			webserviceImageUpload.setKeyId(imageToUpload.getKeyId());
			webserviceImageUpload.setKeyType(imageToUpload.getKeyType());
			webserviceImageUpload.setImageName(imageToUpload.getImageName());
			webserviceImageUpload.setImageCategory(imageToUpload.getImageCategoryCode());
			webserviceImageUpload.setBinaryImage(imageToUpload.getImageData());
			webserviceImageUpload.setUserId(imageToUpload.getUserId());
			webserviceImageUpload.setIMGSRCNM(imageToUpload.getImageSourceCode());
			webserviceImageUpload.setSourceSystem(Integer.toString(defaultSourceSystemCode));
			webserviceImageUpload.setIMGPRTYCD(IMG_ALTERNATE);
			webserviceImageUpload.setACTVONLINSW(DEFAULT_ACTIVE_ONLINE_SWITCH);
			webserviceImageUpload.setIMGSTATCD(DEFAULT_FALSE_STRING);

			request.getUploadImagesForOnlineMode().add(webserviceImageUpload);
			UploadImagesForOnlineModeReply reply = this.getPort().uploadImagesForOnlineMode(request);
			StringBuilder errorMessage = new StringBuilder("");
			for (UploadImagesForOnlineModeReply.ImagesForOnlineModeUpload uploadRequest: reply.getImagesForOnlineModeUpload()) {
				if(uploadRequest.getError() != null && uploadRequest.getError().equals(CLIENT_TRUE_FLAG)){
					errorMessage.append(uploadRequest.getErrorMessage());
				}
			}
			if(!errorMessage.toString().equals(StringUtils.EMPTY)){
				throw new CheckedSoapException(errorMessage.toString());
			}
		}catch (Exception e) {
			//	throw new SoapException(String.format(ERROR_WEB_SERVICE_UPDATE_RESPONSE, e.getMessage()));
			logger.error(e.getMessage());
			throw new CheckedSoapException(ERR_MSG_IMG_UPLD_FAILURE);
		}
	}
	/**
	 * update image metadata. send to webservice list of object and action code.
	 * then will get message from webservice to check status request
	 *
	 * @param listImageMetadata the list of image metadata use to update.
	 * @param userId the user id do this event.
	 */
	public void updateContentMangementDetails(List<ImageMetaData> listImageMetadata, String userId) {
		ContentManagementServiceServiceagent agent = new ContentManagementServiceServiceagent();
		ContentManagementServicePortType portType = this.getServicePort(agent);
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
			imageMeta.setIMGSTATCD(imageMetaData.getImageStatusCode());
			if (imageMetaData.getImageAltText() != null) {
				if (StringUtils.isEmpty(imageMetaData.getImageAltText())) {
					imageMeta.setIMGALTTXT(IMAGE_TEXT_DEFAULT);
				} else {
					imageMeta.setIMGALTTXT(imageMetaData.getImageAltText());
				}
			}
			list.add(imageMeta);
		}
		request.getImageMetadata().addAll(list);
		request.setTrackingNbr(TRACKING_NUMBER_DEFAULT);
		request.setAuthentication(this.getAuthentication());
		try {
			UpdateContentManagementReply reply;
			reply = portType.updateContentMangementDetails(request);
			StringBuilder errorMessage = new StringBuilder(StringUtils.EMPTY);
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
}
