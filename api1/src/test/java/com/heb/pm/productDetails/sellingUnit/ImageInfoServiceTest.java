package com.heb.pm.productDetails.sellingUnit;

import com.heb.pm.entity.ImageCategory;
import com.heb.pm.entity.ImagePriority;
import com.heb.pm.entity.ImageStatus;
import com.heb.pm.entity.ProductScanImageURI;
import com.heb.pm.entity.SalesChannel;
import com.heb.pm.repository.GenericEntityRelationshipRepository;
import com.heb.pm.repository.ImageCategoryRepository;
import com.heb.pm.repository.ImageInfoRepository;
import com.heb.pm.repository.ImagePriorityRepository;
import com.heb.pm.repository.ImageStatusRepository;
import com.heb.pm.repository.SalesChannelRepository;
import com.heb.pm.ws.CheckedSoapException;
import com.heb.pm.ws.ContentManagementServiceClient;
import com.heb.util.controller.ModifiedEntity;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

public class ImageInfoServiceTest {

	private static final long DEFAULT_LONG = 1L;
	private static final String[] DEFAULT_STRING_ARRAY = {"TEST"};
	private static final String DEFAULT_STRING = "TEST";
	private static final String FAILED_STRING_EXCEPTION="Test threw an exception";

	@InjectMocks
	private ImageInfoService service;

	@Mock
	ImageInfoRepository imageInfoRespository;

	@Mock
	ImageCategoryRepository imageCategoryRepository;

	@Mock
	ImageStatusRepository imageStatusRepository;

	@Mock
	SalesChannelRepository salesChannelRepository;

	@Mock
	ImagePriorityRepository imagePriorityRepository;

	@Mock
	GenericEntityRelationshipRepository genericEntityRelationshipRepository;

	@Mock
	ContentManagementServiceClient client;

	/**
	 * Initializes mockito.
	 */
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

	/**
	 * Tests the getImageInformation method
	 */
	@Test
	public void getImageInformationTest() {
		ArrayList<Long> test = new ArrayList<>();
		Mockito.when(this.imageInfoRespository.findByKeyId(Mockito.anyLong())).thenReturn(this.getDefaultImageInfo());
		ModifiedEntity<List<ProductScanImageURI>> result = this.service.getImageInformation(test, null);
		Assert.assertEquals(result, this.getDefaultImageInfo());
	}

	/**
	 * Tests the getImages method
	 */
	@Test
	public void getImagesTest() {
		try{
			Mockito.when(this.client.getImage(Mockito.anyString())).thenReturn(new byte[0]);
			byte[] result = this.service.getImages(DEFAULT_STRING);
			byte[] test = getDefaultByteArray().get(0);
			Assert.assertEquals(result.length, test.length);
		} catch (Exception e){
			Assert.fail(FAILED_STRING_EXCEPTION);
		}
	}

	/**
	 * Tests the getCategories method
	 */
	@Test
	public void getCategoriesTest() {
		Mockito.when(this.imageCategoryRepository.findAll()).thenReturn(this.getDefaultImageCategoryList());
		List<ImageCategory> result = this.service.getCategories();
		Assert.assertEquals(result, this.getDefaultImageCategoryList());
	}

	/**
	 * Tests the getStatuses method
	 */
	@Test
	public void getStatusesTest() {
		Mockito.when(this.imageStatusRepository.findAll()).thenReturn(this.getDefaultImageStatusList());
		List<ImageStatus> result = this.service.getStatuses();
		Assert.assertEquals(result, this.getDefaultImageStatusList());
	}

	/**
	 * Tests the getDestinations method
	 */
	@Test
	public void getDestinationsTest(){
		Mockito.when(this.salesChannelRepository.findAll()).thenReturn(this.getDefaultSalesChannelList());
		List<SalesChannel> result = this.service.getDestinations();
		Assert.assertEquals(result, this.getDefaultSalesChannelList());
	}

	/**
	 * Tests the getPriorities method
	 */
	@Test
	public void getPrioritiesTest() {
		Mockito.when(this.imagePriorityRepository.findAll()).thenReturn(this.getDefaultImagePriorityList());
		List<ImagePriority> result = this.service.getPriorities();
		Assert.assertEquals(result, this.getDefaultImagePriorityList());
	}

	/**
	 * Generates a default ProductScanImageURI list for testing
	 * @return
	 */
	private List<ProductScanImageURI> getDefaultImageInfo(){
		return new ArrayList<ProductScanImageURI>();
	}

	/**
	 * Generates a default byte[] list for testing
	 * @return
	 */
	private List<byte[]> getDefaultByteArray(){
		ArrayList<byte[]> list = new ArrayList<>();
		list.add(new byte[0]);
		return list;
	}

	/**
	 * Generates a default ImageCategory list for testing
	 * @return
	 */
	private List<ImageCategory> getDefaultImageCategoryList(){
		return new ArrayList<ImageCategory>();
	}

	/**
	 * Generates a default ImageStatus list for testing
	 * @return
	 */
	private List<ImageStatus> getDefaultImageStatusList(){
		return new ArrayList<ImageStatus>();
	}

	/**
	 * Generates a default SalesChannel list for testing
	 * @return
	 */
	private List<SalesChannel> getDefaultSalesChannelList(){
		return new ArrayList<SalesChannel>();
	}

	/**
	 * Generates a default ImagePriority list for testing
	 * @return
	 */
	private List<ImagePriority> getDefaultImagePriorityList(){
		return new ArrayList<ImagePriority>();
	}

}