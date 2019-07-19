/*
 * com.heb.pm.entity.ProductDiscontinue
 *
 * Copyright (c) 2017 HEB
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of HEB.
 */
package com.heb.pm.productDetails.sellingUnit;

import com.heb.jaf.security.AuthorizedResource;
import com.heb.jaf.security.EditPermission;
import com.heb.jaf.security.ViewPermission;
import com.heb.pm.ApiConstants;
import com.heb.pm.ResourceConstants;
import com.heb.pm.entity.CopyToHierarchyRequest;
import com.heb.pm.entity.DestinationChanges;
import com.heb.pm.entity.GenericEntity;
import com.heb.pm.entity.ImageCategory;
import com.heb.pm.entity.ImageMetaData;
import com.heb.pm.entity.ImagePriority;
import com.heb.pm.entity.ImageSource;
import com.heb.pm.entity.ImageStatus;
import com.heb.pm.entity.ImageToUpload;
import com.heb.pm.entity.ProductScanImageBanner;
import com.heb.pm.entity.ProductScanImageURI;
import com.heb.pm.entity.SalesChannel;
import com.heb.pm.ws.CheckedSoapException;
import com.heb.sadc.utils.AntiVirusException;
import com.heb.util.controller.ModifiedEntity;
import com.heb.util.controller.NonEmptyParameterValidator;
import com.heb.util.controller.UserInfo;
import com.heb.util.jpa.LazyObjectResolver;
import com.heb.util.jpa.PageableResult;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * REST endpoint for Image Information.
 *
 * @author s753601
 * @since 2.13.0
 */
@RestController()
@RequestMapping(ApiConstants.BASE_APPLICATION_URL + ImageInfoController.IMAGE_INFO_URL)
@AuthorizedResource(ResourceConstants.IMAGE_INFO)
public class ImageInfoController {

	private static final Logger logger = LoggerFactory.getLogger(ImageInfoController.class);

	protected static final String IMAGE_INFO_URL = "/imageInfo";
	/**
	 * The constant GET_GLADSON_DATA.
	 */
	protected static final String GET_IMAGE_INFO = "/getImageInfo";
	protected static final String GET_IMAGES = "/getImages";
	protected static final String GET_IMAGE_CATEGORIES="/getImageCategories";
	protected static final String GET_IMAGE_STATUSES="/getImageStatuses";
	protected static final String GET_IMAGE_DESTINATION="/getImageDestinations";
	protected static final String GET_IMAGE_PRIORITIES="/getImagePriorities";
	protected static final String GET_IMAGE_SOURCES="/getImageSources";
	protected static final String UPLOAD_IMAGE="/uploadImage";
	protected static final String UPDATE_IMAGE_INFO="/updateImageInfo";
	protected static final String COPY_TO_HIERARCHY_PATH="/copyToHierarchyPath";
	protected static final String CHECK_IMAGE_VALID="/checkImageValid";
	protected static final String COPY_TO_HIERARCHY="/copyToHierarchy";
	protected static final String GET_IMAGE_PRIMARY="/imagePrimary";
	protected static final String GET_ACTIVE_ONLINE_IMAGES = "/getActiveOnlineImages";
	protected static final String GET_SUBMITTED_IMAGES = "/getSubmittedImages";
	protected static final String GET_REJECTED_IMAGES = "/getRejectedImages";
	protected static final String GET_COUNT_SUBMITTED_IMAGES = "/getCountSubmittedImages";

	// Log Messages
	private static final String GET_IMAGE_INFO_REQUEST =
			"User %s from IP %s has requested image information";
	private static final String GET_IMAGE_PRIORITIES_REQUEST =
			"User %s from IP %s has requested image priorities";
	private static final String GET_IMAGE_DESTINATION_REQUEST =
			"User %s from IP %s has requested image destinations";
	private static final String GET_IMAGE_STATUSES_REQUEST =
			"User %s from IP %s has requested image statuses";
	private static final String GET_IMAGE_CATEGORIES_REQUEST =
			"User %s from IP %s has requested image categories";
	private static final String GET_IMAGES_REQUEST =
			"User %s from IP %s has requested images";
	private static final String GET_IMAGE_SOURCES_REQUEST =
			"User %s from IP %s has requested image sources";
	private static final String UPLOAD_IMAGE_REQUEST =
			"User %s from IP %s has requested to upload image %s";
	private static final String VIRUS_DETECTED_LOG=
			"User %s from IP %s has attempted to upload an image with a virus, request rejected";
	private static final String UPDATE_IMAGE_INFO_REQUEST =
			"User %s from IP %s has requested to update image info";
	private static final String IMAGE_HIERARCHY_PATH_REQUEST =
			"User %s from IP %s has requested the default path for the images %s product";
	private static final String GET_IMAGE_PRIMARY_REQUEST =
			"User %s from IP %s has requested to get primary image for product %d and sales channel %s";
	private static final String IMAGE_HIERARCHY_COPY_REQUEST =
			"User %s from IP %s has requested to copy these clip ids %s to a customer hierarchy";

