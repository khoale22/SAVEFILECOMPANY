package com.heb.pm.productDetails.sellingUnit;

import com.heb.pm.entity.ImageCategory;
import com.heb.pm.entity.ImagePriority;
import com.heb.pm.entity.ImageStatus;
import com.heb.pm.entity.ProductScanImageURI;
import com.heb.pm.entity.SalesChannel;
import com.heb.pm.entity.SellingUnit;
import com.heb.util.controller.ModifiedEntity;
import com.heb.util.controller.UserInfo;
import com.sun.org.apache.xpath.internal.operations.Mod;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.context.MessageSource;
import testSupport.CommonMocks;

import java.util.ArrayList;
import java.util.List;

/**
 * Tests the image info Controller
 * @author s753601
 * @version 2.13.0
 */public class ImageInfoControllerTest {
	@InjectMocks
	private ImageInfoController imageInfoController;

	@Mock
	private ImageInfoService service;

	@Mock
	private UserInfo userInfo;

	@Mock
	private MessageSource messageSource;

	private static final long DEFAULT_LONG= 0L;
	private static final String DEFAULT_STRING="";

	/**
	 * Initializes mockito.
	 */
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

	/**
	 * Tests getImageInfoTest
	 */
	@Test
	public void getImageInfoTest() {
		ArrayList<Long> test = new ArrayList<>();
		Mockito.when(this.service.getImageInformation(Mockito.anyListOf(Long.class), null)).thenReturn(this.getDefaultImageInfo());
		ModifiedEntity<List<ProductScanImageURI>> result = this.imageInfoController.getImageInfo(new ArrayList<Long>(), CommonMocks.getServletRequest());
		Assert.assertEquals(result, this.getDefaultImageInfo());
	}

	/**
	 * Tests getImagesTest.
	 */
	@Test
	public void getImagesTest() {
		Mockito.when(this.service.getImages(Mockito.anyString())).thenReturn(new byte[0]);
		ModifiedEntity<byte[]> result = this.imageInfoController.getImages(DEFAULT_STRING, CommonMocks.getServletRequest());
		Assert.assertEquals(result, this.getDefaultByteArray());
	}

	/**
	 * Tests getImageCategoriesTest.
	 */
	@Test
	public void getImageCategoriesTest() {
		Mockito.when(this.service.getCategories()).thenReturn(this.getDefaultImageCategoryList());
		List<ImageCategory> result = this.imageInfoController.getImageCategories(CommonMocks.getServletRequest());
		Assert.assertEquals(result, this.getDefaultImageCategoryList());
	}

	/**
	 * Tests getImageStatusesTest.
	 */
	@Test	public void getImageStatusesTest() {
		Mockito.when(this.service.getStatuses()).thenReturn(this.getDefaultImageStatusList());
		List<ImageStatus> result = this.imageInfoController.getImageStatuses( CommonMocks.getServletRequest());
		Assert.assertEquals(result, this.getDefaultImageStatusList());
	}

	/**
	 * Tests getImageDestinationsTest.
	 */
	@Test
	public void getImageDestinationsTest() {
		Mockito.when(this.service.getDestinations()).thenReturn(this.getDefaultSalesChannelList());
		List<SalesChannel> result = this.imageInfoController.getImageDestinations(CommonMocks.getServletRequest());
		Assert.assertEquals(result, this.getDefaultSalesChannelList());
	}

	/**
	 * Tests getImagePrioritiesTest.
	 */
	@Test
	public void getImagePrioritiesTest() {
		Mockito.when(this.service.getPriorities()).thenReturn(this.getDefaultImagePriorityList());
		List<ImagePriority> result = this.imageInfoController.getImagePriorities(CommonMocks.getServletRequest());
		Assert.assertEquals(result, this.getDefaultImagePriorityList());
	}

	/**
	 * Generates a default ProductScanImageURI list for testing
	 * @return
	 */
	private ModifiedEntity<List<ProductScanImageURI>> getDefaultImageInfo(){
		return new ModifiedEntity<>(new ArrayList<ProductScanImageURI>(), "");
	}

	/**
	 * Generates a default byte[] list for testing
	 * @return
	 */
	private ModifiedEntity<byte[]> getDefaultByteArray(){
		return new ModifiedEntity<>(new byte[0], DEFAULT_STRING);
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