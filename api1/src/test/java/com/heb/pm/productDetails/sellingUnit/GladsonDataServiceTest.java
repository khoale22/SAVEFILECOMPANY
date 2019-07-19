package com.heb.pm.productDetails.sellingUnit;

import com.heb.pm.repository.ProductScanCodeExtentRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 * Created by m594201 on 6/19/2017.
 */
public class GladsonDataServiceTest {

	@InjectMocks
	private GladsonDataService service;

	@Mock
	private ProductScanCodeExtentRepository repository;

	@Before
	public void setup(){
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void getGladsonData() throws Exception {

		this.service.getGladsonRetailDimensionalData(123L);
	}

}