	private static final String NO_IMG_KEY = "DruInfoController.missingUPC/SequenceNumber";
	private static final String NO_IMG_UPC = "UPC or Sequence Number cannot be found.";
	private static final String DEFAULT_UPDATE_SUCCESS_MESSAGE = "Successfully Updated.";
	private static final String UPDATE_SUCCESS_MESSAGE_KEY =
			"ImageInfoController.updateSuccessful";
	private static final String VIRUS_DETECTED_MESSAGE=
			"Image requested because it contained a virus";
	private static final String FAILED_WEBSERVICE_MESSAGE=
			"Webservice Failed to upload Image";
	private static final String CHECK_IMAGE_VALID_MESSAGE = "User %s from IP %s has requested to check the image is valid or not with upc: %d.";

	private LazyObjectResolver<ProductScanImageURI> resolver = new ImageInfoResolver();

	@Autowired
	private UserInfo userInfo;
	@Autowired private ImageInfoService imageInfoService;
	@Autowired
	private MessageSource messageSource;
	@Autowired
	private NonEmptyParameterValidator parameterValidator;

	private class ImageInfoResolver implements LazyObjectResolver<ProductScanImageURI> {

		/**
		 * Currently this fetch is getting:
		 * 1. the key
		 * 2. the Image type
		 * 3. the Image Category
		 * 4. the Image Status
		 * 5. the Image Priority
		 * 6. the product scan image banner list
		 * 7. the source system
		 * 8. the image source
		 * 9. the image metadata
		 * 10. the image destinations
		 * @param productScanImageURI
		 */

		@Override
		public void fetch(ProductScanImageURI productScanImageURI) {
			if(productScanImageURI.getKey() != null){
				productScanImageURI.getKey().getSequenceNumber();
			}
			if(productScanImageURI.getImageCategory() != null){
				productScanImageURI.getImageCategory().getId();
			}
			if(productScanImageURI.getImageType() != null){
				productScanImageURI.getImageType().getId();
			}
			if(productScanImageURI.getImageStatus() !=null){
				productScanImageURI.getImageStatus().getAbbreviation();
			}
			if(productScanImageURI.getImagePriority() !=null){
				productScanImageURI.getImagePriority().getAbbreviation();
			}
			if(productScanImageURI.getProductScanImageBannerList() !=null){
				for (ProductScanImageBanner banner: productScanImageURI.getProductScanImageBannerList()) {
					banner.getKey().getId();
					if(banner.getSalesChannel() != null){
						banner.getSalesChannel().getAbbreviation();
					}
				}
			}
			if(productScanImageURI.getSourceSystem() !=null){
				productScanImageURI.getSourceSystem().getId();
			}
			if(productScanImageURI.getImageSource() != null){
				productScanImageURI.getImageSource().getAbbreviation();
			}
			if(productScanImageURI.getImageMetaDataList()!=null){
				for (ImageMetaData data: productScanImageURI.getImageMetaDataList()) {
					data.getKey().getId();
					data.getEntity().getAbbreviation();
				}
			}
			if(productScanImageURI.getDestinations().size() >0){
				for (SalesChannel destination: productScanImageURI.getDestinations()) {
					destination.getAbbreviation();
				}
			}
		}
	}

