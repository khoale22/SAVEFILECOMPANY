package com.heb.pm.productDetails.sellingUnit;

import com.heb.pm.entity.ProductScanCodeExtent;
import com.heb.pm.repository.ProductScanCodeExtentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Holds all business logic related to gladson measuring data.
 *
 * @author m594201
 * @since 2.7.0
 */
@Service
public class GladsonDataService {

	@Autowired
	private ProductScanCodeExtentRepository productScanCodeExtentRepository;


	/**
	 * Gets gladson data that is representative of the measurements tied to the pictures of the products.
	 *
	 * @param upc the scn_cd_id tied to the set of data.
	 * @return the gladson data that has the measurements represented by the image.
	 */
	public List<ProductScanCodeExtent> getGladsonRetailDimensionalData(Long upc) {

		ProductScanCodeExtent productScanCodeExtent = new ProductScanCodeExtent();

		List<ProductScanCodeExtent> productScanCodeExtentList = this.productScanCodeExtentRepository.findByKeyScanCodeIdAndKeyProdExtDataCodeIn(upc, productScanCodeExtent.getDimensionDataCodes());

		return productScanCodeExtentList;
	}
}
