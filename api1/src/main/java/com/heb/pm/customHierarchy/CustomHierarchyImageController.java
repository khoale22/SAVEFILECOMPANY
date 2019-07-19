package com.heb.pm.customHierarchy;

import com.heb.jaf.security.AuthorizedResource;
import com.heb.jaf.security.EditPermission;
import com.heb.jaf.security.ViewPermission;
import com.heb.pm.ApiConstants;
import com.heb.pm.ResourceConstants;
import com.heb.pm.entity.GenericEntityRelationship;
import com.heb.pm.entity.HierarchyContext;
import com.heb.pm.entity.ImageCategory;
import com.heb.pm.entity.ImageMetaData;
import com.heb.pm.entity.ImagePriority;
import com.heb.pm.entity.ImageSource;
import com.heb.pm.entity.ImageStatus;
import com.heb.pm.entity.ImageToUpload;
import com.heb.pm.productDetails.sellingUnit.ImageInfoService;
import com.heb.pm.ws.CheckedSoapException;
import com.heb.sadc.utils.AntiVirusException;
import com.heb.util.controller.ModifiedEntity;
import com.heb.util.controller.UserInfo;
import com.heb.util.jpa.LazyObjectResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * REST controller that returns all information related to generic entity relationship.
 *
 * @author 753601
 * @since 2.16.0
 */
@RestController()
@RequestMapping(ApiConstants.BASE_APPLICATION_URL + CustomHierarchyImageController.CUSTOM_HIERARCHY_IMAGE_INFO_URL)
@AuthorizedResource(ResourceConstants.CUSTOM_HIERARCHY_VIEW)
public class CustomHierarchyImageController {

	private static final Logger logger = LoggerFactory.getLogger(CustomHierarchyImageController.class);

	// url's used
	protected static final String CUSTOM_HIERARCHY_IMAGE_INFO_URL = "/customHierarchy/imageInformation";
	private static final String GET_CUSTOM_HIERARCHY_IMAGE_INFO = "getImageInfo";
	private static final String GET_IMAGES = "getImages";
	protected static final String GET_IMAGE_CATEGORIES="/getImageCategories";
	protected static final String GET_IMAGE_STATUSES="/getImageStatuses";
	protected static final String GET_IMAGE_PRIORITIES="/getImagePriorities";
	protected static final String GET_IMAGE_SOURCES="/getImageSources";


	// logs
	private static final String GET_CUSTOM_HIERARCHY_IMAGE_INFO_MESSAGE =
			"User %s from IP %s has requested Image Information for entity %d.";
	private static final String UPDATE_CUSTOM_HIERARCHY_IMAGE_INFO_MESSAGE =
			"User %s from IP %s has requested to update %d images.";
	private static final String GET_IMAGES_REQUEST =
			"User %s from IP %s has requested images";
	private static final String DEFAULT_UPDATE_SUCCESS_MESSAGE = "Save successful";
	private static final String UPDATE_SUCCESS_MESSAGE_KEY ="CustomHierarchyImageController.updateSuccessful";
	private static final String UPLOAD_IMAGE_REQUEST =
			"User %s from IP %s has requested to upload image %s";
	private static final String VIRUS_DETECTED_LOG=
			"User %s from IP %s has attempted to upload an image with a virus, request rejected";
	private static final String VIRUS_DETECTED_MESSAGE=	"Image requested because it contained a virus";
	private static final String FAILED_WEBSERVICE_MESSAGE= "Webservice Failed to upload Image";

	@Autowired
	private ImageInfoService imageInfoService;

	@Autowired
	private	UserInfo userInfo;

	@Autowired
	private MessageSource messageSource;

	ImageMetaDataResolver resolver = new ImageMetaDataResolver();

	private class ImageMetaDataResolver implements LazyObjectResolver<ImageMetaData> {

		/**
		 * Currently this fetch is getting:
		 * 1. the key
		 * 2. the Image Category
		 * 3. the Image Status
		 * 4. the Image Priority
		 * 5. the image source
		 * @param imageMetaData
		 */

		@Override
		public void fetch(ImageMetaData imageMetaData) {
			if(imageMetaData.getKey() != null){
				imageMetaData.getKey().getSequenceNumber();
			}
			if(imageMetaData.getImageCategory() != null){
				imageMetaData.getImageCategory().getId();
			}
			if(imageMetaData.getImageStatus() !=null){
				imageMetaData.getImageStatus().getAbbreviation();
			}
			if(imageMetaData.getImagePriority() !=null){
				imageMetaData.getImagePriority().getAbbreviation();
			}
			if(imageMetaData.getSourceSystem() !=null){
				imageMetaData.getSourceSystem().getId();
			}
		}
	}