	/**
	 *
	 * Request all images associated with a products upc
	 * @param request the request
	 * @return the list
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.POST, value = ImageInfoController.GET_IMAGE_INFO)
	public ModifiedEntity<List<ProductScanImageURI>> getImageInfo(@RequestBody List<Long> upcs, HttpServletRequest request){
		this.logGetImageInformation(request.getRemoteAddr());
		ModifiedEntity<List<ProductScanImageURI>> results=this.imageInfoService.getImageInformation(upcs, null);
		results.getData().forEach(this.resolver::fetch);
		return  results;
	}

	/**
	 * Request to get all of the byte[] representation of the images
	 * @param request the request
	 * @return the list
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.POST, value = ImageInfoController.GET_IMAGES)
	public ModifiedEntity<byte[]> getImages(@RequestBody String uri, HttpServletRequest request){
		this.logGetImages(request.getRemoteAddr());
		byte[] image =this.imageInfoService.getImages(uri);
		return new ModifiedEntity<>(image,"Successful");
	}

	/**
	 * Request for all the values in the ImageCategories code table
	 * @param request
	 * @return all of the records int the ImageCategories code table
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = ImageInfoController.GET_IMAGE_CATEGORIES)
	public List<ImageCategory> getImageCategories(HttpServletRequest request){
		this.logGetImageCategories(request.getRemoteAddr());
		List<ImageCategory> categories = this.imageInfoService.getCategories();
		return categories;
	}

	/**
	 * Request for all the values in the ImageStatuses code table
	 * @param request
	 * @return all of the records int the ImageStatuses code table
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = ImageInfoController.GET_IMAGE_STATUSES)
	public List<ImageStatus> getImageStatuses(HttpServletRequest request){
		this.logGetImageStatuses(request.getRemoteAddr());
		List<ImageStatus> statuses = this.imageInfoService.getStatuses();
		return statuses;
	}

	/**
	 * Request for all the values in the ImageDestinations code table
	 * @param request
	 * @return all of the records int the ImageDestinations code table
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = ImageInfoController.GET_IMAGE_DESTINATION)
	public List<SalesChannel> getImageDestinations(HttpServletRequest request){
		this.logGetImageDestinations(request.getRemoteAddr());
		List<SalesChannel> destinations = this.imageInfoService.getDestinations();
		return destinations;
	}

	/**
	 * Update image info.
	 *
	 * @param upcs                 the list of upc.
	 * @param productScanImageURIS the list of product scan image uri.
	 * @param request              the HttpServletRequest.
	 */
	@EditPermission
	@RequestMapping(method = RequestMethod.POST, value = ImageInfoController.UPDATE_IMAGE_INFO)
	public ModifiedEntity<String> updateImageInfo(@RequestParam List<Long> upcs,
												  @RequestBody List<ProductScanImageURI> productScanImageURIS, HttpServletRequest request){
		this.logUpdateImageInfo(request.getRemoteAddr());
		this.parameterValidator.validate(productScanImageURIS, ImageInfoController.NO_IMG_UPC,
				ImageInfoController.NO_IMG_KEY, request.getLocale());
		String updateMessage = DEFAULT_UPDATE_SUCCESS_MESSAGE;
		this.imageInfoService.updateImageInfo(upcs, productScanImageURIS, this.userInfo.getUserId());
		this.logUpdateImageInfo(request.getRemoteAddr());
		return new ModifiedEntity<>(null, updateMessage);
	}

