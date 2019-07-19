package com.heb.pm.productDetails.sellingUnit;

import com.heb.pm.entity.ProductScanCodeExtent;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import testSupport.CommonMocks;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by m594201 on 6/19/2017.
 */
public class GladsonDataControllerTest {

	@InjectMocks
	private GladsonDataController gladsonDataController;

	@Mock
	private GladsonDataService service;

	private List<ProductScanCodeExtent> productScanCodeExtents;

	@Before
	public void setup(){
		MockitoAnnotations.initMocks(this);
		productScanCodeExtents = new ArrayList<>();
	}

	@Test
	public void getGladsonData() throws Exception {
		Mockito.when(this.service.getGladsonRetailDimensionalData(Mockito.anyLong())).thenReturn(productScanCodeExtents);

		this.gladsonDataController.getGladsonRetailDimensionalData(123L, CommonMocks.getServletRequest());

		Assert.assertNotNull("Results are null", productScanCodeExtents);

	}

}