	/**
	 * Get all custom hierarchy list.
	 *
	 * @param request the request
	 * @return the list
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = GET_CUSTOM_HIERARCHY_IMAGE_INFO)
	public List<ImageMetaData> findByHierarchyContext(
			@Param(value = "entityId") Long entityId, HttpServletRequest request){
		CustomHierarchyImageController.logger.info(String.format(CustomHierarchyImageController.GET_CUSTOM_HIERARCHY_IMAGE_INFO_MESSAGE,
				userInfo, request.getRemoteAddr(), entityId));
		List<ImageMetaData> hierarchyImages = this.imageInfoService.getImageInfoForCustomHierarchy(entityId);
		for (ImageMetaData image: hierarchyImages){
			this.resolver.fetch(image);
		}
		return hierarchyImages;
	}

	/**
	 * Request to get all of the byte[] representation of the images
	 * @param request the request
	 * @return the list
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.POST, value = CustomHierarchyImageController.GET_IMAGES)
	public ModifiedEntity<byte[]> getImages(@RequestBody String uri, HttpServletRequest request){
		CustomHierarchyImageController.logger.info(String.format(CustomHierarchyImageController.GET_IMAGES_REQUEST, this.userInfo.getUserId(), request.getRemoteAddr()));
		byte[] image =this.imageInfoService.getImages(uri);
		return new ModifiedEntity<>(image,"Successful");
	}

	/**
	 * This method will update the image data of a hierarchy level
	 * @param metaDataUpdates the changes to be made
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@EditPermission
	@RequestMapping(method = RequestMethod.POST, value = "updateImages")
	public ModifiedEntity<List<ImageMetaData>> updateImageMetaData(@RequestBody List<ImageMetaData> metaDataUpdates, HttpServletRequest request) throws Exception{
		CustomHierarchyImageController.logger.info(
				String.format(CustomHierarchyImageController.UPDATE_CUSTOM_HIERARCHY_IMAGE_INFO_MESSAGE,
						this.userInfo.getUserId(), request.getRemoteAddr(), metaDataUpdates.size()));
		List<ImageMetaData> updatedImageMetaData = this.imageInfoService.updateImageMetaData(metaDataUpdates, this.userInfo.getUserId());
		String updateMessage = this.messageSource.getMessage(
				CustomHierarchyImageController.UPDATE_SUCCESS_MESSAGE_KEY,
				new Object[]{metaDataUpdates.get(0).getKey().getId()}, CustomHierarchyImageController.DEFAULT_UPDATE_SUCCESS_MESSAGE, request.getLocale());
		for (ImageMetaData image: updatedImageMetaData){
			this.resolver.fetch(image);
		}
		return new ModifiedEntity<>(updatedImageMetaData, updateMessage);
	}

	/**
	 * This method will upload a new image to a custom hierarchy level
	 * @param uploadImage the image to be uploaded
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@EditPermission
	@RequestMapping(method = RequestMethod.POST, value = "uploadImage")
	public ModifiedEntity<String> upLoadNewImage(@RequestBody ImageToUpload uploadImage, HttpServletRequest request) throws Exception{
		uploadImage.setUserId(this.userInfo.getUserId());
		CustomHierarchyImageController.logger.info(
				String.format(CustomHierarchyImageController.UPLOAD_IMAGE_REQUEST, this.userInfo.getUserId(), request.getRemoteAddr(), uploadImage.toString()));
		try{
			this.imageInfoService.uploadImageFromHierarchy(uploadImage);
			String updateMessage = this.messageSource.getMessage(
					CustomHierarchyImageController.UPDATE_SUCCESS_MESSAGE_KEY,
					new Object[]{uploadImage.getUpc()}, CustomHierarchyImageController.DEFAULT_UPDATE_SUCCESS_MESSAGE, request.getLocale());
			return new ModifiedEntity<>(updateMessage, updateMessage);
		} catch (AntiVirusException e){
			CustomHierarchyImageController.logger.error(String.format(CustomHierarchyImageController.VIRUS_DETECTED_LOG, this.userInfo.getUserId(), request.getRemoteAddr()));
			throw new Exception(VIRUS_DETECTED_MESSAGE);
		} catch (CheckedSoapException e){
			CustomHierarchyImageController.logger.error(String.format(CustomHierarchyImageController.FAILED_WEBSERVICE_MESSAGE, this.userInfo.getUserId(), request.getRemoteAddr()));
			throw new Exception(e.getMessage());
		}

	}

	/**
	 * Request for all the values in the ImageCategories code table
	 * @param request
	 * @return all of the records int the ImageCategories code table
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = CustomHierarchyImageController.GET_IMAGE_CATEGORIES)
	public List<ImageCategory> getImageCategories(HttpServletRequest request){
		List<ImageCategory> categories = this.imageInfoService.getCategories();
		return categories;
	}

	/**
	 * Request for all the values in the ImageStatuses code table
	 * @param request
	 * @return all of the records int the ImageStatuses code table
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = CustomHierarchyImageController.GET_IMAGE_STATUSES)
	public List<ImageStatus> getImageStatuses(HttpServletRequest request){
		List<ImageStatus> statuses = this.imageInfoService.getStatuses();
		return statuses;
	}

	/**
	 * Request for all the values in the ImagePriorities code table
	 * @param request
	 * @return all of the records int the ImagePriorities code table
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = CustomHierarchyImageController.GET_IMAGE_PRIORITIES)
	public List<ImagePriority> getImagePriorities(HttpServletRequest request){
		List<ImagePriority> priorities = this.imageInfoService.getPriorities();
		return priorities;
	}

	/**
	 * Request for all the values in the ImageSource code table
	 * @param request
	 * @return all of the records int the ImageSource code table
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = CustomHierarchyImageController.GET_IMAGE_SOURCES)
	public List<ImageSource> getImageSources(HttpServletRequest request){
		List<ImageSource> sources = this.imageInfoService.getSources();
		return sources;
	}
}