	/**
	 * Request all submitted images with a products upc
	 *
	 * @param upcs           the list of upc.
	 * @param isShowRejected the show rejected image or not.
	 * @param page           the page get data.
	 * @param pageSize       the page size.
	 * @param includeCounts  the has include count or not.
	 * @param request        the HttpServletRequest.
	 * @return ModifiedEntity<PageableResult   <   ProductScanImageURI>>.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = GET_SUBMITTED_IMAGES)
	public ModifiedEntity<PageableResult<ProductScanImageURI>> getSubmittedImages(@RequestParam(value = "upcs") List<Long> upcs,
																				  @RequestParam(value = "isShowRejected") boolean isShowRejected,
																				  @RequestParam(value = "page", required = false) Integer page,
																				  @RequestParam(value = "pageSize", required = false) Integer pageSize,
																				  @RequestParam(value = "includeCounts", required = false) Boolean includeCounts,
																				  HttpServletRequest request) {
		this.logGetImageInformation(request.getRemoteAddr());
		ModifiedEntity<PageableResult<ProductScanImageURI>> results = this.imageInfoService.getSubmittedImages(upcs, isShowRejected,
				page, pageSize, includeCounts);
		results.getData().getData().forEach(this.resolver::fetch);
		return results;
	}

	/**
	 * Request all rejected images with a products upc
	 *
	 * @param upcs           the list of upc.
	 * @param page           the page get data.
	 * @param pageSize       the page size.
	 * @param includeCounts  the has include count or not.
	 * @param request        the HttpServletRequest.
	 * @return ModifiedEntity<PageableResult<ProductScanImageURI>>.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = GET_REJECTED_IMAGES)
	public ModifiedEntity<PageableResult<ProductScanImageURI>> getRejectedImages(@RequestParam(value = "upcs") List<Long> upcs,
																				  @RequestParam(value = "page", required = false) Integer page,
																				  @RequestParam(value = "pageSize", required = false) Integer pageSize,
																				  @RequestParam(value = "includeCounts", required = false) Boolean includeCounts,
																				  HttpServletRequest request) {
		this.logGetImageInformation(request.getRemoteAddr());
		ModifiedEntity<PageableResult<ProductScanImageURI>> results = this.imageInfoService.getRejectedImages(upcs, page, pageSize, includeCounts);
		results.getData().getData().forEach(this.resolver::fetch);
		return results;
	}

	/**
	 * Request all submitted images with a products upc.
	 * @param upcs the list of upc.
	 * @param request the HttpServletRequest.
	 * @return ModifiedEntity<List<ProductScanImageURI>>.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = GET_COUNT_SUBMITTED_IMAGES)
	public Map<String, Integer> getSubmittedImagesWithCounts(@RequestParam(value = "upcs") List<Long> upcs,
															 HttpServletRequest request) {
		this.logGetImageInformation(request.getRemoteAddr());
		Map<String, Integer> results = this.imageInfoService.getSubmittedImagesWithCounts(upcs);
		return results;
	}

	/**
	 * Request all active online images with a products upc
	 *
	 * @param upcs    the list of upc.
	 * @param request the HttpServletRequest.
	 * @return the ModifiedEntity<List<ProductScanImageURI>>
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = GET_ACTIVE_ONLINE_IMAGES)
	public ModifiedEntity<List<ProductScanImageURI>> getActiveOnlineImages(@RequestParam(value = "upcs") List<Long> upcs, HttpServletRequest request) {
		this.logGetImageInformation(request.getRemoteAddr());
		ModifiedEntity<List<ProductScanImageURI>> results = this.imageInfoService.getActiveOnlineImages(upcs);
		results.getData().forEach(this.resolver::fetch);
		return results;
	}

	/**
	 * Request for all the values in the ImagePriorities code table
	 * @param request
	 * @return all of the records int the ImagePriorities code table
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = ImageInfoController.GET_IMAGE_PRIORITIES)
	public List<ImagePriority> getImagePriorities(HttpServletRequest request){
		this.logGetImagePriorities(request.getRemoteAddr());
		List<ImagePriority> priorities = this.imageInfoService.getPriorities();
		return priorities;
	}

	/**
	 * Request for all the values in the ImageSource code table
	 * @param request
	 * @return all of the records int the ImageSource code table
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = ImageInfoController.GET_IMAGE_SOURCES)
	public List<ImageSource> getImageSources(HttpServletRequest request){
		this.logGetImageSources(request.getRemoteAddr());
		List<ImageSource> sources = this.imageInfoService.getSources();
		return sources;
	}

	/**
	 * Request to upload a new image
	 * @param uploadImage
	 * @param request
	 * @return a message stating if the upload was successful or not
	 */
	@EditPermission
	@RequestMapping(method = RequestMethod.POST, value = ImageInfoController.UPLOAD_IMAGE)
	public ModifiedEntity<String> upLoadNewImage(@RequestBody ImageToUpload uploadImage, HttpServletRequest request) throws Exception{
		this.logImageToUploadRequest(request.getRemoteAddr(), uploadImage);
		uploadImage.setUserId(this.userInfo.getUserId());
		try{
			String updateMessage = StringUtils.EMPTY;
			if(this.imageInfoService.uploadImage(uploadImage) != 0){
				updateMessage = this.messageSource.getMessage(
						ImageInfoController.UPDATE_SUCCESS_MESSAGE_KEY,
						new Object[]{uploadImage.getUpc()}, ImageInfoController.DEFAULT_UPDATE_SUCCESS_MESSAGE, request.getLocale());
			}
			return new ModifiedEntity<>(updateMessage, updateMessage);
		} catch (AntiVirusException e){
			ImageInfoController.logger.error(String.format(ImageInfoController.VIRUS_DETECTED_LOG, this.userInfo.getUserId(), request.getRemoteAddr()));
			throw new Exception(VIRUS_DETECTED_MESSAGE);
		} catch (CheckedSoapException e){
			ImageInfoController.logger.error(String.format(ImageInfoController.FAILED_WEBSERVICE_MESSAGE, this.userInfo.getUserId(), request.getRemoteAddr()));
			throw new Exception(e.getMessage());
		}
	}
	/**
	 * Request to check if the image meet specifications or not.
	 * @param upc
	 * @param request
	 * @return boolean.
	 */
	@ViewPermission
	@RequestMapping (method = RequestMethod.GET,value =ImageInfoController.CHECK_IMAGE_VALID)
	public ModifiedEntity<Boolean> checkImageValid(@RequestParam("upc") Long upc,HttpServletRequest request) {
		ImageInfoController.logger.info(String.format(ImageInfoController.CHECK_IMAGE_VALID_MESSAGE, this.userInfo.getUserId(), request.getRemoteAddr(), upc));
		return new ModifiedEntity<>(this.imageInfoService.isImageValid(upc), StringUtils.EMPTY);
	}


	/**
	 * This method will find the default path for a product to the root of the HEB.com customer hierarchy so the user will
	 * know what path is being used in the copy to hierarchy function.
	 * @param prodId the product id used to get the frist link
	 * @param request
	 * @return the default hierarchy path for HEB.com
	 * @throws Exception
	 */
	@EditPermission
	@RequestMapping(method= RequestMethod.GET, value = ImageInfoController.COPY_TO_HIERARCHY_PATH)
	public ModifiedEntity<List<GenericEntity>> copyToHierarchyPath(@RequestParam long prodId, HttpServletRequest request) throws Exception{
		ImageInfoController.logger.info(String.format(ImageInfoController.IMAGE_HIERARCHY_PATH_REQUEST, this.userInfo.getUserId(), request.getLocale(), prodId));
		List<GenericEntity> results =this.imageInfoService.findCustomHierarchyPaths(prodId);
		return new ModifiedEntity<>(results, ImageInfoController.DEFAULT_UPDATE_SUCCESS_MESSAGE);
	}

	/**
	 * This method will preform the copy to hierarchy function where it will take a list of image info and create links
	 * between them and certain level of the HEB.com hierarchy
	 * @param copyToHierarchyRequest this object holds all data needed to link the selected image info to the HEB.com customer hierarchy
	 * @param request
	 * @return the updated image info
	 * @throws CheckedSoapException from errors in writing to the database
	 */
	@EditPermission
	@RequestMapping(method= RequestMethod.POST, value = ImageInfoController.COPY_TO_HIERARCHY)
	public ModifiedEntity<List<ProductScanImageURI>> copyToHierarchy(@RequestBody CopyToHierarchyRequest copyToHierarchyRequest, HttpServletRequest request) throws CheckedSoapException{
		ImageInfoController.logger.info(String.format(ImageInfoController.IMAGE_HIERARCHY_PATH_REQUEST, this.userInfo.getUserId(), request.getLocale(), copyToHierarchyRequest.getUpcs().toString()));
		this.imageInfoService.copyToHierarchy(copyToHierarchyRequest, this.userInfo.getUserId());
		String updateMessage = ImageInfoController.DEFAULT_UPDATE_SUCCESS_MESSAGE;
		ModifiedEntity<List<ProductScanImageURI>> results =this.imageInfoService.getImageInformation(copyToHierarchyRequest.getUpcs(), updateMessage);
		results.getData().forEach(this.resolver::fetch);
		return  results;
	}

	@ViewPermission
	@RequestMapping(method= RequestMethod.GET, value = ImageInfoController.GET_IMAGE_PRIMARY)
	public ModifiedEntity<ProductScanImageURI> getPrimaryImageByProductId(
			@RequestParam(value = "productId", required = true) long productId,
			@RequestParam(value = "salesChannelCode", required = true) String salesChannelCode,
			@RequestParam(value = "width", required = false) Integer width,
			@RequestParam(value = "height", required = false) Integer height,
			HttpServletRequest request) throws CheckedSoapException{
		ImageInfoController.logger.info(String.format(ImageInfoController.GET_IMAGE_PRIMARY_REQUEST,
				this.userInfo.getUserId(), request.getLocale(), productId, salesChannelCode));
		ProductScanImageURI productScanImageURI = this.imageInfoService.getPrimaryImageByProductId(productId, salesChannelCode);
		ModifiedEntity<ProductScanImageURI> modifiedEntity = null;
		if (productScanImageURI != null) {
			if (width != null & height != null) {
				productScanImageURI.setImage(this.resize(productScanImageURI.getImage(), width, height));
			}

			modifiedEntity = new ModifiedEntity<>(productScanImageURI, String.valueOf(productId));
		}
		return modifiedEntity;
	}

	/**
	 * Resizes an image to the given width and height.
	 * @param imageInByte
	 * @param width
	 * @param height
	 * @return
	 * @throws IOException
	 */
	public byte[] resize(byte[] imageInByte, Integer width, Integer height) {
		if (imageInByte != null && imageInByte.length > 0 && width != null && height != null) {
			InputStream in = new ByteArrayInputStream(imageInByte);
			BufferedImage inputImage = null;
			try {
				inputImage = ImageIO.read(in);

				// creates output image
				BufferedImage outputImage = new BufferedImage(width, height, inputImage.getType());

				// scales the input image to the output image
				Graphics2D g2d = outputImage.createGraphics();
				g2d.drawImage(inputImage, 0, 0, width, height, null);
				g2d.dispose();

				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				ImageIO.write(outputImage, "png", baos);
				imageInByte = baos.toByteArray();
			} catch (IOException e) {
				logger.error("Unable to parse image");
			}
		}
		return imageInByte;
	}

	/**
	 * Log's a user's request to get the image information.
	 *
	 * @param ip The IP address th user is logged in from.
	 */
	private void logGetImageInformation(String ip){
		ImageInfoController.logger.info(String.format(ImageInfoController.GET_IMAGE_INFO_REQUEST, this.userInfo.getUserId(), ip));
	}

	/**
	 * Log's a user's request to get the image priorities.
	 *
	 * @param ip The IP address th user is logged in from.
	 */
	private void logGetImagePriorities(String ip){
		ImageInfoController.logger.info(String.format(ImageInfoController.GET_IMAGE_PRIORITIES_REQUEST, this.userInfo.getUserId(), ip));
	}

	/**
	 * Log's a user's request to get the image destinations.
	 *
	 * @param ip The IP address th user is logged in from.
	 */
	private void logGetImageDestinations(String ip){
		ImageInfoController.logger.info(String.format(ImageInfoController.GET_IMAGE_DESTINATION_REQUEST, this.userInfo.getUserId(), ip));
	}

	/**
	 * Log's a user's request to get image statuses.
	 *
	 * @param ip The IP address th user is logged in from.
	 */
	private void logGetImageStatuses(String ip){
		ImageInfoController.logger.info(String.format(ImageInfoController.GET_IMAGE_STATUSES_REQUEST, this.userInfo.getUserId(), ip));
	}

	/**
	 * Log's a user's request to get the image categories.
	 *
	 * @param ip The IP address th user is logged in from.
	 */
	private void logGetImageCategories(String ip){
		ImageInfoController.logger.info(String.format(ImageInfoController.GET_IMAGE_CATEGORIES_REQUEST, this.userInfo.getUserId(), ip));
	}

	/**
	 * Log's a user's request to get images.
	 *
	 * @param ip The IP address th user is logged in from.
	 */
	private void logGetImages(String ip){
		ImageInfoController.logger.info(String.format(ImageInfoController.GET_IMAGES_REQUEST, this.userInfo.getUserId(), ip));
	}

	/**
	 * Log's a user's request to get images.
	 *
	 * @param ip The IP address th user is logged in from.
	 */
	private void logGetImageSources(String ip){
		ImageInfoController.logger.info(String.format(ImageInfoController.GET_IMAGE_SOURCES_REQUEST, this.userInfo.getUserId(), ip));
	}

	/**
	 * Log's a user's request to get images.
	 *
	 * @param ip The IP address th user is logged in from.
	 */
	private void logImageToUploadRequest(String ip, ImageToUpload imageToUpload){
		ImageInfoController.logger.info(String.format(ImageInfoController.UPLOAD_IMAGE_REQUEST, this.userInfo.getUserId(), ip, imageToUpload.toString()));
	}

	/**
	 * Log's a user's request to get images.
	 *
	 * @param ip The IP address th user is logged in from.
	 */
	private void logUpdateImageInfo(String ip){
		ImageInfoController.logger.info(String.format(ImageInfoController.UPDATE_IMAGE_INFO_REQUEST, this.userInfo.getUserId(), ip));
	}

}

